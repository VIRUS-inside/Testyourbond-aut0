package javax.servlet.http;

import java.util.Enumeration;

/**
 * @deprecated
 */
public abstract interface HttpSessionContext
{
  /**
   * @deprecated
   */
  public abstract HttpSession getSession(String paramString);
  
  /**
   * @deprecated
   */
  public abstract Enumeration<String> getIds();
}
