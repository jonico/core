<?xml version="1.0"?>
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
	exclude-result-prefixes="xsl xs ccf stringutil" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.w3.org/1999/XSL/Transform http://www.w3.org/2007/schema-for-xslt20.xsd">
	
	<xsl:template match='/ccf:artifact[@artifactType = "plainArtifact"]'>
		<artifact xmlns="http://ccf.open.collab.net/GenericArtifactV1.0">
			<xsl:copy-of select="@*" />
			<xsl:apply-templates />
		</artifact>
	</xsl:template>
	
	<xsl:template match='ccf:field[@fieldName="Name"]'>
		<xsl:variable name="titleValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">title</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:choose>
				<xsl:when test="$titleValue != ''">
					<xsl:value-of select="." />
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>No Title</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</field>
	</xsl:template>

	<xsl:template match='ccf:field[@fieldName="Description"]'>
		<xsl:variable name="descriptionValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">description</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:choose>
				<xsl:when test="$descriptionValue != ''">
					<xsl:value-of select="." />
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>No Description</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</field>
	</xsl:template>
	
	<xsl:template match='ccf:field[@fieldName="Parent Package"]'>
		<xsl:variable name="riskValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Parent Dependency</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
		</field>
	</xsl:template>
	
	<xsl:template match='ccf:field[@fieldName="Status"]'>
		<xsl:copy-of select="@*" />
		<xsl:attribute name="fieldName">status</xsl:attribute>
		<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
		<xsl:variable name="statusValue" as="xs:string" select="." />
			<xsl:if test="$statusValue = '&lt;no entry&gt;'">
				<xsl:text>None</xsl:text>
			</xsl:if>
	</xsl:template>	

</xsl:stylesheet>