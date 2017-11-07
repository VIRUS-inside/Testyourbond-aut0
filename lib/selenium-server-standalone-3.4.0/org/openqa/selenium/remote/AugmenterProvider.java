package org.openqa.selenium.remote;

public abstract interface AugmenterProvider
{
  public abstract Class<?> getDescribedInterface();
  
  public abstract InterfaceImplementation getImplementation(Object paramObject);
}
