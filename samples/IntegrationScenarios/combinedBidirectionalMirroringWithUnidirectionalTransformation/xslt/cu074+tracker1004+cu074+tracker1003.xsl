<?xml version="1.0" encoding="UTF-8"?>

<!-- Insert your code here to make this scenario more realistic -->
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns="" exclude-result-prefixes="xsl xs fn">
	<xsl:template match="/node()">
		<xsl:copy-of select='.' />
	</xsl:template>
</xsl:stylesheet>