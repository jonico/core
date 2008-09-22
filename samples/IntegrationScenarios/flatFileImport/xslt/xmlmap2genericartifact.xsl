<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ccf="http://ccf.open.collab.net/GenericArtifactV1.0" xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xsl xs">
	<xsl:template match="/node()" priority="2">
		<artifact xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" artifactAction="create" artifactMode="complete" artifactType="plainArtifact" sourceArtifactLastModifiedDate="unknown" targetArtifactLastModifiedDate="unknown" artifactLastReadTransactionId="unknown" sourceArtifactVersion="unknown" targetArtifactVersion="unknown" conflictResolutionPriority="unknown" sourceArtifactId="CSV-1" sourceRepositoryId="input.txt" sourceRepositoryKind="CSVClearQuestFile" sourceSystemId="CSVFile" sourceSystemKind="Filesystem" targetArtifactId="NEW" targetRepositoryId="tracker1001" targetRepositoryKind="TRACKER" targetSystemId="cu011" targetSystemKind="CSFE 5.0" transactionId="0" errorCode="ok" includesFieldMetaData="false">
			<!-- Constant fields -->
			<!--<field>
				<xsl:attribute name="fieldName">customer</xsl:attribute>
				<xsl:attribute name="fieldDisplayName">customer</xsl:attribute>
				<xsl:attribute name="fieldAction">replace</xsl:attribute>
				<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
				<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				<xsl:attribute name="fieldValueType">String</xsl:attribute>
				<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
			</field>-->
			<field><xsl:attribute name="fieldName">description</xsl:attribute><xsl:attribute name="fieldDisplayName">description</xsl:attribute><xsl:attribute name="fieldAction">replace</xsl:attribute><xsl:attribute name="fieldType">mandatoryField</xsl:attribute><xsl:attribute name="fieldValueHasChanged">true</xsl:attribute><xsl:attribute name="fieldValueType">String</xsl:attribute><xsl:attribute name="fieldValueIsNull">false</xsl:attribute>The artifact was read in by a CSV file</field>
			<!--<field>
				<xsl:attribute name="fieldName">category</xsl:attribute>
				<xsl:attribute name="fieldDisplayName">category</xsl:attribute>
				<xsl:attribute name="fieldAction">replace</xsl:attribute>
				<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
				<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				<xsl:attribute name="fieldValueType">String</xsl:attribute>
				<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
			</field>
			<field>
				<xsl:attribute name="fieldName">group</xsl:attribute>
				<xsl:attribute name="fieldDisplayName">group</xsl:attribute>
				<xsl:attribute name="fieldAction">replace</xsl:attribute>
				<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
				<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				<xsl:attribute name="fieldValueType">String</xsl:attribute>
				<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
			</field>-->
			<field>
				<xsl:attribute name="fieldName">estimatedHours</xsl:attribute>
				<xsl:attribute name="fieldDisplayName">estimatedHours</xsl:attribute>
				<xsl:attribute name="fieldAction">replace</xsl:attribute>
				<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
				<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				<xsl:attribute name="fieldValueType">Integer</xsl:attribute>
				<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				<xsl:value-of select="50"/>
			</field>
			<field>
				<xsl:attribute name="fieldName">actualHours</xsl:attribute>
				<xsl:attribute name="fieldDisplayName">actualHours</xsl:attribute>
				<xsl:attribute name="fieldAction">replace</xsl:attribute>
				<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
				<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				<xsl:attribute name="fieldValueType">Integer</xsl:attribute>
				<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				<xsl:value-of select="25"/>
			</field>
			<xsl:apply-templates/>
		</artifact>
	</xsl:template>
	<!-- Dynamic fields -->
	<xsl:template match="node()" priority="1">
		<xsl:variable name="fieldName" as="xs:string">
			<xsl:value-of select="local-name()"/>
		</xsl:variable>
		<xsl:if test="$fieldName = 'Headline'">
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
		<xsl:if test="$fieldName = 'State'">
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
		<xsl:if test="$fieldName = 'Priority'">
			<xsl:variable name="priorityValue" as="xs:string">
				<xsl:value-of select="."/>
			</xsl:variable>
			<field>
				<xsl:attribute name="fieldName">priority</xsl:attribute>
				<xsl:attribute name="fieldDisplayName"><xsl:value-of select="$fieldName"/></xsl:attribute>
				<xsl:attribute name="fieldAction">replace</xsl:attribute>
				<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
				<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				<xsl:attribute name="fieldValueType">Integer</xsl:attribute>
				<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				<xsl:choose>
					<xsl:when test="$priorityValue = '3-Normal Queue'">
						<xsl:text>3</xsl:text>
					</xsl:when>
					<xsl:when test="$priorityValue = '2-Give High Attention'">
						<xsl:text>2</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>1</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</field>
		</xsl:if>
		<xsl:if test="$fieldName = 'Owner'">
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
		<!--<xsl:if test="$fieldName = 'reportedReleaseId'">
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
		</xsl:if>-->
		<!--<xsl:if test="$fieldName = 'resolvedReleaseId'">
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
		</xsl:if>-->
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
		<xsl:if test="$fieldName = 'artifactId'">
			<field>
				<xsl:attribute name="fieldName">id</xsl:attribute>
				<xsl:attribute name="fieldDisplayName"><xsl:value-of select="$fieldName"/></xsl:attribute>
				<xsl:attribute name="fieldAction">replace</xsl:attribute>
				<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
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
		<xsl:if test="$fieldName = 'Severity'">
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
		<xsl:if test="$fieldName = 'version'">
			<field>
				<xsl:attribute name="fieldName">version</xsl:attribute>
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
				<xsl:attribute name="fieldValueType">User</xsl:attribute>
				<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				<xsl:value-of select="text()"/>
			</field>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
