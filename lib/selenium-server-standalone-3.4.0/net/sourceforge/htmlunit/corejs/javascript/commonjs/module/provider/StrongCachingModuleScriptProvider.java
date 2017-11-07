package net.sourceforge.htmlunit.corejs.javascript.commonjs.module.provider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.sourceforge.htmlunit.corejs.javascript.commonjs.module.ModuleScript;
















public class StrongCachingModuleScriptProvider
  extends CachingModuleScriptProviderBase
{
  private static final long serialVersionUID = 1L;
  private final Map<String, CachingModuleScriptProviderBase.CachedModuleScript> modules = new ConcurrentHashMap(16, 0.75F, 
    getConcurrencyLevel());
  






  public StrongCachingModuleScriptProvider(ModuleSourceProvider moduleSourceProvider)
  {
    super(moduleSourceProvider);
  }
  
  protected CachingModuleScriptProviderBase.CachedModuleScript getLoadedModule(String moduleId)
  {
    return (CachingModuleScriptProviderBase.CachedModuleScript)modules.get(moduleId);
  }
  

  protected void putLoadedModule(String moduleId, ModuleScript moduleScript, Object validator)
  {
    modules.put(moduleId, new CachingModuleScriptProviderBase.CachedModuleScript(moduleScript, validator));
  }
}
