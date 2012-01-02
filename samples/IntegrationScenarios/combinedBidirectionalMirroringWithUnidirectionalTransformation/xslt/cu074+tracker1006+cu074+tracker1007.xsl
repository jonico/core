<?xml version="1.0" encoding="UTF-8"?>
	<!--
		Copyright 2009 CollabNet, Inc. ("CollabNet") Licensed under the Apache
		License, Version 2.0 (the "License"); you may not use this file except
		in compliance with the License. You may obtain a copy of the License
		at http://www.apache.org/licenses/LICENSE-2.0 Unless required by
		applicable law or agreed to in writing, software distributed under the
		License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
		CONDITIONS OF ANY KIND, either express or implied. See the License for
		the specific language governing permissions and limitations under the
		License.
	-->
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ccf="http://ccf.open.collab.net/GenericArtifactV1.0"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:stringutil="xalan://com.collabnet.ccf.core.utils.GATransformerUtil"
	exclude-result-prefixes="xsl xs stringutil">
	<xsl:template match='/ccf:artifact[@artifactType = "plainArtifact"]'>
		<artifact xmlns="http://ccf.open.collab.net/GenericArtifactV1.0">
			<xsl:copy-of select="@*" />
			<xsl:apply-templates />
		</artifact>
	</xsl:template>
	<xsl:template match="/ccf:artifact[@artifactType = 'attachment']">
		<xsl:copy-of select="." />
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="estimatedHours"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">actualHours</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="actualHours"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">estimatedHours</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="title"]'>
		<xsl:copy-of select="." />
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="id"]'>
		<xsl:copy-of select="." />
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="description"]'>
		<xsl:copy-of select="." />
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="status"]'>
		<xsl:variable name="statusValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:if test="$statusValue = 'Open'">
				<xsl:text>Open</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Closed'">
				<xsl:text>Closed</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Pending'">
				<xsl:text>Fixed</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="category"]'>
		<xsl:variable name="typeValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:if test="$typeValue = 'Source Category A'">
				<xsl:text>Target Category A</xsl:text>
			</xsl:if>
			<xsl:if test="$typeValue = 'Source Category B'">
				<xsl:text>Target Category B</xsl:text>
			</xsl:if>
			<xsl:if test="$typeValue = 'Source Category C'">
				<xsl:text>Target Category C</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Detected By"]'>
		<xsl:variable name="detectedBy" as="xs:string">
			<xsl:value-of select="." />
		</xsl:variable>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">BG_DETECTED_BY</xsl:attribute>
			<xsl:if test="$detectedBy = 'connector'">
				<xsl:text>alex_qc</xsl:text>
			</xsl:if>
			<xsl:if test="$detectedBy = 'mseethar'">
				<xsl:text>cecil_qc</xsl:text>
			</xsl:if>
			<xsl:if test="$detectedBy = 'admin'">
				<xsl:text>admin</xsl:text>
			</xsl:if>
			<xsl:if test="$detectedBy = 'none'">
				<xsl:text>none</xsl:text>
			</xsl:if>
			<xsl:if test="$detectedBy = 'None'">
				<xsl:text>None</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="severity"]'>
		<xsl:copy-of select="." />
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Detected On"]'>
		<xsl:copy-of select="." />
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="priority"]'>
		<xsl:variable name="priorityValue" as="xs:string">
			<xsl:value-of select="." />
		</xsl:variable>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldValueType">String</xsl:attribute>
			<xsl:if test="$priorityValue = '1'">
				<xsl:text>5</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = '2'">
				<xsl:text>4</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = '3'">
				<xsl:text>3</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = '4'">
				<xsl:text>2</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = '5'">
				<xsl:text>1</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Test Date"]'>
		<xsl:copy-of select="." />
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="assignedTo"]'>
		<xsl:variable name="assignedTo" as="xs:string">
			<xsl:value-of select="." />
		</xsl:variable>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:if test="$assignedTo = 'connector'">
				<xsl:text>connector</xsl:text>
			</xsl:if>
			<xsl:if test="$assignedTo = 'venugopala'">
				<xsl:text>venugopala</xsl:text>
			</xsl:if>
			<xsl:if test="$assignedTo = 'mseethar'">
				<xsl:text>mseethar</xsl:text>
			</xsl:if>
			<xsl:if test="$assignedTo = 'mark'">
				<xsl:text>none</xsl:text>
			</xsl:if>
			<xsl:if test="$assignedTo = 'None'">
				<xsl:text>None</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template
		match='ccf:field[@fieldName="Text Part 2 (to be concatenated within transformation)"]'>
		<xsl:variable name="ccfText" as="xs:string">
			<xsl:text>CCF</xsl:text>
		</xsl:variable>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Constant Value Flex Text Field (should always contain CCF)</xsl:attribute>
			<xsl:value-of select="$ccfText"></xsl:value-of>
		</field>
	</xsl:template>
	<xsl:template
		match='ccf:field[@fieldName="Text Part 1 (to be concatenated in transformation)"]'>
		<xsl:variable name="firstText" as="xs:string"
			select="stringutil:trim(string(.))" />
		<xsl:variable name="secondText" as="xs:string"
			select="stringutil:trim(string(/ccf:artifact/ccf:field[@fieldName='Text Part 2 (to be concatenated within transformation)']))" />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Concatenated Flex Field Text (part 1 + part 2)</xsl:attribute>
			<xsl:value-of select="concat($firstText,' ',$secondText)" />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Comment Text"]'>
		<xsl:copy-of select='.' />
	</xsl:template>
	<xsl:template match="text()" />
</xsl:stylesheet>