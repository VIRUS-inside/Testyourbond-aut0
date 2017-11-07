package javax.xml.parsers;

public class FactoryConfigurationError
  extends Error
{
  private Exception exception;
  
  public FactoryConfigurationError()
  {
    exception = null;
  }
  
  public FactoryConfigurationError(String paramString)
  {
    super(paramString);
    exception = null;
  }
  
  public FactoryConfigurationError(Exception paramException)
  {
    super(paramException.toString());
    exception = paramException;
  }
  
  public FactoryConfigurationError(Exception paramException, String paramString)
  {
    super(paramString);
    exception = paramException;
  }
  
  public String getMessage()
  {
    String str = super.getMessage();
    if ((str == null) && (exception != null)) {
      return exception.getMessage();
    }
    return str;
  }
  
  public Exception getException()
  {
    return exception;
  }
}
