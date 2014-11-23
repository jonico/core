<?xml version="1.0"?>
	<!--
		Copyright 2011 CollabNet, Inc. ("CollabNet") Licensed under the Apache
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
			<xsl:attribute name="fieldName">System.Title</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="description"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Microsoft.VSTS.TCM.ReproSteps</xsl:attribute>
			<xsl:value-of select="stringutil:encodeHTMLToEntityReferences(string(.))"/>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="status"]'>
		<xsl:variable name="statusValue" as="xs:string" select="."/>
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">System.State</xsl:attribute>
			<xsl:choose>
				<xsl:when test="$statusValue ='Active'">Active</xsl:when>
				<xsl:when test="$statusValue ='Resolved'">Resolved</xsl:when>
				<xsl:when test="$statusValue ='Closed'">Closed</xsl:when>
		</xsl:choose>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="priority"]'>
		<xsl:variable name="priorityValue" as="xs:string" select="."/>
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">BG_PRIORITY</xsl:attribute>
			<xsl:if test="$priorityValue = '5'">
				<xsl:text>1-Low</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = '4'">
				<xsl:text>2-Medium</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = '3'">
				<xsl:text>3-High</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = '2'">
				<xsl:text>4-Very High</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = '1'">
				<xsl:text>5-Urgent</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
<!-- 	<xsl:template match='ccf:field[@fieldName="assignedTo"]'> -->
<!-- 		<xsl:variable name="assignedTo" as="xs:string" select="."/> -->
<!-- 		<field> -->
<!-- 			<xsl:copy-of select="@*"/> -->
<!-- 			<xsl:attribute name="fieldName">System.AssignTo</xsl:attribute> -->
<!-- 		</field> -->
<!-- 	</xsl:template> -->
	<xsl:template match='ccf:field[@fieldName="Comment Text"]'>
		<field><xsl:copy-of select="@*"/><xsl:attribute name="fieldName">System.History</xsl:attribute><xsl:value-of select="stringutil:encodeHTMLToEntityReferences(string(.))"/></field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="reason"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">System.Reason</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<!--<xsl:template match='ccf:field[@fieldName=""]'> -->
<!-- 	<field> -->
<!-- 		<xsl:copy-of select="@*" /> -->
<!-- 		<xsl:attribute name="fieldName">Microsoft.VSTS.Common.ActivatedBy</xsl:attribute> -->
<!-- 		<xsl:attribute name="fieldType"></xsl:attribute> -->
<!-- 		<xsl:value-of select="." /> -->
<!-- 	</field> -->
<!-- </xsl:template> -->
	<xsl:template match='ccf:field[@fieldName="Area"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">System.AreaPath</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Iteration"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">System.IterationPath</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Stack Rank"]'>
		<xsl:variable name="stackRankValue" as="xs:string" select="." /> 
			<field>
				<xsl:copy-of select="@*" />
				<xsl:attribute name="fieldName">Microsoft.VSTS.Common.StackRank</xsl:attribute>
				<xsl:attribute name="fieldType">flexField</xsl:attribute>
				<xsl:attribute name="fieldValueType">String</xsl:attribute>
				<xsl:choose>
					<xsl:when test="$stackRankValue = ''"><xsl:value-of select="." /></xsl:when>
					<xsl:when test="contains($stackRankValue, '.')"><xsl:value-of select="." /></xsl:when>
					<xsl:otherwise><xsl:value-of select="floor(.)" /></xsl:otherwise>
				</xsl:choose>
			</field>
	</xsl:template>
</xsl:stylesheet>
