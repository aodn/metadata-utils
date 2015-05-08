
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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;



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
        props.setProperty("password",pass);

        // props.setProperty("search_path","soop_sst,public");
        props.setProperty("ssl","true");
        props.setProperty("sslfactory","org.postgresql.ssl.NonValidatingFactory");
        props.setProperty("driver","org.postgresql.Driver");

        return DriverManager.getConnection(url, props);
    }



    public static void main(String[] args) throws Exception {

        Options options = new Options();
        options.addOption("url", true, "jdbc connection string, eg. jdbc:postgresql://127.0.0.1/geonetwork");
        options.addOption("u", true, "user");
        options.addOption("p", true, "password");
        options.addOption("uuid", true, "metadata record uuid");
        options.addOption("help", false, "show help");
        options.addOption("t", true, "xslt file for transform");
        options.addOption("stdout", false, "dump with context fields to stdout");
        options.addOption("stdout2", false, "dump to stdout");
        options.addOption("update", false, "actually update the record in db");
        options.addOption("all", false, "do all records");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if(cmd.hasOption("help") || !cmd.hasOption("url") || !cmd.hasOption("u") || !cmd.hasOption("u")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Updater", options);
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
        if(cmd.hasOption("t")) {
            transformer = getTransformerFromFile(cmd.getOptionValue("t"));
        } else {
            transformer = identity;
        }

        int count = 0;
        while(rs.next()) {
            int id = rs.getInt("id");
            String data = rs.getString("data");
            // String uuid = (String) rs.getObject("uuid");
            // System.out.println( "id " + id + ", uuid " + uuid );

            // context stuff...

            // decode record
            InputStream is = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
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
                Text nodeValue = document.createTextNode(formattedValue);
                node.appendChild(nodeValue);

                context.appendChild(node);
            }


            // do the transform
            DOMResult output = new DOMResult();
            transformer.transform(new DOMSource(document), output);
            document = (Document) output.getNode();



            if(cmd.hasOption("stdout")) {
                //  format the output
                Writer writer = new StringWriter();
                identity.transform(new DOMSource(document), new StreamResult(writer));
                System.out.println(writer.toString());
            }


            // pick out the recod
            XPath xpath = XPathFactory.newInstance().newXPath();
            Node myNode = (Node) xpath.compile("//root/record/*").evaluate(document, XPathConstants.NODE);
            Writer writer = new StringWriter();
            identity.transform(new DOMSource(myNode), new StreamResult(writer));
            data = writer.toString();

            if(cmd.hasOption("stdout2")) {
                System.out.println(data);
            }


            if(cmd.hasOption("update")) {

                PreparedStatement updateStmt = conn.prepareStatement("update metadata set data=? where id=?");
                updateStmt.setString(1, data);
                updateStmt.setInt(2, id);
                updateStmt.executeUpdate();
                // close...?
                // TODO should be finally.
                updateStmt.close();
            }

            ++count;
        }

        // TODO finally
        stmt.close();
        rs.close();
        conn.close();

        System.out.println("records processed " + count);
    }
}


