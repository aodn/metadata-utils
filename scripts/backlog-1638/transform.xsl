<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:gco="http://www.isotc211.org/2005/gco"
                xmlns:gmd="http://www.isotc211.org/2005/gmd"
                xmlns:gmx="http://www.isotc211.org/2005/gmx"
                xmlns:geonet="http://www.fao.org/geonetwork"

                exclude-result-prefixes="xsl gco gmd gmx geonet">

    <xsl:output method="xml" encoding="UTF-8" indent="yes"/>

    <xsl:variable name="oldCredit">Integrated Marine Observing System (IMOS) - IMOS is a national collaborative research infrastructure, supported by the Australian Government</xsl:variable>
    <xsl:variable name="oldCredit1">Integrated Marine Observing System (IMOS) - IMOS is a national collaborative research infrastructure, supported by Australian Government</xsl:variable>
    <xsl:variable name="oldCredit2">Integrated Marine Observing System (IMOS). IMOS is a national collaborative research infrastructure, supported by the Australian Government</xsl:variable>
    <xsl:variable name="oldCredit3">Integrated Marine Observing System (IMOS). IMOS is a national collaborative research infrastructure, supported by Australian Government</xsl:variable>
    <xsl:variable name="newCredit">Australia’s Integrated Marine Observing System (IMOS) is enabled by the National Collaborative Research Infrastructure Strategy (NCRIS). It is operated by a consortium of institutions as an unincorporated joint venture, with the University of Tasmania as Lead Agent</xsl:variable>

    <xsl:variable name="oldAttributionConstraints">Data was sourced from the Integrated Marine Observing System (IMOS) - IMOS is a national collaborative research infrastructure, supported by the Australian Government.</xsl:variable>
    <xsl:variable name="oldAttributionConstraints2">Data was sourced from the Integrated Marine Observing System (IMOS) - IMOS is supported by the Australian Government through the National Collaborative Research Infrastructure Strategy (NCRIS) and the Super Science Initiative (SSI).</xsl:variable>
    <xsl:variable name="newAttributionConstraints">Data was sourced from Australia’s Integrated Marine Observing System (IMOS) – IMOS is enabled by the National Collaborative Research Infrastructure strategy (NCRIS).</xsl:variable>

    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

    <!--Match all MCP versions -->
    <xsl:template match="*[local-name() = 'MD_Metadata']/gmd:identificationInfo/*[local-name() = 'MD_DataIdentification']/gmd:credit/gco:CharacterString">
        <xsl:choose>
            <xsl:when test="contains(., $oldCredit)">
                <xsl:value-of select="substring-before(.,$oldCredit)"/>
                <xsl:value-of select="$newCredit"/>
                <xsl:value-of select="substring-after(.,$oldCredit)"/>
            </xsl:when>
            <xsl:when test="contains(., $oldCredit1)">
                <xsl:value-of select="substring-before(.,$oldCredit1)"/>
                <xsl:value-of select="$newCredit"/>
                <xsl:value-of select="substring-after(.,$oldCredit1)"/>
            </xsl:when>
            <xsl:when test="contains(., $oldCredit2)">
                <gco:CharacterString>
                    <xsl:value-of select="substring-before(.,$oldCredit2)"/>
                    <xsl:value-of select="$newCredit"/>
                    <xsl:value-of select="substring-after(.,$oldCredit2)"/>
                </gco:CharacterString>
            </xsl:when>
            <xsl:when test="contains(., $oldCredit3)">
                <gco:CharacterString>
                    <xsl:value-of select="substring-before(.,$oldCredit3)"/>
                    <xsl:value-of select="$newCredit"/>
                    <xsl:value-of select="substring-after(.,$oldCredit3)"/>
                </gco:CharacterString>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy-of select="."/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!--Match all MCP versions -->
    <xsl:template match="*[local-name() = 'MD_Metadata']/gmd:identificationInfo/*[local-name() = 'MD_DataIdentification']/gmd:resourceConstraints/*[local-name() = 'MD_Commons']/*[local-name() = 'attributionConstraints']/gco:CharacterString">
        <xsl:choose>
            <xsl:when test="contains(., $oldAttributionConstraints)">
                <gco:CharacterString>
                    <xsl:value-of select="substring-before(.,$oldAttributionConstraints)"/>
                    <xsl:value-of select="$newAttributionConstraints"/>
                    <xsl:value-of select="substring-after(.,$oldAttributionConstraints)"/>
                </gco:CharacterString>
            </xsl:when>
            <xsl:when test="contains(., $oldAttributionConstraints2)">
                <gco:CharacterString>
                    <xsl:value-of select="substring-before(.,$oldAttributionConstraints2)"/>
                    <xsl:value-of select="$newAttributionConstraints"/>
                    <xsl:value-of select="substring-after(.,$oldAttributionConstraints2)"/>
                </gco:CharacterString>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy-of select="."/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
</xsl:stylesheet>


