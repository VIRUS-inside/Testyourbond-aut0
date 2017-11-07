package org.seleniumhq.jetty9.server.handler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Collection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.seleniumhq.jetty9.io.EndPoint;
import org.seleniumhq.jetty9.server.Handler;
import org.seleniumhq.jetty9.server.HttpChannel;
import org.seleniumhq.jetty9.server.Request;
import org.seleniumhq.jetty9.util.IncludeExcludeSet;
import org.seleniumhq.jetty9.util.InetAddressSet;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;



























public class InetAccessHandler
  extends HandlerWrapper
{
  private static final Logger LOG = Log.getLogger(InetAccessHandler.class);
  
  private final IncludeExcludeSet<String, InetAddress> _set = new IncludeExcludeSet(InetAddressSet.class);
  


  public InetAccessHandler() {}
  


  public void include(String pattern)
  {
    _set.include(pattern);
  }
  






  public void include(String... patterns)
  {
    _set.include(patterns);
  }
  






  public void exclude(String pattern)
  {
    _set.exclude(pattern);
  }
  






  public void exclude(String... patterns)
  {
    _set.exclude(patterns);
  }
  




  public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    HttpChannel channel = baseRequest.getHttpChannel();
    if (channel != null)
    {
      EndPoint endp = channel.getEndPoint();
      if (endp != null)
      {
        InetSocketAddress address = endp.getRemoteAddress();
        if ((address != null) && (!isAllowed(address.getAddress(), request)))
        {
          response.sendError(403);
          baseRequest.setHandled(true);
          return;
        }
      }
    }
    
    getHandler().handle(target, baseRequest, request, response);
  }
  







  protected boolean isAllowed(InetAddress address, HttpServletRequest request)
  {
    boolean allowed = _set.test(address);
    if (LOG.isDebugEnabled())
      LOG.debug("{} {} {} for {}", new Object[] { this, allowed ? "allowed" : "denied", address, request });
    return allowed;
  }
  
  public void dump(Appendable out, String indent)
    throws IOException
  {
    dumpBeans(out, indent, new Collection[] { _set.getIncluded(), _set.getExcluded() });
  }
}
