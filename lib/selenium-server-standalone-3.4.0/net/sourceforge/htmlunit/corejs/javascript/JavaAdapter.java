package net.sourceforge.htmlunit.corejs.javascript;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import net.sourceforge.htmlunit.corejs.classfile.ClassFileWriter;

public final class JavaAdapter implements IdFunctionCall
{
  public JavaAdapter() {}
  
  static class JavaAdapterSignature
  {
    Class<?> superClass;
    Class<?>[] interfaces;
    ObjToIntMap names;
    
    JavaAdapterSignature(Class<?> superClass, Class<?>[] interfaces, ObjToIntMap names)
    {
      this.superClass = superClass;
      this.interfaces = interfaces;
      this.names = names;
    }
    
    public boolean equals(Object obj)
    {
      if (!(obj instanceof JavaAdapterSignature))
        return false;
      JavaAdapterSignature sig = (JavaAdapterSignature)obj;
      if (superClass != superClass)
        return false;
      if (interfaces != interfaces) {
        if (interfaces.length != interfaces.length)
          return false;
        for (int i = 0; i < interfaces.length; i++)
          if (interfaces[i] != interfaces[i])
            return false;
      }
      if (names.size() != names.size())
        return false;
      ObjToIntMap.Iterator iter = new ObjToIntMap.Iterator(names);
      for (iter.start(); !iter.done(); iter.next()) {
        String name = (String)iter.getKey();
        int arity = iter.getValue();
        if (arity != names.get(name, arity + 1))
          return false;
      }
      return true;
    }
    
    public int hashCode()
    {
      return 
        superClass.hashCode() + java.util.Arrays.hashCode(interfaces) ^ names.size();
    }
  }
  
  public static void init(Context cx, Scriptable scope, boolean sealed) {
    JavaAdapter obj = new JavaAdapter();
    IdFunctionObject ctor = new IdFunctionObject(obj, FTAG, 1, "JavaAdapter", 1, scope);
    
    ctor.markAsConstructor(null);
    if (sealed) {
      ctor.sealObject();
    }
    ctor.exportAsScopeProperty();
  }
  
  public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args)
  {
    if ((f.hasTag(FTAG)) && 
      (f.methodId() == 1)) {
      return js_createAdapter(cx, scope, args);
    }
    
    throw f.unknown();
  }
  
  public static Object convertResult(Object result, Class<?> c) {
    if ((result == Undefined.instance) && (c != ScriptRuntime.ObjectClass) && (c != ScriptRuntime.StringClass))
    {

      return null;
    }
    return Context.jsToJava(result, c);
  }
  
  public static Scriptable createAdapterWrapper(Scriptable obj, Object adapter)
  {
    Scriptable scope = ScriptableObject.getTopLevelScope(obj);
    NativeJavaObject res = new NativeJavaObject(scope, adapter, null, true);
    res.setPrototype(obj);
    return res;
  }
  
  public static Object getAdapterSelf(Class<?> adapterClass, Object adapter) throws NoSuchFieldException, IllegalAccessException
  {
    Field self = adapterClass.getDeclaredField("self");
    return self.get(adapter);
  }
  
  static Object js_createAdapter(Context cx, Scriptable scope, Object[] args)
  {
    int N = args.length;
    if (N == 0) {
      throw ScriptRuntime.typeError0("msg.adapter.zero.args");
    }
    








    for (int classCount = 0; classCount < N - 1; classCount++) {
      Object arg = args[classCount];
      



      if ((arg instanceof NativeObject)) {
        break;
      }
      if (!(arg instanceof NativeJavaClass)) {
        throw ScriptRuntime.typeError2("msg.not.java.class.arg", 
          String.valueOf(classCount), 
          ScriptRuntime.toString(arg));
      }
    }
    Class<?> superClass = null;
    Class<?>[] intfs = new Class[classCount];
    int interfaceCount = 0;
    for (int i = 0; i < classCount; i++) {
      Class<?> c = ((NativeJavaClass)args[i]).getClassObject();
      if (!c.isInterface()) {
        if (superClass != null) {
          throw ScriptRuntime.typeError2("msg.only.one.super", superClass
            .getName(), c.getName());
        }
        superClass = c;
      } else {
        intfs[(interfaceCount++)] = c;
      }
    }
    
    if (superClass == null) {
      superClass = ScriptRuntime.ObjectClass;
    }
    
    Class<?>[] interfaces = new Class[interfaceCount];
    System.arraycopy(intfs, 0, interfaces, 0, interfaceCount);
    
    Scriptable obj = ScriptableObject.ensureScriptable(args[classCount]);
    
    Class<?> adapterClass = getAdapterClass(scope, superClass, interfaces, obj);
    


    int argsCount = N - classCount - 1;
    try { Object adapter;
      Object adapter; if (argsCount > 0)
      {


        Object[] ctorArgs = new Object[argsCount + 2];
        ctorArgs[0] = obj;
        ctorArgs[1] = cx.getFactory();
        System.arraycopy(args, classCount + 1, ctorArgs, 2, argsCount);
        
        NativeJavaClass classWrapper = new NativeJavaClass(scope, adapterClass, true);
        
        NativeJavaMethod ctors = members.ctors;
        int index = ctors.findCachedFunction(cx, ctorArgs);
        if (index < 0) {
          String sig = NativeJavaMethod.scriptSignature(args);
          throw Context.reportRuntimeError2("msg.no.java.ctor", adapterClass
            .getName(), sig);
        }
        

        adapter = NativeJavaClass.constructInternal(ctorArgs, methods[index]);
      }
      else {
        Class<?>[] ctorParms = { ScriptRuntime.ScriptableClass, ScriptRuntime.ContextFactoryClass };
        
        Object[] ctorArgs = { obj, cx.getFactory() };
        
        adapter = adapterClass.getConstructor(ctorParms).newInstance(ctorArgs);
      }
      
      Object self = getAdapterSelf(adapterClass, adapter);
      
      if ((self instanceof Wrapper)) {
        Object unwrapped = ((Wrapper)self).unwrap();
        if ((unwrapped instanceof Scriptable)) {
          if ((unwrapped instanceof ScriptableObject)) {
            ScriptRuntime.setObjectProtoAndParent((ScriptableObject)unwrapped, scope);
          }
          
          return unwrapped;
        }
      }
      return self;
    } catch (Exception ex) {
      throw Context.throwAsScriptRuntimeEx(ex);
    }
  }
  
  public static void writeAdapterObject(Object javaObject, ObjectOutputStream out)
    throws java.io.IOException
  {
    Class<?> cl = javaObject.getClass();
    out.writeObject(cl.getSuperclass().getName());
    
    Class<?>[] interfaces = cl.getInterfaces();
    String[] interfaceNames = new String[interfaces.length];
    
    for (int i = 0; i < interfaces.length; i++) {
      interfaceNames[i] = interfaces[i].getName();
    }
    out.writeObject(interfaceNames);
    try
    {
      Object delegee = cl.getField("delegee").get(javaObject);
      out.writeObject(delegee);
      return;
    }
    catch (IllegalAccessException localIllegalAccessException) {}catch (NoSuchFieldException localNoSuchFieldException) {}
    
    throw new java.io.IOException();
  }
  

  public static Object readAdapterObject(Scriptable self, ObjectInputStream in)
    throws java.io.IOException, ClassNotFoundException
  {
    Context cx = Context.getCurrentContext();
    ContextFactory factory; ContextFactory factory; if (cx != null) {
      factory = cx.getFactory();
    } else {
      factory = null;
    }
    
    Class<?> superClass = Class.forName((String)in.readObject());
    
    String[] interfaceNames = (String[])in.readObject();
    Class<?>[] interfaces = new Class[interfaceNames.length];
    
    for (int i = 0; i < interfaceNames.length; i++) {
      interfaces[i] = Class.forName(interfaceNames[i]);
    }
    Scriptable delegee = (Scriptable)in.readObject();
    
    Class<?> adapterClass = getAdapterClass(self, superClass, interfaces, delegee);
    

    Class<?>[] ctorParms = { ScriptRuntime.ContextFactoryClass, ScriptRuntime.ScriptableClass, ScriptRuntime.ScriptableClass };
    
    Object[] ctorArgs = { factory, delegee, self };
    try {
      return adapterClass.getConstructor(ctorParms).newInstance(ctorArgs);
    }
    catch (InstantiationException localInstantiationException) {}catch (IllegalAccessException localIllegalAccessException) {}catch (java.lang.reflect.InvocationTargetException localInvocationTargetException) {}catch (NoSuchMethodException localNoSuchMethodException) {}
    



    throw new ClassNotFoundException("adapter");
  }
  
  private static ObjToIntMap getObjectFunctionNames(Scriptable obj) {
    Object[] ids = ScriptableObject.getPropertyIds(obj);
    ObjToIntMap map = new ObjToIntMap(ids.length);
    for (int i = 0; i != ids.length; i++)
      if ((ids[i] instanceof String))
      {
        String id = (String)ids[i];
        Object value = ScriptableObject.getProperty(obj, id);
        if ((value instanceof Function)) {
          Function f = (Function)value;
          
          int length = ScriptRuntime.toInt32(ScriptableObject.getProperty(f, "length"));
          if (length < 0) {
            length = 0;
          }
          map.put(id, length);
        }
      }
    return map;
  }
  
  private static Class<?> getAdapterClass(Scriptable scope, Class<?> superClass, Class<?>[] interfaces, Scriptable obj)
  {
    ClassCache cache = ClassCache.get(scope);
    
    Map<JavaAdapterSignature, Class<?>> generated = cache.getInterfaceAdapterCacheMap();
    
    ObjToIntMap names = getObjectFunctionNames(obj);
    
    JavaAdapterSignature sig = new JavaAdapterSignature(superClass, interfaces, names);
    Class<?> adapterClass = (Class)generated.get(sig);
    if (adapterClass == null) {
      String adapterName = "adapter" + cache.newClassSerialNumber();
      byte[] code = createAdapterCode(names, adapterName, superClass, interfaces, null);
      

      adapterClass = loadAdapterClass(adapterName, code);
      if (cache.isCachingEnabled()) {
        generated.put(sig, adapterClass);
      }
    }
    return adapterClass;
  }
  


  public static byte[] createAdapterCode(ObjToIntMap functionNames, String adapterName, Class<?> superClass, Class<?>[] interfaces, String scriptClassName)
  {
    ClassFileWriter cfw = new ClassFileWriter(adapterName, superClass.getName(), "<adapter>");
    cfw.addField("factory", "Lnet/sourceforge/htmlunit/corejs/javascript/ContextFactory;", (short)17);
    


    cfw.addField("delegee", "Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;", (short)17);
    


    cfw.addField("self", "Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;", (short)17);
    


    int interfacesCount = interfaces == null ? 0 : interfaces.length;
    for (int i = 0; i < interfacesCount; i++) {
      if (interfaces[i] != null) {
        cfw.addInterface(interfaces[i].getName());
      }
    }
    String superName = superClass.getName().replace('.', '/');
    Constructor<?>[] ctors = superClass.getDeclaredConstructors();
    for (Constructor<?> ctor : ctors) {
      int mod = ctor.getModifiers();
      if ((Modifier.isPublic(mod)) || (Modifier.isProtected(mod))) {
        generateCtor(cfw, adapterName, superName, ctor);
      }
    }
    generateSerialCtor(cfw, adapterName, superName);
    if (scriptClassName != null) {
      generateEmptyCtor(cfw, adapterName, superName, scriptClassName);
    }
    
    ObjToIntMap generatedOverrides = new ObjToIntMap();
    ObjToIntMap generatedMethods = new ObjToIntMap();
    

    for (int i = 0; i < interfacesCount; i++) {
      Method[] methods = interfaces[i].getMethods();
      for (int j = 0; j < methods.length; j++) {
        Method method = methods[j];
        int mods = method.getModifiers();
        if ((!Modifier.isStatic(mods)) && (!Modifier.isFinal(mods)))
        {

          String methodName = method.getName();
          Class<?>[] argTypes = method.getParameterTypes();
          if (!functionNames.has(methodName)) {
            try {
              superClass.getMethod(methodName, argTypes);


            }
            catch (NoSuchMethodException localNoSuchMethodException) {}


          }
          else
          {

            String methodSignature = getMethodSignature(method, argTypes);
            String methodKey = methodName + methodSignature;
            if (!generatedOverrides.has(methodKey)) {
              generateMethod(cfw, adapterName, methodName, argTypes, method
                .getReturnType(), true);
              generatedOverrides.put(methodKey, 0);
              generatedMethods.put(methodName, 0);
            }
          }
        }
      }
    }
    


    Method[] methods = getOverridableMethods(superClass);
    for (int j = 0; j < methods.length; j++) {
      Method method = methods[j];
      int mods = method.getModifiers();
      


      boolean isAbstractMethod = Modifier.isAbstract(mods);
      String methodName = method.getName();
      if ((isAbstractMethod) || (functionNames.has(methodName)))
      {

        Class<?>[] argTypes = method.getParameterTypes();
        String methodSignature = getMethodSignature(method, argTypes);
        String methodKey = methodName + methodSignature;
        if (!generatedOverrides.has(methodKey)) {
          generateMethod(cfw, adapterName, methodName, argTypes, method
            .getReturnType(), true);
          generatedOverrides.put(methodKey, 0);
          generatedMethods.put(methodName, 0);
          


          if (!isAbstractMethod) {
            generateSuper(cfw, adapterName, superName, methodName, methodSignature, argTypes, method
            
              .getReturnType());
          }
        }
      }
    }
    


    ObjToIntMap.Iterator iter = new ObjToIntMap.Iterator(functionNames);
    for (iter.start(); !iter.done(); iter.next()) {
      String functionName = (String)iter.getKey();
      if (!generatedMethods.has(functionName))
      {
        int length = iter.getValue();
        Class<?>[] parms = new Class[length];
        for (int k = 0; k < length; k++)
          parms[k] = ScriptRuntime.ObjectClass;
        generateMethod(cfw, adapterName, functionName, parms, ScriptRuntime.ObjectClass, false);
      }
    }
    return cfw.toByteArray();
  }
  
  static Method[] getOverridableMethods(Class<?> clazz) {
    ArrayList<Method> list = new ArrayList();
    HashSet<String> skip = new HashSet();
    



    for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
      appendOverridableMethods(c, list, skip);
    }
    for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
      for (Class<?> intf : c.getInterfaces())
        appendOverridableMethods(intf, list, skip);
    }
    return (Method[])list.toArray(new Method[list.size()]);
  }
  
  private static void appendOverridableMethods(Class<?> c, ArrayList<Method> list, HashSet<String> skip)
  {
    Method[] methods = c.getDeclaredMethods();
    for (int i = 0; i < methods.length; i++) {
      String methodKey = methods[i].getName() + getMethodSignature(methods[i], methods[i]
        .getParameterTypes());
      if (!skip.contains(methodKey))
      {
        int mods = methods[i].getModifiers();
        if (!Modifier.isStatic(mods))
        {
          if (Modifier.isFinal(mods))
          {

            skip.add(methodKey);

          }
          else if ((Modifier.isPublic(mods)) || (Modifier.isProtected(mods))) {
            list.add(methods[i]);
            skip.add(methodKey);
          }
        }
      }
    }
  }
  
  static Class<?> loadAdapterClass(String className, byte[] classBytes) {
    Class<?> domainClass = SecurityController.getStaticSecurityDomainClass();
    Object staticDomain; Object staticDomain; if ((domainClass == java.security.CodeSource.class) || (domainClass == ProtectionDomain.class))
    {


      ProtectionDomain protectionDomain = SecurityUtilities.getScriptProtectionDomain();
      if (protectionDomain == null)
        protectionDomain = JavaAdapter.class.getProtectionDomain();
      Object staticDomain;
      if (domainClass == java.security.CodeSource.class)
      {
        staticDomain = protectionDomain == null ? null : protectionDomain.getCodeSource();
      } else {
        staticDomain = protectionDomain;
      }
    } else {
      staticDomain = null;
    }
    GeneratedClassLoader loader = SecurityController.createLoader(null, staticDomain);
    
    Class<?> result = loader.defineClass(className, classBytes);
    loader.linkClass(result);
    return result;
  }
  
  public static Function getFunction(Scriptable obj, String functionName) {
    Object x = ScriptableObject.getProperty(obj, functionName);
    if (x == Scriptable.NOT_FOUND)
    {




      return null;
    }
    if (!(x instanceof Function)) {
      throw ScriptRuntime.notFunctionError(x, functionName);
    }
    return (Function)x;
  }
  





  public static Object callMethod(ContextFactory factory, final Scriptable thisObj, final Function f, final Object[] args, final long argsToWrap)
  {
    if (f == null)
    {
      return null;
    }
    if (factory == null) {
      factory = ContextFactory.getGlobal();
    }
    
    Scriptable scope = f.getParentScope();
    if (argsToWrap == 0L) {
      return Context.call(factory, f, scope, thisObj, args);
    }
    
    Context cx = Context.getCurrentContext();
    if (cx != null) {
      return doCall(cx, scope, thisObj, f, args, argsToWrap);
    }
    factory.call(new ContextAction() {
      public Object run(Context cx) {
        return JavaAdapter.doCall(cx, val$scope, thisObj, f, args, argsToWrap);
      }
    });
  }
  


  private static Object doCall(Context cx, Scriptable scope, Scriptable thisObj, Function f, Object[] args, long argsToWrap)
  {
    for (int i = 0; i != args.length; i++) {
      if (0L != (argsToWrap & 1 << i)) {
        Object arg = args[i];
        if (!(arg instanceof Scriptable)) {
          args[i] = cx.getWrapFactory().wrap(cx, scope, arg, null);
        }
      }
    }
    return f.call(cx, scope, thisObj, args);
  }
  
  public static Scriptable runScript(Script script) {
    
      (Scriptable)ContextFactory.getGlobal().call(new ContextAction() {
        public Object run(Context cx) {
          ScriptableObject global = ScriptRuntime.getGlobal(cx);
          val$script.exec(cx, global);
          return global;
        }
      });
  }
  
  private static void generateCtor(ClassFileWriter cfw, String adapterName, String superName, Constructor<?> superCtor)
  {
    short locals = 3;
    Class<?>[] parameters = superCtor.getParameterTypes();
    


    if (parameters.length == 0) {
      cfw.startMethod("<init>", "(Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Lnet/sourceforge/htmlunit/corejs/javascript/ContextFactory;)V", (short)1);
      




      cfw.add(42);
      cfw.addInvoke(183, superName, "<init>", "()V");
    } else {
      StringBuilder sig = new StringBuilder("(Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Lnet/sourceforge/htmlunit/corejs/javascript/ContextFactory;");
      

      int marker = sig.length();
      
      Class<?>[] arrayOfClass1 = parameters;int i = arrayOfClass1.length; for (Class<?> localClass1 = 0; localClass1 < i; localClass1++) { c = arrayOfClass1[localClass1];
        appendTypeString(sig, c);
      }
      sig.append(")V");
      cfw.startMethod("<init>", sig.toString(), (short)1);
      


      cfw.add(42);
      short paramOffset = 3;
      Class<?>[] arrayOfClass2 = parameters;localClass1 = arrayOfClass2.length; for (Class<?> c = 0; c < localClass1; c++) { Class<?> parameter = arrayOfClass2[c];
        paramOffset = (short)(paramOffset + generatePushParam(cfw, paramOffset, parameter));
      }
      locals = paramOffset;
      sig.delete(1, marker);
      cfw.addInvoke(183, superName, "<init>", sig
        .toString());
    }
    

    cfw.add(42);
    cfw.add(43);
    cfw.add(181, adapterName, "delegee", "Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;");
    


    cfw.add(42);
    cfw.add(44);
    cfw.add(181, adapterName, "factory", "Lnet/sourceforge/htmlunit/corejs/javascript/ContextFactory;");
    

    cfw.add(42);
    
    cfw.add(43);
    cfw.add(42);
    cfw.addInvoke(184, "net/sourceforge/htmlunit/corejs/javascript/JavaAdapter", "createAdapterWrapper", "(Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Ljava/lang/Object;)Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;");
    




    cfw.add(181, adapterName, "self", "Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;");
    

    cfw.add(177);
    cfw.stopMethod(locals);
  }
  
  private static void generateSerialCtor(ClassFileWriter cfw, String adapterName, String superName)
  {
    cfw.startMethod("<init>", "(Lnet/sourceforge/htmlunit/corejs/javascript/ContextFactory;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;)V", (short)1);
    






    cfw.add(42);
    cfw.addInvoke(183, superName, "<init>", "()V");
    

    cfw.add(42);
    cfw.add(43);
    cfw.add(181, adapterName, "factory", "Lnet/sourceforge/htmlunit/corejs/javascript/ContextFactory;");
    


    cfw.add(42);
    cfw.add(44);
    cfw.add(181, adapterName, "delegee", "Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;");
    

    cfw.add(42);
    cfw.add(45);
    cfw.add(181, adapterName, "self", "Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;");
    

    cfw.add(177);
    cfw.stopMethod((short)4);
  }
  
  private static void generateEmptyCtor(ClassFileWriter cfw, String adapterName, String superName, String scriptClassName)
  {
    cfw.startMethod("<init>", "()V", (short)1);
    

    cfw.add(42);
    cfw.addInvoke(183, superName, "<init>", "()V");
    

    cfw.add(42);
    cfw.add(1);
    cfw.add(181, adapterName, "factory", "Lnet/sourceforge/htmlunit/corejs/javascript/ContextFactory;");
    


    cfw.add(187, scriptClassName);
    cfw.add(89);
    cfw.addInvoke(183, scriptClassName, "<init>", "()V");
    

    cfw.addInvoke(184, "net/sourceforge/htmlunit/corejs/javascript/JavaAdapter", "runScript", "(Lnet/sourceforge/htmlunit/corejs/javascript/Script;)Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;");
    



    cfw.add(76);
    

    cfw.add(42);
    cfw.add(43);
    cfw.add(181, adapterName, "delegee", "Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;");
    

    cfw.add(42);
    
    cfw.add(43);
    cfw.add(42);
    cfw.addInvoke(184, "net/sourceforge/htmlunit/corejs/javascript/JavaAdapter", "createAdapterWrapper", "(Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Ljava/lang/Object;)Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;");
    




    cfw.add(181, adapterName, "self", "Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;");
    

    cfw.add(177);
    cfw.stopMethod((short)2);
  }
  






  static void generatePushWrappedArgs(ClassFileWriter cfw, Class<?>[] argTypes, int arrayLength)
  {
    cfw.addPush(arrayLength);
    cfw.add(189, "java/lang/Object");
    int paramOffset = 1;
    for (int i = 0; i != argTypes.length; i++) {
      cfw.add(89);
      cfw.addPush(i);
      paramOffset += generateWrapArg(cfw, paramOffset, argTypes[i]);
      cfw.add(83);
    }
  }
  





  private static int generateWrapArg(ClassFileWriter cfw, int paramOffset, Class<?> argType)
  {
    int size = 1;
    if (!argType.isPrimitive()) {
      cfw.add(25, paramOffset);
    }
    else if (argType == Boolean.TYPE)
    {
      cfw.add(187, "java/lang/Boolean");
      cfw.add(89);
      cfw.add(21, paramOffset);
      cfw.addInvoke(183, "java/lang/Boolean", "<init>", "(Z)V");

    }
    else if (argType == Character.TYPE)
    {
      cfw.add(21, paramOffset);
      cfw.addInvoke(184, "java/lang/String", "valueOf", "(C)Ljava/lang/String;");

    }
    else
    {
      cfw.add(187, "java/lang/Double");
      cfw.add(89);
      String typeName = argType.getName();
      switch (typeName.charAt(0))
      {
      case 'b': 
      case 'i': 
      case 's': 
        cfw.add(21, paramOffset);
        cfw.add(135);
        break;
      
      case 'l': 
        cfw.add(22, paramOffset);
        cfw.add(138);
        size = 2;
        break;
      
      case 'f': 
        cfw.add(23, paramOffset);
        cfw.add(141);
        break;
      case 'd': 
        cfw.add(24, paramOffset);
        size = 2;
      }
      
      cfw.addInvoke(183, "java/lang/Double", "<init>", "(D)V");
    }
    
    return size;
  }
  







  static void generateReturnResult(ClassFileWriter cfw, Class<?> retType, boolean callConvertResult)
  {
    if (retType == Void.TYPE) {
      cfw.add(87);
      cfw.add(177);
    }
    else if (retType == Boolean.TYPE) {
      cfw.addInvoke(184, "net/sourceforge/htmlunit/corejs/javascript/Context", "toBoolean", "(Ljava/lang/Object;)Z");
      

      cfw.add(172);
    }
    else if (retType == Character.TYPE)
    {


      cfw.addInvoke(184, "net/sourceforge/htmlunit/corejs/javascript/Context", "toString", "(Ljava/lang/Object;)Ljava/lang/String;");
      

      cfw.add(3);
      cfw.addInvoke(182, "java/lang/String", "charAt", "(I)C");
      
      cfw.add(172);
    }
    else if (retType.isPrimitive()) {
      cfw.addInvoke(184, "net/sourceforge/htmlunit/corejs/javascript/Context", "toNumber", "(Ljava/lang/Object;)D");
      

      String typeName = retType.getName();
      switch (typeName.charAt(0)) {
      case 'b': 
      case 'i': 
      case 's': 
        cfw.add(142);
        cfw.add(172);
        break;
      case 'l': 
        cfw.add(143);
        cfw.add(173);
        break;
      case 'f': 
        cfw.add(144);
        cfw.add(174);
        break;
      case 'd': 
        cfw.add(175);
        break;
      case 'c': case 'e': case 'g': case 'h': case 'j': case 'k': case 'm': 
      case 'n': case 'o': case 'p': case 'q': case 'r': default: 
        throw new RuntimeException("Unexpected return type " + retType.toString());
      }
    }
    else {
      String retTypeStr = retType.getName();
      if (callConvertResult) {
        cfw.addLoadConstant(retTypeStr);
        cfw.addInvoke(184, "java/lang/Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;");
        

        cfw.addInvoke(184, "net/sourceforge/htmlunit/corejs/javascript/JavaAdapter", "convertResult", "(Ljava/lang/Object;Ljava/lang/Class;)Ljava/lang/Object;");
      }
      



      cfw.add(192, retTypeStr);
      cfw.add(176);
    }
  }
  

  private static void generateMethod(ClassFileWriter cfw, String genName, String methodName, Class<?>[] parms, Class<?> returnType, boolean convertResult)
  {
    StringBuilder sb = new StringBuilder();
    int paramsEnd = appendMethodSignature(parms, returnType, sb);
    String methodSignature = sb.toString();
    cfw.startMethod(methodName, methodSignature, (short)1);
    




    cfw.add(42);
    cfw.add(180, genName, "factory", "Lnet/sourceforge/htmlunit/corejs/javascript/ContextFactory;");
    


    cfw.add(42);
    cfw.add(180, genName, "self", "Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;");
    


    cfw.add(42);
    cfw.add(180, genName, "delegee", "Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;");
    
    cfw.addPush(methodName);
    cfw.addInvoke(184, "net/sourceforge/htmlunit/corejs/javascript/JavaAdapter", "getFunction", "(Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Ljava/lang/String;)Lnet/sourceforge/htmlunit/corejs/javascript/Function;");
    






    generatePushWrappedArgs(cfw, parms, parms.length);
    

    if (parms.length > 64)
    {

      throw Context.reportRuntimeError0("JavaAdapter can not subclass methods with more then 64 arguments.");
    }
    

    long convertionMask = 0L;
    for (int i = 0; i != parms.length; i++) {
      if (!parms[i].isPrimitive()) {
        convertionMask |= 1 << i;
      }
    }
    cfw.addPush(convertionMask);
    


    cfw.addInvoke(184, "net/sourceforge/htmlunit/corejs/javascript/JavaAdapter", "callMethod", "(Lnet/sourceforge/htmlunit/corejs/javascript/ContextFactory;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Lnet/sourceforge/htmlunit/corejs/javascript/Function;[Ljava/lang/Object;J)Ljava/lang/Object;");
    






    generateReturnResult(cfw, returnType, convertResult);
    
    cfw.stopMethod((short)paramsEnd);
  }
  




  private static int generatePushParam(ClassFileWriter cfw, int paramOffset, Class<?> paramType)
  {
    if (!paramType.isPrimitive()) {
      cfw.addALoad(paramOffset);
      return 1;
    }
    String typeName = paramType.getName();
    switch (typeName.charAt(0))
    {
    case 'b': 
    case 'c': 
    case 'i': 
    case 's': 
    case 'z': 
      cfw.addILoad(paramOffset);
      return 1;
    
    case 'l': 
      cfw.addLLoad(paramOffset);
      return 2;
    
    case 'f': 
      cfw.addFLoad(paramOffset);
      return 1;
    case 'd': 
      cfw.addDLoad(paramOffset);
      return 2;
    }
    throw Kit.codeBug();
  }
  




  private static void generatePopResult(ClassFileWriter cfw, Class<?> retType)
  {
    if (retType.isPrimitive()) {
      String typeName = retType.getName();
      switch (typeName.charAt(0)) {
      case 'b': 
      case 'c': 
      case 'i': 
      case 's': 
      case 'z': 
        cfw.add(172);
        break;
      case 'l': 
        cfw.add(173);
        break;
      case 'f': 
        cfw.add(174);
        break;
      case 'd': 
        cfw.add(175);
      }
    }
    else {
      cfw.add(176);
    }
  }
  






  private static void generateSuper(ClassFileWriter cfw, String genName, String superName, String methodName, String methodSignature, Class<?>[] parms, Class<?> returnType)
  {
    cfw.startMethod("super$" + methodName, methodSignature, (short)1);
    


    cfw.add(25, 0);
    

    int paramOffset = 1;
    for (Class<?> parm : parms) {
      paramOffset += generatePushParam(cfw, paramOffset, parm);
    }
    

    cfw.addInvoke(183, superName, methodName, methodSignature);
    


    Class<?> retType = returnType;
    if (!retType.equals(Void.TYPE)) {
      generatePopResult(cfw, retType);
    } else {
      cfw.add(177);
    }
    cfw.stopMethod((short)(paramOffset + 1));
  }
  



  private static String getMethodSignature(Method method, Class<?>[] argTypes)
  {
    StringBuilder sb = new StringBuilder();
    appendMethodSignature(argTypes, method.getReturnType(), sb);
    return sb.toString();
  }
  
  static int appendMethodSignature(Class<?>[] argTypes, Class<?> returnType, StringBuilder sb)
  {
    sb.append('(');
    int firstLocal = 1 + argTypes.length;
    for (Class<?> type : argTypes) {
      appendTypeString(sb, type);
      if ((type == Long.TYPE) || (type == Double.TYPE))
      {
        firstLocal++;
      }
    }
    sb.append(')');
    appendTypeString(sb, returnType);
    return firstLocal;
  }
  
  private static StringBuilder appendTypeString(StringBuilder sb, Class<?> type)
  {
    while (type.isArray()) {
      sb.append('[');
      type = type.getComponentType();
    }
    if (type.isPrimitive()) { char typeLetter;
      char typeLetter;
      if (type == Boolean.TYPE) {
        typeLetter = 'Z'; } else { char typeLetter;
        if (type == Long.TYPE) {
          typeLetter = 'J';
        } else {
          String typeName = type.getName();
          typeLetter = Character.toUpperCase(typeName.charAt(0));
        } }
      sb.append(typeLetter);
    } else {
      sb.append('L');
      sb.append(type.getName().replace('.', '/'));
      sb.append(';');
    }
    return sb;
  }
  
  static int[] getArgsToConvert(Class<?>[] argTypes) {
    int count = 0;
    for (int i = 0; i != argTypes.length; i++) {
      if (!argTypes[i].isPrimitive())
        count++;
    }
    if (count == 0)
      return null;
    int[] array = new int[count];
    count = 0;
    for (int i = 0; i != argTypes.length; i++) {
      if (!argTypes[i].isPrimitive())
        array[(count++)] = i;
    }
    return array;
  }
  
  private static final Object FTAG = "JavaAdapter";
  private static final int Id_JavaAdapter = 1;
}
