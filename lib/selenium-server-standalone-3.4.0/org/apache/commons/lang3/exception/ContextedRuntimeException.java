package org.apache.commons.lang3.exception;

import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;






















































































public class ContextedRuntimeException
  extends RuntimeException
  implements ExceptionContext
{
  private static final long serialVersionUID = 20110706L;
  private final ExceptionContext exceptionContext;
  
  public ContextedRuntimeException()
  {
    exceptionContext = new DefaultExceptionContext();
  }
  






  public ContextedRuntimeException(String message)
  {
    super(message);
    exceptionContext = new DefaultExceptionContext();
  }
  






  public ContextedRuntimeException(Throwable cause)
  {
    super(cause);
    exceptionContext = new DefaultExceptionContext();
  }
  







  public ContextedRuntimeException(String message, Throwable cause)
  {
    super(message, cause);
    exceptionContext = new DefaultExceptionContext();
  }
  






  public ContextedRuntimeException(String message, Throwable cause, ExceptionContext context)
  {
    super(message, cause);
    if (context == null) {
      context = new DefaultExceptionContext();
    }
    exceptionContext = context;
  }
  














  public ContextedRuntimeException addContextValue(String label, Object value)
  {
    exceptionContext.addContextValue(label, value);
    return this;
  }
  













  public ContextedRuntimeException setContextValue(String label, Object value)
  {
    exceptionContext.setContextValue(label, value);
    return this;
  }
  



  public List<Object> getContextValues(String label)
  {
    return exceptionContext.getContextValues(label);
  }
  



  public Object getFirstContextValue(String label)
  {
    return exceptionContext.getFirstContextValue(label);
  }
  



  public List<Pair<String, Object>> getContextEntries()
  {
    return exceptionContext.getContextEntries();
  }
  



  public Set<String> getContextLabels()
  {
    return exceptionContext.getContextLabels();
  }
  






  public String getMessage()
  {
    return getFormattedExceptionMessage(super.getMessage());
  }
  






  public String getRawMessage()
  {
    return super.getMessage();
  }
  



  public String getFormattedExceptionMessage(String baseMessage)
  {
    return exceptionContext.getFormattedExceptionMessage(baseMessage);
  }
}
