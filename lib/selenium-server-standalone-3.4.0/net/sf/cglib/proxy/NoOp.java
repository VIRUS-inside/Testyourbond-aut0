package net.sf.cglib.proxy;






















public abstract interface NoOp
  extends Callback
{
  public static final NoOp INSTANCE = new NoOp() {};
}
