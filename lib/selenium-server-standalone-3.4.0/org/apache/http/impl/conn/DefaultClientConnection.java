package org.apache.http.impl.conn;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.impl.SocketHttpClientConnection;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;




































@Deprecated
public class DefaultClientConnection
  extends SocketHttpClientConnection
  implements OperatedClientConnection, ManagedHttpClientConnection, HttpContext
{
  private final Log log = LogFactory.getLog(getClass());
  private final Log headerLog = LogFactory.getLog("org.apache.http.headers");
  private final Log wireLog = LogFactory.getLog("org.apache.http.wire");
  

  private volatile Socket socket;
  

  private HttpHost targetHost;
  

  private boolean connSecure;
  

  private volatile boolean shutdown;
  
  private final Map<String, Object> attributes;
  

  public DefaultClientConnection()
  {
    attributes = new HashMap();
  }
  
  public String getId()
  {
    return null;
  }
  
  public final HttpHost getTargetHost()
  {
    return targetHost;
  }
  
  public final boolean isSecure()
  {
    return connSecure;
  }
  
  public final Socket getSocket()
  {
    return socket;
  }
  
  public SSLSession getSSLSession()
  {
    if ((socket instanceof SSLSocket)) {
      return ((SSLSocket)socket).getSession();
    }
    return null;
  }
  
  public void opening(Socket sock, HttpHost target)
    throws IOException
  {
    assertNotOpen();
    socket = sock;
    targetHost = target;
    

    if (shutdown) {
      sock.close();
      
      throw new InterruptedIOException("Connection already shutdown");
    }
  }
  
  public void openCompleted(boolean secure, HttpParams params) throws IOException
  {
    Args.notNull(params, "Parameters");
    assertNotOpen();
    connSecure = secure;
    bind(socket, params);
  }
  












  public void shutdown()
    throws IOException
  {
    shutdown = true;
    try {
      super.shutdown();
      if (log.isDebugEnabled()) {
        log.debug("Connection " + this + " shut down");
      }
      Socket sock = socket;
      if (sock != null) {
        sock.close();
      }
    } catch (IOException ex) {
      log.debug("I/O error shutting down connection", ex);
    }
  }
  
  public void close() throws IOException
  {
    try {
      super.close();
      if (log.isDebugEnabled()) {
        log.debug("Connection " + this + " closed");
      }
    } catch (IOException ex) {
      log.debug("I/O error closing connection", ex);
    }
  }
  


  protected SessionInputBuffer createSessionInputBuffer(Socket socket, int buffersize, HttpParams params)
    throws IOException
  {
    SessionInputBuffer inbuffer = super.createSessionInputBuffer(socket, buffersize > 0 ? buffersize : 8192, params);
    


    if (wireLog.isDebugEnabled()) {
      inbuffer = new LoggingSessionInputBuffer(inbuffer, new Wire(wireLog), HttpProtocolParams.getHttpElementCharset(params));
    }
    


    return inbuffer;
  }
  


  protected SessionOutputBuffer createSessionOutputBuffer(Socket socket, int buffersize, HttpParams params)
    throws IOException
  {
    SessionOutputBuffer outbuffer = super.createSessionOutputBuffer(socket, buffersize > 0 ? buffersize : 8192, params);
    


    if (wireLog.isDebugEnabled()) {
      outbuffer = new LoggingSessionOutputBuffer(outbuffer, new Wire(wireLog), HttpProtocolParams.getHttpElementCharset(params));
    }
    


    return outbuffer;
  }
  




  protected HttpMessageParser<HttpResponse> createResponseParser(SessionInputBuffer buffer, HttpResponseFactory responseFactory, HttpParams params)
  {
    return new DefaultHttpResponseParser(buffer, null, responseFactory, params);
  }
  
  public void bind(Socket socket)
    throws IOException
  {
    bind(socket, new BasicHttpParams());
  }
  


  public void update(Socket sock, HttpHost target, boolean secure, HttpParams params)
    throws IOException
  {
    assertOpen();
    Args.notNull(target, "Target host");
    Args.notNull(params, "Parameters");
    
    if (sock != null) {
      socket = sock;
      bind(sock, params);
    }
    targetHost = target;
    connSecure = secure;
  }
  
  public HttpResponse receiveResponseHeader() throws HttpException, IOException
  {
    HttpResponse response = super.receiveResponseHeader();
    if (log.isDebugEnabled()) {
      log.debug("Receiving response: " + response.getStatusLine());
    }
    if (headerLog.isDebugEnabled()) {
      headerLog.debug("<< " + response.getStatusLine().toString());
      Header[] headers = response.getAllHeaders();
      for (Header header : headers) {
        headerLog.debug("<< " + header.toString());
      }
    }
    return response;
  }
  
  public void sendRequestHeader(HttpRequest request) throws HttpException, IOException
  {
    if (log.isDebugEnabled()) {
      log.debug("Sending request: " + request.getRequestLine());
    }
    super.sendRequestHeader(request);
    if (headerLog.isDebugEnabled()) {
      headerLog.debug(">> " + request.getRequestLine().toString());
      Header[] headers = request.getAllHeaders();
      for (Header header : headers) {
        headerLog.debug(">> " + header.toString());
      }
    }
  }
  
  public Object getAttribute(String id)
  {
    return attributes.get(id);
  }
  
  public Object removeAttribute(String id)
  {
    return attributes.remove(id);
  }
  
  public void setAttribute(String id, Object obj)
  {
    attributes.put(id, obj);
  }
}
