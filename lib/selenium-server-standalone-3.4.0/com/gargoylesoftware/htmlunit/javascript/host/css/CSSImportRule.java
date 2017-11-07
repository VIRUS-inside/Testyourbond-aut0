package com.gargoylesoftware.htmlunit.javascript.host.css;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;

































@JsxClass
public class CSSImportRule
  extends CSSRule
{
  private com.gargoylesoftware.htmlunit.javascript.host.dom.MediaList media_;
  private CSSStyleSheet importedStylesheet_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public CSSImportRule() {}
  
  protected CSSImportRule(CSSStyleSheet stylesheet, org.w3c.dom.css.CSSImportRule rule)
  {
    super(stylesheet, rule);
  }
  



  @JsxGetter
  public String getHref()
  {
    return getImportRule().getHref();
  }
  



  @JsxGetter
  public com.gargoylesoftware.htmlunit.javascript.host.dom.MediaList getMedia()
  {
    if (media_ == null) {
      CSSStyleSheet parent = getParentStyleSheet();
      org.w3c.dom.stylesheets.MediaList ml = getImportRule().getMedia();
      media_ = new com.gargoylesoftware.htmlunit.javascript.host.dom.MediaList(parent, ml);
    }
    return media_;
  }
  



  @JsxGetter
  public CSSStyleSheet getStyleSheet()
  {
    if (importedStylesheet_ == null) {
      CSSStyleSheet owningSheet = getParentStyleSheet();
      HTMLElement ownerNode = owningSheet.getOwnerNode();
      org.w3c.dom.css.CSSStyleSheet importedStylesheet = getImportRule().getStyleSheet();
      importedStylesheet_ = new CSSStyleSheet(ownerNode, importedStylesheet, owningSheet.getUri());
    }
    return importedStylesheet_;
  }
  



  private org.w3c.dom.css.CSSImportRule getImportRule()
  {
    return (org.w3c.dom.css.CSSImportRule)getRule();
  }
}
