package com.gargoylesoftware.htmlunit.javascript.host.css;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;


























@JsxClass
public class CSSRuleList
  extends SimpleScriptable
{
  private final List<CSSRule> rules_ = new ArrayList();
  




  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public CSSRuleList() {}
  



  public CSSRuleList(CSSStyleSheet stylesheet)
  {
    setParentScope(stylesheet.getParentScope());
    setPrototype(getPrototype(getClass()));
  }
  



  protected void addRule(CSSRule rule)
  {
    rules_.add(rule);
  }
  


  protected void clearRules()
  {
    rules_.clear();
  }
  



  @JsxGetter
  public int getLength()
  {
    if (rules_ != null) {
      return rules_.size();
    }
    return 0;
  }
  




  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public Object item(int index)
  {
    return null;
  }
  



  public Object[] getIds()
  {
    List<String> idList = new ArrayList();
    
    int length = getLength();
    for (int i = 0; i < length; i++) {
      idList.add(Integer.toString(i));
    }
    
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_CSSRULELIST_ENUM_ITEM_LENGTH)) {
      idList.add("item");
      idList.add("length");
    }
    else {
      idList.add("length");
      idList.add("item");
    }
    return idList.toArray();
  }
  



  public boolean has(int index, Scriptable start)
  {
    return (index >= 0) && (index < getLength());
  }
  



  public boolean has(String name, Scriptable start)
  {
    if (("length".equals(name)) || ("item".equals(name))) {
      return true;
    }
    try {
      return has(Integer.parseInt(name), start);
    }
    catch (Exception localException) {}
    

    return false;
  }
  



  public Object get(int index, Scriptable start)
  {
    if ((index < 0) || (getLength() <= index)) {
      return NOT_FOUND;
    }
    return rules_.get(index);
  }
}
