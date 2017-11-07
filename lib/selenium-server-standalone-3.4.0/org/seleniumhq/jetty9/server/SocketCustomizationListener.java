package org.seleniumhq.jetty9.server;

import java.net.Socket;
import org.seleniumhq.jetty9.io.Connection;
import org.seleniumhq.jetty9.io.Connection.Listener;
import org.seleniumhq.jetty9.io.EndPoint;
import org.seleniumhq.jetty9.io.SocketChannelEndPoint;
import org.seleniumhq.jetty9.io.ssl.SslConnection;
import org.seleniumhq.jetty9.io.ssl.SslConnection.DecryptedEndPoint;
































public class SocketCustomizationListener
  implements Connection.Listener
{
  private final boolean _ssl;
  
  public SocketCustomizationListener()
  {
    this(true);
  }
  




  public SocketCustomizationListener(boolean ssl)
  {
    _ssl = ssl;
  }
  

  public void onOpened(Connection connection)
  {
    EndPoint endp = connection.getEndPoint();
    boolean ssl = false;
    
    if ((_ssl) && ((endp instanceof SslConnection.DecryptedEndPoint)))
    {
      endp = ((SslConnection.DecryptedEndPoint)endp).getSslConnection().getEndPoint();
      ssl = true;
    }
    
    if ((endp instanceof SocketChannelEndPoint))
    {
      Socket socket = ((SocketChannelEndPoint)endp).getSocket();
      customize(socket, connection.getClass(), ssl);
    }
  }
  
  protected void customize(Socket socket, Class<? extends Connection> connection, boolean ssl) {}
  
  public void onClosed(Connection connection) {}
}
