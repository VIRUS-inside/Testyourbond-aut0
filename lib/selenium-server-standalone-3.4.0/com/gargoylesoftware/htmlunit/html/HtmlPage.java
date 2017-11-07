package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.Cache;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.History;
import com.gargoylesoftware.htmlunit.IncorrectnessListener;
import com.gargoylesoftware.htmlunit.OnbeforeunloadHandler;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.RefreshHandler;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.TopLevelWindow;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.impl.SelectableTextInput;
import com.gargoylesoftware.htmlunit.html.impl.SimpleRange;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;
import com.gargoylesoftware.htmlunit.javascript.PostponedAction;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.event.BeforeUnloadEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Script;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.ranges.Range;






























































public class HtmlPage
  extends SgmlPage
{
  private static final Log LOG = LogFactory.getLog(HtmlPage.class);
  
  private static final Comparator<DomElement> documentPositionComparator = new DocumentPositionComparator();
  
  private HTMLParser.HtmlUnitDOMBuilder builder_;
  
  private transient Charset originalCharset_;
  
  private Map<String, SortedSet<DomElement>> idMap_ = Collections.synchronizedMap(new HashMap());
  
  private Map<String, SortedSet<DomElement>> nameMap_ = Collections.synchronizedMap(new HashMap());
  
  private SortedSet<BaseFrameElement> frameElements_ = new TreeSet(documentPositionComparator);
  private int parserCount_;
  private int snippetParserCount_;
  private int inlineSnippetParserCount_;
  private Collection<HtmlAttributeChangeListener> attributeListeners_;
  private final Object lock_ = new String();
  private List<PostponedAction> afterLoadActions_ = Collections.synchronizedList(new ArrayList());
  private boolean cleaning_;
  private HtmlBase base_;
  private URL baseUrl_;
  private List<AutoCloseable> autoCloseableList_;
  private ElementFromPointHandler elementFromPointHandler_;
  private DomElement elementWithFocus_;
  private List<Range> selectionRanges_ = new ArrayList(3);
  

  private static final List<String> TABBABLE_TAGS = Arrays.asList(new String[] { "a", "area", "button", "input", "object", "select", "textarea" });
  
  private static final List<String> ACCEPTABLE_TAG_NAMES = Arrays.asList(new String[] { "a", "area", "button", "input", "label", "legend", "textarea" });
  
  static class DocumentPositionComparator implements Comparator<DomElement>, Serializable {
    DocumentPositionComparator() {}
    
    public int compare(DomElement elt1, DomElement elt2) { short relation = elt1.compareDocumentPosition(elt2);
      if (relation == 0) {
        return 0;
      }
      if (((relation & 0x8) != 0) || ((relation & 0x2) != 0)) {
        return 1;
      }
      
      return -1;
    }
  }
  






  public HtmlPage(WebResponse webResponse, WebWindow webWindow)
  {
    super(webResponse, webWindow);
  }
  



  public HtmlPage getPage()
  {
    return this;
  }
  



  public boolean hasCaseSensitiveTagNames()
  {
    return false;
  }
  






  public void initialize()
    throws IOException, FailingHttpStatusCodeException
  {
    WebWindow enclosingWindow = getEnclosingWindow();
    boolean isAboutBlank = getUrl() == WebClient.URL_ABOUT_BLANK;
    if (isAboutBlank)
    {
      if (((enclosingWindow instanceof FrameWindow)) && 
        (!((FrameWindow)enclosingWindow).getFrameElement().isContentLoaded())) {
        return;
      }
      

      if ((enclosingWindow instanceof TopLevelWindow)) {
        TopLevelWindow topWindow = (TopLevelWindow)enclosingWindow;
        WebWindow openerWindow = topWindow.getOpener();
        if ((openerWindow != null) && (openerWindow.getEnclosedPage() != null)) {
          baseUrl_ = openerWindow.getEnclosedPage().getWebResponse().getWebRequest().getUrl();
        }
      }
    }
    loadFrames();
    


    if (!isAboutBlank) {
      if (hasFeature(BrowserVersionFeatures.FOCUS_BODY_ELEMENT_AT_START)) {
        setElementWithFocus(getBody());
      }
      setReadyState("complete");
      getDocumentElement().setReadyState("complete");
    }
    
    executeEventHandlersIfNeeded("DOMContentLoaded");
    executeDeferredScriptsIfNeeded();
    setReadyStateOnDeferredScriptsIfNeeded();
    

    boolean isFrameWindow = enclosingWindow instanceof FrameWindow;
    boolean isFirstPageInFrameWindow = false;
    if (isFrameWindow) {
      isFrameWindow = ((FrameWindow)enclosingWindow).getFrameElement() instanceof HtmlFrame;
      
      History hist = enclosingWindow.getHistory();
      if ((hist.getLength() > 0) && (WebClient.URL_ABOUT_BLANK == hist.getUrl(0))) {
        isFirstPageInFrameWindow = hist.getLength() <= 2;
      }
      else {
        isFirstPageInFrameWindow = enclosingWindow.getHistory().getLength() < 2;
      }
    }
    
    if ((isFrameWindow) && (!isFirstPageInFrameWindow)) {
      executeEventHandlersIfNeeded("load");
    }
    
    for (FrameWindow frameWindow : getFrames()) {
      if ((frameWindow.getFrameElement() instanceof HtmlFrame)) {
        Page page = frameWindow.getEnclosedPage();
        if ((page != null) && (page.isHtmlPage())) {
          ((HtmlPage)page).executeEventHandlersIfNeeded("load");
        }
      }
    }
    
    if (!isFrameWindow) {
      executeEventHandlersIfNeeded("load");
    }
    try
    {
      while (!afterLoadActions_.isEmpty()) {
        PostponedAction action = (PostponedAction)afterLoadActions_.remove(0);
        action.execute();
      }
    }
    catch (IOException e) {
      throw e;
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
    executeRefreshIfNeeded();
  }
  



  void addAfterLoadAction(PostponedAction action)
  {
    afterLoadActions_.add(action);
  }
  




  public void cleanUp()
  {
    if (cleaning_) {
      return;
    }
    cleaning_ = true;
    super.cleanUp();
    executeEventHandlersIfNeeded("unload");
    deregisterFramesIfNeeded();
    cleaning_ = false;
    if (autoCloseableList_ != null) {
      for (AutoCloseable closeable : new ArrayList(autoCloseableList_)) {
        try {
          closeable.close();
        }
        catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }
  }
  



  public HtmlElement getDocumentElement()
  {
    return (HtmlElement)super.getDocumentElement();
  }
  



  public HtmlElement getBody()
  {
    HtmlElement doc = getDocumentElement();
    if (doc != null) {
      for (DomNode node : doc.getChildren()) {
        if (((node instanceof HtmlBody)) || ((node instanceof HtmlFrameSet))) {
          return (HtmlElement)node;
        }
      }
    }
    return null;
  }
  



  public HtmlElement getHead()
  {
    HtmlElement doc = getDocumentElement();
    if (doc != null) {
      for (DomNode node : doc.getChildren()) {
        if ((node instanceof HtmlHead)) {
          return (HtmlElement)node;
        }
      }
    }
    return null;
  }
  



  public Document getOwnerDocument()
  {
    return null;
  }
  




  public org.w3c.dom.Node importNode(org.w3c.dom.Node importedNode, boolean deep)
  {
    throw new UnsupportedOperationException("HtmlPage.importNode is not yet implemented.");
  }
  




  public String getInputEncoding()
  {
    throw new UnsupportedOperationException("HtmlPage.getInputEncoding is not yet implemented.");
  }
  




  public String getXmlEncoding()
  {
    throw new UnsupportedOperationException("HtmlPage.getXmlEncoding is not yet implemented.");
  }
  




  public boolean getXmlStandalone()
  {
    throw new UnsupportedOperationException("HtmlPage.getXmlStandalone is not yet implemented.");
  }
  



  public void setXmlStandalone(boolean xmlStandalone)
    throws DOMException
  {
    throw new UnsupportedOperationException("HtmlPage.setXmlStandalone is not yet implemented.");
  }
  




  public String getXmlVersion()
  {
    throw new UnsupportedOperationException("HtmlPage.getXmlVersion is not yet implemented.");
  }
  



  public void setXmlVersion(String xmlVersion)
    throws DOMException
  {
    throw new UnsupportedOperationException("HtmlPage.setXmlVersion is not yet implemented.");
  }
  




  public boolean getStrictErrorChecking()
  {
    throw new UnsupportedOperationException("HtmlPage.getStrictErrorChecking is not yet implemented.");
  }
  




  public void setStrictErrorChecking(boolean strictErrorChecking)
  {
    throw new UnsupportedOperationException("HtmlPage.setStrictErrorChecking is not yet implemented.");
  }
  




  public String getDocumentURI()
  {
    throw new UnsupportedOperationException("HtmlPage.getDocumentURI is not yet implemented.");
  }
  




  public void setDocumentURI(String documentURI)
  {
    throw new UnsupportedOperationException("HtmlPage.setDocumentURI is not yet implemented.");
  }
  



  public org.w3c.dom.Node adoptNode(org.w3c.dom.Node source)
    throws DOMException
  {
    throw new UnsupportedOperationException("HtmlPage.adoptNode is not yet implemented.");
  }
  




  public DOMConfiguration getDomConfig()
  {
    throw new UnsupportedOperationException("HtmlPage.getDomConfig is not yet implemented.");
  }
  




  public org.w3c.dom.Node renameNode(org.w3c.dom.Node newNode, String namespaceURI, String qualifiedName)
    throws DOMException
  {
    throw new UnsupportedOperationException("HtmlPage.renameNode is not yet implemented.");
  }
  



  public Charset getCharset()
  {
    if (originalCharset_ == null) {
      originalCharset_ = getWebResponse().getContentCharset();
    }
    return originalCharset_;
  }
  




  public DOMImplementation getImplementation()
  {
    throw new UnsupportedOperationException("HtmlPage.getImplementation is not yet implemented.");
  }
  




  public DomElement createElement(String tagName)
  {
    if (tagName.indexOf(':') == -1) {
      tagName = tagName.toLowerCase(Locale.ROOT);
    }
    return HTMLParser.getFactory(tagName).createElementNS(this, null, tagName, null, true);
  }
  



  public DomElement createElementNS(String namespaceURI, String qualifiedName)
  {
    return 
      HTMLParser.getElementFactory(this, namespaceURI, qualifiedName, true).createElementNS(this, namespaceURI, qualifiedName, null, true);
  }
  




  public Attr createAttributeNS(String namespaceURI, String qualifiedName)
  {
    throw new UnsupportedOperationException("HtmlPage.createAttributeNS is not yet implemented.");
  }
  




  public EntityReference createEntityReference(String id)
  {
    throw new UnsupportedOperationException("HtmlPage.createEntityReference is not yet implemented.");
  }
  




  public ProcessingInstruction createProcessingInstruction(String namespaceURI, String qualifiedName)
  {
    throw new UnsupportedOperationException("HtmlPage.createProcessingInstruction is not yet implemented.");
  }
  



  public DomElement getElementById(String elementId)
  {
    SortedSet<DomElement> elements = (SortedSet)idMap_.get(elementId);
    if (elements != null) {
      return (DomElement)elements.first();
    }
    return null;
  }
  





  public HtmlAnchor getAnchorByName(String name)
    throws ElementNotFoundException
  {
    return (HtmlAnchor)getDocumentElement().getOneHtmlElementByAttribute("a", "name", name);
  }
  





  public HtmlAnchor getAnchorByHref(String href)
    throws ElementNotFoundException
  {
    return (HtmlAnchor)getDocumentElement().getOneHtmlElementByAttribute("a", "href", href);
  }
  



  public List<HtmlAnchor> getAnchors()
  {
    return getDocumentElement().getElementsByTagNameImpl("a");
  }
  




  public HtmlAnchor getAnchorByText(String text)
    throws ElementNotFoundException
  {
    WebAssert.notNull("text", text);
    
    for (HtmlAnchor anchor : getAnchors()) {
      if (text.equals(anchor.asText())) {
        return anchor;
      }
    }
    throw new ElementNotFoundException("a", "<text>", text);
  }
  




  public HtmlForm getFormByName(String name)
    throws ElementNotFoundException
  {
    List<HtmlForm> forms = getDocumentElement().getElementsByAttribute("form", "name", name);
    if (forms.isEmpty()) {
      throw new ElementNotFoundException("form", "name", name);
    }
    return (HtmlForm)forms.get(0);
  }
  



  public List<HtmlForm> getForms()
  {
    return getDocumentElement().getElementsByTagNameImpl("form");
  }
  






  public URL getFullyQualifiedUrl(String relativeUrl)
    throws MalformedURLException
  {
    URL baseUrl = getBaseURL();
    

    if (hasFeature(BrowserVersionFeatures.URL_MISSING_SLASHES)) {
      boolean incorrectnessNotified = false;
      while ((relativeUrl.startsWith("http:")) && (!relativeUrl.startsWith("http://"))) {
        if (!incorrectnessNotified) {
          notifyIncorrectness("Incorrect URL \"" + relativeUrl + "\" has been corrected");
          incorrectnessNotified = true;
        }
        relativeUrl = "http:/" + relativeUrl.substring(5);
      }
    }
    
    return WebClient.expandUrl(baseUrl, relativeUrl);
  }
  


  public String getResolvedTarget(String elementTarget)
  {
    String resolvedTarget;
    
    String resolvedTarget;
    
    if (base_ == null) {
      resolvedTarget = elementTarget;
    } else { String resolvedTarget;
      if ((elementTarget != null) && (!elementTarget.isEmpty())) {
        resolvedTarget = elementTarget;
      }
      else
        resolvedTarget = base_.getTargetAttribute();
    }
    return resolvedTarget;
  }
  





  public List<String> getTabbableElementIds()
  {
    List<String> list = new ArrayList();
    
    for (HtmlElement element : getTabbableElements()) {
      list.add(element.getId());
    }
    
    return Collections.unmodifiableList(list);
  }
  


























  public List<HtmlElement> getTabbableElements()
  {
    List<HtmlElement> tabbableElements = new ArrayList();
    for (HtmlElement element : getHtmlElementDescendants()) {
      String tagName = element.getTagName();
      if (TABBABLE_TAGS.contains(tagName)) {
        boolean disabled = element.hasAttribute("disabled");
        if ((!disabled) && (element.getTabIndex() != HtmlElement.TAB_INDEX_OUT_OF_BOUNDS)) {
          tabbableElements.add(element);
        }
      }
    }
    Collections.sort(tabbableElements, createTabOrderComparator());
    return Collections.unmodifiableList(tabbableElements);
  }
  
  private static Comparator<HtmlElement> createTabOrderComparator() {
    new Comparator()
    {
      public int compare(HtmlElement element1, HtmlElement element2) {
        Short i1 = element1.getTabIndex();
        Short i2 = element2.getTabIndex();
        short index1;
        short index1;
        if (i1 != null) {
          index1 = i1.shortValue();
        }
        else {
          index1 = -1;
        }
        short index2;
        short index2;
        if (i2 != null) {
          index2 = i2.shortValue();
        }
        else {
          index2 = -1;
        }
        int result;
        int result;
        if ((index1 > 0) && (index2 > 0)) {
          result = index1 - index2;
        } else { int result;
          if (index1 > 0) {
            result = -1;
          } else { int result;
            if (index2 > 0) {
              result = 1;
            } else { int result;
              if (index1 == index2) {
                result = 0;
              }
              else
                result = index2 - index1;
            }
          } }
        return result;
      }
    };
  }
  











  public HtmlElement getHtmlElementByAccessKey(char accessKey)
  {
    List<HtmlElement> elements = getHtmlElementsByAccessKey(accessKey);
    if (elements.isEmpty()) {
      return null;
    }
    return (HtmlElement)elements.get(0);
  }
  
















  public List<HtmlElement> getHtmlElementsByAccessKey(char accessKey)
  {
    List<HtmlElement> elements = new ArrayList();
    
    String searchString = Character.toString(accessKey).toLowerCase(Locale.ROOT);
    for (HtmlElement element : getHtmlElementDescendants()) {
      if (ACCEPTABLE_TAG_NAMES.contains(element.getTagName())) {
        String accessKeyAttribute = element.getAttribute("accesskey");
        if (searchString.equalsIgnoreCase(accessKeyAttribute)) {
          elements.add(element);
        }
      }
    }
    
    return elements;
  }
  









  public ScriptResult executeJavaScript(String sourceCode)
  {
    return executeJavaScriptIfPossible(sourceCode, "injected script", 1);
  }
  



















  public ScriptResult executeJavaScriptIfPossible(String sourceCode, String sourceName, int startLine)
  {
    if (!getWebClient().getOptions().isJavaScriptEnabled()) {
      return new ScriptResult(null, this);
    }
    
    if (StringUtils.startsWithIgnoreCase(sourceCode, "javascript:")) {
      sourceCode = sourceCode.substring("javascript:".length()).trim();
      if (sourceCode.startsWith("return ")) {
        sourceCode = sourceCode.substring("return ".length());
      }
    }
    
    Object result = getWebClient().getJavaScriptEngine().execute(this, sourceCode, sourceName, startLine);
    return new ScriptResult(result, getWebClient().getCurrentWindow().getEnclosedPage());
  }
  
  static enum JavaScriptLoadResult
  {
    NOOP, 
    
    SUCCESS, 
    
    DOWNLOAD_ERROR, 
    
    COMPILATION_ERROR;
  }
  











  JavaScriptLoadResult loadExternalJavaScriptFile(String srcAttribute, Charset charset)
    throws FailingHttpStatusCodeException
  {
    WebClient client = getWebClient();
    if ((StringUtils.isBlank(srcAttribute)) || (!client.getOptions().isJavaScriptEnabled())) {
      return JavaScriptLoadResult.NOOP;
    }
    
    try
    {
      URL scriptURL = getFullyQualifiedUrl(srcAttribute);
      String protocol = scriptURL.getProtocol();
      if ("javascript".equals(protocol)) {
        LOG.info("Ignoring script src [" + srcAttribute + "]");
        return JavaScriptLoadResult.NOOP;
      }
      if ((!"http".equals(protocol)) && (!"https".equals(protocol)) && 
        (!"data".equals(protocol)) && (!"file".equals(protocol))) {
        client.getJavaScriptErrorListener().malformedScriptURL(this, srcAttribute, 
          new MalformedURLException("unknown protocol: '" + protocol + "'"));
        return JavaScriptLoadResult.NOOP;
      }
    }
    catch (MalformedURLException e) {
      client.getJavaScriptErrorListener().malformedScriptURL(this, srcAttribute, e);
      return JavaScriptLoadResult.NOOP;
    }
    
    try
    {
      script = loadJavaScriptFromUrl(scriptURL, charset);
    } catch (IOException e) {
      Script script;
      client.getJavaScriptErrorListener().loadScriptError(this, scriptURL, e);
      return JavaScriptLoadResult.DOWNLOAD_ERROR;
    } catch (FailingHttpStatusCodeException e) {
      URL scriptURL;
      client.getJavaScriptErrorListener().loadScriptError(this, scriptURL, e);
      throw e;
    }
    Script script;
    if (script == null) {
      return JavaScriptLoadResult.COMPILATION_ERROR;
    }
    
    client.getJavaScriptEngine().execute(this, script);
    return JavaScriptLoadResult.SUCCESS;
  }
  












  private Script loadJavaScriptFromUrl(URL url, Charset charset)
    throws IOException, FailingHttpStatusCodeException
  {
    Charset scriptEncoding = charset;
    Charset pageEncoding = getCharset();
    WebRequest referringRequest = getWebResponse().getWebRequest();
    
    WebClient client = getWebClient();
    Cache cache = client.getCache();
    
    WebRequest request = new WebRequest(url, getWebClient().getBrowserVersion().getScriptAcceptHeader());
    request.setAdditionalHeaders(new HashMap(referringRequest.getAdditionalHeaders()));
    request.setAdditionalHeader("Referer", referringRequest.getUrl().toString());
    request.setAdditionalHeader("Accept", client.getBrowserVersion().getScriptAcceptHeader());
    



    WebResponse response = client.loadWebResponse(request);
    


    Object cachedScript = cache.getCachedObject(request);
    if ((cachedScript instanceof Script)) {
      return (Script)cachedScript;
    }
    
    client.printContentIfNecessary(response);
    client.throwFailingHttpStatusCodeExceptionIfNecessary(response);
    
    int statusCode = response.getStatusCode();
    boolean successful = (statusCode >= 200) && (statusCode < 300);
    boolean noContent = statusCode == 204;
    if ((!successful) || (noContent)) {
      throw new IOException("Unable to download JavaScript from '" + url + "' (status " + statusCode + ").");
    }
    

    String contentType = response.getContentType();
    if ((!"application/javascript".equalsIgnoreCase(contentType)) && 
      (!"application/ecmascript".equalsIgnoreCase(contentType)))
    {
      if (("text/javascript".equals(contentType)) || 
        ("text/ecmascript".equals(contentType)) || 
        ("application/x-javascript".equalsIgnoreCase(contentType))) {
        getWebClient().getIncorrectnessListener().notify(
          "Obsolete content type encountered: '" + contentType + "'.", this);
      }
      else {
        getWebClient().getIncorrectnessListener().notify(
          "Expected content type of 'application/javascript' or 'application/ecmascript' for remotely loaded JavaScript element at '" + 
          url + "', " + 
          "but got '" + contentType + "'.", this);
      }
    }
    
    if (scriptEncoding == null) {
      Charset contentCharset = response.getContentCharset();
      if (!contentCharset.equals(StandardCharsets.ISO_8859_1)) {
        scriptEncoding = contentCharset;
      }
      else if (!pageEncoding.equals(StandardCharsets.ISO_8859_1)) {
        scriptEncoding = pageEncoding;
      }
      else {
        scriptEncoding = StandardCharsets.ISO_8859_1;
      }
    }
    
    String scriptCode = response.getContentAsString(scriptEncoding);
    if (scriptCode != null) {
      JavaScriptEngine javaScriptEngine = client.getJavaScriptEngine();
      Script script = javaScriptEngine.compile(this, scriptCode, url.toExternalForm(), 1);
      if ((script != null) && (cache.cacheIfPossible(request, response, script)))
      {
        return script;
      }
      
      response.cleanUp();
      return script;
    }
    
    response.cleanUp();
    return null;
  }
  




  public String getTitleText()
  {
    HtmlTitle titleElement = getTitleElement();
    if (titleElement != null) {
      return titleElement.asText();
    }
    return "";
  }
  




  public void setTitleText(String message)
  {
    HtmlTitle titleElement = getTitleElement();
    if (titleElement == null) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("No title element, creating one");
      }
      HtmlHead head = (HtmlHead)getFirstChildElement(getDocumentElement(), HtmlHead.class);
      if (head == null)
      {
        throw new IllegalStateException("Headelement was not defined for this page");
      }
      Map<String, DomAttr> emptyMap = Collections.emptyMap();
      titleElement = new HtmlTitle("title", this, emptyMap);
      if (head.getFirstChild() != null) {
        head.getFirstChild().insertBefore(titleElement);
      }
      else {
        head.appendChild(titleElement);
      }
    }
    
    titleElement.setNodeValue(message);
  }
  





  private static DomElement getFirstChildElement(DomElement startElement, Class<?> clazz)
  {
    if (startElement == null) {
      return null;
    }
    for (DomElement element : startElement.getChildElements()) {
      if (clazz.isInstance(element)) {
        return element;
      }
    }
    
    return null;
  }
  





  private DomElement getFirstChildElementRecursive(DomElement startElement, Class<?> clazz)
  {
    if (startElement == null) {
      return null;
    }
    for (DomElement element : startElement.getChildElements()) {
      if (clazz.isInstance(element)) {
        return element;
      }
      DomElement childFound = getFirstChildElementRecursive(element, clazz);
      if (childFound != null) {
        return childFound;
      }
    }
    
    return null;
  }
  




  private HtmlTitle getTitleElement()
  {
    return (HtmlTitle)getFirstChildElementRecursive(getDocumentElement(), HtmlTitle.class);
  }
  





  private boolean executeEventHandlersIfNeeded(String eventType)
  {
    if (!getWebClient().getOptions().isJavaScriptEnabled()) {
      return true;
    }
    

    WebWindow window = getEnclosingWindow();
    if (window.getScriptableObject() != null) {
      HtmlElement element = getDocumentElement();
      if (element == null)
        return true;
      Event event;
      Event event;
      if (eventType.equals("beforeunload")) {
        event = new BeforeUnloadEvent(element, eventType);
      }
      else {
        event = new Event(element, eventType);
      }
      ScriptResult result = element.fireEvent(event);
      if (!isOnbeforeunloadAccepted(this, event, result)) {
        return false;
      }
    }
    

    if ((window instanceof FrameWindow)) {
      FrameWindow fw = (FrameWindow)window;
      BaseFrameElement frame = fw.getFrameElement();
      

      if (("load".equals(eventType)) && ((frame.getParentNode() instanceof DomDocumentFragment))) {
        return true;
      }
      
      if (frame.hasEventHandlers("on" + eventType)) {
        if (LOG.isDebugEnabled())
          LOG.debug("Executing on" + eventType + " handler for " + frame);
        Event event;
        Event event;
        if (eventType.equals("beforeunload")) {
          event = new BeforeUnloadEvent(frame, eventType);
        }
        else {
          event = new Event(frame, eventType);
        }
        ScriptResult result = ((com.gargoylesoftware.htmlunit.javascript.host.dom.Node)frame.getScriptableObject()).executeEventLocally(event);
        if (!isOnbeforeunloadAccepted((HtmlPage)frame.getPage(), event, result)) {
          return false;
        }
      }
    }
    
    return true;
  }
  




  public boolean isOnbeforeunloadAccepted()
  {
    return executeEventHandlersIfNeeded("beforeunload");
  }
  
  private boolean isOnbeforeunloadAccepted(HtmlPage page, Event event, ScriptResult result) {
    if (event.getType().equals("beforeunload")) {
      boolean ie = hasFeature(BrowserVersionFeatures.JS_CALL_RESULT_IS_LAST_RETURN_VALUE);
      String message = getBeforeUnloadMessage(event, result, ie);
      if (message != null) {
        OnbeforeunloadHandler handler = getWebClient().getOnbeforeunloadHandler();
        if (handler == null) {
          LOG.warn("document.onbeforeunload() returned a string in event.returnValue, but no onbeforeunload handler installed.");
        }
        else
        {
          return handler.handleEvent(page, message);
        }
      }
    }
    return true;
  }
  
  private static String getBeforeUnloadMessage(Event event, ScriptResult result, boolean ie) {
    String message = null;
    if (event.getReturnValue() != Undefined.instance) {
      if ((!ie) || (event.getReturnValue() != null) || (result == null) || (result.getJavaScriptResult() == null) || 
        (result.getJavaScriptResult() == Undefined.instance)) {
        message = Context.toString(event.getReturnValue());
      }
      
    }
    else if (result != null) {
      if (ie) {
        if (result.getJavaScriptResult() != Undefined.instance) {
          message = Context.toString(result.getJavaScriptResult());
        }
      }
      else if ((result.getJavaScriptResult() != null) && 
        (result.getJavaScriptResult() != Undefined.instance)) {
        message = Context.toString(result.getJavaScriptResult());
      }
    }
    
    return message;
  }
  






  private void executeRefreshIfNeeded()
    throws IOException
  {
    WebWindow window = getEnclosingWindow();
    if (window == null) {
      return;
    }
    
    String refreshString = getRefreshStringOrNull();
    if ((refreshString == null) || (refreshString.isEmpty())) {
      return;
    }
    



    int index = StringUtils.indexOfAnyBut(refreshString, "0123456789");
    boolean timeOnly = index == -1;
    URL url;
    double time; if (timeOnly)
    {
      try {
        time = Double.parseDouble(refreshString);
      } catch (NumberFormatException e) {
        double time;
        LOG.error("Malformed refresh string (no ';' but not a number): " + refreshString, e); return;
      }
      double time;
      url = getUrl();
    }
    else
    {
      try {
        time = Double.parseDouble(refreshString.substring(0, index).trim());
      } catch (NumberFormatException e) {
        double time;
        LOG.error("Malformed refresh string (no valid number before ';') " + refreshString, e);
        return;
      }
      index = refreshString.toLowerCase(Locale.ROOT).indexOf("url=", index);
      if (index == -1) {
        LOG.error("Malformed refresh string (found ';' but no 'url='): " + refreshString);
        return;
      }
      StringBuilder builder = new StringBuilder(refreshString.substring(index + 4));
      URL url; if (StringUtils.isBlank(builder.toString()))
      {
        url = getUrl();
      }
      else {
        if ((builder.charAt(0) == '"') || (builder.charAt(0) == '\'')) {
          builder.deleteCharAt(0);
        }
        if ((builder.charAt(builder.length() - 1) == '"') || (builder.charAt(builder.length() - 1) == '\'')) {
          builder.deleteCharAt(builder.length() - 1);
        }
        String urlString = builder.toString();
        try {
          url = getFullyQualifiedUrl(urlString);
        } catch (MalformedURLException e) {
          URL url;
          LOG.error("Malformed URL in refresh string: " + refreshString, e);
          throw e;
        }
      }
    }
    URL url;
    int timeRounded = (int)time;
    checkRecursion();
    getWebClient().getRefreshHandler().handleRefresh(this, url, timeRounded);
  }
  
  private void checkRecursion() {
    StackTraceElement[] elements = new Exception().getStackTrace();
    if (elements.length > 500) {
      for (int i = 0; i < 500; i++) {
        if (!elements[i].getClassName().startsWith("com.gargoylesoftware.htmlunit.")) {
          return;
        }
      }
      WebResponse webResponse = getWebResponse();
      throw new FailingHttpStatusCodeException("Too much redirect for " + 
        webResponse.getWebRequest().getUrl(), webResponse);
    }
  }
  




  private String getRefreshStringOrNull()
  {
    List<HtmlMeta> metaTags = getMetaTags("refresh");
    if (!metaTags.isEmpty()) {
      return ((HtmlMeta)metaTags.get(0)).getContentAttribute().trim();
    }
    return getWebResponse().getResponseHeaderValue("Refresh");
  }
  


  private void executeDeferredScriptsIfNeeded()
  {
    if (!getWebClient().getOptions().isJavaScriptEnabled()) {
      return;
    }
    if (hasFeature(BrowserVersionFeatures.JS_DEFERRED)) {
      HtmlElement doc = getDocumentElement();
      List<HtmlElement> elements = doc.getElementsByTagName("script");
      for (HtmlElement e : elements) {
        if ((e instanceof HtmlScript)) {
          HtmlScript script = (HtmlScript)e;
          if (script.isDeferred()) {
            script.executeScriptIfNeeded();
          }
        }
      }
    }
  }
  


  private void setReadyStateOnDeferredScriptsIfNeeded()
  {
    if ((getWebClient().getOptions().isJavaScriptEnabled()) && (hasFeature(BrowserVersionFeatures.JS_DEFERRED))) {
      List<HtmlElement> elements = getDocumentElement().getElementsByTagName("script");
      for (HtmlElement e : elements) {
        if ((e instanceof HtmlScript)) {
          HtmlScript script = (HtmlScript)e;
          if (script.isDeferred()) {
            script.setAndExecuteReadyState("complete");
          }
        }
      }
    }
  }
  


  public void deregisterFramesIfNeeded()
  {
    for (WebWindow window : getFrames()) {
      getWebClient().deregisterWebWindow(window);
      Page page = window.getEnclosedPage();
      if ((page != null) && (page.isHtmlPage()))
      {

        ((HtmlPage)page).deregisterFramesIfNeeded();
      }
    }
  }
  



  public List<FrameWindow> getFrames()
  {
    List<FrameWindow> list = new ArrayList(frameElements_.size());
    for (BaseFrameElement frameElement : frameElements_) {
      list.add(frameElement.getEnclosedWindow());
    }
    return list;
  }
  




  public FrameWindow getFrameByName(String name)
    throws ElementNotFoundException
  {
    for (FrameWindow frame : getFrames()) {
      if (frame.getName().equals(name)) {
        return frame;
      }
    }
    
    throw new ElementNotFoundException("frame or iframe", "name", name);
  }
  








  public DomElement pressAccessKey(char accessKey)
    throws IOException
  {
    HtmlElement element = getHtmlElementByAccessKey(accessKey);
    if (element != null) {
      element.focus();
      Page newPage;
      Page newPage; if (((element instanceof HtmlAnchor)) || ((element instanceof HtmlArea)) || ((element instanceof HtmlButton)) || 
        ((element instanceof HtmlInput)) || ((element instanceof HtmlLabel)) || ((element instanceof HtmlLegend)) || 
        ((element instanceof HtmlTextArea)) || ((element instanceof HtmlArea))) {
        newPage = element.click();
      }
      else {
        newPage = this;
      }
      
      if ((newPage != this) && (getFocusedElement() == element))
      {
        getFocusedElement().blur();
      }
    }
    
    return getFocusedElement();
  }
  





  public HtmlElement tabToNextElement()
  {
    List<HtmlElement> elements = getTabbableElements();
    if (elements.isEmpty()) {
      setFocusedElement(null);
      return null;
    }
    

    DomElement elementWithFocus = getFocusedElement();
    HtmlElement elementToGiveFocus; HtmlElement elementToGiveFocus; if (elementWithFocus == null) {
      elementToGiveFocus = (HtmlElement)elements.get(0);
    }
    else {
      int index = elements.indexOf(elementWithFocus);
      HtmlElement elementToGiveFocus; if (index == -1)
      {
        elementToGiveFocus = (HtmlElement)elements.get(0);
      } else {
        HtmlElement elementToGiveFocus;
        if (index == elements.size() - 1) {
          elementToGiveFocus = (HtmlElement)elements.get(0);
        }
        else {
          elementToGiveFocus = (HtmlElement)elements.get(index + 1);
        }
      }
    }
    
    setFocusedElement(elementToGiveFocus);
    return elementToGiveFocus;
  }
  





  public HtmlElement tabToPreviousElement()
  {
    List<HtmlElement> elements = getTabbableElements();
    if (elements.isEmpty()) {
      setFocusedElement(null);
      return null;
    }
    

    DomElement elementWithFocus = getFocusedElement();
    HtmlElement elementToGiveFocus; HtmlElement elementToGiveFocus; if (elementWithFocus == null) {
      elementToGiveFocus = (HtmlElement)elements.get(elements.size() - 1);
    }
    else {
      int index = elements.indexOf(elementWithFocus);
      HtmlElement elementToGiveFocus; if (index == -1)
      {
        elementToGiveFocus = (HtmlElement)elements.get(elements.size() - 1);
      } else {
        HtmlElement elementToGiveFocus;
        if (index == 0) {
          elementToGiveFocus = (HtmlElement)elements.get(elements.size() - 1);
        }
        else {
          elementToGiveFocus = (HtmlElement)elements.get(index - 1);
        }
      }
    }
    
    setFocusedElement(elementToGiveFocus);
    return elementToGiveFocus;
  }
  









  public <E extends HtmlElement> E getHtmlElementById(String elementId)
    throws ElementNotFoundException
  {
    DomElement element = getElementById(elementId);
    if (element == null) {
      throw new ElementNotFoundException("*", "id", elementId);
    }
    return (HtmlElement)element;
  }
  








  public <E extends DomElement> E getElementByName(String name)
    throws ElementNotFoundException
  {
    SortedSet<DomElement> elements = (SortedSet)nameMap_.get(name);
    if (elements != null) {
      return (DomElement)elements.first();
    }
    throw new ElementNotFoundException("*", "name", name);
  }
  







  public List<DomElement> getElementsByName(String name)
  {
    SortedSet<DomElement> elements = (SortedSet)nameMap_.get(name);
    if (elements != null) {
      return new ArrayList(elements);
    }
    return Collections.emptyList();
  }
  






  public List<DomElement> getElementsByIdAndOrName(String idAndOrName)
  {
    Collection<DomElement> list1 = (Collection)idMap_.get(idAndOrName);
    Collection<DomElement> list2 = (Collection)nameMap_.get(idAndOrName);
    List<DomElement> list = new ArrayList();
    if (list1 != null) {
      list.addAll(list1);
    }
    if (list2 != null) {
      for (DomElement elt : list2) {
        if (!list.contains(elt)) {
          list.add(elt);
        }
      }
    }
    return list;
  }
  




  void notifyNodeAdded(DomNode node)
  {
    if ((node instanceof DomElement)) {
      addMappedElement((DomElement)node, true);
      
      if ((node instanceof BaseFrameElement)) {
        frameElements_.add((BaseFrameElement)node);
      }
      for (HtmlElement child : node.getHtmlElementDescendants()) {
        if ((child instanceof BaseFrameElement)) {
          frameElements_.add((BaseFrameElement)child);
        }
      }
      
      if ("base".equals(node.getNodeName())) {
        calculateBase();
      }
    }
    node.onAddedToPage();
  }
  




  void notifyNodeRemoved(DomNode node)
  {
    if ((node instanceof HtmlElement)) {
      removeMappedElement((HtmlElement)node, true, true);
      
      if ((node instanceof BaseFrameElement)) {
        frameElements_.remove(node);
      }
      for (HtmlElement child : node.getHtmlElementDescendants()) {
        if ((child instanceof BaseFrameElement)) {
          frameElements_.remove(child);
        }
      }
      
      if ("base".equals(node.getNodeName())) {
        calculateBase();
      }
    }
  }
  



  void addMappedElement(DomElement element)
  {
    addMappedElement(element, false);
  }
  




  void addMappedElement(DomElement element, boolean recurse)
  {
    if (isAncestorOf(element)) {
      addElement(idMap_, element, "id", recurse);
      addElement(nameMap_, element, "name", recurse);
    }
  }
  
  private void addElement(Map<String, SortedSet<DomElement>> map, DomElement element, String attribute, boolean recurse)
  {
    String value = getAttributeValue(element, attribute);
    
    if (DomElement.ATTRIBUTE_NOT_DEFINED != value) {
      SortedSet<DomElement> elements = (SortedSet)map.get(value);
      if (elements == null) {
        elements = new TreeSet(documentPositionComparator);
        elements.add(element);
        map.put(value, elements);
      }
      else if (!elements.contains(element)) {
        elements.add(element);
      }
    }
    if (recurse) {
      for (DomElement child : element.getChildElements()) {
        addElement(map, child, attribute, true);
      }
    }
  }
  
  private static String getAttributeValue(DomElement element, String attribute)
  {
    String value = element.getAttribute(attribute);
    
    if ((DomElement.ATTRIBUTE_NOT_DEFINED == value) && 
      (!(element instanceof HtmlApplet)) && 
      (!(element instanceof HtmlObject)))
    {

      ScriptableObject scriptObject = element.getScriptableObject();
      

      if (scriptObject.has(attribute, scriptObject)) {
        Object jsValue = scriptObject.get(attribute, scriptObject);
        if ((jsValue != null) && (jsValue != Scriptable.NOT_FOUND) && ((jsValue instanceof String))) {
          value = (String)jsValue;
        }
      }
    }
    return value;
  }
  



  void removeMappedElement(HtmlElement element)
  {
    removeMappedElement(element, false, false);
  }
  





  void removeMappedElement(DomElement element, boolean recurse, boolean descendant)
  {
    if ((descendant) || (isAncestorOf(element))) {
      removeElement(idMap_, element, "id", recurse);
      removeElement(nameMap_, element, "name", recurse);
    }
  }
  
  private void removeElement(Map<String, SortedSet<DomElement>> map, DomElement element, String attribute, boolean recurse)
  {
    String value = getAttributeValue(element, attribute);
    
    if (DomElement.ATTRIBUTE_NOT_DEFINED != value) {
      SortedSet<DomElement> elements = (SortedSet)map.remove(value);
      if ((elements != null) && ((elements.size() != 1) || (!elements.contains(element)))) {
        elements.remove(element);
        map.put(value, elements);
      }
    }
    if (recurse) {
      for (DomElement child : element.getChildElements()) {
        removeElement(map, child, attribute, true);
      }
    }
  }
  





  static boolean isMappedElement(Document document, String attributeName)
  {
    return ((document instanceof HtmlPage)) && (
      ("name".equals(attributeName)) || ("id".equals(attributeName)));
  }
  
  private void calculateBase() {
    List<HtmlElement> baseElements = getDocumentElement().getElementsByTagName("base");
    switch (baseElements.size()) {
    case 0: 
      base_ = null;
      break;
    
    case 1: 
      base_ = ((HtmlBase)baseElements.get(0));
      break;
    
    default: 
      base_ = ((HtmlBase)baseElements.get(0));
      notifyIncorrectness("Multiple 'base' detected, only the first is used.");
    }
    
  }
  



  void loadFrames()
    throws FailingHttpStatusCodeException
  {
    for (FrameWindow w : getFrames()) {
      BaseFrameElement frame = w.getFrameElement();
      


      if ((frame.getEnclosedWindow() != null) && 
        (WebClient.URL_ABOUT_BLANK == frame.getEnclosedPage().getUrl()) && 
        (!frame.isContentLoaded())) {
        frame.loadInnerPage();
      }
    }
  }
  




  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    builder.append("HtmlPage(");
    builder.append(getUrl());
    builder.append(")@");
    builder.append(hashCode());
    return builder.toString();
  }
  




  protected List<HtmlMeta> getMetaTags(String httpEquiv)
  {
    if (getDocumentElement() == null) {
      return Collections.emptyList();
    }
    String nameLC = httpEquiv.toLowerCase(Locale.ROOT);
    List<HtmlMeta> tags = getDocumentElement().getElementsByTagNameImpl("meta");
    List<HtmlMeta> foundTags = new ArrayList();
    for (HtmlMeta htmlMeta : tags) {
      if (nameLC.equals(htmlMeta.getHttpEquivAttribute().toLowerCase(Locale.ROOT))) {
        foundTags.add(htmlMeta);
      }
    }
    return foundTags;
  }
  





  protected HtmlPage clone()
  {
    HtmlPage result = (HtmlPage)super.clone();
    elementWithFocus_ = null;
    
    idMap_ = Collections.synchronizedMap(new HashMap());
    nameMap_ = Collections.synchronizedMap(new HashMap());
    
    return result;
  }
  




  public HtmlPage cloneNode(boolean deep)
  {
    HtmlPage result = (HtmlPage)super.cloneNode(false);
    SimpleScriptable jsObjClone = ((SimpleScriptable)getScriptableObject()).clone();
    jsObjClone.setDomNode(result);
    

    if (deep) {
      synchronized (lock_) {
        attributeListeners_ = null;
      }
      selectionRanges_ = new ArrayList(3);
      afterLoadActions_ = new ArrayList();
      frameElements_ = new TreeSet(documentPositionComparator);
      for (DomNode child = getFirstChild(); child != null; child = child.getNextSibling()) {
        result.appendChild(child.cloneNode(true));
      }
    }
    return result;
  }
  






  public void addHtmlAttributeChangeListener(HtmlAttributeChangeListener listener)
  {
    WebAssert.notNull("listener", listener);
    synchronized (lock_) {
      if (attributeListeners_ == null) {
        attributeListeners_ = new LinkedHashSet();
      }
      attributeListeners_.add(listener);
    }
  }
  







  public void removeHtmlAttributeChangeListener(HtmlAttributeChangeListener listener)
  {
    WebAssert.notNull("listener", listener);
    synchronized (lock_) {
      if (attributeListeners_ != null) {
        attributeListeners_.remove(listener);
      }
    }
  }
  



  void fireHtmlAttributeAdded(HtmlAttributeChangeEvent event)
  {
    List<HtmlAttributeChangeListener> listeners = safeGetAttributeListeners();
    if (listeners != null) {
      for (HtmlAttributeChangeListener listener : listeners) {
        listener.attributeAdded(event);
      }
    }
  }
  



  void fireHtmlAttributeReplaced(HtmlAttributeChangeEvent event)
  {
    List<HtmlAttributeChangeListener> listeners = safeGetAttributeListeners();
    if (listeners != null) {
      for (HtmlAttributeChangeListener listener : listeners) {
        listener.attributeReplaced(event);
      }
    }
  }
  



  void fireHtmlAttributeRemoved(HtmlAttributeChangeEvent event)
  {
    List<HtmlAttributeChangeListener> listeners = safeGetAttributeListeners();
    if (listeners != null) {
      for (HtmlAttributeChangeListener listener : listeners) {
        listener.attributeRemoved(event);
      }
    }
  }
  
  private List<HtmlAttributeChangeListener> safeGetAttributeListeners() {
    synchronized (lock_) {
      if (attributeListeners_ != null) {
        return new ArrayList(attributeListeners_);
      }
      return null;
    }
  }
  


  protected void checkChildHierarchy(org.w3c.dom.Node newChild)
    throws DOMException
  {
    if ((newChild instanceof Element)) {
      if (getDocumentElement() != null) {
        throw new DOMException((short)3, 
          "The Document may only have a single child Element.");
      }
    }
    else if ((newChild instanceof DocumentType)) {
      if (getDoctype() != null) {
        throw new DOMException((short)3, 
          "The Document may only have a single child DocumentType.");
      }
    }
    else if ((!(newChild instanceof Comment)) && (!(newChild instanceof ProcessingInstruction))) {
      throw new DOMException((short)3, 
        "The Document may not have a child of this type: " + newChild.getNodeType());
    }
    super.checkChildHierarchy(newChild);
  }
  



  public boolean isBeingParsed()
  {
    return parserCount_ > 0;
  }
  


  void registerParsingStart()
  {
    parserCount_ += 1;
  }
  


  void registerParsingEnd()
  {
    parserCount_ -= 1;
  }
  









  boolean isParsingHtmlSnippet()
  {
    return snippetParserCount_ > 0;
  }
  


  void registerSnippetParsingStart()
  {
    snippetParserCount_ += 1;
  }
  


  void registerSnippetParsingEnd()
  {
    snippetParserCount_ -= 1;
  }
  







  boolean isParsingInlineHtmlSnippet()
  {
    return inlineSnippetParserCount_ > 0;
  }
  


  void registerInlineSnippetParsingStart()
  {
    inlineSnippetParserCount_ += 1;
  }
  


  void registerInlineSnippetParsingEnd()
  {
    inlineSnippetParserCount_ -= 1;
  }
  



  public Page refresh()
    throws IOException
  {
    return getWebClient().getPage(getWebResponse().getWebRequest());
  }
  







  public void writeInParsedStream(String string)
  {
    builder_.pushInputString(string);
  }
  



  void setBuilder(HTMLParser.HtmlUnitDOMBuilder htmlUnitDOMBuilder)
  {
    builder_ = htmlUnitDOMBuilder;
  }
  



  HTMLParser.HtmlUnitDOMBuilder getBuilder()
  {
    return builder_;
  }
  




  public Map<String, String> getNamespaces()
  {
    NamedNodeMap attributes = getDocumentElement().getAttributes();
    Map<String, String> namespaces = new HashMap();
    for (int i = 0; i < attributes.getLength(); i++) {
      Attr attr = (Attr)attributes.item(i);
      String name = attr.getName();
      if (name.startsWith("xmlns")) {
        int startPos = 5;
        if ((name.length() > 5) && (name.charAt(5) == ':')) {
          startPos = 6;
        }
        name = name.substring(startPos);
        namespaces.put(name, attr.getValue());
      }
    }
    return namespaces;
  }
  



  protected void setDocumentType(DocumentType type)
  {
    super.setDocumentType(type);
  }
  





  public void save(File file)
    throws IOException
  {
    new XmlSerializer().save(this, file);
  }
  



  public boolean isQuirksMode()
  {
    return "BackCompat".equals(((HTMLDocument)getScriptableObject()).getCompatMode());
  }
  




  public boolean isAttachedToPage()
  {
    return true;
  }
  



  public boolean isHtmlPage()
  {
    return true;
  }
  


  public URL getBaseURL()
  {
    URL baseUrl;
    
    if (base_ == null) {
      URL baseUrl = getUrl();
      WebWindow window = getEnclosingWindow();
      boolean frame = window != window.getTopWindow();
      if (frame) {
        boolean frameSrcIsNotSet = baseUrl == WebClient.URL_ABOUT_BLANK;
        boolean frameSrcIsJs = "javascript".equals(baseUrl.getProtocol());
        if ((frameSrcIsNotSet) || (frameSrcIsJs)) {
          baseUrl = 
            ((HtmlPage)window.getTopWindow().getEnclosedPage()).getWebResponse().getWebRequest().getUrl();
        }
      }
      else if (baseUrl_ != null) {
        baseUrl = baseUrl_;
      }
    }
    else {
      String href = base_.getHrefAttribute().trim();
      URL baseUrl; if (StringUtils.isEmpty(href)) {
        baseUrl = getUrl();
      }
      else {
        URL url = getUrl();
        try { URL baseUrl;
          if ((href.startsWith("http://")) || (href.startsWith("https://"))) {
            baseUrl = new URL(href);
          } else { URL baseUrl;
            if (href.startsWith("//")) {
              baseUrl = new URL(String.format("%s:%s", new Object[] { url.getProtocol(), href }));
            } else { URL baseUrl;
              if (href.startsWith("/")) {
                int port = Window.getPort(url);
                baseUrl = new URL(String.format("%s://%s:%d%s", new Object[] { url.getProtocol(), url.getHost(), Integer.valueOf(port), href }));
              } else { URL baseUrl;
                if (url.toString().endsWith("/")) {
                  baseUrl = new URL(String.format("%s%s", new Object[] { url.toString(), href }));
                }
                else
                  baseUrl = new URL(String.format("%s/%s", new Object[] { url.toString(), href }));
              }
            }
          } } catch (MalformedURLException e) { URL baseUrl;
          notifyIncorrectness("Invalid base url: \"" + href + "\", ignoring it");
          baseUrl = url;
        }
      }
    }
    
    return baseUrl;
  }
  





  public void addAutoCloseable(AutoCloseable autoCloseable)
  {
    if (autoCloseableList_ == null) {
      autoCloseableList_ = new ArrayList();
    }
    autoCloseableList_.add(autoCloseable);
  }
  



  public boolean handles(Event event)
  {
    if (("blur".equals(event.getType())) || ("focus".equals(event.getType()))) {
      return true;
    }
    return super.handles(event);
  }
  



  public void setElementFromPointHandler(ElementFromPointHandler elementFromPointHandler)
  {
    elementFromPointHandler_ = elementFromPointHandler;
  }
  








  public HtmlElement getElementFromPoint(int x, int y)
  {
    if (elementFromPointHandler_ == null) {
      LOG.warn("ElementFromPointHandler was not specicifed for " + this);
      if ((x <= 0) || (y <= 0)) {
        return null;
      }
      return getBody();
    }
    return elementFromPointHandler_.getElementFromPoint(this, x, y);
  }
  







  public boolean setFocusedElement(DomElement newElement)
  {
    return setFocusedElement(newElement, false);
  }
  








  public boolean setFocusedElement(DomElement newElement, boolean windowActivated)
  {
    if ((elementWithFocus_ == newElement) && (!windowActivated))
    {
      return true;
    }
    
    DomElement oldFocusedElement = elementWithFocus_;
    elementWithFocus_ = null;
    
    if (!windowActivated) {
      if (hasFeature(BrowserVersionFeatures.EVENT_FOCUS_IN_FOCUS_OUT_BLUR)) {
        if (oldFocusedElement != null) {
          oldFocusedElement.fireEvent("focusout");
        }
        
        if (newElement != null) {
          newElement.fireEvent("focusin");
        }
      }
      
      if (oldFocusedElement != null) {
        oldFocusedElement.removeFocus();
        oldFocusedElement.fireEvent("blur");
      }
    }
    
    elementWithFocus_ = newElement;
    
    if (((elementWithFocus_ instanceof SelectableTextInput)) && 
      (hasFeature(BrowserVersionFeatures.PAGE_SELECTION_RANGE_FROM_SELECTABLE_TEXT_INPUT))) {
      SelectableTextInput sti = (SelectableTextInput)elementWithFocus_;
      setSelectionRange(new SimpleRange(sti, sti.getSelectionStart(), sti, sti.getSelectionEnd()));
    }
    
    if (elementWithFocus_ != null) {
      elementWithFocus_.focus();
      elementWithFocus_.fireEvent("focus");
    }
    
    if (hasFeature(BrowserVersionFeatures.EVENT_FOCUS_FOCUS_IN_BLUR_OUT)) {
      if (oldFocusedElement != null) {
        oldFocusedElement.fireEvent("focusout");
      }
      
      if (newElement != null) {
        newElement.fireEvent("focusin");
      }
    }
    


    return this == getEnclosingWindow().getEnclosedPage();
  }
  




  public DomElement getFocusedElement()
  {
    return elementWithFocus_;
  }
  





  public void setElementWithFocus(DomElement elementWithFocus)
  {
    elementWithFocus_ = elementWithFocus;
  }
  







  public List<Range> getSelectionRanges()
  {
    return selectionRanges_;
  }
  






  public void setSelectionRange(Range selectionRange)
  {
    selectionRanges_.clear();
    selectionRanges_.add(selectionRange);
  }
  















  public ScriptResult executeJavaScriptFunctionIfPossible(Function function, Scriptable thisObject, Object[] args, DomNode htmlElementScope)
  {
    if (!getWebClient().getOptions().isJavaScriptEnabled()) {
      return new ScriptResult(null, this);
    }
    
    JavaScriptEngine engine = getWebClient().getJavaScriptEngine();
    Object result = engine.callFunction(this, function, thisObject, args, htmlElementScope);
    
    return new ScriptResult(result, getWebClient().getCurrentWindow().getEnclosedPage());
  }
  
  private void writeObject(ObjectOutputStream oos) throws IOException {
    oos.defaultWriteObject();
    oos.writeObject(originalCharset_ == null ? null : originalCharset_.name());
  }
  
  private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
    ois.defaultReadObject();
    String charsetName = (String)ois.readObject();
    if (charsetName != null) {
      originalCharset_ = Charset.forName(charsetName);
    }
  }
}
