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
import com.gargoylesoftware.htmlunit.javascript.host.dom.Document;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventHandler;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventListenersContainer;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLScriptElement;
import com.gargoylesoftware.htmlunit.util.EncodingSniffer;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import net.sourceforge.htmlunit.corejs.javascript.BaseFunction;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;






































public class HtmlScript
  extends HtmlElement
  implements ScriptElement
{
  private static final Log LOG = LogFactory.getLog(HtmlScript.class);
  


  public static final String TAG_NAME = "script";
  


  private static final String SLASH_SLASH_COLON = "//:";
  

  private boolean executed_;
  

  private boolean createdByJavascript_;
  


  HtmlScript(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  



  public final String getCharsetAttribute()
  {
    return getAttribute("charset");
  }
  



  public final Charset getCharset()
  {
    String charsetName = getCharsetAttribute();
    return EncodingSniffer.toCharset(charsetName);
  }
  







  public final String getTypeAttribute()
  {
    return getAttribute("type");
  }
  







  public final String getLanguageAttribute()
  {
    return getAttribute("language");
  }
  



  public final String getSrcAttribute()
  {
    return getSrcAttributeNormalized();
  }
  



  public final String getEventAttribute()
  {
    return getAttribute("event");
  }
  



  public final String getHtmlForAttribute()
  {
    return getAttribute("for");
  }
  







  public final String getDeferAttribute()
  {
    return getAttribute("defer");
  }
  



  protected boolean isDeferred()
  {
    return getDeferAttribute() != ATTRIBUTE_NOT_DEFINED;
  }
  



  public boolean mayBeDisplayed()
  {
    return false;
  }
  






  public void setAttributeNS(String namespaceURI, String qualifiedName, String attributeValue, boolean notifyAttributeChangeListeners)
  {
    if ((namespaceURI != null) || (!"src".equals(qualifiedName))) {
      super.setAttributeNS(namespaceURI, qualifiedName, attributeValue, notifyAttributeChangeListeners);
      return;
    }
    
    String oldValue = getAttributeNS(namespaceURI, qualifiedName);
    super.setAttributeNS(namespaceURI, qualifiedName, attributeValue, notifyAttributeChangeListeners);
    
    if ((isAttachedToPage()) && (oldValue.isEmpty()) && (getFirstChild() == null)) {
      PostponedAction action = new PostponedAction(getPage())
      {
        public void execute() {
          executeScriptIfNeeded();
        }
      };
      JavaScriptEngine engine = getPage().getWebClient().getJavaScriptEngine();
      engine.addPostponedAction(action);
    }
  }
  




  protected void onAllChildrenAddedToPage(boolean postponed)
  {
    if ((getOwnerDocument() instanceof XmlPage)) {
      return;
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("Script node added: " + asXml());
    }
    
    PostponedAction action = new PostponedAction(getPage(), "Execution of script " + this)
    {
      public void execute() {
        HTMLDocument jsDoc = (HTMLDocument)
          ((Window)getPage().getEnclosingWindow().getScriptableObject()).getDocument();
        jsDoc.setExecutingDynamicExternalPosponed((getStartLineNumber() == -1) && 
          (getSrcAttribute() != HtmlScript.ATTRIBUTE_NOT_DEFINED));
        try
        {
          executeScriptIfNeeded();
        }
        finally {
          jsDoc.setExecutingDynamicExternalPosponed(false);
        }
        
      }
    };
    JavaScriptEngine engine = getPage().getWebClient().getJavaScriptEngine();
    if ((hasAttribute("async")) && (!engine.isScriptRunning())) {
      HtmlPage owningPage = getHtmlPageOrNull();
      owningPage.addAfterLoadAction(action);
    }
    else if ((hasAttribute("async")) || (
      (postponed) && (StringUtils.isBlank(getTextContent())))) {
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
  


  private void executeInlineScriptIfNeeded()
  {
    if (!isExecutionNeeded()) {
      return;
    }
    
    String src = getSrcAttribute();
    if (src != ATTRIBUTE_NOT_DEFINED) {
      return;
    }
    
    String forr = getHtmlForAttribute();
    String event = getEventAttribute();
    
    if (event.endsWith("()")) {
      event = event.substring(0, event.length() - 2);
    }
    
    String scriptCode = getScriptCode();
    if ((event != ATTRIBUTE_NOT_DEFINED) && (forr != ATTRIBUTE_NOT_DEFINED) && 
      (hasFeature(BrowserVersionFeatures.JS_SCRIPT_SUPPORTS_FOR_AND_EVENT_WINDOW)) && ("window".equals(forr))) {
      Window window = (Window)getPage().getEnclosingWindow().getScriptableObject();
      BaseFunction function = new EventHandler(this, event, scriptCode);
      window.getEventListenersContainer().addEventListener(StringUtils.substring(event, 2), function, false);
      return;
    }
    if ((forr == ATTRIBUTE_NOT_DEFINED) || ("onload".equals(event))) {
      String url = getPage().getUrl().toExternalForm();
      int line1 = getStartLineNumber();
      int line2 = getEndLineNumber();
      int col1 = getStartColumnNumber();
      int col2 = getEndColumnNumber();
      String desc = "script in " + url + " from (" + line1 + ", " + col1 + 
        ") to (" + line2 + ", " + col2 + ")";
      
      executed_ = true;
      ((HtmlPage)getPage()).executeJavaScriptIfPossible(scriptCode, desc, line1);
    }
  }
  


  private String getScriptCode()
  {
    Iterable<DomNode> textNodes = getChildren();
    StringBuilder scriptCode = new StringBuilder();
    for (DomNode node : textNodes) {
      if ((node instanceof DomText)) {
        DomText domText = (DomText)node;
        scriptCode.append(domText.getData());
      }
    }
    return scriptCode.toString();
  }
  




  public void executeScriptIfNeeded()
  {
    if (!isExecutionNeeded()) {
      return;
    }
    
    HtmlPage page = (HtmlPage)getPage();
    
    String src = getSrcAttribute();
    if (src.equals("//:")) {
      executeEvent("error");
      return;
    }
    
    if (src != ATTRIBUTE_NOT_DEFINED) {
      if (!src.startsWith("javascript:"))
      {
        if (LOG.isDebugEnabled()) {
          LOG.debug("Loading external JavaScript: " + src);
        }
        try {
          executed_ = true;
          HtmlPage.JavaScriptLoadResult result = page.loadExternalJavaScriptFile(src, getCharset());
          if (result == HtmlPage.JavaScriptLoadResult.SUCCESS) {
            executeEvent("load");
          }
          else if (result == HtmlPage.JavaScriptLoadResult.DOWNLOAD_ERROR) {
            executeEvent("error");
          }
        }
        catch (FailingHttpStatusCodeException e) {
          executeEvent("error");
          throw e;
        }
      }
    }
    else if (getFirstChild() != null)
    {
      executeInlineScriptIfNeeded();
      
      if (hasFeature(BrowserVersionFeatures.EVENT_ONLOAD_INTERNAL_JAVASCRIPT)) {
        executeEvent("load");
      }
    }
  }
  
  private void executeEvent(String type) {
    HTMLScriptElement script = (HTMLScriptElement)getScriptableObject();
    Event event = new Event(this, type);
    script.executeEventLocally(event);
  }
  




  private boolean isExecutionNeeded()
  {
    if (executed_) {
      return false;
    }
    
    if (!isAttachedToPage()) {
      return false;
    }
    

    SgmlPage page = getPage();
    if (!page.getWebClient().getOptions().isJavaScriptEnabled()) {
      return false;
    }
    

    HtmlPage htmlPage = getHtmlPageOrNull();
    if ((htmlPage != null) && (htmlPage.isParsingHtmlSnippet())) {
      return false;
    }
    

    for (DomNode o = this; o != null; o = o.getParentNode()) {
      if (((o instanceof HtmlInlineFrame)) || ((o instanceof HtmlNoFrames))) {
        return false;
      }
    }
    


    if ((page.getEnclosingWindow() != null) && (page.getEnclosingWindow().getEnclosedPage() != page)) {
      return false;
    }
    

    if (!isJavaScript(getTypeAttribute(), getLanguageAttribute())) {
      String t = getTypeAttribute();
      String l = getLanguageAttribute();
      LOG.warn("Script is not JavaScript (type: " + t + ", language: " + l + "). Skipping execution.");
      return false;
    }
    



    if (!getPage().isAncestorOf(this)) {
      return false;
    }
    
    return true;
  }
  









  boolean isJavaScript(String typeAttribute, String languageAttribute)
  {
    BrowserVersion browserVersion = getPage().getWebClient().getBrowserVersion();
    
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
  






  protected void setAndExecuteReadyState(String state) {}
  





  public String asText()
  {
    return "";
  }
  





  protected boolean isEmptyXmlTagExpanded()
  {
    return true;
  }
  



  protected void printChildrenAsXml(String indent, PrintWriter printWriter)
  {
    DomCharacterData textNode = (DomCharacterData)getFirstChild();
    if (textNode == null) {
      return;
    }
    
    String data = textNode.getData();
    if (data.contains("//<![CDATA[")) {
      printWriter.print(data);
      printWriter.print("\r\n");
    }
    else {
      printWriter.print("//<![CDATA[");
      printWriter.print("\r\n");
      printWriter.print(data);
      printWriter.print("\r\n");
      printWriter.print("//]]>");
      printWriter.print("\r\n");
    }
  }
  





  public void resetExecuted()
  {
    executed_ = false;
  }
  
  public void processImportNode(Document doc)
  {
    super.processImportNode(doc);
    
    executed_ = true;
  }
  




  public String toString()
  {
    StringWriter writer = new StringWriter();
    PrintWriter printWriter = new PrintWriter(writer);
    
    printWriter.print(getClass().getSimpleName());
    printWriter.print("[<");
    printOpeningTagContentAsXml(printWriter);
    printWriter.print(">");
    printWriter.print(getScriptCode());
    printWriter.print("]");
    printWriter.flush();
    return writer.toString();
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    return HtmlElement.DisplayStyle.NONE;
  }
  





  public void markAsCreatedByJavascript()
  {
    createdByJavascript_ = true;
  }
  






  public boolean wasCreatedByJavascript()
  {
    return createdByJavascript_;
  }
  



  public boolean isExecuted()
  {
    return executed_;
  }
  



  public void setExecuted(boolean executed)
  {
    executed_ = executed;
  }
}
