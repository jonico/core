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
              <view ltx="689" rbx="1068" rby="1263"/>
              <data>
                <root scrollposition="1">
                  <header>
                    <namespaces>
                      <namespace/>
                    </namespaces>
                  </header>
                  <entry name="artifact" inpkey="101055512" expanded="1">
                    <entry name="topLevelAttributes" inpkey="101054992">
                      <entry name="artifactMode" type="attribute" inpkey="101055304"/>
                      <entry name="artifactAction" type="attribute" inpkey="101054888"/>
                      <entry name="sourceArtifactVersion" type="attribute" inpkey="101054784"/>
                      <entry name="targetArtifactVersion" type="attribute" inpkey="101055408"/>
                      <entry name="sourceArtifactLastModifiedDate" type="attribute" inpkey="101055616"/>
                      <entry name="targetArtifactLastModifiedDate" type="attribute" inpkey="101055720"/>
                      <entry name="conflictResolutionPriority" type="attribute" inpkey="101055824"/>
                      <entry name="artifactType" type="attribute" inpkey="101055928"/>
                      <entry name="sourceSystemKind" type="attribute" inpkey="101056032"/>
                      <entry name="sourceSystemId" type="attribute" inpkey="101056136"/>
                      <entry name="sourceRepositoryKind" type="attribute" inpkey="101056240"/>
                      <entry name="sourceRepositoryId" type="attribute" inpkey="101056344"/>
                      <entry name="sourceArtifactId" type="attribute" inpkey="86700808"/>
                      <entry name="targetSystemKind" type="attribute" inpkey="86700912"/>
                      <entry name="targetSystemId" type="attribute" inpkey="86701016"/>
                      <entry name="targetRepositoryKind" type="attribute" inpkey="86701120"/>
                      <entry name="targetRepositoryId" type="attribute" inpkey="86701224"/>
                      <entry name="targetArtifactId" type="attribute" inpkey="86701328"/>
                      <entry name="depParentSourceRepositoryKind" type="attribute" inpkey="86701432"/>
                      <entry name="depParentSourceRepositoryId" type="attribute" inpkey="86701624"/>
                      <entry name="depParentSourceArtifactId" type="attribute" inpkey="86701816"/>
                      <entry name="depChildSourceRepositoryKind" type="attribute" inpkey="86702008"/>
                      <entry name="depChildSourceRepositoryId" type="attribute" inpkey="86702200"/>
                      <entry name="depChildSourceArtifactId" type="attribute" inpkey="86702392"/>
                      <entry name="depParentTargetRepositoryKind" type="attribute" inpkey="86702584"/>
                      <entry name="depParentTargetRepositoryId" type="attribute" inpkey="86702776"/>
                      <entry name="depParentTargetArtifactId" type="attribute" inpkey="86703008"/>
                      <entry name="depChildTargetRepositoryKind" type="attribute" inpkey="86703280"/>
                      <entry name="depChildTargetRepositoryId" type="attribute" inpkey="86703552"/>
                      <entry name="depChildTargetArtifactId" type="attribute" inpkey="101056576"/>
                      <entry name="errorCode" type="attribute" inpkey="101056848"/>
                      <entry name="transactionId" type="attribute" inpkey="101057112"/>
                      <entry name="includesFieldMetaData" type="attribute" inpkey="101057296"/>
                      <entry name="sourceSystemTimezone" type="attribute" inpkey="101057528"/>
                      <entry name="targetSystemTimezone" type="attribute" inpkey="101057712"/>
                    </entry>
                    <entry name="title" inpkey="101055096"/>
                    <entry name="description" inpkey="101055200"/>
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
                  <entry name="artifact" outkey="101076616" expanded="1">
                    <entry name="topLevelAttributes" outkey="101076880">
                      <entry name="artifactMode" type="attribute" outkey="101077136"/>
                      <entry name="artifactAction" type="attribute" outkey="101077360"/>
                      <entry name="sourceArtifactVersion" type="attribute" outkey="101077616"/>
                      <entry name="targetArtifactVersion" type="attribute" outkey="101077888"/>
                      <entry name="sourceArtifactLastModifiedDate" type="attribute" outkey="101078160"/>
                      <entry name="targetArtifactLastModifiedDate" type="attribute" outkey="101078520"/>
                      <entry name="conflictResolutionPriority" type="attribute" outkey="101078792"/>
                      <entry name="artifactType" type="attribute" outkey="101078976"/>
                      <entry name="sourceSystemKind" type="attribute" outkey="101079200"/>
                      <entry name="sourceSystemId" type="attribute" outkey="101079456"/>
                      <entry name="sourceRepositoryKind" type="attribute" outkey="101079696"/>
                      <entry name="sourceRepositoryId" type="attribute" outkey="101079936"/>
                      <entry name="sourceArtifactId" type="attribute" outkey="101080176"/>
                      <entry name="targetSystemKind" type="attribute" outkey="101080416"/>
                      <entry name="targetSystemId" type="attribute" outkey="101080656"/>
                      <entry name="targetRepositoryKind" type="attribute" outkey="101080896"/>
                      <entry name="targetRepositoryId" type="attribute" outkey="101081136"/>
                      <entry name="targetArtifactId" type="attribute" outkey="101081376"/>
                      <entry name="depParentSourceRepositoryKind" type="attribute" outkey="101081616"/>
                      <entry name="depParentSourceRepositoryId" type="attribute" outkey="101081888"/>
                      <entry name="depParentSourceArtifactId" type="attribute" outkey="101082160"/>
                      <entry name="depChildSourceRepositoryKind" type="attribute" outkey="101082432"/>
                      <entry name="depChildSourceRepositoryId" type="attribute" outkey="101082704"/>
                      <entry name="depChildSourceArtifactId" type="attribute" outkey="101082976"/>
                      <entry name="depParentTargetRepositoryKind" type="attribute" outkey="101083248"/>
                      <entry name="depParentTargetRepositoryId" type="attribute" outkey="101083520"/>
                      <entry name="depParentTargetArtifactId" type="attribute" outkey="101083792"/>
                      <entry name="depChildTargetRepositoryKind" type="attribute" outkey="101084064"/>
                      <entry name="depChildTargetRepositoryId" type="attribute" outkey="101084336"/>
                      <entry name="depChildTargetArtifactId" type="attribute" outkey="101084608"/>
                      <entry name="errorCode" type="attribute" outkey="101084880"/>
                      <entry name="transactionId" type="attribute" outkey="101085144"/>
                      <entry name="includesFieldMetaData" type="attribute" outkey="101085328"/>
                      <entry name="sourceSystemTimezone" type="attribute" outkey="101085616"/>
                      <entry name="targetSystemTimezone" type="attribute" outkey="101085856"/>
                    </entry>
                    <entry name="RQ_REQ_COMMENT" outkey="101118696"/>
                    <entry name="RQ_REQ_NAME" outkey="101119400"/>
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
              <vertex vertexkey="101076616">
                <edges>
                  <edge vertexkey="101055512" edgekey="101125816"/>
                </edges>
              </vertex>
              <vertex vertexkey="101076880">
                <edges>
                  <edge vertexkey="101054992" edgekey="101126232"/>
                </edges>
              </vertex>
              <vertex vertexkey="101077136">
                <edges>
                  <edge vertexkey="101055304" edgekey="101126088"/>
                </edges>
              </vertex>
              <vertex vertexkey="101077360">
                <edges>
                  <edge vertexkey="101054888" edgekey="101125952"/>
                </edges>
              </vertex>
              <vertex vertexkey="101077616">
                <edges>
                  <edge vertexkey="101054784" edgekey="101127592"/>
                </edges>
              </vertex>
              <vertex vertexkey="101077888">
                <edges>
                  <edge vertexkey="101055408" edgekey="101127728"/>
                </edges>
              </vertex>
              <vertex vertexkey="101078160">
                <edges>
                  <edge vertexkey="101055616" edgekey="101127864"/>
                </edges>
              </vertex>
              <vertex vertexkey="101078520">
                <edges>
                  <edge vertexkey="101055720" edgekey="101128000"/>
                </edges>
              </vertex>
              <vertex vertexkey="101078792">
                <edges>
                  <edge vertexkey="101055824" edgekey="101128136"/>
                </edges>
              </vertex>
              <vertex vertexkey="101078976">
                <edges>
                  <edge vertexkey="101055928" edgekey="101128272"/>
                </edges>
              </vertex>
              <vertex vertexkey="101079200">
                <edges>
                  <edge vertexkey="101056032" edgekey="101128408"/>
                </edges>
              </vertex>
              <vertex vertexkey="101079456">
                <edges>
                  <edge vertexkey="101056136" edgekey="101128544"/>
                </edges>
              </vertex>
              <vertex vertexkey="101079696">
                <edges>
                  <edge vertexkey="101056240" edgekey="101128680"/>
                </edges>
              </vertex>
              <vertex vertexkey="101079936">
                <edges>
                  <edge vertexkey="101056344" edgekey="101128816"/>
                </edges>
              </vertex>
              <vertex vertexkey="101080176">
                <edges>
                  <edge vertexkey="86700808" edgekey="101128952"/>
                </edges>
              </vertex>
              <vertex vertexkey="101080416">
                <edges>
                  <edge vertexkey="86700912" edgekey="101129088"/>
                </edges>
              </vertex>
              <vertex vertexkey="101080656">
                <edges>
                  <edge vertexkey="86701016" edgekey="101129224"/>
                </edges>
              </vertex>
              <vertex vertexkey="101080896">
                <edges>
                  <edge vertexkey="86701120" edgekey="101129360"/>
                </edges>
              </vertex>
              <vertex vertexkey="101081136">
                <edges>
                  <edge vertexkey="86701224" edgekey="101129496"/>
                </edges>
              </vertex>
              <vertex vertexkey="101081376">
                <edges>
                  <edge vertexkey="86701328" edgekey="101129632"/>
                </edges>
              </vertex>
              <vertex vertexkey="101081616">
                <edges>
                  <edge vertexkey="86701432" edgekey="101129768"/>
                </edges>
              </vertex>
              <vertex vertexkey="101081888">
                <edges>
                  <edge vertexkey="86701624" edgekey="101129904"/>
                </edges>
              </vertex>
              <vertex vertexkey="101082160">
                <edges>
                  <edge vertexkey="86701816" edgekey="101130040"/>
                </edges>
              </vertex>
              <vertex vertexkey="101082432">
                <edges>
                  <edge vertexkey="86702008" edgekey="101130176"/>
                </edges>
              </vertex>
              <vertex vertexkey="101082704">
                <edges>
                  <edge vertexkey="86702200" edgekey="101130312"/>
                </edges>
              </vertex>
              <vertex vertexkey="101082976">
                <edges>
                  <edge vertexkey="86702392" edgekey="101130448"/>
                </edges>
              </vertex>
              <vertex vertexkey="101083248">
                <edges>
                  <edge vertexkey="86702584" edgekey="101130632"/>
                </edges>
              </vertex>
              <vertex vertexkey="101083520">
                <edges>
                  <edge vertexkey="86702776" edgekey="101130816"/>
                </edges>
              </vertex>
              <vertex vertexkey="101083792">
                <edges>
                  <edge vertexkey="86703008" edgekey="101131000"/>
                </edges>
              </vertex>
              <vertex vertexkey="101084064">
                <edges>
                  <edge vertexkey="86703280" edgekey="101131184"/>
                </edges>
              </vertex>
              <vertex vertexkey="101084336">
                <edges>
                  <edge vertexkey="86703552" edgekey="101131368"/>
                </edges>
              </vertex>
              <vertex vertexkey="101084608">
                <edges>
                  <edge vertexkey="101056576" edgekey="101131552"/>
                </edges>
              </vertex>
              <vertex vertexkey="101084880">
                <edges>
                  <edge vertexkey="101056848" edgekey="101131736"/>
                </edges>
              </vertex>
              <vertex vertexkey="101085144">
                <edges>
                  <edge vertexkey="101057112" edgekey="101131920"/>
                </edges>
              </vertex>
              <vertex vertexkey="101085328">
                <edges>
                  <edge vertexkey="101057296" edgekey="101132104"/>
                </edges>
              </vertex>
              <vertex vertexkey="101085616">
                <edges>
                  <edge vertexkey="101057528" edgekey="101132288"/>
                </edges>
              </vertex>
              <vertex vertexkey="101085856">
                <edges>
                  <edge vertexkey="101057712" edgekey="101132472"/>
                </edges>
              </vertex>
              <vertex vertexkey="101118696">
                <edges>
                  <edge vertexkey="101055200" edgekey="86665320"/>
                </edges>
              </vertex>
              <vertex vertexkey="101119400">
                <edges>
                  <edge vertexkey="101055096" edgekey="100517264"/>
                </edges>
              </vertex>
            </vertices>
          </graph>
        </structure>
      </component>
    </mapping>
  </xsl:template>
</xsl:stylesheet>
