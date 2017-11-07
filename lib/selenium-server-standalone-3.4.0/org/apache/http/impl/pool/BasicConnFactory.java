package org.apache.http.impl.pool;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpConnectionFactory;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.impl.DefaultBHttpClientConnectionFactory;
import org.apache.http.params.HttpParamConfig;
import org.apache.http.params.HttpParams;
import org.apache.http.pool.ConnFactory;
import org.apache.http.util.Args;










































@Contract(threading=ThreadingBehavior.IMMUTABLE_CONDITIONAL)
public class BasicConnFactory
  implements ConnFactory<HttpHost, HttpClientConnection>
{
  private final SocketFactory plainfactory;
  private final SSLSocketFactory sslfactory;
  private final int connectTimeout;
  private final SocketConfig sconfig;
  private final HttpConnectionFactory<? extends HttpClientConnection> connFactory;
  
  @Deprecated
  public BasicConnFactory(SSLSocketFactory sslfactory, HttpParams params)
  {
    Args.notNull(params, "HTTP params");
    plainfactory = null;
    this.sslfactory = sslfactory;
    connectTimeout = params.getIntParameter("http.connection.timeout", 0);
    sconfig = HttpParamConfig.getSocketConfig(params);
    connFactory = new DefaultBHttpClientConnectionFactory(HttpParamConfig.getConnectionConfig(params));
  }
  




  @Deprecated
  public BasicConnFactory(HttpParams params)
  {
    this(null, params);
  }
  








  public BasicConnFactory(SocketFactory plainfactory, SSLSocketFactory sslfactory, int connectTimeout, SocketConfig sconfig, ConnectionConfig cconfig)
  {
    this.plainfactory = plainfactory;
    this.sslfactory = sslfactory;
    this.connectTimeout = connectTimeout;
    this.sconfig = (sconfig != null ? sconfig : SocketConfig.DEFAULT);
    connFactory = new DefaultBHttpClientConnectionFactory(cconfig != null ? cconfig : ConnectionConfig.DEFAULT);
  }
  




  public BasicConnFactory(int connectTimeout, SocketConfig sconfig, ConnectionConfig cconfig)
  {
    this(null, null, connectTimeout, sconfig, cconfig);
  }
  


  public BasicConnFactory(SocketConfig sconfig, ConnectionConfig cconfig)
  {
    this(null, null, 0, sconfig, cconfig);
  }
  


  public BasicConnFactory()
  {
    this(null, null, 0, SocketConfig.DEFAULT, ConnectionConfig.DEFAULT);
  }
  

  @Deprecated
  protected HttpClientConnection create(Socket socket, HttpParams params)
    throws IOException
  {
    int bufsize = params.getIntParameter("http.socket.buffer-size", 8192);
    DefaultBHttpClientConnection conn = new DefaultBHttpClientConnection(bufsize);
    conn.bind(socket);
    return conn;
  }
  
  public HttpClientConnection create(HttpHost host) throws IOException
  {
    String scheme = host.getSchemeName();
    Socket socket = null;
    if ("http".equalsIgnoreCase(scheme)) {
      socket = plainfactory != null ? plainfactory.createSocket() : new Socket();
    }
    if ("https".equalsIgnoreCase(scheme)) {
      socket = (sslfactory != null ? sslfactory : SSLSocketFactory.getDefault()).createSocket();
    }
    
    if (socket == null) {
      throw new IOException(scheme + " scheme is not supported");
    }
    String hostname = host.getHostName();
    int port = host.getPort();
    if (port == -1) {
      if (host.getSchemeName().equalsIgnoreCase("http")) {
        port = 80;
      } else if (host.getSchemeName().equalsIgnoreCase("https")) {
        port = 443;
      }
    }
    socket.setSoTimeout(sconfig.getSoTimeout());
    if (sconfig.getSndBufSize() > 0) {
      socket.setSendBufferSize(sconfig.getSndBufSize());
    }
    if (sconfig.getRcvBufSize() > 0) {
      socket.setReceiveBufferSize(sconfig.getRcvBufSize());
    }
    socket.setTcpNoDelay(sconfig.isTcpNoDelay());
    int linger = sconfig.getSoLinger();
    if (linger >= 0) {
      socket.setSoLinger(true, linger);
    }
    socket.setKeepAlive(sconfig.isSoKeepAlive());
    socket.connect(new InetSocketAddress(hostname, port), connectTimeout);
    return (HttpClientConnection)connFactory.createConnection(socket);
  }
}
