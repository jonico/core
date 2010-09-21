CollabNet Connector Framework
Version 1.4.2
                 
Introduction:
-------------             
CollabNet Connector Framework enables shipment of defect data from HP Quality Center to a CollabNet tracker
(Project Tracker or TeamForge tracker), and vice versa.

For more information on the CollabNet Connector Framework please
visit http://ccf.open.collab.net and http://help.collab.net

System Requirements
-------------------
1. Operating System Requirements
    Windows XP SP2
    Windows 2003 Server (with Microsoft Visual C++ 2005 SP1 Redistributable Package installed)

2. Memory requirement
    Minimum 1 GB RAM is recommended

3. OTAClient
	- Download and install the Microsoft Visual Studio 2005 libraries: http://www.microsoft.com/downloads/details.aspx?FamilyID=200b2fd9-ae1a-4a14-%20984d-389c36f85647&displaylang=en
    OTAClient.dll must be installed on the connector machine
        - OTAClient.dll will be installed automatically if the HP Quality Center web interface is accessed on the connector machine
        - Log in using the HP QC user name and password

4. HP Quality Center
    Quality Center versions 9.0, 9.2 and 10 are supported
    Quality Center installation with the following backend databases are tested
        1. SQL Server 8.00.760
        2. Oracle 10g

5. Java Requirements
    JDK 1.6 or above is required
    
6. User interface requirements
    Eclipse 3.4 version which includes the XML editor (e.g. all downloads on eclipse.org that contain the Java IDE)

7. Requirement for graphical field mapping feature
    Standard edition of Altova MapForce version 2008 (rel 2) or 2009


Getting Started
---------------
For help on setting up CCF and its GUI, see the following:
	- Quick Start Guides to connect HP Quality center to CollabNet Project Tracker or TeamForge Tracker: http://help.collab.net/
    - Documentation in the CCF project: http://ccf.open.collab.net
	- Release Notes on http://help.collab.net/
	- Frequently Asked Questions: http://ccf.open.collab.net/wiki/Frequently_Asked_Questions


License
-------
Please refer to License.txt for license information.