package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.AlertHandler;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.ConfirmHandler;
import com.gargoylesoftware.htmlunit.DialogWindow;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.PromptHandler;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.StatusHandler;
import com.gargoylesoftware.htmlunit.StorageHolder;
import com.gargoylesoftware.htmlunit.StorageHolder.Type;
import com.gargoylesoftware.htmlunit.TopLevelWindow;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.WebWindowNotFoundException;
import com.gargoylesoftware.htmlunit.html.BaseFrameElement;
import com.gargoylesoftware.htmlunit.html.DomChangeEvent;
import com.gargoylesoftware.htmlunit.html.DomChangeListener;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlAttributeChangeEvent;
import com.gargoylesoftware.htmlunit.html.HtmlAttributeChangeListener;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlEmbed;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlFrame;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlMap;
import com.gargoylesoftware.htmlunit.html.HtmlObject;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlStyle;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.PostponedAction;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.background.BackgroundJavaScriptFactory;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJob;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;
import com.gargoylesoftware.htmlunit.javascript.configuration.CanSetReadOnly;
import com.gargoylesoftware.htmlunit.javascript.configuration.CanSetReadOnlyStatus;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.crypto.Crypto;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSS2Properties;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleSheet;
import com.gargoylesoftware.htmlunit.javascript.host.css.MediaQueryList;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleMedia;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleSheetList;
import com.gargoylesoftware.htmlunit.javascript.host.dom.AbstractList.EffectOnCache;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Document;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Selection;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventListenersContainer;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;
import com.gargoylesoftware.htmlunit.javascript.host.event.MessageEvent;
import com.gargoylesoftware.htmlunit.javascript.host.html.DataTransfer;
import com.gargoylesoftware.htmlunit.javascript.host.html.DocumentProxy;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBodyElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.gargoylesoftware.htmlunit.javascript.host.performance.Performance;
import com.gargoylesoftware.htmlunit.javascript.host.speech.SpeechSynthesis;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLDocument;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.WeakHashMap;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.ContextFactory;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.FunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;







































@JsxClass
public class Window
  extends EventTarget
  implements Function, AutoCloseable
{
  private static final Log LOG = LogFactory.getLog(Window.class);
  
  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public static final short TEMPORARY = 0;
  
  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public static final short PERSISTENT = 1;
  
  private static final int MIN_TIMER_DELAY = 1;
  
  private Document document_;
  
  private DocumentProxy documentProxy_;
  
  private Navigator navigator_;
  
  private WebWindow webWindow_;
  
  private WindowProxy windowProxy_;
  
  private Screen screen_;
  
  private History history_;
  
  private Location location_;
  private ScriptableObject console_;
  private ApplicationCache applicationCache_;
  private Selection selection_;
  private Event currentEvent_;
  private String status_ = "";
  private Map<Class<? extends Scriptable>, Scriptable> prototypes_ = new HashMap();
  private Map<String, Scriptable> prototypesPerJSName_ = new HashMap();
  private Object controllers_;
  private Object opener_;
  private Object top_ = NOT_FOUND;
  


  private Crypto crypto_;
  


  private transient WeakHashMap<Element, Map<String, CSS2Properties>> computedStyles_ = new WeakHashMap();
  
  private final Map<StorageHolder.Type, Storage> storages_ = new HashMap();
  







  public Window() {}
  






  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public static Scriptable jsConstructor(Context cx, Object[] args, Function ctorObj, boolean inNewExpr)
  {
    throw ScriptRuntime.typeError("Illegal constructor");
  }
  




  private void readObject(ObjectInputStream stream)
    throws IOException, ClassNotFoundException
  {
    stream.defaultReadObject();
    computedStyles_ = new WeakHashMap();
  }
  





  public Scriptable getPrototype(Class<? extends SimpleScriptable> jsClass)
  {
    return (Scriptable)prototypes_.get(jsClass);
  }
  




  public Scriptable getPrototype(String className)
  {
    return (Scriptable)prototypesPerJSName_.get(className);
  }
  





  public void setPrototypes(Map<Class<? extends Scriptable>, Scriptable> map, Map<String, Scriptable> prototypesPerJSName)
  {
    prototypes_ = map;
    prototypesPerJSName_ = prototypesPerJSName;
  }
  





  @JsxFunction
  public void alert(Object message)
  {
    String stringMessage = Context.toString(message);
    AlertHandler handler = getWebWindow().getWebClient().getAlertHandler();
    if (handler == null) {
      LOG.warn("window.alert(\"" + stringMessage + "\") no alert handler installed");
    }
    else {
      handler.handleAlert(document_.getPage(), stringMessage);
    }
  }
  




  @JsxFunction
  public String btoa(String stringToEncode)
  {
    return new String(Base64.encodeBase64(stringToEncode.getBytes()));
  }
  




  @JsxFunction
  public String atob(String encodedData)
  {
    return new String(Base64.decodeBase64(encodedData.getBytes()));
  }
  




  @JsxFunction
  public boolean confirm(String message)
  {
    ConfirmHandler handler = getWebWindow().getWebClient().getConfirmHandler();
    if (handler == null) {
      LOG.warn("window.confirm(\"" + 
        message + "\") no confirm handler installed, simulating the OK button");
      return true;
    }
    return handler.handleConfirm(document_.getPage(), message);
  }
  





  @JsxFunction
  public String prompt(String message, Object defaultValue)
  {
    PromptHandler handler = getWebWindow().getWebClient().getPromptHandler();
    if (handler == null) {
      LOG.warn("window.prompt(\"" + message + "\") no prompt handler installed");
      return null;
    }
    if (defaultValue == Undefined.instance) {
      defaultValue = null;
    }
    else {
      defaultValue = Context.toString(defaultValue);
    }
    return handler.handlePrompt(document_.getPage(), message, (String)defaultValue);
  }
  



  @JsxGetter(propertyName="document")
  public DocumentProxy getDocument_js()
  {
    return documentProxy_;
  }
  



  public Document getDocument()
  {
    return document_;
  }
  



  @JsxGetter
  public ApplicationCache getApplicationCache()
  {
    return applicationCache_;
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public Object getEvent()
  {
    return currentEvent_;
  }
  



  public Event getCurrentEvent()
  {
    return currentEvent_;
  }
  



  public void setCurrentEvent(Event event)
  {
    currentEvent_ = event;
  }
  












  @JsxFunction
  public WindowProxy open(Object url, Object name, Object features, Object replace)
  {
    String urlString = null;
    if (url != Undefined.instance) {
      urlString = Context.toString(url);
    }
    String windowName = "";
    if (name != Undefined.instance) {
      windowName = Context.toString(name);
    }
    String featuresString = null;
    if (features != Undefined.instance) {
      featuresString = Context.toString(features);
    }
    WebClient webClient = getWebWindow().getWebClient();
    
    if (webClient.getOptions().isPopupBlockerEnabled()) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Ignoring window.open() invocation because popups are blocked.");
      }
      return null;
    }
    
    boolean replaceCurrentEntryInBrowsingHistory = false;
    if (replace != Undefined.instance) {
      replaceCurrentEntryInBrowsingHistory = Context.toBoolean(replace);
    }
    if (((featuresString != null) || (replaceCurrentEntryInBrowsingHistory)) && (LOG.isDebugEnabled())) {
      LOG.debug(
        "window.open: features and replaceCurrentEntryInBrowsingHistory not implemented: url=[" + 
        urlString + 
        "] windowName=[" + windowName + 
        "] features=[" + featuresString + 
        "] replaceCurrentEntry=[" + replaceCurrentEntryInBrowsingHistory + 
        "]");
    }
    

    if ((StringUtils.isEmpty(urlString)) && (!"".equals(windowName))) {
      try {
        WebWindow webWindow = webClient.getWebWindowByName(windowName);
        return getProxy(webWindow);
      }
      catch (WebWindowNotFoundException localWebWindowNotFoundException) {}
    }
    

    URL newUrl = makeUrlForOpenWindow(urlString);
    WebWindow newWebWindow = webClient.openWindow(newUrl, windowName, webWindow_);
    return getProxy(newWebWindow);
  }
  
  private URL makeUrlForOpenWindow(String urlString) {
    if (urlString.isEmpty()) {
      return WebClient.URL_ABOUT_BLANK;
    }
    try
    {
      Page page = getWebWindow().getEnclosedPage();
      if ((page != null) && (page.isHtmlPage())) {
        return ((HtmlPage)page).getFullyQualifiedUrl(urlString);
      }
      return new URL(urlString);
    }
    catch (MalformedURLException e) {
      LOG.error("Unable to create URL for openWindow: relativeUrl=[" + urlString + "]", e); }
    return null;
  }
  











  @JsxFunction
  public int setTimeout(Object code, int timeout, Object language)
  {
    if (timeout < 1) {
      timeout = 1;
    }
    if (code == null) {
      throw Context.reportRuntimeError("Function not provided.");
    }
    

    WebWindow webWindow = getWebWindow();
    Page page = (Page)getDomNodeOrNull();
    int id; if ((code instanceof String)) {
      String s = (String)code;
      String description = "window.setTimeout(" + s + ", " + timeout + ")";
      JavaScriptJob job = BackgroundJavaScriptFactory.theFactory()
        .createJavaScriptJob(timeout, null, description, webWindow, s);
      id = webWindow.getJobManager().addJob(job, page);
    } else { int id;
      if ((code instanceof Function)) {
        Function f = (Function)code;
        String functionName;
        String functionName; if ((f instanceof FunctionObject)) {
          functionName = ((FunctionObject)f).getFunctionName();
        }
        else {
          functionName = String.valueOf(f);
        }
        
        String description = "window.setTimeout(" + functionName + ", " + timeout + ")";
        JavaScriptJob job = BackgroundJavaScriptFactory.theFactory()
          .createJavaScriptJob(timeout, null, description, webWindow, f);
        id = webWindow.getJobManager().addJob(job, page);
      }
      else {
        throw Context.reportRuntimeError("Unknown type for function."); } }
    int id;
    return id;
  }
  




  @JsxFunction
  public void clearTimeout(int timeoutId)
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("clearTimeout(" + timeoutId + ")");
    }
    getWebWindow().getJobManager().removeJob(timeoutId);
  }
  



  @JsxGetter
  public Navigator getNavigator()
  {
    return navigator_;
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public Navigator getClientInformation()
  {
    return navigator_;
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public DataTransfer getClipboardData()
  {
    DataTransfer dataTransfer = new DataTransfer();
    dataTransfer.setParentScope(this);
    dataTransfer.setPrototype(getPrototype(dataTransfer.getClass()));
    return dataTransfer;
  }
  



  @JsxGetter(propertyName="window")
  public Window getWindow_js()
  {
    return this;
  }
  



  @JsxGetter
  public Window getSelf()
  {
    return this;
  }
  



  @JsxGetter
  public Storage getLocalStorage()
  {
    return getStorage(StorageHolder.Type.LOCAL_STORAGE);
  }
  



  @JsxGetter
  public Storage getSessionStorage()
  {
    return getStorage(StorageHolder.Type.SESSION_STORAGE);
  }
  




  public Storage getStorage(StorageHolder.Type storageType)
  {
    Storage storage = (Storage)storages_.get(storageType);
    if (storage == null) {
      WebWindow webWindow = getWebWindow();
      Map<String, String> store = webWindow.getWebClient().getStorageHolder().getStore(storageType, 
        webWindow.getEnclosedPage());
      storage = new Storage(this, store);
      storages_.put(storageType, storage);
    }
    
    return storage;
  }
  



  @JsxGetter
  public Location getLocation()
  {
    return location_;
  }
  



  @JsxSetter
  public void setLocation(String newLocation)
    throws IOException
  {
    location_.setHref(newLocation);
  }
  



  @JsxGetter
  public ScriptableObject getConsole()
  {
    return console_;
  }
  



  @JsxSetter
  public void setConsole(ScriptableObject console)
  {
    console_ = console;
  }
  



  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public void dump(String message)
  {
    if ((console_ instanceof Console)) {
      Console.log(null, console_, new Object[] { message }, null);
    }
  }
  






  @JsxFunction
  public int requestAnimationFrame(Object callback)
  {
    return 1;
  }
  





  @JsxFunction
  public void cancelAnimationFrame(Object requestId) {}
  





  @JsxGetter
  public Screen getScreen()
  {
    return screen_;
  }
  



  @JsxGetter
  public History getHistory()
  {
    return history_;
  }
  



  @JsxGetter
  public External getExternal()
  {
    External external = new External();
    external.setParentScope(this);
    external.setPrototype(getPrototype(external.getClass()));
    return external;
  }
  



  public void initialize(WebWindow webWindow)
  {
    webWindow_ = webWindow;
    webWindow_.setScriptableObject(this);
    
    windowProxy_ = new WindowProxy(webWindow_);
    
    Page enclosedPage = webWindow.getEnclosedPage();
    if ((enclosedPage instanceof XmlPage)) {
      document_ = new XMLDocument();
    }
    else {
      document_ = new HTMLDocument();
    }
    document_.setParentScope(this);
    document_.setPrototype(getPrototype(document_.getClass()));
    document_.setWindow(this);
    
    if ((enclosedPage instanceof SgmlPage)) {
      SgmlPage page = (SgmlPage)enclosedPage;
      document_.setDomNode(page);
      
      DomHtmlAttributeChangeListenerImpl listener = new DomHtmlAttributeChangeListenerImpl(null);
      page.addDomChangeListener(listener);
      
      if (page.isHtmlPage()) {
        ((HtmlPage)page).addHtmlAttributeChangeListener(listener);
        ((HtmlPage)page).addAutoCloseable(this);
      }
    }
    
    documentProxy_ = new DocumentProxy(webWindow_);
    
    navigator_ = new Navigator();
    navigator_.setParentScope(this);
    navigator_.setPrototype(getPrototype(navigator_.getClass()));
    
    screen_ = new Screen();
    screen_.setParentScope(this);
    screen_.setPrototype(getPrototype(screen_.getClass()));
    
    history_ = new History();
    history_.setParentScope(this);
    history_.setPrototype(getPrototype(history_.getClass()));
    
    location_ = new Location();
    location_.setParentScope(this);
    location_.setPrototype(getPrototype(location_.getClass()));
    location_.initialize(this);
    
    console_ = new Console();
    ((Console)console_).setWebWindow(webWindow_);
    console_.setParentScope(this);
    ((Console)console_).setPrototype(getPrototype(((SimpleScriptable)console_).getClass()));
    
    applicationCache_ = new ApplicationCache();
    applicationCache_.setParentScope(this);
    applicationCache_.setPrototype(getPrototype(applicationCache_.getClass()));
    

    Context ctx = Context.getCurrentContext();
    controllers_ = ctx.newObject(this);
    
    if ((webWindow_ instanceof TopLevelWindow)) {
      WebWindow opener = ((TopLevelWindow)webWindow_).getOpener();
      if (opener != null) {
        opener_ = opener.getScriptableObject();
      }
    }
  }
  



  public void initialize(Page enclosedPage)
  {
    if ((enclosedPage != null) && (enclosedPage.isHtmlPage())) {
      HtmlPage htmlPage = (HtmlPage)enclosedPage;
      



      setDomNode(htmlPage);
      clearEventListenersContainer();
      
      WebAssert.notNull("document_", document_);
      document_.setDomNode(htmlPage);
    }
  }
  




  public void initialize() {}
  




  @JsxGetter
  public Object getTop()
  {
    if (top_ != NOT_FOUND) {
      return top_;
    }
    
    WebWindow top = getWebWindow().getTopWindow();
    return top.getScriptableObject();
  }
  



  @JsxSetter
  public void setTop(Object o)
  {
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_WINDOW_TOP_WRITABLE)) {
      top_ = o;
    }
  }
  



  @JsxGetter
  public ScriptableObject getParent()
  {
    WebWindow parent = getWebWindow().getParentWindow();
    return parent.getScriptableObject();
  }
  



  @JsxGetter
  public Object getOpener()
  {
    Object opener = opener_;
    if ((opener instanceof Window)) {
      opener = windowProxy_;
    }
    return opener;
  }
  



  @JsxSetter
  public void setOpener(Object newValue)
  {
    if ((getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_WINDOW_CHANGE_OPENER_ONLY_WINDOW_OBJECT)) && 
      (newValue != null) && (newValue != Undefined.instance) && (!(newValue instanceof Window))) {
      throw Context.reportRuntimeError("Can't set opener to something other than a window!");
    }
    opener_ = newValue;
  }
  



  @JsxGetter
  public Object getFrameElement()
  {
    WebWindow window = getWebWindow();
    if ((window instanceof FrameWindow)) {
      return ((FrameWindow)window).getFrameElement().getScriptableObject();
    }
    return null;
  }
  



  @JsxGetter(propertyName="frames")
  public Window getFrames_js()
  {
    return this;
  }
  



  @JsxGetter
  public int getLength()
  {
    return getFrames().getLength();
  }
  



  private HTMLCollection getFrames()
  {
    HtmlPage page = (HtmlPage)getWebWindow().getEnclosedPage();
    return new HTMLCollectionFrames(page);
  }
  



  public WebWindow getWebWindow()
  {
    return webWindow_;
  }
  


  @JsxFunction
  public void focus()
  {
    WebWindow window = getWebWindow();
    window.getWebClient().setCurrentWindow(window);
  }
  


  @JsxFunction
  public void blur()
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("window.blur() not implemented");
    }
  }
  


  @JsxFunction(functionName="close")
  public void close_js()
  {
    WebWindow webWindow = getWebWindow();
    if ((webWindow instanceof TopLevelWindow)) {
      ((TopLevelWindow)webWindow).close();
    }
    else {
      webWindow.getWebClient().deregisterWebWindow(webWindow);
    }
  }
  



  @JsxGetter
  @CanSetReadOnly(CanSetReadOnlyStatus.IGNORE)
  public boolean getClosed()
  {
    WebWindow webWindow = getWebWindow();
    return !webWindow.getWebClient().containsWebWindow(webWindow);
  }
  




  @JsxFunction
  public void moveTo(int x, int y)
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("window.moveTo() not implemented");
    }
  }
  




  @JsxFunction
  public void moveBy(int x, int y)
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("window.moveBy() not implemented");
    }
  }
  




  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void navigate(String url)
    throws IOException
  {
    getLocation().assign(url);
  }
  




  @JsxFunction
  public void resizeBy(int width, int height)
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("window.resizeBy() not implemented");
    }
  }
  




  @JsxFunction
  public void resizeTo(int width, int height)
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("window.resizeTo() not implemented");
    }
  }
  




  @JsxFunction
  public void scroll(int x, int y)
  {
    scrollTo(x, y);
  }
  




  @JsxFunction
  public void scrollBy(int x, int y)
  {
    HTMLElement body = ((HTMLDocument)document_).getBody();
    if (body != null) {
      body.setScrollLeft(body.getScrollLeft() + x);
      body.setScrollTop(body.getScrollTop() + y);
    }
  }
  



  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public void scrollByLines(int lines)
  {
    HTMLElement body = ((HTMLDocument)document_).getBody();
    if (body != null) {
      body.setScrollTop(body.getScrollTop() + 19 * lines);
    }
  }
  



  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public void scrollByPages(int pages)
  {
    HTMLElement body = ((HTMLDocument)document_).getBody();
    if (body != null) {
      body.setScrollTop(body.getScrollTop() + getInnerHeight() * pages);
    }
  }
  




  @JsxFunction
  public void scrollTo(int x, int y)
  {
    HTMLElement body = ((HTMLDocument)document_).getBody();
    if (body != null) {
      body.setScrollLeft(x);
      body.setScrollTop(y);
    }
  }
  



  @JsxGetter
  public Object getOnload()
  {
    Object onload = getHandlerForJavaScript("load");
    if (onload == null)
    {
      HtmlPage page = (HtmlPage)getWebWindow().getEnclosedPage();
      HtmlElement body = page.getBody();
      if (body != null) {
        HTMLBodyElement b = (HTMLBodyElement)body.getScriptableObject();
        return b.getEventHandler("onload");
      }
      return null;
    }
    return onload;
  }
  



  @JsxSetter
  public void setOnload(Object onload)
  {
    getEventListenersContainer().setEventHandlerProp("load", onload);
  }
  



  @JsxGetter
  public Object getOnclick()
  {
    return getHandlerForJavaScript("click");
  }
  



  @JsxSetter
  public void setOnclick(Object onclick)
  {
    setHandlerForJavaScript("click", onclick);
  }
  



  @JsxGetter
  public Object getOndblclick()
  {
    return getHandlerForJavaScript("dblclick");
  }
  



  @JsxSetter
  public void setOndblclick(Object ondblclick)
  {
    setHandlerForJavaScript("dblclick", ondblclick);
  }
  



  @JsxGetter
  public Object getOnhashchange()
  {
    return getHandlerForJavaScript("hashchange");
  }
  



  @JsxSetter
  public void setOnhashchange(Object onhashchange)
  {
    setHandlerForJavaScript("hashchange", onhashchange);
  }
  



  @JsxGetter
  public String getName()
  {
    return getWebWindow().getName();
  }
  



  @JsxSetter
  public void setName(String name)
  {
    getWebWindow().setName(name);
  }
  



  @JsxGetter
  public Object getOnbeforeunload()
  {
    return getHandlerForJavaScript("beforeunload");
  }
  



  @JsxSetter
  public void setOnbeforeunload(Object onbeforeunload)
  {
    setHandlerForJavaScript("beforeunload", onbeforeunload);
  }
  



  @JsxGetter
  public Object getOnerror()
  {
    return getHandlerForJavaScript("error");
  }
  



  @JsxSetter
  public void setOnerror(Object onerror)
  {
    setHandlerForJavaScript("error", onerror);
  }
  



  @JsxGetter
  public Object getOnmessage()
  {
    return getHandlerForJavaScript("message");
  }
  



  @JsxSetter
  public void setOnmessage(Object onmessage)
  {
    setHandlerForJavaScript("message", onmessage);
  }
  



  public void triggerOnError(ScriptException e)
  {
    Object o = getOnerror();
    if ((o instanceof Function)) {
      Function f = (Function)o;
      String msg = e.getMessage();
      String url = e.getPage().getUrl().toExternalForm();
      int line = e.getFailingLineNumber();
      
      int column = e.getFailingColumnNumber();
      Object[] args = { msg, url, Integer.valueOf(line), Integer.valueOf(column), e };
      f.call(Context.getCurrentContext(), this, this, args);
    }
  }
  
  private Object getHandlerForJavaScript(String eventName) {
    return getEventListenersContainer().getEventHandlerProp(eventName);
  }
  
  private void setHandlerForJavaScript(String eventName, Object handler) {
    if ((handler == null) || ((handler instanceof Function))) {
      getEventListenersContainer().setEventHandlerProp(eventName, handler);
    }
  }
  




  public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args)
  {
    throw Context.reportRuntimeError("Window is not a function.");
  }
  



  public Scriptable construct(Context cx, Scriptable scope, Object[] args)
  {
    throw Context.reportRuntimeError("Window is not a function.");
  }
  





  public Object getWithFallback(String name)
  {
    Object result = NOT_FOUND;
    
    DomNode domNode = getDomNodeOrNull();
    if (domNode != null)
    {

      HtmlPage page = (HtmlPage)domNode.getPage();
      result = getFrameWindowByName(page, name);
      
      if (result == NOT_FOUND) {
        result = getElementsByName(page, name);
        
        if (result == NOT_FOUND) {
          try
          {
            HtmlElement htmlElement = page.getHtmlElementById(name);
            if ((getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_WINDOW_FRAME_BY_ID_RETURNS_WINDOW)) && 
              ((htmlElement instanceof HtmlFrame))) {
              HtmlFrame frame = (HtmlFrame)htmlElement;
              result = getScriptableFor(frame.getEnclosedWindow());
            }
            else {
              result = getScriptableFor(htmlElement);
            }
          }
          catch (ElementNotFoundException e) {
            result = NOT_FOUND;
          }
        }
      }
      
      if ((result instanceof Window)) {
        WebWindow webWindow = ((Window)result).getWebWindow();
        result = getProxy(webWindow);
      }
    }
    
    return result;
  }
  



  public Object get(int index, Scriptable start)
  {
    if (getWebWindow() == null) {
      return Undefined.instance;
    }
    
    HTMLCollection frames = getFrames();
    if ((index < 0) || (index >= frames.getLength())) {
      return Undefined.instance;
    }
    return frames.item(Integer.valueOf(index));
  }
  
  private static Object getFrameWindowByName(HtmlPage page, String name) {
    try {
      return page.getFrameByName(name).getScriptableObject();
    }
    catch (ElementNotFoundException e) {}
    return NOT_FOUND;
  }
  






  private Object getElementsByName(final HtmlPage page, String name)
  {
    List<DomElement> elements = page.getElementsByName(name);
    
    boolean includeFormFields = getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_WINDOW_FORMFIELDS_ACCESSIBLE_BY_NAME);
    final Filter filter = new Filter(includeFormFields, null);
    
    Iterator<DomElement> it = elements.iterator();
    while (it.hasNext()) {
      if (!filter.matches(it.next())) {
        it.remove();
      }
    }
    
    if (elements.isEmpty()) {
      return NOT_FOUND;
    }
    
    if (elements.size() == 1) {
      return getScriptableFor(elements.get(0));
    }
    

    final String expElementName = "null".equals(name) ? "" : name;
    
    new HTMLCollection(page, true)
    {
      protected List<Object> computeElements() {
        List<DomElement> expElements = page.getElementsByName(expElementName);
        List<Object> result = new ArrayList(expElements.size());
        
        for (DomElement domElement : expElements) {
          if (Window.Filter.access$1(filter, domElement)) {
            result.add(domElement);
          }
        }
        return result;
      }
      
      protected AbstractList.EffectOnCache getEffectOnCache(HtmlAttributeChangeEvent event)
      {
        if ("name".equals(event.getName())) {
          return AbstractList.EffectOnCache.RESET;
        }
        return AbstractList.EffectOnCache.NONE;
      }
    };
  }
  




  public static WindowProxy getProxy(WebWindow w)
  {
    return getScriptableObjectwindowProxy_;
  }
  



  @JsxGetter
  public String getStatus()
  {
    return status_;
  }
  



  @JsxSetter
  public void setStatus(String message)
  {
    status_ = message;
    
    StatusHandler statusHandler = webWindow_.getWebClient().getStatusHandler();
    if (statusHandler != null) {
      statusHandler.statusMessageChanged(webWindow_.getEnclosedPage(), message);
    }
  }
  









  @JsxFunction
  public int setInterval(Object code, int timeout, Object language)
  {
    if (timeout < 1) {
      timeout = 1;
    }
    
    WebWindow w = getWebWindow();
    Page page = (Page)getDomNodeOrNull();
    String description = "window.setInterval(" + timeout + ")";
    if (code == null)
      throw Context.reportRuntimeError("Function not provided.");
    int id;
    if ((code instanceof String)) {
      String s = (String)code;
      JavaScriptJob job = BackgroundJavaScriptFactory.theFactory()
        .createJavaScriptJob(timeout, Integer.valueOf(timeout), description, w, s);
      id = w.getJobManager().addJob(job, page);
    } else { int id;
      if ((code instanceof Function)) {
        Function f = (Function)code;
        JavaScriptJob job = BackgroundJavaScriptFactory.theFactory()
          .createJavaScriptJob(timeout, Integer.valueOf(timeout), description, w, f);
        id = w.getJobManager().addJob(job, page);
      }
      else {
        throw Context.reportRuntimeError("Unknown type for function."); } }
    int id;
    return id;
  }
  






  @JsxFunction
  public void clearInterval(int intervalID)
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("clearInterval(" + intervalID + ")");
    }
    getWebWindow().getJobManager().removeJob(intervalID);
  }
  




  @JsxGetter
  public int getInnerWidth()
  {
    return getWebWindow().getInnerWidth();
  }
  




  @JsxGetter
  public int getOuterWidth()
  {
    return getWebWindow().getOuterWidth();
  }
  




  @JsxGetter
  public int getInnerHeight()
  {
    return getWebWindow().getInnerHeight();
  }
  




  @JsxGetter
  public int getOuterHeight()
  {
    return getWebWindow().getOuterHeight();
  }
  





  @JsxFunction
  public void print()
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("window.print() not implemented");
    }
  }
  







  @JsxFunction
  public void captureEvents(String type) {}
  







  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void CollectGarbage() {}
  






  @JsxFunction
  public CSS2Properties getComputedStyle(Object element, String pseudoElement)
  {
    if (!(element instanceof Element)) {
      throw ScriptRuntime.typeError("parameter 1 is not of type 'Element'");
    }
    Element e = (Element)element;
    synchronized (computedStyles_) {
      Map<String, CSS2Properties> elementMap = (Map)computedStyles_.get(e);
      if (elementMap != null) {
        CSS2Properties style = (CSS2Properties)elementMap.get(pseudoElement);
        if (style != null) {
          return style;
        }
      }
    }
    
    CSSStyleDeclaration original = e.getStyle();
    CSS2Properties style = new CSS2Properties(original);
    
    StyleSheetList sheets = ((HTMLDocument)e.getOwnerDocument()).getStyleSheets();
    boolean trace = LOG.isTraceEnabled();
    for (int i = 0; i < sheets.getLength(); i++) {
      CSSStyleSheet sheet = (CSSStyleSheet)sheets.item(i);
      if ((sheet.isActive()) && (sheet.isEnabled())) {
        if (trace) {
          LOG.trace("modifyIfNecessary: " + sheet + ", " + style + ", " + e);
        }
        sheet.modifyIfNecessary(style, e, pseudoElement);
      }
    }
    
    synchronized (computedStyles_) {
      Map<String, CSS2Properties> elementMap = (Map)computedStyles_.get(element);
      if (elementMap == null) {
        elementMap = new WeakHashMap();
        computedStyles_.put(e, elementMap);
      }
      elementMap.put(pseudoElement, style);
    }
    
    return style;
  }
  



  @JsxFunction
  public Selection getSelection()
  {
    WebWindow webWindow = getWebWindow();
    
    if ((webWindow instanceof FrameWindow)) {
      FrameWindow frameWindow = (FrameWindow)webWindow;
      if ((getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_WINDOW_SELECTION_NULL_IF_INVISIBLE)) && 
        (!frameWindow.getFrameElement().isDisplayed())) {
        return null;
      }
    }
    return getSelectionImpl();
  }
  



  public Selection getSelectionImpl()
  {
    if (selection_ == null) {
      selection_ = new Selection();
      selection_.setParentScope(this);
      selection_.setPrototype(getPrototype(selection_.getClass()));
    }
    return selection_;
  }
  








  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public Object showModalDialog(String url, Object arguments, String features)
  {
    WebWindow webWindow = getWebWindow();
    WebClient client = webWindow.getWebClient();
    try {
      URL completeUrl = ((HtmlPage)getDomNodeOrDie()).getFullyQualifiedUrl(url);
      DialogWindow dialog = client.openDialogWindow(completeUrl, webWindow, arguments);
      



      ScriptableObject jsDialog = dialog.getScriptableObject();
      return jsDialog.get("returnValue", jsDialog);
    }
    catch (IOException e) {
      throw Context.throwAsScriptRuntimeEx(e);
    }
  }
  







  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public Object showModelessDialog(String url, Object arguments, String features)
  {
    WebWindow webWindow = getWebWindow();
    WebClient client = webWindow.getWebClient();
    try {
      URL completeUrl = ((HtmlPage)getDomNodeOrDie()).getFullyQualifiedUrl(url);
      DialogWindow dialog = client.openDialogWindow(completeUrl, webWindow, arguments);
      return (Window)dialog.getScriptableObject();
    }
    catch (IOException e)
    {
      throw Context.throwAsScriptRuntimeEx(e);
    }
  }
  





  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public Object getControllers()
  {
    return controllers_;
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public void setControllers(Object value)
  {
    controllers_ = value;
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public int getMozInnerScreenX()
  {
    return 11;
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public int getMozInnerScreenY()
  {
    return 91;
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public int getMozPaintCount()
  {
    return 0;
  }
  

  private static final Set<String> ATTRIBUTES_AFFECTING_PARENT = new HashSet(Arrays.asList(new String[] {
    "style", 
    "class", 
    "height", 
    "width" }));
  
  private static final class Filter {
    private final boolean includeFormFields_;
    
    private Filter(boolean includeFormFields) {
      includeFormFields_ = includeFormFields;
    }
    
    private boolean matches(Object object) {
      if (((object instanceof HtmlEmbed)) || 
        ((object instanceof HtmlForm)) || 
        ((object instanceof HtmlImage)) || 
        ((object instanceof HtmlObject))) {
        return true;
      }
      if ((includeFormFields_) && (
        ((object instanceof HtmlAnchor)) || 
        ((object instanceof HtmlButton)) || 
        ((object instanceof HtmlInput)) || 
        ((object instanceof HtmlMap)) || 
        ((object instanceof HtmlSelect)) || 
        ((object instanceof HtmlTextArea)))) {
        return true;
      }
      return false;
    }
  }
  


  public void clearComputedStyles()
  {
    synchronized (computedStyles_) {
      computedStyles_.clear();
    }
  }
  



  public void clearComputedStyles(Element element)
  {
    synchronized (computedStyles_) {
      computedStyles_.remove(element);
    }
  }
  

















  private class DomHtmlAttributeChangeListenerImpl
    implements DomChangeListener, HtmlAttributeChangeListener
  {
    private DomHtmlAttributeChangeListenerImpl() {}
    
















    public void nodeAdded(DomChangeEvent event)
    {
      nodeChanged(event.getChangedNode(), null);
    }
    



    public void nodeDeleted(DomChangeEvent event)
    {
      nodeChanged(event.getChangedNode(), null);
    }
    



    public void attributeAdded(HtmlAttributeChangeEvent event)
    {
      nodeChanged(event.getHtmlElement(), event.getName());
    }
    



    public void attributeRemoved(HtmlAttributeChangeEvent event)
    {
      nodeChanged(event.getHtmlElement(), event.getName());
    }
    



    public void attributeReplaced(HtmlAttributeChangeEvent event)
    {
      nodeChanged(event.getHtmlElement(), event.getName());
    }
    
    private void nodeChanged(DomNode changed, String attribName)
    {
      if ((changed instanceof HtmlStyle)) {
        clearComputedStyles();
        return;
      }
      if ((changed instanceof HtmlLink)) {
        String rel = ((HtmlLink)changed).getRelAttribute().toLowerCase(Locale.ROOT);
        if ("stylesheet".equals(rel)) {
          clearComputedStyles();
          return;
        }
      }
      
      synchronized (computedStyles_) {
        boolean clearParents = Window.ATTRIBUTES_AFFECTING_PARENT.contains(attribName);
        Iterator<Map.Entry<Element, Map<String, CSS2Properties>>> i = 
          computedStyles_.entrySet().iterator(); while (i.hasNext()) {
          Map.Entry<Element, Map<String, CSS2Properties>> entry = (Map.Entry)i.next();
          DomNode node = ((Element)entry.getKey()).getDomNodeOrDie();
          if ((changed == node) || 
            (changed.getParentNode() == node.getParentNode()) || 
            (changed.isAncestorOf(node)) || (
            (clearParents) && (node.isAncestorOf(changed)))) {
            i.remove();
          }
        }
      }
    }
  }
  




  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public String ScriptEngine()
  {
    return "JScript";
  }
  




  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public int ScriptEngineBuildVersion()
  {
    return 12345;
  }
  




  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public int ScriptEngineMajorVersion()
  {
    return getBrowserVersion().getBrowserVersionNumeric();
  }
  




  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public int ScriptEngineMinorVersion()
  {
    return 0;
  }
  





  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public void stop() {}
  





  @JsxGetter
  public int getPageXOffset()
  {
    return 0;
  }
  



  @JsxGetter
  public int getPageYOffset()
  {
    return 0;
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public int getScrollX()
  {
    return 0;
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public int getScrollY()
  {
    return 0;
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public Netscape getNetscape()
  {
    return new Netscape(this);
  }
  




  public boolean isConst(String name)
  {
    if (("undefined".equals(name)) || ("Infinity".equals(name)) || ("NaN".equals(name))) {
      return false;
    }
    
    return super.isConst(name);
  }
  



  public boolean dispatchEvent(Event event)
  {
    event.setTarget(this);
    ScriptResult result = fireEvent(event);
    return !event.isAborted(result);
  }
  



  @JsxGetter
  public Object getOnchange()
  {
    return getHandlerForJavaScript("change");
  }
  



  @JsxSetter
  public void setOnchange(Object onchange)
  {
    setHandlerForJavaScript("change", onchange);
  }
  



  @JsxGetter
  public Object getOnsubmit()
  {
    return getHandlerForJavaScript("submit");
  }
  



  @JsxSetter
  public void setOnsubmit(Object onsubmit)
  {
    setHandlerForJavaScript("submit", onsubmit);
  }
  






  @JsxFunction
  public void postMessage(String message, String targetOrigin, Object transfer)
  {
    URL currentURL = getWebWindow().getEnclosedPage().getUrl();
    
    if ((!"*".equals(targetOrigin)) && (!"/".equals(targetOrigin))) {
      URL targetURL = null;
      try {
        targetURL = new URL(targetOrigin);
      }
      catch (Exception e) {
        throw Context.throwAsScriptRuntimeEx(
          new Exception(
          "SyntaxError: Failed to execute 'postMessage' on 'Window': Invalid target origin '" + 
          targetOrigin + "' was specified (reason: " + e.getMessage() + "."));
      }
      
      if (getPort(targetURL) != getPort(currentURL)) {
        return;
      }
      if (!targetURL.getHost().equals(currentURL.getHost())) {
        return;
      }
      if (!targetURL.getProtocol().equals(currentURL.getProtocol())) {
        return;
      }
    }
    final MessageEvent event = new MessageEvent();
    String origin = currentURL.getProtocol() + "://" + currentURL.getHost() + ':' + currentURL.getPort();
    event.initMessageEvent("message", false, false, message, origin, "", this, transfer);
    event.setParentScope(this);
    event.setPrototype(getPrototype(event.getClass()));
    
    final JavaScriptEngine jsEngine = getWebWindow().getWebClient().getJavaScriptEngine();
    PostponedAction action = new PostponedAction(getDomNodeOrDie().getPage())
    {
      public void execute() throws Exception {
        ContextAction contextAction = new ContextAction()
        {
          public Object run(Context cx) {
            return Boolean.valueOf(dispatchEvent(val$event));
          }
          
        };
        ContextFactory cf = jsEngine.getContextFactory();
        cf.call(contextAction);
      }
    };
    jsEngine.addPostponedAction(action);
  }
  




  public static int getPort(URL url)
  {
    int port = url.getPort();
    if (port == -1) {
      if ("http".equals(url.getProtocol())) {
        port = 80;
      }
      else {
        port = 443;
      }
    }
    return port;
  }
  



  @JsxGetter
  public Performance getPerformance()
  {
    Performance performance = new Performance();
    performance.setParentScope(this);
    performance.setPrototype(getPrototype(performance.getClass()));
    return performance;
  }
  



  @JsxGetter
  public int getDevicePixelRatio()
  {
    return 1;
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public StyleMedia getStyleMedia()
  {
    StyleMedia styleMedia = new StyleMedia();
    styleMedia.setParentScope(this);
    styleMedia.setPrototype(getPrototype(styleMedia.getClass()));
    return styleMedia;
  }
  





  @JsxFunction
  public MediaQueryList matchMedia(String mediaQueryString)
  {
    MediaQueryList mediaQueryList = new MediaQueryList(mediaQueryString);
    mediaQueryList.setParentScope(this);
    mediaQueryList.setPrototype(getPrototype(mediaQueryList.getClass()));
    return mediaQueryList;
  }
  












  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public boolean find(String search, boolean caseSensitive, boolean backwards, boolean wrapAround, boolean wholeWord, boolean searchInFrames, boolean showDialog)
  {
    return false;
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public SpeechSynthesis getSpeechSynthesis()
  {
    SpeechSynthesis speechSynthesis = new SpeechSynthesis();
    speechSynthesis.setParentScope(this);
    speechSynthesis.setPrototype(getPrototype(speechSynthesis.getClass()));
    return speechSynthesis;
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public Object getOffscreenBuffering()
  {
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_WINDOW_FRAMES_ACCESSIBLE_BY_ID)) {
      return "auto";
    }
    return Boolean.valueOf(true);
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public Crypto getCrypto()
  {
    if (crypto_ == null) {
      crypto_ = new Crypto(this);
    }
    return crypto_;
  }
  



  public void close()
  {
    Symbol.remove(this);
  }
  
  public void setParentScope(Scriptable parent) {}
}
