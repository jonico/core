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
			<field><xsl:attribute name="fieldName">priority</xsl:attribute><xsl:attribute name="fieldAction">replace</xsl:attribute><xsl:attribute name="fieldType">mandatoryField</xsl:attribute><xsl:attribute name="fieldValueHasChanged">true</xsl:attribute><xsl:attribute name="fieldValueType">Integer</xsl:attribute><xsl:attribute name="fieldValueIsNull">false</xsl:attribute>0</field>
			<xsl:apply-templates />
		</artifact>
	</xsl:template>
	<xsl:template match="/ccf:artifact[@artifactType = 'attachment']">
		<xsl:copy-of select="." />
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="System.Title"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">title</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="System.Description"]'>
		<xsl:variable name="descriptionValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">description</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:if test="$descriptionValue = ''">
				<xsl:text> </xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="System.State"]'>
		<xsl:variable name="statusValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">status</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:if test="$statusValue = 'Active'">
				<xsl:text>In Development</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Closed'">
				<xsl:text>Completed</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Resolved'">
				<xsl:text>Completed</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="System.History"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Comment Text</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
 			<xsl:value-of select="stringutil:stripHTML(string(.))" />
		</field>
	</xsl:template>
	<xsl:template match="text()" />
<!-- 	<xsl:template match='ccf:field[@fieldName="System.AssignedTo"]'> -->
<!-- 		<field> -->
<!-- 			<xsl:copy-of select="@*" /> -->
<!-- 			<xsl:attribute name="fieldName"></xsl:attribute> -->
<!-- 			<xsl:attribute name="fieldType"></xsl:attribute> -->
<!-- 			<xsl:value-of select="." /> -->
<!-- 		</field> -->
<!-- 	</xsl:template> -->
<!-- 	<xsl:template match='ccf:field[@fieldName="System.Reason"]'> -->
<!-- 		<field> -->
<!-- 			<xsl:copy-of select="@*" /> -->
<!-- 			<xsl:attribute name="fieldName"></xsl:attribute> -->
<!-- 			<xsl:attribute name="fieldType"></xsl:attribute> -->
<!-- 			<xsl:value-of select="." /> -->
<!-- 		</field> -->
<!-- 	</xsl:template> -->
<!-- 	<xsl:template match='ccf:field[@fieldName="Microsoft.VSTS.Common.ActivatedBy"]'> -->
<!-- 		<field> -->
<!-- 			<xsl:copy-of select="@*" /> -->
<!-- 			<xsl:attribute name="fieldName"></xsl:attribute> -->
<!-- 			<xsl:attribute name="fieldType"></xsl:attribute> -->
<!-- 			<xsl:value-of select="." /> -->
<!-- 		</field> -->
<!-- 	</xsl:template> -->
<!-- 	<xsl:template match='ccf:field[@fieldName="System.AreaPath"]'> -->
<!-- 		<field> -->
<!-- 			<xsl:copy-of select="@*" /> -->
<!-- 			<xsl:attribute name="fieldName"></xsl:attribute> -->
<!-- 			<xsl:attribute name="fieldType"></xsl:attribute> -->
<!-- 			<xsl:value-of select="." /> -->
<!-- 		</field> -->
<!-- 	</xsl:template> -->
<!-- 	<xsl:template match='ccf:field[@fieldName="System.IterationPath"]'> -->
<!-- 		<field> -->
<!-- 			<xsl:copy-of select="@*" /> -->
<!-- 			<xsl:attribute name="fieldName"></xsl:attribute> -->
<!-- 			<xsl:attribute name="fieldType"></xsl:attribute> -->
<!-- 			<xsl:value-of select="." /> -->
<!-- 		</field> -->
<!-- 	</xsl:template> -->
<!-- 	<xsl:template match='ccf:field[@fieldName="Microsoft.VSTS.Common.Risk"]'> -->
<!-- 		<field> -->
<!-- 			<xsl:copy-of select="@*" /> -->
<!-- 			<xsl:attribute name="fieldName"></xsl:attribute> -->
<!-- 			<xsl:attribute name="fieldType"></xsl:attribute> -->
<!-- 			<xsl:value-of select="." /> -->
<!-- 		</field> -->
<!-- 	</xsl:template> -->
<!-- 	<xsl:template match='ccf:field[@fieldName="Microsoft.VSTS.Common.StackRank"]'> -->
<!-- 		<field> -->
<!-- 			<xsl:copy-of select="@*" /> -->
<!-- 			<xsl:attribute name="fieldName"></xsl:attribute> -->
<!-- 			<xsl:attribute name="fieldType"></xsl:attribute> -->
<!-- 			<xsl:value-of select="." /> -->
<!-- 		</field> -->
<!-- 	</xsl:template> -->
<!-- 	<xsl:template match='ccf:field[@fieldName="Microsoft.VSTS.Scheduling.StoryPoints"]'> -->
<!-- 		<field> -->
<!-- 			<xsl:copy-of select="@*" /> -->
<!-- 			<xsl:attribute name="fieldName"></xsl:attribute> -->
<!-- 			<xsl:attribute name="fieldType"></xsl:attribute> -->
<!-- 			<xsl:value-of select="." /> -->
<!-- 		</field> -->
<!-- 	</xsl:template> -->
</xsl:stylesheet>