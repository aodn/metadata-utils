<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:gco="http://www.isotc211.org/2005/gco" xmlns:gmd="http://www.isotc211.org/2005/gmd"
                xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp" exclude-result-prefixes="mcp" version="2.0">

    <!--

    https://github.com/aodn/backlog/issues/986

-->
    <!-- list of collections to exclude from WMS filter checking -->
    <xsl:variable name="excludedCollections">
        <collection match="default_excluded_layer"/>
    </xsl:variable>

    <!-- default action is to copy -->
    <xsl:template match="@* | node()">
        <xsl:copy>
            <xsl:apply-templates select="@* | node()"/>
        </xsl:copy>
    </xsl:template>

    <!-- substitute all old IMOS portal URL with prod portal URLs aodn/content#368 -->
    <xsl:template match="gmd:URL[matches(., 'imos.aodn.org.au/imos123*')]">
        <gmd:URL><xsl:value-of select="replace(., 'imos.aodn.org.au/imos123/home', 'portal.aodn.org.au/search')"/></gmd:URL>
    </xsl:template>

    <!--replace imos.aodn.org.au/imos123 found in f439db64-5551-15d1-e043-08114f8ce23c-->
    <xsl:template match="gmd:dataSetURI/gco:CharacterString[matches(., 'imos.aodn.org.au/imos123')]">
        <gco:CharacterString><xsl:value-of select="replace(., 'imos.aodn.org.au/imos123', 'portal.aodn.org.au')"/></gco:CharacterString>
    </xsl:template>

    <!-- substitute unwanted portal link descriptions aodn/content#368 -->
    <xsl:variable name="unwantedPortalLink" select="string('View and download this data through the interactive AODN Portal')"></xsl:variable>
    <xsl:template match="gmd:description/gco:CharacterString[matches(., $unwantedPortalLink)]" priority="2">
        <gco:CharacterString><xsl:value-of select="replace(., $unwantedPortalLink, 'View and download data though the AODN Portal')"/></gco:CharacterString>
    </xsl:template>

    <!-- BODACC links aodn/content/issues/353-->
    <xsl:variable name="unwantedBODACCWinDash" select="string('The BODAAC is a WFS service that returns a list of OpenDAP URLs matching a query.')"></xsl:variable>
    <xsl:template match="gmd:CI_OnlineResource/gmd:description/gco:CharacterString[../../gmd:protocol/*/text()='IMOS:AGGREGATION—bodaac']" >
        <gco:CharacterString><xsl:value-of select="replace(., $unwantedBODACCWinDash, 'The ncUrlList is a WFS service that returns a list of URLs matching a query.')"/></gco:CharacterString>
    </xsl:template>

    <!-- BODACC links aodn/content/issues/353-->
    <xsl:variable name="unwantedBODACC" select="string('The BODAAC is a WFS service that returns a list of OpenDAP URLs matching a query.')"></xsl:variable>
    <xsl:template match="gmd:CI_OnlineResource/gmd:description/gco:CharacterString[../../gmd:protocol/*/text()='IMOS:AGGREGATION--bodaac']" >
        <gco:CharacterString><xsl:value-of select="replace(., $unwantedBODACC, 'The ncUrlList is a WFS service that returns a list of URLs matching a query.')"/></gco:CharacterString>
    </xsl:template>

    <!-- BODACC links aodn/content/issues#353-->
    <xsl:template match="gmd:CI_OnlineResource/gmd:description/gco:CharacterString[../../gmd:protocol/*/text()='WWW:LINK-1.0-http--link']" priority="3">
        <gco:CharacterString><xsl:value-of select="replace(., 'BODAAC help documentation', 'ncUrlList help documentation')"/></gco:CharacterString>
    </xsl:template>

    <!--Update all IMOS phone numbers-->
    <xsl:template match="gmd:voice[gco:CharacterString = '61 3 6226 7488']">
        <gmd:voice>
        <xsl:element name="gco:CharacterString">
            <xsl:value-of select='string("61 3 6226 7549")'/>
        </xsl:element>
        </gmd:voice>
    </xsl:template>

</xsl:stylesheet>