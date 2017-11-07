package javax.servlet.http;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;

public abstract interface HttpServletRequest
  extends ServletRequest
{
  public static final String BASIC_AUTH = "BASIC";
  public static final String FORM_AUTH = "FORM";
  public static final String CLIENT_CERT_AUTH = "CLIENT_CERT";
  public static final String DIGEST_AUTH = "DIGEST";
  
  public abstract String getAuthType();
  
  public abstract Cookie[] getCookies();
  
  public abstract long getDateHeader(String paramString);
  
  public abstract String getHeader(String paramString);
  
  public abstract Enumeration<String> getHeaders(String paramString);
  
  public abstract Enumeration<String> getHeaderNames();
  
  public abstract int getIntHeader(String paramString);
  
  public abstract String getMethod();
  
  public abstract String getPathInfo();
  
  public abstract String getPathTranslated();
  
  public abstract String getContextPath();
  
  public abstract String getQueryString();
  
  public abstract String getRemoteUser();
  
  public abstract boolean isUserInRole(String paramString);
  
  public abstract Principal getUserPrincipal();
  
  public abstract String getRequestedSessionId();
  
  public abstract String getRequestURI();
  
  public abstract StringBuffer getRequestURL();
  
  public abstract String getServletPath();
  
  public abstract HttpSession getSession(boolean paramBoolean);
  
  public abstract HttpSession getSession();
  
  public abstract String changeSessionId();
  
  public abstract boolean isRequestedSessionIdValid();
  
  public abstract boolean isRequestedSessionIdFromCookie();
  
  public abstract boolean isRequestedSessionIdFromURL();
  
  /**
   * @deprecated
   */
  public abstract boolean isRequestedSessionIdFromUrl();
  
  public abstract boolean authenticate(HttpServletResponse paramHttpServletResponse)
    throws IOException, ServletException;
  
  public abstract void login(String paramString1, String paramString2)
    throws ServletException;
  
  public abstract void logout()
    throws ServletException;
  
  public abstract Collection<Part> getParts()
    throws IOException, ServletException;
  
  public abstract Part getPart(String paramString)
    throws IOException, ServletException;
  
  public abstract <T extends HttpUpgradeHandler> T upgrade(Class<T> paramClass)
    throws IOException, ServletException;
}
