/*

# Update all records
java -cp  ./target/myartifcat-1.0.0.jar  au.org.emii.PostgresEditor  -url jdbc:postgresql://10.11.12.13:5432/geonetwork -u geonetwork -p geonetwork    -t trans-2.0.xslt   -all -update

java -cp  ./target/myartifcat-1.0.0.jar  au.org.emii.PostgresEditor  -url jdbc:postgresql://10.11.12.13:5432/geonetwork -u geonetwork -p geonetwork   -uuid 4402cb50-e20a-44ee-93e6-4728259250d2  -t trans-2.0.xslt   -stdout

*/



package au.org.emii;


import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.io.ByteArrayInputStream;

import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;


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
        // TODO this is terrible, should work with a stream, and let the caller set this up.
        // similarly for the next function
        final TransformerFactory tsf = TransformerFactory.newInstance();
        final InputStream is  = new FileInputStream( filename );

        return tsf.newTransformer(new StreamSource(is));
    }

    public static Transformer getIdentityTransformer()
        throws FileNotFoundException, TransformerConfigurationException
    {
 
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();

        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        return transformer; 
    }

/*
    public static Transformer getTransformerFromString ( String input )
        throws FileNotFoundException, TransformerConfigurationException
    {
        final TransformerFactory tsf = TransformerFactory.newInstance();

    InputStream is = new ByteArrayInputStream( input.getBytes());//StandardCharsets.UTF_8));

//      final InputStream is  = new StringInputStream( input );

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
*/


  /* TODO remove - shouldn't ever need string -> string transform */

/*
    public static String transform ( Transformer transformer, String xmlString)
        throws TransformerException
    {
        final StringReader xmlReader = new StringReader(xmlString);
        final StringWriter xmlWriter = new StringWriter();

        transformer.transform(new StreamSource(xmlReader), new StreamResult(xmlWriter));

        return xmlWriter.toString();
    }
*/
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
        options.addOption("stdout", false, "dump raw result to stdout");
        options.addOption("stdout2", false, "dump transformed record to stdout");
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

        // String query = "SELECT id,uuid,data FROM metadata ";
        String query = "SELECT * FROM metadata ";

        // uuid or all
        if(cmd.hasOption("uuid")) {
            query += " where uuid = '" + cmd.getOptionValue("uuid") + "'";
        }
        else if(cmd.hasOption("all")) {
            ;
        }
        else {
            throw new Exception( "Options should include one of -uuid or -all" );
        }

/*
        // we could get rid of stdout
        if( !( cmd.hasOption("stdout") || cmd.hasOption( "update")) ) {
            throw new Exception( "Options should include one of -stdout or -update" );
        }
*/

        PreparedStatement stmt = conn.prepareStatement( query );
        ResultSet rs = stmt.executeQuery();


        // setup the identity transform
        Transformer identity = Misc.getIdentityTransformer(); 


        Transformer transformer = null;
        if( cmd.hasOption("t")) {
            transformer = Misc.getTransformerFromFile( cmd.getOptionValue("t") );
        }
        else {
            transformer = identity; 
        }

        int count = 0;
        while ( rs.next() )
        {
            int id = rs.getInt("id");
            String data = rs.getString("data");
            // String uuid = (String) rs.getObject("uuid");
            // System.out.println( "id " + id + ", uuid " + uuid );

            // context stuff...

            // decode record
            InputStream is = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is );
            Node record = document.getFirstChild();

            // create new root element
            Element root = document.createElement("root");
            document.removeChild( record );
            document.appendChild( root );

            // add context
            Element context = document.createElement("context");
            root.appendChild( context);

            // add record
            Element recordNode = document.createElement("record");
            recordNode.appendChild(record);
            root.appendChild( recordNode);


            // loop resultset and and add db fields context 
            ResultSetMetaData md = rs.getMetaData();
            int columns = md.getColumnCount();
            for(int i=1; i<=columns; i++)  {
                String name =  md.getColumnName(i);

                if(name.equals("data")) // ignore the actual record
                    continue;

                Object value = rs.getObject(i);
                String formattedValue = "";
                if(value == null)
                    formattedValue = "";
                else
                    formattedValue = value.toString();

                Element node = document.createElement(name);
                Text nodeValue = document.createTextNode( formattedValue );
                node.appendChild(nodeValue);

                context.appendChild( node );
            }


            // do the transform
            DOMResult output = new DOMResult();
            transformer.transform( new DOMSource( document ), output);
            document = (Document) output.getNode();


            
            // TODO remove this, should use the identity transform
            if( cmd.hasOption("stdout")) {
                //  format the output
                Writer writer = new StringWriter();
                identity.transform( new DOMSource( document ), new StreamResult( writer ));
                System.out.println( writer.toString() );
            }
    

            // pick out the recod
            XPath xpath = XPathFactory.newInstance().newXPath();
            Node myNode = (Node) xpath.compile("//root/record/*").evaluate( document, XPathConstants.NODE);           
            Writer writer = new StringWriter();
            identity.transform( new DOMSource( myNode), new StreamResult( writer ));
            data = writer.toString();

            if( cmd.hasOption("stdout2")) {
                System.out.println( data );
            }


            if( cmd.hasOption("update")) {

                PreparedStatement updateStmt = conn.prepareStatement( "update metadata set data=? where id=?" ) ;
                updateStmt.setString(1, data );
                updateStmt.setInt(2, id );
                updateStmt.executeUpdate();
                // close...?
                // TODO should be finally.
                updateStmt.close();
            }

            ++count;
        }


        stmt.close();
        rs.close();
        conn.close();

        System.out.println( "records processed " + count );
    }
}


