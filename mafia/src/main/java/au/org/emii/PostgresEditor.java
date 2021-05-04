
package au.org.emii;

import java.io.*;
import java.nio.charset.StandardCharsets;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import java.util.Properties;

import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.validation.Validator;

import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Schema;
import javax.xml.XMLConstants;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;

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

  private boolean isValid = true;
  public boolean isValid()
  {
      return isValid;
  }

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
      isValid = false;
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

public class PostgresEditor {

    private static Transformer getTransformerFromFile(String filename)
        throws FileNotFoundException, TransformerConfigurationException {

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

        props.setProperty("ssl","true");
        props.setProperty("sslfactory","org.postgresql.ssl.NonValidatingFactory");
        props.setProperty("driver","org.postgresql.Driver");

        return DriverManager.getConnection(url, props);
    }

    private static String getUUIDsFromFile(String filename) throws IOException {
        StringBuffer result = new StringBuffer();

        BufferedReader br = new BufferedReader(new FileReader(filename));
        while (br.ready()) {
            if(result.length()!=0)
                result.append(", ");
            result.append("'"+br.readLine()+"'");
        }
        return result.toString();
    }

    private static void writeDocument(Document document, String filename) throws IOException, TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(document), new StreamResult(new File(filename)));
    }

    private static void write(String data, String filename, boolean append) throws IOException {
        FileWriter fw = new FileWriter(filename, append); //the true will append the new data
        fw.write(data);//appends the string to the file
        fw.close();
    }

    public static void main(String[] args) throws Exception {

        Options options = new Options();

        options.addOption("help", false, "show help");

        // connection credentials
        options.addOption("url", true, "jdbc connection string, eg. jdbc:postgresql://127.0.0.1/geonetwork");
        options.addOption("user", true, "user");
        options.addOption("pass", true, "password");

        // selection
        options.addOption("uuid", true, "etl applies to specific metadata record");
        options.addOption("all", false, "etl applies to all metadata records");
        options.addOption("selected", true, "etl applies to selected metadata records");
        options.addOption("isharvested", true, "etl applies to harvested or non-harvested metadata records depending upon provided value (y or n)");
        options.addOption("istemplate", true, "etl applies to template records depending upon provided value (y or n)");

        // additional metadata context fields
        options.addOption("context", false, "expose additional metadata fields (eg. record uuid) to etl");

        // transform 
        options.addOption("transform", true, "transform stylesheet (.xslt) file to use");

        // validation
        options.addOption("validate", true, "validation schema (.xsd) file to use or provide schema folder (schema.xsd will be selected depending upon schema type)");
        options.addOption("invalids", true, "Provide a name of the file to list all invalid uuids.");
        options.addOption("xmloutputpath", true, "Provide a folder path to store xml files.");

        // output
        options.addOption("stdout", false, "output to stdout");
        options.addOption("update", false, "perform inplace update of the metadata record");


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


        /*
        STAGE 1: get the meta data from the database
         */

        String query = "SELECT * FROM metadata ";

        // uuid or all
        query += " where 1=1 ";
        if(cmd.hasOption("uuid")) {
            query += " and uuid = '" + cmd.getOptionValue("uuid") + "'";
        } else if(cmd.hasOption("selected")) {
            query += " and uuid IN (" + getUUIDsFromFile(cmd.getOptionValue("selected")) + ")";
        } else if(cmd.hasOption("all")) {
            ;
        } else {
            throw new Exception("Options should include one of -uuid or -all or -selected");
        }

        // apply harvested filter
        if(cmd.hasOption("isharvested")) {
            query += " and isharvested = '" + cmd.getOptionValue("isharvested") + "'";
        }

        // apply istemplate filter
        if(cmd.hasOption("istemplate")) {
            query += " and istemplate = '" + cmd.getOptionValue("istemplate") + "'";
        }

        query += " order by uuid";

        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        /*
        STAGE 2: get the transform
         */

        // setup the identity transform
        Transformer identity = getIdentityTransformer();

        Transformer transformer = null;
        if(cmd.hasOption("transform")) {
            transformer = getTransformerFromFile(cmd.getOptionValue("transform"));
        } else {
            transformer = identity;
        }

        /*
        STAGE 3: transform each record
         */

        // Empty invalid records list file
        if(cmd.hasOption("invalids")) {
            new FileWriter(cmd.getOptionValue("invalids")).close();
        }

        int count = 0;
        while(rs.next()) {
            int id = rs.getInt("id");
            String data = rs.getString("data");
            String uuid = (String) rs.getObject("uuid");
            String schemaid = (String) rs.getObject("schemaid");

            System.out.println( "----------------------------------------------------" );
            System.out.println( "id: " + id + " uuid: " + uuid + " schemaid: " + schemaid );
            System.out.println( "----------------------------------------------------" );

            // decode record
            InputStream is = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);

            /*
            STAGE 3a: Add extra "context" nodes to the XML
            */

            if(cmd.hasOption("context")) {

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

            /*
            STAGE 3b: Perform the transformation
             */

            // write original XML
            if(cmd.hasOption("xmloutputpath"))
                writeDocument(document, cmd.getOptionValue("xmloutputpath")+"/"+uuid+"/metadata/metadata.xml");

            // perform requested transform
            DOMResult output = new DOMResult();
            transformer.transform(new DOMSource(document), output);
            document = (Document) output.getNode();

            if(cmd.hasOption("context") && !cmd.hasOption("stdout")) {
                // pick out the transformed record
                XPath xpath = XPathFactory.newInstance().newXPath();
                Node myNode = (Node) xpath.compile("//root/record/*").evaluate(document, XPathConstants.NODE);

                // remove current children
                while (document.getFirstChild() != null) {
                    document.removeChild(document.getFirstChild());
                }

                // re-add the transformed record (only)
                document.appendChild(myNode);
            }

            // write transformed XML
            if(cmd.hasOption("xmloutputpath") && cmd.hasOption("transform"))
                writeDocument(document, cmd.getOptionValue("xmloutputpath")+"/"+uuid+"/metadata/metadata.transform.xml");

            // the double-handling back to text is to enable us to extract line numbers
            Writer writer = new StringWriter();
            identity.transform(new DOMSource(document), new StreamResult(writer));
            data = writer.toString();

            /*
            STAGE 3c: validate result
             */

            if(cmd.hasOption("validate")) {
                String filename = cmd.getOptionValue("validate");
                if(filename.indexOf(".xsd")==-1)
                    filename+="/"+schemaid+"/schema.xsd";
                System.out.println( "validation xsd filename '" + filename + "'" );

                SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                Schema schema = schemaFactory.newSchema( new File( filename ) );
                Validator validator = schema.newValidator();
                MyValidationErrorHandler myValidationErrorHandler = new MyValidationErrorHandler();
                validator.setErrorHandler(myValidationErrorHandler);
                validator.validate( new StreamSource(new StringReader(data)));
                if(cmd.hasOption("invalids")) {
                    if(!myValidationErrorHandler.isValid())
                        write(uuid+"\n", ""+cmd.getOptionValue("invalids"), true);
                }
            }

            /*
            STAGE 3d: Output result (stdout or database)
             */

            if(cmd.hasOption("stdout")) {
                System.out.println(data);
            }
            else if(cmd.hasOption("update")) {
                // update the db
                PreparedStatement updateStmt = conn.prepareStatement("update metadata set data=?, changedate=to_char(current_timestamp, 'YYYY-MM-DD\"T\"HH24:MI:SS') where id=?");
                updateStmt.setString(1, data);
                updateStmt.setInt(2, id);
                updateStmt.executeUpdate();
                updateStmt.close();
            }

            ++count;
        }

        stmt.close();
        rs.close();
        conn.close();

        System.out.println( "----------------------------------------------------" );
        System.out.println("records processed " + count);
    }
}


