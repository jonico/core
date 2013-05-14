<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ccf="http://ccf.open.collab.net/GenericArtifactV1.0" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:stringutil="xalan://com.collabnet.ccf.core.utils.StringUtils" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="2.0" exclude-result-prefixes="xsl xs ccf stringutil" xsi:schemaLocation="http://www.w3.org/1999/XSL/Transform http://www.w3.org/2007/schema-for-xslt20.xsd">

	<!-- This key can be used for every mapping -->
	<xsl:key name="mappingKey" match="mapping" use="@teamforge"/>


	<xsl:template name="getCQUserName">
		<xsl:variable name="originalUserName" select="."/>

		<xsl:if test="$originalUserName != ''">
			<xsl:for-each select="document('../../userMappings.xml')/SchneiderUserMappingStructure/SchneiderUserMapping">
				<xsl:variable name="valueLookUpResult" as="xs:string" select="key(&quot;mappingKey&quot;,$originalUserName)/@clearquest"/>
				<xsl:choose>
					<xsl:when test="$valueLookUpResult!=&quot;&quot;">
						<xsl:value-of select="$valueLookUpResult"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="key(&quot;mappingKey&quot;,&quot;default&quot;)/@clearquest"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
		</xsl:if>
	</xsl:template>


	<!-- template starts here, then calls all other templates to fill in their 
		parts. -->
	<!-- named templates without 'match' won't put anything in, they need to 
		be called. -->
	<xsl:template match="/ccf:artifact[@artifactType = &quot;plainArtifact&quot;]">
		<artifact xmlns="http://ccf.open.collab.net/GenericArtifactV1.0">
			<xsl:copy-of select="@*"/>
			<xsl:apply-templates/>
			<xsl:if test="not(ccf:field[@fieldName=&quot;Product&quot;])">
				<field>
					<xsl:attribute name="fieldName">Product</xsl:attribute>
					<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
					<xsl:attribute name="fieldAction">replace</xsl:attribute>
					<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
					<xsl:attribute name="fieldValueType">String</xsl:attribute>
					<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
					<xsl:text>RSU</xsl:text>
				</field>
			</xsl:if>
			<xsl:if test="not(ccf:field[@fieldName=&quot;Project&quot;])">
				<field>
					<xsl:attribute name="fieldName">Project</xsl:attribute>
					<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
					<xsl:attribute name="fieldAction">replace</xsl:attribute>
					<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
					<xsl:attribute name="fieldValueType">String</xsl:attribute>
					<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
					<xsl:text>LVSA</xsl:text>
				</field>
			</xsl:if>
		</artifact>
	</xsl:template>

	<xsl:template match="ccf:field[@fieldName=&quot;assignedTo&quot;]">
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">Owner</xsl:attribute>
			<xsl:call-template name="getCQUserName"/>
		</field>
	</xsl:template>

	<xsl:template match="/ccf:artifact[@artifactType = 'attachment']">
		<xsl:copy-of select="."/>
	</xsl:template>

	<xsl:template match="ccf:field[@fieldName=&quot;id&quot;]">
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">CTF-ID</xsl:attribute>
			<xsl:value-of select="."/>
		</field>
	</xsl:template>

	<xsl:template match="ccf:field[@fieldName=&quot;title&quot;]">
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">Headline</xsl:attribute>
			<xsl:value-of select="."/>
		</field>
	</xsl:template>

	<!-- multiple comments will be inserted separately into the target field 
		using separate edit sesssions -->
	<!-- This is currently used to create notes?log entries using the 'Note_Entry" 
		field -->
	<xsl:template match="ccf:field[@fieldName=&quot;Comment Text&quot;]">
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">Note_Entry</xsl:attribute>
			<xsl:attribute name="fieldType">notesField</xsl:attribute>
			<xsl:value-of select="stringutil:stripHTML(string(.))"/>
		</field>
	</xsl:template>

	<xsl:template match="ccf:field[@fieldName=&quot;description&quot;]">
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">Description</xsl:attribute>
			<xsl:value-of select="stringutil:stripHTML(string(.))"/>
			<!-- <xsl:value-of select="."/> -->
		</field>
	</xsl:template>

	<xsl:template match="ccf:field[@fieldName=&quot;priority&quot;]">
		<xsl:variable name="priorityValue" as="xs:string" select="."/>
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">Priority</xsl:attribute>
			<xsl:if test="$priorityValue = '5'">
				<xsl:text>4-Low Priority</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = '4'">
				<xsl:text>4-Low Priority</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = '3'">
				<xsl:text>3-Normal Queue</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = '2'">
				<xsl:text>2-Give High Attention</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = '1'">
				<xsl:text>1-Resolve Immediately</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>

	<!-- with the same values on both sides, the if tests are actually not necessary -->
	<xsl:template match="ccf:field[@fieldName=&quot;Severity&quot;]">
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
	<xsl:template match="ccf:field[@fieldName=&quot;status&quot;]">
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
				<xsl:text>Assigned</xsl:text>
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

	<!-- <xsl:template match='ccf:field[@fieldName="Test Date"]'> -->
	<!-- <field> -->
	<!-- <xsl:copy-of select="@*"/> -->
	<!-- <xsl:attribute name="fieldName">BG_USER_03</xsl:attribute> -->
	<!-- <xsl:value-of select="."/> -->
	<!-- </field> -->
	<!-- </xsl:template> -->

	<!-- <xsl:template match='ccf:field[@fieldName="assignedTo"]'> -->
	<!-- <xsl:variable name="assignedTo" as="xs:string" select="."/> -->
	<!-- <field> -->
	<!-- <xsl:copy-of select="@*"/> -->
	<!-- <xsl:attribute name="fieldName">BG_RESPONSIBLE</xsl:attribute> -->
	<!-- <xsl:choose> -->
	<!-- <xsl:when test="$assignedTo != 'nobody'"> -->
	<!-- <xsl:value-of select="."/> -->
	<!-- </xsl:when> -->
	<!-- </xsl:choose> -->
	<!-- </field> -->
	<!-- </xsl:template> -->

	<xsl:template match="text()"/>


</xsl:stylesheet>