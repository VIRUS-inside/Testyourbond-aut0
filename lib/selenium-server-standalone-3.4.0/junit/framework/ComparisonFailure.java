package junit.framework;



public class ComparisonFailure
  extends AssertionFailedError
{
  private static final int MAX_CONTEXT_LENGTH = 20;
  

  private static final long serialVersionUID = 1L;
  

  private String fExpected;
  

  private String fActual;
  


  public ComparisonFailure(String message, String expected, String actual)
  {
    super(message);
    fExpected = expected;
    fActual = actual;
  }
  






  public String getMessage()
  {
    return new ComparisonCompactor(20, fExpected, fActual).compact(super.getMessage());
  }
  




  public String getActual()
  {
    return fActual;
  }
  




  public String getExpected()
  {
    return fExpected;
  }
}
