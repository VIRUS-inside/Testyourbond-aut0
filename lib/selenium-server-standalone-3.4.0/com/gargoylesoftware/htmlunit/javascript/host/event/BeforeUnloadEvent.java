package com.gargoylesoftware.htmlunit.javascript.host.event;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;





























@JsxClass
public class BeforeUnloadEvent
  extends Event
{
  public BeforeUnloadEvent()
  {
    setEventType("");
    setReturnValue("");
  }
  


  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public void jConstructor()
  {
    Context.throwAsScriptRuntimeEx(new IllegalArgumentException("Illegal Constructor"));
  }
  





  public BeforeUnloadEvent(DomNode domNode, String type)
  {
    super(domNode, type);
    
    setBubbles(false);
    setReturnValue(Undefined.instance);
  }
  



  public void eventCreated()
  {
    super.eventCreated();
    
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.EVENT_BEFOREUNLOAD_AUTO_TYPE)) {
      setEventType("beforeunload");
      setCancelable(true);
    }
  }
  




  @JsxGetter
  public Object getReturnValue()
  {
    return super.getReturnValue();
  }
  




  @JsxSetter
  public void setReturnValue(Object returnValue)
  {
    super.setReturnValue(returnValue);
  }
}
