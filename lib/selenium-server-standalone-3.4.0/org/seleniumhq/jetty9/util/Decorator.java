package org.seleniumhq.jetty9.util;

public abstract interface Decorator
{
  public abstract <T> T decorate(T paramT);
  
  public abstract void destroy(Object paramObject);
}
