This scenario shows how unidirectional artifact transformation can be done 
(from Unidirectional Transformation Scenario Source Tracker to Unidirectional 
Transformation Scenario Target Tracker). During this transformation, the artifacts' 
priority will be inversed, the actual hours will be swapped with the estimated hours, 
all single and multiple select values starting with "Source" will be changed to start 
with "Target", one flex field will be removed, and two flex fields will be concatenated 
in stored within one text field. One target flex field should always contain the string 
constant "CCF". All other field values will be copied.


Configuring the SFEE tracker information:

1.  Please refer the db script in db/SFEE2SFEE.script. Edit the source
	repository id to reflect your source SFEE tracker id and
	change the target repository id to reflect your target
	SFEE tracker id.
	
2.	Change the source SFEE SOAP server URL, username and password in the config/sfee.properties
	file as shown below.
	sfee.server.1.url=<SFEE SOAP server URL>
	sfee.server.1.username=<connector user>
	sfee.server.1.password=<connector user's password>