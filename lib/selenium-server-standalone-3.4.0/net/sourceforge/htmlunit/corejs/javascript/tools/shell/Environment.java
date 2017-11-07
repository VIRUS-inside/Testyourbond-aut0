package net.sourceforge.htmlunit.corejs.javascript.tools.shell;

import java.util.Map;
import java.util.Properties;
import java.util.Set;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;



















public class Environment
  extends ScriptableObject
{
  static final long serialVersionUID = -430727378460177065L;
  private Environment thePrototypeInstance = null;
  
  public static void defineClass(ScriptableObject scope) {
    try {
      ScriptableObject.defineClass(scope, Environment.class);
    } catch (Exception e) {
      throw new Error(e.getMessage());
    }
  }
  
  public String getClassName()
  {
    return "Environment";
  }
  
  public Environment() {
    if (thePrototypeInstance == null)
      thePrototypeInstance = this;
  }
  
  public Environment(ScriptableObject scope) {
    setParentScope(scope);
    Object ctor = ScriptRuntime.getTopLevelProp(scope, "Environment");
    if ((ctor != null) && ((ctor instanceof Scriptable))) {
      Scriptable s = (Scriptable)ctor;
      setPrototype((Scriptable)s.get("prototype", s));
    }
  }
  
  public boolean has(String name, Scriptable start)
  {
    if (this == thePrototypeInstance) {
      return super.has(name, start);
    }
    return System.getProperty(name) != null;
  }
  
  public Object get(String name, Scriptable start)
  {
    if (this == thePrototypeInstance) {
      return super.get(name, start);
    }
    String result = System.getProperty(name);
    if (result != null) {
      return ScriptRuntime.toObject(getParentScope(), result);
    }
    return Scriptable.NOT_FOUND;
  }
  
  public void put(String name, Scriptable start, Object value)
  {
    if (this == thePrototypeInstance) {
      super.put(name, start, value);
    } else
      System.getProperties().put(name, ScriptRuntime.toString(value));
  }
  
  private Object[] collectIds() {
    Map<Object, Object> props = System.getProperties();
    return props.keySet().toArray();
  }
  
  public Object[] getIds()
  {
    if (this == thePrototypeInstance)
      return super.getIds();
    return collectIds();
  }
  
  public Object[] getAllIds()
  {
    if (this == thePrototypeInstance)
      return super.getAllIds();
    return collectIds();
  }
}
