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
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ccf="http://ccf.open.collab.net/GenericArtifactV1.0"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xsl xs ccf stringutil">
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
		<xsl:if test="@fieldName = 'Comment Text'">
			<xsl:element name="COMMENT_TEXT"><xsl:value-of select="text()"/></xsl:element>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>