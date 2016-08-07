<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="2.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"

    xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp"

    xmlns:gco="http://www.isotc211.org/2005/gco"
    xmlns:gmd="http://www.isotc211.org/2005/gmd"
    xmlns:gmx="http://www.isotc211.org/2005/gmx"
    xmlns:geonet="http://www.fao.org/geonetwork"

    exclude-result-prefixes="xsl mcp gco gmd gmx geonet"
    >

    <xsl:output method="xml" indent="yes"/>

    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="gmd:distributionInfo//gmd:CI_OnlineResource[ gmd:linkage/gmd:URL[ matches( text(), 'http://imosmest\.(aodn|emii)\.org\.au')] ]/gmd:linkage/gmd:URL/text()">
      
        <!-- ORIGINAL<xsl:value-of select='.'/>
        <xsl:text>&#xa;</xsl:text> -->

        <xsl:value-of select='replace(., "http://imosmest\.(aodn|emii)\.org\.au:?[0-9]*/geonetwork/srv/en", "https://catalogue-imos.aodn.org.au/geonetwork/srv/eng")'/>

    </xsl:template>

</xsl:stylesheet>

