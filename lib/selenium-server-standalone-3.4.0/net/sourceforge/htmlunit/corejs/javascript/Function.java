package net.sourceforge.htmlunit.corejs.javascript;

public abstract interface Function
  extends Scriptable, Callable
{
  public abstract Object call(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject);
  
  public abstract Scriptable construct(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject);
}
