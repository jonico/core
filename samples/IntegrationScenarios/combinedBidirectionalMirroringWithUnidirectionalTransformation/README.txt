This scenario combines the combined bidirectional mirroring scenario and unidirectional 
transformation scenario. What you can see from the wiring configuration is that the 
wiring looks exactly the same as in the bidirectional mirroring scenario. 
The only difference is the initial content in the synchronization information table that 
now also contains the data records to let the CCF synchronize the trackers defined in the 
unidirectional transformation scenario. Goal of this scenario is to show that in order to 
synchronize multiple repositories, no changes in the wirings have to be done. 
In fact, synchronization records can be added/deleted to the synchronization information 
table at runtime and the CCF will start/stop synchronization of the repositories specified 
in these rows.

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

 	
	