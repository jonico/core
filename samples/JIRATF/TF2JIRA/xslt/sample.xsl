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
			<xsl:apply-templates mode="nonSpecificFields"/> <!-- Apply templates that match for all types. -->
			
			<xsl:variable name="artifactType" select="substring-after(substring-after(@sourceRepositoryId, '-'), '-')"/>			
			<xsl:choose>
				<xsl:when test="$artifactType = 'Bug'">
					<xsl:apply-templates mode="bugSpecificFields"/>
				</xsl:when>
				<xsl:when test="$artifactType = 'Task'">
					<xsl:apply-templates mode="taskSpecificFields"/>
				</xsl:when>
				<xsl:when test="$artifactType = 'User Story'">
					<xsl:apply-templates mode="userStorySpecificFields"/>
				</xsl:when>	
				<!-- If you want to add another artifact type to be synchronized, you need to add a condition here and you must add the artifact type specific templates with the new mode. -->
			</xsl:choose>
		</artifact>
	</xsl:template>
	<xsl:template match="/ccf:artifact[@artifactType = 'attachment']">
		<xsl:copy-of select="." />
	</xsl:template>
	
	<!-- begin of templates for non specific fields from TFS to TF -->
	<xsl:template match='ccf:field[@fieldName="summary"]' mode="nonSpecificFields">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">title</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="System.History"]' mode="nonSpecificFields">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Comment Text</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="stringutil:stripHTML(string(.))" />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="System.AreaPath"]' mode="nonSpecificFields">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Area</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="System.IterationPath"]' mode="nonSpecificFields">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Iteration</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Microsoft.VSTS.Common.StackRank"]' mode="nonSpecificFields">
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
	<xsl:template match='ccf:field[@fieldName="System.AssignedTo"]' mode="nonSpecificFields">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">assignedTo</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<!-- end of templates for non specific fields from TFS to TF -->
	<!-- begin of templates for bug specific fields from TFS to TF -->
	<xsl:template match='ccf:field[@fieldName="description"]' mode="bugSpecificFields">
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
	<xsl:template match='ccf:field[@fieldName="System.State"]' mode="bugSpecificFields">
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
	<xsl:template match='ccf:field[@fieldName="Microsoft.VSTS.Common.Priority"]' mode="bugSpecificFields">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">priority</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Microsoft.VSTS.Common.Severity"]' mode="bugSpecificFields">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Severity</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="System.Reason"]' mode="bugSpecificFields">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Reason</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Microsoft.VSTS.Common.ResolvedReason"]' mode="bugSpecificFields">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Resolved Reason</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<!-- end of templates for bug specific fields from TFS to TF -->
	<!-- begin of templates for task specific fields from TFS to TF -->
		<xsl:template match='ccf:field[@fieldName="System.Description"]' mode="taskSpecificFields">
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
	<xsl:template match='ccf:field[@fieldName="System.State"]' mode="taskSpecificFields">
		<xsl:variable name="statusValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">status</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:if test="$statusValue = 'Active'">
				<xsl:text>Active</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Closed'">
				<xsl:text>Closed</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="System.Reason"]' mode="taskSpecificFields">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Reason</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Microsoft.VSTS.Common.Priority"]' mode="taskSpecificFields">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">priority</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Microsoft.VSTS.Scheduling.OriginalEstimate"]' mode="taskSpecificFields">
		<xsl:variable name="originalEffortValue" as="xs:string" select="." />
			<field>
				<xsl:copy-of select="@*" />
				<xsl:attribute name="fieldName">estimatedHours</xsl:attribute>
				<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
				<xsl:attribute name="fieldValueType">String</xsl:attribute>
				<xsl:choose>
					<xsl:when test="$originalEffortValue = ''"><xsl:value-of select="." /></xsl:when>
					<xsl:otherwise><xsl:value-of select="floor(.)" /></xsl:otherwise>
				</xsl:choose>
			</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Microsoft.VSTS.Scheduling.RemainingWork"]' mode="taskSpecificFields">
		<xsl:variable name="remainningValue" as="xs:string" select="." />
			<field>
				<xsl:copy-of select="@*" />
				<xsl:attribute name="fieldName">remainingEffort</xsl:attribute>
				<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
				<xsl:attribute name="fieldValueType">String</xsl:attribute>
				<xsl:choose>
					<xsl:when test="$remainningValue = ''"><xsl:value-of select="." /></xsl:when>
					<xsl:otherwise><xsl:value-of select="floor(.)" /></xsl:otherwise>
				</xsl:choose>
			</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Microsoft.VSTS.Scheduling.CompletedWork"]' mode="taskSpecificFields">
		<xsl:variable name="completedWorkValue" as="xs:string" select="." />
			<field>
				<xsl:copy-of select="@*" />
				<xsl:attribute name="fieldName">actualHours</xsl:attribute>
				<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
				<xsl:attribute name="fieldValueType">String</xsl:attribute>
				<xsl:choose>
					<xsl:when test="$completedWorkValue = ''"><xsl:value-of select="." /></xsl:when>
					<xsl:otherwise><xsl:value-of select="floor(.)" /></xsl:otherwise>
				</xsl:choose>
			</field>
	</xsl:template>
	<!-- end of templates for task specific fields from TFS to TF -->
	<!-- begin of templates for user story specific fields from TFS to TF -->
	<xsl:template match='ccf:field[@fieldName="System.Description"]' mode="userStorySpecificFields">
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
	<xsl:template match='ccf:field[@fieldName="System.State"]' mode="userStorySpecificFields">
		<xsl:variable name="statusValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">status</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:if test="$statusValue = 'Active'">
				<xsl:text>Active</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Closed'">
				<xsl:text>Closed</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Resolved'">
				<xsl:text>Resolved</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="System.Reason"]' mode="userStorySpecificFields">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Reason</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Microsoft.VSTS.Common.Risk"]' mode="userStorySpecificFields">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Risk</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Microsoft.VSTS.Scheduling.StoryPoints"]' mode="userStorySpecificFields">
		<xsl:variable name="storyPointsValue" as="xs:string" select="." />
			<field>
				<xsl:copy-of select="@*" />
				<xsl:attribute name="fieldName">points</xsl:attribute>
				<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
				<xsl:attribute name="fieldValueType">String</xsl:attribute>
				<xsl:choose>
					<xsl:when test="$storyPointsValue = ''"><xsl:value-of select="." /></xsl:when>
					<xsl:otherwise><xsl:value-of select="floor(.)" /></xsl:otherwise>
				</xsl:choose>
			</field>
	</xsl:template>
	<!-- end of templates for user story specific fields from TFS to TF -->
		
	<xsl:template match="text()" />
</xsl:stylesheet>