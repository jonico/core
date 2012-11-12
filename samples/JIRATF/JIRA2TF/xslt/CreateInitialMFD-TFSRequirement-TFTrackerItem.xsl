<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:fn="http://www.w3.org/2005/xpath-functions" version="2.0" exclude-result-prefixes="xsl xs fn">
  <xsl:template match="node()">
    <mapping xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="12">  
      <component name="defaultmap1" blackbox="0" editable="1"> 
        <properties SelectedLanguage="xslt"/>  
        <structure> 
          <children> 
            <component name="constant" library="core" uid="12" kind="2"> 
              <properties/>  
              <targets> 
                <datapoint pos="0" key="105830416"/> 
              </targets>  
              <view ltx="597" lty="266"/>  
              <data> 
                <constant value="Open" datatype="string"/>  
                <wsdl/> 
              </data> 
            </component>  
            <component name="document" library="xml" uid="2" kind="14"> 
              <properties XSLTTargetEncoding="UTF-8" XSLTDefaultOutput="1"/>  
              <view ltx="689" rbx="1068" rby="1263"/>  
              <data> 
                <root scrollposition="1"> 
                  <header> 
                    <namespaces> 
                      <namespace/> 
                    </namespaces> 
                  </header>  
                  <entry name="artifact" inpkey="117116656" expanded="1"> 
                    <entry name="topLevelAttributes" inpkey="117116760"> 
                      <entry name="artifactMode" type="attribute" inpkey="117117280"/>  
                      <entry name="artifactAction" type="attribute" inpkey="117117384"/>  
                      <entry name="sourceArtifactVersion" type="attribute" inpkey="117117488"/>  
                      <entry name="targetArtifactVersion" type="attribute" inpkey="117117592"/>  
                      <entry name="sourceArtifactLastModifiedDate" type="attribute" inpkey="117117696"/>  
                      <entry name="targetArtifactLastModifiedDate" type="attribute" inpkey="117117800"/>  
                      <entry name="conflictResolutionPriority" type="attribute" inpkey="117117904"/>  
                      <entry name="artifactType" type="attribute" inpkey="117118008"/>  
                      <entry name="sourceSystemKind" type="attribute" inpkey="117118112"/>  
                      <entry name="sourceSystemId" type="attribute" inpkey="117118216"/>  
                      <entry name="sourceRepositoryKind" type="attribute" inpkey="117118320"/>  
                      <entry name="sourceRepositoryId" type="attribute" inpkey="117118424"/>  
                      <entry name="sourceArtifactId" type="attribute" inpkey="117118528"/>  
                      <entry name="targetSystemKind" type="attribute" inpkey="117118632"/>  
                      <entry name="targetSystemId" type="attribute" inpkey="117118736"/>  
                      <entry name="targetRepositoryKind" type="attribute" inpkey="117118840"/>  
                      <entry name="targetRepositoryId" type="attribute" inpkey="117118944"/>  
                      <entry name="targetArtifactId" type="attribute" inpkey="117119048"/>  
                      <entry name="depParentSourceRepositoryKind" type="attribute" inpkey="117119152"/>  
                      <entry name="depParentSourceRepositoryId" type="attribute" inpkey="117119256"/>  
                      <entry name="depParentSourceArtifactId" type="attribute" inpkey="117119400"/>  
                      <entry name="depChildSourceRepositoryKind" type="attribute" inpkey="117119504"/>  
                      <entry name="depChildSourceRepositoryId" type="attribute" inpkey="117119608"/>  
                      <entry name="depChildSourceArtifactId" type="attribute" inpkey="117119712"/>  
                      <entry name="depParentTargetRepositoryKind" type="attribute" inpkey="117119816"/>  
                      <entry name="depParentTargetRepositoryId" type="attribute" inpkey="117119920"/>  
                      <entry name="depParentTargetArtifactId" type="attribute" inpkey="117120024"/>  
                      <entry name="depChildTargetRepositoryKind" type="attribute" inpkey="117120128"/>  
                      <entry name="depChildTargetRepositoryId" type="attribute" inpkey="117120232"/>  
                      <entry name="depChildTargetArtifactId" type="attribute" inpkey="117120336"/>  
                      <entry name="errorCode" type="attribute" inpkey="117120440"/>  
                      <entry name="transactionId" type="attribute" inpkey="117120544"/>  
                      <entry name="includesFieldMetaData" type="attribute" inpkey="117120648"/>  
                      <entry name="sourceSystemTimezone" type="attribute" inpkey="117120752"/>  
                      <entry name="targetSystemTimezone" type="attribute" inpkey="117120856"/> 
                    </entry>  
                    <entry name="description" inpkey="117116864"/>  
                    <entry name="status" inpkey="117116968"/>  
                    <entry name="title" inpkey="117117072"/>  
                    <entry name="CommentText" inpkey="117117176"/> 
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
                  <entry name="artifact" outkey="117038624" expanded="1"> 
                    <entry name="topLevelAttributes" outkey="117038936"> 
                      <entry name="artifactMode" type="attribute" outkey="117039248"/>  
                      <entry name="artifactAction" type="attribute" outkey="117039352"/>  
                      <entry name="sourceArtifactVersion" type="attribute" outkey="117039456"/>  
                      <entry name="targetArtifactVersion" type="attribute" outkey="117039560"/>  
                      <entry name="sourceArtifactLastModifiedDate" type="attribute" outkey="117039664"/>  
                      <entry name="targetArtifactLastModifiedDate" type="attribute" outkey="117039768"/>  
                      <entry name="conflictResolutionPriority" type="attribute" outkey="117039872"/>  
                      <entry name="artifactType" type="attribute" outkey="117039976"/>  
                      <entry name="sourceSystemKind" type="attribute" outkey="117040080"/>  
                      <entry name="sourceSystemId" type="attribute" outkey="117040184"/>  
                      <entry name="sourceRepositoryKind" type="attribute" outkey="117040288"/>  
                      <entry name="sourceRepositoryId" type="attribute" outkey="117040392"/>  
                      <entry name="sourceArtifactId" type="attribute" outkey="117040496"/>  
                      <entry name="targetSystemKind" type="attribute" outkey="117040600"/>  
                      <entry name="targetSystemId" type="attribute" outkey="117040704"/>  
                      <entry name="targetRepositoryKind" type="attribute" outkey="117040808"/>  
                      <entry name="targetRepositoryId" type="attribute" outkey="117040912"/>  
                      <entry name="targetArtifactId" type="attribute" outkey="117041016"/>  
                      <entry name="depParentSourceRepositoryKind" type="attribute" outkey="117041120"/>  
                      <entry name="depParentSourceRepositoryId" type="attribute" outkey="117107112"/>  
                      <entry name="depParentSourceArtifactId" type="attribute" outkey="117107216"/>  
                      <entry name="depChildSourceRepositoryKind" type="attribute" outkey="117107320"/>  
                      <entry name="depChildSourceRepositoryId" type="attribute" outkey="117107424"/>  
                      <entry name="depChildSourceArtifactId" type="attribute" outkey="117107528"/>  
                      <entry name="depParentTargetRepositoryKind" type="attribute" outkey="117107632"/>  
                      <entry name="depParentTargetRepositoryId" type="attribute" outkey="117107736"/>  
                      <entry name="depParentTargetArtifactId" type="attribute" outkey="117107840"/>  
                      <entry name="depChildTargetRepositoryKind" type="attribute" outkey="117107944"/>  
                      <entry name="depChildTargetRepositoryId" type="attribute" outkey="117108048"/>  
                      <entry name="depChildTargetArtifactId" type="attribute" outkey="117108152"/>  
                      <entry name="errorCode" type="attribute" outkey="117108256"/>  
                      <entry name="transactionId" type="attribute" outkey="117108360"/>  
                      <entry name="includesFieldMetaData" type="attribute" outkey="117108464"/>  
                      <entry name="sourceSystemTimezone" type="attribute" outkey="117108568"/>  
                      <entry name="targetSystemTimezone" type="attribute" outkey="117108672"/> 
                    </entry>  
                    <entry name="summary" outkey="117114848"/>  
                    <entry name="description" outkey="117039040"/>  
                    <entry name="comment" outkey="117039144"/> 
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
              <vertex vertexkey="105830416"> 
                <edges> 
                  <edge vertexkey="117116968" edgekey="116807744"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117038624"> 
                <edges> 
                  <edge vertexkey="117116656" edgekey="116807856"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117038936"> 
                <edges> 
                  <edge vertexkey="117116760" edgekey="116807912"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117039040"> 
                <edges> 
                  <edge vertexkey="117116864" edgekey="116807968"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117039144"> 
                <edges> 
                  <edge vertexkey="117117176" edgekey="116808024"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117039248"> 
                <edges> 
                  <edge vertexkey="117117280" edgekey="116808080"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117039352"> 
                <edges> 
                  <edge vertexkey="117117384" edgekey="116808136"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117039456"> 
                <edges> 
                  <edge vertexkey="117117488" edgekey="116808192"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117039560"> 
                <edges> 
                  <edge vertexkey="117117592" edgekey="116808248"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117039664"> 
                <edges> 
                  <edge vertexkey="117117696" edgekey="116808304"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117039768"> 
                <edges> 
                  <edge vertexkey="117117800" edgekey="116808360"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117039872"> 
                <edges> 
                  <edge vertexkey="117117904" edgekey="116808416"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117039976"> 
                <edges> 
                  <edge vertexkey="117118008" edgekey="116808472"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117040080"> 
                <edges> 
                  <edge vertexkey="117118112" edgekey="116808528"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117040184"> 
                <edges> 
                  <edge vertexkey="117118216" edgekey="116808584"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117040288"> 
                <edges> 
                  <edge vertexkey="117118320" edgekey="116808640"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117040392"> 
                <edges> 
                  <edge vertexkey="117118424" edgekey="116808696"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117040496"> 
                <edges> 
                  <edge vertexkey="117118528" edgekey="116808752"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117040600"> 
                <edges> 
                  <edge vertexkey="117118632" edgekey="116808808"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117040704"> 
                <edges> 
                  <edge vertexkey="117118736" edgekey="116808864"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117040808"> 
                <edges> 
                  <edge vertexkey="117118840" edgekey="116808920"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117040912"> 
                <edges> 
                  <edge vertexkey="117118944" edgekey="116808976"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117041016"> 
                <edges> 
                  <edge vertexkey="117119048" edgekey="116809032"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117041120"> 
                <edges> 
                  <edge vertexkey="117119152" edgekey="116809088"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117107112"> 
                <edges> 
                  <edge vertexkey="117119256" edgekey="116809144"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117107216"> 
                <edges> 
                  <edge vertexkey="117119400" edgekey="116809200"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117107320"> 
                <edges> 
                  <edge vertexkey="117119504" edgekey="116809256"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117107424"> 
                <edges> 
                  <edge vertexkey="117119608" edgekey="116809312"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117107528"> 
                <edges> 
                  <edge vertexkey="117119712" edgekey="116809368"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117107632"> 
                <edges> 
                  <edge vertexkey="117119816" edgekey="116809424"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117107736"> 
                <edges> 
                  <edge vertexkey="117119920" edgekey="116809480"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117107840"> 
                <edges> 
                  <edge vertexkey="117120024" edgekey="116809536"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117107944"> 
                <edges> 
                  <edge vertexkey="117120128" edgekey="116809592"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117108048"> 
                <edges> 
                  <edge vertexkey="117120232" edgekey="116809648"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117108152"> 
                <edges> 
                  <edge vertexkey="117120336" edgekey="116809704"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117108256"> 
                <edges> 
                  <edge vertexkey="117120440" edgekey="116809760"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117108360"> 
                <edges> 
                  <edge vertexkey="117120544" edgekey="116809816"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117108464"> 
                <edges> 
                  <edge vertexkey="117120648" edgekey="116809872"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117108568"> 
                <edges> 
                  <edge vertexkey="117120752" edgekey="116809928"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117108672"> 
                <edges> 
                  <edge vertexkey="117120856" edgekey="116809984"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="117114848"> 
                <edges> 
                  <edge vertexkey="117117072" edgekey="105675440"/> 
                </edges> 
              </vertex> 
            </vertices> 
          </graph> 
        </structure> 
      </component> 
    </mapping>
  </xsl:template>
</xsl:stylesheet>
