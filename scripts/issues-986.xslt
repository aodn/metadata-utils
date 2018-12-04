<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:gco="http://www.isotc211.org/2005/gco" xmlns:gmd="http://www.isotc211.org/2005/gmd"
                xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp" exclude-result-prefixes="mcp" version="2.0">

    <!--

    https://github.com/aodn/backlog/issues/986

-->
    <xsl:template match="/">
      <xsl:choose> 
        <xsl:when test="exists(//gmd:CI_OnlineResource[gmd:protocol/*/text()='WWW:LINK-1.0-http--metadata-URL' and matches(gmd:linkage/*/text(), 'catalogue-imos.aodn.org.au')])">
          <xsl:apply-templates select="." mode="imosRecord"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:apply-templates select="." mode="other"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:template>   

    <!-- list of collections to exclude from WMS filter checking -->
    <xsl:variable name="excludedCollections">
        <collection match="default_excluded_layer"/>
    </xsl:variable>

    <!-- default action is to copy -->
    <xsl:template mode="imosRecord" match="@* | node()">
        <xsl:copy>
            <xsl:apply-templates mode="imosRecord" select="@* | node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template mode="other" match="@* | node()">
        <xsl:copy>
            <xsl:apply-templates mode="other" select="@* | node()"/>
        </xsl:copy>
    </xsl:template>


    <!-- substitute all old IMOS portal URL with prod portal URLs aodn/content#368 -->
    <xsl:template mode="imosRecord" match="gmd:URL[matches(., 'imos.aodn.org.au/imos123*')]">
        <gmd:URL><xsl:value-of select="replace(., 'imos.aodn.org.au/imos123/home', 'portal.aodn.org.au/search')"/></gmd:URL>
    </xsl:template>

    <!--replace imos.aodn.org.au/imos123 found in f439db64-5551-15d1-e043-08114f8ce23c-->
    <xsl:template mode="imosRecord" match="gmd:dataSetURI/gco:CharacterString[matches(., 'imos.aodn.org.au/imos123')]">
        <gco:CharacterString><xsl:value-of select="replace(., 'imos.aodn.org.au/imos123', 'portal.aodn.org.au')"/></gco:CharacterString>
    </xsl:template>

    <!-- substitute unwanted portal link descriptions aodn/content#368 -->
    <xsl:variable name="unwantedPortalLink" select="string('View and download this data through the interactive AODN Portal')"></xsl:variable>
    <xsl:template mode="imosRecord" match="gmd:description/gco:CharacterString[matches(., $unwantedPortalLink)]" priority="2">
        <gco:CharacterString><xsl:value-of select="replace(., $unwantedPortalLink, 'View and download data though the AODN Portal')"/></gco:CharacterString>
    </xsl:template>

    <!-- BODACC links aodn/content/issues/353-->
    <xsl:variable name="unwantedBODACCWinDash" select="string('The BODAAC is a WFS service that returns a list of OpenDAP URLs matching a query.')"></xsl:variable>
    <xsl:template mode="imosRecord" match="gmd:CI_OnlineResource/gmd:description/gco:CharacterString[../../gmd:protocol/*/text()='IMOS:AGGREGATION—bodaac']" >
        <gco:CharacterString><xsl:value-of select="replace(., $unwantedBODACCWinDash, 'The ncUrlList is a WFS service that returns a list of URLs matching a query.')"/></gco:CharacterString>
    </xsl:template>

    <!-- BODACC links aodn/content/issues/353-->
    <xsl:variable name="unwantedBODACC" select="string('The BODAAC is a WFS service that returns a list of OpenDAP URLs matching a query.')"></xsl:variable>
    <xsl:template mode="imosRecord" match="gmd:CI_OnlineResource/gmd:description/gco:CharacterString[../../gmd:protocol/*/text()='IMOS:AGGREGATION--bodaac']" >
        <gco:CharacterString><xsl:value-of select="replace(., $unwantedBODACC, 'The ncUrlList is a WFS service that returns a list of URLs matching a query.')"/></gco:CharacterString>
    </xsl:template>

    <!-- BODACC links aodn/content/issues#353-->
    <xsl:template mode="imosRecord" match="gmd:CI_OnlineResource/gmd:description/gco:CharacterString[../../gmd:protocol/*/text()='WWW:LINK-1.0-http--link']" priority="3">
        <gco:CharacterString><xsl:value-of select="replace(., 'BODAAC help documentation', 'ncUrlList help documentation')"/></gco:CharacterString>
    </xsl:template>

    <!-- update phone numbers for imos collection-level records -->
    <xsl:template mode="imosRecord" match="gmd:citedResponsibleParty/gmd:CI_ResponsibleParty[gmd:organisationName/gco:CharacterString/text() = 'Integrated Marine Observing System (IMOS)']//gmd:voice/gco:CharacterString">
      <gco:CharacterString>61 3 6226 7549</gco:CharacterString>
    </xsl:template>

    <xsl:template mode="imosRecord" match="gmd:thesaurusName[gmd:CI_Citation/gmd:title/gco:CharacterString = 'water-bodies (internal use)' or gmd:CI_Citation/gmd:title/gco:CharacterString = 'land-masses (internal use)']">
        <gmd:thesaurusName>
            <gmd:CI_Citation>
                <gmd:title>
                    <gco:CharacterString>AODN Geographic Extents Vocabulary</gco:CharacterString>
                </gmd:title>
                <gmd:date>
                    <gmd:CI_Date>
                        <gmd:date>
                            <gco:Date>2017-06-15</gco:Date>
                        </gmd:date>
                        <gmd:dateType>
                            <gmd:CI_DateTypeCode codeList="http://schemas.aodn.org.au/mcp-2.0/schema/resources/Codelist/gmxCodelists.xml#CI_DateTypeCode" codeListValue="revision">revision</gmd:CI_DateTypeCode>
                        </gmd:dateType>
                    </gmd:CI_Date>
                </gmd:date>
                <gmd:otherCitationDetails>
                    <gco:CharacterString>https://vocabs.ands.org.au/aodn-geographic-extents-vocabulary</gco:CharacterString>
                </gmd:otherCitationDetails>
            </gmd:CI_Citation>
        </gmd:thesaurusName>
    </xsl:template>
</xsl:stylesheet>
