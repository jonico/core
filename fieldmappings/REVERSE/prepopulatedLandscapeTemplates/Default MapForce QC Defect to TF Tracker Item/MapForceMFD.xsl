<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:fn="http://www.w3.org/2005/xpath-functions" version="2.0" exclude-result-prefixes="xsl xs fn">
  <xsl:template match="node()">
    <mapping xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="11">
      <component name="defaultmap1" blackbox="0" editable="1">
        <properties SelectedLanguage="xslt"/>
        <structure>
          <children>
            <component name="constant" library="core" uid="4" kind="2">
              <properties/>
              <targets>
                <datapoint pos="0" key="73866640"/>
              </targets>
              <view ltx="387" lty="109" rbx="-94" rby="-46"/>
              <data>
                <constant datatype="string"/>
                <wsdl/>
              </data>
            </component>
            <component name="equal" library="core" uid="6" kind="5">
              <properties/>
              <sources>
                <datapoint pos="0" key="73865968"/>
                <datapoint pos="1" key="73861760"/>
              </sources>
              <targets>
                <datapoint pos="0" key="73815104"/>
              </targets>
              <view ltx="444" lty="87"/>
            </component>
            <component name="constant" library="core" uid="7" kind="2">
              <properties/>
              <targets>
                <datapoint pos="0" key="73816696"/>
              </targets>
              <view ltx="448" lty="149" rbx="-33" rby="-6"/>
              <data>
                <constant value="0" datatype="string"/>
                <wsdl/>
              </data>
            </component>
            <component name="constant" library="core" uid="8" kind="2">
              <properties/>
              <targets>
                <datapoint pos="0" key="73817352"/>
              </targets>
              <view ltx="391" lty="219" rbx="-90" rby="64"/>
              <data>
                <constant datatype="string"/>
                <wsdl/>
              </data>
            </component>
            <component name="constant" library="core" uid="10" kind="2">
              <properties/>
              <targets>
                <datapoint pos="0" key="73818120"/>
              </targets>
              <view ltx="452" lty="259" rbx="-29" rby="104"/>
              <data>
                <constant value="0" datatype="string"/>
                <wsdl/>
              </data>
            </component>
            <component name="equal" library="core" uid="9" kind="5">
              <properties/>
              <sources>
                <datapoint pos="0" key="73824288"/>
                <datapoint pos="1" key="73814608"/>
              </sources>
              <targets>
                <datapoint pos="0" key="73814304"/>
              </targets>
              <view ltx="448" lty="197" rbx="4" rby="110"/>
            </component>
            <component name="value-map" library="core" uid="3" kind="23">
              <properties/>
              <sources>
                <datapoint pos="0" key="81994376"/>
              </sources>
              <targets>
                <datapoint pos="0" key="81994776"/>
              </targets>
              <view ltx="479" lty="296" rbx="14" rby="-732"/>
              <data>
                <wsdl/>
                <valuemap enableDefaultValue="1">
                  <valuemapTable>
                    <entry from="1-Low" to="5"/>
                    <entry from="2-Medium" to="4"/>
                    <entry from="3-High" to="3"/>
                    <entry from="4-Very High" to="2"/>
                    <entry from="5-Urgent" to="1"/>
                  </valuemapTable>
                  <input name="input" type="string"/>
                  <result name="result" type="string" defaultValue="5"/>
                </valuemap>
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
                  <entry name="artifact" outkey="84446952" expanded="1">
                    <entry name="topLevelAttributes" outkey="82001168">
                      <entry name="artifactMode" type="attribute" outkey="84482928"/>
                      <entry name="artifactAction" type="attribute" outkey="84483136"/>
                      <entry name="sourceArtifactVersion" type="attribute" outkey="84483656"/>
                      <entry name="targetArtifactVersion" type="attribute" outkey="84483760"/>
                      <entry name="sourceArtifactLastModifiedDate" type="attribute" outkey="84483864"/>
                      <entry name="targetArtifactLastModifiedDate" type="attribute" outkey="84483240"/>
                      <entry name="conflictResolutionPriority" type="attribute" outkey="84483344"/>
                      <entry name="artifactType" type="attribute" outkey="84483448"/>
                      <entry name="sourceSystemKind" type="attribute" outkey="84483552"/>
                      <entry name="sourceSystemId" type="attribute" outkey="82002136"/>
                      <entry name="sourceRepositoryKind" type="attribute" outkey="82002240"/>
                      <entry name="sourceRepositoryId" type="attribute" outkey="82002344"/>
                      <entry name="sourceArtifactId" type="attribute" outkey="82002448"/>
                      <entry name="targetSystemKind" type="attribute" outkey="82002552"/>
                      <entry name="targetSystemId" type="attribute" outkey="82002656"/>
                      <entry name="targetRepositoryKind" type="attribute" outkey="82002760"/>
                      <entry name="targetRepositoryId" type="attribute" outkey="82002864"/>
                      <entry name="targetArtifactId" type="attribute" outkey="82002968"/>
                      <entry name="depParentSourceRepositoryKind" type="attribute" outkey="82003072"/>
                      <entry name="depParentSourceRepositoryId" type="attribute" outkey="82003176"/>
                      <entry name="depParentSourceArtifactId" type="attribute" outkey="82003368"/>
                      <entry name="depChildSourceRepositoryKind" type="attribute" outkey="82003560"/>
                      <entry name="depChildSourceRepositoryId" type="attribute" outkey="82003752"/>
                      <entry name="depChildSourceArtifactId" type="attribute" outkey="82003944"/>
                      <entry name="depParentTargetRepositoryKind" type="attribute" outkey="82004136"/>
                      <entry name="depParentTargetRepositoryId" type="attribute" outkey="82004328"/>
                      <entry name="depParentTargetArtifactId" type="attribute" outkey="82004520"/>
                      <entry name="depChildTargetRepositoryKind" type="attribute" outkey="82004712"/>
                      <entry name="depChildTargetRepositoryId" type="attribute" outkey="84484056"/>
                      <entry name="depChildTargetArtifactId" type="attribute" outkey="84484160"/>
                      <entry name="errorCode" type="attribute" outkey="84484352"/>
                      <entry name="transactionId" type="attribute" outkey="84484456"/>
                      <entry name="includesFieldMetaData" type="attribute" outkey="84484560"/>
                      <entry name="sourceSystemTimezone" type="attribute" outkey="84484752"/>
                      <entry name="targetSystemTimezone" type="attribute" outkey="84484912"/>
                    </entry>
                    <entry name="BG_ACTUAL_FIX_TIME" outkey="84482200"/>
                    <entry name="BG_DESCRIPTION" outkey="84482304"/>
                    <entry name="BG_DEV_COMMENTS" outkey="84482408"/>
                    <entry name="BG_ESTIMATED_FIX_TIME" outkey="84482512"/>
                    <entry name="BG_PRIORITY" outkey="84482616"/>
                    <entry name="BG_RESPONSIBLE" outkey="84482720"/>
                    <entry name="BG_STATUS" outkey="84482824"/>
                    <entry name="BG_SUMMARY" outkey="84483032"/>
                  </entry>
                </root>
                <document schema="{@sourceSchemaName}">
                  <xsl:attribute name="instanceroot">{}artifact</xsl:attribute>
                </document>
                <wsdl/>
              </data>
            </component>
            <component name="if-else" library="core" uid="5" kind="4">
              <properties/>
              <sources>
                <datapoint pos="0" key="81996040"/>
                <datapoint pos="1" key="81996224"/>
                <datapoint pos="2" key="81996408"/>
              </sources>
              <targets>
                <datapoint pos="0" key="81996592"/>
              </targets>
              <view ltx="528" lty="76" rbx="47" rby="-79"/>
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
                  <entry name="artifact" inpkey="84513704" expanded="1">
                    <entry name="topLevelAttributes" inpkey="84513240">
                      <entry name="artifactMode" type="attribute" inpkey="84515080"/>
                      <entry name="artifactAction" type="attribute" inpkey="84515320"/>
                      <entry name="sourceArtifactVersion" type="attribute" inpkey="84516616"/>
                      <entry name="targetArtifactVersion" type="attribute" inpkey="84516888"/>
                      <entry name="sourceArtifactLastModifiedDate" type="attribute" inpkey="84515840"/>
                      <entry name="targetArtifactLastModifiedDate" type="attribute" inpkey="84516160"/>
                      <entry name="conflictResolutionPriority" type="attribute" inpkey="84516432"/>
                      <entry name="artifactType" type="attribute" inpkey="84517160"/>
                      <entry name="sourceSystemKind" type="attribute" inpkey="84517384"/>
                      <entry name="sourceSystemId" type="attribute" inpkey="84517640"/>
                      <entry name="sourceRepositoryKind" type="attribute" inpkey="84517880"/>
                      <entry name="sourceRepositoryId" type="attribute" inpkey="84518120"/>
                      <entry name="sourceArtifactId" type="attribute" inpkey="84518360"/>
                      <entry name="targetSystemKind" type="attribute" inpkey="84518600"/>
                      <entry name="targetSystemId" type="attribute" inpkey="84518840"/>
                      <entry name="targetRepositoryKind" type="attribute" inpkey="84519080"/>
                      <entry name="targetRepositoryId" type="attribute" inpkey="84519320"/>
                      <entry name="targetArtifactId" type="attribute" inpkey="84519560"/>
                      <entry name="depParentSourceRepositoryKind" type="attribute" inpkey="84519800"/>
                      <entry name="depParentSourceRepositoryId" type="attribute" inpkey="84520072"/>
                      <entry name="depParentSourceArtifactId" type="attribute" inpkey="84520344"/>
                      <entry name="depChildSourceRepositoryKind" type="attribute" inpkey="84520616"/>
                      <entry name="depChildSourceRepositoryId" type="attribute" inpkey="84520888"/>
                      <entry name="depChildSourceArtifactId" type="attribute" inpkey="84521160"/>
                      <entry name="depParentTargetRepositoryKind" type="attribute" inpkey="84521432"/>
                      <entry name="depParentTargetRepositoryId" type="attribute" inpkey="84521704"/>
                      <entry name="depParentTargetArtifactId" type="attribute" inpkey="84521976"/>
                      <entry name="depChildTargetRepositoryKind" type="attribute" inpkey="84522248"/>
                      <entry name="depChildTargetRepositoryId" type="attribute" inpkey="84522520"/>
                      <entry name="depChildTargetArtifactId" type="attribute" inpkey="84522792"/>
                      <entry name="errorCode" type="attribute" inpkey="84523064"/>
                      <entry name="transactionId" type="attribute" inpkey="84523328"/>
                      <entry name="includesFieldMetaData" type="attribute" inpkey="84523512"/>
                      <entry name="sourceSystemTimezone" type="attribute" inpkey="84523800"/>
                      <entry name="targetSystemTimezone" type="attribute" inpkey="84524040"/>
                    </entry>
                    <entry name="actualHours" inpkey="84524968"/>
                    <entry name="estimatedHours" inpkey="84525112"/>
                    <entry name="assignedTo" inpkey="84515576"/>
                    <entry name="description" inpkey="84513928"/>
                    <entry name="priority" inpkey="84514168"/>
                    <entry name="status" inpkey="84514392"/>
                    <entry name="title" inpkey="84514616"/>
                    <entry name="CommentText" inpkey="84514800"/>
                  </entry>
                </root>
                <document schema="{@targetSchemaName}">
                  <xsl:attribute name="instanceroot">{}artifact</xsl:attribute>
                </document>
                <wsdl/>
              </data>
            </component>
            <component name="if-else" library="core" uid="11" kind="4">
              <properties/>
              <sources>
                <datapoint pos="0" key="81997600"/>
                <datapoint pos="1" key="81997784"/>
                <datapoint pos="2" key="81997968"/>
              </sources>
              <targets>
                <datapoint pos="0" key="81998152"/>
              </targets>
              <view ltx="532" lty="186" rbx="51" rby="31"/>
            </component>
          </children>
          <graph directed="1">
            <edges/>
            <vertices>
              <vertex vertexkey="73814304">
                <edges>
                  <edge vertexkey="81997600" edgekey="84547120"/>
                </edges>
              </vertex>
              <vertex vertexkey="73815104">
                <edges>
                  <edge vertexkey="81996040" edgekey="84546576"/>
                </edges>
              </vertex>
              <vertex vertexkey="73816696">
                <edges>
                  <edge vertexkey="81996224" edgekey="84546712"/>
                </edges>
              </vertex>
              <vertex vertexkey="73817352">
                <edges>
                  <edge vertexkey="73824288" edgekey="84545928"/>
                </edges>
              </vertex>
              <vertex vertexkey="73818120">
                <edges>
                  <edge vertexkey="81997784" edgekey="84546984"/>
                </edges>
              </vertex>
              <vertex vertexkey="73866640">
                <edges>
                  <edge vertexkey="73865968" edgekey="84546848"/>
                </edges>
              </vertex>
              <vertex vertexkey="81994776">
                <edges>
                  <edge vertexkey="84514168" edgekey="84547256"/>
                </edges>
              </vertex>
              <vertex vertexkey="81996592">
                <edges>
                  <edge vertexkey="84524968" edgekey="81975216"/>
                </edges>
              </vertex>
              <vertex vertexkey="81998152">
                <edges>
                  <edge vertexkey="84525112" edgekey="84447776"/>
                </edges>
              </vertex>
              <vertex vertexkey="82001168">
                <edges>
                  <edge vertexkey="84513240" edgekey="84547528"/>
                </edges>
              </vertex>
              <vertex vertexkey="82002136">
                <edges>
                  <edge vertexkey="84517640" edgekey="84550792"/>
                </edges>
              </vertex>
              <vertex vertexkey="82002240">
                <edges>
                  <edge vertexkey="84517880" edgekey="84550976"/>
                </edges>
              </vertex>
              <vertex vertexkey="82002344">
                <edges>
                  <edge vertexkey="84518120" edgekey="84551160"/>
                </edges>
              </vertex>
              <vertex vertexkey="82002448">
                <edges>
                  <edge vertexkey="84518360" edgekey="84551344"/>
                </edges>
              </vertex>
              <vertex vertexkey="82002552">
                <edges>
                  <edge vertexkey="84518600" edgekey="84551528"/>
                </edges>
              </vertex>
              <vertex vertexkey="82002656">
                <edges>
                  <edge vertexkey="84518840" edgekey="84551712"/>
                </edges>
              </vertex>
              <vertex vertexkey="82002760">
                <edges>
                  <edge vertexkey="84519080" edgekey="84551896"/>
                </edges>
              </vertex>
              <vertex vertexkey="82002864">
                <edges>
                  <edge vertexkey="84519320" edgekey="84552080"/>
                </edges>
              </vertex>
              <vertex vertexkey="82002968">
                <edges>
                  <edge vertexkey="84519560" edgekey="84552264"/>
                </edges>
              </vertex>
              <vertex vertexkey="82003072">
                <edges>
                  <edge vertexkey="84519800" edgekey="84552448"/>
                </edges>
              </vertex>
              <vertex vertexkey="82003176">
                <edges>
                  <edge vertexkey="84520072" edgekey="84552632"/>
                </edges>
              </vertex>
              <vertex vertexkey="82003368">
                <edges>
                  <edge vertexkey="84520344" edgekey="84552816"/>
                </edges>
              </vertex>
              <vertex vertexkey="82003560">
                <edges>
                  <edge vertexkey="84520616" edgekey="84553000"/>
                </edges>
              </vertex>
              <vertex vertexkey="82003752">
                <edges>
                  <edge vertexkey="84520888" edgekey="84553184"/>
                </edges>
              </vertex>
              <vertex vertexkey="82003944">
                <edges>
                  <edge vertexkey="84521160" edgekey="84553368"/>
                </edges>
              </vertex>
              <vertex vertexkey="82004136">
                <edges>
                  <edge vertexkey="84521432" edgekey="84553552"/>
                </edges>
              </vertex>
              <vertex vertexkey="82004328">
                <edges>
                  <edge vertexkey="84521704" edgekey="84553736"/>
                </edges>
              </vertex>
              <vertex vertexkey="82004520">
                <edges>
                  <edge vertexkey="84521976" edgekey="84553920"/>
                </edges>
              </vertex>
              <vertex vertexkey="82004712">
                <edges>
                  <edge vertexkey="84522248" edgekey="84554104"/>
                </edges>
              </vertex>
              <vertex vertexkey="84446952">
                <edges>
                  <edge vertexkey="84513704" edgekey="84547800"/>
                </edges>
              </vertex>
              <vertex vertexkey="84482200">
                <edges>
                  <edge vertexkey="73861760" edgekey="84547936"/>
                  <edge vertexkey="81996408" edgekey="84548072"/>
                </edges>
              </vertex>
              <vertex vertexkey="84482304">
                <edges>
                  <edge vertexkey="84513928" edgekey="84548208"/>
                </edges>
              </vertex>
              <vertex vertexkey="84482408">
                <edges>
                  <edge vertexkey="84514800" edgekey="84548344"/>
                </edges>
              </vertex>
              <vertex vertexkey="84482512">
                <edges>
                  <edge vertexkey="73814608" edgekey="84548480"/>
                  <edge vertexkey="81997968" edgekey="84548616"/>
                </edges>
              </vertex>
              <vertex vertexkey="84482616">
                <edges>
                  <edge vertexkey="81994376" edgekey="84548752"/>
                </edges>
              </vertex>
              <vertex vertexkey="84482720">
                <edges>
                  <edge vertexkey="84515576" edgekey="84548888"/>
                </edges>
              </vertex>
              <vertex vertexkey="84482824">
                <edges>
                  <edge vertexkey="84514392" edgekey="84549024"/>
                </edges>
              </vertex>
              <vertex vertexkey="84482928">
                <edges>
                  <edge vertexkey="84515080" edgekey="84547664"/>
                </edges>
              </vertex>
              <vertex vertexkey="84483032">
                <edges>
                  <edge vertexkey="84514616" edgekey="84549160"/>
                </edges>
              </vertex>
              <vertex vertexkey="84483136">
                <edges>
                  <edge vertexkey="84515320" edgekey="84549320"/>
                </edges>
              </vertex>
              <vertex vertexkey="84483240">
                <edges>
                  <edge vertexkey="84516160" edgekey="84549872"/>
                </edges>
              </vertex>
              <vertex vertexkey="84483344">
                <edges>
                  <edge vertexkey="84516432" edgekey="84550240"/>
                </edges>
              </vertex>
              <vertex vertexkey="84483448">
                <edges>
                  <edge vertexkey="84517160" edgekey="84550424"/>
                </edges>
              </vertex>
              <vertex vertexkey="84483552">
                <edges>
                  <edge vertexkey="84517384" edgekey="84550608"/>
                </edges>
              </vertex>
              <vertex vertexkey="84483656">
                <edges>
                  <edge vertexkey="84516616" edgekey="84550056"/>
                </edges>
              </vertex>
              <vertex vertexkey="84483760">
                <edges>
                  <edge vertexkey="84516888" edgekey="84549504"/>
                </edges>
              </vertex>
              <vertex vertexkey="84483864">
                <edges>
                  <edge vertexkey="84515840" edgekey="84549688"/>
                </edges>
              </vertex>
              <vertex vertexkey="84484056">
                <edges>
                  <edge vertexkey="84522520" edgekey="84554288"/>
                </edges>
              </vertex>
              <vertex vertexkey="84484160">
                <edges>
                  <edge vertexkey="84522792" edgekey="84554472"/>
                </edges>
              </vertex>
              <vertex vertexkey="84484352">
                <edges>
                  <edge vertexkey="84523064" edgekey="84554656"/>
                </edges>
              </vertex>
              <vertex vertexkey="84484456">
                <edges>
                  <edge vertexkey="84523328" edgekey="84554840"/>
                </edges>
              </vertex>
              <vertex vertexkey="84484560">
                <edges>
                  <edge vertexkey="84523512" edgekey="84555024"/>
                </edges>
              </vertex>
              <vertex vertexkey="84484752">
                <edges>
                  <edge vertexkey="84523800" edgekey="84555208"/>
                </edges>
              </vertex>
              <vertex vertexkey="84484912">
                <edges>
                  <edge vertexkey="84524040" edgekey="84555392"/>
                </edges>
              </vertex>
            </vertices>
          </graph>
        </structure>
      </component>
    </mapping>
  </xsl:template>
</xsl:stylesheet>
