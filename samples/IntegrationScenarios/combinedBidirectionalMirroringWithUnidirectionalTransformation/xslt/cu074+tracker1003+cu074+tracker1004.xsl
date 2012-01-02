<?xml version="1.0" encoding="UTF-8"?>

<!--
 Copyright 2009 CollabNet, Inc. ("CollabNet")

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
-->


<!-- Insert your code here to make this scenario more realistic -->
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns="" exclude-result-prefixes="xsl xs fn ccf stringutil">
	<xsl:template match="/node()">
		<xsl:copy-of select='.' />
	</xsl:template>
</xsl:stylesheet>