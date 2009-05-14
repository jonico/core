<?xml version="1.0"?>

<!-- 
	If there is a need to do so, I can extend the XsltProcessor component
	that it passes global parameters to this XSLT-Script
	
	XSLT allows you to use global variables (stylesheet parameters),
	template parameters, modes and function parameters  
-->

<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ccf="http://ccf.open.collab.net/GenericArtifactV1.0"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:ptutil="xalan://com.collabnet.ccf.pi.cee.pt.v50.ProjectTrackerHelper"
	exclude-result-prefixes="xsl xs" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.w3.org/1999/XSL/Transform http://www.w3.org/2007/schema-for-xslt20.xsd">
	
	
	<!--  This key can be used for every  mapping -->
	<xsl:key name='mappingKey' match='mapping' use='@source' />

	<!-- 
		Template to substitute CEE user values
		Will look up the userMappings.xml for a substitute value
		If not found, it will use a default value
	-->
	<xsl:template name="substituteCEEUserName">
		<xsl:variable name="originalUserName" select="."/>

		<xsl:if test="$originalUserName != ''">
			<xsl:for-each
				select="document('userMappings.xml')/CCFMappingStructure/CEE2SFEEUserMapping">
				<xsl:variable name="valueLookUpResult" as="xs:string"
					select='key("mappingKey",$originalUserName)/@target' />
				<xsl:choose>
					<xsl:when test='$valueLookUpResult!=""'>
						<xsl:value-of select='$valueLookUpResult' />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of
							select='key("mappingKey","default")/@target' />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
		</xsl:if>
	</xsl:template>

	<xsl:variable name="sourceArtifactId" as="xs:string"
		select="/ccf:artifact/@sourceArtifactId"/>
		
	<xsl:variable name="artifactTypeNameSpace" as="xs:string" 
		select="ptutil:getNamespaceWithBraces($sourceArtifactId)" />

	<xsl:template
		match='/ccf:artifact[@artifactType = "plainArtifact"]'>
		<artifact
			xmlns="http://ccf.open.collab.net/GenericArtifactV1.0">
			<xsl:copy-of select="@*" />

			<!-- This is a rule that falls back to a default value if it does not find a substitution value -->
			<xsl:for-each
				select='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Assigned to")]'>
				<xsl:if test="position()=last()">
					<field>
						<xsl:copy-of select="@*" />
						<xsl:attribute name="fieldName">assignedTo</xsl:attribute>
						<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
						<xsl:variable name="fieldContent" select="." />
						<xsl:call-template name="substituteCEEUserName"/>
					</field>
				</xsl:if>
			</xsl:for-each>
			<xsl:apply-templates />
		</artifact>
	</xsl:template>

	<xsl:template match="/ccf:artifact[@artifactType = 'attachment']">
		<xsl:copy-of select="." />
	</xsl:template>

	<xsl:template
		match='ccf:field[@fieldName="{urn:ws.tracker.collabnet.com}id"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:attribute name="fieldName">OCN Bug Id</xsl:attribute>
			<xsl:value-of select="."/>
		</field>
	</xsl:template>

	<xsl:template
		match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Summary")]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:attribute name="fieldName">title</xsl:attribute>
			<!--<xsl:value-of
				select="concat(concat(substring-after($sourceArtifactId,'issue:'),': '),.)"/>-->
			<xsl:value-of
				select="."/>
		</field>
	</xsl:template>

	<xsl:template
		match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Description")]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:attribute name="fieldName">description</xsl:attribute>
			<xsl:value-of select="."/>
		</field>
	</xsl:template>
	
	<xsl:template
		match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Component")]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:attribute name="fieldName">category</xsl:attribute>
			<xsl:value-of select="."/>
		</field>
	</xsl:template>

	<xsl:template
		match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Status")]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:attribute name="fieldName">status</xsl:attribute>
			<xsl:value-of select="."/>
		</field>
	</xsl:template>

	<xsl:template
		match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Priority")]'>
		<xsl:variable name="priorityValue" as="xs:string">
			<xsl:value-of select="." />
		</xsl:variable>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:attribute name="fieldName">priority</xsl:attribute>
			<xsl:if test="$priorityValue = 'P5'">
				<xsl:text>5</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = 'P4'">
				<xsl:text>4</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = 'P3'">
				<xsl:text>3</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = 'P2'">
				<xsl:text>2</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = 'P1'">
				<xsl:text>1</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = ''">
				<xsl:text>5</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>

	<xsl:template
		match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Version")]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Found in Version</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="."/>
		</field>
	</xsl:template>

	<xsl:template
		match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Target milestone")]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Target Milestone</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="."/>
		</field>
	</xsl:template>

	<xsl:template
		match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Issue type")]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Issue Type</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="."/>
		</field>
	</xsl:template>

	<!-- This is a rule that falls back to a default value if it does not find a substitution value -->
	<xsl:template
		match='ccf:field[@fieldName="{urn:ws.tracker.collabnet.com}createdBy"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Detected by</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:attribute name="fieldValueType">User</xsl:attribute>
			<xsl:call-template name="substituteCEEUserName"/>
		</field>
	</xsl:template>

	<!-- This is a rule that falls back to a default value if it does not find a substitution value -->
	<xsl:template
		match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Assigned to")]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Assigned to</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:call-template name="substituteCEEUserName"/>
		</field>
	</xsl:template>

	<!-- This is a rule that does only create values if a target value has been found -->
	<xsl:template
		match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Carbon copy")]'>
		<xsl:variable name="fieldContent" select="." />
		<xsl:variable name="allAttributes" select="@*" />
		<xsl:if test="$fieldContent != ''">
			<xsl:for-each
				select="document('userMappings.xml')/CCFMappingStructure/CEE2SFEEUserMapping">
				<xsl:variable name="valueLookUpResult" as="xs:string"
					select='key("mappingKey",$fieldContent)/@target' />
				<xsl:choose>
					<xsl:when test='$valueLookUpResult!=""'>
						<field>
							<xsl:copy-of select="$allAttributes" />
							<xsl:attribute name="fieldName">Carbon copy</xsl:attribute>
							<xsl:attribute name="fieldType">flexField</xsl:attribute>
							<xsl:value-of select='$valueLookUpResult' />
						</field>
					</xsl:when>
				</xsl:choose>
			</xsl:for-each>
		</xsl:if>

		<xsl:if test="$fieldContent = ''">
			<field>
				<xsl:copy-of select="$allAttributes" />
				<xsl:attribute name="fieldName">Carbon copy</xsl:attribute>
				<xsl:attribute name="fieldType">flexField</xsl:attribute>
			</field>
		</xsl:if>
	</xsl:template>

	<xsl:template
		match='ccf:field[@fieldName="{urn:ws.tracker.collabnet.com}comment"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Comment Text</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="."/>
		</field>
	</xsl:template>

	<xsl:template match="text()" />
</xsl:stylesheet>