package net.sourceforge.htmlunit.corejs.javascript.debug;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

public abstract interface DebugFrame
{
  public abstract void onEnter(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject);
  
  public abstract void onLineChange(Context paramContext, int paramInt);
  
  public abstract void onExceptionThrown(Context paramContext, Throwable paramThrowable);
  
  public abstract void onExit(Context paramContext, boolean paramBoolean, Object paramObject);
  
  public abstract void onDebuggerStatement(Context paramContext);
}
