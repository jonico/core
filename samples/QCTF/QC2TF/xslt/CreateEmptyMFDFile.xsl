<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:template match="node()">
		<mapping xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="11">
			<component name="defaultmap1" blackbox="0" editable="1">
				<properties SelectedLanguage="xslt"/>
				<structure>
					<children>
						<component name="document" library="xml" uid="1" kind="14">
							<properties/>
							<view rbx="367" rby="1262"/>
							<data>
								<root scrollposition="1">
									<header>
										<namespaces>
											<namespace/>
										</namespaces>
									</header>
									<entry name="artifact" expanded="1"/>
								</root>
								<document schema="{@sourceSchemaName}" instanceroot="artifact"/>
								<wsdl/>
							</data>
						</component>
						<component name="document" library="xml" uid="2" kind="14">
							<properties/>
							<view ltx="557" rbx="936" rby="1263"/>
							<data>
								<root scrollposition="1">
									<header>
										<namespaces>
											<namespace/>
										</namespaces>
									</header>
									<entry name="artifact" expanded="1"/>
								</root>
								<document schema="{@targetSchemaName}" instanceroot="artifact"/>
								<wsdl/>
							</data>
						</component>
					</children>
					<graph directed="1">
						<vertices/>
					</graph>
				</structure>
			</component>
		</mapping>
	</xsl:template>
</xsl:stylesheet>
