package com.gargoylesoftware.htmlunit;

import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.ErrorHandler;





















public class DefaultCssErrorHandler
  implements ErrorHandler, Serializable
{
  private static final Log LOG = LogFactory.getLog(DefaultCssErrorHandler.class);
  

  public DefaultCssErrorHandler() {}
  
  public void error(CSSParseException exception)
  {
    LOG.warn("CSS error: " + buildMessage(exception));
  }
  



  public void fatalError(CSSParseException exception)
  {
    LOG.warn("CSS fatal error: " + buildMessage(exception));
  }
  



  public void warning(CSSParseException exception)
  {
    LOG.warn("CSS warning: " + buildMessage(exception));
  }
  




  private static String buildMessage(CSSParseException exception)
  {
    String uri = exception.getURI();
    int line = exception.getLineNumber();
    int col = exception.getColumnNumber();
    
    if (uri == null) {
      return "[" + line + ":" + col + "] " + exception.getMessage();
    }
    return "'" + uri + "' [" + line + ":" + col + "] " + exception.getMessage();
  }
}
