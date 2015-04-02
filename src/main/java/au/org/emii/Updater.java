

package au.org.emii;

import java.io.InputStream ;
import java.io.FileInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Reader;

import java.sql.*;
import java.sql.SQLException;

import java.util.Properties;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.cli.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;




import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
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


// xslt examples,
// http://fandry.blogspot.com.au/2012/04/java-xslt-processing-with-saxon.html


class BadCLIArgumentsException extends Exception
{
	public BadCLIArgumentsException( String message )
	{
		super(message);
	}
}



class ConnectHttps {


  ConnectHttps( String serverURL )
  {
    this.serverURL = serverURL;
  }

  final String serverURL ;

	private final static String USER_AGENT = "Mozilla/5.0";
  

	public void init() throws Exception
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


  public void request( String url ) throws Exception
  {
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


  // should return a string...
  public void getRecord( String uuid ) throws Exception {
  
    String surl = serverURL + "/geonetwork/srv/eng/xml.metadata.get?uuid=" + uuid; 

	  // String surl = "http://www.cnn.com";
    URL url = new URL( surl );// "https://securewebsite.com");
    URLConnection con = url.openConnection();
    Reader reader = new InputStreamReader(con.getInputStream());
    while (true) {
      int ch = reader.read();
      if (ch==-1) {
        break;
      }
      System.out.print((char)ch);
    }
  }



}




class Updater
{

	public Updater ()
	{ }

	private static Transformer getTransformer( String filename )
		throws FileNotFoundException, TransformerConfigurationException
	{
		final TransformerFactory tsf = TransformerFactory.newInstance();
		final InputStream is  = new FileInputStream( filename );

		return tsf.newTransformer(new StreamSource(is));
	}

	private static String transform ( Transformer transformer, String xmlString)
		throws TransformerException
	{
		final StringReader xmlReader = new StringReader(xmlString);
		final StringWriter xmlWriter = new StringWriter();

		transformer.transform(new StreamSource(xmlReader), new StreamResult(xmlWriter));

		return xmlWriter.toString();
	}

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


	public static void main(String[] args)
		throws FileNotFoundException, TransformerConfigurationException, TransformerException,
			SQLException, ParseException, BadCLIArgumentsException, IOException 
			, Exception
	{

//		sendGet(); 


    ConnectHttps a = new ConnectHttps( "https://catalogue-123.aodn.org.au" ); 
	  a.init(); 
//    a.getRecord( "4402cb50-e20a-44ee-93e6-4728259250d2" );
    
    a.request( "https://catalogue-123.aodn.org.au/geonetwork/srv/eng/xml.metadata.get?uuid=4402cb50-e20a-44ee-93e6-4728259250d2" );

    // a.connect() ; 
 
 

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
			transformer = getTransformer(cmd.getOptionValue("t") );
		}

        int count = 0;
        while ( rs.next() )
        {
			int id = (Integer) rs.getObject("id");
			String uuid = (String) rs.getObject("uuid");
			String data = (String) rs.getObject("data");

			if( transformer != null ) {
				data = transform( transformer, data);
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


