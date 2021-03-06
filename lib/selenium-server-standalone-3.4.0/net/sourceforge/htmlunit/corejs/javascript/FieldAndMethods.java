package net.sourceforge.htmlunit.corejs.javascript;

import java.lang.reflect.Field;











































































































































































































































































































































































































































































































































































































































































































































































































































































class FieldAndMethods
  extends NativeJavaMethod
{
  static final long serialVersionUID = -9222428244284796755L;
  Field field;
  Object javaObject;
  
  FieldAndMethods(Scriptable scope, MemberBox[] methods, Field field)
  {
    super(methods);
    this.field = field;
    setParentScope(scope);
    setPrototype(ScriptableObject.getFunctionPrototype(scope));
  }
  
  public Object getDefaultValue(Class<?> hint)
  {
    if (hint == ScriptRuntime.FunctionClass) {
      return this;
    }
    try
    {
      Object rval = field.get(javaObject);
      type = field.getType();
    } catch (IllegalAccessException accEx) { Class<?> type;
      throw Context.reportRuntimeError1("msg.java.internal.private", field
        .getName()); }
    Class<?> type;
    Context cx = Context.getContext();
    Object rval = cx.getWrapFactory().wrap(cx, this, rval, type);
    if ((rval instanceof Scriptable)) {
      rval = ((Scriptable)rval).getDefaultValue(hint);
    }
    return rval;
  }
}
