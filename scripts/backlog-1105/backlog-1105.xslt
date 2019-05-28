<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp"
                xmlns="http://www.isotc211.org/2005/gmd"
                xmlns:gmd="http://www.isotc211.org/2005/gmd"
                xmlns:gco="http://www.isotc211.org/2005/gco"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                gco:isoType="gmd:MD_Metadata"
                xsi:schemaLocation="http://bluenet3.antcrc.utas.edu.au/mcp http://bluenet3.antcrc.utas.edu.au/mcp-1.5-experimental/schema.xsd http://www.isotc211.org/2005/srv http://schemas.opengis.net/iso/19139/20060504/srv/srv.xsd http://www.isotc211.org/2005/gmx http://www.isotc211.org/2005/gmx/gmx.xsd">

    <xsl:output method="xml" indent="yes" encoding="UTF-8" />

    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template name="string-replace">
        <xsl:param name="string" />
        <xsl:param name="replace" />
        <xsl:param name="with" />
        <xsl:choose>
            <xsl:when test="contains($string, $replace)">
                <xsl:value-of select="substring-before($string, $replace)" />
                <xsl:value-of select="$with" />
                <xsl:call-template name="string-replace">
                    <xsl:with-param name="string" select="substring-after($string,$replace)" />
                    <xsl:with-param name="replace" select="$replace" />
                    <xsl:with-param name="with" select="$with" />
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$string" />
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="gmd:URL[contains(text(),'geoserver.imos.org.au')]" >
        <xsl:variable name="string-mod">
            <xsl:call-template name="string-replace">
                <xsl:with-param name="string" select="text()" />
                <xsl:with-param name="replace" select="'geoserver.imos.org.au'" />
                <xsl:with-param name="with" select="'geoserver-123.aodn.org.au'" />
            </xsl:call-template>
        </xsl:variable>
        <xsl:copy>
            <xsl:value-of select="$string-mod"/>
        </xsl:copy>
    </xsl:template>




</xsl:stylesheet>