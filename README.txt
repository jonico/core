CollabNet Connector Framework (HP Quality Center and CollabNet ScrumworksPro Integrations with CollabNet TeamForge)
Version 1.5.1

Introduction:
-------------             

The CollabNet Connector Framework enables synchronization
of backlog items, tasks, releases, sprints, themes, teams and products
between CollabNet TeamForge (5.3+) and ScrumWorks Pro (4.4+).

CollabNet Connector Framework also enables shipment of defect and requirement data 
from HP Quality Center (9.0, 9.2, 10.0, 11.0) to a CollabNet tracker
(Project Tracker or TeamForge tracker), and vice versa.

For more information on the CollabNet Connector Framework please
visit http://ccf.open.collab.net and http://help.collab.net


System Requirements
-------------------
1. Operating System Requirements
    MS Windows XP SP2
    MS Windows 2003 Server
    MS Windows 2008 Server
    MS Windows Vista
    MS Windows 7
    Red Hat Enterprise Linux 5.3

If HP Quality Center should be synchronized, CCF has to run in a machine that can access Quality Center using Internet Explorer.
CCF needs TDAdmin permissions and Windows has to be configured in a way that the HP Quality Center COM API can be accessed from a service. 

2. Memory requirement
    Minimum 1 GB RAM is recommended

3. Java Requirements
    JDK 1.6/Java 6 or above is required

4. User interface requirements
    Eclipse 3.4 or higher version which includes the XML editor
    (e. g. all downloads on eclipse.org that contain the Java IDE)
    
5. Requirement for graphical field mapping feature (only relevant for HP Quality Center Integration)
    Standard/Professional edition of Altova MapForce version 2008 (rel 2) or newer


Getting Started
---------------
For help on setting up CCF and its GUI, see the following:
	- Quick Start Guide: http://help.collab.net
	- Documentation in the CCF project: http://ccf.open.collab.net
	- Frequently Asked Questions: 
	  http://ccf.open.collab.net/wiki/Frequently_Asked_Questions
	- Best Practices Guide: http://ccf.open.collab.net/wiki/BestPractices

For new developers:

	How to setup source code:

		1.For code development we uses Spring Tool Suite(STS) eclipse(version 2.8 or greater).
			Download Link -> https://sagan-production.cfapps.io/tools
		2.Checkout the source code from the repo and import the project as java project.
		3.Follow STS eclipse based menu options to import the project.
			File -> Import -> Java -> File System
		4.Above said works well for compiling and building the project locally in your system.

	To send patch for review to the community:

		1.Make sure your code follows our code style configuration defined.
		2.Make sure all unit test case followed runs successfully.
		3.Finally Create a patch and send it to our development mailing list.					
					
		
	Code style configuration:
	
		This project follows java code style conventions,from STS eclipse we have exported the code style formatting configuration into a config file (available as part of this project).
		Developers need to follow the steps mentioned below to configure and to use it.
		
		1.Import the Code style configuration file "ccf-formatter.xml".To import follow steps in STS eclipse.				
			Window -> Preferences -> Java -> Code Style -> Formatter -> import 
		2.Also configure Member sort order(this is to align and order variable and methods).Following changes needs to be applied.
			Window -> Preferences -> Java -> Appearance -> Member Sort Order
		3.In Member Sort Order dialog window correct sequential order as mentioned below
			Types,Fields,Static Fields,Initializers,Static Initializers,Constructor,Methods,Static Methods
		4.Also in Member Sort Order dialog window enable "Sort members in same category by visibility" option and correct the order as given below.
			Public,Protected,Private,Default  
		5.By default we have configured to format the code, when we save the code changes.

License
-------
Please refer to License.txt for license information.


Support for CollabNet Certified Integrations
--------------------------------------------
Find out about CollabNet Technical Support: 
http://www.open.collab.net/support/

Information about CollabNet Consulting services is at 
http://www.open.collab.net/consulting/

Join openCollabNet for community support: http://open.collab.net


About CollabNet
---------------  
CollabNet leads the industry in Agile application lifecycle management 
(Agile ALM) in the Cloud. The CollabNet TeamForge™ ALM platform, 
CollabNet Subversion software configuration management (SCM) solution, 
and ScrumWorks® project and program management software enable teams 
using any environment, methodology, and technology to increase productivity 
by up to 50% and to reduce the cost of software development by up to 80%.  
The company also offers training, including Certified ScrumMaster training, 
software development process improvement services, and an innovative 
community management approach to driving enterprise development success.  
As the founder of the open source Subversion project, CollabNet has 
collaborative development for distributed teams in its DNA. Millions of 
users at more than 2,500 organizations have transformed the way they 
develop software with CollabNet. 
For more information, visit http://www.collab.net.