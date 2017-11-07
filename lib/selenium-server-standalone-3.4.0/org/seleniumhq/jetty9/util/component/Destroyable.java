package org.seleniumhq.jetty9.util.component;

import org.seleniumhq.jetty9.util.annotation.ManagedObject;
import org.seleniumhq.jetty9.util.annotation.ManagedOperation;

@ManagedObject
public abstract interface Destroyable
{
  @ManagedOperation(value="Destroys this component", impact="ACTION")
  public abstract void destroy();
}
