package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlTemplate;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DocumentFragment;
import net.sourceforge.htmlunit.corejs.javascript.Context;































@JsxClass(domClass=HtmlTemplate.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
public class HTMLTemplateElement
  extends HTMLElement
{
  @JsxConstructor
  public HTMLTemplateElement() {}
  
  @JsxGetter
  public DocumentFragment getContent()
  {
    DocumentFragment result = new DocumentFragment();
    result.setPrototype(getPrototype(result.getClass()));
    result.setParentScope(getParentScope());
    result.setDomNode(((HtmlTemplate)getDomNodeOrDie()).getContent());
    
    return result;
  }
  




  @JsxGetter
  public String getInnerHTML()
  {
    try
    {
      domNode = ((HtmlTemplate)getDomNodeOrDie()).getContent();
    } catch (IllegalStateException e) {
      DomNode domNode;
      Context.throwAsScriptRuntimeEx(e);
      return ""; }
    DomNode domNode;
    return getInnerHTML(domNode);
  }
}
