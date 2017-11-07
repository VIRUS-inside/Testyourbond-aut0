package com.gargoylesoftware.htmlunit.javascript.background;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.lang.ref.WeakReference;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;























abstract class JavaScriptExecutionJob
  extends BasicJavaScriptJob
{
  private static final Log LOG = LogFactory.getLog(JavaScriptExecutionJob.class);
  



  private final String label_;
  



  private final transient WeakReference<WebWindow> window_;
  



  JavaScriptExecutionJob(int initialDelay, Integer period, String label, WebWindow window)
  {
    super(initialDelay, period);
    label_ = label;
    window_ = new WeakReference(window);
  }
  

  public void run()
  {
    WebWindow w = (WebWindow)window_.get();
    if (w == null)
    {
      return;
    }
    
    if (LOG.isDebugEnabled()) {
      LOG.debug("Executing " + this + ".");
    }
    
    try
    {
      if (w.isClosed()) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("Enclosing window is now closed. Execution cancelled.");
        }
        return;
      }
      if (!w.getWebClient().containsWebWindow(w)) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("Enclosing window is now closed. Execution cancelled.");
        }
        return;
      }
      

      Page enclosedPage = w.getEnclosedPage();
      if ((enclosedPage == null) || (!enclosedPage.isHtmlPage())) {
        if (enclosedPage == null) {
          if (LOG.isDebugEnabled()) {
            LOG.debug("The page that originated this job doesn't exist anymore. Execution cancelled.");
          }
          return;
        }
        if (LOG.isDebugEnabled()) {
          LOG.debug("The page that originated this job is no html page (" + 
            enclosedPage.getClass().getName() + "). Execution cancelled.");
        }
        return;
      }
      
      runJavaScript((HtmlPage)enclosedPage);
    }
    finally {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Finished executing " + this + ".");
      }
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("Finished executing " + this + ".");
    }
  }
  


  public String toString()
  {
    return "JavaScript Execution Job " + getId() + ": " + label_;
  }
  
  protected abstract void runJavaScript(HtmlPage paramHtmlPage);
}
