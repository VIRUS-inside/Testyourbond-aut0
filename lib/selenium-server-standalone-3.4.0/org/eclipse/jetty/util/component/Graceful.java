package org.eclipse.jetty.util.component;

import java.util.concurrent.Future;

public abstract interface Graceful
{
  public abstract Future<Void> shutdown();
}
