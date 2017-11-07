package net.sourceforge.htmlunit.corejs.javascript.tools.debugger;

import net.sourceforge.htmlunit.corejs.javascript.debug.DebuggableScript;

public abstract interface SourceProvider
{
  public abstract String getSource(DebuggableScript paramDebuggableScript);
}
