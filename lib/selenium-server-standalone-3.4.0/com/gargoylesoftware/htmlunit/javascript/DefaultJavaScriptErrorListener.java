package com.gargoylesoftware.htmlunit.javascript;

import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;






















public class DefaultJavaScriptErrorListener
  implements JavaScriptErrorListener, Serializable
{
  private static final Log LOG = LogFactory.getLog(DefaultJavaScriptErrorListener.class);
  

  public DefaultJavaScriptErrorListener() {}
  

  public void scriptException(HtmlPage page, ScriptException scriptException) {}
  

  public void timeoutError(HtmlPage page, long allowedTime, long executionTime) {}
  
  public void malformedScriptURL(HtmlPage page, String url, MalformedURLException malformedURLException)
  {
    LOG.error("Unable to build URL for script src tag [" + url + "]", malformedURLException);
  }
  
  public void loadScriptError(HtmlPage page, URL scriptUrl, Exception exception)
  {
    LOG.error("Error loading JavaScript from [" + scriptUrl + "].", exception);
  }
}
