package org.apache.xerces.dom.events;

import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget;

public class EventImpl
  implements Event
{
  public String type = null;
  public EventTarget target;
  public EventTarget currentTarget;
  public short eventPhase;
  public boolean initialized = false;
  public boolean bubbles = true;
  public boolean cancelable = false;
  public boolean stopPropagation = false;
  public boolean preventDefault = false;
  protected long timeStamp = System.currentTimeMillis();
  
  public EventImpl() {}
  
  public void initEvent(String paramString, boolean paramBoolean1, boolean paramBoolean2)
  {
    type = paramString;
    bubbles = paramBoolean1;
    cancelable = paramBoolean2;
    initialized = true;
  }
  
  public boolean getBubbles()
  {
    return bubbles;
  }
  
  public boolean getCancelable()
  {
    return cancelable;
  }
  
  public EventTarget getCurrentTarget()
  {
    return currentTarget;
  }
  
  public short getEventPhase()
  {
    return eventPhase;
  }
  
  public EventTarget getTarget()
  {
    return target;
  }
  
  public String getType()
  {
    return type;
  }
  
  public long getTimeStamp()
  {
    return timeStamp;
  }
  
  public void stopPropagation()
  {
    stopPropagation = true;
  }
  
  public void preventDefault()
  {
    preventDefault = true;
  }
}
