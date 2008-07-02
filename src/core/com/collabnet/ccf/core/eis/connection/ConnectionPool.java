package com.collabnet.ccf.core.eis.connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class implements the Connection pooling mechanism for the
 * connection manager. It caches connection for each repository. It can be thought
 * of a pool of Connection pools.
 * Each system and repository combination is allowed to have maxConnectionsPerPool
 * number of connections.
 * 
 * When there is a request for a connection the ConnectionPool looks up the pool
 * for that system+repository for a free connection. If the pool contains a free
 * connection it returns the connection to the client. If there are no free connections
 * and if the maxConnectionsPerPool is not reached then the ConnectionPool
 * requests the ConnectionFactory to create a new connection and returns it to the
 * client. If the pool reached the maxConnectionsPerPool a MaxConnectionsPerPoolReachedException
 * is thrown.
 * 
 * @author madhusuthanan (madhusuthanan@collab.net)
 *
 * @param <T>
 */
public final class ConnectionPool<T> {
	private static final Log log = LogFactory.getLog(ConnectionPool.class);
	private ConnectionFactory<T> factory = null;
	private HashMap<String, ArrayList<ConnectionInfo>> connectionPool 
					= new HashMap<String, ArrayList<ConnectionInfo>>();
	private HashMap<T, ConnectionInfo> reversePoolMap = new HashMap<T, ConnectionInfo>();
	private static final String KEY_DELIMITER = ":";
	private long maxIdleTime = 2*60*1000;
	public long scavengerInterval = 2*60*1000;
	private int maxConnectionsPerPool = 5;
	
	public ConnectionPool(){
		new Scavenger().start();
	}
	/**
	 * Gets a free connection from the pool for the system, repository combination.
	 * The connectionFactory should be set before asking for a connection from the pool.
	 * 
	 * If is a free connection for this system and repository combination it is
	 * returned to the caller.
	 * If there are no free connections available and the maxConnectionsPerPool is not
	 * reached then the ConnectionFactory is asked to create a new connection for the system and 
	 * repository combination using the connectionInfo and the credentialInfo.
	 * 
	 * @param systemId - System id for the system to which the connection is needed
	 * @param systemKind - System lind for the system to which the connection is needed
	 * @param repositoryId - Repository id for the repository to which the connection is
	 * 						needed
	 * @param repositoryKind - Repository kind of the repository
	 * @param connectionInfo - Connection information for the system and repository.
	 * 							This typically contains the server name or URL
	 * @param credentialInfo - Credential information for the system and repository.
	 * 							Typically contains the username and passwword for the
	 * 							system and repository combination.
	 * 
	 * @return - Returns the free connection object or the connection object created by 
	 * 			the ConnectionFactory
	 *  
	 * @throws MaxConnectionsReachedException - If the configured maximum connections per
	 * 										pool is reached for this system and repository
	 * 										combination. 
	 * @throws ConnectionException 
	 */
	public T getConnection(String systemId,
			String systemKind, String repositoryId,
			String repositoryKind, String connectionInfo,
			String credentialInfo) throws MaxConnectionsReachedException, ConnectionException {
		if(factory == null){
			throw new IllegalArgumentException("Connection Factory is not set");
		}
		log.debug("Requesting a free connection");
		T connection = getFreeConnectionForKey(systemId, systemKind, repositoryId,
				repositoryKind, connectionInfo, credentialInfo);
		if(connection == null){
			log.debug("No free connection... Creating a connection");
			connection = createConnection(systemId, systemKind, repositoryId,
					repositoryKind, connectionInfo, credentialInfo);
		}
		return connection;
	}
	
	/**
	 * If the configured maximum number of connections per pool is not reached
	 * the ConnectionFactory is requested to create a new connection for the
	 * system and repository combination with the supplied connectionInfo and
	 * the credentialInfo.
	 * 
	 * @param systemId - System id for the system to which the connection is needed
	 * @param systemKind - System lind for the system to which the connection is needed
	 * @param repositoryId - Repository id for the repository to which the connection is
	 * 						needed
	 * @param repositoryKind - Repository kind of the repository
	 * @param connectionInfo - Connection information for the system and repository.
	 * 							This typically contains the server name or URL
	 * @param credentialInfo - Credential information for the system and repository.
	 * 							Typically contains the username and passwword for the
	 * 							system and repository combination.
	 * @return - Returns the free connection object or the connection object created by 
	 * 			the ConnectionFactory
	 * @throws MaxConnectionsReachedException - If the configured maximum connections per
	 * 										pool is reached for this system and repository
	 * 										combination. 
	 * @throws ConnectionException 
	 */
	private T createConnection(String systemId,
			String systemKind, String repositoryId,
			String repositoryKind, String connectionInfo,
			String credentialInfo) throws MaxConnectionsReachedException, ConnectionException {
		String key = generateKey(systemId,
				systemKind, repositoryId,
				repositoryKind, connectionInfo,
				credentialInfo);
		if(isPoolReachedMaxConnections(key)){
			throw new MaxConnectionsReachedException("Pool "+key
					+"Reached max connections "+maxConnectionsPerPool);
		}
		T createdConnection = factory.createConnection(systemId, systemKind, repositoryId,
				repositoryKind, connectionInfo, credentialInfo);
		ConnectionInfo info = new ConnectionInfo();
		info.setKey(key);
		info.setConnection(createdConnection);
		info.poppedFromPool();
		addToPool(key, info);
		return createdConnection;
	}
	
	/**
	 * Determines if the pool with the key reached the maximum number of connections.
	 * @param key - The key generated from the system and repository information.
	 * @return - true if the max number of connections reached for this pool
	 * 			false otherwise.
	 */
	private boolean isPoolReachedMaxConnections(String key){
		ArrayList<ConnectionInfo> connections = connectionPool.get(key);
		if(connections != null){
			synchronized(connections){
				int connectionsCount = connections.size();
				if(connectionsCount < maxConnectionsPerPool){
					return false;
				}
				else {
					log.info("Number of connections for pool "+key+" is "+connectionsCount+" and currently used up to the maximum.");
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Adds the newly created connection to the pool.
	 * @param key - The key to which this connection object to be bound.
	 * @param info - The ConnectionInfo object for this connection pool entry.
	 */
	private void addToPool(String key, ConnectionInfo info) {
		ArrayList<ConnectionInfo> connections = connectionPool.get(key);
		synchronized(this){
			if(connections == null){
				connections = new ArrayList<ConnectionInfo>();
				connectionPool.put(key, connections);
			}
		}
		synchronized(connections){
			connections.add(info);
			reversePoolMap.put(info.getConnection(), info);
		}
	}
	/**
	 * Searches the connection pool for the key (that is generated by the system and 
	 * repository information)
	 * The parameters are same as above for the getConnetion() and createConnection()
	 * methods.
	 * 
	 * @param systemId
	 * @param systemKind
	 * @param repositoryId
	 * @param repositoryKind
	 * @param connectionInfo
	 * @param credentialInfo
	 * @return
	 */
	private T  getFreeConnectionForKey(String systemId,
			String systemKind, String repositoryId,
			String repositoryKind, String connectionInfo,
			String credentialInfo) {
		String key = generateKey(systemId,
				systemKind, repositoryId,
				repositoryKind, connectionInfo,
				credentialInfo);
		T connection = null;
		ArrayList<ConnectionInfo> connectionInfos = connectionPool.get(key);
		if(connectionInfos != null){
			synchronized(connectionInfos){
				for(ConnectionInfo info: connectionInfos){
					boolean isConnectionFree = info.isFree();
					if(isConnectionFree){
						T retreivedConnection = info.getConnection();
						boolean isConnectionAlive = factory.isAlive(retreivedConnection);
						if(isConnectionAlive){
							info.poppedFromPool();
							connection = retreivedConnection;
							break;
						}
					}
				}
			}
		}
		return connection;
	}

	/**
	 * Marks the connection object in the pool as free there by releasing the 
	 * connection back to the pool.
	 * 
	 * @param connection - The connection object that should be released to the
	 * 						pool.
	 */
	public void releaseConnection(T connection){
		ConnectionInfo info = reversePoolMap.get(connection);
		String key = info.getKey();
		ArrayList<ConnectionInfo> connectionInfos = connectionPool.get(key);
		log.debug("Returning connection to pool...!");
		synchronized(connectionInfos){
			info.returnedToPool();
		}
	}
	/**
	 * Generates a unique key (which is nothing but a string generated by
	 * concatenating the strings of systemId, systemKind, repositoryId, repositoryKind,
	 * connectionInfo and credentialInfo) to identify the pool.
	 * 
	 * Parameters are same as that of the getConnection() or createConnection() methods.
	 * 
	 * @param systemId
	 * @param systemKind
	 * @param repositoryId
	 * @param repositoryKind
	 * @param connectionInfo
	 * @param credentialInfo
	 * @return
	 */
	private String generateKey(String systemId, String systemKind,
			String repositoryId, String repositoryKind, String connectionInfo,
			String credentialInfo) {
		return systemId + KEY_DELIMITER + systemKind + KEY_DELIMITER +
				repositoryId + KEY_DELIMITER + repositoryKind + KEY_DELIMITER +
				connectionInfo + KEY_DELIMITER + credentialInfo;
	}
	
	/**
	 * Returns the ConnectionFactory object that is used by the ConnectionPool
	 * to create and close connections for a particular system.
	 * 
	 * @return - The ConnectionFactory object
	 */
	public ConnectionFactory<T> getFactory() {
		return factory;
	}
	
	/**
	 * Sets the ConnectionFactory object that is used by the ConnectionPool to
	 * create and close the connections.
	 * 
	 * @param factory - The ConnectionFactory object
	 */
	public void setFactory(ConnectionFactory<T> factory) {
		this.factory = factory;
	}
	
	/**
	 * The scavenger thread which is responsible to close and remove the connection
	 * objects that are
	 * 	1. not used longer than maxIdleTime
	 *  2. not alive or got disconnected from the system.
	 *  
	 * @author madhusuthanan
	 *
	 */
	private class Scavenger extends Thread {
		public void run(){
			while(true){
				Set<String> keys = connectionPool.keySet();
				for(String key:keys){
					ArrayList<ConnectionInfo> connectionInfos = connectionPool.get(key);
					synchronized(connectionInfos){
						Iterator<ConnectionInfo> it = connectionInfos.iterator();
						while(it.hasNext()){
							ConnectionInfo info = it.next();
							if(info.isFreeFor(maxIdleTime) ||
									(!factory.isAlive(info.getConnection()))){
								it.remove();
								reversePoolMap.remove(info.getConnection());
							}
						}
					}
				}
				try {
					Thread.sleep(scavengerInterval);
				} catch (InterruptedException e) {
					e.printStackTrace();
					break;
				}
			}
		}
	}
	
	/**
	 * This class's objects represent a connection in the pool.
	 * It holds a reference to the Connection object for a particular
	 * system and repository.
	 * Keeps the time when this connection was used last.
	 * A boolean value to indicate whether the Connection object
	 * associated with this ConnectionInfo object is currently used
	 * or not.
	 * 
	 * @author madhusuthanan
	 *
	 */
	private class ConnectionInfo{
		private boolean isFree = true;
		private T connection = null;
		private long lastUsed = -1;
		private String key = null;
		
		/**
		 * Marks that the Connection object is currently used by a client
		 * and sets the lastUsed time to the current time.
		 */
		public synchronized void poppedFromPool(){
			isFree = false;
			lastUsed = System.currentTimeMillis();
		}
		
		/**
		 * Marks that the Connection object associated with this method is
		 * free and updates the lastUsed time of this Connection object to the
		 * current time.
		 */
		public synchronized void returnedToPool(){
			lastUsed = System.currentTimeMillis();
			isFree = true;
		}
		
		/**
		 * Signals whether the Connection object associated with this ConnectionInfo
		 * object is free or not.
		 * 
		 * @return - true if the Connection is free.
		 * 			false otherwise
		 */
		public synchronized boolean isFree(){
			return isFree;
		}
		
		/**
		 * Gives the Connection object associated with this ConnectionInfo
		 * object.
		 * 
		 * @return - The Connection object
		 */
		public synchronized T getConnection(){
			return connection;
		}
		
		/**
		 * Associates a Connection object with this ConnectionInfo.
		 * 
		 * @param connection - The Connection object to be associated
		 */
		public synchronized void setConnection(T connection){
			this.connection = connection;
		}
		public synchronized boolean isFreeFor(long timeInMillis){
			if(isFree){
				long now = System.currentTimeMillis();
				long freeTime = now - lastUsed;
				if(freeTime >= timeInMillis){
					return true;
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
		}
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
	}
	
	/**
	 * Returns the maximum idle time for a Connection object in a pool.
	 * 
	 * @return - The configured maximum idle time for a Connection object
	 */
	public long getMaxIdleTime() {
		return maxIdleTime;
	}
	
	/**
	 * Sets the maximum idle time for a connection in the pool.
	 * 
	 * @param maxIdleTime - The maximum time that a connection object
	 * 						can exist unused in a pool
	 */
	public void setMaxIdleTime(long maxIdleTime) {
		this.maxIdleTime = maxIdleTime;
	}
	
	/**
	 * Returns the interval between two successive scavenger thread runs.
	 * 
	 * @return - Interval between two successive scavenger thread runs
	 */
	public long getScavengerInterval() {
		return scavengerInterval;
	}
	
	/**
	 * Sets the interval between two successive scavenger thread runs
	 * @param scavengerInterval - Interval between two successive scavenger runs
	 */
	public void setScavengerInterval(long scavengerInterval) {
		this.scavengerInterval = scavengerInterval;
	}
	
	/**
	 * Returns the maximum number of connections configured per pool
	 * 
	 * @return - max connections configured per pool.
	 */
	public int getMaxConnectionsPerPool() {
		return maxConnectionsPerPool;
	}
	
	/**
	 * Sets the maximum number of connections per pool
	 * @param maxConnectionsPerPool - Maximum mnumber of connections per pool
	 */
	public void setMaxConnectionsPerPool(int maxConnectionsPerPool) {
		this.maxConnectionsPerPool = maxConnectionsPerPool;
	}
}
