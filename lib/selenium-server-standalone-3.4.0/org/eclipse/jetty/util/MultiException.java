package org.eclipse.jetty.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

























public class MultiException
  extends Exception
{
  private List<Throwable> nested;
  
  public MultiException()
  {
    super("Multiple exceptions");
  }
  

  public void add(Throwable e)
  {
    if (e == null) {
      throw new IllegalArgumentException();
    }
    if (nested == null)
    {
      initCause(e);
      nested = new ArrayList();
    }
    else {
      addSuppressed(e);
    }
    if ((e instanceof MultiException))
    {
      MultiException me = (MultiException)e;
      nested.addAll(nested);
    }
    else {
      nested.add(e);
    }
  }
  
  public int size()
  {
    return nested == null ? 0 : nested.size();
  }
  

  public List<Throwable> getThrowables()
  {
    if (nested == null)
      return Collections.emptyList();
    return nested;
  }
  

  public Throwable getThrowable(int i)
  {
    return (Throwable)nested.get(i);
  }
  







  public void ifExceptionThrow()
    throws Exception
  {
    if (nested == null) {
      return;
    }
    switch (nested.size())
    {
    case 0: 
      break;
    case 1: 
      Throwable th = (Throwable)nested.get(0);
      if ((th instanceof Error))
        throw ((Error)th);
      if ((th instanceof Exception))
        throw ((Exception)th);
      break; }
    throw this;
  }
  










  public void ifExceptionThrowRuntime()
    throws Error
  {
    if (nested == null) {
      return;
    }
    switch (nested.size())
    {
    case 0: 
      break;
    case 1: 
      Throwable th = (Throwable)nested.get(0);
      if ((th instanceof Error))
        throw ((Error)th);
      if ((th instanceof RuntimeException)) {
        throw ((RuntimeException)th);
      }
      throw new RuntimeException(th);
    default: 
      throw new RuntimeException(this);
    }
    
  }
  






  public void ifExceptionThrowMulti()
    throws MultiException
  {
    if (nested == null) {
      return;
    }
    if (nested.size() > 0) {
      throw this;
    }
  }
  

  public String toString()
  {
    StringBuilder str = new StringBuilder();
    str.append(MultiException.class.getSimpleName());
    if ((nested == null) || (nested.size() <= 0)) {
      str.append("[]");
    } else {
      str.append(nested);
    }
    return str.toString();
  }
}
