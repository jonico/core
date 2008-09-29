package com.collabnet.ccf.core.eis.connection;


import java.util.WeakHashMap;


/**
 * The connection manager is responsible to manage the connection pools
 * for each source/target system.
 * It creates and manages one pool per repository.
 * As the connection manager does not know the type of the connection object
 * for each and every system that exists in the world, it depends on the 
 * ConnectionFactory class to retrive a connection for a particular system.
 * The plugin providers should implement the ConnectionFactory 
 * (com.collabnet.ccf.core.eis.connection.ConnectionFactory) by properly parameterizing the
 * connection object's class into the implemented class.
 * In the wiring configuration file they need to set the ConnectionFactory bean
 * in the ConnectionManager's connectionFactory property.
 * 
 *  A sample wiring file
 * snippet that configures a ConnectionManager to manage SFEE connections is shown below.
 * <code>
 * <bean id="SFEEConnectionManager"
 * 		class="com.collabnet.ccf.core.eis.connection.ConnectionManager">
 * 		<description>
 * 			The connection manager implements a connection pooling mechanism where the 
 * 			connections to multiple systems are cached for the readers and writers
 * 			to retrieve when needed. Before returning the connection to the client
 * 			the connection manager checks if the connection is live and only returns 
 * 			valid connections.
 * 			It assigns and manages one pool per repository.
 * 		</description>
 * 		<property name="maxConnectionsPerPool" value="5"></property>
 * 		<property name="maxIdleTimeForConnection" value="600000" />
 * 		<property name="scavengerInterval" value="120000"></property>
 * 		<property name="connectionFactory" ref="SFEEConnectionFactory"></property>
 * 	</bean>
 * 	<bean id="SFEEConnectionFactory"
 * 		class="com.collabnet.ccf.pi.sfee.v44.SFEEConnectionFactory">
 * 		<description>
 * 			This bean is an implementation of the com.collabnet.ccf.core.eis.connection.ConnectionFactory
 * 			interface. It is responsible to create and close an connection for a given
 * 			repository.
 * 
 * 			In this case SFEEConnectionFactory manages com.collabnet.ccf.pi.sfee.v44.Connection
 * 			objects.
 * 		</description>
 * 	</bean>
 * </code>
 * @author Madhusuthanan Seetharam (madhusuthanan@collab.net)
 *
 * @param <T> - The class of the connection object to be managed
 * 				As the connection object class will vary depending on the 
 * 				system that we connect, the connection manager needs to know the
 * 				class of the connection object that it should manage.
 * 
 */
public final class ConnectionManager<T> {
	private ConnectionPool<T> pool = new ConnectionPool<T>();
	
	private WeakHashMap<String,T> connectionLookupTable= new WeakHashMap<String,T>();

	/**
	 * Returns a Connection object for a particular system and repository
	 * combination from the pool. The connection information typically contains the server url
	 * to which the connection to be made.
	 * The credential information typically has the username and password concatenated
	 * by a delimiter.
	 * The connection info and credential info are used by the ConnectionFactory
	 * to create a connection.
	 * 
	 * @param systemId - The system id for the system to which the connection is to
	 * 					 be made 
	 * @param systemKind - System kind for the system to which the connection to be made
	 * @param repositoryId - The repository id for the repository to which the connection
	 * 						 to be created
	 * @param repositoryKind - The repository kind for the repository
	 * @param connectionInfo - Connection information. Typically contains the server
	 * 						   name or URL
	 * @param credentialInfo - Credential information for the system and repository combination.
	 * 						   Typically contains the username and password separated by a
	 * 						   delimiter.
	 * @return
	 * @throws MaxConnectionsReachedException - when the maximum connections for the
	 * 			particular repository exceeded.
	 * @throws ConnectionException 
	 */
	public T getConnection(String systemId,
			String systemKind, String repositoryId,
			String repositoryKind, String connectionInfo,
			String credentialInfo) throws MaxConnectionsReachedException, ConnectionException{
		return pool.getConnection(systemId, systemKind, repositoryId,
				repositoryKind, connectionInfo, credentialInfo,this);
	}
	/**
	 * Releases the connection to the pool. When the Readers/Writers are done
	 * with the connection object, they should release the connection to the pool.
	 * If the connection is not released, further calls to the getConnection
	 * will result in the creation of new Connection objects and accumulations of the
	 * connection objects will lead to the MaxConnectionsReachedException on
	 * further calls to the getConnection() method.
	 * @param connection
	 */
	public void releaseConnection(T connection){
		pool.releaseConnection(connection);
	}
	/**
	 * Sets the ConnectionFactory object that knows how to create a connection
	 * to a particular repository.
	 * This object is an instance of the class that is implemented by the plugin
	 * developer by implementing the ConnectionFactory interface.
	 * @param factory
	 */
	public void setConnectionFactory(ConnectionFactory<T> factory){
		pool.setFactory(factory);
	}
	/**
	 * Returns the ConnectionFactory object.
	 * @return - an instance of the ConnectionFactory class implemented by the
	 * 			plugin developer.
	 */
	public ConnectionFactory<T> getConnectionFactory(){
		return pool.getFactory();
	}
	/**
	 * Returns the maximum connection configured per pool.
	 * @return - Maximum connections limit per pool
	 */
	public int getMaxConnectionsPerPool() {
		return pool.getMaxConnectionsPerPool();
	}
	/**
	 * Sets the maximum number of connections allowed per pool.
	 * 
	 * As the connection manager maintains a connection pool per
	 * repository this property applies to each pool that is managed
	 * by the ConnectionManager.
	 * 
	 * @param maxConnectionsPerPool - Maximum connections per pool
	 */
	public void setMaxConnectionsPerPool(int maxConnectionsPerPool) {
		pool.setMaxConnectionsPerPool(maxConnectionsPerPool);
	}
	/**
	 * Returns the maximum idle time for a connection object in milliseconds.
	 * @return - The maximum idle time for the connections in the pool.
	 */
	public long getMaxIdleTimeForConnection() {
		return pool.getMaxIdleTime();
	}
	/**
	 * Sets the maximum time that a connection can be idle in a pool.
	 * If there are connections that stay unused for a longer time
	 * than the maxIdleTimeForConnection they are closed and removed by the scavenger
	 * thread.
	 * 
	 * @param maxIdleTimeForConnection - The maximum idle time for the connections in the pool.
	 */
	public void setMaxIdleTimeForConnection(long maxIdleTimeForConnection) {
		pool.setMaxIdleTime(maxIdleTimeForConnection);
	}
	/**
	 * Returns the interval in which the scavenger thread is run
	 * 
	 * @return - Interval between scavenger thread runs
	 */
	public long getScavengerInterval() {
		return pool.getScavengerInterval();
	}
	/**
	 * Sets the interval between scavenger thread runs.
	 * 
	 * The scavenger thread is responsible to close connections that are
	 * not used for the maxIdleTimeForConnection. This property sets the interval
	 * between two successive scavenger thread runs.
	 * 
	 * @param scavengerInterval - Interval between successive scavenger thread runs.
	 */
	public void setScavengerInterval(long scavengerInterval) {
		pool.setScavengerInterval(scavengerInterval);
	}
	
	/**
	 * This method allows to store a connection under a certain key so that it can be retrieved later
	 * The connection is stored in a weak hash map so that the entry is automaticaly removed if no component
	 * uses the connection any more
	 * @param key
	 * @param connection
	 */
	public void registerConnection(String key, T connection) {
			connectionLookupTable.put(key, connection);
	}
	
	/**
	 * Returns the connection associated with the key if the connection is still used
	 * @param key key under which the connection was registered
	 * @return connection associated with the key or null if no connection was found
	 */
	public T lookupRegisteredConnection(String key) {
		return connectionLookupTable.get(key);
	}
}
