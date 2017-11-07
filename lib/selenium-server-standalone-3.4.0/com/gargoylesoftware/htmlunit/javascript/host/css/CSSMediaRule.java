package com.gargoylesoftware.htmlunit.javascript.host.css;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;

































@JsxClass
public class CSSMediaRule
  extends CSSConditionRule
{
  private com.gargoylesoftware.htmlunit.javascript.host.dom.MediaList media_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public CSSMediaRule() {}
  
  protected CSSMediaRule(CSSStyleSheet stylesheet, org.w3c.dom.css.CSSMediaRule rule)
  {
    super(stylesheet, rule);
  }
  



  @JsxGetter
  public com.gargoylesoftware.htmlunit.javascript.host.dom.MediaList getMedia()
  {
    if (media_ == null) {
      CSSStyleSheet parent = getParentStyleSheet();
      org.w3c.dom.stylesheets.MediaList ml = getMediaRule().getMedia();
      media_ = new com.gargoylesoftware.htmlunit.javascript.host.dom.MediaList(parent, ml);
    }
    return media_;
  }
  



  private org.w3c.dom.css.CSSMediaRule getMediaRule()
  {
    return (org.w3c.dom.css.CSSMediaRule)getRule();
  }
}
