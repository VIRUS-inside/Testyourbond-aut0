package org.apache.xml.serializer;

import java.io.IOException;
import java.io.Writer;
import java.util.Properties;
import org.apache.xml.serializer.utils.Messages;
import org.apache.xml.serializer.utils.Utils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;


































public class ToHTMLStream
  extends ToStream
{
  protected boolean m_inDTD = false;
  


  private boolean m_inBlockElem = false;
  




  private final CharInfo m_htmlcharInfo = CharInfo.getCharInfo(CharInfo.HTML_ENTITIES_RESOURCE, "html");
  



  static final Trie m_elementFlags = new Trie();
  
  static {
    initTagReference(m_elementFlags);
  }
  
  static void initTagReference(Trie m_elementFlags)
  {
    m_elementFlags.put("BASEFONT", new ElemDesc(2));
    m_elementFlags.put("FRAME", new ElemDesc(10));
    

    m_elementFlags.put("FRAMESET", new ElemDesc(8));
    m_elementFlags.put("NOFRAMES", new ElemDesc(8));
    m_elementFlags.put("ISINDEX", new ElemDesc(10));
    

    m_elementFlags.put("APPLET", new ElemDesc(2097152));
    

    m_elementFlags.put("CENTER", new ElemDesc(8));
    m_elementFlags.put("DIR", new ElemDesc(8));
    m_elementFlags.put("MENU", new ElemDesc(8));
    

    m_elementFlags.put("TT", new ElemDesc(4096));
    m_elementFlags.put("I", new ElemDesc(4096));
    m_elementFlags.put("B", new ElemDesc(4096));
    m_elementFlags.put("BIG", new ElemDesc(4096));
    m_elementFlags.put("SMALL", new ElemDesc(4096));
    m_elementFlags.put("EM", new ElemDesc(8192));
    m_elementFlags.put("STRONG", new ElemDesc(8192));
    m_elementFlags.put("DFN", new ElemDesc(8192));
    m_elementFlags.put("CODE", new ElemDesc(8192));
    m_elementFlags.put("SAMP", new ElemDesc(8192));
    m_elementFlags.put("KBD", new ElemDesc(8192));
    m_elementFlags.put("VAR", new ElemDesc(8192));
    m_elementFlags.put("CITE", new ElemDesc(8192));
    m_elementFlags.put("ABBR", new ElemDesc(8192));
    m_elementFlags.put("ACRONYM", new ElemDesc(8192));
    m_elementFlags.put("SUP", new ElemDesc(98304));
    

    m_elementFlags.put("SUB", new ElemDesc(98304));
    

    m_elementFlags.put("SPAN", new ElemDesc(98304));
    

    m_elementFlags.put("BDO", new ElemDesc(98304));
    

    m_elementFlags.put("BR", new ElemDesc(98314));
    






    m_elementFlags.put("BODY", new ElemDesc(8));
    m_elementFlags.put("ADDRESS", new ElemDesc(56));
    





    m_elementFlags.put("DIV", new ElemDesc(56));
    





    m_elementFlags.put("A", new ElemDesc(32768));
    m_elementFlags.put("MAP", new ElemDesc(98312));
    


    m_elementFlags.put("AREA", new ElemDesc(10));
    

    m_elementFlags.put("LINK", new ElemDesc(131082));
    


    m_elementFlags.put("IMG", new ElemDesc(2195458));
    






    m_elementFlags.put("OBJECT", new ElemDesc(2326528));
    






    m_elementFlags.put("PARAM", new ElemDesc(2));
    m_elementFlags.put("HR", new ElemDesc(58));
    






    m_elementFlags.put("P", new ElemDesc(56));
    





    m_elementFlags.put("H1", new ElemDesc(262152));
    

    m_elementFlags.put("H2", new ElemDesc(262152));
    

    m_elementFlags.put("H3", new ElemDesc(262152));
    

    m_elementFlags.put("H4", new ElemDesc(262152));
    

    m_elementFlags.put("H5", new ElemDesc(262152));
    

    m_elementFlags.put("H6", new ElemDesc(262152));
    

    m_elementFlags.put("PRE", new ElemDesc(1048584));
    

    m_elementFlags.put("Q", new ElemDesc(98304));
    

    m_elementFlags.put("BLOCKQUOTE", new ElemDesc(56));
    





    m_elementFlags.put("INS", new ElemDesc(0));
    m_elementFlags.put("DEL", new ElemDesc(0));
    m_elementFlags.put("DL", new ElemDesc(56));
    





    m_elementFlags.put("DT", new ElemDesc(8));
    m_elementFlags.put("DD", new ElemDesc(8));
    m_elementFlags.put("OL", new ElemDesc(524296));
    

    m_elementFlags.put("UL", new ElemDesc(524296));
    

    m_elementFlags.put("LI", new ElemDesc(8));
    m_elementFlags.put("FORM", new ElemDesc(8));
    m_elementFlags.put("LABEL", new ElemDesc(16384));
    m_elementFlags.put("INPUT", new ElemDesc(18434));
    


    m_elementFlags.put("SELECT", new ElemDesc(18432));
    

    m_elementFlags.put("OPTGROUP", new ElemDesc(0));
    m_elementFlags.put("OPTION", new ElemDesc(0));
    m_elementFlags.put("TEXTAREA", new ElemDesc(18432));
    

    m_elementFlags.put("FIELDSET", new ElemDesc(24));
    

    m_elementFlags.put("LEGEND", new ElemDesc(0));
    m_elementFlags.put("BUTTON", new ElemDesc(18432));
    

    m_elementFlags.put("TABLE", new ElemDesc(56));
    





    m_elementFlags.put("CAPTION", new ElemDesc(8));
    m_elementFlags.put("THEAD", new ElemDesc(8));
    m_elementFlags.put("TFOOT", new ElemDesc(8));
    m_elementFlags.put("TBODY", new ElemDesc(8));
    m_elementFlags.put("COLGROUP", new ElemDesc(8));
    m_elementFlags.put("COL", new ElemDesc(10));
    

    m_elementFlags.put("TR", new ElemDesc(8));
    m_elementFlags.put("TH", new ElemDesc(0));
    m_elementFlags.put("TD", new ElemDesc(0));
    m_elementFlags.put("HEAD", new ElemDesc(4194312));
    

    m_elementFlags.put("TITLE", new ElemDesc(8));
    m_elementFlags.put("BASE", new ElemDesc(10));
    

    m_elementFlags.put("META", new ElemDesc(131082));
    


    m_elementFlags.put("STYLE", new ElemDesc(131336));
    


    m_elementFlags.put("SCRIPT", new ElemDesc(229632));
    






    m_elementFlags.put("NOSCRIPT", new ElemDesc(56));
    





    m_elementFlags.put("HTML", new ElemDesc(8388616));
    



    m_elementFlags.put("FONT", new ElemDesc(4096));
    

    m_elementFlags.put("S", new ElemDesc(4096));
    m_elementFlags.put("STRIKE", new ElemDesc(4096));
    

    m_elementFlags.put("U", new ElemDesc(4096));
    

    m_elementFlags.put("NOBR", new ElemDesc(4096));
    

    m_elementFlags.put("IFRAME", new ElemDesc(56));
    







    m_elementFlags.put("LAYER", new ElemDesc(56));
    






    m_elementFlags.put("ILAYER", new ElemDesc(56));
    











    ElemDesc elemDesc = (ElemDesc)m_elementFlags.get("a");
    elemDesc.setAttr("HREF", 2);
    elemDesc.setAttr("NAME", 2);
    

    elemDesc = (ElemDesc)m_elementFlags.get("area");
    
    elemDesc.setAttr("HREF", 2);
    elemDesc.setAttr("NOHREF", 4);
    

    elemDesc = (ElemDesc)m_elementFlags.get("base");
    
    elemDesc.setAttr("HREF", 2);
    

    elemDesc = (ElemDesc)m_elementFlags.get("button");
    elemDesc.setAttr("DISABLED", 4);
    

    elemDesc = (ElemDesc)m_elementFlags.get("blockquote");
    
    elemDesc.setAttr("CITE", 2);
    

    elemDesc = (ElemDesc)m_elementFlags.get("del");
    elemDesc.setAttr("CITE", 2);
    

    elemDesc = (ElemDesc)m_elementFlags.get("dir");
    elemDesc.setAttr("COMPACT", 4);
    


    elemDesc = (ElemDesc)m_elementFlags.get("div");
    elemDesc.setAttr("SRC", 2);
    elemDesc.setAttr("NOWRAP", 4);
    

    elemDesc = (ElemDesc)m_elementFlags.get("dl");
    elemDesc.setAttr("COMPACT", 4);
    

    elemDesc = (ElemDesc)m_elementFlags.get("form");
    elemDesc.setAttr("ACTION", 2);
    


    elemDesc = (ElemDesc)m_elementFlags.get("frame");
    elemDesc.setAttr("SRC", 2);
    elemDesc.setAttr("LONGDESC", 2);
    elemDesc.setAttr("NORESIZE", 4);
    

    elemDesc = (ElemDesc)m_elementFlags.get("head");
    elemDesc.setAttr("PROFILE", 2);
    

    elemDesc = (ElemDesc)m_elementFlags.get("hr");
    elemDesc.setAttr("NOSHADE", 4);
    


    elemDesc = (ElemDesc)m_elementFlags.get("iframe");
    elemDesc.setAttr("SRC", 2);
    elemDesc.setAttr("LONGDESC", 2);
    


    elemDesc = (ElemDesc)m_elementFlags.get("ilayer");
    elemDesc.setAttr("SRC", 2);
    

    elemDesc = (ElemDesc)m_elementFlags.get("img");
    elemDesc.setAttr("SRC", 2);
    elemDesc.setAttr("LONGDESC", 2);
    elemDesc.setAttr("USEMAP", 2);
    elemDesc.setAttr("ISMAP", 4);
    

    elemDesc = (ElemDesc)m_elementFlags.get("input");
    
    elemDesc.setAttr("SRC", 2);
    elemDesc.setAttr("USEMAP", 2);
    elemDesc.setAttr("CHECKED", 4);
    elemDesc.setAttr("DISABLED", 4);
    elemDesc.setAttr("ISMAP", 4);
    elemDesc.setAttr("READONLY", 4);
    

    elemDesc = (ElemDesc)m_elementFlags.get("ins");
    elemDesc.setAttr("CITE", 2);
    


    elemDesc = (ElemDesc)m_elementFlags.get("layer");
    elemDesc.setAttr("SRC", 2);
    

    elemDesc = (ElemDesc)m_elementFlags.get("link");
    elemDesc.setAttr("HREF", 2);
    

    elemDesc = (ElemDesc)m_elementFlags.get("menu");
    elemDesc.setAttr("COMPACT", 4);
    

    elemDesc = (ElemDesc)m_elementFlags.get("object");
    
    elemDesc.setAttr("CLASSID", 2);
    elemDesc.setAttr("CODEBASE", 2);
    elemDesc.setAttr("DATA", 2);
    elemDesc.setAttr("ARCHIVE", 2);
    elemDesc.setAttr("USEMAP", 2);
    elemDesc.setAttr("DECLARE", 4);
    

    elemDesc = (ElemDesc)m_elementFlags.get("ol");
    elemDesc.setAttr("COMPACT", 4);
    

    elemDesc = (ElemDesc)m_elementFlags.get("optgroup");
    elemDesc.setAttr("DISABLED", 4);
    

    elemDesc = (ElemDesc)m_elementFlags.get("option");
    elemDesc.setAttr("SELECTED", 4);
    elemDesc.setAttr("DISABLED", 4);
    

    elemDesc = (ElemDesc)m_elementFlags.get("q");
    elemDesc.setAttr("CITE", 2);
    

    elemDesc = (ElemDesc)m_elementFlags.get("script");
    elemDesc.setAttr("SRC", 2);
    elemDesc.setAttr("FOR", 2);
    elemDesc.setAttr("DEFER", 4);
    

    elemDesc = (ElemDesc)m_elementFlags.get("select");
    elemDesc.setAttr("DISABLED", 4);
    elemDesc.setAttr("MULTIPLE", 4);
    

    elemDesc = (ElemDesc)m_elementFlags.get("table");
    elemDesc.setAttr("NOWRAP", 4);
    

    elemDesc = (ElemDesc)m_elementFlags.get("td");
    elemDesc.setAttr("NOWRAP", 4);
    

    elemDesc = (ElemDesc)m_elementFlags.get("textarea");
    elemDesc.setAttr("DISABLED", 4);
    elemDesc.setAttr("READONLY", 4);
    

    elemDesc = (ElemDesc)m_elementFlags.get("th");
    elemDesc.setAttr("NOWRAP", 4);
    



    elemDesc = (ElemDesc)m_elementFlags.get("tr");
    elemDesc.setAttr("NOWRAP", 4);
    

    elemDesc = (ElemDesc)m_elementFlags.get("ul");
    elemDesc.setAttr("COMPACT", 4);
  }
  



  private static final ElemDesc m_dummy = new ElemDesc(8);
  

  private boolean m_specialEscapeURLs = true;
  

  private boolean m_omitMetaTag = false;
  





  public void setSpecialEscapeURLs(boolean bool)
  {
    m_specialEscapeURLs = bool;
  }
  





  public void setOmitMetaTag(boolean bool)
  {
    m_omitMetaTag = bool;
  }
  






















  public void setOutputFormat(Properties format)
  {
    String value = format.getProperty("{http://xml.apache.org/xalan}use-url-escaping");
    if (value != null) {
      m_specialEscapeURLs = OutputPropertyUtils.getBooleanProperty("{http://xml.apache.org/xalan}use-url-escaping", format);
    }
    








    value = format.getProperty("{http://xml.apache.org/xalan}omit-meta-tag");
    if (value != null) {
      m_omitMetaTag = OutputPropertyUtils.getBooleanProperty("{http://xml.apache.org/xalan}omit-meta-tag", format);
    }
    



    super.setOutputFormat(format);
  }
  





  private final boolean getSpecialEscapeURLs()
  {
    return m_specialEscapeURLs;
  }
  





  private final boolean getOmitMetaTag()
  {
    return m_omitMetaTag;
  }
  











  public static final ElemDesc getElemDesc(String name)
  {
    Object obj = m_elementFlags.get(name);
    if (null != obj)
      return (ElemDesc)obj;
    return m_dummy;
  }
  






  private Trie m_htmlInfo = new Trie(m_elementFlags);
  



  private ElemDesc getElemDesc2(String name)
  {
    Object obj = m_htmlInfo.get2(name);
    if (null != obj)
      return (ElemDesc)obj;
    return m_dummy;
  }
  








  public ToHTMLStream()
  {
    m_doIndent = true;
    m_charInfo = m_htmlcharInfo;
    
    m_prefixMap = new NamespaceMappings();
  }
  











  protected void startDocumentInternal()
    throws SAXException
  {
    super.startDocumentInternal();
    
    m_needToCallStartDocument = false;
    m_needToOutputDocTypeDecl = true;
    m_startNewLine = false;
    setOmitXMLDeclaration(true);
  }
  




  private void outputDocTypeDecl(String name)
    throws SAXException
  {
    if (true == m_needToOutputDocTypeDecl)
    {
      String doctypeSystem = getDoctypeSystem();
      String doctypePublic = getDoctypePublic();
      if ((null != doctypeSystem) || (null != doctypePublic))
      {
        Writer writer = m_writer;
        try
        {
          writer.write("<!DOCTYPE ");
          writer.write(name);
          
          if (null != doctypePublic)
          {
            writer.write(" PUBLIC \"");
            writer.write(doctypePublic);
            writer.write(34);
          }
          
          if (null != doctypeSystem)
          {
            if (null == doctypePublic) {
              writer.write(" SYSTEM \"");
            } else {
              writer.write(" \"");
            }
            writer.write(doctypeSystem);
            writer.write(34);
          }
          
          writer.write(62);
          outputLineSep();
        }
        catch (IOException e)
        {
          throw new SAXException(e);
        }
      }
    }
    
    m_needToOutputDocTypeDecl = false;
  }
  








  public final void endDocument()
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
  

















  public void startElement(String namespaceURI, String localName, String name, Attributes atts)
    throws SAXException
  {
    ElemContext elemContext = m_elemContext;
    

    if (m_startTagOpen)
    {
      closeStartTag();
      m_startTagOpen = false;
    }
    else if (m_cdataTagOpen)
    {
      closeCDATA();
      m_cdataTagOpen = false;
    }
    else if (m_needToCallStartDocument)
    {
      startDocumentInternal();
      m_needToCallStartDocument = false;
    }
    
    if (m_needToOutputDocTypeDecl) {
      String n = name;
      if ((n == null) || (n.length() == 0))
      {

        n = localName;
      }
      outputDocTypeDecl(n);
    }
    


    if ((null != namespaceURI) && (namespaceURI.length() > 0))
    {
      super.startElement(namespaceURI, localName, name, atts);
      
      return;
    }
    

    try
    {
      ElemDesc elemDesc = getElemDesc2(name);
      int elemFlags = elemDesc.getFlags();
      

      if (m_doIndent)
      {

        boolean isBlockElement = (elemFlags & 0x8) != 0;
        if (m_ispreserve) {
          m_ispreserve = false;
        } else if ((null != m_elementName) && ((!m_inBlockElem) || (isBlockElement)))
        {




          m_startNewLine = true;
          
          indent();
        }
        
        m_inBlockElem = (!isBlockElement);
      }
      

      if (atts != null) {
        addAttributes(atts);
      }
      m_isprevtext = false;
      Writer writer = m_writer;
      writer.write(60);
      writer.write(name);
      


      if (m_tracer != null) {
        firePseudoAttributes();
      }
      if ((elemFlags & 0x2) != 0)
      {


        m_elemContext = elemContext.push();
        


        m_elemContext.m_elementName = name;
        m_elemContext.m_elementDesc = elemDesc;
        return;
      }
      

      elemContext = elemContext.push(namespaceURI, localName, name);
      m_elemContext = elemContext;
      m_elementDesc = elemDesc;
      m_isRaw = ((elemFlags & 0x100) != 0);
      


      if ((elemFlags & 0x400000) != 0)
      {

        closeStartTag();
        m_startTagOpen = false;
        if (!m_omitMetaTag)
        {
          if (m_doIndent)
            indent();
          writer.write("<META http-equiv=\"Content-Type\" content=\"text/html; charset=");
          
          String encoding = getEncoding();
          String encode = Encodings.getMimeEncoding(encoding);
          writer.write(encode);
          writer.write("\">");
        }
      }
    }
    catch (IOException e)
    {
      throw new SAXException(e);
    }
  }
  














  public final void endElement(String namespaceURI, String localName, String name)
    throws SAXException
  {
    if (m_cdataTagOpen) {
      closeCDATA();
    }
    
    if ((null != namespaceURI) && (namespaceURI.length() > 0))
    {
      super.endElement(namespaceURI, localName, name);
      
      return;
    }
    

    try
    {
      ElemContext elemContext = m_elemContext;
      ElemDesc elemDesc = m_elementDesc;
      int elemFlags = elemDesc.getFlags();
      boolean elemEmpty = (elemFlags & 0x2) != 0;
      

      if (m_doIndent)
      {
        boolean isBlockElement = (elemFlags & 0x8) != 0;
        boolean shouldIndent = false;
        
        if (m_ispreserve)
        {
          m_ispreserve = false;
        }
        else if ((m_doIndent) && ((!m_inBlockElem) || (isBlockElement)))
        {
          m_startNewLine = true;
          shouldIndent = true;
        }
        if ((!m_startTagOpen) && (shouldIndent))
          indent(m_currentElemDepth - 1);
        m_inBlockElem = (!isBlockElement);
      }
      
      Writer writer = m_writer;
      if (!m_startTagOpen)
      {
        writer.write("</");
        writer.write(name);
        writer.write(62);


      }
      else
      {

        if (m_tracer != null) {
          super.fireStartElem(name);
        }
        

        int nAttrs = m_attributes.getLength();
        if (nAttrs > 0)
        {
          processAttributes(m_writer, nAttrs);
          
          m_attributes.clear();
        }
        if (!elemEmpty)
        {





          writer.write("></");
          writer.write(name);
          writer.write(62);
        }
        else
        {
          writer.write(62);
        }
      }
      

      if ((elemFlags & 0x200000) != 0)
        m_ispreserve = true;
      m_isprevtext = false;
      

      if (m_tracer != null) {
        super.fireEndElem(name);
      }
      
      if (elemEmpty)
      {



        m_elemContext = m_prev;
        return;
      }
      

      if (!m_startTagOpen)
      {
        if ((m_doIndent) && (!m_preserves.isEmpty()))
          m_preserves.pop();
      }
      m_elemContext = m_prev;

    }
    catch (IOException e)
    {
      throw new SAXException(e);
    }
  }
  














  protected void processAttribute(Writer writer, String name, String value, ElemDesc elemDesc)
    throws IOException
  {
    writer.write(32);
    
    if (((value.length() == 0) || (value.equalsIgnoreCase(name))) && (elemDesc != null) && (elemDesc.isAttrFlagSet(name, 4)))
    {


      writer.write(name);


    }
    else
    {

      writer.write(name);
      writer.write("=\"");
      if ((elemDesc != null) && (elemDesc.isAttrFlagSet(name, 2)))
      {
        writeAttrURI(writer, value, m_specialEscapeURLs);
      } else
        writeAttrString(writer, value, getEncoding());
      writer.write(34);
    }
  }
  




  private boolean isASCIIDigit(char c)
  {
    return (c >= '0') && (c <= '9');
  }
  









  private static String makeHHString(int i)
  {
    String s = Integer.toHexString(i).toUpperCase();
    if (s.length() == 1)
    {
      s = "0" + s;
    }
    return s;
  }
  






  private boolean isHHSign(String str)
  {
    boolean sign = true;
    try
    {
      r = (char)Integer.parseInt(str, 16);
    }
    catch (NumberFormatException e) {
      char r;
      sign = false;
    }
    return sign;
  }
  


























  public void writeAttrURI(Writer writer, String string, boolean doURLEscaping)
    throws IOException
  {
    int end = string.length();
    if (end > m_attrBuff.length)
    {
      m_attrBuff = new char[end * 2 + 1];
    }
    string.getChars(0, end, m_attrBuff, 0);
    char[] chars = m_attrBuff;
    
    int cleanStart = 0;
    int cleanLength = 0;
    

    char ch = '\000';
    for (int i = 0; i < end; i++)
    {
      ch = chars[i];
      
      if ((ch < ' ') || (ch > '~'))
      {
        if (cleanLength > 0)
        {
          writer.write(chars, cleanStart, cleanLength);
          cleanLength = 0;
        }
        if (doURLEscaping)
        {










          if (ch <= '')
          {
            writer.write(37);
            writer.write(makeHHString(ch));
          }
          else if (ch <= '߿')
          {


            int high = ch >> '\006' | 0xC0;
            int low = ch & 0x3F | 0x80;
            
            writer.write(37);
            writer.write(makeHHString(high));
            writer.write(37);
            writer.write(makeHHString(low));
          }
          else if (Encodings.isHighUTF16Surrogate(ch))
          {








            int highSurrogate = ch & 0x3FF;
            




            int wwww = (highSurrogate & 0x3C0) >> 6;
            int uuuuu = wwww + 1;
            

            int zzzz = (highSurrogate & 0x3C) >> 2;
            

            int yyyyyy = (highSurrogate & 0x3) << 4 & 0x30;
            

            ch = chars[(++i)];
            

            int lowSurrogate = ch & 0x3FF;
            

            yyyyyy |= (lowSurrogate & 0x3C0) >> 6;
            

            int xxxxxx = lowSurrogate & 0x3F;
            
            int byte1 = 0xF0 | uuuuu >> 2;
            int byte2 = 0x80 | (uuuuu & 0x3) << 4 & 0x30 | zzzz;
            
            int byte3 = 0x80 | yyyyyy;
            int byte4 = 0x80 | xxxxxx;
            
            writer.write(37);
            writer.write(makeHHString(byte1));
            writer.write(37);
            writer.write(makeHHString(byte2));
            writer.write(37);
            writer.write(makeHHString(byte3));
            writer.write(37);
            writer.write(makeHHString(byte4));
          }
          else
          {
            int high = ch >> '\f' | 0xE0;
            int middle = (ch & 0xFC0) >> '\006' | 0x80;
            
            int low = ch & 0x3F | 0x80;
            
            writer.write(37);
            writer.write(makeHHString(high));
            writer.write(37);
            writer.write(makeHHString(middle));
            writer.write(37);
            writer.write(makeHHString(low));
          }
          
        }
        else if (escapingNotNeeded(ch))
        {
          writer.write(ch);
        }
        else
        {
          writer.write("&#");
          writer.write(Integer.toString(ch));
          writer.write(59);
        }
        



        cleanStart = i + 1;



      }
      else if (ch == '"')
      {










        if (cleanLength > 0)
        {
          writer.write(chars, cleanStart, cleanLength);
          cleanLength = 0;
        }
        


        if (doURLEscaping) {
          writer.write("%22");
        } else {
          writer.write("&quot;");
        }
        

        cleanStart = i + 1;
      }
      else if (ch == '&')
      {



        if (cleanLength > 0)
        {
          writer.write(chars, cleanStart, cleanLength);
          cleanLength = 0;
        }
        writer.write("&amp;");
        cleanStart = i + 1;

      }
      else
      {

        cleanLength++;
      }
    }
    


    if (cleanLength > 1)
    {



      if (cleanStart == 0) {
        writer.write(string);
      } else {
        writer.write(chars, cleanStart, cleanLength);
      }
    } else if (cleanLength == 1)
    {


      writer.write(ch);
    }
  }
  










  public void writeAttrString(Writer writer, String string, String encoding)
    throws IOException
  {
    int end = string.length();
    if (end > m_attrBuff.length)
    {
      m_attrBuff = new char[end * 2 + 1];
    }
    string.getChars(0, end, m_attrBuff, 0);
    char[] chars = m_attrBuff;
    


    int cleanStart = 0;
    int cleanLength = 0;
    
    char ch = '\000';
    for (int i = 0; i < end; i++)
    {
      ch = chars[i];
      




      if ((escapingNotNeeded(ch)) && (!m_charInfo.shouldMapAttrChar(ch)))
      {
        cleanLength++;
      }
      else if (('<' == ch) || ('>' == ch))
      {
        cleanLength++;
      }
      else if (('&' == ch) && (i + 1 < end) && ('{' == chars[(i + 1)]))
      {

        cleanLength++;
      }
      else
      {
        if (cleanLength > 0)
        {
          writer.write(chars, cleanStart, cleanLength);
          cleanLength = 0;
        }
        int pos = accumDefaultEntity(writer, ch, i, chars, end, false, true);
        
        if (i != pos)
        {
          i = pos - 1;
        }
        else
        {
          if (Encodings.isHighUTF16Surrogate(ch))
          {

            writeUTF16Surrogate(ch, chars, i, end);
            i++;
          }
          













          String outputStringForChar = m_charInfo.getOutputStringForChar(ch);
          if (null != outputStringForChar)
          {
            writer.write(outputStringForChar);
          }
          else if (escapingNotNeeded(ch))
          {
            writer.write(ch);
          }
          else
          {
            writer.write("&#");
            writer.write(Integer.toString(ch));
            writer.write(59);
          }
        }
        cleanStart = i + 1;
      }
    }
    


    if (cleanLength > 1)
    {



      if (cleanStart == 0) {
        writer.write(string);
      } else {
        writer.write(chars, cleanStart, cleanLength);
      }
    } else if (cleanLength == 1)
    {


      writer.write(ch);
    }
  }
  






























  public final void characters(char[] chars, int start, int length)
    throws SAXException
  {
    if (m_elemContext.m_isRaw)
    {
      try
      {

        if (m_elemContext.m_startTagOpen)
        {
          closeStartTag();
          m_elemContext.m_startTagOpen = false;
        }
        
        m_ispreserve = true;
        
        writeNormalizedChars(chars, start, length, false, m_lineSepUse);
        

        if (m_tracer != null) {
          super.fireCharEvent(chars, start, length);
        }
        return;
      }
      catch (IOException ioe)
      {
        throw new SAXException(Utils.messages.createMessage("ER_OIERROR", null), ioe);
      }
    }
    


    super.characters(chars, start, length);
  }
  





























  public final void cdata(char[] ch, int start, int length)
    throws SAXException
  {
    if ((null != m_elemContext.m_elementName) && ((m_elemContext.m_elementName.equalsIgnoreCase("SCRIPT")) || (m_elemContext.m_elementName.equalsIgnoreCase("STYLE"))))
    {

      try
      {

        if (m_elemContext.m_startTagOpen)
        {
          closeStartTag();
          m_elemContext.m_startTagOpen = false;
        }
        
        m_ispreserve = true;
        
        if (shouldIndent()) {
          indent();
        }
        
        writeNormalizedChars(ch, start, length, true, m_lineSepUse);
      }
      catch (IOException ioe)
      {
        throw new SAXException(Utils.messages.createMessage("ER_OIERROR", null), ioe);


      }
      

    }
    else
    {

      super.cdata(ch, start, length);
    }
  }
  













  public void processingInstruction(String target, String data)
    throws SAXException
  {
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
        else if (m_cdataTagOpen)
        {
          closeCDATA();
        }
        else if (m_needToCallStartDocument)
        {
          startDocumentInternal();
        }
        







        if (true == m_needToOutputDocTypeDecl) {
          outputDocTypeDecl("html");
        }
        
        if (shouldIndent()) {
          indent();
        }
        Writer writer = m_writer;
        
        writer.write("<?");
        writer.write(target);
        
        if ((data.length() > 0) && (!Character.isSpaceChar(data.charAt(0)))) {
          writer.write(32);
        }
        
        writer.write(data);
        writer.write(62);
        



        if (m_elemContext.m_currentElemDepth <= 0) {
          outputLineSep();
        }
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
  







  public final void entityReference(String name)
    throws SAXException
  {
    try
    {
      Writer writer = m_writer;
      writer.write(38);
      writer.write(name);
      writer.write(59);
    }
    catch (IOException e)
    {
      throw new SAXException(e);
    }
  }
  

  public final void endElement(String elemName)
    throws SAXException
  {
    endElement(null, null, elemName);
  }
  














  public void processAttributes(Writer writer, int nAttrs)
    throws IOException, SAXException
  {
    for (int i = 0; i < nAttrs; i++)
    {
      processAttribute(writer, m_attributes.getQName(i), m_attributes.getValue(i), m_elemContext.m_elementDesc);
    }
  }
  












  protected void closeStartTag()
    throws SAXException
  {
    try
    {
      if (m_tracer != null) {
        super.fireStartElem(m_elemContext.m_elementName);
      }
      int nAttrs = m_attributes.getLength();
      if (nAttrs > 0)
      {
        processAttributes(m_writer, nAttrs);
        
        m_attributes.clear();
      }
      
      m_writer.write(62);
      




      if (m_CdataElems != null)
        m_elemContext.m_isCdataSection = isCdataSection();
      if (m_doIndent)
      {
        m_isprevtext = false;
        m_preserves.push(m_ispreserve);
      }
      
    }
    catch (IOException e)
    {
      throw new SAXException(e);
    }
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
  
  public void startDTD(String name, String publicId, String systemId)
    throws SAXException
  {
    m_inDTD = true;
    super.startDTD(name, publicId, systemId);
  }
  




  public void endDTD()
    throws SAXException
  {
    m_inDTD = false;
  }
  





















































  public void addUniqueAttribute(String name, String value, int flags)
    throws SAXException
  {
    try
    {
      Writer writer = m_writer;
      if (((flags & 0x1) > 0) && (m_htmlcharInfo.onlyQuotAmpLtGt))
      {





        writer.write(32);
        writer.write(name);
        writer.write("=\"");
        writer.write(value);
        writer.write(34);
      }
      else if (((flags & 0x2) > 0) && ((value.length() == 0) || (value.equalsIgnoreCase(name))))
      {


        writer.write(32);
        writer.write(name);
      }
      else
      {
        writer.write(32);
        writer.write(name);
        writer.write("=\"");
        if ((flags & 0x4) > 0)
        {
          writeAttrURI(writer, value, m_specialEscapeURLs);
        }
        else
        {
          writeAttrString(writer, value, getEncoding());
        }
        writer.write(34);
      }
    } catch (IOException e) {
      throw new SAXException(e);
    }
  }
  

  public void comment(char[] ch, int start, int length)
    throws SAXException
  {
    if (m_inDTD) {
      return;
    }
    



    if (m_elemContext.m_startTagOpen)
    {
      closeStartTag();
      m_elemContext.m_startTagOpen = false;
    }
    else if (m_cdataTagOpen)
    {
      closeCDATA();
    }
    else if (m_needToCallStartDocument)
    {
      startDocumentInternal();
    }
    





    if (m_needToOutputDocTypeDecl) {
      outputDocTypeDecl("html");
    }
    super.comment(ch, start, length);
  }
  
  public boolean reset()
  {
    boolean ret = super.reset();
    if (!ret)
      return false;
    resetToHTMLStream();
    return true;
  }
  


  private void resetToHTMLStream()
  {
    m_inBlockElem = false;
    m_inDTD = false;
    m_omitMetaTag = false;
    m_specialEscapeURLs = true;
  }
  

  public void attributeDecl(String eName, String aName, String type, String valueDefault, String value)
    throws SAXException
  {}
  

  public void elementDecl(String name, String model)
    throws SAXException
  {}
  

  public void internalEntityDecl(String name, String value)
    throws SAXException
  {}
  

  public void externalEntityDecl(String name, String publicId, String systemId)
    throws SAXException
  {}
  

  static class Trie
  {
    public static final int ALPHA_SIZE = 128;
    
    final Node m_Root;
    private char[] m_charBuffer = new char[0];
    


    private final boolean m_lowerCaseOnly;
    


    public Trie()
    {
      m_Root = new Node();
      m_lowerCaseOnly = false;
    }
    





    public Trie(boolean lowerCaseOnly)
    {
      m_Root = new Node();
      m_lowerCaseOnly = lowerCaseOnly;
    }
    









    public Object put(String key, Object value)
    {
      int len = key.length();
      if (len > m_charBuffer.length)
      {

        m_charBuffer = new char[len];
      }
      
      Node node = m_Root;
      
      for (int i = 0; i < len; i++)
      {
        Node nextNode = m_nextChar[Character.toLowerCase(key.charAt(i))];
        

        if (nextNode != null)
        {
          node = nextNode;
        } else {
          for (; 
              
              i < len; i++)
          {
            Node newNode = new Node();
            if (m_lowerCaseOnly)
            {

              m_nextChar[Character.toLowerCase(key.charAt(i))] = newNode;


            }
            else
            {

              m_nextChar[Character.toUpperCase(key.charAt(i))] = newNode;
              

              m_nextChar[Character.toLowerCase(key.charAt(i))] = newNode;
            }
            

            node = newNode;
          }
        }
      }
      

      Object ret = m_Value;
      
      m_Value = value;
      
      return ret;
    }
    








    public Object get(String key)
    {
      int len = key.length();
      



      if (m_charBuffer.length < len) {
        return null;
      }
      Node node = m_Root;
      switch (len)
      {




      case 0: 
        return null;
      


      case 1: 
        char ch = key.charAt(0);
        if (ch < '')
        {
          node = m_nextChar[ch];
          if (node != null)
            return m_Value;
        }
        return null;
      }
      
      





















      for (int i = 0; i < len; i++)
      {

        char ch = key.charAt(i);
        if ('' <= ch)
        {

          return null;
        }
        
        node = m_nextChar[ch];
        if (node == null) {
          return null;
        }
      }
      return m_Value;
    }
    



    private static class Node
    {
      final Node[] m_nextChar;
      

      Object m_Value;
      


      Node()
      {
        m_nextChar = new Node[''];
        m_Value = null;
      }
    }
    














    public Trie(Trie existingTrie)
    {
      m_Root = m_Root;
      m_lowerCaseOnly = m_lowerCaseOnly;
      

      int max = existingTrie.getLongestKeyLength();
      m_charBuffer = new char[max];
    }
    









    public Object get2(String key)
    {
      int len = key.length();
      



      if (m_charBuffer.length < len) {
        return null;
      }
      Node node = m_Root;
      switch (len)
      {




      case 0: 
        return null;
      


      case 1: 
        char ch = key.charAt(0);
        if (ch < '')
        {
          node = m_nextChar[ch];
          if (node != null)
            return m_Value;
        }
        return null;
      }
      
      








      key.getChars(0, len, m_charBuffer, 0);
      
      for (int i = 0; i < len; i++)
      {
        char ch = m_charBuffer[i];
        if ('' <= ch)
        {

          return null;
        }
        
        node = m_nextChar[ch];
        if (node == null) {
          return null;
        }
      }
      return m_Value;
    }
    





    public int getLongestKeyLength()
    {
      return m_charBuffer.length;
    }
  }
}
