package netscape.javascript;






public class JSException
  extends RuntimeException
{
  public static final int EXCEPTION_TYPE_EMPTY = -1;
  



  public static final int EXCEPTION_TYPE_VOID = 0;
  



  public static final int EXCEPTION_TYPE_OBJECT = 1;
  



  public static final int EXCEPTION_TYPE_FUNCTION = 2;
  



  public static final int EXCEPTION_TYPE_STRING = 3;
  



  public static final int EXCEPTION_TYPE_NUMBER = 4;
  



  public static final int EXCEPTION_TYPE_BOOLEAN = 5;
  



  public static final int EXCEPTION_TYPE_ERROR = 6;
  



  private int wrappedExceptionType_ = -1;
  

  private Object wrappedException_;
  


  public JSException()
  {
    this(null);
  }
  



  public JSException(String message)
  {
    this(message, null, -1, null, -1);
  }
  









  public JSException(String message, String filename, int lineno, String source, int tokenIndex)
  {
    super(message);
  }
  




  public JSException(int exceptionType, Object exception)
  {
    this();
    
    wrappedException_ = exception;
  }
  



  public int getWrappedExceptionType()
  {
    return wrappedExceptionType_;
  }
  



  public Object getWrappedException()
  {
    return wrappedException_;
  }
}
