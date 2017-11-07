package org.apache.xml.serializer;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import org.apache.xml.serializer.utils.Messages;
import org.apache.xml.serializer.utils.Utils;
import org.xml.sax.SAXException;



































public class ToXMLStream
  extends ToStream
{
  private CharInfo m_xmlcharInfo = CharInfo.getCharInfo(CharInfo.XML_ENTITIES_RESOURCE, "xml");
  




  public ToXMLStream()
  {
    m_charInfo = m_xmlcharInfo;
    
    initCDATA();
    
    m_prefixMap = new NamespaceMappings();
  }
  







  public void CopyFrom(ToXMLStream xmlListener)
  {
    setWriter(m_writer);
    


    String encoding = xmlListener.getEncoding();
    setEncoding(encoding);
    
    setOmitXMLDeclaration(xmlListener.getOmitXMLDeclaration());
    
    m_ispreserve = m_ispreserve;
    m_preserves = m_preserves;
    m_isprevtext = m_isprevtext;
    m_doIndent = m_doIndent;
    setIndentAmount(xmlListener.getIndentAmount());
    m_startNewLine = m_startNewLine;
    m_needToOutputDocTypeDecl = m_needToOutputDocTypeDecl;
    setDoctypeSystem(xmlListener.getDoctypeSystem());
    setDoctypePublic(xmlListener.getDoctypePublic());
    setStandalone(xmlListener.getStandalone());
    setMediaType(xmlListener.getMediaType());
    m_encodingInfo = m_encodingInfo;
    m_spaceBeforeClose = m_spaceBeforeClose;
    m_cdataStartCalled = m_cdataStartCalled;
  }
  









  public void startDocumentInternal()
    throws SAXException
  {
    if (m_needToCallStartDocument)
    {
      super.startDocumentInternal();
      m_needToCallStartDocument = false;
      
      if (m_inEntityRef) {
        return;
      }
      m_needToOutputDocTypeDecl = true;
      m_startNewLine = false;
      



      String version = getXMLVersion();
      if (!getOmitXMLDeclaration())
      {
        String encoding = Encodings.getMimeEncoding(getEncoding());
        String standalone;
        String standalone;
        if (m_standaloneWasSpecified)
        {
          standalone = " standalone=\"" + getStandalone() + "\"";
        }
        else
        {
          standalone = "";
        }
        
        try
        {
          Writer writer = m_writer;
          writer.write("<?xml version=\"");
          writer.write(version);
          writer.write("\" encoding=\"");
          writer.write(encoding);
          writer.write(34);
          writer.write(standalone);
          writer.write("?>");
          if ((m_doIndent) && (
            (m_standaloneWasSpecified) || (getDoctypePublic() != null) || (getDoctypeSystem() != null)))
          {










            writer.write(m_lineSep, 0, m_lineSepLen);
          }
          
        }
        catch (IOException e)
        {
          throw new SAXException(e);
        }
      }
    }
  }
  








  public void endDocument()
    throws SAXException
  {
    flushPending();
    if ((m_doIndent) && (!m_isprevtext))
    {
      try
      {
        outputLineSep();
      }
      catch (IOException e)
      {
        throw new SAXException(e);
      }
    }
    
    flushWriter();
    
    if (m_tracer != null) {
      super.fireEndDoc();
    }
  }
  












  public void startPreserving()
    throws SAXException
  {
    m_preserves.push(true);
    
    m_ispreserve = true;
  }
  








  public void endPreserving()
    throws SAXException
  {
    m_ispreserve = (m_preserves.isEmpty() ? false : m_preserves.pop());
  }
  











  public void processingInstruction(String target, String data)
    throws SAXException
  {
    if (m_inEntityRef) {
      return;
    }
    flushPending();
    
    if (target.equals("javax.xml.transform.disable-output-escaping"))
    {
      startNonEscaping();
    }
    else if (target.equals("javax.xml.transform.enable-output-escaping"))
    {
      endNonEscaping();
    }
    else
    {
      try
      {
        if (m_elemContext.m_startTagOpen)
        {
          closeStartTag();
          m_elemContext.m_startTagOpen = false;
        }
        else if (m_needToCallStartDocument) {
          startDocumentInternal();
        }
        if (shouldIndent()) {
          indent();
        }
        Writer writer = m_writer;
        writer.write("<?");
        writer.write(target);
        
        if ((data.length() > 0) && (!Character.isSpaceChar(data.charAt(0))))
        {
          writer.write(32);
        }
        int indexOfQLT = data.indexOf("?>");
        
        if (indexOfQLT >= 0)
        {


          if (indexOfQLT > 0)
          {
            writer.write(data.substring(0, indexOfQLT));
          }
          
          writer.write("? >");
          
          if (indexOfQLT + 2 < data.length())
          {
            writer.write(data.substring(indexOfQLT + 2));
          }
        }
        else
        {
          writer.write(data);
        }
        
        writer.write(63);
        writer.write(62);
        









        m_startNewLine = true;
      }
      catch (IOException e)
      {
        throw new SAXException(e);
      }
    }
    
    if (m_tracer != null) {
      super.fireEscapingEvent(target, data);
    }
  }
  





  public void entityReference(String name)
    throws SAXException
  {
    if (m_elemContext.m_startTagOpen)
    {
      closeStartTag();
      m_elemContext.m_startTagOpen = false;
    }
    
    try
    {
      if (shouldIndent()) {
        indent();
      }
      Writer writer = m_writer;
      writer.write(38);
      writer.write(name);
      writer.write(59);
    }
    catch (IOException e)
    {
      throw new SAXException(e);
    }
    
    if (m_tracer != null) {
      super.fireEntityReference(name);
    }
  }
  









  public void addUniqueAttribute(String name, String value, int flags)
    throws SAXException
  {
    if (m_elemContext.m_startTagOpen)
    {
      try
      {

        String patchedName = patchName(name);
        Writer writer = m_writer;
        if (((flags & 0x1) > 0) && (m_xmlcharInfo.onlyQuotAmpLtGt))
        {






          writer.write(32);
          writer.write(patchedName);
          writer.write("=\"");
          writer.write(value);
          writer.write(34);
        }
        else
        {
          writer.write(32);
          writer.write(patchedName);
          writer.write("=\"");
          writeAttrString(writer, value, getEncoding());
          writer.write(34);
        }
      } catch (IOException e) {
        throw new SAXException(e);
      }
    }
  }
  

















  public void addAttribute(String uri, String localName, String rawName, String type, String value, boolean xslAttribute)
    throws SAXException
  {
    if (m_elemContext.m_startTagOpen)
    {
      boolean was_added = addAttributeAlways(uri, localName, rawName, type, value, xslAttribute);
      








      if ((was_added) && (!xslAttribute) && (!rawName.startsWith("xmlns")))
      {
        String prefixUsed = ensureAttributesNamespaceIsDeclared(uri, localName, rawName);
        



        if ((prefixUsed != null) && (rawName != null) && (!rawName.startsWith(prefixUsed)))
        {




          rawName = prefixUsed + ":" + localName;
        }
      }
      
      addAttributeAlways(uri, localName, rawName, type, value, xslAttribute);







    }
    else
    {







      String msg = Utils.messages.createMessage("ER_ILLEGAL_ATTRIBUTE_POSITION", new Object[] { localName });
      

      try
      {
        Transformer tran = super.getTransformer();
        ErrorListener errHandler = tran.getErrorListener();
        


        if ((null != errHandler) && (m_sourceLocator != null)) {
          errHandler.warning(new TransformerException(msg, m_sourceLocator));
        } else {
          System.out.println(msg);
        }
        

      }
      catch (TransformerException e)
      {

        SAXException se = new SAXException(e);
        throw se;
      }
    }
  }
  


  public void endElement(String elemName)
    throws SAXException
  {
    endElement(null, null, elemName);
  }
  











  public void namespaceAfterStartElement(String prefix, String uri)
    throws SAXException
  {
    if (m_elemContext.m_elementURI == null)
    {
      String prefix1 = getPrefixPart(m_elemContext.m_elementName);
      if ((prefix1 == null) && ("".equals(prefix)))
      {




        m_elemContext.m_elementURI = uri;
      }
    }
    startPrefixMapping(prefix, uri, false);
  }
  







  protected boolean pushNamespace(String prefix, String uri)
  {
    try
    {
      if (m_prefixMap.pushNamespace(prefix, uri, m_elemContext.m_currentElemDepth))
      {

        startPrefixMapping(prefix, uri);
        return true;
      }
    }
    catch (SAXException e) {}
    


    return false;
  }
  






  public boolean reset()
  {
    boolean wasReset = false;
    if (super.reset())
    {



      wasReset = true;
    }
    return wasReset;
  }
  










  private void resetToXMLStream() {}
  









  private String getXMLVersion()
  {
    String xmlVersion = getVersion();
    if ((xmlVersion == null) || (xmlVersion.equals("1.0")))
    {
      xmlVersion = "1.0";
    }
    else if (xmlVersion.equals("1.1"))
    {
      xmlVersion = "1.1";
    }
    else
    {
      String msg = Utils.messages.createMessage("ER_XML_VERSION_NOT_SUPPORTED", new Object[] { xmlVersion });
      

      try
      {
        Transformer tran = super.getTransformer();
        ErrorListener errHandler = tran.getErrorListener();
        
        if ((null != errHandler) && (m_sourceLocator != null)) {
          errHandler.warning(new TransformerException(msg, m_sourceLocator));
        } else {
          System.out.println(msg);
        }
      } catch (Exception e) {}
      xmlVersion = "1.0";
    }
    return xmlVersion;
  }
}
