package org.apache.xml.serializer;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import org.apache.xml.serializer.utils.Messages;
import org.apache.xml.serializer.utils.Utils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;















































public class ToTextStream
  extends ToStream
{
  public ToTextStream() {}
  
  protected void startDocumentInternal()
    throws SAXException
  {
    super.startDocumentInternal();
    
    m_needToCallStartDocument = false;
  }
  















  public void endDocument()
    throws SAXException
  {
    flushPending();
    flushWriter();
    if (m_tracer != null) {
      super.fireEndDoc();
    }
  }
  

































  public void startElement(String namespaceURI, String localName, String name, Attributes atts)
    throws SAXException
  {
    if (m_tracer != null) {
      super.fireStartElem(name);
      firePseudoAttributes();
    }
  }
  


























  public void endElement(String namespaceURI, String localName, String name)
    throws SAXException
  {
    if (m_tracer != null) {
      super.fireEndElem(name);
    }
  }
  

























  public void characters(char[] ch, int start, int length)
    throws SAXException
  {
    flushPending();
    
    try
    {
      if (inTemporaryOutputState())
      {










        m_writer.write(ch, start, length);
      }
      else
      {
        writeNormalizedChars(ch, start, length, m_lineSepUse);
      }
      
      if (m_tracer != null) {
        super.fireCharEvent(ch, start, length);
      }
    }
    catch (IOException ioe) {
      throw new SAXException(ioe);
    }
  }
  












  public void charactersRaw(char[] ch, int start, int length)
    throws SAXException
  {
    try
    {
      writeNormalizedChars(ch, start, length, m_lineSepUse);
    }
    catch (IOException ioe)
    {
      throw new SAXException(ioe);
    }
  }
  


















  void writeNormalizedChars(char[] ch, int start, int length, boolean useLineSep)
    throws IOException, SAXException
  {
    String encoding = getEncoding();
    Writer writer = m_writer;
    int end = start + length;
    

    char S_LINEFEED = '\n';
    




    for (int i = start; i < end; i++) {
      char c = ch[i];
      
      if (('\n' == c) && (useLineSep)) {
        writer.write(m_lineSep, 0, m_lineSepLen);
      }
      else if (m_encodingInfo.isInEncoding(c)) {
        writer.write(c);
      }
      else if (Encodings.isHighUTF16Surrogate(c)) {
        int codePoint = writeUTF16Surrogate(c, ch, i, end);
        if (codePoint != 0)
        {

          String integralValue = Integer.toString(codePoint);
          String msg = Utils.messages.createMessage("ER_ILLEGAL_CHARACTER", new Object[] { integralValue, encoding });
          





          System.err.println(msg);
        }
        
        i++;



      }
      else if (encoding != null)
      {




        writer.write(38);
        writer.write(35);
        writer.write(Integer.toString(c));
        writer.write(59);
        


        String integralValue = Integer.toString(c);
        String msg = Utils.messages.createMessage("ER_ILLEGAL_CHARACTER", new Object[] { integralValue, encoding });
        





        System.err.println(msg);

      }
      else
      {
        writer.write(c);
      }
    }
  }
  




























  public void cdata(char[] ch, int start, int length)
    throws SAXException
  {
    try
    {
      writeNormalizedChars(ch, start, length, m_lineSepUse);
      if (m_tracer != null) {
        super.fireCDATAEvent(ch, start, length);
      }
    }
    catch (IOException ioe) {
      throw new SAXException(ioe);
    }
  }
  



























  public void ignorableWhitespace(char[] ch, int start, int length)
    throws SAXException
  {
    try
    {
      writeNormalizedChars(ch, start, length, m_lineSepUse);
    }
    catch (IOException ioe)
    {
      throw new SAXException(ioe);
    }
  }
  




















  public void processingInstruction(String target, String data)
    throws SAXException
  {
    flushPending();
    
    if (m_tracer != null) {
      super.fireEscapingEvent(target, data);
    }
  }
  







  public void comment(String data)
    throws SAXException
  {
    int length = data.length();
    if (length > m_charsBuff.length)
    {
      m_charsBuff = new char[length * 2 + 1];
    }
    data.getChars(0, length, m_charsBuff, 0);
    comment(m_charsBuff, 0, length);
  }
  













  public void comment(char[] ch, int start, int length)
    throws SAXException
  {
    flushPending();
    if (m_tracer != null) {
      super.fireCommentEvent(ch, start, length);
    }
  }
  





  public void entityReference(String name)
    throws SAXException
  {
    if (m_tracer != null) {
      super.fireEntityReference(name);
    }
  }
  






  public void addAttribute(String uri, String localName, String rawName, String type, String value, boolean XSLAttribute) {}
  






  public void endCDATA()
    throws SAXException
  {}
  





  public void endElement(String elemName)
    throws SAXException
  {
    if (m_tracer != null) {
      super.fireEndElem(elemName);
    }
  }
  





  public void startElement(String elementNamespaceURI, String elementLocalName, String elementName)
    throws SAXException
  {
    if (m_needToCallStartDocument) {
      startDocumentInternal();
    }
    if (m_tracer != null) {
      super.fireStartElem(elementName);
      firePseudoAttributes();
    }
  }
  






  public void characters(String characters)
    throws SAXException
  {
    int length = characters.length();
    if (length > m_charsBuff.length)
    {
      m_charsBuff = new char[length * 2 + 1];
    }
    characters.getChars(0, length, m_charsBuff, 0);
    characters(m_charsBuff, 0, length);
  }
  






  public void addAttribute(String name, String value) {}
  





  public void addUniqueAttribute(String qName, String value, int flags)
    throws SAXException
  {}
  





  public boolean startPrefixMapping(String prefix, String uri, boolean shouldFlush)
    throws SAXException
  {
    return false;
  }
  



  public void startPrefixMapping(String prefix, String uri)
    throws SAXException
  {}
  


  public void namespaceAfterStartElement(String prefix, String uri)
    throws SAXException
  {}
  


  public void flushPending()
    throws SAXException
  {
    if (m_needToCallStartDocument)
    {
      startDocumentInternal();
      m_needToCallStartDocument = false;
    }
  }
}
