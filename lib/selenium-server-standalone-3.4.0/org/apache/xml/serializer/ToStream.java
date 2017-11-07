package org.apache.xml.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import org.apache.xml.serializer.utils.Messages;
import org.apache.xml.serializer.utils.Utils;
import org.apache.xml.serializer.utils.WrappedRuntimeException;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
































public abstract class ToStream
  extends SerializerBase
{
  private static final String COMMENT_BEGIN = "<!--";
  private static final String COMMENT_END = "-->";
  protected BoolStack m_disableOutputEscapingStates = new BoolStack();
  












  EncodingInfo m_encodingInfo = new EncodingInfo(null, null, '\000');
  









  protected BoolStack m_preserves = new BoolStack();
  








  protected boolean m_ispreserve = false;
  








  protected boolean m_isprevtext = false;
  


  private static final char[] s_systemLineSep = SecuritySupport.getSystemProperty("line.separator").toCharArray();
  







  protected char[] m_lineSep = s_systemLineSep;
  




  protected boolean m_lineSepUse = true;
  




  protected int m_lineSepLen = m_lineSep.length;
  



  protected CharInfo m_charInfo;
  


  boolean m_shouldFlush = true;
  



  protected boolean m_spaceBeforeClose = false;
  





  boolean m_startNewLine;
  




  protected boolean m_inDoctype = false;
  



  boolean m_isUTF8 = false;
  




  protected boolean m_cdataStartCalled = false;
  




  private boolean m_expandDTDEntities = true;
  





  public ToStream() {}
  





  protected void closeCDATA()
    throws SAXException
  {
    try
    {
      m_writer.write("]]>");
      
      m_cdataTagOpen = false;
    }
    catch (IOException e)
    {
      throw new SAXException(e);
    }
  }
  







  public void serialize(Node node)
    throws IOException
  {
    try
    {
      TreeWalker walker = new TreeWalker(this);
      

      walker.traverse(node);
    }
    catch (SAXException se)
    {
      throw new WrappedRuntimeException(se);
    }
  }
  



  protected boolean m_escaping = true;
  
  OutputStream m_outputStream;
  
  private boolean m_writer_set_by_user;
  
  protected final void flushWriter()
    throws SAXException
  {
    Writer writer = m_writer;
    if (null != writer)
    {
      try
      {
        if ((writer instanceof WriterToUTF8Buffered))
        {
          if (m_shouldFlush) {
            ((WriterToUTF8Buffered)writer).flush();
          } else
            ((WriterToUTF8Buffered)writer).flushBuffer();
        }
        if ((writer instanceof WriterToASCI))
        {
          if (m_shouldFlush) {
            writer.flush();
          }
          

        }
        else
        {
          writer.flush();
        }
      }
      catch (IOException ioe)
      {
        throw new SAXException(ioe);
      }
    }
  }
  







  public OutputStream getOutputStream()
  {
    return m_outputStream;
  }
  















  public void elementDecl(String name, String model)
    throws SAXException
  {
    if (m_inExternalDTD) {
      return;
    }
    try {
      Writer writer = m_writer;
      DTDprolog();
      
      writer.write("<!ELEMENT ");
      writer.write(name);
      writer.write(32);
      writer.write(model);
      writer.write(62);
      writer.write(m_lineSep, 0, m_lineSepLen);
    }
    catch (IOException e)
    {
      throw new SAXException(e);
    }
  }
  















  public void internalEntityDecl(String name, String value)
    throws SAXException
  {
    if (m_inExternalDTD) {
      return;
    }
    try {
      DTDprolog();
      outputEntityDecl(name, value);
    }
    catch (IOException e)
    {
      throw new SAXException(e);
    }
  }
  








  void outputEntityDecl(String name, String value)
    throws IOException
  {
    Writer writer = m_writer;
    writer.write("<!ENTITY ");
    writer.write(name);
    writer.write(" \"");
    writer.write(value);
    writer.write("\">");
    writer.write(m_lineSep, 0, m_lineSepLen);
  }
  





  protected final void outputLineSep()
    throws IOException
  {
    m_writer.write(m_lineSep, 0, m_lineSepLen);
  }
  
  void setProp(String name, String val, boolean defaultVal) {
    if (val != null)
    {

      char first = getFirstCharLocName(name);
      switch (first) {
      case 'c': 
        if ("cdata-section-elements".equals(name)) {
          String cdataSectionNames = val;
          addCdataSectionElements(cdataSectionNames); }
        break;
      
      case 'd': 
        if ("doctype-system".equals(name)) {
          m_doctypeSystem = val;
        } else if ("doctype-public".equals(name)) {
          m_doctypePublic = val;
          if (val.startsWith("-//W3C//DTD XHTML"))
            m_spaceBeforeClose = true;
        }
        break;
      case 'e': 
        String newEncoding = val;
        if ("encoding".equals(name)) {
          String possible_encoding = Encodings.getMimeEncoding(val);
          if (possible_encoding != null)
          {


            super.setProp("mime-name", possible_encoding, defaultVal);
          }
          
          String oldExplicitEncoding = getOutputPropertyNonDefault("encoding");
          String oldDefaultEncoding = getOutputPropertyDefault("encoding");
          if (((defaultVal) && ((oldDefaultEncoding == null) || (!oldDefaultEncoding.equalsIgnoreCase(newEncoding)))) || ((!defaultVal) && ((oldExplicitEncoding == null) || (!oldExplicitEncoding.equalsIgnoreCase(newEncoding)))))
          {



            EncodingInfo encodingInfo = Encodings.getEncodingInfo(newEncoding);
            if ((newEncoding != null) && (name == null))
            {


              String msg = Utils.messages.createMessage("ER_ENCODING_NOT_SUPPORTED", new Object[] { newEncoding });
              

              String msg2 = "Warning: encoding \"" + newEncoding + "\" not supported, using " + "UTF-8";
              

              try
              {
                Transformer tran = super.getTransformer();
                if (tran != null) {
                  ErrorListener errHandler = tran.getErrorListener();
                  

                  if ((null != errHandler) && (m_sourceLocator != null))
                  {
                    errHandler.warning(new TransformerException(msg, m_sourceLocator));
                    

                    errHandler.warning(new TransformerException(msg2, m_sourceLocator));
                  }
                  else
                  {
                    System.out.println(msg);
                    System.out.println(msg2);
                  }
                } else {
                  System.out.println(msg);
                  System.out.println(msg2);
                }
              }
              catch (Exception e) {}
              

              newEncoding = "UTF-8";
              val = "UTF-8";
              encodingInfo = Encodings.getEncodingInfo(newEncoding);
            }
            






            if ((!defaultVal) || (oldExplicitEncoding == null)) {
              m_encodingInfo = encodingInfo;
              if (newEncoding != null) {
                m_isUTF8 = newEncoding.equals("UTF-8");
              }
              
              OutputStream os = getOutputStream();
              if (os != null) {
                Writer w = getWriter();
                



                String oldEncoding = getOutputProperty("encoding");
                if (((w == null) || (!m_writer_set_by_user)) && (!newEncoding.equalsIgnoreCase(oldEncoding)))
                {




                  super.setProp(name, val, defaultVal);
                  setOutputStreamInternal(os, false);
                }
              }
            }
          } }
        break;
      
      case 'i': 
        if ("{http://xml.apache.org/xalan}indent-amount".equals(name)) {
          setIndentAmount(Integer.parseInt(val));
        } else if ("indent".equals(name)) {
          boolean b = "yes".equals(val);
          m_doIndent = b; }
        break;
      

      case 'l': 
        if ("{http://xml.apache.org/xalan}line-separator".equals(name)) {
          m_lineSep = val.toCharArray();
          m_lineSepLen = m_lineSep.length;
        }
        
        break;
      case 'm': 
        if ("media-type".equals(name)) {
          m_mediatype = val;
        }
        break;
      case 'o': 
        if ("omit-xml-declaration".equals(name)) {
          boolean b = "yes".equals(val);
          m_shouldNotWriteXMLHeader = b; }
        break;
      

      case 's': 
        if ("standalone".equals(name)) {
          if (defaultVal) {
            setStandaloneInternal(val);
          } else {
            m_standaloneWasSpecified = true;
            setStandaloneInternal(val);
          }
        }
        
        break;
      case 'v': 
        if ("version".equals(name)) {
          m_version = val;
        }
        
        break;
      }
      
      
      super.setProp(name, val, defaultVal);
    }
  }
  









  public void setOutputFormat(Properties format)
  {
    boolean shouldFlush = m_shouldFlush;
    
    if (format != null)
    {





      Enumeration propNames = format.propertyNames();
      while (propNames.hasMoreElements())
      {
        String key = (String)propNames.nextElement();
        
        String value = format.getProperty(key);
        
        String explicitValue = (String)format.get(key);
        if ((explicitValue == null) && (value != null))
        {
          setOutputPropertyDefault(key, value);
        }
        if (explicitValue != null)
        {
          setOutputProperty(key, explicitValue);
        }
      }
    }
    


    String entitiesFileName = (String)format.get("{http://xml.apache.org/xalan}entities");
    

    if (null != entitiesFileName)
    {

      String method = (String)format.get("method");
      

      m_charInfo = CharInfo.getCharInfo(entitiesFileName, method);
    }
    



    m_shouldFlush = shouldFlush;
  }
  




  public Properties getOutputFormat()
  {
    Properties def = new Properties();
    
    Set s = getOutputPropDefaultKeys();
    Iterator i = s.iterator();
    while (i.hasNext()) {
      String key = (String)i.next();
      String val = getOutputPropertyDefault(key);
      def.put(key, val);
    }
    

    Properties props = new Properties(def);
    
    Set s = getOutputPropKeys();
    Iterator i = s.iterator();
    while (i.hasNext()) {
      String key = (String)i.next();
      String val = getOutputPropertyNonDefault(key);
      if (val != null) {
        props.put(key, val);
      }
    }
    return props;
  }
  







  public void setWriter(Writer writer)
  {
    setWriterInternal(writer, true);
  }
  

  private void setWriterInternal(Writer writer, boolean setByUser)
  {
    m_writer_set_by_user = setByUser;
    m_writer = writer;
    

    if (m_tracer != null) {
      boolean noTracerYet = true;
      Writer w2 = m_writer;
      while ((w2 instanceof WriterChain)) {
        if ((w2 instanceof SerializerTraceWriter)) {
          noTracerYet = false;
          break;
        }
        w2 = ((WriterChain)w2).getWriter();
      }
      if (noTracerYet) {
        m_writer = new SerializerTraceWriter(m_writer, m_tracer);
      }
    }
  }
  











  public boolean setLineSepUse(boolean use_sytem_line_break)
  {
    boolean oldValue = m_lineSepUse;
    m_lineSepUse = use_sytem_line_break;
    return oldValue;
  }
  











  public void setOutputStream(OutputStream output)
  {
    setOutputStreamInternal(output, true);
  }
  
  private void setOutputStreamInternal(OutputStream output, boolean setByUser)
  {
    m_outputStream = output;
    String encoding = getOutputProperty("encoding");
    if ("UTF-8".equalsIgnoreCase(encoding))
    {


      setWriterInternal(new WriterToUTF8Buffered(output), false);
    } else if (("WINDOWS-1250".equals(encoding)) || ("US-ASCII".equals(encoding)) || ("ASCII".equals(encoding)))
    {



      setWriterInternal(new WriterToASCI(output), false);
    } else if (encoding != null) {
      Writer osw = null;
      try
      {
        osw = Encodings.getWriter(output, encoding);
      }
      catch (UnsupportedEncodingException uee)
      {
        osw = null;
      }
      

      if (osw == null) {
        System.out.println("Warning: encoding \"" + encoding + "\" not supported" + ", using " + "UTF-8");
        





        encoding = "UTF-8";
        setEncoding(encoding);
        try {
          osw = Encodings.getWriter(output, encoding);
        }
        catch (UnsupportedEncodingException e)
        {
          e.printStackTrace();
        }
      }
      setWriterInternal(osw, false);
    }
    else
    {
      Writer osw = new OutputStreamWriter(output);
      setWriterInternal(osw, false);
    }
  }
  



  public boolean setEscaping(boolean escape)
  {
    boolean temp = m_escaping;
    m_escaping = escape;
    return temp;
  }
  










  protected void indent(int depth)
    throws IOException
  {
    if (m_startNewLine) {
      outputLineSep();
    }
    


    if (m_indentAmount > 0) {
      printSpace(depth * m_indentAmount);
    }
  }
  



  protected void indent()
    throws IOException
  {
    indent(m_elemContext.m_currentElemDepth);
  }
  




  private void printSpace(int n)
    throws IOException
  {
    Writer writer = m_writer;
    for (int i = 0; i < n; i++)
    {
      writer.write(32);
    }
  }
  


























  public void attributeDecl(String eName, String aName, String type, String valueDefault, String value)
    throws SAXException
  {
    if (m_inExternalDTD) {
      return;
    }
    try {
      Writer writer = m_writer;
      DTDprolog();
      
      writer.write("<!ATTLIST ");
      writer.write(eName);
      writer.write(32);
      
      writer.write(aName);
      writer.write(32);
      writer.write(type);
      if (valueDefault != null)
      {
        writer.write(32);
        writer.write(valueDefault);
      }
      


      writer.write(62);
      writer.write(m_lineSep, 0, m_lineSepLen);
    }
    catch (IOException e)
    {
      throw new SAXException(e);
    }
  }
  





  public Writer getWriter()
  {
    return m_writer;
  }
  

















  public void externalEntityDecl(String name, String publicId, String systemId)
    throws SAXException
  {
    try
    {
      DTDprolog();
      
      m_writer.write("<!ENTITY ");
      m_writer.write(name);
      if (publicId != null) {
        m_writer.write(" PUBLIC \"");
        m_writer.write(publicId);
      }
      else
      {
        m_writer.write(" SYSTEM \"");
        m_writer.write(systemId);
      }
      m_writer.write("\" >");
      m_writer.write(m_lineSep, 0, m_lineSepLen);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }
  

  protected boolean escapingNotNeeded(char ch)
  {
    boolean ret;
    
    boolean ret;
    
    if (ch < '')
    {
      boolean ret;
      
      if ((ch >= ' ') || ('\n' == ch) || ('\r' == ch) || ('\t' == ch))
      {
        ret = true;
      } else {
        ret = false;
      }
    } else {
      ret = m_encodingInfo.isInEncoding(ch);
    }
    return ret;
  }
  
























  protected int writeUTF16Surrogate(char c, char[] ch, int i, int end)
    throws IOException
  {
    int codePoint = 0;
    if (i + 1 >= end)
    {
      throw new IOException(Utils.messages.createMessage("ER_INVALID_UTF16_SURROGATE", new Object[] { Integer.toHexString(c) }));
    }
    



    char high = c;
    char low = ch[(i + 1)];
    if (!Encodings.isLowUTF16Surrogate(low)) {
      throw new IOException(Utils.messages.createMessage("ER_INVALID_UTF16_SURROGATE", new Object[] { Integer.toHexString(c) + " " + Integer.toHexString(low) }));
    }
    






    Writer writer = m_writer;
    

    if (m_encodingInfo.isInEncoding(c, low))
    {

      writer.write(ch, i, 2);

    }
    else
    {

      String encoding = getEncoding();
      if (encoding != null)
      {


        codePoint = Encodings.toCodePoint(high, low);
        
        writer.write(38);
        writer.write(35);
        writer.write(Integer.toString(codePoint));
        writer.write(59);

      }
      else
      {
        writer.write(ch, i, 2);
      }
    }
    
    return codePoint;
  }
  
























  int accumDefaultEntity(Writer writer, char ch, int i, char[] chars, int len, boolean fromTextNode, boolean escLF)
    throws IOException
  {
    if ((!escLF) && ('\n' == ch))
    {
      writer.write(m_lineSep, 0, m_lineSepLen);




    }
    else if (((fromTextNode) && (m_charInfo.shouldMapTextChar(ch))) || ((!fromTextNode) && (m_charInfo.shouldMapAttrChar(ch))))
    {
      String outputStringForChar = m_charInfo.getOutputStringForChar(ch);
      
      if (null != outputStringForChar)
      {
        writer.write(outputStringForChar);
      }
      else {
        return i;
      }
    } else {
      return i;
    }
    
    return i + 1;
  }
  


















  void writeNormalizedChars(char[] ch, int start, int length, boolean isCData, boolean useSystemLineSeparator)
    throws IOException, SAXException
  {
    Writer writer = m_writer;
    int end = start + length;
    
    for (int i = start; i < end; i++)
    {
      char c = ch[i];
      
      if (('\n' == c) && (useSystemLineSeparator))
      {
        writer.write(m_lineSep, 0, m_lineSepLen);
      }
      else if ((isCData) && (!escapingNotNeeded(c)))
      {

        if (m_cdataTagOpen) {
          closeCDATA();
        }
        
        if (Encodings.isHighUTF16Surrogate(c))
        {
          writeUTF16Surrogate(c, ch, i, end);
          i++;
        }
        else
        {
          writer.write("&#");
          
          String intStr = Integer.toString(c);
          
          writer.write(intStr);
          writer.write(59);



        }
        



      }
      else if ((isCData) && (i < end - 2) && (']' == c) && (']' == ch[(i + 1)]) && ('>' == ch[(i + 2)]))
      {





        writer.write("]]]]><![CDATA[>");
        
        i += 2;


      }
      else if (escapingNotNeeded(c))
      {
        if ((isCData) && (!m_cdataTagOpen))
        {
          writer.write("<![CDATA[");
          m_cdataTagOpen = true;
        }
        writer.write(c);


      }
      else if (Encodings.isHighUTF16Surrogate(c))
      {
        if (m_cdataTagOpen)
          closeCDATA();
        writeUTF16Surrogate(c, ch, i, end);
        i++;
      }
      else
      {
        if (m_cdataTagOpen)
          closeCDATA();
        writer.write("&#");
        
        String intStr = Integer.toString(c);
        
        writer.write(intStr);
        writer.write(59);
      }
    }
  }
  








  public void endNonEscaping()
    throws SAXException
  {
    m_disableOutputEscapingStates.pop();
  }
  









  public void startNonEscaping()
    throws SAXException
  {
    m_disableOutputEscapingStates.push(true);
  }
  




























  protected void cdata(char[] ch, int start, int length)
    throws SAXException
  {
    try
    {
      int old_start = start;
      if (m_elemContext.m_startTagOpen)
      {
        closeStartTag();
        m_elemContext.m_startTagOpen = false;
      }
      m_ispreserve = true;
      
      if (shouldIndent()) {
        indent();
      }
      boolean writeCDataBrackets = (length >= 1) && (escapingNotNeeded(ch[start]));
      





      if ((writeCDataBrackets) && (!m_cdataTagOpen))
      {
        m_writer.write("<![CDATA[");
        m_cdataTagOpen = true;
      }
      

      if (isEscapingDisabled())
      {
        charactersRaw(ch, start, length);
      }
      else {
        writeNormalizedChars(ch, start, length, true, m_lineSepUse);
      }
      



      if (writeCDataBrackets)
      {





        if (ch[(start + length - 1)] == ']') {
          closeCDATA();
        }
      }
      
      if (m_tracer != null) {
        super.fireCDATAEvent(ch, old_start, length);
      }
    }
    catch (IOException ioe) {
      throw new SAXException(Utils.messages.createMessage("ER_OIERROR", null), ioe);
    }
  }
  










  private boolean isEscapingDisabled()
  {
    return m_disableOutputEscapingStates.peekOrFalse();
  }
  











  protected void charactersRaw(char[] ch, int start, int length)
    throws SAXException
  {
    if (m_inEntityRef) {
      return;
    }
    try {
      if (m_elemContext.m_startTagOpen)
      {
        closeStartTag();
        m_elemContext.m_startTagOpen = false;
      }
      
      m_ispreserve = true;
      
      m_writer.write(ch, start, length);
    }
    catch (IOException e)
    {
      throw new SAXException(e);
    }
  }
  
































  public void characters(char[] chars, int start, int length)
    throws SAXException
  {
    if ((length == 0) || ((m_inEntityRef) && (!m_expandDTDEntities))) {
      return;
    }
    m_docIsEmpty = false;
    
    if (m_elemContext.m_startTagOpen)
    {
      closeStartTag();
      m_elemContext.m_startTagOpen = false;
    }
    else if (m_needToCallStartDocument)
    {
      startDocumentInternal();
    }
    
    if ((m_cdataStartCalled) || (m_elemContext.m_isCdataSection))
    {



      cdata(chars, start, length);
      
      return;
    }
    
    if (m_cdataTagOpen) {
      closeCDATA();
    }
    if ((m_disableOutputEscapingStates.peekOrFalse()) || (!m_escaping))
    {
      charactersRaw(chars, start, length);
      

      if (m_tracer != null) {
        super.fireCharEvent(chars, start, length);
      }
      return;
    }
    
    if (m_elemContext.m_startTagOpen)
    {
      closeStartTag();
      m_elemContext.m_startTagOpen = false;
    }
    







    try
    {
      int end = start + length;
      int lastDirtyCharProcessed = start - 1;
      
      Writer writer = m_writer;
      boolean isAllWhitespace = true;
      

      int i = start;
      while ((i < end) && (isAllWhitespace)) {
        char ch1 = chars[i];
        
        if (m_charInfo.shouldMapTextChar(ch1))
        {



          writeOutCleanChars(chars, i, lastDirtyCharProcessed);
          String outputStringForChar = m_charInfo.getOutputStringForChar(ch1);
          
          writer.write(outputStringForChar);
          

          isAllWhitespace = false;
          lastDirtyCharProcessed = i;
          
          i++;
        }
        else {
          switch (ch1)
          {

          case ' ': 
            i++;
            break;
          case '\n': 
            lastDirtyCharProcessed = processLineFeed(chars, i, lastDirtyCharProcessed, writer);
            
            i++;
            break;
          case '\r': 
            writeOutCleanChars(chars, i, lastDirtyCharProcessed);
            writer.write("&#13;");
            lastDirtyCharProcessed = i;
            i++;
            break;
          
          case '\t': 
            i++;
            break;
          


          default: 
            isAllWhitespace = false;
          }
          
        }
      }
      



      if ((i < end) || (!isAllWhitespace)) {
        m_ispreserve = true;
      }
      for (; 
          i < end; i++)
      {
        char ch = chars[i];
        
        if (m_charInfo.shouldMapTextChar(ch))
        {


          writeOutCleanChars(chars, i, lastDirtyCharProcessed);
          String outputStringForChar = m_charInfo.getOutputStringForChar(ch);
          writer.write(outputStringForChar);
          lastDirtyCharProcessed = i;

        }
        else if (ch <= '\037')
        {














          switch (ch)
          {
          case '\t': 
            break;
          
          case '\n': 
            lastDirtyCharProcessed = processLineFeed(chars, i, lastDirtyCharProcessed, writer);
            break;
          case '\r': 
            writeOutCleanChars(chars, i, lastDirtyCharProcessed);
            writer.write("&#13;");
            lastDirtyCharProcessed = i;
            
            break;
          case '\013': case '\f': default: 
            writeOutCleanChars(chars, i, lastDirtyCharProcessed);
            writer.write("&#");
            writer.write(Integer.toString(ch));
            writer.write(59);
            lastDirtyCharProcessed = i;
            break;
          }
          
        }
        else if (ch >= '')
        {




          if (ch <= '')
          {

            writeOutCleanChars(chars, i, lastDirtyCharProcessed);
            writer.write("&#");
            writer.write(Integer.toString(ch));
            writer.write(59);
            lastDirtyCharProcessed = i;
          }
          else if (ch == ' ')
          {
            writeOutCleanChars(chars, i, lastDirtyCharProcessed);
            writer.write("&#8232;");
            lastDirtyCharProcessed = i;
          }
          else if (!m_encodingInfo.isInEncoding(ch))
          {









            writeOutCleanChars(chars, i, lastDirtyCharProcessed);
            writer.write("&#");
            writer.write(Integer.toString(ch));
            writer.write(59);
            lastDirtyCharProcessed = i;
          }
        }
      }
      


      int startClean = lastDirtyCharProcessed + 1;
      if (i > startClean)
      {
        int lengthClean = i - startClean;
        m_writer.write(chars, startClean, lengthClean);
      }
      

      m_isprevtext = true;
    }
    catch (IOException e)
    {
      throw new SAXException(e);
    }
    

    if (m_tracer != null)
      super.fireCharEvent(chars, start, length);
  }
  
  private int processLineFeed(char[] chars, int i, int lastProcessed, Writer writer) throws IOException {
    if ((m_lineSepUse) && ((m_lineSepLen != 1) || (m_lineSep[0] != '\n')))
    {





      writeOutCleanChars(chars, i, lastProcessed);
      writer.write(m_lineSep, 0, m_lineSepLen);
      lastProcessed = i;
    }
    return lastProcessed;
  }
  
  private void writeOutCleanChars(char[] chars, int i, int lastProcessed) throws IOException
  {
    int startClean = lastProcessed + 1;
    if (startClean < i)
    {
      int lengthClean = i - startClean;
      m_writer.write(chars, startClean, lengthClean);
    }
  }
  









  private static boolean isCharacterInC0orC1Range(char ch)
  {
    if ((ch == '\t') || (ch == '\n') || (ch == '\r')) {
      return false;
    }
    return ((ch >= '') && (ch <= '')) || ((ch >= '\001') && (ch <= '\037'));
  }
  







  private static boolean isNELorLSEPCharacter(char ch)
  {
    return (ch == '') || (ch == ' ');
  }
  

















  private int processDirty(char[] chars, int end, int i, char ch, int lastDirty, boolean fromTextNode)
    throws IOException
  {
    int startClean = lastDirty + 1;
    

    if (i > startClean)
    {
      int lengthClean = i - startClean;
      m_writer.write(chars, startClean, lengthClean);
    }
    

    if (('\n' == ch) && (fromTextNode))
    {
      m_writer.write(m_lineSep, 0, m_lineSepLen);
    }
    else
    {
      startClean = accumDefaultEscape(m_writer, ch, i, chars, end, fromTextNode, false);
      







      i = startClean - 1;
    }
    

    return i;
  }
  






  public void characters(String s)
    throws SAXException
  {
    if ((m_inEntityRef) && (!m_expandDTDEntities))
      return;
    int length = s.length();
    if (length > m_charsBuff.length)
    {
      m_charsBuff = new char[length * 2 + 1];
    }
    s.getChars(0, length, m_charsBuff, 0);
    characters(m_charsBuff, 0, length);
  }
  

























  private int accumDefaultEscape(Writer writer, char ch, int i, char[] chars, int len, boolean fromTextNode, boolean escLF)
    throws IOException
  {
    int pos = accumDefaultEntity(writer, ch, i, chars, len, fromTextNode, escLF);
    
    if (i == pos)
    {
      if (Encodings.isHighUTF16Surrogate(ch))
      {




        int codePoint = 0;
        
        if (i + 1 >= len)
        {
          throw new IOException(Utils.messages.createMessage("ER_INVALID_UTF16_SURROGATE", new Object[] { Integer.toHexString(ch) }));
        }
        







        char next = chars[(++i)];
        
        if (!Encodings.isLowUTF16Surrogate(next)) {
          throw new IOException(Utils.messages.createMessage("ER_INVALID_UTF16_SURROGATE", new Object[] { Integer.toHexString(ch) + " " + Integer.toHexString(next) }));
        }
        








        codePoint = Encodings.toCodePoint(ch, next);
        

        writer.write("&#");
        writer.write(Integer.toString(codePoint));
        writer.write(59);
        pos += 2;



      }
      else
      {


        if ((isCharacterInC0orC1Range(ch)) || (isNELorLSEPCharacter(ch)))
        {
          writer.write("&#");
          writer.write(Integer.toString(ch));
          writer.write(59);
        }
        else if (((!escapingNotNeeded(ch)) || ((fromTextNode) && (m_charInfo.shouldMapTextChar(ch))) || ((!fromTextNode) && (m_charInfo.shouldMapAttrChar(ch)))) && (m_elemContext.m_currentElemDepth > 0))
        {



          writer.write("&#");
          writer.write(Integer.toString(ch));
          writer.write(59);
        }
        else
        {
          writer.write(ch);
        }
        pos++;
      }
    }
    
    return pos;
  }
  


























  public void startElement(String namespaceURI, String localName, String name, Attributes atts)
    throws SAXException
  {
    if (m_inEntityRef) {
      return;
    }
    if (m_needToCallStartDocument)
    {
      startDocumentInternal();
      m_needToCallStartDocument = false;
      m_docIsEmpty = false;
    }
    else if (m_cdataTagOpen) {
      closeCDATA();
    }
    try {
      if (m_needToOutputDocTypeDecl) {
        if (null != getDoctypeSystem()) {
          outputDocTypeDecl(name, true);
        }
        m_needToOutputDocTypeDecl = false;
      }
      



      if (m_elemContext.m_startTagOpen)
      {
        closeStartTag();
        m_elemContext.m_startTagOpen = false;
      }
      
      if (namespaceURI != null) {
        ensurePrefixIsDeclared(namespaceURI, name);
      }
      m_ispreserve = false;
      
      if ((shouldIndent()) && (m_startNewLine))
      {
        indent();
      }
      
      m_startNewLine = true;
      
      Writer writer = m_writer;
      writer.write(60);
      writer.write(name);
    }
    catch (IOException e)
    {
      throw new SAXException(e);
    }
    

    if (atts != null) {
      addAttributes(atts);
    }
    m_elemContext = m_elemContext.push(namespaceURI, localName, name);
    m_isprevtext = false;
    
    if (m_tracer != null) {
      firePseudoAttributes();
    }
  }
  























  public void startElement(String elementNamespaceURI, String elementLocalName, String elementName)
    throws SAXException
  {
    startElement(elementNamespaceURI, elementLocalName, elementName, null);
  }
  
  public void startElement(String elementName) throws SAXException
  {
    startElement(null, null, elementName, null);
  }
  







  void outputDocTypeDecl(String name, boolean closeDecl)
    throws SAXException
  {
    if (m_cdataTagOpen) {
      closeCDATA();
    }
    try {
      Writer writer = m_writer;
      writer.write("<!DOCTYPE ");
      writer.write(name);
      
      String doctypePublic = getDoctypePublic();
      if (null != doctypePublic)
      {
        writer.write(" PUBLIC \"");
        writer.write(doctypePublic);
        writer.write(34);
      }
      
      String doctypeSystem = getDoctypeSystem();
      if (null != doctypeSystem)
      {
        if (null == doctypePublic) {
          writer.write(" SYSTEM \"");
        } else {
          writer.write(" \"");
        }
        writer.write(doctypeSystem);
        
        if (closeDecl)
        {
          writer.write("\">");
          writer.write(m_lineSep, 0, m_lineSepLen);
          closeDecl = false;
        }
        else {
          writer.write(34);
        }
      }
    }
    catch (IOException e) {
      throw new SAXException(e);
    }
  }
  

















  public void processAttributes(Writer writer, int nAttrs)
    throws IOException, SAXException
  {
    String encoding = getEncoding();
    for (int i = 0; i < nAttrs; i++)
    {

      String name = m_attributes.getQName(i);
      String value = m_attributes.getValue(i);
      writer.write(32);
      writer.write(name);
      writer.write("=\"");
      writeAttrString(writer, value, encoding);
      writer.write(34);
    }
  }
  












  public void writeAttrString(Writer writer, String string, String encoding)
    throws IOException
  {
    int len = string.length();
    if (len > m_attrBuff.length)
    {
      m_attrBuff = new char[len * 2 + 1];
    }
    string.getChars(0, len, m_attrBuff, 0);
    char[] stringChars = m_attrBuff;
    
    for (int i = 0; i < len; i++)
    {
      char ch = stringChars[i];
      
      if (m_charInfo.shouldMapAttrChar(ch))
      {


        accumDefaultEscape(writer, ch, i, stringChars, len, false, true);
      }
      else {
        if (('\000' <= ch) && (ch <= '\037')) {}
        













        switch (ch)
        {
        case '\t': 
          writer.write("&#9;");
          break;
        case '\n': 
          writer.write("&#10;");
          break;
        case '\r': 
          writer.write("&#13;");
          break;
        case '\013': case '\f': default: 
          writer.write("&#");
          writer.write(Integer.toString(ch));
          writer.write(59);
          continue;
          


          if (ch < '')
          {

            writer.write(ch);
          }
          else if (ch <= '')
          {

            writer.write("&#");
            writer.write(Integer.toString(ch));
            writer.write(59);
          }
          else if (ch == ' ')
          {
            writer.write("&#8232;");
          }
          else if (m_encodingInfo.isInEncoding(ch))
          {


            writer.write(ch);


          }
          else
          {

            writer.write("&#");
            writer.write(Integer.toString(ch));
            writer.write(59);
          }
          






          break;
        }
        
      }
    }
  }
  







  public void endElement(String namespaceURI, String localName, String name)
    throws SAXException
  {
    if (m_inEntityRef) {
      return;
    }
    

    m_prefixMap.popNamespaces(m_elemContext.m_currentElemDepth, null);
    
    try
    {
      Writer writer = m_writer;
      if (m_elemContext.m_startTagOpen)
      {
        if (m_tracer != null)
          super.fireStartElem(m_elemContext.m_elementName);
        int nAttrs = m_attributes.getLength();
        if (nAttrs > 0)
        {
          processAttributes(m_writer, nAttrs);
          
          m_attributes.clear();
        }
        if (m_spaceBeforeClose) {
          writer.write(" />");
        } else {
          writer.write("/>");

        }
        

      }
      else
      {

        if (m_cdataTagOpen) {
          closeCDATA();
        }
        if (shouldIndent())
          indent(m_elemContext.m_currentElemDepth - 1);
        writer.write(60);
        writer.write(47);
        writer.write(name);
        writer.write(62);
      }
    }
    catch (IOException e)
    {
      throw new SAXException(e);
    }
    
    if ((!m_elemContext.m_startTagOpen) && (m_doIndent))
    {
      m_ispreserve = (m_preserves.isEmpty() ? false : m_preserves.pop());
    }
    
    m_isprevtext = false;
    

    if (m_tracer != null)
      super.fireEndElem(name);
    m_elemContext = m_elemContext.m_prev;
  }
  





  public void endElement(String name)
    throws SAXException
  {
    endElement(null, null, name);
  }
  
















  public void startPrefixMapping(String prefix, String uri)
    throws SAXException
  {
    startPrefixMapping(prefix, uri, true);
  }
  











  public boolean startPrefixMapping(String prefix, String uri, boolean shouldFlush)
    throws SAXException
  {
    int pushDepth;
    









    int pushDepth;
    









    if (shouldFlush)
    {
      flushPending();
      
      pushDepth = m_elemContext.m_currentElemDepth + 1;

    }
    else
    {
      pushDepth = m_elemContext.m_currentElemDepth;
    }
    boolean pushed = m_prefixMap.pushNamespace(prefix, uri, pushDepth);
    
    if (pushed)
    {






      if ("".equals(prefix))
      {
        String name = "xmlns";
        addAttributeAlways("http://www.w3.org/2000/xmlns/", name, name, "CDATA", uri, false);


      }
      else if (!"".equals(uri))
      {

        String name = "xmlns:" + prefix;
        




        addAttributeAlways("http://www.w3.org/2000/xmlns/", prefix, name, "CDATA", uri, false);
      }
    }
    
    return pushed;
  }
  










  public void comment(char[] ch, int start, int length)
    throws SAXException
  {
    int start_old = start;
    if (m_inEntityRef)
      return;
    if (m_elemContext.m_startTagOpen)
    {
      closeStartTag();
      m_elemContext.m_startTagOpen = false;
    }
    else if (m_needToCallStartDocument)
    {
      startDocumentInternal();
      m_needToCallStartDocument = false;
    }
    
    try
    {
      int limit = start + length;
      boolean wasDash = false;
      if (m_cdataTagOpen) {
        closeCDATA();
      }
      if (shouldIndent()) {
        indent();
      }
      Writer writer = m_writer;
      writer.write("<!--");
      
      for (int i = start; i < limit; i++)
      {
        if ((wasDash) && (ch[i] == '-'))
        {
          writer.write(ch, start, i - start);
          writer.write(" -");
          start = i + 1;
        }
        wasDash = ch[i] == '-';
      }
      

      if (length > 0)
      {

        int remainingChars = limit - start;
        if (remainingChars > 0) {
          writer.write(ch, start, remainingChars);
        }
        if (ch[(limit - 1)] == '-')
          writer.write(32);
      }
      writer.write("-->");
    }
    catch (IOException e)
    {
      throw new SAXException(e);
    }
    









    m_startNewLine = true;
    
    if (m_tracer != null) {
      super.fireCommentEvent(ch, start_old, length);
    }
  }
  




  public void endCDATA()
    throws SAXException
  {
    if (m_cdataTagOpen)
      closeCDATA();
    m_cdataStartCalled = false;
  }
  




  public void endDTD()
    throws SAXException
  {
    try
    {
      if (m_needToOutputDocTypeDecl)
      {
        outputDocTypeDecl(m_elemContext.m_elementName, false);
        m_needToOutputDocTypeDecl = false;
      }
      Writer writer = m_writer;
      if (!m_inDoctype) {
        writer.write("]>");
      }
      else {
        writer.write(62);
      }
      
      writer.write(m_lineSep, 0, m_lineSepLen);
    }
    catch (IOException e)
    {
      throw new SAXException(e);
    }
  }
  












  public void endPrefixMapping(String prefix)
    throws SAXException
  {}
  












  public void ignorableWhitespace(char[] ch, int start, int length)
    throws SAXException
  {
    if (0 == length)
      return;
    characters(ch, start, length);
  }
  








  public void skippedEntity(String name)
    throws SAXException
  {}
  








  public void startCDATA()
    throws SAXException
  {
    m_cdataStartCalled = true;
  }
  














  public void startEntity(String name)
    throws SAXException
  {
    if (name.equals("[dtd]")) {
      m_inExternalDTD = true;
    }
    if ((!m_expandDTDEntities) && (!m_inExternalDTD))
    {



      startNonEscaping();
      characters("&" + name + ';');
      endNonEscaping();
    }
    
    m_inEntityRef = true;
  }
  






  protected void closeStartTag()
    throws SAXException
  {
    if (m_elemContext.m_startTagOpen)
    {

      try
      {
        if (m_tracer != null)
          super.fireStartElem(m_elemContext.m_elementName);
        int nAttrs = m_attributes.getLength();
        if (nAttrs > 0)
        {
          processAttributes(m_writer, nAttrs);
          
          m_attributes.clear();
        }
        m_writer.write(62);
      }
      catch (IOException e)
      {
        throw new SAXException(e);
      }
      




      if (m_CdataElems != null) {
        m_elemContext.m_isCdataSection = isCdataSection();
      }
      if (m_doIndent)
      {
        m_isprevtext = false;
        m_preserves.push(m_ispreserve);
      }
    }
  }
  

















  public void startDTD(String name, String publicId, String systemId)
    throws SAXException
  {
    setDoctypeSystem(systemId);
    setDoctypePublic(publicId);
    
    m_elemContext.m_elementName = name;
    m_inDoctype = true;
  }
  




  public int getIndentAmount()
  {
    return m_indentAmount;
  }
  





  public void setIndentAmount(int m_indentAmount)
  {
    this.m_indentAmount = m_indentAmount;
  }
  






  protected boolean shouldIndent()
  {
    return (m_doIndent) && (!m_ispreserve) && (!m_isprevtext) && (m_elemContext.m_currentElemDepth > 0);
  }
  

















  private void setCdataSectionElements(String key, Properties props)
  {
    String s = props.getProperty(key);
    
    if (null != s)
    {

      Vector v = new Vector();
      int l = s.length();
      boolean inCurly = false;
      StringBuffer buf = new StringBuffer();
      



      for (int i = 0; i < l; i++)
      {
        char c = s.charAt(i);
        
        if (Character.isWhitespace(c))
        {
          if (!inCurly)
          {
            if (buf.length() <= 0)
              continue;
            addCdataSectionElement(buf.toString(), v);
            buf.setLength(0); continue;
          }
          

        }
        else if ('{' == c) {
          inCurly = true;
        } else if ('}' == c) {
          inCurly = false;
        }
        buf.append(c);
      }
      
      if (buf.length() > 0)
      {
        addCdataSectionElement(buf.toString(), v);
        buf.setLength(0);
      }
      
      setCdataSectionElements(v);
    }
  }
  









  private void addCdataSectionElement(String URI_and_localName, Vector v)
  {
    StringTokenizer tokenizer = new StringTokenizer(URI_and_localName, "{}", false);
    
    String s1 = tokenizer.nextToken();
    String s2 = tokenizer.hasMoreTokens() ? tokenizer.nextToken() : null;
    
    if (null == s2)
    {

      v.addElement(null);
      v.addElement(s1);

    }
    else
    {
      v.addElement(s1);
      v.addElement(s2);
    }
  }
  








  public void setCdataSectionElements(Vector URI_and_localNames)
  {
    if (URI_and_localNames != null)
    {
      int len = URI_and_localNames.size() - 1;
      if (len > 0)
      {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i += 2)
        {

          if (i != 0)
            sb.append(' ');
          String uri = (String)URI_and_localNames.elementAt(i);
          String localName = (String)URI_and_localNames.elementAt(i + 1);
          
          if (uri != null)
          {

            sb.append('{');
            sb.append(uri);
            sb.append('}');
          }
          sb.append(localName);
        }
        m_StringOfCDATASections = sb.toString();
      }
    }
    initCdataElems(m_StringOfCDATASections);
  }
  













  protected String ensureAttributesNamespaceIsDeclared(String ns, String localName, String rawName)
    throws SAXException
  {
    if ((ns != null) && (ns.length() > 0))
    {


      int index = 0;
      String prefixFromRawName = (index = rawName.indexOf(":")) < 0 ? "" : rawName.substring(0, index);
      



      if (index > 0)
      {

        String uri = m_prefixMap.lookupNamespace(prefixFromRawName);
        if ((uri != null) && (uri.equals(ns)))
        {


          return null;
        }
        



        startPrefixMapping(prefixFromRawName, ns, false);
        addAttribute("http://www.w3.org/2000/xmlns/", prefixFromRawName, "xmlns:" + prefixFromRawName, "CDATA", ns, false);
        




        return prefixFromRawName;
      }
      




      String prefix = m_prefixMap.lookupPrefix(ns);
      if (prefix == null)
      {


        prefix = m_prefixMap.generateNextPrefix();
        startPrefixMapping(prefix, ns, false);
        addAttribute("http://www.w3.org/2000/xmlns/", prefix, "xmlns:" + prefix, "CDATA", ns, false);
      }
      





      return prefix;
    }
    

    return null;
  }
  

  void ensurePrefixIsDeclared(String ns, String rawName)
    throws SAXException
  {
    if ((ns != null) && (ns.length() > 0))
    {
      int index;
      boolean no_prefix = (index = rawName.indexOf(":")) < 0;
      String prefix = no_prefix ? "" : rawName.substring(0, index);
      
      if (null != prefix)
      {
        String foundURI = m_prefixMap.lookupNamespace(prefix);
        
        if ((null == foundURI) || (!foundURI.equals(ns)))
        {
          startPrefixMapping(prefix, ns);
          



          addAttributeAlways("http://www.w3.org/2000/xmlns/", no_prefix ? "xmlns" : prefix, "xmlns:" + prefix, "CDATA", ns, false);
        }
      }
    }
  }
  










  public void flushPending()
    throws SAXException
  {
    if (m_needToCallStartDocument)
    {
      startDocumentInternal();
      m_needToCallStartDocument = false;
    }
    if (m_elemContext.m_startTagOpen)
    {
      closeStartTag();
      m_elemContext.m_startTagOpen = false;
    }
    
    if (m_cdataTagOpen)
    {
      closeCDATA();
      m_cdataTagOpen = false;
    }
    if (m_writer != null) {
      try {
        m_writer.flush();
      }
      catch (IOException e) {}
    }
  }
  








  public void setContentHandler(ContentHandler ch) {}
  








  public boolean addAttributeAlways(String uri, String localName, String rawName, String type, String value, boolean xslAttribute)
  {
    int index;
    







    int index;
    







    if ((uri == null) || (localName == null) || (uri.length() == 0)) {
      index = m_attributes.getIndex(rawName);
    } else {
      index = m_attributes.getIndex(uri, localName);
    }
    boolean was_added;
    if (index >= 0)
    {
      String old_value = null;
      if (m_tracer != null)
      {
        old_value = m_attributes.getValue(index);
        if (value.equals(old_value)) {
          old_value = null;
        }
      }
      



      m_attributes.setValue(index, value);
      boolean was_added = false;
      if (old_value != null) {
        firePseudoAttributes();
      }
      
    }
    else
    {
      if (xslAttribute)
      {













        int colonIndex = rawName.indexOf(':');
        if (colonIndex > 0)
        {
          String prefix = rawName.substring(0, colonIndex);
          NamespaceMappings.MappingRecord existing_mapping = m_prefixMap.getMappingFromPrefix(prefix);
          



          if ((existing_mapping != null) && (m_declarationDepth == m_elemContext.m_currentElemDepth) && (!m_uri.equals(uri)))
          {












            prefix = m_prefixMap.lookupPrefix(uri);
            if (prefix == null)
            {









              prefix = m_prefixMap.generateNextPrefix();
            }
            
            rawName = prefix + ':' + localName;
          }
        }
        





        try
        {
          prefixUsed = ensureAttributesNamespaceIsDeclared(uri, localName, rawName);
        }
        catch (SAXException e)
        {
          String prefixUsed;
          



          e.printStackTrace();
        }
      }
      m_attributes.addAttribute(uri, localName, rawName, type, value);
      was_added = true;
      if (m_tracer != null)
        firePseudoAttributes();
    }
    return was_added;
  }
  






  protected void firePseudoAttributes()
  {
    if (m_tracer != null)
    {
      try
      {

        m_writer.flush();
        

        StringBuffer sb = new StringBuffer();
        int nAttrs = m_attributes.getLength();
        if (nAttrs > 0)
        {


          Writer writer = new WritertoStringBuffer(sb);
          

          processAttributes(writer, nAttrs);
        }
        


        sb.append('>');
        


        char[] ch = sb.toString().toCharArray();
        m_tracer.fireGenerateEvent(11, ch, 0, ch.length);
      }
      catch (IOException ioe) {}catch (SAXException se) {}
    }
  }
  









  private static class WritertoStringBuffer
    extends Writer
  {
    private final StringBuffer m_stringbuf;
    









    WritertoStringBuffer(StringBuffer sb)
    {
      m_stringbuf = sb;
    }
    
    public void write(char[] arg0, int arg1, int arg2) throws IOException
    {
      m_stringbuf.append(arg0, arg1, arg2);
    }
    


    public void flush()
      throws IOException
    {}
    

    public void close()
      throws IOException
    {}
    

    public void write(int i)
    {
      m_stringbuf.append((char)i);
    }
    
    public void write(String s)
    {
      m_stringbuf.append(s);
    }
  }
  


  public void setTransformer(Transformer transformer)
  {
    super.setTransformer(transformer);
    if ((m_tracer != null) && (!(m_writer instanceof SerializerTraceWriter)))
    {
      setWriterInternal(new SerializerTraceWriter(m_writer, m_tracer), false);
    }
  }
  







  public boolean reset()
  {
    boolean wasReset = false;
    if (super.reset())
    {
      resetToStream();
      wasReset = true;
    }
    return wasReset;
  }
  




  private void resetToStream()
  {
    m_cdataStartCalled = false;
    






    m_disableOutputEscapingStates.clear();
    

    m_escaping = true;
    

    m_expandDTDEntities = true;
    m_inDoctype = false;
    m_ispreserve = false;
    m_isprevtext = false;
    m_isUTF8 = false;
    m_lineSep = s_systemLineSep;
    m_lineSepLen = s_systemLineSep.length;
    m_lineSepUse = true;
    
    m_preserves.clear();
    m_shouldFlush = true;
    m_spaceBeforeClose = false;
    m_startNewLine = false;
    m_writer_set_by_user = false;
  }
  




  public void setEncoding(String encoding)
  {
    setOutputProperty("encoding", encoding);
  }
  





  static final class BoolStack
  {
    private boolean[] m_values;
    




    private int m_allocatedSize;
    




    private int m_index;
    





    public BoolStack()
    {
      this(32);
    }
    






    public BoolStack(int size)
    {
      m_allocatedSize = size;
      m_values = new boolean[size];
      m_index = -1;
    }
    





    public final int size()
    {
      return m_index + 1;
    }
    




    public final void clear()
    {
      m_index = -1;
    }
    








    public final boolean push(boolean val)
    {
      if (m_index == m_allocatedSize - 1) {
        grow();
      }
      return m_values[(++m_index)] = val;
    }
    







    public final boolean pop()
    {
      return m_values[(m_index--)];
    }
    








    public final boolean popAndTop()
    {
      m_index -= 1;
      
      return m_index >= 0 ? m_values[m_index] : false;
    }
    






    public final void setTop(boolean b)
    {
      m_values[m_index] = b;
    }
    







    public final boolean peek()
    {
      return m_values[m_index];
    }
    






    public final boolean peekOrFalse()
    {
      return m_index > -1 ? m_values[m_index] : false;
    }
    






    public final boolean peekOrTrue()
    {
      return m_index > -1 ? m_values[m_index] : true;
    }
    






    public boolean isEmpty()
    {
      return m_index == -1;
    }
    





    private void grow()
    {
      m_allocatedSize *= 2;
      
      boolean[] newVector = new boolean[m_allocatedSize];
      
      System.arraycopy(m_values, 0, newVector, 0, m_index + 1);
      
      m_values = newVector;
    }
  }
  





  public void notationDecl(String name, String pubID, String sysID)
    throws SAXException
  {
    try
    {
      DTDprolog();
      
      m_writer.write("<!NOTATION ");
      m_writer.write(name);
      if (pubID != null) {
        m_writer.write(" PUBLIC \"");
        m_writer.write(pubID);
      }
      else
      {
        m_writer.write(" SYSTEM \"");
        m_writer.write(sysID);
      }
      m_writer.write("\" >");
      m_writer.write(m_lineSep, 0, m_lineSepLen);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }
  




  public void unparsedEntityDecl(String name, String pubID, String sysID, String notationName)
    throws SAXException
  {
    try
    {
      DTDprolog();
      
      m_writer.write("<!ENTITY ");
      m_writer.write(name);
      if (pubID != null) {
        m_writer.write(" PUBLIC \"");
        m_writer.write(pubID);
      }
      else
      {
        m_writer.write(" SYSTEM \"");
        m_writer.write(sysID);
      }
      m_writer.write("\" NDATA ");
      m_writer.write(notationName);
      m_writer.write(" >");
      m_writer.write(m_lineSep, 0, m_lineSepLen);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }
  



  private void DTDprolog()
    throws SAXException, IOException
  {
    Writer writer = m_writer;
    if (m_needToOutputDocTypeDecl)
    {
      outputDocTypeDecl(m_elemContext.m_elementName, false);
      m_needToOutputDocTypeDecl = false;
    }
    if (m_inDoctype)
    {
      writer.write(" [");
      writer.write(m_lineSep, 0, m_lineSepLen);
      m_inDoctype = false;
    }
  }
  



  public void setDTDEntityExpansion(boolean expand)
  {
    m_expandDTDEntities = expand;
  }
  



  public void setNewLine(char[] eolChars)
  {
    m_lineSep = eolChars;
    m_lineSepLen = eolChars.length;
  }
  










  public void addCdataSectionElements(String URI_and_localNames)
  {
    if (URI_and_localNames != null)
      initCdataElems(URI_and_localNames);
    if (m_StringOfCDATASections == null) {
      m_StringOfCDATASections = URI_and_localNames;
    } else {
      m_StringOfCDATASections = (m_StringOfCDATASections + " " + URI_and_localNames);
    }
  }
}
