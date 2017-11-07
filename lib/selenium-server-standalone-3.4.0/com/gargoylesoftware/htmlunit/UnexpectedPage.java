package com.gargoylesoftware.htmlunit;

import java.io.IOException;
import java.io.InputStream;



























public class UnexpectedPage
  extends AbstractPage
{
  public UnexpectedPage(WebResponse webResponse, WebWindow enclosingWindow)
  {
    super(webResponse, enclosingWindow);
  }
  




  public InputStream getInputStream()
    throws IOException
  {
    return getWebResponse().getContentAsStream();
  }
}
