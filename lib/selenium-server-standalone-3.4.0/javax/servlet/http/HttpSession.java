package javax.servlet.http;

import java.util.Enumeration;
import javax.servlet.ServletContext;

public abstract interface HttpSession
{
  public abstract long getCreationTime();
  
  public abstract String getId();
  
  public abstract long getLastAccessedTime();
  
  public abstract ServletContext getServletContext();
  
  public abstract void setMaxInactiveInterval(int paramInt);
  
  public abstract int getMaxInactiveInterval();
  
  /**
   * @deprecated
   */
  public abstract HttpSessionContext getSessionContext();
  
  public abstract Object getAttribute(String paramString);
  
  /**
   * @deprecated
   */
  public abstract Object getValue(String paramString);
  
  public abstract Enumeration<String> getAttributeNames();
  
  /**
   * @deprecated
   */
  public abstract String[] getValueNames();
  
  public abstract void setAttribute(String paramString, Object paramObject);
  
  /**
   * @deprecated
   */
  public abstract void putValue(String paramString, Object paramObject);
  
  public abstract void removeAttribute(String paramString);
  
  /**
   * @deprecated
   */
  public abstract void removeValue(String paramString);
  
  public abstract void invalidate();
  
  public abstract boolean isNew();
}
