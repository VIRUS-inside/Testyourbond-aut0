package org.w3c.css.sac;






public class CSSException
  extends RuntimeException
{
  protected String s;
  




  public static final short SAC_UNSPECIFIED_ERR = 0;
  




  public static final short SAC_NOT_SUPPORTED_ERR = 1;
  




  public static final short SAC_SYNTAX_ERR = 2;
  




  protected static final String S_SAC_UNSPECIFIED_ERR = "unknown error";
  




  protected static final String S_SAC_NOT_SUPPORTED_ERR = "not supported";
  



  protected static final String S_SAC_SYNTAX_ERR = "syntax error";
  



  protected Exception e;
  



  protected short code;
  




  public CSSException() {}
  




  public CSSException(String paramString)
  {
    code = 0;
    s = paramString;
  }
  



  public CSSException(Exception paramException)
  {
    code = 0;
    e = paramException;
  }
  



  public CSSException(short paramShort)
  {
    code = paramShort;
  }
  





  public CSSException(short paramShort, String paramString, Exception paramException)
  {
    code = paramShort;
    s = paramString;
    e = paramException;
  }
  





  public String getMessage()
  {
    if (s != null)
      return s;
    if (e != null) {
      return e.getMessage();
    }
    switch (code) {
    case 0: 
      return "unknown error";
    case 1: 
      return "not supported";
    case 2: 
      return "syntax error";
    }
    return null;
  }
  




  public short getCode()
  {
    return code;
  }
  


  public Exception getException()
  {
    return e;
  }
}
