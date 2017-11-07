package com.gargoylesoftware.htmlunit.html.applets;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.net.URL;
import java.util.HashMap;




























public class AppletStubImpl
  implements AppletStub
{
  private final AppletContextImpl appletContextImpl_;
  private final HashMap<String, String> parameters_;
  private final URL codebase_;
  private final URL documentbase_;
  
  public AppletStubImpl(HtmlPage htmlPage, HashMap<String, String> parameters, URL codebase, URL documentbase)
  {
    appletContextImpl_ = new AppletContextImpl(htmlPage);
    parameters_ = parameters;
    codebase_ = codebase;
    documentbase_ = documentbase;
  }
  





  public void appletResize(int width, int height) {}
  




  public AppletContext getAppletContext()
  {
    return appletContextImpl_;
  }
  



  public URL getCodeBase()
  {
    return codebase_;
  }
  



  public URL getDocumentBase()
  {
    return documentbase_;
  }
  



  public String getParameter(String name)
  {
    return (String)parameters_.get(name);
  }
  



  public boolean isActive()
  {
    throw new RuntimeException(
      "Not yet implemented! (com.gargoylesoftware.htmlunit.html.applets.AppletStubImpl.isActive())");
  }
}
