package org.eclipse.jetty.websocket.common.events.annotated;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.websocket.common.util.ReflectUtils;























public class CallableMethod
{
  private static final Logger LOG = Log.getLogger(CallableMethod.class);
  protected final Class<?> pojo;
  protected final Method method;
  protected Class<?>[] paramTypes;
  
  public CallableMethod(Class<?> pojo, Method method)
  {
    Objects.requireNonNull(pojo, "Pojo cannot be null");
    Objects.requireNonNull(method, "Method cannot be null");
    this.pojo = pojo;
    this.method = method;
    paramTypes = method.getParameterTypes();
  }
  
  public Object call(Object obj, Object... args)
  {
    if ((pojo == null) || (method == null))
    {
      LOG.warn("Cannot execute call: pojo={}, method={}", new Object[] { pojo, method });
      return null;
    }
    
    if (obj == null)
    {
      String err = String.format("Cannot call %s on null object", new Object[] { method });
      LOG.warn(new RuntimeException(err));
      return null;
    }
    
    if (args.length < paramTypes.length)
    {
      throw new IllegalArgumentException("Call arguments length [" + args.length + "] must always be greater than or equal to captured args length [" + paramTypes.length + "]");
    }
    

    try
    {
      return method.invoke(obj, args);
    }
    catch (Throwable t)
    {
      String err = formatMethodCallError(args);
      throw unwrapRuntimeException(err, t);
    }
  }
  
  private RuntimeException unwrapRuntimeException(String err, Throwable t)
  {
    Throwable ret = t;
    
    while ((ret instanceof InvocationTargetException))
    {
      ret = ((InvocationTargetException)ret).getCause();
    }
    
    if ((ret instanceof RuntimeException))
    {
      return (RuntimeException)ret;
    }
    
    return new RuntimeException(err, ret);
  }
  
  public String formatMethodCallError(Object... args)
  {
    StringBuilder err = new StringBuilder();
    err.append("Cannot call method ");
    err.append(ReflectUtils.toString(pojo, method));
    err.append(" with args: [");
    
    boolean delim = false;
    for (Object arg : args)
    {
      if (delim)
      {
        err.append(", ");
      }
      if (arg == null)
      {
        err.append("<null>");
      }
      else
      {
        err.append(arg.getClass().getName());
      }
      delim = true;
    }
    err.append("]");
    return err.toString();
  }
  
  public Method getMethod()
  {
    return method;
  }
  
  public Class<?>[] getParamTypes()
  {
    return paramTypes;
  }
  
  public Class<?> getPojo()
  {
    return pojo;
  }
  

  public String toString()
  {
    return String.format("%s[%s]", new Object[] { getClass().getSimpleName(), method.toGenericString() });
  }
}
