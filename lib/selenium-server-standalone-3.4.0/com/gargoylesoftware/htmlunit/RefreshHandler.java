package com.gargoylesoftware.htmlunit;

import java.io.IOException;
import java.net.URL;

public abstract interface RefreshHandler
{
  public abstract void handleRefresh(Page paramPage, URL paramURL, int paramInt)
    throws IOException;
}
