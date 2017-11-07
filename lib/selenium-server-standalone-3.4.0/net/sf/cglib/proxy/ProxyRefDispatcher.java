package net.sf.cglib.proxy;

public abstract interface ProxyRefDispatcher
  extends Callback
{
  public abstract Object loadObject(Object paramObject)
    throws Exception;
}
