package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.PostponedAction;
import com.gargoylesoftware.htmlunit.util.UrlUtils;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Attr;
































public abstract class BaseFrameElement
  extends HtmlElement
{
  private static final Log LOG = LogFactory.getLog(BaseFrameElement.class);
  private FrameWindow enclosedWindow_;
  private boolean contentLoaded_ = false;
  private boolean createdByJavascript_ = false;
  private boolean loadSrcWhenAddedToPage_ = false;
  







  protected BaseFrameElement(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
    
    init();
    
    if ((page != null) && (page.isHtmlPage()) && (((HtmlPage)page).isParsingHtmlSnippet()))
    {

      String src = getSrcAttribute();
      if ((src != ATTRIBUTE_NOT_DEFINED) && (!"about:blank".equals(src))) {
        loadSrcWhenAddedToPage_ = true;
      }
    }
  }
  
  private void init() {
    FrameWindow enclosedWindow = null;
    try {
      HtmlPage htmlPage = getHtmlPageOrNull();
      if (htmlPage != null) {
        enclosedWindow = new FrameWindow(this);
        

        WebClient webClient = htmlPage.getWebClient();
        HtmlPage temporaryPage = (HtmlPage)webClient.getPage(enclosedWindow, 
          new WebRequest(WebClient.URL_ABOUT_BLANK));
        temporaryPage.setReadyState("loading");
      }
    }
    catch (FailingHttpStatusCodeException localFailingHttpStatusCodeException) {}catch (IOException localIOException) {}
    




    enclosedWindow_ = enclosedWindow;
  }
  








  public void loadInnerPage()
    throws FailingHttpStatusCodeException
  {
    String source = getSrcAttribute();
    if ((source.isEmpty()) || (StringUtils.startsWithIgnoreCase(source, "about:"))) {
      source = "about:blank";
    }
    
    loadInnerPageIfPossible(source);
    
    Page enclosedPage = getEnclosedPage();
    if ((enclosedPage != null) && (enclosedPage.isHtmlPage())) {
      final HtmlPage htmlPage = (HtmlPage)enclosedPage;
      JavaScriptEngine jsEngine = getPage().getWebClient().getJavaScriptEngine();
      if (jsEngine.isScriptRunning()) {
        PostponedAction action = new PostponedAction(getPage())
        {
          public void execute() throws Exception {
            htmlPage.setReadyState("complete");
          }
        };
        jsEngine.addPostponedAction(action);
      }
      else {
        htmlPage.setReadyState("complete");
      }
    }
  }
  





  boolean isContentLoaded()
  {
    return contentLoaded_;
  }
  




  void setContentLoaded()
  {
    contentLoaded_ = true;
  }
  


  private void loadInnerPageIfPossible(String src)
    throws FailingHttpStatusCodeException
  {
    setContentLoaded();
    if (!src.isEmpty())
    {
      try {
        url = ((HtmlPage)getPage()).getFullyQualifiedUrl(src);
      } catch (MalformedURLException e) {
        URL url;
        notifyIncorrectness("Invalid src attribute of " + getTagName() + ": url=[" + src + "]. Ignored."); return;
      }
      URL url;
      if (isAlreadyLoadedByAncestor(url)) {
        notifyIncorrectness("Recursive src attribute of " + getTagName() + ": url=[" + src + "]. Ignored.");
        return;
      }
      try {
        WebRequest request = new WebRequest(url);
        request.setAdditionalHeader("Referer", getPage().getUrl().toExternalForm());
        getPage().getEnclosingWindow().getWebClient().getPage(enclosedWindow_, request);
      }
      catch (IOException e) {
        LOG.error("IOException when getting content for " + getTagName() + ": url=[" + url + "]", e);
      }
    }
  }
  




  private boolean isAlreadyLoadedByAncestor(URL url)
  {
    WebWindow window = getPage().getEnclosingWindow();
    while (window != null) {
      if (UrlUtils.sameFile(url, window.getEnclosedPage().getUrl())) {
        return true;
      }
      if (window == window.getParentWindow())
      {
        window = null;
      }
      else {
        window = window.getParentWindow();
      }
    }
    return false;
  }
  






  public final String getLongDescAttribute()
  {
    return getAttribute("longdesc");
  }
  






  public final String getNameAttribute()
  {
    return getAttribute("name");
  }
  




  public final void setNameAttribute(String name)
  {
    setAttribute("name", name);
  }
  






  public final String getSrcAttribute()
  {
    return getSrcAttributeNormalized();
  }
  






  public final String getFrameBorderAttribute()
  {
    return getAttribute("frameborder");
  }
  






  public final String getMarginWidthAttribute()
  {
    return getAttribute("marginwidth");
  }
  






  public final String getMarginHeightAttribute()
  {
    return getAttribute("marginheight");
  }
  






  public final String getNoResizeAttribute()
  {
    return getAttribute("noresize");
  }
  






  public final String getScrollingAttribute()
  {
    return getAttribute("scrolling");
  }
  






  public final String getOnLoadAttribute()
  {
    return getAttribute("onload");
  }
  





  public Page getEnclosedPage()
  {
    return getEnclosedWindow().getEnclosedPage();
  }
  



  public FrameWindow getEnclosedWindow()
  {
    return enclosedWindow_;
  }
  



  public final void setSrcAttribute(String attribute)
  {
    setAttribute("src", attribute);
  }
  




  public void setAttributeNS(String namespaceURI, String qualifiedName, String attributeValue, boolean notifyAttributeChangeListeners)
  {
    if ((attributeValue != null) && ("src".equals(qualifiedName))) {
      attributeValue = attributeValue.trim();
    }
    
    super.setAttributeNS(namespaceURI, qualifiedName, attributeValue, notifyAttributeChangeListeners);
    
    if (("src".equals(qualifiedName)) && ("about:blank" != attributeValue)) {
      if (isAttachedToPage()) {
        loadSrc();
      }
      else {
        loadSrcWhenAddedToPage_ = true;
      }
    }
  }
  



  public Attr setAttributeNode(Attr attribute)
  {
    String qualifiedName = attribute.getName();
    String attributeValue = null;
    if ("src".equals(qualifiedName)) {
      attributeValue = attribute.getValue().trim();
    }
    
    Attr result = super.setAttributeNode(attribute);
    
    if (("src".equals(qualifiedName)) && ("about:blank" != attributeValue)) {
      if (isAttachedToPage()) {
        loadSrc();
      }
      else {
        loadSrcWhenAddedToPage_ = true;
      }
    }
    
    return result;
  }
  
  private void loadSrc() {
    loadSrcWhenAddedToPage_ = false;
    final String src = getSrcAttribute();
    
    JavaScriptEngine jsEngine = getPage().getWebClient().getJavaScriptEngine();
    


    if ((!jsEngine.isScriptRunning()) || (src.startsWith("javascript:"))) {
      loadInnerPageIfPossible(src);
    }
    else {
      final Page pageInFrame = getEnclosedPage();
      PostponedAction action = new PostponedAction(getPage())
      {
        public void execute() throws Exception {
          if ((!src.isEmpty()) && (getSrcAttribute().equals(src))) {
            loadInnerPage();
          }
        }
        

        public boolean isStillAlive()
        {
          return (super.isStillAlive()) && (pageInFrame == getEnclosedPage());
        }
      };
      jsEngine.addPostponedAction(action);
    }
  }
  
  protected void onAddedToPage()
  {
    super.onAddedToPage();
    
    if (loadSrcWhenAddedToPage_) {
      loadSrc();
    }
  }
  





  public void markAsCreatedByJavascript()
  {
    createdByJavascript_ = true;
  }
  





  public void unmarkAsCreatedByJavascript()
  {
    createdByJavascript_ = false;
  }
  






  public boolean wasCreatedByJavascript()
  {
    return createdByJavascript_;
  }
  




  public DomNode cloneNode(boolean deep)
  {
    BaseFrameElement clone = (BaseFrameElement)super.cloneNode(deep);
    clone.init();
    return clone;
  }
  




  public void remove()
  {
    super.remove();
    getEnclosedWindow().close();
  }
}
