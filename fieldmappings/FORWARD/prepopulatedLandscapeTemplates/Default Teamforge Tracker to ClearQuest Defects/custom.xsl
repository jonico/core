<?xml version="1.0"?>
<!-- Teamforge Tracker ==> ClearQuest Defects -->

<!-- Copyright 2009 CollabNet, Inc. ("CollabNet") Licensed under the Apache 
	License, Version 2.0 (the "License"); you may not use this file except in 
	compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 
	Unless required by applicable law or agreed to in writing, software distributed 
	under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES 
	OR CONDITIONS OF ANY KIND, either express or implied. See the License for 
	the specific language governing permissions and limitations under the License. --> 
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ccf="http://ccf.open.collab.net/GenericArtifactV1.0"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:stringutil="xalan://com.collabnet.ccf.core.utils.StringUtils"
	exclude-result-prefixes="xsl xs ccf stringutil" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.w3.org/1999/XSL/Transform http://www.w3.org/2007/schema-for-xslt20.xsd">

	<!-- This key can be used for every mapping -->
	<xsl:key name='mappingKey' match='mapping' use='@teamforge' />


	<xsl:template name="getCQUserName">
		<xsl:variable name="originalUserName" select="." />

		<xsl:if test="$originalUserName != ''">
			<xsl:for-each
				select="document('../../userMappings.xml')/SchneiderUserMappingStructure/SchneiderUserMapping">
				<xsl:variable name="valueLookUpResult" as="xs:string"
					select='key("mappingKey",$originalUserName)/@clearquest' />
				<xsl:choose>
					<xsl:when test='$valueLookUpResult!=""'>
						<xsl:value-of select='$valueLookUpResult' />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select='key("mappingKey","default")/@clearquest' />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
		</xsl:if>
	</xsl:template>


	<!-- template starts here, then calls all other templates to fill in their 
		parts. -->
	<!-- named templates without 'match' won't put anything in, they need to 
		be called. -->
	<xsl:template match='/ccf:artifact[@artifactType = "plainArtifact"]'>
		<artifact xmlns="http://ccf.open.collab.net/GenericArtifactV1.0">
			<xsl:copy-of select="@*" />
			<xsl:variable name="statusFieldValue" as="xs:string" select="/ccf:artifact/ccf:field[@fieldName='status']" />
			<!-- excluded states, won't be shipped -->
			<xsl:if test="	$statusFieldValue = 'New' or
							$statusFieldValue = 'Submitted' or 
							$statusFieldValue = 'UnderVerification' or 
							$statusFieldValue = 'Verified' or
							$statusFieldValue = 'UnderValidation' or
							$statusFieldValue = 'Validated'">
        		<xsl:attribute name="artifactAction">ignore</xsl:attribute>
			</xsl:if>
			<xsl:apply-templates />
		</artifact>
	</xsl:template>

	<xsl:template match='ccf:field[@fieldName="assignedTo"]'>
		<xsl:variable name="assignedUserLogin" as="xs:string" select="."></xsl:variable>
		<xsl:if test="$assignedUserLogin!='nobody'">
			<field>
				<xsl:copy-of select="@*" />
				<xsl:attribute name="fieldName">Owner</xsl:attribute>
				<xsl:value-of select="."/>
			</field>
		</xsl:if>
	</xsl:template>

	<xsl:template match="/ccf:artifact[@artifactType = 'attachment']">
		<xsl:copy-of select="." />
	</xsl:template>

	<xsl:template match='ccf:field[@fieldName="actualHours"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">ActualEffort</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>

<!-- 	<xsl:template match='ccf:field[@fieldName="reportedReleaseId"]'> -->
<!-- 		<field> -->
<!-- 			<xsl:copy-of select="@*" /> -->
<!-- 			<xsl:attribute name="fieldName">CorrectionVersion</xsl:attribute> -->
<!-- 			<xsl:value-of select="."/> -->
<!-- 		</field> -->
<!-- 	</xsl:template> -->
	

	<xsl:template match='ccf:field[@fieldName="description"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Description</xsl:attribute>
			<xsl:value-of select="stringutil:stripHTML(string(.))" />
			<!-- <xsl:value-of select="."/> -->
		</field>
	</xsl:template>

	<xsl:template match='ccf:field[@fieldName="estimatedHours"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">EstimatedEffort</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>

	<xsl:template match='ccf:field[@fieldName="title"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Headline</xsl:attribute>
			<xsl:value-of select="."/>
		</field>
	</xsl:template>

	<!-- multiple comments will be inserted separately into the target field 
		using separate edit sesssions -->
	<!-- The 'Note_Entry" field will create Notes_Log entries-->
	<xsl:template match='ccf:field[@fieldName="Comment Text"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Note_Entry</xsl:attribute>
			<xsl:attribute name="fieldType">notesField</xsl:attribute>
			<xsl:value-of select="stringutil:stripHTML(string(.))" />
		</field>
	</xsl:template>

	<xsl:template match='ccf:field[@fieldName="id"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">CTFid</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>

	<xsl:template match='ccf:field[@fieldName="Owner Full Name"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">OwnerFullName</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>

	<xsl:template match='ccf:field[@fieldName="Product"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Product</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>

	<xsl:template match='ccf:field[@fieldName="Product Configuration"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">ProductConfiguration</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>

	<xsl:template match='ccf:field[@fieldName="Project"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Project</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>

	<xsl:template match='ccf:field[@fieldName="Resolution"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Resolution</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>

	<xsl:template match='ccf:field[@fieldName="priority"]'>
		<xsl:variable name="priorityValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Severity</xsl:attribute>
			<xsl:choose>
				<xsl:when test="$priorityValue = '1'">
					<xsl:text>1-Critical</xsl:text>
				</xsl:when>
				<xsl:when test="$priorityValue = '2'">
					<xsl:text>2-Major</xsl:text>
				</xsl:when>
				<xsl:when test="$priorityValue = '3'">
					<xsl:text>3-Average</xsl:text>
				</xsl:when>
				<xsl:when test="$priorityValue = '4'">
					<xsl:text>4-Minor</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>4-Minor</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</field>
	</xsl:template>


	<!-- in clearquest, the state field cannot be set by itself -->
	<!-- instead, a transition needs to be executed -->
	<xsl:template match='ccf:field[@fieldName="status"]'>
		<xsl:variable name="statusValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">State</xsl:attribute>
			<!-- a fieldType of "transitionField" will trigger the first available action in CQ -->
			<!-- that transitions between the current and the new state. If there is no
			     transition, the artifact will be hospitalized -->
			<xsl:attribute name="fieldType">transitionField</xsl:attribute>
			<xsl:if test="$statusValue = 'UnderAnalysis'">
				<xsl:text>UnderAnalysis</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Analyzed'">
				<xsl:text>Analyzed</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'UnderCorrection'">
				<xsl:text>UnderCorrection</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Corrected'">
				<xsl:text>Corrected</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Postponed'">
				<xsl:text>Postponed</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Closed'">
				<xsl:text>Closed</xsl:text>
			</xsl:if>
			<!-- other status values will either be skipped (see top of file) or hospitalized -->
		</field>
	</xsl:template>
	
	<xsl:template match="text()" />


</xsl:stylesheet>
