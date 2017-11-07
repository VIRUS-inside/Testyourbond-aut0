package org.openqa.selenium;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.internal.BuildInfo;



















public class WebDriverException
  extends RuntimeException
{
  public static final String SESSION_ID = "Session ID";
  public static final String DRIVER_INFO = "Driver info";
  protected static final String BASE_SUPPORT_URL = "http://seleniumhq.org/exceptions/";
  private static final String HOST_NAME;
  private static final String HOST_ADDRESS;
  private Map<String, String> extraInfo = new HashMap();
  





  static
  {
    String current = System.getProperty("os.name");
    String host = System.getenv("HOSTNAME");
    if (host == null) {
      host = System.getenv("COMPUTERNAME");
    }
    if ((host == null) && ("Mac OS X".equals(current))) {
      try {
        Process process = Runtime.getRuntime().exec("hostname");
        
        if (!process.waitFor(2L, TimeUnit.SECONDS)) {
          process.destroyForcibly();
        }
        if (process.exitValue() == 0) {
          InputStreamReader isr = new InputStreamReader(process.getInputStream());Throwable localThrowable6 = null;
          try { BufferedReader reader = new BufferedReader(isr);Throwable localThrowable7 = null;
            try { host = reader.readLine();
            }
            catch (Throwable localThrowable1)
            {
              localThrowable7 = localThrowable1;throw localThrowable1; } finally {} } catch (Throwable localThrowable4) { localThrowable6 = localThrowable4;throw localThrowable4;
          }
          finally {
            if (isr != null) if (localThrowable6 != null) try { isr.close(); } catch (Throwable localThrowable5) { localThrowable6.addSuppressed(localThrowable5); } else isr.close();
          }
        }
      } catch (InterruptedException e) { Thread.currentThread().interrupt();
        throw new RuntimeException(e);
      }
      catch (Exception localException1) {}
    }
    
    if (host == null) {
      try
      {
        host = InetAddress.getLocalHost().getHostName();
      } catch (Exception e) {
        host = "Unknown";
      }
    }
    
    HOST_NAME = host;
    
    String address = null;
    
    if ("Mac OS X".equals(current)) {
      try {
        NetworkInterface en0 = NetworkInterface.getByName("en0");
        Object addresses = en0.getInetAddresses();
        if (((Enumeration)addresses).hasMoreElements()) {
          InetAddress inetAddress = (InetAddress)((Enumeration)addresses).nextElement();
          address = inetAddress.getHostAddress();
        }
      }
      catch (Exception localException2) {}
    }
    

    if (address == null) {
      try
      {
        address = InetAddress.getLocalHost().getHostAddress();
      } catch (Exception e) {
        address = "Unknown";
      }
    }
    
    HOST_ADDRESS = address;
  }
  



  public WebDriverException(String message)
  {
    super(message);
  }
  
  public WebDriverException(Throwable cause) {
    super(cause);
  }
  
  public WebDriverException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public String getMessage()
  {
    return (super.getCause() instanceof WebDriverException) ? 
      super.getMessage() : createMessage(super.getMessage());
  }
  
  private String createMessage(String originalMessageString)
  {
    String supportMessage = "For documentation on this error, please visit: " + getSupportUrl() + "\n";
    
    return (originalMessageString == null ? "" : new StringBuilder().append(originalMessageString).append("\n").toString()) + supportMessage + 
    
      getBuildInformation() + "\n" + 
      getSystemInformation() + 
      getAdditionalInformation();
  }
  
  public String getSystemInformation() {
    return String.format("System info: host: '%s', ip: '%s', os.name: '%s', os.arch: '%s', os.version: '%s', java.version: '%s'", new Object[] { HOST_NAME, HOST_ADDRESS, 
    

      System.getProperty("os.name"), 
      System.getProperty("os.arch"), 
      System.getProperty("os.version"), 
      System.getProperty("java.version") });
  }
  
  public String getSupportUrl() {
    return null;
  }
  
  public BuildInfo getBuildInformation() {
    return new BuildInfo();
  }
  
  public static String getDriverName(StackTraceElement[] stackTraceElements) {
    String driverName = "unknown";
    for (StackTraceElement e : stackTraceElements) {
      if (e.getClassName().endsWith("Driver")) {
        String[] bits = e.getClassName().split("\\.");
        driverName = bits[(bits.length - 1)];
      }
    }
    
    return driverName;
  }
  
  public void addInfo(String key, String value) {
    extraInfo.put(key, value);
  }
  
  public String getAdditionalInformation() {
    if (!extraInfo.containsKey("Driver info")) {
      extraInfo.put("Driver info", "driver.version: " + getDriverName(getStackTrace()));
    }
    
    String result = "";
    for (Map.Entry<String, String> entry : extraInfo.entrySet()) {
      if ((entry.getValue() != null) && (((String)entry.getValue()).startsWith((String)entry.getKey()))) {
        result = result + "\n" + (String)entry.getValue();
      } else {
        result = result + "\n" + (String)entry.getKey() + ": " + (String)entry.getValue();
      }
    }
    return result;
  }
  
  public WebDriverException() {}
}
