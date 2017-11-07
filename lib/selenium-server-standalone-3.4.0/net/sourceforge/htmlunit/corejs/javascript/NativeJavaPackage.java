package net.sourceforge.htmlunit.corejs.javascript;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashSet;
import java.util.Set;















public class NativeJavaPackage
  extends ScriptableObject
{
  static final long serialVersionUID = 7445054382212031523L;
  private String packageName;
  private transient ClassLoader classLoader;
  
  NativeJavaPackage(boolean internalUsage, String packageName, ClassLoader classLoader)
  {
    this.packageName = packageName;
    this.classLoader = classLoader;
  }
  



  @Deprecated
  public NativeJavaPackage(String packageName, ClassLoader classLoader)
  {
    this(false, packageName, classLoader);
  }
  



  @Deprecated
  public NativeJavaPackage(String packageName)
  {
    this(false, packageName, 
      Context.getCurrentContext().getApplicationClassLoader());
  }
  
  public String getClassName()
  {
    return "JavaPackage";
  }
  
  public boolean has(String id, Scriptable start)
  {
    return true;
  }
  
  public boolean has(int index, Scriptable start)
  {
    return false;
  }
  


  public void put(String id, Scriptable start, Object value) {}
  

  public void put(int index, Scriptable start, Object value)
  {
    throw Context.reportRuntimeError0("msg.pkg.int");
  }
  
  public Object get(String id, Scriptable start)
  {
    return getPkgProperty(id, start, true);
  }
  
  public Object get(int index, Scriptable start)
  {
    return NOT_FOUND;
  }
  

  NativeJavaPackage forcePackage(String name, Scriptable scope)
  {
    Object cached = super.get(name, this);
    if ((cached != null) && ((cached instanceof NativeJavaPackage))) {
      return (NativeJavaPackage)cached;
    }
    String newPackage = packageName + "." + name;
    
    NativeJavaPackage pkg = new NativeJavaPackage(true, newPackage, classLoader);
    
    ScriptRuntime.setObjectProtoAndParent(pkg, scope);
    super.put(name, this, pkg);
    return pkg;
  }
  

  synchronized Object getPkgProperty(String name, Scriptable start, boolean createPkg)
  {
    Object cached = super.get(name, start);
    if (cached != NOT_FOUND)
      return cached;
    if ((negativeCache != null) && (negativeCache.contains(name)))
    {
      return null;
    }
    
    String className = packageName + '.' + name;
    
    Context cx = Context.getContext();
    ClassShutter shutter = cx.getClassShutter();
    Scriptable newValue = null;
    if ((shutter == null) || (shutter.visibleToScripts(className))) {
      Class<?> cl = null;
      if (classLoader != null) {
        cl = Kit.classOrNull(classLoader, className);
      } else {
        cl = Kit.classOrNull(className);
      }
      if (cl != null) {
        WrapFactory wrapFactory = cx.getWrapFactory();
        newValue = wrapFactory.wrapJavaClass(cx, getTopLevelScope(this), cl);
        
        newValue.setPrototype(getPrototype());
      }
    }
    if (newValue == null) {
      if (createPkg)
      {
        NativeJavaPackage pkg = new NativeJavaPackage(true, className, classLoader);
        ScriptRuntime.setObjectProtoAndParent(pkg, getParentScope());
        newValue = pkg;
      }
      else {
        if (negativeCache == null)
          negativeCache = new HashSet();
        negativeCache.add(name);
      }
    }
    if (newValue != null)
    {

      super.put(name, start, newValue);
    }
    return newValue;
  }
  
  public Object getDefaultValue(Class<?> ignored)
  {
    return toString();
  }
  
  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    in.defaultReadObject();
    
    classLoader = Context.getCurrentContext().getApplicationClassLoader();
  }
  
  public String toString()
  {
    return "[JavaPackage " + packageName + "]";
  }
  
  public boolean equals(Object obj)
  {
    if ((obj instanceof NativeJavaPackage)) {
      NativeJavaPackage njp = (NativeJavaPackage)obj;
      return (packageName.equals(packageName)) && (classLoader == classLoader);
    }
    
    return false;
  }
  
  public int hashCode()
  {
    return 
      packageName.hashCode() ^ (classLoader == null ? 0 : classLoader.hashCode());
  }
  


  private Set<String> negativeCache = null;
}
