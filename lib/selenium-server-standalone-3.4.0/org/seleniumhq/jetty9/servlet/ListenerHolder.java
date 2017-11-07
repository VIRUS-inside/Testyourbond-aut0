package org.seleniumhq.jetty9.servlet;

import java.util.EventListener;



























public class ListenerHolder
  extends BaseHolder<EventListener>
{
  private EventListener _listener;
  
  public ListenerHolder(Source source)
  {
    super(source);
  }
  

  public void setListener(EventListener listener)
  {
    _listener = listener;
    setClassName(listener.getClass().getName());
    setHeldClass(listener.getClass());
    _extInstance = true;
  }
  
  public EventListener getListener()
  {
    return _listener;
  }
  


  public void doStart()
    throws Exception
  {
    if (_listener == null) {
      throw new IllegalStateException("No listener instance");
    }
    super.doStart();
  }
}
