package com.google.common.eventbus;

import com.google.common.annotations.Beta;
import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.base.Preconditions;





























@Beta
public class DeadEvent
{
  private final Object source;
  private final Object event;
  
  public DeadEvent(Object source, Object event)
  {
    this.source = Preconditions.checkNotNull(source);
    this.event = Preconditions.checkNotNull(event);
  }
  





  public Object getSource()
  {
    return source;
  }
  





  public Object getEvent()
  {
    return event;
  }
  
  public String toString()
  {
    return 
    

      MoreObjects.toStringHelper(this).add("source", source).add("event", event).toString();
  }
}
