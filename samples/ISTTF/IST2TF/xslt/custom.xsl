<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ccf="http://ccf.open.collab.net/GenericArtifactV1.0" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:stringutil="xalan://com.collabnet.ccf.core.utils.GATransformerUtil" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="2.0" exclude-result-prefixes="xsl xs ccf stringutil" xsi:schemaLocation="http://www.w3.org/1999/XSL/Transform http://www.w3.org/2007/schema-for-xslt20.xsd">

<!-- LIST OF SPIRA TEST INCIDENT FIELDS 
  All these field names are case sensitive! 
    - artifactTypeId
    - closedDate
    - comments (since last visit)
    - completionPercent
    - concurrencyDate
    - creationDate
    - description
    - detectedReleaseId
    - detectedReleaseVersionNumber
    - estimatedEffort
    - fixedBuildId
    - fixedBuildName
    - incidentId
    - incidentStatusId
    - incidentStatusName
    - incidentStatusOpenStatus
    - incidentTypeId
    - incidentTypeName
    - name
    - openerId
    - openerName
    - ownerId
    - priorityId
    - priorityName
    - projectedEffort
    - projectId
    - projectName
    - remainingEffort
    - resolvedReleaseId
    - resolvedReleaseVersionNumber
    - severityId
    - severityName
    - startDate
    - testRunStepId
    - verifiedReleaseId
    - verifiedReleaseVersionNumber
 -->


	<xsl:template match="/ccf:artifact[@artifactType = &quot;plainArtifact&quot;]">
		<artifact xmlns="http://ccf.open.collab.net/GenericArtifactV1.0">
			<xsl:copy-of select="@*"/>
			<xsl:apply-templates/>
		</artifact>
	</xsl:template>
	<xsl:template match="/ccf:artifact[@artifactType = 'attachment']">
		<xsl:copy-of select="."/>
	</xsl:template>
	
	<xsl:template match="ccf:field[@fieldName='incidentId']">
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">SpiraTest Incident ID</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="."/>
		</field>
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">Incident URL</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:text>https://test.ebaotech.com/SpiraTest/21/Incident/<xsl:value-of select="."/>.aspx</xsl:text>
		</field>

	</xsl:template>

	<xsl:template match="ccf:field[@fieldName='creationDate']">
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">Test Date</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="."/>
		</field>
	</xsl:template>
	
	<xsl:template match="ccf:field[@fieldName='description']">
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">description</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:choose>
				<xsl:when test="@fieldValueType='HTMLString'">
					<xsl:value-of select="stringutil:stripHTML(string(.))"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="."/>
				</xsl:otherwise>
			</xsl:choose>
		</field>
	</xsl:template>
	<xsl:template match="ccf:field[@fieldName='name']">
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">title</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:value-of select="."/>
		</field>
	</xsl:template>

	<xsl:template match="ccf:field[@fieldName='incidentStatusName']">
		<xsl:variable name="statusValue" as="xs:string" select="."/>
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">status</xsl:attribute>
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
	

	<xsl:template match="ccf:field[@fieldName='priorityName']">
		<xsl:variable name="priorityValue" as="xs:string" select="."/>
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">priority</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:if test="$priorityValue = '1 - Critical'">
				<xsl:text>1</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = '2 - High'">
				<xsl:text>2</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = '3 - Medium'">
				<xsl:text>3</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = '4 - Low'">
				<xsl:text>4</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
		<xsl:template match="ccf:field[@fieldName='comments']">
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">Comment Text</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:choose>
				<xsl:when test="@fieldValueType='HTMLString'">
					<xsl:value-of select="stringutil:stripHTML(string(.))"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="."/>
				</xsl:otherwise>
			</xsl:choose>
		</field>
	</xsl:template>
	<xsl:template match="text()"/>
</xsl:stylesheet>