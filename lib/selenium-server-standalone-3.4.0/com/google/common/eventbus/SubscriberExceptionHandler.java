package com.google.common.eventbus;

public abstract interface SubscriberExceptionHandler
{
  public abstract void handleException(Throwable paramThrowable, SubscriberExceptionContext paramSubscriberExceptionContext);
}
