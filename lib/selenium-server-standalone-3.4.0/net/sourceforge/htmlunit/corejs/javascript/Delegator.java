package net.sourceforge.htmlunit.corejs.javascript;






















public class Delegator
  implements Function
{
  protected Scriptable obj = null;
  








  public Delegator() {}
  








  public Delegator(Scriptable obj)
  {
    this.obj = obj;
  }
  




  protected Delegator newInstance()
  {
    try
    {
      return (Delegator)getClass().newInstance();
    } catch (Exception ex) {
      throw Context.throwAsScriptRuntimeEx(ex);
    }
  }
  




  public Scriptable getDelegee()
  {
    return obj;
  }
  






  public void setDelegee(Scriptable obj)
  {
    this.obj = obj;
  }
  


  public String getClassName()
  {
    return getDelegee().getClassName();
  }
  



  public Object get(String name, Scriptable start)
  {
    return getDelegee().get(name, start);
  }
  



  public Object get(int index, Scriptable start)
  {
    return getDelegee().get(index, start);
  }
  



  public boolean has(String name, Scriptable start)
  {
    return getDelegee().has(name, start);
  }
  



  public boolean has(int index, Scriptable start)
  {
    return getDelegee().has(index, start);
  }
  



  public void put(String name, Scriptable start, Object value)
  {
    getDelegee().put(name, start, value);
  }
  



  public void put(int index, Scriptable start, Object value)
  {
    getDelegee().put(index, start, value);
  }
  


  public void delete(String name)
  {
    getDelegee().delete(name);
  }
  


  public void delete(int index)
  {
    getDelegee().delete(index);
  }
  


  public Scriptable getPrototype()
  {
    return getDelegee().getPrototype();
  }
  


  public void setPrototype(Scriptable prototype)
  {
    getDelegee().setPrototype(prototype);
  }
  


  public Scriptable getParentScope()
  {
    return getDelegee().getParentScope();
  }
  


  public void setParentScope(Scriptable parent)
  {
    getDelegee().setParentScope(parent);
  }
  


  public Object[] getIds()
  {
    return getDelegee().getIds();
  }
  












  public Object getDefaultValue(Class<?> hint)
  {
    return (hint == null) || (hint == ScriptRuntime.ScriptableClass) || (hint == ScriptRuntime.FunctionClass) ? this : 
    
      getDelegee().getDefaultValue(hint);
  }
  


  public boolean hasInstance(Scriptable instance)
  {
    return getDelegee().hasInstance(instance);
  }
  



  public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args)
  {
    return ((Function)getDelegee()).call(cx, scope, thisObj, args);
  }
  
















  public Scriptable construct(Context cx, Scriptable scope, Object[] args)
  {
    if (getDelegee() == null)
    {

      Delegator n = newInstance();
      Scriptable delegee;
      Scriptable delegee; if (args.length == 0) {
        delegee = new NativeObject();
      } else {
        delegee = ScriptRuntime.toObject(cx, scope, args[0]);
      }
      n.setDelegee(delegee);
      return n;
    }
    return ((Function)getDelegee()).construct(cx, scope, args);
  }
}
