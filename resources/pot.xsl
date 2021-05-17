<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
				xmlns:gco="http://standards.iso.org/iso/19115/-3/gco/1.0"
				xmlns:mdb="http://standards.iso.org/iso/19115/-3/mdb/2.0"
				xmlns:cit="http://standards.iso.org/iso/19115/-3/cit/2.0">

	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>

	<!-- Point of truth -->
	<xsl:template match="mdb:metadataLinkage/cit:CI_OnlineResource/cit:linkage/gco:CharacterString">
		<xsl:copy>
			<xsl:value-of select="replace(text(), 'api/records/([^/]+)$', 'eng/catalog.search#/metadata/$1')"/>
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>
