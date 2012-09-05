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
			<xsl:choose>
				<xsl:when test="@sourceSystemKind = 'TFS'"> <!-- TFS to TF -->
					<xsl:apply-templates mode="nonSpecificFields-TFS2TF"/> <!-- Apply templates that match for all types. -->
					<xsl:choose>
						<xsl:when test="substring(@sourceRepositoryId, string-length(@sourceRepositoryId) - string-length('Bug') + 1) = 'Bug'">
							<!-- @sourceRepositoryId ends with Bug -->
							<xsl:apply-templates mode="bugSpecificFields-TFS2TF"/>
						</xsl:when>
						<xsl:when test="substring(@sourceRepositoryId, string-length(@sourceRepositoryId) - string-length('Task') + 1) = 'Task'">
							<!-- @sourceRepositoryId ends with Task -->
							<xsl:apply-templates mode="taskSpecificFields-TFS2TF"/>
						</xsl:when>
						<xsl:when test="substring(@sourceRepositoryId, string-length(@sourceRepositoryId) - string-length('User Story') + 1) = 'User Story'">
							<!-- @sourceRepositoryId ends with User Story -->
							<xsl:apply-templates mode="userStorySpecificFields-TFS2TF"/>
						</xsl:when>	
					</xsl:choose>
				</xsl:when>				
				<xsl:when test="@targetSystemKind = 'TFS'"> <!-- TF to TFS -->
					<xsl:apply-templates mode="nonSpecificFields-TF2TFS"/> <!-- Apply templates that match for all types. -->
					<xsl:choose>
						<xsl:when test="substring(@targetRepositoryId, string-length(@targetRepositoryId) - string-length('Bug') + 1) = 'Bug'">
							<!-- @sourceRepositoryId ends with Bug -->
							<xsl:apply-templates mode="bugSpecificFields-TF2TFS"/>
						</xsl:when>
						<xsl:when test="substring(@targetRepositoryId, string-length(@targetRepositoryId) - string-length('Task') + 1) = 'Task'">
							<!-- @sourceRepositoryId ends with Task -->
							<xsl:apply-templates mode="taskSpecificFields-TF2TFS"/>
						</xsl:when>
						<xsl:when test="substring(@targetRepositoryId, string-length(@targetRepositoryId) - string-length('User Story') + 1) = 'User Story'">
							<!-- @sourceRepositoryId ends with User Story -->
							<xsl:apply-templates mode="userStorySpecificFields-TF2TFS"/>
						</xsl:when>
					</xsl:choose>
				</xsl:when>				
			</xsl:choose>						
		</artifact>
	</xsl:template>
	<xsl:template match="/ccf:artifact[@artifactType = 'attachment']">
		<xsl:copy-of select="." />
	</xsl:template>
	
	<!-- begin of templates for non specific fields from TFS to TF -->
	<xsl:template match='ccf:field[@fieldName="System.Title"]' mode="nonSpecificFields-TFS2TF">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">title</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="System.History"]' mode="nonSpecificFields-TFS2TF">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Comment Text</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="stringutil:stripHTML(string(.))" />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="System.AreaPath"]' mode="nonSpecificFields-TFS2TF">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Area</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="System.IterationPath"]' mode="nonSpecificFields-TFS2TF">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Iteration</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Microsoft.VSTS.Common.StackRank"]' mode="nonSpecificFields-TFS2TF">
		<xsl:variable name="stackRankValue" as="xs:string" select="." /> 
			<field>
				<xsl:copy-of select="@*" />
				<xsl:attribute name="fieldName">Stack Rank</xsl:attribute>
				<xsl:attribute name="fieldType">flexField</xsl:attribute>
				<xsl:attribute name="fieldValueType">String</xsl:attribute>
				<xsl:choose>
					<xsl:when test="$stackRankValue = ''"><xsl:value-of select="." /></xsl:when>
					<xsl:when test="contains($stackRankValue, '.')"><xsl:value-of select="." /></xsl:when>
					<xsl:otherwise><xsl:value-of select="floor(.)" /></xsl:otherwise>
				</xsl:choose>
			</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="System.AssignedTo"]' mode="nonSpecificFields-TFS2TF">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">assignedTo</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<!-- end of templates for non specific fields from TFS to TF -->
	<!-- begin of templates for bug specific fields from TFS to TF -->
	<xsl:template match='ccf:field[@fieldName="Microsoft.VSTS.TCM.ReproSteps"]' mode="bugSpecificFields-TFS2TF">
		<xsl:variable name="descriptionValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">description</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:value-of select="stringutil:stripHTML(string(.))" />
			<xsl:if test="$descriptionValue = ''">
				<xsl:text> </xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="System.State"]' mode="bugSpecificFields-TFS2TF">
		<xsl:variable name="statusValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">status</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:if test="$statusValue = 'Active'">
				<xsl:text>Active</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Resolved'">
				<xsl:text>Resolved</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Closed'">
				<xsl:text>Closed</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Microsoft.VSTS.Common.Priority"]' mode="bugSpecificFields-TFS2TF">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">priority</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Microsoft.VSTS.Common.Severity"]' mode="bugSpecificFields-TFS2TF">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Severity</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="System.Reason"]' mode="bugSpecificFields-TFS2TF">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Reason</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Microsoft.VSTS.Common.ResolvedReason"]' mode="bugSpecificFields-TFS2TF">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Resolved Reason</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<!-- end of templates for bug specific fields from TFS to TF -->
	
	<!-- begin of templates for non specific fields from TF to TFS -->
	<xsl:template match='ccf:field[@fieldName="title"]' mode="nonSpecificFields-TF2TFS">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">System.Title</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Comment Text"]' mode="nonSpecificFields-TF2TFS">
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">System.History</xsl:attribute>
			<xsl:value-of select="stringutil:encodeHTMLToEntityReferences(string(.))"/>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Area"]' mode="nonSpecificFields-TF2TFS">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">System.AreaPath</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Iteration"]' mode="nonSpecificFields-TF2TFS">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">System.IterationPath</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Stack Rank"]' mode="nonSpecificFields-TF2TFS">
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
	<!-- end of templates for non specific fields from TF to TFS -->	
	<!-- begin of templates for bug specific fields from TF to TFS -->
	<xsl:template match='ccf:field[@fieldName="description"]' mode="bugSpecificFields-TF2TFS">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Microsoft.VSTS.TCM.ReproSteps</xsl:attribute>
			<xsl:value-of select="stringutil:encodeHTMLToEntityReferences(string(.))"/>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="status"]' mode="bugSpecificFields-TF2TFS">
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
	<xsl:template match='ccf:field[@fieldName="priority"]' mode="bugSpecificFields-TF2TFS">
		<xsl:variable name="priorityValue" as="xs:string" select="."/>
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">Microsoft.VSTS.Common.Priority</xsl:attribute>
			<xsl:attribute name="fieldValueType">String</xsl:attribute>
			<xsl:choose>
				<!-- If the priority in TF is set to None, the priority in TFS will set to the default value. -->
				<xsl:when test="$priorityValue = '0'"><xsl:value-of select="2" /></xsl:when>
				<xsl:when test="$priorityValue = '5'"><xsl:value-of select="4" /></xsl:when>
				<xsl:otherwise><xsl:value-of select="." /></xsl:otherwise>
			</xsl:choose>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="reason"]' mode="bugSpecificFields-TF2TFS">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">System.Reason</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<!-- end of templates for bug specific fields from TF to TFS -->
	
	<xsl:template match="text()" />
</xsl:stylesheet>