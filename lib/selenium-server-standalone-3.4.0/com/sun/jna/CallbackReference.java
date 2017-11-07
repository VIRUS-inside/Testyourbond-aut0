package com.sun.jna;

import com.sun.jna.win32.DLLCallback;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
















class CallbackReference
  extends WeakReference
{
  static final Map callbackMap = new WeakHashMap();
  static final Map directCallbackMap = new WeakHashMap();
  static final Map pointerCallbackMap = new WeakHashMap();
  static final Map allocations = new WeakHashMap();
  private static final Method PROXY_CALLBACK_METHOD;
  
  static
  {
    try {
      PROXY_CALLBACK_METHOD = CallbackProxy.class.getMethod("callback", new Class[] { [Ljava.lang.Object.class });
    }
    catch (Exception e) {
      throw new Error("Error looking up CallbackProxy.callback() method");
    }
  }
  
  private static final Map initializers = new WeakHashMap();
  
  static void setCallbackThreadInitializer(Callback cb, CallbackThreadInitializer initializer) { synchronized (callbackMap) {
      if (initializer != null) {
        initializers.put(cb, initializer);
      }
      else
        initializers.remove(cb);
    }
  }
  
  static class AttachOptions
    extends Structure {
    public boolean daemon;
    public boolean detach;
    public String name;
    
    AttachOptions() { setStringEncoding("utf8"); }
    
    protected List getFieldOrder() { return Arrays.asList(new String[] { "daemon", "detach", "name" }); }
  }
  

  private static ThreadGroup initializeThread(Callback cb, AttachOptions args)
  {
    CallbackThreadInitializer init = null;
    if ((cb instanceof DefaultCallbackProxy)) {
      cb = ((DefaultCallbackProxy)cb).getCallback();
    }
    synchronized (callbackMap) {
      init = (CallbackThreadInitializer)initializers.get(cb);
    }
    ThreadGroup group = null;
    if (init != null) {
      group = init.getThreadGroup(cb);
      name = init.getName(cb);
      daemon = init.isDaemon(cb);
      detach = init.detach(cb);
      args.write();
    }
    return group;
  }
  


  Pointer cbstruct;
  
  Pointer trampoline;
  
  public static Callback getCallback(Class type, Pointer p)
  {
    return getCallback(type, p, false);
  }
  
  private static Callback getCallback(Class type, Pointer p, boolean direct) {
    if (p == null) {
      return null;
    }
    
    if (!type.isInterface())
      throw new IllegalArgumentException("Callback type must be an interface");
    Map map = direct ? directCallbackMap : callbackMap;
    synchronized (callbackMap) {
      Callback cb = null;
      Reference ref = (Reference)pointerCallbackMap.get(p);
      if (ref != null) {
        cb = (Callback)ref.get();
        if ((cb != null) && (!type.isAssignableFrom(cb.getClass()))) {
          throw new IllegalStateException("Pointer " + p + " already mapped to " + cb);
        }
        return cb;
      }
      int ctype = AltCallingConvention.class.isAssignableFrom(type) ? 1 : 0;
      
      Map foptions = new HashMap(Native.getLibraryOptions(type));
      foptions.put("invoking-method", getCallbackMethod(type));
      NativeFunctionHandler h = new NativeFunctionHandler(p, ctype, foptions);
      cb = (Callback)Proxy.newProxyInstance(type.getClassLoader(), new Class[] { type }, h);
      
      map.put(cb, null);
      pointerCallbackMap.put(p, new WeakReference(cb));
      return cb;
    }
  }
  




  private CallbackReference(Callback callback, int callingConvention, boolean direct)
  {
    super(callback);
    TypeMapper mapper = Native.getTypeMapper(callback.getClass());
    




    boolean ppc = Platform.isPPC();
    if (direct) {
      Method m = getCallbackMethod(callback);
      Class[] ptypes = m.getParameterTypes();
      for (int i = 0; i < ptypes.length; i++)
      {
        if ((ppc) && ((ptypes[i] == Float.TYPE) || (ptypes[i] == Double.TYPE)))
        {
          direct = false;
          break;
        }
        
        if ((mapper != null) && (mapper.getFromNativeConverter(ptypes[i]) != null))
        {
          direct = false;
          break;
        }
      }
      if ((mapper != null) && (mapper.getToNativeConverter(m.getReturnType()) != null))
      {
        direct = false;
      }
    }
    
    String encoding = Native.getStringEncoding(callback.getClass());
    if (direct) {
      method = getCallbackMethod(callback);
      Class[] nativeParamTypes = method.getParameterTypes();
      Class returnType = method.getReturnType();
      int flags = 1;
      if ((callback instanceof DLLCallback)) {
        flags |= 0x2;
      }
      long peer = Native.createNativeCallback(callback, method, nativeParamTypes, returnType, callingConvention, flags, encoding);
      


      cbstruct = (peer != 0L ? new Pointer(peer) : null);
    }
    else {
      if ((callback instanceof CallbackProxy)) {
        proxy = ((CallbackProxy)callback);
      }
      else {
        proxy = new DefaultCallbackProxy(getCallbackMethod(callback), mapper, encoding);
      }
      Class[] nativeParamTypes = proxy.getParameterTypes();
      Class returnType = proxy.getReturnType();
      



      if (mapper != null) {
        for (int i = 0; i < nativeParamTypes.length; i++) {
          FromNativeConverter rc = mapper.getFromNativeConverter(nativeParamTypes[i]);
          if (rc != null) {
            nativeParamTypes[i] = rc.nativeType();
          }
        }
        ToNativeConverter tn = mapper.getToNativeConverter(returnType);
        if (tn != null) {
          returnType = tn.nativeType();
        }
      }
      for (int i = 0; i < nativeParamTypes.length; i++) {
        nativeParamTypes[i] = getNativeType(nativeParamTypes[i]);
        if (!isAllowableNativeType(nativeParamTypes[i])) {
          String msg = "Callback argument " + nativeParamTypes[i] + " requires custom type conversion";
          
          throw new IllegalArgumentException(msg);
        }
      }
      returnType = getNativeType(returnType);
      if (!isAllowableNativeType(returnType)) {
        String msg = "Callback return type " + returnType + " requires custom type conversion";
        
        throw new IllegalArgumentException(msg);
      }
      int flags = (callback instanceof DLLCallback) ? 2 : 0;
      
      long peer = Native.createNativeCallback(proxy, PROXY_CALLBACK_METHOD, nativeParamTypes, returnType, callingConvention, flags, encoding);
      


      cbstruct = (peer != 0L ? new Pointer(peer) : null);
    }
  }
  
  private Class getNativeType(Class cls) {
    if (Structure.class.isAssignableFrom(cls))
    {
      Structure.validate(cls);
      if (!Structure.ByValue.class.isAssignableFrom(cls))
        return Pointer.class;
    } else {
      if (NativeMapped.class.isAssignableFrom(cls)) {
        return NativeMappedConverter.getInstance(cls).nativeType();
      }
      if ((cls == String.class) || (cls == WString.class) || (cls == [Ljava.lang.String.class) || (cls == [Lcom.sun.jna.WString.class) || (Callback.class.isAssignableFrom(cls)))
      {



        return Pointer.class; }
    }
    return cls;
  }
  
  private static Method checkMethod(Method m) {
    if (m.getParameterTypes().length > 256) {
      String msg = "Method signature exceeds the maximum parameter count: " + m;
      
      throw new UnsupportedOperationException(msg);
    }
    return m;
  }
  



  static Class findCallbackClass(Class type)
  {
    if (!Callback.class.isAssignableFrom(type)) {
      throw new IllegalArgumentException(type.getName() + " is not derived from com.sun.jna.Callback");
    }
    if (type.isInterface()) {
      return type;
    }
    Class[] ifaces = type.getInterfaces();
    for (int i = 0; i < ifaces.length; i++) {
      if (Callback.class.isAssignableFrom(ifaces[i])) {
        try
        {
          getCallbackMethod(ifaces[i]);
          return ifaces[i];
        }
        catch (IllegalArgumentException e) {}
      }
    }
    

    if (Callback.class.isAssignableFrom(type.getSuperclass())) {
      return findCallbackClass(type.getSuperclass());
    }
    return type;
  }
  
  private static Method getCallbackMethod(Callback callback) {
    return getCallbackMethod(findCallbackClass(callback.getClass()));
  }
  
  private static Method getCallbackMethod(Class cls)
  {
    Method[] pubMethods = cls.getDeclaredMethods();
    Method[] classMethods = cls.getMethods();
    Set pmethods = new HashSet(Arrays.asList(pubMethods));
    pmethods.retainAll(Arrays.asList(classMethods));
    

    for (Iterator i = pmethods.iterator(); i.hasNext();) {
      Method m = (Method)i.next();
      if (Callback.FORBIDDEN_NAMES.contains(m.getName())) {
        i.remove();
      }
    }
    Method[] methods = (Method[])pmethods.toArray(new Method[pmethods.size()]);
    if (methods.length == 1) {
      return checkMethod(methods[0]);
    }
    for (int i = 0; i < methods.length; i++) {
      Method m = methods[i];
      if ("callback".equals(m.getName())) {
        return checkMethod(m);
      }
    }
    String msg = "Callback must implement a single public method, or one public method named 'callback'";
    
    throw new IllegalArgumentException(msg);
  }
  
  private void setCallbackOptions(int options)
  {
    cbstruct.setInt(Pointer.SIZE, options);
  }
  
  public Pointer getTrampoline()
  {
    if (trampoline == null) {
      trampoline = cbstruct.getPointer(0L);
    }
    return trampoline;
  }
  
  protected void finalize()
  {
    dispose();
  }
  
  protected synchronized void dispose()
  {
    if (cbstruct != null) {
      Native.freeNativeCallback(cbstruct.peer);
      cbstruct.peer = 0L;
      cbstruct = null;
    }
  }
  
  private Callback getCallback() {
    return (Callback)get();
  }
  


  private static Pointer getNativeFunctionPointer(Callback cb)
  {
    if (Proxy.isProxyClass(cb.getClass())) {
      Object handler = Proxy.getInvocationHandler(cb);
      if ((handler instanceof NativeFunctionHandler)) {
        return ((NativeFunctionHandler)handler).getPointer();
      }
    }
    return null;
  }
  


  public static Pointer getFunctionPointer(Callback cb)
  {
    return getFunctionPointer(cb, false);
  }
  
  private static Pointer getFunctionPointer(Callback cb, boolean direct)
  {
    Pointer fp = null;
    if (cb == null) {
      return null;
    }
    if ((fp = getNativeFunctionPointer(cb)) != null) {
      return fp;
    }
    int callingConvention = (cb instanceof AltCallingConvention) ? 1 : 0;
    
    Map map = direct ? directCallbackMap : callbackMap;
    synchronized (callbackMap) {
      CallbackReference cbref = (CallbackReference)map.get(cb);
      if (cbref == null) {
        cbref = new CallbackReference(cb, callingConvention, direct);
        map.put(cb, cbref);
        pointerCallbackMap.put(cbref.getTrampoline(), new WeakReference(cb));
        if (initializers.containsKey(cb)) {
          cbref.setCallbackOptions(1);
        }
      }
      return cbref.getTrampoline();
    }
  }
  
  private class DefaultCallbackProxy implements CallbackProxy {
    private final Method callbackMethod;
    private ToNativeConverter toNative;
    private final FromNativeConverter[] fromNative;
    private final String encoding;
    
    public DefaultCallbackProxy(Method callbackMethod, TypeMapper mapper, String encoding) { this.callbackMethod = callbackMethod;
      this.encoding = encoding;
      Class[] argTypes = callbackMethod.getParameterTypes();
      Class returnType = callbackMethod.getReturnType();
      fromNative = new FromNativeConverter[argTypes.length];
      if (NativeMapped.class.isAssignableFrom(returnType)) {
        toNative = NativeMappedConverter.getInstance(returnType);
      }
      else if (mapper != null) {
        toNative = mapper.getToNativeConverter(returnType);
      }
      for (int i = 0; i < fromNative.length; i++) {
        if (NativeMapped.class.isAssignableFrom(argTypes[i])) {
          fromNative[i] = new NativeMappedConverter(argTypes[i]);
        }
        else if (mapper != null) {
          fromNative[i] = mapper.getFromNativeConverter(argTypes[i]);
        }
      }
      if (!callbackMethod.isAccessible()) {
        try {
          callbackMethod.setAccessible(true);
        }
        catch (SecurityException e) {
          throw new IllegalArgumentException("Callback method is inaccessible, make sure the interface is public: " + callbackMethod);
        }
      }
    }
    
    public Callback getCallback() {
      return CallbackReference.this.getCallback();
    }
    
    private Object invokeCallback(Object[] args) {
      Class[] paramTypes = callbackMethod.getParameterTypes();
      Object[] callbackArgs = new Object[args.length];
      

      for (int i = 0; i < args.length; i++) {
        Class type = paramTypes[i];
        Object arg = args[i];
        if (fromNative[i] != null) {
          FromNativeContext context = new CallbackParameterContext(type, callbackMethod, args, i);
          
          callbackArgs[i] = fromNative[i].fromNative(arg, context);
        }
        else {
          callbackArgs[i] = convertArgument(arg, type);
        }
      }
      
      Object result = null;
      Callback cb = getCallback();
      if (cb != null) {
        try {
          result = convertResult(callbackMethod.invoke(cb, callbackArgs));
        }
        catch (IllegalArgumentException e) {
          Native.getCallbackExceptionHandler().uncaughtException(cb, e);
        }
        catch (IllegalAccessException e) {
          Native.getCallbackExceptionHandler().uncaughtException(cb, e);
        }
        catch (InvocationTargetException e) {
          Native.getCallbackExceptionHandler().uncaughtException(cb, e.getTargetException());
        }
      }
      
      for (int i = 0; i < callbackArgs.length; i++) {
        if (((callbackArgs[i] instanceof Structure)) && (!(callbackArgs[i] instanceof Structure.ByValue)))
        {
          ((Structure)callbackArgs[i]).autoWrite();
        }
      }
      
      return result;
    }
    



    public Object callback(Object[] args)
    {
      try
      {
        return invokeCallback(args);
      }
      catch (Throwable t) {
        Native.getCallbackExceptionHandler().uncaughtException(getCallback(), t); }
      return null;
    }
    



    private Object convertArgument(Object value, Class dstType)
    {
      if ((value instanceof Pointer)) {
        if (dstType == String.class) {
          value = ((Pointer)value).getString(0L, encoding);
        }
        else if (dstType == WString.class) {
          value = new WString(((Pointer)value).getWideString(0L));
        }
        else if (dstType == [Ljava.lang.String.class) {
          value = ((Pointer)value).getStringArray(0L, encoding);
        }
        else if (dstType == [Lcom.sun.jna.WString.class) {
          value = ((Pointer)value).getWideStringArray(0L);
        }
        else if (Callback.class.isAssignableFrom(dstType)) {
          value = CallbackReference.getCallback(dstType, (Pointer)value);
        }
        else if (Structure.class.isAssignableFrom(dstType))
        {

          if (Structure.ByValue.class.isAssignableFrom(dstType)) {
            Structure s = Structure.newInstance(dstType);
            byte[] buf = new byte[s.size()];
            ((Pointer)value).read(0L, buf, 0, buf.length);
            s.getPointer().write(0L, buf, 0, buf.length);
            s.read();
            value = s;
          }
          else {
            Structure s = Structure.newInstance(dstType, (Pointer)value);
            s.conditionalAutoRead();
            value = s;
          }
        }
      }
      else if (((Boolean.TYPE == dstType) || (Boolean.class == dstType)) && ((value instanceof Number)))
      {
        value = Function.valueOf(((Number)value).intValue() != 0);
      }
      return value;
    }
    
    private Object convertResult(Object value) {
      if (toNative != null) {
        value = toNative.toNative(value, new CallbackResultContext(callbackMethod));
      }
      if (value == null)
        return null;
      Class cls = value.getClass();
      if (Structure.class.isAssignableFrom(cls)) {
        if (Structure.ByValue.class.isAssignableFrom(cls)) {
          return value;
        }
        return ((Structure)value).getPointer();
      }
      if ((cls == Boolean.TYPE) || (cls == Boolean.class)) {
        return Boolean.TRUE.equals(value) ? Function.INTEGER_TRUE : Function.INTEGER_FALSE;
      }
      
      if ((cls == String.class) || (cls == WString.class)) {
        return CallbackReference.getNativeString(value, cls == WString.class);
      }
      if ((cls == [Ljava.lang.String.class) || (cls == WString.class)) {
        StringArray sa = cls == [Ljava.lang.String.class ? new StringArray((String[])value, encoding) : new StringArray((WString[])value);
        


        CallbackReference.allocations.put(value, sa);
        return sa;
      }
      if (Callback.class.isAssignableFrom(cls)) {
        return CallbackReference.getFunctionPointer((Callback)value);
      }
      return value;
    }
    
    public Class[] getParameterTypes() { return callbackMethod.getParameterTypes(); }
    
    public Class getReturnType() {
      return callbackMethod.getReturnType();
    }
  }
  

  private static class NativeFunctionHandler
    implements InvocationHandler
  {
    private final Function function;
    private final Map options;
    
    public NativeFunctionHandler(Pointer address, int callingConvention, Map options)
    {
      String encoding = (String)options.get("string-encoding");
      function = new Function(address, callingConvention, encoding);
      this.options = options;
    }
    
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
      if (Library.Handler.OBJECT_TOSTRING.equals(method)) {
        String str = "Proxy interface to " + function;
        Method m = (Method)options.get("invoking-method");
        Class cls = CallbackReference.findCallbackClass(m.getDeclaringClass());
        str = str + " (" + cls.getName() + ")";
        
        return str;
      }
      if (Library.Handler.OBJECT_HASHCODE.equals(method)) {
        return new Integer(hashCode());
      }
      if (Library.Handler.OBJECT_EQUALS.equals(method)) {
        Object o = args[0];
        if ((o != null) && (Proxy.isProxyClass(o.getClass()))) {
          return Function.valueOf(Proxy.getInvocationHandler(o) == this);
        }
        return Boolean.FALSE;
      }
      if (Function.isVarArgs(method)) {
        args = Function.concatenateVarArgs(args);
      }
      return function.invoke(method.getReturnType(), args, options);
    }
    
    public Pointer getPointer() {
      return function;
    }
  }
  


  private static boolean isAllowableNativeType(Class cls)
  {
    return (cls == Void.TYPE) || (cls == Void.class) || (cls == Boolean.TYPE) || (cls == Boolean.class) || (cls == Byte.TYPE) || (cls == Byte.class) || (cls == Short.TYPE) || (cls == Short.class) || (cls == Character.TYPE) || (cls == Character.class) || (cls == Integer.TYPE) || (cls == Integer.class) || (cls == Long.TYPE) || (cls == Long.class) || (cls == Float.TYPE) || (cls == Float.class) || (cls == Double.TYPE) || (cls == Double.class) || ((Structure.ByValue.class.isAssignableFrom(cls)) && (Structure.class.isAssignableFrom(cls))) || (Pointer.class.isAssignableFrom(cls));
  }
  



  CallbackProxy proxy;
  


  Method method;
  

  private static Pointer getNativeString(Object value, boolean wide)
  {
    if (value != null) {
      NativeString ns = new NativeString(value.toString(), wide);
      
      allocations.put(value, ns);
      return ns.getPointer();
    }
    return null;
  }
}
