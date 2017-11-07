package org.seleniumhq.jetty9.continuation;

import java.util.EventListener;

public abstract interface ContinuationListener
  extends EventListener
{
  public abstract void onComplete(Continuation paramContinuation);
  
  public abstract void onTimeout(Continuation paramContinuation);
}
