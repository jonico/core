This sample demonstrates shipping the artifacts and the changes to
from one SFEE tracker to another SFEE tracker.

1. Configuring the SFEE tracker information
	Please refer the db script in db/SFEE2SFEE.script. Edit the source
	repository id to reflect your source SFEE tracker id and
	change the target repository id to reflect your target
	SFEE tracker id.
	
	Change the source SFEE SOAP server URL, username and password in the config/sfee.properties
	file as shown below.
	sfee.server.1.url=<SFEE SOAP server URL>
	sfee.server.1.username=<connector user>
	sfee.server.1.password=<connector user's password>
	
	In this sample we are synchronizing two trackers from the same
	SFEE system.
	
	Also the sample assumes that the fields are similar in both the SFEE
	trackers that are synchronized.