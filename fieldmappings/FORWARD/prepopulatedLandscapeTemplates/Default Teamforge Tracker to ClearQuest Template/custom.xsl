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
		
		Teamforge Tracker -> ClearQuest
	-->
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ccf="http://ccf.open.collab.net/GenericArtifactV1.0"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:stringutil="xalan://com.collabnet.ccf.core.utils.StringUtils"
	exclude-result-prefixes="xsl xs ccf stringutil" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.w3.org/1999/XSL/Transform http://www.w3.org/2007/schema-for-xslt20.xsd">

	<!--  This key can be used for every  mapping -->
	<xsl:key name='mappingKey' match='mapping' use='@teamforge' />


	<xsl:template name="getCQUserName">
		<xsl:variable name="originalUserName" select="."/>
	
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
						<xsl:value-of
							select='key("mappingKey","default")/@clearquest' />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
		</xsl:if>
	</xsl:template>


	<!-- template starts here, then calls all other templates to fill in their parts. -->
	<!-- named templates without 'match' won't put anything in, they need to be called. -->
	<xsl:template match='/ccf:artifact[@artifactType = "plainArtifact"]'>
		<artifact xmlns="http://ccf.open.collab.net/GenericArtifactV1.0">
			<xsl:copy-of select="@*" />
			<xsl:apply-templates />
		</artifact>
	</xsl:template>

	<xsl:template
		match='ccf:field[@fieldName="assignedTo"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Owner</xsl:attribute>
			<xsl:call-template name="getCQUserName"/>
		</field>
	</xsl:template>

	<xsl:template match="/ccf:artifact[@artifactType = 'attachment']">
		<xsl:copy-of select="." />
	</xsl:template>

	<xsl:template match='ccf:field[@fieldName="id"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">CTF-ID</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	
	<xsl:template match='ccf:field[@fieldName="title"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Headline</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>

	<!-- multiple comments will be inserted separately into the target field using separate edit sesssions -->
	<!-- This is currently used to create notes?log entries using the 'Note_Entry" field -->
	<xsl:template match='ccf:field[@fieldName="Comment Text"]'>
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">Note_Entry</xsl:attribute>
			<xsl:attribute name="fieldType">notesField</xsl:attribute>
			<xsl:value-of select="stringutil:stripHTML(string(.))"/>
		</field>
	</xsl:template>

	<xsl:template match='ccf:field[@fieldName="description"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Description</xsl:attribute>
			<xsl:value-of select="stringutil:stripHTML(string(.))"/>
<!-- 			<xsl:value-of select="."/> -->
		</field>
	</xsl:template>
	
<!-- 	<xsl:template match='ccf:field[@fieldName="priority"]'> -->
<!-- 		<xsl:variable name="priorityValue" as="xs:string" select="."/> -->
<!-- 		<field> -->
<!-- 			<xsl:copy-of select="@*"/> -->
<!-- 			<xsl:attribute name="fieldName">Priority</xsl:attribute> -->
<!-- 			<xsl:if test="$priorityValue = '5'"> -->
<!-- 				<xsl:text>4-Low Priority</xsl:text> -->
<!-- 			</xsl:if> -->
<!-- 			<xsl:if test="$priorityValue = '4'"> -->
<!-- 				<xsl:text>4-Low Priority</xsl:text> -->
<!-- 			</xsl:if> -->
<!-- 			<xsl:if test="$priorityValue = '3'"> -->
<!-- 				<xsl:text>3-Normal Queue</xsl:text> -->
<!-- 			</xsl:if> -->
<!-- 			<xsl:if test="$priorityValue = '2'"> -->
<!-- 				<xsl:text>2-Give High Attention</xsl:text> -->
<!-- 			</xsl:if> -->
<!-- 			<xsl:if test="$priorityValue = '1'"> -->
<!-- 				<xsl:text>1-Resolve Immediately</xsl:text> -->
<!-- 			</xsl:if> -->
<!-- 		</field> -->
<!-- 	</xsl:template> -->

	<!-- with the same values on both sides, the if tests are actually not necessary -->
	<xsl:template match='ccf:field[@fieldName="Severity"]'>
		<xsl:variable name="severityValue" as="xs:string" select="."/>
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">Severity</xsl:attribute>
			<xsl:if test="$severityValue = '1-Critical'">
				<xsl:text>1-Critical</xsl:text>
			</xsl:if>
			<xsl:if test="$severityValue = '2-Major'">
				<xsl:text>2-Major</xsl:text>
			</xsl:if>
			<xsl:if test="$severityValue = '3-Average'">
				<xsl:text>3-Average</xsl:text>
			</xsl:if>
			<xsl:if test="$severityValue = '4-Minor'">
				<xsl:text>4-Minor</xsl:text>
			</xsl:if>
			<xsl:if test="$severityValue = '5-Enhancement'">
				<xsl:text>5-Enhancement</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
		
	<!-- in clearquest, the state field cannot be set by itself -->
	<!-- instead, a transition needs to be executed -->
	<xsl:template match='ccf:field[@fieldName="status"]'>
		<xsl:variable name="statusValue" as="xs:string" select="."/>
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">State</xsl:attribute>
			<!-- a fieldType of "transitionField" will affect the action in CQ -->
			<xsl:attribute name="fieldType">transitionField</xsl:attribute>
			<xsl:if test="$statusValue = 'Open'">
				<xsl:text>Submitted</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'In Progress'">
				<xsl:text>UnderAnalysis</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Fixed'">
				<xsl:text>Resolved</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Pending'">
				<xsl:text>Opened</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Closed'">
				<xsl:text>Closed</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Re-opened (not fixed)'">
				<xsl:text>Opened</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Cannot reproduce'">
				<xsl:text>Postponed</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Not a bug'">
				<xsl:text>Postponed</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Duplicate'">
				<xsl:text>Duplicate</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	
	<xsl:template match="text()"/>
	
	
</xsl:stylesheet>
