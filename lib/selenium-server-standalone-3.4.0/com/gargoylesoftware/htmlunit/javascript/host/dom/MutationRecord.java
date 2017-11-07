package com.gargoylesoftware.htmlunit.javascript.host.dom;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
































@JsxClass
public class MutationRecord
  extends SimpleScriptable
{
  private String type_;
  private ScriptableObject target_;
  private String oldValue_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public MutationRecord() {}
  
  void setType(String type)
  {
    type_ = type;
  }
  



  @JsxGetter
  public String getType()
  {
    return type_;
  }
  



  void setTarget(ScriptableObject target)
  {
    target_ = target;
  }
  



  @JsxGetter
  public ScriptableObject getTarget()
  {
    return target_;
  }
  



  void setOldValue(String oldValue)
  {
    oldValue_ = oldValue;
  }
  



  @JsxGetter
  public String getOldValue()
  {
    return oldValue_;
  }
}
