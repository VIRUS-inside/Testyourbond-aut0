package net.sourceforge.htmlunit.corejs.javascript;


class SpecialRef
  extends Ref
{
  static final long serialVersionUID = -7521596632456797847L;
  
  private static final int SPECIAL_NONE = 0;
  
  private static final int SPECIAL_PROTO = 1;
  
  private static final int SPECIAL_PARENT = 2;
  
  private Scriptable target;
  private int type;
  private String name;
  
  private SpecialRef(Scriptable target, int type, String name)
  {
    this.target = target;
    this.type = type;
    this.name = name;
  }
  
  static Ref createSpecial(Context cx, Scriptable scope, Object object, String name)
  {
    Scriptable target = ScriptRuntime.toObjectOrNull(cx, object, scope);
    if (target == null) {
      throw ScriptRuntime.undefReadError(object, name);
    }
    
    int type;
    if (name.equals("__proto__")) {
      type = 1; } else { int type;
      if (name.equals("__parent__")) {
        type = 2;
      } else
        throw new IllegalArgumentException(name);
    }
    int type;
    if (!cx.hasFeature(5))
    {
      type = 0;
    }
    
    return new SpecialRef(target, type, name);
  }
  
  public Object get(Context cx)
  {
    switch (type) {
    case 0: 
      return ScriptRuntime.getObjectProp(target, name, cx);
    case 1: 
      return target.getPrototype();
    case 2: 
      return target.getParentScope();
    }
    throw Kit.codeBug();
  }
  

  @Deprecated
  public Object set(Context cx, Object value)
  {
    throw new IllegalStateException();
  }
  
  public Object set(Context cx, Scriptable scope, Object value)
  {
    switch (type) {
    case 0: 
      return ScriptRuntime.setObjectProp(target, name, value, cx);
    case 1: 
    case 2: 
      Scriptable obj = ScriptRuntime.toObjectOrNull(cx, value, scope);
      if (obj != null)
      {

        Scriptable search = obj;
        do {
          if (search == target) {
            throw Context.reportRuntimeError1("msg.cyclic.value", name);
          }
          
          if (type == 1) {
            search = search.getPrototype();
          } else {
            search = search.getParentScope();
          }
        } while (search != null);
      }
      if (type == 1) {
        target.setPrototype(obj);
      } else {
        target.setParentScope(obj);
      }
      return obj;
    }
    
    throw Kit.codeBug();
  }
  

  public boolean has(Context cx)
  {
    if (type == 0) {
      return ScriptRuntime.hasObjectElem(target, name, cx);
    }
    return true;
  }
  
  public boolean delete(Context cx)
  {
    if (type == 0) {
      return ScriptRuntime.deleteObjectElem(target, name, cx);
    }
    return false;
  }
}
