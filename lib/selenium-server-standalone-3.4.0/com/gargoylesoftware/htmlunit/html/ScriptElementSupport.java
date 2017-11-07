package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.PostponedAction;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventHandler;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventListenersContainer;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import java.net.URL;
import net.sourceforge.htmlunit.corejs.javascript.BaseFunction;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;






















public final class ScriptElementSupport
{
  private static final Log LOG = LogFactory.getLog(ScriptElementSupport.class);
  



  private static final String SLASH_SLASH_COLON = "//:";
  



  private ScriptElementSupport() {}
  



  public static void onAllChildrenAddedToPage(final DomElement element, boolean postponed)
  {
    if ((element.getOwnerDocument() instanceof XmlPage)) {
      return;
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("Script node added: " + element.asXml());
    }
    
    PostponedAction action = new PostponedAction(element.getPage(), "Execution of script " + element)
    {
      public void execute() {
        HTMLDocument jsDoc = (HTMLDocument)
          ((Window)element.getPage().getEnclosingWindow().getScriptableObject()).getDocument();
        jsDoc.setExecutingDynamicExternalPosponed((element.getStartLineNumber() == -1) && 
          (((ScriptElement)element).getSrcAttribute() != DomElement.ATTRIBUTE_NOT_DEFINED));
        try
        {
          ScriptElementSupport.executeScriptIfNeeded(element);
        }
        finally {
          jsDoc.setExecutingDynamicExternalPosponed(false);
        }
        
      }
    };
    JavaScriptEngine engine = element.getPage().getWebClient().getJavaScriptEngine();
    if ((element.hasAttribute("async")) && (!engine.isScriptRunning())) {
      HtmlPage owningPage = element.getHtmlPageOrNull();
      owningPage.addAfterLoadAction(action);
    }
    else if ((element.hasAttribute("async")) || (
      (postponed) && (StringUtils.isBlank(element.getTextContent())))) {
      engine.addPostponedAction(action);
    }
    else {
      try {
        action.execute();
      }
      catch (RuntimeException e) {
        throw e;
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }
  





  public static void executeScriptIfNeeded(DomElement element)
  {
    if (!isExecutionNeeded(element)) {
      return;
    }
    
    HtmlPage page = (HtmlPage)element.getPage();
    
    String src = ((ScriptElement)element).getSrcAttribute();
    if (src.equals("//:")) {
      executeEvent(element, "error");
      return;
    }
    
    if (src != DomElement.ATTRIBUTE_NOT_DEFINED) {
      if (!src.startsWith("javascript:"))
      {
        if (LOG.isDebugEnabled()) {
          LOG.debug("Loading external JavaScript: " + src);
        }
        try {
          ScriptElement scriptElement = (ScriptElement)element;
          scriptElement.setExecuted(true);
          HtmlPage.JavaScriptLoadResult result = 
            page.loadExternalJavaScriptFile(src, scriptElement.getCharset());
          if (result == HtmlPage.JavaScriptLoadResult.SUCCESS) {
            executeEvent(element, "load");
          }
          else if (result == HtmlPage.JavaScriptLoadResult.DOWNLOAD_ERROR) {
            executeEvent(element, "error");
          }
        }
        catch (FailingHttpStatusCodeException e) {
          executeEvent(element, "error");
          throw e;
        }
      }
    }
    else if (element.getFirstChild() != null)
    {
      executeInlineScriptIfNeeded(element);
      
      if (element.hasFeature(BrowserVersionFeatures.EVENT_ONLOAD_INTERNAL_JAVASCRIPT)) {
        executeEvent(element, "load");
      }
    }
  }
  





  private static boolean isExecutionNeeded(DomElement element)
  {
    if (((ScriptElement)element).isExecuted()) {
      return false;
    }
    
    if (!element.isAttachedToPage()) {
      return false;
    }
    

    SgmlPage page = element.getPage();
    if (!page.getWebClient().getOptions().isJavaScriptEnabled()) {
      return false;
    }
    

    HtmlPage htmlPage = element.getHtmlPageOrNull();
    if ((htmlPage != null) && (htmlPage.isParsingHtmlSnippet())) {
      return false;
    }
    

    for (DomNode o = element; o != null; o = o.getParentNode()) {
      if (((o instanceof HtmlInlineFrame)) || ((o instanceof HtmlNoFrames))) {
        return false;
      }
    }
    


    if ((page.getEnclosingWindow() != null) && (page.getEnclosingWindow().getEnclosedPage() != page)) {
      return false;
    }
    

    String t = element.getAttribute("type");
    String l = element.getAttribute("language");
    if (!isJavaScript(element, t, l)) {
      LOG.warn("Script is not JavaScript (type: " + t + ", language: " + l + "). Skipping execution.");
      return false;
    }
    



    return element.getPage().isAncestorOf(element);
  }
  










  static boolean isJavaScript(DomElement element, String typeAttribute, String languageAttribute)
  {
    BrowserVersion browserVersion = element.getPage().getWebClient().getBrowserVersion();
    
    if (browserVersion.hasFeature(BrowserVersionFeatures.HTMLSCRIPT_TRIM_TYPE)) {
      typeAttribute = typeAttribute.trim();
    }
    
    if (StringUtils.isNotEmpty(typeAttribute)) {
      if (("text/javascript".equalsIgnoreCase(typeAttribute)) || 
        ("text/ecmascript".equalsIgnoreCase(typeAttribute))) {
        return true;
      }
      
      if (("application/javascript".equalsIgnoreCase(typeAttribute)) || 
        ("application/ecmascript".equalsIgnoreCase(typeAttribute)) || 
        ("application/x-javascript".equalsIgnoreCase(typeAttribute))) {
        return true;
      }
      return false;
    }
    
    if (StringUtils.isNotEmpty(languageAttribute)) {
      return StringUtils.startsWithIgnoreCase(languageAttribute, "javascript");
    }
    return true;
  }
  
  private static void executeEvent(DomElement element, String type) {
    EventTarget eventTarget = (EventTarget)element.getScriptableObject();
    Event event = new Event(element, type);
    eventTarget.executeEventLocally(event);
  }
  


  private static void executeInlineScriptIfNeeded(DomElement element)
  {
    if (!isExecutionNeeded(element)) {
      return;
    }
    
    String src = ((ScriptElement)element).getSrcAttribute();
    if (src != DomElement.ATTRIBUTE_NOT_DEFINED) {
      return;
    }
    
    String forr = element.getAttribute("for");
    String event = element.getAttribute("event");
    
    if (event.endsWith("()")) {
      event = event.substring(0, event.length() - 2);
    }
    
    String scriptCode = getScriptCode(element);
    if ((event != DomElement.ATTRIBUTE_NOT_DEFINED) && (forr != DomElement.ATTRIBUTE_NOT_DEFINED) && 
      (element.hasFeature(BrowserVersionFeatures.JS_SCRIPT_SUPPORTS_FOR_AND_EVENT_WINDOW)) && ("window".equals(forr))) {
      Window window = (Window)element.getPage().getEnclosingWindow().getScriptableObject();
      BaseFunction function = new EventHandler(element, event, scriptCode);
      window.getEventListenersContainer().addEventListener(StringUtils.substring(event, 2), function, false);
      return;
    }
    
    if ((forr == DomElement.ATTRIBUTE_NOT_DEFINED) || ("onload".equals(event))) {
      String url = element.getPage().getUrl().toExternalForm();
      int line1 = element.getStartLineNumber();
      int line2 = element.getEndLineNumber();
      int col1 = element.getStartColumnNumber();
      int col2 = element.getEndColumnNumber();
      String desc = "script in " + url + " from (" + line1 + ", " + col1 + 
        ") to (" + line2 + ", " + col2 + ")";
      
      ((ScriptElement)element).setExecuted(true);
      ((HtmlPage)element.getPage()).executeJavaScriptIfPossible(scriptCode, desc, line1);
    }
  }
  


  private static String getScriptCode(DomElement element)
  {
    Iterable<DomNode> textNodes = element.getChildren();
    StringBuilder scriptCode = new StringBuilder();
    for (DomNode node : textNodes) {
      if ((node instanceof DomText)) {
        DomText domText = (DomText)node;
        scriptCode.append(domText.getData());
      }
    }
    return scriptCode.toString();
  }
}
