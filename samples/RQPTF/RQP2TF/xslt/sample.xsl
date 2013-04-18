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
	<xsl:template match="/ccf:artifact[@artifactType = 'attachment']">
		<xsl:copy-of select="." />
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Name"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">title</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="ReqPro ID"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">ReqPro ID</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Acceptance Criteria"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Acceptance Criteria</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<!-- 
	<xsl:template match='ccf:field[@fieldName="Description"]'>
		<xsl:variable name="descriptionValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">description</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:choose>
				<xsl:when test="@fieldValueType='HTMLString'">
					<xsl:value-of select="stringutil:stripHTML(string(.))" />
				</xsl:when>
	-->
<!-- 				<xsl:when test="$descriptionValue = ''"> -->
<!-- 					<xsl:text>None</xsl:text> -->
<!-- 				</xsl:when> -->
	<!-- 
				<xsl:otherwise>
					<xsl:value-of select="." />
				</xsl:otherwise>
			</xsl:choose>
		</field>
	</xsl:template>
	 -->
	<xsl:template match='ccf:field[@fieldName="Allocation"]'>
		<xsl:variable name="allocationValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Allocation</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:if test="$allocationValue = '&lt;no entry&gt;'">
				<xsl:text>None</xsl:text>
			</xsl:if>
			<xsl:if test="$allocationValue = 'Not yet allocated'">
				<xsl:text>Not yet allocated</xsl:text>
			</xsl:if>
			<xsl:if test="$allocationValue = 'Product A'">
				<xsl:text>Product A</xsl:text>
			</xsl:if>
			<xsl:if test="$allocationValue = 'Component 1'">
				<xsl:text>Component 1</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Status"]'>
		<xsl:variable name="statusValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">status</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:if test="$statusValue = 'Pending'">
				<xsl:text>Pending</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Obsolete'">
				<xsl:text>Rejected</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Category"]'>
		<xsl:variable name="categoryValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">category</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:if test="$categoryValue = '&lt;no entry&gt;'">
				<xsl:text>None</xsl:text>
			</xsl:if>
			<xsl:if test="$categoryValue = 'User Story'">
				<xsl:text>User Story</xsl:text>
			</xsl:if>
			<xsl:if test="$categoryValue = 'Technical Story'">
				<xsl:text>Technical Story</xsl:text>
			</xsl:if>
			<xsl:if test="$categoryValue = 'Requirement Story'">
				<xsl:text>Requirement Story</xsl:text>
			</xsl:if>
			<xsl:if test="$categoryValue = 'Spike'">
				<xsl:text>Spike</xsl:text>
			</xsl:if>
			<xsl:if test="$categoryValue = 'Improvement Story'">
				<xsl:text>Improvement Story</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Text"]'>
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
					<xsl:text>None</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Comments"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Comment Text</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:value-of select="stringutil:stripHTML(string(.))" />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Business Value"]'>
		<xsl:variable name="businessValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Business Value</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:if test="$businessValue = '&lt;no entry&gt;'">
				<xsl:text>None</xsl:text>
			</xsl:if>
			<xsl:if test="$businessValue = '1- Highest'">
				<xsl:text>1- Highest</xsl:text>
			</xsl:if>
			<xsl:if test="$businessValue = '2- High'">
				<xsl:text>2- High</xsl:text>
			</xsl:if>
			<xsl:if test="$businessValue = '3- Medium'">
				<xsl:text>3- Medium</xsl:text>
			</xsl:if>
			<xsl:if test="$businessValue = '4- Low'">
				<xsl:text>4- Low</xsl:text>
			</xsl:if>
			<xsl:if test="$businessValue = '5- Lowest'">
				<xsl:text>5- Lowest</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Priority"]'>
		<xsl:variable name="priority" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">priority</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:if test="$priority = '1 - Highest'">
				<xsl:text>1</xsl:text>
			</xsl:if>
			<xsl:if test="$priority = '2 - High'">
				<xsl:text>2</xsl:text>
			</xsl:if>
			<xsl:if test="$priority = '3 - Medium'">
				<xsl:text>3</xsl:text>
			</xsl:if>
			<xsl:if test="$priority = '4 - Low'">
				<xsl:text>4</xsl:text>
			</xsl:if>
			<xsl:if test="$priority = '5 - Lowest'">
				<xsl:text>5</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
<!-- 	<xsl:template match='ccf:field[@fieldName="Assigned To"]'> -->
<!-- 		<field> -->
<!-- 			<xsl:copy-of select="@*" /> -->
<!-- 			<xsl:attribute name="fieldName">dummy</xsl:attribute> -->
<!-- 			<xsl:attribute name="fieldType">flexField</xsl:attribute> -->
<!-- 			<xsl:value-of select="." /> -->
<!-- 		</field> -->
<!-- 	</xsl:template> -->
</xsl:stylesheet>