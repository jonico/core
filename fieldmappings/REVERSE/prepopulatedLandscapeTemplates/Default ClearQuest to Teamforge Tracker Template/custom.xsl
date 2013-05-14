<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ccf="http://ccf.open.collab.net/GenericArtifactV1.0" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:stringutil="xalan://com.collabnet.ccf.core.utils.GATransformerUtil" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="2.0" exclude-result-prefixes="xsl xs ccf stringutil" xsi:schemaLocation="http://www.w3.org/1999/XSL/Transform http://www.w3.org/2007/schema-for-xslt20.xsd">
	<xsl:template match="/ccf:artifact[@artifactType = &quot;plainArtifact&quot;]">
		<artifact xmlns="http://ccf.open.collab.net/GenericArtifactV1.0">
			<xsl:copy-of select="@*"/>
			<xsl:apply-templates/>
			<xsl:if test="not(ccf:field[@fieldName=&quot;title&quot;])">
				<field>
					<xsl:attribute name="fieldName">title</xsl:attribute>
					<xsl:attribute name="fieldAction">replace</xsl:attribute>
					<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
					<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
					<xsl:attribute name="fieldValueType">String</xsl:attribute>
					<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
					(no headline in CQ)
				</field>
			</xsl:if>
			<xsl:if test="not(ccf:field[@fieldName=&quot;description&quot;])">
				<field>
					<xsl:attribute name="fieldName">description</xsl:attribute>
					<xsl:attribute name="fieldAction">replace</xsl:attribute>
					<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
					<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
					<xsl:attribute name="fieldValueType">String</xsl:attribute>
					<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
					(no description in CQ)
				</field>
			</xsl:if>
			<xsl:if test="not(ccf:field[@fieldName=&quot;status&quot;])">
				<field>
					<xsl:attribute name="fieldName">status</xsl:attribute>
					<xsl:attribute name="fieldAction">replace</xsl:attribute>
					<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
					<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
					<xsl:attribute name="fieldValueType">String</xsl:attribute>
					<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
					<xsl:text>Open</xsl:text>
				</field>
			</xsl:if>
			<!-- uncomment the folllowing line fail before XML conversion, the payload will be in the log file -->
			<xsl:wrong>yeah</xsl:wrong>
			<!-- uncomment the folllowing field block to create a failed shipment after conversion -->
			<field>
				<xsl:attribute name="fieldName">status</xsl:attribute>
				<xsl:attribute name="fieldAction">replace</xsl:attribute>
				<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
				<xsl:attribute name="fieldValueHasChanged">true</xsl:attribute>
				<xsl:attribute name="fieldValueType">String</xsl:attribute>
				<xsl:attribute name="fieldValueIsNull">false</xsl:attribute>
				<xsl:text>THE STATUS DOES NOT EXIST</xsl:text>
			</field>
			
		</artifact>
	</xsl:template>

	<xsl:template match="/ccf:artifact[@artifactType = 'attachment']">
		<xsl:copy-of select="."/>
	</xsl:template>

	<!-- ID field of clearquest goes in flexfield -->
	<xsl:template match="ccf:field[@fieldName=&quot;id&quot;]">
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">RCQ-ID</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:attribute name="fieldValueType">String</xsl:attribute>
			<xsl:value-of select="."/>
		</field>
	</xsl:template>

	<!-- Headline in cq, Title in tf -->
	<xsl:template match="ccf:field[@fieldName=&quot;headline&quot;]">
		<xsl:variable name="titleValue" as="xs:string" select="."/>
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">title</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:value-of select="."/>
		</field>
	</xsl:template>

	<!-- Description in cq and tf -->
	<xsl:template match="ccf:field[@fieldName=&quot;description&quot;]">
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
	
	

	<xsl:template match="text()"/>
</xsl:stylesheet>