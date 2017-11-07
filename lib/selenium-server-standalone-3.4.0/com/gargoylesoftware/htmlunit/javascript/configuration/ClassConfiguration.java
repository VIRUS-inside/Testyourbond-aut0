package com.gargoylesoftware.htmlunit.javascript.configuration;

import com.gargoylesoftware.htmlunit.javascript.HtmlUnitScriptable;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import net.sourceforge.htmlunit.corejs.javascript.Context;
























public final class ClassConfiguration
{
  private Map<String, PropertyInfo> propertyMap_ = new HashMap();
  private Map<String, Method> functionMap_ = new HashMap();
  private Map<String, PropertyInfo> staticPropertyMap_ = new HashMap();
  private Map<String, Method> staticFunctionMap_ = new HashMap();
  private List<ConstantInfo> constants_ = new ArrayList();
  

  private String extendedClassName_;
  

  private final Class<? extends HtmlUnitScriptable> hostClass_;
  

  private final String hostClassSimpleName_;
  

  private Member jsConstructor_;
  
  private final Class<?>[] domClasses_;
  
  private final boolean jsObject_;
  
  private final String className_;
  

  public ClassConfiguration(Class<? extends HtmlUnitScriptable> hostClass, Class<?>[] domClasses, boolean jsObject, String className)
  {
    Class<?> superClass = hostClass.getSuperclass();
    if (superClass != SimpleScriptable.class) {
      extendedClassName_ = superClass.getSimpleName();
    }
    else {
      extendedClassName_ = "";
    }
    hostClass_ = hostClass;
    hostClassSimpleName_ = hostClass_.getSimpleName();
    jsObject_ = jsObject;
    domClasses_ = domClasses;
    if (className == null) {
      className_ = getHostClass().getSimpleName();
    }
    else {
      className_ = className;
    }
  }
  
  void setJSConstructor(Member jsConstructor) {
    if (jsConstructor_ != null) {
      throw new IllegalStateException("Can not have two constructors for " + 
        jsConstructor_.getDeclaringClass().getName());
    }
    jsConstructor_ = jsConstructor;
  }
  





  public void addProperty(String name, Method getter, Method setter)
  {
    PropertyInfo info = new PropertyInfo(getter, setter);
    propertyMap_.put(name, info);
  }
  





  public void addStaticProperty(String name, Method getter, Method setter)
  {
    PropertyInfo info = new PropertyInfo(getter, setter);
    staticPropertyMap_.put(name, info);
  }
  


  public void addConstant(String name)
  {
    try
    {
      Object value = getHostClass().getField(name).get(null);
      int flag = 5;
      
      if (getClassName().endsWith("Array")) {
        flag |= 0x2;
      }
      constants_.add(new ConstantInfo(name, value, flag));
    }
    catch (Exception e) {
      throw Context.reportRuntimeError("Cannot get field '" + name + "' for type: " + 
        getHostClass().getName());
    }
  }
  



  public Map<String, PropertyInfo> getPropertyMap()
  {
    return propertyMap_;
  }
  



  public Set<Map.Entry<String, PropertyInfo>> getStaticPropertyEntries()
  {
    return staticPropertyMap_.entrySet();
  }
  



  public Set<Map.Entry<String, Method>> getFunctionEntries()
  {
    return functionMap_.entrySet();
  }
  



  public Set<Map.Entry<String, Method>> getStaticFunctionEntries()
  {
    return staticFunctionMap_.entrySet();
  }
  



  public Set<String> getFunctionKeys()
  {
    return functionMap_.keySet();
  }
  



  public List<ConstantInfo> getConstants()
  {
    return constants_;
  }
  




  public void addFunction(String name, Method method)
  {
    functionMap_.put(name, method);
  }
  




  public void addStaticFunction(String name, Method method)
  {
    staticFunctionMap_.put(name, method);
  }
  


  public String getExtendedClassName()
  {
    return extendedClassName_;
  }
  



  public Class<? extends HtmlUnitScriptable> getHostClass()
  {
    return hostClass_;
  }
  


  public String getHostClassSimpleName()
  {
    return hostClassSimpleName_;
  }
  



  public Member getJsConstructor()
  {
    return jsConstructor_;
  }
  




  public Class<?>[] getDomClasses()
  {
    return domClasses_;
  }
  


  public boolean isJsObject()
  {
    return jsObject_;
  }
  



  public String getClassName()
  {
    return className_;
  }
  



  public static class PropertyInfo
  {
    private final Method readMethod_;
    


    private final Method writeMethod_;
    


    public PropertyInfo(Method readMethod, Method writeMethod)
    {
      readMethod_ = readMethod;
      writeMethod_ = writeMethod;
    }
    


    public Method getReadMethod()
    {
      return readMethod_;
    }
    


    public Method getWriteMethod()
    {
      return writeMethod_;
    }
  }
  


  public static class ConstantInfo
  {
    private final String name_;
    

    private final Object value_;
    

    private final int flag_;
    


    public ConstantInfo(String name, Object value, int flag)
    {
      name_ = name;
      value_ = value;
      flag_ = flag;
    }
    


    public String getName()
    {
      return name_;
    }
    


    public Object getValue()
    {
      return value_;
    }
    


    public int getFlag()
    {
      return flag_;
    }
  }
}
