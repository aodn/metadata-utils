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

    <!-- upgrade gogoduck V1 to gogoduck v2 (excluding CARS) -->

    <xsl:template priority="10" 
        match="gmd:CI_OnlineResource[gmd:protocol/*/text()='IMOS:AGGREGATION--gogoduck' and not(starts-with(gmd:name/*/text(), 'cars_'))]/*/gmd:URL">
        <xsl:element name="gmd:URL">
            <xsl:text>http://geoserver-wps-systest.aodn.org.au/geoserver/wps</xsl:text>
        </xsl:element>
    </xsl:template>
    
    <xsl:template 
        match="gmd:CI_OnlineResource[not(starts-with(gmd:name/*/text(), 'cars_'))]/gmd:protocol/*/text()[.='IMOS:AGGREGATION--gogoduck']">
        <xsl:text>OGC:WPS--gogoduck</xsl:text>
    </xsl:template>

    <!-- add netcdf subset service option to ANMN current velocity time series if not there already -->

    <xsl:template match="gmd:MD_DigitalTransferOptions[//gmd:fileIdentifier/*/text() = '7e13b5f3-4a70-4e31-9e95-335efa491c5c' and not(.//gmd:protocol/*/text()='OGC:WPS--netcdf-subset-service')]">
        <xsl:copy xml:space="preserve"><xsl:apply-templates/>  <gmd:onLine>
            <gmd:CI_OnlineResource>
              <gmd:linkage>
                <gmd:URL>http://geoserver-wps-systest.aodn.org.au/geoserver/wps</gmd:URL>
              </gmd:linkage>
              <gmd:protocol>
                <gco:CharacterString>OGC:WPS--netcdf-subset-service</gco:CharacterString>
              </gmd:protocol>
              <gmd:name>
                <gco:CharacterString>imos:anmn_ts_timeseries_data</gco:CharacterString>
              </gmd:name>
              <gmd:description>
                <gco:CharacterString>This is a WPS service that returns a zip file of subsetted NetCDF files matching a query.</gco:CharacterString>
              </gmd:description>
            </gmd:CI_OnlineResource>
          </gmd:onLine>
        </xsl:copy>
    </xsl:template>

    <!-- substitute production service endpoints with systest service endpoints -->

    <xsl:template match="gmd:URL[matches(., $urlSubstitutionSelector)]">
        <xsl:variable name="url" select="."/>

        <xsl:for-each select="$urlSubstitutions/substitution">
            <xsl:if test="matches(string($url), string(@match))">
                <gmd:URL><xsl:value-of select="replace($url, string(@match), string(@replaceWith))"/></gmd:URL>
            </xsl:if>
        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>
