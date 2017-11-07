package org.seleniumhq.jetty9.server.handler;

import java.io.IOException;
import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.seleniumhq.jetty9.server.Handler;
import org.seleniumhq.jetty9.server.HttpChannel;
import org.seleniumhq.jetty9.server.Request;
import org.seleniumhq.jetty9.server.RequestLog;































public class RequestLogHandler
  extends HandlerWrapper
{
  private RequestLog _requestLog;
  
  public RequestLogHandler() {}
  
  public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    if (baseRequest.getDispatcherType() == DispatcherType.REQUEST)
      baseRequest.getHttpChannel().addRequestLog(_requestLog);
    if (_handler != null) {
      _handler.handle(target, baseRequest, request, response);
    }
  }
  
  public void setRequestLog(RequestLog requestLog)
  {
    updateBean(_requestLog, requestLog);
    _requestLog = requestLog;
  }
  

  public RequestLog getRequestLog()
  {
    return _requestLog;
  }
}
