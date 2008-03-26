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

	<!-- Pattern how to rename a certain field 
		<xsl:template
		match='field[@name="SFEE-Version" and @isFlexField="true"]'
		priority='3'>
		<field name='version' isFlexField='false'>
		<xsl:copy-of select='node()' />
		</field>
		</xsl:template>
	-->

	<!-- Pattern how to filter a certain field  	
		<xsl:template
		match='field[@name="SFEE-Version" and @isFlexField="true"]'
		priority='3'>
		</xsl:template>
	-->

	<!-- Pattern how to copy all fields if there is no rule with a higher priority
		<xsl:template match='field' priority='1'>
		<xsl:copy-of select='.' />
		</xsl:template>
	-->

	<!-- Pattern how to filter all flex-Fields if there is no rule with a higher priority
		<xsl:template match='field[@isFlexField="yes"]' priority='1'>
		</xsl:template>
	-->

	<!-- Pattern how to change the value of a certain field with a value mapping rule and add another field
		<xsl:variable name="trackerMapping">
		<mapping source="tracker1003" target="tracker1006" />
		<mapping source="tracker1004" target="tracker1007" />
		<mapping source="tracker1005" target="tracker1008" />
		<mapping source="tracker1008" target="tracker1005" />
		<mapping source="tracker1007" target="tracker1004" />
		<mapping source="tracker1006" target="tracker1003" />
		</xsl:variable>
		
		<xsl:variable name="conflictMapping">
		<mapping source="tracker1003" override="true" />
		<mapping source="tracker1004" override="true" />
		<mapping source="tracker1005" override="true" />
		<mapping source="tracker1008" override="false" />
		<mapping source="tracker1007" override="false" />
		<mapping source="tracker1006" override="false" />
		</xsl:variable>
		
		
		<xsl:key name='trackerMapper' match='mapping' use='@source' />
		<xsl:key name='conflictMapper' match='mapping' use='@source' />
		
		<xsl:template
		match='field[@name="folderId" and @isFlexField="false"]'
		priority='3'>
		<xsl:variable name="trackerValue" as="xs:string"
		select="value" />
		<field name='forceOverride' isFlexField='false'>
		<type>Boolean</type>
		<value isNull='false'>
		<xsl:value-of
		select='key("conflictMapper",$trackerValue,$conflictMapping)/@override' />
		</value>
		</field>
		<field name='folderId' isFlexField='false'>
		<xsl:copy-of select='type' />
		<value isNull='false'>
		<xsl:value-of
		select='key("trackerMapper",$trackerValue,$trackerMapping)/@target' />
		</value>
		</field>
		</xsl:template>
	-->

	<!-- Pattern how to proceed only dependencies using a special mode (has to be defined)
		<xsl:template match='/SFEEArtifact'>
		<SFEEArtifact>
		<xsl:apply-templates mode="specialDependencyMode"/>
		</SFEEArtifact>
		</xsl:template>
	-->

	<!-- Pattern how to proceed only trackers with folderId tracker1005 using a special mode (has to be defined)
		<xsl:template match='/SFEEArtifact[field[@name="folderId" and @isFlexField="false" and value="tracker1005"]]' priority='2'>
		<SFEEArtifact>
		<xsl:apply-templates mode="specialTracker1005Mode"/>
		</SFEEArtifact>
		</xsl:template>
	-->
	<!--	<xsl:variable name="docRoot" select="/" />-->
  <!-- Whenever you match any node or any attribute -->
  <xsl:template match="node()|@*">
    <!-- Copy the current node -->
    <xsl:copy>
      <!-- Including any child nodes it has and any attributes -->
      <xsl:apply-templates select="node()|@*"/>
    </xsl:copy>
  </xsl:template>

</xsl:stylesheet>
