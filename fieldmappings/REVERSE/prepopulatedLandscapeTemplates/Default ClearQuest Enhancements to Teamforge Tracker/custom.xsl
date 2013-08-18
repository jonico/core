<?xml version="1.0"?>

<!-- ClearQuest Enhancements ==> Teamforge Tracker -->

<!-- Copyright 2009 CollabNet, Inc. ("CollabNet") Licensed under the Apache 
	License, Version 2.0 (the "License"); you may not use this file except in 
	compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 
	Unless required by applicable law or agreed to in writing, software distributed 
	under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES 
	OR CONDITIONS OF ANY KIND, either express or implied. See the License for 
	the specific language governing permissions and limitations under the License. -->	
	 
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ccf="http://ccf.open.collab.net/GenericArtifactV1.0"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:stringutil="xalan://com.collabnet.ccf.core.utils.GATransformerUtil"
	exclude-result-prefixes="xsl xs ccf stringutil" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.w3.org/1999/XSL/Transform http://www.w3.org/2007/schema-for-xslt20.xsd">
	<xsl:template match='/ccf:artifact[@artifactType = "plainArtifact"]'>
		<artifact xmlns="http://ccf.open.collab.net/GenericArtifactV1.0">
			<xsl:copy-of select="@*" />
			<xsl:variable name="statusFieldValue" as="xs:string" select="/ccf:artifact/ccf:field[@fieldName='State']" />
			<xsl:if test="	$statusFieldValue = 'Closed' or
							$statusFieldValue = 'Submitted' or 
							$statusFieldValue = 'Corrected' or 
							$statusFieldValue = 'UnderVerification' or 
							$statusFieldValue = 'Verified' or
							$statusFieldValue = 'UnderValidation' or
							$statusFieldValue = 'Validated' or
							$statusFieldValue = 'Duplicate' or
							$statusFieldValue = 'Opened' or
							$statusFieldValue = 'UnderMonitoring'">
        		<xsl:attribute name="artifactAction">ignore</xsl:attribute>
			</xsl:if>
			<xsl:apply-templates />
		</artifact>
	</xsl:template>

	<xsl:template match="/ccf:artifact[@artifactType = 'attachment']">
		<xsl:copy-of select="." />
	</xsl:template>

	<xsl:template match='ccf:field[@fieldName="id"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">RCQ-ID</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:attribute name="fieldValueType">String</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>

	<xsl:template match='ccf:field[@fieldName="Analysis"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Analysis</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:attribute name="fieldValueType">String</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>

	<xsl:template match='ccf:field[@fieldName="Component"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Component</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:attribute name="fieldValueType">String</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>

	<xsl:template match='ccf:field[@fieldName="Description"]'>
		<xsl:variable name="descrValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">description</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:if test="$descrValue!=''">
				<xsl:value-of select="." />
			</xsl:if>
			<xsl:if test="$descrValue=''">
				<xsl:text>(no description in CQ)</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>

	<xsl:template match='ccf:field[@fieldName="EstimatedEffort"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">estimatedHours</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:attribute name="fieldValueType">String</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>

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

	<xsl:template match='ccf:field[@fieldName="Notes_Log"]'>
		<xsl:variable name="notesEntryValue" select="." />
		<xsl:if test="$notesEntryValue!=''">
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
		</xsl:if>
	</xsl:template>

	<xsl:template match='ccf:field[@fieldName="OwnerFullName"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Owner Full Name</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:attribute name="fieldValueType">String</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>

	<xsl:template match='ccf:field[@fieldName="Product"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Product</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:attribute name="fieldValueType">String</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>

	<xsl:template match='ccf:field[@fieldName="ProductConfiguration"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Product Configuration</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:attribute name="fieldValueType">String</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>

	<xsl:template match='ccf:field[@fieldName="Project"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Project</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:attribute name="fieldValueType">String</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>

	<xsl:template match='ccf:field[@fieldName="Severity"]'>
		<xsl:variable name="severityValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">priority</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:choose>
				<xsl:when test="$severityValue = '1-Critical'">
					<xsl:text>1</xsl:text>
				</xsl:when>
				<xsl:when test="$severityValue = '2-Major'">
					<xsl:text>2</xsl:text>
				</xsl:when>
				<xsl:when test="$severityValue = '3-Average'">
					<xsl:text>3</xsl:text>
				</xsl:when>
				<xsl:when test="$severityValue = '4-Minor'">
					<xsl:text>4</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>5</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</field>
	</xsl:template>

	<xsl:template match='ccf:field[@fieldName="State"]'>
		<xsl:variable name="statusValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">status</xsl:attribute>
			<xsl:choose>
				<xsl:when test="$statusValue=''">
					<xsl:text>New</xsl:text>
				</xsl:when>
				<xsl:when test="$statusValue='UnderAnalysis'">
					<xsl:text>Under Analysis</xsl:text>
				</xsl:when>
				<xsl:when test="$statusValue='Analyzed'">
					<xsl:text>Analyzed</xsl:text>
				</xsl:when>
				<xsl:when test="$statusValue='UnderCorrection'">
					<xsl:text>Under Correction</xsl:text>
				</xsl:when>
				<xsl:when test="$statusValue='Postponed'">
					<xsl:text>Postponed</xsl:text>
				</xsl:when>
				<xsl:when test="$statusValue='Closed'">
					<xsl:text>Closed</xsl:text>
				</xsl:when>
			</xsl:choose>
<!-- 			<xsl:if test="$statusValue='Submitted'"> -->
<!-- 				<xsl:text>Submitted</xsl:text> -->
<!-- 			</xsl:if> -->
<!-- 			<xsl:if test="$statusValue='Corrected'"> -->
<!-- 				<xsl:text>Corrected</xsl:text> -->
<!-- 			</xsl:if> -->
<!-- 			<xsl:if test="$statusValue='UnderVerification'"> -->
<!-- 				<xsl:text>Under Verification</xsl:text> -->
<!-- 			</xsl:if> -->
<!-- 			<xsl:if test="$statusValue='Verified'"> -->
<!-- 				<xsl:text>Verified</xsl:text> -->
<!-- 			</xsl:if> -->
<!-- 			<xsl:if test="$statusValue='UnderValidation'"> -->
<!-- 				<xsl:text>Under Validation</xsl:text> -->
<!-- 			</xsl:if> -->
<!-- 			<xsl:if test="$statusValue='Validated'"> -->
<!-- 				<xsl:text>Validated</xsl:text> -->
<!-- 			</xsl:if> -->
<!-- 			<xsl:if test="$statusValue='Duplicate'"> -->
<!-- 				<xsl:text>Closed</xsl:text> -->
<!-- 			</xsl:if> -->
<!-- 			<xsl:if test="$statusValue='Opened'"> -->
<!-- 				<xsl:text>Opened</xsl:text> -->
<!-- 			</xsl:if> -->
<!-- 			<xsl:if test="$statusValue='UnderMonitoring'"> -->
<!-- 				<xsl:text>Under Monitoring</xsl:text> -->
<!-- 			</xsl:if> -->
		</field>
	</xsl:template>

	<xsl:template match='ccf:field[@fieldName="Submit_Date"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">CQ Submit Date String</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:attribute name="fieldValueType">String</xsl:attribute>
			<xsl:value-of select="." />
		</field>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">CQ Submit Date</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:attribute name="fieldValueType">DateTime</xsl:attribute>
			<xsl:value-of select="." />
<!-- 			<xsl:value-of select="rcqdateutil:convertDateString(string(.),'')" /> -->
		</field>
	</xsl:template>

	<xsl:template match='ccf:field[@fieldName="Submitter"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">CQ Submitter</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:attribute name="fieldValueType">String</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>

	<!-- ClearQuest ID -->
	<xsl:template match='ccf:field[@fieldName="id"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">RCQ-ID</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:attribute name="fieldValueType">String</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>

	<xsl:template match="text()" />
</xsl:stylesheet>