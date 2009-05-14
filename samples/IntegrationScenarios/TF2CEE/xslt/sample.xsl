<?xml version="1.0"?>
	<!--
		If there is a need to do so, I can extend the XsltProcessor component
		that it passes global parameters to this XSLT-Script XSLT allows you
		to use global variables (stylesheet parameters), template parameters,
		modes and function parameters
	-->
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ccf="http://ccf.open.collab.net/GenericArtifactV1.0"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:ptutil="xalan://com.collabnet.ccf.pi.cee.pt.v50.ProjectTrackerHelper"
	exclude-result-prefixes="xsl xs" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.w3.org/1999/XSL/Transform http://www.w3.org/2007/schema-for-xslt20.xsd">
	<xsl:variable name="sourceArtifactId" as="xs:string"
		select="/ccf:artifact/@sourceArtifactId" />
	
	<!--  This key can be used for every mapping-->
	<xsl:key name='mappingKey' match='mapping' use='@source' />
	<!--
		Template to substitute SFEE user values Will look up the
		userMappings.xml for a substitute value If not found, it will use a
		default value
	-->
	<xsl:template name="substituteSFEEUserName">
		<xsl:variable name="originalUserName" select="." />
		<xsl:if test="$originalUserName != ''">
			<xsl:for-each
				select="document('userMappings.xml')/CCFMappingStructure/SFEE2CEEUserMapping">
				<xsl:variable name="valueLookUpResult" as="xs:string"
					select='key("mappingKey",$originalUserName)/@target' />
				<xsl:choose>
					<xsl:when test='$valueLookUpResult!=""'>
						<xsl:value-of select='$valueLookUpResult' />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select='key("mappingKey","default")/@target' />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
		</xsl:if>
	</xsl:template>
	<xsl:template match='/ccf:artifact[@artifactType = "plainArtifact"]'>
		<artifact xmlns="http://ccf.open.collab.net/GenericArtifactV1.0">
			<xsl:copy-of select="@*" />
			<xsl:apply-templates />
		</artifact>
	</xsl:template>
	<xsl:template match="/ccf:artifact[@artifactType = 'attachment']">
		<xsl:copy-of select="." />
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="OCN Bug Id"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:attribute name="fieldName">Forge Id</xsl:attribute>
			<xsl:value-of select="$sourceArtifactId"></xsl:value-of>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="category"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:attribute name="fieldName">Component</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="title"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:attribute name="fieldName">Summary</xsl:attribute>
			<!--<xsl:value-of
				select="substring-after(.,': ')"/>-->
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="description"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:attribute name="fieldName">Description</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="status"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:attribute name="fieldName">Status</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="priority"]'>
		<xsl:variable name="priorityValue" as="xs:string">
			<xsl:value-of select="." />
		</xsl:variable>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:attribute name="fieldName">Priority</xsl:attribute>
			<xsl:if test="$priorityValue = '5'">
				<xsl:text>P5</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = '4'">
				<xsl:text>P4</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = '3'">
				<xsl:text>P3</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = '2'">
				<xsl:text>P2</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = '1'">
				<xsl:text>P1</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = ''">
				<xsl:text>P5</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Found in Version"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Version</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Target Milestone"]'>
		<xsl:variable name="priorityValue" as="xs:string">
			<xsl:value-of select="." />
		</xsl:variable>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Target milestone</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Issue Type"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Issue type</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<!--
		This is a rule that falls back to a default value if it does not find
		a substitution value
	-->
	<xsl:template match='ccf:field[@fieldName="createdBy"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Detected by</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:attribute name="fieldValueType">String</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<!--
		This is a rule that falls back to a default value if it does not find
		a substitution value
	-->
	<xsl:template match='ccf:field[@fieldName="Assigned to"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Assigned to</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:call-template name="substituteSFEEUserName" />
		</field>
	</xsl:template>
	<!--
		This is a rule that does only create values if a target value has been
		found
	-->
	<xsl:template match='ccf:field[@fieldName="Carbon copy"]'>
		<xsl:variable name="fieldContent" select="." />
		<xsl:variable name="allAttributes" select="@*" />
		<xsl:if test="$fieldContent != ''">
			<xsl:for-each
				select="document('userMappings.xml')/CCFMappingStructure/SFEE2CEEUserMapping">
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
	<xsl:template match='ccf:field[@fieldName="Comment Text"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">{urn:ws.tracker.collabnet.com}comment</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="."></xsl:value-of>
		</field>
	</xsl:template>
	<xsl:template match="text()" />
</xsl:stylesheet>