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


    <xsl:variable name="aodnContact">
        <gmd:contact>
            <gmd:CI_ResponsibleParty>
                <gmd:organisationName>
                    <gco:CharacterString>Integrated Marine Observing System (IMOS)</gco:CharacterString>
                </gmd:organisationName>
                <gmd:positionName>
                    <gco:CharacterString>Data Officer</gco:CharacterString>
                </gmd:positionName>
                <xsl:import href="xslt/geonetwork_update_backlogs_324/aodnContact.xsl"/>
                <gmd:role>
                    <gmd:CI_RoleCode
                            codeList="http://schemas.aodn.org.au/mcp-2.0/schema/resources/Codelist/gmxCodelists.xml#CI_RoleCode"
                            codeListValue="distributor">distributor
                    </gmd:CI_RoleCode>
                </gmd:role>
            </gmd:CI_ResponsibleParty>
        </gmd:contact>
    </xsl:variable>

    <xsl:variable name="aodnPointOfContact">
        <gmd:pointOfContact>
            <gmd:CI_ResponsibleParty>
                <gmd:organisationName>
                    <gco:CharacterString>Integrated Marine Observing System (IMOS)</gco:CharacterString>
                </gmd:organisationName>
                <gmd:positionName>
                    <gco:CharacterString>Data Officer</gco:CharacterString>
                </gmd:positionName>
                <xsl:import href="xslt/geonetwork_update_backlogs_324/aodnPointOfContact.xsl"/>
                <gmd:role>
                    <gmd:CI_RoleCode
                            codeList="http://schemas.aodn.org.au/mcp-2.0/schema/resources/Codelist/gmxCodelists.xml#CI_RoleCode"
                            codeListValue="pointOfContact">pointOfContact
                    </gmd:CI_RoleCode>
                </gmd:role>
            </gmd:CI_ResponsibleParty>
        </gmd:pointOfContact>
    </xsl:variable>

    <xsl:template match="node()|@*">
        <xsl:copy>
            <xsl:apply-templates select="node()|@*"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="gmd:URL/text()[.='http://i.creativecommons.org/l/by/4.0/88x31.png']">https://licensebuttons.net/l/by/4.0/88x31.png</xsl:template>

    <xsl:template match="mcp:MD_Metadata/gmd:identificationInfo/mcp:MD_DataIdentification/gmd:pointOfContact">
        <xsl:choose>
            <xsl:when test="gmd:CI_ResponsibleParty/gmd:organisationName/gco:CharacterString/text()[.='Integrated Marine Observing System (IMOS)']">
                <xsl:copy-of select="$aodnPointOfContact"/>
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
            <xsl:when test="gmd:CI_ResponsibleParty/gmd:organisationName/gco:CharacterString/text()[.='Integrated Marine Observing System (IMOS)']">
                <xsl:copy-of select="$aodnContact"/>
                <!--<xsl:comment>Match!</xsl:comment>-->
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy-of select="."/>
                <!--<xsl:comment>No Match!</xsl:comment>-->
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>


