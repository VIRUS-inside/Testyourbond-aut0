package com.gargoylesoftware.htmlunit.javascript.host.event;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.html.DomDocumentFragment;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClasses;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;







































@JsxClasses({@com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(isJSObject=false, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})})
public class EventTarget
  extends SimpleScriptable
{
  private EventListenersContainer eventListenersContainer_;
  
  @JsxConstructor
  public EventTarget() {}
  
  @JsxFunction
  public void addEventListener(String type, Scriptable listener, boolean useCapture)
  {
    getEventListenersContainer().addEventListener(type, listener, useCapture);
  }
  



  public final EventListenersContainer getEventListenersContainer()
  {
    if (eventListenersContainer_ == null) {
      eventListenersContainer_ = new EventListenersContainer(this);
    }
    return eventListenersContainer_;
  }
  





  public ScriptResult executeEventLocally(Event event)
  {
    EventListenersContainer eventListenersContainer = getEventListenersContainer();
    if (eventListenersContainer != null) {
      Window window = getWindow();
      Object[] args = { event };
      

      Object[] propHandlerArgs = args;
      
      Event previousEvent = window.getCurrentEvent();
      window.setCurrentEvent(event);
      try {
        return eventListenersContainer.executeListeners(event, args, propHandlerArgs);
      }
      finally {
        window.setCurrentEvent(previousEvent);
      }
    }
    return null;
  }
  




  public ScriptResult fireEvent(Event event)
  {
    Window window = getWindow();
    Object[] args = { event };
    
    event.startFire();
    ScriptResult result = null;
    Event previousEvent = window.getCurrentEvent();
    window.setCurrentEvent(event);
    
    try
    {
      EventListenersContainer windowsListeners = window.getEventListenersContainer();
      

      event.setEventPhase((short)1);
      boolean windowEventIfDetached = getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_EVENT_WINDOW_EXECUTE_IF_DITACHED);
      
      boolean isAttached = false;
      for (DomNode node = getDomNodeOrNull(); node != null; node = node.getParentNode()) {
        if (((node instanceof Document)) || ((node instanceof DomDocumentFragment))) {
          isAttached = true;
          break;
        }
      }
      ScriptResult localScriptResult1;
      if ((isAttached) || (windowEventIfDetached)) {
        result = windowsListeners.executeCapturingListeners(event, args);
        if (event.isPropagationStopped()) {
          return result;
        }
      }
      List<EventTarget> eventTargetList = new ArrayList();
      EventTarget eventTarget = this;
      while (eventTarget != null) {
        if (isAttached) {
          eventTargetList.add(eventTarget);
        }
        DomNode domNode = eventTarget.getDomNodeOrNull();
        eventTarget = null;
        if ((domNode != null) && (domNode.getParentNode() != null)) {
          eventTarget = (EventTarget)domNode.getParentNode().getScriptableObject();
        }
      }
      
      boolean ie = getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_CALL_RESULT_IS_LAST_RETURN_VALUE);
      for (int i = eventTargetList.size() - 1; i >= 0; i--) {
        EventTarget jsNode = (EventTarget)eventTargetList.get(i);
        EventListenersContainer elc = eventListenersContainer_;
        if ((elc != null) && (isAttached)) {
          ScriptResult r = elc.executeCapturingListeners(event, args);
          result = ScriptResult.combine(r, result, ie);
          if (event.isPropagationStopped()) {
            return result;
          }
        }
      }
      

      Object[] propHandlerArgs = args;
      

      event.setEventPhase((short)2);
      eventTarget = this;
      while (eventTarget != null) {
        EventTarget jsNode = eventTarget;
        EventListenersContainer elc = eventListenersContainer_;
        if ((elc != null) && (!(jsNode instanceof Window)) && ((isAttached) || (!(jsNode instanceof HTMLElement)))) {
          ScriptResult r = elc.executeBubblingListeners(event, args, propHandlerArgs);
          result = ScriptResult.combine(r, result, ie);
          if (event.isPropagationStopped()) {
            return result;
          }
        }
        DomNode domNode = eventTarget.getDomNodeOrNull();
        eventTarget = null;
        if ((domNode != null) && (domNode.getParentNode() != null)) {
          eventTarget = (EventTarget)domNode.getParentNode().getScriptableObject();
        }
        event.setEventPhase((short)3);
      }
      
      if ((isAttached) || (windowEventIfDetached)) {
        ScriptResult r = windowsListeners.executeBubblingListeners(event, args, propHandlerArgs);
        result = ScriptResult.combine(r, result, ie);
      }
    }
    finally {
      event.endFire();
      window.setCurrentEvent(previousEvent);
    }
    event.endFire();
    window.setCurrentEvent(previousEvent);
    

    return result;
  }
  




  public boolean hasEventHandlers(String eventName)
  {
    if (eventListenersContainer_ == null) {
      return false;
    }
    return eventListenersContainer_.hasEventListeners(StringUtils.substring(eventName, 2));
  }
  




  public Function getEventHandler(String eventName)
  {
    if (eventListenersContainer_ == null) {
      return null;
    }
    return eventListenersContainer_.getEventHandler(StringUtils.substring(eventName, 2));
  }
  




  protected Object getEventHandlerProp(String eventName)
  {
    if (eventListenersContainer_ == null) {
      return null;
    }
    
    String name = StringUtils.substring(eventName.toLowerCase(Locale.ROOT), 2);
    return eventListenersContainer_.getEventHandlerProp(name);
  }
  








  @JsxFunction
  public boolean dispatchEvent(Event event)
  {
    event.setTarget(this);
    DomElement element = (DomElement)getDomNodeOrNull();
    ScriptResult result = null;
    if (event.getType().equals("click")) {
      try {
        element.click(event, true);
      }
      catch (IOException e) {
        throw Context.reportRuntimeError("Error calling click(): " + e.getMessage());
      }
      
    } else {
      result = fireEvent(event);
    }
    return !event.isAborted(result);
  }
  







  @JsxFunction
  public void removeEventListener(String type, Scriptable listener, boolean useCapture)
  {
    getEventListenersContainer().removeEventListener(type, listener, useCapture);
  }
  

  protected void setEventHandlerProp(String eventName, Object value)
  {
    EventListenersContainer container;
    
    EventListenersContainer container;
    
    if (isEventHandlerOnWindow()) {
      container = getWindow().getEventListenersContainer();
    }
    else {
      container = getEventListenersContainer();
    }
    container.setEventHandlerProp(
      StringUtils.substring(eventName.toLowerCase(Locale.ROOT), 2), value);
  }
  



  protected boolean isEventHandlerOnWindow()
  {
    return false;
  }
  




  public void setEventHandler(String eventName, Function eventHandler)
  {
    setEventHandlerProp(eventName, eventHandler);
  }
  


  protected void clearEventListenersContainer()
  {
    eventListenersContainer_ = null;
  }
}
