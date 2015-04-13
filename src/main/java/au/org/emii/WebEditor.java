/*

	java -cp  ./target/myartifcat-1.0.0.jar  au.org.emii.WebEditor

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

import java.net.URLEncoder ; 


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



import org.apache.commons.codec.binary.Base64;


// xslt examples,
// http://fandry.blogspot.com.au/2012/04/java-xslt-processing-with-saxon.html


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


  // post, http://stackoverflow.com/questions/1359689/how-to-send-http-request-in-java 
  
  // http://stackoverflow.com/questions/18701167/problems-handling-http-302-in-java-with-httpurlconnection


  // should be called get...
  public String httpGet( String url_ ) throws Exception
  {
    HttpURLConnection connection = null;  
    try {
 
      URL url = new URL( baseURL + url_ );
      connection = (HttpURLConnection) url.openConnection();
   
      // optional default is GET
      connection.setRequestMethod("GET");

      connection.setRequestProperty("Content-Type", "application/xml;charset=UTF-8");
      connection.setRequestProperty("Content-Language", "en-US");  


  
      System.out.println( "setting cookie " + sessionID ); 
      connection.setRequestProperty("Cookie", sessionID );

      connection.connect();

      System.out.println( "response code " + connection.getResponseCode() ); 

      String s = globInputStream( connection.getInputStream() );

      System.out.println( "response " + s ); 
   
      return s;
      }
      finally {
        if(connection != null) {
          connection.disconnect(); 
        }
    }
  }


  public void httpPostXML( String url_, String data ) 
    throws Exception
  {
    // URL url;
    HttpURLConnection connection = null;  
    try {

      URL url = new URL( baseURL + url_ );
      connection = (HttpURLConnection) url.openConnection();
   
      connection.setRequestMethod("POST");
      
      /*connection.setRequestProperty("Content-Type", "application/xml");
      connection.setRequestProperty("Content-Length", "" + Integer.toString( data.getBytes().length));
      connection.setRequestProperty("Content-Language", "en-US");  
      */
      // connection.setInstanceFollowRedirects(true );
/*
      connection.setRequestProperty("Content-Type", "text/xml");
      connection.setRequestProperty("Content-Length", Integer.toString( data.getBytes().length) );
      connection.setRequestProperty("Content-Language", "en-US");
*/



      // data = URLEncoder.encode( data , "UTF-8");
      // connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

      // we shouldn't have to url encode like this...

      connection.setRequestProperty("User-Agent", "Mozilla/5.0");  
      connection.setRequestProperty("Content-Type", "application/xml;charset=UTF-8");

//      connection.setRequestProperty("Content-Language", "en-US");  
      connection.setRequestProperty("Content-Length", Integer.toString( data.getBytes().length) );


      // use basic authentification
      if( false )
      {
        // c.login( "admin", "rqpxNDd8BS" ); 

        String name = "admin";
        String password = "rqpxNDd8BS";

        String authString = name + ":" + password;
        System.out.println("auth string: " + authString);
        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        String authStringEnc = new String(authEncBytes);
        System.out.println("Base64 encoded auth string: " + authStringEnc);

        // URL url = new URL(webPage);
        // URLConnection urlConnection = url.openConnection();
        connection .setRequestProperty("Authorization", "Basic " + authStringEnc);

      }
      else {
        // use session id cookie
        System.out.println( "setting session cookie " + sessionID );
        connection.setRequestProperty("Cookie", sessionID );
      }


     // connection.setUseCaches (false);
      connection.setDoInput(true);
      connection.setDoOutput(true);

      connection .connect();  

      // connection.setRequestProperty("Cookie", URLEncoder.encode( cookies_ , "UTF-8"));
      // connection.setRequestProperty( cookies_  );

      // do request
      writeOutputStream( connection.getOutputStream(), data ); 

      System.out.println( "response code " + connection.getResponseCode() );
      System.out.println( "response message " + connection.getResponseMessage() );

      String newUrl = connection.getHeaderField("location");
      System.out.println("newUrl: "+ newUrl );


/*
      Map< String, String> cookies = extractCookies( connection.getHeaderField("Set-Cookie") ); 
      for( String key : cookies.keySet()  )
      {
          System.out.println( "response cookie " + key + "=" + cookies.get( key )  ) ;  
      }
*/

      // get response 
      String response = globInputStream( connection.getInputStream() );
      System.out.println( "globbed " + response);
 
 
      // response code is 302 here, should be checking goes to non-long redirect... 

      // Map< String, String> cookies = extractCookies( connection.getHeaderField("Set-Cookie") ); 
      // set the session id...
      // System.out.println( "JSESSIONID is " + cookies.get( "JSESSIONID" ) ); 
      // this.sessionID = cookies.get( "JSESSIONID" );
      // String newUrl = connection.getHeaderField("location");
      // System.out.println("newUrl: "+ newUrl );
 //     System.out.println(response.toString());
      /* System.out.println("here1" );
            System.out.println(response.toString());
            System.out.println( connection.getResponseCode() );
            System.out.println("here2" );
      */
    } finally {

      if(connection != null) {
        connection.disconnect(); 
      }
    }
  }






  // should return a string...
  public String getRecord( String uuid ) 
    throws Exception 
  {
    return this.httpGet( "/geonetwork/srv/eng/xml.metadata.get?uuid=" + uuid ) ; 
  }

/*
  public void createUser( )
    throws Exception 
  {

    System.out.println( "*** create user" ); 

    String template = 
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + 
      "<request>" + 
      " <operation>newuser</operation>" +
      " <username>fuck</username>" +
      " <password>fuck</password>" +
      " <profile>Editor</profile>" +
      " <name>Samantha</name>" +
      " <city>Amsterdam</city>" +
      " <country>Netherlands</country>" +
      " <email>Netherlands</email>" +
      " <data/>" + 
      "</request>" ; 

      this.httpPostXML( "/geonetwork/srv/en/user.update", template ); 
  }
*/


  public void login( String user, String pass ) 
    throws Exception
  {

    // URL url;
    HttpURLConnection connection = null;  
    try {

      System.out.println("*** doing login" );

      String data = "username=" + user + "&password=" + pass ; 

      //Create connection
      URL url = new URL ( baseURL + "/geonetwork/j_spring_security_check" );
      // URL url = new URL ( baseURL + "/geonetwork/srv/en/xml.user.login" );

      connection = (HttpURLConnection)url.openConnection();
      connection.setRequestMethod("POST");
      // connection.setRequestProperty("Content-Type", "application/xml");
      connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      connection.setRequestProperty("Content-Language", "en-US");  

      connection.setRequestProperty( "Accept", "application/octet-stream" );


/*
      connection.setRequestProperty("Content-Type", "text/xml");
      connection.setRequestProperty("Content-Length", Integer.toString( data.getBytes().length) );
      connection.setRequestProperty("Content-Language", "en-US");
*/

      // connection.setInstanceFollowRedirects(true );

      connection.setUseCaches (false);
      connection.setDoInput(true);
      connection.setDoOutput(true);

      //Send request
      writeOutputStream( connection.getOutputStream (), data ); 

      //Get Response    
      String response = globInputStream( connection.getInputStream() );
 
      // response code is 302 here, should be checking goes to non-long redirect... 
/*
      Map< STRING, String> cookies = extractCookies( connection.getHeaderField("Set-Cookie") ); 

      // set the session id...
      System.out.println( "got JSESSIONID is " + cookies.get( "JSESSIONID" ) ); 
      this.sessionID = cookies.get( "JSESSIONID" );
*/
     this.sessionID  = connection.getHeaderField("Set-Cookie") .split(";")[ 0] ;

      System.out.println( "got sesssion " + this.sessionID  ); 
 

      String newUrl = connection.getHeaderField("location");
      System.out.println("login redirect newUrl: "+ newUrl );

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










public class WebEditor
{

  public static void main(String[] args)
    throws Exception
  {

    //sendGet(); 
//    HttpProxy c = new HttpProxy( "https://catalogue-123.aodn.org.au" ); 
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

    // String result = c.httpGet( "/geonetwork/srv/en/xml.group.list" ) ; 
    // System.out.println( "result is " + result ); 


//    String record = c.getRecord( "4402cb50-e20a-44ee-93e6-4728259250d2" );
    //// System.out.println( "record is " + record ); 

//      System.out.println("updating record" );
 //   c.updateRecord( "4402cb50-e20a-44ee-93e6-4728259250d2" , record ); 
 

 //   c. createUser( ); 


*/

    HttpProxy c = new HttpProxy( "https://catalogue-123.aodn.org.au" ); 
    // HttpProxy c = new HttpProxy( "http://127.0.0.1:8081" ); 
  
    c.enableSelfSignedSSL(); 

            
    c.login( "admin", "rqpxNDd8BS" );  // 10.11.12.13

//    c.login( "admin", "QhbHVQ75R9uP" );  // production 

 /*  
*/
    // ///////////////////////////
    System.out.println( "*** doing xml.usergroups.list "  ) ; 
    c.httpGet( "/geonetwork/srv/en/xml.usergroups.list?id=3" ) ; 


    // ///////////////////////////
    System.out.println( "*** doing post xml.usergroups.list "  ) ; 
     String data =
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + 
      "<request>" + 
      "  <id>3</id>" + 
      "<request> " 
      ;
    data = "";
    c.httpPostXML( "/geonetwork/srv/en/xml.usergroups.list", data ) ; 


/*
   String data = 
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + 
      "<request>" + 
      " <operation>newuser</operation>" +
      " <username>aaa</username>" +
      " <password>bbb</password>" +
      " <profile>Editor</profile>" +
      " <name>Samantha</name>" +
      " <city>Amsterdam</city>" +
      " <country>Netherlands</country>" +
      " <email>Netherlands</email>" +
      " <data/>" + 
      "</request>" ; 

    c.httpPostXML( "/geonetwork/srv/en/user.update", data ) ; 
*/
    

  } 

}
