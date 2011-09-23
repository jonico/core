<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:fn="http://www.w3.org/2005/xpath-functions" version="2.0" exclude-result-prefixes="xsl xs fn">
  <xsl:template match="node()">
    <mapping xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="11">  
      <component name="defaultmap1" blackbox="0" editable="1"> 
        <properties SelectedLanguage="xslt"/>  
        <structure> 
          <children> 
            <component name="equal" library="core" uid="13" kind="5"> 
              <properties/>  
              <sources> 
                <datapoint pos="0" key="73862096"/>  
                <datapoint pos="1" key="73815440"/> 
              </sources>  
              <targets> 
                <datapoint pos="0" key="73817032"/> 
              </targets>  
              <view ltx="464" lty="36" rbx="-54" rby="-43"/> 
            </component>  
            <component name="equal" library="core" uid="14" kind="5"> 
              <properties/>  
              <sources> 
                <datapoint pos="0" key="73817688"/>  
                <datapoint pos="1" key="73818456"/> 
              </sources>  
              <targets> 
                <datapoint pos="0" key="73824624"/> 
              </targets>  
              <view ltx="465" lty="108"/> 
            </component>  
            <component name="logical-or" library="core" uid="17" kind="5" growable="1" growablebasename="value"> 
              <properties/>  
              <sources> 
                <datapoint pos="0" key="73814944"/>  
                <datapoint pos="1" key="73814640"/> 
              </sources>  
              <targets> 
                <datapoint pos="0" key="76670656"/> 
              </targets>  
              <view ltx="536" lty="76" rbx="-26"/> 
            </component>  
            <component name="constant" library="core" uid="19" kind="2"> 
              <properties/>  
              <targets> 
                <datapoint pos="0" key="76675064"/> 
              </targets>  
              <view ltx="526" lty="171" rbx="-126" rby="-186"/>  
              <data> 
                <constant value="ignore" datatype="string"/>  
                <wsdl/> 
              </data> 
            </component>  
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
                  <entry name="artifact" inpkey="84463728" expanded="1"> 
                    <entry name="topLevelAttributes" inpkey="84463992"> 
                      <entry name="artifactMode" type="attribute" inpkey="84464656"/>  
                      <entry name="artifactAction" type="attribute" inpkey="84464896"/>  
                      <entry name="sourceArtifactVersion" type="attribute" inpkey="84465152"/>  
                      <entry name="targetArtifactVersion" type="attribute" inpkey="84465336"/>  
                      <entry name="sourceArtifactLastModifiedDate" type="attribute" inpkey="84465608"/>  
                      <entry name="targetArtifactLastModifiedDate" type="attribute" inpkey="84465968"/>  
                      <entry name="conflictResolutionPriority" type="attribute" inpkey="84466240"/>  
                      <entry name="artifactType" type="attribute" inpkey="84466424"/>  
                      <entry name="sourceSystemKind" type="attribute" inpkey="84466648"/>  
                      <entry name="sourceSystemId" type="attribute" inpkey="84466904"/>  
                      <entry name="sourceRepositoryKind" type="attribute" inpkey="84467144"/>  
                      <entry name="sourceRepositoryId" type="attribute" inpkey="84467384"/>  
                      <entry name="sourceArtifactId" type="attribute" inpkey="84467624"/>  
                      <entry name="targetSystemKind" type="attribute" inpkey="84467864"/>  
                      <entry name="targetSystemId" type="attribute" inpkey="84468104"/>  
                      <entry name="targetRepositoryKind" type="attribute" inpkey="84468344"/>  
                      <entry name="targetRepositoryId" type="attribute" inpkey="84468584"/>  
                      <entry name="targetArtifactId" type="attribute" inpkey="84468824"/>  
                      <entry name="depParentSourceRepositoryKind" type="attribute" inpkey="84469064"/>  
                      <entry name="depParentSourceRepositoryId" type="attribute" inpkey="84469336"/>  
                      <entry name="depParentSourceArtifactId" type="attribute" inpkey="84469608"/>  
                      <entry name="depChildSourceRepositoryKind" type="attribute" inpkey="84469880"/>  
                      <entry name="depChildSourceRepositoryId" type="attribute" inpkey="84470152"/>  
                      <entry name="depChildSourceArtifactId" type="attribute" inpkey="84470424"/>  
                      <entry name="depParentTargetRepositoryKind" type="attribute" inpkey="84470696"/>  
                      <entry name="depParentTargetRepositoryId" type="attribute" inpkey="84470968"/>  
                      <entry name="depParentTargetArtifactId" type="attribute" inpkey="84471240"/>  
                      <entry name="depChildTargetRepositoryKind" type="attribute" inpkey="84471512"/>  
                      <entry name="depChildTargetRepositoryId" type="attribute" inpkey="84471784"/>  
                      <entry name="depChildTargetArtifactId" type="attribute" inpkey="84472056"/>  
                      <entry name="errorCode" type="attribute" inpkey="84472328"/>  
                      <entry name="transactionId" type="attribute" inpkey="84472592"/>  
                      <entry name="includesFieldMetaData" type="attribute" inpkey="84472776"/>  
                      <entry name="sourceSystemTimezone" type="attribute" inpkey="84473064"/>  
                      <entry name="targetSystemTimezone" type="attribute" inpkey="84473304"/> 
                    </entry>  
                    <entry name="RQ_REQ_COMMENT" inpkey="84464192"/>  
                    <entry name="RQ_REQ_NAME" inpkey="84464432"/> 
                  </entry> 
                </root>  
                <document schema="{@targetSchemaName}">
                  <xsl:attribute name="instanceroot">{}artifact</xsl:attribute>
                </document>  
                <wsdl/> 
              </data> 
            </component>  
            <component name="constant" library="core" uid="16" kind="2"> 
              <properties/>  
              <targets> 
                <datapoint pos="0" key="73866304"/> 
              </targets>  
              <view ltx="364" lty="146" rbx="-288" rby="-160"/>  
              <data> 
                <constant value="resync" datatype="string"/>  
                <wsdl/> 
              </data> 
            </component>  
            <component name="constant" library="core" uid="15" kind="2"> 
              <properties/>  
              <targets> 
                <datapoint pos="0" key="73866976"/> 
              </targets>  
              <view ltx="364" lty="76" rbx="-288" rby="-230"/>  
              <data> 
                <constant value="update" datatype="string"/>  
                <wsdl/> 
              </data> 
            </component>  
            <component name="if-else" library="core" uid="18" kind="4"> 
              <properties/>  
              <sources> 
                <datapoint pos="0" key="76671768"/>  
                <datapoint pos="1" key="76671952"/>  
                <datapoint pos="2" key="76672136"/> 
              </sources>  
              <targets> 
                <datapoint pos="0" key="76672320"/> 
              </targets>  
              <view ltx="621" lty="73" rbx="-31" rby="-284"/> 
            </component>  
            <component name="document" library="xml" uid="1" kind="14"> 
              <properties XSLTTargetEncoding="UTF-8"/>  
              <view ltx="-49" lty="-12" rbx="336" rby="915"/>  
              <data> 
                <root scrollposition="1"> 
                  <header> 
                    <namespaces> 
                      <namespace/> 
                    </namespaces> 
                  </header>  
                  <entry name="artifact" outkey="84409944" expanded="1"> 
                    <entry name="topLevelAttributes" outkey="84446248"> 
                      <entry name="artifactMode" type="attribute" outkey="84445224"/>  
                      <entry name="artifactAction" type="attribute" outkey="84445328"/>  
                      <entry name="sourceArtifactVersion" type="attribute" outkey="84445952"/>  
                      <entry name="targetArtifactVersion" type="attribute" outkey="84446056"/>  
                      <entry name="sourceArtifactLastModifiedDate" type="attribute" outkey="84445432"/>  
                      <entry name="targetArtifactLastModifiedDate" type="attribute" outkey="84445536"/>  
                      <entry name="conflictResolutionPriority" type="attribute" outkey="84445640"/>  
                      <entry name="artifactType" type="attribute" outkey="84445744"/>  
                      <entry name="sourceSystemKind" type="attribute" outkey="84445848"/>  
                      <entry name="sourceSystemId" type="attribute" outkey="84446560"/>  
                      <entry name="sourceRepositoryKind" type="attribute" outkey="84446664"/>  
                      <entry name="sourceRepositoryId" type="attribute" outkey="84446768"/>  
                      <entry name="sourceArtifactId" type="attribute" outkey="84446872"/>  
                      <entry name="targetSystemKind" type="attribute" outkey="84446976"/>  
                      <entry name="targetSystemId" type="attribute" outkey="84447080"/>  
                      <entry name="targetRepositoryKind" type="attribute" outkey="81965064"/>  
                      <entry name="targetRepositoryId" type="attribute" outkey="81965168"/>  
                      <entry name="targetArtifactId" type="attribute" outkey="81965272"/>  
                      <entry name="depParentSourceRepositoryKind" type="attribute" outkey="81966336"/>  
                      <entry name="depParentSourceRepositoryId" type="attribute" outkey="81965376"/>  
                      <entry name="depParentSourceArtifactId" type="attribute" outkey="81965568"/>  
                      <entry name="depChildSourceRepositoryKind" type="attribute" outkey="81965760"/>  
                      <entry name="depChildSourceRepositoryId" type="attribute" outkey="81965952"/>  
                      <entry name="depChildSourceArtifactId" type="attribute" outkey="81966144"/>  
                      <entry name="depParentTargetRepositoryKind" type="attribute" outkey="81966528"/>  
                      <entry name="depParentTargetRepositoryId" type="attribute" outkey="81966720"/>  
                      <entry name="depParentTargetArtifactId" type="attribute" outkey="81966912"/>  
                      <entry name="depChildTargetRepositoryKind" type="attribute" outkey="81967104"/>  
                      <entry name="depChildTargetRepositoryId" type="attribute" outkey="81967296"/>  
                      <entry name="depChildTargetArtifactId" type="attribute" outkey="81967488"/>  
                      <entry name="errorCode" type="attribute" outkey="81967680"/>  
                      <entry name="transactionId" type="attribute" outkey="81967784"/>  
                      <entry name="includesFieldMetaData" type="attribute" outkey="81967888"/>  
                      <entry name="sourceSystemTimezone" type="attribute" outkey="84447272"/>  
                      <entry name="targetSystemTimezone" type="attribute" outkey="84447376"/> 
                    </entry>  
                    <entry name="title" outkey="84446352"/>  
                    <entry name="description" outkey="84446456"/> 
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
              <vertex vertexkey="73817032"> 
                <edges> 
                  <edge vertexkey="73814944" edgekey="84505872"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="73824624"> 
                <edges> 
                  <edge vertexkey="73814640" edgekey="84507704"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="73866304"> 
                <edges> 
                  <edge vertexkey="73818456" edgekey="84507840"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="73866976"> 
                <edges> 
                  <edge vertexkey="73815440" edgekey="84507976"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="76670656"> 
                <edges> 
                  <edge vertexkey="76671768" edgekey="84508112"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="76672320"> 
                <edges> 
                  <edge vertexkey="84464896" edgekey="84508384"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="76675064"> 
                <edges> 
                  <edge vertexkey="76672136" edgekey="84508248"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="81965064"> 
                <edges> 
                  <edge vertexkey="84468344" edgekey="84511376"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="81965168"> 
                <edges> 
                  <edge vertexkey="84468584" edgekey="84511512"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="81965272"> 
                <edges> 
                  <edge vertexkey="84468824" edgekey="84511648"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="81965376"> 
                <edges> 
                  <edge vertexkey="84469336" edgekey="84511920"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="81965568"> 
                <edges> 
                  <edge vertexkey="84469608" edgekey="84512056"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="81965760"> 
                <edges> 
                  <edge vertexkey="84469880" edgekey="84512192"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="81965952"> 
                <edges> 
                  <edge vertexkey="84470152" edgekey="84512328"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="81966144"> 
                <edges> 
                  <edge vertexkey="84470424" edgekey="84512464"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="81966336"> 
                <edges> 
                  <edge vertexkey="84469064" edgekey="84511784"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="81966528"> 
                <edges> 
                  <edge vertexkey="84470696" edgekey="84512600"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="81966720"> 
                <edges> 
                  <edge vertexkey="84470968" edgekey="84512736"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="81966912"> 
                <edges> 
                  <edge vertexkey="84471240" edgekey="84512872"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="81967104"> 
                <edges> 
                  <edge vertexkey="84471512" edgekey="84513008"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="81967296"> 
                <edges> 
                  <edge vertexkey="84471784" edgekey="84513144"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="81967488"> 
                <edges> 
                  <edge vertexkey="84472056" edgekey="84513280"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="81967680"> 
                <edges> 
                  <edge vertexkey="84472328" edgekey="84513416"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="81967784"> 
                <edges> 
                  <edge vertexkey="84472592" edgekey="84513552"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="81967888"> 
                <edges> 
                  <edge vertexkey="84472776" edgekey="84513688"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84409944"> 
                <edges> 
                  <edge vertexkey="84463728" edgekey="84508520"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84445224"> 
                <edges> 
                  <edge vertexkey="84464656" edgekey="84509064"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84445328"> 
                <edges> 
                  <edge vertexkey="73862096" edgekey="84509200"/>  
                  <edge vertexkey="73817688" edgekey="84509336"/>  
                  <edge vertexkey="76671952" edgekey="84509472"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84445432"> 
                <edges> 
                  <edge vertexkey="84465608" edgekey="84509880"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84445536"> 
                <edges> 
                  <edge vertexkey="84465968" edgekey="84510016"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84445640"> 
                <edges> 
                  <edge vertexkey="84466240" edgekey="84510152"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84445744"> 
                <edges> 
                  <edge vertexkey="84466424" edgekey="84510288"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84445848"> 
                <edges> 
                  <edge vertexkey="84466648" edgekey="84510424"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84445952"> 
                <edges> 
                  <edge vertexkey="84465152" edgekey="84509608"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84446056"> 
                <edges> 
                  <edge vertexkey="84465336" edgekey="84509744"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84446248"> 
                <edges> 
                  <edge vertexkey="84463992" edgekey="84508656"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84446352"> 
                <edges> 
                  <edge vertexkey="84464432" edgekey="84508792"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84446456"> 
                <edges> 
                  <edge vertexkey="84464192" edgekey="84508928"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84446560"> 
                <edges> 
                  <edge vertexkey="84466904" edgekey="84510560"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84446664"> 
                <edges> 
                  <edge vertexkey="84467144" edgekey="84510696"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84446768"> 
                <edges> 
                  <edge vertexkey="84467384" edgekey="84510832"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84446872"> 
                <edges> 
                  <edge vertexkey="84467624" edgekey="84510968"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84446976"> 
                <edges> 
                  <edge vertexkey="84467864" edgekey="84511104"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84447080"> 
                <edges> 
                  <edge vertexkey="84468104" edgekey="84511240"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84447272"> 
                <edges> 
                  <edge vertexkey="84473064" edgekey="84513824"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84447376"> 
                <edges> 
                  <edge vertexkey="84473304" edgekey="84513984"/> 
                </edges> 
              </vertex> 
            </vertices> 
          </graph> 
        </structure> 
      </component> 
    </mapping>
  </xsl:template>
</xsl:stylesheet>
