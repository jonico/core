<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:fn="http://www.w3.org/2005/xpath-functions" version="2.0" exclude-result-prefixes="xsl xs fn">
  <xsl:template match="node()">
    <mapping xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="11">
      <component name="defaultmap1" blackbox="0" editable="1">
        <properties SelectedLanguage="xslt"/>
        <structure>
          <children>
            <component name="constant" library="core" uid="12" kind="2">
              <properties/>
              <targets>
                <datapoint pos="0" key="73865968"/>
              </targets>
              <view ltx="413" lty="343" rbx="-206" rby="-497"/>
              <data>
                <constant value="nobody" datatype="string"/>
                <wsdl/>
              </data>
            </component>
            <component name="constant" library="core" uid="9" kind="2">
              <properties/>
              <targets>
                <datapoint pos="0" key="73861760"/>
              </targets>
              <view ltx="536" lty="384" rbx="-264" rby="-489"/>
              <data>
                <constant datatype="string"/>
                <wsdl/>
              </data>
            </component>
            <component name="equal" library="core" uid="10" kind="5">
              <properties/>
              <sources>
                <datapoint pos="0" key="73815104"/>
                <datapoint pos="1" key="73816696"/>
              </sources>
              <targets>
                <datapoint pos="0" key="73817352"/>
              </targets>
              <view ltx="526" lty="322" rbx="-129" rby="-388"/>
            </component>
            <component name="if-else" library="core" uid="11" kind="4">
              <properties/>
              <sources>
                <datapoint pos="0" key="73818120"/>
                <datapoint pos="1" key="73824288"/>
                <datapoint pos="2" key="73814608"/>
              </sources>
              <targets>
                <datapoint pos="0" key="73814304"/>
              </targets>
              <view ltx="623" lty="325" rbx="158" rby="-484"/>
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
                  <entry name="artifact" inpkey="84417160" expanded="1">
                    <entry name="topLevelAttributes" inpkey="81973384">
                      <entry name="artifactMode" type="attribute" inpkey="84453320"/>
                      <entry name="artifactAction" type="attribute" inpkey="84453528"/>
                      <entry name="sourceArtifactVersion" type="attribute" inpkey="84453944"/>
                      <entry name="targetArtifactVersion" type="attribute" inpkey="84454048"/>
                      <entry name="sourceArtifactLastModifiedDate" type="attribute" inpkey="84453632"/>
                      <entry name="targetArtifactLastModifiedDate" type="attribute" inpkey="84453736"/>
                      <entry name="conflictResolutionPriority" type="attribute" inpkey="84453840"/>
                      <entry name="artifactType" type="attribute" inpkey="84454240"/>
                      <entry name="sourceSystemKind" type="attribute" inpkey="84454344"/>
                      <entry name="sourceSystemId" type="attribute" inpkey="84454448"/>
                      <entry name="sourceRepositoryKind" type="attribute" inpkey="84454552"/>
                      <entry name="sourceRepositoryId" type="attribute" inpkey="84454656"/>
                      <entry name="sourceArtifactId" type="attribute" inpkey="84454760"/>
                      <entry name="targetSystemKind" type="attribute" inpkey="84454864"/>
                      <entry name="targetSystemId" type="attribute" inpkey="84454968"/>
                      <entry name="targetRepositoryKind" type="attribute" inpkey="84455072"/>
                      <entry name="targetRepositoryId" type="attribute" inpkey="84455176"/>
                      <entry name="targetArtifactId" type="attribute" inpkey="84455280"/>
                      <entry name="depParentSourceRepositoryKind" type="attribute" inpkey="81974264"/>
                      <entry name="depParentSourceRepositoryId" type="attribute" inpkey="81974456"/>
                      <entry name="depParentSourceArtifactId" type="attribute" inpkey="81974648"/>
                      <entry name="depChildSourceRepositoryKind" type="attribute" inpkey="81974840"/>
                      <entry name="depChildSourceRepositoryId" type="attribute" inpkey="81975032"/>
                      <entry name="depChildSourceArtifactId" type="attribute" inpkey="81975224"/>
                      <entry name="depParentTargetRepositoryKind" type="attribute" inpkey="81975416"/>
                      <entry name="depParentTargetRepositoryId" type="attribute" inpkey="81975608"/>
                      <entry name="depParentTargetArtifactId" type="attribute" inpkey="81975800"/>
                      <entry name="depChildTargetRepositoryKind" type="attribute" inpkey="81975992"/>
                      <entry name="depChildTargetRepositoryId" type="attribute" inpkey="81976184"/>
                      <entry name="depChildTargetArtifactId" type="attribute" inpkey="81976376"/>
                      <entry name="errorCode" type="attribute" inpkey="81976568"/>
                      <entry name="transactionId" type="attribute" inpkey="81976672"/>
                      <entry name="includesFieldMetaData" type="attribute" inpkey="81976776"/>
                      <entry name="sourceSystemTimezone" type="attribute" inpkey="81976968"/>
                      <entry name="targetSystemTimezone" type="attribute" inpkey="84455384"/>
                    </entry>
                    <entry name="BG_ACTUAL_FIX_TIME" inpkey="84452592"/>
                    <entry name="BG_DESCRIPTION" inpkey="84452696"/>
                    <entry name="BG_DEV_COMMENTS" inpkey="84452800"/>
                    <entry name="BG_ESTIMATED_FIX_TIME" inpkey="84452904"/>
                    <entry name="BG_PRIORITY" inpkey="84453008"/>
                    <entry name="BG_RESPONSIBLE" inpkey="84453112"/>
                    <entry name="BG_STATUS" inpkey="84453216"/>
                    <entry name="BG_SUMMARY" inpkey="84453424"/>
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
                  <entry name="artifact" outkey="84483872" expanded="1">
                    <entry name="topLevelAttributes" outkey="84484104">
                      <entry name="artifactMode" type="attribute" outkey="84485496"/>
                      <entry name="artifactAction" type="attribute" outkey="84485736"/>
                      <entry name="sourceArtifactVersion" type="attribute" outkey="84487512"/>
                      <entry name="targetArtifactVersion" type="attribute" outkey="84487784"/>
                      <entry name="sourceArtifactLastModifiedDate" type="attribute" outkey="84485992"/>
                      <entry name="targetArtifactLastModifiedDate" type="attribute" outkey="84486616"/>
                      <entry name="conflictResolutionPriority" type="attribute" outkey="84486848"/>
                      <entry name="artifactType" type="attribute" outkey="84487032"/>
                      <entry name="sourceSystemKind" type="attribute" outkey="84487256"/>
                      <entry name="sourceSystemId" type="attribute" outkey="84488056"/>
                      <entry name="sourceRepositoryKind" type="attribute" outkey="84488296"/>
                      <entry name="sourceRepositoryId" type="attribute" outkey="84488536"/>
                      <entry name="sourceArtifactId" type="attribute" outkey="84488776"/>
                      <entry name="targetSystemKind" type="attribute" outkey="84489016"/>
                      <entry name="targetSystemId" type="attribute" outkey="84489256"/>
                      <entry name="targetRepositoryKind" type="attribute" outkey="84489496"/>
                      <entry name="targetRepositoryId" type="attribute" outkey="84489736"/>
                      <entry name="targetArtifactId" type="attribute" outkey="84489976"/>
                      <entry name="depParentSourceRepositoryKind" type="attribute" outkey="84490216"/>
                      <entry name="depParentSourceRepositoryId" type="attribute" outkey="84490488"/>
                      <entry name="depParentSourceArtifactId" type="attribute" outkey="84490760"/>
                      <entry name="depChildSourceRepositoryKind" type="attribute" outkey="84491032"/>
                      <entry name="depChildSourceRepositoryId" type="attribute" outkey="84491304"/>
                      <entry name="depChildSourceArtifactId" type="attribute" outkey="84491576"/>
                      <entry name="depParentTargetRepositoryKind" type="attribute" outkey="84491848"/>
                      <entry name="depParentTargetRepositoryId" type="attribute" outkey="84492120"/>
                      <entry name="depParentTargetArtifactId" type="attribute" outkey="84492392"/>
                      <entry name="depChildTargetRepositoryKind" type="attribute" outkey="84492664"/>
                      <entry name="depChildTargetRepositoryId" type="attribute" outkey="84492936"/>
                      <entry name="depChildTargetArtifactId" type="attribute" outkey="84493208"/>
                      <entry name="errorCode" type="attribute" outkey="84493480"/>
                      <entry name="transactionId" type="attribute" outkey="84493744"/>
                      <entry name="includesFieldMetaData" type="attribute" outkey="84493928"/>
                      <entry name="sourceSystemTimezone" type="attribute" outkey="84494216"/>
                      <entry name="targetSystemTimezone" type="attribute" outkey="84494456"/>
                    </entry>
                    <entry name="actualHours" outkey="84511920"/>
                    <entry name="estimatedHours" outkey="84512064"/>
                    <entry name="assignedTo" outkey="84486352"/>
                    <entry name="description" outkey="84484360"/>
                    <entry name="priority" outkey="84484584"/>
                    <entry name="status" outkey="84484808"/>
                    <entry name="title" outkey="84485032"/>
                    <entry name="CommentText" outkey="84485216"/>
                  </entry>
                </root>
                <document schema="{@sourceSchemaName}">
                  <xsl:attribute name="instanceroot">{}artifact</xsl:attribute>
                </document>
                <wsdl/>
              </data>
            </component>
            <component name="value-map" library="core" uid="3" kind="23">
              <properties/>
              <sources>
                <datapoint pos="0" key="81969728"/>
              </sources>
              <targets>
                <datapoint pos="0" key="81970128"/>
              </targets>
              <view ltx="503" lty="241" rbx="38" rby="-787"/>
              <data>
                <wsdl/>
                <valuemap enableDefaultValue="1">
                  <valuemapTable>
                    <entry from="5" to="1-Low"/>
                    <entry from="4" to="2-Medium"/>
                    <entry from="3" to="3-High"/>
                    <entry from="2" to="4-Very High"/>
                    <entry from="1" to="5-Urgent"/>
                  </valuemapTable>
                  <input name="input" type="string"/>
                  <result name="result" type="string" defaultValue="1-Low"/>
                </valuemap>
              </data>
            </component>
          </children>
          <graph directed="1">
            <edges/>
            <vertices>
              <vertex vertexkey="73814304">
                <edges>
                  <edge vertexkey="84453112" edgekey="84517208"/>
                </edges>
              </vertex>
              <vertex vertexkey="73817352">
                <edges>
                  <edge vertexkey="73818120" edgekey="84515960"/>
                </edges>
              </vertex>
              <vertex vertexkey="73861760">
                <edges>
                  <edge vertexkey="73824288" edgekey="84516936"/>
                </edges>
              </vertex>
              <vertex vertexkey="73865968">
                <edges>
                  <edge vertexkey="73815104" edgekey="84517072"/>
                </edges>
              </vertex>
              <vertex vertexkey="81970128">
                <edges>
                  <edge vertexkey="84453008" edgekey="84517344"/>
                </edges>
              </vertex>
              <vertex vertexkey="84483872">
                <edges>
                  <edge vertexkey="84417160" edgekey="84517480"/>
                </edges>
              </vertex>
              <vertex vertexkey="84484104">
                <edges>
                  <edge vertexkey="81973384" edgekey="84517616"/>
                </edges>
              </vertex>
              <vertex vertexkey="84484360">
                <edges>
                  <edge vertexkey="84452696" edgekey="84518024"/>
                </edges>
              </vertex>
              <vertex vertexkey="84484584">
                <edges>
                  <edge vertexkey="81969728" edgekey="84518160"/>
                </edges>
              </vertex>
              <vertex vertexkey="84484808">
                <edges>
                  <edge vertexkey="84453216" edgekey="84518296"/>
                </edges>
              </vertex>
              <vertex vertexkey="84485032">
                <edges>
                  <edge vertexkey="84453424" edgekey="84518432"/>
                </edges>
              </vertex>
              <vertex vertexkey="84485216">
                <edges>
                  <edge vertexkey="84452800" edgekey="84518568"/>
                </edges>
              </vertex>
              <vertex vertexkey="84485496">
                <edges>
                  <edge vertexkey="84453320" edgekey="84518704"/>
                </edges>
              </vertex>
              <vertex vertexkey="84485736">
                <edges>
                  <edge vertexkey="84453528" edgekey="84518840"/>
                </edges>
              </vertex>
              <vertex vertexkey="84485992">
                <edges>
                  <edge vertexkey="84453632" edgekey="84519248"/>
                </edges>
              </vertex>
              <vertex vertexkey="84486352">
                <edges>
                  <edge vertexkey="73816696" edgekey="84517752"/>
                  <edge vertexkey="73814608" edgekey="84517888"/>
                </edges>
              </vertex>
              <vertex vertexkey="84486616">
                <edges>
                  <edge vertexkey="84453736" edgekey="84519384"/>
                </edges>
              </vertex>
              <vertex vertexkey="84486848">
                <edges>
                  <edge vertexkey="84453840" edgekey="84519520"/>
                </edges>
              </vertex>
              <vertex vertexkey="84487032">
                <edges>
                  <edge vertexkey="84454240" edgekey="84519656"/>
                </edges>
              </vertex>
              <vertex vertexkey="84487256">
                <edges>
                  <edge vertexkey="84454344" edgekey="84519816"/>
                </edges>
              </vertex>
              <vertex vertexkey="84487512">
                <edges>
                  <edge vertexkey="84453944" edgekey="84518976"/>
                </edges>
              </vertex>
              <vertex vertexkey="84487784">
                <edges>
                  <edge vertexkey="84454048" edgekey="84519112"/>
                </edges>
              </vertex>
              <vertex vertexkey="84488056">
                <edges>
                  <edge vertexkey="84454448" edgekey="84520000"/>
                </edges>
              </vertex>
              <vertex vertexkey="84488296">
                <edges>
                  <edge vertexkey="84454552" edgekey="84520184"/>
                </edges>
              </vertex>
              <vertex vertexkey="84488536">
                <edges>
                  <edge vertexkey="84454656" edgekey="84520368"/>
                </edges>
              </vertex>
              <vertex vertexkey="84488776">
                <edges>
                  <edge vertexkey="84454760" edgekey="84520552"/>
                </edges>
              </vertex>
              <vertex vertexkey="84489016">
                <edges>
                  <edge vertexkey="84454864" edgekey="84520736"/>
                </edges>
              </vertex>
              <vertex vertexkey="84489256">
                <edges>
                  <edge vertexkey="84454968" edgekey="84520920"/>
                </edges>
              </vertex>
              <vertex vertexkey="84489496">
                <edges>
                  <edge vertexkey="84455072" edgekey="84521104"/>
                </edges>
              </vertex>
              <vertex vertexkey="84489736">
                <edges>
                  <edge vertexkey="84455176" edgekey="84521288"/>
                </edges>
              </vertex>
              <vertex vertexkey="84489976">
                <edges>
                  <edge vertexkey="84455280" edgekey="84521472"/>
                </edges>
              </vertex>
              <vertex vertexkey="84490216">
                <edges>
                  <edge vertexkey="81974264" edgekey="84521656"/>
                </edges>
              </vertex>
              <vertex vertexkey="84490488">
                <edges>
                  <edge vertexkey="81974456" edgekey="84521840"/>
                </edges>
              </vertex>
              <vertex vertexkey="84490760">
                <edges>
                  <edge vertexkey="81974648" edgekey="84522024"/>
                </edges>
              </vertex>
              <vertex vertexkey="84491032">
                <edges>
                  <edge vertexkey="81974840" edgekey="84522208"/>
                </edges>
              </vertex>
              <vertex vertexkey="84491304">
                <edges>
                  <edge vertexkey="81975032" edgekey="84522392"/>
                </edges>
              </vertex>
              <vertex vertexkey="84491576">
                <edges>
                  <edge vertexkey="81975224" edgekey="84522576"/>
                </edges>
              </vertex>
              <vertex vertexkey="84491848">
                <edges>
                  <edge vertexkey="81975416" edgekey="84522760"/>
                </edges>
              </vertex>
              <vertex vertexkey="84492120">
                <edges>
                  <edge vertexkey="81975608" edgekey="84522944"/>
                </edges>
              </vertex>
              <vertex vertexkey="84492392">
                <edges>
                  <edge vertexkey="81975800" edgekey="84523128"/>
                </edges>
              </vertex>
              <vertex vertexkey="84492664">
                <edges>
                  <edge vertexkey="81975992" edgekey="84523312"/>
                </edges>
              </vertex>
              <vertex vertexkey="84492936">
                <edges>
                  <edge vertexkey="81976184" edgekey="84523496"/>
                </edges>
              </vertex>
              <vertex vertexkey="84493208">
                <edges>
                  <edge vertexkey="81976376" edgekey="84523680"/>
                </edges>
              </vertex>
              <vertex vertexkey="84493480">
                <edges>
                  <edge vertexkey="81976568" edgekey="84523864"/>
                </edges>
              </vertex>
              <vertex vertexkey="84493744">
                <edges>
                  <edge vertexkey="81976672" edgekey="84524048"/>
                </edges>
              </vertex>
              <vertex vertexkey="84493928">
                <edges>
                  <edge vertexkey="81976776" edgekey="84524232"/>
                </edges>
              </vertex>
              <vertex vertexkey="84494216">
                <edges>
                  <edge vertexkey="81976968" edgekey="84524416"/>
                </edges>
              </vertex>
              <vertex vertexkey="84494456">
                <edges>
                  <edge vertexkey="84455384" edgekey="84524600"/>
                </edges>
              </vertex>
              <vertex vertexkey="84511920">
                <edges>
                  <edge vertexkey="84452592" edgekey="76668288"/>
                </edges>
              </vertex>
              <vertex vertexkey="84512064">
                <edges>
                  <edge vertexkey="84452904" edgekey="74068832"/>
                </edges>
              </vertex>
            </vertices>
          </graph>
        </structure>
      </component>
    </mapping>
  </xsl:template>
</xsl:stylesheet>
