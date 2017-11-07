package net.sourceforge.htmlunit.corejs.javascript;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

































































public class FunctionObject
  extends BaseFunction
{
  static final long serialVersionUID = -5332312783643935019L;
  private static final short VARARGS_METHOD = -1;
  private static final short VARARGS_CTOR = -2;
  private static boolean sawSecurityException;
  public static final int JAVA_UNSUPPORTED_TYPE = 0;
  public static final int JAVA_STRING_TYPE = 1;
  public static final int JAVA_INT_TYPE = 2;
  public static final int JAVA_BOOLEAN_TYPE = 3;
  public static final int JAVA_DOUBLE_TYPE = 4;
  public static final int JAVA_SCRIPTABLE_TYPE = 5;
  public static final int JAVA_OBJECT_TYPE = 6;
  MemberBox member;
  private String functionName;
  private transient byte[] typeTags;
  private int parmsLength;
  private transient boolean hasVoidReturn;
  private transient int returnTypeTag;
  private boolean isStatic;
  
  public FunctionObject(String name, Member methodOrConstructor, Scriptable scope)
  {
    if ((methodOrConstructor instanceof Constructor)) {
      member = new MemberBox((Constructor)methodOrConstructor);
      isStatic = true;
    } else {
      member = new MemberBox((Method)methodOrConstructor);
      isStatic = member.isStatic();
    }
    String methodName = member.getName();
    functionName = name;
    Class<?>[] types = member.argTypes;
    int arity = types.length;
    if ((arity == 4) && ((types[1].isArray()) || (types[2].isArray())))
    {
      if (types[1].isArray()) {
        if ((!isStatic) || (types[0] != ScriptRuntime.ContextClass) || 
        
          (types[1].getComponentType() != ScriptRuntime.ObjectClass) || (types[2] != ScriptRuntime.FunctionClass) || (types[3] != Boolean.TYPE))
        {

          throw Context.reportRuntimeError1("msg.varargs.ctor", methodName);
        }
        
        parmsLength = -2;
      } else {
        if ((!isStatic) || (types[0] != ScriptRuntime.ContextClass) || (types[1] != ScriptRuntime.ScriptableClass) || 
        

          (types[2].getComponentType() != ScriptRuntime.ObjectClass) || (types[3] != ScriptRuntime.FunctionClass))
        {
          throw Context.reportRuntimeError1("msg.varargs.fun", methodName);
        }
        
        parmsLength = -1;
      }
    } else {
      parmsLength = arity;
      if (arity > 0) {
        typeTags = new byte[arity];
        for (int i = 0; i != arity; i++) {
          int tag = getTypeTag(types[i]);
          if (tag == 0) {
            throw Context.reportRuntimeError2("msg.bad.parms", types[i]
              .getName(), methodName);
          }
          typeTags[i] = ((byte)tag);
        }
      }
    }
    
    if (member.isMethod()) {
      Method method = member.method();
      Class<?> returnType = method.getReturnType();
      if (returnType == Void.TYPE) {
        hasVoidReturn = true;
      } else {
        returnTypeTag = getTypeTag(returnType);
      }
    } else {
      Class<?> ctorType = member.getDeclaringClass();
      if (!ScriptRuntime.ScriptableClass.isAssignableFrom(ctorType)) {
        throw Context.reportRuntimeError1("msg.bad.ctor.return", ctorType
          .getName());
      }
    }
    
    ScriptRuntime.setFunctionProtoAndParent(this, scope);
  }
  



  public static int getTypeTag(Class<?> type)
  {
    if (type == ScriptRuntime.StringClass)
      return 1;
    if ((type == ScriptRuntime.IntegerClass) || (type == Integer.TYPE))
      return 2;
    if ((type == ScriptRuntime.BooleanClass) || (type == Boolean.TYPE))
      return 3;
    if ((type == ScriptRuntime.DoubleClass) || (type == Double.TYPE))
      return 4;
    if (ScriptRuntime.ScriptableClass.isAssignableFrom(type))
      return 5;
    if (type == ScriptRuntime.ObjectClass) {
      return 6;
    }
    


    return 0;
  }
  
  public static Object convertArg(Context cx, Scriptable scope, Object arg, int typeTag)
  {
    switch (typeTag) {
    case 1: 
      if ((arg instanceof String))
        return arg;
      return ScriptRuntime.toString(arg);
    case 2: 
      if ((arg instanceof Integer))
        return arg;
      return Integer.valueOf(ScriptRuntime.toInt32(arg));
    case 3: 
      if ((arg instanceof Boolean))
        return arg;
      return ScriptRuntime.toBoolean(arg) ? Boolean.TRUE : Boolean.FALSE;
    case 4: 
      if ((arg instanceof Double))
        return arg;
      return new Double(ScriptRuntime.toNumber(arg));
    case 5: 
      return ScriptRuntime.toObjectOrNull(cx, arg, scope);
    case 6: 
      return arg;
    }
    throw new IllegalArgumentException();
  }
  






  public int getArity()
  {
    return parmsLength < 0 ? 1 : parmsLength;
  }
  



  public int getLength()
  {
    return getArity();
  }
  
  public String getFunctionName()
  {
    return functionName == null ? "" : functionName;
  }
  


  public Member getMethodOrConstructor()
  {
    if (member.isMethod()) {
      return member.method();
    }
    return member.ctor();
  }
  
  static Method findSingleMethod(Method[] methods, String name)
  {
    Method found = null;
    int i = 0; for (int N = methods.length; i != N; i++) {
      Method method = methods[i];
      if ((method != null) && (name.equals(method.getName()))) {
        if (found != null) {
          throw Context.reportRuntimeError2("msg.no.overload", name, method
            .getDeclaringClass().getName());
        }
        found = method;
      }
    }
    return found;
  }
  








  static Method[] getMethodList(Class<?> clazz)
  {
    Method[] methods = null;
    
    try
    {
      if (!sawSecurityException) {
        methods = clazz.getDeclaredMethods();
      }
    } catch (SecurityException e) {
      sawSecurityException = true;
    }
    if (methods == null) {
      methods = clazz.getMethods();
    }
    int count = 0;
    for (int i = 0; i < methods.length; i++) {
      if (sawSecurityException ? methods[i].getDeclaringClass() != clazz : 
        !Modifier.isPublic(methods[i].getModifiers())) {
        methods[i] = null;
      } else {
        count++;
      }
    }
    Method[] result = new Method[count];
    int j = 0;
    for (int i = 0; i < methods.length; i++) {
      if (methods[i] != null)
        result[(j++)] = methods[i];
    }
    return result;
  }
  
















  public void addAsConstructor(Scriptable scope, Scriptable prototype)
  {
    initAsConstructor(scope, prototype);
    defineProperty(scope, prototype.getClassName(), this, 2);
  }
  
  void initAsConstructor(Scriptable scope, Scriptable prototype)
  {
    ScriptRuntime.setFunctionProtoAndParent(this, scope);
    setImmunePrototypeProperty(prototype);
    
    prototype.setParentScope(this);
    
    defineProperty(prototype, "constructor", this, 7);
    
    setParentScope(scope);
  }
  





  @Deprecated
  public static Object convertArg(Context cx, Scriptable scope, Object arg, Class<?> desired)
  {
    int tag = getTypeTag(desired);
    if (tag == 0) {
      throw Context.reportRuntimeError1("msg.cant.convert", desired
        .getName());
    }
    return convertArg(cx, scope, arg, tag);
  }
  











  public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args)
  {
    boolean checkMethodResult = false;
    int argsLength = args.length;
    
    for (int i = 0; i < argsLength; i++)
    {
      if ((args[i] instanceof ConsString))
        args[i] = args[i].toString();
    }
    Object result;
    Object result;
    if (parmsLength < 0) {
      if (parmsLength == -1) {
        Object[] invokeArgs = { cx, thisObj, args, this };
        Object result = member.invoke(null, invokeArgs);
        checkMethodResult = true;
      } else {
        boolean inNewExpr = thisObj == null;
        Boolean b = inNewExpr ? Boolean.TRUE : Boolean.FALSE;
        Object[] invokeArgs = { cx, args, this, b };
        
        result = member.isCtor() ? member.newInstance(invokeArgs) : member.invoke(null, invokeArgs);
      }
    }
    else {
      if (!isStatic) {
        Class<?> clazz = member.getDeclaringClass();
        if ((thisObj instanceof Delegator)) {
          thisObj = ((Delegator)thisObj).getDelegee();
        }
        if (!clazz.isInstance(thisObj)) {
          boolean compatible = false;
          if (thisObj == scope) {
            Scriptable parentScope = getParentScope();
            if (scope != parentScope)
            {

              compatible = clazz.isInstance(parentScope);
              if (compatible) {
                thisObj = parentScope;
              }
            }
          }
          if (!compatible)
          {
            throw ScriptRuntime.typeError1("msg.incompat.call", functionName);
          }
        }
      }
      
      Object[] invokeArgs;
      
      if (parmsLength == argsLength)
      {

        Object[] invokeArgs = args;
        for (int i = 0; i != parmsLength; i++) {
          Object arg = args[i];
          Object converted = convertArg(cx, scope, arg, typeTags[i]);
          if (arg != converted) {
            if (invokeArgs == args) {
              invokeArgs = (Object[])args.clone();
            }
            invokeArgs[i] = converted;
          }
        } } else { Object[] invokeArgs;
        if (parmsLength == 0) {
          invokeArgs = ScriptRuntime.emptyArgs;
        } else {
          invokeArgs = new Object[parmsLength];
          for (int i = 0; i != parmsLength; i++) {
            Object arg = i < argsLength ? args[i] : Undefined.instance;
            
            invokeArgs[i] = convertArg(cx, scope, arg, typeTags[i]);
          }
        }
      }
      if (member.isMethod()) {
        Object result = member.invoke(thisObj, invokeArgs);
        checkMethodResult = true;
      } else {
        result = member.newInstance(invokeArgs);
      }
    }
    

    if (checkMethodResult) {
      if (hasVoidReturn) {
        result = Undefined.instance;
      } else if (returnTypeTag == 0) {
        result = cx.getWrapFactory().wrap(cx, scope, result, null);
      }
    }
    




    return result;
  }
  





  public Scriptable createObject(Context cx, Scriptable scope)
  {
    if ((member.isCtor()) || (parmsLength == -2)) {
      return null;
    }
    try
    {
      result = (Scriptable)member.getDeclaringClass().newInstance();
    } catch (Exception ex) { Scriptable result;
      throw Context.throwAsScriptRuntimeEx(ex);
    }
    Scriptable result;
    result.setPrototype(getClassPrototype());
    result.setParentScope(getParentScope());
    return result;
  }
  
  boolean isVarArgsMethod() {
    return parmsLength == -1;
  }
  
  boolean isVarArgsConstructor() {
    return parmsLength == -2;
  }
  
  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    in.defaultReadObject();
    if (parmsLength > 0) {
      Class<?>[] types = member.argTypes;
      typeTags = new byte[parmsLength];
      for (int i = 0; i != parmsLength; i++) {
        typeTags[i] = ((byte)getTypeTag(types[i]));
      }
    }
    if (member.isMethod()) {
      Method method = member.method();
      Class<?> returnType = method.getReturnType();
      if (returnType == Void.TYPE) {
        hasVoidReturn = true;
      } else {
        returnTypeTag = getTypeTag(returnType);
      }
    }
  }
}
