package javax.servlet.http;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.ServletRequestWrapper;







































































public class HttpServletRequestWrapper
  extends ServletRequestWrapper
  implements HttpServletRequest
{
  public HttpServletRequestWrapper(HttpServletRequest request)
  {
    super(request);
  }
  
  private HttpServletRequest _getHttpServletRequest() {
    return (HttpServletRequest)super.getRequest();
  }
  




  public String getAuthType()
  {
    return _getHttpServletRequest().getAuthType();
  }
  




  public Cookie[] getCookies()
  {
    return _getHttpServletRequest().getCookies();
  }
  




  public long getDateHeader(String name)
  {
    return _getHttpServletRequest().getDateHeader(name);
  }
  




  public String getHeader(String name)
  {
    return _getHttpServletRequest().getHeader(name);
  }
  




  public Enumeration<String> getHeaders(String name)
  {
    return _getHttpServletRequest().getHeaders(name);
  }
  




  public Enumeration<String> getHeaderNames()
  {
    return _getHttpServletRequest().getHeaderNames();
  }
  




  public int getIntHeader(String name)
  {
    return _getHttpServletRequest().getIntHeader(name);
  }
  




  public String getMethod()
  {
    return _getHttpServletRequest().getMethod();
  }
  




  public String getPathInfo()
  {
    return _getHttpServletRequest().getPathInfo();
  }
  




  public String getPathTranslated()
  {
    return _getHttpServletRequest().getPathTranslated();
  }
  




  public String getContextPath()
  {
    return _getHttpServletRequest().getContextPath();
  }
  




  public String getQueryString()
  {
    return _getHttpServletRequest().getQueryString();
  }
  




  public String getRemoteUser()
  {
    return _getHttpServletRequest().getRemoteUser();
  }
  




  public boolean isUserInRole(String role)
  {
    return _getHttpServletRequest().isUserInRole(role);
  }
  




  public Principal getUserPrincipal()
  {
    return _getHttpServletRequest().getUserPrincipal();
  }
  




  public String getRequestedSessionId()
  {
    return _getHttpServletRequest().getRequestedSessionId();
  }
  




  public String getRequestURI()
  {
    return _getHttpServletRequest().getRequestURI();
  }
  




  public StringBuffer getRequestURL()
  {
    return _getHttpServletRequest().getRequestURL();
  }
  




  public String getServletPath()
  {
    return _getHttpServletRequest().getServletPath();
  }
  




  public HttpSession getSession(boolean create)
  {
    return _getHttpServletRequest().getSession(create);
  }
  




  public HttpSession getSession()
  {
    return _getHttpServletRequest().getSession();
  }
  






  public String changeSessionId()
  {
    return _getHttpServletRequest().changeSessionId();
  }
  




  public boolean isRequestedSessionIdValid()
  {
    return _getHttpServletRequest().isRequestedSessionIdValid();
  }
  




  public boolean isRequestedSessionIdFromCookie()
  {
    return _getHttpServletRequest().isRequestedSessionIdFromCookie();
  }
  




  public boolean isRequestedSessionIdFromURL()
  {
    return _getHttpServletRequest().isRequestedSessionIdFromURL();
  }
  




  public boolean isRequestedSessionIdFromUrl()
  {
    return _getHttpServletRequest().isRequestedSessionIdFromUrl();
  }
  






  public boolean authenticate(HttpServletResponse response)
    throws IOException, ServletException
  {
    return _getHttpServletRequest().authenticate(response);
  }
  






  public void login(String username, String password)
    throws ServletException
  {
    _getHttpServletRequest().login(username, password);
  }
  





  public void logout()
    throws ServletException
  {
    _getHttpServletRequest().logout();
  }
  








  public Collection<Part> getParts()
    throws IOException, ServletException
  {
    return _getHttpServletRequest().getParts();
  }
  





  public Part getPart(String name)
    throws IOException, ServletException
  {
    return _getHttpServletRequest().getPart(name);
  }
  







  public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass)
    throws IOException, ServletException
  {
    return _getHttpServletRequest().upgrade(handlerClass);
  }
}
