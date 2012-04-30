								Generic Artifact Template
								=========================
								
This file describes aspects relating to the Generic Artifact and its template.

1. Specification:
	The specification for the generic artifact is provided directly inside the XML schema file genericartifactschema.xsd
	
2. Helpers:
	The GenericArtifactHelper class contains methods to create a Generic Artifact. All Reader classes
	are expected to use this class to generate generic artifacts and writer classes may chose to use it to parse the xml documents.
	However, support processor that only change some aspects of an already existing artifact document should manipulate its contents
	by using XPath expressions in order not consume unnecessary main memory. 