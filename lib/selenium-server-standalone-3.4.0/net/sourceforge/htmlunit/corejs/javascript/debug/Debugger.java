package net.sourceforge.htmlunit.corejs.javascript.debug;

import net.sourceforge.htmlunit.corejs.javascript.Context;

public abstract interface Debugger
{
  public abstract void handleCompilationDone(Context paramContext, DebuggableScript paramDebuggableScript, String paramString);
  
  public abstract DebugFrame getFrame(Context paramContext, DebuggableScript paramDebuggableScript);
}
