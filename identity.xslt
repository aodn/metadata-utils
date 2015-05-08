<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="2.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:mcp="http://schemas.aodn.org.au/mcp-2.0"
  xmlns:gco="http://www.isotc211.org/2005/gco"
  xmlns:gmd="http://www.isotc211.org/2005/gmd"
  xmlns:gmx="http://www.isotc211.org/2005/gmx"
  xmlns:geonet="http://www.fao.org/geonetwork"

  exclude-result-prefixes="xsl mcp gco gmd gmx geonet"
>

    <xsl:output method="xml" indent="yes"/>

    <!-- xsl:template match="/root/context/isharvested">
      <xsl:choose>
          <xsl:when test="matches(. ,'n')">

            WHOOT
              <xsl:copy>
                  <xsl:apply-templates select="@*|node()"/>
              </xsl:copy>


          </xsl:when>

          <xsl:otherwise>
          </xsl:otherwise>
      </xsl:choose>
    </xsl:template -->

    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>


  	<xsl:template match="mcp:MD_Metadata/gmd:contact">

      <xsl:choose>
        <xsl:when test="matches( /root/context/isharvested, 'n' )">
        <!-- xsl:when test="1=1"-->
            WHOOT1
        </xsl:when>
        <xsl:otherwise>
            WHOOT2
        </xsl:otherwise>
      </xsl:choose>

	</xsl:template>






</xsl:stylesheet>

