package org.seleniumhq.jetty9.server;

import org.seleniumhq.jetty9.util.annotation.ManagedAttribute;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;
import org.seleniumhq.jetty9.util.component.LifeCycle;

@ManagedObject("Handler of Multiple Handlers")
public abstract interface HandlerContainer
  extends LifeCycle
{
  @ManagedAttribute("handlers in this container")
  public abstract Handler[] getHandlers();
  
  @ManagedAttribute("all contained handlers")
  public abstract Handler[] getChildHandlers();
  
  public abstract Handler[] getChildHandlersByClass(Class<?> paramClass);
  
  public abstract <T extends Handler> T getChildHandlerByClass(Class<T> paramClass);
}
