package javax.servlet;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Map;
import java.util.Set;
import javax.servlet.descriptor.JspConfigDescriptor;

public abstract interface ServletContext
{
  public static final String TEMPDIR = "javax.servlet.context.tempdir";
  public static final String ORDERED_LIBS = "javax.servlet.context.orderedLibs";
  
  public abstract String getContextPath();
  
  public abstract ServletContext getContext(String paramString);
  
  public abstract int getMajorVersion();
  
  public abstract int getMinorVersion();
  
  public abstract int getEffectiveMajorVersion();
  
  public abstract int getEffectiveMinorVersion();
  
  public abstract String getMimeType(String paramString);
  
  public abstract Set<String> getResourcePaths(String paramString);
  
  public abstract URL getResource(String paramString)
    throws MalformedURLException;
  
  public abstract InputStream getResourceAsStream(String paramString);
  
  public abstract RequestDispatcher getRequestDispatcher(String paramString);
  
  public abstract RequestDispatcher getNamedDispatcher(String paramString);
  
  /**
   * @deprecated
   */
  public abstract Servlet getServlet(String paramString)
    throws ServletException;
  
  /**
   * @deprecated
   */
  public abstract Enumeration<Servlet> getServlets();
  
  /**
   * @deprecated
   */
  public abstract Enumeration<String> getServletNames();
  
  public abstract void log(String paramString);
  
  /**
   * @deprecated
   */
  public abstract void log(Exception paramException, String paramString);
  
  public abstract void log(String paramString, Throwable paramThrowable);
  
  public abstract String getRealPath(String paramString);
  
  public abstract String getServerInfo();
  
  public abstract String getInitParameter(String paramString);
  
  public abstract Enumeration<String> getInitParameterNames();
  
  public abstract boolean setInitParameter(String paramString1, String paramString2);
  
  public abstract Object getAttribute(String paramString);
  
  public abstract Enumeration<String> getAttributeNames();
  
  public abstract void setAttribute(String paramString, Object paramObject);
  
  public abstract void removeAttribute(String paramString);
  
  public abstract String getServletContextName();
  
  public abstract ServletRegistration.Dynamic addServlet(String paramString1, String paramString2);
  
  public abstract ServletRegistration.Dynamic addServlet(String paramString, Servlet paramServlet);
  
  public abstract ServletRegistration.Dynamic addServlet(String paramString, Class<? extends Servlet> paramClass);
  
  public abstract <T extends Servlet> T createServlet(Class<T> paramClass)
    throws ServletException;
  
  public abstract ServletRegistration getServletRegistration(String paramString);
  
  public abstract Map<String, ? extends ServletRegistration> getServletRegistrations();
  
  public abstract FilterRegistration.Dynamic addFilter(String paramString1, String paramString2);
  
  public abstract FilterRegistration.Dynamic addFilter(String paramString, Filter paramFilter);
  
  public abstract FilterRegistration.Dynamic addFilter(String paramString, Class<? extends Filter> paramClass);
  
  public abstract <T extends Filter> T createFilter(Class<T> paramClass)
    throws ServletException;
  
  public abstract FilterRegistration getFilterRegistration(String paramString);
  
  public abstract Map<String, ? extends FilterRegistration> getFilterRegistrations();
  
  public abstract SessionCookieConfig getSessionCookieConfig();
  
  public abstract void setSessionTrackingModes(Set<SessionTrackingMode> paramSet);
  
  public abstract Set<SessionTrackingMode> getDefaultSessionTrackingModes();
  
  public abstract Set<SessionTrackingMode> getEffectiveSessionTrackingModes();
  
  public abstract void addListener(String paramString);
  
  public abstract <T extends EventListener> void addListener(T paramT);
  
  public abstract void addListener(Class<? extends EventListener> paramClass);
  
  public abstract <T extends EventListener> T createListener(Class<T> paramClass)
    throws ServletException;
  
  public abstract JspConfigDescriptor getJspConfigDescriptor();
  
  public abstract ClassLoader getClassLoader();
  
  public abstract void declareRoles(String... paramVarArgs);
  
  public abstract String getVirtualServerName();
}
