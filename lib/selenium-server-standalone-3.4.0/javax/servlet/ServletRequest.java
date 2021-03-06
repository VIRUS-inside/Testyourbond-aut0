package javax.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

public abstract interface ServletRequest
{
  public abstract Object getAttribute(String paramString);
  
  public abstract Enumeration<String> getAttributeNames();
  
  public abstract String getCharacterEncoding();
  
  public abstract void setCharacterEncoding(String paramString)
    throws UnsupportedEncodingException;
  
  public abstract int getContentLength();
  
  public abstract long getContentLengthLong();
  
  public abstract String getContentType();
  
  public abstract ServletInputStream getInputStream()
    throws IOException;
  
  public abstract String getParameter(String paramString);
  
  public abstract Enumeration<String> getParameterNames();
  
  public abstract String[] getParameterValues(String paramString);
  
  public abstract Map<String, String[]> getParameterMap();
  
  public abstract String getProtocol();
  
  public abstract String getScheme();
  
  public abstract String getServerName();
  
  public abstract int getServerPort();
  
  public abstract BufferedReader getReader()
    throws IOException;
  
  public abstract String getRemoteAddr();
  
  public abstract String getRemoteHost();
  
  public abstract void setAttribute(String paramString, Object paramObject);
  
  public abstract void removeAttribute(String paramString);
  
  public abstract Locale getLocale();
  
  public abstract Enumeration<Locale> getLocales();
  
  public abstract boolean isSecure();
  
  public abstract RequestDispatcher getRequestDispatcher(String paramString);
  
  /**
   * @deprecated
   */
  public abstract String getRealPath(String paramString);
  
  public abstract int getRemotePort();
  
  public abstract String getLocalName();
  
  public abstract String getLocalAddr();
  
  public abstract int getLocalPort();
  
  public abstract ServletContext getServletContext();
  
  public abstract AsyncContext startAsync()
    throws IllegalStateException;
  
  public abstract AsyncContext startAsync(ServletRequest paramServletRequest, ServletResponse paramServletResponse)
    throws IllegalStateException;
  
  public abstract boolean isAsyncStarted();
  
  public abstract boolean isAsyncSupported();
  
  public abstract AsyncContext getAsyncContext();
  
  public abstract DispatcherType getDispatcherType();
}
