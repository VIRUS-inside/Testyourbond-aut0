package com.gargoylesoftware.htmlunit.javascript.background;

import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;



























class JavaScriptStringJob
  extends JavaScriptExecutionJob
{
  private final String script_;
  
  JavaScriptStringJob(int initialDelay, Integer period, String label, WebWindow window, String script)
  {
    super(initialDelay, period, label, window);
    script_ = script;
  }
  

  protected void runJavaScript(HtmlPage page)
  {
    if (script_ == null) {
      return;
    }
    page.executeJavaScriptIfPossible(script_, "JavaScriptStringJob", 1);
  }
}
