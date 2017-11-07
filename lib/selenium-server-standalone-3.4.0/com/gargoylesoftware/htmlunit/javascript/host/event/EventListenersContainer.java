package com.gargoylesoftware.htmlunit.javascript.host.event;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.NativeObject;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;






















public class EventListenersContainer
  implements Serializable
{
  private static final Log LOG = LogFactory.getLog(EventListenersContainer.class);
  
  static class TypeContainer implements Serializable {
    private List<Scriptable> capturingListeners_;
    private List<Scriptable> bubblingListeners_;
    private Object handler_;
    
    TypeContainer() {
      capturingListeners_ = Collections.unmodifiableList(new ArrayList());
      bubblingListeners_ = Collections.unmodifiableList(new ArrayList());
    }
    
    private TypeContainer(List<Scriptable> capturingListeners, List<Scriptable> bubblingListeners, Object handler)
    {
      capturingListeners_ = Collections.unmodifiableList(new ArrayList(capturingListeners));
      bubblingListeners_ = Collections.unmodifiableList(new ArrayList(bubblingListeners));
      handler_ = handler;
    }
    
    private List<Scriptable> getListeners(boolean useCapture) {
      if (useCapture) {
        return capturingListeners_;
      }
      return bubblingListeners_;
    }
    
    private synchronized boolean addListener(Scriptable listener, boolean useCapture) {
      List<Scriptable> listeners = getListeners(useCapture);
      
      if (listeners.contains(listener)) {
        return false;
      }
      
      List<Scriptable> newListeners = new ArrayList(listeners.size() + 1);
      newListeners.addAll(listeners);
      newListeners.add(listener);
      newListeners = Collections.unmodifiableList(newListeners);
      
      if (useCapture) {
        capturingListeners_ = newListeners;
      }
      else {
        bubblingListeners_ = newListeners;
      }
      
      return true;
    }
    
    private synchronized void removeListener(Scriptable listener, boolean useCapture) {
      List<Scriptable> listeners = getListeners(useCapture);
      
      int idx = listeners.indexOf(listener);
      if (idx < 0) {
        return;
      }
      
      List<Scriptable> newListeners = new ArrayList(listeners);
      newListeners.remove(idx);
      newListeners = Collections.unmodifiableList(newListeners);
      
      if (useCapture) {
        capturingListeners_ = newListeners;
      }
      else {
        bubblingListeners_ = newListeners;
      }
    }
    
    protected TypeContainer clone()
    {
      return new TypeContainer(capturingListeners_, bubblingListeners_, handler_);
    }
  }
  
  private final Map<String, TypeContainer> typeContainers_ = new HashMap();
  

  private final EventTarget jsNode_;
  


  public EventListenersContainer(EventTarget jsNode)
  {
    jsNode_ = jsNode;
  }
  






  public boolean addEventListener(String type, Scriptable listener, boolean useCapture)
  {
    if (listener == null) {
      return true;
    }
    
    TypeContainer container = getTypeContainer(type);
    boolean added = container.addListener(listener, useCapture);
    if (!added) {
      if (LOG.isDebugEnabled()) {
        LOG.debug(type + " listener already registered, skipping it (" + listener + ")");
      }
      return false;
    }
    return true;
  }
  
  private TypeContainer getTypeContainer(String type) {
    String typeLC = type.toLowerCase(Locale.ROOT);
    TypeContainer container = (TypeContainer)typeContainers_.get(typeLC);
    if (container == null) {
      container = new TypeContainer();
      typeContainers_.put(typeLC, container);
    }
    return container;
  }
  





  public List<Scriptable> getListeners(String eventType, boolean useCapture)
  {
    TypeContainer container = (TypeContainer)typeContainers_.get(eventType.toLowerCase(Locale.ROOT));
    if (container != null) {
      return container.getListeners(useCapture);
    }
    return null;
  }
  





  public void removeEventListener(String eventType, Scriptable listener, boolean useCapture)
  {
    if (listener == null) {
      return;
    }
    
    TypeContainer container = (TypeContainer)typeContainers_.get(eventType.toLowerCase(Locale.ROOT));
    if (container != null) {
      container.removeListener(listener, useCapture);
    }
  }
  




  public void setEventHandlerProp(String eventName, Object value)
  {
    Object handler = value;
    if (handler == Undefined.instance) {
      handler = null;
    }
    
    TypeContainer container = getTypeContainer(eventName);
    handler_ = handler;
  }
  




  public Object getEventHandlerProp(String eventType)
  {
    TypeContainer container = (TypeContainer)typeContainers_.get(eventType);
    if (container == null) {
      return null;
    }
    return handler_;
  }
  
  private ScriptResult executeEventListeners(boolean useCapture, Event event, Object[] args) {
    DomNode node = jsNode_.getDomNodeOrNull();
    
    if ((node == null) || (!node.handles(event))) {
      return null;
    }
    
    ScriptResult allResult = null;
    List<Scriptable> listeners = getListeners(event.getType(), useCapture);
    if ((listeners != null) && (!listeners.isEmpty())) {
      event.setCurrentTarget(jsNode_);
      HtmlPage page = (HtmlPage)node.getPage();
      

      for (Scriptable listener : listeners) {
        Function function = null;
        Scriptable thisObject = null;
        if ((listener instanceof Function)) {
          function = (Function)listener;
          thisObject = jsNode_;
        }
        else if ((listener instanceof NativeObject)) {
          Object handleEvent = ScriptableObject.getProperty(listener, "handleEvent");
          if ((handleEvent instanceof Function)) {
            function = (Function)handleEvent;
            thisObject = listener;
          }
        }
        if (function != null) {
          ScriptResult result = 
            page.executeJavaScriptFunctionIfPossible(function, thisObject, args, node);
          if (event.isPropagationStopped()) {
            allResult = result;
          }
          if (jsNode_.getBrowserVersion().hasFeature(BrowserVersionFeatures.EVENT_FALSE_RESULT)) {
            if (ScriptResult.isFalse(result)) {
              allResult = result;
            }
            else {
              Object eventReturnValue = event.getReturnValue();
              if (((eventReturnValue instanceof Boolean)) && (!((Boolean)eventReturnValue).booleanValue())) {
                allResult = new ScriptResult(Boolean.FALSE, page);
              }
            }
          }
        }
        if (event.isImmediatePropagationStopped()) {
          break;
        }
      }
    }
    return allResult;
  }
  
  private ScriptResult executeEventHandler(Event event, Object[] propHandlerArgs) {
    DomNode node = jsNode_.getDomNodeOrNull();
    
    if ((node != null) && (!node.handles(event))) {
      return null;
    }
    Function handler = getEventHandler(event.getType());
    if (handler != null) {
      event.setCurrentTarget(jsNode_);
      HtmlPage page = (HtmlPage)(node != null ? 
        node.getPage() : 
        jsNode_.getWindow().getWebWindow().getEnclosedPage());
      if (LOG.isDebugEnabled()) {
        LOG.debug("Executing " + event.getType() + " handler for " + node);
      }
      return page.executeJavaScriptFunctionIfPossible(handler, jsNode_, 
        propHandlerArgs, page);
    }
    return null;
  }
  







  public ScriptResult executeBubblingListeners(Event event, Object[] args, Object[] propHandlerArgs)
  {
    ScriptResult result = null;
    

    DomNode domNode = jsNode_.getDomNodeOrNull();
    if (!(domNode instanceof HtmlBody)) {
      result = executeEventHandler(event, propHandlerArgs);
      if (event.isPropagationStopped()) {
        return result;
      }
    }
    

    ScriptResult newResult = executeEventListeners(false, event, args);
    if (newResult != null) {
      result = newResult;
    }
    return result;
  }
  





  public ScriptResult executeCapturingListeners(Event event, Object[] args)
  {
    return executeEventListeners(true, event, args);
  }
  




  public Function getEventHandler(String eventName)
  {
    Object handler = getEventHandlerProp(eventName.toLowerCase(Locale.ROOT));
    if ((handler instanceof Function)) {
      return (Function)handler;
    }
    return null;
  }
  




  public boolean hasEventListeners(String eventType)
  {
    TypeContainer container = (TypeContainer)typeContainers_.get(eventType);
    return (container != null) && (
      ((handler_ instanceof Function)) || 
      (!bubblingListeners_.isEmpty()) || 
      (!capturingListeners_.isEmpty()));
  }
  







  public ScriptResult executeListeners(Event event, Object[] args, Object[] propHandlerArgs)
  {
    event.setEventPhase((short)1);
    ScriptResult result = executeEventListeners(true, event, args);
    if (event.isPropagationStopped()) {
      return result;
    }
    

    event.setEventPhase((short)2);
    ScriptResult newResult = executeEventHandler(event, propHandlerArgs);
    if (newResult != null) {
      result = newResult;
    }
    if (event.isPropagationStopped()) {
      return result;
    }
    

    event.setEventPhase((short)3);
    newResult = executeEventListeners(false, event, args);
    if (newResult != null) {
      result = newResult;
    }
    
    return result;
  }
  



  public void copyFrom(EventListenersContainer eventListenersContainer)
  {
    for (Map.Entry<String, TypeContainer> entry : typeContainers_.entrySet()) {
      TypeContainer conainer = ((TypeContainer)entry.getValue()).clone();
      typeContainers_.put((String)entry.getKey(), conainer);
    }
  }
  



  public String toString()
  {
    return getClass().getSimpleName() + "[node=" + jsNode_ + " handlers=" + typeContainers_.keySet() + "]";
  }
}
