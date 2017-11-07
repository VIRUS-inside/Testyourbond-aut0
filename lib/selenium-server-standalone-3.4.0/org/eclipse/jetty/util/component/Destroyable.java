package org.eclipse.jetty.util.component;

import org.eclipse.jetty.util.annotation.ManagedObject;
import org.eclipse.jetty.util.annotation.ManagedOperation;

@ManagedObject
public abstract interface Destroyable
{
  @ManagedOperation(value="Destroys this component", impact="ACTION")
  public abstract void destroy();
}
