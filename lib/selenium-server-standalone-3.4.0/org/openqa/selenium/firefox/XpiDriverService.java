package org.openqa.selenium.firefox;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.firefox.internal.ClasspathExtension;
import org.openqa.selenium.firefox.internal.Extension;
import org.openqa.selenium.firefox.internal.FileExtension;
import org.openqa.selenium.remote.service.DriverService;
import org.openqa.selenium.remote.service.DriverService.Builder;





















public class XpiDriverService
  extends DriverService
{
  private final Lock lock = new ReentrantLock();
  

  private final int port;
  
  private final FirefoxBinary binary;
  
  private final FirefoxProfile profile;
  
  private File profileDir;
  

  private XpiDriverService(File executable, int port, ImmutableList<String> args, ImmutableMap<String, String> environment, FirefoxBinary binary, FirefoxProfile profile)
    throws IOException
  {
    super(executable, port, args, environment);
    
    Preconditions.checkState(port > 0, "Port must be set");
    
    this.port = port;
    this.binary = binary;
    this.profile = profile;
    
    String firefoxLogFile = System.getProperty("webdriver.firefox.logfile");
    
    if (firefoxLogFile != null) {
      if ("/dev/stdout".equals(firefoxLogFile)) {
        sendOutputTo(System.out);
      } else {
        sendOutputTo(new FileOutputStream(firefoxLogFile));
      }
    }
  }
  
  protected URL getUrl(int port) throws IOException
  {
    return new URL("http", "localhost", port, "/hub");
  }
  
  public void start() throws IOException
  {
    lock.lock();
    try {
      profile.setPreference("webdriver_firefox_port", port);
      addWebDriverExtension(profile);
      profileDir = profile.layoutOnDisk();
      
      binary.setOutputWatcher(getOutputStream());
      
      binary.startProfile(profile, profileDir, new String[] { "-foreground" });
      
      waitUntilAvailable();
      
      lock.unlock(); } finally { lock.unlock();
    }
  }
  
  public void stop()
  {
    lock.lock();
    try {
      binary.quit();
      profile.cleanTemporaryModel();
      profile.clean(profileDir);
      
      lock.unlock(); } finally { lock.unlock();
    }
  }
  
  private void addWebDriverExtension(FirefoxProfile profile) {
    if (profile.containsWebDriverExtension()) {
      return;
    }
    profile.addExtension("webdriver", (Extension)loadCustomExtension().orElse(loadDefaultExtension()));
  }
  
  private Optional<Extension> loadCustomExtension() {
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
  





  public static XpiDriverService createDefaultService()
  {
    try
    {
      return (XpiDriverService)((Builder)new Builder(null).usingAnyFreePort()).build();
    } catch (WebDriverException e) {
      throw new IllegalStateException(e.getMessage(), e.getCause());
    }
  }
  
  public static Builder builder() {
    return new Builder(null);
  }
  
  public static class Builder extends DriverService.Builder<XpiDriverService, Builder>
  {
    private FirefoxBinary binary = null;
    private FirefoxProfile profile = null;
    

    private Builder() {}
    
    public Builder withBinary(FirefoxBinary binary)
    {
      this.binary = ((FirefoxBinary)Preconditions.checkNotNull(binary));
      return this;
    }
    
    public Builder withProfile(FirefoxProfile profile) {
      this.profile = ((FirefoxProfile)Preconditions.checkNotNull(profile));
      return this;
    }
    
    protected File findDefaultExecutable()
    {
      return new FirefoxBinary().getFile();
    }
    
    protected ImmutableList<String> createArgs()
    {
      return ImmutableList.of("-foreground");
    }
    



    protected XpiDriverService createDriverService(File exe, int port, ImmutableList<String> args, ImmutableMap<String, String> environment)
    {
      try
      {
        return new XpiDriverService(exe, port, args, environment, binary == null ? new FirefoxBinary() : binary, profile == null ? new FirefoxProfile() : profile, null);


      }
      catch (IOException e)
      {


        throw new WebDriverException(e);
      }
    }
  }
}
