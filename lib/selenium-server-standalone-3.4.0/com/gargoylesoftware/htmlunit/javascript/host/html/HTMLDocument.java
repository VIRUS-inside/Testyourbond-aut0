package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.BaseFrameElement;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlApplet;
import com.gargoylesoftware.htmlunit.html.HtmlArea;
import com.gargoylesoftware.htmlunit.html.HtmlAttributeChangeEvent;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlScript;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.PostponedAction;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.CanSetReadOnly;
import com.gargoylesoftware.htmlunit.javascript.configuration.CanSetReadOnlyStatus;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleSheetList;
import com.gargoylesoftware.htmlunit.javascript.host.dom.AbstractList.EffectOnCache;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Attr;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Document;
import com.gargoylesoftware.htmlunit.javascript.host.dom.NodeIterator;
import com.gargoylesoftware.htmlunit.javascript.host.dom.NodeList;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Range;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Selection;
import com.gargoylesoftware.htmlunit.javascript.host.dom.TreeWalker;
import com.gargoylesoftware.htmlunit.javascript.host.event.BeforeUnloadEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.CloseEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.CustomEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.HashChangeEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.KeyboardEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.MessageEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.MouseEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.MutationEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.PointerEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.PopStateEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.ProgressEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.UIEvent;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.EncodingSniffer;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import net.sourceforge.htmlunit.corejs.javascript.Callable;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.FunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.css.sac.CSSException;
import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentType;
import org.w3c.dom.traversal.NodeFilter;































































@JsxClass
public class HTMLDocument
  extends Document
{
  private static final Log LOG = LogFactory.getLog(HTMLDocument.class);
  


  private static final String LAST_MODIFIED_DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";
  


  private static final Map<String, Class<? extends Event>> SUPPORTED_DOM2_EVENT_TYPE_MAP;
  


  private static final Map<String, Class<? extends Event>> SUPPORTED_DOM3_EVENT_TYPE_MAP;
  


  private static final Map<String, Class<? extends Event>> SUPPORTED_VENDOR_EVENT_TYPE_MAP;
  


  private static final Set<String> EXECUTE_CMDS_IE = new HashSet();
  
  private static final Set<String> EXECUTE_CMDS_FF = new HashSet();
  private static final Set<String> EXECUTE_CMDS_CHROME = new HashSet();
  



  private static int UniqueID_Counter_ = 1;
  
  private static enum ParsingStatus { OUTSIDE,  START,  IN_NAME,  INSIDE,  IN_STRING;
  }
  

  private HTMLElement activeElement_;
  private final StringBuilder writeBuilder_ = new StringBuilder();
  private boolean writeInCurrentDocument_ = true;
  private String domain_;
  private String uniqueID_;
  private String lastModified_;
  private String compatMode_;
  private int documentMode_ = -1;
  private boolean closePostponedAction_;
  private boolean executionExternalPostponed_;
  
  static
  {
    Map<String, Class<? extends Event>> dom2EventMap = new HashMap();
    dom2EventMap.put("HTMLEvents", Event.class);
    dom2EventMap.put("MouseEvents", MouseEvent.class);
    dom2EventMap.put("MutationEvents", MutationEvent.class);
    dom2EventMap.put("UIEvents", UIEvent.class);
    SUPPORTED_DOM2_EVENT_TYPE_MAP = Collections.unmodifiableMap(dom2EventMap);
    
    Map<String, Class<? extends Event>> dom3EventMap = new HashMap();
    dom3EventMap.put("Event", Event.class);
    dom3EventMap.put("KeyboardEvent", KeyboardEvent.class);
    dom3EventMap.put("MouseEvent", MouseEvent.class);
    dom3EventMap.put("MessageEvent", MessageEvent.class);
    dom3EventMap.put("MutationEvent", MutationEvent.class);
    dom3EventMap.put("UIEvent", UIEvent.class);
    dom3EventMap.put("CustomEvent", CustomEvent.class);
    dom3EventMap.put("CloseEvent", CloseEvent.class);
    SUPPORTED_DOM3_EVENT_TYPE_MAP = Collections.unmodifiableMap(dom3EventMap);
    
    Map<String, Class<? extends Event>> additionalEventMap = new HashMap();
    additionalEventMap.put("BeforeUnloadEvent", BeforeUnloadEvent.class);
    additionalEventMap.put("Events", Event.class);
    additionalEventMap.put("HashChangeEvent", HashChangeEvent.class);
    additionalEventMap.put("KeyEvents", KeyboardEvent.class);
    additionalEventMap.put("PointerEvent", PointerEvent.class);
    additionalEventMap.put("PopStateEvent", PopStateEvent.class);
    additionalEventMap.put("ProgressEvent", ProgressEvent.class);
    SUPPORTED_VENDOR_EVENT_TYPE_MAP = Collections.unmodifiableMap(additionalEventMap);
    

















    List<String> cmds = Arrays.asList(new String[] {"2D-Position", "AbsolutePosition", "BlockDirLTR", "BlockDirRTL", "BrowseMode", "ClearAuthenticationCache", "CreateBookmark", "Copy", "Cut", "DirLTR", "DirRTL", "EditMode", "InlineDirLTR", "InlineDirRTL", "InsertButton", "InsertFieldset", "InsertIFrame", "InsertInputButton", "InsertInputCheckbox", "InsertInputFileUpload", "InsertInputHidden", "InsertInputImage", "InsertInputPassword", "InsertInputRadio", "InsertInputReset", "InsertInputSubmit", "InsertInputText", "InsertMarquee", "InsertSelectDropdown", "InsertSelectListbox", "InsertTextArea", "LiveResize", "MultipleSelection", "Open", "OverWrite", "PlayImage", "Refresh", "RemoveParaFormat", "SaveAs", "SizeToControl", "SizeToControlHeight", "SizeToControlWidth", "Stop", "StopImage", "UnBookmark", "Paste" });
    
    for (String cmd : cmds) {
      EXECUTE_CMDS_IE.add(cmd.toLowerCase(Locale.ROOT));
    }
    













    cmds = Arrays.asList(new String[] { "BackColor", "BackgroundImageCache", "Bold", "CreateLink", "Delete", "FontName", "FontSize", "ForeColor", "FormatBlock", "Indent", "InsertHorizontalRule", "InsertImage", "InsertOrderedList", "InsertParagraph", "InsertUnorderedList", "Italic", "JustifyCenter", "JustifyFull", "JustifyLeft", "JustifyNone", "JustifyRight", "Outdent", "Print", "Redo", "RemoveFormat", "SelectAll", "StrikeThrough", "Subscript", "Superscript", "Underline", "Undo", "Unlink", "Unselect" });
    
    for (String cmd : cmds) {
      EXECUTE_CMDS_IE.add(cmd.toLowerCase(Locale.ROOT));
      if (!"Bold".equals(cmd)) {
        EXECUTE_CMDS_CHROME.add(cmd.toLowerCase(Locale.ROOT));
      }
    }
    







    cmds = Arrays.asList(new String[] { "backColor", "bold", "contentReadOnly", "copy", "createLink", "cut", "decreaseFontSize", "delete", "fontName", "fontSize", "foreColor", "formatBlock", "heading", "hiliteColor", "increaseFontSize", "indent", "insertHorizontalRule", "insertHTML", "insertImage", "insertOrderedList", "insertUnorderedList", "insertParagraph", "italic", "justifyCenter", "JustifyFull", "justifyLeft", "justifyRight", "outdent", "paste", "redo", "removeFormat", "selectAll", "strikeThrough", "subscript", "superscript", "underline", "undo", "unlink", "useCSS", "styleWithCSS" });
    
    for (String cmd : cmds) {
      EXECUTE_CMDS_FF.add(cmd.toLowerCase(Locale.ROOT));
      if (!"bold".equals(cmd)) {
        EXECUTE_CMDS_CHROME.add(cmd.toLowerCase(Locale.ROOT));
      }
    }
  }
  



  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLDocument() {}
  



  public DomNode getDomNodeOrDie()
  {
    try
    {
      return super.getDomNodeOrDie();
    }
    catch (IllegalStateException e) {
      throw Context.reportRuntimeError("No node attached to this object");
    }
  }
  




  public HtmlPage getPage()
  {
    return (HtmlPage)getDomNodeOrDie();
  }
  



  @JsxGetter
  public Object getForms()
  {
    new HTMLCollection(getDomNodeOrDie(), false)
    {
      protected boolean isMatching(DomNode node) {
        return ((node instanceof HtmlForm)) && (node.getPrefix() == null);
      }
      

      public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args)
      {
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOCUMENT_FORMS_FUNCTION_SUPPORTED)) {
          return super.call(cx, scope, thisObj, args);
        }
        throw Context.reportRuntimeError("TypeError: document.forms is not a function");
      }
    };
  }
  




  @JsxGetter
  public Object getLinks()
  {
    new HTMLCollection(getDomNodeOrDie(), true)
    {
      protected boolean isMatching(DomNode node) {
        return (((node instanceof HtmlAnchor)) || ((node instanceof HtmlArea))) && 
          (((HtmlElement)node).hasAttribute("href"));
      }
      
      protected AbstractList.EffectOnCache getEffectOnCache(HtmlAttributeChangeEvent event)
      {
        HtmlElement node = event.getHtmlElement();
        if ((((node instanceof HtmlAnchor)) || ((node instanceof HtmlArea))) && ("href".equals(event.getName()))) {
          return AbstractList.EffectOnCache.RESET;
        }
        return AbstractList.EffectOnCache.NONE;
      }
    };
  }
  




  @JsxGetter
  public String getLastModified()
  {
    if (lastModified_ == null) {
      WebResponse webResponse = getPage().getWebResponse();
      String stringDate = webResponse.getResponseHeaderValue("Last-Modified");
      if (stringDate == null) {
        stringDate = webResponse.getResponseHeaderValue("Date");
      }
      Date lastModified = parseDateOrNow(stringDate);
      lastModified_ = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.ROOT).format(lastModified);
    }
    return lastModified_;
  }
  
  private static Date parseDateOrNow(String stringDate) {
    Date date = com.gargoylesoftware.htmlunit.util.StringUtils.parseHttpDate(stringDate);
    if (date == null) {
      return new Date();
    }
    return date;
  }
  






  @JsxGetter
  public Object getAnchors()
  {
    new HTMLCollection(getDomNodeOrDie(), true)
    {
      protected boolean isMatching(DomNode node) {
        if (!(node instanceof HtmlAnchor)) {
          return false;
        }
        HtmlAnchor anchor = (HtmlAnchor)node;
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_ANCHORS_REQUIRES_NAME_OR_ID)) {
          return (anchor.hasAttribute("name")) || (anchor.hasAttribute("id"));
        }
        return anchor.hasAttribute("name");
      }
      
      protected AbstractList.EffectOnCache getEffectOnCache(HtmlAttributeChangeEvent event)
      {
        HtmlElement node = event.getHtmlElement();
        if (!(node instanceof HtmlAnchor)) {
          return AbstractList.EffectOnCache.NONE;
        }
        if (("name".equals(event.getName())) || ("id".equals(event.getName()))) {
          return AbstractList.EffectOnCache.RESET;
        }
        return AbstractList.EffectOnCache.NONE;
      }
    };
  }
  







  @JsxGetter
  public Object getApplets()
  {
    new HTMLCollection(getDomNodeOrDie(), false)
    {
      protected boolean isMatching(DomNode node) {
        return node instanceof HtmlApplet;
      }
    };
  }
  









  @JsxFunction
  public static void write(Context context, Scriptable thisObj, Object[] args, Function function)
  {
    HTMLDocument thisAsDocument = getDocument(thisObj);
    thisAsDocument.write(concatArgsAsString(args));
  }
  




  private static String concatArgsAsString(Object[] args)
  {
    StringBuilder builder = new StringBuilder();
    Object[] arrayOfObject = args;int j = args.length; for (int i = 0; i < j; i++) { Object arg = arrayOfObject[i];
      builder.append(Context.toString(arg));
    }
    return builder.toString();
  }
  









  @JsxFunction
  public static void writeln(Context context, Scriptable thisObj, Object[] args, Function function)
  {
    HTMLDocument thisAsDocument = getDocument(thisObj);
    thisAsDocument.write(concatArgsAsString(args) + "\n");
  }
  








  private static HTMLDocument getDocument(Scriptable thisObj)
  {
    if (((thisObj instanceof HTMLDocument)) && ((thisObj.getPrototype() instanceof HTMLDocument))) {
      return (HTMLDocument)thisObj;
    }
    if (((thisObj instanceof DocumentProxy)) && ((thisObj.getPrototype() instanceof HTMLDocument))) {
      return (HTMLDocument)((DocumentProxy)thisObj).getDelegee();
    }
    
    Window window = getWindow(thisObj);
    if (window.getBrowserVersion().hasFeature(BrowserVersionFeatures.HTMLDOCUMENT_FUNCTION_DETACHED)) {
      return (HTMLDocument)window.getDocument();
    }
    throw Context.reportRuntimeError("Function can't be used detached from document");
  }
  







  public void setExecutingDynamicExternalPosponed(boolean executing)
  {
    executionExternalPostponed_ = executing;
  }
  








  protected void write(String content)
  {
    if (executionExternalPostponed_) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("skipping write for external posponed: " + content);
      }
      return;
    }
    
    if (LOG.isDebugEnabled()) {
      LOG.debug("write: " + content);
    }
    
    HtmlPage page = (HtmlPage)getDomNodeOrDie();
    if (!page.isBeingParsed()) {
      writeInCurrentDocument_ = false;
    }
    

    writeBuilder_.append(content);
    

    if (!writeInCurrentDocument_) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("wrote content to buffer");
      }
      scheduleImplicitClose();
      return;
    }
    String bufferedContent = writeBuilder_.toString();
    if (!canAlreadyBeParsed(bufferedContent)) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("write: not enough content to parse it now");
      }
      return;
    }
    
    writeBuilder_.setLength(0);
    page.writeInParsedStream(bufferedContent);
  }
  
  private void scheduleImplicitClose() {
    if (!closePostponedAction_) {
      closePostponedAction_ = true;
      HtmlPage page = (HtmlPage)getDomNodeOrDie();
      final WebWindow enclosingWindow = page.getEnclosingWindow();
      page.getWebClient().getJavaScriptEngine().addPostponedAction(new PostponedAction(page)
      {
        public void execute() throws Exception {
          if (writeBuilder_.length() != 0) {
            close();
          }
          closePostponedAction_ = false;
        }
        
        public boolean isStillAlive()
        {
          return !enclosingWindow.isClosed();
        }
      });
    }
  }
  







  static boolean canAlreadyBeParsed(String content)
  {
    ParsingStatus tagState = ParsingStatus.OUTSIDE;
    int tagNameBeginIndex = 0;
    int scriptTagCount = 0;
    boolean tagIsOpen = true;
    char stringBoundary = '\000';
    boolean stringSkipNextChar = false;
    int index = 0;
    char openingQuote = '\000';
    for (char currentChar : content.toCharArray()) {
      switch (tagState) {
      case INSIDE: 
        if (currentChar == '<') {
          tagState = ParsingStatus.START;
          tagIsOpen = true;
        }
        else if ((scriptTagCount > 0) && ((currentChar == '\'') || (currentChar == '"'))) {
          tagState = ParsingStatus.IN_STRING;
          stringBoundary = currentChar;
          stringSkipNextChar = false;
        }
        break;
      case IN_NAME: 
        if (currentChar == '/') {
          tagIsOpen = false;
          tagNameBeginIndex = index + 1;
        }
        else {
          tagNameBeginIndex = index;
        }
        tagState = ParsingStatus.IN_NAME;
        break;
      case IN_STRING: 
        if ((Character.isWhitespace(currentChar)) || (currentChar == '>')) {
          String tagName = content.substring(tagNameBeginIndex, index);
          if ("script".equalsIgnoreCase(tagName)) {
            if (tagIsOpen) {
              scriptTagCount++;
            }
            else if (scriptTagCount > 0)
            {
              scriptTagCount--;
            }
          }
          if (currentChar == '>') {
            tagState = ParsingStatus.OUTSIDE;
          }
          else {
            tagState = ParsingStatus.INSIDE;
          }
        }
        else if (!Character.isLetter(currentChar)) {
          tagState = ParsingStatus.OUTSIDE;
        }
        break;
      case OUTSIDE: 
        if (currentChar == openingQuote) {
          openingQuote = '\000';
        }
        else if (openingQuote == 0) {
          if ((currentChar == '\'') || (currentChar == '"')) {
            openingQuote = currentChar;
          }
          else if ((currentChar == '>') && (openingQuote == 0)) {
            tagState = ParsingStatus.OUTSIDE;
          }
        }
        break;
      case START: 
        if (stringSkipNextChar) {
          stringSkipNextChar = false;

        }
        else if (currentChar == stringBoundary) {
          tagState = ParsingStatus.OUTSIDE;
        }
        else if (currentChar == '\\') {
          stringSkipNextChar = true;
        }
        
        break;
      }
      
      
      index++;
    }
    if ((scriptTagCount > 0) || (tagState != ParsingStatus.OUTSIDE)) {
      if (LOG.isDebugEnabled()) {
        StringBuilder message = new StringBuilder();
        message.append("canAlreadyBeParsed() retruns false for content: '");
        message.append(org.apache.commons.lang3.StringUtils.abbreviateMiddle(content, ".", 100));
        message.append("' (scriptTagCount: " + scriptTagCount);
        message.append(" tagState: " + tagState);
        message.append(")");
        LOG.debug(message.toString());
      }
      return false;
    }
    
    return true;
  }
  




  HtmlElement getLastHtmlElement(HtmlElement node)
  {
    DomNode lastChild = node.getLastChild();
    if ((lastChild == null) || 
      (!(lastChild instanceof HtmlElement)) || 
      ((lastChild instanceof HtmlScript))) {
      return node;
    }
    
    return getLastHtmlElement((HtmlElement)lastChild);
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public String getBaseURI()
  {
    return getPage().getBaseURL().toString();
  }
  



  @JsxGetter
  public String getCookie()
  {
    HtmlPage page = getPage();
    
    URL url = page.getUrl();
    
    StringBuilder builder = new StringBuilder();
    Set<Cookie> cookies = page.getWebClient().getCookies(url);
    for (Cookie cookie : cookies) {
      if (!cookie.isHttpOnly())
      {

        if (builder.length() != 0) {
          builder.append("; ");
        }
        if (!"HTMLUNIT_EMPTY_COOKIE".equals(cookie.getName())) {
          builder.append(cookie.getName());
          builder.append("=");
        }
        builder.append(cookie.getValue());
      }
    }
    return builder.toString();
  }
  




  @JsxGetter
  public String getCompatMode()
  {
    getDocumentMode();
    return compatMode_;
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public int getDocumentMode()
  {
    if (documentMode_ != -1) {
      return documentMode_;
    }
    
    compatMode_ = "CSS1Compat";
    
    BrowserVersion browserVersion = getBrowserVersion();
    if (isQuirksDocType()) {
      compatMode_ = "BackCompat";
    }
    
    float version = browserVersion.getBrowserVersionNumeric();
    documentMode_ = ((int)Math.floor(version));
    return documentMode_;
  }
  





  public void forceDocumentMode(int documentMode)
  {
    documentMode_ = documentMode;
    compatMode_ = (documentMode == 5 ? "BackCompat" : "CSS1Compat");
  }
  
  private boolean isQuirksDocType() {
    DocumentType docType = getPage().getDoctype();
    if (docType != null) {
      String systemId = docType.getSystemId();
      if (systemId != null) {
        if ("http://www.w3.org/TR/html4/strict.dtd".equals(systemId)) {
          return false;
        }
        
        if ("http://www.w3.org/TR/html4/loose.dtd".equals(systemId)) {
          String publicId = docType.getPublicId();
          if ("-//W3C//DTD HTML 4.01 Transitional//EN".equals(publicId)) {
            return false;
          }
        }
        
        if (("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd".equals(systemId)) || 
          ("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd".equals(systemId))) {
          return false;
        }
      }
      else if (docType.getPublicId() == null) {
        if (docType.getName() != null) {
          return false;
        }
        return true;
      }
    }
    return true;
  }
  




  @JsxSetter
  public void setCookie(String newCookie)
  {
    HtmlPage page = getPage();
    WebClient client = page.getWebClient();
    
    client.addCookie(newCookie, getPage().getUrl(), this);
  }
  



  @JsxGetter
  public Object getImages()
  {
    new HTMLCollection(getDomNodeOrDie(), false)
    {
      protected boolean isMatching(DomNode node) {
        return node instanceof HtmlImage;
      }
    };
  }
  



  @JsxGetter
  public String getInputEncoding()
  {
    Charset encoding = getPage().getCharset();
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.HTMLDOCUMENT_CHARSET_LOWERCASE)) {
      return encoding.name();
    }
    return EncodingSniffer.translateEncodingLabel(encoding);
  }
  



  @JsxGetter
  public String getCharacterSet()
  {
    Charset charset = getPage().getCharset();
    if ((charset != null) && (getBrowserVersion().hasFeature(BrowserVersionFeatures.HTMLDOCUMENT_CHARSET_LOWERCASE))) {
      return charset.name().toLowerCase(Locale.ROOT);
    }
    return EncodingSniffer.translateEncodingLabel(charset);
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public String getCharset()
  {
    Charset charset = getPage().getCharset();
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.HTMLDOCUMENT_CHARSET_LOWERCASE)) {
      return charset.name().toLowerCase(Locale.ROOT);
    }
    return EncodingSniffer.translateEncodingLabel(charset);
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public String getDefaultCharset()
  {
    return "windows-1252";
  }
  



  @JsxGetter(propertyName="URL")
  public String getURL()
  {
    return getPage().getUrl().toExternalForm();
  }
  




  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public String getUniqueID()
  {
    if (uniqueID_ == null) {
      uniqueID_ = ("ms__id" + UniqueID_Counter_++);
    }
    return uniqueID_;
  }
  



  @JsxGetter
  public HTMLCollection getAll()
  {
    new HTMLAllCollection(getDomNodeOrDie())
    {
      protected boolean isMatching(DomNode node) {
        return true;
      }
      
      public boolean avoidObjectDetection()
      {
        return true;
      }
    };
  }
  
















  @JsxFunction
  public Object open(Object url, Object name, Object features, Object replace)
  {
    HtmlPage page = getPage();
    if (page.isBeingParsed()) {
      LOG.warn("Ignoring call to open() during the parsing stage.");
      return null;
    }
    

    if (!writeInCurrentDocument_) {
      LOG.warn("Function open() called when document is already open.");
    }
    writeInCurrentDocument_ = false;
    if (((getWindow().getWebWindow() instanceof FrameWindow)) && 
      ("about:blank".equals(getPage().getUrl().toExternalForm()))) {
      URL enclosingUrl = ((FrameWindow)getWindow().getWebWindow()).getEnclosingPage().getUrl();
      getPage().getWebResponse().getWebRequest().setUrl(enclosingUrl);
    }
    return this;
  }
  






  @JsxFunction
  public void close()
    throws IOException
  {
    if (writeInCurrentDocument_) {
      LOG.warn("close() called when document is not open.");
    }
    else {
      HtmlPage page = getPage();
      URL url = page.getUrl();
      StringWebResponse webResponse = new StringWebResponse(writeBuilder_.toString(), url);
      webResponse.setFromJavascript(true);
      writeInCurrentDocument_ = true;
      writeBuilder_.setLength(0);
      
      WebClient webClient = page.getWebClient();
      WebWindow window = page.getEnclosingWindow();
      webClient.loadWebResponseInto(webResponse, window);
    }
  }
  


  private void implicitCloseIfNecessary()
  {
    if (!writeInCurrentDocument_) {
      try {
        close();
      }
      catch (IOException e) {
        throw Context.throwAsScriptRuntimeEx(e);
      }
    }
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public Object getParentWindow()
  {
    return getWindow();
  }
  



  public Object appendChild(Object childObject)
  {
    throw Context.reportRuntimeError("Node cannot be inserted at the specified point in the hierarchy.");
  }
  




  @JsxFunction
  public Object getElementById(String id)
  {
    implicitCloseIfNecessary();
    Object result = null;
    DomElement domElement = getPage().getElementById(id);
    if (domElement == null)
    {
      if (LOG.isDebugEnabled()) {
        LOG.debug("getElementById(" + id + "): no DOM node found with this id");
      }
    }
    else {
      Object jsElement = getScriptableFor(domElement);
      if (jsElement == NOT_FOUND) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("getElementById(" + id + 
            ") cannot return a result as there isn't a JavaScript object for the HTML element " + 
            domElement.getClass().getName());
        }
      }
      else {
        result = jsElement;
      }
    }
    return result;
  }
  





  @JsxFunction
  public HTMLCollection getElementsByClassName(String className)
  {
    return ((HTMLElement)getDocumentElement()).getElementsByClassName(className);
  }
  








  @JsxFunction
  public HTMLCollection getElementsByName(String elementName)
  {
    implicitCloseIfNecessary();
    if ("null".equals(elementName)) {
      return HTMLCollection.emptyCollection(getWindow().getDomNodeOrDie());
    }
    
    final String expElementName = "null".equals(elementName) ? "" : elementName;
    
    final HtmlPage page = getPage();
    HTMLCollection collection = new HTMLCollection(page, true)
    {
      protected List<Object> computeElements() {
        return new ArrayList(page.getElementsByName(expElementName));
      }
      
      protected AbstractList.EffectOnCache getEffectOnCache(HtmlAttributeChangeEvent event)
      {
        if ("name".equals(event.getName())) {
          return AbstractList.EffectOnCache.RESET;
        }
        return AbstractList.EffectOnCache.NONE;
      }
      
    };
    return collection;
  }
  






  protected Object getWithPreemption(String name)
  {
    HtmlPage page = (HtmlPage)getDomNodeOrNull();
    if ((page == null) || (getBrowserVersion().hasFeature(BrowserVersionFeatures.HTMLDOCUMENT_GET_PREFERS_STANDARD_FUNCTIONS))) {
      Object response = getPrototype().get(name, this);
      if (response != NOT_FOUND) {
        return response;
      }
    }
    return getIt(name);
  }
  
  private Object getIt(final String name) {
    final HtmlPage page = (HtmlPage)getDomNodeOrNull();
    
    final boolean forIDAndOrName = getBrowserVersion().hasFeature(BrowserVersionFeatures.HTMLDOCUMENT_GET_FOR_ID_AND_OR_NAME);
    final boolean alsoFrames = getBrowserVersion().hasFeature(BrowserVersionFeatures.HTMLDOCUMENT_GET_ALSO_FRAMES);
    HTMLCollection collection = new HTMLCollection(page, true) {
      protected List<Object> computeElements() {
        List<DomElement> elements;
        List<DomElement> elements;
        if (forIDAndOrName) {
          elements = page.getElementsByIdAndOrName(name);
        }
        else {
          elements = page.getElementsByName(name);
        }
        List<Object> matchingElements = new ArrayList();
        for (DomElement elt : elements) {
          if (((elt instanceof HtmlForm)) || ((elt instanceof HtmlImage)) || ((elt instanceof HtmlApplet)) || (
            (alsoFrames) && ((elt instanceof BaseFrameElement)))) {
            matchingElements.add(elt);
          }
        }
        return matchingElements;
      }
      
      protected AbstractList.EffectOnCache getEffectOnCache(HtmlAttributeChangeEvent event)
      {
        String attributeName = event.getName();
        if ("name".equals(attributeName)) {
          return AbstractList.EffectOnCache.RESET;
        }
        if ((forIDAndOrName) && ("id".equals(attributeName))) {
          return AbstractList.EffectOnCache.RESET;
        }
        
        return AbstractList.EffectOnCache.NONE;
      }
      
      protected SimpleScriptable getScriptableFor(Object object)
      {
        if ((alsoFrames) && ((object instanceof BaseFrameElement))) {
          return (SimpleScriptable)((BaseFrameElement)object).getEnclosedWindow().getScriptableObject();
        }
        return super.getScriptableFor(object);
      }
      
    };
    int length = collection.getLength();
    if (length == 0) {
      return NOT_FOUND;
    }
    if (length == 1) {
      return collection.item(Integer.valueOf(0));
    }
    
    return collection;
  }
  



  @JsxGetter
  @CanSetReadOnly(CanSetReadOnlyStatus.EXCEPTION)
  public HTMLElement getBody()
  {
    HtmlPage page = getPage();
    HtmlElement body = page.getBody();
    if (body != null) {
      return (HTMLElement)body.getScriptableObject();
    }
    return null;
  }
  



  @JsxGetter
  public HTMLElement getHead()
  {
    HtmlElement head = getPage().getHead();
    if (head != null) {
      return (HTMLElement)head.getScriptableObject();
    }
    return null;
  }
  



  @JsxGetter
  public String getTitle()
  {
    return getPage().getTitleText();
  }
  



  @JsxSetter
  public void setTitle(String title)
  {
    getPage().setTitleText(title);
  }
  




  @JsxGetter
  public String getBgColor()
  {
    String color = getPage().getBody().getAttribute("bgColor");
    if ((color == DomElement.ATTRIBUTE_NOT_DEFINED) && (getBrowserVersion().hasFeature(BrowserVersionFeatures.HTMLDOCUMENT_COLOR))) {
      color = "#ffffff";
    }
    if ((getBrowserVersion().hasFeature(BrowserVersionFeatures.HTML_COLOR_EXPAND_ZERO)) && ("#0".equals(color))) {
      color = "#000000";
    }
    return color;
  }
  




  @JsxSetter
  public void setBgColor(String color)
  {
    HTMLBodyElement body = (HTMLBodyElement)getPage().getBody().getScriptableObject();
    body.setBgColor(color);
  }
  



  @JsxGetter
  public String getAlinkColor()
  {
    String color = getPage().getBody().getAttribute("aLink");
    if ((color == DomElement.ATTRIBUTE_NOT_DEFINED) && (getBrowserVersion().hasFeature(BrowserVersionFeatures.HTMLDOCUMENT_COLOR))) {
      color = "#0000ff";
    }
    if ((getBrowserVersion().hasFeature(BrowserVersionFeatures.HTML_COLOR_EXPAND_ZERO)) && ("#0".equals(color))) {
      color = "#000000";
    }
    return color;
  }
  



  @JsxSetter
  public void setAlinkColor(String color)
  {
    HTMLBodyElement body = (HTMLBodyElement)getPage().getBody().getScriptableObject();
    body.setALink(color);
  }
  



  @JsxGetter
  public String getLinkColor()
  {
    String color = getPage().getBody().getAttribute("link");
    if ((color == DomElement.ATTRIBUTE_NOT_DEFINED) && (getBrowserVersion().hasFeature(BrowserVersionFeatures.HTMLDOCUMENT_COLOR))) {
      color = "#0000ff";
    }
    if ((getBrowserVersion().hasFeature(BrowserVersionFeatures.HTML_COLOR_EXPAND_ZERO)) && ("#0".equals(color))) {
      color = "#000000";
    }
    return color;
  }
  



  @JsxSetter
  public void setLinkColor(String color)
  {
    HTMLBodyElement body = (HTMLBodyElement)getPage().getBody().getScriptableObject();
    body.setLink(color);
  }
  



  @JsxGetter
  public String getVlinkColor()
  {
    String color = getPage().getBody().getAttribute("vLink");
    if ((color == DomElement.ATTRIBUTE_NOT_DEFINED) && (getBrowserVersion().hasFeature(BrowserVersionFeatures.HTMLDOCUMENT_COLOR))) {
      color = "#800080";
    }
    if ((getBrowserVersion().hasFeature(BrowserVersionFeatures.HTML_COLOR_EXPAND_ZERO)) && ("#0".equals(color))) {
      color = "#000000";
    }
    return color;
  }
  



  @JsxSetter
  public void setVlinkColor(String color)
  {
    HTMLBodyElement body = (HTMLBodyElement)getPage().getBody().getScriptableObject();
    body.setVLink(color);
  }
  



  @JsxGetter
  public String getFgColor()
  {
    String color = getPage().getBody().getAttribute("text");
    if ((color == DomElement.ATTRIBUTE_NOT_DEFINED) && (getBrowserVersion().hasFeature(BrowserVersionFeatures.HTMLDOCUMENT_COLOR))) {
      color = "#000000";
    }
    if ((getBrowserVersion().hasFeature(BrowserVersionFeatures.HTML_COLOR_EXPAND_ZERO)) && ("#0".equals(color))) {
      color = "#000000";
    }
    return color;
  }
  



  @JsxSetter
  public void setFgColor(String color)
  {
    HTMLBodyElement body = (HTMLBodyElement)getPage().getBody().getScriptableObject();
    body.setText(color);
  }
  








  @JsxGetter
  public String getReadyState()
  {
    DomNode node = getDomNodeOrDie();
    return node.getReadyState();
  }
  






  @JsxGetter
  public String getDomain()
  {
    if (domain_ == null) {
      URL url = getPage().getUrl();
      if (url == WebClient.URL_ABOUT_BLANK) {
        WebWindow w = getWindow().getWebWindow();
        if ((w instanceof FrameWindow)) {
          url = ((FrameWindow)w).getEnclosingPage().getUrl();
        }
        else {
          return null;
        }
      }
      domain_ = url.getHost().toLowerCase(Locale.ROOT);
    }
    
    return domain_;
  }
  



























  @JsxSetter
  public void setDomain(String newDomain)
  {
    BrowserVersion browserVersion = getBrowserVersion();
    

    if ((WebClient.URL_ABOUT_BLANK == getPage().getUrl()) && 
      (browserVersion.hasFeature(BrowserVersionFeatures.JS_DOCUMENT_SETTING_DOMAIN_THROWS_FOR_ABOUT_BLANK))) {
      throw Context.reportRuntimeError("Illegal domain value, cannot set domain from \"" + 
        WebClient.URL_ABOUT_BLANK + "\" to: \"" + 
        newDomain + "\".");
    }
    
    newDomain = newDomain.toLowerCase(Locale.ROOT);
    
    String currentDomain = getDomain();
    if (currentDomain.equalsIgnoreCase(newDomain)) {
      return;
    }
    
    if (newDomain.indexOf('.') == -1) {
      throw Context.reportRuntimeError("Illegal domain value, cannot set domain from: \"" + 
        currentDomain + "\" to: \"" + newDomain + "\" (new domain has to contain a dot).");
    }
    
    if ((currentDomain.indexOf('.') > -1) && 
      (!currentDomain.toLowerCase(Locale.ROOT).endsWith("." + newDomain.toLowerCase(Locale.ROOT)))) {
      throw Context.reportRuntimeError("Illegal domain value, cannot set domain from: \"" + 
        currentDomain + "\" to: \"" + newDomain + "\"");
    }
    
    domain_ = newDomain;
  }
  



  @JsxGetter
  public Object getScripts()
  {
    new HTMLCollection(getDomNodeOrDie(), false)
    {
      protected boolean isMatching(DomNode node) {
        return node instanceof HtmlScript;
      }
    };
  }
  




  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public Object getFrames()
  {
    return getWindow().getFrames_js();
  }
  






  @JsxGetter
  public StyleSheetList getStyleSheets()
  {
    return new StyleSheetList(this);
  }
  









  @JsxFunction
  public Event createEvent(String eventType)
    throws DOMException
  {
    Class<? extends Event> clazz = null;
    clazz = (Class)SUPPORTED_DOM2_EVENT_TYPE_MAP.get(eventType);
    if (clazz == null) {
      clazz = (Class)SUPPORTED_DOM3_EVENT_TYPE_MAP.get(eventType);
      if ((CloseEvent.class == clazz) && 
        (getBrowserVersion().hasFeature(BrowserVersionFeatures.EVENT_ONCLOSE_DOCUMENT_CREATE_NOT_SUPPORTED))) {
        clazz = null;
      }
    }
    if ((clazz == null) && (
      ("Events".equals(eventType)) || 
      (("KeyEvents".equals(eventType)) && (getBrowserVersion().hasFeature(BrowserVersionFeatures.EVENT_TYPE_KEY_EVENTS))) || 
      (("HashChangeEvent".equals(eventType)) && 
      (getBrowserVersion().hasFeature(BrowserVersionFeatures.EVENT_TYPE_HASHCHANGEEVENT))) || 
      (("BeforeUnloadEvent".equals(eventType)) && 
      (getBrowserVersion().hasFeature(BrowserVersionFeatures.EVENT_TYPE_BEFOREUNLOADEVENT))) || 
      (("PointerEvent".equals(eventType)) && 
      (getBrowserVersion().hasFeature(BrowserVersionFeatures.EVENT_TYPE_POINTEREVENT))) || 
      ("PopStateEvent".equals(eventType)) || (
      ("ProgressEvent".equals(eventType)) && 
      (getBrowserVersion().hasFeature(BrowserVersionFeatures.EVENT_TYPE_PROGRESSEVENT))))) {
      clazz = (Class)SUPPORTED_VENDOR_EVENT_TYPE_MAP.get(eventType);
    }
    if (clazz == null) {
      Context.throwAsScriptRuntimeEx(new DOMException((short)9, 
        "Event Type is not supported: " + eventType));
      return null;
    }
    try {
      Event event = (Event)clazz.newInstance();
      event.setParentScope(getWindow());
      event.setPrototype(getPrototype(clazz));
      event.eventCreated();
      return event;
    }
    catch (InstantiationException e) {
      throw Context.reportRuntimeError("Failed to instantiate event: class ='" + clazz.getName() + 
        "' for event type of '" + eventType + "': " + e.getMessage());
    }
    catch (IllegalAccessException e) {
      throw Context.reportRuntimeError("Failed to instantiate event: class ='" + clazz.getName() + 
        "' for event type of '" + eventType + "': " + e.getMessage());
    }
  }
  







  @JsxFunction
  public Object elementFromPoint(int x, int y)
  {
    HtmlElement element = getPage().getElementFromPoint(x, y);
    return element == null ? null : element.getScriptableObject();
  }
  




  @JsxFunction
  public Range createRange()
  {
    Range r = new Range(this);
    r.setParentScope(getWindow());
    r.setPrototype(getPrototype(Range.class));
    return r;
  }
  






















  @JsxFunction
  public Object createTreeWalker(com.gargoylesoftware.htmlunit.javascript.host.dom.Node root, double whatToShow, Scriptable filter, boolean expandEntityReferences)
    throws DOMException
  {
    int whatToShowI = (int)Double.valueOf(whatToShow).longValue();
    
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_TREEWALKER_EXPAND_ENTITY_REFERENCES_FALSE)) {
      expandEntityReferences = false;
    }
    
    boolean filterFunctionOnly = getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_TREEWALKER_FILTER_FUNCTION_ONLY);
    NodeFilter filterWrapper = createFilterWrapper(filter, filterFunctionOnly);
    TreeWalker t = new TreeWalker(getPage(), root, whatToShowI, filterWrapper, expandEntityReferences);
    t.setParentScope(getWindow(this));
    t.setPrototype(staticGetPrototype(getWindow(this), TreeWalker.class));
    return t;
  }
  
  private static NodeFilter createFilterWrapper(Scriptable filter, final boolean filterFunctionOnly)
  {
    NodeFilter filterWrapper = null;
    if (filter != null) {
      filterWrapper = new NodeFilter()
      {
        public short acceptNode(org.w3c.dom.Node n) {
          Object[] args = { ((DomNode)n).getScriptableObject() };
          Object response;
          Object response; if ((HTMLDocument.this instanceof Callable)) {
            response = ((Callable)HTMLDocument.this).call(Context.getCurrentContext(), HTMLDocument.this, HTMLDocument.this, args);
          }
          else {
            if (filterFunctionOnly) {
              throw Context.reportRuntimeError("only a function is allowed as filter");
            }
            response = ScriptableObject.callMethod(HTMLDocument.this, "acceptNode", args);
          }
          return (short)(int)Context.toNumber(response);
        }
      };
    }
    return filterWrapper;
  }
  

  private static Scriptable staticGetPrototype(Window window, Class<? extends SimpleScriptable> javaScriptClass)
  {
    Scriptable prototype = window.getPrototype(javaScriptClass);
    if ((prototype == null) && (javaScriptClass != SimpleScriptable.class)) {
      return staticGetPrototype(window, javaScriptClass.getSuperclass());
    }
    return prototype;
  }
  





  @JsxFunction
  public boolean queryCommandSupported(String cmd)
  {
    return hasCommand(cmd, true);
  }
  
  private boolean hasCommand(String cmd, boolean includeBold) {
    if (cmd == null) {
      return false;
    }
    
    String cmdLC = cmd.toLowerCase(Locale.ROOT);
    if (getBrowserVersion().isIE()) {
      return EXECUTE_CMDS_IE.contains(cmdLC);
    }
    if (getBrowserVersion().isChrome()) {
      return (EXECUTE_CMDS_CHROME.contains(cmdLC)) || ((includeBold) && ("bold".equalsIgnoreCase(cmd)));
    }
    return EXECUTE_CMDS_FF.contains(cmdLC);
  }
  





  @JsxFunction
  public boolean queryCommandEnabled(String cmd)
  {
    return hasCommand(cmd, true);
  }
  







  @JsxFunction
  public boolean execCommand(String cmd, boolean userInterface, Object value)
  {
    if (!hasCommand(cmd, false)) {
      return false;
    }
    LOG.warn("Nothing done for execCommand(" + cmd + ", ...) (feature not implemented)");
    return true;
  }
  




  @JsxGetter
  public HTMLElement getActiveElement()
  {
    if (activeElement_ == null) {
      HtmlElement body = getPage().getBody();
      if (body != null) {
        activeElement_ = ((HTMLElement)getScriptableFor(body));
      }
    }
    
    return activeElement_;
  }
  




  @JsxFunction
  public boolean hasFocus()
  {
    return (activeElement_ != null) && (getPage().getFocusedElement() == activeElement_.getDomNodeOrDie());
  }
  






  public void setActiveElement(HTMLElement element)
  {
    activeElement_ = element;
    
    if (element != null)
    {

      WebWindow window = element.getDomNodeOrDie().getPage().getEnclosingWindow();
      if ((window instanceof FrameWindow)) {
        BaseFrameElement frame = ((FrameWindow)window).getFrameElement();
        if ((frame instanceof HtmlInlineFrame)) {
          Window winWithFrame = (Window)frame.getPage().getEnclosingWindow().getScriptableObject();
          ((HTMLDocument)winWithFrame.getDocument()).setActiveElement(
            (HTMLElement)frame.getScriptableObject());
        }
      }
    }
  }
  









  @JsxFunction
  public boolean dispatchEvent(Event event)
  {
    event.setTarget(this);
    ScriptResult result = fireEvent(event);
    return !event.isAborted(result);
  }
  





  @JsxFunction
  public NodeList querySelectorAll(String selectors)
  {
    try
    {
      List<Object> nodes = new ArrayList();
      for (DomNode domNode : getDomNodeOrDie().querySelectorAll(selectors)) {
        nodes.add(domNode.getScriptableObject());
      }
      return NodeList.staticNodeList(this, nodes);
    }
    catch (CSSException e) {
      throw Context.reportRuntimeError("An invalid or illegal selector was specified (selector: '" + 
        selectors + "' error: " + e.getMessage() + ").");
    }
  }
  



  @JsxFunction
  public com.gargoylesoftware.htmlunit.javascript.host.dom.Node querySelector(String selectors)
  {
    try
    {
      DomNode node = getDomNodeOrDie().querySelector(selectors);
      if (node != null) {
        return (com.gargoylesoftware.htmlunit.javascript.host.dom.Node)node.getScriptableObject();
      }
      return null;
    }
    catch (CSSException e) {
      throw Context.reportRuntimeError("An invalid or illegal selector was specified (selector: '" + 
        selectors + "' error: " + e.getMessage() + ").");
    }
  }
  



  public Object get(String name, Scriptable start)
  {
    Object response = super.get(name, start);
    


    if (((response instanceof FunctionObject)) && 
      (("querySelectorAll".equals(name)) || ("querySelector".equals(name))) && 
      (getBrowserVersion().hasFeature(BrowserVersionFeatures.QUERYSELECTORALL_NOT_IN_QUIRKS))) {
      Document document = null;
      if ((start instanceof DocumentProxy))
      {
        document = ((DocumentProxy)start).getDelegee();
      }
      else {
        DomNode page = ((HTMLDocument)start).getDomNodeOrNull();
        if (page != null) {
          document = (Document)page.getScriptableObject();
        }
      }
      if ((document != null) && ((document instanceof HTMLDocument)) && 
        (((HTMLDocument)document).getDocumentMode() < 8)) {
        return NOT_FOUND;
      }
    }
    
    return response;
  }
  





  @JsxFunction
  public void clear() {}
  





  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setHead(ScriptableObject head) {}
  





  @JsxFunction
  public Selection getSelection()
  {
    return getWindow().getSelectionImpl();
  }
  



  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public boolean releaseCapture()
  {
    return true;
  }
  








  @JsxFunction
  public NodeIterator createNodeIterator(com.gargoylesoftware.htmlunit.javascript.host.dom.Node root, int whatToShow, Scriptable filter)
  {
    NodeFilter filterWrapper = createFilterWrapper(filter, false);
    NodeIterator iterator = new NodeIterator(getPage(), root, whatToShow, filterWrapper);
    iterator.setParentScope(getParentScope());
    iterator.setPrototype(getPrototype(iterator.getClass()));
    return iterator;
  }
  






  public Attr createAttribute(String attributeName)
  {
    String name = attributeName;
    if ((org.apache.commons.lang3.StringUtils.isNotEmpty(name)) && 
      (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOCUMENT_CREATE_ATTRUBUTE_LOWER_CASE))) {
      name = name.toLowerCase();
    }
    
    return super.createAttribute(name);
  }
}
