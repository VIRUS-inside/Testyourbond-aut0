package org.seleniumhq.jetty9.server;

import javax.servlet.ServletException;
import org.seleniumhq.jetty9.io.QuietException;






























public class QuietServletException
  extends ServletException
  implements QuietException
{
  public QuietServletException() {}
  
  public QuietServletException(String message, Throwable rootCause)
  {
    super(message, rootCause);
  }
  
  public QuietServletException(String message)
  {
    super(message);
  }
  
  public QuietServletException(Throwable rootCause)
  {
    super(rootCause);
  }
}
