<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:fn="http://www.w3.org/2005/xpath-functions" version="2.0" exclude-result-prefixes="xsl xs fn">
  <xsl:template match="node()">
    <mapping xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="11">
      <component name="defaultmap1" blackbox="0" editable="1">
        <properties SelectedLanguage="xslt"/>
        <structure>
          <children>
            <component name="value-map" library="core" uid="20" kind="23">
              <properties/>
              <sources>
                <datapoint pos="0" key="73866640"/>
              </sources>
              <targets>
                <datapoint pos="0" key="73865968"/>
              </targets>
              <view ltx="510" lty="274" rbx="-142" rby="-47"/>
              <data>
                <wsdl/>
                <valuemap>
                  <valuemapTable>
                    <entry from="0" to="2-Medium"/>
                    <entry from="1" to="5-Urgent"/>
                    <entry from="2" to="4-Very High"/>
                    <entry from="3" to="3-High"/>
                    <entry from="4" to="2-Medium"/>
                    <entry from="5" to="1-Low"/>
                  </valuemapTable>
                  <input name="input" type="string"/>
                  <result name="result" type="string"/>
                </valuemap>
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
                  <entry name="artifact" outkey="84566968" expanded="1">
                    <entry name="topLevelAttributes" outkey="84566488">
                      <entry name="artifactMode" type="attribute" outkey="84566264"/>
                      <entry name="artifactAction" type="attribute" outkey="84569064"/>
                      <entry name="sourceArtifactVersion" type="attribute" outkey="84569320"/>
                      <entry name="targetArtifactVersion" type="attribute" outkey="84569592"/>
                      <entry name="sourceArtifactLastModifiedDate" type="attribute" outkey="84569864"/>
                      <entry name="targetArtifactLastModifiedDate" type="attribute" outkey="84570224"/>
                      <entry name="conflictResolutionPriority" type="attribute" outkey="84570496"/>
                      <entry name="artifactType" type="attribute" outkey="84570680"/>
                      <entry name="sourceSystemKind" type="attribute" outkey="84570904"/>
                      <entry name="sourceSystemId" type="attribute" outkey="84571160"/>
                      <entry name="sourceRepositoryKind" type="attribute" outkey="84571400"/>
                      <entry name="sourceRepositoryId" type="attribute" outkey="84571640"/>
                      <entry name="sourceArtifactId" type="attribute" outkey="84571880"/>
                      <entry name="targetSystemKind" type="attribute" outkey="84572120"/>
                      <entry name="targetSystemId" type="attribute" outkey="84572360"/>
                      <entry name="targetRepositoryKind" type="attribute" outkey="84572600"/>
                      <entry name="targetRepositoryId" type="attribute" outkey="84572840"/>
                      <entry name="targetArtifactId" type="attribute" outkey="84573080"/>
                      <entry name="depParentSourceRepositoryKind" type="attribute" outkey="84573320"/>
                      <entry name="depParentSourceRepositoryId" type="attribute" outkey="84573592"/>
                      <entry name="depParentSourceArtifactId" type="attribute" outkey="84573864"/>
                      <entry name="depChildSourceRepositoryKind" type="attribute" outkey="84574136"/>
                      <entry name="depChildSourceRepositoryId" type="attribute" outkey="84574408"/>
                      <entry name="depChildSourceArtifactId" type="attribute" outkey="84574680"/>
                      <entry name="depParentTargetRepositoryKind" type="attribute" outkey="84574952"/>
                      <entry name="depParentTargetRepositoryId" type="attribute" outkey="84575224"/>
                      <entry name="depParentTargetArtifactId" type="attribute" outkey="84575496"/>
                      <entry name="depChildTargetRepositoryKind" type="attribute" outkey="84575768"/>
                      <entry name="depChildTargetRepositoryId" type="attribute" outkey="84576040"/>
                      <entry name="depChildTargetArtifactId" type="attribute" outkey="84576312"/>
                      <entry name="errorCode" type="attribute" outkey="84576584"/>
                      <entry name="transactionId" type="attribute" outkey="84576848"/>
                      <entry name="includesFieldMetaData" type="attribute" outkey="84577032"/>
                      <entry name="sourceSystemTimezone" type="attribute" outkey="84577320"/>
                      <entry name="targetSystemTimezone" type="attribute" outkey="84577560"/>
                    </entry>
                    <entry name="description" outkey="84567928"/>
                    <entry name="priority" outkey="84568152"/>
                    <entry name="title" outkey="84568600"/>
                    <entry name="CommentText" outkey="84568784"/>
                  </entry>
                </root>
                <document schema="{@sourceSchemaName}">
                  <xsl:attribute name="instanceroot">{}artifact</xsl:attribute>
                </document>
                <wsdl/>
              </data>
            </component>
            <component name="document" library="xml" uid="2" kind="14">
              <properties XSLTTargetEncoding="UTF-8" XSLTDefaultOutput="1"/>
              <view ltx="746" lty="-9" rbx="1127" rby="1486"/>
              <data>
                <root scrollposition="1">
                  <header>
                    <namespaces>
                      <namespace/>
                    </namespaces>
                  </header>
                  <entry name="artifact" inpkey="76672376" expanded="1">
                    <entry name="topLevelAttributes" inpkey="84491960">
                      <entry name="artifactMode" type="attribute" inpkey="76672480"/>
                      <entry name="artifactAction" type="attribute" inpkey="76672584"/>
                      <entry name="sourceArtifactVersion" type="attribute" inpkey="76672688"/>
                      <entry name="targetArtifactVersion" type="attribute" inpkey="76672792"/>
                      <entry name="sourceArtifactLastModifiedDate" type="attribute" inpkey="76672896"/>
                      <entry name="targetArtifactLastModifiedDate" type="attribute" inpkey="76673000"/>
                      <entry name="conflictResolutionPriority" type="attribute" inpkey="84086760"/>
                      <entry name="artifactType" type="attribute" inpkey="84086864"/>
                      <entry name="sourceSystemKind" type="attribute" inpkey="84086968"/>
                      <entry name="sourceSystemId" type="attribute" inpkey="84087072"/>
                      <entry name="sourceRepositoryKind" type="attribute" inpkey="84087176"/>
                      <entry name="sourceRepositoryId" type="attribute" inpkey="84087280"/>
                      <entry name="sourceArtifactId" type="attribute" inpkey="84087384"/>
                      <entry name="targetSystemKind" type="attribute" inpkey="84087488"/>
                      <entry name="targetSystemId" type="attribute" inpkey="84087592"/>
                      <entry name="targetRepositoryKind" type="attribute" inpkey="84087696"/>
                      <entry name="targetRepositoryId" type="attribute" inpkey="84087800"/>
                      <entry name="targetArtifactId" type="attribute" inpkey="84087904"/>
                      <entry name="depParentSourceRepositoryKind" type="attribute" inpkey="84088584"/>
                      <entry name="depParentSourceRepositoryId" type="attribute" inpkey="84088816"/>
                      <entry name="depParentSourceArtifactId" type="attribute" inpkey="84088008"/>
                      <entry name="depChildSourceRepositoryKind" type="attribute" inpkey="84088200"/>
                      <entry name="depChildSourceRepositoryId" type="attribute" inpkey="84088392"/>
                      <entry name="depChildSourceArtifactId" type="attribute" inpkey="84089088"/>
                      <entry name="depParentTargetRepositoryKind" type="attribute" inpkey="84089360"/>
                      <entry name="depParentTargetRepositoryId" type="attribute" inpkey="84525048"/>
                      <entry name="depParentTargetArtifactId" type="attribute" inpkey="84525240"/>
                      <entry name="depChildTargetRepositoryKind" type="attribute" inpkey="84525512"/>
                      <entry name="depChildTargetRepositoryId" type="attribute" inpkey="84525784"/>
                      <entry name="depChildTargetArtifactId" type="attribute" inpkey="84526056"/>
                      <entry name="errorCode" type="attribute" inpkey="84526328"/>
                      <entry name="transactionId" type="attribute" inpkey="84526592"/>
                      <entry name="includesFieldMetaData" type="attribute" inpkey="84526776"/>
                      <entry name="sourceSystemTimezone" type="attribute" inpkey="84527008"/>
                      <entry name="targetSystemTimezone" type="attribute" inpkey="84527192"/>
                    </entry>
                    <entry name="RQ_DEV_COMMENTS" inpkey="81974304"/>
                    <entry name="RQ_REQ_COMMENT" inpkey="81974408"/>
                    <entry name="RQ_REQ_NAME" inpkey="81974512"/>
                    <entry name="RQ_REQ_PRIORITY" inpkey="84524632"/>
                  </entry>
                </root>
                <document schema="{@targetSchemaName}">
                  <xsl:attribute name="instanceroot">{}artifact</xsl:attribute>
                </document>
                <wsdl/>
              </data>
            </component>
          </children>
          <graph directed="1">
            <edges/>
            <vertices>
              <vertex vertexkey="73865968">
                <edges>
                  <edge vertexkey="84524632" edgekey="84599704"/>
                </edges>
              </vertex>
              <vertex vertexkey="84566264">
                <edges>
                  <edge vertexkey="76672480" edgekey="84600536"/>
                </edges>
              </vertex>
              <vertex vertexkey="84566488">
                <edges>
                  <edge vertexkey="84491960" edgekey="84600400"/>
                </edges>
              </vertex>
              <vertex vertexkey="84566968">
                <edges>
                  <edge vertexkey="76672376" edgekey="84600672"/>
                </edges>
              </vertex>
              <vertex vertexkey="84567928">
                <edges>
                  <edge vertexkey="81974408" edgekey="84600944"/>
                </edges>
              </vertex>
              <vertex vertexkey="84568152">
                <edges>
                  <edge vertexkey="73866640" edgekey="84601080"/>
                </edges>
              </vertex>
              <vertex vertexkey="84568600">
                <edges>
                  <edge vertexkey="81974512" edgekey="84601352"/>
                </edges>
              </vertex>
              <vertex vertexkey="84568784">
                <edges>
                  <edge vertexkey="81974304" edgekey="84601488"/>
                </edges>
              </vertex>
              <vertex vertexkey="84569064">
                <edges>
                  <edge vertexkey="76672584" edgekey="84602304"/>
                </edges>
              </vertex>
              <vertex vertexkey="84569320">
                <edges>
                  <edge vertexkey="76672688" edgekey="84602440"/>
                </edges>
              </vertex>
              <vertex vertexkey="84569592">
                <edges>
                  <edge vertexkey="76672792" edgekey="84602576"/>
                </edges>
              </vertex>
              <vertex vertexkey="84569864">
                <edges>
                  <edge vertexkey="76672896" edgekey="84602712"/>
                </edges>
              </vertex>
              <vertex vertexkey="84570224">
                <edges>
                  <edge vertexkey="76673000" edgekey="84602848"/>
                </edges>
              </vertex>
              <vertex vertexkey="84570496">
                <edges>
                  <edge vertexkey="84086760" edgekey="84602984"/>
                </edges>
              </vertex>
              <vertex vertexkey="84570680">
                <edges>
                  <edge vertexkey="84086864" edgekey="84603120"/>
                </edges>
              </vertex>
              <vertex vertexkey="84570904">
                <edges>
                  <edge vertexkey="84086968" edgekey="84603256"/>
                </edges>
              </vertex>
              <vertex vertexkey="84571160">
                <edges>
                  <edge vertexkey="84087072" edgekey="84603392"/>
                </edges>
              </vertex>
              <vertex vertexkey="84571400">
                <edges>
                  <edge vertexkey="84087176" edgekey="84603552"/>
                </edges>
              </vertex>
              <vertex vertexkey="84571640">
                <edges>
                  <edge vertexkey="84087280" edgekey="84603736"/>
                </edges>
              </vertex>
              <vertex vertexkey="84571880">
                <edges>
                  <edge vertexkey="84087384" edgekey="84603920"/>
                </edges>
              </vertex>
              <vertex vertexkey="84572120">
                <edges>
                  <edge vertexkey="84087488" edgekey="84604104"/>
                </edges>
              </vertex>
              <vertex vertexkey="84572360">
                <edges>
                  <edge vertexkey="84087592" edgekey="84604288"/>
                </edges>
              </vertex>
              <vertex vertexkey="84572600">
                <edges>
                  <edge vertexkey="84087696" edgekey="84604472"/>
                </edges>
              </vertex>
              <vertex vertexkey="84572840">
                <edges>
                  <edge vertexkey="84087800" edgekey="84604656"/>
                </edges>
              </vertex>
              <vertex vertexkey="84573080">
                <edges>
                  <edge vertexkey="84087904" edgekey="84604840"/>
                </edges>
              </vertex>
              <vertex vertexkey="84573320">
                <edges>
                  <edge vertexkey="84088584" edgekey="84605024"/>
                </edges>
              </vertex>
              <vertex vertexkey="84573592">
                <edges>
                  <edge vertexkey="84088816" edgekey="84605208"/>
                </edges>
              </vertex>
              <vertex vertexkey="84573864">
                <edges>
                  <edge vertexkey="84088008" edgekey="84605392"/>
                </edges>
              </vertex>
              <vertex vertexkey="84574136">
                <edges>
                  <edge vertexkey="84088200" edgekey="84605576"/>
                </edges>
              </vertex>
              <vertex vertexkey="84574408">
                <edges>
                  <edge vertexkey="84088392" edgekey="84605760"/>
                </edges>
              </vertex>
              <vertex vertexkey="84574680">
                <edges>
                  <edge vertexkey="84089088" edgekey="84605944"/>
                </edges>
              </vertex>
              <vertex vertexkey="84574952">
                <edges>
                  <edge vertexkey="84089360" edgekey="84606128"/>
                </edges>
              </vertex>
              <vertex vertexkey="84575224">
                <edges>
                  <edge vertexkey="84525048" edgekey="84606312"/>
                </edges>
              </vertex>
              <vertex vertexkey="84575496">
                <edges>
                  <edge vertexkey="84525240" edgekey="84606496"/>
                </edges>
              </vertex>
              <vertex vertexkey="84575768">
                <edges>
                  <edge vertexkey="84525512" edgekey="84606680"/>
                </edges>
              </vertex>
              <vertex vertexkey="84576040">
                <edges>
                  <edge vertexkey="84525784" edgekey="84606864"/>
                </edges>
              </vertex>
              <vertex vertexkey="84576312">
                <edges>
                  <edge vertexkey="84526056" edgekey="84607048"/>
                </edges>
              </vertex>
              <vertex vertexkey="84576584">
                <edges>
                  <edge vertexkey="84526328" edgekey="84607232"/>
                </edges>
              </vertex>
              <vertex vertexkey="84576848">
                <edges>
                  <edge vertexkey="84526592" edgekey="84607416"/>
                </edges>
              </vertex>
              <vertex vertexkey="84577032">
                <edges>
                  <edge vertexkey="84526776" edgekey="84607600"/>
                </edges>
              </vertex>
              <vertex vertexkey="84577320">
                <edges>
                  <edge vertexkey="84527008" edgekey="84607784"/>
                </edges>
              </vertex>
              <vertex vertexkey="84577560">
                <edges>
                  <edge vertexkey="84527192" edgekey="84607968"/>
                </edges>
              </vertex>
            </vertices>
          </graph>
        </structure>
      </component>
    </mapping>
  </xsl:template>
</xsl:stylesheet>
