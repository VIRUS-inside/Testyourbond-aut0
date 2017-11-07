package com.gargoylesoftware.htmlunit.xml;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomCDataSection;
import com.gargoylesoftware.htmlunit.html.DomComment;
import com.gargoylesoftware.htmlunit.html.DomDocumentType;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomProcessingInstruction;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.ElementFactory;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.svg.SvgElementFactory;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xerces.dom.DeferredDocumentImpl;
import org.apache.xerces.dom.DeferredNode;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.AttributesImpl;


































public final class XmlUtil
{
  @Deprecated
  public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
  
  private static final Log LOG = LogFactory.getLog(XmlUtil.class);
  
  private static final ErrorHandler DISCARD_MESSAGES_HANDLER = new ErrorHandler()
  {
    public void error(SAXParseException exception) {}
    









    public void fatalError(SAXParseException exception) {}
    








    public void warning(SAXParseException exception) {}
  };
  








  private XmlUtil() {}
  








  public static Document buildDocument(WebResponse webResponse)
    throws IOException, SAXException, ParserConfigurationException
  {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    
    if (webResponse == null) {
      return factory.newDocumentBuilder().newDocument();
    }
    
    factory.setNamespaceAware(true);
    InputStreamReader reader = new InputStreamReader(
      new BOMInputStream(webResponse.getContentAsStream()), 
      webResponse.getContentCharset());
    

    TrackBlankContentReader tracker = new TrackBlankContentReader(reader);
    
    InputSource source = new InputSource(tracker);
    DocumentBuilder builder = factory.newDocumentBuilder();
    builder.setErrorHandler(DISCARD_MESSAGES_HANDLER);
    builder.setEntityResolver(new EntityResolver()
    {
      public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException
      {
        return new InputSource(new StringReader(""));
      }
    });
    try {
      return builder.parse(source);
    }
    catch (SAXException e) {
      if (tracker.wasBlank()) {
        return factory.newDocumentBuilder().newDocument();
      }
      throw e;
    }
  }
  

  private static final class TrackBlankContentReader
    extends Reader
  {
    private Reader reader_;
    private boolean wasBlank_ = true;
    
    TrackBlankContentReader(Reader characterStream) {
      reader_ = characterStream;
    }
    
    public boolean wasBlank() {
      return wasBlank_;
    }
    
    public void close() throws IOException
    {
      reader_.close();
    }
    
    public int read(char[] cbuf, int off, int len) throws IOException
    {
      int result = reader_.read(cbuf, off, len);
      
      if ((wasBlank_) && (result > -1)) {
        for (int i = 0; i < result; i++) {
          char ch = cbuf[(off + i)];
          if (!Character.isWhitespace(ch)) {
            wasBlank_ = false;
            break;
          }
        }
      }
      
      return result;
    }
  }
  









  public static void appendChild(SgmlPage page, DomNode parent, Node child, boolean handleXHTMLAsHTML)
  {
    appendChild(page, parent, child, handleXHTMLAsHTML, null);
  }
  










  public static void appendChild(SgmlPage page, DomNode parent, Node child, boolean handleXHTMLAsHTML, Map<Integer, List<String>> attributesOrderMap)
  {
    DocumentType documentType = child.getOwnerDocument().getDoctype();
    if ((documentType != null) && ((page instanceof XmlPage))) {
      DomDocumentType domDoctype = new DomDocumentType(
        page, documentType.getName(), documentType.getPublicId(), documentType.getSystemId());
      ((XmlPage)page).setDocumentType(domDoctype);
    }
    DomNode childXml = createFrom(page, child, handleXHTMLAsHTML, attributesOrderMap);
    parent.appendChild(childXml);
    copy(page, child, childXml, handleXHTMLAsHTML, attributesOrderMap);
  }
  
  private static DomNode createFrom(SgmlPage page, Node source, boolean handleXHTMLAsHTML, Map<Integer, List<String>> attributesOrderMap)
  {
    if (source.getNodeType() == 3) {
      return new DomText(page, source.getNodeValue());
    }
    if (source.getNodeType() == 7) {
      return new DomProcessingInstruction(page, source.getNodeName(), source.getNodeValue());
    }
    if (source.getNodeType() == 8) {
      return new DomComment(page, source.getNodeValue());
    }
    if (source.getNodeType() == 10) {
      DocumentType documentType = (DocumentType)source;
      return new DomDocumentType(page, documentType.getName(), documentType.getPublicId(), 
        documentType.getSystemId());
    }
    String ns = source.getNamespaceURI();
    String localName = source.getLocalName();
    if ((handleXHTMLAsHTML) && ("http://www.w3.org/1999/xhtml".equals(ns))) {
      ElementFactory factory = HTMLParser.getFactory(localName);
      return factory.createElementNS(page, ns, localName, 
        namedNodeMapToSaxAttributes(source.getAttributes(), attributesOrderMap, source));
    }
    NamedNodeMap nodeAttributes = source.getAttributes();
    if ((page != null) && (page.isHtmlPage()))
      localName = localName.toUpperCase(Locale.ROOT);
    String qualifiedName;
    String qualifiedName;
    if (source.getPrefix() == null) {
      qualifiedName = localName;
    }
    else {
      qualifiedName = source.getPrefix() + ':' + localName;
    }
    
    String namespaceURI = source.getNamespaceURI();
    if ("http://www.w3.org/2000/svg".equals(namespaceURI)) {
      return HTMLParser.SVG_FACTORY.createElementNS(page, namespaceURI, qualifiedName, 
        namedNodeMapToSaxAttributes(nodeAttributes, attributesOrderMap, source));
    }
    
    Map<String, DomAttr> attributes = new LinkedHashMap();
    for (int i = 0; i < nodeAttributes.getLength(); i++) {
      int orderedIndex = getIndex(nodeAttributes, attributesOrderMap, source, i);
      Attr attribute = (Attr)nodeAttributes.item(orderedIndex);
      String attributeNamespaceURI = attribute.getNamespaceURI();
      String attributeQualifiedName;
      String attributeQualifiedName; if (attribute.getPrefix() != null) {
        attributeQualifiedName = attribute.getPrefix() + ':' + attribute.getLocalName();
      }
      else {
        attributeQualifiedName = attribute.getLocalName();
      }
      String value = attribute.getNodeValue();
      boolean specified = attribute.getSpecified();
      DomAttr xmlAttribute = 
        new DomAttr(page, attributeNamespaceURI, attributeQualifiedName, value, specified);
      attributes.put(attribute.getNodeName(), xmlAttribute);
    }
    return new DomElement(namespaceURI, qualifiedName, page, attributes);
  }
  
  private static Attributes namedNodeMapToSaxAttributes(NamedNodeMap attributesMap, Map<Integer, List<String>> attributesOrderMap, Node element)
  {
    AttributesImpl attributes = new AttributesImpl();
    int length = attributesMap.getLength();
    for (int i = 0; i < length; i++) {
      int orderedIndex = getIndex(attributesMap, attributesOrderMap, element, i);
      Node attr = attributesMap.item(orderedIndex);
      attributes.addAttribute(attr.getNamespaceURI(), attr.getLocalName(), 
        attr.getNodeName(), null, attr.getNodeValue());
    }
    
    return attributes;
  }
  
  private static int getIndex(NamedNodeMap namedNodeMap, Map<Integer, List<String>> attributesOrderMap, Node element, int requiredIndex)
  {
    if ((attributesOrderMap != null) && ((element instanceof DeferredNode))) {
      int elementIndex = ((DeferredNode)element).getNodeIndex();
      List<String> attributesOrderList = (List)attributesOrderMap.get(Integer.valueOf(elementIndex));
      if (attributesOrderList != null) {
        String attributeName = (String)attributesOrderList.get(requiredIndex);
        for (int i = 0; i < namedNodeMap.getLength(); i++) {
          if (namedNodeMap.item(i).getNodeName().equals(attributeName)) {
            return i;
          }
        }
      }
    }
    return requiredIndex;
  }
  








  private static void copy(SgmlPage page, Node source, DomNode dest, boolean handleXHTMLAsHTML, Map<Integer, List<String>> attributesOrderMap)
  {
    NodeList nodeChildren = source.getChildNodes();
    for (int i = 0; i < nodeChildren.getLength(); i++) {
      Node child = nodeChildren.item(i);
      switch (child.getNodeType()) {
      case 1: 
        DomNode childXml = createFrom(page, child, handleXHTMLAsHTML, attributesOrderMap);
        dest.appendChild(childXml);
        copy(page, child, childXml, handleXHTMLAsHTML, attributesOrderMap);
        break;
      
      case 3: 
        dest.appendChild(new DomText(page, child.getNodeValue()));
        break;
      
      case 4: 
        dest.appendChild(new DomCDataSection(page, child.getNodeValue()));
        break;
      
      case 8: 
        dest.appendChild(new DomComment(page, child.getNodeValue()));
        break;
      
      case 7: 
        dest.appendChild(new DomProcessingInstruction(page, child.getNodeName(), child.getNodeValue()));
        break;
      case 2: case 5: 
      case 6: default: 
        LOG.warn("NodeType " + child.getNodeType() + 
          " (" + child.getNodeName() + ") is not yet supported.");
      }
      
    }
  }
  





  public static String lookupNamespaceURI(DomElement element, String prefix)
  {
    String uri = DomElement.ATTRIBUTE_NOT_DEFINED;
    if (prefix.isEmpty()) {
      uri = element.getAttribute("xmlns");
    }
    else {
      uri = element.getAttribute("xmlns:" + prefix);
    }
    if (uri == DomElement.ATTRIBUTE_NOT_DEFINED) {
      DomNode parentNode = element.getParentNode();
      if ((parentNode instanceof DomElement)) {
        uri = lookupNamespaceURI((DomElement)parentNode, prefix);
      }
    }
    return uri;
  }
  





  public static String lookupPrefix(DomElement element, String namespace)
  {
    Map<String, DomAttr> attributes = element.getAttributesMap();
    for (Map.Entry<String, DomAttr> entry : attributes.entrySet()) {
      String name = (String)entry.getKey();
      DomAttr value = (DomAttr)entry.getValue();
      if ((name.startsWith("xmlns:")) && (value.getValue().equals(namespace))) {
        return name.substring(6);
      }
    }
    for (DomNode child : element.getChildren()) {
      if ((child instanceof DomElement)) {
        String prefix = lookupPrefix((DomElement)child, namespace);
        if (prefix != null) {
          return prefix;
        }
      }
    }
    return null;
  }
  






  public static Map<Integer, List<String>> getAttributesOrderMap(Document document)
  {
    Map<Integer, List<String>> map = new HashMap();
    if ((document instanceof DeferredDocumentImpl)) {
      DeferredDocumentImpl deferredDocument = (DeferredDocumentImpl)document;
      int fNodeCount = ((Integer)getPrivate(deferredDocument, "fNodeCount")).intValue();
      for (int i = 0; i < fNodeCount; i++) {
        int type = deferredDocument.getNodeType(i, false);
        if (type == 1) {
          int attrIndex = deferredDocument.getNodeExtra(i, false);
          List<String> attributes = new ArrayList();
          map.put(Integer.valueOf(i), attributes);
          while (attrIndex != -1) {
            attributes.add(deferredDocument.getNodeName(attrIndex, false));
            attrIndex = deferredDocument.getPrevSibling(attrIndex, false);
          }
        }
      }
    }
    return map;
  }
  
  private static <T> T getPrivate(Object object, String fieldName)
  {
    try {
      Field f = object.getClass().getDeclaredField(fieldName);
      f.setAccessible(true);
      return f.get(object);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
