package org.openqa.selenium.remote.http;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.openqa.selenium.InvalidArgumentException;



































































public class JsonHttpCommandCodec
  extends AbstractHttpCommandCodec
{
  public JsonHttpCommandCodec()
  {
    defineCommand("getElementAttribute", get("/session/:sessionId/element/:id/attribute/:name"));
    defineCommand("getElementLocationOnceScrolledIntoView", get("/session/:sessionId/element/:id/location_in_view"));
    defineCommand("isElementDisplayed", get("/session/:sessionId/element/:id/displayed"));
    defineCommand("submitElement", post("/session/:sessionId/element/:id/submit"));
    
    defineCommand("executeScript", post("/session/:sessionId/execute"));
    defineCommand("executeAsyncScript", post("/session/:sessionId/execute_async"));
    
    defineCommand("getPageSource", get("/session/:sessionId/source"));
    
    defineCommand("maximizeCurrentWindow", post("/session/:sessionId/window/:windowHandle/maximize"));
    defineCommand("getWindowPosition", get("/session/:sessionId/window/:windowHandle/position"));
    defineCommand("setWindowPosition", post("/session/:sessionId/window/:windowHandle/position"));
    defineCommand("getCurrentWindowSize", get("/session/:sessionId/window/:windowHandle/size"));
    defineCommand("setCurrentWindowSize", post("/session/:sessionId/window/:windowHandle/size"));
    defineCommand("getCurrentWindowHandle", get("/session/:sessionId/window_handle"));
    defineCommand("getWindowHandles", get("/session/:sessionId/window_handles"));
    
    defineCommand("acceptAlert", post("/session/:sessionId/accept_alert"));
    defineCommand("dismissAlert", post("/session/:sessionId/dismiss_alert"));
    defineCommand("getAlertText", get("/session/:sessionId/alert_text"));
    defineCommand("setAlertValue", post("/session/:sessionId/alert_text"));
    
    defineCommand("getActiveElement", post("/session/:sessionId/element/active"));
    
    defineCommand("clearLocalStorage", delete("/session/:sessionId/local_storage"));
    defineCommand("getLocalStorageKeys", get("/session/:sessionId/local_storage"));
    defineCommand("setLocalStorageItem", post("/session/:sessionId/local_storage"));
    defineCommand("removeLocalStorageItem", delete("/session/:sessionId/local_storage/key/:key"));
    defineCommand("getLocalStorageItem", get("/session/:sessionId/local_storage/key/:key"));
    defineCommand("getLocalStorageSize", get("/session/:sessionId/local_storage/size"));
    
    defineCommand("clearSessionStorage", delete("/session/:sessionId/session_storage"));
    defineCommand("getSessionStorageKey", get("/session/:sessionId/session_storage"));
    defineCommand("setSessionStorageItem", post("/session/:sessionId/session_storage"));
    defineCommand("removeSessionStorageItem", delete("/session/:sessionId/session_storage/key/:key"));
    defineCommand("getSessionStorageItem", get("/session/:sessionId/session_storage/key/:key"));
    defineCommand("getSessionStorageSize", get("/session/:sessionId/session_storage/size"));
    

    defineCommand("mouseButtonDown", post("/session/:sessionId/buttondown"));
    defineCommand("mouseButtonUp", post("/session/:sessionId/buttonup"));
    defineCommand("mouseClick", post("/session/:sessionId/click"));
    defineCommand("mouseDoubleClick", post("/session/:sessionId/doubleclick"));
    defineCommand("mouseMoveTo", post("/session/:sessionId/moveto"));
    defineCommand("sendKeysToActiveElement", post("/session/:sessionId/keys"));
    defineCommand("touchSingleTap", post("/session/:sessionId/touch/click"));
    defineCommand("touchDoubleTap", post("/session/:sessionId/touch/doubleclick"));
    defineCommand("touchDown", post("/session/:sessionId/touch/down"));
    defineCommand("touchFlick", post("/session/:sessionId/touch/flick"));
    defineCommand("touchLongPress", post("/session/:sessionId/touch/longclick"));
    defineCommand("touchMove", post("/session/:sessionId/touch/move"));
    defineCommand("touchScroll", post("/session/:sessionId/touch/scroll"));
    defineCommand("touchUp", post("/session/:sessionId/touch/up"));
  }
  
  protected Map<String, ?> amendParameters(String name, Map<String, ?> parameters)
  {
    switch (name) {
    case "maximizeCurrentWindow": 
    case "setWindowPosition": 
    case "getCurrentWindowSize": 
    case "setCurrentWindowSize": 
      return 
      


        ImmutableMap.builder().putAll(parameters).put("windowHandle", "current").put("handle", "current").build();
    
    case "setTimeout": 
      if (parameters.size() != 1) {
        throw new InvalidArgumentException("The JSON wire protocol only supports setting one time out at a time");
      }
      
      Map.Entry<String, ?> entry = (Map.Entry)parameters.entrySet().iterator().next();
      String type = (String)entry.getKey();
      if ("pageLoad".equals(type)) {
        type = "page load";
      }
      return ImmutableMap.of("type", type, "ms", entry.getValue());
    
    case "switchToWindow": 
      return 
      
        ImmutableMap.builder().put("name", parameters.get("handle")).build();
    }
    
    return parameters;
  }
}
