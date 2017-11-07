package javax.xml.xpath;

import java.io.PrintStream;
import java.io.PrintWriter;

public class XPathException
  extends Exception
{
  private final Throwable cause;
  private static final long serialVersionUID = -1837080260374986980L;
  
  public XPathException(String paramString)
  {
    super(paramString);
    if (paramString == null) {
      throw new NullPointerException("message can't be null");
    }
    cause = null;
  }
  
  public XPathException(Throwable paramThrowable)
  {
    super(paramThrowable == null ? null : paramThrowable.toString());
    cause = paramThrowable;
    if (paramThrowable == null) {
      throw new NullPointerException("cause can't be null");
    }
  }
  
  public Throwable getCause()
  {
    return cause;
  }
  
  public void printStackTrace(PrintStream paramPrintStream)
  {
    if (getCause() != null)
    {
      getCause().printStackTrace(paramPrintStream);
      paramPrintStream.println("--------------- linked to ------------------");
    }
    super.printStackTrace(paramPrintStream);
  }
  
  public void printStackTrace()
  {
    printStackTrace(System.err);
  }
  
  public void printStackTrace(PrintWriter paramPrintWriter)
  {
    if (getCause() != null)
    {
      getCause().printStackTrace(paramPrintWriter);
      paramPrintWriter.println("--------------- linked to ------------------");
    }
    super.printStackTrace(paramPrintWriter);
  }
}
