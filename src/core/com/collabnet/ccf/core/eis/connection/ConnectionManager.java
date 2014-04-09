/*
 * Copyright 2009 CollabNet, Inc. ("CollabNet") Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.collabnet.ccf.core.eis.connection;

import java.util.WeakHashMap;

import com.collabnet.ccf.core.db.RMDConfigExtractor;

/**
 * The connection manager is responsible to manage the connection pools for each
 * source/target system. It creates and manages one pool per repository. As the
 * connection manager does not know the type of the connection object for each
 * and every system that exists in the world, it depends on the
 * ConnectionFactory class to retrive a connection for a particular system. The
 * plugin providers should implement the ConnectionFactory
 * (com.collabnet.ccf.core.eis.connection.ConnectionFactory) by properly
 * parameterizing the connection object's class into the implemented class. In
 * the wiring configuration file they need to set the ConnectionFactory bean in
 * the ConnectionManager's connectionFactory property.
 * 
 * @author Madhusuthanan Seetharam (madhusuthanan@collab.net)
 * 
 * @param <T>
 *            - The class of the connection object to be managed As the
 *            connection object class will vary depending on the system that we
 *            connect, the connection manager needs to know the class of the
 *            connection object that it should manage.
 * 
 */
public class ConnectionManager<T> {

    /**
     * This object is used to pool connections in order not to open a new
     * connection for every single request
     */
    private ConnectionPool<T>      pool                             = null;

    /**
     * This property (true by default) determines whether after a timeout while
     * calling an operation on the connection, the operation should be tried
     * again
     */
    private boolean                enableRetryAfterNetworkTimeout   = true;

    /**
     * This configuration value (default is 120000 ms) specifies the maximum
     * amount of milliseconds, the retry mechanism will wait before retrying an
     * operation after network related timeouts. The value is specified in
     * milliseconds
     */
    private int                    maximumRetryWaitingTime          = 120000;

    /**
     * This value (default 5000 ms) specifies how much to increase the sleeping
     * interval for every try that is made to retry a timed out operation.
     */
    private int                    retryIncrementTime               = 5000;

    /**
     * This property (true by default) determines whether after detecting a
     * session timeout, it should be tried to reconnect using the credentials of
     * the last login
     */
    private boolean                enableReloginAfterSessionTimeout = true;

    /**
     * This property (false by default) determines whether the standard retry
     * code for network and login related exceptions should be used or whether a
     * component specific retry policy should be applied
     */
    private boolean                useStandardTimeoutHandlingCode   = false;

    private WeakHashMap<String, T> connectionLookupTable            = new WeakHashMap<String, T>();

    private RMDConfigExtractor         rmdConfigExtractor               = null;

    public ConnectionManager() {
        pool = new ConnectionPool<T>();
    }

    public ConnectionManager(ConnectionPool<T> pool) {
        this.pool = pool;
    }

    /**
     * Returns the ConnectionFactory object.
     * 
     * @return - an instance of the ConnectionFactory class implemented by the
     *         plugin developer.
     */
    public ConnectionFactory<T> getConnectionFactory() {
        return pool.getFactory();
    }

    /**
     * Returns a Connection object for a particular system and repository
     * combination from the pool. The connection is used to create new
     * artifacts. This is typically done using different credential information
     * than the ones used to update or extract artifacts. The credential
     * information for updates and extractions are the same to avoid infinite
     * update loops and differ from the credentials used to create artifacts in
     * order to force an initial resync after artifact creation. The connection
     * information typically contains the server url to which the connection to
     * be made. The credential information typically has the username and
     * password concatenated by a delimiter. The connection info and credential
     * info are used by the ConnectionFactory to create a connection.
     * 
     * @param systemId
     *            - The system id for the system to which the connection is to
     *            be made
     * @param systemKind
     *            - System kind for the system to which the connection to be
     *            made
     * @param repositoryId
     *            - The repository id for the repository to which the connection
     *            to be created
     * @param repositoryKind
     *            - The repository kind for the repository
     * @param connectionInfo
     *            - Connection information. Typically contains the server name
     *            or URL
     * @param credentialInfoToCreate
     *            - Credential information for the system and repository
     *            combination. Typically contains the username and password
     *            separated by a delimiter. These credential info should not be
     *            used to update or extract artifacts.
     * @return
     * @throws MaxConnectionsReachedException
     *             - when the maximum connections for the particular repository
     *             exceeded.
     * @throws ConnectionException
     */
    public T getConnectionToCreateArtifact(String systemId, String systemKind,
            String repositoryId, String repositoryKind, String connectionInfo,
            String credentialInfoToCreate)
            throws MaxConnectionsReachedException, ConnectionException {
        return pool.getConnection(systemId, systemKind, repositoryId,
                repositoryKind, connectionInfo, credentialInfoToCreate, this);
    }

    /**
     * Returns a Connection object for a particular system and repository
     * combination from the pool. The connection is used to update or extract
     * artifacts. This is typically done using different credential information
     * than the ones used to create artifacts. The credential information for
     * updates and extractions are the same to avoid infinite update loops and
     * differ from the credentials used to create artifacts in order to force an
     * initial resync after artifact creation. The connection information
     * typically contains the server url to which the connection to be made. The
     * credential information typically has the username and password
     * concatenated by a delimiter. The connection info and credential info are
     * used by the ConnectionFactory to create a connection.
     * 
     * @param systemId
     *            - The system id for the system to which the connection is to
     *            be made
     * @param systemKind
     *            - System kind for the system to which the connection to be
     *            made
     * @param repositoryId
     *            - The repository id for the repository to which the connection
     *            to be created
     * @param repositoryKind
     *            - The repository kind for the repository
     * @param connectionInfo
     *            - Connection information. Typically contains the server name
     *            or URL
     * @param credentialInfoToUpdateOrExtract
     *            - Credential information for the system and repository
     *            combination. Typically contains the username and password
     *            separated by a delimiter. These credentials should not be used
     *            to create new artifacts.
     * @return
     * @throws MaxConnectionsReachedException
     *             - when the maximum connections for the particular repository
     *             exceeded.
     * @throws ConnectionException
     */
    public T getConnectionToUpdateOrExtractArtifact(String systemId,
            String systemKind, String repositoryId, String repositoryKind,
            String connectionInfo, String credentialInfoToUpdateAndExtract)
            throws MaxConnectionsReachedException, ConnectionException {
        return pool.getConnection(systemId, systemKind, repositoryId,
                repositoryKind, connectionInfo,
                credentialInfoToUpdateAndExtract, this);
    }

    /**
     * Returns the maximum connection configured per pool.
     * 
     * @return - Maximum connections limit per pool
     */
    public int getMaxConnectionsPerPool() {
        return pool.getMaxConnectionsPerPool();
    }

    /**
     * Returns the maximum idle time for a connection object in milliseconds.
     * 
     * @return - The maximum idle time for the connections in the pool.
     */
    public long getMaxIdleTimeForConnection() {
        return pool.getMaxIdleTime();
    }

    /**
     * Gets the value (default is 120000 ms) that specifies the maximum amount
     * of milliseconds, the retry mechanism will wait before retrying an
     * operation after network related timeouts. The value is specified in
     * milliseconds
     * 
     * @return the maximumRetryWaitingTime
     */
    public int getMaximumRetryWaitingTime() {
        return maximumRetryWaitingTime;
    }

    /**
     * Get the value (default 5000 ms) to specify how much to increase the
     * sleeping interval for every try that is made to retry a timed out
     * operation.
     * 
     * @return the retryIncrementTime
     */
    public int getRetryIncrementTime() {
        return retryIncrementTime;
    }

    public RMDConfigExtractor getRmdConfigExtractor() {
        return rmdConfigExtractor;
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
     * Returns whether to relogin and retry operations after a session timeout
     * or not Default is true
     * 
     * @return the enableReloginAfterSessionTimeout
     */
    public boolean isEnableReloginAfterSessionTimeout() {
        return enableReloginAfterSessionTimeout;
    }

    /**
     * Returns whether to retry operations after a network timeout or not
     * Default is true
     * 
     * @return the enableRetryAfterNetworkTimeout
     */
    public boolean isEnableRetryAfterNetworkTimeout() {
        return enableRetryAfterNetworkTimeout;
    }

    /**
     * Sets whether to use the standard code for reconnects and relogins after
     * network related timeouts and invalid session objects or whether to use a
     * custom implementation Default is false
     * 
     * @return the useStandardTimeoutHandlingCode
     */
    public boolean isUseStandardTimeoutHandlingCode() {
        return useStandardTimeoutHandlingCode;
    }

    /**
     * Returns the connection associated with the key if the connection is still
     * used
     * 
     * @param key
     *            key under which the connection was registered
     * @return connection associated with the key or null if no connection was
     *         found
     */
    public T lookupRegisteredConnection(String key) {
        return connectionLookupTable.get(key);
    }

    /**
     * This method allows to store a connection under a certain key so that it
     * can be retrieved later The connection is stored in a weak hash map so
     * that the entry is automaticaly removed if no component uses the
     * connection any more
     * 
     * @param key
     * @param connection
     */
    public void registerConnection(String key, T connection) {
        connectionLookupTable.put(key, connection);
    }

    /**
     * Releases the connection to the pool. When the Readers/Writers are done
     * with the connection object, they should release the connection to the
     * pool. If the connection is not released, further calls to the
     * getConnection will result in the creation of new Connection objects and
     * accumulations of the connection objects will lead to the
     * MaxConnectionsReachedException on further calls to the getConnection()
     * method.
     * 
     * @param connection
     */
    public void releaseConnection(T connection) {
        pool.releaseConnection(connection);
    }

    /**
     * Sets the ConnectionFactory object that knows how to create a connection
     * to a particular repository. This object is an instance of the class that
     * is implemented by the plugin developer by implementing the
     * ConnectionFactory interface.
     * 
     * @param factory
     */
    public void setConnectionFactory(ConnectionFactory<T> factory) {
        pool.setFactory(factory);
    }

    /**
     * Sets whether to relogin and retry operations after a session timeout or
     * not
     * 
     * @param enableReloginAfterSessionTimeout
     *            the enableReloginAfterSessionTimeout to set
     */
    public void setEnableReloginAfterSessionTimeout(
            boolean enableReloginAfterSessionTimeout) {
        this.enableReloginAfterSessionTimeout = enableReloginAfterSessionTimeout;
    }

    /**
     * Set whether to retry operations after a network timeout or not
     * 
     * @param enableRetryAfterNetworkTimeout
     *            the enableRetryAfterNetworkTimeout to set
     */
    public void setEnableRetryAfterNetworkTimeout(
            boolean enableRetryAfterNetworkTimeout) {
        this.enableRetryAfterNetworkTimeout = enableRetryAfterNetworkTimeout;
    }

    /**
     * Sets the maximum number of connections allowed per pool.
     * 
     * As the connection manager maintains a connection pool per repository this
     * property applies to each pool that is managed by the ConnectionManager.
     * 
     * @param maxConnectionsPerPool
     *            - Maximum connections per pool
     */
    public void setMaxConnectionsPerPool(int maxConnectionsPerPool) {
        pool.setMaxConnectionsPerPool(maxConnectionsPerPool);
    }

    /**
     * Sets the maximum time that a connection can be idle in a pool. If there
     * are connections that stay unused for a longer time than the
     * maxIdleTimeForConnection they are closed and removed by the scavenger
     * thread.
     * 
     * @param maxIdleTimeForConnection
     *            - The maximum idle time for the connections in the pool.
     */
    public void setMaxIdleTimeForConnection(long maxIdleTimeForConnection) {
        pool.setMaxIdleTime(maxIdleTimeForConnection);
    }

    /**
     * Sets the value (default is 120000 ms) to specify the maximum amount of
     * milliseconds, the retry mechanism will wait before retrying an operation
     * after network related timeouts. The value is specified in milliseconds
     * 
     * @param maximumRetryWaitingTime
     *            the maximumRetryWaitingTime to set
     */
    public void setMaximumRetryWaitingTime(int maximumRetryWaitingTime) {
        this.maximumRetryWaitingTime = maximumRetryWaitingTime;
    }

    /**
     * Set the value (default 5000 ms) to specify how much to increase the
     * sleeping interval for every try that is made to retry a timed out
     * operation.
     * 
     * @param retryIncrementTime
     *            the retryIncrementTime to set
     */
    public void setRetryIncrementTime(int retryIncrementTime) {
        this.retryIncrementTime = retryIncrementTime;
    }

    public void setRmdConfigExtractor(RMDConfigExtractor rmdConfigExtractor) {
        this.rmdConfigExtractor = rmdConfigExtractor;
    }

    /**
     * Sets the interval between scavenger thread runs.
     * 
     * The scavenger thread is responsible to close connections that are not
     * used for the maxIdleTimeForConnection. This property sets the interval
     * between two successive scavenger thread runs.
     * 
     * @param scavengerInterval
     *            - Interval between successive scavenger thread runs.
     */
    public void setScavengerInterval(long scavengerInterval) {
        pool.setScavengerInterval(scavengerInterval);
    }

    /**
     * Sets whether to use the standard code for reconnects and relogins after
     * network related timeouts and invalid session objects or whether to use a
     * custom implementation
     * 
     * @param useStandardTimeoutHandlingCode
     *            the useStandardTimeoutHandlingCode to set
     */
    public void setUseStandardTimeoutHandlingCode(
            boolean useStandardTimeoutHandlingCode) {
        this.useStandardTimeoutHandlingCode = useStandardTimeoutHandlingCode;
    }

    public void tearDown() {
        pool.tearDown();
    }
}
