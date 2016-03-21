
package au.org.emii;

import java.io.FileInputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.File;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.validation.Validator;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.XMLConstants;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import org.w3c.dom.Document;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;


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


public class Main {

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


    public static void main(String[] args) throws Exception {

        Options options = new Options();

        options.addOption("help", false, "show help");
        // source
        options.addOption("source", true,     "source doc (.xml) to use");
        // transform
        options.addOption("transform", true, "transform stylesheet (.xslt) file to use");
        options.addOption("validate", true,  "validation schema (.xsd) file to use");
        // output
        options.addOption("stdout", false,    "output to stdout");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if(!cmd.hasOption("source")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Updater", options);
            return;
        }

        Transformer identity = getIdentityTransformer();

        // setup up the user transform
        Transformer transformer = null;
        if(cmd.hasOption("transform")) {
            String filename = cmd.getOptionValue("transform");
            System.out.println( "transform xml filename '" + filename + "'" );
            transformer = getTransformerFromFile(filename);
        } else {
            transformer = identity;
        }

        InputStream is = new FileInputStream(cmd.getOptionValue("source"));

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // Note the awfulness. Not needed for saxon.
        factory.setNamespaceAware(true);
        Document document = factory.newDocumentBuilder().parse(is);


        // perform requested transform
        DOMResult output = new DOMResult();
        transformer.transform(new DOMSource(document), output);
        document = (Document) output.getNode();


        // the double-handling back to text is to enable us to extract line numbers
        Writer writer = new StringWriter();
        identity.transform(new DOMSource(document), new StreamResult(writer));
        String data = writer.toString();

        if(cmd.hasOption("validate")) {
            String filename = cmd.getOptionValue("validate");
            System.out.println( "validation xsd filename '" + filename + "'" );

            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            // Schema schema = schemaFactory.newSchema( new File( "../schema-plugins/iso19139.mcp-2.0/schema.xsd") );
            Schema schema = schemaFactory.newSchema( new File( filename ) );
            Validator validator = schema.newValidator();

            validator.setErrorHandler(  new MyValidationErrorHandler() );
            validator.validate( new StreamSource(new StringReader(data)));
            // validator.validate(  new DOMSource( myNode) );
        }


        if(cmd.hasOption("stdout")) {
            System.out.println(data);
        }
    }
}

