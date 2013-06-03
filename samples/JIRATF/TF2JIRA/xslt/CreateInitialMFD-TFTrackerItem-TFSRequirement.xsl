<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:fn="http://www.w3.org/2005/xpath-functions" version="2.0" exclude-result-prefixes="xsl xs fn">
  <xsl:template match="node()">
    <mapping xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="12">  
      <component name="defaultmap1" blackbox="0" editable="1"> 
        <properties SelectedLanguage="xslt"/>  
        <structure> 
          <children> 
            <component name="document" library="xml" uid="2" kind="14"> 
              <properties XSLTTargetEncoding="UTF-8" XSLTDefaultOutput="1"/>  
              <view ltx="746" lty="-9" rbx="1122" rby="918"/>  
              <data> 
                <root scrollposition="1"> 
                  <header> 
                    <namespaces> 
                      <namespace/> 
                    </namespaces> 
                  </header>  
                  <entry name="artifact" inpkey="114780096" expanded="1"> 
                    <entry name="topLevelAttributes" inpkey="114779992"> 
                      <entry name="artifactMode" type="attribute" inpkey="114779888"/>  
                      <entry name="artifactAction" type="attribute" inpkey="114779784"/>  
                      <entry name="sourceArtifactVersion" type="attribute" inpkey="114779680"/>  
                      <entry name="targetArtifactVersion" type="attribute" inpkey="114779576"/>  
                      <entry name="sourceArtifactLastModifiedDate" type="attribute" inpkey="114779472"/>  
                      <entry name="targetArtifactLastModifiedDate" type="attribute" inpkey="114779368"/>  
                      <entry name="conflictResolutionPriority" type="attribute" inpkey="114779264"/>  
                      <entry name="artifactType" type="attribute" inpkey="114779160"/>  
                      <entry name="sourceSystemKind" type="attribute" inpkey="114779056"/>  
                      <entry name="sourceSystemId" type="attribute" inpkey="114778952"/>  
                      <entry name="sourceRepositoryKind" type="attribute" inpkey="114773960"/>  
                      <entry name="sourceRepositoryId" type="attribute" inpkey="114774064"/>  
                      <entry name="sourceArtifactId" type="attribute" inpkey="114774168"/>  
                      <entry name="targetSystemKind" type="attribute" inpkey="114774272"/>  
                      <entry name="targetSystemId" type="attribute" inpkey="114774376"/>  
                      <entry name="targetRepositoryKind" type="attribute" inpkey="114774480"/>  
                      <entry name="targetRepositoryId" type="attribute" inpkey="114774584"/>  
                      <entry name="targetArtifactId" type="attribute" inpkey="114774688"/>  
                      <entry name="depParentSourceRepositoryKind" type="attribute" inpkey="114774792"/>  
                      <entry name="depParentSourceRepositoryId" type="attribute" inpkey="114774896"/>  
                      <entry name="depParentSourceArtifactId" type="attribute" inpkey="114775000"/>  
                      <entry name="depChildSourceRepositoryKind" type="attribute" inpkey="114780200"/>  
                      <entry name="depChildSourceRepositoryId" type="attribute" inpkey="114780304"/>  
                      <entry name="depChildSourceArtifactId" type="attribute" inpkey="114780408"/>  
                      <entry name="depParentTargetRepositoryKind" type="attribute" inpkey="114780512"/>  
                      <entry name="depParentTargetRepositoryId" type="attribute" inpkey="114780616"/>  
                      <entry name="depParentTargetArtifactId" type="attribute" inpkey="114780720"/>  
                      <entry name="depChildTargetRepositoryKind" type="attribute" inpkey="114780824"/>  
                      <entry name="depChildTargetRepositoryId" type="attribute" inpkey="114780928"/>  
                      <entry name="depChildTargetArtifactId" type="attribute" inpkey="114775312"/>  
                      <entry name="errorCode" type="attribute" inpkey="114781864"/>  
                      <entry name="transactionId" type="attribute" inpkey="114781968"/>  
                      <entry name="includesFieldMetaData" type="attribute" inpkey="115414024"/>  
                      <entry name="sourceSystemTimezone" type="attribute" inpkey="115414128"/>  
                      <entry name="targetSystemTimezone" type="attribute" inpkey="115413920"/> 
                    </entry>  
                    <entry name="summary" inpkey="115413816"/>  
                    <entry name="description" inpkey="115415792"/>  
                    <entry name="status" inpkey="115414544"/>  
                    <entry name="comment" inpkey="115414440"/> 
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
              <view ltx="-18" lty="-9" rbx="367" rby="918"/>  
              <data> 
                <root scrollposition="1"> 
                  <header> 
                    <namespaces> 
                      <namespace/> 
                    </namespaces> 
                  </header>  
                  <entry name="artifact" outkey="115391816" expanded="1"> 
                    <entry name="topLevelAttributes" outkey="115391920"> 
                      <entry name="artifactMode" type="attribute" outkey="115392856"/>  
                      <entry name="artifactAction" type="attribute" outkey="115392960"/>  
                      <entry name="sourceArtifactVersion" type="attribute" outkey="115393064"/>  
                      <entry name="targetArtifactVersion" type="attribute" outkey="115393168"/>  
                      <entry name="sourceArtifactLastModifiedDate" type="attribute" outkey="115393272"/>  
                      <entry name="targetArtifactLastModifiedDate" type="attribute" outkey="115393376"/>  
                      <entry name="conflictResolutionPriority" type="attribute" outkey="115393480"/>  
                      <entry name="artifactType" type="attribute" outkey="115393584"/>  
                      <entry name="sourceSystemKind" type="attribute" outkey="115393688"/>  
                      <entry name="sourceSystemId" type="attribute" outkey="115393792"/>  
                      <entry name="sourceRepositoryKind" type="attribute" outkey="115393896"/>  
                      <entry name="sourceRepositoryId" type="attribute" outkey="115394000"/>  
                      <entry name="sourceArtifactId" type="attribute" outkey="115394104"/>  
                      <entry name="targetSystemKind" type="attribute" outkey="115394208"/>  
                      <entry name="targetSystemId" type="attribute" outkey="115394312"/>  
                      <entry name="targetRepositoryKind" type="attribute" outkey="115394416"/>  
                      <entry name="targetRepositoryId" type="attribute" outkey="115394520"/>  
                      <entry name="targetArtifactId" type="attribute" outkey="115394624"/>  
                      <entry name="depParentSourceRepositoryKind" type="attribute" outkey="115394728"/>  
                      <entry name="depParentSourceRepositoryId" type="attribute" outkey="115394832"/>  
                      <entry name="depParentSourceArtifactId" type="attribute" outkey="115394936"/>  
                      <entry name="depChildSourceRepositoryKind" type="attribute" outkey="115395040"/>  
                      <entry name="depChildSourceRepositoryId" type="attribute" outkey="115395144"/>  
                      <entry name="depChildSourceArtifactId" type="attribute" outkey="115395248"/>  
                      <entry name="depParentTargetRepositoryKind" type="attribute" outkey="115395352"/>  
                      <entry name="depParentTargetRepositoryId" type="attribute" outkey="115395456"/>  
                      <entry name="depParentTargetArtifactId" type="attribute" outkey="115395560"/>  
                      <entry name="depChildTargetRepositoryKind" type="attribute" outkey="115395664"/>  
                      <entry name="depChildTargetRepositoryId" type="attribute" outkey="115395768"/>  
                      <entry name="depChildTargetArtifactId" type="attribute" outkey="115395872"/>  
                      <entry name="errorCode" type="attribute" outkey="115395976"/>  
                      <entry name="transactionId" type="attribute" outkey="115396080"/>  
                      <entry name="includesFieldMetaData" type="attribute" outkey="115396184"/>  
                      <entry name="sourceSystemTimezone" type="attribute" outkey="115396288"/>  
                      <entry name="targetSystemTimezone" type="attribute" outkey="115396392"/> 
                    </entry>  
                    <entry name="description" outkey="115392336"/>  
                    <entry name="title" outkey="115392648"/>  
                    <entry name="CommentText" outkey="115392752"/> 
                  </entry> 
                </root>  
                <document schema="{@sourceSchemaName}">
                  <xsl:attribute name="instanceroot">{}artifact</xsl:attribute>
                </document>  
                <wsdl/> 
              </data> 
            </component>  
            <component name="constant" library="core" uid="13" kind="2"> 
              <properties/>  
              <targets> 
                <datapoint pos="0" key="115550128"/> 
              </targets>  
              <view ltx="607" lty="350" rbx="28" rby="-69"/>  
              <data> 
                <constant value="Open" datatype="string"/>  
                <wsdl/> 
              </data> 
            </component> 
          </children>  
          <graph directed="1"> 
            <edges/>  
            <vertices> 
              <vertex vertexkey="115391816"> 
                <edges> 
                  <edge vertexkey="114780096" edgekey="103582712"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115391920"> 
                <edges> 
                  <edge vertexkey="114779992" edgekey="103583496"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115392336"> 
                <edges> 
                  <edge vertexkey="115415792" edgekey="103729816"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115392648"> 
                <edges> 
                  <edge vertexkey="115413816" edgekey="115000464"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115392752"> 
                <edges> 
                  <edge vertexkey="115414440" edgekey="115005448"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115392856"> 
                <edges> 
                  <edge vertexkey="114779888" edgekey="103583272"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115392960"> 
                <edges> 
                  <edge vertexkey="114779784" edgekey="103582992"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115393064"> 
                <edges> 
                  <edge vertexkey="114779680" edgekey="103583104"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115393168"> 
                <edges> 
                  <edge vertexkey="114779576" edgekey="103583216"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115393272"> 
                <edges> 
                  <edge vertexkey="114779472" edgekey="103582936"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115393376"> 
                <edges> 
                  <edge vertexkey="114779368" edgekey="103583160"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115393480"> 
                <edges> 
                  <edge vertexkey="114779264" edgekey="103582768"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115393584"> 
                <edges> 
                  <edge vertexkey="114779160" edgekey="103582880"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115393688"> 
                <edges> 
                  <edge vertexkey="114779056" edgekey="103582824"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115393792"> 
                <edges> 
                  <edge vertexkey="114778952" edgekey="103582656"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115393896"> 
                <edges> 
                  <edge vertexkey="114773960" edgekey="103582600"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115394000"> 
                <edges> 
                  <edge vertexkey="114774064" edgekey="103582376"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115394104"> 
                <edges> 
                  <edge vertexkey="114774168" edgekey="103582488"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115394208"> 
                <edges> 
                  <edge vertexkey="114774272" edgekey="103581480"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115394312"> 
                <edges> 
                  <edge vertexkey="114774376" edgekey="103582544"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115394416"> 
                <edges> 
                  <edge vertexkey="114774480" edgekey="103581760"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115394520"> 
                <edges> 
                  <edge vertexkey="114774584" edgekey="103582432"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115394624"> 
                <edges> 
                  <edge vertexkey="114774688" edgekey="103582320"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115394728"> 
                <edges> 
                  <edge vertexkey="114774792" edgekey="103582264"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115394832"> 
                <edges> 
                  <edge vertexkey="114774896" edgekey="103582152"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115394936"> 
                <edges> 
                  <edge vertexkey="114775000" edgekey="103566640"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115395040"> 
                <edges> 
                  <edge vertexkey="114780200" edgekey="103582208"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115395144"> 
                <edges> 
                  <edge vertexkey="114780304" edgekey="103582096"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115395248"> 
                <edges> 
                  <edge vertexkey="114780408" edgekey="103581984"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115395352"> 
                <edges> 
                  <edge vertexkey="114780512" edgekey="103582040"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115395456"> 
                <edges> 
                  <edge vertexkey="114780616" edgekey="103581928"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115395560"> 
                <edges> 
                  <edge vertexkey="114780720" edgekey="103581816"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115395664"> 
                <edges> 
                  <edge vertexkey="114780824" edgekey="103581872"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115395768"> 
                <edges> 
                  <edge vertexkey="114780928" edgekey="103565968"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115395872"> 
                <edges> 
                  <edge vertexkey="114775312" edgekey="103566024"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115395976"> 
                <edges> 
                  <edge vertexkey="114781864" edgekey="103581088"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115396080"> 
                <edges> 
                  <edge vertexkey="114781968" edgekey="103566360"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115396184"> 
                <edges> 
                  <edge vertexkey="115414024" edgekey="103581704"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115396288"> 
                <edges> 
                  <edge vertexkey="115414128" edgekey="103581648"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115396392"> 
                <edges> 
                  <edge vertexkey="115413920" edgekey="103563728"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="115550128"> 
                <edges> 
                  <edge vertexkey="115414544" edgekey="115027064"/> 
                </edges> 
              </vertex> 
            </vertices> 
          </graph> 
        </structure> 
      </component> 
    </mapping>
  </xsl:template>
</xsl:stylesheet>
