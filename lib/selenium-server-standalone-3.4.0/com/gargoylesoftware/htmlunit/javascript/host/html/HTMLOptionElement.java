package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.ElementFactory;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlOptionGroup;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import org.xml.sax.helpers.AttributesImpl;































@JsxClass(domClass=HtmlOption.class)
public class HTMLOptionElement
  extends HTMLElement
{
  public HTMLOptionElement() {}
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public void jsConstructor(String newText, String newValue, boolean defaultSelected, boolean selected)
  {
    HtmlPage page = (HtmlPage)getWindow().getWebWindow().getEnclosedPage();
    AttributesImpl attributes = null;
    if (defaultSelected) {
      attributes = new AttributesImpl();
      attributes.addAttribute(null, "selected", "selected", null, "selected");
    }
    
    HtmlOption htmlOption = (HtmlOption)HTMLParser.getFactory("option").createElement(
      page, "option", attributes);
    htmlOption.setSelected(selected);
    setDomNode(htmlOption);
    
    if (!"undefined".equals(newText)) {
      htmlOption.appendChild(new DomText(page, newText));
      htmlOption.setLabelAttribute(newText);
    }
    if (!"undefined".equals(newValue)) {
      htmlOption.setValueAttribute(newValue);
    }
  }
  



  @JsxGetter
  public String getValue()
  {
    String value = getDomNodeOrNull().getAttribute("value");
    if (value == DomElement.ATTRIBUTE_NOT_DEFINED) {
      value = ((HtmlOption)getDomNodeOrNull()).getText();
    }
    return value;
  }
  



  @JsxSetter
  public void setValue(String newValue)
  {
    DomNode dom = getDomNodeOrNull();
    if ((dom instanceof HtmlOption)) {
      ((HtmlOption)dom).setValueAttribute(newValue);
    }
  }
  



  @JsxGetter
  public String getText()
  {
    DomNode dom = getDomNodeOrNull();
    if ((dom instanceof HtmlOption)) {
      return ((HtmlOption)dom).getText();
    }
    return null;
  }
  



  @JsxSetter
  public void setText(String newText)
  {
    DomNode dom = getDomNodeOrNull();
    if ((dom instanceof HtmlOption)) {
      ((HtmlOption)dom).setText(newText);
      
      if (!hasAttribute("label")) {
        setLabel(newText);
      }
    }
  }
  



  @JsxGetter
  public boolean getSelected()
  {
    DomNode dom = getDomNodeOrNull();
    if ((dom instanceof HtmlOption)) {
      return ((HtmlOption)dom).isSelected();
    }
    return false;
  }
  



  @JsxSetter
  public void setSelected(boolean selected)
  {
    HtmlOption optionNode = (HtmlOption)getDomNodeOrNull();
    HtmlSelect enclosingSelect = optionNode.getEnclosingSelect();
    if ((!selected) && (optionNode.isSelected()) && 
      (enclosingSelect != null) && (!enclosingSelect.isMultipleSelectEnabled())) {
      enclosingSelect.getOption(0).setSelected(true, false);
    }
    else {
      optionNode.setSelected(selected, false);
    }
  }
  



  @JsxGetter
  public boolean getDefaultSelected()
  {
    DomNode dom = getDomNodeOrNull();
    if ((dom instanceof HtmlOption)) {
      return ((HtmlOption)dom).isDefaultSelected();
    }
    return false;
  }
  



  @JsxGetter
  public String getLabel()
  {
    DomNode domNode = getDomNodeOrNull();
    if ((domNode instanceof HtmlOption)) {
      return ((HtmlOption)domNode).getLabelAttribute();
    }
    return ((HtmlOptionGroup)domNode).getLabelAttribute();
  }
  



  @JsxSetter
  public void setLabel(String label)
  {
    DomNode domNode = getDomNodeOrNull();
    if ((domNode instanceof HtmlOption)) {
      ((HtmlOption)domNode).setLabelAttribute(label);
    }
    else {
      ((HtmlOptionGroup)domNode).setLabelAttribute(label);
    }
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
  public int getIndex()
  {
    HtmlOption optionNode = (HtmlOption)getDomNodeOrNull();
    if (optionNode != null) {
      HtmlSelect enclosingSelect = optionNode.getEnclosingSelect();
      if (enclosingSelect != null) {
        return enclosingSelect.indexOf(optionNode);
      }
    }
    return 0;
  }
  



  public void setAttribute(String name, String value)
  {
    super.setAttribute(name, value);
    if ("selected".equals(name)) {
      setSelected(true);
    }
  }
  



  public void removeAttribute(String name)
  {
    super.removeAttribute(name);
    if ((getBrowserVersion().hasFeature(BrowserVersionFeatures.HTMLOPTION_REMOVE_SELECTED_ATTRIB_DESELECTS)) && 
      ("selected".equals(name))) {
      setSelected(false);
    }
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
