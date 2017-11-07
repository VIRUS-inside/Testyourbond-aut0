package net.sourceforge.htmlunit.corejs.javascript;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;










final class MemberBox
  implements Serializable
{
  static final long serialVersionUID = 6358550398665688245L;
  private transient Member memberObject;
  transient Class<?>[] argTypes;
  transient Object delegateTo;
  transient boolean vararg;
  transient Function asFunction;
  
  Function asFunction(final String name, Scriptable scope, Scriptable prototype)
  {
    if (asFunction == null) {
      asFunction = new BaseFunction(scope, prototype)
      {
        public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] originalArgs)
        {
          MemberBox nativeGetter = MemberBox.this;
          Object[] args;
          Object getterThis;
          Object[] args; if (delegateTo == null) {
            Object getterThis = thisObj;
            args = ScriptRuntime.emptyArgs;
          } else {
            getterThis = delegateTo;
            args = new Object[] { thisObj };
          }
          return nativeGetter.invoke(getterThis, args);
        }
        
        public String getFunctionName()
        {
          return name;
        }
      };
    }
    return asFunction;
  }
  
  MemberBox(Method method) {
    init(method);
  }
  
  MemberBox(Constructor<?> constructor) {
    init(constructor);
  }
  
  private void init(Method method) {
    memberObject = method;
    argTypes = method.getParameterTypes();
    vararg = VMBridge.instance.isVarArgs(method);
  }
  
  private void init(Constructor<?> constructor) {
    memberObject = constructor;
    argTypes = constructor.getParameterTypes();
    vararg = VMBridge.instance.isVarArgs(constructor);
  }
  
  Method method() {
    return (Method)memberObject;
  }
  
  Constructor<?> ctor() {
    return (Constructor)memberObject;
  }
  
  Member member() {
    return memberObject;
  }
  
  boolean isMethod() {
    return memberObject instanceof Method;
  }
  
  boolean isCtor() {
    return memberObject instanceof Constructor;
  }
  
  boolean isStatic() {
    return Modifier.isStatic(memberObject.getModifiers());
  }
  
  String getName() {
    return memberObject.getName();
  }
  
  Class<?> getDeclaringClass() {
    return memberObject.getDeclaringClass();
  }
  
  String toJavaDeclaration() {
    StringBuilder sb = new StringBuilder();
    if (isMethod()) {
      Method method = method();
      sb.append(method.getReturnType());
      sb.append(' ');
      sb.append(method.getName());
    } else {
      Constructor<?> ctor = ctor();
      String name = ctor.getDeclaringClass().getName();
      int lastDot = name.lastIndexOf('.');
      if (lastDot >= 0) {
        name = name.substring(lastDot + 1);
      }
      sb.append(name);
    }
    sb.append(JavaMembers.liveConnectSignature(argTypes));
    return sb.toString();
  }
  
  public String toString()
  {
    return memberObject.toString();
  }
  
  Object invoke(Object target, Object[] args) {
    Method method = method();
    

    if ((target instanceof Delegator)) {
      target = ((Delegator)target).getDelegee();
    }
    for (int i = 0; i < args.length; i++) {
      if ((args[i] instanceof Delegator)) {
        args[i] = ((Delegator)args[i]).getDelegee();
      }
    }
    
    try
    {
      return method.invoke(target, args);
    } catch (IllegalAccessException ex) {
      Method accessible = searchAccessibleMethod(method, argTypes);
      if (accessible != null) {
        memberObject = accessible;
        method = accessible;
      }
      else if (!VMBridge.instance.tryToMakeAccessible(method)) {
        throw Context.throwAsScriptRuntimeEx(ex);
      }
      

      return method.invoke(target, args);
    }
    catch (InvocationTargetException ite)
    {
      Throwable e = ite;
      do {
        e = ((InvocationTargetException)e).getTargetException();
      } while ((e instanceof InvocationTargetException));
      if ((e instanceof ContinuationPending)) {
        throw ((ContinuationPending)e);
      }
      if (((e instanceof RhinoException)) || 
        (Context.getCurrentContext().hasFeature(101)))
      {
        throw Context.throwAsScriptRuntimeEx(e);
      }
      
      throw new RuntimeException("Exception invoking " + method.getName(), e);
    } catch (IllegalArgumentException iae) {
      StringBuilder builder = new StringBuilder();
      for (Object arg : args)
      {
        String type = arg == null ? "null" : arg.getClass().getSimpleName();
        if (builder.length() != 0) {
          builder.append(", ");
        }
        builder.append(type);
      }
      

      throw new IllegalArgumentException("Exception invoking " + method.getDeclaringClass().getSimpleName() + '.' + method.getName() + "() with arguments [" + builder + "]", iae);
    }
    catch (Exception ex) {
      throw Context.throwAsScriptRuntimeEx(ex);
    }
  }
  
  Object newInstance(Object[] args) {
    Constructor<?> ctor = ctor();
    try
    {
      return ctor.newInstance(args);
    } catch (IllegalAccessException ex) {
      if (!VMBridge.instance.tryToMakeAccessible(ctor)) {
        throw Context.throwAsScriptRuntimeEx(ex);
      }
      
      return ctor.newInstance(args);
    } catch (Exception ex) {
      throw Context.throwAsScriptRuntimeEx(ex);
    }
  }
  
  private static Method searchAccessibleMethod(Method method, Class<?>[] params)
  {
    int modifiers = method.getModifiers();
    if ((Modifier.isPublic(modifiers)) && (!Modifier.isStatic(modifiers))) {
      Class<?> c = method.getDeclaringClass();
      if (!Modifier.isPublic(c.getModifiers())) {
        String name = method.getName();
        Class<?>[] intfs = c.getInterfaces();
        int i = 0; for (int N = intfs.length; i != N; i++) {
          Class<?> intf = intfs[i];
          if (Modifier.isPublic(intf.getModifiers())) {
            try {
              return intf.getMethod(name, params);
            }
            catch (NoSuchMethodException localNoSuchMethodException) {}catch (SecurityException localSecurityException) {}
          }
        }
        for (;;)
        {
          c = c.getSuperclass();
          if (c == null) {
            break;
          }
          if (Modifier.isPublic(c.getModifiers())) {
            try {
              Method m = c.getMethod(name, params);
              int mModifiers = m.getModifiers();
              if ((Modifier.isPublic(mModifiers)) && 
                (!Modifier.isStatic(mModifiers))) {
                return m;
              }
            }
            catch (NoSuchMethodException localNoSuchMethodException1) {}catch (SecurityException localSecurityException1) {}
          }
        }
      }
    }
    
    return null;
  }
  
  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    in.defaultReadObject();
    Member member = readMember(in);
    if ((member instanceof Method)) {
      init((Method)member);
    } else {
      init((Constructor)member);
    }
  }
  
  private void writeObject(ObjectOutputStream out) throws IOException {
    out.defaultWriteObject();
    writeMember(out, memberObject);
  }
  






  private static void writeMember(ObjectOutputStream out, Member member)
    throws IOException
  {
    if (member == null) {
      out.writeBoolean(false);
      return;
    }
    out.writeBoolean(true);
    if ((!(member instanceof Method)) && (!(member instanceof Constructor)))
      throw new IllegalArgumentException("not Method or Constructor");
    out.writeBoolean(member instanceof Method);
    out.writeObject(member.getName());
    out.writeObject(member.getDeclaringClass());
    if ((member instanceof Method)) {
      writeParameters(out, ((Method)member).getParameterTypes());
    } else {
      writeParameters(out, ((Constructor)member).getParameterTypes());
    }
  }
  


  private static Member readMember(ObjectInputStream in)
    throws IOException, ClassNotFoundException
  {
    if (!in.readBoolean())
      return null;
    boolean isMethod = in.readBoolean();
    String name = (String)in.readObject();
    Class<?> declaring = (Class)in.readObject();
    Class<?>[] parms = readParameters(in);
    try {
      if (isMethod) {
        return declaring.getMethod(name, parms);
      }
      return declaring.getConstructor(parms);
    }
    catch (NoSuchMethodException e) {
      throw new IOException("Cannot find member: " + e);
    }
  }
  
  private static final Class<?>[] primitives = { Boolean.TYPE, Byte.TYPE, Character.TYPE, Double.TYPE, Float.TYPE, Integer.TYPE, Long.TYPE, Short.TYPE, Void.TYPE };
  







  private static void writeParameters(ObjectOutputStream out, Class<?>[] parms)
    throws IOException
  {
    out.writeShort(parms.length);
    label115: for (int i = 0; i < parms.length; i++) {
      Class<?> parm = parms[i];
      boolean primitive = parm.isPrimitive();
      out.writeBoolean(primitive);
      if (!primitive) {
        out.writeObject(parm);
      }
      else {
        for (int j = 0; j < primitives.length; j++) {
          if (parm.equals(primitives[j])) {
            out.writeByte(j);
            break label115;
          }
        }
        throw new IllegalArgumentException("Primitive " + parm + " not found");
      }
    }
  }
  


  private static Class<?>[] readParameters(ObjectInputStream in)
    throws IOException, ClassNotFoundException
  {
    Class<?>[] result = new Class[in.readShort()];
    for (int i = 0; i < result.length; i++) {
      if (!in.readBoolean()) {
        result[i] = ((Class)in.readObject());
      }
      else
        result[i] = primitives[in.readByte()];
    }
    return result;
  }
}
