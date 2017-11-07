package org.seleniumhq.jetty9.servlets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.seleniumhq.jetty9.io.EndPoint;
import org.seleniumhq.jetty9.server.HttpChannel;
import org.seleniumhq.jetty9.server.Request;





















public class CloseableDoSFilter
  extends DoSFilter
{
  public CloseableDoSFilter() {}
  
  protected void onRequestTimeout(HttpServletRequest request, HttpServletResponse response, Thread handlingThread)
  {
    Request base_request = Request.getBaseRequest(request);
    base_request.getHttpChannel().getEndPoint().close();
  }
}
