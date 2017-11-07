package org.openqa.selenium.firefox.internal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.firefox.ExtensionConnection;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.NotConnectedException;
import org.openqa.selenium.internal.Lock;
import org.openqa.selenium.io.CircularOutputStream;
import org.openqa.selenium.io.MultiOutputStream;
import org.openqa.selenium.logging.LocalLogs;
import org.openqa.selenium.logging.NeedsLocalLogs;
import org.openqa.selenium.net.NetworkUtils;
import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.Response;





















public class NewProfileExtensionConnection
  implements ExtensionConnection, NeedsLocalLogs
{
  private static final NetworkUtils networkUtils = new NetworkUtils();
  
  private final long connectTimeout;
  
  private final FirefoxBinary process;
  
  private final FirefoxProfile profile;
  private final String host;
  private final Lock lock;
  private File profileDir;
  private HttpCommandExecutor delegate;
  private LocalLogs logs = LocalLogs.getNullLogger();
  
  public NewProfileExtensionConnection(Lock lock, FirefoxBinary binary, FirefoxProfile profile, String host) throws Exception
  {
    this.host = host;
    connectTimeout = binary.getTimeout();
    this.lock = lock;
    this.profile = profile;
    process = binary;
  }
  
  public void start() throws IOException {
    addWebDriverExtensionIfNeeded();
    
    int port = 0;
    
    lock.lock(connectTimeout);
    try {
      port = determineNextFreePort(profile.getIntegerPreference("webdriver_firefox_port", 7055));
      profile.setPreference("webdriver_firefox_port", port);
      
      profileDir = profile.layoutOnDisk();
      
      delegate = new HttpCommandExecutor(buildUrl(host, port));
      delegate.setLocalLogs(logs);
      String firefoxLogFile = System.getProperty("webdriver.firefox.logfile");
      
      if (firefoxLogFile != null) {
        if ("/dev/stdout".equals(firefoxLogFile)) {
          process.setOutputWatcher(System.out);
        } else {
          process.setOutputWatcher(new MultiOutputStream(new CircularOutputStream(), new FileOutputStream(firefoxLogFile)));
        }
      }
      

      process.startProfile(profile, profileDir, new String[] { "-foreground" });
      







      long waitUntil = System.currentTimeMillis() + connectTimeout;
      while (!isConnected()) {
        if (waitUntil < System.currentTimeMillis())
        {
          throw new NotConnectedException(delegate.getAddressOfRemoteServer(), connectTimeout, process.getConsoleOutput());
        }
        try
        {
          Thread.sleep(100L);
        }
        catch (InterruptedException localInterruptedException) {}
      }
    }
    catch (IOException e) {
      e.printStackTrace();
      
      throw new WebDriverException(String.format("Failed to connect to binary %s on port %d; process output follows: \n%s", new Object[] {process
        .toString(), Integer.valueOf(port), process.getConsoleOutput() }), e);
    }
    catch (WebDriverException e) {
      throw new WebDriverException(String.format("Failed to connect to binary %s on port %d; process output follows: \n%s", new Object[] {process
        .toString(), Integer.valueOf(port), process.getConsoleOutput() }), e);
    } catch (Exception e) {
      throw new WebDriverException(e);
    } finally {
      lock.unlock();
    }
  }
  
  protected void addWebDriverExtensionIfNeeded() {
    if (profile.containsWebDriverExtension()) {
      return;
    }
    profile.addExtension("webdriver", (Extension)loadCustomExtension().orElse(loadDefaultExtension()));
  }
  
  private static Optional<Extension> loadCustomExtension() {
    String xpiProperty = System.getProperty("webdriver.firefox.driver");
    if (xpiProperty != null) {
      File xpi = new File(xpiProperty);
      return Optional.of(new FileExtension(xpi));
    }
    return Optional.empty();
  }
  
  private static Extension loadDefaultExtension() {
    return new ClasspathExtension(FirefoxProfile.class, "/" + FirefoxProfile.class
      .getPackage().getName().replace(".", "/") + "/webdriver.xpi");
  }
  
  public Response execute(Command command) throws IOException {
    return delegate.execute(command);
  }
  



  protected int determineNextFreePort(int port)
  {
    for (int newport = port; newport < port + 2000; newport++) {
      Socket socket = new Socket();
      InetSocketAddress address = new InetSocketAddress(networkUtils.obtainLoopbackIp4Address(), newport);
      
      try
      {
        socket.bind(address);
        return newport;
      }
      catch (IOException localIOException4) {}finally
      {
        try {
          socket.close();
        }
        catch (IOException localIOException3) {}
      }
    }
    


    throw new WebDriverException(String.format("Cannot find free port in the range %d to %d ", new Object[] {Integer.valueOf(port), Integer.valueOf(newport) }));
  }
  

  public void quit()
  {
    process.quit();
    if (profileDir != null) {
      profile.clean(profileDir);
    }
  }
  







  private static URL buildUrl(String host, int port)
  {
    String hostToUse = "localhost".equals(host) ? networkUtils.obtainLoopbackIp4Address() : host;
    try {
      return new URL("http", hostToUse, port, "/hub");
    } catch (MalformedURLException e) {
      throw new WebDriverException(e);
    }
  }
  
  public boolean isConnected()
  {
    try {
      delegate.getAddressOfRemoteServer().openConnection().connect();
      return true;
    }
    catch (IOException e) {}
    return false;
  }
  
  public void setLocalLogs(LocalLogs logs)
  {
    if (delegate != null) {
      delegate.setLocalLogs(logs);
    }
    this.logs = logs;
  }
  
  public URI getAddressOfRemoteServer() {
    try {
      return delegate.getAddressOfRemoteServer().toURI();
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }
}
