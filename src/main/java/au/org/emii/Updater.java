

package au.org.emii;

import java.io.InputStream ;
import java.io.FileInputStream;
import java.io.StringReader;
import java.io.StringWriter;

import java.sql.*;

import java.util.Properties;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.cli.*;


class XSLTProcessor {

	// http://fandry.blogspot.com.au/2012/04/java-xslt-processing-with-saxon.html

	public XSLTProcessor() {
	// no instantiation
	}

	private Transformer getCCDTransformer() throws Exception 
	{
		final TransformerFactory tsf = TransformerFactory.newInstance();
		final InputStream is  = new FileInputStream( "trans.xslt" ); 
		return tsf.newTransformer(new StreamSource(is));
	}

	public String stripTextSections(final String xmlString) throws Exception 
	{
		final StringReader xmlReader = new StringReader(xmlString);
		final StringWriter xmlWriter = new StringWriter();
		final Transformer ccdTransformer = getCCDTransformer();

		ccdTransformer.transform(new StreamSource(xmlReader), new StreamResult(xmlWriter));
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

    public static Connection getConn( String url, String user, String pass) throws Exception
	{
		//String url = "jdbc:postgresql://127.0.0.1/postgres";
		// psql -h test-geoserver -U meteo -d harvest

//		String url = "jdbc:postgresql://127.0.0.1/geonetwork";
		Properties props = new Properties();
		props.setProperty("user", user );
		props.setProperty("password",pass );

//		props.setProperty("search_path","soop_sst,public");

		props.setProperty("ssl","true");
		props.setProperty("sslfactory","org.postgresql.ssl.NonValidatingFactory");
		props.setProperty("driver","org.postgresql.Driver" );

		return DriverManager.getConnection(url, props);
		/*if(conn == null) {
			throw new RuntimeException( "Could not get connection" );
		}*/
	}

	public static void main(String[] args) throws Exception
	{
		System.out.println( "hello world" ); 

		Options options = new Options();

		// add t option
		options.addOption("url", true, "jdbc connection string, eg. jdbc:postgresql://127.0.0.1/geonetwork");
		options.addOption("u", true, "user");
		options.addOption("p", true, "password");

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse( options, args);

		String url = cmd.getOptionValue("url");
		String user = cmd.getOptionValue("u");
		String pass = cmd.getOptionValue("p");

		System.out.println( "conn string " + url + " " + user + " " + pass );

/*
		if(cmd.hasOption("u")) {
			// print the date and time
	       System.out.println( "has t" );

			String countryCode = cmd.getOptionValue("t");
			if(countryCode == null) {
				System.out.println( "no argument" );
			} 
			
		}
		else {
			// print the date
	       System.out.println( "doesn't have has t" );
		}

*/
		Connection conn = getConn( url, user, pass); 

		System.out.println( "conn " + conn ); 


		String query = "SELECT data FROM metadata where uuid = '4402cb50-e20a-44ee-93e6-4728259250d2' ";

        PreparedStatement stmt = conn.prepareStatement( query );
        ResultSet rs =  stmt.executeQuery();

        System.out.println( "got some data " );
        int count = 0;
        while ( rs.next() ) 
        {

			System.out.println("**************");
			String s = (String) rs.getObject( 1);
		//	System.out.println(s);

//			String result = new XSLTProcessor(). stripTextSections( s );  
//			System.out.println(result);
	
            ++count;
			break;
        }
        System.out.println( "count " + count );

	}
}


