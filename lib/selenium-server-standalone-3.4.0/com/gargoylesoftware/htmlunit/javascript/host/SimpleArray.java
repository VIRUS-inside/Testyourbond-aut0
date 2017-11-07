package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;






















@JsxClass(isJSObject=false)
public class SimpleArray
  extends SimpleScriptable
{
  private final List<Object> elements_ = new ArrayList();
  

  public SimpleArray() {}
  

  @JsxFunction
  public Object item(int index)
  {
    return elements_.get(index);
  }
  



  protected Object getWithPreemption(String name)
  {
    Object response = namedItem(name);
    if (response != null) {
      return response;
    }
    return NOT_FOUND;
  }
  




  public final Object get(int index, Scriptable start)
  {
    SimpleArray array = (SimpleArray)start;
    List<Object> elements = elements_;
    
    if ((index >= 0) && (index < elements.size())) {
      return elements.get(index);
    }
    return null;
  }
  




  @JsxFunction
  public Object namedItem(String name)
  {
    for (Object element : elements_) {
      if (name.equals(getItemName(element))) {
        return element;
      }
    }
    return null;
  }
  





  protected String getItemName(Object element)
  {
    return null;
  }
  



  @JsxGetter
  public int getLength()
  {
    return elements_.size();
  }
  



  void add(Object element)
  {
    elements_.add(element);
  }
}
