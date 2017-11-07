package com.gargoylesoftware.htmlunit.javascript;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.debug.DebugFrame;
import net.sourceforge.htmlunit.corejs.javascript.debug.DebuggableScript;
import net.sourceforge.htmlunit.corejs.javascript.debug.Debugger;























public class DebuggerAdapter
  implements Debugger
{
  public DebuggerAdapter() {}
  
  public void handleCompilationDone(Context cx, DebuggableScript functionOrScript, String source) {}
  
  public DebugFrame getFrame(Context cx, DebuggableScript fnOrScript)
  {
    return new DebugFrameAdapter();
  }
}
