package javax.xml.transform;

public class TransformerFactoryConfigurationError
  extends Error
{
  private Exception exception;
  
  public TransformerFactoryConfigurationError()
  {
    exception = null;
  }
  
  public TransformerFactoryConfigurationError(String paramString)
  {
    super(paramString);
    exception = null;
  }
  
  public TransformerFactoryConfigurationError(Exception paramException)
  {
    super(paramException.toString());
    exception = paramException;
  }
  
  public TransformerFactoryConfigurationError(Exception paramException, String paramString)
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
