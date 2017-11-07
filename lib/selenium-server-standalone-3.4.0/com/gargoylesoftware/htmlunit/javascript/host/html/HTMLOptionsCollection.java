package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.ElementFactory;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClasses;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.dom.NodeList;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.EvaluatorException;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;













































@JsxClasses({@com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(isJSObject=false, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})})
public class HTMLOptionsCollection
  extends SimpleScriptable
{
  private HtmlSelect htmlSelect_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public HTMLOptionsCollection() {}
  
  public HTMLOptionsCollection(SimpleScriptable parentScope)
  {
    setParentScope(parentScope);
    setPrototype(getPrototype(getClass()));
  }
  



  public String getClassName()
  {
    if ((getWindow().getWebWindow() != null) && 
      (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_SELECT_OPTIONS_HAS_SELECT_CLASS_NAME))) {
      return "HTMLSelectElement";
    }
    return super.getClassName();
  }
  



  public void initialize(HtmlSelect select)
  {
    WebAssert.notNull("select", select);
    htmlSelect_ = select;
  }
  







  public Object get(int index, Scriptable start)
  {
    if ((htmlSelect_ == null) || (index < 0)) {
      return Undefined.instance;
    }
    
    if (index >= htmlSelect_.getOptionSize()) {
      if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_SELECT_OPTIONS_NULL_FOR_OUTSIDE)) {
        return null;
      }
      return Undefined.instance;
    }
    
    return getScriptableFor(htmlSelect_.getOption(index));
  }
  









  public void put(String name, Scriptable start, Object value)
  {
    if (htmlSelect_ == null)
    {

      super.put(name, start, value);
      return;
    }
    
    HTMLSelectElement parent = (HTMLSelectElement)htmlSelect_.getScriptableObject();
    
    if ((!has(name, start)) && (ScriptableObject.hasProperty(parent, name))) {
      ScriptableObject.putProperty(parent, name, value);
    }
    else {
      super.put(name, start, value);
    }
  }
  





  @JsxFunction
  public Object item(int index)
  {
    return get(index, null);
  }
  






  public void put(int index, Scriptable start, Object newValue)
  {
    if (newValue == null)
    {
      htmlSelect_.removeOption(index);
    }
    else {
      HTMLOptionElement option = (HTMLOptionElement)newValue;
      HtmlOption htmlOption = (HtmlOption)option.getDomNodeOrNull();
      if (index >= getLength()) {
        setLength(index);
        
        htmlSelect_.appendOption(htmlOption);
      }
      else
      {
        htmlSelect_.replaceOption(index, htmlOption);
      }
    }
  }
  




  @JsxGetter
  public int getLength()
  {
    return htmlSelect_.getOptionSize();
  }
  





  @JsxSetter
  public void setLength(int newLength)
  {
    if (newLength < 0) {
      if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_SELECT_OPTIONS_IGNORE_NEGATIVE_LENGTH)) {
        return;
      }
      throw Context.reportRuntimeError("Length is negative");
    }
    
    int currentLength = htmlSelect_.getOptionSize();
    if (currentLength > newLength) {
      htmlSelect_.setOptionSize(newLength);
    }
    else {
      for (int i = currentLength; i < newLength; i++) {
        HtmlOption option = (HtmlOption)HTMLParser.getFactory("option").createElement(
          htmlSelect_.getPage(), "option", null);
        htmlSelect_.appendOption(option);
        if (!getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_SELECT_OPTIONS_DONT_ADD_EMPTY_TEXT_CHILD_WHEN_EXPANDING)) {
          option.appendChild(new DomText(option.getPage(), ""));
        }
      }
    }
  }
  




































  @JsxFunction
  public void add(Object newOptionObject, Object beforeOptionObject)
  {
    int index = getLength();
    
    HtmlOption htmlOption = (HtmlOption)((HTMLOptionElement)newOptionObject).getDomNodeOrNull();
    
    HtmlOption beforeOption = null;
    
    if ((beforeOptionObject instanceof Number)) {
      index = ((Integer)Context.jsToJava(beforeOptionObject, Integer.class)).intValue();
      if ((index < 0) || (index >= getLength()))
      {
        htmlSelect_.appendOption(htmlOption);
        return;
      }
      
      beforeOption = (HtmlOption)((HTMLOptionElement)item(index)).getDomNodeOrDie();
    }
    else if ((beforeOptionObject instanceof HTMLOptionElement)) {
      beforeOption = (HtmlOption)((HTMLOptionElement)beforeOptionObject).getDomNodeOrDie();
      if (beforeOption.getParentNode() != htmlSelect_) {
        throw new EvaluatorException("Unknown option.");
      }
    }
    
    if (beforeOption == null) {
      htmlSelect_.appendOption(htmlOption);
      return;
    }
    
    beforeOption.insertBefore(htmlOption);
  }
  



  @JsxFunction
  public void remove(int index)
  {
    int idx = index;
    BrowserVersion browser = getBrowserVersion();
    if (idx < 0) {
      if (browser.hasFeature(BrowserVersionFeatures.JS_SELECT_OPTIONS_REMOVE_IGNORE_IF_INDEX_NEGATIVE)) {
        return;
      }
      if ((index < 0) && (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_SELECT_OPTIONS_REMOVE_THROWS_IF_NEGATIV))) {
        throw Context.reportRuntimeError("Invalid index for option collection: " + index);
      }
    }
    
    idx = Math.max(idx, 0);
    if (idx >= getLength()) {
      if (browser.hasFeature(BrowserVersionFeatures.JS_SELECT_OPTIONS_REMOVE_IGNORE_IF_INDEX_TOO_LARGE)) {
        return;
      }
      idx = 0;
    }
    htmlSelect_.removeOption(idx);
  }
  
  public boolean has(int index, Scriptable start)
  {
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_SELECT_OPTIONS_IN_ALWAYS_TRUE)) {
      return true;
    }
    
    return super.has(index, start);
  }
  



  @JsxGetter
  public int getSelectedIndex()
  {
    return htmlSelect_.getSelectedIndex();
  }
  



  @JsxSetter
  public void setSelectedIndex(int index)
  {
    htmlSelect_.setSelectedIndex(index);
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public NodeList getChildNodes()
  {
    new NodeList(htmlSelect_, false)
    {
      protected List<Object> computeElements() {
        List<Object> response = new ArrayList();
        for (DomNode child : htmlSelect_.getChildren()) {
          response.add(child);
        }
        
        return response;
      }
    };
  }
}
