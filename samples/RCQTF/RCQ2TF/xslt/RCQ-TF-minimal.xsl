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
		
		UPDATE: ClearQuest -> Teamforge Basic fields, NO status fields, NO attachments, NO history 
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
<!-- 	<xsl:template match="/ccf:artifact[@artifactType = 'attachment']"> -->
<!-- 		<xsl:copy-of select="." /> -->
<!-- 	</xsl:template> -->
	<!--  ID field of clearquest goes in flexfield -->
	<xsl:template match='ccf:field[@fieldName="id"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">RCQ-ID</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:attribute name="fieldValueType">String</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>

	<!-- Headline in cq, Title in tf -->
	<xsl:template match='ccf:field[@fieldName="Headline"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">title</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	
	<!-- Descrioption in cq and tf -->
	<xsl:template match='ccf:field[@fieldName="Description"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">description</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:choose>
				<xsl:when test="@fieldValueType='HTMLString'">
					<xsl:value-of select="stringutil:stripHTML(string(.))" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="." />
				</xsl:otherwise>
			</xsl:choose>
		</field>
	</xsl:template>

	
	<xsl:template match='ccf:field[@fieldName="Priority"]'>
		<xsl:variable name="prioValue" as="xs:string" select="." />
		<!-- '1-Resolve Immediately' ,  '2-Give High Attention' , '3-Normal Queue' , '4-Low Priority' -->
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">priority</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:choose>
				<xsl:when test="$prioValue = '1-Resolve Immediately'">
					<xsl:text>1</xsl:text>
				</xsl:when>
				<xsl:when test="$prioValue = '2-Give High Attention'">
					<xsl:text>2</xsl:text>
				</xsl:when>
				<xsl:when test="$prioValue = '3-Normal Queue'">
					<xsl:text>3</xsl:text>
				</xsl:when>
				<xsl:when test="$prioValue = '4-Low Priority'">
					<xsl:text>4</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>5</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</field>
	</xsl:template>


    <!-- these are db and tracker dependent -->
	<xsl:template match='ccf:field[@fieldName="State"]'>
		<xsl:variable name="statusValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">status</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:if test="$statusValue = 'Submitted'">
				<xsl:text>Open</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Assigned'">
				<xsl:text>In Progress</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Opened'">
				<xsl:text>Pending</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Resolved'">
				<xsl:text>Fixed</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Closed'">
				<xsl:text>Closed</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Duplicate'">
				<xsl:text>Duplicate</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Postponed'">
				<xsl:text>Not a bug</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	
	
	<xsl:template match='ccf:field[@fieldName="history"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Comment Text</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:choose>
				<xsl:when test="@fieldValueType='HTMLString'">
					<xsl:value-of select="stringutil:stripHTML(string(.))" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="." />
				</xsl:otherwise>
			</xsl:choose>
		</field>
	</xsl:template>

	<!-- append Notes to the comments target field -->
	<xsl:template match='ccf:field[@fieldName="Notes_Log"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Comment Text</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:attribute name="fieldAction">append</xsl:attribute>
Notes Log:
			<xsl:choose>
				<xsl:when test="@fieldValueType='HTMLString'">
					<xsl:value-of select="stringutil:stripHTML(string(.))" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="." />
				</xsl:otherwise>
			</xsl:choose>
		</field>
	</xsl:template>
	<xsl:template match="text()" />
</xsl:stylesheet>
