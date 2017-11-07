package com.gargoylesoftware.htmlunit.javascript.host.event;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.NativeObject;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;





























@JsxClass
public class PopStateEvent
  extends Event
{
  private Object state_;
  
  public PopStateEvent()
  {
    setEventType("");
  }
  



  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public void jsConstructor(String type, ScriptableObject details)
  {
    super.jsConstructor(type, details);
    
    if ((details != null) && (!Undefined.instance.equals(details))) {
      state_ = details.get("state");
    }
  }
  






  public PopStateEvent(EventTarget target, String type, Object state)
  {
    super(target, type);
    if (((state instanceof NativeObject)) && (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_POP_STATE_EVENT_CLONE_STATE))) {
      NativeObject old = (NativeObject)state;
      NativeObject newState = new NativeObject();
      for (Object o : ScriptableObject.getPropertyIds(old)) {
        String property = Context.toString(o);
        newState.defineProperty(property, ScriptableObject.getProperty(old, property), 0);
      }
      state_ = newState;
    }
    else {
      state_ = state;
    }
  }
  



  @JsxGetter
  public Object getState()
  {
    return state_;
  }
  



  public void eventCreated()
  {
    super.eventCreated();
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_POP_STATE_EVENT_TYPE)) {
      setType("popstate");
      setCancelable(true);
    }
  }
  



  public void put(String name, Scriptable start, Object value)
  {
    if (!"state".equals(name)) {
      super.put(name, start, value);
    }
  }
}
