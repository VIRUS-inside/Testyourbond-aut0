package com.gargoylesoftware.htmlunit.javascript;

import java.lang.reflect.Method;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.FunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;



























public class HtmlUnitScriptable
  extends ScriptableObject
{
  private String className_;
  
  public HtmlUnitScriptable() {}
  
  public String getClassName()
  {
    if (className_ != null) {
      return className_;
    }
    if (getPrototype() != null) {
      return getPrototype().getClassName();
    }
    String className = getClass().getSimpleName();
    if (className.isEmpty())
    {
      className = getClass().getSuperclass().getSimpleName();
    }
    return className;
  }
  





  public void setClassName(String className)
  {
    className_ = className;
  }
  




  public void defineProperty(String propertyName, Class<?> clazz, int attributes)
  {
    int length = propertyName.length();
    if (length == 0) {
      throw new IllegalArgumentException();
    }
    char[] buf = new char[3 + length];
    propertyName.getChars(0, length, buf, 3);
    buf[3] = Character.toUpperCase(buf[3]);
    buf[0] = 'g';
    buf[1] = 'e';
    buf[2] = 't';
    String getterName = new String(buf);
    buf[0] = 's';
    String setterName = new String(buf);
    
    Method[] methods = clazz.getMethods();
    Method getter = findMethod(methods, getterName);
    Method setter = findMethod(methods, setterName);
    if (setter == null) {
      attributes |= 0x1;
    }
    defineProperty(propertyName, null, getter, setter, attributes);
  }
  




  public void defineFunctionProperties(String[] names, Class<?> clazz, int attributes)
  {
    Method[] methods = clazz.getMethods();
    for (String name : names) {
      Method method = findMethod(methods, name);
      if (method == null) {
        throw Context.reportRuntimeError("Method \"" + name + "\" not found in \"" + clazz.getName() + '"');
      }
      FunctionObject f = new FunctionObject(name, method, this);
      defineProperty(name, f, attributes);
    }
  }
  


  private static Method findMethod(Method[] methods, String name)
  {
    Method[] arrayOfMethod = methods;int j = methods.length; for (int i = 0; i < j; i++) { Method m = arrayOfMethod[i];
      if (m.getName().equals(name)) {
        return m;
      }
    }
    return null;
  }
  



  public void setParentScope(Scriptable m)
  {
    if (m == this) {
      throw new IllegalArgumentException("Object can't be its own parentScope");
    }
    super.setParentScope(m);
  }
  



  public Object getDefaultValue(Class<?> typeHint)
  {
    return "[object " + getClassName() + "]";
  }
  


  public void put(String name, Scriptable start, Object value)
  {
    try
    {
      super.put(name, start, value);
    }
    catch (IllegalArgumentException e)
    {
      throw Context.reportRuntimeError("'set " + 
        name + "' called on an object that does not implement interface " + getClassName());
    }
  }
}
