package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.host.dom.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;




















































@JsxClass
public class HTMLCollection
  extends AbstractList
{
  private int currentIndex_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLCollection() {}
  
  public HTMLCollection(DomNode domNode, boolean attributeChangeSensitive)
  {
    super(domNode, attributeChangeSensitive);
  }
  




  HTMLCollection(DomNode domNode, List<?> initialElements)
  {
    super(domNode, initialElements);
  }
  




  public static HTMLCollection emptyCollection(DomNode domNode)
  {
    final List<Object> list = Collections.emptyList();
    new HTMLCollection(domNode, false)
    {
      public List<Object> getElements() {
        return list;
      }
    };
  }
  



  protected AbstractList create(DomNode parentScope, List<?> initialElements)
  {
    return new HTMLCollection(parentScope, initialElements);
  }
  



  public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args)
  {
    if (supportsParentheses()) {
      return super.call(cx, scope, thisObj, args);
    }
    
    throw Context.reportRuntimeError("TypeError - HTMLCollection does nont support function like access");
  }
  




  protected boolean supportsParentheses()
  {
    return getBrowserVersion().hasFeature(BrowserVersionFeatures.HTMLCOLLECTION_SUPPORTS_PARANTHESES);
  }
  



  protected Object getWithPreemptionByName(String name, List<Object> elements)
  {
    List<Object> matchingElements = new ArrayList();
    boolean searchName = isGetWithPreemptionSearchName();
    for (Object next : elements) {
      if (((next instanceof DomElement)) && (
        (searchName) || ((next instanceof HtmlInput)) || ((next instanceof HtmlForm)))) {
        String nodeName = ((DomElement)next).getAttribute("name");
        if (name.equals(nodeName)) {
          matchingElements.add(next);
        }
      }
    }
    
    if (matchingElements.isEmpty()) {
      if (getBrowserVersion().hasFeature(BrowserVersionFeatures.HTMLCOLLECTION_ITEM_SUPPORTS_DOUBLE_INDEX_ALSO)) {
        Double doubleValue = Double.valueOf(Context.toNumber(name));
        if (!doubleValue.isNaN()) {
          Object object = get(doubleValue.intValue(), this);
          if (object != NOT_FOUND) {
            return object;
          }
        }
      }
      return NOT_FOUND;
    }
    if (matchingElements.size() == 1) {
      return getScriptableForElement(matchingElements.get(0));
    }
    

    DomNode domNode = getDomNodeOrNull();
    HTMLCollection collection = new HTMLCollection(domNode, matchingElements);
    collection.setAvoidObjectDetection(true);
    return collection;
  }
  



  protected boolean isGetWithPreemptionSearchName()
  {
    return true;
  }
  






  public Object item(Object index)
  {
    if (((index instanceof String)) && (getBrowserVersion().hasFeature(BrowserVersionFeatures.HTMLCOLLECTION_ITEM_SUPPORTS_ID_SEARCH_ALSO))) {
      String name = (String)index;
      Object result = namedItem(name);
      return result;
    }
    
    int idx = 0;
    Double doubleValue = Double.valueOf(Context.toNumber(index));
    if (!doubleValue.isNaN()) {
      idx = doubleValue.intValue();
    }
    
    Object object = get(idx, this);
    if (object == NOT_FOUND) {
      return null;
    }
    return object;
  }
  






  @JsxFunction
  public Object namedItem(String name)
  {
    List<Object> elements = getElements();
    BrowserVersion browserVersion = getBrowserVersion();
    if (browserVersion.hasFeature(BrowserVersionFeatures.HTMLCOLLECTION_NAMED_ITEM_ID_FIRST)) {
      for (Object next : elements) {
        if ((next instanceof DomElement)) {
          DomElement elem = (DomElement)next;
          String id = elem.getId();
          if (name.equals(id)) {
            return getScriptableForElement(elem);
          }
        }
      }
    }
    for (Object next : elements) {
      if ((next instanceof DomElement)) {
        DomElement elem = (DomElement)next;
        String nodeName = elem.getAttribute("name");
        if (name.equals(nodeName)) {
          return getScriptableForElement(elem);
        }
        
        String id = elem.getId();
        if (name.equals(id)) {
          return getScriptableForElement(elem);
        }
      }
    }
    return null;
  }
  




  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public Object nextNode()
  {
    List<Object> elements = getElements();
    Object nextNode; Object nextNode; if ((currentIndex_ >= 0) && (currentIndex_ < elements.size())) {
      nextNode = elements.get(currentIndex_);
    }
    else {
      nextNode = null;
    }
    currentIndex_ += 1;
    return nextNode;
  }
  


  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void reset()
  {
    currentIndex_ = 0;
  }
  







  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public Object tags(final String tagName)
  {
    HTMLCollection collection = new HTMLSubCollection(this)
    {
      protected boolean isMatching(DomNode node) {
        return tagName.equalsIgnoreCase(node.getLocalName());
      }
    };
    return collection;
  }
}
