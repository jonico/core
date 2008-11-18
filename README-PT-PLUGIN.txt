This README file gives the details about the meta data fields
that are shipped by the CEE Project Tracker plugin.

Meta data fields that are shipped by PT plugin
----------------------------------------------

1. Created on time
    This field's value is encoded in a GenericArtifact field
    with the name "{urn:ws.tracker.collabnet.com}createdOn"
    
    To enable this field to be shipped to a target system, include
    the following mapping in the XSLT transformation file.
    
    <xsl:template
        match='ccf:field[@fieldName="{urn:ws.tracker.collabnet.com}createdOn"]'>
        <field>
            <xsl:attribute name="fieldName">Created On field Name on the target system</xsl:attribute>
            <xsl:attribute name="fieldDisplayName">Display name of the Created On field on the target system</xsl:attribute>
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
    
2. Reason field
    This field's value is encoded in a GenericArtifact field
    with the name "{urn:ws.tracker.collabnet.com}reason"
    
    To enable this field to be shipped to a target system, include
    the following mapping in the XSLT transformation file.
    
    <xsl:template
        match='ccf:field[@fieldName="{urn:ws.tracker.collabnet.com}reason"]'>
        <field>
            <xsl:attribute name="fieldName">Reason field Name on the target system</xsl:attribute>
            <xsl:attribute name="fieldDisplayName">Reason field display Name on the target system</xsl:attribute>
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
    
3. Modified by field
    This field's value is encoded in a GenericArtifact field
    with the name "{urn:ws.tracker.collabnet.com}modifiedBy"
    
    To enable this field to be shipped to a target system, include
    the following mapping in the XSLT transformation file.
    
    <xsl:template
        match='ccf:field[@fieldName="{urn:ws.tracker.collabnet.com}modifiedBy"]'>
        <field>
            <xsl:attribute name="fieldName">Modified By Name on the target system</xsl:attribute>
            <xsl:attribute name="fieldDisplayName">Modified By display Name on the target system</xsl:attribute>
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
    
4. Created by field
    This field's value is encoded in a GenericArtifact field
    with the name "{urn:ws.tracker.collabnet.com}createdBy"
    
    To enable this field to be shipped to a target system, include
    the following mapping in the XSLT transformation file.
    
    <xsl:template
        match='ccf:field[@fieldName="{urn:ws.tracker.collabnet.com}createdBy"]'>
        <field>
            <xsl:attribute name="fieldName">Created by field Name on the target system</xsl:attribute>
            <xsl:attribute name="fieldDisplayName">Created by field display Name</xsl:attribute>
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
    
5. Modified on field
    This field's value is encoded in a GenericArtifact field
    with the name "{urn:ws.tracker.collabnet.com}modifiedOn"
    
    To enable this field to be shipped to a target system, include
    the following mapping in the XSLT transformation file.
    
    <xsl:template
        match='ccf:field[@fieldName="{urn:ws.tracker.collabnet.com}modifiedOn"]'>
        <field>
            <xsl:attribute name="fieldName">Modified on field Name on the target system</xsl:attribute>
            <xsl:attribute name="fieldDisplayName">Modified on field display Name</xsl:attribute>
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

5. PT artifact id
    This field's value is encoded in a GenericArtifact field
    with the name "{urn:ws.tracker.collabnet.com}id"
    
    To enable this field to be shipped to a target system, include
    the following mapping in the XSLT transformation file.
    
    <xsl:template
        match='ccf:field[@fieldName="{urn:ws.tracker.collabnet.com}id"]'>
        <field>
            <xsl:attribute name="fieldName">PT artifact id field Name on the target system</xsl:attribute>
            <xsl:attribute name="fieldDisplayName">PT artifact id field display Name</xsl:attribute>
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
 
 5. Comments
    This field's value is encoded in a GenericArtifact field
    with the name "{urn:ws.tracker.collabnet.com}comment"
    
    To enable this field to be shipped to a target system, include
    the following mapping in the XSLT transformation file.
    
    <xsl:template
        match='ccf:field[@fieldName="{urn:ws.tracker.collabnet.com}comment"]'>
        <field>
            <xsl:attribute name="fieldName">Comment field Name on the target system</xsl:attribute>
            <xsl:attribute name="fieldDisplayName">PT artifact id field display Name</xsl:attribute>
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