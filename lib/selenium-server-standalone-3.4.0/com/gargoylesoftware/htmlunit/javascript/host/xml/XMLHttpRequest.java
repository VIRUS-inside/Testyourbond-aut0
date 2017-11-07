package com.gargoylesoftware.htmlunit.javascript.host.xml;

import com.gargoylesoftware.htmlunit.AjaxController;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.FormEncodingType;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.background.BackgroundJavaScriptFactory;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJob;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventListenersContainer;
import com.gargoylesoftware.htmlunit.javascript.host.event.ProgressEvent;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import com.gargoylesoftware.htmlunit.util.EncodingSniffer;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.util.WebResponseWrapper;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.ContextFactory;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.auth.UsernamePasswordCredentials;









































@JsxClass
public class XMLHttpRequest
  extends XMLHttpRequestEventTarget
{
  private static final Log LOG = LogFactory.getLog(XMLHttpRequest.class);
  
  @JsxConstant
  public static final int UNSENT = 0;
  
  @JsxConstant
  public static final int OPENED = 1;
  
  @JsxConstant
  public static final int HEADERS_RECEIVED = 2;
  
  @JsxConstant
  public static final int LOADING = 3;
  
  @JsxConstant
  public static final int DONE = 4;
  
  private static final String HEADER_ORIGIN = "Origin";
  
  private static final String HEADER_ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
  
  private static final String HEADER_ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";
  
  private static final String HEADER_ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
  
  private static final String HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
  private static final String HEADER_ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
  private static final String ALLOW_ORIGIN_ALL = "*";
  private static final String[] ALL_PROPERTIES_ = { "onreadystatechange", "readyState", "responseText", "responseXML", 
    "status", "statusText", "abort", "getAllResponseHeaders", "getResponseHeader", "open", "send", 
    "setRequestHeader" };
  


  private static Collection<String> PROHIBITED_HEADERS_ = Arrays.asList(new String[] { "accept-charset", "accept-encoding", "connection", "content-length", "cookie", "cookie2", "content-transfer-encoding", "date", "expect", "host", "keep-alive", "referer", "te", "trailer", "transfer-encoding", "upgrade", "user-agent", "via" });
  
  private int state_;
  
  private Function stateChangeHandler_;
  
  private Function loadHandler_;
  private Function errorHandler_;
  private WebRequest webRequest_;
  private boolean async_;
  private int jobID_;
  private WebResponse webResponse_;
  private String overriddenMimeType_;
  private HtmlPage containingPage_;
  private final boolean caseSensitiveProperties_;
  private boolean withCredentials_;
  
  @JsxConstructor
  public XMLHttpRequest()
  {
    this(true);
  }
  



  public XMLHttpRequest(boolean caseSensitiveProperties)
  {
    caseSensitiveProperties_ = caseSensitiveProperties;
    state_ = 0;
  }
  



  @JsxGetter
  public Function getOnreadystatechange()
  {
    return stateChangeHandler_;
  }
  



  @JsxSetter
  public void setOnreadystatechange(Function stateChangeHandler)
  {
    stateChangeHandler_ = stateChangeHandler;
    if (state_ == 1) {
      setState(state_, null);
    }
  }
  





  private void setState(int state, Context context)
  {
    state_ = state;
    
    BrowserVersion browser = getBrowserVersion();
    if ((stateChangeHandler_ != null) && ((async_) || (state == 4))) {
      Scriptable scope = stateChangeHandler_.getParentScope();
      JavaScriptEngine jsEngine = containingPage_.getWebClient().getJavaScriptEngine();
      
      if (LOG.isDebugEnabled()) {
        LOG.debug("Calling onreadystatechange handler for state " + state);
      }
      Object[] params = { new Event(this, "readystatechange") };
      jsEngine.callFunction(containingPage_, stateChangeHandler_, scope, this, params);
      if (LOG.isDebugEnabled()) {
        if (context == null) {
          context = Context.getCurrentContext();
        }
        LOG.debug("onreadystatechange handler: " + context.decompileFunction(stateChangeHandler_, 4));
        LOG.debug("Calling onreadystatechange handler for state " + state + ". Done.");
      }
    }
    
    if (state == 4) {
      JavaScriptEngine jsEngine = containingPage_.getWebClient().getJavaScriptEngine();
      
      ProgressEvent event = new ProgressEvent(this, "load");
      Object[] params = { event };
      boolean lengthComputable = browser.hasFeature(BrowserVersionFeatures.XHR_LENGTH_COMPUTABLE);
      if (lengthComputable) {
        event.setLengthComputable(true);
      }
      
      if (webResponse_ != null) {
        long contentLength = webResponse_.getContentLength();
        event.setLoaded(contentLength);
        if (lengthComputable) {
          event.setTotal(contentLength);
        }
      }
      
      if (loadHandler_ != null) {
        jsEngine.callFunction(containingPage_, loadHandler_, loadHandler_.getParentScope(), this, params);
      }
      
      List<Scriptable> handlers = getEventListenersContainer().getListeners("load", false);
      if (handlers != null) {
        for (Scriptable scriptable : handlers) {
          if ((scriptable instanceof Function)) {
            Function function = (Function)scriptable;
            jsEngine.callFunction(containingPage_, function, function.getParentScope(), this, params);
          }
        }
      }
      
      handlers = getEventListenersContainer().getListeners("load", true);
      if (handlers != null) {
        for (Scriptable scriptable : handlers) {
          if ((scriptable instanceof Function)) {
            Function function = (Function)scriptable;
            jsEngine.callFunction(containingPage_, function, function.getParentScope(), this, params);
          }
        }
      }
    }
  }
  



  @JsxGetter
  public Function getOnload()
  {
    return loadHandler_;
  }
  



  @JsxSetter
  public void setOnload(Function loadHandler)
  {
    loadHandler_ = loadHandler;
  }
  



  @JsxGetter
  public Function getOnerror()
  {
    return errorHandler_;
  }
  



  @JsxSetter
  public void setOnerror(Function errorHandler)
  {
    errorHandler_ = errorHandler;
  }
  




  private void processError(Context context)
  {
    if (errorHandler_ != null) {
      Scriptable scope = errorHandler_.getParentScope();
      JavaScriptEngine jsEngine = containingPage_.getWebClient().getJavaScriptEngine();
      
      Object[] params = { new ProgressEvent(this, "error") };
      
      if (LOG.isDebugEnabled()) {
        LOG.debug("Calling onerror handler");
      }
      jsEngine.callFunction(containingPage_, errorHandler_, this, scope, params);
      if (LOG.isDebugEnabled()) {
        if (context == null) {
          context = Context.getCurrentContext();
        }
        LOG.debug("onerror handler: " + context.decompileFunction(errorHandler_, 4));
        LOG.debug("Calling onerror handler done.");
      }
    }
  }
  










  @JsxGetter
  public int getReadyState()
  {
    return state_;
  }
  



  @JsxGetter
  public String getResponseText()
  {
    if ((state_ == 0) || (state_ == 1)) {
      return "";
    }
    if (webResponse_ != null) {
      Charset encoding = webResponse_.getContentCharset();
      if (encoding == null) {
        return "";
      }
      String content = webResponse_.getContentAsString(encoding);
      if (content == null) {
        return "";
      }
      return content;
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("XMLHttpRequest.responseText was retrieved before the response was available.");
    }
    return "";
  }
  



  @JsxGetter
  public Object getResponseXML()
  {
    if (webResponse_ == null) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("XMLHttpRequest.responseXML returns null because there in no web resonse so far (has send() been called?)");
      }
      
      return null;
    }
    if ((webResponse_ instanceof NetworkErrorWebResponse)) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("XMLHttpRequest.responseXML returns of an network error (" + 
          ((NetworkErrorWebResponse)webResponse_).getError() + ")");
      }
      return null;
    }
    String contentType = webResponse_.getContentType();
    if ((contentType.isEmpty()) || (contentType.contains("xml"))) {
      WebWindow webWindow = getWindow().getWebWindow();
      try {
        XmlPage page = new XmlPage(webResponse_, webWindow);
        XMLDocument document = new XMLDocument();
        document.setPrototype(getPrototype(document.getClass()));
        document.setParentScope(getWindow());
        document.setDomNode(page);
        return document;
      }
      catch (IOException e) {
        LOG.warn("Failed parsing XML document " + webResponse_.getWebRequest().getUrl() + ": " + 
          e.getMessage());
        return null;
      }
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("XMLHttpRequest.responseXML was called but the response is " + 
        webResponse_.getContentType());
    }
    return null;
  }
  




  @JsxGetter
  public int getStatus()
  {
    if ((state_ == 0) || (state_ == 1)) {
      return 0;
    }
    if (webResponse_ != null) {
      return webResponse_.getStatusCode();
    }
    
    LOG.error("XMLHttpRequest.status was retrieved without a response available (readyState: " + 
      state_ + ").");
    return 0;
  }
  



  @JsxGetter
  public String getStatusText()
  {
    if ((state_ == 0) || (state_ == 1)) {
      return "";
    }
    if (webResponse_ != null) {
      return webResponse_.getStatusMessage();
    }
    
    LOG.error("XMLHttpRequest.statusText was retrieved without a response available (readyState: " + 
      state_ + ").");
    return null;
  }
  


  @JsxFunction
  public void abort()
  {
    getWindow().getWebWindow().getJobManager().stopJob(jobID_);
  }
  



  @JsxFunction
  public String getAllResponseHeaders()
  {
    if ((state_ == 0) || (state_ == 1)) {
      return "";
    }
    if (webResponse_ != null) {
      StringBuilder builder = new StringBuilder();
      for (NameValuePair header : webResponse_.getResponseHeaders()) {
        builder.append(header.getName()).append(": ").append(header.getValue()).append("\r\n");
      }
      if (getBrowserVersion().hasFeature(BrowserVersionFeatures.XHR_ALL_RESPONSE_HEADERS_APPEND_CRLF)) {
        builder.append("\r\n");
      }
      return builder.toString();
    }
    
    LOG.error("XMLHttpRequest.getAllResponseHeaders() was called without a response available (readyState: " + 
      state_ + ").");
    return null;
  }
  




  @JsxFunction
  public String getResponseHeader(String headerName)
  {
    if ((state_ == 0) || (state_ == 1)) {
      return null;
    }
    if (webResponse_ != null) {
      return webResponse_.getResponseHeaderValue(headerName);
    }
    
    LOG.error("XMLHttpRequest.getAllResponseHeaders(..) was called without a response available (readyState: " + 
      state_ + ").");
    return null;
  }
  








  @JsxFunction
  public void open(String method, Object urlParam, Object asyncParam, Object user, Object password)
  {
    if (((urlParam == null) || ("".equals(urlParam))) && (!getBrowserVersion().hasFeature(BrowserVersionFeatures.XHR_OPEN_ALLOW_EMTPY_URL))) {
      throw Context.reportRuntimeError("URL for XHR.open can't be empty!");
    }
    

    boolean async = true;
    if (asyncParam != Undefined.instance) {
      async = ScriptRuntime.toBoolean(asyncParam);
    }
    
    if ((!async) && 
      (getWithCredentials()) && 
      (getBrowserVersion().hasFeature(BrowserVersionFeatures.XHR_OPEN_WITHCREDENTIALS_TRUE_IN_SYNC_EXCEPTION))) {
      throw Context.reportRuntimeError(
        "open() in sync mode is not possible because 'withCredentials' is set to true");
    }
    
    String url = Context.toString(urlParam);
    

    containingPage_ = ((HtmlPage)getWindow().getWebWindow().getEnclosedPage());
    try
    {
      URL fullUrl = containingPage_.getFullyQualifiedUrl(url);
      URL originUrl = containingPage_.getFullyQualifiedUrl("");
      if (!isAllowCrossDomainsFor(fullUrl)) {
        throw Context.reportRuntimeError("Access to restricted URI denied");
      }
      
      WebRequest request = new WebRequest(fullUrl, getBrowserVersion().getXmlHttpRequestAcceptHeader());
      request.setCharset(StandardCharsets.UTF_8);
      request.setAdditionalHeader("Referer", containingPage_.getUrl().toExternalForm());
      
      if (!isSameOrigin(originUrl, fullUrl)) {
        StringBuilder origin = new StringBuilder().append(originUrl.getProtocol()).append("://")
          .append(originUrl.getHost());
        if (originUrl.getPort() != -1) {
          origin.append(':').append(originUrl.getPort());
        }
        request.setAdditionalHeader("Origin", origin.toString());
      }
      try
      {
        request.setHttpMethod(HttpMethod.valueOf(method.toUpperCase(Locale.ROOT)));
      }
      catch (IllegalArgumentException e) {
        LOG.info("Incorrect HTTP Method '" + method + "'");
        return;
      }
      

      if ((user != null) && (user != Undefined.instance)) {
        String userCred = user.toString();
        
        String passwordCred = "";
        if ((password != null) && (password != Undefined.instance)) {
          passwordCred = password.toString();
        }
        
        request.setCredentials(new UsernamePasswordCredentials(userCred, passwordCred));
      }
      webRequest_ = request;
    }
    catch (MalformedURLException e) {
      LOG.error("Unable to initialize XMLHttpRequest using malformed URL '" + url + "'.");
      return;
    }
    
    async_ = async;
    
    setState(1, null);
  }
  
  private boolean isAllowCrossDomainsFor(URL newUrl) {
    BrowserVersion browser = getBrowserVersion();
    if ((browser.hasFeature(BrowserVersionFeatures.XHR_NO_CROSS_ORIGIN_TO_ABOUT)) && 
      ("about".equals(newUrl.getProtocol()))) {
      return false;
    }
    
    return true;
  }
  
  private boolean isSameOrigin(URL originUrl, URL newUrl) {
    if (!originUrl.getHost().equals(newUrl.getHost())) {
      return false;
    }
    
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.XHR_IGNORE_PORT_FOR_SAME_ORIGIN)) {
      return true;
    }
    
    int originPort = originUrl.getPort();
    if (originPort == -1) {
      originPort = originUrl.getDefaultPort();
    }
    int newPort = newUrl.getPort();
    if (newPort == -1) {
      newPort = newUrl.getDefaultPort();
    }
    return originPort == newPort;
  }
  



  @JsxFunction
  public void send(Object content)
  {
    if (webRequest_ == null) {
      return;
    }
    prepareRequest(content);
    
    WebClient client = getWindow().getWebWindow().getWebClient();
    AjaxController ajaxController = client.getAjaxController();
    HtmlPage page = (HtmlPage)getWindow().getWebWindow().getEnclosedPage();
    boolean synchron = ajaxController.processSynchron(page, webRequest_, async_);
    if (synchron) {
      doSend(Context.getCurrentContext());
    }
    else {
      if (getBrowserVersion().hasFeature(BrowserVersionFeatures.XHR_FIRE_STATE_OPENED_AGAIN_IN_ASYNC_MODE))
      {

        setState(1, Context.getCurrentContext());
      }
      

      final Scriptable startingScope = getWindow();
      ContextFactory cf = client.getJavaScriptEngine().getContextFactory();
      ContextAction action = new ContextAction()
      {

        public Object run(Context cx)
        {
          Stack<Scriptable> stack = 
            (Stack)cx.getThreadLocal("startingScope");
          if (stack == null) {
            stack = new Stack();
            cx.putThreadLocal("startingScope", stack);
          }
          stack.push(startingScope);
          try
          {
            XMLHttpRequest.this.doSend(cx);
          }
          finally {
            stack.pop();
          }
          return null;
        }
        
        public String toString()
        {
          return 
          
            "XMLHttpRequest " + webRequest_.getHttpMethod().toString() + " '" + webRequest_.getUrl().toExternalForm() + "'";
        }
      };
      JavaScriptJob job = BackgroundJavaScriptFactory.theFactory()
        .createJavascriptXMLHttpRequestJob(cf, action);
      if (LOG.isDebugEnabled()) {
        LOG.debug("Starting XMLHttpRequest thread for asynchronous request");
      }
      jobID_ = getWindow().getWebWindow().getJobManager().addJob(job, page);
    }
  }
  



  private void prepareRequest(Object content)
  {
    if ((content != null) && 
      ((HttpMethod.POST == webRequest_.getHttpMethod()) || 
      (HttpMethod.PUT == webRequest_.getHttpMethod()) || 
      (HttpMethod.PATCH == webRequest_.getHttpMethod())) && 
      (!Undefined.instance.equals(content))) {
      if ((content instanceof FormData)) {
        ((FormData)content).fillRequest(webRequest_);
      }
      else {
        String body = Context.toString(content);
        if (!body.isEmpty()) {
          if (LOG.isDebugEnabled()) {
            LOG.debug("Setting request body to: " + body);
          }
          webRequest_.setRequestBody(body);
        }
      }
    }
  }
  



  private void doSend(Context context)
  {
    WebClient wc = getWindow().getWebWindow().getWebClient();
    try {
      String originHeaderValue = (String)webRequest_.getAdditionalHeaders().get("Origin");
      if ((originHeaderValue != null) && (isPreflight())) {
        WebRequest preflightRequest = new WebRequest(webRequest_.getUrl(), HttpMethod.OPTIONS);
        

        preflightRequest.setAdditionalHeader("Origin", originHeaderValue);
        

        preflightRequest.setAdditionalHeader(
          "Access-Control-Request-Method", 
          webRequest_.getHttpMethod().name());
        

        StringBuilder builder = new StringBuilder();
        
        Iterator localIterator = new TreeMap(webRequest_.getAdditionalHeaders()).entrySet().iterator();
        while (localIterator.hasNext()) {
          Map.Entry<String, String> header = (Map.Entry)localIterator.next();
          String name = ((String)header.getKey()).toLowerCase(Locale.ROOT);
          if (isPreflightHeader(name, (String)header.getValue())) {
            if (builder.length() != 0) {
              builder.append(',');
            }
            builder.append(name);
          }
        }
        preflightRequest.setAdditionalHeader("Access-Control-Request-Headers", builder.toString());
        

        WebResponse preflightResponse = wc.loadWebResponse(preflightRequest);
        if (!isPreflightAuthorized(preflightResponse)) {
          setState(2, context);
          setState(3, context);
          setState(4, context);
          if (LOG.isDebugEnabled()) {
            LOG.debug("No permitted request for URL " + webRequest_.getUrl());
          }
          Context.throwAsScriptRuntimeEx(
            new RuntimeException("No permitted \"Access-Control-Allow-Origin\" header."));
          return;
        }
      }
      WebResponse webResponse = wc.loadWebResponse(webRequest_);
      if (LOG.isDebugEnabled()) {
        LOG.debug("Web response loaded successfully.");
      }
      boolean allowOriginResponse = true;
      if (originHeaderValue != null) {
        String value = webResponse.getResponseHeaderValue("Access-Control-Allow-Origin");
        allowOriginResponse = originHeaderValue.equals(value);
        if (getWithCredentials()) {
          allowOriginResponse = (allowOriginResponse) || (
            (getBrowserVersion().hasFeature(BrowserVersionFeatures.XHR_WITHCREDENTIALS_ALLOW_ORIGIN_ALL)) && 
            ("*".equals(value)));
          

          value = webResponse.getResponseHeaderValue("Access-Control-Allow-Credentials");
          allowOriginResponse = (allowOriginResponse) && (Boolean.parseBoolean(value));
        }
        else {
          allowOriginResponse = (allowOriginResponse) || ("*".equals(value));
        }
      }
      if (allowOriginResponse) {
        if (overriddenMimeType_ == null) {
          webResponse_ = webResponse;
        }
        else {
          int index = overriddenMimeType_.toLowerCase(Locale.ROOT).indexOf("charset=");
          String charsetName = "";
          if (index != -1) {
            charsetName = overriddenMimeType_.substring(index + "charset=".length());
          }
          Charset charset = EncodingSniffer.toCharset(charsetName);
          if ((charset == null) && 
            (getBrowserVersion().hasFeature(BrowserVersionFeatures.XHR_USE_DEFAULT_CHARSET_FROM_PAGE))) {
            HTMLDocument doc = (HTMLDocument)containingPage_.getScriptableObject();
            charset = Charset.forName(doc.getDefaultCharset());
          }
          final String charsetNameFinal = charsetName;
          final Charset charsetFinal = charset;
          webResponse_ = new WebResponseWrapper(webResponse)
          {
            public String getContentType() {
              return overriddenMimeType_;
            }
            
            public Charset getContentCharset() {
              if ((charsetNameFinal.isEmpty()) || (
                (charsetFinal == null) && 
                (getBrowserVersion().hasFeature(BrowserVersionFeatures.XHR_OPEN_WITHCREDENTIALS_TRUE_IN_SYNC_EXCEPTION)))) {
                return super.getContentCharset();
              }
              return charsetFinal;
            }
          };
        }
      }
      if (allowOriginResponse) {
        setState(2, context);
        setState(3, context);
        setState(4, context);
      }
      else {
        if (LOG.isDebugEnabled()) {
          LOG.debug("No permitted \"Access-Control-Allow-Origin\" header for URL " + webRequest_.getUrl());
        }
        throw new IOException("No permitted \"Access-Control-Allow-Origin\" header.");
      }
    }
    catch (IOException e) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("IOException: returning a network error response.", e);
      }
      webResponse_ = new NetworkErrorWebResponse(webRequest_, e, null);
      setState(2, context);
      setState(4, context);
      if (async_) {
        processError(context);
      }
      else {
        Context.throwAsScriptRuntimeEx(e);
      }
    }
  }
  
  private boolean isPreflight() {
    HttpMethod method = webRequest_.getHttpMethod();
    if ((method != HttpMethod.GET) && (method != HttpMethod.HEAD) && (method != HttpMethod.POST)) {
      return true;
    }
    for (Map.Entry<String, String> header : webRequest_.getAdditionalHeaders().entrySet()) {
      if (isPreflightHeader(((String)header.getKey()).toLowerCase(Locale.ROOT), (String)header.getValue())) {
        return true;
      }
    }
    return false;
  }
  
  private boolean isPreflightAuthorized(WebResponse preflightResponse) {
    String originHeader = preflightResponse.getResponseHeaderValue("Access-Control-Allow-Origin");
    if ((!"*".equals(originHeader)) && 
      (!((String)webRequest_.getAdditionalHeaders().get("Origin")).equals(originHeader))) {
      return false;
    }
    String headersHeader = preflightResponse.getResponseHeaderValue("Access-Control-Allow-Headers");
    if (headersHeader == null) {
      headersHeader = "";
    }
    else {
      headersHeader = headersHeader.toLowerCase(Locale.ROOT);
    }
    for (Map.Entry<String, String> header : webRequest_.getAdditionalHeaders().entrySet()) {
      String key = ((String)header.getKey()).toLowerCase(Locale.ROOT);
      if ((isPreflightHeader(key, (String)header.getValue())) && 
        (!headersHeader.contains(key))) {
        return false;
      }
    }
    return true;
  }
  



  private static boolean isPreflightHeader(String name, String value)
  {
    if ("content-type".equals(name)) {
      String lcValue = value.toLowerCase(Locale.ROOT);
      if ((lcValue.startsWith(FormEncodingType.URL_ENCODED.getName())) || 
        (lcValue.startsWith(FormEncodingType.MULTIPART.getName())) || 
        (lcValue.startsWith("text/plain"))) {
        return false;
      }
      return true;
    }
    if (("accept".equals(name)) || ("accept-language".equals(name)) || ("content-language".equals(name)) || 
      ("referer".equals(name)) || ("accept-encoding".equals(name)) || ("origin".equals(name))) {
      return false;
    }
    return true;
  }
  





  @JsxFunction
  public void setRequestHeader(String name, String value)
  {
    if (!isAuthorizedHeader(name)) {
      LOG.warn("Ignoring XMLHttpRequest.setRequestHeader for " + name + 
        ": it is a restricted header");
      return;
    }
    
    if (webRequest_ != null) {
      webRequest_.setAdditionalHeader(name, value);
    }
    else {
      throw Context.reportRuntimeError("The open() method must be called before setRequestHeader().");
    }
  }
  





  static boolean isAuthorizedHeader(String name)
  {
    String nameLowerCase = name.toLowerCase(Locale.ROOT);
    if (PROHIBITED_HEADERS_.contains(nameLowerCase)) {
      return false;
    }
    if ((nameLowerCase.startsWith("proxy-")) || (nameLowerCase.startsWith("sec-"))) {
      return false;
    }
    return true;
  }
  






  @JsxFunction
  public void overrideMimeType(String mimeType)
  {
    if ((getBrowserVersion().hasFeature(BrowserVersionFeatures.XHR_OVERRIDE_MIME_TYPE_BEFORE_SEND)) && 
      (state_ != 0) && (state_ != 1)) {
      throw Context.reportRuntimeError("Property 'overrideMimeType' not writable after sent.");
    }
    overriddenMimeType_ = mimeType;
  }
  



  @JsxGetter
  public boolean getWithCredentials()
  {
    return withCredentials_;
  }
  



  @JsxSetter
  public void setWithCredentials(boolean withCredentials)
  {
    if ((!async_) && (state_ != 0) && 
      (getBrowserVersion().hasFeature(BrowserVersionFeatures.XHR_WITHCREDENTIALS_NOT_WRITEABLE_IN_SYNC_EXCEPTION))) {
      throw Context.reportRuntimeError("Property 'withCredentials' not writable in sync mode.");
    }
    
    withCredentials_ = withCredentials;
  }
  



  public Object get(String name, Scriptable start)
  {
    if (!caseSensitiveProperties_) {
      for (String property : ALL_PROPERTIES_) {
        if (property.equalsIgnoreCase(name)) {
          name = property;
          break;
        }
      }
    }
    return super.get(name, start);
  }
  



  public void put(String name, Scriptable start, Object value)
  {
    if (!caseSensitiveProperties_) {
      for (String property : ALL_PROPERTIES_) {
        if (property.equalsIgnoreCase(name)) {
          name = property;
          break;
        }
      }
    }
    super.put(name, start, value);
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public XMLHttpRequestUpload getUpload()
  {
    XMLHttpRequestUpload upload = new XMLHttpRequestUpload();
    upload.setParentScope(getParentScope());
    upload.setPrototype(getPrototype(upload.getClass()));
    return upload;
  }
  



  @JsxGetter(value={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)}, propertyName="upload")
  public XMLHttpRequestEventTarget getUploadIE()
  {
    XMLHttpRequestEventTarget upload = new XMLHttpRequestEventTarget();
    upload.setParentScope(getParentScope());
    upload.setPrototype(getPrototype(upload.getClass()));
    return upload;
  }
  
  private static final class NetworkErrorWebResponse extends WebResponse {
    private final WebRequest request_;
    private final IOException error_;
    
    private NetworkErrorWebResponse(WebRequest webRequest, IOException error) {
      super(null, 0L);
      request_ = webRequest;
      error_ = error;
    }
    
    public int getStatusCode()
    {
      return 0;
    }
    
    public String getStatusMessage()
    {
      return "";
    }
    
    public String getContentType()
    {
      return "";
    }
    
    public String getContentAsString()
    {
      return "";
    }
    
    public String getContentAsString(String encoding)
    {
      return "";
    }
    
    public String getContentAsString(String encoding, String defaultEncoding)
    {
      return "";
    }
    
    public InputStream getContentAsStream()
    {
      return null;
    }
    
    public List<NameValuePair> getResponseHeaders()
    {
      return Collections.emptyList();
    }
    
    public String getResponseHeaderValue(String headerName)
    {
      return "";
    }
    
    public long getLoadTime()
    {
      return 0L;
    }
    
    public Charset getContentCharset()
    {
      return null;
    }
    
    public Charset getContentCharsetOrNull()
    {
      return null;
    }
    
    public WebRequest getWebRequest()
    {
      return request_;
    }
    


    public IOException getError()
    {
      return error_;
    }
  }
}
