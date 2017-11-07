package org.apache.commons.lang3.concurrent;

public abstract interface CircuitBreaker<T>
{
  public abstract boolean isOpen();
  
  public abstract boolean isClosed();
  
  public abstract boolean checkState();
  
  public abstract void close();
  
  public abstract void open();
  
  public abstract boolean incrementAndCheckState(T paramT);
}
