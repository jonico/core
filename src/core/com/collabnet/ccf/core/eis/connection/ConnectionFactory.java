package com.collabnet.ccf.core.eis.connection;

/**
 * @author madhusuthanan
 *
 * @param <T>
 */
public interface ConnectionFactory<T> {
	public T createConnection(String systemId,
			String systemKind, String repositoryId,
			String repositoryKind, String connectionInfo,
			String credentialInfo);
	public void closeConnection(T connection);
	public boolean isAlive(T connection);
}
