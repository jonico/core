<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:ccf="http://ccf.open.collab.net/GenericArtifactV1.0"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	exclude-result-prefixes="xsl xs">
	<xsl:template match='/node()' priority="2">
		<artifact xmlns="http://ccf.open.collab.net/GenericArtifactV1.0"
			artifactAction="update" artifactMode="complete"
			artifactType="plainArtifact"
			artifactLastModifiedDate="unknown"
			artifactLastReadTransactionId="unknown" artifactVersion="unknown"
			conflicResolutionPolicy="unknown" sourceArtifactId="unknown"
			sourceRepositoryId="csvinputfile.dat" sourceRepositoryKind="CSVClearQuestFile"
			sourceSystemId="" sourceSystemKind="Filesystem"
			targetArtifactId="unknown" targetRepositoryId="csvoutputfile.dat"
			targetRepositoryKind="CSVSFEETrackerFile" targetSystemId="localhost"
			targetSystemKind="Filesystem">
			<xsl:apply-templates />
		</artifact>
	</xsl:template>
	<xsl:template match="node()" priority="1">
		<field>
			<xsl:attribute name="fieldName"><xsl:value-of select="local-name()"/></xsl:attribute>
		    <xsl:attribute name="fieldDisplayName"><xsl:value-of select="local-name()"/></xsl:attribute>
		    <xsl:attribute name="fieldAction">replace</xsl:attribute>
		    <xsl:attribute name="fieldType">flexField</xsl:attribute>
		  	<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
		  	<xsl:attribute name="fieldValueType">String</xsl:attribute>
		  	<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
			<xsl:value-of select="text()"/>
		</field>
	</xsl:template>
</xsl:stylesheet>