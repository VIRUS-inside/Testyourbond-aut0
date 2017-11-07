package com.gargoylesoftware.htmlunit.javascript.host.css;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import org.w3c.dom.css.CSSUnknownRule;

































@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(value=com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF, minVersion=52), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
public class CSSKeyframesRule
  extends CSSRule
{
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public CSSKeyframesRule() {}
  
  protected CSSKeyframesRule(CSSStyleSheet stylesheet, CSSUnknownRule rule)
  {
    super(stylesheet, rule);
  }
  




  @JsxGetter
  public short getType()
  {
    return 7;
  }
  



  public Object getDefaultValue(Class<?> hint)
  {
    if (((String.class.equals(hint)) || (hint == null)) && 
      (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_CSS_MOZ_CSS_KEYFRAMES_RULE))) {
      return "[object MozCSSKeyframesRule]";
    }
    return super.getDefaultValue(hint);
  }
}
