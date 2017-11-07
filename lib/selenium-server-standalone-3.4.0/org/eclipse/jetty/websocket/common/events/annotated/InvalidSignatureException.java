package org.eclipse.jetty.websocket.common.events.annotated;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.eclipse.jetty.websocket.api.InvalidWebSocketException;
import org.eclipse.jetty.websocket.common.events.ParamList;





















public class InvalidSignatureException
  extends InvalidWebSocketException
{
  public static InvalidSignatureException build(Method method, Class<? extends Annotation> annoClass, ParamList... paramlists)
  {
    StringBuilder err = new StringBuilder();
    err.append("Invalid declaration of ");
    err.append(method);
    err.append(System.lineSeparator());
    
    err.append("Acceptable method declarations for @");
    err.append(annoClass.getSimpleName());
    err.append(" are:");
    for (ParamList validParams : paramlists)
    {
      for (Class<?>[] params : validParams)
      {
        err.append(System.lineSeparator());
        err.append("public void ").append(method.getName());
        err.append('(');
        boolean delim = false;
        for (Class<?> type : params)
        {
          if (delim)
          {
            err.append(',');
          }
          err.append(' ');
          err.append(type.getName());
          if (type.isArray())
          {
            err.append("[]");
          }
          delim = true;
        }
        err.append(')');
      }
    }
    return new InvalidSignatureException(err.toString());
  }
  
  public InvalidSignatureException(String message)
  {
    super(message);
  }
  
  public InvalidSignatureException(String message, Throwable cause)
  {
    super(message, cause);
  }
}
