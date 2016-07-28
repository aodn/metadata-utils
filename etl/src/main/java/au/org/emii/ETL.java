
package au.org.emii;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerConfigurationException;

// import javax.xml.validation.*;
// import javax.xml.bind.Validator;
import javax.xml.validation.Validator;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Schema;
import javax.xml.XMLConstants;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;

import org.w3c.dom.Document;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import org.apache.commons.io.IOUtils;


class ValidationErrorHandler implements ErrorHandler {

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


class Helpers {

    public static Transformer getTransformerFromFile(InputStream is)
        throws TransformerConfigurationException {

        TransformerFactory tsf = TransformerFactory.newInstance();

        return tsf.newTransformer(new StreamSource(is));
    }

    public static Transformer getIdentityTransformer()
        throws FileNotFoundException, TransformerConfigurationException {

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();

        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        return transformer;
    }

    public static String readStdin() throws IOException {

        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            Writer out = new StringWriter()
        ) {
            IOUtils.copy(in, out);
            return out.toString();
        }
    }
}


public class ETL {

    public static void main(String[] args) throws Exception {

        Options options = new Options();

        options.addOption("help",       false,  "show help");
        options.addOption("file",       true,   "input file (.xml). No file indicates stdin");
        options.addOption("transform",  true,   "transform stylesheet file (.xslt)");
        options.addOption("validate",   true,   "validation schema file (.xsd)");
        options.addOption("text",       false,  "specify output type is text not xml by default");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if(cmd.hasOption("help")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Updater", options);
            return;
        }

        try (InputStream is = 
            (cmd.hasOption("file") && cmd.getOptionValue("file") != "-") 
                ? new FileInputStream(cmd.getOptionValue("file"))
                : new ByteArrayInputStream(Helpers.readStdin().getBytes(StandardCharsets.UTF_8))
        ) { 

            // setup transform
            Transformer transform = null;
            if(cmd.hasOption("transform")) {
                try (
                    InputStream transformIS = new FileInputStream(cmd.getOptionValue("transform"))
                ) {
                    transform = Helpers.getTransformerFromFile(transformIS);
                }
            } else {
                transform = Helpers.getIdentityTransformer();
            }

            // extract document node
            final Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);

            if(cmd.hasOption("text")) {

                // if output is text type, then we must avoid generating a DOMSource output
                StringWriter writer = new StringWriter();
                transform.transform(new DOMSource(document), new StreamResult(writer));
                System.out.println(writer.toString());
            } else {

                // transform to new dom
                DOMResult output = new DOMResult();
                transform.transform(new DOMSource(document), output);

                // and transform back to text,
                // the double-handling allows us to report correct line numbers when doign validation
                Writer writer = new StringWriter();
                Helpers.getIdentityTransformer().transform(new DOMSource(output.getNode()), new StreamResult(writer));
                String data = writer.toString();

                if(cmd.hasOption("validate")) {

                    String filename = cmd.getOptionValue("validate");
                    System.out.println( "validation xsd filename '" + filename + "'" );

                    SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

                    Schema schema = schemaFactory.newSchema(new File(filename));
                    Validator validator = schema.newValidator();

                    validator.setErrorHandler(new ValidationErrorHandler());
                    validator.validate(new StreamSource(new StringReader(data)));
                }

                System.out.println(data);
            }
        }
    }
}


