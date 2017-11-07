package org.apache.xml.dtm.ref;

import org.apache.xml.dtm.DTM;
import org.apache.xml.utils.NodeConsumer;
import org.apache.xml.utils.XMLString;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.AttributesImpl;































public class DTMTreeWalker
{
  private ContentHandler m_contentHandler = null;
  



  protected DTM m_dtm;
  



  public void setDTM(DTM dtm)
  {
    m_dtm = dtm;
  }
  





  public ContentHandler getcontentHandler()
  {
    return m_contentHandler;
  }
  





  public void setcontentHandler(ContentHandler ch)
  {
    m_contentHandler = ch;
  }
  






  public DTMTreeWalker() {}
  





  public DTMTreeWalker(ContentHandler contentHandler, DTM dtm)
  {
    m_contentHandler = contentHandler;
    m_dtm = dtm;
  }
  










  public void traverse(int pos)
    throws SAXException
  {
    int top = pos;
    
    while (-1 != pos)
    {
      startNode(pos);
      int nextNode = m_dtm.getFirstChild(pos);
      while (-1 == nextNode)
      {
        endNode(pos);
        
        if (top != pos)
        {

          nextNode = m_dtm.getNextSibling(pos);
          
          if (-1 == nextNode)
          {
            pos = m_dtm.getParent(pos);
            
            if ((-1 == pos) || (top == pos))
            {


              if (-1 != pos) {
                endNode(pos);
              }
              nextNode = -1;
            }
          }
        }
      }
      

      pos = nextNode;
    }
  }
  















  public void traverse(int pos, int top)
    throws SAXException
  {
    while (-1 != pos)
    {
      startNode(pos);
      int nextNode = m_dtm.getFirstChild(pos);
      while (-1 == nextNode)
      {
        endNode(pos);
        
        if ((-1 == top) || (top != pos))
        {

          nextNode = m_dtm.getNextSibling(pos);
          
          if (-1 == nextNode)
          {
            pos = m_dtm.getParent(pos);
            
            if ((-1 == pos) || ((-1 != top) && (top == pos)))
            {
              nextNode = -1;
            }
          }
        }
      }
      

      pos = nextNode;
    }
  }
  

  boolean nextIsRaw = false;
  



  private final void dispatachChars(int node)
    throws SAXException
  {
    m_dtm.dispatchCharactersEvents(node, m_contentHandler, false);
  }
  








  protected void startNode(int node)
    throws SAXException
  {
    if ((m_contentHandler instanceof NodeConsumer)) {}
    




    switch (m_dtm.getNodeType(node))
    {

    case 8: 
      XMLString data = m_dtm.getStringValue(node);
      
      if ((m_contentHandler instanceof LexicalHandler))
      {
        LexicalHandler lh = (LexicalHandler)m_contentHandler;
        data.dispatchAsComment(lh);
      }
      
      break;
    
    case 11: 
      break;
    
    case 9: 
      m_contentHandler.startDocument();
      break;
    case 1: 
      DTM dtm = m_dtm;
      
      for (int nsn = dtm.getFirstNamespaceNode(node, true); -1 != nsn; 
          nsn = dtm.getNextNamespaceNode(node, nsn, true))
      {

        String prefix = dtm.getNodeNameX(nsn);
        
        m_contentHandler.startPrefixMapping(prefix, dtm.getNodeValue(nsn));
      }
      



      String ns = dtm.getNamespaceURI(node);
      if (null == ns) {
        ns = "";
      }
      
      AttributesImpl attrs = new AttributesImpl();
      

      for (int i = dtm.getFirstAttribute(node); 
          i != -1; 
          i = dtm.getNextAttribute(i))
      {
        attrs.addAttribute(dtm.getNamespaceURI(i), dtm.getLocalName(i), dtm.getNodeName(i), "CDATA", dtm.getNodeValue(i));
      }
      





      m_contentHandler.startElement(ns, m_dtm.getLocalName(node), m_dtm.getNodeName(node), attrs);
      


      break;
    
    case 7: 
      String name = m_dtm.getNodeName(node);
      

      if (name.equals("xslt-next-is-raw"))
      {
        nextIsRaw = true;
      }
      else
      {
        m_contentHandler.processingInstruction(name, m_dtm.getNodeValue(node));
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
      if ((m_contentHandler instanceof LexicalHandler))
      {
        ((LexicalHandler)m_contentHandler).startEntity(m_dtm.getNodeName(node));
      }
      







      break;
    }
    
  }
  







  protected void endNode(int node)
    throws SAXException
  {
    switch (m_dtm.getNodeType(node))
    {
    case 9: 
      m_contentHandler.endDocument();
      break;
    case 1: 
      String ns = m_dtm.getNamespaceURI(node);
      if (null == ns)
        ns = "";
      m_contentHandler.endElement(ns, m_dtm.getLocalName(node), m_dtm.getNodeName(node));
      


      for (int nsn = m_dtm.getFirstNamespaceNode(node, true); -1 != nsn; 
          nsn = m_dtm.getNextNamespaceNode(node, nsn, true))
      {

        String prefix = m_dtm.getNodeNameX(nsn);
        
        m_contentHandler.endPrefixMapping(prefix);
      }
      break;
    case 4: 
      break;
    
    case 5: 
      if ((m_contentHandler instanceof LexicalHandler))
      {
        LexicalHandler lh = (LexicalHandler)m_contentHandler;
        
        lh.endEntity(m_dtm.getNodeName(node)); }
      break;
    }
  }
}
