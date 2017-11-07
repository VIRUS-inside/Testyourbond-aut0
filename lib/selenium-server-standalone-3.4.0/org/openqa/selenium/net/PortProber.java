package org.openqa.selenium.net;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Platform;






















public class PortProber
{
  private static final Random random = new Random();
  private static final EphemeralPortRangeDetector ephemeralRangeDetector;
  
  static {
    Platform current = Platform.getCurrent();
    
    if (current.is(Platform.LINUX)) {
      ephemeralRangeDetector = LinuxEphemeralPortRangeDetector.getInstance();
    } else if (current.is(Platform.XP)) {
      ephemeralRangeDetector = new OlderWindowsVersionEphemeralPortDetector();
    } else {
      ephemeralRangeDetector = new FixedIANAPortRange();
    }
  }
  






  public static int findFreePort()
  {
    for (int i = 0; i < 5; i++) {
      int seedPort = createAcceptablePort();
      int suggestedPort = checkPortIsFree(seedPort);
      if (suggestedPort != -1) {
        return suggestedPort;
      }
    }
    throw new RuntimeException("Unable to find a free port");
  }
  



  public static final int HIGHEST_PORT = 65535;
  

  public static final int START_OF_USER_PORTS = 1024;
  

  private static int createAcceptablePort()
  {
    synchronized (random)
    {


      int freeAbove = 65535 - ephemeralRangeDetector.getHighestEphemeralPort();
      int freeBelow = Math.max(0, ephemeralRangeDetector.getLowestEphemeralPort() - 1024);
      int LAST_PORT;
      int FIRST_PORT; int LAST_PORT; if (freeAbove > freeBelow) {
        int FIRST_PORT = ephemeralRangeDetector.getHighestEphemeralPort();
        LAST_PORT = 65535;
      } else {
        FIRST_PORT = 1024;
        LAST_PORT = ephemeralRangeDetector.getLowestEphemeralPort();
      }
      
      if (FIRST_PORT == LAST_PORT) {
        return FIRST_PORT;
      }
      if (FIRST_PORT > LAST_PORT) {
        throw new UnsupportedOperationException("Could not find ephemeral port to use");
      }
      int randomInt = random.nextInt();
      int portWithoutOffset = Math.abs(randomInt % (LAST_PORT - FIRST_PORT + 1));
      return portWithoutOffset + FIRST_PORT;
    }
  }
  
  private static int checkPortIsFree(int port)
  {
    try {
      ServerSocket socket = new ServerSocket();
      socket.setReuseAddress(true);
      socket.bind(new InetSocketAddress("localhost", port));
      int localPort = socket.getLocalPort();
      socket.close();
      return localPort;
    } catch (IOException e) {}
    return -1;
  }
  
  public static boolean pollPort(int port)
  {
    return pollPort(port, 15, TimeUnit.SECONDS);
  }
  
  public static boolean pollPort(int port, int timeout, TimeUnit unit) {
    long end = System.currentTimeMillis() + unit.toMillis(timeout);
    while (System.currentTimeMillis() < end) {
      try {
        Socket socket = new Socket();
        socket.setReuseAddress(true);
        socket.bind(new InetSocketAddress("localhost", port));
        socket.close();
        return true;
      }
      catch (ConnectException localConnectException) {}catch (UnknownHostException e)
      {
        throw new RuntimeException(e);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    
    return false;
  }
  
  public static void waitForPortUp(int port, int timeout, TimeUnit unit) {
    long end = System.currentTimeMillis() + unit.toMillis(timeout);
    while (System.currentTimeMillis() < end) {
      try {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("localhost", port), 1000);
        socket.close();
        return;

      }
      catch (ConnectException localConnectException) {}catch (SocketTimeoutException localSocketTimeoutException) {}catch (IOException e)
      {

        throw new RuntimeException(e);
      }
    }
  }
  
  private PortProber() {}
}
