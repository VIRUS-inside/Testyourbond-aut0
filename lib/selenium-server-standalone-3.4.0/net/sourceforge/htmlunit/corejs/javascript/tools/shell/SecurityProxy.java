package net.sourceforge.htmlunit.corejs.javascript.tools.shell;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.SecurityController;

public abstract class SecurityProxy
  extends SecurityController
{
  public SecurityProxy() {}
  
  protected abstract void callProcessFileSecure(Context paramContext, Scriptable paramScriptable, String paramString);
}
