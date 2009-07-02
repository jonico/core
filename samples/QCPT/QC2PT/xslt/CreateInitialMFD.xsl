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
              <view ltx="557" rbx="936" rby="1263"/>  
              <data> 
                <root scrollposition="1"> 
                  <header> 
                    <namespaces> 
                      <namespace/> 
                    </namespaces> 
                  </header>  
                  <entry name="artifact" inpkey="76455104" expanded="1"> 
                    <entry name="topLevelAttributes" inpkey="73619688"> 
                      <entry name="artifactMode" type="attribute" inpkey="73620344"/>  
                      <entry name="artifactAction" type="attribute" inpkey="76455208"/>  
                      <entry name="sourceArtifactVersion" type="attribute" inpkey="84163456"/>  
                      <entry name="targetArtifactVersion" type="attribute" inpkey="84163560"/>  
                      <entry name="sourceArtifactLastModifiedDate" type="attribute" inpkey="76455312"/>  
                      <entry name="targetArtifactLastModifiedDate" type="attribute" inpkey="84163144"/>  
                      <entry name="conflictResolutionPriority" type="attribute" inpkey="84163248"/>  
                      <entry name="artifactType" type="attribute" inpkey="84163352"/>  
                      <entry name="sourceSystemKind" type="attribute" inpkey="84163664"/>  
                      <entry name="sourceSystemId" type="attribute" inpkey="84163768"/>  
                      <entry name="sourceRepositoryKind" type="attribute" inpkey="84163872"/>  
                      <entry name="sourceRepositoryId" type="attribute" inpkey="84163976"/>  
                      <entry name="sourceArtifactId" type="attribute" inpkey="84164080"/>  
                      <entry name="targetSystemKind" type="attribute" inpkey="84164184"/>  
                      <entry name="targetSystemId" type="attribute" inpkey="84164288"/>  
                      <entry name="targetRepositoryKind" type="attribute" inpkey="84164392"/>  
                      <entry name="targetRepositoryId" type="attribute" inpkey="84164496"/>  
                      <entry name="targetArtifactId" type="attribute" inpkey="76442712"/>  
                      <entry name="depParentSourceRepositoryKind" type="attribute" inpkey="76442816"/>  
                      <entry name="depParentSourceRepositoryId" type="attribute" inpkey="76443008"/>  
                      <entry name="depParentSourceArtifactId" type="attribute" inpkey="76443200"/>  
                      <entry name="depChildSourceRepositoryKind" type="attribute" inpkey="76443392"/>  
                      <entry name="depChildSourceRepositoryId" type="attribute" inpkey="76443584"/>  
                      <entry name="depChildSourceArtifactId" type="attribute" inpkey="76443776"/>  
                      <entry name="depParentTargetRepositoryKind" type="attribute" inpkey="76443968"/>  
                      <entry name="depParentTargetRepositoryId" type="attribute" inpkey="76444160"/>  
                      <entry name="depParentTargetArtifactId" type="attribute" inpkey="76444352"/>  
                      <entry name="depChildTargetRepositoryKind" type="attribute" inpkey="76444544"/>  
                      <entry name="depChildTargetRepositoryId" type="attribute" inpkey="76444736"/>  
                      <entry name="depChildTargetArtifactId" type="attribute" inpkey="76444928"/>  
                      <entry name="errorCode" type="attribute" inpkey="76445120"/>  
                      <entry name="transactionId" type="attribute" inpkey="76445224"/>  
                      <entry name="includesFieldMetaData" type="attribute" inpkey="76445328"/>  
                      <entry name="sourceSystemTimezone" type="attribute" inpkey="76445520"/>  
                      <entry name="targetSystemTimezone" type="attribute" inpkey="84164600"/> 
                    </entry>  
                    <entry name="comment" inpkey="84167960"/> 
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
                  <entry name="artifact" outkey="84182384" expanded="1"> 
                    <entry name="topLevelAttributes" outkey="84182608"> 
                      <entry name="artifactMode" type="attribute" outkey="84182160"/>  
                      <entry name="artifactAction" type="attribute" outkey="84182864"/>  
                      <entry name="sourceArtifactVersion" type="attribute" outkey="84183104"/>  
                      <entry name="targetArtifactVersion" type="attribute" outkey="84183376"/>  
                      <entry name="sourceArtifactLastModifiedDate" type="attribute" outkey="84183648"/>  
                      <entry name="targetArtifactLastModifiedDate" type="attribute" outkey="84184008"/>  
                      <entry name="conflictResolutionPriority" type="attribute" outkey="84184280"/>  
                      <entry name="artifactType" type="attribute" outkey="84184464"/>  
                      <entry name="sourceSystemKind" type="attribute" outkey="84184688"/>  
                      <entry name="sourceSystemId" type="attribute" outkey="84184944"/>  
                      <entry name="sourceRepositoryKind" type="attribute" outkey="84185192"/>  
                      <entry name="sourceRepositoryId" type="attribute" outkey="84185432"/>  
                      <entry name="sourceArtifactId" type="attribute" outkey="84185672"/>  
                      <entry name="targetSystemKind" type="attribute" outkey="84185912"/>  
                      <entry name="targetSystemId" type="attribute" outkey="84186152"/>  
                      <entry name="targetRepositoryKind" type="attribute" outkey="84186392"/>  
                      <entry name="targetRepositoryId" type="attribute" outkey="84186632"/>  
                      <entry name="targetArtifactId" type="attribute" outkey="84186872"/>  
                      <entry name="depParentSourceRepositoryKind" type="attribute" outkey="84187112"/>  
                      <entry name="depParentSourceRepositoryId" type="attribute" outkey="84187384"/>  
                      <entry name="depParentSourceArtifactId" type="attribute" outkey="84187656"/>  
                      <entry name="depChildSourceRepositoryKind" type="attribute" outkey="84187928"/>  
                      <entry name="depChildSourceRepositoryId" type="attribute" outkey="84188200"/>  
                      <entry name="depChildSourceArtifactId" type="attribute" outkey="84188472"/>  
                      <entry name="depParentTargetRepositoryKind" type="attribute" outkey="84188744"/>  
                      <entry name="depParentTargetRepositoryId" type="attribute" outkey="84189016"/>  
                      <entry name="depParentTargetArtifactId" type="attribute" outkey="84189288"/>  
                      <entry name="depChildTargetRepositoryKind" type="attribute" outkey="84189560"/>  
                      <entry name="depChildTargetRepositoryId" type="attribute" outkey="84189832"/>  
                      <entry name="depChildTargetArtifactId" type="attribute" outkey="84190104"/>  
                      <entry name="errorCode" type="attribute" outkey="84190376"/>  
                      <entry name="transactionId" type="attribute" outkey="84190640"/>  
                      <entry name="includesFieldMetaData" type="attribute" outkey="84190824"/>  
                      <entry name="sourceSystemTimezone" type="attribute" outkey="84191112"/>  
                      <entry name="targetSystemTimezone" type="attribute" outkey="84191352"/> 
                    </entry>  
                    <entry name="BG_DEV_COMMENTS" outkey="84212704"/> 
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
              <vertex vertexkey="84182160"> 
                <edges> 
                  <edge vertexkey="73620344" edgekey="84218384"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84182384"> 
                <edges> 
                  <edge vertexkey="76455104" edgekey="84216648"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84182608"> 
                <edges> 
                  <edge vertexkey="73619688" edgekey="84218248"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84182864"> 
                <edges> 
                  <edge vertexkey="76455208" edgekey="84218520"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84183104"> 
                <edges> 
                  <edge vertexkey="84163456" edgekey="84218656"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84183376"> 
                <edges> 
                  <edge vertexkey="84163560" edgekey="84218792"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84183648"> 
                <edges> 
                  <edge vertexkey="76455312" edgekey="84218928"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84184008"> 
                <edges> 
                  <edge vertexkey="84163144" edgekey="84219064"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84184280"> 
                <edges> 
                  <edge vertexkey="84163248" edgekey="84219200"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84184464"> 
                <edges> 
                  <edge vertexkey="84163352" edgekey="84219336"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84184688"> 
                <edges> 
                  <edge vertexkey="84163664" edgekey="84219472"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84184944"> 
                <edges> 
                  <edge vertexkey="84163768" edgekey="84219608"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84185192"> 
                <edges> 
                  <edge vertexkey="84163872" edgekey="84219744"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84185432"> 
                <edges> 
                  <edge vertexkey="84163976" edgekey="84219880"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84185672"> 
                <edges> 
                  <edge vertexkey="84164080" edgekey="84220016"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84185912"> 
                <edges> 
                  <edge vertexkey="84164184" edgekey="84220152"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84186152"> 
                <edges> 
                  <edge vertexkey="84164288" edgekey="84220288"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84186392"> 
                <edges> 
                  <edge vertexkey="84164392" edgekey="84220472"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84186632"> 
                <edges> 
                  <edge vertexkey="84164496" edgekey="84220656"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84186872"> 
                <edges> 
                  <edge vertexkey="76442712" edgekey="84220840"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84187112"> 
                <edges> 
                  <edge vertexkey="76442816" edgekey="84221024"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84187384"> 
                <edges> 
                  <edge vertexkey="76443008" edgekey="84221208"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84187656"> 
                <edges> 
                  <edge vertexkey="76443200" edgekey="84221392"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84187928"> 
                <edges> 
                  <edge vertexkey="76443392" edgekey="84221576"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84188200"> 
                <edges> 
                  <edge vertexkey="76443584" edgekey="84221760"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84188472"> 
                <edges> 
                  <edge vertexkey="76443776" edgekey="84221944"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84188744"> 
                <edges> 
                  <edge vertexkey="76443968" edgekey="84222128"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84189016"> 
                <edges> 
                  <edge vertexkey="76444160" edgekey="84222312"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84189288"> 
                <edges> 
                  <edge vertexkey="76444352" edgekey="84222496"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84189560"> 
                <edges> 
                  <edge vertexkey="76444544" edgekey="84222680"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84189832"> 
                <edges> 
                  <edge vertexkey="76444736" edgekey="84222864"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84190104"> 
                <edges> 
                  <edge vertexkey="76444928" edgekey="84223048"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84190376"> 
                <edges> 
                  <edge vertexkey="76445120" edgekey="84223232"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84190640"> 
                <edges> 
                  <edge vertexkey="76445224" edgekey="84223416"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84190824"> 
                <edges> 
                  <edge vertexkey="76445328" edgekey="84223600"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84191112"> 
                <edges> 
                  <edge vertexkey="76445520" edgekey="84223784"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84191352"> 
                <edges> 
                  <edge vertexkey="84164600" edgekey="84223968"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84212704"> 
                <edges> 
                  <edge vertexkey="84167960" edgekey="73860272"/> 
                </edges> 
              </vertex> 
            </vertices> 
          </graph> 
        </structure> 
      </component> 
    </mapping>
  </xsl:template>
</xsl:stylesheet>
