package org.seleniumhq.jetty9.continuation;

import javax.servlet.ServletResponse;

public abstract interface Continuation
{
  public static final String ATTRIBUTE = "org.seleniumhq.jetty9.continuation";
  
  public abstract void setTimeout(long paramLong);
  
  public abstract void suspend();
  
  public abstract void suspend(ServletResponse paramServletResponse);
  
  public abstract void resume();
  
  public abstract void complete();
  
  public abstract boolean isSuspended();
  
  public abstract boolean isResumed();
  
  public abstract boolean isExpired();
  
  public abstract boolean isInitial();
  
  public abstract boolean isResponseWrapped();
  
  public abstract ServletResponse getServletResponse();
  
  public abstract void addContinuationListener(ContinuationListener paramContinuationListener);
  
  public abstract void setAttribute(String paramString, Object paramObject);
  
  public abstract Object getAttribute(String paramString);
  
  public abstract void removeAttribute(String paramString);
  
  public abstract void undispatch()
    throws ContinuationThrowable;
}
