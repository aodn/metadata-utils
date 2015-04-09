/*
java -cp  ./target/myartifcat-1.0.0.jar  au.org.emii.Client1


Cookies code,

	http://stackoverflow.com/questions/18701167/problems-handling-http-302-in-java-with-httpurlconnection

*/

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
        connection.setRequestProperty("Content-Type", "application/xml");

        connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
        connection.setRequestProperty("Content-Language", "UTF8");  
/*
    con.setRequestMethod("POST");
    con.setRequestProperty("Content-Type", "application/xml");
    con.setRequestProperty("Content-Length", "" + Integer.toString( xml.getBytes().length));
    // con.setRequestProperty("Content-Language", "en-US");  
    con.setRequestProperty("Content-Language", "en-US");  
*/


		connection.setInstanceFollowRedirects(true );

        connection.setUseCaches (false);
        connection.setDoInput(true);
        connection.setDoOutput(true);

        //Send request
        DataOutputStream wr = new DataOutputStream ( connection.getOutputStream ());
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


		//Obtenemos la cookie por si se necesita
		String cookies = connection.getHeaderField("Set-Cookie");
		System.out.println("Cookies: "+cookies);


		String newUrl = connection.getHeaderField("location");
		System.out.println("newUrl: "+ newUrl );

		System.out.println("here1" );
		System.out.println(response.toString());
  
		System.out.println( connection.getResponseCode() );
		System.out.println("here2" );

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






class Client1
{

	public static void main(String[] args)
		throws Exception
	{

		//sendGet(); 
		HttpProxy c = new HttpProxy( ); // "https://catalogue-123.aodn.org.au"); 
		c.enableSelfSignedSSL(); 

		// GeonetworkServer g = new GeonetworkServer( c, "https://catalogue-123.aodn.org.au" ); 


		System.out.println( " hi \n" ); 

		 // rename to template,
		String template = 
		  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + 
		  "<request/>"  
; 

		Document doc = GeonetworkServer.xMLFromString( template);  

		XPathFactory xPathfactory = XPathFactory.newInstance();


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


		String baseURL = "https://catalogue-123.aodn.org.au" ; 
		String path = baseURL + "/geonetwork/srv/eng/xml.metadata.update"; 

		String x = c.executePost( path, result ); 


	}	

}
