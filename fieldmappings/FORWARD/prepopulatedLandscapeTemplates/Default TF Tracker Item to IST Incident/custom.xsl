<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ccf="http://ccf.open.collab.net/GenericArtifactV1.0" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:stringutil="xalan://com.collabnet.ccf.core.utils.StringUtils" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="2.0" exclude-result-prefixes="xsl xs ccf stringutil" xsi:schemaLocation="http://www.w3.org/1999/XSL/Transform http://www.w3.org/2007/schema-for-xslt20.xsd">
<!-- LIST OF SPIRA TEST MANDATORY INCIDENT FIELDS
   EXCEPTION: Comments needs to be a flexField when shipping them to CTF
  
  		All these field names are case sensitive!
	 
		ArtifactTypeId 					Name 
		ClosedDate 						OpenerId 
		Comments (since last visit) 	OpenerName 
		CompletionPercent 				OwnerId 
		ConcurrencyDate 				PriorityId 
		CreationDate 					PriorityName 
		Description 					ProjectedEffort 
		DetectedReleaseId 				ProjectId 
		DetectedReleaseVersionNumber 	ProjectName 
		EstimatedEffort 				RemainingEffort 
		FixedBuildId 					ResolvedReleaseId 
		FixedBuildName 					ResolvedReleaseVersionNumber 
		IncidentId 						SeverityId 
		IncidentStatusId 				SeverityName 
		IncidentStatusName 				StartDate 
		IncidentStatusOpenStatus 		TestRunStepId 
		IncidentTypeId 					VerifiedReleaseId 
		IncidentTypeName 				VerifiedReleaseVersionNumber 
		
		THIS TEMPLATE SHIPS FROM TEAMFORGE => SPIRATEST
 -->
 
 	<xsl:template match="/ccf:artifact[@artifactType = &quot;plainArtifact&quot;]">
		<artifact xmlns="http://ccf.open.collab.net/GenericArtifactV1.0">
			<xsl:copy-of select="@*"/>
			<xsl:apply-templates/>
		</artifact>
	</xsl:template>

	<xsl:template match="ccf:field[@fieldName='title']">
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">Name</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:value-of select="."/>
		</field>
	</xsl:template>

	<xsl:template match="ccf:field[@fieldName='status']">
		<xsl:variable name="statusValue" as="xs:string" select="."/>
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">IncidentStatusName</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:if test="$statusValue = 'Assigned'">
				<xsl:text>Assigned</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Closed'">
				<xsl:text>Closed</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Duplicate'">
				<xsl:text>Duplicate</xsl:text>
			</xsl:if>	
			<xsl:if test="$statusValue = 'New'">
				<xsl:text>New</xsl:text>
			</xsl:if>	
			<xsl:if test="$statusValue = 'Not Reproducible'">
				<xsl:text>Not Reproducible</xsl:text>
			</xsl:if>			
			<xsl:if test="$statusValue = 'Open'">
				<xsl:text>Open</xsl:text>
			</xsl:if>	
			<xsl:if test="$statusValue = 'Reopen'">
				<xsl:text>Reopen</xsl:text>
			</xsl:if>	
			<xsl:if test="$statusValue = 'Resolved'">
				<xsl:text>Resolved</xsl:text>
			</xsl:if>	
		</field>
	</xsl:template>
	
	<xsl:template match="ccf:field[@fieldName='priority']">
		<xsl:variable name="priorityValue" as="xs:string" select="."/>
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">PriorityName</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:if test="$priorityValue = '5'">
				<xsl:text>4 - Low</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = '4'">
				<xsl:text>4 - Low</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = '3'">
				<xsl:text>3 - Medium</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = '2'">
				<xsl:text>2 - High</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = '1'">
				<xsl:text>1 - Critical</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	
	<xsl:template match="ccf:field[@fieldName='id']">
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">TeamForge Incident #</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="."/>
		</field>
	</xsl:template>
	
	<xsl:template match="ccf:field[@fieldName='Req ID']">
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">Req ID</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="."/>
		</field>
	</xsl:template>
	
	<xsl:template match="ccf:field[@fieldName='description']">
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">Description</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:value-of select="."/>
		</field>
	</xsl:template>
	
	<xsl:template match="ccf:field[@fieldName='Comment Text']">
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">Comments</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="."/>
		</field>
	</xsl:template>

	<xsl:template match="/ccf:artifact[@artifactType = 'attachment']">
		<xsl:copy-of select="."/>
	</xsl:template>

	
	<xsl:template match="text()"/>
</xsl:stylesheet>