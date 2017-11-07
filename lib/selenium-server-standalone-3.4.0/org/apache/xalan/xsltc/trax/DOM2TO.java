package org.apache.xalan.xsltc.trax;

import java.io.IOException;
import org.apache.xml.serializer.NamespaceMappings;
import org.apache.xml.serializer.SerializationHandler;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
































public class DOM2TO
  implements XMLReader, Locator
{
  private static final String EMPTYSTRING = "";
  private static final String XMLNS_PREFIX = "xmlns";
  private Node _dom;
  private SerializationHandler _handler;
  
  public DOM2TO(Node root, SerializationHandler handler)
  {
    _dom = root;
    _handler = handler;
  }
  
  public ContentHandler getContentHandler() {
    return null;
  }
  
  public void setContentHandler(ContentHandler handler) {}
  
  public void parse(InputSource unused)
    throws IOException, SAXException
  {
    parse(_dom);
  }
  
  public void parse() throws IOException, SAXException {
    if (_dom != null) {
      boolean isIncomplete = _dom.getNodeType() != 9;
      

      if (isIncomplete) {
        _handler.startDocument();
        parse(_dom);
        _handler.endDocument();
      }
      else {
        parse(_dom);
      }
    }
  }
  




  private void parse(Node node)
    throws IOException, SAXException
  {
    if (node == null) return;
    Node next;
    switch (node.getNodeType())
    {
    case 2: 
    case 5: 
    case 6: 
    case 10: 
    case 12: 
      break;
    case 4: 
      _handler.startCDATA();
      _handler.characters(node.getNodeValue());
      _handler.endCDATA();
      break;
    
    case 8: 
      _handler.comment(node.getNodeValue());
      break;
    
    case 9: 
      _handler.startDocument();
      next = node.getFirstChild();
      while (next != null) {
        parse(next);
        next = next.getNextSibling();
      }
      _handler.endDocument();
      break;
    
    case 11: 
      next = node.getFirstChild();
    case 1: case 7: case 3:  while (next != null) {
        parse(next);
        next = next.getNextSibling(); continue;
        




        String qname = node.getNodeName();
        _handler.startElement(null, null, qname);
        


        NamedNodeMap map = node.getAttributes();
        int length = map.getLength();
        

        for (int i = 0; i < length; i++) {
          Node attr = map.item(i);
          String qnameAttr = attr.getNodeName();
          

          if (qnameAttr.startsWith("xmlns")) {
            String uriAttr = attr.getNodeValue();
            int colon = qnameAttr.lastIndexOf(':');
            String prefix = colon > 0 ? qnameAttr.substring(colon + 1) : "";
            
            _handler.namespaceAfterStartElement(prefix, uriAttr);
          }
        }
        

        NamespaceMappings nm = new NamespaceMappings();
        for (int i = 0; i < length; i++) {
          Node attr = map.item(i);
          String qnameAttr = attr.getNodeName();
          

          if (!qnameAttr.startsWith("xmlns")) {
            String uriAttr = attr.getNamespaceURI();
            
            if ((uriAttr != null) && (!uriAttr.equals(""))) {
              int colon = qnameAttr.lastIndexOf(':');
              




              String newPrefix = nm.lookupPrefix(uriAttr);
              if (newPrefix == null)
                newPrefix = nm.generateNextPrefix();
              String prefix = colon > 0 ? qnameAttr.substring(0, colon) : newPrefix;
              
              _handler.namespaceAfterStartElement(prefix, uriAttr);
              _handler.addAttribute(prefix + ":" + qnameAttr, attr.getNodeValue());
            }
            else {
              _handler.addAttribute(qnameAttr, attr.getNodeValue());
            }
          }
        }
        

        String uri = node.getNamespaceURI();
        String localName = node.getLocalName();
        

        if (uri != null) {
          int colon = qname.lastIndexOf(':');
          String prefix = colon > 0 ? qname.substring(0, colon) : "";
          _handler.namespaceAfterStartElement(prefix, uri);





        }
        else if ((uri == null) && (localName != null)) {
          String prefix = "";
          _handler.namespaceAfterStartElement(prefix, "");
        }
        


        next = node.getFirstChild();
        while (next != null) {
          parse(next);
          next = next.getNextSibling();
        }
        

        _handler.endElement(qname);
        break;
        

        _handler.processingInstruction(node.getNodeName(), node.getNodeValue());
        
        break;
        

        _handler.characters(node.getNodeValue());
      }
    }
    
  }
  


  public DTDHandler getDTDHandler()
  {
    return null;
  }
  



  public ErrorHandler getErrorHandler()
  {
    return null;
  }
  




  public boolean getFeature(String name)
    throws SAXNotRecognizedException, SAXNotSupportedException
  {
    return false;
  }
  




  public void setFeature(String name, boolean value)
    throws SAXNotRecognizedException, SAXNotSupportedException
  {}
  



  public void parse(String sysId)
    throws IOException, SAXException
  {
    throw new IOException("This method is not yet implemented.");
  }
  




  public void setDTDHandler(DTDHandler handler)
    throws NullPointerException
  {}
  




  public void setEntityResolver(EntityResolver resolver)
    throws NullPointerException
  {}
  



  public EntityResolver getEntityResolver()
  {
    return null;
  }
  





  public void setErrorHandler(ErrorHandler handler)
    throws NullPointerException
  {}
  




  public void setProperty(String name, Object value)
    throws SAXNotRecognizedException, SAXNotSupportedException
  {}
  




  public Object getProperty(String name)
    throws SAXNotRecognizedException, SAXNotSupportedException
  {
    return null;
  }
  



  public int getColumnNumber()
  {
    return 0;
  }
  



  public int getLineNumber()
  {
    return 0;
  }
  



  public String getPublicId()
  {
    return null;
  }
  



  public String getSystemId()
  {
    return null;
  }
  
  private String getNodeTypeFromCode(short code)
  {
    String retval = null;
    switch (code) {
    case 2: 
      retval = "ATTRIBUTE_NODE"; break;
    case 4: 
      retval = "CDATA_SECTION_NODE"; break;
    case 8: 
      retval = "COMMENT_NODE"; break;
    case 11: 
      retval = "DOCUMENT_FRAGMENT_NODE"; break;
    case 9: 
      retval = "DOCUMENT_NODE"; break;
    case 10: 
      retval = "DOCUMENT_TYPE_NODE"; break;
    case 1: 
      retval = "ELEMENT_NODE"; break;
    case 6: 
      retval = "ENTITY_NODE"; break;
    case 5: 
      retval = "ENTITY_REFERENCE_NODE"; break;
    case 12: 
      retval = "NOTATION_NODE"; break;
    case 7: 
      retval = "PROCESSING_INSTRUCTION_NODE"; break;
    case 3: 
      retval = "TEXT_NODE";
    }
    return retval;
  }
}
