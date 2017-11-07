package com.gargoylesoftware.htmlunit.javascript.host.css;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.util.StringUtils;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
























@JsxClass
public class CSSStyleRule
  extends CSSRule
{
  private static final Pattern SELECTOR_PARTS_PATTERN = Pattern.compile("[\\.#]?[a-zA-Z]+");
  private static final Pattern SELECTOR_REPLACE_PATTERN = Pattern.compile("\\*([\\.#])");
  




  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public CSSStyleRule() {}
  




  protected CSSStyleRule(CSSStyleSheet stylesheet, org.w3c.dom.css.CSSStyleRule rule)
  {
    super(stylesheet, rule);
  }
  



  @JsxGetter
  public String getSelectorText()
  {
    String selectorText = ((org.w3c.dom.css.CSSStyleRule)getRule()).getSelectorText();
    Matcher m = SELECTOR_PARTS_PATTERN.matcher(selectorText);
    StringBuffer sb = new StringBuffer();
    while (m.find()) {
      String fixedName = m.group();
      
      if ((getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_SELECTOR_TEXT_LOWERCASE)) && 
        (!fixedName.isEmpty()) && ('.' != fixedName.charAt(0)) && ('#' != fixedName.charAt(0))) {
        fixedName = fixedName.toLowerCase(Locale.ROOT);
      }
      fixedName = StringUtils.sanitizeForAppendReplacement(fixedName);
      m.appendReplacement(sb, fixedName);
    }
    m.appendTail(sb);
    

    selectorText = SELECTOR_REPLACE_PATTERN.matcher(sb.toString()).replaceAll("$1");
    return selectorText;
  }
  



  @JsxSetter
  public void setSelectorText(String selectorText)
  {
    ((org.w3c.dom.css.CSSStyleRule)getRule()).setSelectorText(selectorText);
  }
  



  @JsxGetter
  public CSSStyleDeclaration getStyle()
  {
    return new CSSStyleDeclaration(getParentScope(), ((org.w3c.dom.css.CSSStyleRule)getRule()).getStyle());
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public boolean getReadOnly()
  {
    return false;
  }
}
