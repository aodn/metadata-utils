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

    <xsl:import href="xslt/geonetwork_update_backlogs_324/ciResponsibleParty.xsl"/>

    <xsl:template match="node()|@*">
        <xsl:copy>
            <xsl:apply-templates select="node()|@*"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="gmd:URL/text()[.='http://i.creativecommons.org/l/by/4.0/88x31.png']">https://licensebuttons.net/l/by/4.0/88x31.png</xsl:template>

    <xsl:template match="mcp:MD_Metadata/gmd:identificationInfo/mcp:MD_DataIdentification/gmd:pointOfContact">
        <xsl:choose>
            <xsl:when test="gmd:CI_ResponsibleParty/gmd:organisationName/gco:CharacterString/text()[.='Integrated Marine Observing System (IMOS)']">
                <gmd:pointOfContact>
                    <xsl:call-template name="ciResponsibleParty">
                        <xsl:with-param name="codeList" select="'http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#CI_RoleCode'" />
                        <xsl:with-param name="codeListValue" select="'pointOfContact'" />
                    </xsl:call-template>
                </gmd:pointOfContact>
                <!--<xsl:comment>Match!</xsl:comment>-->
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy-of select="."/>
                <!--<xsl:comment>No Match!</xsl:comment>-->
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="mcp:MD_Metadata/gmd:contact">
        <xsl:choose>
            <xsl:when test="contains(gmd:CI_ResponsibleParty/gmd:organisationName/gco:CharacterString/text(), 'Integrated Marine Observing System (IMOS)')">
                <gmd:contact>
                <xsl:call-template name="ciResponsibleParty">
                    <xsl:with-param name="codeList" select="'http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#CI_RoleCode'" />
                    <xsl:with-param name="codeListValue" select="'distributor'" />
                </xsl:call-template>
                </gmd:contact>
                <!--<xsl:comment>Match!</xsl:comment>-->
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy-of select="."/>
                <!--<xsl:comment>No Match!</xsl:comment>-->
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>


