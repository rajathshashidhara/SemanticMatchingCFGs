/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlhash;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author soursugar
 */
public class XMLHash {

    /**
     * @param args the command line arguments. First argument corresponds to XSD
     * file. Other arguments are XML files hashed into a table.
     */
    public static void main(String[] args) {
        try {
            XMLValidatingParser p = new XMLValidatingParser(args[0]);
            SchemaPreprocessor spreproc = new SchemaPreprocessor(args[0], p);
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(XMLHash.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
