
      // http://examples.javacodegeeks.com/core-java/xml/dom/add-cdata-section-to-dom-document/
      // NodeList myNodeList = (NodeList) xpath.compile("//request/data").evaluate( doc, XPathConstants.NODESET); 


/*
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//      DBf.setValidating(false);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new FileInputStream(new File("in.xml")));
    //    Document doc = GeonetworkServer.xMLFromString( s );  
        Element element = doc.getDocumentElement();
        CDATASection cdata = doc.createCDATASection("mycdata");
        element.appendChild(cdata);
//      myNodeList.item(0).appendChild( cdata );
      doc.getDocumentElement().appendChild( cdata);
    GeonetworkServer.prettyPrint( doc ) ; 
*/


/*      Element element = doc.getDocumentElement();
      CDATASection cdata = doc.createCDATASection("mycdata");
      element.appendChild(cdata);
*/

/*
      newCDATA=xmlDoc.createCDATASection(newtext);
      x[i].appendChild(newCDATA);
*/
/* 
    {
      NodeList myNodeList = (NodeList) xpath.compile("//request/uuid").evaluate( doc, XPathConstants.NODESET); 
      myNodeList.item(0).setTextContent("Hi mom!");
    }
*/



class  Helper
{
  // change name XMLofString 
  public static Document xMLFromString(String xml) throws Exception
  {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//    factory.setValidating(false);
    DocumentBuilder builder = factory.newDocumentBuilder();
    InputSource is = new InputSource(new StringReader(xml));
    return builder.parse(is);
  }



}



class BadCLIArgumentsException extends Exception
{
  public BadCLIArgumentsException( String message )
  {
    super(message);
  }
}

class BadHttpReturnCode extends Exception
{
  public BadHttpReturnCode( String message )
  {
    super(message);
  }
}



  Map< String, String> extractCookies( String cookies )
  {
      // extract the cookies ...
      Map< String, String> result = new HashMap< String, String>();

      for( String part : cookies.split(";") )
      {
        // System.out.println("part: "+ part );
        String[] keyvals = part.split("=");
        // System.out.println("keyvals.length "+ keyvals.length ); 
        if( keyvals.length == 1) {
          result.put( keyvals[ 0], null );  
        }
        else if( keyvals.length == 2) {
          result.put( keyvals[ 0], keyvals[ 1] );  
        }
      }

    return result; 
  }

 


  // should'nt return anything
  public void updateRecord( String uuid, String record ) throws Exception 
  {
    /*
    /geonetwork/srv/eng/xml.metadata.update
    <?xml version="1.0" encoding="UTF-8"?>
    <request>
      <id>11</id>
      <version>1</version>
      <data><![CDATA[
        <gmd:MD_Metadata xmlns:gmd="http://www.isotc211.org/2005/gmd"
                         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        ...
              </gmd:DQ_DataQuality>
          </gmd:dataQualityInfo>
        </gmd:MD_Metadata>]]>
      </data>
    </request>
    */

    // rename to template,
    String template = 
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + 
      "<request>" + 
      " <uuid/>" +
      " <version>1</version>" + 
      " <data/>" + 
      "</request>" ; 

    Document doc = Helper.xMLFromString( template);  

    XPathFactory xPathfactory = XPathFactory.newInstance();

    // substitute the data 
    {
      XPath xpath = xPathfactory.newXPath();
      NodeList myNodeList = (NodeList) xpath.compile("//request/data").evaluate( doc, XPathConstants.NODESET); 
      CDATASection cdata = doc.createCDATASection( record );
      myNodeList.item( 0).appendChild(cdata);
    }

    // substitute the uuid 
    {
      XPath xpath = xPathfactory.newXPath();
      NodeList myNodeList = (NodeList) xpath.compile("//request/uuid").evaluate( doc, XPathConstants.NODESET); 
      myNodeList.item(0).setTextContent( uuid);
    }

    String identity = 
      "<xsl:stylesheet version=\"2.0\"" + 
      " xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">" + 
      " <xsl:output omit-xml-declaration=\"no\" indent=\"yes\"" + 
      " cdata-section-elements=\"data\"/>" +
      " <xsl:strip-space elements=\"*\"/>" +
      " <xsl:template match=\"node()|@*\">" +
      "     <xsl:copy>" +
      "       <xsl:apply-templates select=\"node()|@*\"/>" +
      "     </xsl:copy>" +
      " </xsl:template>" +
      "</xsl:stylesheet>"
      ;

    Transformer transformer = Misc.getTransformerFromString( identity ); 

    String result = Misc.transform ( transformer, doc ); 

//    System.out.println( "here -> \n" + result ); 

    this.httpPostXML( "/geonetwork/srv/eng/xml.metadata.update", result ); 

  }




  // should'nt return anything
  public void updateRecord( String uuid, String record ) throws Exception 
  {
    /*
    /geonetwork/srv/eng/xml.metadata.update
    <?xml version="1.0" encoding="UTF-8"?>
    <request>
      <id>11</id>
      <version>1</version>
      <data><![CDATA[
        <gmd:MD_Metadata xmlns:gmd="http://www.isotc211.org/2005/gmd"
                         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        ...
              </gmd:DQ_DataQuality>
          </gmd:dataQualityInfo>
        </gmd:MD_Metadata>]]>
      </data>
    </request>
    */

    // rename to template,
    String template = 
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + 
      "<request>" + 
      " <uuid/>" +
      " <version>1</version>" + 
      " <data/>" + 
      "</request>" ; 

    Document doc = Helper.xMLFromString( template);  

    XPathFactory xPathfactory = XPathFactory.newInstance();

    // substitute the data 
    {
      XPath xpath = xPathfactory.newXPath();
      NodeList myNodeList = (NodeList) xpath.compile("//request/data").evaluate( doc, XPathConstants.NODESET); 
      CDATASection cdata = doc.createCDATASection( record );
      myNodeList.item( 0).appendChild(cdata);
    }

    // substitute the uuid 
    {
      XPath xpath = xPathfactory.newXPath();
      NodeList myNodeList = (NodeList) xpath.compile("//request/uuid").evaluate( doc, XPathConstants.NODESET); 
      myNodeList.item(0).setTextContent( uuid);
    }

    String identity = 
      "<xsl:stylesheet version=\"2.0\"" + 
      " xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">" + 
      " <xsl:output omit-xml-declaration=\"no\" indent=\"yes\"" + 
      " cdata-section-elements=\"data\"/>" +
      " <xsl:strip-space elements=\"*\"/>" +
      " <xsl:template match=\"node()|@*\">" +
      "     <xsl:copy>" +
      "       <xsl:apply-templates select=\"node()|@*\"/>" +
      "     </xsl:copy>" +
      " </xsl:template>" +
      "</xsl:stylesheet>"
      ;

    Transformer transformer = Misc.getTransformerFromString( identity ); 

    String result = Misc.transform ( transformer, doc ); 

//    System.out.println( "here -> \n" + result ); 

    this.httpPostXML( "/geonetwork/srv/eng/xml.metadata.update", result ); 

  }




class Misc
{

  public Misc ()
  { }


  public static Transformer getTransformerFromFile( String filename )
    throws FileNotFoundException, TransformerConfigurationException
  {
    final TransformerFactory tsf = TransformerFactory.newInstance();
    final InputStream is  = new FileInputStream( filename );

    return tsf.newTransformer(new StreamSource(is));
  }


  public static Transformer getTransformerFromString ( String input )
    throws FileNotFoundException, TransformerConfigurationException
  {
    final TransformerFactory tsf = TransformerFactory.newInstance();

    InputStream is = new ByteArrayInputStream( input.getBytes());//StandardCharsets.UTF_8));

//    final InputStream is  = new StringInputStream( input );

    return tsf.newTransformer(new StreamSource(is));
  }



  public static String transform ( Transformer transformer, Document xml )
    throws TransformerException
  {
    //    Transformer transformer = Updater1.getTransformerFromString ( identity ); 
      Writer writer = new StringWriter();
      StreamResult result=new StreamResult( writer );

      transformer.transform( new DOMSource(xml), result);

      // System.out.println( writer.toString() );
      return writer.toString();
  }

  /* TODO remove - shouldn't ever need string -> string transform */
  public static String transform ( Transformer transformer, String xmlString)
    throws TransformerException
  {
    final StringReader xmlReader = new StringReader(xmlString);
    final StringWriter xmlWriter = new StringWriter();

    transformer.transform(new StreamSource(xmlReader), new StreamResult(xmlWriter));

    return xmlWriter.toString();
  }

}


-v for verbose to show request,response 
-k disable certificate checking
-d @filename to specify the request

curl -k -v   -X POST -d @out  'https://catalogue-123.aodn.org.au/geonetwork/srv/eng/xml.user.login' 

      /*
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/xml");
            con.setRequestProperty("Content-Length", "" + Integer.toString( xml.getBytes().length));
            // con.setRequestProperty("Content-Language", "en-US");  
            con.setRequestProperty("Content-Language", "en-US");  
        */

        /*
            connection.setRequestProperty("username", "admin");  
            connection.setRequestProperty("password", "rqpxNDd8BS");  
        */

