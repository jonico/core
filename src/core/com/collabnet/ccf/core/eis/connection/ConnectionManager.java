package com.collabnet.ccf.core.eis.connection;

/**
 * @author madhusuthanan
 *
 * @param <T>
 */
public final class ConnectionManager<T> {
	private ConnectionPool<T> pool = new ConnectionPool<T>();

	public long scavengerInterval = -1;
	public T getConnection(String systemId,
			String systemKind, String repositoryId,
			String repositoryKind, String connectionInfo,
			String credentialInfo) throws MaxConnectionsReachedException{
		return pool.getConnection(systemId, systemKind, repositoryId,
				repositoryKind, connectionInfo, credentialInfo);
	}
	public void releaseConnection(T connection){
		pool.releaseConnection(connection);
	}
	public void setConnectionFactory(ConnectionFactory<T> factory){
		pool.setFactory(factory);
	}
	public ConnectionFactory<T> getConnectionFactory(){
		return pool.getFactory();
	}
	public int getMaxConnectionsPerPool() {
		return pool.getMaxConnectionsPerPool();
	}
	public void setMaxConnectionsPerPool(int maxConnectionsPerPool) {
		pool.setMaxConnectionsPerPool(maxConnectionsPerPool);
	}
	public long getMaxIdleTimeForConnection() {
		return pool.getMaxIdleTime();
	}
	public void setMaxIdleTimeForConnection(long maxIdleTimeForConnection) {
		pool.setMaxIdleTime(maxIdleTimeForConnection);
	}
	public long getScavengerInterval() {
		return pool.getScavengerInterval();
	}
	public void setScavengerInterval(long scavengerInterval) {
		pool.setScavengerInterval(scavengerInterval);
	}
}
