<?xml version="1.0"?>

<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ccf="http://ccf.open.collab.net/GenericArtifactV1.0"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	exclude-result-prefixes="xsl xs">
	<xsl:template match='/ccf:artifact[@artifactType = "plainArtifact"]'>
		<artifact xmlns="http://ccf.open.collab.net/GenericArtifactV1.0">
			<xsl:attribute name="artifactAction"><xsl:value-of select="@artifactAction" /></xsl:attribute>
			<xsl:attribute name="artifactMode"><xsl:value-of select="@artifactMode" /></xsl:attribute>
			<xsl:attribute name="artifactType"><xsl:value-of select="@artifactType" /></xsl:attribute>
			<xsl:attribute name="artifactLastModifiedDate"><xsl:value-of select="@artifactLastModifiedDate" /></xsl:attribute>
			<xsl:attribute name="transactionId"><xsl:value-of select="@transactionId" /></xsl:attribute>
			<xsl:attribute name="errorCode"><xsl:value-of select="@errorCode" /></xsl:attribute>
			<xsl:attribute name="includesFieldMetaData"><xsl:value-of select="@includesFieldMetaData" /></xsl:attribute>
			<xsl:attribute name="artifactVersion"><xsl:value-of select="@artifactVersion" /></xsl:attribute>
			<xsl:attribute name="conflicResolutionPolicy"><xsl:value-of select="@conflicResolutionPolicy" /></xsl:attribute>
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
			<xsl:for-each select="field">
			</xsl:for-each>
			<xsl:apply-templates />
		</artifact>
	</xsl:template>
	<xsl:template match="/ccf:artifact[@artifactType = 'attachment']">
		<xsl:copy-of select="."/>
	</xsl:template>
	<xsl:template
		match='ccf:field[@fieldName="actualHours"]'>
		<field>
		    <xsl:attribute name="fieldName">BG_ACTUAL_FIX_TIME</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">Actual Fix Time</xsl:attribute>
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
		match='ccf:field[@fieldName="estimatedHours"]'>
		<field>
		    <xsl:attribute name="fieldName">BG_ESTIMATED_FIX_TIME</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">Estimated Fix Time</xsl:attribute>
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
		match='ccf:field[@fieldName="QC-Id"]'>
		<field>
		    <xsl:attribute name="fieldName">BG_BUG_ID</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">Defect ID</xsl:attribute>
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
		match='ccf:field[@fieldName="title"]'>
		<field>
		    <xsl:attribute name="fieldName">BG_SUMMARY</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">Summary</xsl:attribute>
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
		match='ccf:field[@fieldName="description"]'>
		<field>
		    <xsl:attribute name="fieldName">BG_DESCRIPTION</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">Description</xsl:attribute>
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
		match='ccf:field[@fieldName="status"]'>
		<xsl:variable name="statusValue" as="xs:string" select="." />
		<field>
		    <xsl:attribute name="fieldName">BG_STATUS</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">Status</xsl:attribute>
		    <xsl:attribute name="fieldAction"><xsl:value-of select="@fieldAction" /></xsl:attribute>
		    <xsl:attribute name="fieldType"><xsl:value-of select="@fieldType" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueHasChanged"><xsl:value-of select="@fieldValueHasChanged" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueType"><xsl:value-of select="@fieldValueType" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueIsNull"><xsl:value-of select="@fieldValueIsNull" /></xsl:attribute>
		  	<xsl:attribute name="minOccurs"><xsl:value-of select="@minOccurs" /></xsl:attribute>
		  	<xsl:attribute name="maxOccurs"><xsl:value-of select="@maxOccurs" /></xsl:attribute>
		  	<xsl:attribute name="nullValueSupported"><xsl:value-of select="@nullValueSupported" /></xsl:attribute>
		  	<xsl:attribute name="alternativeFieldName"><xsl:value-of select="@alternativeFieldName" /></xsl:attribute>
		  	
		  	<xsl:if test="$statusValue = 'Open'"><xsl:text>Open</xsl:text></xsl:if>
			<xsl:if test="$statusValue = 'Closed'"><xsl:text>Closed</xsl:text></xsl:if>
			<xsl:if test="$statusValue = 'Pending'"><xsl:text>Fixed</xsl:text></xsl:if>
	  	</field>
	</xsl:template>

	 <xsl:template
		match='ccf:field[@fieldName="category"]'>
		<xsl:variable name="typeValue" as="xs:string" select="." />
		<field>
		    <xsl:attribute name="fieldName">BG_USER_02</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">Category</xsl:attribute>
		    <xsl:attribute name="fieldAction"><xsl:value-of select="@fieldAction" /></xsl:attribute>
		    <xsl:attribute name="fieldType"><xsl:value-of select="@fieldType" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueHasChanged"><xsl:value-of select="@fieldValueHasChanged" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueType"><xsl:value-of select="@fieldValueType" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueIsNull"><xsl:value-of select="@fieldValueIsNull" /></xsl:attribute>
		  	<xsl:attribute name="minOccurs"><xsl:value-of select="@minOccurs" /></xsl:attribute>
		  	<xsl:attribute name="maxOccurs"><xsl:value-of select="@maxOccurs" /></xsl:attribute>
		  	<xsl:attribute name="nullValueSupported"><xsl:value-of select="@nullValueSupported" /></xsl:attribute>
		  	<xsl:attribute name="alternativeFieldName"><xsl:value-of select="@alternativeFieldName" /></xsl:attribute>
		  	
		  	<xsl:if test="$typeValue = 'DefectCat'"><xsl:text>Defect</xsl:text></xsl:if>
			<xsl:if test="$typeValue = 'PatchCat'"><xsl:text>Patch</xsl:text></xsl:if>
			<xsl:if test="$typeValue = 'TaskCat'"><xsl:text>Task</xsl:text></xsl:if>
			<xsl:if test="$typeValue = 'EnhancementCat'"><xsl:text>Enhancement</xsl:text></xsl:if>
			<xsl:if test="$typeValue = 'FeatureCat'"><xsl:text>Feature</xsl:text></xsl:if>
	  	</field>
	</xsl:template>
	
	<xsl:template
		match='ccf:field[@fieldName="id"]'>
		<field>
		    <xsl:attribute name="fieldName">BG_USER_01</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">SFEE ID</xsl:attribute>
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
		match='ccf:field[@fieldName="Detected By"]'>
		<xsl:variable name="detectedBy" as="xs:string"><xsl:value-of select="." /></xsl:variable>
		<field>
		    <xsl:attribute name="fieldName">BG_DETECTED_BY</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">Detected By</xsl:attribute>
		    <xsl:attribute name="fieldAction"><xsl:value-of select="@fieldAction" /></xsl:attribute>
		    <xsl:attribute name="fieldType"><xsl:value-of select="@fieldType" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueHasChanged"><xsl:value-of select="@fieldValueHasChanged" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueType"><xsl:value-of select="@fieldValueType" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueIsNull"><xsl:value-of select="@fieldValueIsNull" /></xsl:attribute>
		  	<xsl:attribute name="minOccurs"><xsl:value-of select="@minOccurs" /></xsl:attribute>
		  	<xsl:attribute name="maxOccurs"><xsl:value-of select="@maxOccurs" /></xsl:attribute>
		  	<xsl:attribute name="nullValueSupported"><xsl:value-of select="@nullValueSupported" /></xsl:attribute>
		  	<xsl:attribute name="alternativeFieldName"><xsl:value-of select="@alternativeFieldName" /></xsl:attribute>
		  	<xsl:if test="$detectedBy = 'connector'"><xsl:text>alex_qc</xsl:text></xsl:if>
		  	<xsl:if test="$detectedBy = 'mseethar'"><xsl:text>cecil_qc</xsl:text></xsl:if>
			<xsl:if test="$detectedBy = 'admin'"><xsl:text>admin</xsl:text></xsl:if>
			<xsl:if test="$detectedBy = 'none'"><xsl:text>none</xsl:text></xsl:if>
			<xsl:if test="$detectedBy = 'None'"><xsl:text>None</xsl:text></xsl:if>
	  	</field>
	</xsl:template>
	
	<xsl:template
		match='ccf:field[@fieldName="Severity"]'>
		<field>
		    <xsl:attribute name="fieldName">BG_SEVERITY</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">Severity</xsl:attribute>
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
		match='ccf:field[@fieldName="Detected On"]'>
		<field>
		    <xsl:attribute name="fieldName">BG_DETECTION_DATE</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">Detected on Date</xsl:attribute>
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
		match='ccf:field[@fieldName="priority"]'>
		<xsl:variable name="priorityValue" as="xs:string"><xsl:value-of select="." /></xsl:variable>
		<field>
		    <xsl:attribute name="fieldName">BG_PRIORITY</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">Priority</xsl:attribute>
		    <xsl:attribute name="fieldAction"><xsl:value-of select="@fieldAction" /></xsl:attribute>
		    <xsl:attribute name="fieldType"><xsl:value-of select="@fieldType" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueHasChanged"><xsl:value-of select="@fieldValueHasChanged" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueType">String</xsl:attribute>
		  	<xsl:attribute name="fieldValueIsNull"><xsl:value-of select="@fieldValueIsNull" /></xsl:attribute>
		  	<xsl:attribute name="minOccurs"><xsl:value-of select="@minOccurs" /></xsl:attribute>
		  	<xsl:attribute name="maxOccurs"><xsl:value-of select="@maxOccurs" /></xsl:attribute>
		  	<xsl:attribute name="nullValueSupported"><xsl:value-of select="@nullValueSupported" /></xsl:attribute>
		  	<xsl:attribute name="alternativeFieldName"><xsl:value-of select="@alternativeFieldName" /></xsl:attribute>
		  	
		  	<xsl:if test="$priorityValue = '5'"><xsl:text>1-Low</xsl:text></xsl:if>
			<xsl:if test="$priorityValue = '4'"><xsl:text>2-Medium</xsl:text></xsl:if>
			<xsl:if test="$priorityValue = '3'"><xsl:text>3-High</xsl:text></xsl:if>
			<xsl:if test="$priorityValue = '2'"><xsl:text>4-Very High</xsl:text></xsl:if>
			<xsl:if test="$priorityValue = '1'"><xsl:text>5-Urgent</xsl:text></xsl:if>
		</field>
	</xsl:template> 
	<xsl:template
		match='ccf:field[@fieldName="Test Date"]'>
		<field>
		    <xsl:attribute name="fieldName">BG_USER_03</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">Test Date</xsl:attribute>
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
		match='ccf:field[@fieldName="assignedTo"]'>
		<xsl:variable name="assignedTo" as="xs:string"><xsl:value-of select="." /></xsl:variable>
		<field>
		    <xsl:attribute name="fieldName">BG_RESPONSIBLE</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">Assigned To</xsl:attribute>
		    <xsl:attribute name="fieldAction"><xsl:value-of select="@fieldAction" /></xsl:attribute>
		    <xsl:attribute name="fieldType"><xsl:value-of select="@fieldType" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueHasChanged"><xsl:value-of select="@fieldValueHasChanged" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueType"><xsl:value-of select="@fieldValueType" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueIsNull"><xsl:value-of select="@fieldValueIsNull" /></xsl:attribute>
		  	<xsl:attribute name="minOccurs"><xsl:value-of select="@minOccurs" /></xsl:attribute>
		  	<xsl:attribute name="maxOccurs"><xsl:value-of select="@maxOccurs" /></xsl:attribute>
		  	<xsl:attribute name="nullValueSupported"><xsl:value-of select="@nullValueSupported" /></xsl:attribute>
		  	<xsl:attribute name="alternativeFieldName"><xsl:value-of select="@alternativeFieldName" /></xsl:attribute>
		  	<xsl:if test="$assignedTo = 'connector'"><xsl:text>alex_qc</xsl:text></xsl:if>
			<xsl:if test="$assignedTo = 'admin'"><xsl:text>admin</xsl:text></xsl:if>
			<xsl:if test="$assignedTo = 'mseethar'"><xsl:text>cecil_qc</xsl:text></xsl:if>
			<xsl:if test="$assignedTo = 'none'"><xsl:text>none</xsl:text></xsl:if>
			<xsl:if test="$assignedTo = 'None'"><xsl:text>None</xsl:text></xsl:if>
		</field>
	</xsl:template>
	<xsl:template
		match='ccf:field[@fieldName="Comment Text"]'>
		<field>
		    <xsl:attribute name="fieldName">BG_DEV_COMMENTS</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">Comments</xsl:attribute>
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
	<xsl:template match="text()" />
</xsl:stylesheet>