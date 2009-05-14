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
<?altova_samplexml file:///C:/Documents%20and%20Settings/jnicolai/workspace/CCF/scripts/exampleartifactCSFE.xml?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:ccf="http://ccf.open.collab.net/GenericArtifactV1.0" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:xslo="alias">
	<xsl:namespace-alias stylesheet-prefix="xslo" result-prefix="xsl"/>
	<xsl:template match="/element()" priority="2">
		<xslo:stylesheet version="2.0" xmlns:ccf="http://ccf.open.collab.net/GenericArtifactV1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns="" exclude-result-prefixes="xsl xs fn ccf">
			<xsl:comment>
				<xsl:text>Automatically generated stylesheet to convert from a repository specific schema to the generic artifact format</xsl:text>
			</xsl:comment>
			<xslo:template match="node()" priority="1"/>
			<xslo:template match="/{local-name()}" priority="2">
				<artifact xmlns="http://ccf.open.collab.net/GenericArtifactV1.0">
					<xslo:copy-of select="@*"/>
					<xslo:apply-templates/>
				</artifact>
			</xslo:template>
			<xsl:apply-templates/>
		</xslo:stylesheet>
	</xsl:template>
	<xsl:template match="node()" priority="1"/>
	<xsl:template match="ccf:field" priority="2">
		<xslo:template match="{@alternativeFieldName}" priority="2">
			<field fieldName="{@fieldName}" fieldType="{@fieldType}" fieldValueType="{@fieldValueType}" nullValueSupported="{@nullValueSupported}" minOccurs="{@minOccurs}" maxOccurs="{@maxOccurs}" alternativeFieldName="{@alternativeFieldName}" xmlns="http://ccf.open.collab.net/GenericArtifactV1.0">
				<xslo:copy-of select="@*"/>
				<xsl:choose>
					<xsl:when test='@nullValueSupported="true"'>
						<xslo:choose>
							<xslo:when test="node()!=''">
								<xslo:attribute name="fieldValueIsNull">false</xslo:attribute>
							</xslo:when>
							<xslo:otherwise>
								<xslo:attribute name="fieldValueIsNull">true</xslo:attribute>
							</xslo:otherwise>
						</xslo:choose>
					</xsl:when>
					<xsl:otherwise>
						<xslo:attribute name="fieldValueIsNull">false</xslo:attribute>
					</xsl:otherwise>
				</xsl:choose>
				<xslo:value-of select="node()"/>
			</field>
		</xslo:template>
	</xsl:template>
</xsl:stylesheet>
