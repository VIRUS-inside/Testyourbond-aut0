package net.sourceforge.htmlunit.corejs.javascript;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

class JavaMembers
{
  private Class<?> cl;
  private Map<String, Object> members;
  private Map<String, FieldAndMethods> fieldAndMethods;
  private Map<String, Object> staticMembers;
  private Map<String, FieldAndMethods> staticFieldAndMethods;
  NativeJavaMethod ctors;
  
  JavaMembers(Scriptable scope, Class<?> cl)
  {
    this(scope, cl, false);
  }
  
  JavaMembers(Scriptable scope, Class<?> cl, boolean includeProtected) {
    try {
      Context cx = ContextFactory.getGlobal().enterContext();
      ClassShutter shutter = cx.getClassShutter();
      if ((shutter != null) && (!shutter.visibleToScripts(cl.getName()))) {
        throw Context.reportRuntimeError1("msg.access.prohibited", cl
          .getName());
      }
      members = new HashMap();
      staticMembers = new HashMap();
      this.cl = cl;
      
      boolean includePrivate = cx.hasFeature(13);
      reflect(scope, includeProtected, includePrivate);
    } finally {
      Context.exit();
    }
  }
  
  boolean has(String name, boolean isStatic) {
    Map<String, Object> ht = isStatic ? staticMembers : members;
    Object obj = ht.get(name);
    if (obj != null) {
      return true;
    }
    return findExplicitFunction(name, isStatic) != null;
  }
  
  Object get(Scriptable scope, String name, Object javaObject, boolean isStatic)
  {
    Map<String, Object> ht = isStatic ? staticMembers : members;
    Object member = ht.get(name);
    if ((!isStatic) && (member == null))
    {
      member = staticMembers.get(name);
    }
    if (member == null) {
      member = getExplicitFunction(scope, name, javaObject, isStatic);
      
      if (member == null)
        return Scriptable.NOT_FOUND;
    }
    if ((member instanceof Scriptable)) {
      return member;
    }
    Context cx = Context.getContext();
    try
    {
      Class<?> type;
      if ((member instanceof BeanProperty)) {
        BeanProperty bp = (BeanProperty)member;
        if (getter == null)
          return Scriptable.NOT_FOUND;
        Object rval = getter.invoke(javaObject, Context.emptyArgs);
        type = getter.method().getReturnType();
      } else {
        Field field = (Field)member;
        Object rval = field.get(isStatic ? null : javaObject);
        type = field.getType();
      }
    } catch (Exception ex) { Class<?> type;
      throw Context.throwAsScriptRuntimeEx(ex); }
    Class<?> type;
    Object rval;
    scope = ScriptableObject.getTopLevelScope(scope);
    return cx.getWrapFactory().wrap(cx, scope, rval, type);
  }
  
  void put(Scriptable scope, String name, Object javaObject, Object value, boolean isStatic)
  {
    Map<String, Object> ht = isStatic ? staticMembers : members;
    Object member = ht.get(name);
    if ((!isStatic) && (member == null))
    {
      member = staticMembers.get(name);
    }
    if (member == null)
      throw reportMemberNotFound(name);
    if ((member instanceof FieldAndMethods)) {
      FieldAndMethods fam = (FieldAndMethods)ht.get(name);
      member = field;
    }
    

    if ((member instanceof BeanProperty)) {
      BeanProperty bp = (BeanProperty)member;
      if (setter == null) {
        throw reportMemberNotFound(name);
      }
      


      if ((setters == null) || (value == null)) {
        Class<?> setType = setter.argTypes[0];
        Object[] args = { Context.jsToJava(value, setType) };
        try {
          setter.invoke(javaObject, args);
        } catch (Exception ex) {
          throw Context.throwAsScriptRuntimeEx(ex);
        }
      } else {
        Object[] args = { value };
        setters.call(Context.getContext(), 
          ScriptableObject.getTopLevelScope(scope), scope, args);
      }
    } else {
      if (!(member instanceof Field)) {
        String str = member == null ? "msg.java.internal.private" : "msg.java.method.assign";
        
        throw Context.reportRuntimeError1(str, name);
      }
      Field field = (Field)member;
      Object javaValue = Context.jsToJava(value, field.getType());
      try {
        field.set(javaObject, javaValue);
      } catch (IllegalAccessException accessEx) {
        if ((field.getModifiers() & 0x10) != 0)
        {
          return;
        }
        throw Context.throwAsScriptRuntimeEx(accessEx);
      } catch (IllegalArgumentException argEx) {
        throw Context.reportRuntimeError3("msg.java.internal.field.type", value
        
          .getClass().getName(), field, javaObject
          .getClass().getName());
      }
    }
  }
  
  Object[] getIds(boolean isStatic) {
    Map<String, Object> map = isStatic ? staticMembers : members;
    return map.keySet().toArray(new Object[map.size()]);
  }
  
  static String javaSignature(Class<?> type) {
    if (!type.isArray()) {
      return type.getName();
    }
    int arrayDimension = 0;
    do {
      arrayDimension++;
      type = type.getComponentType();
    } while (type.isArray());
    String name = type.getName();
    String suffix = "[]";
    if (arrayDimension == 1) {
      return name.concat(suffix);
    }
    int length = name.length() + arrayDimension * suffix.length();
    StringBuilder sb = new StringBuilder(length);
    sb.append(name);
    while (arrayDimension != 0) {
      arrayDimension--;
      sb.append(suffix);
    }
    return sb.toString();
  }
  

  static String liveConnectSignature(Class<?>[] argTypes)
  {
    int N = argTypes.length;
    if (N == 0) {
      return "()";
    }
    StringBuilder sb = new StringBuilder();
    sb.append('(');
    for (int i = 0; i != N; i++) {
      if (i != 0) {
        sb.append(',');
      }
      sb.append(javaSignature(argTypes[i]));
    }
    sb.append(')');
    return sb.toString();
  }
  
  private MemberBox findExplicitFunction(String name, boolean isStatic) {
    int sigStart = name.indexOf('(');
    if (sigStart < 0) {
      return null;
    }
    
    Map<String, Object> ht = isStatic ? staticMembers : members;
    MemberBox[] methodsOrCtors = null;
    boolean isCtor = (isStatic) && (sigStart == 0);
    String trueName;
    Object obj; NativeJavaMethod njm; if (isCtor)
    {
      methodsOrCtors = ctors.methods;
    }
    else {
      trueName = name.substring(0, sigStart);
      obj = ht.get(trueName);
      if ((!isStatic) && (obj == null))
      {
        obj = staticMembers.get(trueName);
      }
      if ((obj instanceof NativeJavaMethod)) {
        njm = (NativeJavaMethod)obj;
        methodsOrCtors = methods;
      }
    }
    
    if (methodsOrCtors != null) {
      trueName = methodsOrCtors;obj = trueName.length; for (njm = 0; njm < obj; njm++) { MemberBox methodsOrCtor = trueName[njm];
        Class<?>[] type = argTypes;
        String sig = liveConnectSignature(type);
        if ((sigStart + sig.length() == name.length()) && 
          (name.regionMatches(sigStart, sig, 0, sig.length()))) {
          return methodsOrCtor;
        }
      }
    }
    
    return null;
  }
  
  private Object getExplicitFunction(Scriptable scope, String name, Object javaObject, boolean isStatic)
  {
    Map<String, Object> ht = isStatic ? staticMembers : members;
    Object member = null;
    MemberBox methodOrCtor = findExplicitFunction(name, isStatic);
    
    if (methodOrCtor != null) {
      Scriptable prototype = ScriptableObject.getFunctionPrototype(scope);
      
      if (methodOrCtor.isCtor()) {
        NativeJavaConstructor fun = new NativeJavaConstructor(methodOrCtor);
        
        fun.setPrototype(prototype);
        member = fun;
        ht.put(name, fun);
      } else {
        String trueName = methodOrCtor.getName();
        member = ht.get(trueName);
        
        if (((member instanceof NativeJavaMethod)) && (methods.length > 1))
        {
          NativeJavaMethod fun = new NativeJavaMethod(methodOrCtor, name);
          
          fun.setPrototype(prototype);
          ht.put(name, fun);
          member = fun;
        }
      }
    }
    
    return member;
  }
  






  private static Method[] discoverAccessibleMethods(Class<?> clazz, boolean includeProtected, boolean includePrivate)
  {
    Map<MethodSignature, Method> map = new HashMap();
    discoverAccessibleMethods(clazz, map, includeProtected, includePrivate);
    return (Method[])map.values().toArray(new Method[map.size()]);
  }
  
  private static void discoverAccessibleMethods(Class<?> clazz, Map<MethodSignature, Method> map, boolean includeProtected, boolean includePrivate)
  {
    Object methods;
    if ((Modifier.isPublic(clazz.getModifiers())) || (includePrivate)) {
      try {
        if ((includeProtected) || (includePrivate)) {
          for (;;) { if (clazz == null)
              break label297;
            try { Method[] methods = clazz.getDeclaredMethods();
              Method[] arrayOfMethod1 = methods;int i = arrayOfMethod1.length; for (localMethod1 = 0; localMethod1 < i; localMethod1++) { method = arrayOfMethod1[localMethod1];
                int mods = method.getModifiers();
                
                if ((Modifier.isPublic(mods)) || (Modifier.isProtected(mods)) || (includePrivate))
                {
                  MethodSignature sig = new MethodSignature(method);
                  
                  if (!map.containsKey(sig)) {
                    if ((includePrivate) && 
                      (!method.isAccessible()))
                      method.setAccessible(true);
                    map.put(sig, method);
                  }
                }
              }
              clazz = clazz.getSuperclass();

            }
            catch (SecurityException e)
            {
              methods = clazz.getMethods();
              Object localObject1 = methods;Method localMethod1 = localObject1.length; for (Method method = 0; method < localMethod1; method++) { Method method = localObject1[method];
                MethodSignature sig = new MethodSignature(method);
                
                if (!map.containsKey(sig)) {
                  map.put(sig, method);
                }
              }
              break label297;
            }
          }
        }
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
          MethodSignature sig = new MethodSignature(method);
          

          if (!map.containsKey(sig))
            map.put(sig, method);
        }
        label297:
        return;
      } catch (SecurityException e) {
        Context.reportWarning(
        
          "Could not discover accessible methods of class " + clazz.getName() + " due to lack of privileges, attemping superclasses/interfaces.");
      }
    }
    




    Class<?>[] interfaces = clazz.getInterfaces();
    for (Class<?> intface : interfaces) {
      discoverAccessibleMethods(intface, map, includeProtected, includePrivate);
    }
    
    Object superclass = clazz.getSuperclass();
    if (superclass != null) {
      discoverAccessibleMethods((Class)superclass, map, includeProtected, includePrivate);
    }
  }
  
  private static final class MethodSignature
  {
    private final String name;
    private final Class<?>[] args;
    
    private MethodSignature(String name, Class<?>[] args) {
      this.name = name;
      this.args = args;
    }
    
    MethodSignature(Method method) {
      this(method.getName(), method.getParameterTypes());
    }
    
    public boolean equals(Object o)
    {
      if ((o instanceof MethodSignature)) {
        MethodSignature ms = (MethodSignature)o;
        return (name.equals(name)) && (java.util.Arrays.equals(args, args));
      }
      return false;
    }
    
    public int hashCode()
    {
      return name.hashCode() ^ args.length;
    }
  }
  




  private void reflect(Scriptable scope, boolean includeProtected, boolean includePrivate)
  {
    Method[] methods = discoverAccessibleMethods(cl, includeProtected, includePrivate);
    
    for (method : methods) {
      int mods = method.getModifiers();
      boolean isStatic = Modifier.isStatic(mods);
      Map<String, Object> ht = isStatic ? staticMembers : members;
      String name = method.getName();
      Object value = ht.get(name);
      if (value == null) {
        ht.put(name, method);
      } else { ObjArray overloadedMethods;
        ObjArray overloadedMethods;
        if ((value instanceof ObjArray)) {
          overloadedMethods = (ObjArray)value;
        } else {
          if (!(value instanceof Method)) {
            Kit.codeBug();
          }
          
          overloadedMethods = new ObjArray();
          overloadedMethods.add(value);
          ht.put(name, overloadedMethods);
        }
        overloadedMethods.add(method);
      }
    }
    
    boolean isStatic;
    Object ht;
    for (int tableCursor = 0; tableCursor != 2; tableCursor++) {
      isStatic = tableCursor == 0;
      ht = isStatic ? staticMembers : members;
      for (Map.Entry<String, Object> entry : ((Map)ht).entrySet())
      {
        Object value = entry.getValue();
        MemberBox[] methodBoxes; if ((value instanceof Method)) {
          MemberBox[] methodBoxes = new MemberBox[1];
          methodBoxes[0] = new MemberBox((Method)value);
        } else {
          ObjArray overloadedMethods = (ObjArray)value;
          int N = overloadedMethods.size();
          if (N < 2)
            Kit.codeBug();
          methodBoxes = new MemberBox[N];
          for (int i = 0; i != N; i++) {
            Method method = (Method)overloadedMethods.get(i);
            methodBoxes[i] = new MemberBox(method);
          }
        }
        NativeJavaMethod fun = new NativeJavaMethod(methodBoxes);
        if (scope != null) {
          ScriptRuntime.setFunctionProtoAndParent(fun, scope);
        }
        ((Map)ht).put(entry.getKey(), fun);
      }
    }
    

    Field[] fields = getAccessibleFields(includeProtected, includePrivate);
    Field[] arrayOfField1 = fields;Method localMethod1 = arrayOfField1.length; String name; for (Method method = 0; method < localMethod1; method++) { Field field = arrayOfField1[method];
      name = field.getName();
      int mods = field.getModifiers();
      try {
        boolean isStatic = Modifier.isStatic(mods);
        Map<String, Object> ht = isStatic ? staticMembers : members;
        Object member = ht.get(name);
        if (member == null) {
          ht.put(name, field);
        } else if ((member instanceof NativeJavaMethod)) {
          NativeJavaMethod method = (NativeJavaMethod)member;
          FieldAndMethods fam = new FieldAndMethods(scope, methods, field);
          
          Map<String, FieldAndMethods> fmht = isStatic ? staticFieldAndMethods : fieldAndMethods;
          
          if (fmht == null) {
            fmht = new HashMap();
            if (isStatic) {
              staticFieldAndMethods = fmht;
            } else {
              fieldAndMethods = fmht;
            }
          }
          fmht.put(name, fam);
          ht.put(name, fam);
        } else if ((member instanceof Field)) {
          Field oldField = (Field)member;
          






          if (oldField.getDeclaringClass().isAssignableFrom(field.getDeclaringClass())) {
            ht.put(name, field);
          }
        }
        else {
          Kit.codeBug();
        }
      }
      catch (SecurityException e) {
        Context.reportWarning(
        
          "Could not access field " + name + " of class " + cl.getName() + " due to lack of privileges.");
      }
    }
    
    Map<String, Object> ht;
    Map<String, BeanProperty> toAdd;
    for (int tableCursor = 0; tableCursor != 2; tableCursor++) {
      boolean isStatic = tableCursor == 0;
      ht = isStatic ? staticMembers : members;
      
      toAdd = new HashMap();
      

      for (String name : ht.keySet())
      {
        boolean memberIsGetMethod = name.startsWith("get");
        boolean memberIsSetMethod = name.startsWith("set");
        boolean memberIsIsMethod = name.startsWith("is");
        if ((memberIsGetMethod) || (memberIsIsMethod) || (memberIsSetMethod))
        {


          String nameComponent = name.substring(memberIsIsMethod ? 2 : 3);
          if (nameComponent.length() != 0)
          {


            String beanPropertyName = nameComponent;
            char ch0 = nameComponent.charAt(0);
            if (Character.isUpperCase(ch0)) {
              if (nameComponent.length() == 1) {
                beanPropertyName = nameComponent.toLowerCase();
              } else {
                char ch1 = nameComponent.charAt(1);
                if (!Character.isUpperCase(ch1))
                {
                  beanPropertyName = Character.toLowerCase(ch0) + nameComponent.substring(1);
                }
              }
            }
            


            if (!toAdd.containsKey(beanPropertyName))
            {
              Object v = ht.get(beanPropertyName);
              if ((v == null) || (
              
                (includePrivate) && ((v instanceof Member)) && 
                
                (Modifier.isPrivate(((Member)v).getModifiers()))))
              {







                MemberBox getter = null;
                getter = findGetter(isStatic, ht, "get", nameComponent);
                
                if (getter == null) {
                  getter = findGetter(isStatic, ht, "is", nameComponent);
                }
                

                MemberBox setter = null;
                NativeJavaMethod setters = null;
                String setterName = "set".concat(nameComponent);
                
                if (ht.containsKey(setterName))
                {
                  Object member = ht.get(setterName);
                  if ((member instanceof NativeJavaMethod)) {
                    NativeJavaMethod njmSet = (NativeJavaMethod)member;
                    if (getter != null)
                    {

                      Class<?> type = getter.method().getReturnType();
                      setter = extractSetMethod(type, methods, isStatic);
                    }
                    else
                    {
                      setter = extractSetMethod(methods, isStatic);
                    }
                    
                    if (methods.length > 1) {
                      setters = njmSet;
                    }
                  }
                }
                
                BeanProperty bp = new BeanProperty(getter, setter, setters);
                toAdd.put(beanPropertyName, bp);
              }
            }
          }
        } }
      for (String key : toAdd.keySet()) {
        Object value = toAdd.get(key);
        ht.put(key, value);
      }
    }
    

    Object constructors = getAccessibleConstructors(includePrivate);
    
    MemberBox[] ctorMembers = new MemberBox[constructors.length];
    for (int i = 0; i != constructors.length; i++) {
      ctorMembers[i] = new MemberBox(constructors[i]);
    }
    ctors = new NativeJavaMethod(ctorMembers, cl.getSimpleName());
  }
  

  private Constructor<?>[] getAccessibleConstructors(boolean includePrivate)
  {
    if ((includePrivate) && (cl != ScriptRuntime.ClassClass)) {
      try {
        Constructor<?>[] cons = cl.getDeclaredConstructors();
        java.lang.reflect.AccessibleObject.setAccessible(cons, true);
        
        return cons;
      }
      catch (SecurityException e) {
        Context.reportWarning(
        
          "Could not access constructor  of class " + cl.getName() + " due to lack of privileges.");
      }
    }
    return cl.getConstructors();
  }
  
  private Field[] getAccessibleFields(boolean includeProtected, boolean includePrivate)
  {
    if ((includePrivate) || (includeProtected)) {
      try {
        List<Field> fieldsList = new java.util.ArrayList();
        Class<?> currentClass = cl;
        
        while (currentClass != null)
        {

          Field[] declared = currentClass.getDeclaredFields();
          for (Field field : declared) {
            int mod = field.getModifiers();
            if ((includePrivate) || (Modifier.isPublic(mod)) || 
              (Modifier.isProtected(mod))) {
              if (!field.isAccessible())
                field.setAccessible(true);
              fieldsList.add(field);
            }
          }
          

          currentClass = currentClass.getSuperclass();
        }
        
        return (Field[])fieldsList.toArray(new Field[fieldsList.size()]);
      }
      catch (SecurityException localSecurityException) {}
    }
    
    return cl.getFields();
  }
  
  private MemberBox findGetter(boolean isStatic, Map<String, Object> ht, String prefix, String propertyName)
  {
    String getterName = prefix.concat(propertyName);
    if (ht.containsKey(getterName))
    {
      Object member = ht.get(getterName);
      if ((member instanceof NativeJavaMethod)) {
        NativeJavaMethod njmGet = (NativeJavaMethod)member;
        return extractGetMethod(methods, isStatic);
      }
    }
    return null;
  }
  


  private static MemberBox extractGetMethod(MemberBox[] methods, boolean isStatic)
  {
    for (MemberBox method : methods)
    {

      if ((argTypes.length == 0) && ((!isStatic) || 
        (method.isStatic()))) {
        Class<?> type = method.method().getReturnType();
        if (type == Void.TYPE) break;
        return method;
      }
    }
    

    return null;
  }
  








  private static MemberBox extractSetMethod(Class<?> type, MemberBox[] methods, boolean isStatic)
  {
    for (int pass = 1; pass <= 2; pass++) {
      for (MemberBox method : methods) {
        if ((!isStatic) || (method.isStatic())) {
          Class<?>[] params = argTypes;
          if (params.length == 1) {
            if (pass == 1) {
              if (params[0] == type) {
                return method;
              }
            } else {
              if (pass != 2)
                Kit.codeBug();
              if (params[0].isAssignableFrom(type)) {
                return method;
              }
            }
          }
        }
      }
    }
    return null;
  }
  

  private static MemberBox extractSetMethod(MemberBox[] methods, boolean isStatic)
  {
    for (MemberBox method : methods) {
      if (((!isStatic) || (method.isStatic())) && 
        (method.method().getReturnType() == Void.TYPE) && 
        (argTypes.length == 1)) {
        return method;
      }
    }
    

    return null;
  }
  
  Map<String, FieldAndMethods> getFieldAndMethodsObjects(Scriptable scope, Object javaObject, boolean isStatic)
  {
    Map<String, FieldAndMethods> ht = isStatic ? staticFieldAndMethods : fieldAndMethods;
    
    if (ht == null)
      return null;
    int len = ht.size();
    Map<String, FieldAndMethods> result = new HashMap(len);
    
    for (FieldAndMethods fam : ht.values()) {
      FieldAndMethods famNew = new FieldAndMethods(scope, methods, field);
      
      javaObject = javaObject;
      result.put(field.getName(), famNew);
    }
    return result;
  }
  

  static JavaMembers lookupClass(Scriptable scope, Class<?> dynamicType, Class<?> staticType, boolean includeProtected)
  {
    ClassCache cache = ClassCache.get(scope);
    Map<Class<?>, JavaMembers> ct = cache.getClassCacheMap();
    
    Class<?> cl = dynamicType;
    JavaMembers members;
    for (;;) { members = (JavaMembers)ct.get(cl);
      if (members != null) {
        if (cl != dynamicType)
        {


          ct.put(dynamicType, members);
        }
        return members;
      }
      try {
        members = new JavaMembers(cache.getAssociatedScope(), cl, includeProtected);


      }
      catch (SecurityException e)
      {


        if ((staticType != null) && (staticType.isInterface())) {
          cl = staticType;
          staticType = null;
        } else {
          Class<?> parent = cl.getSuperclass();
          if (parent == null) {
            if (cl.isInterface())
            {
              parent = ScriptRuntime.ObjectClass;
            } else {
              throw e;
            }
          }
          cl = parent;
        }
      }
    }
    
    if (cache.isCachingEnabled()) {
      ct.put(cl, members);
      if (cl != dynamicType)
      {

        ct.put(dynamicType, members);
      }
    }
    return members;
  }
  
  RuntimeException reportMemberNotFound(String memberName) {
    return Context.reportRuntimeError2("msg.java.member.not.found", cl
      .getName(), memberName);
  }
}
