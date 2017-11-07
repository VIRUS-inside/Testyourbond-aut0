package org.apache.http.impl.bootstrap;

import java.io.IOException;
import org.apache.http.ExceptionLogger;
import org.apache.http.HttpServerConnection;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.protocol.HttpService;

































class Worker
  implements Runnable
{
  private final HttpService httpservice;
  private final HttpServerConnection conn;
  private final ExceptionLogger exceptionLogger;
  
  Worker(HttpService httpservice, HttpServerConnection conn, ExceptionLogger exceptionLogger)
  {
    this.httpservice = httpservice;
    this.conn = conn;
    this.exceptionLogger = exceptionLogger;
  }
  
  public HttpServerConnection getConnection() {
    return conn;
  }
  
  public void run()
  {
    try {
      BasicHttpContext localContext = new BasicHttpContext();
      HttpCoreContext context = HttpCoreContext.adapt(localContext);
      while ((!Thread.interrupted()) && (conn.isOpen())) {
        httpservice.handleRequest(conn, context);
        localContext.clear();
      }
      conn.close(); return;
    } catch (Exception ex) {
      exceptionLogger.log(ex);
    } finally {
      try {
        conn.shutdown();
      } catch (IOException ex) {
        exceptionLogger.log(ex);
      }
    }
  }
}
