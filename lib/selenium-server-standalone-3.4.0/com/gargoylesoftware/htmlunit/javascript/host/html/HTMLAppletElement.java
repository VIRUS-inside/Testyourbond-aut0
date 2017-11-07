package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlApplet;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import java.applet.Applet;
import java.lang.reflect.Method;
import net.sourceforge.htmlunit.corejs.javascript.BaseFunction;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;































@JsxClass(domClass=HtmlApplet.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
public class HTMLAppletElement
  extends HTMLElement
{
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLAppletElement() {}
  
  public void setDomNode(DomNode domNode)
  {
    super.setDomNode(domNode);
    
    if (domNode.getPage().getWebClient().getOptions().isAppletEnabled()) {
      try {
        createAppletMethodAndProperties();
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }
  
  private void createAppletMethodAndProperties() throws Exception {
    HtmlApplet appletNode = (HtmlApplet)getDomNodeOrDie();
    final Applet applet = appletNode.getApplet();
    if (applet == null) {
      return;
    }
    

    for (final Method method : applet.getClass().getMethods()) {
      Function f = new BaseFunction()
      {

        public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args)
        {
          Object[] realArgs = new Object[method.getParameterTypes().length];
          for (int i = 0; i < realArgs.length; i++) { Object arg;
            Object arg;
            if (i > args.length) {
              arg = null;
            }
            else {
              arg = Context.jsToJava(args[i], method.getParameterTypes()[i]);
            }
            realArgs[i] = arg;
          }
          try {
            return method.invoke(applet, realArgs);
          }
          catch (Exception e) {
            throw Context.throwAsScriptRuntimeEx(e);
          }
        }
      };
      ScriptableObject.defineProperty(this, method.getName(), f, 1);
    }
  }
  



  @JsxGetter
  public String getAlt()
  {
    String alt = getDomNodeOrDie().getAttribute("alt");
    return alt;
  }
  



  @JsxSetter
  public void setAlt(String alt)
  {
    getDomNodeOrDie().setAttribute("alt", alt);
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public String getBorder()
  {
    String border = getDomNodeOrDie().getAttribute("border");
    return border;
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setBorder(String border)
  {
    getDomNodeOrDie().setAttribute("border", border);
  }
  



  @JsxGetter
  public String getAlign()
  {
    return getAlign(true);
  }
  



  @JsxSetter
  public void setAlign(String align)
  {
    setAlign(align, false);
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public String getClassid()
  {
    return getDomNodeOrDie().getAttribute("classid");
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setClassid(String classid)
  {
    getDomNodeOrDie().setAttribute("classid", classid);
  }
  




  @JsxGetter(propertyName="width")
  public String getWidth_js()
  {
    return getWidthOrHeight("width", Boolean.TRUE);
  }
  



  @JsxSetter
  public void setWidth(String width)
  {
    setWidthOrHeight("width", width, true);
  }
  



  @JsxGetter(propertyName="height")
  public String getHeight_js()
  {
    return getWidthOrHeight("height", Boolean.TRUE);
  }
  



  @JsxSetter
  public void setHeight(String height)
  {
    setWidthOrHeight("height", height, true);
  }
}
