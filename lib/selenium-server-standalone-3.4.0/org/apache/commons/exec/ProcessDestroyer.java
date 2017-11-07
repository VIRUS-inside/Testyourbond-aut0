package org.apache.commons.exec;

public abstract interface ProcessDestroyer
{
  public abstract boolean add(Process paramProcess);
  
  public abstract boolean remove(Process paramProcess);
  
  public abstract int size();
}
