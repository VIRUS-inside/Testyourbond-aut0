package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;





































@JsxClass
public class MimeType
  extends SimpleScriptable
{
  private String description_;
  private String suffixes_;
  private String type_;
  private Plugin enabledPlugin_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public MimeType() {}
  
  public MimeType(String type, String description, String suffixes, Plugin plugin)
  {
    type_ = type;
    description_ = description;
    suffixes_ = suffixes;
    enabledPlugin_ = plugin;
  }
  



  @JsxGetter
  public String getDescription()
  {
    return description_;
  }
  



  @JsxGetter
  public String getSuffixes()
  {
    return suffixes_;
  }
  



  @JsxGetter
  public String getType()
  {
    return type_;
  }
  



  @JsxGetter
  public Object getEnabledPlugin()
  {
    return enabledPlugin_;
  }
}
