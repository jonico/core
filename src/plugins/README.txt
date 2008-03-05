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

1. A reader should implement the IDataProcessor interface.
2. The process() method should return an XML document which should adhere to the Generic Artifact Template
   specifications (http://ccf.open.collab.net/source/browse/*checkout*/ccf/trunk/docs/Generic%20Artifact%20Template.doc).
   This could be achieved easily by using the com.collabnet.ccf.core.ga.GenericArtifactHelper class.
   
ConfigHelper:
1. A ConfigHelper class that should implement a static method getSchemaFields() that 
   returns a List<Field> types, representing the artifact fields, and related info.
