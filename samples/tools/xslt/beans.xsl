<?xml version="1.0"?>
<!--
    [[
    Copyright (C) 2006,2007 The Software Conservancy as Trustee. All rights
    reserved.

    Permission is hereby granted, free of charge, to any person obtaining a
    copy of this software and associated documentation files (the
    "Software"), to deal in the Software without restriction, including
    without limitation the rights to use, copy, modify, merge, publish,
    distribute, sublicense, and/or sell copies of the Software, and to
    permit persons to whom the Software is furnished to do so, subject to
    the following conditions:

    The above copyright notice and this permission notice shall be included
    in all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
    OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
    MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
    NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
    LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
    OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
    WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

    Nothing in this notice shall be deemed to grant any rights to
    trademarks, copyrights, patents, trade secrets or any other intellectual
    property of the licensor or any contributor except as expressly stated
    herein. No patent license is granted separate from the Software, for
    code that you delete from the Software, or for combinations of the
    Software with other software or hardware.
    ]]

    $HeadURL$

    @author Andrew Shire

    Autogenerates detailed documentation for an OA3 XML file such as a cookbook example.
    (interprets Spring config with knowledge of key OA3 semantics).

    Uses "beans.xsl" to generate detailed documentation for each of the beans in the file.
  -->
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns="http://www.w3.org/TR/xhtml1/strict"
                xmlns:beans="http://www.springframework.org/schema/beans">

<xsl:import href="adaptorDoc.xsl"/>

<xsl:param name="oaVersion"/>
<xsl:param name="imageFileExtension" select="'gif'"/>
<!-- Which you have depends on which DNS name you used in your SVN checkout URL: -->
<xsl:param name="filepathGlobPrefix1" select="'HeadURL: https://www.openadaptor.org/svn/openadaptor3/trunk/example/'"/>
<xsl:param name="filepathGlobPrefix2" select="'HeadURL: https://openadaptor3.openadaptor.org/svn/openadaptor3/trunk/example/'"/>
<xsl:param name="filepathGlobPrefix3" select="'$HeadURL: https://openadaptor3.openadaptor.org/svn/openadaptor3/tags/3.4/example/'"/>
<xsl:param name="filepathGlobPrefix4" select="'some value2 defined specifically for your build environment'"/>
<xsl:param name="docsRelativeToTools" select="'../../docs/'"/>
<xsl:param name="xmlRelative" select="'../'"/>

<xsl:param name="showJavaDocLinks" select="'true'"/>
<xsl:param name="showConfigIndexLinks" select="'true'"/>
<xsl:param name="showOverviewLinks" select="'false'"/>
<!-- JavaDoc relative path or an http(s) URL: (must have trailing slash) -->
<xsl:param name="javaDocPath" select="'../../javadocs/'"/>


<xsl:template match="/">
<xsl:variable name="thisExample"
    select="concat(
      substring-before(substring-after(beans:beans/beans:description|comment(),$filepathGlobPrefix1),'.xml '),
      substring-before(substring-after(beans:beans/beans:description|comment(),$filepathGlobPrefix2),'.xml '),
      substring-before(substring-after(beans:beans/beans:description|comment(),$filepathGlobPrefix3),'.xml '),
      substring-before(substring-after(beans:beans/beans:description|comment(),$filepathGlobPrefix4),'.xml ')
    )"/> 

<xsl:variable name="baseRelativeDepth" select="string-length(translate($thisExample,'/abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ._-1234567890','/'))" />
<xsl:variable name="baseRelativeDotDot">
  <xsl:choose>
    <xsl:when test="$baseRelativeDepth = '1'">../</xsl:when>
    <xsl:when test="$baseRelativeDepth = '2'">../../</xsl:when>
    <xsl:when test="$baseRelativeDepth = '3'">../../../</xsl:when>
    <xsl:when test="$baseRelativeDepth = '4'">../../../../</xsl:when>
    <xsl:when test="$baseRelativeDepth = '5'">../../../../../</xsl:when>
    <xsl:when test="$baseRelativeDepth = '6'">../../../../../../</xsl:when>
    <xsl:when test="$baseRelativeDepth = '7'">../../../../../../../</xsl:when>
  </xsl:choose>
</xsl:variable>


<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-15"/>
    <style type="text/css">
        html { background: white }
        body {
            background: white;
            color: black;
            font-family: Arial, Helvetica, san-serif
        }
        td { text-align: left; vertical-align: top }
        th { text-align: left; vertical-align: top }
        a.th:link    {color: white; }
        a.th:visited {color: white; }
    </style>
    <title>Documentation: <xsl:value-of select="$thisExample"/></title>
  </head>

  <body>
    <table bgcolor="#000099" width="100%">
      <tr>
        <th>
          <font color="white">
            <xsl:value-of select="$thisExample"/>
            <xsl:if test="$showOverviewLinks='true'">
              <xsl:text> | </xsl:text>
              <a class="th" href="index.html">Overview</a>
            </xsl:if>
            <xsl:text> | </xsl:text>
            <a class="th" href="{$baseRelativeDotDot}{$xmlRelative}{$thisExample}.xml">XML</a>
            <xsl:if test="$showConfigIndexLinks='true'">
              <xsl:text> | </xsl:text>
              <a class="th" href="{$baseRelativeDotDot}config2beans.html#{translate(translate($thisExample,'_','-'),'/','_')}">ConfigToBeans</a>
              <xsl:text> | </xsl:text>
              <a class="th" href="{$baseRelativeDotDot}allimages.html#{translate(translate($thisExample,'_','-'),'/','_')}">ImagesIndex</a>
            </xsl:if>
          </font>
        </th>
      </tr>
      <tr bgcolor="#CCCCCC">
        <td>
          <xsl:value-of select="$oaVersion" />
        </td>
      </tr>
    </table>
    <p></p>

    <img src="{$baseRelativeDotDot}{$thisExample}.{$imageFileExtension}" usemap="#Map_{translate($thisExample,'/','_')}" alt=""/>
    <xsl:copy-of select="document(concat($docsRelativeToTools,$thisExample,'.map'))"/>

    <xsl:apply-templates select="/beans:beans">
      <xsl:with-param name="baseRelativeDotDot" select="$baseRelativeDotDot" />
      <xsl:with-param name="showJavaDocLinks" select="$showJavaDocLinks" />
      <xsl:with-param name="showConfigIndexLinks" select="$showConfigIndexLinks" />
      <xsl:with-param name="javaDocPath" select="$javaDocPath" />
    </xsl:apply-templates>

  </body>
</html>
</xsl:template>
</xsl:stylesheet>
