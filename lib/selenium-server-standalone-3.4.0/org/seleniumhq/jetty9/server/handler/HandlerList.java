package org.seleniumhq.jetty9.server.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.seleniumhq.jetty9.server.Handler;
import org.seleniumhq.jetty9.server.Request;





























public class HandlerList
  extends HandlerCollection
{
  public HandlerList() {}
  
  public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    Handler[] handlers = getHandlers();
    
    if ((handlers != null) && (isStarted()))
    {
      for (int i = 0; i < handlers.length; i++)
      {
        handlers[i].handle(target, baseRequest, request, response);
        if (baseRequest.isHandled()) {
          return;
        }
      }
    }
  }
}
