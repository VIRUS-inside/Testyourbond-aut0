package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;





































@JsxClass
public class Plugin
  extends SimpleArray
{
  private String description_;
  private String filename_;
  private String name_;
  private String version_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public Plugin() {}
  
  public Plugin(String name, String description, String version, String filename)
  {
    name_ = name;
    description_ = description;
    version_ = version;
    filename_ = filename;
  }
  





  protected String getItemName(Object element)
  {
    return ((MimeType)element).getType();
  }
  



  @JsxGetter
  public String getDescription()
  {
    return description_;
  }
  



  @JsxGetter
  public String getFilename()
  {
    return filename_;
  }
  



  @JsxGetter
  public String getName()
  {
    return name_;
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public String getVersion()
  {
    return version_;
  }
}
