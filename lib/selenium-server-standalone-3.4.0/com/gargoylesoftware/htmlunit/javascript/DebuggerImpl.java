package com.gargoylesoftware.htmlunit.javascript;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.debug.DebugFrame;
import net.sourceforge.htmlunit.corejs.javascript.debug.DebuggableScript;








































public class DebuggerImpl
  extends DebuggerAdapter
{
  public DebuggerImpl() {}
  
  public DebugFrame getFrame(Context cx, DebuggableScript functionOrScript)
  {
    return new DebugFrameImpl(functionOrScript);
  }
}
