package com.gargoylesoftware.htmlunit;

import java.io.Serializable;
import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.ErrorHandler;

public class SilentCssErrorHandler
  implements ErrorHandler, Serializable
{
  public SilentCssErrorHandler() {}
  
  public void error(CSSParseException exception) {}
  
  public void fatalError(CSSParseException exception) {}
  
  public void warning(CSSParseException exception) {}
}
