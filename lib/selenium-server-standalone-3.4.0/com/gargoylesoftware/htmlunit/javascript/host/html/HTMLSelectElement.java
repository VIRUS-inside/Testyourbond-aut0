package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.dom.AbstractList;
import java.util.List;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;







































@JsxClass(domClass=HtmlSelect.class)
public class HTMLSelectElement
  extends FormField
{
  private HTMLOptionsCollection optionsArray_;
  private AbstractList labels_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLSelectElement() {}
  
  public void initialize()
  {
    HtmlSelect htmlSelect = getHtmlSelect();
    htmlSelect.setScriptableObject(this);
    if (optionsArray_ == null) {
      optionsArray_ = new HTMLOptionsCollection(this);
      optionsArray_.initialize(htmlSelect);
    }
  }
  



  @JsxFunction
  public void remove(int index)
  {
    if ((index < 0) && (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_SELECT_REMOVE_IGNORE_IF_INDEX_OUTSIDE))) {
      return;
    }
    HTMLOptionsCollection options = getOptions();
    if ((index >= options.getLength()) && (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_SELECT_REMOVE_IGNORE_IF_INDEX_OUTSIDE))) {
      return;
    }
    
    getOptions().remove(index);
  }
  





  @JsxFunction
  public void add(HTMLOptionElement newOptionObject, Object beforeOptionObject)
  {
    getOptions().add(newOptionObject, beforeOptionObject);
  }
  



  public Object appendChild(Object childObject)
  {
    Object object = super.appendChild(childObject);
    getHtmlSelect().ensureSelectedIndex();
    return object;
  }
  



  public Object insertBeforeImpl(Object[] args)
  {
    Object object = super.insertBeforeImpl(args);
    getHtmlSelect().ensureSelectedIndex();
    return object;
  }
  




  @JsxFunction
  public Object item(int index)
  {
    Object option = getOptions().item(index);
    if (option == Undefined.instance) {
      return null;
    }
    return option;
  }
  

  @JsxGetter
  public String getType()
  {
    String type;
    
    String type;
    if (getHtmlSelect().isMultipleSelectEnabled()) {
      type = "select-multiple";
    }
    else {
      type = "select-one";
    }
    return type;
  }
  



  @JsxGetter
  public HTMLOptionsCollection getOptions()
  {
    if (optionsArray_ == null) {
      initialize();
    }
    return optionsArray_;
  }
  



  @JsxGetter
  public int getSelectedIndex()
  {
    return getHtmlSelect().getSelectedIndex();
  }
  



  @JsxSetter
  public void setSelectedIndex(int index)
  {
    getHtmlSelect().setSelectedIndex(index);
  }
  




  public String getValue()
  {
    HtmlSelect htmlSelect = getHtmlSelect();
    List<HtmlOption> selectedOptions = htmlSelect.getSelectedOptions();
    if (selectedOptions.isEmpty()) {
      return "";
    }
    return ((HTMLOptionElement)((HtmlOption)selectedOptions.get(0)).getScriptableObject()).getValue();
  }
  



  @JsxGetter
  public int getLength()
  {
    return getOptions().getLength();
  }
  



  @JsxSetter
  public void setLength(int newLength)
  {
    getOptions().setLength(newLength);
  }
  






  public Object get(int index, Scriptable start)
  {
    if (getDomNodeOrNull() == null) {
      return NOT_FOUND;
    }
    return getOptions().get(index, start);
  }
  






  public void put(int index, Scriptable start, Object newValue)
  {
    getOptions().put(index, start, newValue);
  }
  



  private HtmlSelect getHtmlSelect()
  {
    return (HtmlSelect)getDomNodeOrDie();
  }
  




  public void setValue(Object newValue)
  {
    String val = Context.toString(newValue);
    getHtmlSelect().setSelectedAttribute(val, true, false);
  }
  



  @JsxGetter
  public int getSize()
  {
    int size = 0;
    String sizeAttribute = getDomNodeOrDie().getAttribute("size");
    if ((sizeAttribute != DomElement.ATTRIBUTE_NOT_DEFINED) && (sizeAttribute != DomElement.ATTRIBUTE_VALUE_EMPTY)) {
      try {
        size = Integer.parseInt(sizeAttribute);
      }
      catch (Exception localException) {}
    }
    

    return size;
  }
  



  @JsxSetter
  public void setSize(String size)
  {
    getDomNodeOrDie().setAttribute("size", size);
  }
  



  @JsxGetter
  public boolean getMultiple()
  {
    return getDomNodeOrDie().hasAttribute("multiple");
  }
  



  @JsxSetter
  public void setMultiple(boolean multiple)
  {
    if (multiple) {
      getDomNodeOrDie().setAttribute("multiple", "multiple");
    }
    else {
      getDomNodeOrDie().removeAttribute("multiple");
    }
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public AbstractList getLabels()
  {
    if (labels_ == null) {
      labels_ = new LabelsHelper(getDomNodeOrDie());
    }
    return labels_;
  }
}
