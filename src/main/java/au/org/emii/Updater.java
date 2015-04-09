

package au.org.emii;


import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.io.ByteArrayInputStream; 

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;



import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.InputStream ;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.io.StringReader;
import java.io.StringWriter;

import java.sql.*;
import java.sql.SQLException;

import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import javax.xml.transform.OutputKeys; 

import javax.xml.namespace.NamespaceContext;


import org.apache.commons.cli.*;

import java.io.FileNotFoundException;
import java.io.IOException;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import javax.xml.xpath.XPathFactory; 
import javax.xml.xpath.XPath; 
import javax.xml.xpath.XPathExpression;

import javax.xml.xpath.XPathConstants;

import javax.xml.transform.dom.DOMSource ;


import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.net.URL;
 
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.security.cert.X509Certificate;

import org.w3c.dom.Document;


import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;



import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException; 

import org.xml.sax.InputSource; 



// xslt examples,
// http://fandry.blogspot.com.au/2012/04/java-xslt-processing-with-saxon.html


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

class HttpProxy 
{
	private final static String USER_AGENT = "Mozilla/5.0";

  HttpProxy(  )
  { }

	public void enableSelfSignedSSL() throws Exception
  {

    /*
       *  fix for
       *    Exception in thread "main" javax.net.ssl.SSLHandshakeException:
       *       sun.security.validator.ValidatorException:
       *           PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException:
       *               unable to find valid certification path to requested target
       */
      TrustManager[] trustAllCerts = new TrustManager[] {
         new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
              return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {  }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {  }
         }
      };

      SSLContext sc = SSLContext.getInstance("SSL");
      sc.init(null, trustAllCerts, new java.security.SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

      // Create all-trusting host name verifier
      HostnameVerifier allHostsValid = new HostnameVerifier() {
          public boolean verify(String hostname, SSLSession session) {
            return true;
          }
      };
      // Install the all-trusting host verifier
      HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
      /*
       * end of the fix
       */
  }

  // should be called get...
  public String get( String url ) throws Exception
  {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		// optional default is GET
		con.setRequestMethod("GET");
 
		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
 
		int responseCode = con.getResponseCode();
    if( responseCode != 200 ) {
        throw new BadHttpReturnCode( "bad return code" ); 
    }

		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader( new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
 
		//print result
//		System.out.println(response.toString());
		return response.toString();
  }

  // post, http://stackoverflow.com/questions/1359689/how-to-send-http-request-in-java 
  // should be called get...

  // we have to encode the xml values...
  // or pass the node...
  // or should it be a node...


  public static String executePost(String targetURL, String urlParameters) {
      URL url;
      HttpURLConnection connection = null;  
      try {
        //Create connection
        url = new URL(targetURL);
        connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", 
             "application/x-www-form-urlencoded");

        connection.setRequestProperty("Content-Length", "" + 
                 Integer.toString(urlParameters.getBytes().length));
        connection.setRequestProperty("Content-Language", "en-US");  

        connection.setUseCaches (false);
        connection.setDoInput(true);
        connection.setDoOutput(true);

        //Send request
        DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream ());
        wr.writeBytes (urlParameters);
        wr.flush ();
        wr.close ();

        //Get Response    
        InputStream is = connection.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuffer response = new StringBuffer(); 
        while((line = rd.readLine()) != null) {
          response.append(line);
          response.append('\r');
        }
        rd.close();
        return response.toString();

      } catch (Exception e) {

        e.printStackTrace();
        return null;

      } finally {

        if(connection != null) {
          connection.disconnect(); 
        }
      }
  }

  public String post( String url, String xml ) throws Exception
  {
    // url = url + "1";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		// optional default is GET
		con.setRequestMethod("POST");
 
		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
 
    
    con.setRequestMethod("POST");
    con.setRequestProperty("Content-Type", "application/xml");
    con.setRequestProperty("Content-Length", "" + Integer.toString( xml.getBytes().length));
 //   con.setRequestProperty("Content-Language", "en-US");  


      con.setUseCaches (false);
      con.setDoInput(true);
      con.setDoOutput(true);

      //Send request
      DataOutputStream wr = new DataOutputStream ( con.getOutputStream ());

      wr.writeBytes ( xml ); // ??
      wr.flush ();
      wr.close ();


		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
 
		//print result

		System.out.println("here1" );
		System.out.println(response.toString());

		System.out.println("here2" );


		return response.toString();
  }


};






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

//		final InputStream is  = new StringInputStream( input );

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



class SimpleNamespaceContext implements NamespaceContext {

    private final Map<String, String> PREF_MAP = new HashMap<String, String>();

    public SimpleNamespaceContext(final Map<String, String> prefMap) {
        PREF_MAP.putAll(prefMap);       
    }

    public String getNamespaceURI(String prefix) {
        return PREF_MAP.get(prefix);
    }

    public String getPrefix(String uri) {
        throw new UnsupportedOperationException();
    }

    public Iterator getPrefixes(String uri) {
        throw new UnsupportedOperationException();
    }

}



class GeonetworkServer 
{
  GeonetworkServer( HttpProxy proxy, String baseURL )
  {
    // change name to proxy,
    this.proxy = proxy; 
    this.baseURL = baseURL;
  }

  private final String baseURL; 
  private final HttpProxy proxy ; 

  // should return a string...
  public String getRecord( String uuid ) throws Exception 
  {
    String path = baseURL + "/geonetwork/srv/eng/xml.metadata.get?uuid=" + uuid; 
    String result = proxy.get( path ) ; 
    // System.out.print( result );
    return result ; 
  }

  // authentication is going to be an issue... 

  // we need post, get, delete 

  // should return a string...
  public String updateRecord( String uuid, String record ) throws Exception 
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

    Document doc = GeonetworkServer.xMLFromString( template);  

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
//    if( true) return ; 

    String path = baseURL + "/geonetwork/srv/eng/xml.metadata.update"; 
    String x = proxy.executePost( path, result ); 
  
    System.out.println( "output of post \n" + x ); 

    return x; 
  }


  // do we want to factor each of these into a class... 
  // 

  public List< String> getRecords( ) throws Exception 
  {
    String path = baseURL + "/geonetwork/srv/eng/xml.search"; 
    String result = proxy.get( path ) ; 
    // System.out.print( result );
    Document doc = xMLFromString( result );

    XPath xpath = getXpath(); 

    XPathExpression expr = xpath.compile("/response/metadata/geonet:info/uuid/text()");
    NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
    
    List< String>  uuids = new ArrayList< String > ();
    for (int i = 0; i < nl.getLength(); i++) {
      // String s = xpath.evaluate( (Element )nl.item(i), "/text()");
      // System.out.println( "s -> '" + s + "'" );
      String uuid = nl.item(i).getNodeValue() ; 
      uuids.add( uuid );
      // System.out.println( "uuid -> '" + uuid + "'" );
    }
    return uuids;
  }

// change name XMLofString 
  public static XPath getXpath() throws Exception
  {
    // see also reusing, http://stackoverflow.com/questions/5568443/avoid-repeated-instantiation-of-inputsource-with-xpath-in-java 

    // we don't need the namespacing to perform the http actions because the geonetwork is 

    XPathFactory xPathfactory = XPathFactory.newInstance();
    XPath xpath = xPathfactory.newXPath();

    Map namespacing = new HashMap<String, String>();
    namespacing.put( "geonet", "http://www.fao.org/geonetwork" );

    xpath.setNamespaceContext( new SimpleNamespaceContext( namespacing ) ) ; 

    return xpath;
  }



  // change name XMLofString 
  public static Document xMLFromString(String xml) throws Exception
  {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();


//    factory.setValidating(false);

    DocumentBuilder builder = factory.newDocumentBuilder();
    InputSource is = new InputSource(new StringReader(xml));
    return builder.parse(is);
  }


/*
  public static String stringFromXML( Document doc ) throws Exception
  {
    // http://stackoverflow.com/questions/315517/is-there-a-more-elegant-way-to-convert-an-xml-document-to-a-string-in-java-than

    TransformerFactory tf = TransformerFactory.newInstance();



    Transformer transformer = tf.newTransformer();


    // http://docs.oracle.com/javaee/1.4/api/javax/xml/transform/OutputKeys.html

    // transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");

    StringWriter writer = new StringWriter();

    transformer.transform(new DOMSource(doc), new StreamResult(writer));

    String output = writer.getBuffer().toString();//.replaceAll("\n|\r", "");
    return output;
  }
*/




}





class Updater
{


    private static Connection getConn( String url, String user, String pass)
		throws SQLException
	{
		Properties props = new Properties();

		props.setProperty("user", user );
		props.setProperty("password",pass );

		// props.setProperty("search_path","soop_sst,public");
		props.setProperty("ssl","true");
		props.setProperty("sslfactory","org.postgresql.ssl.NonValidatingFactory");
		props.setProperty("driver","org.postgresql.Driver" );

		return DriverManager.getConnection(url, props);
	}

/*	
	// HTTP GET request
	private static void sendGet()  throws IOException  
	{
//		String url = "http://www.google.com/search?q=mkyong";

		String url = "https://catalogue-123.aodn.org.au/geonetwork/srv/eng/xml.metadata.get?uuid=4402cb50-e20a-44ee-93e6-4728259250d2";
 
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		// optional default is GET
		con.setRequestMethod("GET");
 
		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
 
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
 
		//print result
		System.out.println(response.toString());
	}
*/

/*
    public static final void prettyPrint(Document xml) throws Exception {
        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        Writer out = new StringWriter();
        tf.transform(new DOMSource(xml), new StreamResult(out));
        System.out.println(out.toString());
    }   
*/


	public static void main(String[] args)
		throws FileNotFoundException, TransformerConfigurationException, TransformerException,
			SQLException, ParseException, BadCLIArgumentsException, IOException 
			, Exception
	{

//		sendGet(); 


    HttpProxy c = new HttpProxy( ); // "https://catalogue-123.aodn.org.au"); 
	  c.enableSelfSignedSSL(); 

    GeonetworkServer g = new GeonetworkServer( c, "https://catalogue-123.aodn.org.au" ); 

/*
    List< String> uuids = g.getRecords();
    for( String uuid : uuids ) {
      String record = g.getRecord( uuid ); 
      System.out.println( "got record " + uuid ); 
      // ok, want to be able putRecord ...
    } 
*/

    String record = g.getRecord( "4402cb50-e20a-44ee-93e6-4728259250d2" );


    g.updateRecord( "4402cb50-e20a-44ee-93e6-4728259250d2", record ); 

 

		if( false ) {
		
		Options options = new Options();
		options.addOption("url", true, "jdbc connection string, eg. jdbc:postgresql://127.0.0.1/geonetwork");
		options.addOption("u", true, "user");
		options.addOption("p", true, "password");
		options.addOption("uuid", true, "metadata record uuid");
		options.addOption("help", false, "show help");
		options.addOption("t", true, "xslt file for transform");
		options.addOption("stdout", false, "dump result to stdout");
		options.addOption("update", false, "actually update the record");
		options.addOption("all", false, "update all records");

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse( options, args);

		if(cmd.hasOption("help") || !cmd.hasOption("url") || !cmd.hasOption("u") || !cmd.hasOption("u")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "Updater", options );
			return;
		}

		Connection conn = getConn(
			cmd.getOptionValue("url"),
			cmd.getOptionValue("u"),
			cmd.getOptionValue("p")
		);

		String query = "SELECT id,uuid,data FROM metadata ";
		if(cmd.hasOption("uuid")) {
			query += " where uuid = '" + cmd.getOptionValue("uuid") + "'";
		}

		else if(cmd.hasOption("stdout") || cmd.hasOption( "update")) {
			;
		}
		else {
			throw new BadCLIArgumentsException( "Options should include -uuid, -all or -stdout" );
		}


        PreparedStatement stmt = conn.prepareStatement( query );
        ResultSet rs = stmt.executeQuery();

		Transformer transformer = null;
		if( cmd.hasOption("t")) {
			transformer = Misc.getTransformerFromFile( cmd.getOptionValue("t") );
		}

        int count = 0;
        while ( rs.next() )
        {
			int id = (Integer) rs.getObject("id");
			String uuid = (String) rs.getObject("uuid");
			String data = (String) rs.getObject("data");

			if( transformer != null ) {
				data = Misc.transform( transformer, data);
			}

			if( cmd.hasOption("stdout")) {
				System.out.println( "id " + id + "\nuuid " + uuid + "\n" + data);
			}

			if( cmd.hasOption("update")) {
				PreparedStatement updateStmt = conn.prepareStatement( "update metadata set data=? where id=?" ) ;
				updateStmt.setString(1, data );
				updateStmt.setInt(2, id );
				updateStmt.executeUpdate();
				// close...?
			}

            ++count;
			break;
        }

        System.out.println( "records processed " + count );
	}
	}
}



/*    
public class Updater1 
{

    public static void main(String[] args) throws Exception {
        System.out.println( "here" );

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//      DBf.setValidating(false);
        DocumentBuilder db = dbf.newDocumentBuilder();

        Document doc = db.parse(new FileInputStream(new File("in.xml")));

        Element element = doc.getDocumentElement();
        CDATASection cdata = doc.createCDATASection("mycdata");
        element.appendChild(cdata);

        prettyPrint(doc);

    }

    public static final void prettyPrint(Document xml) throws Exception {
        //Transformer tf = TransformerFactory.newInstance().newTransformer();

        String identity = 
          "<xsl:stylesheet version=\"2.0\"" + 
          " xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">" + 
          " <xsl:output omit-xml-declaration=\"yes\" indent=\"yes\"" + 
          " cdata-section-elements=\"rss\"/>" +
          " <xsl:strip-space elements=\"*\"/>" +
          " <xsl:template match=\"node()|@*\">" +
          "     <xsl:copy>" +
          "       <xsl:apply-templates select=\"node()|@*\"/>" +
          "     </xsl:copy>" +
          " </xsl:template>" +
          "</xsl:stylesheet>"
          ;

      Transformer transformer = Updater1.getTransformerFromString ( identity ); 

      Writer writer = new StringWriter();
      StreamResult result=new StreamResult( writer );

      transformer.transform( new DOMSource(xml), result);

      System.out.println( writer.toString() );

    }

}



*/
