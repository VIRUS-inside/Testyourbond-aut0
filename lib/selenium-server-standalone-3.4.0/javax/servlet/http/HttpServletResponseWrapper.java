package javax.servlet.http;

import java.io.IOException;
import java.util.Collection;
import javax.servlet.ServletResponseWrapper;









































































public class HttpServletResponseWrapper
  extends ServletResponseWrapper
  implements HttpServletResponse
{
  public HttpServletResponseWrapper(HttpServletResponse response)
  {
    super(response);
  }
  
  private HttpServletResponse _getHttpServletResponse() {
    return (HttpServletResponse)super.getResponse();
  }
  




  public void addCookie(Cookie cookie)
  {
    _getHttpServletResponse().addCookie(cookie);
  }
  




  public boolean containsHeader(String name)
  {
    return _getHttpServletResponse().containsHeader(name);
  }
  




  public String encodeURL(String url)
  {
    return _getHttpServletResponse().encodeURL(url);
  }
  




  public String encodeRedirectURL(String url)
  {
    return _getHttpServletResponse().encodeRedirectURL(url);
  }
  




  /**
   * @deprecated
   */
  public String encodeUrl(String url)
  {
    return _getHttpServletResponse().encodeUrl(url);
  }
  




  /**
   * @deprecated
   */
  public String encodeRedirectUrl(String url)
  {
    return _getHttpServletResponse().encodeRedirectUrl(url);
  }
  



  public void sendError(int sc, String msg)
    throws IOException
  {
    _getHttpServletResponse().sendError(sc, msg);
  }
  



  public void sendError(int sc)
    throws IOException
  {
    _getHttpServletResponse().sendError(sc);
  }
  



  public void sendRedirect(String location)
    throws IOException
  {
    _getHttpServletResponse().sendRedirect(location);
  }
  




  public void setDateHeader(String name, long date)
  {
    _getHttpServletResponse().setDateHeader(name, date);
  }
  




  public void addDateHeader(String name, long date)
  {
    _getHttpServletResponse().addDateHeader(name, date);
  }
  




  public void setHeader(String name, String value)
  {
    _getHttpServletResponse().setHeader(name, value);
  }
  




  public void addHeader(String name, String value)
  {
    _getHttpServletResponse().addHeader(name, value);
  }
  




  public void setIntHeader(String name, int value)
  {
    _getHttpServletResponse().setIntHeader(name, value);
  }
  




  public void addIntHeader(String name, int value)
  {
    _getHttpServletResponse().addIntHeader(name, value);
  }
  




  public void setStatus(int sc)
  {
    _getHttpServletResponse().setStatus(sc);
  }
  






  /**
   * @deprecated
   */
  public void setStatus(int sc, String sm)
  {
    _getHttpServletResponse().setStatus(sc, sm);
  }
  







  public int getStatus()
  {
    return _getHttpServletResponse().getStatus();
  }
  













  public String getHeader(String name)
  {
    return _getHttpServletResponse().getHeader(name);
  }
  















  public Collection<String> getHeaders(String name)
  {
    return _getHttpServletResponse().getHeaders(name);
  }
  













  public Collection<String> getHeaderNames()
  {
    return _getHttpServletResponse().getHeaderNames();
  }
}
