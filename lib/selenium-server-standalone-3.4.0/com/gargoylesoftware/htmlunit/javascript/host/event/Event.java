package com.gargoylesoftware.htmlunit.javascript.host.event;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import java.lang.reflect.Method;
import java.util.LinkedList;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;












































































































@JsxClass
public class Event
  extends SimpleScriptable
{
  protected static final String KEY_CURRENT_EVENT = "Event#current";
  public static final String TYPE_SUBMIT = "submit";
  public static final String TYPE_CHANGE = "change";
  public static final String TYPE_LOAD = "load";
  public static final String TYPE_UNLOAD = "unload";
  public static final String TYPE_POPSTATE = "popstate";
  public static final String TYPE_FOCUS = "focus";
  public static final String TYPE_FOCUS_IN = "focusin";
  public static final String TYPE_FOCUS_OUT = "focusout";
  public static final String TYPE_BLUR = "blur";
  public static final String TYPE_KEY_DOWN = "keydown";
  public static final String TYPE_KEY_PRESS = "keypress";
  public static final String TYPE_INPUT = "input";
  public static final String TYPE_KEY_UP = "keyup";
  public static final String TYPE_RESET = "reset";
  public static final String TYPE_BEFORE_UNLOAD = "beforeunload";
  public static final String TYPE_DOM_DOCUMENT_LOADED = "DOMContentLoaded";
  public static final String TYPE_PROPERTY_CHANGE = "propertychange";
  public static final String TYPE_HASH_CHANGE = "hashchange";
  public static final String TYPE_READY_STATE_CHANGE = "readystatechange";
  public static final String TYPE_ERROR = "error";
  public static final String TYPE_MESSAGE = "message";
  public static final String TYPE_CLOSE = "close";
  public static final String TYPE_OPEN = "open";
  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public static final short NONE = 0;
  @JsxConstant
  public static final short CAPTURING_PHASE = 1;
  @JsxConstant
  public static final short AT_TARGET = 2;
  @JsxConstant
  public static final short BUBBLING_PHASE = 3;
  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public static final int ALT_MASK = 1;
  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public static final int CONTROL_MASK = 2;
  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public static final int SHIFT_MASK = 4;
  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public static final int META_MASK = 8;
  private Object srcElement_;
  private EventTarget target_;
  private Scriptable currentTarget_;
  private String type_ = "";
  

  private Object keyCode_;
  
  private boolean shiftKey_;
  
  private boolean ctrlKey_;
  
  private boolean altKey_;
  
  private String propertyName_;
  
  private boolean stopPropagation_;
  
  private boolean stopImmediatePropagation_;
  
  private Object returnValue_;
  
  private boolean preventDefault_;
  
  private short eventPhase_;
  
  private boolean bubbles_ = true;
  






  private boolean cancelable_ = true;
  



  private final long timeStamp_ = System.currentTimeMillis();
  




  public Event(DomNode domNode, String type)
  {
    this((EventTarget)domNode.getScriptableObject(), type);
    setDomNode(domNode, false);
  }
  




  public Event(EventTarget target, String type)
  {
    srcElement_ = target;
    target_ = target;
    currentTarget_ = target;
    type_ = type;
    setParentScope(target);
    setPrototype(getPrototype(getClass()));
    
    if ("change".equals(type)) {
      cancelable_ = false;
    }
    else if ("load".equals(type)) {
      bubbles_ = false;
      if (getBrowserVersion().hasFeature(BrowserVersionFeatures.EVENT_ONLOAD_CANCELABLE_FALSE)) {
        cancelable_ = false;
      }
    }
  }
  





  public static Event createPropertyChangeEvent(DomNode domNode, String propertyName)
  {
    Event event = new Event(domNode, "propertychange");
    propertyName_ = propertyName;
    return event;
  }
  




  public Event() {}
  



  public void eventCreated()
  {
    setBubbles(false);
    setCancelable(false);
  }
  





  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public void jsConstructor(String type, ScriptableObject details)
  {
    boolean bubbles = false;
    boolean cancelable = false;
    
    if ((details != null) && (!Undefined.instance.equals(details))) {
      Boolean detailBubbles = (Boolean)details.get("bubbles");
      if (detailBubbles != null) {
        bubbles = detailBubbles.booleanValue();
      }
      
      Boolean detailCancelable = (Boolean)details.get("cancelable");
      if (detailCancelable != null) {
        cancelable = detailCancelable.booleanValue();
      }
    }
    initEvent(type, bubbles, cancelable);
  }
  



  public void startFire()
  {
    Context context = Context.getCurrentContext();
    LinkedList<Event> events = (LinkedList)context.getThreadLocal("Event#current");
    if (events == null) {
      events = new LinkedList();
      context.putThreadLocal("Event#current", events);
    }
    events.add(this);
  }
  



  public void endFire()
  {
    ((LinkedList)Context.getCurrentContext().getThreadLocal("Event#current")).removeLast();
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public Object getSrcElement()
  {
    return srcElement_;
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setSrcElement(Object srcElement)
  {
    srcElement_ = srcElement;
  }
  



  @JsxGetter
  public Object getTarget()
  {
    return target_;
  }
  



  public void setTarget(EventTarget target)
  {
    target_ = target;
  }
  




  @JsxGetter
  public Scriptable getCurrentTarget()
  {
    return currentTarget_;
  }
  



  public void setCurrentTarget(Scriptable target)
  {
    currentTarget_ = target;
  }
  



  @JsxGetter
  public String getType()
  {
    return type_;
  }
  



  @JsxSetter
  public void setType(String type)
  {
    type_ = type;
  }
  



  public void setEventType(String eventType)
  {
    type_ = eventType;
  }
  



  @JsxGetter
  public long getTimeStamp()
  {
    return timeStamp_;
  }
  



  protected void setKeyCode(Object keyCode)
  {
    keyCode_ = keyCode;
  }
  



  public Object getKeyCode()
  {
    if (keyCode_ == null) {
      return Integer.valueOf(0);
    }
    return keyCode_;
  }
  



  public boolean getShiftKey()
  {
    return shiftKey_;
  }
  



  protected void setShiftKey(boolean shiftKey)
  {
    shiftKey_ = shiftKey;
  }
  



  public boolean getCtrlKey()
  {
    return ctrlKey_;
  }
  



  protected void setCtrlKey(boolean ctrlKey)
  {
    ctrlKey_ = ctrlKey;
  }
  



  public boolean getAltKey()
  {
    return altKey_;
  }
  



  protected void setAltKey(boolean altKey)
  {
    altKey_ = altKey;
  }
  



  @JsxGetter
  public int getEventPhase()
  {
    return eventPhase_;
  }
  





  public void setEventPhase(short phase)
  {
    if ((phase != 1) && (phase != 2) && (phase != 3)) {
      throw new IllegalArgumentException("Illegal phase specified: " + phase);
    }
    eventPhase_ = phase;
  }
  


  @JsxGetter
  public boolean getBubbles()
  {
    return bubbles_;
  }
  


  protected void setBubbles(boolean bubbles)
  {
    bubbles_ = bubbles;
  }
  


  @JsxGetter
  public boolean getCancelable()
  {
    return cancelable_;
  }
  


  protected void setCancelable(boolean cancelable)
  {
    cancelable_ = cancelable;
  }
  




  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public boolean getDefaultPrevented()
  {
    return (cancelable_) && (preventDefault_);
  }
  


  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public boolean getCancelBubble()
  {
    return stopPropagation_;
  }
  


  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setCancelBubble(boolean newValue)
  {
    stopPropagation_ = newValue;
  }
  


  @JsxFunction
  public void stopPropagation()
  {
    stopPropagation_ = true;
  }
  



  public boolean isPropagationStopped()
  {
    return stopPropagation_;
  }
  


  @JsxFunction
  public void stopImmediatePropagation()
  {
    stopImmediatePropagation_ = true;
    stopPropagation();
  }
  



  public boolean isImmediatePropagationStopped()
  {
    return stopImmediatePropagation_;
  }
  



  public Object getReturnValue()
  {
    return returnValue_;
  }
  



  public void setReturnValue(Object returnValue)
  {
    returnValue_ = returnValue;
  }
  



  public String getPropertyName()
  {
    return propertyName_;
  }
  





  @JsxFunction
  public void initEvent(String type, boolean bubbles, boolean cancelable)
  {
    type_ = type;
    bubbles_ = bubbles;
    cancelable_ = cancelable;
    if (("beforeunload".equals(type)) && (getBrowserVersion().hasFeature(BrowserVersionFeatures.EVENT_FOCUS_FOCUS_IN_BLUR_OUT))) {
      try {
        Class<?> klass = getClass();
        Method readMethod = klass.getMethod("getReturnValue", new Class[0]);
        Method writeMethod = klass.getMethod("setReturnValue", new Class[] { Object.class });
        defineProperty("returnValue", null, readMethod, writeMethod, 0);
        if ("Event".equals(klass.getSimpleName())) {
          setReturnValue(Boolean.TRUE);
        }
      }
      catch (Exception e) {
        throw Context.throwAsScriptRuntimeEx(e);
      }
    }
  }
  




  @JsxFunction
  public void preventDefault()
  {
    preventDefault_ = true;
  }
  







  public boolean isAborted(ScriptResult result)
  {
    return (ScriptResult.isFalse(result)) || (preventDefault_);
  }
  



  public String toString()
  {
    StringBuilder builder = new StringBuilder("Event ");
    builder.append(getType());
    builder.append(" (");
    builder.append("Current Target: ");
    builder.append(currentTarget_);
    builder.append(");");
    return builder.toString();
  }
}
