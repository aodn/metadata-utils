<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:gco="http://www.isotc211.org/2005/gco"
    xmlns:gmd="http://www.isotc211.org/2005/gmd"
    xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp"
    exclude-result-prefixes="mcp"
    version="2.0">
    
    <!-- url substitutions to be performed -->

    <xsl:variable name="urlSubstitutionSelector" select="string-join($urlSubstitutions/substitution/@match, '|')"/>

    <!-- default action is to copy -->

    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

    <!-- upgrade gogoduck V1 to gogoduck v2 (excluding CARS) -->

    <xsl:template priority="10" 
        match="gmd:CI_OnlineResource[gmd:protocol/*/text()='IMOS:AGGREGATION--gogoduck' and not(starts-with(gmd:name/*/text(), 'cars_'))]/*/gmd:URL">
        <xsl:element name="gmd:URL">
            <xsl:text>http://geoserver-wps.aodn.org.au/geoserver/wps</xsl:text>
        </xsl:element>
    </xsl:template>
    
    <xsl:template 
        match="gmd:CI_OnlineResource[not(starts-with(gmd:name/*/text(), 'cars_'))]/gmd:protocol/*/text()[.='IMOS:AGGREGATION--gogoduck']">
        <xsl:text>OGC:WPS--gogoduck</xsl:text>
    </xsl:template>

    <!-- add netcdf subset service option to ANMN current velocity time series if not there already -->

    <xsl:template match="gmd:MD_DigitalTransferOptions[//gmd:fileIdentifier/*/text() = 'ae86e2f5-eaaf-459e-a405-e654d85adb9c' and not(.//gmd:protocol/*/text()='OGC:WPS--netcdf-subset-service')]">
        <xsl:copy xml:space="preserve"><xsl:apply-templates/>  <gmd:onLine>
            <gmd:CI_OnlineResource>
              <gmd:linkage>
                <gmd:URL>http://geoserver-wps.aodn.org.au/geoserver/wps</gmd:URL>
              </gmd:linkage>
              <gmd:protocol>
                <gco:CharacterString>OGC:WPS--netcdf-subset-service</gco:CharacterString>
              </gmd:protocol>
              <gmd:name>
                <gco:CharacterString>imos:anmn_ts_timeseries_data</gco:CharacterString>
              </gmd:name>
              <gmd:description>
                <gco:CharacterString>Moorings - velocity time-series</gco:CharacterString>
              </gmd:description>
            </gmd:CI_OnlineResource>
          </gmd:onLine>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
