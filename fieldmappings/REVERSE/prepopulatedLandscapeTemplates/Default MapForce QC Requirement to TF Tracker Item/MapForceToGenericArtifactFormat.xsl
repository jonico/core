<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:ccf="http://ccf.open.collab.net/GenericArtifactV1.0" version="2.0" exclude-result-prefixes="xsl xs fn ccf"><!--Automatically generated stylesheet to convert from a repository specific schema to the generic artifact format--><xsl:template match="node()" priority="1"/><xsl:template match="/artifact" priority="2"><artifact xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://ccf.open.collab.net/GenericArtifactV1.0 http://ccf.open.collab.net/files/documents/177/1972/genericartifactschema.xsd"><xsl:copy-of select="topLevelAttributes/@*"/><xsl:apply-templates/></artifact></xsl:template><xsl:template match="actualHours" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="Integer" nullValueSupported="false" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">actualHours</xsl:attribute><xsl:attribute name="alternativeFieldName">actualHours</xsl:attribute><xsl:attribute name="fieldValueIsNull">false</xsl:attribute><xsl:value-of select="text()"/></field></xsl:template><xsl:template match="estimatedHours" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="Integer" nullValueSupported="false" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">estimatedHours</xsl:attribute><xsl:attribute name="alternativeFieldName">estimatedHours</xsl:attribute><xsl:attribute name="fieldValueIsNull">false</xsl:attribute><xsl:value-of select="text()"/></field></xsl:template><xsl:template match="remainingEffort" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="Integer" nullValueSupported="false" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">remainingEffort</xsl:attribute><xsl:attribute name="alternativeFieldName">remainingEffort</xsl:attribute><xsl:attribute name="fieldValueIsNull">false</xsl:attribute><xsl:value-of select="text()"/></field></xsl:template><xsl:template match="planningFolder" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="String" nullValueSupported="true" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">planningFolder</xsl:attribute><xsl:attribute name="alternativeFieldName">planningFolder</xsl:attribute><xsl:choose><xsl:when test="text()!=''"><xsl:attribute name="fieldValueIsNull">false</xsl:attribute></xsl:when><xsl:otherwise><xsl:attribute name="fieldValueIsNull">true</xsl:attribute></xsl:otherwise></xsl:choose><xsl:value-of select="text()"/></field></xsl:template><xsl:template match="autosumming" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="Boolean" nullValueSupported="false" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">autosumming</xsl:attribute><xsl:attribute name="alternativeFieldName">autosumming</xsl:attribute><xsl:attribute name="fieldValueIsNull">false</xsl:attribute><xsl:value-of select="text()"/></field></xsl:template><xsl:template match="assignedTo" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="User" nullValueSupported="true" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">assignedTo</xsl:attribute><xsl:attribute name="alternativeFieldName">assignedTo</xsl:attribute><xsl:choose><xsl:when test="text()!=''"><xsl:attribute name="fieldValueIsNull">false</xsl:attribute></xsl:when><xsl:otherwise><xsl:attribute name="fieldValueIsNull">true</xsl:attribute></xsl:otherwise></xsl:choose><xsl:value-of select="text()"/></field></xsl:template><xsl:template match="category" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="String" nullValueSupported="true" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">category</xsl:attribute><xsl:attribute name="alternativeFieldName">category</xsl:attribute><xsl:choose><xsl:when test="text()!=''"><xsl:attribute name="fieldValueIsNull">false</xsl:attribute></xsl:when><xsl:otherwise><xsl:attribute name="fieldValueIsNull">true</xsl:attribute></xsl:otherwise></xsl:choose><xsl:value-of select="text()"/></field></xsl:template><xsl:template match="closeDate" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="DateTime" nullValueSupported="true" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">closeDate</xsl:attribute><xsl:attribute name="alternativeFieldName">closeDate</xsl:attribute><xsl:choose><xsl:when test="text()!=''"><xsl:attribute name="fieldValueIsNull">false</xsl:attribute></xsl:when><xsl:otherwise><xsl:attribute name="fieldValueIsNull">true</xsl:attribute></xsl:otherwise></xsl:choose><xsl:value-of select="text()"/></field></xsl:template><xsl:template match="customer" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="String" nullValueSupported="false" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">customer</xsl:attribute><xsl:attribute name="alternativeFieldName">customer</xsl:attribute><xsl:attribute name="fieldValueIsNull">false</xsl:attribute><xsl:value-of select="text()"/></field></xsl:template><xsl:template match="description" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="String" nullValueSupported="false" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">description</xsl:attribute><xsl:attribute name="alternativeFieldName">description</xsl:attribute><xsl:attribute name="fieldValueIsNull">false</xsl:attribute><xsl:value-of select="text()"/></field></xsl:template><xsl:template match="group" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="String" nullValueSupported="false" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">group</xsl:attribute><xsl:attribute name="alternativeFieldName">group</xsl:attribute><xsl:attribute name="fieldValueIsNull">false</xsl:attribute><xsl:value-of select="text()"/></field></xsl:template><xsl:template match="priority" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="Integer" nullValueSupported="false" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">priority</xsl:attribute><xsl:attribute name="alternativeFieldName">priority</xsl:attribute><xsl:attribute name="fieldValueIsNull">false</xsl:attribute><xsl:value-of select="text()"/></field></xsl:template><xsl:template match="reportedReleaseId" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="String" nullValueSupported="true" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">reportedReleaseId</xsl:attribute><xsl:attribute name="alternativeFieldName">reportedReleaseId</xsl:attribute><xsl:choose><xsl:when test="text()!=''"><xsl:attribute name="fieldValueIsNull">false</xsl:attribute></xsl:when><xsl:otherwise><xsl:attribute name="fieldValueIsNull">true</xsl:attribute></xsl:otherwise></xsl:choose><xsl:value-of select="text()"/></field></xsl:template><xsl:template match="resolvedReleaseId" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="String" nullValueSupported="true" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">resolvedReleaseId</xsl:attribute><xsl:attribute name="alternativeFieldName">resolvedReleaseId</xsl:attribute><xsl:choose><xsl:when test="text()!=''"><xsl:attribute name="fieldValueIsNull">false</xsl:attribute></xsl:when><xsl:otherwise><xsl:attribute name="fieldValueIsNull">true</xsl:attribute></xsl:otherwise></xsl:choose><xsl:value-of select="text()"/></field></xsl:template><xsl:template match="status" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="String" nullValueSupported="false" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">status</xsl:attribute><xsl:attribute name="alternativeFieldName">status</xsl:attribute><xsl:attribute name="fieldValueIsNull">false</xsl:attribute><xsl:value-of select="text()"/></field></xsl:template><xsl:template match="statusClass" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="String" nullValueSupported="false" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">statusClass</xsl:attribute><xsl:attribute name="alternativeFieldName">statusClass</xsl:attribute><xsl:attribute name="fieldValueIsNull">false</xsl:attribute><xsl:value-of select="text()"/></field></xsl:template><xsl:template match="folderId" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="String" nullValueSupported="false" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">folderId</xsl:attribute><xsl:attribute name="alternativeFieldName">folderId</xsl:attribute><xsl:attribute name="fieldValueIsNull">false</xsl:attribute><xsl:value-of select="text()"/></field></xsl:template><xsl:template match="path" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="String" nullValueSupported="false" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">path</xsl:attribute><xsl:attribute name="alternativeFieldName">path</xsl:attribute><xsl:attribute name="fieldValueIsNull">false</xsl:attribute><xsl:value-of select="text()"/></field></xsl:template><xsl:template match="title" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="String" nullValueSupported="false" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">title</xsl:attribute><xsl:attribute name="alternativeFieldName">title</xsl:attribute><xsl:attribute name="fieldValueIsNull">false</xsl:attribute><xsl:value-of select="text()"/></field></xsl:template><xsl:template match="createdBy" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="User" nullValueSupported="false" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">createdBy</xsl:attribute><xsl:attribute name="alternativeFieldName">createdBy</xsl:attribute><xsl:attribute name="fieldValueIsNull">false</xsl:attribute><xsl:value-of select="text()"/></field></xsl:template><xsl:template match="createdDate" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="DateTime" nullValueSupported="false" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">createdDate</xsl:attribute><xsl:attribute name="alternativeFieldName">createdDate</xsl:attribute><xsl:attribute name="fieldValueIsNull">false</xsl:attribute><xsl:value-of select="text()"/></field></xsl:template><xsl:template match="id" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="String" nullValueSupported="false" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">id</xsl:attribute><xsl:attribute name="alternativeFieldName">id</xsl:attribute><xsl:attribute name="fieldValueIsNull">false</xsl:attribute><xsl:value-of select="text()"/></field></xsl:template><xsl:template match="lastModifiedBy" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="User" nullValueSupported="false" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">lastModifiedBy</xsl:attribute><xsl:attribute name="alternativeFieldName">lastModifiedBy</xsl:attribute><xsl:attribute name="fieldValueIsNull">false</xsl:attribute><xsl:value-of select="text()"/></field></xsl:template><xsl:template match="lastModifiedDate" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="DateTime" nullValueSupported="false" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">lastModifiedDate</xsl:attribute><xsl:attribute name="alternativeFieldName">lastModifiedDate</xsl:attribute><xsl:attribute name="fieldValueIsNull">false</xsl:attribute><xsl:value-of select="text()"/></field></xsl:template><xsl:template match="version" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="mandatoryField" fieldValueType="String" nullValueSupported="false" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">version</xsl:attribute><xsl:attribute name="alternativeFieldName">version</xsl:attribute><xsl:attribute name="fieldValueIsNull">false</xsl:attribute><xsl:value-of select="text()"/></field></xsl:template><xsl:template match="CommentText" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="flexField" fieldValueType="String" nullValueSupported="false" minOccurs="0" maxOccurs="unbounded" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">Comment Text</xsl:attribute><xsl:attribute name="alternativeFieldName">CommentText</xsl:attribute><xsl:attribute name="fieldValueIsNull">false</xsl:attribute><xsl:value-of select="text()"/></field></xsl:template><xsl:template match="X_P_Completed" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="flexField" fieldValueType="String" nullValueSupported="false" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">% Completed</xsl:attribute><xsl:attribute name="alternativeFieldName">X_P_Completed</xsl:attribute><xsl:attribute name="fieldValueIsNull">false</xsl:attribute><xsl:value-of select="text()"/></field></xsl:template><xsl:template match="OriginalEstimatedEffort" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="flexField" fieldValueType="String" nullValueSupported="false" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">Original Estimated Effort</xsl:attribute><xsl:attribute name="alternativeFieldName">OriginalEstimatedEffort</xsl:attribute><xsl:attribute name="fieldValueIsNull">false</xsl:attribute><xsl:value-of select="text()"/></field></xsl:template><xsl:template match="Position" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="flexField" fieldValueType="String" nullValueSupported="false" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">Position</xsl:attribute><xsl:attribute name="alternativeFieldName">Position</xsl:attribute><xsl:attribute name="fieldValueIsNull">false</xsl:attribute><xsl:value-of select="text()"/></field></xsl:template><xsl:template match="TaskType" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="flexField" fieldValueType="String" nullValueSupported="false" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">Task Type</xsl:attribute><xsl:attribute name="alternativeFieldName">TaskType</xsl:attribute><xsl:attribute name="fieldValueIsNull">false</xsl:attribute><xsl:value-of select="text()"/></field></xsl:template><xsl:template match="Team" priority="2"><field xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" fieldType="flexField" fieldValueType="String" nullValueSupported="false" minOccurs="0" maxOccurs="1" fieldValueHasChanged="true" fieldAction="replace"><xsl:attribute name="fieldName">Team</xsl:attribute><xsl:attribute name="alternativeFieldName">Team</xsl:attribute><xsl:attribute name="fieldValueIsNull">false</xsl:attribute><xsl:value-of select="text()"/></field></xsl:template></xsl:stylesheet>