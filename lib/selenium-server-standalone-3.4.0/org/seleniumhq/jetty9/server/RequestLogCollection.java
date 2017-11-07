package org.seleniumhq.jetty9.server;

import java.util.ArrayList;
import java.util.Arrays;




















class RequestLogCollection
  implements RequestLog
{
  private final ArrayList<RequestLog> delegates;
  
  public RequestLogCollection(RequestLog... requestLogs)
  {
    delegates = new ArrayList(Arrays.asList(requestLogs));
  }
  
  public void add(RequestLog requestLog)
  {
    delegates.add(requestLog);
  }
  

  public void log(Request request, Response response)
  {
    for (RequestLog delegate : delegates) {
      delegate.log(request, response);
    }
  }
}
