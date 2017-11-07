package net.sourceforge.htmlunit.corejs.javascript.commonjs.module.provider;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.commonjs.module.ModuleScript;
import net.sourceforge.htmlunit.corejs.javascript.commonjs.module.ModuleScriptProvider;


















public class MultiModuleScriptProvider
  implements ModuleScriptProvider
{
  private final ModuleScriptProvider[] providers;
  
  public MultiModuleScriptProvider(Iterable<? extends ModuleScriptProvider> providers)
  {
    List<ModuleScriptProvider> l = new LinkedList();
    for (ModuleScriptProvider provider : providers) {
      l.add(provider);
    }
    this.providers = ((ModuleScriptProvider[])l.toArray(new ModuleScriptProvider[l.size()]));
  }
  
  public ModuleScript getModuleScript(Context cx, String moduleId, URI uri, URI base, Scriptable paths) throws Exception
  {
    for (ModuleScriptProvider provider : providers) {
      ModuleScript script = provider.getModuleScript(cx, moduleId, uri, base, paths);
      
      if (script != null) {
        return script;
      }
    }
    return null;
  }
}
