package com.gargoylesoftware.htmlunit.javascript.host.event;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import java.util.LinkedList;
import net.sourceforge.htmlunit.corejs.javascript.Context;

































































@JsxClass
public class MouseEvent
  extends UIEvent
{
  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public static final int MOZ_SOURCE_UNKNOWN = 0;
  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public static final int MOZ_SOURCE_MOUSE = 1;
  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public static final int MOZ_SOURCE_PEN = 2;
  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public static final int MOZ_SOURCE_ERASER = 3;
  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public static final int MOZ_SOURCE_CURSOR = 4;
  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public static final int MOZ_SOURCE_TOUCH = 5;
  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public static final int MOZ_SOURCE_KEYBOARD = 6;
  public static final String TYPE_CLICK = "click";
  public static final String TYPE_DBL_CLICK = "dblclick";
  public static final String TYPE_MOUSE_OVER = "mouseover";
  public static final String TYPE_MOUSE_MOVE = "mousemove";
  public static final String TYPE_MOUSE_OUT = "mouseout";
  public static final String TYPE_MOUSE_DOWN = "mousedown";
  public static final String TYPE_MOUSE_UP = "mouseup";
  public static final String TYPE_CONTEXT_MENU = "contextmenu";
  public static final int BUTTON_LEFT = 0;
  public static final int BUTTON_MIDDLE = 1;
  public static final int BUTTON_RIGHT = 2;
  private Integer screenX_;
  private Integer screenY_;
  private Integer clientX_;
  private Integer clientY_;
  private int button_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public MouseEvent()
  {
    screenX_ = Integer.valueOf(0);
    screenY_ = Integer.valueOf(0);
    setDetail(1L);
  }
  










  public MouseEvent(DomNode domNode, String type, boolean shiftKey, boolean ctrlKey, boolean altKey, int button)
  {
    super(domNode, type);
    setShiftKey(shiftKey);
    setCtrlKey(ctrlKey);
    setAltKey(altKey);
    setMetaKey(false);
    
    if ((button != 0) && (button != 1) && (button != 2)) {
      throw new IllegalArgumentException("Invalid button code: " + button);
    }
    button_ = button;
    
    if ("dblclick".equals(type)) {
      setDetail(2L);
    }
    else {
      setDetail(1L);
    }
  }
  



  @JsxGetter
  public int getClientX()
  {
    if (clientX_ == null) {
      clientX_ = Integer.valueOf(getScreenX());
    }
    return clientX_.intValue();
  }
  



  @JsxSetter
  public void setClientX(int value)
  {
    clientX_ = Integer.valueOf(value);
  }
  






  @JsxGetter
  public int getScreenX()
  {
    if (screenX_ == null) {
      HTMLElement target = (HTMLElement)getTarget();
      screenX_ = Integer.valueOf(target.getPosX() + 10);
    }
    return screenX_.intValue();
  }
  




  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public int getPageX()
  {
    return getScreenX();
  }
  



  @JsxGetter
  public int getClientY()
  {
    if (clientY_ == null) {
      clientY_ = Integer.valueOf(getScreenY());
    }
    return clientY_.intValue();
  }
  



  @JsxSetter
  public void setClientY(int value)
  {
    clientY_ = Integer.valueOf(value);
  }
  






  @JsxGetter
  public int getScreenY()
  {
    if (screenY_ == null) {
      HTMLElement target = (HTMLElement)getTarget();
      screenY_ = Integer.valueOf(target.getPosY() + 10);
    }
    return screenY_.intValue();
  }
  




  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public int getPageY()
  {
    return getScreenY();
  }
  



  @JsxGetter
  public int getButton()
  {
    return button_;
  }
  



  @JsxSetter
  public void setButton(int value)
  {
    button_ = value;
  }
  




  @JsxGetter
  public int getWhich()
  {
    return button_ + 1;
  }
  

































  @JsxFunction
  public void initMouseEvent(String type, boolean bubbles, boolean cancelable, Object view, int detail, int screenX, int screenY, int clientX, int clientY, boolean ctrlKey, boolean altKey, boolean shiftKey, boolean metaKey, int button, Object relatedTarget)
  {
    initUIEvent(type, bubbles, cancelable, view, detail);
    screenX_ = Integer.valueOf(screenX);
    screenY_ = Integer.valueOf(screenY);
    clientX_ = Integer.valueOf(clientX);
    clientY_ = Integer.valueOf(clientY);
    setCtrlKey(ctrlKey);
    setAltKey(altKey);
    setShiftKey(shiftKey);
    setMetaKey(metaKey);
    button_ = button;
  }
  





  public static MouseEvent getCurrentMouseEvent()
  {
    Context context = Context.getCurrentContext();
    if (context != null) {
      LinkedList<Event> events = (LinkedList)context.getThreadLocal("Event#current");
      if ((events != null) && (!events.isEmpty()) && ((events.getLast() instanceof MouseEvent))) {
        return (MouseEvent)events.getLast();
      }
    }
    return null;
  }
  




  public static boolean isMouseEvent(String type)
  {
    return ("click".equals(type)) || 
      ("mouseover".equals(type)) || 
      ("mousemove".equals(type)) || 
      ("mouseout".equals(type)) || 
      ("mousedown".equals(type)) || 
      ("mouseup".equals(type)) || 
      ("contextmenu".equals(type));
  }
  



  @JsxGetter
  public boolean getAltKey()
  {
    return super.getAltKey();
  }
  



  @JsxGetter
  public boolean getCtrlKey()
  {
    return super.getCtrlKey();
  }
  



  @JsxGetter
  public boolean getShiftKey()
  {
    return super.getShiftKey();
  }
}
