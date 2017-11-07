package net.sourceforge.htmlunit.corejs.javascript;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;

public class NativeJavaObject implements Scriptable, Wrapper, java.io.Serializable
{
  static final long serialVersionUID = -6948590651130498591L;
  private static final int JSTYPE_UNDEFINED = 0;
  private static final int JSTYPE_NULL = 1;
  private static final int JSTYPE_BOOLEAN = 2;
  private static final int JSTYPE_NUMBER = 3;
  private static final int JSTYPE_STRING = 4;
  private static final int JSTYPE_JAVA_CLASS = 5;
  private static final int JSTYPE_JAVA_OBJECT = 6;
  private static final int JSTYPE_JAVA_ARRAY = 7;
  private static final int JSTYPE_OBJECT = 8;
  static final byte CONVERSION_TRIVIAL = 1;
  static final byte CONVERSION_NONTRIVIAL = 0;
  static final byte CONVERSION_NONE = 99;
  protected Scriptable prototype;
  protected Scriptable parent;
  protected transient Object javaObject;
  protected transient Class<?> staticType;
  protected transient JavaMembers members;
  private transient java.util.Map<String, FieldAndMethods> fieldAndMethods;
  protected transient boolean isAdapter;
  
  public NativeJavaObject() {}
  
  public NativeJavaObject(Scriptable scope, Object javaObject, Class<?> staticType)
  {
    this(scope, javaObject, staticType, false);
  }
  
  public NativeJavaObject(Scriptable scope, Object javaObject, Class<?> staticType, boolean isAdapter)
  {
    parent = scope;
    this.javaObject = javaObject;
    this.staticType = staticType;
    this.isAdapter = isAdapter;
    initMembers();
  }
  
  protected void initMembers() { Class<?> dynamicType;
    Class<?> dynamicType;
    if (javaObject != null) {
      dynamicType = javaObject.getClass();
    } else {
      dynamicType = staticType;
    }
    members = JavaMembers.lookupClass(parent, dynamicType, staticType, isAdapter);
    
    fieldAndMethods = members.getFieldAndMethodsObjects(this, javaObject, false);
  }
  
  public boolean has(String name, Scriptable start)
  {
    return members.has(name, false);
  }
  
  public boolean has(int index, Scriptable start) {
    return false;
  }
  
  public Object get(String name, Scriptable start) {
    if (fieldAndMethods != null) {
      Object result = fieldAndMethods.get(name);
      if (result != null) {
        return result;
      }
    }
    

    return members.get(this, name, javaObject, false);
  }
  
  public Object get(int index, Scriptable start) {
    throw members.reportMemberNotFound(Integer.toString(index));
  }
  


  public void put(String name, Scriptable start, Object value)
  {
    if ((prototype == null) || (members.has(name, false))) {
      members.put(this, name, javaObject, value, false);
    } else
      prototype.put(name, prototype, value);
  }
  
  public void put(int index, Scriptable start, Object value) {
    throw members.reportMemberNotFound(Integer.toString(index));
  }
  
  public boolean hasInstance(Scriptable value)
  {
    return false;
  }
  

  public void delete(String name) {}
  
  public void delete(int index) {}
  
  public Scriptable getPrototype()
  {
    if ((prototype == null) && ((javaObject instanceof String))) {
      return TopLevel.getBuiltinPrototype(
        ScriptableObject.getTopLevelScope(parent), TopLevel.Builtins.String);
    }
    
    return prototype;
  }
  


  public void setPrototype(Scriptable m)
  {
    prototype = m;
  }
  


  public Scriptable getParentScope()
  {
    return parent;
  }
  


  public void setParentScope(Scriptable m)
  {
    parent = m;
  }
  
  public Object[] getIds() {
    return members.getIds(false);
  }
  





  @Deprecated
  public static Object wrap(Scriptable scope, Object obj, Class<?> staticType)
  {
    Context cx = Context.getContext();
    return cx.getWrapFactory().wrap(cx, scope, obj, staticType);
  }
  
  public Object unwrap() {
    return javaObject;
  }
  
  public String getClassName() {
    return "JavaObject";
  }
  
  public Object getDefaultValue(Class<?> hint)
  {
    if ((hint == null) && 
      ((javaObject instanceof Boolean)))
      hint = ScriptRuntime.BooleanClass;
    Object value;
    Object value;
    if ((hint == null) || (hint == ScriptRuntime.StringClass)) {
      value = javaObject.toString();
    } else {
      String converterName;
      if (hint == ScriptRuntime.BooleanClass) {
        converterName = "booleanValue"; } else { String converterName;
        if (hint == ScriptRuntime.NumberClass) {
          converterName = "doubleValue";
        } else
          throw Context.reportRuntimeError0("msg.default.value"); }
      String converterName;
      Object converterObject = get(converterName, this);
      Object value; if ((converterObject instanceof Function)) {
        Function f = (Function)converterObject;
        value = f.call(Context.getContext(), f.getParentScope(), this, ScriptRuntime.emptyArgs);
      } else {
        Object value;
        if ((hint == ScriptRuntime.NumberClass) && ((javaObject instanceof Boolean)))
        {
          boolean b = ((Boolean)javaObject).booleanValue();
          value = ScriptRuntime.wrapNumber(b ? 1.0D : 0.0D);
        } else {
          value = javaObject.toString();
        }
      }
    }
    return value;
  }
  




  public static boolean canConvert(Object fromObj, Class<?> to)
  {
    int weight = getConversionWeight(fromObj, to);
    
    return weight < 99;
  }
  





















  static int getConversionWeight(Object fromObj, Class<?> to)
  {
    int fromCode = getJSTypeCode(fromObj);
    
    switch (fromCode)
    {
    case 0: 
      if ((to == ScriptRuntime.StringClass) || (to == ScriptRuntime.ObjectClass))
      {
        return 1;
      }
      
      break;
    case 1: 
      if (!to.isPrimitive()) {
        return 1;
      }
      

      break;
    case 2: 
      if (to == Boolean.TYPE)
        return 1;
      if (to == ScriptRuntime.BooleanClass)
        return 2;
      if (to == ScriptRuntime.ObjectClass)
        return 3;
      if (to == ScriptRuntime.StringClass) {
        return 4;
      }
      
      break;
    case 3: 
      if (to.isPrimitive()) {
        if (to == Double.TYPE)
          return 1;
        if (to != Boolean.TYPE) {
          return 1 + getSizeRank(to);
        }
      } else {
        if (to == ScriptRuntime.StringClass)
        {
          return 9; }
        if (to == ScriptRuntime.ObjectClass)
          return 10;
        if (ScriptRuntime.NumberClass.isAssignableFrom(to))
        {
          return 2;
        }
      }
      
      break;
    case 4: 
      if (to == ScriptRuntime.StringClass)
        return 1;
      if (to.isInstance(fromObj))
        return 2;
      if (to.isPrimitive()) {
        if (to == Character.TYPE)
          return 3;
        if (to != Boolean.TYPE) {
          return 4;
        }
      }
      
      break;
    case 5: 
      if (to == ScriptRuntime.ClassClass)
        return 1;
      if (to == ScriptRuntime.ObjectClass)
        return 3;
      if (to == ScriptRuntime.StringClass) {
        return 4;
      }
      
      break;
    case 6: 
    case 7: 
      Object javaObj = fromObj;
      if ((javaObj instanceof Wrapper)) {
        javaObj = ((Wrapper)javaObj).unwrap();
      }
      if (to.isInstance(javaObj)) {
        return 0;
      }
      if (to == ScriptRuntime.StringClass)
        return 2;
      if ((to.isPrimitive()) && (to != Boolean.TYPE)) {
        return fromCode == 7 ? 99 : 2 + 
          getSizeRank(to);
      }
      

      break;
    case 8: 
      if ((to != ScriptRuntime.ObjectClass) && (to.isInstance(fromObj)))
      {
        return 1;
      }
      if (to.isArray()) {
        if ((fromObj instanceof NativeArray))
        {


          return 2; }
      } else {
        if (to == ScriptRuntime.ObjectClass)
          return 3;
        if (to == ScriptRuntime.StringClass)
          return 4;
        if (to == ScriptRuntime.DateClass) {
          if ((fromObj instanceof NativeDate))
          {
            return 1; }
        } else {
          if (to.isInterface()) {
            if (((fromObj instanceof NativeObject)) || ((fromObj instanceof NativeFunction)))
            {

              return 1;
            }
            return 12; }
          if ((to.isPrimitive()) && (to != Boolean.TYPE))
            return 4 + getSizeRank(to);
        }
      }
      break;
    }
    return 99;
  }
  
  static int getSizeRank(Class<?> aType) {
    if (aType == Double.TYPE)
      return 1;
    if (aType == Float.TYPE)
      return 2;
    if (aType == Long.TYPE)
      return 3;
    if (aType == Integer.TYPE)
      return 4;
    if (aType == Short.TYPE)
      return 5;
    if (aType == Character.TYPE)
      return 6;
    if (aType == Byte.TYPE)
      return 7;
    if (aType == Boolean.TYPE) {
      return 99;
    }
    return 8;
  }
  
  private static int getJSTypeCode(Object value)
  {
    if (value == null)
      return 1;
    if (value == Undefined.instance)
      return 0;
    if ((value instanceof CharSequence))
      return 4;
    if ((value instanceof Number))
      return 3;
    if ((value instanceof Boolean))
      return 2;
    if ((value instanceof Scriptable)) {
      if ((value instanceof NativeJavaClass))
        return 5;
      if ((value instanceof NativeJavaArray))
        return 7;
      if ((value instanceof Wrapper)) {
        return 6;
      }
      return 8;
    }
    if ((value instanceof Class)) {
      return 5;
    }
    Class<?> valueClass = value.getClass();
    if (valueClass.isArray()) {
      return 7;
    }
    return 6;
  }
  









  @Deprecated
  public static Object coerceType(Class<?> type, Object value)
  {
    return coerceTypeImpl(type, value);
  }
  



  static Object coerceTypeImpl(Class<?> type, Object value)
  {
    if ((value != null) && (value.getClass() == type)) {
      return value;
    }
    
    switch (getJSTypeCode(value))
    {

    case 1: 
      if (type.isPrimitive()) {
        reportConversionError(value, type);
      }
      return null;
    
    case 0: 
      if ((type == ScriptRuntime.StringClass) || (type == ScriptRuntime.ObjectClass))
      {
        return "undefined";
      }
      reportConversionError("undefined", type);
      
      break;
    

    case 2: 
      if ((type == Boolean.TYPE) || (type == ScriptRuntime.BooleanClass) || (type == ScriptRuntime.ObjectClass))
      {
        return value; }
      if (type == ScriptRuntime.StringClass) {
        return value.toString();
      }
      reportConversionError(value, type);
      
      break;
    
    case 3: 
      if (type == ScriptRuntime.StringClass)
        return ScriptRuntime.toString(value);
      if (type == ScriptRuntime.ObjectClass)
        return coerceToNumber(Double.TYPE, value);
      if (((type.isPrimitive()) && (type != Boolean.TYPE)) || 
        (ScriptRuntime.NumberClass.isAssignableFrom(type))) {
        return coerceToNumber(type, value);
      }
      reportConversionError(value, type);
      
      break;
    
    case 4: 
      if ((type == ScriptRuntime.StringClass) || (type.isInstance(value)))
        return value.toString();
      if ((type == Character.TYPE) || (type == ScriptRuntime.CharacterClass))
      {




        if (((CharSequence)value).length() == 1) {
          return Character.valueOf(((CharSequence)value).charAt(0));
        }
        return coerceToNumber(type, value);
      }
      if (((type.isPrimitive()) && (type != Boolean.TYPE)) || 
        (ScriptRuntime.NumberClass.isAssignableFrom(type))) {
        return coerceToNumber(type, value);
      }
      reportConversionError(value, type);
      
      break;
    
    case 5: 
      if ((value instanceof Wrapper)) {
        value = ((Wrapper)value).unwrap();
      }
      
      if ((type == ScriptRuntime.ClassClass) || (type == ScriptRuntime.ObjectClass))
      {
        return value; }
      if (type == ScriptRuntime.StringClass) {
        return value.toString();
      }
      reportConversionError(value, type);
      
      break;
    
    case 6: 
    case 7: 
      if ((value instanceof Wrapper)) {
        value = ((Wrapper)value).unwrap();
      }
      if (type.isPrimitive()) {
        if (type == Boolean.TYPE) {
          reportConversionError(value, type);
        }
        return coerceToNumber(type, value);
      }
      if (type == ScriptRuntime.StringClass) {
        return value.toString();
      }
      if (type.isInstance(value)) {
        return value;
      }
      reportConversionError(value, type);
      


      break;
    
    case 8: 
      if (type == ScriptRuntime.StringClass)
        return ScriptRuntime.toString(value);
      if (type.isPrimitive()) {
        if (type == Boolean.TYPE) {
          reportConversionError(value, type);
        }
        return coerceToNumber(type, value); }
      if (type.isInstance(value))
        return value;
      if ((type == ScriptRuntime.DateClass) && ((value instanceof NativeDate)))
      {
        double time = ((NativeDate)value).getJSTimeValue();
        
        return new java.util.Date(time); }
      if ((type.isArray()) && ((value instanceof NativeArray)))
      {

        NativeArray array = (NativeArray)value;
        long length = array.getLength();
        Class<?> arrayType = type.getComponentType();
        Object Result = java.lang.reflect.Array.newInstance(arrayType, (int)length);
        for (int i = 0; i < length; i++) {
          try {
            java.lang.reflect.Array.set(Result, i, 
              coerceTypeImpl(arrayType, array.get(i, array)));
          } catch (EvaluatorException ee) {
            reportConversionError(value, type);
          }
        }
        
        return Result; }
      if ((value instanceof Wrapper)) {
        value = ((Wrapper)value).unwrap();
        if (type.isInstance(value))
          return value;
        reportConversionError(value, type);
      } else { if ((type.isInterface()) && (((value instanceof NativeObject)) || ((value instanceof NativeFunction))))
        {


          return createInterfaceAdapter(type, (ScriptableObject)value);
        }
        reportConversionError(value, type);
      }
      break;
    }
    
    return value;
  }
  






  protected static Object createInterfaceAdapter(Class<?> type, ScriptableObject so)
  {
    Object key = Kit.makeHashKeyFromPair(COERCED_INTERFACE_KEY, type);
    Object old = so.getAssociatedValue(key);
    if (old != null)
    {
      return old;
    }
    Context cx = Context.getContext();
    Object glue = InterfaceAdapter.create(cx, type, so);
    
    glue = so.associateValue(key, glue);
    return glue;
  }
  
  private static Object coerceToNumber(Class<?> type, Object value) {
    Class<?> valueClass = value.getClass();
    

    if ((type == Character.TYPE) || (type == ScriptRuntime.CharacterClass)) {
      if (valueClass == ScriptRuntime.CharacterClass) {
        return value;
      }
      return Character.valueOf(
        (char)(int)toInteger(value, ScriptRuntime.CharacterClass, 0.0D, 65535.0D));
    }
    


    if ((type == ScriptRuntime.ObjectClass) || (type == ScriptRuntime.DoubleClass) || (type == Double.TYPE))
    {
      return valueClass == ScriptRuntime.DoubleClass ? value : new Double(
        toDouble(value));
    }
    
    if ((type == ScriptRuntime.FloatClass) || (type == Float.TYPE)) {
      if (valueClass == ScriptRuntime.FloatClass) {
        return value;
      }
      double number = toDouble(value);
      if ((Double.isInfinite(number)) || (Double.isNaN(number)) || (number == 0.0D))
      {
        return new Float((float)number);
      }
      double absNumber = Math.abs(number);
      if (absNumber < 1.401298464324817E-45D)
        return new Float(number > 0.0D ? 0.0D : -0.0D);
      if (absNumber > 3.4028234663852886E38D) {
        return new Float(number > 0.0D ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY);
      }
      

      return new Float((float)number);
    }
    




    if ((type == ScriptRuntime.IntegerClass) || (type == Integer.TYPE)) {
      if (valueClass == ScriptRuntime.IntegerClass) {
        return value;
      }
      return Integer.valueOf(
        (int)toInteger(value, ScriptRuntime.IntegerClass, -2.147483648E9D, 2.147483647E9D));
    }
    


    if ((type == ScriptRuntime.LongClass) || (type == Long.TYPE)) {
      if (valueClass == ScriptRuntime.LongClass) {
        return value;
      }
      






      double max = Double.longBitsToDouble(4890909195324358655L);
      double min = Double.longBitsToDouble(-4332462841530417152L);
      return Long.valueOf(
        toInteger(value, ScriptRuntime.LongClass, min, max));
    }
    

    if ((type == ScriptRuntime.ShortClass) || (type == Short.TYPE)) {
      if (valueClass == ScriptRuntime.ShortClass) {
        return value;
      }
      return Short.valueOf(
        (short)(int)toInteger(value, ScriptRuntime.ShortClass, -32768.0D, 32767.0D));
    }
    


    if ((type == ScriptRuntime.ByteClass) || (type == Byte.TYPE)) {
      if (valueClass == ScriptRuntime.ByteClass) {
        return value;
      }
      return Byte.valueOf(
        (byte)(int)toInteger(value, ScriptRuntime.ByteClass, -128.0D, 127.0D));
    }
    


    return new Double(toDouble(value));
  }
  
  private static double toDouble(Object value) {
    if ((value instanceof Number))
      return ((Number)value).doubleValue();
    if ((value instanceof String))
      return ScriptRuntime.toNumber((String)value);
    if ((value instanceof Scriptable)) {
      if ((value instanceof Wrapper))
      {
        return toDouble(((Wrapper)value).unwrap());
      }
      return ScriptRuntime.toNumber(value);
    }
    Method meth;
    try
    {
      meth = value.getClass().getMethod("doubleValue", (Class[])null);
    } catch (NoSuchMethodException e) {
      Method meth;
      meth = null;
    } catch (SecurityException e) { Method meth;
      meth = null;
    }
    if (meth != null) {
      try {
        return 
          ((Number)meth.invoke(value, (Object[])null)).doubleValue();
      }
      catch (IllegalAccessException e) {
        reportConversionError(value, Double.TYPE);
      }
      catch (java.lang.reflect.InvocationTargetException e) {
        reportConversionError(value, Double.TYPE);
      }
    }
    return ScriptRuntime.toNumber(value.toString());
  }
  

  private static long toInteger(Object value, Class<?> type, double min, double max)
  {
    double d = toDouble(value);
    
    if ((Double.isInfinite(d)) || (Double.isNaN(d)))
    {
      reportConversionError(ScriptRuntime.toString(value), type);
    }
    
    if (d > 0.0D) {
      d = Math.floor(d);
    } else {
      d = Math.ceil(d);
    }
    
    if ((d < min) || (d > max))
    {
      reportConversionError(ScriptRuntime.toString(value), type);
    }
    return d;
  }
  

  static void reportConversionError(Object value, Class<?> type)
  {
    throw Context.reportRuntimeError2("msg.conversion.not.allowed", 
      String.valueOf(value), JavaMembers.javaSignature(type));
  }
  
  private void writeObject(ObjectOutputStream out) throws java.io.IOException {
    out.defaultWriteObject();
    
    out.writeBoolean(isAdapter);
    if (isAdapter) {
      if (adapter_writeAdapterObject == null) {
        throw new java.io.IOException();
      }
      Object[] args = { javaObject, out };
      try {
        adapter_writeAdapterObject.invoke(null, args);
      } catch (Exception ex) {
        throw new java.io.IOException();
      }
    } else {
      out.writeObject(javaObject);
    }
    
    if (staticType != null) {
      out.writeObject(staticType.getClass().getName());
    } else {
      out.writeObject(null);
    }
  }
  
  private void readObject(ObjectInputStream in) throws java.io.IOException, ClassNotFoundException
  {
    in.defaultReadObject();
    
    isAdapter = in.readBoolean();
    if (isAdapter) {
      if (adapter_readAdapterObject == null)
        throw new ClassNotFoundException();
      Object[] args = { this, in };
      try {
        javaObject = adapter_readAdapterObject.invoke(null, args);
      } catch (Exception ex) {
        throw new java.io.IOException();
      }
    } else {
      javaObject = in.readObject();
    }
    
    String className = (String)in.readObject();
    if (className != null) {
      staticType = Class.forName(className);
    } else {
      staticType = null;
    }
    
    initMembers();
  }
  

















  private static final Object COERCED_INTERFACE_KEY = "Coerced Interface";
  private static Method adapter_writeAdapterObject;
  private static Method adapter_readAdapterObject;
  
  static
  {
    Class<?>[] sig2 = new Class[2];
    Class<?> cl = Kit.classOrNull("net.sourceforge.htmlunit.corejs.javascript.JavaAdapter");
    
    if (cl != null) {
      try {
        sig2[0] = ScriptRuntime.ObjectClass;
        sig2[1] = Kit.classOrNull("java.io.ObjectOutputStream");
        adapter_writeAdapterObject = cl.getMethod("writeAdapterObject", sig2);
        

        sig2[0] = ScriptRuntime.ScriptableClass;
        sig2[1] = Kit.classOrNull("java.io.ObjectInputStream");
        adapter_readAdapterObject = cl.getMethod("readAdapterObject", sig2);
      }
      catch (NoSuchMethodException e)
      {
        adapter_writeAdapterObject = null;
        adapter_readAdapterObject = null;
      }
    }
  }
}
