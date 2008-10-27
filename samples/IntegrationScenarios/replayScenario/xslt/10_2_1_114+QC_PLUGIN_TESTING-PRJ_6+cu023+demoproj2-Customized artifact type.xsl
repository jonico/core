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
	exclude-result-prefixes="xsl xs">
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
			<xsl:apply-templates />
		</artifact>
	</xsl:template>
	<xsl:template match="/ccf:artifact[@artifactType = 'attachment']">
		<xsl:copy-of select="."/>
	</xsl:template>
	<xsl:template
        match='ccf:field[@fieldName="BG_BUG_ID"]'>
        <field>
            <xsl:attribute name="fieldName">Defect ID</xsl:attribute>
            <xsl:attribute name="fieldDisplayName">QC-Id</xsl:attribute>
            <xsl:attribute name="fieldAction"><xsl:value-of select="@fieldAction" /></xsl:attribute>
            <xsl:attribute name="fieldType"><xsl:value-of select="@fieldType" /></xsl:attribute>
            <xsl:attribute name="fieldValueHasChanged"><xsl:value-of select="@fieldValueHasChanged" /></xsl:attribute>
            <xsl:attribute name="fieldValueType">Integer</xsl:attribute>
            <xsl:attribute name="fieldValueIsNull"><xsl:value-of select="@fieldValueIsNull" /></xsl:attribute>
            <xsl:attribute name="minOccurs"><xsl:value-of select="@minOccurs" /></xsl:attribute>
            <xsl:attribute name="maxOccurs"><xsl:value-of select="@maxOccurs" /></xsl:attribute>
            <xsl:attribute name="nullValueSupported"><xsl:value-of select="@nullValueSupported" /></xsl:attribute>
            <xsl:attribute name="alternativeFieldName"><xsl:value-of select="@alternativeFieldName" /></xsl:attribute>
            <xsl:value-of select="."></xsl:value-of>
        </field>
    </xsl:template>
	<xsl:template
		match='ccf:field[@fieldName="BG_SUMMARY"]'>
		<field>
		    <xsl:attribute name="fieldName">Summary</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">Title</xsl:attribute>
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
		match='ccf:field[@fieldName="BG_DESCRIPTION"]'>
		<field>
		    <xsl:attribute name="fieldName">Short description</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">Short description</xsl:attribute>
		    <xsl:attribute name="fieldAction"><xsl:value-of select="@fieldAction" /></xsl:attribute>
		    <xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
		  	<xsl:attribute name="fieldValueHasChanged"><xsl:value-of select="@fieldValueHasChanged" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueType">String</xsl:attribute>
		  	<xsl:attribute name="fieldValueIsNull"><xsl:value-of select="@fieldValueIsNull" /></xsl:attribute>
		  	<xsl:attribute name="minOccurs"><xsl:value-of select="@minOccurs" /></xsl:attribute>
		  	<xsl:attribute name="maxOccurs"><xsl:value-of select="@maxOccurs" /></xsl:attribute>
		  	<xsl:attribute name="nullValueSupported"><xsl:value-of select="@nullValueSupported" /></xsl:attribute>
		  	<xsl:attribute name="alternativeFieldName"><xsl:value-of select="@alternativeFieldName" /></xsl:attribute>
			<xsl:choose>
                <xsl:when test="@fieldValueType='HTMLString'">
                    <xsl:value-of select="stringutil:stripHTML(string(.))"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="."></xsl:value-of>
                </xsl:otherwise>
            </xsl:choose>
	  	</field>
	</xsl:template>

	<xsl:template
		match='ccf:field[@fieldName="BG_STATUS"]'>
		<xsl:variable name="statusValue" as="xs:string" select="." />
		<field>
		    <xsl:attribute name="fieldName">Status</xsl:attribute>
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
			
			<xsl:if test="$statusValue = 'Open'"><xsl:text>STARTED</xsl:text></xsl:if>
            <xsl:if test="$statusValue = 'Closed'"><xsl:text>CLOSED</xsl:text></xsl:if>
            <xsl:if test="$statusValue = 'Fixed'"><xsl:text>RESOLVED</xsl:text></xsl:if>
            <xsl:if test="$statusValue = 'New'"><xsl:text>NEW</xsl:text></xsl:if>
            <xsl:if test="$statusValue = 'Rejected'"><xsl:text>UNCONFIRMED</xsl:text></xsl:if>
            <xsl:if test="$statusValue = 'Reopen'"><xsl:text>REOPENED</xsl:text></xsl:if>
            <xsl:if test="$statusValue = ''"><xsl:text>NEW</xsl:text></xsl:if>
	  	</field>
	</xsl:template>
	<xsl:template
        match='ccf:field[@fieldName="BG_DEV_COMMENTS"]'>
        <field>
            <xsl:attribute name="fieldName">Comment</xsl:attribute>
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
            <xsl:choose>
                <xsl:when test="@fieldValueType='HTMLString'">
                    <xsl:value-of select="stringutil:stripHTML(string(.))"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="."></xsl:value-of>
                </xsl:otherwise>
            </xsl:choose>
        </field>
    </xsl:template>
    <xsl:template
        match='ccf:field[@fieldName="BG_RESPONSIBLE"]'>
        <field>
            <xsl:attribute name="fieldName">Assigned to</xsl:attribute>
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
    </xsl:template>
	<xsl:template
		match='ccf:field[@fieldName="BG_USER_02"]'>
		<field>
		    <xsl:attribute name="fieldName">QA Contact Email</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">QA Contact Email</xsl:attribute>
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
		match='ccf:field[@fieldName="BG_USER_04"]'>
		<field>
		    <xsl:attribute name="fieldName">Testing hours</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">Testing hours</xsl:attribute>
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
		match='ccf:field[@fieldName="BG_USER_05"]'>
		<field>
		    <xsl:attribute name="fieldName">Component affected</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">Component affected</xsl:attribute>
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
		match='ccf:field[@fieldName="BG_DETECTION_VERSION"]'>
		<field>
		    <xsl:attribute name="fieldName">Found in version</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">Found in version</xsl:attribute>
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
		match='ccf:field[@fieldName="BG_USER_07"]'>
		<field>
		    <xsl:attribute name="fieldName">Reproduction steps</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">Reproduction steps</xsl:attribute>
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
	<xsl:template match="text()" />
</xsl:stylesheet>