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
			<xsl:apply-templates mode="nonSpecificFields"/>
			<!-- Apply templates that match for all types. -->
			<xsl:variable name="requirementType" select='ccf:field[@fieldName="RQ_TYPE_ID"]'/>			
			<xsl:choose>
				<xsl:when test="$requirementType = 'Folder'">
					<xsl:apply-templates mode="folderSpecificFields"/>
				</xsl:when>
				<xsl:when test="$requirementType = 'Functional'">
					<xsl:apply-templates mode="functionalSpecificFields"/>
				</xsl:when>	
				<!-- If you want to add another artifact type to be synchronized, you need to add a condition here and you must add the artifact type specific templates with the new mode. -->
			</xsl:choose>
		</artifact>
	</xsl:template>
	<xsl:template match="/ccf:artifact[@artifactType = 'attachment']">
		<xsl:copy-of select="." />
	</xsl:template>	
	<!-- begin of templates for non specific fields from QC to TF -->
	<xsl:template match='ccf:field[@fieldName="RQ_REQ_NAME"]' mode="nonSpecificFields">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">title</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="RQ_TYPE_ID"]' mode="nonSpecificFields">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">QC_REQUIREMENT_TYPE</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="RQ_DEV_COMMENTS"]' mode="nonSpecificFields">
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
	<xsl:template match='ccf:field[@fieldName="RQ_REQ_COMMENT"]' mode="nonSpecificFields">
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
	<xsl:template match='ccf:field[@fieldName="RQ_REQ_PRIORITY"]' mode="nonSpecificFields">
		<xsl:variable name="priorityValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">priority</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:if test="$priorityValue = '1-Low'">
				<xsl:text>5</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = '2-Medium'">
				<xsl:text>4</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = '3-High'">
				<xsl:text>3</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = '4-Very High'">
				<xsl:text>2</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = '5-Urgent'">
				<xsl:text>1</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="RQ_USER_01"]' mode="nonSpecificFields">
		<xsl:variable name="statusValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">status</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:if test="$statusValue = 'Open'">
				<xsl:text>Open</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Closed'">
				<xsl:text>Closed</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Fixed'">
				<xsl:text>Fixed</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'New'">
				<xsl:text>New</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Rejected'">
				<xsl:text>Rejected</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Reopen'">
				<xsl:text>Reopen</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<!-- end of templates for non specific fields from QC to TF -->	
	<!-- begin of templates for folder specific fields from QC to TF -->
	<xsl:template match='ccf:field[@fieldName="RQ_REQ_REVIEWED"]' mode="folderSpecificFields">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Reviewed</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<!-- end of templates for folder specific fields from QC to TF -->
	<!-- begin of templates for functional specific fields from QC to TF -->
	<xsl:template match='ccf:field[@fieldName="RQ_REQ_STATUS"]' mode="functionalSpecificFields">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Direct Cover Status</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<!-- end of templates for functional specific fields from QC to TF -->
	<xsl:template match="text()" />
</xsl:stylesheet>