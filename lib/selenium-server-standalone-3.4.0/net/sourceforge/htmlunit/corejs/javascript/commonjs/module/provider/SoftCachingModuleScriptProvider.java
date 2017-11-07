package net.sourceforge.htmlunit.corejs.javascript.commonjs.module.provider;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Script;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.commonjs.module.ModuleScript;
















public class SoftCachingModuleScriptProvider
  extends CachingModuleScriptProviderBase
{
  private static final long serialVersionUID = 1L;
  private transient ReferenceQueue<Script> scriptRefQueue = new ReferenceQueue();
  
  private transient ConcurrentMap<String, ScriptReference> scripts = new ConcurrentHashMap(16, 0.75F, 
    getConcurrencyLevel());
  






  public SoftCachingModuleScriptProvider(ModuleSourceProvider moduleSourceProvider)
  {
    super(moduleSourceProvider);
  }
  

  public ModuleScript getModuleScript(Context cx, String moduleId, URI uri, URI base, Scriptable paths)
    throws Exception
  {
    for (;;)
    {
      ScriptReference ref = (ScriptReference)scriptRefQueue.poll();
      if (ref == null) {
        break;
      }
      scripts.remove(ref.getModuleId(), ref);
    }
    return super.getModuleScript(cx, moduleId, uri, base, paths);
  }
  
  protected CachingModuleScriptProviderBase.CachedModuleScript getLoadedModule(String moduleId)
  {
    ScriptReference scriptRef = (ScriptReference)scripts.get(moduleId);
    return scriptRef != null ? scriptRef.getCachedModuleScript() : null;
  }
  

  protected void putLoadedModule(String moduleId, ModuleScript moduleScript, Object validator)
  {
    scripts.put(moduleId, new ScriptReference(moduleScript
      .getScript(), moduleId, moduleScript
      .getUri(), moduleScript.getBase(), validator, scriptRefQueue));
  }
  
  private static class ScriptReference extends SoftReference<Script>
  {
    private final String moduleId;
    private final URI uri;
    private final URI base;
    private final Object validator;
    
    ScriptReference(Script script, String moduleId, URI uri, URI base, Object validator, ReferenceQueue<Script> refQueue)
    {
      super(refQueue);
      this.moduleId = moduleId;
      this.uri = uri;
      this.base = base;
      this.validator = validator;
    }
    
    CachingModuleScriptProviderBase.CachedModuleScript getCachedModuleScript() {
      Script script = (Script)get();
      if (script == null) {
        return null;
      }
      return new CachingModuleScriptProviderBase.CachedModuleScript(new ModuleScript(script, uri, base), validator);
    }
    
    String getModuleId()
    {
      return moduleId;
    }
  }
  
  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    scriptRefQueue = new ReferenceQueue();
    scripts = new ConcurrentHashMap();
    
    Map<String, CachingModuleScriptProviderBase.CachedModuleScript> serScripts = (Map)in.readObject();
    for (Map.Entry<String, CachingModuleScriptProviderBase.CachedModuleScript> entry : serScripts
      .entrySet()) {
      CachingModuleScriptProviderBase.CachedModuleScript cachedModuleScript = (CachingModuleScriptProviderBase.CachedModuleScript)entry.getValue();
      putLoadedModule((String)entry.getKey(), cachedModuleScript.getModule(), cachedModuleScript
        .getValidator());
    }
  }
  
  private void writeObject(ObjectOutputStream out) throws IOException {
    Map<String, CachingModuleScriptProviderBase.CachedModuleScript> serScripts = new HashMap();
    for (Map.Entry<String, ScriptReference> entry : scripts.entrySet())
    {
      CachingModuleScriptProviderBase.CachedModuleScript cachedModuleScript = ((ScriptReference)entry.getValue()).getCachedModuleScript();
      if (cachedModuleScript != null) {
        serScripts.put(entry.getKey(), cachedModuleScript);
      }
    }
    out.writeObject(serScripts);
  }
}
