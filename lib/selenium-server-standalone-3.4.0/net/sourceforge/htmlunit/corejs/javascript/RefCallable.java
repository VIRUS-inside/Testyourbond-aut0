package net.sourceforge.htmlunit.corejs.javascript;

public abstract interface RefCallable
  extends Callable
{
  public abstract Ref refCall(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject);
}
