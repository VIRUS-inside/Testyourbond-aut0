package javax.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;








































































public class ServletRequestWrapper
  implements ServletRequest
{
  private ServletRequest request;
  
  public ServletRequestWrapper(ServletRequest request)
  {
    if (request == null) {
      throw new IllegalArgumentException("Request cannot be null");
    }
    this.request = request;
  }
  



  public ServletRequest getRequest()
  {
    return request;
  }
  




  public void setRequest(ServletRequest request)
  {
    if (request == null) {
      throw new IllegalArgumentException("Request cannot be null");
    }
    this.request = request;
  }
  




  public Object getAttribute(String name)
  {
    return request.getAttribute(name);
  }
  




  public Enumeration<String> getAttributeNames()
  {
    return request.getAttributeNames();
  }
  




  public String getCharacterEncoding()
  {
    return request.getCharacterEncoding();
  }
  




  public void setCharacterEncoding(String enc)
    throws UnsupportedEncodingException
  {
    request.setCharacterEncoding(enc);
  }
  




  public int getContentLength()
  {
    return request.getContentLength();
  }
  





  public long getContentLengthLong()
  {
    return request.getContentLengthLong();
  }
  




  public String getContentType()
  {
    return request.getContentType();
  }
  



  public ServletInputStream getInputStream()
    throws IOException
  {
    return request.getInputStream();
  }
  




  public String getParameter(String name)
  {
    return request.getParameter(name);
  }
  




  public Map<String, String[]> getParameterMap()
  {
    return request.getParameterMap();
  }
  




  public Enumeration<String> getParameterNames()
  {
    return request.getParameterNames();
  }
  




  public String[] getParameterValues(String name)
  {
    return request.getParameterValues(name);
  }
  




  public String getProtocol()
  {
    return request.getProtocol();
  }
  




  public String getScheme()
  {
    return request.getScheme();
  }
  




  public String getServerName()
  {
    return request.getServerName();
  }
  




  public int getServerPort()
  {
    return request.getServerPort();
  }
  



  public BufferedReader getReader()
    throws IOException
  {
    return request.getReader();
  }
  




  public String getRemoteAddr()
  {
    return request.getRemoteAddr();
  }
  




  public String getRemoteHost()
  {
    return request.getRemoteHost();
  }
  




  public void setAttribute(String name, Object o)
  {
    request.setAttribute(name, o);
  }
  




  public void removeAttribute(String name)
  {
    request.removeAttribute(name);
  }
  




  public Locale getLocale()
  {
    return request.getLocale();
  }
  




  public Enumeration<Locale> getLocales()
  {
    return request.getLocales();
  }
  




  public boolean isSecure()
  {
    return request.isSecure();
  }
  




  public RequestDispatcher getRequestDispatcher(String path)
  {
    return request.getRequestDispatcher(path);
  }
  




  /**
   * @deprecated
   */
  public String getRealPath(String path)
  {
    return request.getRealPath(path);
  }
  






  public int getRemotePort()
  {
    return request.getRemotePort();
  }
  






  public String getLocalName()
  {
    return request.getLocalName();
  }
  






  public String getLocalAddr()
  {
    return request.getLocalAddr();
  }
  






  public int getLocalPort()
  {
    return request.getLocalPort();
  }
  









  public ServletContext getServletContext()
  {
    return request.getServletContext();
  }
  


















  public AsyncContext startAsync()
    throws IllegalStateException
  {
    return request.startAsync();
  }
  


























  public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse)
    throws IllegalStateException
  {
    return request.startAsync(servletRequest, servletResponse);
  }
  










  public boolean isAsyncStarted()
  {
    return request.isAsyncStarted();
  }
  










  public boolean isAsyncSupported()
  {
    return request.isAsyncSupported();
  }
  



















  public AsyncContext getAsyncContext()
  {
    return request.getAsyncContext();
  }
  











  public boolean isWrapperFor(ServletRequest wrapped)
  {
    if (request == wrapped)
      return true;
    if ((request instanceof ServletRequestWrapper)) {
      return ((ServletRequestWrapper)request).isWrapperFor(wrapped);
    }
    return false;
  }
  
















  public boolean isWrapperFor(Class<?> wrappedType)
  {
    if (!ServletRequest.class.isAssignableFrom(wrappedType)) {
      throw new IllegalArgumentException("Given class " + wrappedType.getName() + " not a subinterface of " + ServletRequest.class.getName());
    }
    

    if (wrappedType.isAssignableFrom(request.getClass()))
      return true;
    if ((request instanceof ServletRequestWrapper)) {
      return ((ServletRequestWrapper)request).isWrapperFor(wrappedType);
    }
    return false;
  }
  










  public DispatcherType getDispatcherType()
  {
    return request.getDispatcherType();
  }
}
