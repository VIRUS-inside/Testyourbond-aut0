package net.sourceforge.htmlunit.corejs.javascript.commonjs.module;

import java.io.Serializable;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Script;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

















public class RequireBuilder
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private boolean sandboxed = true;
  

  private ModuleScriptProvider moduleScriptProvider;
  

  private Script preExec;
  
  private Script postExec;
  

  public RequireBuilder() {}
  

  public RequireBuilder setModuleScriptProvider(ModuleScriptProvider moduleScriptProvider)
  {
    this.moduleScriptProvider = moduleScriptProvider;
    return this;
  }
  







  public RequireBuilder setPostExec(Script postExec)
  {
    this.postExec = postExec;
    return this;
  }
  







  public RequireBuilder setPreExec(Script preExec)
  {
    this.preExec = preExec;
    return this;
  }
  








  public RequireBuilder setSandboxed(boolean sandboxed)
  {
    this.sandboxed = sandboxed;
    return this;
  }
  











  public Require createRequire(Context cx, Scriptable globalScope)
  {
    return new Require(cx, globalScope, moduleScriptProvider, preExec, postExec, sandboxed);
  }
}
