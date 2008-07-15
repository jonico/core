                    Collabnet Connector Framework - Samples
				    ---------------------------------------

About:

   The CCF Samples are provided to give the users a kick-start on using the CCF framework. You will
   have to follow the instructions given below to get CCF working on your environment.

Instructions:

   1. Get a working CCF db schema with data:
        - Setting up the CCF schema (This is to be done once for all possible wirings you might want):
	This is the one base table structure in which all CCF related connectors put their data. This
	base structure has to be created before you can specify the various systems required. You might
	want to configure multiple wirings, but it is recommended that you create one base schema where
	you add on info related to the various wirings.
	If you chose to install the hsql db service during installation, you can skip this step, as
	the service would take care of setting up the CCF schema in a hsql database for you.
	Otherwise, please refer to the CCFSchema.script file in this directory, and create these
	table structures for use by CCF on your database installation.

	- Getting working data into your database (This is to be done for each wiring you might want):
	Please refer to the CCFSampleDBData.script file, and insert similar values that reflect your
	environment into the database tables you created in the previous step.
	NOTE: If you want to use the hsql service and insert values, you could choose to do either
	of the following methods
	* Modify the insert statements in db\*.script to suit your environment, and append
	the contents to the CCF_INSTALL_DIR\CCFDBService\db\CCFDB.script before starting the CCFDB service.
	(or)
	* Start the CCFDBService, and use a third party tool like DBVisualizer (http://www.minq.se/products/dbvis/)
	to insert the appropriate values into your hsql db. To do this, you need to be aware of the following
	information:
	The connection string for the hsql DB would look like: jdbc:hsqldb:hsql://hostname:9001/dbname
	For dbvisualizer, you need to use CCF_INSTALL_DIR\CCFDBService\lib\hsqldb.jar as the jdbc driver

   2. Configure the CCF spring configuration files appropriately:
	Open the config\*.xml file, which will contain a sample wiring. Change the connection parameters
	and other info to reflect your environment

   3. Prepare the xslts:
        The xslts are present in the xslt folder. The xslts are simple rules for transformation of
	data from one system to another. Please modify the xslts appropriately to suit the field
	structure of your source and target systems

   4. Run the connector:
	You can choose to run the connector once, or install it as a service.
	To run it, just execute the RunConnector.bat in this directory
	To install this wiring as a service, simply execute the InstallWiringAsService.bat file. (You will
	be required to manually start the connector service)

   5. Logs:
	When the connector is running, you might want to refer to the logs, which will be present in the
	logs directory in the current directory


Happy Connecting!