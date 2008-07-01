This sample demonstrates the capability of CCF to import 
artifacts data from a CSV file into an SFEE tracker.

1. Configuring the SFEE tracker information
	The trackerId to which the artifacts to be imported is configured in the
	xslt/xmlmap2genericartifact.xsl file as 
		targetRepositoryId="tracker1001"
	Please change this value to the tracker id that you intend to import the
	data
	
	Change the SFEE SOAP server URL and password in the config/sfee.properties
	file as shown below.
	sfee.server.1.url=<SFEE SOAP server URL>
	sfee.server.1.username=<connector user>
	sfee.server.1.password=<connector user's password>

2. Please note that this sample as is does not support the following
	a) importing multi-select field values.
		Mutiselect field values will not be imported properly if there are
		more than one field values are configured in the CSV file.
	b) attachments import.
	c) importing comments
