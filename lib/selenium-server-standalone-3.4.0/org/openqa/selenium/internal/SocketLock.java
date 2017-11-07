package org.openqa.selenium.internal;

import java.io.Closeable;
import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import org.openqa.selenium.WebDriverException;
























public class SocketLock
  implements Closeable, Lock
{
  public static final int DEFAULT_PORT = 7055;
  private static final long DELAY_BETWEEN_SOCKET_CHECKS = 2000L;
  private static Object syncObject = new Object();
  
  private static final InetSocketAddress localhost = new InetSocketAddress("localhost", 7054);
  

  private final Socket lockSocket;
  

  private final InetSocketAddress address;
  

  public SocketLock()
  {
    this(localhost);
  }
  





  public SocketLock(int lockPort)
  {
    this(new InetSocketAddress("localhost", lockPort));
  }
  





  public SocketLock(InetSocketAddress address)
  {
    lockSocket = new Socket();
    this.address = address;
  }
  

  public void lock(long timeoutInMillis)
    throws WebDriverException
  {
    synchronized (syncObject)
    {
      long maxWait = System.currentTimeMillis() + timeoutInMillis;
      do
      {
        try
        {
          if (isLockFree(address)) {
            return;
          }
          

          Thread.sleep((2000.0D * Math.random()));
        } catch (InterruptedException e) {
          throw new WebDriverException(e);
        } catch (IOException e) {
          throw new WebDriverException(e);
        }
      } while (System.currentTimeMillis() < maxWait);
      

      throw new WebDriverException(String.format("Unable to bind to locking port %d within %d ms", new Object[] {Integer.valueOf(address.getPort()), 
        Long.valueOf(timeoutInMillis) }));
    }
  }
  
  public void close() throws IOException
  {
    unlock();
  }
  
  public void unlock() {
    try {
      if (lockSocket.isBound()) lockSocket.close();
    } catch (IOException e) {
      throw new WebDriverException(e);
    }
  }
  




  private boolean isLockFree(InetSocketAddress address)
    throws IOException
  {
    try
    {
      lockSocket.bind(address);
      return true;
    } catch (BindException e) {
      return false;
    } catch (SocketException e) {}
    return false;
  }
  




  public int getLockPort()
  {
    return address.getPort();
  }
}
