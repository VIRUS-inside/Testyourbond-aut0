package com.gargoylesoftware.htmlunit;

import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.javascript.background.BackgroundJavaScriptFactory;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

































public abstract class WebWindowImpl
  implements WebWindow
{
  private static final Log LOG = LogFactory.getLog(WebWindowImpl.class);
  
  private WebClient webClient_;
  private Page enclosedPage_;
  private ScriptableObject scriptObject_;
  private JavaScriptJobManager jobManager_;
  private final List<WebWindowImpl> childWindows_ = new ArrayList();
  private String name_ = "";
  private final History history_ = new History(this);
  
  private boolean closed_;
  
  private int innerHeight_;
  
  private int outerHeight_;
  
  private int innerWidth_;
  
  private int outerWidth_;
  
  public WebWindowImpl(WebClient webClient)
  {
    WebAssert.notNull("webClient", webClient);
    webClient_ = webClient;
    jobManager_ = BackgroundJavaScriptFactory.theFactory().createJavaScriptJobManager(this);
    
    boolean plus16 = false;
    innerHeight_ = 605;
    if (webClient.getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_WINDOW_OUTER_INNER_HEIGHT_DIFF_63)) {
      outerHeight_ = (innerHeight_ + 63);
      plus16 = true;
    }
    else if (webClient.getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_WINDOW_OUTER_INNER_HEIGHT_DIFF_94)) {
      outerHeight_ = (innerHeight_ + 94);
    }
    else if (webClient.getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_WINDOW_OUTER_INNER_HEIGHT_DIFF_93)) {
      outerHeight_ = (innerHeight_ + 93);
      plus16 = true;
    }
    else {
      outerHeight_ = (innerHeight_ + 115);
    }
    innerWidth_ = 1256;
    if (plus16) {
      outerWidth_ = (innerWidth_ + 16);
    }
    else {
      outerWidth_ = (innerWidth_ + 14);
    }
  }
  


  protected void performRegistration()
  {
    webClient_.registerWebWindow(this);
  }
  



  public WebClient getWebClient()
  {
    return webClient_;
  }
  



  public Page getEnclosedPage()
  {
    return enclosedPage_;
  }
  



  public void setEnclosedPage(Page page)
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("setEnclosedPage: " + page);
    }
    if (page == enclosedPage_) {
      return;
    }
    destroyChildren();
    enclosedPage_ = page;
    history_.addPage(page);
    if (isJavaScriptInitializationNeeded()) {
      webClient_.initialize(this);
    }
    webClient_.initialize(page);
  }
  




  protected abstract boolean isJavaScriptInitializationNeeded();
  



  public void setScriptableObject(ScriptableObject scriptObject)
  {
    scriptObject_ = scriptObject;
  }
  



  public ScriptableObject getScriptableObject()
  {
    return scriptObject_;
  }
  



  public JavaScriptJobManager getJobManager()
  {
    return jobManager_;
  }
  






  public void setJobManager(JavaScriptJobManager jobManager)
  {
    jobManager_ = jobManager;
  }
  






  public void addChildWindow(FrameWindow child)
  {
    synchronized (childWindows_) {
      childWindows_.add(child);
    }
  }
  


  protected void destroyChildren()
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("destroyChildren");
    }
    getJobManager().removeAllJobs();
    

    while (!childWindows_.isEmpty()) {
      WebWindowImpl window = (WebWindowImpl)childWindows_.get(0);
      removeChildWindow(window);
    }
  }
  





  public void removeChildWindow(WebWindowImpl window)
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("closing child window: " + window);
    }
    window.setClosed();
    window.getJobManager().shutdown();
    Page page = window.getEnclosedPage();
    if (page != null) {
      page.cleanUp();
    }
    window.destroyChildren();
    
    synchronized (childWindows_) {
      childWindows_.remove(window);
    }
  }
  



  public String getName()
  {
    return name_;
  }
  



  public void setName(String name)
  {
    name_ = name;
  }
  




  public History getHistory()
  {
    return history_;
  }
  



  public boolean isClosed()
  {
    return closed_;
  }
  


  protected void setClosed()
  {
    closed_ = true;
  }
  



  public int getInnerWidth()
  {
    return innerWidth_;
  }
  



  public void setInnerWidth(int innerWidth)
  {
    innerWidth_ = innerWidth;
  }
  



  public int getOuterWidth()
  {
    return outerWidth_;
  }
  



  public void setOuterWidth(int outerWidth)
  {
    outerWidth_ = outerWidth;
  }
  



  public int getInnerHeight()
  {
    return innerHeight_;
  }
  



  public void setInnerHeight(int innerHeight)
  {
    innerHeight_ = innerHeight;
  }
  



  public int getOuterHeight()
  {
    return outerHeight_;
  }
  



  public void setOuterHeight(int outerHeight)
  {
    outerHeight_ = outerHeight;
  }
}
