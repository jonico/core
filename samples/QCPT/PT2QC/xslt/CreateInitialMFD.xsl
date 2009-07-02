<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:fn="http://www.w3.org/2005/xpath-functions" version="2.0" exclude-result-prefixes="xsl xs fn">
  <xsl:template match="node()">
    <mapping xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="11">  
      <component name="defaultmap1" blackbox="0" editable="1"> 
        <properties SelectedLanguage="xslt"/>  
        <structure> 
          <children> 
            <component name="document" library="xml" uid="2" kind="14"> 
              <properties XSLTTargetEncoding="UTF-8" XSLTDefaultOutput="1"/>  
              <view ltx="557" rbx="936" rby="1263"/>  
              <data> 
                <root scrollposition="1"> 
                  <header> 
                    <namespaces> 
                      <namespace/> 
                    </namespaces> 
                  </header>  
                  <entry name="artifact" inpkey="84115200" expanded="1"> 
                    <entry name="topLevelAttributes" inpkey="76457344"> 
                      <entry name="artifactMode" type="attribute" inpkey="73669616"/>  
                      <entry name="artifactAction" type="attribute" inpkey="76457448"/>  
                      <entry name="sourceArtifactVersion" type="attribute" inpkey="76457552"/>  
                      <entry name="targetArtifactVersion" type="attribute" inpkey="84166472"/>  
                      <entry name="sourceArtifactLastModifiedDate" type="attribute" inpkey="84166576"/>  
                      <entry name="targetArtifactLastModifiedDate" type="attribute" inpkey="84166680"/>  
                      <entry name="conflictResolutionPriority" type="attribute" inpkey="84166784"/>  
                      <entry name="artifactType" type="attribute" inpkey="84166888"/>  
                      <entry name="sourceSystemKind" type="attribute" inpkey="84166992"/>  
                      <entry name="sourceSystemId" type="attribute" inpkey="84167096"/>  
                      <entry name="sourceRepositoryKind" type="attribute" inpkey="84167200"/>  
                      <entry name="sourceRepositoryId" type="attribute" inpkey="84167304"/>  
                      <entry name="sourceArtifactId" type="attribute" inpkey="84167408"/>  
                      <entry name="targetSystemKind" type="attribute" inpkey="84167512"/>  
                      <entry name="targetSystemId" type="attribute" inpkey="84167616"/>  
                      <entry name="targetRepositoryKind" type="attribute" inpkey="84167720"/>  
                      <entry name="targetRepositoryId" type="attribute" inpkey="84167824"/>  
                      <entry name="targetArtifactId" type="attribute" inpkey="84167928"/>  
                      <entry name="depParentSourceRepositoryKind" type="attribute" inpkey="84168032"/>  
                      <entry name="depParentSourceRepositoryId" type="attribute" inpkey="84168224"/>  
                      <entry name="depParentSourceArtifactId" type="attribute" inpkey="84168416"/>  
                      <entry name="depChildSourceRepositoryKind" type="attribute" inpkey="76445184"/>  
                      <entry name="depChildSourceRepositoryId" type="attribute" inpkey="76445376"/>  
                      <entry name="depChildSourceArtifactId" type="attribute" inpkey="84168608"/>  
                      <entry name="depParentTargetRepositoryKind" type="attribute" inpkey="76444992"/>  
                      <entry name="depParentTargetRepositoryId" type="attribute" inpkey="76445568"/>  
                      <entry name="depParentTargetArtifactId" type="attribute" inpkey="76445760"/>  
                      <entry name="depChildTargetRepositoryKind" type="attribute" inpkey="76445952"/>  
                      <entry name="depChildTargetRepositoryId" type="attribute" inpkey="76446144"/>  
                      <entry name="depChildTargetArtifactId" type="attribute" inpkey="76446336"/>  
                      <entry name="errorCode" type="attribute" inpkey="76446528"/>  
                      <entry name="transactionId" type="attribute" inpkey="76446632"/>  
                      <entry name="includesFieldMetaData" type="attribute" inpkey="76446736"/>  
                      <entry name="sourceSystemTimezone" type="attribute" inpkey="76446928"/>  
                      <entry name="targetSystemTimezone" type="attribute" inpkey="76447032"/> 
                    </entry>  
                    <entry name="BG_DEV_COMMENTS" inpkey="84170624"/> 
                  </entry> 
                </root>  
                <document schema="{@targetSchemaName}">
                  <xsl:attribute name="instanceroot">{}artifact</xsl:attribute>
                </document>  
                <wsdl/> 
              </data> 
            </component>  
            <component name="document" library="xml" uid="1" kind="14"> 
              <properties XSLTTargetEncoding="UTF-8"/>  
              <view rbx="367" rby="1262"/>  
              <data> 
                <root scrollposition="1"> 
                  <header> 
                    <namespaces> 
                      <namespace/> 
                    </namespaces> 
                  </header>  
                  <entry name="artifact" outkey="84194704" expanded="1"> 
                    <entry name="topLevelAttributes" outkey="84194240"> 
                      <entry name="artifactMode" type="attribute" outkey="84194480"/>  
                      <entry name="artifactAction" type="attribute" outkey="84194928"/>  
                      <entry name="sourceArtifactVersion" type="attribute" outkey="84195184"/>  
                      <entry name="targetArtifactVersion" type="attribute" outkey="84195456"/>  
                      <entry name="sourceArtifactLastModifiedDate" type="attribute" outkey="84195728"/>  
                      <entry name="targetArtifactLastModifiedDate" type="attribute" outkey="84196088"/>  
                      <entry name="conflictResolutionPriority" type="attribute" outkey="84196360"/>  
                      <entry name="artifactType" type="attribute" outkey="84196544"/>  
                      <entry name="sourceSystemKind" type="attribute" outkey="84196768"/>  
                      <entry name="sourceSystemId" type="attribute" outkey="84197024"/>  
                      <entry name="sourceRepositoryKind" type="attribute" outkey="84197264"/>  
                      <entry name="sourceRepositoryId" type="attribute" outkey="84197504"/>  
                      <entry name="sourceArtifactId" type="attribute" outkey="84197744"/>  
                      <entry name="targetSystemKind" type="attribute" outkey="84197984"/>  
                      <entry name="targetSystemId" type="attribute" outkey="84198224"/>  
                      <entry name="targetRepositoryKind" type="attribute" outkey="84198464"/>  
                      <entry name="targetRepositoryId" type="attribute" outkey="84198704"/>  
                      <entry name="targetArtifactId" type="attribute" outkey="84198944"/>  
                      <entry name="depParentSourceRepositoryKind" type="attribute" outkey="84199184"/>  
                      <entry name="depParentSourceRepositoryId" type="attribute" outkey="84199456"/>  
                      <entry name="depParentSourceArtifactId" type="attribute" outkey="84199728"/>  
                      <entry name="depChildSourceRepositoryKind" type="attribute" outkey="84200000"/>  
                      <entry name="depChildSourceRepositoryId" type="attribute" outkey="84200272"/>  
                      <entry name="depChildSourceArtifactId" type="attribute" outkey="84200544"/>  
                      <entry name="depParentTargetRepositoryKind" type="attribute" outkey="84200816"/>  
                      <entry name="depParentTargetRepositoryId" type="attribute" outkey="84201088"/>  
                      <entry name="depParentTargetArtifactId" type="attribute" outkey="84201360"/>  
                      <entry name="depChildTargetRepositoryKind" type="attribute" outkey="84201632"/>  
                      <entry name="depChildTargetRepositoryId" type="attribute" outkey="84201904"/>  
                      <entry name="depChildTargetArtifactId" type="attribute" outkey="84202176"/>  
                      <entry name="errorCode" type="attribute" outkey="84202448"/>  
                      <entry name="transactionId" type="attribute" outkey="84202712"/>  
                      <entry name="includesFieldMetaData" type="attribute" outkey="84202896"/>  
                      <entry name="sourceSystemTimezone" type="attribute" outkey="84203184"/>  
                      <entry name="targetSystemTimezone" type="attribute" outkey="84203424"/> 
                    </entry>  
                    <entry name="comment" outkey="84222408"/> 
                  </entry> 
                </root>  
                <document schema="{@sourceSchemaName}">
                  <xsl:attribute name="instanceroot">{}artifact</xsl:attribute>
                </document>  
                <wsdl/> 
              </data> 
            </component> 
          </children>  
          <graph directed="1"> 
            <edges/>  
            <vertices> 
              <vertex vertexkey="84194240"> 
                <edges> 
                  <edge vertexkey="76457344" edgekey="84223360"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84194480"> 
                <edges> 
                  <edge vertexkey="73669616" edgekey="84221168"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84194704"> 
                <edges> 
                  <edge vertexkey="84115200" edgekey="84223224"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84194928"> 
                <edges> 
                  <edge vertexkey="76457448" edgekey="84223496"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84195184"> 
                <edges> 
                  <edge vertexkey="76457552" edgekey="84223632"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84195456"> 
                <edges> 
                  <edge vertexkey="84166472" edgekey="84223768"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84195728"> 
                <edges> 
                  <edge vertexkey="84166576" edgekey="84223904"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84196088"> 
                <edges> 
                  <edge vertexkey="84166680" edgekey="84224040"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84196360"> 
                <edges> 
                  <edge vertexkey="84166784" edgekey="84224176"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84196544"> 
                <edges> 
                  <edge vertexkey="84166888" edgekey="84224312"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84196768"> 
                <edges> 
                  <edge vertexkey="84166992" edgekey="84224448"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84197024"> 
                <edges> 
                  <edge vertexkey="84167096" edgekey="84224584"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84197264"> 
                <edges> 
                  <edge vertexkey="84167200" edgekey="84224720"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84197504"> 
                <edges> 
                  <edge vertexkey="84167304" edgekey="84224856"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84197744"> 
                <edges> 
                  <edge vertexkey="84167408" edgekey="84224992"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84197984"> 
                <edges> 
                  <edge vertexkey="84167512" edgekey="84225128"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84198224"> 
                <edges> 
                  <edge vertexkey="84167616" edgekey="84225264"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84198464"> 
                <edges> 
                  <edge vertexkey="84167720" edgekey="84225400"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84198704"> 
                <edges> 
                  <edge vertexkey="84167824" edgekey="84225584"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84198944"> 
                <edges> 
                  <edge vertexkey="84167928" edgekey="84225768"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84199184"> 
                <edges> 
                  <edge vertexkey="84168032" edgekey="84225952"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84199456"> 
                <edges> 
                  <edge vertexkey="84168224" edgekey="84226136"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84199728"> 
                <edges> 
                  <edge vertexkey="84168416" edgekey="84226320"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84200000"> 
                <edges> 
                  <edge vertexkey="76445184" edgekey="84226504"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84200272"> 
                <edges> 
                  <edge vertexkey="76445376" edgekey="84226688"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84200544"> 
                <edges> 
                  <edge vertexkey="84168608" edgekey="84226872"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84200816"> 
                <edges> 
                  <edge vertexkey="76444992" edgekey="84227056"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84201088"> 
                <edges> 
                  <edge vertexkey="76445568" edgekey="84227240"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84201360"> 
                <edges> 
                  <edge vertexkey="76445760" edgekey="84227424"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84201632"> 
                <edges> 
                  <edge vertexkey="76445952" edgekey="84227608"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84201904"> 
                <edges> 
                  <edge vertexkey="76446144" edgekey="84227792"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84202176"> 
                <edges> 
                  <edge vertexkey="76446336" edgekey="84227976"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84202448"> 
                <edges> 
                  <edge vertexkey="76446528" edgekey="84228160"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84202712"> 
                <edges> 
                  <edge vertexkey="76446632" edgekey="84228344"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84202896"> 
                <edges> 
                  <edge vertexkey="76446736" edgekey="84228528"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84203184"> 
                <edges> 
                  <edge vertexkey="76446928" edgekey="84228712"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84203424"> 
                <edges> 
                  <edge vertexkey="76447032" edgekey="84228896"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84222408"> 
                <edges> 
                  <edge vertexkey="84170624" edgekey="73898328"/> 
                </edges> 
              </vertex> 
            </vertices> 
          </graph> 
        </structure> 
      </component> 
    </mapping>
  </xsl:template>
</xsl:stylesheet>
