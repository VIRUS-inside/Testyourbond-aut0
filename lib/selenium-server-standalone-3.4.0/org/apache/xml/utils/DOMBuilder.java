package org.apache.xml.utils;

import java.io.Writer;
import java.util.Stack;
import java.util.Vector;
import org.apache.xml.res.XMLMessages;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
































public class DOMBuilder
  implements ContentHandler, LexicalHandler
{
  public Document m_doc;
  protected Node m_currentNode = null;
  

  protected Node m_root = null;
  

  protected Node m_nextSibling = null;
  

  public DocumentFragment m_docFrag = null;
  

  protected Stack m_elemStack = new Stack();
  

  protected Vector m_prefixMappings = new Vector();
  







  public DOMBuilder(Document doc, Node node)
  {
    m_doc = doc;
    m_currentNode = (this.m_root = node);
    
    if ((node instanceof Element)) {
      m_elemStack.push(node);
    }
  }
  






  public DOMBuilder(Document doc, DocumentFragment docFrag)
  {
    m_doc = doc;
    m_docFrag = docFrag;
  }
  






  public DOMBuilder(Document doc)
  {
    m_doc = doc;
  }
  





  public Node getRootDocument()
  {
    return null != m_docFrag ? m_docFrag : m_doc;
  }
  



  public Node getRootNode()
  {
    return m_root;
  }
  





  public Node getCurrentNode()
  {
    return m_currentNode;
  }
  






  public void setNextSibling(Node nextSibling)
  {
    m_nextSibling = nextSibling;
  }
  





  public Node getNextSibling()
  {
    return m_nextSibling;
  }
  





  public Writer getWriter()
  {
    return null;
  }
  





  protected void append(Node newNode)
    throws SAXException
  {
    Node currentNode = m_currentNode;
    
    if (null != currentNode)
    {
      if ((currentNode == m_root) && (m_nextSibling != null)) {
        currentNode.insertBefore(newNode, m_nextSibling);
      } else {
        currentNode.appendChild(newNode);
      }
      
    }
    else if (null != m_docFrag)
    {
      if (m_nextSibling != null) {
        m_docFrag.insertBefore(newNode, m_nextSibling);
      } else {
        m_docFrag.appendChild(newNode);
      }
    }
    else {
      boolean ok = true;
      short type = newNode.getNodeType();
      
      if (type == 3)
      {
        String data = newNode.getNodeValue();
        
        if ((null != data) && (data.trim().length() > 0))
        {
          throw new SAXException(XMLMessages.createXMLMessage("ER_CANT_OUTPUT_TEXT_BEFORE_DOC", null));
        }
        


        ok = false;
      }
      else if (type == 1)
      {
        if (m_doc.getDocumentElement() != null)
        {
          ok = false;
          
          throw new SAXException(XMLMessages.createXMLMessage("ER_CANT_HAVE_MORE_THAN_ONE_ROOT", null));
        }
      }
      


      if (ok)
      {
        if (m_nextSibling != null) {
          m_doc.insertBefore(newNode, m_nextSibling);
        } else {
          m_doc.appendChild(newNode);
        }
      }
    }
  }
  













  public void setDocumentLocator(Locator locator) {}
  













  public void startDocument()
    throws SAXException
  {}
  













  public void endDocument()
    throws SAXException
  {}
  












  public void startElement(String ns, String localName, String name, Attributes atts)
    throws SAXException
  {
    Element elem;
    











    Element elem;
    











    if ((null == ns) || (ns.length() == 0)) {
      elem = m_doc.createElementNS(null, name);
    } else {
      elem = m_doc.createElementNS(ns, name);
    }
    append(elem);
    
    try
    {
      int nAtts = atts.getLength();
      
      if (0 != nAtts)
      {
        for (int i = 0; i < nAtts; i++)
        {



          if (atts.getType(i).equalsIgnoreCase("ID")) {
            setIDAttribute(atts.getValue(i), elem);
          }
          String attrNS = atts.getURI(i);
          
          if ("".equals(attrNS)) {
            attrNS = null;
          }
          


          String attrQName = atts.getQName(i);
          


          if ((attrQName.startsWith("xmlns:")) || (attrQName.equals("xmlns"))) {
            attrNS = "http://www.w3.org/2000/xmlns/";
          }
          

          elem.setAttributeNS(attrNS, attrQName, atts.getValue(i));
        }
      }
      



      int nDecls = m_prefixMappings.size();
      


      for (int i = 0; i < nDecls; i += 2)
      {
        String prefix = (String)m_prefixMappings.elementAt(i);
        
        if (prefix != null)
        {

          String declURL = (String)m_prefixMappings.elementAt(i + 1);
          
          elem.setAttributeNS("http://www.w3.org/2000/xmlns/", prefix, declURL);
        }
      }
      m_prefixMappings.clear();
      


      m_elemStack.push(elem);
      
      m_currentNode = elem;


    }
    catch (Exception de)
    {

      throw new SAXException(de);
    }
  }
  




















  public void endElement(String ns, String localName, String name)
    throws SAXException
  {
    m_elemStack.pop();
    m_currentNode = (m_elemStack.isEmpty() ? null : (Node)m_elemStack.peek());
  }
  
















  public void setIDAttribute(String id, Element elem) {}
  
















  public void characters(char[] ch, int start, int length)
    throws SAXException
  {
    if ((isOutsideDocElem()) && (XMLCharacterRecognizer.isWhiteSpace(ch, start, length)))
    {
      return;
    }
    if (m_inCData)
    {
      cdata(ch, start, length);
      
      return;
    }
    
    String s = new String(ch, start, length);
    
    Node childNode = m_currentNode != null ? m_currentNode.getLastChild() : null;
    if ((childNode != null) && (childNode.getNodeType() == 3)) {
      ((Text)childNode).appendData(s);
    }
    else {
      Text text = m_doc.createTextNode(s);
      append(text);
    }
  }
  










  public void charactersRaw(char[] ch, int start, int length)
    throws SAXException
  {
    if ((isOutsideDocElem()) && (XMLCharacterRecognizer.isWhiteSpace(ch, start, length)))
    {
      return;
    }
    
    String s = new String(ch, start, length);
    
    append(m_doc.createProcessingInstruction("xslt-next-is-raw", "formatter-to-dom"));
    
    append(m_doc.createTextNode(s));
  }
  









  public void startEntity(String name)
    throws SAXException
  {}
  








  public void endEntity(String name)
    throws SAXException
  {}
  








  public void entityReference(String name)
    throws SAXException
  {
    append(m_doc.createEntityReference(name));
  }
  






















  public void ignorableWhitespace(char[] ch, int start, int length)
    throws SAXException
  {
    if (isOutsideDocElem()) {
      return;
    }
    String s = new String(ch, start, length);
    
    append(m_doc.createTextNode(s));
  }
  





  private boolean isOutsideDocElem()
  {
    return (null == m_docFrag) && (m_elemStack.size() == 0) && ((null == m_currentNode) || (m_currentNode.getNodeType() == 9));
  }
  















  public void processingInstruction(String target, String data)
    throws SAXException
  {
    append(m_doc.createProcessingInstruction(target, data));
  }
  










  public void comment(char[] ch, int start, int length)
    throws SAXException
  {
    append(m_doc.createComment(new String(ch, start, length)));
  }
  

  protected boolean m_inCData = false;
  




  public void startCDATA()
    throws SAXException
  {
    m_inCData = true;
    append(m_doc.createCDATASection(""));
  }
  




  public void endCDATA()
    throws SAXException
  {
    m_inCData = false;
  }
  






















  public void cdata(char[] ch, int start, int length)
    throws SAXException
  {
    if ((isOutsideDocElem()) && (XMLCharacterRecognizer.isWhiteSpace(ch, start, length)))
    {
      return;
    }
    String s = new String(ch, start, length);
    
    CDATASection section = (CDATASection)m_currentNode.getLastChild();
    section.appendData(s);
  }
  


















  public void startDTD(String name, String publicId, String systemId)
    throws SAXException
  {}
  

















  public void endDTD()
    throws SAXException
  {}
  

















  public void startPrefixMapping(String prefix, String uri)
    throws SAXException
  {
    if ((null == prefix) || (prefix.length() == 0))
      prefix = "xmlns"; else
      prefix = "xmlns:" + prefix;
    m_prefixMappings.addElement(prefix);
    m_prefixMappings.addElement(uri);
  }
  
  public void endPrefixMapping(String prefix)
    throws SAXException
  {}
  
  public void skippedEntity(String name)
    throws SAXException
  {}
}
