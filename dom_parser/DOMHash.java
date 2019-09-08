import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.*;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;


import java.io.*;

// Only synthesized hashing -- no inherited hashing
public class DOMHash {

    int hashNode(Node n) {
        int hash_value;

        int type = n.getNodeType();
        int totalhash;
        int child_hashvalue;
        int attrs_hashvalue;
        String tagname;
        Node attr;
        NamedNodeMap attrs;
        NodeList child_list;
        Node child;


        switch (type) {
          case Node.ATTRIBUTE_NODE:
            String k = n.getNodeName();
            String v = n.getNodeValue();
            String kv = k.concat(v);

            hash_value =  kv.hashCode();
            break;

          case Node.ELEMENT_NODE:
            tagname = n.getNodeName();
            attrs = n.getAttributes();
            attrs_hashvalue = 31;
            for (int i = 0; i < attrs.getLength(); i++) {
              attr = attrs.item(i);
              attrs_hashvalue = attrs_hashvalue ^ hashNode(attr);
            }

            child_hashvalue = 0;
            child_list = n.getChildNodes();
            for (int i = 0; i < child_list.getLength(); i++) {
              child = child_list.item(i);
              child_hashvalue = (child_hashvalue << 4) ^ (child_hashvalue >> 28) ^ hashNode(child);
            }

            totalhash = 0;
            totalhash = tagname.hashCode();
            totalhash = (totalhash << 4) ^ (totalhash >> 28) ^ attrs_hashvalue;
            totalhash = (totalhash << 4) ^ (totalhash >> 28) ^ child_hashvalue;

            hash_value =  totalhash;
            break;

          case Node.TEXT_NODE:
            hash_value =  n.getNodeValue().hashCode();
            break;

          default:
            tagname = n.getNodeName();
            child_hashvalue = 0;
            child_list = n.getChildNodes();
            for (int i = 0; i < child_list.getLength(); i++) {
              child = child_list.item(i);
              child_hashvalue = (child_hashvalue << 4) ^ (child_hashvalue >> 28) ^ hashNode(child);
            }
            totalhash = 0;
            totalhash = tagname.hashCode();
            totalhash = (totalhash << 4) ^ (totalhash >> 28) ^ child_hashvalue;
            hash_value =  totalhash;
            break;
        }
        return hash_value;
    }

	public static void main(String[] args) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser = dbf.newDocumentBuilder();
		Document document = parser.parse(new File(args[1]));

		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		Source schemaFile = new StreamSource(new File(args[0]));
		Schema schema = factory.newSchema(schemaFile);

		Validator validator = schema.newValidator();

		try {
			validator.validate(new DOMSource(document));
		} catch (SAXException e) {
			System.out.println("Validation failed!");
			return;
		}

        DOMHash dhash = new DOMHash();
        System.out.println(dhash.hashNode(document.getDocumentElement()));
	}
}
