										How to Write a CCF Plug-in
										==========================
									TODOS and specifications to adhere to
									.....................................
					
Directory and Package Structure:

1. The plug-in source should be placed under src/plugins
2. A descriptive path should be chosen as the package name. For example,
   com.collab.net.ccf.pi.qc (Talks about the company - collabnet, the component - ccf,
   the module pi - plugins, and then the actual component connecting to - QC  )

	and not:
   connector.plugin.qc
				
Reader:

1. A reader should extend the com.collabnet.ccf.core.AbstractReader class.
2. The AbstractReader class defines three abstract methods
	a) getChangedArtifact()
		In your class implement this method to return the artifact ids that are changed after the
		last read time or after the last read transaction id in the syncInfo DB xml document 
		whichever is applicable to the system that you are writing the plugin for.
	b) getArtifactData()
		Implement this method to return the artifact data of the artifact with the id as artifactId.
		The artifact data that you are returning must be in a GenericArtifact object. If you intend
		to ship the whole artifact change history to the target system, capture all the changes in
		a list of GenericArtifact object that replicate the artifact change history.
	c) getArtifactAttachments()
		This method should be implemented to return all the attachments to an artifact that are created
		after the last read time in the syncInfo.
	d) getArtifactDependencies()
		Implement this method to return any dependent artifacts for this particular artifact. In SFEE
		dependent artifacts are called associations. Similarly find out the dependent artifacts 
		retrieval procedure for your system and code this method to return those dependent artifacts
		in the GenericArtifact object format.
3. To manage your systems connections we provide a com.collabnet.ccf.core.eis.connection.ConnectionManager
	that can pool connections to your system and manage. All you need to do is to implement the
	com.collabnet.ccf.core.eis.connection.ConnectionFactory interface and set an instance of the 
	ConnectionFactory to the ConnectionManager.
	From your Readers and Writers you should use the ConnectionManager to request a connection to your
	system/repository.

ConfigHelper:
1. A ConfigHelper class that should implement a static method getSchemaFields() that 
   returns a List<Field> types, representing the artifact fields, and related info.
