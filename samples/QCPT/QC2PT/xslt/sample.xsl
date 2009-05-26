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
	<xsl:template match='ccf:field[@fieldName="BG_BUG_ID"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">QC Defect ID</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="BG_SUMMARY"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Summary</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="BG_RESPONSIBLE"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Assigned To</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="BG_USER_13"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Assigned Tester</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="BG_DETECTED_BY"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Detected By</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="BG_PRIORITY"]'>
		<xsl:variable name="fieldValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Priority</xsl:attribute>
			<xsl:if test="$fieldValue = 'Low'">
				<xsl:text>Low</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'Medium'">
				<xsl:text>Medium</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'High'">
				<xsl:text>High</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'Urgent'">
				<xsl:text>Urgent</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="BG_DEV_COMMENTS"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">{urn:ws.tracker.collabnet.com}comment</xsl:attribute>
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
	<xsl:template match='ccf:field[@fieldName="BG_DESCRIPTION"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Details</xsl:attribute>
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
	<xsl:template match='ccf:field[@fieldName="BG_DETECTION_VERSION"]'>
		<xsl:variable name="fieldValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Detected in Version</xsl:attribute>
			<xsl:if test="$fieldValue = 'FNM 5.2'">
				<xsl:text>FNM 5.2</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'FNP 8.2'">
				<xsl:text>FNP 8.2</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="BG_REPRODUCIBLE"]'>
		<xsl:variable name="fieldValue" as="xs:string">
			<xsl:value-of select="." />
		</xsl:variable>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Reproducible</xsl:attribute>
			<xsl:if test="$fieldValue = '?'">
				<xsl:text>?</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'N'">
				<xsl:text>No</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'Y'">
				<xsl:text>Yes</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="BG_SEVERITY"]'>
		<xsl:variable name="fieldValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Severity</xsl:attribute>
			<xsl:if test="$fieldValue = 'Critical'">
				<xsl:text>Critical</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'Major'">
				<xsl:text>Major</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'Minor'">
				<xsl:text>Minor</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="BG_STATUS"]'>
		<xsl:variable name="fieldValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Status</xsl:attribute>
			<xsl:if test="$fieldValue = 'Answered'">
				<xsl:text>Answered</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'Assigned'">
				<xsl:text>Assigned</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'Closed'">
				<xsl:text>Closed</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'Fixed'">
				<xsl:text>Fixed</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'Monitor'">
				<xsl:text>Monitor</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'New'">
				<xsl:text>New</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'Open'">
				<xsl:text>Open</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'Pending Test'">
				<xsl:text>Pending Test</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="BG_USER_08"]'>
		<xsl:variable name="fieldValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Area</xsl:attribute>
			<xsl:if test="$fieldValue = 'FindBugs'">
				<xsl:text>FindBugs</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'FNP-CC'">
				<xsl:text>FNP-CC</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'FNP-CG'">
				<xsl:text>FNP-CG</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'FNP-CV'">
				<xsl:text>FNP-CV</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'GUI'">
				<xsl:text>GUI</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'JUnit'">
				<xsl:text>JUint</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'Reporting'">
				<xsl:text>Reporting</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'Security'">
				<xsl:text>Security</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'Stability'">
				<xsl:text>Stability</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'Unknown'">
				<xsl:text>Unknown</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'Usability'">
				<xsl:text>Usability</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="BG_USER_04"]'>
		<xsl:variable name="fieldValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Customer Visible</xsl:attribute>
			<xsl:if test="$fieldValue = '?'">
				<xsl:text>?</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'N'">
				<xsl:text>No</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'Y'">
				<xsl:text>Yes</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="BG_USER_17"]'>
		<xsl:variable name="fieldValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Disposition</xsl:attribute>
			<xsl:if test="$fieldValue = 'Deferred'">
				<xsl:text>Deferred</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'Duplicate'">
				<xsl:text>Duplicate</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'Limitation'">
				<xsl:text>Limitation</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'Need More Info'">
				<xsl:text>Need More Info</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'Non Problem'">
				<xsl:text>Non Problem</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'Not Applicable'">
				<xsl:text>Not Applicable</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'Rejected'">
				<xsl:text>Rejected</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'Under Investigation'">
				<xsl:text>Under Investigation</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'Unknown'">
				<xsl:text>Unknown</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'Works as Defined'">
				<xsl:text>Works as Defined</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="BG_USER_18"]'>
		<xsl:variable name="fieldValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Type</xsl:attribute>
			<xsl:if test="$fieldValue = 'Documentation'">
				<xsl:text>Documentation</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'Hardware'">
				<xsl:text>Hardware</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'Software'">
				<xsl:text>Software</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="BG_USER_16"]'>
		<xsl:variable name="fieldValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Was Fixed In</xsl:attribute>
			<xsl:if test="$fieldValue = 'FNM 5.2'">
				<xsl:text>FNM 5.2</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'FNP 8.2'">
				<xsl:text>FNP 8.2</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="BG_USER_15"]'>
		<xsl:variable name="fieldValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Will Fix In</xsl:attribute>
			<xsl:if test="$fieldValue = 'FNM 5.2'">
				<xsl:text>FNM 5.2</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'FNP 8.2'">
				<xsl:text>FNP 8.2</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="BG_USER_10"]'>
		<xsl:variable name="fieldValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Phase found in</xsl:attribute>
			<xsl:if test="$fieldValue = 'Development'">
				<xsl:text>Development</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'Field Trail'">
				<xsl:text>Filed Trail</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'Lab Trail'">
				<xsl:text>Lab Trail</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'Support'">
				<xsl:text>Support</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'System Integration Test'">
				<xsl:text>System Integration Test</xsl:text>
			</xsl:if>
			<xsl:if test="$fieldValue = 'System Verification Test'">
				<xsl:text>System Verification Test</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="BG_USER_03"]'>
		<xsl:variable name="fieldValue" as="xs:string" select="." />
		<xsl:choose>
			<xsl:when test="$fieldValue != ''">
				<field>
					<xsl:copy-of select="@*" />
					<xsl:attribute name="fieldName">Internal Reference</xsl:attribute>
					<xsl:value-of select="." />
				</field>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="BG_USER_14"]'>
		<xsl:variable name="fieldValue" as="xs:string" select="." />
		<xsl:choose>
			<xsl:when test="$fieldValue != ''">
				<field>
					<xsl:copy-of select="@*" />
					<xsl:attribute name="fieldName">External Reference</xsl:attribute>
					<xsl:value-of select="." />
				</field>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="BG_USER_09"]'>
		<xsl:variable name="fieldValue" as="xs:string" select="." />
		<xsl:choose>
			<xsl:when test="$fieldValue != ''">
				<field>
					<xsl:copy-of select="@*" />
					<xsl:attribute name="fieldName">Detected in SW-Build</xsl:attribute>
					<xsl:value-of select="." />
				</field>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="BG_USER_11"]'>
		<xsl:variable name="fieldValue" as="xs:string" select="." />
		<xsl:choose>
			<xsl:when test="$fieldValue != ''">
				<field>
					<xsl:copy-of select="@*" />
					<xsl:attribute name="fieldName">Test in SW-build</xsl:attribute>
					<xsl:value-of select="." />
				</field>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="text()" />
</xsl:stylesheet>