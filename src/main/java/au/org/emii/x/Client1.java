/*

  java -cp  ./target/myartifcat-1.0.0.jar  au.org.emii.x.Client1

Cookies code,

  http://stackoverflow.com/questions/18701167/problems-handling-http-302-in-java-with-httpurlconnection

*/

package au.org.emii.x;


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
import java.io.OutputStream;
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
  // private final static String USER_AGENT = "Mozilla/5.0";

  String sessionID; 
  String baseURL;

  HttpProxy( String baseURL)
  { 
    this.baseURL = baseURL;
    this.sessionID = null;
  }

 

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


  public void writeOutputStream( OutputStream os, String data  )
    throws IOException
  {
      DataOutputStream wr = new DataOutputStream ( os );
      wr.writeBytes ( data );
      wr.flush ();
      wr.close ();
  }


  public String globInputStream( InputStream is  )
    throws IOException
  {
    // this shit should be factored... slurpInputStream  
    BufferedReader in = new BufferedReader( new InputStreamReader( is )); //connection.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();
 
    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();
 
    // print result
    // System.out.println(response.toString());
    return response.toString();
  }



  Map< String, String> extractCookies( String cookies )
  {
      // extract the cookies ...
      Map< String, String> result = new HashMap< String, String>();

      for( String part : cookies.split(";") )
      {
        System.out.println("part: "+ part );
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

 

  // post, http://stackoverflow.com/questions/1359689/how-to-send-http-request-in-java 
  // should be called get...

  // we have to encode the xml values...
  // or pass the node...
  // or should it be a node...


  // http://stackoverflow.com/questions/18701167/problems-handling-http-302-in-java-with-httpurlconnection

    // we can't really abstract out the connection handling, because the serveri
    // may decide to disconnect us.
  

  // should be called get...
  public String get( String url_ ) throws Exception
  {
    HttpURLConnection connection = null;  
    try {
 
      URL url = new URL( baseURL + url_ );
      connection = (HttpURLConnection) url.openConnection();
   
      // optional default is GET
      connection.setRequestMethod("GET");
   
      //add request header
      // connection.setRequestProperty("User-Agent", USER_AGENT);

      connection.setRequestProperty("Cookie", "JSESSIONID=" + sessionID );
   
      int responseCode = connection.getResponseCode();
      if( responseCode != 200 ) {
          throw new BadHttpReturnCode( "bad return code" ); 
      }

      // System.out.println("\nSending 'GET' request to URL : " + url);
      // System.out.println("Response Code : " + responseCode);

      return globInputStream( connection.getInputStream() );
      }
      finally {
        if(connection != null) {
          connection.disconnect(); 
        }
    }
  }



  // should return a string...
  public String getRecord( String uuid ) throws Exception 
  {
    return this.get( "/geonetwork/srv/eng/xml.metadata.get?uuid=" + uuid ) ; 
  }




  public void login( String user, String pass ) 
    throws Exception
  {

    // URL url;
    HttpURLConnection connection = null;  
    try {
      String request = "username=admin&password=rqpxNDd8BS";

      //Create connection
      URL url = new URL ( baseURL + "/geonetwork/j_spring_security_check" );
      connection = (HttpURLConnection)url.openConnection();
      connection.setRequestMethod("POST");
      // connection.setRequestProperty("Content-Type", "application/xml");
      connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      connection.setRequestProperty("Content-Length", "" + Integer.toString( request.getBytes().length));
      connection.setRequestProperty("Content-Language", "UTF8");  
      // connection.setInstanceFollowRedirects(true );

      connection.setUseCaches (false);
      connection.setDoInput(true);
      connection.setDoOutput(true);

      //Send request
      writeOutputStream( connection.getOutputStream (), request ); 

      //Get Response    
      String response = globInputStream( connection.getInputStream() );
 
      // response code is 302 here, should be checking goes to non-long redirect... 

      Map< String, String> cookies = extractCookies( connection.getHeaderField("Set-Cookie") ); 

      // set the session id...
      System.out.println( "JSESSIONID is " + cookies.get( "JSESSIONID" ) ); 
      this.sessionID = cookies.get( "JSESSIONID" );


      String newUrl = connection.getHeaderField("location");
      System.out.println("newUrl: "+ newUrl );

      /* System.out.println("here1" );
            System.out.println(response.toString());
            System.out.println( connection.getResponseCode() );
            System.out.println("here2" );
      */
    } /*catch (Exception e) {

      e.printStackTrace();
      // return null;

    } */ finally {

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






class Client1
{

  public static void main(String[] args)
    throws Exception
  {

    //sendGet(); 
    HttpProxy c = new HttpProxy( "https://catalogue-123.aodn.org.au" ); 
    c.enableSelfSignedSSL(); 

    // GeonetworkServer g = new GeonetworkServer( c, "https://catalogue-123.aodn.org.au" ); 
    System.out.println( " hi \n" ); 

   /* 
    // Create request XML**
    Element request = new Element("request")
    .addContent(new Element("username").setText("admin"))
    .addContent(new Element("password").setText("admin"));
  */

/*
     // rename to template,
    String template = 
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + 
      "<request>" + 
      "  <username>admin</username>" + 
      "  <password>rqpxNDd8BS</password>" + 
      "</request>"  
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

    String request = Misc.transform ( transformer, doc ); 


    request = "username=admin1&password=rqpxNDd8BS";

    System.out.println( request);


    String baseURL = "https://catalogue-123.aodn.org.au" ; 
//    String path = baseURL + "/geonetwork/srv/eng/xml.metadata.update"; 
//      String path = baseURL + "/geonetwork/srv/eng/xml.user.login"; 

// https://catalogue-123.aodn.org.au/geonetwork/j_spring_security_check?redirectUrl=/srv/eng/main.home

//      String path = baseURL + "/geonetwork/srv/en/xml.user.login"; 

      String path = baseURL + "/geonetwork/j_spring_security_check"; 

 
//    String path = baseURL + "/geonetwork/login.jsp"; 
*/
              
    c.login( "user", "pass" ); 


    String result = c.get( "/geonetwork/srv/en/xml.group.list" ) ; 
    System.out.println( "result is " + result ); 


    String record = c.getRecord( "4402cb50-e20a-44ee-93e6-4728259250d2" );
    System.out.println( "record is " + record ); 

 
  } 

}
