package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.ObjectInstantiationException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBodyElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import com.gargoylesoftware.htmlunit.svg.SvgElementFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.sourceforge.htmlunit.cyberneko.HTMLConfiguration;
import net.sourceforge.htmlunit.cyberneko.HTMLElements;
import net.sourceforge.htmlunit.cyberneko.HTMLElements.Element;
import net.sourceforge.htmlunit.cyberneko.HTMLEventInfo;
import net.sourceforge.htmlunit.cyberneko.HTMLTagBalancingListener;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.xerces.parsers.AbstractSAXParser;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xni.parser.XMLParserConfiguration;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;













































public final class HTMLParser
{
  public static final String XHTML_NAMESPACE = "http://www.w3.org/1999/xhtml";
  public static final String SVG_NAMESPACE = "http://www.w3.org/2000/svg";
  public static final SvgElementFactory SVG_FACTORY = new SvgElementFactory();
  
  private static final Map<String, ElementFactory> ELEMENT_FACTORIES = new HashMap();
  
  static {
    ELEMENT_FACTORIES.put("input", InputElementFactory.instance);
    
    DefaultElementFactory defaultElementFactory = new DefaultElementFactory();
    for (String tagName : DefaultElementFactory.SUPPORTED_TAGS_) {
      ELEMENT_FACTORIES.put(tagName, defaultElementFactory);
    }
  }
  






  private HTMLParser() {}
  





  public static void parseFragment(DomNode parent, String source)
    throws SAXException, IOException
  {
    parseFragment(parent, parent, source);
  }
  








  public static void parseFragment(DomNode parent, DomNode context, String source)
    throws SAXException, IOException
  {
    HtmlPage page = (HtmlPage)parent.getPage();
    URL url = page.getUrl();
    
    HtmlUnitDOMBuilder domBuilder = new HtmlUnitDOMBuilder(parent, url, source, null);
    domBuilder.setFeature("http://cyberneko.org/html/features/balance-tags/document-fragment", true);
    
    DomNode node = context;
    List<QName> ancestors = new ArrayList();
    while ((node != null) && (node.getNodeType() != 9)) {
      ancestors.add(0, new QName(null, node.getNodeName(), null, null));
      node = node.getParentNode();
    }
    if ((ancestors.isEmpty()) || (!"html".equals(get0localpart))) {
      ancestors.add(0, new QName(null, "html", null, null));
    }
    if ((ancestors.size() == 1) || (!"body".equals(get1localpart))) {
      ancestors.add(1, new QName(null, "body", null, null));
    }
    
    domBuilder.setFeature("http://cyberneko.org/html/features/scanner/allow-selfclosing-tags", true);
    domBuilder.setProperty("http://cyberneko.org/html/properties/balance-tags/fragment-context-stack", ancestors.toArray(new QName[0]));
    
    XMLInputSource in = new XMLInputSource(null, url.toString(), null, new StringReader(source), null);
    
    page.registerParsingStart();
    page.registerSnippetParsingStart();
    try {
      domBuilder.parse(in);
    }
    finally {
      page.registerParsingEnd();
      page.registerSnippetParsingEnd();
    }
  }
  






  public static HtmlPage parseHtml(WebResponse webResponse, WebWindow webWindow)
    throws IOException
  {
    HtmlPage page = new HtmlPage(webResponse, webWindow);
    parse(webResponse, webWindow, page, false);
    return page;
  }
  






  public static XHtmlPage parseXHtml(WebResponse webResponse, WebWindow webWindow)
    throws IOException
  {
    XHtmlPage page = new XHtmlPage(webResponse, webWindow);
    parse(webResponse, webWindow, page, true);
    return page;
  }
  

  private static void parse(WebResponse webResponse, WebWindow webWindow, HtmlPage page, boolean xhtml)
    throws IOException
  {
    webWindow.setEnclosedPage(page);
    
    URL url = webResponse.getWebRequest().getUrl();
    HtmlUnitDOMBuilder domBuilder = new HtmlUnitDOMBuilder(page, url, null, null);
    
    Charset charset = webResponse.getContentCharsetOrNull();
    try
    {
      if (charset == null) {
        Charset specifiedCharset = webResponse.getWebRequest().getCharset();
        if (specifiedCharset != null) {
          charset = specifiedCharset;
        }
      }
      else {
        domBuilder.setFeature("http://cyberneko.org/html/features/scanner/ignore-specified-charset", true);
      }
      

      if (xhtml) {
        domBuilder.setFeature("http://cyberneko.org/html/features/scanner/allow-selfclosing-tags", true);
      }
    }
    catch (Exception e) {
      throw new ObjectInstantiationException("Error setting HTML parser feature", e);
    }
    try {
      e = null;Object localObject1 = null; label255: try { content = webResponse.getContentAsStream();
      }
      finally
      {
        InputStream content;
        

        String encoding;
        

        XMLInputSource in;
        

        Throwable origin;
        

        e = localThrowable1; break label255; if (e != localThrowable1) e.addSuppressed(localThrowable1);
      }
    } finally { page.registerParsingEnd(); } page.registerParsingEnd();
    

    addBodyToPageIfNecessary(page, true, body_ != null);
  }
  











  private static void addBodyToPageIfNecessary(HtmlPage page, boolean originalCall, boolean checkInsideFrameOnly)
  {
    boolean waitToLoad = page.hasFeature(BrowserVersionFeatures.PAGE_WAIT_LOAD_BEFORE_BODY);
    if (((page.getEnclosingWindow() instanceof FrameWindow)) && (originalCall) && (waitToLoad)) {
      return;
    }
    

    Element doc = page.getDocumentElement();
    boolean hasBody = false;
    for (Node child = doc.getFirstChild(); child != null; child = child.getNextSibling()) {
      if (((child instanceof HtmlBody)) || ((child instanceof HtmlFrameSet))) {
        hasBody = true;
        break;
      }
    }
    

    if ((!hasBody) && (!checkInsideFrameOnly)) {
      HtmlBody body = new HtmlBody("body", page, null, false);
      doc.appendChild(body);
    }
    


    if (waitToLoad) {
      for (FrameWindow frame : page.getFrames()) {
        Page containedPage = frame.getEnclosedPage();
        if ((containedPage != null) && (containedPage.isHtmlPage())) {
          addBodyToPageIfNecessary((HtmlPage)containedPage, false, false);
        }
      }
    }
  }
  






  static Throwable extractNestedException(Throwable e)
  {
    Throwable originalException = e;
    Throwable cause = ((XNIException)e).getException();
    while (cause != null) {
      originalException = cause;
      if ((cause instanceof XNIException)) {
        cause = ((XNIException)cause).getException();
      }
      else if ((cause instanceof InvocationTargetException)) {
        cause = cause.getCause();
      }
      else {
        cause = null;
      }
    }
    return originalException;
  }
  



  public static ElementFactory getFactory(String tagName)
  {
    ElementFactory result = (ElementFactory)ELEMENT_FACTORIES.get(tagName);
    
    if (result != null) {
      return result;
    }
    return UnknownElementFactory.instance;
  }
  








  static ElementFactory getElementFactory(SgmlPage page, String namespaceURI, String qualifiedName, boolean insideHtml)
  {
    String qualifiedNameLower = qualifiedName.toLowerCase(Locale.ROOT);
    if (("http://www.w3.org/2000/svg".equals(namespaceURI)) || (
      (SVG_FACTORY.isSupported(qualifiedNameLower)) && 
      (!ELEMENT_FACTORIES.containsKey(qualifiedNameLower)))) {
      return SVG_FACTORY;
    }
    if ((namespaceURI == null) || (namespaceURI.isEmpty()) || 
      (!qualifiedName.contains(":")) || (namespaceURI.equals("http://www.w3.org/1999/xhtml")))
    {
      String tagName = qualifiedName;
      int index = tagName.indexOf(':');
      if (index == -1) {
        tagName = tagName.toLowerCase(Locale.ROOT);
      }
      else {
        tagName = tagName.substring(index + 1);
      }
      ElementFactory factory = (ElementFactory)ELEMENT_FACTORIES.get(tagName);
      
      if (factory != null) {
        return factory;
      }
    }
    return UnknownElementFactory.instance;
  }
  
  static final class HtmlUnitDOMBuilder
    extends AbstractSAXParser implements ContentHandler, LexicalHandler, HTMLTagBalancingListener
  {
    private final HtmlPage page_;
    private Locator locator_;
    
    private static enum HeadParsed
    {
      YES,  SYNTHESIZED,  NO;
    }
    


    private final Deque<DomNode> stack_ = new ArrayDeque();
    
    private DomNode currentNode_;
    private StringBuilder characters_;
    private HeadParsed headParsed_ = HeadParsed.NO;
    private boolean parsingInnerHead_ = false;
    
    private HtmlElement body_;
    
    private boolean lastTagWasSynthesized_;
    
    private HtmlForm formWaitingForLostChildren_;
    
    private static final String FEATURE_AUGMENTATIONS = "http://cyberneko.org/html/features/augmentations";
    private static final String FEATURE_PARSE_NOSCRIPT = "http://cyberneko.org/html/features/parse-noscript-content";
    
    public void pushInputString(String html)
    {
      page_.registerParsingStart();
      page_.registerInlineSnippetParsingStart();
      try {
        WebResponse webResponse = page_.getWebResponse();
        Charset charset = webResponse.getContentCharset();
        String url = webResponse.getWebRequest().getUrl().toString();
        XMLInputSource in = new XMLInputSource(null, url, null, new StringReader(html), charset.name());
        ((HTMLConfiguration)fConfiguration).evaluateInputSource(in);
      }
      finally {
        page_.registerParsingEnd();
        page_.registerInlineSnippetParsingEnd();
      }
    }
    




    private HtmlUnitDOMBuilder(DomNode node, URL url, String htmlContent)
    {
      super();
      page_ = ((HtmlPage)node.getPage());
      
      currentNode_ = node;
      for (Node ancestor : currentNode_.getAncestors()) {
        stack_.push((DomNode)ancestor);
      }
      
      WebClient webClient = page_.getWebClient();
      HTMLParserListener listener = webClient.getHTMLParserListener();
      boolean reportErrors;
      if (listener != null) {
        boolean reportErrors = true;
        fConfiguration.setErrorHandler(new HTMLErrorHandler(listener, url, htmlContent));
      }
      else {
        reportErrors = false;
      }
      try
      {
        setFeature("http://cyberneko.org/html/features/augmentations", true);
        setProperty("http://cyberneko.org/html/properties/names/elems", "default");
        if (!webClient.getBrowserVersion().hasFeature(BrowserVersionFeatures.HTML_ATTRIBUTE_LOWER_CASE)) {
          setProperty("http://cyberneko.org/html/properties/names/attrs", "no-change");
        }
        setFeature("http://cyberneko.org/html/features/report-errors", reportErrors);
        setFeature("http://cyberneko.org/html/features/parse-noscript-content", !webClient.getOptions().isJavaScriptEnabled());
        setFeature("http://cyberneko.org/html/features/scanner/allow-selfclosing-iframe", false);
        
        setContentHandler(this);
        setLexicalHandler(this);
      }
      catch (SAXException e) {
        throw new ObjectInstantiationException("unable to create HTML parser", e);
      }
    }
    




    private static XMLParserConfiguration createConfiguration(WebClient webClient)
    {
      HTMLConfiguration configuration = new HTMLConfiguration();
      htmlElements_.setElement(new HTMLElements.Element((short)5, "AREA", 
        4, (short)51, null));
      BrowserVersion browserVersion = webClient.getBrowserVersion();
      if (browserVersion.isChrome()) {
        htmlElements_.setElement(new HTMLElements.Element((short)25, "COMMAND", 
          4, (short)51, null));
        htmlElements_.setElement(new HTMLElements.Element((short)62, "ISINDEX", 
          1, (short)16, null));
      }
      else if (browserVersion.isIE()) {
        htmlElements_.setElement(new HTMLElements.Element((short)25, "COMMAND", 
          4, (short)51, null));
        htmlElements_.setElement(new HTMLElements.Element((short)71, "MAIN", 
          1, (short)16, null));
      }
      
      return configuration;
    }
    


    public Locator getLocator()
    {
      return locator_;
    }
    

    public void setDocumentLocator(Locator locator)
    {
      locator_ = locator;
    }
    


    public void startDocument()
      throws SAXException
    {}
    

    public void startElement(QName element, XMLAttributes attributes, Augmentations augs)
      throws XNIException
    {
      lastTagWasSynthesized_ = isSynthesized(augs);
      super.startElement(element, attributes, augs);
    }
    


    public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
      throws SAXException
    {
      handleCharacters();
      
      String tagLower = localName.toLowerCase(Locale.ROOT);
      if ((page_.isParsingHtmlSnippet()) && (("html".equals(tagLower)) || ("body".equals(tagLower)))) {
        return;
      }
      
      if (namespaceURI != null) {
        namespaceURI = namespaceURI.trim();
      }
      if ("head".equals(tagLower)) {
        if ((headParsed_ == HeadParsed.YES) || (page_.isParsingHtmlSnippet())) {
          parsingInnerHead_ = true;
          return;
        }
        
        headParsed_ = (lastTagWasSynthesized_ ? HeadParsed.SYNTHESIZED : HeadParsed.YES);

      }
      else if ((headParsed_ == HeadParsed.NO) && (("body".equals(tagLower)) || ("frameset".equals(tagLower)))) {
        ElementFactory factory = HTMLParser.getElementFactory(page_, namespaceURI, "head", true);
        DomElement newElement = factory.createElement(page_, "head", null);
        currentNode_.appendChild(newElement);
        headParsed_ = HeadParsed.SYNTHESIZED;
      }
      


      HtmlBody oldBody = null;
      if (("body".equals(qName)) && ((page_.getBody() instanceof HtmlBody))) {
        oldBody = (HtmlBody)page_.getBody();
      }
      


      if ("form".equals(tagLower)) {
        formWaitingForLostChildren_ = null;
      }
      

      if ((!(page_ instanceof XHtmlPage)) && ("http://www.w3.org/1999/xhtml".equals(namespaceURI))) {
        namespaceURI = null;
      }
      
      boolean keyGenAsSelect = ("keygen".equals(tagLower)) && (page_.hasFeature(BrowserVersionFeatures.KEYGEN_AS_SELECT));
      if (keyGenAsSelect) {
        tagLower = "select";
        qName = "select";
      }
      ElementFactory factory = HTMLParser.getElementFactory(page_, namespaceURI, qName, isInsideHtml());
      if (factory == HTMLParser.SVG_FACTORY) {
        namespaceURI = "http://www.w3.org/2000/svg";
      }
      DomElement newElement = factory.createElementNS(page_, namespaceURI, qName, atts, true);
      newElement.setStartLocation(locator_.getLineNumber(), locator_.getColumnNumber());
      

      addNodeToRightParent(currentNode_, newElement);
      


      if (oldBody != null) {
        oldBody.quietlyRemoveAndMoveChildrenTo(newElement);
      }
      
      if ("body".equals(tagLower)) {
        body_ = ((HtmlElement)newElement);
      }
      else if (("meta".equals(tagLower)) && (page_.hasFeature(BrowserVersionFeatures.META_X_UA_COMPATIBLE))) {
        HtmlMeta meta = (HtmlMeta)newElement;
        if ("X-UA-Compatible".equals(meta.getHttpEquivAttribute())) {
          String content = meta.getContentAttribute();
          if (content.startsWith("IE=")) {
            String mode = content.substring(3).trim();
            int version = page_.getWebClient().getBrowserVersion().getBrowserVersionNumeric();
            if ("edge".equals(mode)) {
              ((HTMLDocument)page_.getScriptableObject()).forceDocumentMode(version);
            } else {
              try
              {
                int value = Integer.parseInt(mode);
                if (value > version) {
                  value = version;
                }
                ((HTMLDocument)page_.getScriptableObject()).forceDocumentMode(value);
              }
              catch (Exception localException) {}
            }
          }
        }
      }
      

      if (keyGenAsSelect) {
        DomElement option = factory.createElementNS(page_, namespaceURI, "option", null, true);
        option.appendChild(new DomText(page_, "High Grade"));
        newElement.appendChild(option);
        
        option = factory.createElementNS(page_, namespaceURI, "option", null, true);
        option.appendChild(new DomText(page_, "Medium Grade"));
        newElement.appendChild(option);
      }
      currentNode_ = newElement;
      stack_.push(currentNode_);
    }
    



    private void addNodeToRightParent(DomNode currentNode, DomElement newElement)
    {
      String currentNodeName = currentNode.getNodeName();
      String newNodeName = newElement.getNodeName();
      
      DomNode parent = currentNode;
      


      if (("tr".equals(newNodeName)) && (!isTableChild(currentNodeName))) {
        parent = findElementOnStack(new String[] { "tbody", "thead", "tfoot" });
      }
      else if ((isTableChild(newNodeName)) && (!"table".equals(currentNodeName))) {
        parent = findElementOnStack(new String[] { "table" });
      }
      else if ((isTableCell(newNodeName)) && (!"tr".equals(currentNodeName))) {
        parent = findElementOnStack(new String[] { "tr" });
      }
      

      if ((parent != currentNode) && ("form".equals(currentNodeName))) {
        formWaitingForLostChildren_ = ((HtmlForm)currentNode);
      }
      
      String parentNodeName = parent.getNodeName();
      
      if ((("table".equals(parentNodeName)) && (!isTableChild(newNodeName))) || 
        ((isTableChild(parentNodeName)) && (!"caption".equals(parentNodeName)) && 
        (!"colgroup".equals(parentNodeName)) && (!"tr".equals(newNodeName))) || 
        (("colgroup".equals(parentNodeName)) && (!"col".equals(newNodeName))) || (
        ("tr".equals(parentNodeName)) && (!isTableCell(newNodeName))))
      {

        if ("form".equals(newNodeName)) {
          formWaitingForLostChildren_ = ((HtmlForm)newElement);
          parent.appendChild(newElement);
        }
        else if ((newElement instanceof SubmittableElement)) {
          if (formWaitingForLostChildren_ != null) {
            formWaitingForLostChildren_.addLostChild((HtmlElement)newElement);
          }
          parent.appendChild(newElement);
        }
        else {
          parent = findElementOnStack(new String[] { "table" });
          parent.insertBefore(newElement);
        }
      }
      else if ((formWaitingForLostChildren_ != null) && ("form".equals(parentNodeName)))
      {

        if ((newElement instanceof SubmittableElement)) {
          formWaitingForLostChildren_.addLostChild((HtmlElement)newElement);
          parent.getParentNode().appendChild(newElement);
        }
        else {
          parent = findElementOnStack(new String[] { "table" });
          parent.insertBefore(newElement);
        }
      }
      else if ((formWaitingForLostChildren_ != null) && ((newElement instanceof SubmittableElement))) {
        formWaitingForLostChildren_.addLostChild((HtmlElement)newElement);
        parent.appendChild(newElement);
      }
      else {
        parent.appendChild(newElement);
      }
    }
    
    private DomNode findElementOnStack(String... searchedElementNames) {
      DomNode searchedNode = null;
      for (DomNode node : stack_) {
        if (ArrayUtils.contains(searchedElementNames, node.getNodeName())) {
          searchedNode = node;
          break;
        }
      }
      
      if (searchedNode == null) {
        searchedNode = (DomNode)stack_.peek();
      }
      
      return searchedNode;
    }
    
    private static boolean isTableChild(String nodeName) {
      return ("thead".equals(nodeName)) || ("tbody".equals(nodeName)) || 
        ("tfoot".equals(nodeName)) || ("caption".equals(nodeName)) || 
        ("colgroup".equals(nodeName));
    }
    
    private static boolean isTableCell(String nodeName) {
      return ("td".equals(nodeName)) || ("th".equals(nodeName));
    }
    


    public void endElement(QName element, Augmentations augs)
      throws XNIException
    {
      lastTagWasSynthesized_ = isSynthesized(augs);
      super.endElement(element, augs);
    }
    


    public void endElement(String namespaceURI, String localName, String qName)
      throws SAXException
    {
      handleCharacters();
      
      String tagLower = localName.toLowerCase(Locale.ROOT);
      
      if ((page_.isParsingHtmlSnippet()) && (("html".equals(tagLower)) || ("body".equals(tagLower)))) {
        return;
      }
      
      if (parsingInnerHead_) {
        if ("head".equals(tagLower)) {
          parsingInnerHead_ = false;
        }
        if ("head".equals(tagLower)) {
          return;
        }
      }
      

      if ("form".equals(tagLower)) {
        formWaitingForLostChildren_ = null;
      }
      
      DomNode previousNode = (DomNode)stack_.pop();
      previousNode.setEndLocation(locator_.getLineNumber(), locator_.getColumnNumber());
      

      if (((previousNode instanceof HtmlForm)) && (lastTagWasSynthesized_)) {
        formWaitingForLostChildren_ = ((HtmlForm)previousNode);
      }
      
      if (!stack_.isEmpty()) {
        currentNode_ = ((DomNode)stack_.peek());
      }
      
      boolean postponed = page_.isParsingInlineHtmlSnippet();
      previousNode.onAllChildrenAddedToPage(postponed);
    }
    
    public void characters(char[] ch, int start, int length)
      throws SAXException
    {
      if (characters_ == null) {
        characters_ = new StringBuilder();
      }
      characters_.append(ch, start, length);
    }
    
    public void ignorableWhitespace(char[] ch, int start, int length)
      throws SAXException
    {
      if (characters_ == null) {
        characters_ = new StringBuilder();
      }
      characters_.append(ch, start, length);
    }
    


    private void handleCharacters()
    {
      if ((characters_ != null) && (characters_.length() != 0)) {
        if ((currentNode_ instanceof HtmlHtml))
        {

          characters_.setLength(0);
        }
        else
        {
          String textValue = characters_.toString();
          DomText text = new DomText(page_, textValue);
          characters_.setLength(0);
          
          if (StringUtils.isNotBlank(textValue))
          {
            if ((currentNode_ instanceof HtmlTableRow)) {
              HtmlTableRow row = (HtmlTableRow)currentNode_;
              HtmlTable enclosingTable = row.getEnclosingTable();
              if (enclosingTable != null) {
                if ((enclosingTable.getPreviousSibling() instanceof DomText)) {
                  DomText domText = (DomText)enclosingTable.getPreviousSibling();
                  domText.setTextContent(domText + textValue);
                }
                else {
                  enclosingTable.insertBefore(text);
                }
              }
            }
            else if ((currentNode_ instanceof HtmlTable)) {
              HtmlTable enclosingTable = (HtmlTable)currentNode_;
              if ((enclosingTable.getPreviousSibling() instanceof DomText)) {
                DomText domText = (DomText)enclosingTable.getPreviousSibling();
                domText.setTextContent(domText + textValue);
              }
              else {
                enclosingTable.insertBefore(text);
              }
            }
            else if ((currentNode_ instanceof HtmlImage)) {
              currentNode_.setNextSibling(text);
            }
            else {
              currentNode_.appendChild(text);
            }
          }
          else {
            currentNode_.appendChild(text);
          }
        }
      }
    }
    
    public void endDocument()
      throws SAXException
    {
      handleCharacters();
      DomNode currentPage = page_;
      currentPage.setEndLocation(locator_.getLineNumber(), locator_.getColumnNumber());
    }
    


    public void startPrefixMapping(String prefix, String uri)
      throws SAXException
    {}
    


    public void endPrefixMapping(String prefix)
      throws SAXException
    {}
    

    public void processingInstruction(String target, String data)
      throws SAXException
    {}
    

    public void skippedEntity(String name)
      throws SAXException
    {}
    

    public void comment(char[] ch, int start, int length)
    {
      handleCharacters();
      String data = new String(ch, start, length);
      DomComment comment = new DomComment(page_, data);
      currentNode_.appendChild(comment);
    }
    



    public void endCDATA() {}
    



    public void endDTD() {}
    



    public void endEntity(String name) {}
    


    public void startCDATA() {}
    


    public void startDTD(String name, String publicId, String systemId)
    {
      DomDocumentType type = new DomDocumentType(page_, name, publicId, systemId);
      page_.setDocumentType(type);
      

      Node child = type;
      page_.appendChild(child);
    }
    




    public void startEntity(String name) {}
    



    public void ignoredEndElement(QName element, Augmentations augs)
    {
      if ("form".equals(localpart)) {
        formWaitingForLostChildren_ = null;
      }
      
      if ((parsingInnerHead_) && ("head".equalsIgnoreCase(localpart))) {
        parsingInnerHead_ = false;
      }
    }
    





    public void ignoredStartElement(QName elem, XMLAttributes attrs, Augmentations augs)
    {
      if ((body_ != null) && ("body".equalsIgnoreCase(localpart)) && (attrs != null)) {
        copyAttributes(body_, attrs);
      }
      if ((body_ != null) && ("html".equalsIgnoreCase(localpart)) && (attrs != null)) {
        copyAttributes((DomElement)body_.getParentNode(), attrs);
      }
      
      if ((headParsed_ == HeadParsed.YES) && ("head".equalsIgnoreCase(localpart))) {
        parsingInnerHead_ = true;
      }
    }
    
    private static void copyAttributes(DomElement to, XMLAttributes attrs) {
      int length = attrs.getLength();
      for (int i = 0; i < length; i++) {
        String attrName = attrs.getLocalName(i).toLowerCase(Locale.ROOT);
        if (to.getAttributes().getNamedItem(attrName) == null) {
          to.setAttribute(attrName, attrs.getValue(i));
          if ((attrName.startsWith("on")) && ((to.getScriptableObject() instanceof HTMLBodyElement))) {
            HTMLBodyElement jsBody = (HTMLBodyElement)to.getScriptableObject();
            jsBody.createEventHandlerFromAttribute(attrName, attrs.getValue(i));
          }
        }
      }
    }
    


    public void parse(XMLInputSource inputSource)
      throws XNIException, IOException
    {
      HtmlUnitDOMBuilder oldBuilder = page_.getBuilder();
      page_.setBuilder(this);
      try {
        super.parse(inputSource);
      }
      finally {
        page_.setBuilder(oldBuilder);
      }
    }
    
    private static boolean isSynthesized(Augmentations augs) {
      HTMLEventInfo info = augs == null ? null : 
        (HTMLEventInfo)augs.getItem("http://cyberneko.org/html/features/augmentations");
      return (info != null) && (info.isSynthesized());
    }
    
    private boolean isInsideHtml() {
      boolean html = true;
      for (DomNode node = currentNode_; node != null; node = node.getParentNode()) {
        if (!(node instanceof HtmlElement)) {
          html = false;
        }
      }
      return html;
    }
  }
}
