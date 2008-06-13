This sample demonstrates the capability of CCF to import 
artifacts data from a database table into an SFEE tracker.

1. Configuring the SFEE tracker information
	The SFEE tracker id is configured in the database in the SYNCHRONIZATION_STATUS
	table. Please refer the target repository id configuration in the 
	db/databaseTableImport.script
	Please change this value to the tracker id that you intend to import the
	data
	
	Change the SFEE SOAP server URL and password in the config/sfee.properties
	file as shown below.
	sfee.server.1.url=<SFEE SOAP server URL>
	sfee.server.1.username=<connector user>
	sfee.server.1.password=<connector user's password>

2. Configuring the ARTIFACTS table
	The table that contains the artifacts are configured in the config\database-table-import.xml
	as
	<bean id="DatabaseTableReader"
		class="org.openadaptor.auxil.connector.jdbc.reader.JDBCReadConnector">
		<description>
			This is the JDBC read connector used by the SynchronizationStatusPollingReader to read the
			repository mapping and the synchronization info.
		</description>
		<property name="jdbcConnection" ref="JdbcConnection" />
		<property name="batchSize" value="1" />
		<property name="resultSetConverter" ref="ResultSetConverter" />
		<property name="sql">
			<value>SELECT * FROM ARTIFACTS</value>
		</property>
	</bean>
	
	This configuration snippet configures the table ARTIFACTS as the source of the
	artifacts data. If you intend to change the table name edit the sql property
	as SELECT * FROM <ARTIFACT_TABLE>

3. Mapping database fields to SFEE tracker fields
	This is done in the XSLT file xslt/record2GenericArtifact.xsl
	Change the field names in the template according to the 
	field names in the database table and the SFEE tracker.

4. Please note that this sample as is does not support the following
	a) importing multi-select field values.
		Mutiselect field values will not be imported properly if there are
		more than one field values are configured in the database table.
	b) attachments import.

5. Limitations
	a) The COMMENT_TEXT in the database table is configured as CHAR(256)
	So you may not be able to import larger comments.
