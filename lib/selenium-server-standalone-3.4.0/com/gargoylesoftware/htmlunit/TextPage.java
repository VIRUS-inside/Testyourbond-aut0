package com.gargoylesoftware.htmlunit;




























public class TextPage
  extends AbstractPage
{
  public TextPage(WebResponse webResponse, WebWindow enclosingWindow)
  {
    super(webResponse, enclosingWindow);
  }
  




  public String getContent()
  {
    return getWebResponse().getContentAsString();
  }
}
