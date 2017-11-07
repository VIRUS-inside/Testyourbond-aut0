package com.gargoylesoftware.htmlunit;

import java.io.IOException;

public abstract interface WebConnection
  extends AutoCloseable
{
  public abstract WebResponse getResponse(WebRequest paramWebRequest)
    throws IOException;
}
