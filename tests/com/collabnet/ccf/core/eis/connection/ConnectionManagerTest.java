package com.collabnet.ccf.core.eis.connection;


import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.core.Constraint;
import org.jmock.core.matcher.InvokeCountMatcher;

/**
 * @author madhusuthanan
 *
 */
public class ConnectionManagerTest extends MockObjectTestCase {
	
	public void setUp(){
		
	}
	@SuppressWarnings("unchecked")
	public void testGetConnection() throws MaxConnectionsReachedException {
		Mock context = new Mock(ConnectionFactory.class);
		String systemId = "sid";
		String systemKind = "skind";
		String repositoryId = "rid";
		String repositoryKind = "rkind";
		String connectionInfo = "cinfo";
		String credentialInfo = "crinfo";
		ConnectionManager<String> manager = new ConnectionManager<String>();
		manager.setMaxIdleTimeForConnection(20);
		manager.setScavengerInterval(60000);
		Object object = context.proxy();
		ConnectionFactory<String> connectionFactory = null;
		if(object instanceof ConnectionFactory){
			connectionFactory = (ConnectionFactory<String>) object;
		}
		manager.setConnectionFactory(connectionFactory);
		context.expects(once()).method("createConnection").with(new Constraint[]{eq(systemId),
				eq(systemKind),
				eq(repositoryId),
				eq(repositoryKind),
				eq(connectionInfo),
				eq(credentialInfo)}).will(returnValue("CreatedConnection"));
		String connection = manager.getConnection(systemId, systemKind, repositoryId,
				repositoryKind, connectionInfo, credentialInfo);
		manager.releaseConnection(connection);
		context.expects(once()).method("isAlive").with(eq("CreatedConnection")).will(returnValue(true));
		connection = manager.getConnection(systemId, systemKind, repositoryId,
				repositoryKind, connectionInfo, credentialInfo);
	}
	@SuppressWarnings("unchecked")
	public void testMultipleGetConnectionWithoutMaxConnectionsReached(){
		
		Mock context = new Mock(ConnectionFactory.class);
		String systemId = "sid";
		String systemKind = "skind";
		String repositoryId = "rid";
		String repositoryKind = "rkind";
		String connectionInfo = "cinfo";
		String credentialInfo = "crinfo";
		ConnectionManager<String> manager = new ConnectionManager<String>();
		int maxConnections = 25;
		manager.setMaxConnectionsPerPool(maxConnections);
		manager.setMaxIdleTimeForConnection(20);
		manager.setScavengerInterval(60000);
		manager.setConnectionFactory((ConnectionFactory<String>)context.proxy());
		int numberOfConnectionsNeeded = maxConnections;
		context.expects(new InvokeCountMatcher(numberOfConnectionsNeeded)).method("createConnection").with(new Constraint[]{eq(systemId),
				eq(systemKind),
				eq(repositoryId),
				eq(repositoryKind),
				eq(connectionInfo),
				eq(credentialInfo)}).will(returnValue("CreatedConnection"));
		context.expects(new InvokeCountMatcher(numberOfConnectionsNeeded*(numberOfConnectionsNeeded-1))).method("isAlive").with(eq("CreatedConnection")).will(returnValue(true));
		String[] connections = new String[numberOfConnectionsNeeded];
		for(int i=0; i < numberOfConnectionsNeeded; i++){
			try {
				connections[i] = manager.getConnection(systemId, systemKind, repositoryId,
						repositoryKind, connectionInfo, credentialInfo);
			} catch (MaxConnectionsReachedException e) {
				fail("Max connections reached at "+(i+1)
						+" although the max connections configured is "+maxConnections);
			}
		}
	}
	@SuppressWarnings("unchecked")
	public void testMultipleGetConnectionWithMaxConnectionsReached(){
		Mock context = new Mock(ConnectionFactory.class);
		String systemId = "sid";
		String systemKind = "skind";
		String repositoryId = "rid";
		String repositoryKind = "rkind";
		String connectionInfo = "cinfo";
		String credentialInfo = "crinfo";
		ConnectionManager<String> manager = new ConnectionManager<String>();
		int maxConnections = 25;
		manager.setMaxConnectionsPerPool(maxConnections);
		manager.setMaxIdleTimeForConnection(20);
		manager.setScavengerInterval(60000);
		manager.setConnectionFactory((ConnectionFactory<String>)context.proxy());
		int numberOfConnectionsNeeded = maxConnections+1;
		context.expects(new InvokeCountMatcher(numberOfConnectionsNeeded)).method("createConnection").with(new Constraint[]{eq(systemId),
				eq(systemKind),
				eq(repositoryId),
				eq(repositoryKind),
				eq(connectionInfo),
				eq(credentialInfo)}).will(returnValue("CreatedConnection"));
		context.expects(new InvokeCountMatcher(numberOfConnectionsNeeded*(numberOfConnectionsNeeded-1))).method("isAlive").with(eq("CreatedConnection")).will(returnValue(true));
		String[] connections = new String[numberOfConnectionsNeeded];
		boolean maxConnectionsExceptionOccured = false;
		for(int i=0; i < numberOfConnectionsNeeded; i++){
			try {
				connections[i] = manager.getConnection(systemId, systemKind, repositoryId,
						repositoryKind, connectionInfo, credentialInfo);
			} catch (MaxConnectionsReachedException e) {
				maxConnectionsExceptionOccured = true;
			}
		}
		assertTrue("Max connections not reached. The max connections configured is "
				+maxConnections, maxConnectionsExceptionOccured);
	}
	public void tearDown(){
		
	}
}
