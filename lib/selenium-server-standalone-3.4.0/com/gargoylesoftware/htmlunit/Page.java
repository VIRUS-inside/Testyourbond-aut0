package com.gargoylesoftware.htmlunit;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;

public abstract interface Page
  extends Serializable
{
  public abstract void initialize()
    throws IOException;
  
  public abstract void cleanUp();
  
  public abstract WebResponse getWebResponse();
  
  public abstract WebWindow getEnclosingWindow();
  
  public abstract URL getUrl();
  
  public abstract boolean isHtmlPage();
}
