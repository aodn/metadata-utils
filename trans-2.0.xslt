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


  <xsl:output method="xml" indent="yes"/>

	<xsl:variable name="imosContact">
		<gmd:contact>
		  <gmd:CI_ResponsibleParty>
			<gmd:organisationName>
			  <gco:CharacterString>Integrated Marine Observing System (IMOS)</gco:CharacterString>
			</gmd:organisationName>
			<gmd:positionName>
			  <gco:CharacterString>eMII Data Officer</gco:CharacterString>
			</gmd:positionName>
			<gmd:contactInfo>
			  <gmd:CI_Contact>
				<gmd:phone>
				  <gmd:CI_Telephone>
					<gmd:voice>
					  <gco:CharacterString>61 3 6226 7488</gco:CharacterString>
					</gmd:voice>
					<gmd:facsimile>
					  <gco:CharacterString>61 3 6226 8575</gco:CharacterString>
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
					  <gco:CharacterString>info@emii.org.au</gco:CharacterString>
					</gmd:electronicMailAddress>
				  </gmd:CI_Address>
				</gmd:address>
				<gmd:onlineResource>
				  <gmd:CI_OnlineResource>
					<gmd:linkage>
					  <gmd:URL>http://imos.org.au/emii.html</gmd:URL>
					</gmd:linkage>
					<gmd:protocol>
					  <gco:CharacterString>WWW:LINK-1.0-http--link</gco:CharacterString>
					</gmd:protocol>
					<gmd:name gco:nilReason="missing">
					  <gco:CharacterString/>
					</gmd:name>
					<gmd:description>
					  <gco:CharacterString>Website of the eMarine Information Infrastructure (eMII)</gco:CharacterString>
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


	<xsl:variable name="imosCommons">
		<mcp:MD_Commons mcp:commonsType="Creative Commons" gco:isoType="gmd:MD_Constraints">
             <mcp:jurisdictionLink>
				  <gmd:URL>http://creativecommons.org/international/</gmd:URL>
             </mcp:jurisdictionLink>
             <mcp:licenseLink>
				  <gmd:URL>http://creativecommons.org/licenses/by/4.0/</gmd:URL>
             </mcp:licenseLink>
             <mcp:imageLink>
				  <gmd:URL>http://i.creativecommons.org/l/by/4.0/88x31.png</gmd:URL>
             </mcp:imageLink>
             <mcp:licenseName>
                 <gco:CharacterString>Attribution 4.0 International</gco:CharacterString>
             </mcp:licenseName>
			  <mcp:attributionConstraints>
				<gco:CharacterString>The citation in a list of references is: "IMOS [year-of-data-download], [Title], [data-access-URL], accessed [date-of-access]."</gco:CharacterString>
			  </mcp:attributionConstraints>
             <mcp:attributionConstraints>
                 <gco:CharacterString>Any users of IMOS data are required to clearly acknowledge the source of the material derived from IMOS in the format: "Data was sourced from the Integrated Marine Observing System (IMOS) - IMOS is a national collaborative research infrastructure, supported by the Australian Government." If relevant, also credit other organisations involved in collection of this particular datastream (as listed in 'credit' in the metadata record).</gco:CharacterString>
             </mcp:attributionConstraints>
         </mcp:MD_Commons>
	</xsl:variable>


	<xsl:variable name="imosCredit1">
		<gmd:credit>
			<gco:CharacterString>Integrated Marine Observing System (IMOS). IMOS is a national collaborative research infrastructure, supported by Australian Government.</gco:CharacterString>
		</gmd:credit>
	</xsl:variable>


	<xsl:variable name="imosCredit2">
		<gmd:credit>
			<gco:CharacterString>Funded by the National Collaborative Research Infrastructure Strategy (NCRIS)</gco:CharacterString>
		</gmd:credit>
	</xsl:variable>




	<xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>


	<xsl:template match="/mcp:MD_Metadata/gmd:contact">

		<xsl:choose>
          <xsl:when test="gmd:CI_ResponsibleParty/gmd:organisationName/gco:CharacterString[matches( lower-case(text()), 'emii|imos' )]">
            <xsl:copy-of select="$imosContact"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:copy-of select="."/>
          </xsl:otherwise>
        </xsl:choose>

	</xsl:template>


     <xsl:template match="/mcp:MD_Metadata/gmd:identificationInfo/mcp:MD_DataIdentification/gmd:resourceConstraints/mcp:MD_Commons">

		<xsl:choose>
          <xsl:when test="mcp:attributionConstraints/gco:CharacterString[matches( lower-case(text()), 'emii|imos' )]">
            <xsl:copy-of select="$imosCommons"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:copy-of select="."/>
          </xsl:otherwise>
        </xsl:choose>

    </xsl:template>


	<xsl:template match="/mcp:MD_Metadata/gmd:identificationInfo/mcp:MD_DataIdentification/gmd:credit">

		<xsl:choose>
          <xsl:when test="gco:CharacterString[matches( text(), '^.*IMOS is supported by the Australian Government.*$' )]">
            <xsl:copy-of select="$imosCredit1"/>
          </xsl:when>

          <xsl:when test="gco:CharacterString[matches( text(), '^Funded by the .* and the Super Science Initiative.*$' )]">
            <xsl:copy-of select="$imosCredit2"/>
          </xsl:when>

          <xsl:otherwise>
            <xsl:copy-of select="."/>
          </xsl:otherwise>
        </xsl:choose>

    </xsl:template>


</xsl:stylesheet>

