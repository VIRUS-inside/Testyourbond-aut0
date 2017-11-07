package org.w3c.css.sac;

public abstract interface ErrorHandler
{
  public abstract void warning(CSSParseException paramCSSParseException)
    throws CSSException;
  
  public abstract void error(CSSParseException paramCSSParseException)
    throws CSSException;
  
  public abstract void fatalError(CSSParseException paramCSSParseException)
    throws CSSException;
}
