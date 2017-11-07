package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.host.dom.AbstractList;
import com.gargoylesoftware.htmlunit.javascript.host.dom.NodeList;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;










































@JsxClass
public class HTMLAllCollection
  extends HTMLCollection
{
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLAllCollection() {}
  
  public HTMLAllCollection(DomNode parentScope)
  {
    super(parentScope, false);
  }
  



  public Object item(Object index)
  {
    Double numb;
    

    BrowserVersion browser;
    

    if ((index instanceof String)) {
      String name = (String)index;
      Object result = namedItem(name);
      if ((result != null) && (Undefined.instance != result)) {
        return result;
      }
      Double numb = Double.valueOf(NaN.0D);
      
      BrowserVersion browser = getBrowserVersion();
      if (!browser.hasFeature(BrowserVersionFeatures.HTMLALLCOLLECTION_DO_NOT_CONVERT_STRINGS_TO_NUMBER)) {
        numb = Double.valueOf(ScriptRuntime.toNumber(index));
      }
      if (numb.isNaN()) {
        return itemNotFound(browser);
      }
    }
    else {
      numb = Double.valueOf(ScriptRuntime.toNumber(index));
      browser = getBrowserVersion();
    }
    
    if (numb.doubleValue() < 0.0D) {
      return itemNotFound(browser);
    }
    
    if ((!browser.hasFeature(BrowserVersionFeatures.HTMLCOLLECTION_ITEM_FUNCT_SUPPORTS_DOUBLE_INDEX_ALSO)) && (
      (Double.isInfinite(numb.doubleValue())) || (numb.doubleValue() != Math.floor(numb.doubleValue())))) {
      return itemNotFound(browser);
    }
    
    Object object = get(numb.intValue(), this);
    if (object == NOT_FOUND) {
      return null;
    }
    return object;
  }
  
  private static Object itemNotFound(BrowserVersion browser) {
    if (browser.hasFeature(BrowserVersionFeatures.HTMLALLCOLLECTION_NULL_IF_ITEM_NOT_FOUND)) {
      return null;
    }
    return Undefined.instance;
  }
  



  public final Object namedItem(String name)
  {
    List<Object> elements = getElements();
    

    List<DomElement> matching = new ArrayList();
    
    BrowserVersion browser = getBrowserVersion();
    
    boolean idFirst = browser.hasFeature(BrowserVersionFeatures.HTMLCOLLECTION_NAMED_ITEM_ID_FIRST);
    if (idFirst) {
      for (Object next : elements) {
        if ((next instanceof DomElement)) {
          DomElement elem = (DomElement)next;
          if (name.equals(elem.getId())) {
            matching.add(elem);
          }
        }
      }
    }
    
    for (Object next : elements) {
      if ((next instanceof DomElement)) {
        DomElement elem = (DomElement)next;
        if (name.equals(elem.getAttribute("name"))) {
          matching.add(elem);
        }
        else if ((!idFirst) && (name.equals(elem.getId()))) {
          matching.add(elem);
        }
      }
    }
    
    if ((matching.size() == 1) || (
      (matching.size() > 1) && 
      (browser.hasFeature(BrowserVersionFeatures.HTMLALLCOLLECTION_NO_COLLECTION_FOR_MANY_HITS)))) {
      return getScriptableForElement(matching.get(0));
    }
    if (matching.isEmpty()) {
      if (browser.hasFeature(BrowserVersionFeatures.HTMLALLCOLLECTION_NULL_IF_NAMED_ITEM_NOT_FOUND)) {
        return null;
      }
      return Undefined.instance;
    }
    

    DomNode domNode = getDomNodeOrNull();
    HTMLCollection collection = new HTMLCollection(domNode, matching);
    collection.setAvoidObjectDetection(true);
    return collection;
  }
  



  public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args)
  {
    BrowserVersion browser = getBrowserVersion();
    if (browser.hasFeature(BrowserVersionFeatures.HTMLALLCOLLECTION_DO_NOT_SUPPORT_PARANTHESES)) {
      if (args.length == 0) {
        throw Context.reportRuntimeError("Zero arguments; need an index or a key.");
      }
      
      if ((args[0] instanceof Number)) {
        return null;
      }
    }
    
    boolean nullIfNotFound = false;
    if (browser.hasFeature(BrowserVersionFeatures.HTMLALLCOLLECTION_INTEGER_INDEX)) {
      if ((args[0] instanceof Number)) {
        double val = ((Number)args[0]).doubleValue();
        if (val != (int)val) {
          return Undefined.instance;
        }
        if (val >= 0.0D) {
          nullIfNotFound = true;
        }
      }
      else {
        String val = Context.toString(args[0]);
        try {
          args[0] = Integer.valueOf(Integer.parseInt(val));
        }
        catch (NumberFormatException localNumberFormatException) {}
      }
    }
    


    Object value = super.call(cx, scope, thisObj, args);
    if ((nullIfNotFound) && (value == Undefined.instance)) {
      return null;
    }
    return value;
  }
  



  protected boolean supportsParentheses()
  {
    return true;
  }
  



  protected AbstractList create(DomNode parentScope, List<?> initialElements)
  {
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.HTMLALLCOLLECTION_NODE_LIST_FOR_DUPLICATES)) {
      return new NodeList(parentScope, initialElements);
    }
    return super.create(parentScope, initialElements);
  }
}
