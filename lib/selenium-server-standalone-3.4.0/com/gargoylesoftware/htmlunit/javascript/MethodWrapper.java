package com.gargoylesoftware.htmlunit.javascript;

import java.lang.reflect.Method;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.FunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import org.apache.commons.lang3.ArrayUtils;

























public class MethodWrapper
  extends ScriptableObject
  implements Function
{
  private final Class<?> clazz_;
  private final Method method_;
  private final int[] jsTypeTags_;
  
  MethodWrapper(String methodName, Class<?> clazz)
    throws NoSuchMethodException
  {
    this(methodName, clazz, ArrayUtils.EMPTY_CLASS_ARRAY);
  }
  







  MethodWrapper(String methodName, Class<?> clazz, Class<?>[] parameterTypes)
    throws NoSuchMethodException
  {
    clazz_ = clazz;
    method_ = clazz.getMethod(methodName, parameterTypes);
    jsTypeTags_ = new int[parameterTypes.length];
    int i = 0;
    for (Class<?> klass : parameterTypes) {
      jsTypeTags_[(i++)] = FunctionObject.getTypeTag(klass);
    }
  }
  




  public String getClassName()
  {
    return "function " + method_.getName();
  }
  




  public Object call(Context context, Scriptable scope, Scriptable thisObj, Object[] args)
  {
    if ((thisObj instanceof ScriptableWrapper)) {
      ScriptableWrapper wrapper = (ScriptableWrapper)thisObj;
      Object wrappedObject = wrapper.getWrappedObject();
      if (clazz_.isInstance(wrappedObject))
      {
        Object[] javaArgs = convertJSArgsToJavaArgs(context, scope, args);
        try {
          javaResp = method_.invoke(wrappedObject, javaArgs);
        } catch (Exception e) {
          Object javaResp;
          throw Context.reportRuntimeError("Exception calling wrapped function " + 
            method_.getName() + ": " + e.getMessage());
        }
      }
      else {
        throw buildInvalidCallException(thisObj);
      }
    }
    else
    {
      throw buildInvalidCallException(thisObj);
    }
    Object javaResp;
    Object jsResp = Context.javaToJS(javaResp, ScriptableObject.getTopLevelScope(scope));
    return jsResp;
  }
  
  private RuntimeException buildInvalidCallException(Scriptable thisObj) {
    return Context.reportRuntimeError("Function " + method_.getName() + 
      " called on incompatible object: " + thisObj);
  }
  






  Object[] convertJSArgsToJavaArgs(Context context, Scriptable scope, Object[] jsArgs)
  {
    if (jsArgs.length != jsTypeTags_.length) {
      throw Context.reportRuntimeError("Bad number of parameters for function " + method_.getName() + 
        ": expected " + jsTypeTags_.length + " got " + jsArgs.length);
    }
    Object[] javaArgs = new Object[jsArgs.length];
    int i = 0;
    for (Object object : jsArgs) {
      javaArgs[i] = FunctionObject.convertArg(context, scope, object, jsTypeTags_[(i++)]);
    }
    return javaArgs;
  }
  



  public Scriptable construct(Context context, Scriptable scope, Object[] args)
  {
    throw Context.reportRuntimeError("Function " + method_.getName() + " can't be used as a constructor");
  }
}
