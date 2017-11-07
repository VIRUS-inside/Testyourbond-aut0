package javax.xml.datatype;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;

public class DatatypeConfigurationException
  extends Exception
{
  private static final long serialVersionUID = -1699373159027047238L;
  private Throwable causeOnJDK13OrBelow;
  private transient boolean isJDK14OrAbove = false;
  
  public DatatypeConfigurationException() {}
  
  public DatatypeConfigurationException(String paramString)
  {
    super(paramString);
  }
  
  public DatatypeConfigurationException(String paramString, Throwable paramThrowable)
  {
    super(paramString);
    initCauseByReflection(paramThrowable);
  }
  
  public DatatypeConfigurationException(Throwable paramThrowable)
  {
    super(paramThrowable == null ? null : paramThrowable.toString());
    initCauseByReflection(paramThrowable);
  }
  
  public void printStackTrace()
  {
    if ((!isJDK14OrAbove) && (causeOnJDK13OrBelow != null)) {
      printStackTrace0(new PrintWriter(System.err, true));
    } else {
      super.printStackTrace();
    }
  }
  
  public void printStackTrace(PrintStream paramPrintStream)
  {
    if ((!isJDK14OrAbove) && (causeOnJDK13OrBelow != null)) {
      printStackTrace0(new PrintWriter(paramPrintStream));
    } else {
      super.printStackTrace(paramPrintStream);
    }
  }
  
  public void printStackTrace(PrintWriter paramPrintWriter)
  {
    if ((!isJDK14OrAbove) && (causeOnJDK13OrBelow != null)) {
      printStackTrace0(paramPrintWriter);
    } else {
      super.printStackTrace(paramPrintWriter);
    }
  }
  
  private void printStackTrace0(PrintWriter paramPrintWriter)
  {
    causeOnJDK13OrBelow.printStackTrace(paramPrintWriter);
    paramPrintWriter.println("------------------------------------------");
    super.printStackTrace(paramPrintWriter);
  }
  
  private void initCauseByReflection(Throwable paramThrowable)
  {
    causeOnJDK13OrBelow = paramThrowable;
    try
    {
      Method localMethod = getClass().getMethod("initCause", new Class[] { Throwable.class });
      localMethod.invoke(this, new Object[] { paramThrowable });
      isJDK14OrAbove = true;
    }
    catch (Exception localException) {}
  }
  
  private void readObject(ObjectInputStream paramObjectInputStream)
    throws IOException, ClassNotFoundException
  {
    paramObjectInputStream.defaultReadObject();
    try
    {
      Method localMethod1 = getClass().getMethod("getCause", new Class[0]);
      Throwable localThrowable = (Throwable)localMethod1.invoke(this, new Object[0]);
      if (causeOnJDK13OrBelow == null)
      {
        causeOnJDK13OrBelow = localThrowable;
      }
      else if (localThrowable == null)
      {
        Method localMethod2 = getClass().getMethod("initCause", new Class[] { Throwable.class });
        localMethod2.invoke(this, new Object[] { causeOnJDK13OrBelow });
      }
      isJDK14OrAbove = true;
    }
    catch (Exception localException) {}
  }
}
