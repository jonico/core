<?xml version="1.0"?>
<!-- Copyright 2009 CollabNet, Inc. ("CollabNet") Licensed under the Apache 
	License, Version 2.0 (the "License"); you may not use this file except in 
	compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 
	Unless required by applicable law or agreed to in writing, software distributed 
	under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES 
	OR CONDITIONS OF ANY KIND, either express or implied. See the License for 
	the specific language governing permissions and limitations under the License. 
	ClearQuest -> Teamforge Basic fields -->
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ccf="http://ccf.open.collab.net/GenericArtifactV1.0"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:stringutil="xalan://com.collabnet.ccf.core.utils.GATransformerUtil"
	exclude-result-prefixes="xsl xs ccf stringutil" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.w3.org/1999/XSL/Transform http://www.w3.org/2007/schema-for-xslt20.xsd">
	<xsl:template match='/ccf:artifact[@artifactType = "plainArtifact"]'>
		<artifact xmlns="http://ccf.open.collab.net/GenericArtifactV1.0">
			<xsl:copy-of select="@*" />
			<xsl:apply-templates />
			<!-- <xsl:if test='not(ccf:field[@fieldName="title"])'> -->
			<!-- <field> -->
			<!-- <xsl:attribute name="fieldName">title</xsl:attribute> -->
			<!-- <xsl:attribute name="fieldAction">replace</xsl:attribute> -->
			<!-- <xsl:attribute name="fieldType">mandatoryField</xsl:attribute> -->
			<!-- <xsl:attribute name="fieldValueHasChanged">true</xsl:attribute> -->
			<!-- <xsl:attribute name="fieldValueType">String</xsl:attribute> -->
			<!-- <xsl:attribute name="fieldValueIsNull">false</xsl:attribute> -->
			<!-- <xsl:text>(no headline in CQ)</xsl:text> -->
			<!-- </field> -->
			<!-- </xsl:if> -->
			<!-- <xsl:if test='not(ccf:field[@fieldName="description"])'> -->
			<!-- <field> -->
			<!-- <xsl:attribute name="fieldName">description</xsl:attribute> -->
			<!-- <xsl:attribute name="fieldAction">replace</xsl:attribute> -->
			<!-- <xsl:attribute name="fieldType">mandatoryField</xsl:attribute> -->
			<!-- <xsl:attribute name="fieldValueHasChanged">true</xsl:attribute> -->
			<!-- <xsl:attribute name="fieldValueType">String</xsl:attribute> -->
			<!-- <xsl:attribute name="fieldValueIsNull">false</xsl:attribute> -->
			<!-- <xsl:text>(no description in CQ)</xsl:text> -->
			<!-- </field> -->
			<!-- </xsl:if> -->
			<!-- <xsl:if test='not(ccf:field[@fieldName="status"])'> -->
			<!-- <field> -->
			<!-- <xsl:attribute name="fieldName">status</xsl:attribute> -->
			<!-- <xsl:attribute name="fieldAction">replace</xsl:attribute> -->
			<!-- <xsl:attribute name="fieldType">mandatoryField</xsl:attribute> -->
			<!-- <xsl:attribute name="fieldValueHasChanged">true</xsl:attribute> -->
			<!-- <xsl:attribute name="fieldValueType">String</xsl:attribute> -->
			<!-- <xsl:attribute name="fieldValueIsNull">false</xsl:attribute> -->
			<!-- <xsl:text>Open</xsl:text> -->
			<!-- </field> -->
			<!-- </xsl:if> -->
			<!-- uncomment the folllowing line fail before XML conversion, the payload 
				will be in the log file -->
<!-- 			<xsl:wrong>yeah</xsl:wrong> -->
			<!-- uncomment the folllowing 2 field blocks to create a failed shipment 
				after conversion, i.e. pazload is available in eclipse&UI -->
<!-- 			<field> -->
<!-- 				<xsl:attribute name="fieldName">status</xsl:attribute> -->
<!-- 				<xsl:attribute name="fieldAction">replace</xsl:attribute> -->
<!-- 				<xsl:attribute name="fieldType">mandatoryField</xsl:attribute> -->
<!-- 				<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute> -->
<!-- 				<xsl:attribute name="fieldValueType">String</xsl:attribute> -->
<!-- 				<xsl:attribute name="fieldValueIsNull">false</xsl:attribute> -->
<!-- 				<xsl:text>THE STATUS DOES NOT EXIST</xsl:text> -->
<!-- 			</field> -->
<!-- 			<field> -->
<!-- 				<xsl:attribute name="fieldName">status</xsl:attribute> -->
<!-- 				<xsl:attribute name="fieldAction">replace</xsl:attribute> -->
<!-- 				<xsl:attribute name="fieldType">mandatoryField</xsl:attribute> -->
<!-- 				<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute> -->
<!-- 				<xsl:attribute name="fieldValueType">String</xsl:attribute> -->
<!-- 				<xsl:attribute name="fieldValueIsNull">false</xsl:attribute> -->
<!-- 				<xsl:text>THE STATUS DOES NOT EXIST EITHER</xsl:text> -->
<!-- 			</field> -->

		</artifact>
	</xsl:template>

	<xsl:template match="/ccf:artifact[@artifactType = 'attachment']">
		<xsl:copy-of select="." />
	</xsl:template>

	<!-- ID field of clearquest goes in flexfield -->
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
	<xsl:template match='ccf:field[@fieldName="State"]'>
		<xsl:variable name="statusValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">status</xsl:attribute>
			<!-- <xsl:value-of select="." /> -->
			<xsl:if test="$statusValue=''">
				<xsl:text>Open</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue='Submitted'">
				<xsl:text>Open</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue='UnderValidation'">
				<xsl:text>In Progress</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue='Verified'">
				<xsl:text>Fixed</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue='UnderAnalysis'">
				<xsl:text>Pending</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue='Closed'">
				<xsl:text>Closed</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue='UnderCorrection'">
				<xsl:text>Re-opened (not fixed)</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue='Postponed'">
				<xsl:text>Cannot reproduce</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue='Postponed'">
				<xsl:text>Not a bug</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue='Duplicate'">
				<xsl:text>Duplicate</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>

	<!-- Headline in cq, Title in tf -->
	<xsl:template match='ccf:field[@fieldName="Headline"]'>
		<xsl:variable name="titleValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">title</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:if test="$titleValue=''">
				<xsl:text>(no headline in CQ)</xsl:text>
			</xsl:if>
			<xsl:if test="$titleValue!=''">
				<xsl:value-of select="." />
			</xsl:if>
		</field>
	</xsl:template>

	<!-- Description in cq and tf -->
	<xsl:template match='ccf:field[@fieldName="Description"]'>
		<xsl:variable name="descrValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">description</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:choose>
				<xsl:when test="@fieldValueType='HTMLString'">
					<xsl:value-of select="stringutil:stripHTML(string(.))" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:if test="$descrValue!=''">
						<xsl:value-of select="." />
					</xsl:if>
					<xsl:if test="$descrValue=''">
						<xsl:text>(no description in CQ)</xsl:text>
					</xsl:if>
				</xsl:otherwise>
			</xsl:choose>
		</field>
	</xsl:template>

	<xsl:template match="text()" />
</xsl:stylesheet>