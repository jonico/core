<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:ccf="http://ccf.open.collab.net/GenericArtifactV1.0" xmlns:stringutil="xalan://com.collabnet.ccf.core.utils.GATransformerUtil" version="2.0" exclude-result-prefixes="stringutil xsl xs fn ccf"><!--Automatically generated stylesheet to convert from a repository specific schema to the generic artifact format--><xsl:template match="node()" priority="1"/><xsl:template match="/artifact" priority="2"><artifact xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://ccf.open.collab.net/GenericArtifactV1.0 http://ccf.open.collab.net/files/documents/177/1972/genericartifactschema.xsd"><xsl:copy-of select="topLevelAttributes/@*"/><xsl:apply-templates/></artifact></xsl:template><xsl:template match="summary" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="String" nullValueSupported="true" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">summary</xsl:attribute><xsl:attribute name="alternativeFieldName">summary</xsl:attribute><xsl:choose><xsl:when test="text()!=''"><xsl:attribute name="fieldValueIsNull">false</xsl:attribute></xsl:when><xsl:otherwise><xsl:attribute name="fieldValueIsNull">true</xsl:attribute></xsl:otherwise></xsl:choose><xsl:value-of select="."/></field></xsl:template><xsl:template match="timetracking" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="String" nullValueSupported="true" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">timetracking</xsl:attribute><xsl:attribute name="alternativeFieldName">timetracking</xsl:attribute><xsl:choose><xsl:when test="text()!=''"><xsl:attribute name="fieldValueIsNull">false</xsl:attribute></xsl:when><xsl:otherwise><xsl:attribute name="fieldValueIsNull">true</xsl:attribute></xsl:otherwise></xsl:choose><xsl:value-of select="."/></field></xsl:template><xsl:template match="issuetype" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="String" nullValueSupported="true" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">issuetype</xsl:attribute><xsl:attribute name="alternativeFieldName">issuetype</xsl:attribute><xsl:choose><xsl:when test="text()!=''"><xsl:attribute name="fieldValueIsNull">false</xsl:attribute></xsl:when><xsl:otherwise><xsl:attribute name="fieldValueIsNull">true</xsl:attribute></xsl:otherwise></xsl:choose><xsl:value-of select="."/></field></xsl:template><xsl:template match="fixVersions" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="String" nullValueSupported="true" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">fixVersions</xsl:attribute><xsl:attribute name="alternativeFieldName">fixVersions</xsl:attribute><xsl:choose><xsl:when test="text()!=''"><xsl:attribute name="fieldValueIsNull">false</xsl:attribute></xsl:when><xsl:otherwise><xsl:attribute name="fieldValueIsNull">true</xsl:attribute></xsl:otherwise></xsl:choose><xsl:value-of select="."/></field></xsl:template><xsl:template match="reporter" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="String" nullValueSupported="true" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">reporter</xsl:attribute><xsl:attribute name="alternativeFieldName">reporter</xsl:attribute><xsl:choose><xsl:when test="text()!=''"><xsl:attribute name="fieldValueIsNull">false</xsl:attribute></xsl:when><xsl:otherwise><xsl:attribute name="fieldValueIsNull">true</xsl:attribute></xsl:otherwise></xsl:choose><xsl:value-of select="."/></field></xsl:template><xsl:template match="description" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="String" nullValueSupported="true" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">description</xsl:attribute><xsl:attribute name="alternativeFieldName">description</xsl:attribute><xsl:choose><xsl:when test="text()!=''"><xsl:attribute name="fieldValueIsNull">false</xsl:attribute></xsl:when><xsl:otherwise><xsl:attribute name="fieldValueIsNull">true</xsl:attribute></xsl:otherwise></xsl:choose><xsl:value-of select="."/></field></xsl:template><xsl:template match="priority" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="String" nullValueSupported="true" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">priority</xsl:attribute><xsl:attribute name="alternativeFieldName">priority</xsl:attribute><xsl:choose><xsl:when test="text()!=''"><xsl:attribute name="fieldValueIsNull">false</xsl:attribute></xsl:when><xsl:otherwise><xsl:attribute name="fieldValueIsNull">true</xsl:attribute></xsl:otherwise></xsl:choose><xsl:value-of select="."/></field></xsl:template><xsl:template match="duedate" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="String" nullValueSupported="true" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">duedate</xsl:attribute><xsl:attribute name="alternativeFieldName">duedate</xsl:attribute><xsl:choose><xsl:when test="text()!=''"><xsl:attribute name="fieldValueIsNull">false</xsl:attribute></xsl:when><xsl:otherwise><xsl:attribute name="fieldValueIsNull">true</xsl:attribute></xsl:otherwise></xsl:choose><xsl:value-of select="."/></field></xsl:template><xsl:template match="customfield_10001" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="String" nullValueSupported="true" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">customfield_10001</xsl:attribute><xsl:attribute name="alternativeFieldName">customfield_10001</xsl:attribute><xsl:choose><xsl:when test="text()!=''"><xsl:attribute name="fieldValueIsNull">false</xsl:attribute></xsl:when><xsl:otherwise><xsl:attribute name="fieldValueIsNull">true</xsl:attribute></xsl:otherwise></xsl:choose><xsl:value-of select="."/></field></xsl:template><xsl:template match="customfield_10000" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="String" nullValueSupported="true" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">customfield_10000</xsl:attribute><xsl:attribute name="alternativeFieldName">customfield_10000</xsl:attribute><xsl:choose><xsl:when test="text()!=''"><xsl:attribute name="fieldValueIsNull">false</xsl:attribute></xsl:when><xsl:otherwise><xsl:attribute name="fieldValueIsNull">true</xsl:attribute></xsl:otherwise></xsl:choose><xsl:value-of select="."/></field></xsl:template><xsl:template match="labels" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="String" nullValueSupported="true" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">labels</xsl:attribute><xsl:attribute name="alternativeFieldName">labels</xsl:attribute><xsl:choose><xsl:when test="text()!=''"><xsl:attribute name="fieldValueIsNull">false</xsl:attribute></xsl:when><xsl:otherwise><xsl:attribute name="fieldValueIsNull">true</xsl:attribute></xsl:otherwise></xsl:choose><xsl:value-of select="."/></field></xsl:template><xsl:template match="assignee" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="String" nullValueSupported="true" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">assignee</xsl:attribute><xsl:attribute name="alternativeFieldName">assignee</xsl:attribute><xsl:choose><xsl:when test="text()!=''"><xsl:attribute name="fieldValueIsNull">false</xsl:attribute></xsl:when><xsl:otherwise><xsl:attribute name="fieldValueIsNull">true</xsl:attribute></xsl:otherwise></xsl:choose><xsl:value-of select="."/></field></xsl:template><xsl:template match="attachment" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="String" nullValueSupported="true" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">attachment</xsl:attribute><xsl:attribute name="alternativeFieldName">attachment</xsl:attribute><xsl:choose><xsl:when test="text()!=''"><xsl:attribute name="fieldValueIsNull">false</xsl:attribute></xsl:when><xsl:otherwise><xsl:attribute name="fieldValueIsNull">true</xsl:attribute></xsl:otherwise></xsl:choose><xsl:value-of select="."/></field></xsl:template><xsl:template match="versions" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="String" nullValueSupported="true" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">versions</xsl:attribute><xsl:attribute name="alternativeFieldName">versions</xsl:attribute><xsl:choose><xsl:when test="text()!=''"><xsl:attribute name="fieldValueIsNull">false</xsl:attribute></xsl:when><xsl:otherwise><xsl:attribute name="fieldValueIsNull">true</xsl:attribute></xsl:otherwise></xsl:choose><xsl:value-of select="."/></field></xsl:template><xsl:template match="project" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="String" nullValueSupported="true" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">project</xsl:attribute><xsl:attribute name="alternativeFieldName">project</xsl:attribute><xsl:choose><xsl:when test="text()!=''"><xsl:attribute name="fieldValueIsNull">false</xsl:attribute></xsl:when><xsl:otherwise><xsl:attribute name="fieldValueIsNull">true</xsl:attribute></xsl:otherwise></xsl:choose><xsl:value-of select="."/></field></xsl:template><xsl:template match="environment" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="String" nullValueSupported="true" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">environment</xsl:attribute><xsl:attribute name="alternativeFieldName">environment</xsl:attribute><xsl:choose><xsl:when test="text()!=''"><xsl:attribute name="fieldValueIsNull">false</xsl:attribute></xsl:when><xsl:otherwise><xsl:attribute name="fieldValueIsNull">true</xsl:attribute></xsl:otherwise></xsl:choose><xsl:value-of select="."/></field></xsl:template><xsl:template match="components" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="String" nullValueSupported="true" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">components</xsl:attribute><xsl:attribute name="alternativeFieldName">components</xsl:attribute><xsl:choose><xsl:when test="text()!=''"><xsl:attribute name="fieldValueIsNull">false</xsl:attribute></xsl:when><xsl:otherwise><xsl:attribute name="fieldValueIsNull">true</xsl:attribute></xsl:otherwise></xsl:choose><xsl:value-of select="."/></field></xsl:template><xsl:template match="status" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="String" nullValueSupported="true" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">status</xsl:attribute><xsl:attribute name="alternativeFieldName">status</xsl:attribute><xsl:choose><xsl:when test="text()!=''"><xsl:attribute name="fieldValueIsNull">false</xsl:attribute></xsl:when><xsl:otherwise><xsl:attribute name="fieldValueIsNull">true</xsl:attribute></xsl:otherwise></xsl:choose><xsl:value-of select="."/></field></xsl:template><xsl:template match="comment" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="String" nullValueSupported="true" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">comment</xsl:attribute><xsl:attribute name="alternativeFieldName">comment</xsl:attribute><xsl:choose><xsl:when test="text()!=''"><xsl:attribute name="fieldValueIsNull">false</xsl:attribute></xsl:when><xsl:otherwise><xsl:attribute name="fieldValueIsNull">true</xsl:attribute></xsl:otherwise></xsl:choose><xsl:value-of select="."/></field></xsl:template></xsl:stylesheet>