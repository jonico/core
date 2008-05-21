package com.collabnet.ccf.core.eis.connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * @author madhusuthanan
 *
 * @param <T>
 */
public final class ConnectionPool<T> {
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
	public T getConnection(String systemId,
			String systemKind, String repositoryId,
			String repositoryKind, String connectionInfo,
			String credentialInfo) throws MaxConnectionsReachedException {
		if(factory == null){
			throw new IllegalArgumentException("Connection Factory is not set");
		}
		System.err.println("Requesting a free connection");
		T connection = getFreeConnectionForKey(systemId, systemKind, repositoryId,
				repositoryKind, connectionInfo, credentialInfo);
		if(connection == null){
			System.err.println("No free connection... Creating a connection");
			connection = createConnection(systemId, systemKind, repositoryId,
					repositoryKind, connectionInfo, credentialInfo);
		}
		return connection;
	}
	
	private T createConnection(String systemId,
			String systemKind, String repositoryId,
			String repositoryKind, String connectionInfo,
			String credentialInfo) throws MaxConnectionsReachedException {
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
	
	private boolean isPoolReachedMaxConnections(String key){
		ArrayList<ConnectionInfo> connections = connectionPool.get(key);
		if(connections != null){
			synchronized(connections){
				int connectionsCount = connections.size();
				System.err.println("Number of connections for pool "+key+" is "+connectionsCount);
				if(connectionsCount < maxConnectionsPerPool){
					return false;
				}
				else {
					return true;
				}
			}
		}
		return false;
	}
	
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
						System.err.println("Connection is free");
						T retreivedConnection = info.getConnection();
						boolean isConnectionAlive = factory.isAlive(retreivedConnection);
						if(isConnectionAlive){
							System.err.println("Connection is live, popping out");
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

	public void releaseConnection(T connection){
		ConnectionInfo info = reversePoolMap.get(connection);
		String key = info.getKey();
		ArrayList<ConnectionInfo> connectionInfos = connectionPool.get(key);
		System.err.println("Returning connection to pool...!");
		synchronized(connectionInfos){
			info.returnedToPool();
		}
	}
	private String generateKey(String systemId, String systemKind,
			String repositoryId, String repositoryKind, String connectionInfo,
			String credentialInfo) {
		return systemId + KEY_DELIMITER + systemKind + KEY_DELIMITER +
				repositoryId + KEY_DELIMITER + repositoryKind + KEY_DELIMITER +
				connectionInfo + KEY_DELIMITER + credentialInfo;
	}
	public ConnectionFactory<T> getFactory() {
		return factory;
	}
	public void setFactory(ConnectionFactory<T> factory) {
		this.factory = factory;
	}
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
	private class ConnectionInfo{
		private boolean isFree = true;
		private T connection = null;
		private long lastUsed = -1;
		private String key = null;
		public synchronized void poppedFromPool(){
			isFree = false;
			lastUsed = System.currentTimeMillis();
		}
		public synchronized void returnedToPool(){
			lastUsed = System.currentTimeMillis();
			isFree = true;
		}
		public synchronized boolean isFree(){
			return isFree;
		}
		public synchronized T getConnection(){
			return connection;
		}
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
	public long getMaxIdleTime() {
		return maxIdleTime;
	}
	public void setMaxIdleTime(long maxIdleTime) {
		this.maxIdleTime = maxIdleTime;
	}
	public long getScavengerInterval() {
		return scavengerInterval;
	}
	public void setScavengerInterval(long scavengerInterval) {
		this.scavengerInterval = scavengerInterval;
	}
	public int getMaxConnectionsPerPool() {
		return maxConnectionsPerPool;
	}
	public void setMaxConnectionsPerPool(int maxConnectionsPerPool) {
		this.maxConnectionsPerPool = maxConnectionsPerPool;
	}
}
