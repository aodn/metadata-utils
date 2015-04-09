/*

java -cp  ./target/myartifcat-1.0.0.jar  au.org.emii.PostgresEditor  -url jdbc:postgresql://127.0.0.1:5432/geonetwork -u geonetwork -p geonetwork   -uuid 4402cb50-e20a-44ee-93e6-4728259250d2  -t trans-2.0.xslt   -stdout

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





public class PostgresEditor 
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



	public static void main(String[] args)
		throws Exception 
	{
		
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
			throw new Exception( "Options should include -uuid, -all or -stdout" );
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


