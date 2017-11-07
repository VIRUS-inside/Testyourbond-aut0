package com.gargoylesoftware.htmlunit.javascript.background;

import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;




























class JavaScriptFunctionJob
  extends JavaScriptExecutionJob
{
  private final Function function_;
  
  JavaScriptFunctionJob(int initialDelay, Integer period, String label, WebWindow window, Function function)
  {
    super(initialDelay, period, label, window);
    function_ = function;
  }
  

  protected void runJavaScript(HtmlPage page)
  {
    HtmlElement doc = page.getDocumentElement();
    Scriptable scriptable = page.getEnclosingWindow().getScriptableObject();
    page.executeJavaScriptFunctionIfPossible(function_, scriptable, new Object[0], doc);
  }
}
