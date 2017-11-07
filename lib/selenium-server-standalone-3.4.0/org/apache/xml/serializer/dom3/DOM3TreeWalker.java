package org.apache.xml.serializer.dom3;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.serializer.utils.Messages;
import org.apache.xml.serializer.utils.Utils;
import org.apache.xml.serializer.utils.XML11Char;
import org.apache.xml.serializer.utils.XMLChar;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Entity;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.ls.LSSerializerFilter;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.LocatorImpl;






































final class DOM3TreeWalker
{
  private SerializationHandler fSerializer = null;
  



  private LocatorImpl fLocator = new LocatorImpl();
  

  private DOMErrorHandler fErrorHandler = null;
  

  private LSSerializerFilter fFilter = null;
  

  private LexicalHandler fLexicalHandler = null;
  

  private int fWhatToShowFilter;
  
  private String fNewLine = null;
  

  private Properties fDOMConfigProperties = null;
  

  private boolean fInEntityRef = false;
  

  private String fXMLVersion = null;
  

  private boolean fIsXMLVersion11 = false;
  

  private boolean fIsLevel3DOM = false;
  

  private int fFeatures = 0;
  

  boolean fNextIsRaw = false;
  

  private static final String XMLNS_URI = "http://www.w3.org/2000/xmlns/";
  

  private static final String XMLNS_PREFIX = "xmlns";
  

  private static final String XML_URI = "http://www.w3.org/XML/1998/namespace";
  

  private static final String XML_PREFIX = "xml";
  

  protected NamespaceSupport fNSBinder;
  

  protected NamespaceSupport fLocalNSBinder;
  

  private int fElementDepth = 0;
  


  private static final int CANONICAL = 1;
  


  private static final int CDATA = 2;
  


  private static final int CHARNORMALIZE = 4;
  


  private static final int COMMENTS = 8;
  


  private static final int DTNORMALIZE = 16;
  


  private static final int ELEM_CONTENT_WHITESPACE = 32;
  


  private static final int ENTITIES = 64;
  


  private static final int INFOSET = 128;
  


  private static final int NAMESPACES = 256;
  


  private static final int NAMESPACEDECLS = 512;
  

  private static final int NORMALIZECHARS = 1024;
  

  private static final int SPLITCDATA = 2048;
  

  private static final int VALIDATE = 4096;
  

  private static final int SCHEMAVALIDATE = 8192;
  

  private static final int WELLFORMED = 16384;
  

  private static final int DISCARDDEFAULT = 32768;
  

  private static final int PRETTY_PRINT = 65536;
  

  private static final int IGNORE_CHAR_DENORMALIZE = 131072;
  

  private static final int XMLDECL = 262144;
  


  DOM3TreeWalker(SerializationHandler serialHandler, DOMErrorHandler errHandler, LSSerializerFilter filter, String newLine)
  {
    fSerializer = serialHandler;
    
    fErrorHandler = errHandler;
    fFilter = filter;
    fLexicalHandler = null;
    fNewLine = newLine;
    
    fNSBinder = new NamespaceSupport();
    fLocalNSBinder = new NamespaceSupport();
    
    fDOMConfigProperties = fSerializer.getOutputFormat();
    fSerializer.setDocumentLocator(fLocator);
    initProperties(fDOMConfigProperties);
    
    try
    {
      fLocator.setSystemId(System.getProperty("user.dir") + File.separator + "dummy.xsl");
    }
    catch (SecurityException se) {}
  }
  












  public void traverse(Node pos)
    throws SAXException
  {
    fSerializer.startDocument();
    

    if (pos.getNodeType() != 9) {
      Document ownerDoc = pos.getOwnerDocument();
      if ((ownerDoc != null) && (ownerDoc.getImplementation().hasFeature("Core", "3.0")))
      {
        fIsLevel3DOM = true;
      }
    }
    else if (((Document)pos).getImplementation().hasFeature("Core", "3.0"))
    {

      fIsLevel3DOM = true;
    }
    

    if ((fSerializer instanceof LexicalHandler)) {
      fLexicalHandler = fSerializer;
    }
    
    if (fFilter != null) {
      fWhatToShowFilter = fFilter.getWhatToShow();
    }
    Node top = pos;
    
    while (null != pos) {
      startNode(pos);
      
      Node nextNode = null;
      
      nextNode = pos.getFirstChild();
      
      while (null == nextNode) {
        endNode(pos);
        
        if (!top.equals(pos))
        {

          nextNode = pos.getNextSibling();
          
          if (null == nextNode) {
            pos = pos.getParentNode();
            
            if ((null == pos) || (top.equals(pos))) {
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
    fSerializer.endDocument();
  }
  












  public void traverse(Node pos, Node top)
    throws SAXException
  {
    fSerializer.startDocument();
    

    if (pos.getNodeType() != 9) {
      Document ownerDoc = pos.getOwnerDocument();
      if ((ownerDoc != null) && (ownerDoc.getImplementation().hasFeature("Core", "3.0")))
      {
        fIsLevel3DOM = true;
      }
    }
    else if (((Document)pos).getImplementation().hasFeature("Core", "3.0"))
    {

      fIsLevel3DOM = true;
    }
    

    if ((fSerializer instanceof LexicalHandler)) {
      fLexicalHandler = fSerializer;
    }
    
    if (fFilter != null) {
      fWhatToShowFilter = fFilter.getWhatToShow();
    }
    while (null != pos) {
      startNode(pos);
      
      Node nextNode = null;
      
      nextNode = pos.getFirstChild();
      
      while (null == nextNode) {
        endNode(pos);
        
        if ((null == top) || (!top.equals(pos)))
        {

          nextNode = pos.getNextSibling();
          
          if (null == nextNode) {
            pos = pos.getParentNode();
            
            if ((null == pos) || ((null != top) && (top.equals(pos)))) {
              nextNode = null;
            }
          }
        }
      }
      

      pos = nextNode;
    }
    fSerializer.endDocument();
  }
  


  private final void dispatachChars(Node node)
    throws SAXException
  {
    if (fSerializer != null) {
      fSerializer.characters(node);
    } else {
      String data = ((Text)node).getData();
      fSerializer.characters(data.toCharArray(), 0, data.length());
    }
  }
  





  protected void startNode(Node node)
    throws SAXException
  {
    if ((node instanceof Locator)) {
      Locator loc = (Locator)node;
      fLocator.setColumnNumber(loc.getColumnNumber());
      fLocator.setLineNumber(loc.getLineNumber());
      fLocator.setPublicId(loc.getPublicId());
      fLocator.setSystemId(loc.getSystemId());
    } else {
      fLocator.setColumnNumber(0);
      fLocator.setLineNumber(0);
    }
    
    switch (node.getNodeType()) {
    case 10: 
      serializeDocType((DocumentType)node, true);
      break;
    case 8: 
      serializeComment((Comment)node);
      break;
    case 11: 
      break;
    case 9: 
      break;
    
    case 1: 
      serializeElement((Element)node, true);
      break;
    case 7: 
      serializePI((ProcessingInstruction)node);
      break;
    case 4: 
      serializeCDATASection((CDATASection)node);
      break;
    case 3: 
      serializeText((Text)node);
      break;
    case 5: 
      serializeEntityReference((EntityReference)node, true);
      break;
    }
    
  }
  







  protected void endNode(Node node)
    throws SAXException
  {
    switch (node.getNodeType()) {
    case 9: 
      break;
    case 10: 
      serializeDocType((DocumentType)node, false);
      break;
    case 1: 
      serializeElement((Element)node, false);
      break;
    case 4: 
      break;
    case 5: 
      serializeEntityReference((EntityReference)node, false);
      break;
    }
    
  }
  









  protected boolean applyFilter(Node node, int nodeType)
  {
    if ((fFilter != null) && ((fWhatToShowFilter & nodeType) != 0))
    {
      short code = fFilter.acceptNode(node);
      switch (code) {
      case 2: 
      case 3: 
        return false;
      }
      
    }
    return true;
  }
  







  protected void serializeDocType(DocumentType node, boolean bStart)
    throws SAXException
  {
    String docTypeName = node.getNodeName();
    String publicId = node.getPublicId();
    String systemId = node.getSystemId();
    String internalSubset = node.getInternalSubset();
    


    if ((internalSubset != null) && (!"".equals(internalSubset)))
    {
      if (bStart)
      {
        try
        {

          Writer writer = fSerializer.getWriter();
          StringBuffer dtd = new StringBuffer();
          
          dtd.append("<!DOCTYPE ");
          dtd.append(docTypeName);
          if (null != publicId) {
            dtd.append(" PUBLIC \"");
            dtd.append(publicId);
            dtd.append('"');
          }
          
          if (null != systemId) {
            if (null == publicId) {
              dtd.append(" SYSTEM \"");
            } else {
              dtd.append(" \"");
            }
            dtd.append(systemId);
            dtd.append('"');
          }
          
          dtd.append(" [ ");
          
          dtd.append(fNewLine);
          dtd.append(internalSubset);
          dtd.append("]>");
          dtd.append(fNewLine);
          
          writer.write(dtd.toString());
          writer.flush();
        }
        catch (IOException e) {
          throw new SAXException(Utils.messages.createMessage("ER_WRITING_INTERNAL_SUBSET", null), e);
        }
        
      }
      

    }
    else if (bStart) {
      if (fLexicalHandler != null) {
        fLexicalHandler.startDTD(docTypeName, publicId, systemId);
      }
    }
    else if (fLexicalHandler != null) {
      fLexicalHandler.endDTD();
    }
  }
  






  protected void serializeComment(Comment node)
    throws SAXException
  {
    if ((fFeatures & 0x8) != 0) {
      String data = node.getData();
      

      if ((fFeatures & 0x4000) != 0) {
        isCommentWellFormed(data);
      }
      
      if (fLexicalHandler != null)
      {

        if (!applyFilter(node, 128)) {
          return;
        }
        
        fLexicalHandler.comment(data.toCharArray(), 0, data.length());
      }
    }
  }
  





  protected void serializeElement(Element node, boolean bStart)
    throws SAXException
  {
    if (bStart) {
      fElementDepth += 1;
      






      if ((fFeatures & 0x4000) != 0) {
        isElementWellFormed(node);
      }
      


      if (!applyFilter(node, 1)) {
        return;
      }
      

      if ((fFeatures & 0x100) != 0) {
        fNSBinder.pushContext();
        fLocalNSBinder.reset();
        
        recordLocalNSDecl(node);
        fixupElementNS(node);
      }
      

      fSerializer.startElement(node.getNamespaceURI(), node.getLocalName(), node.getNodeName());
      



      serializeAttList(node);
    }
    else {
      fElementDepth -= 1;
      

      if (!applyFilter(node, 1)) {
        return;
      }
      
      fSerializer.endElement(node.getNamespaceURI(), node.getLocalName(), node.getNodeName());
      





      if ((fFeatures & 0x100) != 0) {
        fNSBinder.popContext();
      }
    }
  }
  




  protected void serializeAttList(Element node)
    throws SAXException
  {
    NamedNodeMap atts = node.getAttributes();
    int nAttrs = atts.getLength();
    
    for (int i = 0; i < nAttrs; i++) {
      Node attr = atts.item(i);
      
      String localName = attr.getLocalName();
      String attrName = attr.getNodeName();
      String attrPrefix = attr.getPrefix() == null ? "" : attr.getPrefix();
      String attrValue = attr.getNodeValue();
      

      String type = null;
      if (fIsLevel3DOM) {
        type = ((Attr)attr).getSchemaTypeInfo().getTypeName();
      }
      type = type == null ? "CDATA" : type;
      
      String attrNS = attr.getNamespaceURI();
      if ((attrNS != null) && (attrNS.length() == 0)) {
        attrNS = null;
        
        attrName = attr.getLocalName();
      }
      
      boolean isSpecified = ((Attr)attr).getSpecified();
      boolean addAttr = true;
      boolean applyFilter = false;
      boolean xmlnsAttr = (attrName.equals("xmlns")) || (attrName.startsWith("xmlns:"));
      


      if ((fFeatures & 0x4000) != 0) {
        isAttributeWellFormed(attr);
      }
      





      if (((fFeatures & 0x100) != 0) && (!xmlnsAttr))
      {

        if (attrNS != null) {
          attrPrefix = attrPrefix == null ? "" : attrPrefix;
          
          String declAttrPrefix = fNSBinder.getPrefix(attrNS);
          String declAttrNS = fNSBinder.getURI(attrPrefix);
          






          if (("".equals(attrPrefix)) || ("".equals(declAttrPrefix)) || (!attrPrefix.equals(declAttrPrefix)))
          {



            if ((declAttrPrefix != null) && (!"".equals(declAttrPrefix)))
            {

              attrPrefix = declAttrPrefix;
              
              if (declAttrPrefix.length() > 0) {
                attrName = declAttrPrefix + ":" + localName;
              } else {
                attrName = localName;
              }
              

            }
            else if ((attrPrefix != null) && (!"".equals(attrPrefix)) && (declAttrNS == null))
            {

              if ((fFeatures & 0x200) != 0) {
                fSerializer.addAttribute("http://www.w3.org/2000/xmlns/", attrPrefix, "xmlns:" + attrPrefix, "CDATA", attrNS);
                

                fNSBinder.declarePrefix(attrPrefix, attrNS);
                fLocalNSBinder.declarePrefix(attrPrefix, attrNS);
              }
              

            }
            else
            {
              int counter = 1;
              attrPrefix = "NS" + counter++;
              
              while (fLocalNSBinder.getURI(attrPrefix) != null) {
                attrPrefix = "NS" + counter++;
              }
              
              attrName = attrPrefix + ":" + localName;
              


              if ((fFeatures & 0x200) != 0)
              {
                fSerializer.addAttribute("http://www.w3.org/2000/xmlns/", attrPrefix, "xmlns:" + attrPrefix, "CDATA", attrNS);
                

                fNSBinder.declarePrefix(attrPrefix, attrNS);
                fLocalNSBinder.declarePrefix(attrPrefix, attrNS);
              }
              
            }
            
          }
          
        }
        else if (localName == null)
        {
          String msg = Utils.messages.createMessage("ER_NULL_LOCAL_ELEMENT_NAME", new Object[] { attrName });
          


          if (fErrorHandler != null) {
            fErrorHandler.handleError(new DOMErrorImpl((short)2, msg, "ER_NULL_LOCAL_ELEMENT_NAME", null, null, null));
          }
        }
      }
      















      if ((((fFeatures & 0x8000) != 0) && (isSpecified)) || ((fFeatures & 0x8000) == 0))
      {
        applyFilter = true;
      } else {
        addAttr = false;
      }
      
      if (applyFilter)
      {

        if ((fFilter != null) && ((fFilter.getWhatToShow() & 0x2) != 0))
        {


          if (!xmlnsAttr) {
            short code = fFilter.acceptNode(attr);
            switch (code) {
            case 2: 
            case 3: 
              addAttr = false;
              break;
            }
            
          }
        }
      }
      

      if ((addAttr) && (xmlnsAttr))
      {
        if ((fFeatures & 0x200) != 0)
        {
          if ((localName != null) && (!"".equals(localName))) {
            fSerializer.addAttribute(attrNS, localName, attrName, type, attrValue);
          }
        }
      } else if ((addAttr) && (!xmlnsAttr))
      {



        if (((fFeatures & 0x200) != 0) && (attrNS != null)) {
          fSerializer.addAttribute(attrNS, localName, attrName, type, attrValue);


        }
        else
        {

          fSerializer.addAttribute("", localName, attrName, type, attrValue);
        }
      }
      






      if ((xmlnsAttr) && ((fFeatures & 0x200) != 0))
      {
        int index;
        

        String prefix = (index = attrName.indexOf(":")) < 0 ? "" : attrName.substring(index + 1);
        



        if (!"".equals(prefix)) {
          fSerializer.namespaceAfterStartElement(prefix, attrValue);
        }
      }
    }
  }
  





  protected void serializePI(ProcessingInstruction node)
    throws SAXException
  {
    ProcessingInstruction pi = node;
    String name = pi.getNodeName();
    

    if ((fFeatures & 0x4000) != 0) {
      isPIWellFormed(node);
    }
    

    if (!applyFilter(node, 64)) {
      return;
    }
    

    if (name.equals("xslt-next-is-raw")) {
      fNextIsRaw = true;
    } else {
      fSerializer.processingInstruction(name, pi.getData());
    }
  }
  





  protected void serializeCDATASection(CDATASection node)
    throws SAXException
  {
    if ((fFeatures & 0x4000) != 0) {
      isCDATASectionWellFormed(node);
    }
    

    if ((fFeatures & 0x2) != 0)
    {





      String nodeValue = node.getNodeValue();
      int endIndex = nodeValue.indexOf("]]>");
      if ((fFeatures & 0x800) != 0) {
        if (endIndex >= 0)
        {
          String relatedData = nodeValue.substring(0, endIndex + 2);
          
          String msg = Utils.messages.createMessage("cdata-sections-splitted", null);
          



          if (fErrorHandler != null) {
            fErrorHandler.handleError(new DOMErrorImpl((short)1, msg, "cdata-sections-splitted", null, relatedData, null));


          }
          

        }
        


      }
      else if (endIndex >= 0)
      {
        String relatedData = nodeValue.substring(0, endIndex + 2);
        
        String msg = Utils.messages.createMessage("cdata-sections-splitted", null);
        



        if (fErrorHandler != null) {
          fErrorHandler.handleError(new DOMErrorImpl((short)2, msg, "cdata-sections-splitted"));
        }
        




        return;
      }
      


      if (!applyFilter(node, 8)) {
        return;
      }
      

      if (fLexicalHandler != null) {
        fLexicalHandler.startCDATA();
      }
      dispatachChars(node);
      if (fLexicalHandler != null) {
        fLexicalHandler.endCDATA();
      }
    } else {
      dispatachChars(node);
    }
  }
  



  protected void serializeText(Text node)
    throws SAXException
  {
    if (fNextIsRaw) {
      fNextIsRaw = false;
      fSerializer.processingInstruction("javax.xml.transform.disable-output-escaping", "");
      

      dispatachChars(node);
      fSerializer.processingInstruction("javax.xml.transform.enable-output-escaping", "");

    }
    else
    {
      boolean bDispatch = false;
      

      if ((fFeatures & 0x4000) != 0) {
        isTextWellFormed(node);
      }
      


      boolean isElementContentWhitespace = false;
      if (fIsLevel3DOM) {
        isElementContentWhitespace = node.isElementContentWhitespace();
      }
      

      if (isElementContentWhitespace)
      {
        if ((fFeatures & 0x20) != 0) {
          bDispatch = true;
        }
      } else {
        bDispatch = true;
      }
      

      if (!applyFilter(node, 4)) {
        return;
      }
      
      if (bDispatch) {
        dispatachChars(node);
      }
    }
  }
  







  protected void serializeEntityReference(EntityReference node, boolean bStart)
    throws SAXException
  {
    if (bStart) {
      EntityReference eref = node;
      
      if ((fFeatures & 0x40) != 0)
      {




        if ((fFeatures & 0x4000) != 0) {
          isEntityReferneceWellFormed(node);
        }
        


        if ((fFeatures & 0x100) != 0) {
          checkUnboundPrefixInEntRef(node);
        }
      }
      




      if (fLexicalHandler != null)
      {




        fLexicalHandler.startEntity(eref.getNodeName());
      }
    }
    else {
      EntityReference eref = node;
      
      if (fLexicalHandler != null) {
        fLexicalHandler.endEntity(eref.getNodeName());
      }
    }
  }
  











  protected boolean isXMLName(String s, boolean xml11Version)
  {
    if (s == null) {
      return false;
    }
    if (!xml11Version) {
      return XMLChar.isValidName(s);
    }
    return XML11Char.isXML11ValidName(s);
  }
  













  protected boolean isValidQName(String prefix, String local, boolean xml11Version)
  {
    if (local == null)
      return false;
    boolean validNCName = false;
    
    if (!xml11Version) {
      validNCName = ((prefix == null) || (XMLChar.isValidNCName(prefix))) && (XMLChar.isValidNCName(local));
    }
    else
    {
      validNCName = ((prefix == null) || (XML11Char.isXML11ValidNCName(prefix))) && (XML11Char.isXML11ValidNCName(local));
    }
    


    return validNCName;
  }
  





  protected boolean isWFXMLChar(String chardata, Character refInvalidChar)
  {
    if ((chardata == null) || (chardata.length() == 0)) {
      return true;
    }
    
    char[] dataarray = chardata.toCharArray();
    int datalength = dataarray.length;
    

    if (fIsXMLVersion11)
    {
      int i = 0;
      while (i < datalength) {
        if (XML11Char.isXML11Invalid(dataarray[(i++)]))
        {
          char ch = dataarray[(i - 1)];
          if ((XMLChar.isHighSurrogate(ch)) && (i < datalength)) {
            char ch2 = dataarray[(i++)];
            if ((XMLChar.isLowSurrogate(ch2)) && (XMLChar.isSupplemental(XMLChar.supplemental(ch, ch2)))) {
              break;
            }
            
          }
          else
          {
            refInvalidChar = new Character(ch);
            return false;
          }
        }
      }
    }
    else {
      int i = 0;
      while (i < datalength) {
        if (XMLChar.isInvalid(dataarray[(i++)]))
        {
          char ch = dataarray[(i - 1)];
          if ((XMLChar.isHighSurrogate(ch)) && (i < datalength)) {
            char ch2 = dataarray[(i++)];
            if ((XMLChar.isLowSurrogate(ch2)) && (XMLChar.isSupplemental(XMLChar.supplemental(ch, ch2)))) {
              break;
            }
            
          }
          else
          {
            refInvalidChar = new Character(ch);
            return false;
          }
        }
      }
    }
    return true;
  }
  







  protected Character isWFXMLChar(String chardata)
  {
    if ((chardata == null) || (chardata.length() == 0)) {
      return null;
    }
    
    char[] dataarray = chardata.toCharArray();
    int datalength = dataarray.length;
    

    if (fIsXMLVersion11)
    {
      int i = 0;
      while (i < datalength) {
        if (XML11Char.isXML11Invalid(dataarray[(i++)]))
        {
          char ch = dataarray[(i - 1)];
          if ((XMLChar.isHighSurrogate(ch)) && (i < datalength)) {
            char ch2 = dataarray[(i++)];
            if ((XMLChar.isLowSurrogate(ch2)) && (XMLChar.isSupplemental(XMLChar.supplemental(ch, ch2)))) {
              break;
            }
            
          }
          else
          {
            Character refInvalidChar = new Character(ch);
            return refInvalidChar;
          }
        }
      }
    }
    else {
      int i = 0;
      while (i < datalength) {
        if (XMLChar.isInvalid(dataarray[(i++)]))
        {
          char ch = dataarray[(i - 1)];
          if ((XMLChar.isHighSurrogate(ch)) && (i < datalength)) {
            char ch2 = dataarray[(i++)];
            if ((XMLChar.isLowSurrogate(ch2)) && (XMLChar.isSupplemental(XMLChar.supplemental(ch, ch2)))) {
              break;
            }
            
          }
          else
          {
            Character refInvalidChar = new Character(ch);
            return refInvalidChar;
          }
        }
      }
    }
    return null;
  }
  





  protected void isCommentWellFormed(String data)
  {
    if ((data == null) || (data.length() == 0)) {
      return;
    }
    
    char[] dataarray = data.toCharArray();
    int datalength = dataarray.length;
    

    if (fIsXMLVersion11)
    {
      int i = 0;
      while (i < datalength) {
        char c = dataarray[(i++)];
        if (XML11Char.isXML11Invalid(c))
        {
          if ((XMLChar.isHighSurrogate(c)) && (i < datalength)) {
            char c2 = dataarray[(i++)];
            if ((XMLChar.isLowSurrogate(c2)) && (XMLChar.isSupplemental(XMLChar.supplemental(c, c2)))) {}

          }
          else
          {

            String msg = Utils.messages.createMessage("ER_WF_INVALID_CHARACTER_IN_COMMENT", new Object[] { new Character(c) });
            



            if (fErrorHandler != null) {
              fErrorHandler.handleError(new DOMErrorImpl((short)3, msg, "wf-invalid-character", null, null, null));

            }
            

          }
          

        }
        else if ((c == '-') && (i < datalength) && (dataarray[i] == '-')) {
          String msg = Utils.messages.createMessage("ER_WF_DASH_IN_COMMENT", null);
          



          if (fErrorHandler != null) {
            fErrorHandler.handleError(new DOMErrorImpl((short)3, msg, "wf-invalid-character", null, null, null));

          }
          

        }
        
      }
      

    }
    else
    {

      int i = 0;
      while (i < datalength) {
        char c = dataarray[(i++)];
        if (XMLChar.isInvalid(c))
        {
          if ((XMLChar.isHighSurrogate(c)) && (i < datalength)) {
            char c2 = dataarray[(i++)];
            if ((XMLChar.isLowSurrogate(c2)) && (XMLChar.isSupplemental(XMLChar.supplemental(c, c2)))) {}

          }
          else
          {

            String msg = Utils.messages.createMessage("ER_WF_INVALID_CHARACTER_IN_COMMENT", new Object[] { new Character(c) });
            



            if (fErrorHandler != null) {
              fErrorHandler.handleError(new DOMErrorImpl((short)3, msg, "wf-invalid-character", null, null, null));

            }
            

          }
          

        }
        else if ((c == '-') && (i < datalength) && (dataarray[i] == '-')) {
          String msg = Utils.messages.createMessage("ER_WF_DASH_IN_COMMENT", null);
          



          if (fErrorHandler != null) {
            fErrorHandler.handleError(new DOMErrorImpl((short)3, msg, "wf-invalid-character", null, null, null));
          }
        }
      }
    }
  }
  













  protected void isElementWellFormed(Node node)
  {
    boolean isNameWF = false;
    if ((fFeatures & 0x100) != 0) {
      isNameWF = isValidQName(node.getPrefix(), node.getLocalName(), fIsXMLVersion11);

    }
    else
    {

      isNameWF = isXMLName(node.getNodeName(), fIsXMLVersion11);
    }
    
    if (!isNameWF) {
      String msg = Utils.messages.createMessage("wf-invalid-character-in-node-name", new Object[] { "Element", node.getNodeName() });
      



      if (fErrorHandler != null) {
        fErrorHandler.handleError(new DOMErrorImpl((short)3, msg, "wf-invalid-character-in-node-name", null, null, null));
      }
    }
  }
  













  protected void isAttributeWellFormed(Node node)
  {
    boolean isNameWF = false;
    if ((fFeatures & 0x100) != 0) {
      isNameWF = isValidQName(node.getPrefix(), node.getLocalName(), fIsXMLVersion11);

    }
    else
    {

      isNameWF = isXMLName(node.getNodeName(), fIsXMLVersion11);
    }
    
    if (!isNameWF) {
      String msg = Utils.messages.createMessage("wf-invalid-character-in-node-name", new Object[] { "Attr", node.getNodeName() });
      



      if (fErrorHandler != null) {
        fErrorHandler.handleError(new DOMErrorImpl((short)3, msg, "wf-invalid-character-in-node-name", null, null, null));
      }
    }
    









    String value = node.getNodeValue();
    if (value.indexOf('<') >= 0) {
      String msg = Utils.messages.createMessage("ER_WF_LT_IN_ATTVAL", new Object[] { ((Attr)node).getOwnerElement().getNodeName(), node.getNodeName() });
      





      if (fErrorHandler != null) {
        fErrorHandler.handleError(new DOMErrorImpl((short)3, msg, "ER_WF_LT_IN_ATTVAL", null, null, null));
      }
    }
    









    NodeList children = node.getChildNodes();
    for (int i = 0; i < children.getLength(); i++) {
      Node child = children.item(i);
      






      if (child != null)
      {


        switch (child.getNodeType()) {
        case 3: 
          isTextWellFormed((Text)child);
          break;
        case 5: 
          isEntityReferneceWellFormed((EntityReference)child);
        }
        
      }
    }
  }
  














  protected void isPIWellFormed(ProcessingInstruction node)
  {
    if (!isXMLName(node.getNodeName(), fIsXMLVersion11)) {
      String msg = Utils.messages.createMessage("wf-invalid-character-in-node-name", new Object[] { "ProcessingInstruction", node.getTarget() });
      



      if (fErrorHandler != null) {
        fErrorHandler.handleError(new DOMErrorImpl((short)3, msg, "wf-invalid-character-in-node-name", null, null, null));
      }
    }
    










    Character invalidChar = isWFXMLChar(node.getData());
    if (invalidChar != null) {
      String msg = Utils.messages.createMessage("ER_WF_INVALID_CHARACTER_IN_PI", new Object[] { Integer.toHexString(Character.getNumericValue(invalidChar.charValue())) });
      



      if (fErrorHandler != null) {
        fErrorHandler.handleError(new DOMErrorImpl((short)3, msg, "wf-invalid-character", null, null, null));
      }
    }
  }
  















  protected void isCDATASectionWellFormed(CDATASection node)
  {
    Character invalidChar = isWFXMLChar(node.getData());
    
    if (invalidChar != null) {
      String msg = Utils.messages.createMessage("ER_WF_INVALID_CHARACTER_IN_CDATA", new Object[] { Integer.toHexString(Character.getNumericValue(invalidChar.charValue())) });
      



      if (fErrorHandler != null) {
        fErrorHandler.handleError(new DOMErrorImpl((short)3, msg, "wf-invalid-character", null, null, null));
      }
    }
  }
  













  protected void isTextWellFormed(Text node)
  {
    Character invalidChar = isWFXMLChar(node.getData());
    if (invalidChar != null) {
      String msg = Utils.messages.createMessage("ER_WF_INVALID_CHARACTER_IN_TEXT", new Object[] { Integer.toHexString(Character.getNumericValue(invalidChar.charValue())) });
      



      if (fErrorHandler != null) {
        fErrorHandler.handleError(new DOMErrorImpl((short)3, msg, "wf-invalid-character", null, null, null));
      }
    }
  }
  
















  protected void isEntityReferneceWellFormed(EntityReference node)
  {
    if (!isXMLName(node.getNodeName(), fIsXMLVersion11)) {
      String msg = Utils.messages.createMessage("wf-invalid-character-in-node-name", new Object[] { "EntityReference", node.getNodeName() });
      



      if (fErrorHandler != null) {
        fErrorHandler.handleError(new DOMErrorImpl((short)3, msg, "wf-invalid-character-in-node-name", null, null, null));
      }
    }
    








    Node parent = node.getParentNode();
    



    DocumentType docType = node.getOwnerDocument().getDoctype();
    if (docType != null) {
      NamedNodeMap entities = docType.getEntities();
      for (int i = 0; i < entities.getLength(); i++) {
        Entity ent = (Entity)entities.item(i);
        
        String nodeName = node.getNodeName() == null ? "" : node.getNodeName();
        
        String nodeNamespaceURI = node.getNamespaceURI() == null ? "" : node.getNamespaceURI();
        


        String entName = ent.getNodeName() == null ? "" : ent.getNodeName();
        
        String entNamespaceURI = ent.getNamespaceURI() == null ? "" : ent.getNamespaceURI();
        


        if ((parent.getNodeType() == 1) && 
          (entNamespaceURI.equals(nodeNamespaceURI)) && (entName.equals(nodeName)))
        {

          if (ent.getNotationName() != null) {
            String msg = Utils.messages.createMessage("ER_WF_REF_TO_UNPARSED_ENT", new Object[] { node.getNodeName() });
            



            if (fErrorHandler != null) {
              fErrorHandler.handleError(new DOMErrorImpl((short)3, msg, "ER_WF_REF_TO_UNPARSED_ENT", null, null, null));
            }
          }
        }
        










        if ((parent.getNodeType() == 2) && 
          (entNamespaceURI.equals(nodeNamespaceURI)) && (entName.equals(nodeName)))
        {

          if ((ent.getPublicId() != null) || (ent.getSystemId() != null) || (ent.getNotationName() != null))
          {

            String msg = Utils.messages.createMessage("ER_WF_REF_TO_EXTERNAL_ENT", new Object[] { node.getNodeName() });
            



            if (fErrorHandler != null) {
              fErrorHandler.handleError(new DOMErrorImpl((short)3, msg, "ER_WF_REF_TO_EXTERNAL_ENT", null, null, null));
            }
          }
        }
      }
    }
  }
  








  protected void checkUnboundPrefixInEntRef(Node node)
  {
    Node next;
    







    for (Node child = node.getFirstChild(); child != null; child = next) {
      next = child.getNextSibling();
      
      if (child.getNodeType() == 1)
      {


        String prefix = child.getPrefix();
        if ((prefix != null) && (fNSBinder.getURI(prefix) == null))
        {
          String msg = Utils.messages.createMessage("unbound-prefix-in-entity-reference", new Object[] { node.getNodeName(), child.getNodeName(), prefix });
          






          if (fErrorHandler != null) {
            fErrorHandler.handleError(new DOMErrorImpl((short)3, msg, "unbound-prefix-in-entity-reference", null, null, null));
          }
        }
        







        NamedNodeMap attrs = child.getAttributes();
        
        for (int i = 0; i < attrs.getLength(); i++) {
          String attrPrefix = attrs.item(i).getPrefix();
          if ((attrPrefix != null) && (fNSBinder.getURI(attrPrefix) == null))
          {
            String msg = Utils.messages.createMessage("unbound-prefix-in-entity-reference", new Object[] { node.getNodeName(), child.getNodeName(), attrs.item(i) });
            






            if (fErrorHandler != null) {
              fErrorHandler.handleError(new DOMErrorImpl((short)3, msg, "unbound-prefix-in-entity-reference", null, null, null));
            }
          }
        }
      }
      







      if (child.hasChildNodes()) {
        checkUnboundPrefixInEntRef(child);
      }
    }
  }
  







  protected void recordLocalNSDecl(Node node)
  {
    NamedNodeMap atts = ((Element)node).getAttributes();
    int length = atts.getLength();
    
    for (int i = 0; i < length; i++) {
      Node attr = atts.item(i);
      
      String localName = attr.getLocalName();
      String attrPrefix = attr.getPrefix();
      String attrValue = attr.getNodeValue();
      String attrNS = attr.getNamespaceURI();
      
      localName = (localName == null) || ("xmlns".equals(localName)) ? "" : localName;
      

      attrPrefix = attrPrefix == null ? "" : attrPrefix;
      attrValue = attrValue == null ? "" : attrValue;
      attrNS = attrNS == null ? "" : attrNS;
      

      if ("http://www.w3.org/2000/xmlns/".equals(attrNS))
      {

        if ("http://www.w3.org/2000/xmlns/".equals(attrValue)) {
          String msg = Utils.messages.createMessage("ER_NS_PREFIX_CANNOT_BE_BOUND", new Object[] { attrPrefix, "http://www.w3.org/2000/xmlns/" });
          



          if (fErrorHandler != null) {
            fErrorHandler.handleError(new DOMErrorImpl((short)2, msg, "ER_NS_PREFIX_CANNOT_BE_BOUND", null, null, null));



          }
          




        }
        else if ("xmlns".equals(attrPrefix))
        {
          if (attrValue.length() != 0) {
            fNSBinder.declarePrefix(localName, attrValue);
          }
          
        }
        else
        {
          fNSBinder.declarePrefix("", attrValue);
        }
      }
    }
  }
  





  protected void fixupElementNS(Node node)
    throws SAXException
  {
    String namespaceURI = ((Element)node).getNamespaceURI();
    String prefix = ((Element)node).getPrefix();
    String localName = ((Element)node).getLocalName();
    
    if (namespaceURI != null)
    {

      prefix = prefix == null ? "" : prefix;
      String inScopeNamespaceURI = fNSBinder.getURI(prefix);
      
      if ((inScopeNamespaceURI == null) || (!inScopeNamespaceURI.equals(namespaceURI)))
      {










        if ((fFeatures & 0x200) != 0) {
          if (("".equals(prefix)) || ("".equals(namespaceURI))) {
            ((Element)node).setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", namespaceURI);
          } else {
            ((Element)node).setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + prefix, namespaceURI);
          }
        }
        fLocalNSBinder.declarePrefix(prefix, namespaceURI);
        fNSBinder.declarePrefix(prefix, namespaceURI);

      }
      

    }
    else if ((localName == null) || ("".equals(localName)))
    {
      String msg = Utils.messages.createMessage("ER_NULL_LOCAL_ELEMENT_NAME", new Object[] { node.getNodeName() });
      



      if (fErrorHandler != null) {
        fErrorHandler.handleError(new DOMErrorImpl((short)2, msg, "ER_NULL_LOCAL_ELEMENT_NAME", null, null, null));

      }
      


    }
    else
    {

      namespaceURI = fNSBinder.getURI("");
      if ((namespaceURI != null) && (namespaceURI.length() > 0)) {
        ((Element)node).setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", "");
        fLocalNSBinder.declarePrefix("", "");
        fNSBinder.declarePrefix("", "");
      }
    }
  }
  






  private static final Hashtable s_propKeys = new Hashtable();
  




  static
  {
    int i = 2;
    Integer val = new Integer(i);
    s_propKeys.put("{http://www.w3.org/TR/DOM-Level-3-LS}cdata-sections", val);
    



    int i1 = 8;
    val = new Integer(i1);
    s_propKeys.put("{http://www.w3.org/TR/DOM-Level-3-LS}comments", val);
    



    int i2 = 32;
    val = new Integer(i2);
    s_propKeys.put("{http://www.w3.org/TR/DOM-Level-3-LS}element-content-whitespace", val);
    


    int i3 = 64;
    

    val = new Integer(i3);
    s_propKeys.put("{http://www.w3.org/TR/DOM-Level-3-LS}entities", val);
    



    int i4 = 256;
    val = new Integer(i4);
    s_propKeys.put("{http://www.w3.org/TR/DOM-Level-3-LS}namespaces", val);
    



    int i5 = 512;
    val = new Integer(i5);
    s_propKeys.put("{http://www.w3.org/TR/DOM-Level-3-LS}namespace-declarations", val);
    




    int i6 = 2048;
    val = new Integer(i6);
    s_propKeys.put("{http://www.w3.org/TR/DOM-Level-3-LS}split-cdata-sections", val);
    



    int i7 = 16384;
    val = new Integer(i7);
    s_propKeys.put("{http://www.w3.org/TR/DOM-Level-3-LS}well-formed", val);
    



    int i8 = 32768;
    val = new Integer(i8);
    s_propKeys.put("{http://www.w3.org/TR/DOM-Level-3-LS}discard-default-content", val);
    





    s_propKeys.put("{http://www.w3.org/TR/DOM-Level-3-LS}format-pretty-print", "");
    


    s_propKeys.put("omit-xml-declaration", "");
    s_propKeys.put("{http://xml.apache.org/xerces-2j}xml-version", "");
    

    s_propKeys.put("encoding", "");
    s_propKeys.put("{http://xml.apache.org/xerces-2j}entities", "");
  }
  






  protected void initProperties(Properties properties)
  {
    for (Enumeration keys = properties.keys(); keys.hasMoreElements();)
    {
      String key = (String)keys.nextElement();
      









      Object iobj = s_propKeys.get(key);
      if (iobj != null) {
        if ((iobj instanceof Integer))
        {











          int BITFLAG = ((Integer)iobj).intValue();
          if (properties.getProperty(key).endsWith("yes")) {
            fFeatures |= BITFLAG;
          } else {
            fFeatures &= (BITFLAG ^ 0xFFFFFFFF);

          }
          

        }
        else if ("{http://www.w3.org/TR/DOM-Level-3-LS}format-pretty-print".equals(key))
        {


          if (properties.getProperty(key).endsWith("yes")) {
            fSerializer.setIndent(true);
            fSerializer.setIndentAmount(3);
          } else {
            fSerializer.setIndent(false);
          }
        } else if ("omit-xml-declaration".equals(key))
        {


          if (properties.getProperty(key).endsWith("yes")) {
            fSerializer.setOmitXMLDeclaration(true);
          } else {
            fSerializer.setOmitXMLDeclaration(false);
          }
        } else if ("{http://xml.apache.org/xerces-2j}xml-version".equals(key))
        {




          String version = properties.getProperty(key);
          if ("1.1".equals(version)) {
            fIsXMLVersion11 = true;
            fSerializer.setVersion(version);
          } else {
            fSerializer.setVersion("1.0");
          }
        } else if ("encoding".equals(key))
        {

          String encoding = properties.getProperty(key);
          if (encoding != null) {
            fSerializer.setEncoding(encoding);
          }
        } else if ("{http://xml.apache.org/xerces-2j}entities".equals(key))
        {

          if (properties.getProperty(key).endsWith("yes")) {
            fSerializer.setDTDEntityExpansion(false);
          }
          else {
            fSerializer.setDTDEntityExpansion(true);
          }
        }
      }
    }
    



    if (fNewLine != null) {
      fSerializer.setOutputProperty("{http://xml.apache.org/xalan}line-separator", fNewLine);
    }
  }
}
