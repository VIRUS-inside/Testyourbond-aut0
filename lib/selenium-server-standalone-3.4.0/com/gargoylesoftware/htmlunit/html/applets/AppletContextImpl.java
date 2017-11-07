package com.gargoylesoftware.htmlunit.html.applets;

import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AudioClip;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;



















public class AppletContextImpl
  implements AppletContext
{
  private static final Enumeration<Applet> EMPTY_ENUMERATION = Collections.enumeration(Collections.emptyList());
  private HtmlPage htmlPage_;
  
  AppletContextImpl(HtmlPage page) {
    htmlPage_ = page;
  }
  



  public Applet getApplet(String name)
  {
    return null;
  }
  



  public Enumeration<Applet> getApplets()
  {
    return EMPTY_ENUMERATION;
  }
  



  public AudioClip getAudioClip(URL url)
  {
    throw new RuntimeException("Not yet implemented!");
  }
  



  public Image getImage(URL url)
  {
    throw new RuntimeException("Not yet implemented!");
  }
  



  public InputStream getStream(String key)
  {
    throw new RuntimeException("Not yet implemented!");
  }
  



  public Iterator<String> getStreamKeys()
  {
    throw new RuntimeException("Not yet implemented!");
  }
  


  public void setStream(String key, InputStream stream)
    throws IOException
  {
    throw new RuntimeException("Not yet implemented!");
  }
  



  public void showDocument(URL url)
  {
    throw new RuntimeException("Not yet implemented!");
  }
  



  public void showDocument(URL url, String target)
  {
    throw new RuntimeException("Not yet implemented!");
  }
  





  public void showStatus(String status)
  {
    Window window = (Window)htmlPage_.getEnclosingWindow().getScriptableObject();
    window.setStatus(status);
  }
}
