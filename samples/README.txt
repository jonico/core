Collabnet Connector Framework - Samples
				    ---------------------------------------

About:

   The CCF Samples are provided to give the users a kick-start on using the CCF framework.
   
   The integration scenarios are summarized at http://ccf.open.collab.net/wiki/ExampleScenarios
   and explained in detail at http://ccf.open.collab.net/nonav/exampleScenarioDocs/index.html
   
   You will have to follow the instructions given below to get CCF working on your environment.

Instructions:

   1. Prepare the central CCF data base
   
   Both the synchronization status as well as the identity mapping of all artifacts synchronized by
   the CCF are kept in a central data base. The table names and columns refer to the definitions
   of the CCF general terms at http://ccf.open.collab.net/wiki/Naming_Conventions_and_Definitions
   and the generic artifact format specification at
   http://ccf.open.collab.net/servlets/ProjectProcess?pageID=aT1rGG&subpageID=nLKvgT
   
   Currently, the central CCF data base is based on HyberSonic SQL.
   The schema of this data base and some sample content for the integration scenarios can be found
   in the centralCCFDatabase directory in the ccfDatabase.script file.
   
   ***** WRITE HOW TO RUN THE DATA BASE UNDER WINDOWS/LINUX ****
   
   In order to run the integration scenarios on different machines but the preconfigured ones, you have
   to adjust the repository and system ids according to your needs. You may chose two options to adjust
   the contents of the central data base: 
	
	* Modify the insert statements in ccfDatabase.script to suit your environment by hand (please
	make sure to stop the HyberSonic SQL data base before).
	
	* Start the CCFDBService, and use a third party tool like DBVisualizer (http://www.minq.se/products/dbvis/)
	to insert the appropriate values into your hsql db. To do this, you need to be aware of the following
	information:
	The connection string for the hsql DB would look like: jdbc:hsqldb:hsql://hostname:9001/xdb
	For dbvisualizer, you need to use hsqldb.jar as the jdbc driver

   2. Prepare the integration scenario you are interested in
	Every integration scenario contains a config directory where you can find an openadaptor wiring configuratin
	called config.xml. The documentation you can find on http://ccf.open.collab.net/nonav/exampleScenarioDocs/index.html
	was automatically generated out of these files. In most cases you will not change this file directly but the
	configuration values it references. These configuration values are stored in the .properties files contained
	in the same directory. Typically you just have to adjust the URL, user name and password for your SFEE system
	(contained in sfee.properties).
	
	Furthermore you may have to edit the SQL query in property ccf.sfeereader.poller.sql
	(contained in ccf.properties). 
	according to the changes you have made in the central CCF data base synchronization status table before.
	Looking at the generic artifact specification at
	http://ccf.open.collab.net/servlets/ProjectProcess?pageID=aT1rGG&subpageID=nLKvgT and the technical terms
	and definitions at http://ccf.open.collab.net/wiki/Naming_Conventions_and_Definitions will help again
	to exactly understand the meaning of this SQL query.

   3. Prepare the xslts:
   	Some integration scenarios make heavy use of artifact transformation. This transformation is done with the
   	help of XSLT scripts. Predefined xslt scripts are present in the xslt folder of the integration scenarios.
   	If the transformation depends on the source and target repository, XSLT scripts will have to follow certain
   	naming conventions (typically sourceSystem+sourceRepository+targetSystem+targetRepository.xsl). 
   	These naming conventions are explained in detail within the wiring file.
   	 
   	Please modify the name of xslt scripts in the xslt directory according to these rules.
   	If the trackers you like to synchronize have a different field structure as the preconfigured ones,
   	you also have to adjust the content of the xslt files.
   	Use the generic artifact export scenario (http://ccf.open.collab.net/wiki/ExampleScenarios) 
   	to get an impression how the xml documents will look like for your specific trackers.  

   4. Run the integration scenario:
	***** WRITE HOW TO RUN THE EXAMPLES UNDER WINDOWS/LINUX **** 
	
   5. Logs:
	When the connector is running, you might want to refer to the logs, which will be present in the
	logs directory of the integration scenario.


Happy Connecting!