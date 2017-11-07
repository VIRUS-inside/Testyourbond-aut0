package com.gargoylesoftware.htmlunit;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.Serializable;


























public class AjaxController
  implements Serializable
{
  public AjaxController() {}
  
  public boolean processSynchron(HtmlPage page, WebRequest request, boolean async)
  {
    return !async;
  }
}
