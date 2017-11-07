package com.gargoylesoftware.htmlunit;

import com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLActiveXObjectFactory;
import com.gargoylesoftware.htmlunit.attachment.Attachment;
import com.gargoylesoftware.htmlunit.attachment.AttachmentHandler;
import com.gargoylesoftware.htmlunit.gae.GAEUtils;
import com.gargoylesoftware.htmlunit.html.BaseFrameElement;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HTMLParserListener;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.httpclient.HtmlUnitBrowserCompatCookieSpec;
import com.gargoylesoftware.htmlunit.javascript.DefaultJavaScriptErrorListener;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;
import com.gargoylesoftware.htmlunit.javascript.host.Location;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Node;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLIFrameElement;
import com.gargoylesoftware.htmlunit.protocol.data.DataUrlDecoder;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.util.UrlUtils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.message.BufferedHeader;
import org.apache.http.util.CharArrayBuffer;
import org.w3c.css.sac.ErrorHandler;


































































public class WebClient
  implements Serializable, AutoCloseable
{
  private static final Log LOG = LogFactory.getLog(WebClient.class);
  
  private static final int ALLOWED_REDIRECTIONS_SAME_URL = 20;
  
  private transient WebConnection webConnection_;
  
  private CredentialsProvider credentialsProvider_ = new DefaultCredentialsProvider();
  private CookieManager cookieManager_ = new CookieManager();
  private transient JavaScriptEngine scriptEngine_;
  private final Map<String, String> requestHeaders_ = Collections.synchronizedMap(new HashMap(89));
  private IncorrectnessListener incorrectnessListener_ = new IncorrectnessListenerImpl();
  
  private WebConsole webConsole_;
  private AlertHandler alertHandler_;
  private ConfirmHandler confirmHandler_;
  private PromptHandler promptHandler_;
  private StatusHandler statusHandler_;
  private AttachmentHandler attachmentHandler_;
  private AppletConfirmHandler appletConfirmHandler_;
  private AjaxController ajaxController_ = new AjaxController();
  
  private BrowserVersion browserVersion_;
  private PageCreator pageCreator_ = new DefaultPageCreator();
  
  private final Set<WebWindowListener> webWindowListeners_ = new HashSet(5);
  private final Stack<TopLevelWindow> topLevelWindows_ = new Stack();
  private final List<WebWindow> windows_ = Collections.synchronizedList(new ArrayList());
  
  private transient List<WeakReference<JavaScriptJobManager>> jobManagers_ = Collections.synchronizedList(new ArrayList());
  
  private WebWindow currentWindow_;
  private HTMLParserListener htmlParserListener_;
  private ErrorHandler cssErrorHandler_ = new DefaultCssErrorHandler();
  private OnbeforeunloadHandler onbeforeunloadHandler_;
  private Cache cache_ = new Cache();
  

  private static final String TARGET_BLANK = "_blank";
  

  private static final String TARGET_SELF = "_self";
  
  private static final String TARGET_PARENT = "_parent";
  
  private static final String TARGET_TOP = "_top";
  
  public static final String ABOUT_SCHEME = "about:";
  
  public static final String ABOUT_BLANK = "about:blank";
  
  public static final URL URL_ABOUT_BLANK = UrlUtils.toUrlSafe("about:blank");
  
  private ScriptPreProcessor scriptPreProcessor_;
  
  private Map<String, String> activeXObjectMap_ = Collections.emptyMap();
  private transient MSXMLActiveXObjectFactory msxmlActiveXObjectFactory_;
  private RefreshHandler refreshHandler_ = new NiceRefreshHandler(2);
  private JavaScriptErrorListener javaScriptErrorListener_ = new DefaultJavaScriptErrorListener();
  
  private WebClientOptions options_ = new WebClientOptions();
  private WebClientInternals internals_ = new WebClientInternals();
  private final StorageHolder storageHolder_ = new StorageHolder();
  
  private static final WebResponseData responseDataNoHttpResponse_ = new WebResponseData(
    0, "No HTTP Response", Collections.emptyList());
  



  public WebClient()
  {
    this(BrowserVersion.getDefault());
  }
  



  public WebClient(BrowserVersion browserVersion)
  {
    WebAssert.notNull("browserVersion", browserVersion);
    init(browserVersion, new ProxyConfig());
  }
  





  public WebClient(BrowserVersion browserVersion, String proxyHost, int proxyPort)
  {
    WebAssert.notNull("browserVersion", browserVersion);
    WebAssert.notNull("proxyHost", proxyHost);
    init(browserVersion, new ProxyConfig(proxyHost, proxyPort));
  }
  





  private void init(BrowserVersion browserVersion, ProxyConfig proxyConfig)
  {
    browserVersion_ = browserVersion;
    getOptions().setProxyConfig(proxyConfig);
    
    webConnection_ = createWebConnection();
    scriptEngine_ = new JavaScriptEngine(this);
    
    addWebWindowListener(new CurrentWindowTracker(this, null));
    currentWindow_ = new TopLevelWindow("", this);
    fireWindowOpened(new WebWindowEvent(currentWindow_, 1, null, null));
    
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_XML_SUPPORT_VIA_ACTIVEXOBJECT)) {
      initMSXMLActiveX();
    }
  }
  
  private void initMSXMLActiveX() {
    msxmlActiveXObjectFactory_ = new MSXMLActiveXObjectFactory();
    try
    {
      msxmlActiveXObjectFactory_.init(getBrowserVersion());
    }
    catch (Exception e) {
      LOG.error("Exception while initializing MSXML ActiveX for the page", e);
      throw new ScriptException(null, e);
    }
  }
  




  public WebConnection getWebConnection()
  {
    return webConnection_;
  }
  




  public void setWebConnection(WebConnection webConnection)
  {
    WebAssert.notNull("webConnection", webConnection);
    webConnection_ = webConnection;
  }
  




















  public <P extends Page> P getPage(WebWindow webWindow, WebRequest webRequest)
    throws IOException, FailingHttpStatusCodeException
  {
    return getPage(webWindow, webRequest, true);
  }
  


























  <P extends Page> P getPage(WebWindow webWindow, WebRequest webRequest, boolean addToHistory)
    throws IOException, FailingHttpStatusCodeException
  {
    Page page = webWindow.getEnclosedPage();
    
    if (page != null) {
      URL prev = page.getUrl();
      URL current = webRequest.getUrl();
      if ((UrlUtils.sameFile(current, prev)) && 
        (current.getRef() != null) && 
        (!StringUtils.equals(current.getRef(), prev.getRef())))
      {
        page.getWebResponse().getWebRequest().setUrl(current);
        if (addToHistory) {
          webWindow.getHistory().addPage(page);
        }
        
        Window window = (Window)webWindow.getScriptableObject();
        if (window != null) {
          window.getLocation().setHash(current.getRef());
          window.clearComputedStyles();
        }
        return page;
      }
      
      if (page.isHtmlPage()) {
        HtmlPage htmlPage = (HtmlPage)page;
        if (!htmlPage.isOnbeforeunloadAccepted()) {
          if (LOG.isDebugEnabled()) {
            LOG.debug("The registered OnbeforeunloadHandler rejected to load a new page.");
          }
          return page;
        }
      }
    }
    
    if (LOG.isDebugEnabled()) {
      LOG.debug("Get page for window named '" + webWindow.getName() + "', using " + webRequest);
    }
    

    String protocol = webRequest.getUrl().getProtocol();
    WebResponse webResponse; if ("javascript".equals(protocol)) {
      WebResponse webResponse = makeWebResponseForJavaScriptUrl(webWindow, webRequest.getUrl(), webRequest.getCharset());
      if ((webWindow.getEnclosedPage() != null) && (webWindow.getEnclosedPage().getWebResponse() == webResponse))
      {
        return webWindow.getEnclosedPage();
      }
    }
    else {
      webResponse = loadWebResponse(webRequest);
    }
    
    printContentIfNecessary(webResponse);
    loadWebResponseInto(webResponse, webWindow);
    



    if (scriptEngine_ != null) {
      scriptEngine_.registerWindowAndMaybeStartEventLoop(webWindow);
    }
    

    throwFailingHttpStatusCodeExceptionIfNecessary(webResponse);
    return webWindow.getEnclosedPage();
  }
  
















  public <P extends Page> P getPage(WebWindow opener, String target, WebRequest params)
    throws FailingHttpStatusCodeException, IOException
  {
    return getPage(openTargetWindow(opener, target, "_self"), params);
  }
  











  public <P extends Page> P getPage(String url)
    throws IOException, FailingHttpStatusCodeException, MalformedURLException
  {
    return getPage(UrlUtils.toUrlUnsafe(url));
  }
  









  public <P extends Page> P getPage(URL url)
    throws IOException, FailingHttpStatusCodeException
  {
    return getPage(getCurrentWindow().getTopWindow(), 
      new WebRequest(url, getBrowserVersion().getHtmlAcceptHeader()));
  }
  










  public <P extends Page> P getPage(WebRequest request)
    throws IOException, FailingHttpStatusCodeException
  {
    return getPage(getCurrentWindow().getTopWindow(), request);
  }
  

















  public Page loadWebResponseInto(WebResponse webResponse, WebWindow webWindow)
    throws IOException, FailingHttpStatusCodeException
  {
    WebAssert.notNull("webResponse", webResponse);
    WebAssert.notNull("webWindow", webWindow);
    
    if (webResponse.getStatusCode() == 204) {
      return webWindow.getEnclosedPage();
    }
    
    if ((attachmentHandler_ != null) && (Attachment.isAttachment(webResponse))) {
      WebWindow w = openWindow(null, null, webWindow);
      Page page = pageCreator_.createPage(webResponse, w);
      attachmentHandler_.handleAttachment(page);
      return page;
    }
    
    Page oldPage = webWindow.getEnclosedPage();
    if (oldPage != null)
    {
      oldPage.cleanUp();
    }
    Page newPage = null;
    if ((windows_.contains(webWindow)) || (getBrowserVersion().hasFeature(BrowserVersionFeatures.WINDOW_EXECUTE_EVENTS))) {
      newPage = pageCreator_.createPage(webResponse, webWindow);
      
      if (windows_.contains(webWindow)) {
        fireWindowContentChanged(new WebWindowEvent(webWindow, 3, oldPage, newPage));
        

        if (webWindow.getEnclosedPage() == newPage) {
          newPage.initialize();
          

          if (((webWindow instanceof FrameWindow)) && (!newPage.isHtmlPage())) {
            FrameWindow fw = (FrameWindow)webWindow;
            BaseFrameElement frame = fw.getFrameElement();
            if (frame.hasEventHandlers("onload")) {
              if (LOG.isDebugEnabled()) {
                LOG.debug("Executing onload handler for " + frame);
              }
              Event event = new Event(frame, "load");
              ((Node)frame.getScriptableObject()).executeEventLocally(event);
            }
          }
        }
      }
    }
    return newPage;
  }
  







  public void printContentIfNecessary(WebResponse webResponse)
  {
    String contentType = webResponse.getContentType();
    int statusCode = webResponse.getStatusCode();
    boolean successful = (statusCode >= 200) && (statusCode < 300);
    if ((getOptions().getPrintContentOnFailingStatusCode()) && (!successful)) {
      LOG.info("statusCode=[" + statusCode + "] contentType=[" + contentType + "]");
      LOG.info(webResponse.getContentAsString());
    }
  }
  







  public void throwFailingHttpStatusCodeExceptionIfNecessary(WebResponse webResponse)
  {
    int statusCode = webResponse.getStatusCode();
    boolean successful = ((statusCode >= 200) && (statusCode < 300)) || 
      (statusCode == 305) || 
      (statusCode == 304);
    if ((getOptions().isThrowExceptionOnFailingStatusCode()) && (!successful)) {
      throw new FailingHttpStatusCodeException(webResponse);
    }
  }
  





  public void addRequestHeader(String name, String value)
  {
    if ("cookie".equalsIgnoreCase(name)) {
      throw new IllegalArgumentException("Do not add 'Cookie' header, use .getCookieManager() instead");
    }
    requestHeaders_.put(name, value);
  }
  




  public void removeRequestHeader(String name)
  {
    requestHeaders_.remove(name);
  }
  






  public void setCredentialsProvider(CredentialsProvider credentialsProvider)
  {
    WebAssert.notNull("credentialsProvider", credentialsProvider);
    credentialsProvider_ = credentialsProvider;
  }
  




  public CredentialsProvider getCredentialsProvider()
  {
    return credentialsProvider_;
  }
  



  public JavaScriptEngine getJavaScriptEngine()
  {
    return scriptEngine_;
  }
  




  public void setJavaScriptEngine(JavaScriptEngine engine)
  {
    if (engine == null) {
      throw new IllegalArgumentException("Can't set JavaScriptEngine to null");
    }
    scriptEngine_ = engine;
  }
  



  public CookieManager getCookieManager()
  {
    return cookieManager_;
  }
  



  public void setCookieManager(CookieManager cookieManager)
  {
    WebAssert.notNull("cookieManager", cookieManager);
    cookieManager_ = cookieManager;
  }
  



  public void setAlertHandler(AlertHandler alertHandler)
  {
    alertHandler_ = alertHandler;
  }
  



  public AlertHandler getAlertHandler()
  {
    return alertHandler_;
  }
  



  public void setConfirmHandler(ConfirmHandler handler)
  {
    confirmHandler_ = handler;
  }
  



  public ConfirmHandler getConfirmHandler()
  {
    return confirmHandler_;
  }
  



  public void setPromptHandler(PromptHandler handler)
  {
    promptHandler_ = handler;
  }
  



  public PromptHandler getPromptHandler()
  {
    return promptHandler_;
  }
  



  public void setStatusHandler(StatusHandler statusHandler)
  {
    statusHandler_ = statusHandler;
  }
  



  public StatusHandler getStatusHandler()
  {
    return statusHandler_;
  }
  




  public void setJavaScriptErrorListener(JavaScriptErrorListener javaScriptErrorListener)
  {
    if (javaScriptErrorListener == null) {
      javaScriptErrorListener_ = new DefaultJavaScriptErrorListener();
    }
    else {
      javaScriptErrorListener_ = javaScriptErrorListener;
    }
  }
  



  public JavaScriptErrorListener getJavaScriptErrorListener()
  {
    return javaScriptErrorListener_;
  }
  



  public BrowserVersion getBrowserVersion()
  {
    return browserVersion_;
  }
  




  public WebWindow getCurrentWindow()
  {
    return currentWindow_;
  }
  




  public void setCurrentWindow(WebWindow window)
  {
    WebAssert.notNull("window", window);
    if (currentWindow_ == window) {
      return;
    }
    
    if ((currentWindow_ != null) && (!currentWindow_.isClosed())) {
      Page enclosedPage = currentWindow_.getEnclosedPage();
      if ((enclosedPage != null) && (enclosedPage.isHtmlPage())) {
        DomElement focusedElement = ((HtmlPage)enclosedPage).getFocusedElement();
        if (focusedElement != null) {
          focusedElement.fireEvent("blur");
        }
      }
    }
    currentWindow_ = window;
    

    boolean isIFrame = ((currentWindow_ instanceof FrameWindow)) && 
      ((((FrameWindow)currentWindow_).getFrameElement() instanceof HtmlInlineFrame));
    if (!isIFrame)
    {

      Page enclosedPage = currentWindow_.getEnclosedPage();
      if ((enclosedPage != null) && (enclosedPage.isHtmlPage())) {
        Window jsWindow = (Window)currentWindow_.getScriptableObject();
        if (jsWindow != null) {
          HTMLElement activeElement = ((HTMLDocument)jsWindow.getDocument()).getActiveElement();
          if (activeElement != null) {
            ((HtmlPage)enclosedPage).setFocusedElement(activeElement.getDomNodeOrDie(), true);
          }
        }
      }
    }
  }
  




  public void addWebWindowListener(WebWindowListener listener)
  {
    WebAssert.notNull("listener", listener);
    webWindowListeners_.add(listener);
  }
  



  public void removeWebWindowListener(WebWindowListener listener)
  {
    WebAssert.notNull("listener", listener);
    webWindowListeners_.remove(listener);
  }
  
  private void fireWindowContentChanged(WebWindowEvent event) {
    for (WebWindowListener listener : new ArrayList(webWindowListeners_)) {
      listener.webWindowContentChanged(event);
    }
  }
  
  private void fireWindowOpened(WebWindowEvent event) {
    for (WebWindowListener listener : new ArrayList(webWindowListeners_)) {
      listener.webWindowOpened(event);
    }
  }
  
  private void fireWindowClosed(WebWindowEvent event) {
    for (WebWindowListener listener : new ArrayList(webWindowListeners_)) {
      listener.webWindowClosed(event);
    }
  }
  







  public WebWindow openWindow(URL url, String windowName)
  {
    WebAssert.notNull("windowName", windowName);
    return openWindow(url, windowName, getCurrentWindow());
  }
  








  public WebWindow openWindow(URL url, String windowName, WebWindow opener)
  {
    WebWindow window = openTargetWindow(opener, windowName, "_blank");
    HtmlPage openerPage = (HtmlPage)opener.getEnclosedPage();
    if (url != null) {
      try {
        WebRequest request = new WebRequest(url, getBrowserVersion().getHtmlAcceptHeader());
        if ((getBrowserVersion().hasFeature(BrowserVersionFeatures.DIALOGWINDOW_REFERER)) && 
          (openerPage != null)) {
          String referer = openerPage.getUrl().toExternalForm();
          request.setAdditionalHeader("Referer", referer);
        }
        getPage(window, request);
      }
      catch (IOException e) {
        LOG.error("Error loading content into window", e);
      }
      
    } else {
      initializeEmptyWindow(window);
    }
    return window;
  }
  














  public WebWindow openTargetWindow(WebWindow opener, String windowName, String defaultName)
  {
    WebAssert.notNull("opener", opener);
    WebAssert.notNull("defaultName", defaultName);
    
    String windowToOpen = windowName;
    if ((windowToOpen == null) || (windowToOpen.isEmpty())) {
      windowToOpen = defaultName;
    }
    
    WebWindow webWindow = resolveWindow(opener, windowToOpen);
    
    if (webWindow == null) {
      if ("_blank".equals(windowToOpen)) {
        windowToOpen = "";
      }
      webWindow = new TopLevelWindow(windowToOpen, this);
      fireWindowOpened(new WebWindowEvent(webWindow, 1, null, null));
    }
    
    if (((webWindow instanceof TopLevelWindow)) && (webWindow != opener.getTopWindow())) {
      ((TopLevelWindow)webWindow).setOpener(opener);
    }
    
    return webWindow;
  }
  
  private WebWindow resolveWindow(WebWindow opener, String name) {
    if ((name == null) || (name.isEmpty()) || ("_self".equals(name))) {
      return opener;
    }
    
    if ("_parent".equals(name)) {
      return opener.getParentWindow();
    }
    
    if ("_top".equals(name)) {
      return opener.getTopWindow();
    }
    
    if ("_blank".equals(name)) {
      return null;
    }
    

    WebWindow window = opener;
    for (;;) {
      Page page = window.getEnclosedPage();
      if ((page != null) && (page.isHtmlPage())) {
        try {
          FrameWindow frame = ((HtmlPage)page).getFrameByName(name);
          ScriptableObject scriptable = frame.getFrameElement().getScriptableObject();
          if ((scriptable instanceof HTMLIFrameElement)) {
            ((HTMLIFrameElement)scriptable).onRefresh();
          }
          return frame;
        }
        catch (ElementNotFoundException localElementNotFoundException) {}
      }
      


      if (window == window.getParentWindow()) {
        break;
      }
      
      window = window.getParentWindow();
    }
    try
    {
      return getWebWindowByName(name);
    }
    catch (WebWindowNotFoundException localWebWindowNotFoundException) {}
    

    return null;
  }
  










  public DialogWindow openDialogWindow(URL url, WebWindow opener, Object dialogArguments)
    throws IOException
  {
    WebAssert.notNull("url", url);
    WebAssert.notNull("opener", opener);
    
    DialogWindow window = new DialogWindow(this, dialogArguments);
    fireWindowOpened(new WebWindowEvent(window, 1, null, null));
    
    HtmlPage openerPage = (HtmlPage)opener.getEnclosedPage();
    WebRequest request = new WebRequest(url, getBrowserVersion().getHtmlAcceptHeader());
    if ((getBrowserVersion().hasFeature(BrowserVersionFeatures.DIALOGWINDOW_REFERER)) && (openerPage != null)) {
      String referer = openerPage.getUrl().toExternalForm();
      request.setAdditionalHeader("Referer", referer);
    }
    
    getPage(window, request);
    
    return window;
  }
  





  public void setPageCreator(PageCreator pageCreator)
  {
    WebAssert.notNull("pageCreator", pageCreator);
    pageCreator_ = pageCreator;
  }
  




  public PageCreator getPageCreator()
  {
    return pageCreator_;
  }
  







  public WebWindow getWebWindowByName(String name)
    throws WebWindowNotFoundException
  {
    WebAssert.notNull("name", name);
    
    for (WebWindow webWindow : windows_) {
      if (name.equals(webWindow.getName())) {
        return webWindow;
      }
    }
    
    throw new WebWindowNotFoundException(name);
  }
  





  public void initialize(WebWindow webWindow)
  {
    WebAssert.notNull("webWindow", webWindow);
    scriptEngine_.initialize(webWindow);
  }
  





  public void initialize(Page newPage)
  {
    WebAssert.notNull("newPage", newPage);
    ((Window)newPage.getEnclosingWindow().getScriptableObject()).initialize(newPage);
  }
  






  public void initializeEmptyWindow(WebWindow webWindow)
  {
    WebAssert.notNull("webWindow", webWindow);
    initialize(webWindow);
    ((Window)webWindow.getScriptableObject()).initialize();
  }
  






  public void registerWebWindow(WebWindow webWindow)
  {
    WebAssert.notNull("webWindow", webWindow);
    windows_.add(webWindow);
    
    jobManagers_.add(new WeakReference(webWindow.getJobManager()));
  }
  






  public void deregisterWebWindow(WebWindow webWindow)
  {
    WebAssert.notNull("webWindow", webWindow);
    if (windows_.remove(webWindow)) {
      fireWindowClosed(new WebWindowEvent(webWindow, 2, webWindow.getEnclosedPage(), null));
    }
  }
  










  public static URL expandUrl(URL baseUrl, String relativeUrl)
    throws MalformedURLException
  {
    String newUrl = UrlUtils.resolveUrl(baseUrl, relativeUrl);
    return UrlUtils.toUrlUnsafe(newUrl);
  }
  
  private WebResponse makeWebResponseForDataUrl(WebRequest webRequest) throws IOException {
    URL url = webRequest.getUrl();
    List<NameValuePair> responseHeaders = new ArrayList();
    try
    {
      decoder = DataUrlDecoder.decode(url);
    } catch (DecoderException e) {
      DataUrlDecoder decoder;
      throw new IOException(e.getMessage()); }
    DataUrlDecoder decoder;
    responseHeaders.add(new NameValuePair("content-type", 
      decoder.getMediaType() + ";charset=" + decoder.getCharset()));
    DownloadedContent downloadedContent = 
      HttpWebConnection.downloadContent(url.openStream(), getOptions().getMaxInMemory());
    WebResponseData data = new WebResponseData(downloadedContent, 200, "OK", responseHeaders);
    return new WebResponse(data, url, webRequest.getHttpMethod(), 0L);
  }
  
  private static WebResponse makeWebResponseForAboutUrl(URL url) {
    String urlWithoutQuery = StringUtils.substringBefore(url.toExternalForm(), "?");
    if (!"blank".equalsIgnoreCase(StringUtils.substringAfter(urlWithoutQuery, "about:"))) {
      throw new IllegalArgumentException(url + " is not supported, only about:blank is supported now.");
    }
    return new StringWebResponse("", URL_ABOUT_BLANK);
  }
  







  private WebResponse makeWebResponseForFileUrl(WebRequest webRequest)
    throws IOException
  {
    URL cleanUrl = webRequest.getUrl();
    if (cleanUrl.getQuery() != null)
    {
      cleanUrl = UrlUtils.getUrlWithNewQuery(cleanUrl, null);
    }
    if (cleanUrl.getRef() != null)
    {
      cleanUrl = UrlUtils.getUrlWithNewRef(cleanUrl, null);
    }
    
    String fileUrl = cleanUrl.toExternalForm();
    fileUrl = URLDecoder.decode(fileUrl, StandardCharsets.UTF_8.name());
    File file = new File(fileUrl.substring(5));
    if (!file.exists())
    {
      List<NameValuePair> compiledHeaders = new ArrayList();
      compiledHeaders.add(new NameValuePair("Content-Type", "text/html"));
      WebResponseData responseData = 
        new WebResponseData(
        TextUtil.stringToByteArray("File: " + file.getAbsolutePath(), StandardCharsets.UTF_8), 
        404, "Not Found", compiledHeaders);
      return new WebResponse(responseData, webRequest, 0L);
    }
    
    String contentType = guessContentType(file);
    
    DownloadedContent content = new DownloadedContent.OnFile(file, false);
    List<NameValuePair> compiledHeaders = new ArrayList();
    compiledHeaders.add(new NameValuePair("Content-Type", contentType));
    WebResponseData responseData = new WebResponseData(content, 200, "OK", compiledHeaders);
    return new WebResponse(responseData, webRequest, 0L);
  }
  







  public String guessContentType(File file)
  {
    if (file.getName().endsWith(".xhtml"))
    {
      return "application/xhtml+xml";
    }
    

    if (file.getName().endsWith(".js")) {
      return "text/javascript";
    }
    if (file.getName().toLowerCase().endsWith(".css")) {
      return "text/css";
    }
    
    String contentType = URLConnection.guessContentTypeFromName(file.getName());
    if (contentType == null) {
      try { Object localObject1 = null;Object localObject4 = null; Object localObject3; label144: try { inputStream = new BufferedInputStream(new FileInputStream(file));
        } finally { InputStream inputStream;
          localObject3 = localThrowable; break label144; if (localObject3 != localThrowable) localObject3.addSuppressed(localThrowable);
        }
      }
      catch (IOException localIOException1) {}
    }
    if (contentType == null) {
      contentType = "application/octet-stream";
    }
    return contentType;
  }
  
  private WebResponse makeWebResponseForJavaScriptUrl(WebWindow webWindow, URL url, Charset charset)
    throws FailingHttpStatusCodeException, IOException
  {
    HtmlPage page = null;
    if ((webWindow instanceof FrameWindow)) {
      FrameWindow frameWindow = (FrameWindow)webWindow;
      page = (HtmlPage)frameWindow.getEnclosedPage();
    }
    else {
      Page currentPage = webWindow.getEnclosedPage();
      if (currentPage == null)
      {
        currentPage = getPage(webWindow, new WebRequest(URL_ABOUT_BLANK));
      }
      else if ((currentPage instanceof HtmlPage)) {
        page = (HtmlPage)currentPage;
      }
    }
    
    if (page == null) {
      page = (HtmlPage)getPage(webWindow, new WebRequest(URL_ABOUT_BLANK));
    }
    ScriptResult r = page.executeJavaScriptIfPossible(url.toExternalForm(), "JavaScript URL", 1);
    if ((r.getJavaScriptResult() == null) || (ScriptResult.isUndefined(r)))
    {
      return webWindow.getEnclosedPage().getWebResponse();
    }
    
    String contentString = r.getJavaScriptResult().toString();
    StringWebResponse response = new StringWebResponse(contentString, charset, url);
    response.setFromJavascript(true);
    return response;
  }
  


  public WebResponse loadWebResponse(WebRequest webRequest)
    throws IOException
  {
    String str;
    
    switch ((str = webRequest.getUrl().getProtocol()).hashCode()) {case 3076010:  if (str.equals("data")) {} break; case 3143036:  if (str.equals("file")) break;  case 92611469:  if ((goto 107) && (str.equals("about")))
      {
        return makeWebResponseForAboutUrl(webRequest.getUrl());
        

        return makeWebResponseForFileUrl(webRequest);
        

        return makeWebResponseForDataUrl(webRequest);
      }
      break; }
    return loadWebResponseFromWebConnection(webRequest, 20);
  }
  








  private WebResponse loadWebResponseFromWebConnection(WebRequest webRequest, int allowedRedirects)
    throws IOException
  {
    URL url = webRequest.getUrl();
    HttpMethod method = webRequest.getHttpMethod();
    List<NameValuePair> parameters = webRequest.getRequestParameters();
    
    WebAssert.notNull("url", url);
    WebAssert.notNull("method", method);
    WebAssert.notNull("parameters", parameters);
    
    url = UrlUtils.encodeUrl(url, getBrowserVersion().hasFeature(BrowserVersionFeatures.URL_MINIMAL_QUERY_ENCODING), 
      webRequest.getCharset());
    webRequest.setUrl(url);
    
    if (LOG.isDebugEnabled()) {
      LOG.debug("Load response for " + method + " " + url.toExternalForm());
    }
    

    if (webRequest.getProxyHost() == null) {
      ProxyConfig proxyConfig = getOptions().getProxyConfig();
      if (proxyConfig.getProxyAutoConfigUrl() != null) {
        if (!UrlUtils.sameFile(new URL(proxyConfig.getProxyAutoConfigUrl()), url)) {
          String content = proxyConfig.getProxyAutoConfigContent();
          if (content == null) {
            content = 
              getPage(proxyConfig.getProxyAutoConfigUrl()).getWebResponse().getContentAsString();
            proxyConfig.setProxyAutoConfigContent(content);
          }
          String allValue = ProxyAutoConfig.evaluate(content, url);
          if (LOG.isDebugEnabled()) {
            LOG.debug("Proxy Auto-Config: value '" + allValue + "' for URL " + url);
          }
          String value = allValue.split(";")[0].trim();
          if (value.startsWith("PROXY")) {
            value = value.substring(6);
            int colonIndex = value.indexOf(':');
            webRequest.setSocksProxy(false);
            webRequest.setProxyHost(value.substring(0, colonIndex));
            webRequest.setProxyPort(Integer.parseInt(value.substring(colonIndex + 1)));
          }
          else if (value.startsWith("SOCKS")) {
            value = value.substring(6);
            int colonIndex = value.indexOf(':');
            webRequest.setSocksProxy(true);
            webRequest.setProxyHost(value.substring(0, colonIndex));
            webRequest.setProxyPort(Integer.parseInt(value.substring(colonIndex + 1)));
          }
          
        }
      }
      else if (!proxyConfig.shouldBypassProxy(webRequest.getUrl().getHost())) {
        webRequest.setProxyHost(proxyConfig.getProxyHost());
        webRequest.setProxyPort(proxyConfig.getProxyPort());
        webRequest.setSocksProxy(proxyConfig.isSocksProxy());
      }
    }
    

    addDefaultHeaders(webRequest);
    

    WebResponse fromCache = getCache().getCachedResponse(webRequest);
    WebResponse webResponse;
    WebResponse webResponse; if (fromCache != null) {
      webResponse = new WebResponseFromCache(fromCache, webRequest);
    }
    else {
      try {
        webResponse = getWebConnection().getResponse(webRequest);
      } catch (NoHttpResponseException e) {
        WebResponse webResponse;
        return new WebResponse(responseDataNoHttpResponse_, webRequest, 0L);
      }
      getCache().cacheIfPossible(webRequest, webResponse, null);
    }
    

    int status = webResponse.getStatusCode();
    if (status == 305) {
      getIncorrectnessListener().notify("Ignoring HTTP status code [305] 'Use Proxy'", this);
    }
    else if (status >= 301) {
      if (status <= (getBrowserVersion().hasFeature(BrowserVersionFeatures.HTTP_REDIRECT_308) ? 308 : 307))
        if ((status != 304) && 
          (getOptions().isRedirectEnabled()))
        {

          String locationString = null;
          try {
            locationString = webResponse.getResponseHeaderValue("Location");
            if (locationString == null) {
              return webResponse;
            }
            newUrl = expandUrl(url, locationString);
          } catch (MalformedURLException e) {
            URL newUrl;
            getIncorrectnessListener().notify("Got a redirect status code [" + status + " " + 
              webResponse.getStatusMessage() + 
              "] but the location is not a valid URL [" + locationString + 
              "]. Skipping redirection processing.", this);
            return webResponse;
          }
          URL newUrl;
          if (LOG.isDebugEnabled()) {
            LOG.debug("Got a redirect status code [" + status + "] new location = [" + locationString + "]");
          }
          
          if (allowedRedirects == 0) {
            throw new FailingHttpStatusCodeException("Too much redirect for " + 
              webResponse.getWebRequest().getUrl(), webResponse);
          }
          if ((status == 301) || 
            (status == 302) || 
            (status == 303)) {
            WebRequest wrs = new WebRequest(newUrl, HttpMethod.GET);
            for (Map.Entry<String, String> entry : webRequest.getAdditionalHeaders().entrySet()) {
              wrs.setAdditionalHeader((String)entry.getKey(), (String)entry.getValue());
            }
            return loadWebResponseFromWebConnection(wrs, allowedRedirects - 1);
          }
          if ((status == 307) || 
            (status == 308)) {
            WebRequest wrs = new WebRequest(newUrl, webRequest.getHttpMethod());
            wrs.setRequestParameters(parameters);
            for (Map.Entry<String, String> entry : webRequest.getAdditionalHeaders().entrySet()) {
              wrs.setAdditionalHeader((String)entry.getKey(), (String)entry.getValue());
            }
            return loadWebResponseFromWebConnection(wrs, allowedRedirects - 1);
          }
        }
    }
    return webResponse;
  }
  




  private void addDefaultHeaders(WebRequest wrs)
  {
    if (!wrs.isAdditionalHeader("Accept-Language")) {
      wrs.setAdditionalHeader("Accept-Language", getBrowserVersion().getBrowserLanguage());
    }
    
    wrs.getAdditionalHeaders().putAll(requestHeaders_);
  }
  







  public List<WebWindow> getWebWindows()
  {
    return Collections.unmodifiableList(new ArrayList(windows_));
  }
  









  public boolean containsWebWindow(WebWindow webWindow)
  {
    return windows_.contains(webWindow);
  }
  







  public List<TopLevelWindow> getTopLevelWindows()
  {
    return Collections.unmodifiableList(new ArrayList(topLevelWindows_));
  }
  




  public void setRefreshHandler(RefreshHandler handler)
  {
    if (handler == null) {
      refreshHandler_ = new NiceRefreshHandler(2);
    }
    else {
      refreshHandler_ = handler;
    }
  }
  




  public RefreshHandler getRefreshHandler()
  {
    return refreshHandler_;
  }
  



  public void setScriptPreProcessor(ScriptPreProcessor scriptPreProcessor)
  {
    scriptPreProcessor_ = scriptPreProcessor;
  }
  



  public ScriptPreProcessor getScriptPreProcessor()
  {
    return scriptPreProcessor_;
  }
  






  public void setActiveXObjectMap(Map<String, String> activeXObjectMap)
  {
    activeXObjectMap_ = activeXObjectMap;
  }
  



  public Map<String, String> getActiveXObjectMap()
  {
    return activeXObjectMap_;
  }
  



  public MSXMLActiveXObjectFactory getMSXMLActiveXObjectFactory()
  {
    return msxmlActiveXObjectFactory_;
  }
  



  public void setHTMLParserListener(HTMLParserListener listener)
  {
    htmlParserListener_ = listener;
  }
  



  public HTMLParserListener getHTMLParserListener()
  {
    return htmlParserListener_;
  }
  





  public ErrorHandler getCssErrorHandler()
  {
    return cssErrorHandler_;
  }
  





  public void setCssErrorHandler(ErrorHandler cssErrorHandler)
  {
    WebAssert.notNull("cssErrorHandler", cssErrorHandler);
    cssErrorHandler_ = cssErrorHandler;
  }
  





  public void setJavaScriptTimeout(long timeout)
  {
    scriptEngine_.setJavaScriptTimeout(timeout);
  }
  





  public long getJavaScriptTimeout()
  {
    return scriptEngine_.getJavaScriptTimeout();
  }
  





  public IncorrectnessListener getIncorrectnessListener()
  {
    return incorrectnessListener_;
  }
  



  public void setIncorrectnessListener(IncorrectnessListener listener)
  {
    if (listener == null) {
      throw new NullPointerException("Null incorrectness listener.");
    }
    incorrectnessListener_ = listener;
  }
  



  public WebConsole getWebConsole()
  {
    if (webConsole_ == null) {
      webConsole_ = new WebConsole();
    }
    return webConsole_;
  }
  



  public AjaxController getAjaxController()
  {
    return ajaxController_;
  }
  



  public void setAjaxController(AjaxController newValue)
  {
    if (newValue == null) {
      throw new NullPointerException();
    }
    ajaxController_ = newValue;
  }
  



  public void setAttachmentHandler(AttachmentHandler handler)
  {
    attachmentHandler_ = handler;
  }
  



  public AttachmentHandler getAttachmentHandler()
  {
    return attachmentHandler_;
  }
  



  public void setAppletConfirmHandler(AppletConfirmHandler handler)
  {
    appletConfirmHandler_ = handler;
  }
  



  public AppletConfirmHandler getAppletConfirmHandler()
  {
    return appletConfirmHandler_;
  }
  



  public void setOnbeforeunloadHandler(OnbeforeunloadHandler onbeforeunloadHandler)
  {
    onbeforeunloadHandler_ = onbeforeunloadHandler;
  }
  



  public OnbeforeunloadHandler getOnbeforeunloadHandler()
  {
    return onbeforeunloadHandler_;
  }
  



  public Cache getCache()
  {
    return cache_;
  }
  



  public void setCache(Cache cache)
  {
    if (cache == null) {
      throw new IllegalArgumentException("cache should not be null!");
    }
    cache_ = cache;
  }
  
  private static final class CurrentWindowTracker
    implements WebWindowListener, Serializable
  {
    private final WebClient webClient_;
    
    private CurrentWindowTracker(WebClient webClient)
    {
      webClient_ = webClient;
    }
    



    public void webWindowClosed(WebWindowEvent event)
    {
      WebWindow window = event.getWebWindow();
      if ((window instanceof TopLevelWindow)) {
        webClient_.topLevelWindows_.remove(window);
        if (window == webClient_.getCurrentWindow()) {
          if (webClient_.topLevelWindows_.isEmpty())
          {
            TopLevelWindow newWindow = new TopLevelWindow("", webClient_);
            webClient_.topLevelWindows_.push(newWindow);
            webClient_.setCurrentWindow(newWindow);
          }
          else
          {
            webClient_.setCurrentWindow((WebWindow)webClient_.topLevelWindows_.peek());
          }
        }
      }
      else if (window == webClient_.getCurrentWindow())
      {
        webClient_.setCurrentWindow((WebWindow)webClient_.topLevelWindows_.peek());
      }
    }
    



    public void webWindowContentChanged(WebWindowEvent event)
    {
      WebWindow window = event.getWebWindow();
      boolean use = false;
      if ((window instanceof DialogWindow)) {
        use = true;
      }
      else if ((window instanceof TopLevelWindow)) {
        use = event.getOldPage() == null;
      }
      else if ((window instanceof FrameWindow)) {
        FrameWindow fw = (FrameWindow)window;
        String enclosingPageState = fw.getEnclosingPage().getDocumentElement().getReadyState();
        URL frameUrl = fw.getEnclosedPage().getUrl();
        if ((!"complete".equals(enclosingPageState)) || (frameUrl == WebClient.URL_ABOUT_BLANK)) {
          return;
        }
        

        BaseFrameElement frameElement = fw.getFrameElement();
        if (frameElement.isDisplayed()) {
          HTMLElement htmlElement = (HTMLElement)frameElement.getScriptableObject();
          ComputedCSSStyleDeclaration style = 
            htmlElement.getWindow().getComputedStyle(htmlElement, null);
          use = (style.getCalculatedWidth(false, false) != 0) && 
            (style.getCalculatedHeight(false, false) != 0);
        }
      }
      if (use) {
        webClient_.setCurrentWindow(window);
      }
    }
    



    public void webWindowOpened(WebWindowEvent event)
    {
      WebWindow window = event.getWebWindow();
      if ((window instanceof TopLevelWindow)) {
        TopLevelWindow tlw = (TopLevelWindow)window;
        webClient_.topLevelWindows_.push(tlw);
      }
    }
  }
  








  public void close()
  {
    List<TopLevelWindow> topWindows = new ArrayList(topLevelWindows_);
    for (TopLevelWindow topWindow : topWindows) {
      if (topLevelWindows_.contains(topWindow)) {
        topWindow.close();
      }
    }
    


    if (scriptEngine_ != null) {
      scriptEngine_.shutdown();
    }
    try
    {
      webConnection_.close();
    }
    catch (Exception e) {
      LOG.error("Exception while closing the connection", e);
    }
    
    cache_.clear();
  }
  



















  public int waitForBackgroundJavaScript(long timeoutMillis)
  {
    int count = 0;
    long endTime = System.currentTimeMillis() + timeoutMillis;
    for (Iterator<WeakReference<JavaScriptJobManager>> i = jobManagers_.iterator(); i.hasNext();)
    {
      try
      {
        WeakReference<JavaScriptJobManager> reference = (WeakReference)i.next();
        JavaScriptJobManager jobManager = (JavaScriptJobManager)reference.get();
        if (jobManager == null) {
          i.remove();
        }
      }
      catch (ConcurrentModificationException e)
      {
        i = jobManagers_.iterator();
        count = 0;
      }
      WeakReference<JavaScriptJobManager> reference;
      JavaScriptJobManager jobManager;
      long newTimeout = endTime - System.currentTimeMillis();
      count += jobManager.waitForJobs(newTimeout);
    }
    if (count != getAggregateJobCount()) {
      long newTimeout = endTime - System.currentTimeMillis();
      return waitForBackgroundJavaScript(newTimeout);
    }
    return count;
  }
  























  public int waitForBackgroundJavaScriptStartingBefore(long delayMillis)
  {
    int count = 0;
    long endTime = System.currentTimeMillis() + delayMillis;
    for (Iterator<WeakReference<JavaScriptJobManager>> i = jobManagers_.iterator(); i.hasNext();)
    {
      try
      {
        WeakReference<JavaScriptJobManager> reference = (WeakReference)i.next();
        JavaScriptJobManager jobManager = (JavaScriptJobManager)reference.get();
        if (jobManager == null) {
          i.remove();
        }
      }
      catch (ConcurrentModificationException e)
      {
        i = jobManagers_.iterator();
        count = 0; }
      WeakReference<JavaScriptJobManager> reference;
      JavaScriptJobManager jobManager;
      long newDelay = endTime - System.currentTimeMillis();
      count += jobManager.waitForJobsStartingBefore(newDelay);
    }
    if (count != getAggregateJobCount()) {
      long newDelay = endTime - System.currentTimeMillis();
      return waitForBackgroundJavaScriptStartingBefore(newDelay);
    }
    return count;
  }
  



  private int getAggregateJobCount()
  {
    int count = 0;
    for (Iterator<WeakReference<JavaScriptJobManager>> i = jobManagers_.iterator(); i.hasNext();)
    {
      try
      {
        WeakReference<JavaScriptJobManager> reference = (WeakReference)i.next();
        JavaScriptJobManager jobManager = (JavaScriptJobManager)reference.get();
        if (jobManager == null) {
          i.remove();
        }
      }
      catch (ConcurrentModificationException e)
      {
        i = jobManagers_.iterator();
        count = 0; }
      WeakReference<JavaScriptJobManager> reference;
      JavaScriptJobManager jobManager;
      int jobCount = jobManager.getJobCount();
      count += jobCount;
    }
    return count;
  }
  




  private void readObject(ObjectInputStream in)
    throws IOException, ClassNotFoundException
  {
    in.defaultReadObject();
    
    webConnection_ = createWebConnection();
    scriptEngine_ = new JavaScriptEngine(this);
    jobManagers_ = Collections.synchronizedList(new ArrayList());
    
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_XML_SUPPORT_VIA_ACTIVEXOBJECT)) {
      initMSXMLActiveX();
    }
  }
  
  private WebConnection createWebConnection() {
    if (GAEUtils.isGaeMode()) {
      return new UrlFetchWebConnection(this);
    }
    
    return new HttpWebConnection(this);
  }
  
  private static class LoadJob
  {
    private final WebWindow requestingWindow_;
    private final String target_;
    private final WebResponse response_;
    private final URL urlWithOnlyHashChange_;
    private final WeakReference<Page> originalPage_;
    private final WebRequest request_;
    
    LoadJob(WebRequest request, WebWindow requestingWindow, String target, WebResponse response) {
      request_ = request;
      requestingWindow_ = requestingWindow;
      target_ = target;
      response_ = response;
      urlWithOnlyHashChange_ = null;
      originalPage_ = new WeakReference(requestingWindow.getEnclosedPage());
    }
    
    LoadJob(WebRequest request, WebWindow requestingWindow, String target, URL urlWithOnlyHashChange)
    {
      request_ = request;
      requestingWindow_ = requestingWindow;
      target_ = target;
      response_ = null;
      urlWithOnlyHashChange_ = urlWithOnlyHashChange;
      originalPage_ = new WeakReference(requestingWindow.getEnclosedPage());
    }
    
    public boolean isOutdated() {
      if ((target_ != null) && (!target_.isEmpty())) {
        return false;
      }
      if (requestingWindow_.isClosed()) {
        return true;
      }
      if (requestingWindow_.getEnclosedPage() != originalPage_.get()) {
        return true;
      }
      
      return false;
    }
  }
  
  private final List<LoadJob> loadQueue_ = new ArrayList();
  













  public void download(WebWindow requestingWindow, String target, WebRequest request, boolean checkHash, boolean forceLoad, String description)
  {
    WebWindow win = resolveWindow(requestingWindow, target);
    URL url = request.getUrl();
    boolean justHashJump = false;
    
    if ((win != null) && (HttpMethod.POST != request.getHttpMethod())) {
      Page page = win.getEnclosedPage();
      if (page != null) {
        if ((page.isHtmlPage()) && (!((HtmlPage)page).isOnbeforeunloadAccepted())) {
          return;
        }
        
        if (checkHash) {
          URL current = page.getUrl();
          justHashJump = 
            (HttpMethod.GET == request.getHttpMethod()) && 
            (UrlUtils.sameFile(url, current)) && 
            (url.getRef() != null);
        }
      }
    }
    
    synchronized (loadQueue_)
    {
      for (LoadJob loadJob : loadQueue_) {
        if (response_ != null)
        {

          WebRequest otherRequest = request_;
          URL otherUrl = otherRequest.getUrl();
          

          if ((!forceLoad) && 
            (url.getPath().equals(otherUrl.getPath())) && 
            (url.toString().equals(otherUrl.toString())) && 
            (request.getRequestParameters().equals(otherRequest.getRequestParameters())) && 
            (StringUtils.equals(request.getRequestBody(), otherRequest.getRequestBody()))) {
            return;
          }
        }
      }
    }
    LoadJob loadJob;
    if (justHashJump) {
      loadJob = new LoadJob(request, requestingWindow, target, url);
    } else {
      try
      {
        WebResponse response = loadWebResponse(request);
        loadJob = new LoadJob(request, requestingWindow, target, response);
      } catch (IOException e) {
        LoadJob loadJob;
        throw new RuntimeException(e);
      }
    }
    synchronized (loadQueue_) { LoadJob loadJob;
      loadQueue_.add(loadJob);
    }
  }
  










  public void loadDownloadedResponses()
    throws FailingHttpStatusCodeException, IOException
  {
    synchronized (loadQueue_) {
      if (loadQueue_.isEmpty()) {
        return;
      }
      List<LoadJob> queue = new ArrayList(loadQueue_);
      loadQueue_.clear();
    }
    List<LoadJob> queue;
    HashSet<WebWindow> updatedWindows = new HashSet();
    for (int i = queue.size() - 1; i >= 0; i--) {
      LoadJob loadJob = (LoadJob)queue.get(i);
      if (loadJob.isOutdated()) {
        LOG.info("No usage of download: " + loadJob);
      }
      else
      {
        WebWindow window = resolveWindow(requestingWindow_, target_);
        if (!updatedWindows.contains(window)) {
          WebWindow win = openTargetWindow(requestingWindow_, target_, "_self");
          if (urlWithOnlyHashChange_ != null) {
            HtmlPage page = (HtmlPage)requestingWindow_.getEnclosedPage();
            String oldURL = page.getUrl().toExternalForm();
            

            WebRequest req = page.getWebResponse().getWebRequest();
            req.setUrl(urlWithOnlyHashChange_);
            

            Window jsWindow = (Window)win.getScriptableObject();
            if (jsWindow != null) {
              Location location = jsWindow.getLocation();
              location.setHash(oldURL, urlWithOnlyHashChange_.getRef());
            }
            

            win.getHistory().addPage(page);
          }
          else {
            Page pageBeforeLoad = win.getEnclosedPage();
            loadWebResponseInto(response_, win);
            

            if (scriptEngine_ != null) {
              scriptEngine_.registerWindowAndMaybeStartEventLoop(win);
            }
            
            if (pageBeforeLoad != win.getEnclosedPage()) {
              updatedWindows.add(win);
            }
            

            throwFailingHttpStatusCodeExceptionIfNecessary(response_);
          }
        }
        else {
          LOG.info("No usage of download: " + loadJob);
        }
      }
    }
  }
  


  public WebClientOptions getOptions()
  {
    return options_;
  }
  





  public WebClientInternals getInternals()
  {
    return internals_;
  }
  




  public StorageHolder getStorageHolder()
  {
    return storageHolder_;
  }
  





  public synchronized Set<com.gargoylesoftware.htmlunit.util.Cookie> getCookies(URL url)
  {
    CookieManager cookieManager = getCookieManager();
    
    if (!cookieManager.isCookiesEnabled()) {
      return Collections.emptySet();
    }
    
    URL normalizedUrl = cookieManager.replaceForCookieIfNecessary(url);
    
    String host = normalizedUrl.getHost();
    

    if (host.isEmpty()) {
      return Collections.emptySet();
    }
    
    String path = normalizedUrl.getPath();
    String protocol = normalizedUrl.getProtocol();
    boolean secure = "https".equals(protocol);
    
    int port = cookieManager.getPort(normalizedUrl);
    

    cookieManager.clearExpired(new Date());
    
    List<org.apache.http.cookie.Cookie> all = com.gargoylesoftware.htmlunit.util.Cookie.toHttpClient(cookieManager.getCookies());
    List<org.apache.http.cookie.Cookie> matches = new ArrayList();
    
    if (all.size() > 0) {
      CookieOrigin cookieOrigin = new CookieOrigin(host, port, path, secure);
      CookieSpec cookieSpec = new HtmlUnitBrowserCompatCookieSpec(getBrowserVersion());
      for (org.apache.http.cookie.Cookie cookie : all) {
        if (cookieSpec.match(cookie, cookieOrigin)) {
          matches.add(cookie);
        }
      }
    }
    
    Set<com.gargoylesoftware.htmlunit.util.Cookie> cookies = new LinkedHashSet();
    cookies.addAll(com.gargoylesoftware.htmlunit.util.Cookie.fromHttpClient(matches));
    return Collections.unmodifiableSet(cookies);
  }
  





  public void addCookie(String cookieString, URL pageUrl, Object origin)
  {
    BrowserVersion browserVersion = getBrowserVersion();
    CookieManager cookieManager = getCookieManager();
    if (cookieManager.isCookiesEnabled()) {
      CharArrayBuffer buffer = new CharArrayBuffer(cookieString.length() + 22);
      buffer.append("Set-Cookie: ");
      buffer.append(cookieString);
      
      CookieSpec cookieSpec = new HtmlUnitBrowserCompatCookieSpec(browserVersion);
      try
      {
        List<org.apache.http.cookie.Cookie> cookies = 
          cookieSpec.parse(new BufferedHeader(buffer), cookieManager.buildCookieOrigin(pageUrl));
        
        for (org.apache.http.cookie.Cookie cookie : cookies) {
          com.gargoylesoftware.htmlunit.util.Cookie htmlUnitCookie = new com.gargoylesoftware.htmlunit.util.Cookie((ClientCookie)cookie);
          cookieManager.addCookie(htmlUnitCookie);
          
          if (LOG.isDebugEnabled()) {
            LOG.debug("Added cookie: '" + cookieString + "'");
          }
        }
      }
      catch (MalformedCookieException e) {
        getIncorrectnessListener().notify("set-cookie http-equiv meta tag: invalid cookie '" + 
          cookieString + "'; reason: '" + e.getMessage() + "'.", origin);
      }
    }
    else if (LOG.isDebugEnabled()) {
      LOG.debug("Skipped adding cookie: '" + cookieString + "'");
    }
  }
}
