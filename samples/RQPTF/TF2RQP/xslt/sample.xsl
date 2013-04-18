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
	xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:stringutil="xalan://com.collabnet.ccf.core.utils.StringUtils"
	exclude-result-prefixes="xsl xs ccf stringutil" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.w3.org/1999/XSL/Transform http://www.w3.org/2007/schema-for-xslt20.xsd">
	<xsl:template match='/ccf:artifact[@artifactType = "plainArtifact"]'>
		<artifact xmlns="http://ccf.open.collab.net/GenericArtifactV1.0">
			<xsl:copy-of select="@*" />
			<xsl:apply-templates />
		</artifact>
	</xsl:template>
	<xsl:template match="/ccf:artifact[@artifactType = 'attachment']">
		<xsl:copy-of select="." />
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="title"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Name</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="description"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Text</xsl:attribute>
			<xsl:value-of select="string(.)"/>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="status"]'>
		<xsl:variable name="statusValue" as="xs:string" select="string(.)" />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Status</xsl:attribute>
			<xsl:if test="$statusValue = 'Pending'">
				<xsl:text>Pending</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Committed'">
				<xsl:text>Committed</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Done'">
				<xsl:text>Done</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Accepted'">
				<xsl:text>Accepted</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Rejected'">
				<xsl:text>Rejected</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="category"]'>
		<xsl:variable name="typeValue" as="xs:string" select="."/>
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">Category</xsl:attribute>
			<xsl:if test="$typeValue = '&lt;no entry&gt;'">
				<xsl:text>None</xsl:text>
			</xsl:if>
			<xsl:if test="$typeValue = 'User Story'">
				<xsl:text>User Story</xsl:text>
			</xsl:if>
			<xsl:if test="$typeValue = 'Technical Story'">
				<xsl:text>Technical Story</xsl:text>
			</xsl:if>
			<xsl:if test="$typeValue = 'Improvement Story'">
				<xsl:text>Improvement Story</xsl:text>
			</xsl:if>
			<xsl:if test="$typeValue = 'Spike'">
				<xsl:text>Spike</xsl:text>
			</xsl:if>
			<xsl:if test="$typeValue = ''">
				<xsl:text>None</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="id"]'>
		<xsl:variable name="idValue" as="xs:string" select="."/>
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">TeamForge link</xsl:attribute>
			<xsl:value-of select= 'concat("http://ctfdemo.bvision.com/sf/go/", $idValue)'/>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Comment Text"]'>
		<xsl:variable name="typeValue" as="xs:string" select="."/>
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">Comments</xsl:attribute>
			<xsl:choose>
				<xsl:when test="$typeValue != ''"><xsl:value-of select="." /></xsl:when>
				<xsl:otherwise>None</xsl:otherwise>
			</xsl:choose>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Business Value"]'>
	<xsl:variable name="value" as="xs:string" select="."/>
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">Business Value</xsl:attribute>
			<xsl:if test="$value = '1- Highest'">
				<xsl:text>1- Highest</xsl:text>
			</xsl:if>
			<xsl:if test="$value = '2- High'">
				<xsl:text>2- High</xsl:text>
			</xsl:if>
			<xsl:if test="$value = '3- Medium'">
				<xsl:text>3- Medium</xsl:text>
			</xsl:if>
			<xsl:if test="$value = '4- Low'">
				<xsl:text>4- Low</xsl:text>
			</xsl:if>
			<xsl:if test="$value = '5- Lowest'">
				<xsl:text>5- Lowest</xsl:text>
			</xsl:if>
			<xsl:if test="$value = ''">
				<xsl:text>None</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="priority"]'>
	<xsl:variable name="value" as="xs:string" select="."/>
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">Priority</xsl:attribute>
			<xsl:if test="$value = '1'">
				<xsl:text>1 - Highest</xsl:text>
			</xsl:if>
			<xsl:if test="$value = '2'">
				<xsl:text>2 - High</xsl:text>
			</xsl:if>
			<xsl:if test="$value = '3'">
				<xsl:text>3 - Medium</xsl:text>
			</xsl:if>
			<xsl:if test="$value = '4'">
				<xsl:text>4 - Low</xsl:text>
			</xsl:if>
			<xsl:if test="$value = '5'">
				<xsl:text>5 - Lowest</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Acceptance Criteria"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Acceptance Criteria</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Allocation"]'>
		<xsl:variable name="typeValue" as="xs:string" select="."/>
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">Allocation</xsl:attribute>
			<xsl:choose>
				<xsl:when test="$typeValue = 'Product A'"><xsl:value-of select="." /></xsl:when>
				<xsl:when test="$typeValue = 'Component 1'"><xsl:value-of select="." /></xsl:when>
				<xsl:otherwise>Not yet allocated</xsl:otherwise>
			</xsl:choose>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="resolvedReleaseId"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Implemented in Release</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
</xsl:stylesheet>
