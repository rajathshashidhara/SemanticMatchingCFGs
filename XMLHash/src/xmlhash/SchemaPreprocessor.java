/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlhash;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author soursugar
 */
public class SchemaPreprocessor {
    
    Map<String, Boolean> elementType; // true - ordered. false - unordered.
    public SchemaPreprocessor(String xsdfile, XMLValidatingParser parser) 
            throws SAXException, IOException {
        
        elementType = new HashMap<>();        
        Document schema = parser.parse(xsdfile);
        
        Node document_node = schema.getDocumentElement();
        traverseNode(document_node);
//        System.out.println(elementType.size());
//        elementType.keySet().stream().forEach((key) -> {
//            if (elementType.get(key)== false) {
//                System.out.println(key + " is unordered.");
//            } else {
//                System.out.println(key + " is ordered.");
//            }
//        });
    }
    
    private void traverseNode(Node n) {
        switch (n.getNodeType()) {
            case Node.ELEMENT_NODE:                
                if (n.getNodeName().equals("xs:element")) {
                    NamedNodeMap attributes = n.getAttributes();
                    String element_name = 
                            attributes.getNamedItem("name").getNodeValue();
                    
                    if (attributes.getNamedItem("type") != null) {
                        elementType.put(element_name, true);
                    }                    
                    else {
                        NodeList nlist = n.getChildNodes();
                        Node compositor = null;
                        for (int i = 0; i < nlist.getLength(); i++) {
                            if (nlist.item(i).getNodeName().equals("xs:complexType")) {
                                NodeList nlist_compositor = nlist.item(i).getChildNodes();
                                for (int j = 0; j < nlist_compositor.getLength(); j++) {
                                    if (nlist_compositor.item(i).getNodeName().equals("xs:all")) {
                                        compositor = nlist_compositor.item(i);
                                        elementType.put(element_name, false);
                                        break;
                                    } else if(nlist_compositor.item(i).getNodeName().equals("xs:choice")) {
                                        compositor = nlist_compositor.item(i);
                                        elementType.put(element_name, true);
                                        break;
                                    } else if(nlist_compositor.item(i).getNodeName().equals("xs:sequence")) {
                                        compositor = nlist_compositor.item(i);
                                        elementType.put(element_name, true);
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                        
                        if (compositor!= null) {
                            traverseNode(compositor);
                        } else {
                            elementType.put(element_name, true);
                        }                        
                    }
                }
                else {
                    NodeList nlist = n.getChildNodes();
                    for (int i = 0; i < nlist.getLength(); i++) {
                        traverseNode(nlist.item(i));
                    }
                }
                break;
        }
    }
    
    public boolean isOrdered(String elem_name) {
        return elementType.get(elem_name);
    }
    
    public boolean isUnordered(String elem_name) {
        return !elementType.get(elem_name);
    }
}
