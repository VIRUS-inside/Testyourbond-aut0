package com.gargoylesoftware.htmlunit;

import com.gargoylesoftware.htmlunit.html.HtmlApplet;
import com.gargoylesoftware.htmlunit.html.HtmlObject;

public abstract interface AppletConfirmHandler
{
  public abstract boolean confirm(HtmlApplet paramHtmlApplet);
  
  public abstract boolean confirm(HtmlObject paramHtmlObject);
}
