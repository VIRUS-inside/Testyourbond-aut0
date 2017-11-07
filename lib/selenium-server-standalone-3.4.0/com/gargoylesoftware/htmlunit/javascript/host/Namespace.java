package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;




































@JsxClass(isJSObject=false, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
public class Namespace
  extends SimpleScriptable
{
  private String name_;
  private String urn_;
  
  public Namespace() {}
  
  public Namespace(ScriptableObject parentScope, String name, String urn)
  {
    setParentScope(parentScope);
    setPrototype(getPrototype(getClass()));
    name_ = name;
    urn_ = urn;
  }
  



  @JsxGetter
  public String getName()
  {
    return name_;
  }
  



  @JsxGetter
  public String getUrn()
  {
    return urn_;
  }
  



  @JsxSetter
  public void setUrn(String urn)
  {
    urn_ = urn;
  }
}
