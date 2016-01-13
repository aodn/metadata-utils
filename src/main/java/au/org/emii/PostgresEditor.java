
package au.org.emii;

import java.io.FileInputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import java.nio.charset.StandardCharsets;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import java.util.Properties;

import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerConfigurationException;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathConstants;

import javax.xml.XMLConstants;
// import javax.xml.validation.*;
import java.io.File;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import java.io.StringReader; 

import org.xml.sax.ErrorHandler;
//import org.xml.sax.InputSource;
// import org.xml.sax.SAXException;
// import org.xml.sax.SAXParseException;
// import org.xml.sax.XMLReader;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Binder;

// import javax.xml.bind.Validator;
import javax.xml.validation.Validator;

import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Schema;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;





class MyValidationErrorHandler implements ErrorHandler {

    // pass the uuid in to output better errors...

  public void warning(SAXParseException e) throws SAXException {
    show("Warning", e);
  }

  public void error(SAXParseException e) throws SAXException {
    show("Error", e);
  }

  public void fatalError(SAXParseException e) throws SAXException {
    show("Fatal Error", e);
  }

  private void show(String type, SAXParseException e) {

      System.out.println( 
          e.getLineNumber() 
          + ":" 
          + e.getColumnNumber() 
          + " "
          + type 
          + " " 
          + e.getMessage()
          + " " 
          + e.getSystemId()
      );
  }

}



// TODO perhaps change name to Main to make easier to call from cli? 

public class PostgresEditor {

    private static Transformer getTransformerFromFile(String filename)
        throws FileNotFoundException, TransformerConfigurationException {

        // TODO this is terrible, should work with a stream, and let the caller set this up.
        // similarly for the next function
        final TransformerFactory tsf = TransformerFactory.newInstance();
        final InputStream is  = new FileInputStream(filename);

        return tsf.newTransformer(new StreamSource(is));
    }

    private static Transformer getIdentityTransformer()
    throws FileNotFoundException, TransformerConfigurationException {

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();

        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        return transformer;
    }


    private static Connection getConn(String url, String user, String pass) throws SQLException {
        Properties props = new Properties();

        props.setProperty("user", user);
        props.setProperty("password", pass);

        // props.setProperty("search_path","soop_sst,public");
        props.setProperty("ssl","true");
        props.setProperty("sslfactory","org.postgresql.ssl.NonValidatingFactory");
        props.setProperty("driver","org.postgresql.Driver");

        return DriverManager.getConnection(url, props);
    }



    public static void main(String[] args) throws Exception {

        Options options = new Options();
        options.addOption("url", true, "jdbc connection string, eg. jdbc:postgresql://127.0.0.1/geonetwork");
        options.addOption("user", true, "user");
        options.addOption("pass", true, "password");
        options.addOption("uuid", true, "apply action to specific metadata record");
        options.addOption("all", false, "apply actions on all metadata records");

        options.addOption("help", false, "show help");
        options.addOption("transform", true, "stylesheet xslt file to use for transform");
        options.addOption("validate", true, "schema xsd file to use for validation");

        options.addOption("stdout", false, "dump raw metadata record to stdout");
        options.addOption("stdout_with_context", false, "dump metadata record with additional context fields to stdout");
        options.addOption("update", false, "perform inplace update of the metadata record");

        options.addOption("title", false, "output id/uuid of the metadata record to stdout");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if(cmd.hasOption("help") || !cmd.hasOption("url") || !cmd.hasOption("user") || !cmd.hasOption("pass")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Updater", options);
            return;
        }

        Connection conn = getConn(
            cmd.getOptionValue("url"),
            cmd.getOptionValue("user"),
            cmd.getOptionValue("pass")
        );

        // String query = "SELECT id,uuid,data FROM metadata ";
        String query = "SELECT * FROM metadata ";

        // all is kind of implied - but good to be explicit
        // uuid or all
        if(cmd.hasOption("uuid")) {
            query += " where uuid = '" + cmd.getOptionValue("uuid") + "'";
        } else if(cmd.hasOption("all")) {
            ;
        } else {
            throw new Exception("Options should include one of -uuid or -all");
        }


        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();


        // setup the identity transform
        Transformer identity = getIdentityTransformer();


        Transformer transformer = null;
        if(cmd.hasOption("transform")) {
            transformer = getTransformerFromFile(cmd.getOptionValue("transform"));
        } else {
            transformer = identity;
        }

        // TODO this crap really wants to be factored, so we pass the action and the transformer in...
        // loop records
        int count = 0;
        while(rs.next()) {
            int id = rs.getInt("id");
            String data = rs.getString("data");
            String uuid = (String) rs.getObject("uuid");

            if(cmd.hasOption("title")) {
                System.out.println( "-----------------------------------------" );
                System.out.println( "id: " + id + " uuid: " + uuid );
                System.out.println( "-----------------------------------------" );
            }
     

            // TODO - should factor db and xml processing into separate classes/methods

            // decode record
            InputStream is = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);

            if( false ) {

                Node record = document.getFirstChild();

                // create new root element
                Element root = document.createElement("root");
                document.removeChild(record);
                document.appendChild(root);

                // add context
                Element context = document.createElement("context");
                root.appendChild(context);

                // add record
                Element recordNode = document.createElement("record");
                recordNode.appendChild(record);
                root.appendChild(recordNode);


                // loop fields and add to the context
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
                    Text nodeValue = document.createTextNode(formattedValue);
                    node.appendChild(nodeValue);

                    context.appendChild(node);
                }
            }


            // perform requested transform
            DOMResult output = new DOMResult();
            transformer.transform(new DOMSource(document), output);
            document = (Document) output.getNode();

            if( false ) {
                // TODO better names
                // pick out the transformed record
                XPath xpath = XPathFactory.newInstance().newXPath();
                Node myNode = (Node) xpath.compile("//root/record/*").evaluate(document, XPathConstants.NODE);
                
                document = (Document) myNode;
            }

            // we'll do validation here,,,

            // the double-handling back to text is to enable us to extract line numbers
            Writer writer = new StringWriter();
            identity.transform(new DOMSource(document), new StreamResult(writer));
            data = writer.toString();

            if(cmd.hasOption("validate")) {
                String filename = cmd.getOptionValue("validate");
                System.out.println( "validation xsd filename is " + filename );

                SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                // Schema schema = schemaFactory.newSchema( new File( "../schema-plugins/iso19139.mcp-2.0/schema.xsd") );
                Schema schema = schemaFactory.newSchema( new File( filename ) );
                Validator validator = schema.newValidator();

                validator.setErrorHandler(  new MyValidationErrorHandler() );
                validator.validate( new StreamSource(new StringReader(data)));
                // validator.validate(  new DOMSource( myNode) );
            }



            if(cmd.hasOption("stdout")) {
                // emit without context fields
                System.out.println(data);
            }
            else if(cmd.hasOption("update")) {
                // update the db
                PreparedStatement updateStmt = conn.prepareStatement("update metadata set data=? where id=?");
                updateStmt.setString(1, data);
                updateStmt.setInt(2, id);
                updateStmt.executeUpdate();
                // close...?
                // TODO should be finally, using.
                updateStmt.close();
            } else {
                // ok, 
            }

            ++count;
        }

        // TODO finally, using...
        stmt.close();
        rs.close();
        conn.close();

        System.out.println("records processed " + count);
    }
}


