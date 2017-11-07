package org.eclipse.jetty.websocket.common.events.annotated;

import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Method;
import org.eclipse.jetty.websocket.api.Session;






















public class OptionalSessionCallableMethod
  extends CallableMethod
{
  private final boolean wantsSession;
  private final boolean streaming;
  
  public OptionalSessionCallableMethod(Class<?> pojo, Method method)
  {
    super(pojo, method);
    
    boolean foundConnection = false;
    boolean foundStreaming = false;
    
    if (paramTypes != null)
    {
      for (Class<?> paramType : paramTypes)
      {
        if (Session.class.isAssignableFrom(paramType))
        {
          foundConnection = true;
        }
        if ((Reader.class.isAssignableFrom(paramType)) || (InputStream.class.isAssignableFrom(paramType)))
        {
          foundStreaming = true;
        }
      }
    }
    
    wantsSession = foundConnection;
    streaming = foundStreaming;
  }
  
  public void call(Object obj, Session connection, Object... args)
  {
    if (wantsSession)
    {
      Object[] fullArgs = new Object[args.length + 1];
      fullArgs[0] = connection;
      System.arraycopy(args, 0, fullArgs, 1, args.length);
      call(obj, fullArgs);
    }
    else
    {
      call(obj, args);
    }
  }
  
  public boolean isSessionAware()
  {
    return wantsSession;
  }
  
  public boolean isStreaming()
  {
    return streaming;
  }
  

  public String toString()
  {
    return String.format("%s[%s]", new Object[] { getClass().getSimpleName(), method.toGenericString() });
  }
}
