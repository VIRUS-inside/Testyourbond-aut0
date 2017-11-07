package org.apache.http.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import org.apache.http.HttpInetConnection;
import org.apache.http.impl.io.SocketInputBuffer;
import org.apache.http.impl.io.SocketOutputBuffer;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;






































@Deprecated
public class SocketHttpClientConnection
  extends AbstractHttpClientConnection
  implements HttpInetConnection
{
  private volatile boolean open;
  private volatile Socket socket = null;
  

  public SocketHttpClientConnection() {}
  
  protected void assertNotOpen()
  {
    Asserts.check(!open, "Connection is already open");
  }
  
  protected void assertOpen()
  {
    Asserts.check(open, "Connection is not open");
  }
  
















  protected SessionInputBuffer createSessionInputBuffer(Socket socket, int buffersize, HttpParams params)
    throws IOException
  {
    return new SocketInputBuffer(socket, buffersize, params);
  }
  
















  protected SessionOutputBuffer createSessionOutputBuffer(Socket socket, int buffersize, HttpParams params)
    throws IOException
  {
    return new SocketOutputBuffer(socket, buffersize, params);
  }
  


















  protected void bind(Socket socket, HttpParams params)
    throws IOException
  {
    Args.notNull(socket, "Socket");
    Args.notNull(params, "HTTP parameters");
    this.socket = socket;
    
    int buffersize = params.getIntParameter("http.socket.buffer-size", -1);
    init(createSessionInputBuffer(socket, buffersize, params), createSessionOutputBuffer(socket, buffersize, params), params);
    



    open = true;
  }
  
  public boolean isOpen() {
    return open;
  }
  
  protected Socket getSocket() {
    return socket;
  }
  
  public InetAddress getLocalAddress() {
    if (socket != null) {
      return socket.getLocalAddress();
    }
    return null;
  }
  
  public int getLocalPort()
  {
    if (socket != null) {
      return socket.getLocalPort();
    }
    return -1;
  }
  
  public InetAddress getRemoteAddress()
  {
    if (socket != null) {
      return socket.getInetAddress();
    }
    return null;
  }
  
  public int getRemotePort()
  {
    if (socket != null) {
      return socket.getPort();
    }
    return -1;
  }
  
  public void setSocketTimeout(int timeout)
  {
    assertOpen();
    if (socket != null) {
      try {
        socket.setSoTimeout(timeout);
      }
      catch (SocketException ignore) {}
    }
  }
  


  public int getSocketTimeout()
  {
    if (socket != null) {
      try {
        return socket.getSoTimeout();
      } catch (SocketException ignore) {
        return -1;
      }
    }
    return -1;
  }
  
  public void shutdown() throws IOException
  {
    open = false;
    Socket tmpsocket = socket;
    if (tmpsocket != null) {
      tmpsocket.close();
    }
  }
  
  public void close() throws IOException {
    if (!open) {
      return;
    }
    open = false;
    Socket sock = socket;
    try {
      doFlush();
      

      try
      {
        try {}catch (IOException ignore) {}
        

        try {}catch (IOException ignore) {}
      }
      catch (UnsupportedOperationException ignore) {}
    }
    finally
    {
      sock.close();
    }
  }
  
  private static void formatAddress(StringBuilder buffer, SocketAddress socketAddress) {
    if ((socketAddress instanceof InetSocketAddress)) {
      InetSocketAddress addr = (InetSocketAddress)socketAddress;
      buffer.append(addr.getAddress() != null ? addr.getAddress().getHostAddress() : addr.getAddress()).append(':').append(addr.getPort());

    }
    else
    {
      buffer.append(socketAddress);
    }
  }
  
  public String toString()
  {
    if (socket != null) {
      StringBuilder buffer = new StringBuilder();
      SocketAddress remoteAddress = socket.getRemoteSocketAddress();
      SocketAddress localAddress = socket.getLocalSocketAddress();
      if ((remoteAddress != null) && (localAddress != null)) {
        formatAddress(buffer, localAddress);
        buffer.append("<->");
        formatAddress(buffer, remoteAddress);
      }
      return buffer.toString();
    }
    return super.toString();
  }
}
