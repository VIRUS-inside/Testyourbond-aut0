package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import net.sourceforge.htmlunit.corejs.javascript.Context;



























@JsxClass(isJSObject=false)
public class FormField
  extends HTMLElement
{
  public FormField() {}
  
  public void setDomNode(DomNode domNode)
  {
    super.setDomNode(domNode);
    
    HtmlForm form = ((HtmlElement)domNode).getEnclosingForm();
    if (form != null) {
      setParentScope(getScriptableFor(form));
    }
  }
  




  @JsxGetter
  public String getValue()
  {
    return getDomNodeOrDie().getAttribute("value");
  }
  




  @JsxSetter
  public void setValue(Object newValue)
  {
    getDomNodeOrDie().setAttribute("value", Context.toString(newValue));
  }
  




  @JsxGetter
  public String getName()
  {
    return getDomNodeOrDie().getAttribute("name");
  }
  




  @JsxSetter
  public void setName(String newName)
  {
    getDomNodeOrDie().setAttribute("name", newName);
  }
  



  @JsxGetter
  public boolean getDisabled()
  {
    return super.getDisabled();
  }
  



  @JsxSetter
  public void setDisabled(boolean disabled)
  {
    super.setDisabled(disabled);
  }
  




  @JsxGetter
  public HTMLFormElement getForm()
  {
    HtmlForm form = getDomNodeOrDie().getEnclosingForm();
    if (form == null) {
      return null;
    }
    return (HTMLFormElement)getScriptableFor(form);
  }
}
