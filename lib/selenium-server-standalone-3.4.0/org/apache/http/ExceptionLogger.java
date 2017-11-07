package org.apache.http;






























public abstract interface ExceptionLogger
{
  public static final ExceptionLogger NO_OP = new ExceptionLogger()
  {
    public void log(Exception ex) {}
  };
  



  public static final ExceptionLogger STD_ERR = new ExceptionLogger()
  {
    public void log(Exception ex)
    {
      ex.printStackTrace();
    }
  };
  
  public abstract void log(Exception paramException);
}
