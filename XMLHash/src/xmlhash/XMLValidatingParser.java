/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlhash;

import java.io.File;
import java.io.IOException;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author soursugar
 */
public class XMLValidatingParser {
    
    DocumentBuilder parser;
    Validator validator;
    
    public XMLValidatingParser(String xsdfile) 
            throws ParserConfigurationException, SAXException {        
        DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
        parser = dbfactory.newDocumentBuilder();
        
        SchemaFactory schemafactory = 
                SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        File schemafile = new File(xsdfile);
        Schema schema = schemafactory.newSchema(schemafile);
        validator = schema.newValidator();
    }
    
    Document validate(String xmlfile) 
            throws SAXException, IOException, IllegalArgumentException {
        File inputxmlfile = new File(xmlfile);
        Document document = parser.parse(inputxmlfile);
        
        try {
            validator.validate(new DOMSource(document));
        } catch (IllegalArgumentException e) {
            throw e;
        }
        
        return document;
    }
    
    Document parse(String xmlfile) throws SAXException, IOException {
        File inputxmlfile = new File(xmlfile);
        Document document = parser.parse(inputxmlfile);
        return document;
    }
}
