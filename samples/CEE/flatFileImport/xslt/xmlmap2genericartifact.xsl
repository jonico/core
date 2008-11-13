<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:ccf="http://ccf.open.collab.net/GenericArtifactV1.0"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	exclude-result-prefixes="xsl xs">
	<xsl:template match="/node()" priority="2">
	   <xsl:variable name="ptID" as="xs:string">
            <xsl:value-of select="//PT_ID" />
        </xsl:variable>
		<artifact
			xmlns="http://ccf.open.collab.net/GenericArtifactV1.0">
			<xsl:attribute name="artifactAction">update</xsl:attribute>
			<xsl:attribute name="artifactMode">complete</xsl:attribute>
			<xsl:attribute name="artifactType">plainArtifact</xsl:attribute>
			<xsl:attribute name="sourceArtifactLastModifiedDate">unknown</xsl:attribute>
			<xsl:attribute name="targetArtifactLastModifiedDate">unknown</xsl:attribute>
			<xsl:attribute name="artifactLastReadTransactionId">unknown</xsl:attribute>
			<xsl:attribute name="sourceArtifactVersion">unknown</xsl:attribute>
			<xsl:attribute name="targetArtifactVersion">unknown</xsl:attribute>
			<xsl:attribute name="conflictResolutionPriority">alwaysOverride</xsl:attribute>
			<xsl:attribute name="sourceArtifactId">unknown</xsl:attribute>
			<xsl:attribute name="sourceRepositoryId">input.txt</xsl:attribute>
			<xsl:attribute name="sourceRepositoryKind">CSVQCFile</xsl:attribute>
			<xsl:attribute name="sourceSystemId">CSVFile</xsl:attribute>
			<xsl:attribute name="sourceSystemKind">Filesystem</xsl:attribute>
			<xsl:attribute name="targetRepositoryId">madccf:Issue-Tracker</xsl:attribute>
			<xsl:attribute name="targetRepositoryKind">TRACKER</xsl:attribute>
			<xsl:attribute name="targetSystemId">cu011</xsl:attribute>
			<xsl:attribute name="targetSystemKind">CEE 5.0</xsl:attribute>
			<xsl:attribute name="transactionId">0</xsl:attribute>
			<xsl:attribute name="errorCode">ok</xsl:attribute>
			<xsl:attribute name="includesFieldMetaData">false</xsl:attribute>
			<xsl:attribute name="targetArtifactId">
				<xsl:value-of
	                    select="$ptID" />
            </xsl:attribute>
			<xsl:attribute name="sourceSystemTimezone">IST</xsl:attribute>
			<xsl:attribute name="sourceSystemEncoding">unknown</xsl:attribute>
			<xsl:attribute name="targetSystemTimezone">GMT</xsl:attribute>
			<xsl:attribute name="targetSystemEncoding">unknown</xsl:attribute>
			<xsl:apply-templates />
		</artifact>
	</xsl:template>
	<!-- Dynamic fields -->
	<xsl:template match="node()" priority="1">
		<xsl:variable name="fieldName" as="xs:string">
			<xsl:value-of select="local-name()" />
		</xsl:variable>
		<xsl:if test="$fieldName != 'PT_ID'">
			<field>
				<xsl:attribute name="fieldName"><xsl:value-of select="$fieldName" /></xsl:attribute>
				<xsl:attribute name="fieldDisplayName"><xsl:value-of
						select="$fieldName" />
				</xsl:attribute>
				<xsl:attribute name="fieldAction">replace</xsl:attribute>
				<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
				<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				<xsl:attribute name="fieldValueType">String</xsl:attribute>
				<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				<xsl:value-of select="text()" />
			</field>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
