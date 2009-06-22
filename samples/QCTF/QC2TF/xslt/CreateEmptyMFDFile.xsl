<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:template match="node()">
		<mapping xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			version="11">
			<component name="defaultmap1" blackbox="0" editable="1">
				<properties SelectedLanguage="xslt" />
				<structure>
					<children>
						<component name="document" library="xml" uid="1" kind="14">
							<properties />
							<view rbx="367" rby="1262" />
							<data>
								<root scrollposition="1">
									<header>
										<namespaces>
											<namespace />
										</namespaces>
									</header>
									<entry name="artifact" outkey="84194664" expanded="1">
										<entry name="topLevelAttributes" outkey="84194768">
											<entry name="artifactMode" type="attribute" outkey="84426832" />
											<entry name="artifactAction" type="attribute" outkey="84427016" />
											<entry name="sourceArtifactVersion" type="attribute"
												outkey="84427312" />
											<entry name="targetArtifactVersion" type="attribute"
												outkey="84427584" />
											<entry name="sourceArtifactLastModifiedDate" type="attribute"
												outkey="84427856" />
											<entry name="targetArtifactLastModifiedDate" type="attribute"
												outkey="84428256" />
											<entry name="conflictResolutionPriority" type="attribute"
												outkey="84428528" />
											<entry name="artifactType" type="attribute" outkey="84428712" />
											<entry name="sourceSystemKind" type="attribute" outkey="84428896" />
											<entry name="sourceSystemId" type="attribute" outkey="84429192" />
											<entry name="sourceRepositoryKind" type="attribute"
												outkey="84429392" />
											<entry name="sourceRepositoryId" type="attribute"
												outkey="84429632" />
											<entry name="sourceArtifactId" type="attribute" outkey="84429872" />
											<entry name="targetSystemKind" type="attribute" outkey="84430112" />
											<entry name="targetSystemId" type="attribute" outkey="84430352" />
											<entry name="targetRepositoryKind" type="attribute"
												outkey="84430592" />
											<entry name="targetRepositoryId" type="attribute"
												outkey="84430832" />
											<entry name="targetArtifactId" type="attribute" outkey="84431072" />
											<entry name="depParentSourceRepositoryKind" type="attribute"
												outkey="84431312" />
											<entry name="depParentSourceRepositoryId" type="attribute"
												outkey="84431584" />
											<entry name="depParentSourceArtifactId" type="attribute"
												outkey="84431856" />
											<entry name="depChildSourceRepositoryKind" type="attribute"
												outkey="84432128" />
											<entry name="depChildSourceRepositoryId" type="attribute"
												outkey="84432400" />
											<entry name="depChildSourceArtifactId" type="attribute"
												outkey="84432672" />
											<entry name="depParentTargetRepositoryKind" type="attribute"
												outkey="84432944" />
											<entry name="depParentTargetRepositoryId" type="attribute"
												outkey="84433216" />
											<entry name="depParentTargetArtifactId" type="attribute"
												outkey="84433488" />
											<entry name="depChildTargetRepositoryKind" type="attribute"
												outkey="84433760" />
											<entry name="depChildTargetRepositoryId" type="attribute"
												outkey="84434032" />
											<entry name="depChildTargetArtifactId" type="attribute"
												outkey="84434304" />
											<entry name="errorCode" type="attribute" outkey="84434576" />
											<entry name="transactionId" type="attribute" outkey="84434800" />
											<entry name="includesFieldMetaData" type="attribute"
												outkey="84434992" />
											<entry name="sourceSystemTimezone" type="attribute"
												outkey="84435320" />
											<entry name="targetSystemTimezone" type="attribute"
												outkey="84435560" />
										</entry>
									</entry>
								</root>
								<document schema="{@sourceSchemaName}" instanceroot="artifact" />
								<wsdl />
							</data>
						</component>
						<component name="document" library="xml" uid="2" kind="14">
							<properties />
							<view ltx="557" rbx="936" rby="1263" />
							<data>
								<root scrollposition="1">
									<header>
										<namespaces>
											<namespace />
										</namespaces>
									</header>
									<entry name="artifact" inpkey="84201448" expanded="1">
										<entry name="topLevelAttributes" inpkey="84201776">
											<entry name="artifactMode" type="attribute" inpkey="84436080" />
											<entry name="artifactAction" type="attribute" inpkey="84436264" />
											<entry name="sourceArtifactVersion" type="attribute"
												inpkey="84436560" />
											<entry name="targetArtifactVersion" type="attribute"
												inpkey="84436832" />
											<entry name="sourceArtifactLastModifiedDate" type="attribute"
												inpkey="84437104" />
											<entry name="targetArtifactLastModifiedDate" type="attribute"
												inpkey="84437504" />
											<entry name="conflictResolutionPriority" type="attribute"
												inpkey="84437776" />
											<entry name="artifactType" type="attribute" inpkey="84437960" />
											<entry name="sourceSystemKind" type="attribute" inpkey="84438144" />
											<entry name="sourceSystemId" type="attribute" inpkey="84438440" />
											<entry name="sourceRepositoryKind" type="attribute"
												inpkey="84438640" />
											<entry name="sourceRepositoryId" type="attribute"
												inpkey="84438880" />
											<entry name="sourceArtifactId" type="attribute" inpkey="84439120" />
											<entry name="targetSystemKind" type="attribute" inpkey="84439360" />
											<entry name="targetSystemId" type="attribute" inpkey="84439600" />
											<entry name="targetRepositoryKind" type="attribute"
												inpkey="84439840" />
											<entry name="targetRepositoryId" type="attribute"
												inpkey="84440080" />
											<entry name="targetArtifactId" type="attribute" inpkey="84440320" />
											<entry name="depParentSourceRepositoryKind" type="attribute"
												inpkey="84440560" />
											<entry name="depParentSourceRepositoryId" type="attribute"
												inpkey="84440832" />
											<entry name="depParentSourceArtifactId" type="attribute"
												inpkey="84441104" />
											<entry name="depChildSourceRepositoryKind" type="attribute"
												inpkey="84441376" />
											<entry name="depChildSourceRepositoryId" type="attribute"
												inpkey="84441648" />
											<entry name="depChildSourceArtifactId" type="attribute"
												inpkey="84441920" />
											<entry name="depParentTargetRepositoryKind" type="attribute"
												inpkey="84442192" />
											<entry name="depParentTargetRepositoryId" type="attribute"
												inpkey="84442464" />
											<entry name="depParentTargetArtifactId" type="attribute"
												inpkey="84442736" />
											<entry name="depChildTargetRepositoryKind" type="attribute"
												inpkey="84443008" />
											<entry name="depChildTargetRepositoryId" type="attribute"
												inpkey="84443280" />
											<entry name="depChildTargetArtifactId" type="attribute"
												inpkey="84443552" />
											<entry name="errorCode" type="attribute" inpkey="84443824" />
											<entry name="transactionId" type="attribute" inpkey="84444048" />
											<entry name="includesFieldMetaData" type="attribute"
												inpkey="84444232" />
											<entry name="sourceSystemTimezone" type="attribute"
												inpkey="84444560" />
											<entry name="targetSystemTimezone" type="attribute"
												inpkey="84444800" />
										</entry>
									</entry>
								</root>
								<document schema="{@targetSchemaName}" instanceroot="artifact" />
								<wsdl />
							</data>
						</component>
					</children>
					<graph directed="1">
						<edges />
						<vertices>
							<vertex vertexkey="84194664">
								<edges>
									<edge vertexkey="84201448" edgekey="73898248" />
								</edges>
							</vertex>
							<vertex vertexkey="84194768">
								<edges>
									<edge vertexkey="84201776" edgekey="73871424" />
								</edges>
							</vertex>
							<vertex vertexkey="84426832">
								<edges>
									<edge vertexkey="84436080" edgekey="84454872" />
								</edges>
							</vertex>
							<vertex vertexkey="84427016">
								<edges>
									<edge vertexkey="84436264" edgekey="84457792" />
								</edges>
							</vertex>
							<vertex vertexkey="84427312">
								<edges>
									<edge vertexkey="84436560" edgekey="84454928" />
								</edges>
							</vertex>
							<vertex vertexkey="84427584">
								<edges>
									<edge vertexkey="84436832" edgekey="84455072" />
								</edges>
							</vertex>
							<vertex vertexkey="84427856">
								<edges>
									<edge vertexkey="84437104" edgekey="84457904" />
								</edges>
							</vertex>
							<vertex vertexkey="84428256">
								<edges>
									<edge vertexkey="84437504" edgekey="84460792" />
								</edges>
							</vertex>
							<vertex vertexkey="84428528">
								<edges>
									<edge vertexkey="84437776" edgekey="84460848" />
								</edges>
							</vertex>
							<vertex vertexkey="84428712">
								<edges>
									<edge vertexkey="84437960" edgekey="84462512" />
								</edges>
							</vertex>
							<vertex vertexkey="84428896">
								<edges>
									<edge vertexkey="84438144" edgekey="84459496" />
								</edges>
							</vertex>
							<vertex vertexkey="84429192">
								<edges>
									<edge vertexkey="84438440" edgekey="84464944" />
								</edges>
							</vertex>
							<vertex vertexkey="84429392">
								<edges>
									<edge vertexkey="84438640" edgekey="84455768" />
								</edges>
							</vertex>
							<vertex vertexkey="84429632">
								<edges>
									<edge vertexkey="84438880" edgekey="84455824" />
								</edges>
							</vertex>
							<vertex vertexkey="84429872">
								<edges>
									<edge vertexkey="84439120" edgekey="84455920" />
								</edges>
							</vertex>
							<vertex vertexkey="84430112">
								<edges>
									<edge vertexkey="84439360" edgekey="84456016" />
								</edges>
							</vertex>
							<vertex vertexkey="84430352">
								<edges>
									<edge vertexkey="84439600" edgekey="84456112" />
								</edges>
							</vertex>
							<vertex vertexkey="84430592">
								<edges>
									<edge vertexkey="84439840" edgekey="84456208" />
								</edges>
							</vertex>
							<vertex vertexkey="84430832">
								<edges>
									<edge vertexkey="84440080" edgekey="84456304" />
								</edges>
							</vertex>
							<vertex vertexkey="84431072">
								<edges>
									<edge vertexkey="84440320" edgekey="84456400" />
								</edges>
							</vertex>
							<vertex vertexkey="84431312">
								<edges>
									<edge vertexkey="84440560" edgekey="84456496" />
								</edges>
							</vertex>
							<vertex vertexkey="84431584">
								<edges>
									<edge vertexkey="84440832" edgekey="84480424" />
								</edges>
							</vertex>
							<vertex vertexkey="84431856">
								<edges>
									<edge vertexkey="84441104" edgekey="84479376" />
								</edges>
							</vertex>
							<vertex vertexkey="84432128">
								<edges>
									<edge vertexkey="84441376" edgekey="84480656" />
								</edges>
							</vertex>
							<vertex vertexkey="84432400">
								<edges>
									<edge vertexkey="84441648" edgekey="84480312" />
								</edges>
							</vertex>
							<vertex vertexkey="84432672">
								<edges>
									<edge vertexkey="84441920" edgekey="84483096" />
								</edges>
							</vertex>
							<vertex vertexkey="84432944">
								<edges>
									<edge vertexkey="84442192" edgekey="84484440" />
								</edges>
							</vertex>
							<vertex vertexkey="84433216">
								<edges>
									<edge vertexkey="84442464" edgekey="84485784" />
								</edges>
							</vertex>
							<vertex vertexkey="84433488">
								<edges>
									<edge vertexkey="84442736" edgekey="84487128" />
								</edges>
							</vertex>
							<vertex vertexkey="84433760">
								<edges>
									<edge vertexkey="84443008" edgekey="84488472" />
								</edges>
							</vertex>
							<vertex vertexkey="84434032">
								<edges>
									<edge vertexkey="84443280" edgekey="84489816" />
								</edges>
							</vertex>
							<vertex vertexkey="84434304">
								<edges>
									<edge vertexkey="84443552" edgekey="84491160" />
								</edges>
							</vertex>
							<vertex vertexkey="84434576">
								<edges>
									<edge vertexkey="84443824" edgekey="84492624" />
								</edges>
							</vertex>
							<vertex vertexkey="84434800">
								<edges>
									<edge vertexkey="84444048" edgekey="84494136" />
								</edges>
							</vertex>
							<vertex vertexkey="84434992">
								<edges>
									<edge vertexkey="84444232" edgekey="84497160" />
								</edges>
							</vertex>
							<vertex vertexkey="84435320">
								<edges>
									<edge vertexkey="84444560" edgekey="84495648" />
								</edges>
							</vertex>
							<vertex vertexkey="84435560">
								<edges>
									<edge vertexkey="84444800" edgekey="84457368" />
								</edges>
							</vertex>
						</vertices>
					</graph>
				</structure>
			</component>
		</mapping>
	</xsl:template>
</xsl:stylesheet>