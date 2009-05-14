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
	xmlns:ptutil="xalan://com.collabnet.ccf.pi.cee.pt.v50.ProjectTrackerHelper"
	exclude-result-prefixes="xsl xs ccf stringutil" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.w3.org/1999/XSL/Transform http://www.w3.org/2007/schema-for-xslt20.xsd">
	<xsl:variable name="sourceArtifactId" as="xs:string"
		select="/ccf:artifact/@sourceArtifactId" />
	<xsl:variable name="artifactTypeNameSpace" as="xs:string"
		select="ptutil:getNamespaceWithBraces($sourceArtifactId)" />
	<xsl:template match="/ccf:artifact[@artifactType = 'attachment']">
		<xsl:copy-of select="." />
	</xsl:template>
	<xsl:template match='/ccf:artifact[@artifactType = "plainArtifact"]'>
		<artifact xmlns="http://ccf.open.collab.net/GenericArtifactV1.0">
			<xsl:copy-of select="@*" />
			<xsl:apply-templates />
			<xsl:variable name="assignedTo" as="xs:string"
				select="concat($artifactTypeNameSpace,'Assigned To')" />
			<xsl:for-each select='ccf:field[@fieldName=string($assignedTo)]'>
				<xsl:if test="position()=last()">
					<field>
						<xsl:copy-of select="@*" />
						<xsl:attribute name="fieldName">BG_RESPONSIBLE</xsl:attribute>
						<xsl:value-of select="." />
					</field>
				</xsl:if>
			</xsl:for-each>
			<xsl:variable name="detectedBy" as="xs:string"
				select="concat($artifactTypeNameSpace,'Detected By')" />
			<xsl:for-each select='ccf:field[@fieldName=string($detectedBy)]'>
				<xsl:if test="position()=last()">
					<field>
						<xsl:copy-of select="@*" />
						<xsl:attribute name="fieldName">BG_DETECTED_BY</xsl:attribute>
						<xsl:value-of select="." />
					</field>
				</xsl:if>
			</xsl:for-each>
		</artifact>
	</xsl:template>
	<xsl:template
		match='ccf:field[@fieldName="{urn:ws.tracker.collabnet.com}id"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">BG_USER_07</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template
		match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Summary")]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">BG_SUMMARY</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template
		match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Assigned Tester")]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">BG_USER_13</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template
		match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Priority")]'>
		<xsl:variable name="priorityValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">BG_PRIORITY</xsl:attribute>
			<xsl:if test="$priorityValue = 'Urgent'">
				<xsl:text>Urgent</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = 'High'">
				<xsl:text>High</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = 'Medium'">
				<xsl:text>Medium</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = 'Low'">
				<xsl:text>Low</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template
		match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Severity")]'>
		<xsl:variable name="SeverityValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">BG_SEVERITY</xsl:attribute>
			<xsl:if test="$SeverityValue = 'Critical'">
				<xsl:text>Critical</xsl:text>
			</xsl:if>
			<xsl:if test="$SeverityValue = 'Major'">
				<xsl:text>Major</xsl:text>
			</xsl:if>
			<xsl:if test="$SeverityValue = 'Minor'">
				<xsl:text>Minor</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template
		match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Type")]'>
		<xsl:variable name="typeValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">BG_USER_18</xsl:attribute>
			<xsl:if test="$typeValue = 'Documentation'">
				<xsl:text>Documentation</xsl:text>
			</xsl:if>
			<xsl:if test="$typeValue = 'Hardware'">
				<xsl:text>Hardware</xsl:text>
			</xsl:if>
			<xsl:if test="$typeValue = 'Software'">
				<xsl:text>Software</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template
		match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Area")]'>
		<xsl:variable name="AreaValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">BG_USER_08</xsl:attribute>
			<xsl:if test="$AreaValue = 'FindBugs'">
				<xsl:text>FindBugs</xsl:text>
			</xsl:if>
			<xsl:if test="$AreaValue = 'FNP-CC'">
				<xsl:text>FNP-CC</xsl:text>
			</xsl:if>
			<xsl:if test="$AreaValue = 'FNP-CG'">
				<xsl:text>FNP-CG</xsl:text>
			</xsl:if>
			<xsl:if test="$AreaValue = 'FNP-CV'">
				<xsl:text>FNP-CV</xsl:text>
			</xsl:if>
			<xsl:if test="$AreaValue = 'GUI'">
				<xsl:text>GUI</xsl:text>
			</xsl:if>
			<xsl:if test="$AreaValue = 'JUint'">
				<xsl:text>JUnit</xsl:text>
			</xsl:if>
			<xsl:if test="$AreaValue = 'Reporting'">
				<xsl:text>Reporting</xsl:text>
			</xsl:if>
			<xsl:if test="$AreaValue = 'Security'">
				<xsl:text>Security</xsl:text>
			</xsl:if>
			<xsl:if test="$AreaValue = 'Stability'">
				<xsl:text>Stability</xsl:text>
			</xsl:if>
			<xsl:if test="$AreaValue = 'Unknown'">
				<xsl:text>Unknown</xsl:text>
			</xsl:if>
			<xsl:if test="$AreaValue = 'Usability'">
				<xsl:text>Usability</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template
		match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Status")]'>
		<xsl:variable name="StatusValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">BG_STATUS</xsl:attribute>
			<xsl:if test="$StatusValue = 'Open'">
				<xsl:text>Open</xsl:text>
			</xsl:if>
			<xsl:if test="$StatusValue = 'Answered'">
				<xsl:text>Answered</xsl:text>
			</xsl:if>
			<xsl:if test="$StatusValue = 'Assigned'">
				<xsl:text>Assigned</xsl:text>
			</xsl:if>
			<xsl:if test="$StatusValue = 'Closed'">
				<xsl:text>Closed</xsl:text>
			</xsl:if>
			<xsl:if test="$StatusValue = 'Fixed'">
				<xsl:text>Fixed</xsl:text>
			</xsl:if>
			<xsl:if test="$StatusValue = 'Monitor'">
				<xsl:text>Monitor</xsl:text>
			</xsl:if>
			<xsl:if test="$StatusValue = 'Pending Test'">
				<xsl:text>Pending Test</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template
		match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Customer Visible")]'>
		<xsl:variable name="custValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">BG_USER_04</xsl:attribute>
			<xsl:if test="$custValue = '?'">
				<xsl:text>?</xsl:text>
			</xsl:if>
			<xsl:if test="$custValue = 'Yes'">
				<xsl:text>Y</xsl:text>
			</xsl:if>
			<xsl:if test="$custValue = 'No'">
				<xsl:text>N</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template
		match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Reproducible")]'>
		<xsl:variable name="repValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">BG_REPRODUCIBLE</xsl:attribute>
			<xsl:if test="$repValue = '?'">
				<xsl:text>?</xsl:text>
			</xsl:if>
			<xsl:if test="$repValue = 'Yes'">
				<xsl:text>Y</xsl:text>
			</xsl:if>
			<xsl:if test="$repValue = 'No'">
				<xsl:text>N</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template
		match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Detected in Version")]'>
		<xsl:variable name="detValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">BG_DETECTION_VERSION</xsl:attribute>
			<xsl:if test="$detValue = 'FNM 5.2'">
				<xsl:text>FNM 5.2</xsl:text>
			</xsl:if>
			<xsl:if test="$detValue = 'FNP 8.2'">
				<xsl:text>FNP 8.2</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template
		match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Detected in SW-Build")]'>
		<xsl:variable name="priorityValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">BG_USER_09</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template
		match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Test in SW-build")]'>
		<xsl:variable name="priorityValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">BG_USER_11</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template
		match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Will Fix In")]'>
		<xsl:variable name="willValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">BG_USER_15</xsl:attribute>
			<xsl:if test="$willValue = 'FNM 5.2'">
				<xsl:text>FNM 5.2</xsl:text>
			</xsl:if>
			<xsl:if test="$willValue = 'FNP 8.2'">
				<xsl:text>FNP 8.2</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template
		match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Was Fixed In")]'>
		<xsl:variable name="wasValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">BG_USER_16</xsl:attribute>
			<xsl:if test="$wasValue = 'FNM 5.2'">
				<xsl:text>FNM 5.2</xsl:text>
			</xsl:if>
			<xsl:if test="$wasValue = 'FNP 8.2'">
				<xsl:text>FNP 8.2</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template
		match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Phase found in")]'>
		<xsl:variable name="pfiValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">BG_USER_10</xsl:attribute>
			<xsl:if test="$pfiValue = 'Development'">
				<xsl:text>Development</xsl:text>
			</xsl:if>
			<xsl:if test="$pfiValue = 'Lab Trail'">
				<xsl:text>Lab Trial</xsl:text>
			</xsl:if>
			<xsl:if test="$pfiValue = 'Field Trail'">
				<xsl:text>Field Trial</xsl:text>
			</xsl:if>
			<xsl:if test="$pfiValue = 'Support'">
				<xsl:text>Support</xsl:text>
			</xsl:if>
			<xsl:if test="$pfiValue = 'System Integration Test'">
				<xsl:text>System Integration Test</xsl:text>
			</xsl:if>
			<xsl:if test="$pfiValue = 'System Verification Test'">
				<xsl:text>System Verification Test</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template
		match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Internal Reference")]'>
		<xsl:variable name="priorityValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">BG_USER_03</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template
		match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"External Reference")]'>
		<xsl:variable name="priorityValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">BG_USER_14</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template
		match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Disposition")]'>
		<xsl:variable name="dispValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">BG_USER_17</xsl:attribute>
			<xsl:if test="$dispValue = 'Deferred'">
				<xsl:text>Deferred</xsl:text>
			</xsl:if>
			<xsl:if test="$dispValue = 'Duplicate'">
				<xsl:text>Duplicate</xsl:text>
			</xsl:if>
			<xsl:if test="$dispValue = 'Limitation'">
				<xsl:text>Limitation</xsl:text>
			</xsl:if>
			<xsl:if test="$dispValue = 'Need More Info'">
				<xsl:text>Need More Info</xsl:text>
			</xsl:if>
			<xsl:if test="$dispValue = 'Non Problem'">
				<xsl:text>Non Problem</xsl:text>
			</xsl:if>
			<xsl:if test="$dispValue = 'Not Applicable'">
				<xsl:text>Not Applicable</xsl:text>
			</xsl:if>
			<xsl:if test="$dispValue = 'Rejected'">
				<xsl:text>Rejected</xsl:text>
			</xsl:if>
			<xsl:if test="$dispValue = 'Under Investigation'">
				<xsl:text>Under Investigation</xsl:text>
			</xsl:if>
			<xsl:if test="$dispValue = 'Unknown'">
				<xsl:text>Unknown</xsl:text>
			</xsl:if>
			<xsl:if test="$dispValue = 'Works as Defined'">
				<xsl:text>Works as Defined</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template
		match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Details")]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">BG_DESCRIPTION</xsl:attribute>
			<xsl:text>&lt;html&gt;&lt;body&gt;</xsl:text>
			<xsl:value-of select="stringutil:encodeHTMLToEntityReferences(string(.))"/>
			<xsl:text>&lt;/body&gt;&lt;/html&gt;</xsl:text>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="{urn:ws.tracker.collabnet.com}comment"]'>
		<xsl:variable name="comment" as="xs:string" select="."/>
		<xsl:choose>
			<xsl:when test="$comment=''">
				<xsl:text/>
			</xsl:when>
			<xsl:otherwise>
				<field>
					<xsl:copy-of select="@*"/>
					<xsl:attribute name="fieldName">BG_DEV_COMMENTS</xsl:attribute>
					<xsl:value-of select="stringutil:encodeHTMLToEntityReferences(string(.))"/>
				</field>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="{urn:ws.tracker.collabnet.com}reason"]'>
		<xsl:variable name="comment" as="xs:string" select="."/>
		<xsl:choose>
			<xsl:when test="$comment=''">
				<xsl:text/>
			</xsl:when>
			<xsl:otherwise>
				<field>
					<xsl:copy-of select="@*"/>
					<xsl:attribute name="fieldName">BG_DEV_COMMENTS</xsl:attribute>
					<xsl:value-of select="stringutil:encodeHTMLToEntityReferences(string(.))"/>
				</field>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="{urn:ws.tracker.collabnet.com}createdOn"]'>
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">BG_DETECTION_DATE</xsl:attribute>
			<xsl:value-of select="."/>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="{urn:ws.tracker.collabnet.com}modifiedOn"]'>
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">BG_USER_12</xsl:attribute>
			<xsl:value-of select="."/>
		</field>
	</xsl:template>
	<xsl:template match="text()"/>
</xsl:stylesheet>
