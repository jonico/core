<?xml version="1.0"?>

<!-- 
	If there is a need to do so, I can extend the XsltProcessor component
	that it passes global parameters to this XSLT-Script
	
	XSLT allows you to use global variables (stylesheet parameters),
	template parameters, modes and function parameters  
-->

<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns=""
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	exclude-result-prefixes="xsl xs">
	<xsl:template match='/artifact'>
		<artifact>
			<xsl:attribute name="artifactAction"><xsl:value-of select="@artifactAction" /></xsl:attribute>
			<xsl:attribute name="artifactMode"><xsl:value-of select="@artifactMode" /></xsl:attribute>
			<xsl:attribute name="artifactType"><xsl:value-of select="@artifactType" /></xsl:attribute>
			<xsl:attribute name="artifactLastModifiedDate"><xsl:value-of select="@artifactLastModifiedDate" /></xsl:attribute>
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
	<xsl:template
		match='field[@fieldName="BG_ACTUAL_FIX_TIME"]'>
		<field>
		    <xsl:attribute name="fieldName">ActualHours</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">ActualHours</xsl:attribute>
		    <xsl:attribute name="fieldAction"><xsl:value-of select="@fieldAction" /></xsl:attribute>
		    <xsl:attribute name="fieldType"><xsl:value-of select="@fieldType" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueHasChanged"><xsl:value-of select="@fieldValueHasChanged" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueType"><xsl:value-of select="@fieldValueType" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueIsNull"><xsl:value-of select="@fieldValueIsNull" /></xsl:attribute>
		  	<xsl:value-of select="."></xsl:value-of>
	  	</field>
	</xsl:template>
	<xsl:template
		match='field[@fieldName="BG_ESTIMATED_FIX_TIME"]'>
		<field>
		    <xsl:attribute name="fieldName">EstimatedHours</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">EstimatedHours</xsl:attribute>
		    <xsl:attribute name="fieldAction"><xsl:value-of select="@fieldAction" /></xsl:attribute>
		    <xsl:attribute name="fieldType"><xsl:value-of select="@fieldType" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueHasChanged"><xsl:value-of select="@fieldValueHasChanged" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueType"><xsl:value-of select="@fieldValueType" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueIsNull"><xsl:value-of select="@fieldValueIsNull" /></xsl:attribute>
		  	<xsl:value-of select="."></xsl:value-of>
	  	</field>
	</xsl:template>
	
	<xsl:template
		match='field[@fieldName="BG_BUG_ID"]'>
		<field>
		    <xsl:attribute name="fieldName">QC-Id</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">QC-Id</xsl:attribute>
		    <xsl:attribute name="fieldAction"><xsl:value-of select="@fieldAction" /></xsl:attribute>
		    <xsl:attribute name="fieldType"><xsl:value-of select="@fieldType" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueHasChanged"><xsl:value-of select="@fieldValueHasChanged" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueType"><xsl:value-of select="@fieldValueType" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueIsNull"><xsl:value-of select="@fieldValueIsNull" /></xsl:attribute>
		  	<xsl:value-of select="."></xsl:value-of>
	  	</field>
	</xsl:template>
	<xsl:template
		match='field[@fieldName="BG_SUMMARY"]'>
		<field>
		    <xsl:attribute name="fieldName">Title</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">Title</xsl:attribute>
		    <xsl:attribute name="fieldAction"><xsl:value-of select="@fieldAction" /></xsl:attribute>
		    <xsl:attribute name="fieldType"><xsl:value-of select="@fieldType" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueHasChanged"><xsl:value-of select="@fieldValueHasChanged" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueType"><xsl:value-of select="@fieldValueType" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueIsNull"><xsl:value-of select="@fieldValueIsNull" /></xsl:attribute>
		  	<xsl:value-of select="."></xsl:value-of>
	  	</field>
	</xsl:template>
	<xsl:template
		match='field[@fieldName="BG_DESCRIPTION"]'>
		<field>
		    <xsl:attribute name="fieldName">Description</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">Description</xsl:attribute>
		    <xsl:attribute name="fieldAction"><xsl:value-of select="@fieldAction" /></xsl:attribute>
		    <xsl:attribute name="fieldType"><xsl:value-of select="@fieldType" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueHasChanged"><xsl:value-of select="@fieldValueHasChanged" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueType"><xsl:value-of select="@fieldValueType" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueIsNull"><xsl:value-of select="@fieldValueIsNull" /></xsl:attribute>
		  	<xsl:value-of select="."></xsl:value-of>
	  	</field>
	</xsl:template>

	<xsl:template
		match='field[@fieldName="BG_STATUS"]'>
		<xsl:variable name="statusValue" as="xs:string" select="." />
		<field>
		    <xsl:attribute name="fieldName">Status</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">Status</xsl:attribute>
		    <xsl:attribute name="fieldAction"><xsl:value-of select="@fieldAction" /></xsl:attribute>
		    <xsl:attribute name="fieldType"><xsl:value-of select="@fieldType" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueHasChanged"><xsl:value-of select="@fieldValueHasChanged" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueType"><xsl:value-of select="@fieldValueType" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueIsNull"><xsl:value-of select="@fieldValueIsNull" /></xsl:attribute>
		  	
		  	<xsl:if test="$statusValue = 'Open'"><xsl:text>Open</xsl:text></xsl:if>
			<xsl:if test="$statusValue = 'Closed'"><xsl:text>Closed</xsl:text></xsl:if>
			<xsl:if test="$statusValue = 'Fixed'"><xsl:text>Pending</xsl:text></xsl:if>
			<xsl:if test="$statusValue = 'Rejected'"><xsl:text>Pending</xsl:text></xsl:if>
			<xsl:if test="$statusValue = 'Reopen'"><xsl:text>Open</xsl:text></xsl:if> 
			<xsl:if test="$statusValue = 'New'"><xsl:text>Open</xsl:text></xsl:if>
	  	</field>
	</xsl:template>

	 <xsl:template
		match='field[@fieldName="BG_USER_03"]'>
		<xsl:variable name="typeValue" as="xs:string" select="." />
		<field>
		    <xsl:attribute name="fieldName">Category</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">Category</xsl:attribute>
		    <xsl:attribute name="fieldAction"><xsl:value-of select="@fieldAction" /></xsl:attribute>
		    <xsl:attribute name="fieldType"><xsl:value-of select="@fieldType" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueHasChanged"><xsl:value-of select="@fieldValueHasChanged" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueType"><xsl:value-of select="@fieldValueType" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueIsNull"><xsl:value-of select="@fieldValueIsNull" /></xsl:attribute>
		  	<xsl:if test="$typeValue = 'Defect'"><xsl:text>DefectCat</xsl:text></xsl:if>
			<xsl:if test="$typeValue = 'Patch'"><xsl:text>PatchCat</xsl:text></xsl:if>
			<xsl:if test="$typeValue = 'Task'"><xsl:text>TaskCat</xsl:text></xsl:if>
			<xsl:if test="$typeValue = 'Enhancement'"><xsl:text>EnhancementCat</xsl:text></xsl:if>
			<xsl:if test="$typeValue = 'Feature'"><xsl:text>FeatureCat</xsl:text></xsl:if> 
	  	</field>
	</xsl:template>
		

	 <xsl:template
		match='field[@fieldName="BG_PRIORITY"]'>
		<xsl:variable name="priorityValue" as="xs:string"><xsl:value-of select="." /></xsl:variable>
		<field>
		    <xsl:attribute name="fieldName">Priority</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">Priority</xsl:attribute>
		    <xsl:attribute name="fieldAction"><xsl:value-of select="@fieldAction" /></xsl:attribute>
		    <xsl:attribute name="fieldType"><xsl:value-of select="@fieldType" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueHasChanged"><xsl:value-of select="@fieldValueHasChanged" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueType"><xsl:value-of select="@fieldValueType" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueIsNull"><xsl:value-of select="@fieldValueIsNull" /></xsl:attribute>
		  	
			<xsl:if test="$priorityValue = '1-Low'"><xsl:text>5</xsl:text></xsl:if>
			<xsl:if test="$priorityValue = '2-Medium'"><xsl:text>4</xsl:text></xsl:if>
			<xsl:if test="$priorityValue = '3-High'"><xsl:text>3</xsl:text></xsl:if>
			<xsl:if test="$priorityValue = '4-Very High'"><xsl:text>2</xsl:text></xsl:if>
			<xsl:if test="$priorityValue = '5-Urgent'"><xsl:text>1</xsl:text></xsl:if> 
		</field>
	</xsl:template> 
	<xsl:template
		match='field[@fieldName="BG_RESPONSIBLE"]'>
		<field>
		    <xsl:attribute name="fieldName">AssignedTo</xsl:attribute>
		    <xsl:attribute name="fieldDisplayName">AssignedTo</xsl:attribute>
		    <xsl:attribute name="fieldAction"><xsl:value-of select="@fieldAction" /></xsl:attribute>
		    <xsl:attribute name="fieldType"><xsl:value-of select="@fieldType" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueHasChanged"><xsl:value-of select="@fieldValueHasChanged" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueType"><xsl:value-of select="@fieldValueType" /></xsl:attribute>
		  	<xsl:attribute name="fieldValueIsNull"><xsl:value-of select="@fieldValueIsNull" /></xsl:attribute>
		  	<xsl:value-of select="."></xsl:value-of>
		</field>
	</xsl:template>
	<xsl:template match="text()" />
</xsl:stylesheet>