package com.steadystate.css.util;

import java.io.Serializable;
import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.ErrorHandler;

























public class ThrowCssExceptionErrorHandler
  implements ErrorHandler, Serializable
{
  private static final long serialVersionUID = -3933638774901855095L;
  public static final ThrowCssExceptionErrorHandler INSTANCE = new ThrowCssExceptionErrorHandler();
  
  public ThrowCssExceptionErrorHandler() {}
  
  public void error(CSSParseException exception)
  {
    throw exception;
  }
  


  public void fatalError(CSSParseException exception)
  {
    throw exception;
  }
  
  public void warning(CSSParseException exception) {}
}
