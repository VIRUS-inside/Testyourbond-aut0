package com.gargoylesoftware.htmlunit;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public abstract interface ScriptPreProcessor
{
  public abstract String preProcess(HtmlPage paramHtmlPage, String paramString1, String paramString2, int paramInt, HtmlElement paramHtmlElement);
}
