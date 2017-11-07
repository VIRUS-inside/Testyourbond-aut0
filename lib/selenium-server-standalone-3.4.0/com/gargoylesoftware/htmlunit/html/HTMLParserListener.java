package com.gargoylesoftware.htmlunit.html;

import java.net.URL;

































public abstract interface HTMLParserListener
{
  public static final HTMLParserListener LOG_REPORTER = new SimpleHTMLParserListener();
  
  public abstract void error(String paramString1, URL paramURL, String paramString2, int paramInt1, int paramInt2, String paramString3);
  
  public abstract void warning(String paramString1, URL paramURL, String paramString2, int paramInt1, int paramInt2, String paramString3);
}
