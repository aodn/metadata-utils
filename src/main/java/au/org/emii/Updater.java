
// rm *.class ; javac main.java  ; java main

// time javac test2.java -cp .:netcdfAll-4.2.jar
// time java -cp .:postgresql-9.1-901.jdbc4.jar:netcdfAll-4.2.jar  test2


package au.org.emii;

//import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream ;
import java.io.IOException;

import java.io.FileInputStream;
import java.io.BufferedInputStream;

import java.util.ArrayList; //io.BufferedInputStream;
import java.util.List; //io.BufferedInputStream;
//import java.util.ArrayDouble; //io.BufferedInputStream;

import java.util.HashMap; //io.BufferedInputStream;
import java.util.Map; //io.BufferedInputStream;

import java.util.Date; 

import java.sql.*;

import java.util.Properties;
import java.lang.RuntimeException;

import java.text.SimpleDateFormat;
/*import java.util.List;
import java.util.Set;
import java.util.ArrayList;
*/
//import java.util.Date;

import ucar.nc2.NetcdfFileWriteable;
import ucar.nc2.Dimension;
//import ucar.nc2.DataType.DOUBLE;
//import ucar.nc2.*;

import ucar.ma2.DataType;
import ucar.ma2.Array;

import ucar.ma2.ArrayDouble;
import ucar.ma2.ArrayFloat;
import ucar.ma2.ArrayByte;
//import ucar.ma2.ArrayString;

import ucar.ma2.Index;

import java.util.Arrays;

import java.util.regex.Pattern ;
import java.util.regex.Matcher;


import java.util.Iterator;

/*
import au.org.emii.geoserver.extensions.filters.layer.data.FilterConfiguration;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.xml.sax.SAXException;
*/

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
// import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import java.util.AbstractMap.SimpleImmutableEntry;
// java.util.AbstractMap.SimpleImmutableEntry<K,V>;



//import java.util.StringTokenizer;

// string tokenizer isn't going to work because may not be strings.

// do everything twice?
// or leave the value there...


// two interfaces a builder to generate, and then a class to use.



import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;




class XSLTProcessor {

 private static final HashMap<String,Transformer> TRANSFORMER_CACHE = new HashMap<String,Transformer>();

 final static String CCD_NO_TEXT_SECTIONS = "/ccd_no_text_sections.xsl";
  
 private XSLTProcessor() {
   // no instantiation
 }

 private static Transformer getCCDTransformer() throws TransformerConfigurationException {

  Transformer transformer = TRANSFORMER_CACHE.get(CCD_NO_TEXT_SECTIONS);
  if (transformer == null) { 
   TransformerFactory tsf = TransformerFactory.newInstance();
         InputStream is = XSLTProcessor.class.getResourceAsStream(CCD_NO_TEXT_SECTIONS);
   transformer = tsf.newTransformer(new StreamSource(is));
   return transformer;
  }
  return transformer;
 }
 
 public static String stripTextSections(final String xmlString) throws TransformerConfigurationException,
        TransformerException, 
        TransformerFactoryConfigurationError {

        final StringReader xmlReader = new StringReader(xmlString);
        final StringWriter xmlWriter = new StringWriter();
        final Transformer ccdTransformer = getCCDTransformer();
        ccdTransformer.transform(new StreamSource(xmlReader),
            new StreamResult(xmlWriter));
        
        return xmlWriter.toString();
 }
}


class Updater 
{

	public Updater ()
	{

	}

	// not sure if we should do this here...

	// certainly configuration should be pulled out... and put in resources.

    public static Connection getConn() throws Exception
	{
		//String url = "jdbc:postgresql://127.0.0.1/postgres";

		// psql -h test-geoserver -U meteo -d harvest

		String url = "jdbc:postgresql://127.0.0.1/geonetwork";
		Properties props = new Properties();
		props.setProperty("user","meteo");
		props.setProperty("password","meteo");

//		props.setProperty("search_path","soop_sst,public");
/*
		String url = "jdbc:postgresql://dbprod.emii.org.au/harvest";
		Properties props = new Properties();
		props.setProperty("user","jfca");
		props.setProperty("password","fredfred");
*/

		props.setProperty("ssl","true");
		props.setProperty("sslfactory","org.postgresql.ssl.NonValidatingFactory");
		props.setProperty("driver","org.postgresql.Driver" );

		Connection conn = DriverManager.getConnection(url, props);
		/*if(conn == null) {
			throw new RuntimeException( "Could not get connection" );
		}*/
		return conn;
	}

	public static void main(String[] args) throws Exception
	{
		System.out.println( "hello world" ); 

		Connection conn = getConn(); 

		System.out.println( "conn " + conn ); 


		String query = "SELECT data FROM metadata ";

        PreparedStatement stmt = conn.prepareStatement( query );
        ResultSet rs =  stmt.executeQuery();

        System.out.println( "got some data " );
        int count = 0;
        while ( rs.next() ) 
        {

			System.out.println("**************");
			String s = (String) rs.getObject( 1);

			System.out.println(s);

            ++count;
			break;
        }
        System.out.println( "count " + count );

	}
}


