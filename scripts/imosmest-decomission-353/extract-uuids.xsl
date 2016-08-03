<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="2.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:geonet="http://www.fao.org/geonetwork"
>
  <xsl:output method="text" />

  <!-- extract the returned uuid -->
  <xsl:template match="/response/metadata/geonet:info/uuid">
      <xsl:copy-of select="text()"/>
      <xsl:text>&#xa;</xsl:text>
  </xsl:template>

  <!-- discard other nodes -->
  <xsl:template match="metadata|geonet:info|metadatacreationdate|id|schema|createDate|changeDate|source|score|category|summary|keywords|keyword" >
      <xsl:apply-templates select="*" />
  </xsl:template>

  <!-- match -->
  <xsl:template match="@*|node()">
      <xsl:copy>
          <xsl:apply-templates select="@*|node()"/>
      </xsl:copy>
  </xsl:template>

</xsl:stylesheet>

