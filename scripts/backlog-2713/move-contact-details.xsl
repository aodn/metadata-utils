<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:cit="http://standards.iso.org/iso/19115/-3/cit/2.0"
    xmlns:mdb="http://standards.iso.org/iso/19115/-3/mdb/2.0"
    xmlns:mri="http://standards.iso.org/iso/19115/-3/mri/1.0"
    xmlns:mrd="http://standards.iso.org/iso/19115/-3/mrd/1.0"
    exclude-result-prefixes="xs"
    version="2.0">

    <xsl:output indent="yes"/>

    <!-- default action is to copy -->

    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

    <!-- move contact information from organisation to individual
         in cited responsible party (resource), point of contact
         distributor contact and metadata contact sections 
         where this still makes sense -->

    <xsl:template match="cit:CI_Organisation[
                           cit:contactInfo
                           and count(cit:individual)=1
                           and not(*/cit:contactInfo) 
                           and ancestor::mri:citation
                              |ancestor::mri:pointOfContact
                              |ancestor::mrd:distributorContact
                              |ancestor::mdb:contact]">
        <xsl:element name="cit:CI_Organisation">
            <xsl:apply-templates select="./@*"/>
            <xsl:apply-templates select="cit:name"/>
            <xsl:apply-templates select="cit:partyIdentifier"/>
            <xsl:apply-templates select="cit:logo"/>
            <xsl:variable name="individual" select="cit:individual/cit:CI_Individual"/>
            <cit:individual>
                <cit:CI_Individual>
                    <xsl:apply-templates select="$individual/cit:name"/>
                    <xsl:apply-templates select="cit:contactInfo"/>
                    <xsl:apply-templates select="$individual/cit:partyIdentifier"/>
                    <xsl:apply-templates select="$individual/cit:positionName"/>
                </cit:CI_Individual>
            </cit:individual>
        </xsl:element>
    </xsl:template>
</xsl:stylesheet>
