package org.eclipse.jetty.io;

import java.net.Socket;
import java.nio.ByteBuffer;

public abstract interface NetworkTrafficListener
{
  public abstract void opened(Socket paramSocket);
  
  public abstract void incoming(Socket paramSocket, ByteBuffer paramByteBuffer);
  
  public abstract void outgoing(Socket paramSocket, ByteBuffer paramByteBuffer);
  
  public abstract void closed(Socket paramSocket);
  
  public static class Adapter
    implements NetworkTrafficListener
  {
    public Adapter() {}
    
    public void opened(Socket socket) {}
    
    public void incoming(Socket socket, ByteBuffer bytes) {}
    
    public void outgoing(Socket socket, ByteBuffer bytes) {}
    
    public void closed(Socket socket) {}
  }
}
