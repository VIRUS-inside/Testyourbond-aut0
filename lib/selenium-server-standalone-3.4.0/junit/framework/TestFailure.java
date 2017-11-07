package junit.framework;

import java.io.PrintWriter;
import java.io.StringWriter;









public class TestFailure
{
  protected Test fFailedTest;
  protected Throwable fThrownException;
  
  public TestFailure(Test failedTest, Throwable thrownException)
  {
    fFailedTest = failedTest;
    fThrownException = thrownException;
  }
  


  public Test failedTest()
  {
    return fFailedTest;
  }
  


  public Throwable thrownException()
  {
    return fThrownException;
  }
  



  public String toString()
  {
    return fFailedTest + ": " + fThrownException.getMessage();
  }
  



  public String trace()
  {
    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    thrownException().printStackTrace(writer);
    return stringWriter.toString();
  }
  


  public String exceptionMessage()
  {
    return thrownException().getMessage();
  }
  




  public boolean isFailure()
  {
    return thrownException() instanceof AssertionFailedError;
  }
}
