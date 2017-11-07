package net.sf.cglib.proxy;

public abstract interface Dispatcher
  extends Callback
{
  public abstract Object loadObject()
    throws Exception;
}
