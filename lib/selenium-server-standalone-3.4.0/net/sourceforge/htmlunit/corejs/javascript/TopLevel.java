package net.sourceforge.htmlunit.corejs.javascript;

import java.util.EnumMap;







































public class TopLevel
  extends IdScriptableObject
{
  static final long serialVersionUID = -4648046356662472260L;
  private EnumMap<Builtins, BaseFunction> ctors;
  private EnumMap<NativeErrors, BaseFunction> errors;
  public TopLevel() {}
  
  public static enum Builtins
  {
    Object, 
    
    Array, 
    
    Function, 
    
    String, 
    
    Number, 
    
    Boolean, 
    
    RegExp, 
    
    Error;
    

    private Builtins() {}
  }
  
  static enum NativeErrors
  {
    Error, 
    
    EvalError, 
    
    RangeError, 
    
    ReferenceError, 
    
    SyntaxError, 
    
    TypeError, 
    
    URIError, 
    
    InternalError, 
    
    JavaException;
    

    private NativeErrors() {}
  }
  
  public String getClassName()
  {
    return "global";
  }
  







  public void cacheBuiltins()
  {
    ctors = new EnumMap(Builtins.class);
    for (Builtins builtin : Builtins.values()) {
      Object value = ScriptableObject.getProperty(this, builtin.name());
      if ((value instanceof BaseFunction)) {
        ctors.put(builtin, (BaseFunction)value);
      }
    }
    errors = new EnumMap(NativeErrors.class);
    for (NativeErrors error : NativeErrors.values()) {
      Object value = ScriptableObject.getProperty(this, error.name());
      if ((value instanceof BaseFunction)) {
        errors.put(error, (BaseFunction)value);
      }
    }
  }
  















  public static Function getBuiltinCtor(Context cx, Scriptable scope, Builtins type)
  {
    assert (scope.getParentScope() == null);
    if ((scope instanceof TopLevel)) {
      Function result = ((TopLevel)scope).getBuiltinCtor(type);
      if (result != null) {
        return result;
      }
    }
    
    return ScriptRuntime.getExistingCtor(cx, scope, type.name());
  }
  















  static Function getNativeErrorCtor(Context cx, Scriptable scope, NativeErrors type)
  {
    assert (scope.getParentScope() == null);
    if ((scope instanceof TopLevel)) {
      Function result = ((TopLevel)scope).getNativeErrorCtor(type);
      if (result != null) {
        return result;
      }
    }
    
    return ScriptRuntime.getExistingCtor(cx, scope, type.name());
  }
  













  public static Scriptable getBuiltinPrototype(Scriptable scope, Builtins type)
  {
    assert (scope.getParentScope() == null);
    if ((scope instanceof TopLevel)) {
      Scriptable result = ((TopLevel)scope).getBuiltinPrototype(type);
      if (result != null) {
        return result;
      }
    }
    
    return ScriptableObject.getClassPrototype(scope, type.name());
  }
  








  public BaseFunction getBuiltinCtor(Builtins type)
  {
    return ctors != null ? (BaseFunction)ctors.get(type) : null;
  }
  








  BaseFunction getNativeErrorCtor(NativeErrors type)
  {
    return errors != null ? (BaseFunction)errors.get(type) : null;
  }
  








  public Scriptable getBuiltinPrototype(Builtins type)
  {
    BaseFunction func = getBuiltinCtor(type);
    Object proto = func != null ? func.getPrototypeProperty() : null;
    return (proto instanceof Scriptable) ? (Scriptable)proto : null;
  }
}
