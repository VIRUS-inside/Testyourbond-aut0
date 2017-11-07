package net.sourceforge.htmlunit.corejs.javascript.tools.debugger;

import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

public abstract interface ScopeProvider
{
  public abstract Scriptable getScope();
}
