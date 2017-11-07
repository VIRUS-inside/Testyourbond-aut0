package javax.xml.stream;

public class FactoryConfigurationError
  extends Error
{
  private static final long serialVersionUID = -2994412584589975744L;
  private Exception nested;
  
  public FactoryConfigurationError() {}
  
  public FactoryConfigurationError(Exception paramException)
  {
    nested = paramException;
  }
  
  public FactoryConfigurationError(Exception paramException, String paramString)
  {
    super(paramString);
    nested = paramException;
  }
  
  public FactoryConfigurationError(String paramString)
  {
    super(paramString);
  }
  
  public FactoryConfigurationError(String paramString, Exception paramException)
  {
    super(paramString);
    nested = paramException;
  }
  
  public Exception getException()
  {
    return nested;
  }
  
  public String getMessage()
  {
    String str = super.getMessage();
    if (str != null) {
      return str;
    }
    if (nested != null)
    {
      str = nested.getMessage();
      if (str == null) {
        str = nested.getClass().toString();
      }
    }
    return str;
  }
}
