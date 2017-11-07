package com.gargoylesoftware.htmlunit;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
























public class TopLevelWindow
  extends WebWindowImpl
{
  private static final Log LOG = LogFactory.getLog(TopLevelWindow.class);
  


  private WebWindow opener_;
  



  protected TopLevelWindow(String name, WebClient webClient)
  {
    super(webClient);
    WebAssert.notNull("name", name);
    setName(name);
    performRegistration();
  }
  




  public WebWindow getParentWindow()
  {
    return this;
  }
  




  public WebWindow getTopWindow()
  {
    return this;
  }
  



  protected boolean isJavaScriptInitializationNeeded()
  {
    Page enclosedPage = getEnclosedPage();
    return (getScriptableObject() == null) || 
      (enclosedPage.getUrl() == WebClient.URL_ABOUT_BLANK) || 
      (!(enclosedPage.getWebResponse() instanceof StringWebResponse));
  }
  





  public String toString()
  {
    return "TopLevelWindow[name=\"" + getName() + "\"]";
  }
  



  public void setOpener(WebWindow opener)
  {
    opener_ = opener;
  }
  



  public WebWindow getOpener()
  {
    return opener_;
  }
  


  public void close()
  {
    setClosed();
    Page page = getEnclosedPage();
    if (page != null) {
      if (page.isHtmlPage()) {
        HtmlPage htmlPage = (HtmlPage)page;
        if (!htmlPage.isOnbeforeunloadAccepted()) {
          if (LOG.isDebugEnabled()) {
            LOG.debug("The registered OnbeforeunloadHandler rejected the window close event.");
          }
          return;
        }
      }
      page.cleanUp();
    }
    
    getJobManager().shutdown();
    destroyChildren();
    getWebClient().deregisterWebWindow(this);
  }
}
