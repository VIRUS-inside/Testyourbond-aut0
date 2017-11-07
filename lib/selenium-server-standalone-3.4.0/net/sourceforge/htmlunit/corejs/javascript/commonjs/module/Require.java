package net.sourceforge.htmlunit.corejs.javascript.commonjs.module;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.sourceforge.htmlunit.corejs.javascript.BaseFunction;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Script;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;























public class Require
  extends BaseFunction
{
  private static final long serialVersionUID = 1L;
  private final ModuleScriptProvider moduleScriptProvider;
  private final Scriptable nativeScope;
  private final Scriptable paths;
  private final boolean sandboxed;
  private final Script preExec;
  private final Script postExec;
  private String mainModuleId = null;
  
  private Scriptable mainExports;
  
  private final Map<String, Scriptable> exportedModuleInterfaces = new ConcurrentHashMap();
  private final Object loadLock = new Object();
  

  private static final ThreadLocal<Map<String, Scriptable>> loadingModuleInterfaces = new ThreadLocal();
  

























  public Require(Context cx, Scriptable nativeScope, ModuleScriptProvider moduleScriptProvider, Script preExec, Script postExec, boolean sandboxed)
  {
    this.moduleScriptProvider = moduleScriptProvider;
    this.nativeScope = nativeScope;
    this.sandboxed = sandboxed;
    this.preExec = preExec;
    this.postExec = postExec;
    setPrototype(ScriptableObject.getFunctionPrototype(nativeScope));
    if (!sandboxed) {
      paths = cx.newArray(nativeScope, 0);
      defineReadOnlyProperty(this, "paths", paths);
    } else {
      paths = null;
    }
  }
  




















  public Scriptable requireMain(Context cx, String mainModuleId)
  {
    if (this.mainModuleId != null) {
      if (!this.mainModuleId.equals(mainModuleId)) {
        throw new IllegalStateException("Main module already set to " + this.mainModuleId);
      }
      
      return mainExports;
    }
    

    try
    {
      moduleScript = moduleScriptProvider.getModuleScript(cx, mainModuleId, null, null, paths);
    } catch (RuntimeException x) {
      ModuleScript moduleScript;
      throw x;
    } catch (Exception x) {
      throw new RuntimeException(x);
    }
    ModuleScript moduleScript;
    if (moduleScript != null) {
      mainExports = getExportedModuleInterface(cx, mainModuleId, null, null, true);
    }
    else if (!sandboxed)
    {
      URI mainUri = null;
      
      try
      {
        mainUri = new URI(mainModuleId);
      }
      catch (URISyntaxException localURISyntaxException) {}
      


      if ((mainUri == null) || (!mainUri.isAbsolute())) {
        File file = new File(mainModuleId);
        if (!file.isFile()) {
          throw ScriptRuntime.throwError(cx, nativeScope, "Module \"" + mainModuleId + "\" not found.");
        }
        
        mainUri = file.toURI();
      }
      mainExports = getExportedModuleInterface(cx, mainUri.toString(), mainUri, null, true);
    }
    

    this.mainModuleId = mainModuleId;
    return mainExports;
  }
  






  public void install(Scriptable scope)
  {
    ScriptableObject.putProperty(scope, "require", this);
  }
  

  public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args)
  {
    if ((args == null) || (args.length < 1)) {
      throw ScriptRuntime.throwError(cx, scope, "require() needs one argument");
    }
    

    String id = (String)Context.jsToJava(args[0], String.class);
    URI uri = null;
    URI base = null;
    if ((id.startsWith("./")) || (id.startsWith("../"))) {
      if (!(thisObj instanceof ModuleScope)) {
        throw ScriptRuntime.throwError(cx, scope, "Can't resolve relative module ID \"" + id + "\" when require() is used outside of a module");
      }
      


      ModuleScope moduleScope = (ModuleScope)thisObj;
      base = moduleScope.getBase();
      URI current = moduleScope.getUri();
      uri = current.resolve(id);
      
      if (base == null)
      {

        id = uri.toString();
      }
      else {
        id = base.relativize(current).resolve(id).toString();
        if (id.charAt(0) == '.')
        {

          if (sandboxed) {
            throw ScriptRuntime.throwError(cx, scope, "Module \"" + id + "\" is not contained in sandbox.");
          }
          
          id = uri.toString();
        }
      }
    }
    
    return getExportedModuleInterface(cx, id, uri, base, false);
  }
  
  public Scriptable construct(Context cx, Scriptable scope, Object[] args)
  {
    throw ScriptRuntime.throwError(cx, scope, "require() can not be invoked as a constructor");
  }
  


  private Scriptable getExportedModuleInterface(Context cx, String id, URI uri, URI base, boolean isMain)
  {
    Scriptable exports = (Scriptable)exportedModuleInterfaces.get(id);
    if (exports != null) {
      if (isMain) {
        throw new IllegalStateException("Attempt to set main module after it was loaded");
      }
      
      return exports;
    }
    


    Map<String, Scriptable> threadLoadingModules = (Map)loadingModuleInterfaces.get();
    if (threadLoadingModules != null) {
      exports = (Scriptable)threadLoadingModules.get(id);
      if (exports != null) {
        return exports;
      }
    }
    







    synchronized (loadLock)
    {

      exports = (Scriptable)exportedModuleInterfaces.get(id);
      if (exports != null) {
        return exports;
      }
      
      ModuleScript moduleScript = getModule(cx, id, uri, base);
      if ((sandboxed) && (!moduleScript.isSandboxed())) {
        throw ScriptRuntime.throwError(cx, nativeScope, "Module \"" + id + "\" is not contained in sandbox.");
      }
      
      exports = cx.newObject(nativeScope);
      
      boolean outermostLocked = threadLoadingModules == null;
      if (outermostLocked) {
        threadLoadingModules = new HashMap();
        loadingModuleInterfaces.set(threadLoadingModules);
      }
      







      threadLoadingModules.put(id, exports);
      
      try
      {
        Scriptable newExports = executeModuleScript(cx, id, exports, moduleScript, isMain);
        
        if (exports != newExports) {
          threadLoadingModules.put(id, newExports);
          exports = newExports;
        }
      }
      catch (RuntimeException e) {
        threadLoadingModules.remove(id);
        throw e;
      } finally {
        if (outermostLocked)
        {





          exportedModuleInterfaces.putAll(threadLoadingModules);
          loadingModuleInterfaces.set(null);
        }
      }
    }
    return exports;
  }
  

  private Scriptable executeModuleScript(Context cx, String id, Scriptable exports, ModuleScript moduleScript, boolean isMain)
  {
    ScriptableObject moduleObject = (ScriptableObject)cx.newObject(nativeScope);
    URI uri = moduleScript.getUri();
    URI base = moduleScript.getBase();
    defineReadOnlyProperty(moduleObject, "id", id);
    if (!sandboxed) {
      defineReadOnlyProperty(moduleObject, "uri", uri.toString());
    }
    Scriptable executionScope = new ModuleScope(nativeScope, uri, base);
    




    executionScope.put("exports", executionScope, exports);
    executionScope.put("module", executionScope, moduleObject);
    moduleObject.put("exports", moduleObject, exports);
    install(executionScope);
    if (isMain) {
      defineReadOnlyProperty(this, "main", moduleObject);
    }
    executeOptionalScript(preExec, cx, executionScope);
    moduleScript.getScript().exec(cx, executionScope);
    executeOptionalScript(postExec, cx, executionScope);
    return ScriptRuntime.toObject(cx, nativeScope, 
      ScriptableObject.getProperty(moduleObject, "exports"));
  }
  
  private static void executeOptionalScript(Script script, Context cx, Scriptable executionScope)
  {
    if (script != null) {
      script.exec(cx, executionScope);
    }
  }
  
  private static void defineReadOnlyProperty(ScriptableObject obj, String name, Object value)
  {
    ScriptableObject.putProperty(obj, name, value);
    obj.setAttributes(name, 5);
  }
  
  private ModuleScript getModule(Context cx, String id, URI uri, URI base)
  {
    try
    {
      ModuleScript moduleScript = moduleScriptProvider.getModuleScript(cx, id, uri, base, paths);
      if (moduleScript == null) {
        throw ScriptRuntime.throwError(cx, nativeScope, "Module \"" + id + "\" not found.");
      }
      
      return moduleScript;
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw Context.throwAsScriptRuntimeEx(e);
    }
  }
  
  public String getFunctionName()
  {
    return "require";
  }
  
  public int getArity()
  {
    return 1;
  }
  
  public int getLength()
  {
    return 1;
  }
}
