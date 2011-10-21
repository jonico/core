<?xml version="1.0" encoding="UTF-8"?>

<!--
	Copyright 2009 CollabNet, Inc. ("CollabNet")
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
-->

<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:ccf="http://ccf.open.collab.net/GenericArtifactV1.0"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	exclude-result-prefixes="xsl xs ccf stringutil">
	<xsl:variable name="dbid" select="//dbid" />
	<xsl:template match="/node()" priority="2">
		<artifact
			xmlns="http://ccf.open.collab.net/GenericArtifactV1.0">
			<xsl:attribute name="artifactAction">create</xsl:attribute>
			<xsl:attribute name="artifactMode">complete</xsl:attribute>
			<xsl:attribute name="artifactType">plainArtifact</xsl:attribute>
			<xsl:attribute name="sourceArtifactLastModifiedDate">unknown</xsl:attribute>
			<xsl:attribute name="targetArtifactLastModifiedDate">unknown</xsl:attribute>
			<xsl:attribute name="artifactLastReadTransactionId">unknown</xsl:attribute>
			<xsl:attribute name="sourceArtifactVersion">unknown</xsl:attribute>
			<xsl:attribute name="targetArtifactVersion">unknown</xsl:attribute>
			<xsl:attribute name="conflictResolutionPriority">alwaysOverride</xsl:attribute>
			<xsl:attribute name="sourceArtifactId"><xsl:value-of
					select="$dbid" />
			</xsl:attribute>
			<xsl:attribute name="sourceRepositoryId">input.txt</xsl:attribute>
			<xsl:attribute name="sourceRepositoryKind">CSVClearQuestFile</xsl:attribute>
			<xsl:attribute name="sourceSystemId">CSVFile</xsl:attribute>
			<xsl:attribute name="sourceSystemKind">Filesystem</xsl:attribute>
			<xsl:attribute name="targetRepositoryId">tracker1001</xsl:attribute>
			<xsl:attribute name="targetRepositoryKind">TRACKER</xsl:attribute>
			<xsl:attribute name="targetSystemId">cu011</xsl:attribute>
			<xsl:attribute name="targetSystemKind">CSFE 5.0</xsl:attribute>
			<xsl:attribute name="transactionId">0</xsl:attribute>
			<xsl:attribute name="errorCode">ok</xsl:attribute>
			<xsl:attribute name="includesFieldMetaData">false</xsl:attribute>
			<xsl:attribute name="targetArtifactId">unknown</xsl:attribute>
			<xsl:attribute name="sourceSystemTimezone">unknown</xsl:attribute>
			<!--						<xsl:attribute name="sourceSystemEncoding">unknown</xsl:attribute>-->
			<xsl:attribute name="targetSystemTimezone">unknown</xsl:attribute>
			<xsl:attribute name="sourceArtifactLastModifiedDate">unknown</xsl:attribute>
			<xsl:attribute name="depParentSourceArtifactId">unknown</xsl:attribute>
			<xsl:attribute name="depParentSourceRepositoryId">unknown</xsl:attribute>
			<xsl:attribute name="depParentSourceRepositoryKind">unknown</xsl:attribute>
			<xsl:attribute name="depParentTargetArtifactId">unknown</xsl:attribute>
			<xsl:attribute name="depParentTargetRepositoryId">unknown</xsl:attribute>
			<xsl:attribute name="depParentTargetRepositoryKind">unknown</xsl:attribute>
			
			<!--						<xsl:attribute name="targetSystemEncoding">unknown</xsl:attribute>-->
			<!-- Constant fields -->
			<!--<field>
				<xsl:attribute name="fieldName">customer</xsl:attribute>
				<xsl:attribute name="fieldAction">replace</xsl:attribute>
				<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
				<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				<xsl:attribute name="fieldValueType">String</xsl:attribute>
				<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				</field>-->
			<field>
				<xsl:attribute name="fieldName">description</xsl:attribute>
				<xsl:attribute name="fieldAction">replace</xsl:attribute>
				<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
				<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				<xsl:attribute name="fieldValueType">String</xsl:attribute>
				<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				The artifact was read in by a CSV file
			</field>
			<!--<field>
				<xsl:attribute name="fieldName">category</xsl:attribute>
				<xsl:attribute name="fieldAction">replace</xsl:attribute>
				<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
				<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				<xsl:attribute name="fieldValueType">String</xsl:attribute>
				<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				</field>
				<field>
				<xsl:attribute name="fieldName">group</xsl:attribute>
				<xsl:attribute name="fieldAction">replace</xsl:attribute>
				<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
				<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				<xsl:attribute name="fieldValueType">String</xsl:attribute>
				<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				</field>-->
			<field>
				<xsl:attribute name="fieldName">estimatedHours</xsl:attribute>
				<xsl:attribute name="fieldAction">replace</xsl:attribute>
				<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
				<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				<xsl:attribute name="fieldValueType">Integer</xsl:attribute>
				<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				<xsl:value-of select="50" />
			</field>
			<field>
				<xsl:attribute name="fieldName">actualHours</xsl:attribute>
				<xsl:attribute name="fieldAction">replace</xsl:attribute>
				<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
				<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				<xsl:attribute name="fieldValueType">Integer</xsl:attribute>
				<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				<xsl:value-of select="25" />
			</field>
			<xsl:apply-templates />
		</artifact>
	</xsl:template>
	<!-- Dynamic fields -->
	<xsl:template match="node()" priority="1">
		<xsl:variable name="fieldName" as="xs:string">
			<xsl:value-of select="local-name()" />
		</xsl:variable>
		<xsl:if test="$fieldName = 'Headline'">
			<field>
				<xsl:attribute name="fieldName">title</xsl:attribute>
				<xsl:attribute name="fieldAction">replace</xsl:attribute>
				<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
				<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				<xsl:attribute name="fieldValueType">String</xsl:attribute>
				<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				<xsl:value-of select="text()" />
			</field>
		</xsl:if>
		<xsl:if test="$fieldName = 'State'">
			<field>
				<xsl:attribute name="fieldName">status</xsl:attribute>
				<xsl:attribute name="fieldAction">replace</xsl:attribute>
				<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
				<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				<xsl:attribute name="fieldValueType">String</xsl:attribute>
				<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				<xsl:value-of select="text()" />
			</field>
		</xsl:if>
		<xsl:if test="$fieldName = 'Priority'">
			<xsl:variable name="priorityValue" as="xs:string">
				<xsl:value-of select="." />
			</xsl:variable>
			<field>
				<xsl:attribute name="fieldName">priority</xsl:attribute>
				<xsl:attribute name="fieldDisplayName"><xsl:value-of
						select="$fieldName" />
				</xsl:attribute>
				<xsl:attribute name="fieldAction">replace</xsl:attribute>
				<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
				<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				<xsl:attribute name="fieldValueType">Integer</xsl:attribute>
				<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				<xsl:choose>
					<xsl:when
						test="$priorityValue = '3-Normal Queue'">
						<xsl:text>3</xsl:text>
					</xsl:when>
					<xsl:when
						test="$priorityValue = '2-Give High Attention'">
						<xsl:text>2</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>1</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</field>
		</xsl:if>
		<xsl:if test="$fieldName = 'Owner'">
			<field>
				<xsl:attribute name="fieldName">assignedTo</xsl:attribute>
				<xsl:attribute name="fieldAction">replace</xsl:attribute>
				<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
				<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				<xsl:attribute name="fieldValueType">String</xsl:attribute>
				<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				<xsl:value-of select="text()" />
			</field>
		</xsl:if>
		<!--<xsl:if test="$fieldName = 'reportedReleaseId'">
			<field>
			<xsl:attribute name="fieldName">reportedReleaseId</xsl:attribute>
			<xsl:attribute name="fieldAction">replace</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
			<xsl:attribute name="fieldValueType">String</xsl:attribute>
			<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
			<xsl:value-of select="text()"/>
			</field>
			</xsl:if>-->
		<!--<xsl:if test="$fieldName = 'resolvedReleaseId'">
			<field>
			<xsl:attribute name="fieldName">resolvedReleaseId</xsl:attribute>
			<xsl:attribute name="fieldAction">replace</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
			<xsl:attribute name="fieldValueType">String</xsl:attribute>
			<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
			<xsl:value-of select="text()"/>
			</field>
			</xsl:if>-->
		<xsl:if test="$fieldName = 'dbid'">
			<field>
				<xsl:attribute name="fieldName">dbid</xsl:attribute>
				<xsl:attribute name="fieldAction">replace</xsl:attribute>
				<xsl:attribute name="fieldType">flexField</xsl:attribute>
				<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				<xsl:attribute name="fieldValueType">String</xsl:attribute>
				<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				<xsl:value-of select="text()" />
			</field>
		</xsl:if>
		<xsl:if test="$fieldName = 'artifactId'">
			<field>
				<xsl:attribute name="fieldName">id</xsl:attribute>
				<xsl:attribute name="fieldAction">replace</xsl:attribute>
				<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
				<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				<xsl:attribute name="fieldValueType">String</xsl:attribute>
				<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				<xsl:value-of select="text()" />
			</field>
		</xsl:if>
		<xsl:if test="$fieldName = 'id'">
			<field>
				<xsl:attribute name="fieldName">id</xsl:attribute>
				<xsl:attribute name="fieldAction">replace</xsl:attribute>
				<xsl:attribute name="fieldType">flexField</xsl:attribute>
				<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				<xsl:attribute name="fieldValueType">String</xsl:attribute>
				<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				<xsl:value-of select="text()" />
			</field>
		</xsl:if>
		<xsl:if test="$fieldName = 'is_active'">
			<field>
				<xsl:attribute name="fieldName">is_active</xsl:attribute>
				<xsl:attribute name="fieldAction">replace</xsl:attribute>
				<xsl:attribute name="fieldType">flexField</xsl:attribute>
				<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				<xsl:attribute name="fieldValueType">String</xsl:attribute>
				<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				<xsl:value-of select="text()" />
			</field>
		</xsl:if>
		<xsl:if test="$fieldName = 'is_duplicate'">
			<field>
				<xsl:attribute name="fieldName">is_duplicate</xsl:attribute>
				<xsl:attribute name="fieldAction">replace</xsl:attribute>
				<xsl:attribute name="fieldType">flexField</xsl:attribute>
				<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				<xsl:attribute name="fieldValueType">String</xsl:attribute>
				<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				<xsl:value-of select="text()" />
			</field>
		</xsl:if>
		<xsl:if test="$fieldName = 'locked_by'">
			<field>
				<xsl:attribute name="fieldName">locked_by</xsl:attribute>
				<xsl:attribute name="fieldAction">replace</xsl:attribute>
				<xsl:attribute name="fieldType">flexField</xsl:attribute>
				<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				<xsl:attribute name="fieldValueType">String</xsl:attribute>
				<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				<xsl:value-of select="text()" />
			</field>
		</xsl:if>
		<xsl:if test="$fieldName = 'lock_version'">
			<field>
				<xsl:attribute name="fieldName">lock_version</xsl:attribute>
				<xsl:attribute name="fieldAction">replace</xsl:attribute>
				<xsl:attribute name="fieldType">flexField</xsl:attribute>
				<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				<xsl:attribute name="fieldValueType">String</xsl:attribute>
				<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				<xsl:value-of select="text()" />
			</field>
		</xsl:if>
		<xsl:if test="$fieldName = 'old_id'">
			<field>
				<xsl:attribute name="fieldName">old_id</xsl:attribute>
				<xsl:attribute name="fieldAction">replace</xsl:attribute>
				<xsl:attribute name="fieldType">flexField</xsl:attribute>
				<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				<xsl:attribute name="fieldValueType">String</xsl:attribute>
				<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				<xsl:value-of select="text()" />
			</field>
		</xsl:if>
		<xsl:if test="$fieldName = 'record_type'">
			<field>
				<xsl:attribute name="fieldName">record_type</xsl:attribute>
				<xsl:attribute name="fieldAction">replace</xsl:attribute>
				<xsl:attribute name="fieldType">flexField</xsl:attribute>
				<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				<xsl:attribute name="fieldValueType">String</xsl:attribute>
				<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				<xsl:value-of select="text()" />
			</field>
		</xsl:if>
		<xsl:if test="$fieldName = 'Resolution'">
			<field>
				<xsl:attribute name="fieldName">Resolution</xsl:attribute>
				<xsl:attribute name="fieldAction">replace</xsl:attribute>
				<xsl:attribute name="fieldType">flexField</xsl:attribute>
				<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				<xsl:attribute name="fieldValueType">String</xsl:attribute>
				<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				<xsl:value-of select="text()" />
			</field>
		</xsl:if>
		<xsl:if test="$fieldName = 'Resolution_Statetype'">
			<field>
				<xsl:attribute name="fieldName">Resolution_Statetype</xsl:attribute>
				<xsl:attribute name="fieldAction">replace</xsl:attribute>
				<xsl:attribute name="fieldType">flexField</xsl:attribute>
				<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				<xsl:attribute name="fieldValueType">String</xsl:attribute>
				<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				<xsl:value-of select="text()" />
			</field>
		</xsl:if>
		<xsl:if test="$fieldName = 'Severity'">
			<field>
				<xsl:attribute name="fieldName">severity</xsl:attribute>
				<xsl:attribute name="fieldAction">replace</xsl:attribute>
				<xsl:attribute name="fieldType">flexField</xsl:attribute>
				<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				<xsl:attribute name="fieldValueType">String</xsl:attribute>
				<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				<xsl:value-of select="text()" />
			</field>
		</xsl:if>
		<xsl:if test="$fieldName = 'version'">
			<field>
				<xsl:attribute name="fieldName">version</xsl:attribute>
				<xsl:attribute name="fieldAction">replace</xsl:attribute>
				<xsl:attribute name="fieldType">flexField</xsl:attribute>
				<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				<xsl:attribute name="fieldValueType">String</xsl:attribute>
				<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				<xsl:value-of select="text()" />
			</field>
		</xsl:if>

		<xsl:if test="$fieldName = 'Submit_Date'">
			<field>
				<xsl:attribute name="fieldName">Submit_Date</xsl:attribute>
				<xsl:attribute name="fieldAction">replace</xsl:attribute>
				<xsl:attribute name="fieldType">flexField</xsl:attribute>
				<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				<xsl:attribute name="fieldValueType">String</xsl:attribute>
				<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				<xsl:value-of select="text()" />
			</field>
		</xsl:if>
		<xsl:if test="$fieldName = 'Submitter'">
			<field>
				<xsl:attribute name="fieldName">Submitter</xsl:attribute>
				<xsl:attribute name="fieldAction">replace</xsl:attribute>
				<xsl:attribute name="fieldType">flexField</xsl:attribute>
				<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				<xsl:attribute name="fieldValueType">User</xsl:attribute>
				<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				<xsl:value-of select="text()" />
			</field>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
