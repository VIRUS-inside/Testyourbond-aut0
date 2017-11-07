package com.gargoylesoftware.htmlunit;

import java.io.IOException;

public abstract interface PageCreator
{
  public abstract Page createPage(WebResponse paramWebResponse, WebWindow paramWebWindow)
    throws IOException;
}
