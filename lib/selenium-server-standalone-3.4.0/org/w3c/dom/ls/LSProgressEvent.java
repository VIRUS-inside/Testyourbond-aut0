package org.w3c.dom.ls;

import org.w3c.dom.events.Event;

public abstract interface LSProgressEvent
  extends Event
{
  public abstract LSInput getInput();
  
  public abstract int getPosition();
  
  public abstract int getTotalSize();
}
