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
            <xsl:when test="gmd:CI_ResponsibleParty/gmd:organisationName/gco:CharacterString[matches( text(), 'Integrated Marine Observing System (IMOS)|eMarine Information Infrastructure (eMII)|eMII project|Derwent Estuary Program' )]">
                <xsl:copy-of select="$aodnContact"/>
                <!--<xsl:comment>Match!</xsl:comment>-->
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy-of select="."/>
                <!--<xsl:comment>No Match!</xsl:comment>-->
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:variable name="aodnPointOfContact">
        <gmd:pointOfContact>
            <gmd:CI_ResponsibleParty>
                <gmd:organisationName>
                    <gco:CharacterString>Integrated Marine Observing System (IMOS)</gco:CharacterString>
                </gmd:organisationName>
                <gmd:positionName>
                    <gco:CharacterString>Data Officer</gco:CharacterString>
                </gmd:positionName>
                <gmd:contactInfo>
                    <gmd:CI_Contact>
                        <gmd:phone>
                            <gmd:CI_Telephone>
                                <gmd:voice>
                                    <gco:CharacterString>61 3 6226 7488</gco:CharacterString>
                                </gmd:voice>
                                <gmd:facsimile>
                                    <gco:CharacterString>61 3 6226 2107 5</gco:CharacterString>
                                </gmd:facsimile>
                            </gmd:CI_Telephone>
                        </gmd:phone>
                        <gmd:address>
                            <gmd:CI_Address>
                                <gmd:deliveryPoint>
                                    <gco:CharacterString>University of Tasmania</gco:CharacterString>
                                </gmd:deliveryPoint>
                                <gmd:deliveryPoint>
                                    <gco:CharacterString>Private Bag 110</gco:CharacterString>
                                </gmd:deliveryPoint>
                                <gmd:city>
                                    <gco:CharacterString>Hobart</gco:CharacterString>
                                </gmd:city>
                                <gmd:administrativeArea>
                                    <gco:CharacterString>Tasmania</gco:CharacterString>
                                </gmd:administrativeArea>
                                <gmd:postalCode>
                                    <gco:CharacterString>7001</gco:CharacterString>
                                </gmd:postalCode>
                                <gmd:country>
                                    <gco:CharacterString>Australia</gco:CharacterString>
                                </gmd:country>
                                <gmd:electronicMailAddress>
                                    <gco:CharacterString>info@aodn.org.au</gco:CharacterString>
                                </gmd:electronicMailAddress>
                            </gmd:CI_Address>
                        </gmd:address>
                        <gmd:onlineResource>
                            <gmd:CI_OnlineResource>
                                <gmd:linkage>
                                    <gmd:URL>http://imos.org.au/aodn.html</gmd:URL>
                                </gmd:linkage>
                                <gmd:protocol>
                                    <gco:CharacterString>WWW:LINK-1.0-http--link</gco:CharacterString>
                                </gmd:protocol>
                                <gmd:name gco:nilReason="missing">
                                    <gco:CharacterString/>
                                </gmd:name>
                                <gmd:description>
                                    <gco:CharacterString>Website of the Australian Ocean Data Network (AODN)</gco:CharacterString>
                                </gmd:description>
                            </gmd:CI_OnlineResource>
                        </gmd:onlineResource>
                    </gmd:CI_Contact>
                </gmd:contactInfo>
                <gmd:role>
                    <gmd:CI_RoleCode codeList="http://schemas.aodn.org.au/mcp-2.0/schema/resources/Codelist/gmxCodelists.xml#CI_RoleCode" codeListValue="pointOfContact">pointOfContact</gmd:CI_RoleCode>
                </gmd:role>
            </gmd:CI_ResponsibleParty>
        </gmd:pointOfContact>
    </xsl:variable>

    <xsl:variable name="aodnContact">
        <gmd:contact>
        <gmd:CI_ResponsibleParty>
            <gmd:organisationName>
                <gco:CharacterString>Integrated Marine Observing System (IMOS)</gco:CharacterString>
            </gmd:organisationName>
            <gmd:positionName>
                <gco:CharacterString>Data Officer</gco:CharacterString>
            </gmd:positionName>
            <gmd:contactInfo>
                <gmd:CI_Contact>
                    <gmd:phone>
                        <gmd:CI_Telephone>
                            <gmd:voice>
                                <gco:CharacterString>61 3 6226 7488</gco:CharacterString>
                            </gmd:voice>
                            <gmd:facsimile>
                                <gco:CharacterString>61 3 6226 2107</gco:CharacterString>
                            </gmd:facsimile>
                        </gmd:CI_Telephone>
                    </gmd:phone>
                    <gmd:address>
                        <gmd:CI_Address>
                            <gmd:deliveryPoint>
                                <gco:CharacterString>University of Tasmania</gco:CharacterString>
                            </gmd:deliveryPoint>
                            <gmd:deliveryPoint>
                                <gco:CharacterString>Private Bag 110</gco:CharacterString>
                            </gmd:deliveryPoint>
                            <gmd:city>
                                <gco:CharacterString>Hobart</gco:CharacterString>
                            </gmd:city>
                            <gmd:administrativeArea>
                                <gco:CharacterString>Tasmania</gco:CharacterString>
                            </gmd:administrativeArea>
                            <gmd:postalCode>
                                <gco:CharacterString>7001</gco:CharacterString>
                            </gmd:postalCode>
                            <gmd:country>
                                <gco:CharacterString>Australia</gco:CharacterString>
                            </gmd:country>
                            <gmd:electronicMailAddress>
                                <gco:CharacterString>info@aodn.org.au</gco:CharacterString>
                            </gmd:electronicMailAddress>
                        </gmd:CI_Address>
                    </gmd:address>
                    <gmd:onlineResource>
                        <gmd:CI_OnlineResource>
                            <gmd:linkage>
                                <gmd:URL> http://imos.org.au/aodn.html </gmd:URL>
                            </gmd:linkage>
                            <gmd:protocol>
                                <gco:CharacterString>WWW:LINK-1.0-http--link</gco:CharacterString>
                            </gmd:protocol>
                            <gmd:name gco:nilReason="missing">
                                <gco:CharacterString/>
                            </gmd:name>
                            <gmd:description>
                                <gco:CharacterString>Website of the Australian Ocean Data Network (AODN)</gco:CharacterString>
                            </gmd:description>
                        </gmd:CI_OnlineResource>
                    </gmd:onlineResource>
                </gmd:CI_Contact>
            </gmd:contactInfo>
            <gmd:role>
                <gmd:CI_RoleCode codeList="http://schemas.aodn.org.au/mcp-2.0/schema/resources/Codelist/gmxCodelists.xml#CI_RoleCode" codeListValue="distributor">distributor</gmd:CI_RoleCode>
            </gmd:role>
        </gmd:CI_ResponsibleParty>
        </gmd:contact>
    </xsl:variable>
</xsl:stylesheet>


