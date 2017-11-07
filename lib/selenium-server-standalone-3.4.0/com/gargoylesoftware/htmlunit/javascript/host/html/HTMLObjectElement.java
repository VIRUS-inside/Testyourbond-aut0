package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlObject;
import com.gargoylesoftware.htmlunit.javascript.HtmlUnitContextFactory;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.ActiveXObjectImpl;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import java.applet.Applet;
import java.lang.reflect.Method;
import java.util.Map;
import net.sourceforge.htmlunit.corejs.javascript.BaseFunction;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.NativeJavaObject;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Wrapper;





























@JsxClass(domClass=HtmlObject.class)
public class HTMLObjectElement
  extends HTMLElement
  implements Wrapper
{
  private Scriptable wrappedActiveX_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLObjectElement() {}
  
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
    HtmlObject appletNode = (HtmlObject)getDomNodeOrDie();
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
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public String getAlt()
  {
    String alt = getDomNodeOrDie().getAttribute("alt");
    return alt;
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setAlt(String alt)
  {
    getDomNodeOrDie().setAttribute("alt", alt);
  }
  



  @JsxGetter
  public String getBorder()
  {
    String border = getDomNodeOrDie().getAttribute("border");
    return border;
  }
  



  @JsxSetter
  public void setBorder(String border)
  {
    getDomNodeOrDie().setAttribute("border", border);
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public String getClassid()
  {
    String classid = getDomNodeOrDie().getAttribute("classid");
    return classid;
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setClassid(String classid)
  {
    getDomNodeOrDie().setAttribute("classid", classid);
    if ((classid.indexOf(':') != -1) && (getBrowserVersion().hasFeature(BrowserVersionFeatures.HTML_OBJECT_CLASSID))) {
      WebClient webClient = getWindow().getWebWindow().getWebClient();
      Map<String, String> map = webClient.getActiveXObjectMap();
      if (map != null) {
        String xClassString = (String)map.get(classid);
        if (xClassString != null) {
          try {
            Class<?> xClass = Class.forName(xClassString);
            Object object = xClass.newInstance();
            boolean contextCreated = false;
            if (Context.getCurrentContext() == null) {
              new HtmlUnitContextFactory(webClient).enterContext();
              contextCreated = true;
            }
            wrappedActiveX_ = Context.toObject(object, getParentScope());
            if (contextCreated) {
              Context.exit();
            }
          }
          catch (Exception e) {
            throw Context.reportRuntimeError("ActiveXObject Error: failed instantiating class " + 
              xClassString + " because " + e.getMessage() + ".");
          }
          return;
        }
      }
      if ((webClient.getOptions().isActiveXNative()) && 
        (System.getProperty("os.name").contains("Windows"))) {
        try {
          wrappedActiveX_ = new ActiveXObjectImpl(classid);
          wrappedActiveX_.setParentScope(getParentScope());
        }
        catch (Exception e) {
          Context.throwAsScriptRuntimeEx(e);
        }
      }
    }
  }
  





  public Object get(String name, Scriptable start)
  {
    if ((wrappedActiveX_ instanceof NativeJavaObject)) {
      NativeJavaObject obj = (NativeJavaObject)wrappedActiveX_;
      Object result = obj.get(name, start);
      if (Scriptable.NOT_FOUND != result) {
        return result;
      }
      return super.get(name, start);
    }
    
    if (wrappedActiveX_ != null) {
      return wrappedActiveX_.get(name, start);
    }
    return super.get(name, start);
  }
  





  public void put(String name, Scriptable start, Object value)
  {
    if ((wrappedActiveX_ instanceof NativeJavaObject)) {
      if (wrappedActiveX_.has(name, start)) {
        wrappedActiveX_.put(name, start, value);
      }
      else {
        super.put(name, start, value);
      }
      return;
    }
    
    if (wrappedActiveX_ != null) {
      wrappedActiveX_.put(name, start, value);
      return;
    }
    
    super.put(name, start, value);
  }
  



  public Object unwrap()
  {
    if ((wrappedActiveX_ instanceof Wrapper)) {
      return ((Wrapper)wrappedActiveX_).unwrap();
    }
    return wrappedActiveX_;
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
  



  @JsxGetter
  public String getName()
  {
    return getDomNodeOrDie().getAttribute("name");
  }
  



  @JsxSetter
  public void setName(String name)
  {
    getDomNodeOrDie().setAttribute("name", name);
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
