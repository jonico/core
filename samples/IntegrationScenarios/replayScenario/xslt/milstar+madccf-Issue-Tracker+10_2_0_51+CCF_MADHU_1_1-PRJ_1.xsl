<?xml version="1.0"?>

<!-- 
	If there is a need to do so, I can extend the XsltProcessor component
	that it passes global parameters to this XSLT-Script
	
	XSLT allows you to use global variables (stylesheet parameters),
	template parameters, modes and function parameters  
-->

<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ccf="http://ccf.open.collab.net/GenericArtifactV1.0"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:stringutil="xalan://com.collabnet.ccf.core.utils.GATransformerUtil"
	xmlns:ptutil="xalan://com.collabnet.ccf.pi.cee.pt.v50.ProjectTrackerHelper"
	exclude-result-prefixes="xsl xs">
	<xsl:variable name="sourceArtifactId" as="xs:string" select="/ccf:artifact/@sourceArtifactId"></xsl:variable>
	<xsl:variable name="artifactTypeNameSpace" as="xs:string">
        <xsl:value-of select="ptutil:getNamespaceWithBraces($sourceArtifactId)"/>
    </xsl:variable>
	<xsl:template match='/ccf:artifact[@artifactType = "plainArtifact"]'>
		<artifact xmlns="http://ccf.open.collab.net/GenericArtifactV1.0">
			<xsl:attribute name="artifactAction"><xsl:value-of select="@artifactAction" /></xsl:attribute>
			<xsl:attribute name="artifactMode"><xsl:value-of select="@artifactMode" /></xsl:attribute>
			<xsl:attribute name="artifactType"><xsl:value-of select="@artifactType" /></xsl:attribute>
			<xsl:attribute name="sourceArtifactLastModifiedDate"><xsl:value-of select="@sourceArtifactLastModifiedDate" /></xsl:attribute>
			<xsl:attribute name="targetArtifactLastModifiedDate"><xsl:value-of select="@targetArtifactLastModifiedDate" /></xsl:attribute>
			<xsl:attribute name="transactionId"><xsl:value-of select="@transactionId" /></xsl:attribute>
			<xsl:attribute name="sourceArtifactVersion"><xsl:value-of select="@sourceArtifactVersion" /></xsl:attribute>
			<xsl:attribute name="targetArtifactVersion"><xsl:value-of select="@targetArtifactVersion" /></xsl:attribute>
			<xsl:attribute name="errorCode"><xsl:value-of select="@errorCode" /></xsl:attribute>
			<xsl:attribute name="includesFieldMetaData"><xsl:value-of select="@includesFieldMetaData" /></xsl:attribute>
			<xsl:attribute name="conflictResolutionPriority"><xsl:value-of select="@conflictResolutionPriority" /></xsl:attribute>
			<xsl:attribute name="sourceArtifactId"><xsl:value-of select="@sourceArtifactId" /></xsl:attribute>
			<xsl:attribute name="sourceRepositoryId"><xsl:value-of select="@sourceRepositoryId" /></xsl:attribute>
			<xsl:attribute name="sourceRepositoryKind"><xsl:value-of select="@sourceRepositoryKind" /></xsl:attribute>
			<xsl:attribute name="sourceSystemId"><xsl:value-of select="@sourceSystemId" /></xsl:attribute>
			<xsl:attribute name="sourceSystemKind"><xsl:value-of select="@sourceSystemKind" /></xsl:attribute>
			<xsl:attribute name="targetArtifactId"><xsl:value-of select="@targetArtifactId" /></xsl:attribute>
			<xsl:attribute name="targetRepositoryId"><xsl:value-of select="@targetRepositoryId" /></xsl:attribute>
			<xsl:attribute name="targetRepositoryKind"><xsl:value-of select="@targetRepositoryKind" /></xsl:attribute>
			<xsl:attribute name="targetSystemId"><xsl:value-of select="@targetSystemId" /></xsl:attribute>
			<xsl:attribute name="targetSystemKind"><xsl:value-of select="@targetSystemKind" /></xsl:attribute>
			<xsl:attribute name="depParentSourceArtifactId"><xsl:value-of select="@depParentSourceArtifactId" /></xsl:attribute>
			<xsl:attribute name="depParentSourceRepositoryId"><xsl:value-of select="@depParentSourceRepositoryId" /></xsl:attribute>
			<xsl:attribute name="depParentSourceRepositoryKind"><xsl:value-of select="@depParentSourceRepositoryKind" /></xsl:attribute>
			<xsl:attribute name="depParentTargetArtifactId"><xsl:value-of select="@depParentTargetArtifactId" /></xsl:attribute>
			<xsl:attribute name="depParentTargetRepositoryId"><xsl:value-of select="@depParentTargetRepositoryId" /></xsl:attribute>
			<xsl:attribute name="depParentTargetRepositoryKind"><xsl:value-of select="@depParentTargetRepositoryKind" /></xsl:attribute>
			<xsl:apply-templates></xsl:apply-templates>
			<xsl:variable name="assignedTo" as="xs:string" select="concat($artifactTypeNameSpace,'Assigned')" />
            <xsl:for-each
                select='ccf:field[@fieldName=string($assignedTo)]'>
                <xsl:if test="position()=last()">
                    <field>
                        <xsl:attribute name="fieldName">BG_RESPONSIBLE</xsl:attribute>
                        <xsl:attribute name="fieldDisplayName">Assigned to</xsl:attribute>
                        <xsl:attribute name="fieldAction"><xsl:value-of select="@fieldAction" /></xsl:attribute>
                        <xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
                        <xsl:attribute name="fieldValueHasChanged"><xsl:value-of select="@fieldValueHasChanged" /></xsl:attribute>
                        <xsl:attribute name="fieldValueType"><xsl:value-of select="@fieldValueType" /></xsl:attribute>
                        <xsl:attribute name="fieldValueIsNull"><xsl:value-of select="@fieldValueIsNull" /></xsl:attribute>
                        <xsl:attribute name="minOccurs"><xsl:value-of select="@minOccurs" /></xsl:attribute>
                        <xsl:attribute name="maxOccurs"><xsl:value-of select="@maxOccurs" /></xsl:attribute>
                        <xsl:attribute name="nullValueSupported"><xsl:value-of select="@nullValueSupported" /></xsl:attribute>
                        <xsl:attribute name="alternativeFieldName"><xsl:value-of select="@alternativeFieldName" /></xsl:attribute>
                        <xsl:value-of select="."></xsl:value-of>
                    </field>
                </xsl:if>
            </xsl:for-each>
            <xsl:variable name="qaContact" as="xs:string" select="concat($artifactTypeNameSpace,'QA_Contact')" />
            <xsl:for-each
		        select='ccf:field[@fieldName=string($qaContact)]'>
		        <xsl:if test="position()=last()">
			        <field>
			            <xsl:attribute name="fieldName">BG_USER_06</xsl:attribute>
			            <xsl:attribute name="fieldDisplayName">QA contact</xsl:attribute>
			            <xsl:attribute name="fieldAction"><xsl:value-of select="@fieldAction" /></xsl:attribute>
			            <xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			            <xsl:attribute name="fieldValueHasChanged"><xsl:value-of select="@fieldValueHasChanged" /></xsl:attribute>
			            <xsl:attribute name="fieldValueType"><xsl:value-of select="@fieldValueType" /></xsl:attribute>
			            <xsl:attribute name="fieldValueIsNull"><xsl:value-of select="@fieldValueIsNull" /></xsl:attribute>
			            <xsl:attribute name="minOccurs"><xsl:value-of select="@minOccurs" /></xsl:attribute>
			            <xsl:attribute name="maxOccurs"><xsl:value-of select="@maxOccurs" /></xsl:attribute>
			            <xsl:attribute name="nullValueSupported"><xsl:value-of select="@nullValueSupported" /></xsl:attribute>
			            <xsl:attribute name="alternativeFieldName"><xsl:value-of select="@alternativeFieldName" /></xsl:attribute>
			            <xsl:value-of select="."></xsl:value-of>
			        </field>
		        </xsl:if>
		    </xsl:for-each>
		    <xsl:variable name="ccField" as="xs:string" select="concat($artifactTypeNameSpace,'Cc')" />
		    <xsl:for-each
		        select='ccf:field[@fieldName=string($ccField)]'>
		        <xsl:if test="position()=last()">
			        <field>
			            <xsl:attribute name="fieldName">BG_USER_05</xsl:attribute>
			            <xsl:attribute name="fieldDisplayName">CC</xsl:attribute>
			            <xsl:attribute name="fieldAction"><xsl:value-of select="@fieldAction" /></xsl:attribute>
			            <xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			            <xsl:attribute name="fieldValueHasChanged"><xsl:value-of select="@fieldValueHasChanged" /></xsl:attribute>
			            <xsl:attribute name="fieldValueType"><xsl:value-of select="@fieldValueType" /></xsl:attribute>
			            <xsl:attribute name="fieldValueIsNull"><xsl:value-of select="@fieldValueIsNull" /></xsl:attribute>
			            <xsl:attribute name="minOccurs"><xsl:value-of select="@minOccurs" /></xsl:attribute>
			            <xsl:attribute name="maxOccurs"><xsl:value-of select="@maxOccurs" /></xsl:attribute>
			            <xsl:attribute name="nullValueSupported"><xsl:value-of select="@nullValueSupported" /></xsl:attribute>
			            <xsl:attribute name="alternativeFieldName"><xsl:value-of select="@alternativeFieldName" /></xsl:attribute>
			            <xsl:value-of select="."></xsl:value-of>
			        </field>
		        </xsl:if>
		    </xsl:for-each>
		</artifact>
	</xsl:template>
	<xsl:template match="/ccf:artifact[@artifactType = 'attachment']">
		<xsl:copy-of select="."/>
	</xsl:template>
	<!-- <xsl:template match="/ccf:artifact[@artifactType = 'plainArtifact']">
	   <xsl:variable name="statusValue" as="xs:string" select="concat($artifactTypeNameSpace,'Assigned')" />
	   <xsl:message>1</xsl:message>
	   <xsl:for-each
                select='ccf:field[@fieldName=string($statusValue)]'>
                <xsl:if test="position()=last()">
                <xsl:message>2</xsl:message>
                    <field>
                        <xsl:attribute name="fieldName">BG_RESPONSIBLE</xsl:attribute>
                        <xsl:attribute name="fieldDisplayName">Assigned To</xsl:attribute>
                        <xsl:attribute name="fieldAction"><xsl:value-of select="@fieldAction" /></xsl:attribute>
                        <xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
                        <xsl:attribute name="fieldValueHasChanged"><xsl:value-of select="@fieldValueHasChanged" /></xsl:attribute>
                        <xsl:attribute name="fieldValueType"><xsl:value-of select="@fieldValueType" /></xsl:attribute>
                        <xsl:attribute name="fieldValueIsNull"><xsl:value-of select="@fieldValueIsNull" /></xsl:attribute>
                        <xsl:attribute name="minOccurs"><xsl:value-of select="@minOccurs" /></xsl:attribute>
                        <xsl:attribute name="maxOccurs"><xsl:value-of select="@maxOccurs" /></xsl:attribute>
                        <xsl:attribute name="nullValueSupported"><xsl:value-of select="@nullValueSupported" /></xsl:attribute>
                        <xsl:attribute name="alternativeFieldName"><xsl:value-of select="@alternativeFieldName" /></xsl:attribute>
                        <xsl:value-of select="."></xsl:value-of>
                    </field>
                </xsl:if>
            </xsl:for-each>
	</xsl:template> -->
	<xsl:template
        match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Defect ID")]'>
        <field>
            <xsl:attribute name="fieldName">BG_BUG_ID</xsl:attribute>
            <xsl:attribute name="fieldDisplayName">Original issue ID</xsl:attribute>
            <xsl:attribute name="fieldAction"><xsl:value-of select="@fieldAction" /></xsl:attribute>
            <xsl:attribute name="fieldType"><xsl:value-of select="@fieldType" /></xsl:attribute>
            <xsl:attribute name="fieldValueHasChanged"><xsl:value-of select="@fieldValueHasChanged" /></xsl:attribute>
            <xsl:attribute name="fieldValueType"><xsl:value-of select="@fieldValueType" /></xsl:attribute>
            <xsl:attribute name="fieldValueIsNull"><xsl:value-of select="@fieldValueIsNull" /></xsl:attribute>
            <xsl:attribute name="minOccurs"><xsl:value-of select="@minOccurs" /></xsl:attribute>
            <xsl:attribute name="maxOccurs"><xsl:value-of select="@maxOccurs" /></xsl:attribute>
            <xsl:attribute name="nullValueSupported"><xsl:value-of select="@nullValueSupported" /></xsl:attribute>
            <xsl:attribute name="alternativeFieldName"><xsl:value-of select="@alternativeFieldName" /></xsl:attribute>
            <xsl:value-of select="."></xsl:value-of>
        </field>
    </xsl:template>
	<xsl:template
		match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Summary")]'>
		<field>
		    <xsl:attribute name="fieldName">BG_SUMMARY</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">Summary</xsl:attribute>
		    <xsl:attribute name="fieldAction"><xsl:value-of select="@fieldAction" /></xsl:attribute>
		    <xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
		  	<xsl:attribute name="fieldValueHasChanged"><xsl:value-of select="@fieldValueHasChanged" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueType"><xsl:value-of select="@fieldValueType" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueIsNull"><xsl:value-of select="@fieldValueIsNull" /></xsl:attribute>
		  	<xsl:attribute name="minOccurs"><xsl:value-of select="@minOccurs" /></xsl:attribute>
		  	<xsl:attribute name="maxOccurs"><xsl:value-of select="@maxOccurs" /></xsl:attribute>
		  	<xsl:attribute name="nullValueSupported"><xsl:value-of select="@nullValueSupported" /></xsl:attribute>
		  	<xsl:attribute name="alternativeFieldName"><xsl:value-of select="@alternativeFieldName" /></xsl:attribute>
		  	<xsl:value-of select="."></xsl:value-of>
	  	</field>
	</xsl:template>
		<xsl:template
		match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Description")]'>
		<field>
		    <xsl:attribute name="fieldName">BG_DESCRIPTION</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">Description</xsl:attribute>
		    <xsl:attribute name="fieldAction"><xsl:value-of select="@fieldAction" /></xsl:attribute>
		    <xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
		  	<xsl:attribute name="fieldValueHasChanged"><xsl:value-of select="@fieldValueHasChanged" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueType">String</xsl:attribute>
		  	<xsl:attribute name="fieldValueIsNull"><xsl:value-of select="@fieldValueIsNull" /></xsl:attribute>
		  	<xsl:attribute name="minOccurs"><xsl:value-of select="@minOccurs" /></xsl:attribute>
		  	<xsl:attribute name="maxOccurs"><xsl:value-of select="@maxOccurs" /></xsl:attribute>
		  	<xsl:attribute name="nullValueSupported"><xsl:value-of select="@nullValueSupported" /></xsl:attribute>
		  	<xsl:attribute name="alternativeFieldName"><xsl:value-of select="@alternativeFieldName" /></xsl:attribute>
            <xsl:value-of select="."></xsl:value-of>
	  	</field>
	</xsl:template>


   <!--  <xsl:template name="AssignedTo">
        <xsl:message>POSITION <xsl:value-of select="./position()"/></xsl:message>
        <xsl:message>LAST <xsl:value-of select="./last()"/></xsl:message>
        <xsl:if test="=last()">
	        <field>
	            <xsl:attribute name="fieldName">BG_RESPONSIBLE</xsl:attribute>
	            <xsl:attribute name="fieldDisplayName">Assigned To</xsl:attribute>
	            <xsl:attribute name="fieldAction"><xsl:value-of select="@fieldAction" /></xsl:attribute>
	            <xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
	            <xsl:attribute name="fieldValueHasChanged"><xsl:value-of select="@fieldValueHasChanged" /></xsl:attribute>
	            <xsl:attribute name="fieldValueType"><xsl:value-of select="@fieldValueType" /></xsl:attribute>
	            <xsl:attribute name="fieldValueIsNull"><xsl:value-of select="@fieldValueIsNull" /></xsl:attribute>
	            <xsl:attribute name="minOccurs"><xsl:value-of select="@minOccurs" /></xsl:attribute>
	            <xsl:attribute name="maxOccurs"><xsl:value-of select="@maxOccurs" /></xsl:attribute>
	            <xsl:attribute name="nullValueSupported"><xsl:value-of select="@nullValueSupported" /></xsl:attribute>
	            <xsl:attribute name="alternativeFieldName"><xsl:value-of select="@alternativeFieldName" /></xsl:attribute>
	            <xsl:value-of select="."></xsl:value-of>
	        </field>
        </xsl:if>
    </xsl:template> -->
    

	<xsl:template
		match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Status")]'>
		<xsl:variable name="statusValue" as="xs:string" select="." />
		<field>
		    <xsl:attribute name="fieldName">BG_STATUS</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">Status</xsl:attribute>
		    <xsl:attribute name="fieldAction"><xsl:value-of select="@fieldAction" /></xsl:attribute>
		    <xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
		  	<xsl:attribute name="fieldValueHasChanged"><xsl:value-of select="@fieldValueHasChanged" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueType"><xsl:value-of select="@fieldValueType" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueIsNull"><xsl:value-of select="@fieldValueIsNull" /></xsl:attribute>
		  	<xsl:attribute name="minOccurs"><xsl:value-of select="@minOccurs" /></xsl:attribute>
		  	<xsl:attribute name="maxOccurs"><xsl:value-of select="@maxOccurs" /></xsl:attribute>
		  	<xsl:attribute name="nullValueSupported"><xsl:value-of select="@nullValueSupported" /></xsl:attribute>
		  	<xsl:attribute name="alternativeFieldName"><xsl:value-of select="@alternativeFieldName" /></xsl:attribute>
			
			<xsl:if test="$statusValue = 'STARTED'"><xsl:text>Open</xsl:text></xsl:if>
            <xsl:if test="$statusValue = 'CLOSED'"><xsl:text>Closed</xsl:text></xsl:if>
            <xsl:if test="$statusValue = 'RESOLVED'"><xsl:text>Fixed</xsl:text></xsl:if>
            <xsl:if test="$statusValue = 'VERIFIED'"><xsl:text>Fixed</xsl:text></xsl:if>
            <xsl:if test="$statusValue = 'UNCONFIRMED'"><xsl:text>Rejected</xsl:text></xsl:if>
            <xsl:if test="$statusValue = 'REOPENED'"><xsl:text>Reopen</xsl:text></xsl:if>
            <xsl:if test="$statusValue = 'NEW'"><xsl:text>New</xsl:text></xsl:if>
            <xsl:if test="$statusValue = ''"><xsl:text>New</xsl:text></xsl:if>
	  	</field>
	</xsl:template>

	 <xsl:template
		match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Priority")]'>
		<xsl:variable name="priorityValue" as="xs:string"><xsl:value-of select="." /></xsl:variable>
		<field>
		    <xsl:attribute name="fieldName">BG_PRIORITY</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">Priority</xsl:attribute>
		    <xsl:attribute name="fieldAction"><xsl:value-of select="@fieldAction" /></xsl:attribute>
		    <xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
		  	<xsl:attribute name="fieldValueHasChanged"><xsl:value-of select="@fieldValueHasChanged" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueType"><xsl:value-of select="@fieldValueType" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueIsNull"><xsl:value-of select="@fieldValueIsNull" /></xsl:attribute>
		  	<xsl:attribute name="minOccurs"><xsl:value-of select="@minOccurs" /></xsl:attribute>
		  	<xsl:attribute name="maxOccurs"><xsl:value-of select="@maxOccurs" /></xsl:attribute>
		  	<xsl:attribute name="nullValueSupported"><xsl:value-of select="@nullValueSupported" /></xsl:attribute>
		  	<xsl:attribute name="alternativeFieldName"><xsl:value-of select="@alternativeFieldName" /></xsl:attribute>
		  	
			<xsl:if test="$priorityValue = 'P1'"><xsl:text>5-Urgent</xsl:text></xsl:if>
            <xsl:if test="$priorityValue = 'P2'"><xsl:text>4-Very High</xsl:text></xsl:if>
            <xsl:if test="$priorityValue = 'P3'"><xsl:text>3-High</xsl:text></xsl:if>
            <xsl:if test="$priorityValue = 'P4'"><xsl:text>2-Medium</xsl:text></xsl:if>
            <xsl:if test="$priorityValue = 'P5'"><xsl:text>1-Low</xsl:text></xsl:if> 
		</field>
	</xsl:template>
	<xsl:template
		match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Component")]'>
		<xsl:variable name="priorityValue" as="xs:string"><xsl:value-of select="." /></xsl:variable>
		<field>
		    <xsl:attribute name="fieldName">BG_USER_01</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">Component</xsl:attribute>
		    <xsl:attribute name="fieldAction"><xsl:value-of select="@fieldAction" /></xsl:attribute>
		    <xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
		  	<xsl:attribute name="fieldValueHasChanged"><xsl:value-of select="@fieldValueHasChanged" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueType"><xsl:value-of select="@fieldValueType" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueIsNull"><xsl:value-of select="@fieldValueIsNull" /></xsl:attribute>
		  	<xsl:attribute name="minOccurs"><xsl:value-of select="@minOccurs" /></xsl:attribute>
		  	<xsl:attribute name="maxOccurs"><xsl:value-of select="@maxOccurs" /></xsl:attribute>
		  	<xsl:attribute name="nullValueSupported"><xsl:value-of select="@nullValueSupported" /></xsl:attribute>
		  	<xsl:attribute name="alternativeFieldName"><xsl:value-of select="@alternativeFieldName" /></xsl:attribute>
			<xsl:value-of select="."></xsl:value-of>
		</field>
	</xsl:template>
	<xsl:template
		match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Version")]'>
		<xsl:variable name="priorityValue" as="xs:string"><xsl:value-of select="." /></xsl:variable>
		<field>
		    <xsl:attribute name="fieldName">BG_DETECTION_VERSION</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">Version</xsl:attribute>
		    <xsl:attribute name="fieldAction"><xsl:value-of select="@fieldAction" /></xsl:attribute>
		    <xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
		  	<xsl:attribute name="fieldValueHasChanged"><xsl:value-of select="@fieldValueHasChanged" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueType"><xsl:value-of select="@fieldValueType" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueIsNull"><xsl:value-of select="@fieldValueIsNull" /></xsl:attribute>
		  	<xsl:attribute name="minOccurs"><xsl:value-of select="@minOccurs" /></xsl:attribute>
		  	<xsl:attribute name="maxOccurs"><xsl:value-of select="@maxOccurs" /></xsl:attribute>
		  	<xsl:attribute name="nullValueSupported"><xsl:value-of select="@nullValueSupported" /></xsl:attribute>
		  	<xsl:attribute name="alternativeFieldName"><xsl:value-of select="@alternativeFieldName" /></xsl:attribute>
		  	<xsl:if test="$priorityValue = 'B1'"><xsl:text>1.0</xsl:text></xsl:if>
			<xsl:if test="$priorityValue = 'B2'"><xsl:text>2.0</xsl:text></xsl:if>
			<xsl:if test="$priorityValue = '1.0'"><xsl:text>3.0</xsl:text></xsl:if>
		</field>
	</xsl:template>
	<xsl:template
		match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Sub-component")]'>
		<xsl:variable name="priorityValue" as="xs:string"><xsl:value-of select="." /></xsl:variable>
		<field>
		    <xsl:attribute name="fieldName">BG_USER_03</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">Sub-component</xsl:attribute>
		    <xsl:attribute name="fieldAction"><xsl:value-of select="@fieldAction" /></xsl:attribute>
		    <xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
		  	<xsl:attribute name="fieldValueHasChanged"><xsl:value-of select="@fieldValueHasChanged" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueType"><xsl:value-of select="@fieldValueType" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueIsNull"><xsl:value-of select="@fieldValueIsNull" /></xsl:attribute>
		  	<xsl:attribute name="minOccurs"><xsl:value-of select="@minOccurs" /></xsl:attribute>
		  	<xsl:attribute name="maxOccurs"><xsl:value-of select="@maxOccurs" /></xsl:attribute>
		  	<xsl:attribute name="nullValueSupported"><xsl:value-of select="@nullValueSupported" /></xsl:attribute>
		  	<xsl:attribute name="alternativeFieldName"><xsl:value-of select="@alternativeFieldName" /></xsl:attribute>
			<xsl:if test="$priorityValue = 'SubComponent 1'"><xsl:text>Subcomponent 1</xsl:text></xsl:if>
		        <xsl:if test="$priorityValue = 'SubComponent 2'"><xsl:text>Subcomponent 2</xsl:text></xsl:if>
		        <xsl:if test="$priorityValue = 'SubComponent 3'"><xsl:text>Subcomponent 3</xsl:text></xsl:if>
		</field>
	</xsl:template>
	<xsl:template
		match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Platform")]'>
		<xsl:variable name="priorityValue" as="xs:string"><xsl:value-of select="." /></xsl:variable>
		<field>
		    <xsl:attribute name="fieldName">BG_USER_07</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">Platform</xsl:attribute>
		    <xsl:attribute name="fieldAction"><xsl:value-of select="@fieldAction" /></xsl:attribute>
		    <xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
		  	<xsl:attribute name="fieldValueHasChanged"><xsl:value-of select="@fieldValueHasChanged" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueType"><xsl:value-of select="@fieldValueType" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueIsNull"><xsl:value-of select="@fieldValueIsNull" /></xsl:attribute>
		  	<xsl:attribute name="minOccurs"><xsl:value-of select="@minOccurs" /></xsl:attribute>
		  	<xsl:attribute name="maxOccurs"><xsl:value-of select="@maxOccurs" /></xsl:attribute>
		  	<xsl:attribute name="nullValueSupported"><xsl:value-of select="@nullValueSupported" /></xsl:attribute>
		  	<xsl:attribute name="alternativeFieldName"><xsl:value-of select="@alternativeFieldName" /></xsl:attribute>
		  	<xsl:value-of select="."></xsl:value-of>
		</field>
	</xsl:template>
	<xsl:template
		match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Operating system")]'>
		<xsl:variable name="priorityValue" as="xs:string"><xsl:value-of select="." /></xsl:variable>
		<field>
		    <xsl:attribute name="fieldName">BG_USER_02</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">Operating system</xsl:attribute>
		    <xsl:attribute name="fieldAction"><xsl:value-of select="@fieldAction" /></xsl:attribute>
		    <xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
		  	<xsl:attribute name="fieldValueHasChanged"><xsl:value-of select="@fieldValueHasChanged" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueType"><xsl:value-of select="@fieldValueType" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueIsNull"><xsl:value-of select="@fieldValueIsNull" /></xsl:attribute>
		  	<xsl:attribute name="minOccurs"><xsl:value-of select="@minOccurs" /></xsl:attribute>
		  	<xsl:attribute name="maxOccurs"><xsl:value-of select="@maxOccurs" /></xsl:attribute>
		  	<xsl:attribute name="nullValueSupported"><xsl:value-of select="@nullValueSupported" /></xsl:attribute>
		  	<xsl:attribute name="alternativeFieldName"><xsl:value-of select="@alternativeFieldName" /></xsl:attribute>
		  	<xsl:value-of select="."></xsl:value-of>
		</field>
	</xsl:template>
	<xsl:template
        match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Multi Select")]'>
        <xsl:variable name="priorityValue" as="xs:string"><xsl:value-of select="." /></xsl:variable>
        <field>
            <xsl:attribute name="fieldName">BG_USER_11</xsl:attribute>
            <xsl:attribute name="fieldDisplayName">Multi select</xsl:attribute>
            <xsl:attribute name="fieldAction"><xsl:value-of select="@fieldAction" /></xsl:attribute>
            <xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
            <xsl:attribute name="fieldValueHasChanged"><xsl:value-of select="@fieldValueHasChanged" /></xsl:attribute>
            <xsl:attribute name="fieldValueType"><xsl:value-of select="@fieldValueType" /></xsl:attribute>
            <xsl:attribute name="fieldValueIsNull"><xsl:value-of select="@fieldValueIsNull" /></xsl:attribute>
            <xsl:attribute name="minOccurs"><xsl:value-of select="@minOccurs" /></xsl:attribute>
            <xsl:attribute name="maxOccurs"><xsl:value-of select="@maxOccurs" /></xsl:attribute>
            <xsl:attribute name="nullValueSupported"><xsl:value-of select="@nullValueSupported" /></xsl:attribute>
            <xsl:attribute name="alternativeFieldName"><xsl:value-of select="@alternativeFieldName" /></xsl:attribute>
             <xsl:if test="$priorityValue = 'MultiList A'"><xsl:text>Option 1</xsl:text></xsl:if>
            <xsl:if test="$priorityValue = 'MultiList B'"><xsl:text>Option 2</xsl:text></xsl:if>
            <xsl:if test="$priorityValue = 'MultiList C'"><xsl:text>Option 3</xsl:text></xsl:if>
        </field>
    </xsl:template>
	<xsl:template
        match='ccf:field[@fieldName="Comment"]'>
        <xsl:variable name="comment" as="xs:string"><xsl:value-of select="." /></xsl:variable>
        <xsl:choose>
                <xsl:when test="$comment=''">
                    <xsl:text />
                </xsl:when>
                <xsl:otherwise>
			        <field>
			            <xsl:attribute name="fieldName">BG_DEV_COMMENTS</xsl:attribute>
			            <xsl:attribute name="fieldDisplayName">Comments</xsl:attribute>
			            <xsl:attribute name="fieldAction"><xsl:value-of select="@fieldAction" /></xsl:attribute>
			            <xsl:attribute name="fieldType"><xsl:value-of select="@fieldType" /></xsl:attribute>
			            <xsl:attribute name="fieldValueHasChanged"><xsl:value-of select="@fieldValueHasChanged" /></xsl:attribute>
			            <xsl:attribute name="fieldValueType">String</xsl:attribute>
			            <xsl:attribute name="fieldValueIsNull"><xsl:value-of select="@fieldValueIsNull" /></xsl:attribute>
			            <xsl:attribute name="minOccurs"><xsl:value-of select="@minOccurs" /></xsl:attribute>
			            <xsl:attribute name="maxOccurs"><xsl:value-of select="@maxOccurs" /></xsl:attribute>
			            <xsl:attribute name="nullValueSupported"><xsl:value-of select="@nullValueSupported" /></xsl:attribute>
			            <xsl:attribute name="alternativeFieldName"><xsl:value-of select="@alternativeFieldName" /></xsl:attribute>
			            <xsl:value-of select="stringutil:encodeHTMLToEntityReferences(string(.))"/>
			        </field>
			    </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template
        match='ccf:field[@fieldName="{urn:ws.tracker.collabnet.com}reason"]'>
        <xsl:variable name="comment" as="xs:string"><xsl:value-of select="." /></xsl:variable>
        <xsl:choose>
                <xsl:when test="$comment=''">
                    <xsl:text />
                </xsl:when>
                <xsl:otherwise>
                    <field>
                        <xsl:attribute name="fieldName">BG_DEV_COMMENTS</xsl:attribute>
                        <xsl:attribute name="fieldDisplayName">Comments</xsl:attribute>
                        <xsl:attribute name="fieldAction"><xsl:value-of select="@fieldAction" /></xsl:attribute>
                        <xsl:attribute name="fieldType"><xsl:value-of select="@fieldType" /></xsl:attribute>
                        <xsl:attribute name="fieldValueHasChanged"><xsl:value-of select="@fieldValueHasChanged" /></xsl:attribute>
                        <xsl:attribute name="fieldValueType">String</xsl:attribute>
                        <xsl:attribute name="fieldValueIsNull"><xsl:value-of select="@fieldValueIsNull" /></xsl:attribute>
                        <xsl:attribute name="minOccurs"><xsl:value-of select="@minOccurs" /></xsl:attribute>
                        <xsl:attribute name="maxOccurs"><xsl:value-of select="@maxOccurs" /></xsl:attribute>
                        <xsl:attribute name="nullValueSupported"><xsl:value-of select="@nullValueSupported" /></xsl:attribute>
                        <xsl:attribute name="alternativeFieldName"><xsl:value-of select="@alternativeFieldName" /></xsl:attribute>
                        <xsl:value-of select="stringutil:encodeHTMLToEntityReferences(string(.))"/>
                    </field>
                </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template
		match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Detected on Date")]'>
		<field>
		    <xsl:attribute name="fieldName">BG_DETECTION_DATE</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">Detected on Date</xsl:attribute>
		    <xsl:attribute name="fieldAction"><xsl:value-of select="@fieldAction" /></xsl:attribute>
		    <xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
		  	<xsl:attribute name="fieldValueHasChanged"><xsl:value-of select="@fieldValueHasChanged" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueType"><xsl:value-of select="@fieldValueType" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueIsNull"><xsl:value-of select="@fieldValueIsNull" /></xsl:attribute>
		  	<xsl:attribute name="minOccurs"><xsl:value-of select="@minOccurs" /></xsl:attribute>
		  	<xsl:attribute name="maxOccurs"><xsl:value-of select="@maxOccurs" /></xsl:attribute>
		  	<xsl:attribute name="nullValueSupported"><xsl:value-of select="@nullValueSupported" /></xsl:attribute>
		  	<xsl:attribute name="alternativeFieldName"><xsl:value-of select="@alternativeFieldName" /></xsl:attribute>
		  	<xsl:value-of select="."></xsl:value-of>
	  	</field>
	</xsl:template>
	<xsl:template
        match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Detected on Date")]'>
        <field>
            <xsl:attribute name="fieldName">BG_DETECTION_DATE</xsl:attribute>
            <xsl:attribute name="fieldDisplayName">Detected on Date</xsl:attribute>
            <xsl:attribute name="fieldAction"><xsl:value-of select="@fieldAction" /></xsl:attribute>
            <xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
            <xsl:attribute name="fieldValueHasChanged"><xsl:value-of select="@fieldValueHasChanged" /></xsl:attribute>
            <xsl:attribute name="fieldValueType"><xsl:value-of select="@fieldValueType" /></xsl:attribute>
            <xsl:attribute name="fieldValueIsNull"><xsl:value-of select="@fieldValueIsNull" /></xsl:attribute>
            <xsl:attribute name="minOccurs"><xsl:value-of select="@minOccurs" /></xsl:attribute>
            <xsl:attribute name="maxOccurs"><xsl:value-of select="@maxOccurs" /></xsl:attribute>
            <xsl:attribute name="nullValueSupported"><xsl:value-of select="@nullValueSupported" /></xsl:attribute>
            <xsl:attribute name="alternativeFieldName"><xsl:value-of select="@alternativeFieldName" /></xsl:attribute>
            <xsl:value-of select="."></xsl:value-of>
        </field>
    </xsl:template>
	<xsl:template
        match='ccf:field[@fieldName=concat($artifactTypeNameSpace,"Issue_State")]'>
        <field>
            <xsl:attribute name="fieldName">BG_USER_08</xsl:attribute>
            <xsl:attribute name="fieldDisplayName">PT_ID</xsl:attribute>
            <xsl:attribute name="fieldAction"><xsl:value-of select="@fieldAction" /></xsl:attribute>
            <xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
            <xsl:attribute name="fieldValueHasChanged"><xsl:value-of select="@fieldValueHasChanged" /></xsl:attribute>
            <xsl:attribute name="fieldValueType">String</xsl:attribute>
            <xsl:attribute name="fieldValueIsNull"><xsl:value-of select="@fieldValueIsNull" /></xsl:attribute>
            <xsl:attribute name="minOccurs"><xsl:value-of select="@minOccurs" /></xsl:attribute>
            <xsl:attribute name="maxOccurs"><xsl:value-of select="@maxOccurs" /></xsl:attribute>
            <xsl:attribute name="nullValueSupported"><xsl:value-of select="@nullValueSupported" /></xsl:attribute>
            <xsl:attribute name="alternativeFieldName"><xsl:value-of select="@alternativeFieldName" /></xsl:attribute>
            <xsl:value-of select="."></xsl:value-of>
        </field>
    </xsl:template>

	<xsl:template match="text()" />

</xsl:stylesheet>