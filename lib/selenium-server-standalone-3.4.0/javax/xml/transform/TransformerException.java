package javax.xml.transform;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TransformerException
  extends Exception
{
  private static final long serialVersionUID = 975798773772956428L;
  SourceLocator locator;
  Throwable containedException;
  
  public SourceLocator getLocator()
  {
    return locator;
  }
  
  public void setLocator(SourceLocator paramSourceLocator)
  {
    locator = paramSourceLocator;
  }
  
  public Throwable getException()
  {
    return containedException;
  }
  
  public Throwable getCause()
  {
    return containedException == this ? null : containedException;
  }
  
  public synchronized Throwable initCause(Throwable paramThrowable)
  {
    if (containedException != null) {
      throw new IllegalStateException("Can't overwrite cause");
    }
    if (paramThrowable == this) {
      throw new IllegalArgumentException("Self-causation not permitted");
    }
    containedException = paramThrowable;
    return this;
  }
  
  public TransformerException(String paramString)
  {
    super(paramString);
    containedException = null;
    locator = null;
  }
  
  public TransformerException(Throwable paramThrowable)
  {
    super(paramThrowable.toString());
    containedException = paramThrowable;
    locator = null;
  }
  
  public TransformerException(String paramString, Throwable paramThrowable)
  {
    super((paramString == null) || (paramString.length() == 0) ? paramThrowable.toString() : paramString);
    containedException = paramThrowable;
    locator = null;
  }
  
  public TransformerException(String paramString, SourceLocator paramSourceLocator)
  {
    super(paramString);
    containedException = null;
    locator = paramSourceLocator;
  }
  
  public TransformerException(String paramString, SourceLocator paramSourceLocator, Throwable paramThrowable)
  {
    super(paramString);
    containedException = paramThrowable;
    locator = paramSourceLocator;
  }
  
  public String getMessageAndLocation()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    String str1 = super.getMessage();
    if (null != str1) {
      localStringBuffer.append(str1);
    }
    if (null != locator)
    {
      String str2 = locator.getSystemId();
      int i = locator.getLineNumber();
      int j = locator.getColumnNumber();
      if (null != str2)
      {
        localStringBuffer.append("; SystemID: ");
        localStringBuffer.append(str2);
      }
      if (0 != i)
      {
        localStringBuffer.append("; Line#: ");
        localStringBuffer.append(i);
      }
      if (0 != j)
      {
        localStringBuffer.append("; Column#: ");
        localStringBuffer.append(j);
      }
    }
    return localStringBuffer.toString();
  }
  
  public String getLocationAsString()
  {
    if (null != locator)
    {
      StringBuffer localStringBuffer = new StringBuffer();
      String str = locator.getSystemId();
      int i = locator.getLineNumber();
      int j = locator.getColumnNumber();
      if (null != str)
      {
        localStringBuffer.append("; SystemID: ");
        localStringBuffer.append(str);
      }
      if (0 != i)
      {
        localStringBuffer.append("; Line#: ");
        localStringBuffer.append(i);
      }
      if (0 != j)
      {
        localStringBuffer.append("; Column#: ");
        localStringBuffer.append(j);
      }
      return localStringBuffer.toString();
    }
    return null;
  }
  
  public void printStackTrace()
  {
    printStackTrace(new PrintWriter(System.err, true));
  }
  
  public void printStackTrace(PrintStream paramPrintStream)
  {
    printStackTrace(new PrintWriter(paramPrintStream));
  }
  
  public void printStackTrace(PrintWriter paramPrintWriter)
  {
    if (paramPrintWriter == null) {
      paramPrintWriter = new PrintWriter(System.err, true);
    }
    try
    {
      String str1 = getLocationAsString();
      if (null != str1) {
        paramPrintWriter.println(str1);
      }
      super.printStackTrace(paramPrintWriter);
    }
    catch (Throwable localThrowable1) {}
    int i = 0;
    try
    {
      Throwable.class.getMethod("getCause", (Class[])null);
      i = 1;
    }
    catch (NoSuchMethodException localNoSuchMethodException1) {}
    if (i == 0)
    {
      Throwable localThrowable2 = getException();
      for (int j = 0; (j < 10) && (null != localThrowable2); j++)
      {
        paramPrintWriter.println("---------");
        try
        {
          if ((localThrowable2 instanceof TransformerException))
          {
            String str2 = ((TransformerException)localThrowable2).getLocationAsString();
            if (null != str2) {
              paramPrintWriter.println(str2);
            }
          }
          localThrowable2.printStackTrace(paramPrintWriter);
        }
        catch (Throwable localThrowable3)
        {
          paramPrintWriter.println("Could not print stack trace...");
        }
        try
        {
          Method localMethod = localThrowable2.getClass().getMethod("getException", (Class[])null);
          if (null != localMethod)
          {
            Throwable localThrowable4 = localThrowable2;
            localThrowable2 = (Throwable)localMethod.invoke(localThrowable2, (Object[])null);
            if (localThrowable4 == localThrowable2) {
              break;
            }
          }
          else
          {
            localThrowable2 = null;
          }
        }
        catch (InvocationTargetException localInvocationTargetException)
        {
          localThrowable2 = null;
        }
        catch (IllegalAccessException localIllegalAccessException)
        {
          localThrowable2 = null;
        }
        catch (NoSuchMethodException localNoSuchMethodException2)
        {
          localThrowable2 = null;
        }
      }
    }
    paramPrintWriter.flush();
  }
}
