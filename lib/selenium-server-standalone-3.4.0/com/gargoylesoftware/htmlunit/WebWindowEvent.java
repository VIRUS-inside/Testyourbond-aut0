package com.gargoylesoftware.htmlunit;

import java.util.EventObject;




































public final class WebWindowEvent
  extends EventObject
{
  private final Page oldPage_;
  private final Page newPage_;
  private final int type_;
  public static final int OPEN = 1;
  public static final int CLOSE = 2;
  public static final int CHANGE = 3;
  
  public WebWindowEvent(WebWindow webWindow, int type, Page oldPage, Page newPage)
  {
    super(webWindow);
    oldPage_ = oldPage;
    newPage_ = newPage;
    
    switch (type) {
    case 1: 
    case 2: 
    case 3: 
      type_ = type;
      break;
    
    default: 
      throw new IllegalArgumentException(
        "type must be one of OPEN, CLOSE, CHANGE but got " + type);
    }
    
  }
  





  public boolean equals(Object object)
  {
    if (object == null) {
      return false;
    }
    if (getClass() == object.getClass()) {
      WebWindowEvent event = (WebWindowEvent)object;
      return (isEqual(getSource(), event.getSource())) && 
        (getEventType() == event.getEventType()) && 
        (isEqual(getOldPage(), event.getOldPage())) && 
        (isEqual(getNewPage(), event.getNewPage()));
    }
    return false;
  }
  




  public int hashCode()
  {
    return source.hashCode();
  }
  



  public Page getOldPage()
  {
    return oldPage_;
  }
  



  public Page getNewPage()
  {
    return newPage_;
  }
  



  public WebWindow getWebWindow()
  {
    return (WebWindow)getSource();
  }
  
  private static boolean isEqual(Object object1, Object object2) {
    boolean result;
    boolean result;
    if ((object1 == null) && (object2 == null)) {
      result = true;
    } else { boolean result;
      if ((object1 == null) || (object2 == null)) {
        result = false;
      }
      else {
        result = object1.equals(object2);
      }
    }
    return result;
  }
  




  public String toString()
  {
    StringBuilder builder = new StringBuilder(80);
    builder.append("WebWindowEvent(source=[");
    builder.append(getSource());
    builder.append("] type=[");
    switch (type_) {
    case 1: 
      builder.append("OPEN");
      break;
    case 2: 
      builder.append("CLOSE");
      break;
    case 3: 
      builder.append("CHANGE");
      break;
    default: 
      builder.append(type_);
    }
    
    builder.append("] oldPage=[");
    builder.append(getOldPage());
    builder.append("] newPage=[");
    builder.append(getNewPage());
    builder.append("])");
    
    return builder.toString();
  }
  
  public int getEventType()
  {
    return type_;
  }
}
