                CollabNet Tracker Connector to HP Quality Center 4.1
                ----------------------------------------------------
                
Introduction:
-------------             
CollabNet Tracker Connector to HP Quality Center is based on CollabNet Connector Framework 1.1.
It enables shipment of defect data from HP Quality Center to a CollabNet tracker (Project Tracker or SourceForge tracker), and vice versa.

For more information on the CollabNet Connector Framework please
visit http://ccf.open.collab.net

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
    Quality Center versions 9.0 and 9.2 are supported
    Quality Center installation with the following backend databases are tested
        1. SQL Server 8.00.760
        2. Oracle 10g

5. Java Requirements
    JDK 1.5 or above is required

Getting Started
---------------
This package comes with some preconfigured sample scenarios. For help on setting up the central CCF database and synchronizing the source and target systems, see the following:
	- Quick Start Guides to connect HP Quality center to CollabNet Project Tracker or SourceForge Tracker: http://help.collab.net/
        - Documentation in the CCF project: http://ccf.open.collab.net/servlets/ProjectProcess?pageID=aT1rGG&subpageID=lTTUmC
	- Release Notes on http://help.collab.net/
	- Frequently Asked Questions: http://ccf.open.collab.net/wiki/Frequently_Asked_Questions

Make sure that you set the email configuration in log4j.xml to receive auto-generated mails in case of issues.

Support
-------
Please refer to License.txt for support and other options.