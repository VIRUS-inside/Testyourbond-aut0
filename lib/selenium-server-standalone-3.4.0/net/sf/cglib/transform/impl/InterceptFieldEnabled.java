package net.sf.cglib.transform.impl;

public abstract interface InterceptFieldEnabled
{
  public abstract void setInterceptFieldCallback(InterceptFieldCallback paramInterceptFieldCallback);
  
  public abstract InterceptFieldCallback getInterceptFieldCallback();
}
