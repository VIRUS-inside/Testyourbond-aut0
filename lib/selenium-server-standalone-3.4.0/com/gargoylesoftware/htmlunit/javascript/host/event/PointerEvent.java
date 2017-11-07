package com.gargoylesoftware.htmlunit.javascript.host.event;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.NativeObject;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;




























@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
public class PointerEvent
  extends MouseEvent
{
  private int pointerId_;
  private int width_;
  private int height_;
  private double pressure_;
  private int tiltX_;
  private int tiltY_;
  private String pointerType_ = "";
  




  private boolean isPrimary_;
  




  public PointerEvent() {}
  




  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public static Scriptable jsConstructor(Context cx, Object[] args, Function ctorObj, boolean inNewExpr)
  {
    PointerEvent event = new PointerEvent();
    if (args.length != 0) {
      event.setType(Context.toString(args[0]));
      event.setBubbles(false);
      event.setCancelable(false);
      width_ = 1;
      height_ = 1;
    }
    
    if (args.length > 1) {
      NativeObject object = (NativeObject)args[1];
      event.setBubbles(((Boolean)getValue(object, "bubbles", Boolean.valueOf(event.getBubbles()))).booleanValue());
      pointerId_ = ((Integer)getValue(object, "pointerId", Integer.valueOf(pointerId_))).intValue();
      width_ = ((Integer)getValue(object, "width", Integer.valueOf(width_))).intValue();
      height_ = ((Integer)getValue(object, "height", Integer.valueOf(height_))).intValue();
      pressure_ = ((Double)getValue(object, "pressure", Double.valueOf(pressure_))).doubleValue();
      tiltX_ = ((Integer)getValue(object, "tiltX", Integer.valueOf(tiltX_))).intValue();
      tiltY_ = ((Integer)getValue(object, "tiltY", Integer.valueOf(tiltY_))).intValue();
      pointerType_ = ((String)getValue(object, "pointerType", pointerType_));
      isPrimary_ = ((Boolean)getValue(object, "isPrimary", Boolean.valueOf(isPrimary_))).booleanValue();
    }
    return event;
  }
  
  private static Object getValue(ScriptableObject object, String name, Object defaulValue) {
    Object value = object.get(name);
    if (value != null) {
      if ((defaulValue instanceof String)) {
        value = String.valueOf(value);
      }
      else if ((defaulValue instanceof Double)) {
        value = Double.valueOf(Context.toNumber(value));
      }
      else if ((defaulValue instanceof Number)) {
        value = Integer.valueOf((int)Context.toNumber(value));
      }
      else {
        value = Boolean.valueOf(Context.toBoolean(value));
      }
    }
    else {
      value = defaulValue;
    }
    return value;
  }
  










  public PointerEvent(DomNode domNode, String type, boolean shiftKey, boolean ctrlKey, boolean altKey, int button)
  {
    super(domNode, type, shiftKey, ctrlKey, altKey, button);
    setDetail(0L);
    
    pointerId_ = 1;
    width_ = 1;
    height_ = 1;
    pointerType_ = "mouse";
    isPrimary_ = true;
  }
  
























































  @JsxFunction
  public void initPointerEvent(String type, boolean bubbles, boolean cancelable, Object view, int detail, int screenX, int screenY, int clientX, int clientY, boolean ctrlKey, boolean altKey, boolean shiftKey, boolean metaKey, int button, Object relatedTarget, int offsetX, int offsetY, int width, int height, Double pressure, int rotation, int tiltX, int tiltY, int pointerId, String pointerType, int hwTimestamp, boolean isPrimary)
  {
    super.initMouseEvent(type, bubbles, cancelable, view, detail, screenX, screenY, clientX, clientY, ctrlKey, 
      altKey, shiftKey, metaKey, button, relatedTarget);
    width_ = width;
    height_ = height;
    pressure_ = pressure.doubleValue();
    tiltX_ = tiltX;
    tiltY_ = tiltY;
    pointerId_ = pointerId;
    pointerType_ = pointerType;
    isPrimary_ = isPrimary;
  }
  


  @JsxGetter
  public long getPointerId()
  {
    return pointerId_;
  }
  


  @JsxGetter
  public long getWidth()
  {
    return width_;
  }
  


  @JsxGetter
  public long getHeight()
  {
    return height_;
  }
  


  @JsxGetter
  public double getPressure()
  {
    return pressure_;
  }
  


  @JsxGetter
  public long getTiltX()
  {
    return tiltX_;
  }
  


  @JsxGetter
  public long getTiltY()
  {
    return tiltY_;
  }
  


  @JsxGetter
  public String getPointerType()
  {
    return pointerType_;
  }
  


  @JsxGetter(propertyName="isPrimary")
  public boolean isPrimary()
  {
    return isPrimary_;
  }
}
