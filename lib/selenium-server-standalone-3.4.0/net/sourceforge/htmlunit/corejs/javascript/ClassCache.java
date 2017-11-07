package net.sourceforge.htmlunit.corejs.javascript;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;













public class ClassCache
  implements Serializable
{
  private static final long serialVersionUID = -8866246036237312215L;
  private static final Object AKEY = "ClassCache";
  private volatile boolean cachingIsEnabled = true;
  

  private transient Map<Class<?>, JavaMembers> classTable;
  

  private transient Map<JavaAdapter.JavaAdapterSignature, Class<?>> classAdapterCache;
  

  private transient Map<Class<?>, Object> interfaceAdapterCache;
  
  private int generatedClassSerial;
  
  private Scriptable associatedScope;
  

  public ClassCache() {}
  

  public static ClassCache get(Scriptable scope)
  {
    ClassCache cache = (ClassCache)ScriptableObject.getTopScopeValue(scope, AKEY);
    
    if (cache == null) {
      throw new RuntimeException("Can't find top level scope for ClassCache.get");
    }
    
    return cache;
  }
  











  public boolean associate(ScriptableObject topScope)
  {
    if (topScope.getParentScope() != null)
    {
      throw new IllegalArgumentException();
    }
    if (this == topScope.associateValue(AKEY, this)) {
      associatedScope = topScope;
      return true;
    }
    return false;
  }
  


  public synchronized void clearCaches()
  {
    classTable = null;
    classAdapterCache = null;
    interfaceAdapterCache = null;
  }
  



  public final boolean isCachingEnabled()
  {
    return cachingIsEnabled;
  }
  


















  public synchronized void setCachingEnabled(boolean enabled)
  {
    if (enabled == cachingIsEnabled)
      return;
    if (!enabled)
      clearCaches();
    cachingIsEnabled = enabled;
  }
  


  Map<Class<?>, JavaMembers> getClassCacheMap()
  {
    if (classTable == null)
    {


      classTable = new ConcurrentHashMap(16, 0.75F, 1);
    }
    
    return classTable;
  }
  
  Map<JavaAdapter.JavaAdapterSignature, Class<?>> getInterfaceAdapterCacheMap() {
    if (classAdapterCache == null) {
      classAdapterCache = new ConcurrentHashMap(16, 0.75F, 1);
    }
    
    return classAdapterCache;
  }
  



  @Deprecated
  public boolean isInvokerOptimizationEnabled()
  {
    return false;
  }
  






  @Deprecated
  public synchronized void setInvokerOptimizationEnabled(boolean enabled) {}
  






  public final synchronized int newClassSerialNumber()
  {
    return ++generatedClassSerial;
  }
  
  Object getInterfaceAdapter(Class<?> cl) {
    return interfaceAdapterCache == null ? null : interfaceAdapterCache
      .get(cl);
  }
  
  synchronized void cacheInterfaceAdapter(Class<?> cl, Object iadapter) {
    if (cachingIsEnabled) {
      if (interfaceAdapterCache == null) {
        interfaceAdapterCache = new ConcurrentHashMap(16, 0.75F, 1);
      }
      
      interfaceAdapterCache.put(cl, iadapter);
    }
  }
  
  Scriptable getAssociatedScope() {
    return associatedScope;
  }
}
