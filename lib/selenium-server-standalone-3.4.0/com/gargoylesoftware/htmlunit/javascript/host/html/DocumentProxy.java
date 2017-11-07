package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptableProxy;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Document;


























public class DocumentProxy
  extends SimpleScriptableProxy<Document>
{
  private final WebWindow webWindow_;
  
  public DocumentProxy(WebWindow webWindow)
  {
    webWindow_ = webWindow;
  }
  



  public Document getDelegee()
  {
    Window w = (Window)webWindow_.getScriptableObject();
    return w.getDocument();
  }
}
