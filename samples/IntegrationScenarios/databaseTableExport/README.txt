This sample demonstrates the capability of CCF to export 
artifacts from an SFEE tracker into a database table.

1. Configuring the SFEE tracker information
	The SFEE tracker id is configured in the database in the SYNCHRONIZATION_STATUS
	table. Please refer the source repository id configuration in the 
	db/databaseTableImport.script
	Please change this value to the tracker id that you intend to export the
	data from.
	
	Change the SFEE SOAP server URL and password in the config/sfee.properties
	file as shown below.
	sfee.server.1.url=<SFEE SOAP server URL>
	sfee.server.1.username=<connector user>
	sfee.server.1.password=<connector user's password>

2. Configuring the ARTIFACTS table
	The table that needs to be exported with the SFEE artifacts
	id configured in the config\database-table-export.xml
	as
	<bean id="DatabaseTableWriter"
		class="org.openadaptor.auxil.connector.jdbc.writer.MapTableWriter">
		<description>
			This is the writer object that writes the artifact data
			into the database table.
		</description>
		<property name="tableName" value="ARTIFACTS"></property>
		<property name="outputColumns">
			<list>
				<value>TITLE</value>
				<value>DESCRIPTION</value>
				<value>GROUP_VAL</value>
				<value>STATUS</value>
				<value>CATEGORY</value>
				<value>CUSTOMER</value>
				<value>PRIORITY</value>
				<value>ASSIGNED_TO</value>
				<value>REPORTED_IN_RELEASE</value>
				<value>ESTIMATED_HOURS</value>
			</list>
		</property>
	</bean>
	
	This configuration snippet configures the table ARTIFACTS as the target of the
	artifacts data. If you intend to change the table name edit the tableName property.
	You also need to change the column names if the table contains
	different column names.

3. Mapping database fields to SFEE tracker fields
	This is done in the XSLT file xslt/genericArtifact2Record.xsl
	Change the field names in the template according to the 
	field names in the database table and the SFEE tracker.

4. Configuring the pollLimit
	If there are n number of artifacts present in the SFEE tracker
	you need to configure the pollLimit property for the SynchronizationStatusPollingReader
	 as follows in the config/SFEE2FLATFILE.xml
	
		<bean id="SynchronizationStatusPollingReader"
		class="com.collabnet.ccf.core.test.plugins.SimpleLoopingPollingReadConnector">
		<description>
			This polling connector polls the CCF database tables that contain the repository
			mapping and the synchronization info and feeds the details to the source repository
			reader piped down the line. Please refer the processMap configuration for the Router
			bean.
			
			In this case this polling reader reads the synchronization status using the database
			read connector configured in the property delegate.
			
			Polling interval seconds is configured as 0 to indicate that the polling reader should
			read from the database read connector continuously without and pauses in between.
		</description>
		<property name="pollLimit" value="n" />
		<property name="delegate" ref="SynchronizationStatusPollingReaderJdbcConnection" />
		<property name="pollIntervalSecs" value="0" />
	</bean>

5. Please note that this sample as is does not support the following
	a) exporting multi-select field values.
		Mutiselect field values will not be imported properly if there are
		more than one field values are configured in the tracker field.
	b) attachments import.

6. Limitations
	a) The COMMENT_TEXT in the database table is configured as CHAR(256)
	So you may not be able to export larger comments into the table.
