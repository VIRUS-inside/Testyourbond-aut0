package net.sourceforge.htmlunit.corejs.javascript.optimizer;

import net.sourceforge.htmlunit.corejs.javascript.Callable;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Script;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

public final class OptRuntime extends ScriptRuntime
{
  public static final Double zeroObj = new Double(0.0D);
  public static final Double oneObj = new Double(1.0D);
  public static final Double minusOneObj = new Double(-1.0D);
  

  public OptRuntime() {}
  
  public static Object call0(Callable fun, Scriptable thisObj, Context cx, Scriptable scope)
  {
    return fun.call(cx, scope, thisObj, ScriptRuntime.emptyArgs);
  }
  



  public static Object call1(Callable fun, Scriptable thisObj, Object arg0, Context cx, Scriptable scope)
  {
    return fun.call(cx, scope, thisObj, new Object[] { arg0 });
  }
  



  public static Object call2(Callable fun, Scriptable thisObj, Object arg0, Object arg1, Context cx, Scriptable scope)
  {
    return fun.call(cx, scope, thisObj, new Object[] { arg0, arg1 });
  }
  



  public static Object callN(Callable fun, Scriptable thisObj, Object[] args, Context cx, Scriptable scope)
  {
    return fun.call(cx, scope, thisObj, args);
  }
  



  public static Object callName(Object[] args, String name, Context cx, Scriptable scope)
  {
    Callable f = getNameFunctionAndThis(name, cx, scope);
    Scriptable thisObj = lastStoredScriptable(cx);
    return f.call(cx, scope, thisObj, args);
  }
  


  public static Object callName0(String name, Context cx, Scriptable scope)
  {
    Callable f = getNameFunctionAndThis(name, cx, scope);
    Scriptable thisObj = lastStoredScriptable(cx);
    return f.call(cx, scope, thisObj, ScriptRuntime.emptyArgs);
  }
  



  public static Object callProp0(Object value, String property, Context cx, Scriptable scope)
  {
    Callable f = getPropFunctionAndThis(value, property, cx, scope);
    Scriptable thisObj = lastStoredScriptable(cx);
    return f.call(cx, scope, thisObj, ScriptRuntime.emptyArgs);
  }
  
  public static Object add(Object val1, double val2) {
    if ((val1 instanceof Scriptable))
      val1 = ((Scriptable)val1).getDefaultValue(null);
    if (!(val1 instanceof CharSequence))
      return wrapDouble(toNumber(val1) + val2);
    return new net.sourceforge.htmlunit.corejs.javascript.ConsString((CharSequence)val1, toString(val2));
  }
  
  public static Object add(double val1, Object val2) {
    if ((val2 instanceof Scriptable))
      val2 = ((Scriptable)val2).getDefaultValue(null);
    if (!(val2 instanceof CharSequence))
      return wrapDouble(toNumber(val2) + val1);
    return new net.sourceforge.htmlunit.corejs.javascript.ConsString(toString(val1), (CharSequence)val2);
  }
  





  @Deprecated
  public static Object elemIncrDecr(Object obj, double index, Context cx, int incrDecrMask)
  {
    return elemIncrDecr(obj, index, cx, getTopCallScope(cx), incrDecrMask);
  }
  
  public static Object elemIncrDecr(Object obj, double index, Context cx, Scriptable scope, int incrDecrMask)
  {
    return ScriptRuntime.elemIncrDecr(obj, new Double(index), cx, scope, incrDecrMask);
  }
  
  public static Object[] padStart(Object[] currentArgs, int count)
  {
    Object[] result = new Object[currentArgs.length + count];
    System.arraycopy(currentArgs, 0, result, count, currentArgs.length);
    return result;
  }
  
  public static void initFunction(net.sourceforge.htmlunit.corejs.javascript.NativeFunction fn, int functionType, Scriptable scope, Context cx)
  {
    ScriptRuntime.initFunction(cx, scope, fn, functionType, false);
  }
  


  public static Object callSpecial(Context cx, Callable fun, Scriptable thisObj, Object[] args, Scriptable scope, Scriptable callerThis, int callType, String fileName, int lineNumber)
  {
    return ScriptRuntime.callSpecial(cx, fun, thisObj, args, scope, callerThis, callType, fileName, lineNumber);
  }
  

  public static Object newObjectSpecial(Context cx, Object fun, Object[] args, Scriptable scope, Scriptable callerThis, int callType)
  {
    return ScriptRuntime.newSpecial(cx, fun, args, scope, callType);
  }
  
  public static Double wrapDouble(double num) {
    if (num == 0.0D) {
      if (1.0D / num > 0.0D)
      {
        return zeroObj; }
    } else {
      if (num == 1.0D)
        return oneObj;
      if (num == -1.0D)
        return minusOneObj;
      if (num != num)
        return NaNobj;
    }
    return new Double(num);
  }
  
  static String encodeIntArray(int[] array)
  {
    if (array == null) {
      return null;
    }
    int n = array.length;
    char[] buffer = new char[1 + n * 2];
    buffer[0] = '\001';
    for (int i = 0; i != n; i++) {
      int value = array[i];
      int shift = 1 + i * 2;
      buffer[shift] = ((char)(value >>> 16));
      buffer[(shift + 1)] = ((char)value);
    }
    return new String(buffer);
  }
  
  private static int[] decodeIntArray(String str, int arraySize)
  {
    if (arraySize == 0) {
      if (str != null)
        throw new IllegalArgumentException();
      return null;
    }
    if ((str.length() != 1 + arraySize * 2) && (str.charAt(0) != '\001')) {
      throw new IllegalArgumentException();
    }
    int[] array = new int[arraySize];
    for (int i = 0; i != arraySize; i++) {
      int shift = 1 + i * 2;
      array[i] = (str.charAt(shift) << '\020' | str.charAt(shift + 1));
    }
    return array;
  }
  
  public static Scriptable newArrayLiteral(Object[] objects, String encodedInts, int skipCount, Context cx, Scriptable scope)
  {
    int[] skipIndexces = decodeIntArray(encodedInts, skipCount);
    return newArrayLiteral(objects, skipIndexces, cx, scope);
  }
  
  public static void main(final Script script, String[] args) {
    net.sourceforge.htmlunit.corejs.javascript.ContextFactory.getGlobal().call(new net.sourceforge.htmlunit.corejs.javascript.ContextAction() {
      public Object run(Context cx) {
        net.sourceforge.htmlunit.corejs.javascript.ScriptableObject global = ScriptRuntime.getGlobal(cx);
        


        Object[] argsCopy = new Object[val$args.length];
        System.arraycopy(val$args, 0, argsCopy, 0, val$args.length);
        Scriptable argsObj = cx.newArray(global, argsCopy);
        global.defineProperty("arguments", argsObj, 2);
        
        script.exec(cx, global);
        return null;
      }
    });
  }
  
  public static void throwStopIteration(Object obj)
  {
    throw new net.sourceforge.htmlunit.corejs.javascript.JavaScriptException(net.sourceforge.htmlunit.corejs.javascript.NativeIterator.getStopIterationObject((Scriptable)obj), "", 0);
  }
  
  public static Scriptable createNativeGenerator(net.sourceforge.htmlunit.corejs.javascript.NativeFunction funObj, Scriptable scope, Scriptable thisObj, int maxLocals, int maxStack)
  {
    return new net.sourceforge.htmlunit.corejs.javascript.NativeGenerator(scope, funObj, new GeneratorState(thisObj, maxLocals, maxStack));
  }
  
  public static Object[] getGeneratorStackState(Object obj)
  {
    GeneratorState rgs = (GeneratorState)obj;
    if (stackState == null)
      stackState = new Object[maxStack];
    return stackState;
  }
  
  public static Object[] getGeneratorLocalsState(Object obj) {
    GeneratorState rgs = (GeneratorState)obj;
    if (localsState == null)
      localsState = new Object[maxLocals];
    return localsState;
  }
  

  public static class GeneratorState
  {
    static final String CLASS_NAME = "net/sourceforge/htmlunit/corejs/javascript/optimizer/OptRuntime$GeneratorState";
    public int resumptionPoint;
    static final String resumptionPoint_NAME = "resumptionPoint";
    static final String resumptionPoint_TYPE = "I";
    public Scriptable thisObj;
    static final String thisObj_NAME = "thisObj";
    static final String thisObj_TYPE = "Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;";
    Object[] stackState;
    Object[] localsState;
    int maxLocals;
    int maxStack;
    
    GeneratorState(Scriptable thisObj, int maxLocals, int maxStack)
    {
      this.thisObj = thisObj;
      this.maxLocals = maxLocals;
      this.maxStack = maxStack;
    }
  }
}
