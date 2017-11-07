package org.apache.http.protocol;

import java.util.Map;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.util.Args;













































@Deprecated
@Contract(threading=ThreadingBehavior.SAFE)
public class HttpRequestHandlerRegistry
  implements HttpRequestHandlerResolver
{
  private final UriPatternMatcher<HttpRequestHandler> matcher;
  
  public HttpRequestHandlerRegistry()
  {
    matcher = new UriPatternMatcher();
  }
  






  public void register(String pattern, HttpRequestHandler handler)
  {
    Args.notNull(pattern, "URI request pattern");
    Args.notNull(handler, "Request handler");
    matcher.register(pattern, handler);
  }
  




  public void unregister(String pattern)
  {
    matcher.unregister(pattern);
  }
  



  public void setHandlers(Map<String, HttpRequestHandler> map)
  {
    matcher.setObjects(map);
  }
  





  public Map<String, HttpRequestHandler> getHandlers()
  {
    return matcher.getObjects();
  }
  
  public HttpRequestHandler lookup(String requestURI) {
    return (HttpRequestHandler)matcher.lookup(requestURI);
  }
}
