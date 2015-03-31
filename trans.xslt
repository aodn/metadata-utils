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

	<xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
 
     <xsl:template match="gmd:contact">  
		<whoot>sometext </whoot>
    </xsl:template>

     <xsl:template match="gmd:identificationInfo/mcp:MD_DataIdentification/gmd:resourceConstraints/mcp:MD_Commons/mcp:attributionConstraints/gco:CharacterString[matches(.,'Data was sourced')]">  
		<whoot>sometext2</whoot>
    </xsl:template>


</xsl:stylesheet>
