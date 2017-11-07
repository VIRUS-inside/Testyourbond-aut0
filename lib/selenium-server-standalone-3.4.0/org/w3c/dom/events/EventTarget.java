package org.w3c.dom.events;

public abstract interface EventTarget
{
  public abstract void addEventListener(String paramString, EventListener paramEventListener, boolean paramBoolean);
  
  public abstract void removeEventListener(String paramString, EventListener paramEventListener, boolean paramBoolean);
  
  public abstract boolean dispatchEvent(Event paramEvent)
    throws EventException;
}
