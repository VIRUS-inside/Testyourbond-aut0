package org.seleniumhq.jetty9.server;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;


























public class ServletRequestHttpWrapper
  extends ServletRequestWrapper
  implements HttpServletRequest
{
  public ServletRequestHttpWrapper(ServletRequest request)
  {
    super(request);
  }
  

  public String getAuthType()
  {
    return null;
  }
  

  public Cookie[] getCookies()
  {
    return null;
  }
  

  public long getDateHeader(String name)
  {
    return 0L;
  }
  

  public String getHeader(String name)
  {
    return null;
  }
  

  public Enumeration<String> getHeaders(String name)
  {
    return null;
  }
  

  public Enumeration<String> getHeaderNames()
  {
    return null;
  }
  

  public int getIntHeader(String name)
  {
    return 0;
  }
  

  public String getMethod()
  {
    return null;
  }
  

  public String getPathInfo()
  {
    return null;
  }
  

  public String getPathTranslated()
  {
    return null;
  }
  

  public String getContextPath()
  {
    return null;
  }
  

  public String getQueryString()
  {
    return null;
  }
  

  public String getRemoteUser()
  {
    return null;
  }
  

  public boolean isUserInRole(String role)
  {
    return false;
  }
  

  public Principal getUserPrincipal()
  {
    return null;
  }
  

  public String getRequestedSessionId()
  {
    return null;
  }
  

  public String getRequestURI()
  {
    return null;
  }
  

  public StringBuffer getRequestURL()
  {
    return null;
  }
  

  public String getServletPath()
  {
    return null;
  }
  

  public HttpSession getSession(boolean create)
  {
    return null;
  }
  

  public HttpSession getSession()
  {
    return null;
  }
  

  public boolean isRequestedSessionIdValid()
  {
    return false;
  }
  

  public boolean isRequestedSessionIdFromCookie()
  {
    return false;
  }
  

  public boolean isRequestedSessionIdFromURL()
  {
    return false;
  }
  

  public boolean isRequestedSessionIdFromUrl()
  {
    return false;
  }
  



  public boolean authenticate(HttpServletResponse response)
    throws IOException, ServletException
  {
    return false;
  }
  



  public Part getPart(String name)
    throws IOException, ServletException
  {
    return null;
  }
  



  public Collection<Part> getParts()
    throws IOException, ServletException
  {
    return null;
  }
  






  public void login(String username, String password)
    throws ServletException
  {}
  





  public void logout()
    throws ServletException
  {}
  





  public String changeSessionId()
  {
    return null;
  }
  




  public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass)
    throws IOException, ServletException
  {
    return null;
  }
}
