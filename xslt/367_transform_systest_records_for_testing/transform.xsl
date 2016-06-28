<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:gco="http://www.isotc211.org/2005/gco"
    xmlns:gmd="http://www.isotc211.org/2005/gmd"
    xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp"
    exclude-result-prefixes="mcp"
    version="2.0">
    
    <!-- url substitutions to be performed -->

    <xsl:variable name="urlSubstitutions">
        <substitution match="https?://geoserver-123.aodn.org.au(:443)?" replaceWith="http://geoserver-systest.aodn.org.au"/>
        <substitution match="https?://catalogue-imos.aodn.org.au(:443)?" replaceWith="http://catalogue-systest.aodn.org.au"/>
        <substitution match="https?://thredds.aodn.org.au(:443)?" replaceWith="http://thredds-systest.aodn.org.au"/>
    </xsl:variable>

    <xsl:variable name="urlSubstitutionSelector" select="string-join($urlSubstitutions/substitution/@match, '|')"/>

    <!-- default action is to copy -->

    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

    <!-- upgrade gogoduck V1 to gogoduck v2 -->

    <xsl:template priority="10" match="gmd:onLine/gmd:CI_OnlineResource[gmd:protocol/gco:CharacterString/text()='IMOS:AGGREGATION--gogoduck']/gmd:linkage/gmd:URL">
        <gmd:URL>http://geoserver-wps-systest.aodn.org.au/geoserver/wps</gmd:URL>
    </xsl:template>

    <xsl:template match="gco:CharacterString/text()[.='IMOS:AGGREGATION--gogoduck']">OGC:WPS--gogoduck</xsl:template>
    
    <!-- perform substitutions on any remaining matching url's -->

    <xsl:template match="gmd:URL[matches(., $urlSubstitutionSelector)]">
        <xsl:variable name="url" select="."/>

        <xsl:for-each select="$urlSubstitutions/substitution">
            <xsl:if test="matches(string($url), string(@match))">
                <gmd:URL><xsl:value-of select="replace($url, string(@match), string(@replaceWith))"/></gmd:URL>
            </xsl:if>
        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>
