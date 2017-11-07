package com.gargoylesoftware.htmlunit.javascript.host.css;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.css.CSSUnknownRule;



























@JsxClass
public class CSSRule
  extends SimpleScriptable
{
  private static final Log LOG = LogFactory.getLog(CSSRule.class);
  



  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public static final short UNKNOWN_RULE = 0;
  



  @JsxConstant
  public static final short STYLE_RULE = 1;
  



  @JsxConstant
  public static final short CHARSET_RULE = 2;
  



  @JsxConstant
  public static final short IMPORT_RULE = 3;
  



  @JsxConstant
  public static final short MEDIA_RULE = 4;
  



  @JsxConstant
  public static final short FONT_FACE_RULE = 5;
  



  @JsxConstant
  public static final short PAGE_RULE = 6;
  



  @JsxConstant
  public static final short KEYFRAMES_RULE = 7;
  



  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public static final short MOZ_KEYFRAMES_RULE = 7;
  



  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public static final short WEBKIT_KEYFRAMES_RULE = 7;
  



  @JsxConstant
  public static final short KEYFRAME_RULE = 8;
  



  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public static final short WEBKIT_KEYFRAME_RULE = 8;
  



  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public static final short MOZ_KEYFRAME_RULE = 8;
  


  @JsxConstant
  public static final short NAMESPACE_RULE = 10;
  


  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public static final short COUNTER_STYLE_RULE = 11;
  


  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public static final short SUPPORTS_RULE = 12;
  


  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public static final short FONT_FEATURE_VALUES_RULE = 14;
  


  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public static final short VIEWPORT_RULE = 15;
  


  private final CSSStyleSheet stylesheet_;
  


  private final org.w3c.dom.css.CSSRule rule_;
  



  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public CSSRule()
  {
    stylesheet_ = null;
    rule_ = null;
  }
  





  public static CSSRule create(CSSStyleSheet stylesheet, org.w3c.dom.css.CSSRule rule)
  {
    switch (rule.getType()) {
    case 1: 
      return new CSSStyleRule(stylesheet, (org.w3c.dom.css.CSSStyleRule)rule);
    case 3: 
      return new CSSImportRule(stylesheet, (org.w3c.dom.css.CSSImportRule)rule);
    

    case 4: 
      return new CSSMediaRule(stylesheet, (org.w3c.dom.css.CSSMediaRule)rule);
    case 5: 
      return new CSSFontFaceRule(stylesheet, (org.w3c.dom.css.CSSFontFaceRule)rule);
    case 0: 
      CSSUnknownRule unknownRule = (CSSUnknownRule)rule;
      if (unknownRule.getCssText().startsWith("@keyframes")) {
        return new CSSKeyframesRule(stylesheet, (CSSUnknownRule)rule);
      }
      LOG.warn("Unknown CSSRule " + rule.getClass().getName() + 
        " is not yet supported; rule content: '" + rule.getCssText() + "'");
      break;
    case 2: default: 
      LOG.warn("CSSRule " + rule.getClass().getName() + 
        " is not yet supported; rule content: '" + rule.getCssText() + "'");
    }
    
    return null;
  }
  




  protected CSSRule(CSSStyleSheet stylesheet, org.w3c.dom.css.CSSRule rule)
  {
    stylesheet_ = stylesheet;
    rule_ = rule;
    setParentScope(stylesheet);
    setPrototype(getPrototype(getClass()));
  }
  



  @JsxGetter
  public short getType()
  {
    return rule_.getType();
  }
  




  @JsxGetter
  public String getCssText()
  {
    return rule_.getCssText();
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setCssText(String cssText)
  {
    rule_.setCssText(cssText);
  }
  



  @JsxGetter
  public CSSStyleSheet getParentStyleSheet()
  {
    return stylesheet_;
  }
  




  @JsxGetter
  public CSSRule getParentRule()
  {
    org.w3c.dom.css.CSSRule parentRule = rule_.getParentRule();
    if (parentRule != null) {
      return create(stylesheet_, parentRule);
    }
    return null;
  }
  



  protected org.w3c.dom.css.CSSRule getRule()
  {
    return rule_;
  }
}
