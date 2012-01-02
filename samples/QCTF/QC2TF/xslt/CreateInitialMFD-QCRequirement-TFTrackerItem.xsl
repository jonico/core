<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:fn="http://www.w3.org/2005/xpath-functions" version="2.0" exclude-result-prefixes="xsl xs fn">
  <xsl:template match="node()">
    <mapping xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="11">  
      <component name="defaultmap1" blackbox="0" editable="1"> 
        <properties SelectedLanguage="xslt"/>  
        <structure> 
          <children> 
            <component name="value-map" library="core" uid="28" kind="23"> 
              <properties/>  
              <sources> 
                <datapoint pos="0" key="82055568"/> 
              </sources>  
              <targets> 
                <datapoint pos="0" key="82055968"/> 
              </targets>  
              <view ltx="515" lty="456" rbx="-225" rby="-3"/>  
              <data> 
                <wsdl/>  
                <valuemap enableDefaultValue="1"> 
                  <valuemapTable> 
                    <entry from="1-Low" to="5"/>  
                    <entry from="2-Medium" to="4"/>  
                    <entry from="3-High" to="3"/>  
                    <entry from="4-Very High" to="2"/>  
                    <entry from="5-Urgent" to="1"/>  
                    <entry to="0"/> 
                  </valuemapTable>  
                  <input name="Input" type="string"/>  
                  <result name="result" type="string" defaultValue="5"/> 
                </valuemap> 
              </data> 
            </component>  
            <component name="document" library="xml" uid="2" kind="14"> 
              <properties XSLTTargetEncoding="UTF-8" XSLTDefaultOutput="1"/>  
              <view ltx="832" lty="-3" rbx="1211" rby="1260"/>  
              <data> 
                <root scrollposition="1"> 
                  <header> 
                    <namespaces> 
                      <namespace/> 
                    </namespaces> 
                  </header>  
                  <entry name="artifact" inpkey="84674064" expanded="1"> 
                    <entry name="topLevelAttributes" inpkey="84673360"> 
                      <entry name="artifactMode" type="attribute" inpkey="84673600"/>  
                      <entry name="artifactAction" type="attribute" inpkey="84676160"/>  
                      <entry name="sourceArtifactVersion" type="attribute" inpkey="84677456"/>  
                      <entry name="targetArtifactVersion" type="attribute" inpkey="84677744"/>  
                      <entry name="sourceArtifactLastModifiedDate" type="attribute" inpkey="84676416"/>  
                      <entry name="targetArtifactLastModifiedDate" type="attribute" inpkey="84676776"/>  
                      <entry name="conflictResolutionPriority" type="attribute" inpkey="84677048"/>  
                      <entry name="artifactType" type="attribute" inpkey="84677232"/>  
                      <entry name="sourceSystemKind" type="attribute" inpkey="84678016"/>  
                      <entry name="sourceSystemId" type="attribute" inpkey="84678256"/>  
                      <entry name="sourceRepositoryKind" type="attribute" inpkey="84678496"/>  
                      <entry name="sourceRepositoryId" type="attribute" inpkey="84678736"/>  
                      <entry name="sourceArtifactId" type="attribute" inpkey="84678976"/>  
                      <entry name="targetSystemKind" type="attribute" inpkey="84679216"/>  
                      <entry name="targetSystemId" type="attribute" inpkey="84679456"/>  
                      <entry name="targetRepositoryKind" type="attribute" inpkey="84679696"/>  
                      <entry name="targetRepositoryId" type="attribute" inpkey="84679936"/>  
                      <entry name="targetArtifactId" type="attribute" inpkey="84680176"/>  
                      <entry name="depParentSourceRepositoryKind" type="attribute" inpkey="84680416"/>  
                      <entry name="depParentSourceRepositoryId" type="attribute" inpkey="84680688"/>  
                      <entry name="depParentSourceArtifactId" type="attribute" inpkey="84680960"/>  
                      <entry name="depChildSourceRepositoryKind" type="attribute" inpkey="84681232"/>  
                      <entry name="depChildSourceRepositoryId" type="attribute" inpkey="84681504"/>  
                      <entry name="depChildSourceArtifactId" type="attribute" inpkey="84681776"/>  
                      <entry name="depParentTargetRepositoryKind" type="attribute" inpkey="84682048"/>  
                      <entry name="depParentTargetRepositoryId" type="attribute" inpkey="84682320"/>  
                      <entry name="depParentTargetArtifactId" type="attribute" inpkey="84682592"/>  
                      <entry name="depChildTargetRepositoryKind" type="attribute" inpkey="84682864"/>  
                      <entry name="depChildTargetRepositoryId" type="attribute" inpkey="84683136"/>  
                      <entry name="depChildTargetArtifactId" type="attribute" inpkey="84683408"/>  
                      <entry name="errorCode" type="attribute" inpkey="84683680"/>  
                      <entry name="transactionId" type="attribute" inpkey="84683944"/>  
                      <entry name="includesFieldMetaData" type="attribute" inpkey="84684128"/>  
                      <entry name="sourceSystemTimezone" type="attribute" inpkey="84684416"/>  
                      <entry name="targetSystemTimezone" type="attribute" inpkey="84684656"/> 
                    </entry>  
                    <entry name="description" inpkey="84675024"/>  
                    <entry name="priority" inpkey="84675248"/>  
                    <entry name="title" inpkey="84675696"/>  
                    <entry name="CommentText" inpkey="84675880"/> 
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
                  <entry name="artifact" outkey="84631040" expanded="1"> 
                    <entry name="topLevelAttributes" outkey="84630520"> 
                      <entry name="artifactMode" type="attribute" outkey="82061384"/>  
                      <entry name="artifactAction" type="attribute" outkey="82061488"/>  
                      <entry name="sourceArtifactVersion" type="attribute" outkey="82061592"/>  
                      <entry name="targetArtifactVersion" type="attribute" outkey="82061696"/>  
                      <entry name="sourceArtifactLastModifiedDate" type="attribute" outkey="82061800"/>  
                      <entry name="targetArtifactLastModifiedDate" type="attribute" outkey="82061904"/>  
                      <entry name="conflictResolutionPriority" type="attribute" outkey="82062008"/>  
                      <entry name="artifactType" type="attribute" outkey="82062112"/>  
                      <entry name="sourceSystemKind" type="attribute" outkey="82062216"/>  
                      <entry name="sourceSystemId" type="attribute" outkey="82062320"/>  
                      <entry name="sourceRepositoryKind" type="attribute" outkey="82062424"/>  
                      <entry name="sourceRepositoryId" type="attribute" outkey="82062528"/>  
                      <entry name="sourceArtifactId" type="attribute" outkey="82062632"/>  
                      <entry name="targetSystemKind" type="attribute" outkey="82062816"/>  
                      <entry name="targetSystemId" type="attribute" outkey="82063000"/>  
                      <entry name="targetRepositoryKind" type="attribute" outkey="82063184"/>  
                      <entry name="targetRepositoryId" type="attribute" outkey="82063368"/>  
                      <entry name="targetArtifactId" type="attribute" outkey="82063552"/>  
                      <entry name="depParentSourceRepositoryKind" type="attribute" outkey="82063736"/>  
                      <entry name="depParentSourceRepositoryId" type="attribute" outkey="82064008"/>  
                      <entry name="depParentSourceArtifactId" type="attribute" outkey="82064280"/>  
                      <entry name="depChildSourceRepositoryKind" type="attribute" outkey="82064552"/>  
                      <entry name="depChildSourceRepositoryId" type="attribute" outkey="82064824"/>  
                      <entry name="depChildSourceArtifactId" type="attribute" outkey="82065096"/>  
                      <entry name="depParentTargetRepositoryKind" type="attribute" outkey="84631728"/>  
                      <entry name="depParentTargetRepositoryId" type="attribute" outkey="84632000"/>  
                      <entry name="depParentTargetArtifactId" type="attribute" outkey="84632272"/>  
                      <entry name="depChildTargetRepositoryKind" type="attribute" outkey="84632544"/>  
                      <entry name="depChildTargetRepositoryId" type="attribute" outkey="84632816"/>  
                      <entry name="depChildTargetArtifactId" type="attribute" outkey="84633088"/>  
                      <entry name="errorCode" type="attribute" outkey="84633360"/>  
                      <entry name="transactionId" type="attribute" outkey="84633624"/>  
                      <entry name="includesFieldMetaData" type="attribute" outkey="84633808"/>  
                      <entry name="sourceSystemTimezone" type="attribute" outkey="84634040"/>  
                      <entry name="targetSystemTimezone" type="attribute" outkey="84634224"/> 
                    </entry>  
                    <entry name="RQ_DEV_COMMENTS" outkey="84630624"/>  
                    <entry name="RQ_REQ_COMMENT" outkey="84630728"/>  
                    <entry name="RQ_REQ_NAME" outkey="84630832"/>  
                    <entry name="RQ_REQ_PRIORITY" outkey="84630936"/> 
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
              <vertex vertexkey="82055968"> 
                <edges> 
                  <edge vertexkey="84675248" edgekey="84709312"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="82061384"> 
                <edges> 
                  <edge vertexkey="84673600" edgekey="84712696"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="82061488"> 
                <edges> 
                  <edge vertexkey="84676160" edgekey="84712880"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="82061592"> 
                <edges> 
                  <edge vertexkey="84677456" edgekey="84713064"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="82061696"> 
                <edges> 
                  <edge vertexkey="84677744" edgekey="84713248"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="82061800"> 
                <edges> 
                  <edge vertexkey="84676416" edgekey="84713432"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="82061904"> 
                <edges> 
                  <edge vertexkey="84676776" edgekey="84713616"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="82062008"> 
                <edges> 
                  <edge vertexkey="84677048" edgekey="84713800"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="82062112"> 
                <edges> 
                  <edge vertexkey="84677232" edgekey="84713984"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="82062216"> 
                <edges> 
                  <edge vertexkey="84678016" edgekey="84714168"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="82062320"> 
                <edges> 
                  <edge vertexkey="84678256" edgekey="84714352"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="82062424"> 
                <edges> 
                  <edge vertexkey="84678496" edgekey="84714536"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="82062528"> 
                <edges> 
                  <edge vertexkey="84678736" edgekey="84714720"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="82062632"> 
                <edges> 
                  <edge vertexkey="84678976" edgekey="84714904"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="82062816"> 
                <edges> 
                  <edge vertexkey="84679216" edgekey="84715088"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="82063000"> 
                <edges> 
                  <edge vertexkey="84679456" edgekey="84715272"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="82063184"> 
                <edges> 
                  <edge vertexkey="84679696" edgekey="84715456"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="82063368"> 
                <edges> 
                  <edge vertexkey="84679936" edgekey="84715640"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="82063552"> 
                <edges> 
                  <edge vertexkey="84680176" edgekey="84715824"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="82063736"> 
                <edges> 
                  <edge vertexkey="84680416" edgekey="84716008"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="82064008"> 
                <edges> 
                  <edge vertexkey="84680688" edgekey="84716192"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="82064280"> 
                <edges> 
                  <edge vertexkey="84680960" edgekey="84716376"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="82064552"> 
                <edges> 
                  <edge vertexkey="84681232" edgekey="84716560"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="82064824"> 
                <edges> 
                  <edge vertexkey="84681504" edgekey="84716744"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="82065096"> 
                <edges> 
                  <edge vertexkey="84681776" edgekey="84716928"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84630520"> 
                <edges> 
                  <edge vertexkey="84673360" edgekey="84709584"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84630624"> 
                <edges> 
                  <edge vertexkey="84675880" edgekey="84709856"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84630728"> 
                <edges> 
                  <edge vertexkey="84675024" edgekey="84709992"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84630832"> 
                <edges> 
                  <edge vertexkey="84675696" edgekey="84710128"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84630936"> 
                <edges> 
                  <edge vertexkey="82055568" edgekey="84710264"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84631040"> 
                <edges> 
                  <edge vertexkey="84674064" edgekey="84709720"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84631728"> 
                <edges> 
                  <edge vertexkey="84682048" edgekey="84717112"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84632000"> 
                <edges> 
                  <edge vertexkey="84682320" edgekey="84717296"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84632272"> 
                <edges> 
                  <edge vertexkey="84682592" edgekey="84717480"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84632544"> 
                <edges> 
                  <edge vertexkey="84682864" edgekey="84717664"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84632816"> 
                <edges> 
                  <edge vertexkey="84683136" edgekey="84717848"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84633088"> 
                <edges> 
                  <edge vertexkey="84683408" edgekey="84718032"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84633360"> 
                <edges> 
                  <edge vertexkey="84683680" edgekey="84718216"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84633624"> 
                <edges> 
                  <edge vertexkey="84683944" edgekey="84718400"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84633808"> 
                <edges> 
                  <edge vertexkey="84684128" edgekey="84718584"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84634040"> 
                <edges> 
                  <edge vertexkey="84684416" edgekey="84718768"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84634224"> 
                <edges> 
                  <edge vertexkey="84684656" edgekey="84718952"/> 
                </edges> 
              </vertex> 
            </vertices> 
          </graph> 
        </structure> 
      </component> 
    </mapping>
  </xsl:template>
</xsl:stylesheet>
