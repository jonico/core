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
		<xsl:if test="@fieldName = 'title'">
			<xsl:element name="TITLE"><xsl:value-of select="text()"/></xsl:element>
		</xsl:if>
		<xsl:if test="@fieldName = 'description'">
			<xsl:element name="DESCRIPTION"><xsl:value-of select="text()"/></xsl:element>
		</xsl:if>
		<xsl:if test="@fieldName = 'group'">
			<xsl:element name="GROUP_VAL"><xsl:value-of select="text()"/></xsl:element>
		</xsl:if>
		<xsl:if test="@fieldName = 'status'">
			<xsl:element name="STATUS"><xsl:value-of select="text()"/></xsl:element>
		</xsl:if>
		<xsl:if test="@fieldName = 'category'">
			<xsl:element name="CATEGORY"><xsl:value-of select="text()"/></xsl:element>
		</xsl:if>
		<xsl:if test="@fieldName = 'customer'">
			<xsl:element name="CUSTOMER"><xsl:value-of select="text()"/></xsl:element>
		</xsl:if>
		<xsl:if test="@fieldName = 'priority'">
			<xsl:element name="PRIORITY"><xsl:value-of select="text()"/></xsl:element>
		</xsl:if>
		<xsl:if test="@fieldName = 'assignedTo'">
			<xsl:element name="ASSIGNED_TO"><xsl:value-of select="text()"/></xsl:element>
		</xsl:if>
		<xsl:if test="@fieldName = 'reportedReleaseId'">
			<xsl:element name="REPORTED_RELEASE_ID"><xsl:value-of select="text()"/></xsl:element>
		</xsl:if>
		<xsl:if test="@fieldName = 'estimatedHours'">
			<xsl:element name="ESTIMATED_HOURS"><xsl:value-of select="text()"/></xsl:element>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>