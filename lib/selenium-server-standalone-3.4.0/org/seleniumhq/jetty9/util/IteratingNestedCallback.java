package org.seleniumhq.jetty9.util;

import org.seleniumhq.jetty9.util.thread.Invocable.InvocationType;





































public abstract class IteratingNestedCallback
  extends IteratingCallback
{
  final Callback _callback;
  
  public IteratingNestedCallback(Callback callback)
  {
    _callback = callback;
  }
  

  public Invocable.InvocationType getInvocationType()
  {
    return _callback.getInvocationType();
  }
  

  protected void onCompleteSuccess()
  {
    _callback.succeeded();
  }
  

  protected void onCompleteFailure(Throwable x)
  {
    _callback.failed(x);
  }
  

  public String toString()
  {
    return String.format("%s@%x", new Object[] { getClass().getSimpleName(), Integer.valueOf(hashCode()) });
  }
}
