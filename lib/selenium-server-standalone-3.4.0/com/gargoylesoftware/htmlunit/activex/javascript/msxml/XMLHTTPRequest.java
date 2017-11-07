package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import com.gargoylesoftware.htmlunit.AjaxController;
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
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.xml.FormData;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.ContextFactory;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.auth.UsernamePasswordCredentials;






























@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
public class XMLHTTPRequest
  extends MSXMLScriptable
{
  private static final Log LOG = LogFactory.getLog(XMLHTTPRequest.class);
  
  public static final int STATE_UNSENT = 0;
  
  public static final int STATE_OPENED = 1;
  
  public static final int STATE_HEADERS_RECEIVED = 2;
  
  public static final int STATE_LOADING = 3;
  
  public static final int STATE_DONE = 4;
  
  private static final String HEADER_ORIGIN = "Origin";
  
  private static final char REQUEST_HEADERS_SEPARATOR = ',';
  
  private static final String HEADER_ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
  
  private static final String HEADER_ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";
  
  private static final String HEADER_ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
  private static final String HEADER_ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
  private static final String ALLOW_ORIGIN_ALL = "*";
  private static final String[] ALL_PROPERTIES_ = { "onreadystatechange", "readyState", "responseText", "responseXML", 
    "status", "statusText", "abort", "getAllResponseHeaders", "getResponseHeader", "open", "send", 
    "setRequestHeader" };
  


  private static Collection<String> PROHIBITED_HEADERS_ = Arrays.asList(new String[] { "accept-charset", "accept-encoding", "connection", "content-length", "cookie", "cookie2", "content-transfer-encoding", "date", "expect", "host", "keep-alive", "referer", "te", "trailer", "transfer-encoding", "upgrade", "user-agent", "via" });
  
  private int state_ = 0;
  
  private Function stateChangeHandler_;
  
  private WebRequest webRequest_;
  
  private boolean async_;
  
  private int jobID_;
  
  private WebResponse webResponse_;
  
  private HtmlPage containingPage_;
  
  private boolean openedMultipleTimes_;
  private boolean sent_;
  
  @JsxConstructor
  public XMLHTTPRequest() {}
  
  @JsxGetter
  public Object getOnreadystatechange()
  {
    if (stateChangeHandler_ == null) {
      return Undefined.instance;
    }
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
    
    if ((stateChangeHandler_ != null) && (!openedMultipleTimes_)) {
      Scriptable scope = stateChangeHandler_.getParentScope();
      JavaScriptEngine jsEngine = containingPage_.getWebClient().getJavaScriptEngine();
      
      if (LOG.isDebugEnabled()) {
        LOG.debug("Calling onreadystatechange handler for state " + state);
      }
      Object[] params = ArrayUtils.EMPTY_OBJECT_ARRAY;
      
      jsEngine.callFunction(containingPage_, stateChangeHandler_, scope, this, params);
      if (LOG.isDebugEnabled()) {
        if (context == null) {
          context = Context.getCurrentContext();
        }
        LOG.debug("onreadystatechange handler: " + context.decompileFunction(stateChangeHandler_, 4));
        LOG.debug("Calling onreadystatechange handler for state " + state + ". Done.");
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
    if (state_ == 0) {
      throw Context.reportRuntimeError(
        "The data necessary to complete this operation is not yet available (request not opened).");
    }
    if (state_ != 4) {
      throw Context.reportRuntimeError("Unspecified error (request not sent).");
    }
    if (webResponse_ != null) {
      String content = webResponse_.getContentAsString();
      if (content == null) {
        return "";
      }
      return content;
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("XMLHTTPRequest.responseText was retrieved before the response was available.");
    }
    return "";
  }
  



  @JsxGetter
  public Object getResponseXML()
  {
    if (state_ == 0) {
      throw Context.reportRuntimeError("Unspecified error (request not opened).");
    }
    if ((state_ == 4) && (webResponse_ != null) && (!(webResponse_ instanceof NetworkErrorWebResponse))) {
      String contentType = webResponse_.getContentType();
      if (contentType.contains("xml")) {
        try {
          XmlPage page = new XmlPage(webResponse_, getWindow().getWebWindow(), true, false);
          XMLDOMDocument doc = new XMLDOMDocument();
          doc.setDomNode(page);
          doc.setPrototype(getPrototype(doc.getClass()));
          doc.setEnvironment(getEnvironment());
          doc.setParentScope(getWindow());
          return doc;
        }
        catch (IOException e) {
          LOG.warn("Failed parsing XML document " + webResponse_.getWebRequest().getUrl() + ": " + 
            e.getMessage());
          return null;
        }
      }
    }
    XMLDOMDocument doc = new XMLDOMDocument(getWindow().getWebWindow());
    doc.setPrototype(getPrototype(doc.getClass()));
    doc.setEnvironment(getEnvironment());
    return doc;
  }
  



  @JsxGetter
  public int getStatus()
  {
    if (state_ != 4) {
      throw Context.reportRuntimeError("Unspecified error (request not sent).");
    }
    if (webResponse_ != null) {
      return webResponse_.getStatusCode();
    }
    
    LOG.error("XMLHTTPRequest.status was retrieved without a response available (readyState: " + 
      state_ + ").");
    return 0;
  }
  



  @JsxGetter
  public String getStatusText()
  {
    if (state_ != 4) {
      throw Context.reportRuntimeError("Unspecified error (request not sent).");
    }
    if (webResponse_ != null) {
      return webResponse_.getStatusMessage();
    }
    
    LOG.error("XMLHTTPRequest.statusText was retrieved without a response available (readyState: " + 
      state_ + ").");
    return null;
  }
  


  @JsxFunction
  public void abort()
  {
    getWindow().getWebWindow().getJobManager().stopJob(jobID_);
    setState(0, Context.getCurrentContext());
  }
  



  @JsxFunction
  public String getAllResponseHeaders()
  {
    if ((state_ == 0) || (state_ == 1)) {
      throw Context.reportRuntimeError("Unspecified error (request not sent).");
    }
    if (webResponse_ != null) {
      StringBuilder builder = new StringBuilder();
      for (NameValuePair header : webResponse_.getResponseHeaders()) {
        builder.append(header.getName()).append(": ").append(header.getValue()).append("\r\n");
      }
      return "\r\n";
    }
    
    LOG.error("XMLHTTPRequest.getAllResponseHeaders() was called without a response available (readyState: " + 
      state_ + ").");
    return null;
  }
  




  @JsxFunction
  public String getResponseHeader(String header)
  {
    if ((state_ == 0) || (state_ == 1)) {
      throw Context.reportRuntimeError("Unspecified error (request not sent).");
    }
    if ((header == null) || ("null".equals(header))) {
      throw Context.reportRuntimeError("Type mismatch (header is null).");
    }
    if ("".equals(header)) {
      throw Context.reportRuntimeError("Invalid argument (header is empty).");
    }
    if (webResponse_ != null) {
      String value = webResponse_.getResponseHeaderValue(header);
      if (value == null) {
        return "";
      }
      return value;
    }
    
    LOG.error("XMLHTTPRequest.getResponseHeader(..) was called without a response available (readyState: " + 
      state_ + ").");
    return null;
  }
  











  @JsxFunction
  public void open(String method, Object url, Object asyncParam, Object user, Object password)
  {
    if ((method == null) || ("null".equals(method))) {
      throw Context.reportRuntimeError("Type mismatch (method is null).");
    }
    if ((url == null) || ("null".equals(url))) {
      throw Context.reportRuntimeError("Type mismatch (url is null).");
    }
    state_ = 0;
    openedMultipleTimes_ = (webRequest_ != null);
    sent_ = false;
    webRequest_ = null;
    webResponse_ = null;
    if (("".equals(method)) || ("TRACE".equalsIgnoreCase(method))) {
      throw Context.reportRuntimeError("Invalid procedure call or argument (method is invalid).");
    }
    if ("".equals(url)) {
      throw Context.reportRuntimeError("Invalid procedure call or argument (url is empty).");
    }
    

    boolean async = true;
    if (asyncParam != Undefined.instance) {
      async = ScriptRuntime.toBoolean(asyncParam);
    }
    
    String urlAsString = Context.toString(url);
    

    containingPage_ = ((HtmlPage)getWindow().getWebWindow().getEnclosedPage());
    try
    {
      URL fullUrl = containingPage_.getFullyQualifiedUrl(urlAsString);
      
      WebRequest request = new WebRequest(fullUrl);
      request.setCharset(StandardCharsets.UTF_8);
      request.setAdditionalHeader("Referer", containingPage_.getUrl().toExternalForm());
      
      request.setHttpMethod(HttpMethod.valueOf(method.toUpperCase(Locale.ROOT)));
      

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
      LOG.error("Unable to initialize XMLHTTPRequest using malformed URL '" + urlAsString + "'.");
      return;
    }
    catch (IllegalArgumentException e) {
      LOG.error("Unable to initialize XMLHTTPRequest using illegal argument '" + e.getMessage() + "'.");
      webRequest_ = null;
    }
    
    async_ = async;
    
    setState(1, null);
  }
  



  @JsxFunction
  public void send(Object body)
  {
    if (webRequest_ == null) {
      setState(4, Context.getCurrentContext());
      return;
    }
    if (sent_) {
      throw Context.reportRuntimeError("Unspecified error (request already sent).");
    }
    sent_ = true;
    
    prepareRequest(body);
    

    setState(1, Context.getCurrentContext());
    
    WebClient client = getWindow().getWebWindow().getWebClient();
    AjaxController ajaxController = client.getAjaxController();
    HtmlPage page = (HtmlPage)getWindow().getWebWindow().getEnclosedPage();
    boolean synchron = ajaxController.processSynchron(page, webRequest_, async_);
    if (synchron) {
      doSend(Context.getCurrentContext());
    }
    else
    {
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
            XMLHTTPRequest.this.doSend(cx);
          }
          finally {
            stack.pop();
          }
          return null;
        }
      };
      JavaScriptJob job = BackgroundJavaScriptFactory.theFactory()
        .createJavascriptXMLHttpRequestJob(cf, action);
      if (LOG.isDebugEnabled()) {
        LOG.debug("Starting XMLHTTPRequest thread for asynchronous request");
      }
      jobID_ = getWindow().getWebWindow().getJobManager().addJob(job, page);
    }
  }
  



  private void prepareRequest(Object content)
  {
    if ((content != null) && (!Undefined.instance.equals(content))) {
      if ((!"".equals(content)) && (HttpMethod.GET == webRequest_.getHttpMethod())) {
        webRequest_.setHttpMethod(HttpMethod.POST);
      }
      if ((HttpMethod.POST == webRequest_.getHttpMethod()) || 
        (HttpMethod.PUT == webRequest_.getHttpMethod()) || 
        (HttpMethod.PATCH == webRequest_.getHttpMethod())) {
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
        for (Map.Entry<String, String> header : webRequest_.getAdditionalHeaders().entrySet()) {
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
        allowOriginResponse = (allowOriginResponse) || ("*".equals(value));
      }
      if (allowOriginResponse) {
        webResponse_ = webResponse;
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
      webResponse_ = new NetworkErrorWebResponse(webRequest_, null);
      setState(2, context);
      setState(4, context);
      if (!async_) {
        throw Context.reportRuntimeError("Object not found.");
      }
    }
  }
  
  private boolean isPreflight() {
    HttpMethod method = webRequest_.getHttpMethod();
    if ((method != HttpMethod.GET) && (method != HttpMethod.HEAD) && (method != HttpMethod.POST)) {
      return true;
    }
    for (Map.Entry<String, String> header : webRequest_.getAdditionalHeaders().entrySet())
    {
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
    if ((name == null) || ("null".equals(name))) {
      throw Context.reportRuntimeError("Type mismatch (name is null).");
    }
    if ("".equals(name)) {
      throw Context.reportRuntimeError("Invalid argument (name is empty).");
    }
    if ((value == null) || ("null".equals(value))) {
      throw Context.reportRuntimeError("Type mismatch (value is null).");
    }
    if (StringUtils.isBlank(value)) {
      return;
    }
    if (!isAuthorizedHeader(name)) {
      LOG.warn("Ignoring XMLHTTPRequest.setRequestHeader for " + name + 
        ": it is a restricted header");
      return;
    }
    
    if (webRequest_ == null) {
      throw Context.reportRuntimeError("Unspecified error (request not opened).");
    }
    webRequest_.setAdditionalHeader(name, value);
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
  



  public Object get(String name, Scriptable start)
  {
    for (String property : ALL_PROPERTIES_) {
      if (property.equalsIgnoreCase(name)) {
        name = property;
        break;
      }
    }
    return super.get(name, start);
  }
  



  public void put(String name, Scriptable start, Object value)
  {
    for (String property : ALL_PROPERTIES_) {
      if (property.equalsIgnoreCase(name)) {
        name = property;
        break;
      }
    }
    super.put(name, start, value);
  }
  
  private static final class NetworkErrorWebResponse extends WebResponse {
    private final WebRequest request_;
    
    private NetworkErrorWebResponse(WebRequest webRequest) {
      super(null, 0L);
      request_ = webRequest;
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
  }
}
