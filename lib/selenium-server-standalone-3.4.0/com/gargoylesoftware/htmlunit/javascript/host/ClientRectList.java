package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClasses;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;































@JsxClasses({@com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(isJSObject=false, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})})
public class ClientRectList
  extends SimpleScriptable
{
  private final List<ClientRect> clientRects_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public ClientRectList()
  {
    clientRects_ = new ArrayList();
  }
  



  @JsxGetter
  public int getLength()
  {
    return clientRects_.size();
  }
  




  public final Object get(int index, Scriptable start)
  {
    if ((index >= 0) && (index < clientRects_.size())) {
      return clientRects_.get(index);
    }
    return NOT_FOUND;
  }
  




  @JsxFunction
  public ClientRect item(int index)
  {
    if ((index >= 0) && (index < clientRects_.size())) {
      return (ClientRect)clientRects_.get(index);
    }
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_CLIENTRECTLIST_THROWS_IF_ITEM_NOT_FOUND)) {
      throw Context.reportRuntimeError("Invalid index '" + index + "'");
    }
    return null;
  }
  



  public void add(ClientRect clientRect)
  {
    clientRects_.add(clientRect);
  }
  
  public Object getDefaultValue(Class<?> hint)
  {
    if ((String.class == hint) && 
      (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_CLIENTRECTLIST_DEFAUL_VALUE_FROM_FIRST))) {
      if (clientRects_.size() > 0) {
        return ((ClientRect)clientRects_.get(0)).getDefaultValue(hint);
      }
      return "";
    }
    return super.getDefaultValue(hint);
  }
}
