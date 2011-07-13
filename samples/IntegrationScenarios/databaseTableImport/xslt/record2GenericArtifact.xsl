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


<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:ccf="http://ccf.open.collab.net/GenericArtifactV1.0"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	exclude-result-prefixes="xsl xs ccf stringutil">
	<xsl:template match='/node()' priority="2">
	<xsl:variable name="sourceArtifactId" as="xs:string"><xsl:value-of select="/record/ID" /></xsl:variable>
		<artifact xmlns="http://ccf.open.collab.net/GenericArtifactV1.0"
			artifactAction="create" artifactMode="complete"
			artifactType="plainArtifact"
			sourceArtifactLastModifiedDate="unknown"
			targetArtifactLastModifiedDate="unknown"
			artifactLastReadTransactionId="unknown" sourceArtifactVersion="unknown"
			targetArtifactVersion="unknown"
			conflictResolutionPriority="unknown"
			sourceRepositoryId="ARTIFACTS" sourceRepositoryKind="TABLE"
			sourceSystemId="localhost" sourceSystemKind="HSQL DB"
			targetArtifactId="NEW" targetRepositoryId="tracker1002"
			targetRepositoryKind="TRACKER" targetSystemId="cu011"
			targetSystemKind="SFEE4.4" transactionId="0"
			errorCode="ok" includesFieldMetaData="false"
			sourceSystemTimezone="unknown" targetSystemTimezone="unknown"
			sourceSystemEncoding="unknown" targetSystemEncoding="unknown">
			<xsl:attribute name="sourceArtifactId"><xsl:value-of select="$sourceArtifactId"/></xsl:attribute>
			<xsl:apply-templates />
		</artifact>
	</xsl:template>
	<xsl:template match="node()" priority="1">
			<xsl:variable name="fieldName" as="xs:string"><xsl:value-of select="local-name()" /></xsl:variable>
			<xsl:if test="$fieldName = 'TITLE'">
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
			<xsl:if test="$fieldName = 'DESCRIPTION'">
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
			<xsl:if test="$fieldName = 'STATUS'">
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
			<xsl:if test="$fieldName = 'CATEGORY'">
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
			<xsl:if test="$fieldName = 'PRIORITY'">
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
			<xsl:if test="$fieldName = 'ASSIGNED_TO'">
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
			<xsl:if test="$fieldName = 'GROUP_VAL'">
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
			<xsl:if test="$fieldName = 'CUSTOMER'">
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
			<xsl:if test="$fieldName = 'REPORTED_IN_RELEASE'">
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
			<xsl:if test="$fieldName = 'ESTIMATED_HOURS'">
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
			<xsl:if test="$fieldName = 'COMMENT_TEXT'">
				<field>
					<xsl:attribute name="fieldName">Comment Text</xsl:attribute>
				    <xsl:attribute name="fieldDisplayName"><xsl:value-of select="$fieldName"/></xsl:attribute>
				    <xsl:attribute name="fieldAction">replace</xsl:attribute>
				    <xsl:attribute name="fieldType">flexField</xsl:attribute>
				  	<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				  	<xsl:attribute name="fieldValueType">Integer</xsl:attribute>
				  	<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				  	<xsl:value-of select="text()"/>
				</field>
			</xsl:if>
	</xsl:template>
</xsl:stylesheet>