<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:ccf="http://ccf.open.collab.net/GenericArtifactV1.0"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	exclude-result-prefixes="xsl xs">
	<xsl:template match='/node()' priority="2">
		<artifact xmlns="http://ccf.open.collab.net/GenericArtifactV1.0"
			artifactAction="create" artifactMode="complete"
			artifactType="plainArtifact"
			sourceArtifactLastModifiedDate="unknown"
			targetArtifactLastModifiedDate="unknown"
			artifactLastReadTransactionId="unknown" sourceArtifactVersion="unknown"
			targetArtifactVersion="unknown"
			conflictResolutionPriority="unknown" sourceArtifactId="CSV-1"
			sourceRepositoryId="input.txt" sourceRepositoryKind="CSVClearQuestFile"
			sourceSystemId="CSVFile" sourceSystemKind="Filesystem"
			targetArtifactId="NEW" targetRepositoryId="tracker1001"
			targetRepositoryKind="TRACKER" targetSystemId="cu011"
			targetSystemKind="SFEE 4.4" transactionId="0"
			errorCode="ok" includesFieldMetaData="false">
			<xsl:apply-templates />
		</artifact>
	</xsl:template>
	<xsl:template match="node()" priority="1">
			<xsl:variable name="fieldName" as="xs:string"><xsl:value-of select="local-name()" /></xsl:variable>
			<xsl:if test="$fieldName = 'title'">
				<field>
					<xsl:attribute name="fieldName">title</xsl:attribute>
				    <xsl:attribute name="fieldDisplayName"><xsl:value-of select="$fieldName"/></xsl:attribute>
				    <xsl:attribute name="fieldAction">replace</xsl:attribute>
				    <xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
				  	<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				  	<xsl:attribute name="fieldValueType">String</xsl:attribute>
				  	<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				  	<xsl:value-of select="text()"/>
				</field>
			</xsl:if>
			<xsl:if test="$fieldName = 'description'">
				<field>
					<xsl:attribute name="fieldName">description</xsl:attribute>
				    <xsl:attribute name="fieldDisplayName"><xsl:value-of select="$fieldName"/></xsl:attribute>
				    <xsl:attribute name="fieldAction">replace</xsl:attribute>
				    <xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
				  	<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				  	<xsl:attribute name="fieldValueType">String</xsl:attribute>
				  	<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				  	<xsl:value-of select="text()"/>
				</field>
			</xsl:if>
			<xsl:if test="$fieldName = 'status'">
				<field>
					<xsl:attribute name="fieldName">status</xsl:attribute>
				    <xsl:attribute name="fieldDisplayName"><xsl:value-of select="$fieldName"/></xsl:attribute>
				    <xsl:attribute name="fieldAction">replace</xsl:attribute>
				    <xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
				  	<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				  	<xsl:attribute name="fieldValueType">String</xsl:attribute>
				  	<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				  	<xsl:value-of select="text()"/>
				</field>
			</xsl:if>
			<xsl:if test="$fieldName = 'category'">
				<field>
					<xsl:attribute name="fieldName">category</xsl:attribute>
				    <xsl:attribute name="fieldDisplayName"><xsl:value-of select="$fieldName"/></xsl:attribute>
				    <xsl:attribute name="fieldAction">replace</xsl:attribute>
				    <xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
				  	<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				  	<xsl:attribute name="fieldValueType">String</xsl:attribute>
				  	<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				  	<xsl:value-of select="text()"/>
				</field>
			</xsl:if>
			<xsl:if test="$fieldName = 'priority'">
				<field>
					<xsl:attribute name="fieldName">priority</xsl:attribute>
				    <xsl:attribute name="fieldDisplayName"><xsl:value-of select="$fieldName"/></xsl:attribute>
				    <xsl:attribute name="fieldAction">replace</xsl:attribute>
				    <xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
				  	<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				  	<xsl:attribute name="fieldValueType">Integer</xsl:attribute>
				  	<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				  	<xsl:value-of select="text()"/>
				</field>
			</xsl:if>
			<xsl:if test="$fieldName = 'assignedTo'">
				<field>
					<xsl:attribute name="fieldName">assignedTo</xsl:attribute>
				    <xsl:attribute name="fieldDisplayName"><xsl:value-of select="$fieldName"/></xsl:attribute>
				    <xsl:attribute name="fieldAction">replace</xsl:attribute>
				    <xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
				  	<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				  	<xsl:attribute name="fieldValueType">String</xsl:attribute>
				  	<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				  	<xsl:value-of select="text()"/>
				</field>
			</xsl:if>
			<xsl:if test="$fieldName = 'group'">
				<field>
					<xsl:attribute name="fieldName">group</xsl:attribute>
				    <xsl:attribute name="fieldDisplayName"><xsl:value-of select="$fieldName"/></xsl:attribute>
				    <xsl:attribute name="fieldAction">replace</xsl:attribute>
				    <xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
				  	<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				  	<xsl:attribute name="fieldValueType">String</xsl:attribute>
				  	<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				  	<xsl:value-of select="text()"/>
				</field>
			</xsl:if>
			<xsl:if test="$fieldName = 'customer'">
				<field>
					<xsl:attribute name="fieldName">customer</xsl:attribute>
				    <xsl:attribute name="fieldDisplayName"><xsl:value-of select="$fieldName"/></xsl:attribute>
				    <xsl:attribute name="fieldAction">replace</xsl:attribute>
				    <xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
				  	<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				  	<xsl:attribute name="fieldValueType">String</xsl:attribute>
				  	<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				  	<xsl:value-of select="text()"/>
				</field>
			</xsl:if>
			<xsl:if test="$fieldName = 'reportedReleaseId'">
				<field>
					<xsl:attribute name="fieldName">reportedReleaseId</xsl:attribute>
				    <xsl:attribute name="fieldDisplayName"><xsl:value-of select="$fieldName"/></xsl:attribute>
				    <xsl:attribute name="fieldAction">replace</xsl:attribute>
				    <xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
				  	<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				  	<xsl:attribute name="fieldValueType">String</xsl:attribute>
				  	<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				  	<xsl:value-of select="text()"/>
				</field>
			</xsl:if>
			<xsl:if test="$fieldName = 'resolvedReleaseId'">
				<field>
					<xsl:attribute name="fieldName">resolvedReleaseId</xsl:attribute>
				    <xsl:attribute name="fieldDisplayName"><xsl:value-of select="$fieldName"/></xsl:attribute>
				    <xsl:attribute name="fieldAction">replace</xsl:attribute>
				    <xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
				  	<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				  	<xsl:attribute name="fieldValueType">String</xsl:attribute>
				  	<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				  	<xsl:value-of select="text()"/>
				</field>
			</xsl:if>
			<xsl:if test="$fieldName = 'estimatedHours'">
				<field>
					<xsl:attribute name="fieldName">estimatedHours</xsl:attribute>
				    <xsl:attribute name="fieldDisplayName"><xsl:value-of select="$fieldName"/></xsl:attribute>
				    <xsl:attribute name="fieldAction">replace</xsl:attribute>
				    <xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
				  	<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				  	<xsl:attribute name="fieldValueType">Integer</xsl:attribute>
				  	<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				  	<xsl:value-of select="text()"/>
				</field>
			</xsl:if>
			<xsl:if test="$fieldName = 'actualHours'">
				<field>
					<xsl:attribute name="fieldName">actualHours</xsl:attribute>
				    <xsl:attribute name="fieldDisplayName"><xsl:value-of select="$fieldName"/></xsl:attribute>
				    <xsl:attribute name="fieldAction">replace</xsl:attribute>
				    <xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
				  	<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				  	<xsl:attribute name="fieldValueType">Integer</xsl:attribute>
				  	<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				  	<xsl:value-of select="text()"/>
				</field>
			</xsl:if>
			<xsl:if test="$fieldName = 'dbid'">
				<field>
					<xsl:attribute name="fieldName">dbid</xsl:attribute>
				    <xsl:attribute name="fieldDisplayName"><xsl:value-of select="$fieldName"/></xsl:attribute>
				    <xsl:attribute name="fieldAction">replace</xsl:attribute>
				    <xsl:attribute name="fieldType">flexField</xsl:attribute>
				  	<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				  	<xsl:attribute name="fieldValueType">String</xsl:attribute>
				  	<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				  	<xsl:value-of select="text()"/>
				</field>
			</xsl:if>
			<xsl:if test="$fieldName = 'id'">
				<field>
					<xsl:attribute name="fieldName">id</xsl:attribute>
				    <xsl:attribute name="fieldDisplayName"><xsl:value-of select="$fieldName"/></xsl:attribute>
				    <xsl:attribute name="fieldAction">replace</xsl:attribute>
				    <xsl:attribute name="fieldType">flexField</xsl:attribute>
				  	<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				  	<xsl:attribute name="fieldValueType">String</xsl:attribute>
				  	<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				  	<xsl:value-of select="text()"/>
				</field>
			</xsl:if>
			<xsl:if test="$fieldName = 'is_active'">
				<field>
					<xsl:attribute name="fieldName">is_active</xsl:attribute>
				    <xsl:attribute name="fieldDisplayName"><xsl:value-of select="$fieldName"/></xsl:attribute>
				    <xsl:attribute name="fieldAction">replace</xsl:attribute>
				    <xsl:attribute name="fieldType">flexField</xsl:attribute>
				  	<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				  	<xsl:attribute name="fieldValueType">String</xsl:attribute>
				  	<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				  	<xsl:value-of select="text()"/>
				</field>
			</xsl:if>
			<xsl:if test="$fieldName = 'is_duplicate'">
				<field>
					<xsl:attribute name="fieldName">is_duplicate</xsl:attribute>
				    <xsl:attribute name="fieldDisplayName"><xsl:value-of select="$fieldName"/></xsl:attribute>
				    <xsl:attribute name="fieldAction">replace</xsl:attribute>
				    <xsl:attribute name="fieldType">flexField</xsl:attribute>
				  	<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				  	<xsl:attribute name="fieldValueType">String</xsl:attribute>
				  	<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				  	<xsl:value-of select="text()"/>
				</field>
			</xsl:if>
			<xsl:if test="$fieldName = 'locked_by'">
				<field>
					<xsl:attribute name="fieldName">locked_by</xsl:attribute>
				    <xsl:attribute name="fieldDisplayName"><xsl:value-of select="$fieldName"/></xsl:attribute>
				    <xsl:attribute name="fieldAction">replace</xsl:attribute>
				    <xsl:attribute name="fieldType">flexField</xsl:attribute>
				  	<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				  	<xsl:attribute name="fieldValueType">String</xsl:attribute>
				  	<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				  	<xsl:value-of select="text()"/>
				</field>
			</xsl:if>
			<xsl:if test="$fieldName = 'lock_version'">
				<field>
					<xsl:attribute name="fieldName">lock_version</xsl:attribute>
				    <xsl:attribute name="fieldDisplayName"><xsl:value-of select="$fieldName"/></xsl:attribute>
				    <xsl:attribute name="fieldAction">replace</xsl:attribute>
				    <xsl:attribute name="fieldType">flexField</xsl:attribute>
				  	<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				  	<xsl:attribute name="fieldValueType">String</xsl:attribute>
				  	<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				  	<xsl:value-of select="text()"/>
				</field>
			</xsl:if>
			<xsl:if test="$fieldName = 'old_id'">
				<field>
					<xsl:attribute name="fieldName">old_id</xsl:attribute>
				    <xsl:attribute name="fieldDisplayName"><xsl:value-of select="$fieldName"/></xsl:attribute>
				    <xsl:attribute name="fieldAction">replace</xsl:attribute>
				    <xsl:attribute name="fieldType">flexField</xsl:attribute>
				  	<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				  	<xsl:attribute name="fieldValueType">String</xsl:attribute>
				  	<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				  	<xsl:value-of select="text()"/>
				</field>
			</xsl:if>
			<xsl:if test="$fieldName = 'record_type'">
				<field>
					<xsl:attribute name="fieldName">record_type</xsl:attribute>
				    <xsl:attribute name="fieldDisplayName"><xsl:value-of select="$fieldName"/></xsl:attribute>
				    <xsl:attribute name="fieldAction">replace</xsl:attribute>
				    <xsl:attribute name="fieldType">flexField</xsl:attribute>
				  	<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				  	<xsl:attribute name="fieldValueType">String</xsl:attribute>
				  	<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				  	<xsl:value-of select="text()"/>
				</field>
			</xsl:if>
			<xsl:if test="$fieldName = 'Resolution'">
				<field>
					<xsl:attribute name="fieldName">Resolution</xsl:attribute>
				    <xsl:attribute name="fieldDisplayName"><xsl:value-of select="$fieldName"/></xsl:attribute>
				    <xsl:attribute name="fieldAction">replace</xsl:attribute>
				    <xsl:attribute name="fieldType">flexField</xsl:attribute>
				  	<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				  	<xsl:attribute name="fieldValueType">String</xsl:attribute>
				  	<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				  	<xsl:value-of select="text()"/>
				</field>
			</xsl:if>
			<xsl:if test="$fieldName = 'Resolution_Statetype'">
				<field>
					<xsl:attribute name="fieldName">Resolution_Statetype</xsl:attribute>
				    <xsl:attribute name="fieldDisplayName"><xsl:value-of select="$fieldName"/></xsl:attribute>
				    <xsl:attribute name="fieldAction">replace</xsl:attribute>
				    <xsl:attribute name="fieldType">flexField</xsl:attribute>
				  	<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				  	<xsl:attribute name="fieldValueType">String</xsl:attribute>
				  	<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				  	<xsl:value-of select="text()"/>
				</field>
			</xsl:if>
			<xsl:if test="$fieldName = 'severity'">
				<field>
					<xsl:attribute name="fieldName">severity</xsl:attribute>
				    <xsl:attribute name="fieldDisplayName"><xsl:value-of select="$fieldName"/></xsl:attribute>
				    <xsl:attribute name="fieldAction">replace</xsl:attribute>
				    <xsl:attribute name="fieldType">flexField</xsl:attribute>
				  	<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				  	<xsl:attribute name="fieldValueType">String</xsl:attribute>
				  	<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				  	<xsl:value-of select="text()"/>
				</field>
			</xsl:if>
			<xsl:if test="$fieldName = 'Submit_Date'">
				<field>
					<xsl:attribute name="fieldName">Submit_Date</xsl:attribute>
				    <xsl:attribute name="fieldDisplayName"><xsl:value-of select="$fieldName"/></xsl:attribute>
				    <xsl:attribute name="fieldAction">replace</xsl:attribute>
				    <xsl:attribute name="fieldType">flexField</xsl:attribute>
				  	<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				  	<xsl:attribute name="fieldValueType">String</xsl:attribute>
				  	<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				  	<xsl:value-of select="text()"/>
				</field>
			</xsl:if>
			<xsl:if test="$fieldName = 'Submitter'">
				<field>
					<xsl:attribute name="fieldName">Submitter</xsl:attribute>
				    <xsl:attribute name="fieldDisplayName"><xsl:value-of select="$fieldName"/></xsl:attribute>
				    <xsl:attribute name="fieldAction">replace</xsl:attribute>
				    <xsl:attribute name="fieldType">flexField</xsl:attribute>
				  	<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				  	<xsl:attribute name="fieldValueType">String</xsl:attribute>
				  	<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				  	<xsl:value-of select="text()"/>
				</field>
			</xsl:if>
	</xsl:template>
</xsl:stylesheet>