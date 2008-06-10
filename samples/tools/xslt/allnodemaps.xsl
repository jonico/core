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
  
  Produces a single HTML page containing all cookbook examples as
  embedded clickable node map images.
  This is used to generate docs/images/index.html.
  
  Related to "nodemap.xsl" which produces same output except for
  just one cookbook example.
  That is used to generate a separate page for every cookbook example.
-->
<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns="http://www.w3.org/TR/xhtml1/strict"
  xmlns:beans="http://www.springframework.org/schema/beans">

  <xsl:param name="oaVersion" />
  <xsl:param name="imageFileExtension" />
  <xsl:param name="docsRelativeToTools" select="'../../docs/'"/>

  <xsl:template match="/cookbook">
    <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en"
      lang="en">
      <head>
        <meta http-equiv="Content-Type"
          content="text/html; charset=iso-8859-15" />
        <style type="text/css">
          html { background: white }
          body { background: white; color: black; font-family: Arial, Helvetica, san-serif }
          td { text-align: left; vertical-align: top }
          th { text-align: left; vertical-align: top }
          a.th:link {color: white; }
          a.th:visited {color: white; }
        </style>
        <title>ImagesIndex</title>
      </head>

      <body>
      <table bgcolor="#000099" width="100%">
          <tr>
            <th>
              <font color="white">
                <a class="th" href="./config2beans.html">ConfigToBeans</a>
                <xsl:text> | </xsl:text>
                <a class="th" href="./beans2config.html">BeansToConfig</a>
                <xsl:text> | </xsl:text>
                <xsl:text>ImagesIndex</xsl:text>
              </font>
            </th>
          </tr>
          <tr bgcolor="#CCCCCC">
            <td>
              <xsl:value-of select="$oaVersion" />
            </td>
          </tr>
        </table>

        <xsl:for-each select="beans">
          <p><font color="white">.</font></p>
          <table border="1" bgcolor="#CCCCCC">
            <xsl:choose>
              <!-- Named cookbook example -->
              <xsl:when test="@id">
                <xsl:variable name="idAsRelativePath" select="translate(translate(@id,'_','/'),'-','_')" />

                <!-- Cookbook example name -->
                <tr bgcolor="#000099">
                  <th>
                    <font color="white">
                      <a class="th" name="{@id}">
                        <xsl:value-of select="$idAsRelativePath" />
                      </a>
                      <xsl:text> | </xsl:text>
                      <a class="th" href="./{$idAsRelativePath}.html">Documentation</a>
                      <xsl:text> | </xsl:text>
                      <a class="th" href="./{$idAsRelativePath}.xml">XML</a>
                      <xsl:text> | </xsl:text>
                      <a class="th" href="./config2beans.html#{@id}">ConfigToBeans</a>
                    </font>
                  </th>
                </tr>
                <!-- Cookbook example node map -->
                <tr bgcolor="#FFFFFF">
                  <td>
                    <img src="{$idAsRelativePath}.{$imageFileExtension}" usemap="#Map_{translate(@id, '-', '_')}" alt="" />
                    <xsl:copy-of select="document(concat($docsRelativeToTools,$idAsRelativePath,'.localmap'))" />
                  </td>
                </tr>
                <!-- Cookbook example description -->
                <!-- <tr bgcolor="#FFFFFF"> -->
                <!--   <td><pre><xsl:apply-templates select="description"/></pre></td> -->
                <!-- </tr> -->
              </xsl:when>
              <!-- Anonymous cookbook example ("Header" filename is missing from comment block)-->
              <xsl:otherwise>
                <tr bgcolor="#000099">
                  <td>
                    <font color="white">(config example missing name)</font>
                  </td>
                </tr>
              </xsl:otherwise>
            </xsl:choose>
          </table>
        </xsl:for-each>

      </body>
    </html>
  </xsl:template>
</xsl:stylesheet>
