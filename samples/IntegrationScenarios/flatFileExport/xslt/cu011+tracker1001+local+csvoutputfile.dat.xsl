<?xml version="1.0"?>

<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ccf="http://ccf.open.collab.net/GenericArtifactV1.0"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xsl xs">
	<xsl:template match='/ccf:artifact'>
		<record>
			<xsl:apply-templates />
		</record>
	</xsl:template>
	<xsl:template match='ccf:field'>
		<xsl:variable name="fieldName" as="xs:string"><xsl:value-of select="@fieldName" /></xsl:variable>
		<xsl:choose>
			<xsl:when test="$fieldName = 'id' and @fieldType = 'mandatoryField'">
				<xsl:element name="artifactId"><xsl:value-of select="text()"/></xsl:element>
			</xsl:when>
			<xsl:when test="$fieldName = 'Comment Text'"/>
			<xsl:otherwise>
				<xsl:element name="{$fieldName}"><xsl:value-of select="text()"/></xsl:element>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>