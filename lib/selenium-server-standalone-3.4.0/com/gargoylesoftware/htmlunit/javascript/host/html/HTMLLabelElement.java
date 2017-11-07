package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlLabel;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;































@JsxClass(domClass=HtmlLabel.class)
public class HTMLLabelElement
  extends HTMLElement
{
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLLabelElement() {}
  
  @JsxGetter
  public String getHtmlFor()
  {
    return ((HtmlLabel)getDomNodeOrDie()).getForAttribute();
  }
  




  @JsxSetter
  public void setHtmlFor(String id)
  {
    ((HtmlLabel)getDomNodeOrDie()).setAttribute("for", id);
  }
  


  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public HTMLElement getControl()
  {
    String id = getHtmlFor();
    if (StringUtils.isBlank(id)) {
      return null;
    }
    HtmlElement htmlElem = getDomNodeOrDie();
    Element elem = htmlElem.getPage().getElementById(id);
    if ((elem == null) || (!(elem instanceof HtmlElement))) {
      return null;
    }
    return (HTMLElement)((HtmlElement)elem).getScriptableObject();
  }
  




  @JsxGetter
  public HTMLFormElement getForm()
  {
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_LABEL_FORM_NULL)) {
      return null;
    }
    HtmlForm form = getDomNodeOrDie().getEnclosingForm();
    if (form == null) {
      return null;
    }
    return (HTMLFormElement)getScriptableFor(form);
  }
}
