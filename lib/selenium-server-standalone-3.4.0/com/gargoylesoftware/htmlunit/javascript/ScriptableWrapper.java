package com.gargoylesoftware.htmlunit.javascript;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.FunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import org.apache.commons.lang3.ArrayUtils;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

























public class ScriptableWrapper
  extends ScriptableObject
{
  private static final Class<?>[] METHOD_PARAMS_INT = { Integer.TYPE };
  private static final Class<?>[] METHOD_PARAMS_STRING = { String.class };
  
  private final Map<String, Method> properties_ = new HashMap();
  

  private Method getByIndexMethod_;
  

  private final Object javaObject_;
  
  private final String jsClassName_;
  
  private Method getByNameFallback_;
  

  public ScriptableWrapper(Scriptable scope, Object javaObject, Class<?> staticType)
  {
    javaObject_ = javaObject;
    setParentScope(scope);
    


    if ((NodeList.class.equals(staticType)) || 
      (NamedNodeMap.class.equals(staticType))) {
      try {
        jsClassName_ = staticType.getSimpleName();
        





        Method length = javaObject.getClass().getMethod("getLength", ArrayUtils.EMPTY_CLASS_ARRAY);
        properties_.put("length", length);
        
        Method item = javaObject.getClass().getMethod("item", METHOD_PARAMS_INT);
        defineProperty("item", new MethodWrapper("item", staticType, METHOD_PARAMS_INT), 0);
        
        Method toString = getClass().getMethod("jsToString", ArrayUtils.EMPTY_CLASS_ARRAY);
        defineProperty("toString", new FunctionObject("toString", toString, this), 0);
        
        getByIndexMethod_ = item;
        
        if (!NamedNodeMap.class.equals(staticType)) return;
        Method getNamedItem = javaObject.getClass().getMethod("getNamedItem", METHOD_PARAMS_STRING);
        defineProperty("getNamedItem", 
          new MethodWrapper("getNamedItem", staticType, METHOD_PARAMS_STRING), 0);
        
        getByNameFallback_ = getNamedItem;
      }
      catch (Exception e)
      {
        throw new RuntimeException("Method not found", e);
      }
      
    } else {
      throw new RuntimeException("Unknown type: " + staticType.getName());
    }
  }
  




  public Object get(String name, Scriptable start)
  {
    Method propertyGetter = (Method)properties_.get(name);
    Object response;
    Object response; if (propertyGetter != null) {
      response = invoke(propertyGetter);
    }
    else {
      Object fromSuper = super.get(name, start);
      Object response; if (fromSuper != Scriptable.NOT_FOUND) {
        response = fromSuper;
      }
      else {
        Object byName = invoke(getByNameFallback_, new Object[] { name });
        Object response; if (byName != null) {
          response = byName;
        }
        else {
          response = Scriptable.NOT_FOUND;
        }
      }
    }
    
    return Context.javaToJS(response, 
      ScriptableObject.getTopLevelScope(start));
  }
  




  public boolean has(String name, Scriptable start)
  {
    return (properties_.containsKey(name)) || (super.has(name, start));
  }
  




  protected Object invoke(Method method)
  {
    return invoke(method, ArrayUtils.EMPTY_OBJECT_ARRAY);
  }
  




  protected Object invoke(Method method, Object[] args)
  {
    try
    {
      return method.invoke(javaObject_, args);
    }
    catch (Exception e) {
      throw new RuntimeException(
        "Invocation of method on java object failed", e);
    }
  }
  




  public Object get(int index, Scriptable start)
  {
    if (getByIndexMethod_ != null) {
      Object byIndex = invoke(getByIndexMethod_, new Object[] { Integer.valueOf(index) });
      return Context.javaToJS(byIndex, ScriptableObject.getTopLevelScope(start));
    }
    return super.get(index, start);
  }
  




  public Object getDefaultValue(Class<?> hint)
  {
    if ((String.class.equals(hint)) || (hint == null)) {
      return jsToString();
    }
    return super.getDefaultValue(hint);
  }
  



  public String jsToString()
  {
    return "[object " + getClassName() + "]";
  }
  




  public String getClassName()
  {
    return jsClassName_;
  }
  



  public Object getWrappedObject()
  {
    return javaObject_;
  }
}
