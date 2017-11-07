package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import org.apache.commons.lang3.StringUtils;

























@JsxClass(isJSObject=false, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
public class NamespaceCollection
  extends SimpleScriptable
  implements Function
{
  private final HTMLDocument doc_;
  private final List<Namespace> namespaces_;
  
  public NamespaceCollection()
  {
    doc_ = null;
    namespaces_ = new ArrayList();
  }
  



  public NamespaceCollection(HTMLDocument doc)
  {
    doc_ = doc;
    namespaces_ = new ArrayList();
    
    setParentScope(doc);
    setPrototype(getPrototype(getClass()));
    
    Map<String, String> namespacesMap = doc_.getPage().getNamespaces();
    for (Map.Entry<String, String> entry : namespacesMap.entrySet()) {
      String key = (String)entry.getKey();
      if (!key.isEmpty()) {
        namespaces_.add(new Namespace(doc_, key, (String)entry.getValue()));
      }
    }
  }
  







  @JsxFunction
  public final Namespace add(String namespace, String urn, String url)
  {
    Namespace n = new Namespace(doc_, namespace, urn);
    namespaces_.add(n);
    return n;
  }
  



  @JsxGetter
  public final int getLength()
  {
    return namespaces_.size();
  }
  




  @JsxFunction
  public final Object item(Object index)
  {
    if ((index instanceof Number)) {
      Number n = (Number)index;
      int i = n.intValue();
      return get(i, this);
    }
    String key = String.valueOf(index);
    return get(key, this);
  }
  

  public Object get(int index, Scriptable start)
  {
    if ((index >= 0) && (index < namespaces_.size())) {
      return namespaces_.get(index);
    }
    return super.get(index, start);
  }
  

  public Object get(String name, Scriptable start)
  {
    for (Namespace n : namespaces_) {
      if (StringUtils.equals(n.getName(), name)) {
        return n;
      }
    }
    return super.get(name, start);
  }
  

  public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args)
  {
    if (args.length != 1) {
      return NOT_FOUND;
    }
    return item(args[0]);
  }
  

  public Scriptable construct(Context cx, Scriptable scope, Object[] args)
  {
    return null;
  }
}
