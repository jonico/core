<?xml version="1.0" encoding="UTF-8"?>

<!--
 Copyright 2009 CollabNet, Inc. ("CollabNet")

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
-->

<!-- 
	1. What is this crazy name given to this xsl file?
		The CCF xsl transformer (com.collabnet.ccf.core.transformer.XsltProcessor) is
		capable of loading the correct XSL tranformation template provided we name the
		XSL file in the following naming convention
		     ${Source system id}+${source repository id}+$target system id}+${target repository id}.xsl
	2. How do I find these values?
		These values are configured in the database table SYNCHRONIZATION_STATUS. For SFEE trackers the
		target repository id is the tracker id in SFEE. Please refer the customreader.script
	3. After writing the transformer XSL what should I do?
		You need to configure the mapping in the database table. Please refer the customreader.script
 -->
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ccf="http://ccf.open.collab.net/GenericArtifactV1.0"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:stringutil="xalan://com.collabnet.ccf.core.utils.GATransformerUtil"
	exclude-result-prefixes="xsl xs ccf stringutil">
	<!--
		Let us copy the artifact tag's attributes as they are.
	 -->
	<xsl:template match='/ccf:artifact[@artifactType = "plainArtifact"]'>
		<artifact xmlns="http://ccf.open.collab.net/GenericArtifactV1.0">
			<xsl:copy-of
                    select="@*" />
			<xsl:apply-templates />
		</artifact>
	</xsl:template>
	<xsl:template match="/ccf:artifact[@artifactType = 'attachment']">
		<xsl:copy-of select="."/>
	</xsl:template>
	<!--
		We know that our SampleReader gives out the following fields.
		 1. Title
		 2. Summary
		 3. Status
		 4. Priority
		 5. Assigned To
		 Let us transfer these fields one by one into the outgoing Generic Artifact
		 xml.
	 -->
	 <!--
	 	Let us start with the Title field.
	 	The Title field is transformed into a field called title
	  -->
	<xsl:template
		match='ccf:field[@fieldName="Title"]'>
		<field>
			<xsl:copy-of select="@*" />
		    <xsl:attribute name="fieldName">title</xsl:attribute>
		  	<xsl:value-of select="."/>
	  	</field>
	</xsl:template>
	<!--
		Let us copy the Summary field now. We are copying the Summary field into
		the SFEE tracker field description.
	 -->
	<xsl:template
		match='ccf:field[@fieldName="Summary"]'>
		<field>
			<xsl:copy-of select="@*" />
		    <xsl:attribute name="fieldName">description</xsl:attribute>
		   	<xsl:value-of select="."/>
	  	</field>
	</xsl:template>
	
	<!--
		The Status field is copied to the SFEE tracker field status.
	 -->
	<xsl:template
		match='ccf:field[@fieldName="Status"]'>
		<xsl:variable name="statusValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
		    <xsl:attribute name="fieldName">status</xsl:attribute>
		    <xsl:value-of select="."/>
	  	</field>
	</xsl:template>
	<!--
		Let us now transform the priority field.
		The priority field is given as "Highest" in the SampleReader.
		But in SFEE we have numbers from 1 to 5 as the priority.
		1 being the highest priority 5 being the lowest.
		So we need to translate the value "Highest" to 1.
		That is exactly what is done here.
	 -->
	 <xsl:template
		match='ccf:field[@fieldName="Priority"]'>
		<xsl:variable name="priorityValue" as="xs:string"><xsl:value-of select="." /></xsl:variable>
		<field>
			<xsl:copy-of select="@*" />
		    <xsl:attribute name="fieldName">priority</xsl:attribute>
		  	
		  	<xsl:if test="$priorityValue = 'Lowest'"><xsl:text>5</xsl:text></xsl:if>
			<xsl:if test="$priorityValue = 'Low'"><xsl:text>4</xsl:text></xsl:if>
			<xsl:if test="$priorityValue = 'Medium'"><xsl:text>3</xsl:text></xsl:if>
			<xsl:if test="$priorityValue = 'High'"><xsl:text>2</xsl:text></xsl:if>
			<xsl:if test="$priorityValue = 'Highest'"><xsl:text>1</xsl:text></xsl:if>
		</field>
	</xsl:template>
	<!--
		Now let us transform the "Assigned To" field given by the SampleReader.
		Not always the systems that are being connected by CCF will have their
		users configured to be the same at both ends. In those cases we need to
		map the source system users to the target system users.
		
		In our case the user martian is mapped to the target system user mseethar.
	 -->
	<xsl:template
		match='ccf:field[@fieldName="Assigned To"]'>
		<xsl:variable name="assignedTo" as="xs:string"><xsl:value-of select="." /></xsl:variable>
		<field>
			<xsl:copy-of select="@*" />
		    <xsl:attribute name="fieldName">assignedTo</xsl:attribute>
		  	<xsl:if test="$assignedTo = 'martian'"><xsl:text>mseethar</xsl:text></xsl:if>
			<xsl:if test="$assignedTo = 'None'"><xsl:text>None</xsl:text></xsl:if>
		</field>
	</xsl:template>
	<xsl:template match="text()" />
</xsl:stylesheet>