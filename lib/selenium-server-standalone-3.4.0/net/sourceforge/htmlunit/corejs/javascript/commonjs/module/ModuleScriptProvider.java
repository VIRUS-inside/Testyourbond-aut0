package net.sourceforge.htmlunit.corejs.javascript.commonjs.module;

import java.net.URI;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

public abstract interface ModuleScriptProvider
{
  public abstract ModuleScript getModuleScript(Context paramContext, String paramString, URI paramURI1, URI paramURI2, Scriptable paramScriptable)
    throws Exception;
}
