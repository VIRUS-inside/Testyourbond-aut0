package org.apache.xml.serializer;

import java.io.File;
import org.apache.xml.serializer.utils.AttList;
import org.apache.xml.serializer.utils.DOM2Helper;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.LocatorImpl;













































public final class TreeWalker
{
  private final ContentHandler m_contentHandler;
  private final SerializationHandler m_Serializer;
  protected final DOM2Helper m_dh;
  private final LocatorImpl m_locator = new LocatorImpl();
  





  public ContentHandler getContentHandler()
  {
    return m_contentHandler;
  }
  
  public TreeWalker(ContentHandler ch) {
    this(ch, null);
  }
  





  public TreeWalker(ContentHandler contentHandler, String systemId)
  {
    m_contentHandler = contentHandler;
    if ((m_contentHandler instanceof SerializationHandler)) {
      m_Serializer = ((SerializationHandler)m_contentHandler);
    }
    else {
      m_Serializer = null;
    }
    
    m_contentHandler.setDocumentLocator(m_locator);
    if (systemId != null) {
      m_locator.setSystemId(systemId);
    } else {
      try
      {
        m_locator.setSystemId(System.getProperty("user.dir") + File.separator + "dummy.xsl");
      }
      catch (SecurityException se) {}
    }
    


    if (m_contentHandler != null) {
      m_contentHandler.setDocumentLocator(m_locator);
    }
    try {
      m_locator.setSystemId(System.getProperty("user.dir") + File.separator + "dummy.xsl");
    }
    catch (SecurityException se) {}
    

    m_dh = new DOM2Helper();
  }
  












  public void traverse(Node pos)
    throws SAXException
  {
    m_contentHandler.startDocument();
    
    Node top = pos;
    
    while (null != pos)
    {
      startNode(pos);
      
      Node nextNode = pos.getFirstChild();
      
      while (null == nextNode)
      {
        endNode(pos);
        
        if (!top.equals(pos))
        {

          nextNode = pos.getNextSibling();
          
          if (null == nextNode)
          {
            pos = pos.getParentNode();
            
            if ((null == pos) || (top.equals(pos)))
            {
              if (null != pos) {
                endNode(pos);
              }
              nextNode = null;
            }
          }
        }
      }
      

      pos = nextNode;
    }
    m_contentHandler.endDocument();
  }
  













  public void traverse(Node pos, Node top)
    throws SAXException
  {
    m_contentHandler.startDocument();
    
    while (null != pos)
    {
      startNode(pos);
      
      Node nextNode = pos.getFirstChild();
      
      while (null == nextNode)
      {
        endNode(pos);
        
        if ((null == top) || (!top.equals(pos)))
        {

          nextNode = pos.getNextSibling();
          
          if (null == nextNode)
          {
            pos = pos.getParentNode();
            
            if ((null == pos) || ((null != top) && (top.equals(pos))))
            {
              nextNode = null;
            }
          }
        }
      }
      

      pos = nextNode;
    }
    m_contentHandler.endDocument();
  }
  

  boolean nextIsRaw = false;
  



  private final void dispatachChars(Node node)
    throws SAXException
  {
    if (m_Serializer != null)
    {
      m_Serializer.characters(node);
    }
    else
    {
      String data = ((Text)node).getData();
      m_contentHandler.characters(data.toCharArray(), 0, data.length());
    }
  }
  


















  protected void startNode(Node node)
    throws SAXException
  {
    if ((node instanceof Locator))
    {
      Locator loc = (Locator)node;
      m_locator.setColumnNumber(loc.getColumnNumber());
      m_locator.setLineNumber(loc.getLineNumber());
      m_locator.setPublicId(loc.getPublicId());
      m_locator.setSystemId(loc.getSystemId());
    }
    else
    {
      m_locator.setColumnNumber(0);
      m_locator.setLineNumber(0);
    }
    
    switch (node.getNodeType())
    {

    case 8: 
      String data = ((Comment)node).getData();
      
      if ((m_contentHandler instanceof LexicalHandler))
      {
        LexicalHandler lh = (LexicalHandler)m_contentHandler;
        
        lh.comment(data.toCharArray(), 0, data.length());
      }
      
      break;
    
    case 11: 
      break;
    
    case 9: 
      break;
    
    case 1: 
      Element elem_node = (Element)node;
      



      String uri = elem_node.getNamespaceURI();
      if (uri != null) {
        String prefix = elem_node.getPrefix();
        if (prefix == null)
          prefix = "";
        m_contentHandler.startPrefixMapping(prefix, uri);
      }
      
      NamedNodeMap atts = elem_node.getAttributes();
      int nAttrs = atts.getLength();
      




      for (int i = 0; i < nAttrs; i++)
      {
        Node attr = atts.item(i);
        String attrName = attr.getNodeName();
        int colon = attrName.indexOf(':');
        


        if ((attrName.equals("xmlns")) || (attrName.startsWith("xmlns:")))
        {
          String prefix;
          
          String prefix;
          if (colon < 0) {
            prefix = "";
          } else {
            prefix = attrName.substring(colon + 1);
          }
          m_contentHandler.startPrefixMapping(prefix, attr.getNodeValue());

        }
        else if (colon > 0) {
          String prefix = attrName.substring(0, colon);
          String uri = attr.getNamespaceURI();
          if (uri != null) {
            m_contentHandler.startPrefixMapping(prefix, uri);
          }
        }
      }
      String ns = m_dh.getNamespaceOfNode(node);
      if (null == ns)
        ns = "";
      m_contentHandler.startElement(ns, m_dh.getLocalNameOfNode(node), node.getNodeName(), new AttList(atts, m_dh));
      


      break;
    
    case 7: 
      ProcessingInstruction pi = (ProcessingInstruction)node;
      String name = pi.getNodeName();
      

      if (name.equals("xslt-next-is-raw"))
      {
        nextIsRaw = true;
      }
      else
      {
        m_contentHandler.processingInstruction(pi.getNodeName(), pi.getData());
      }
      

      break;
    
    case 4: 
      boolean isLexH = m_contentHandler instanceof LexicalHandler;
      LexicalHandler lh = isLexH ? (LexicalHandler)m_contentHandler : null;
      

      if (isLexH)
      {
        lh.startCDATA();
      }
      
      dispatachChars(node);
      

      if (isLexH)
      {
        lh.endCDATA();
      }
      

      break;
    


    case 3: 
      if (nextIsRaw)
      {
        nextIsRaw = false;
        
        m_contentHandler.processingInstruction("javax.xml.transform.disable-output-escaping", "");
        dispatachChars(node);
        m_contentHandler.processingInstruction("javax.xml.transform.enable-output-escaping", "");
      }
      else
      {
        dispatachChars(node);
      }
      
      break;
    
    case 5: 
      EntityReference eref = (EntityReference)node;
      
      if ((m_contentHandler instanceof LexicalHandler))
      {
        ((LexicalHandler)m_contentHandler).startEntity(eref.getNodeName());
      }
      






      break;
    }
    
  }
  








  protected void endNode(Node node)
    throws SAXException
  {
    switch (node.getNodeType())
    {
    case 9: 
      break;
    
    case 1: 
      String ns = m_dh.getNamespaceOfNode(node);
      if (null == ns)
        ns = "";
      m_contentHandler.endElement(ns, m_dh.getLocalNameOfNode(node), node.getNodeName());
      


      if (m_Serializer == null)
      {


        Element elem_node = (Element)node;
        NamedNodeMap atts = elem_node.getAttributes();
        int nAttrs = atts.getLength();
        


        for (int i = nAttrs - 1; 0 <= i; i--)
        {
          Node attr = atts.item(i);
          String attrName = attr.getNodeName();
          int colon = attrName.indexOf(':');
          

          if ((attrName.equals("xmlns")) || (attrName.startsWith("xmlns:")))
          {
            String prefix;
            
            String prefix;
            if (colon < 0) {
              prefix = "";
            } else {
              prefix = attrName.substring(colon + 1);
            }
            m_contentHandler.endPrefixMapping(prefix);
          }
          else if (colon > 0) {
            String prefix = attrName.substring(0, colon);
            m_contentHandler.endPrefixMapping(prefix);
          }
        }
        
        String uri = elem_node.getNamespaceURI();
        if (uri != null) {
          String prefix = elem_node.getPrefix();
          if (prefix == null)
            prefix = "";
          m_contentHandler.endPrefixMapping(prefix);
        }
      }
      break;
    
    case 4: 
      break;
    
    case 5: 
      EntityReference eref = (EntityReference)node;
      
      if ((m_contentHandler instanceof LexicalHandler))
      {
        LexicalHandler lh = (LexicalHandler)m_contentHandler;
        
        lh.endEntity(eref.getNodeName());
      }
      
      break;
    }
  }
}
