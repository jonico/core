This sample demonstrates the capability of CCF to export the 
artifacts in an SFEE tracker into a CSV file.

1. Configuring the SFEE tracker information
	Please refer the db script in db/SFEE2FLATFILE.script. Edit the source
	repository id to reflect your SFEE tracker id.
	
	Change the SFEE SOAP server URL and password in the config/sfee.properties
	file as shown below.
	sfee.server.1.url=<SFEE SOAP server URL>
	sfee.server.1.username=<connector user>
	sfee.server.1.password=<connector user's password>

2. Configuring the pollLimit
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
			
			Polling interval seconds is configured as n to indicate that the polling reader should
			read from the database read connector n number of times in order to ship all the
			SFEE tracker artifacts.
		</description>
		<property name="pollLimit" value="n" />
		<property name="delegate" ref="SynchronizationStatusPollingReaderJdbcConnection" />
		<property name="pollIntervalSecs" value="0" />
	</bean>

3. Please note that this sample as is does not support the following
	a) exporting multi-select field values.
		Mutiselect field values will not be exported properly if there are
		more than one field values are selected in the tracker.
	b) attachments will not be exported into the CSV file.
