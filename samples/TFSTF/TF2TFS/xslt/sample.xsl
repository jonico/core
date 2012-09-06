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
			
			<xsl:variable name="artifactType" select="substring-after(substring-after(@targetRepositoryId, '-'), '-')"/>
			<xsl:variable name="artifactTypeInLowerCase" select="translate($artifactType, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')"/>

<!-- 			<xsl:variable name="mode" select="concat($artifactTypeInLowerCase,'SpecificFields')"/> -->
<!-- 			<xsl:apply-templates mode="{$mode}"/> Apply artifact type specific templates, e.g. task-specific templates. -->
			
			<xsl:choose>
				<xsl:when test="$artifactTypeInLowerCase = 'bug'">
					<xsl:apply-templates mode="bugSpecificFields"/>
				</xsl:when>
				<xsl:when test="$artifactTypeInLowerCase = 'task'">
					<xsl:apply-templates mode="taskSpecificFields"/>
				</xsl:when>
				<xsl:when test="$artifactTypeInLowerCase = 'user story'">
					<xsl:apply-templates mode="userStorySpecificFields"/>
				</xsl:when>	
			</xsl:choose>
		</artifact>
	</xsl:template>
	<xsl:template match="/ccf:artifact[@artifactType = 'attachment']">
		<xsl:copy-of select="." />
	</xsl:template>
	
	<!-- begin of templates for non specific fields from TF to TFS -->
	<xsl:template match='ccf:field[@fieldName="title"]' mode="nonSpecificFields">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">System.Title</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Comment Text"]' mode="nonSpecificFields">
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">System.History</xsl:attribute>
			<xsl:value-of select="stringutil:encodeHTMLToEntityReferences(string(.))"/>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Area"]' mode="nonSpecificFields">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">System.AreaPath</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Iteration"]' mode="nonSpecificFields">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">System.IterationPath</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Stack Rank"]' mode="nonSpecificFields">
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
	<xsl:template match='ccf:field[@fieldName="description"]' mode="bugSpecificFields">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Microsoft.VSTS.TCM.ReproSteps</xsl:attribute>
			<xsl:value-of select="stringutil:encodeHTMLToEntityReferences(string(.))"/>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="status"]' mode="bugSpecificFields">
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
	<xsl:template match='ccf:field[@fieldName="priority"]' mode="bugSpecificFields">
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
	<xsl:template match='ccf:field[@fieldName="reason"]' mode="bugSpecificFields">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">System.Reason</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
		<xsl:template match='ccf:field[@fieldName="Severity"]' mode="bugSpecificFields">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Microsoft.VSTS.Common.Severity</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<!-- end of templates for bug specific fields from TF to TFS -->
	<!-- begin of templates for task specific fields from TF to TFS -->
	<xsl:template match='ccf:field[@fieldName="description"]' mode="taskSpecificFields">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">System.Description</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="status"]' mode="taskSpecificFields">
		<xsl:variable name="statusValue" as="xs:string" select="."/>
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">System.State</xsl:attribute>
			<xsl:if test="$statusValue = 'Active'">
				<xsl:text>Active</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Closed'">
				<xsl:text>Closed</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Reason"]' mode="taskSpecificFields">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">System.Reason</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="priority"]' mode="taskSpecificFields">
		<xsl:variable name="priorityValue" as="xs:string" select="."/>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Microsoft.VSTS.Common.Priority</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:attribute name="fieldValueType">String</xsl:attribute>
			<xsl:choose>
				<!-- If the priority in TF is set to None, the priority in TFS will set to the default value. -->
				<xsl:when test="$priorityValue = '0'"><xsl:value-of select="2" /></xsl:when>
				<xsl:when test="$priorityValue = '5'"><xsl:value-of select="4" /></xsl:when>
				<xsl:otherwise><xsl:value-of select="." /></xsl:otherwise>
			</xsl:choose>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="estimatedHours"]' mode="taskSpecificFields">
		<xsl:variable name="originalEffortValue" as="xs:string" select="." />
			<field>
				<xsl:copy-of select="@*" />
				<xsl:attribute name="fieldName">Microsoft.VSTS.Scheduling.OriginalEstimate</xsl:attribute>
				<xsl:attribute name="fieldValueType">String</xsl:attribute>
				<xsl:choose>
					<xsl:when test="$originalEffortValue = ''"><xsl:value-of select="." /></xsl:when>
					<xsl:otherwise><xsl:value-of select="floor(.)" /></xsl:otherwise>
				</xsl:choose>
			</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="remainingEffort"]' mode="taskSpecificFields">
		<xsl:variable name="remainningValue" as="xs:string" select="." />
			<field>
				<xsl:copy-of select="@*" />
				<xsl:attribute name="fieldName">Microsoft.VSTS.Scheduling.RemainingWork</xsl:attribute>
				<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
				<xsl:attribute name="fieldValueType">String</xsl:attribute>
				<xsl:choose>
					<xsl:when test="$remainningValue = ''"><xsl:value-of select="." /></xsl:when>
					<xsl:otherwise><xsl:value-of select="floor(.)" /></xsl:otherwise>
				</xsl:choose>
			</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="actualHours"]' mode="taskSpecificFields">
		<xsl:variable name="completedWorkValue" as="xs:string" select="." />
			<field>
				<xsl:copy-of select="@*" />
				<xsl:attribute name="fieldName">Microsoft.VSTS.Scheduling.CompletedWork</xsl:attribute>
				<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
				<xsl:attribute name="fieldValueType">String</xsl:attribute>
				<xsl:choose>
					<xsl:when test="$completedWorkValue = ''"><xsl:value-of select="." /></xsl:when>
					<xsl:otherwise><xsl:value-of select="floor(.)" /></xsl:otherwise>
				</xsl:choose>
			</field>
	</xsl:template>
	<!-- end of templates for task specific fields from TF to TFS -->
	<!-- begin of templates for user story specific fields from TF to TFS -->
	<xsl:template match='ccf:field[@fieldName="description"]' mode="userStorySpecificFields">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">System.Description</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="status"]' mode="userStorySpecificFields">
		<xsl:variable name="statusValue" as="xs:string" select="."/>
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">System.State</xsl:attribute>
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
	<xsl:template match='ccf:field[@fieldName="Risk"]' mode="userStorySpecificFields">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Microsoft.VSTS.Common.Risk</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="points"]' mode="userStorySpecificFields">
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Microsoft.VSTS.Scheduling.StoryPoints</xsl:attribute>
			<xsl:attribute name="fieldValueType">String</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<!-- end of templates for user story specific fields from TF to TFS -->
	
	<xsl:template match="text()" />
</xsl:stylesheet>