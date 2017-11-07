package net.sf.cglib.proxy;

public abstract interface FixedValue
  extends Callback
{
  public abstract Object loadObject()
    throws Exception;
}
