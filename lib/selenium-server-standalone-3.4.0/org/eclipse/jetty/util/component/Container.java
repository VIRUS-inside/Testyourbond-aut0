package org.eclipse.jetty.util.component;

import java.util.Collection;

public abstract interface Container
{
  public abstract boolean addBean(Object paramObject);
  
  public abstract Collection<Object> getBeans();
  
  public abstract <T> Collection<T> getBeans(Class<T> paramClass);
  
  public abstract <T> T getBean(Class<T> paramClass);
  
  public abstract boolean removeBean(Object paramObject);
  
  public abstract void addEventListener(Listener paramListener);
  
  public abstract void removeEventListener(Listener paramListener);
  
  public static abstract interface InheritedListener
    extends Container.Listener
  {}
  
  public static abstract interface Listener
  {
    public abstract void beanAdded(Container paramContainer, Object paramObject);
    
    public abstract void beanRemoved(Container paramContainer, Object paramObject);
  }
}
