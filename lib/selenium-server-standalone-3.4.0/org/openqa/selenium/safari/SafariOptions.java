package org.openqa.selenium.safari;

import com.google.common.base.Objects;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.Map;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;





















































public class SafariOptions
{
  public static final String CAPABILITY = "safari.options";
  private int port = 0;
  



  private boolean useCleanSession = false;
  



  private boolean useTechnologyPreview = false;
  



  public SafariOptions() {}
  


  public static SafariOptions fromCapabilities(Capabilities capabilities)
    throws WebDriverException
  {
    Object cap = capabilities.getCapability("safari.options");
    if ((cap instanceof SafariOptions))
      return (SafariOptions)cap;
    if ((cap instanceof Map)) {
      try {
        return fromJsonMap((Map)cap);
      } catch (IOException e) {
        throw new WebDriverException(e);
      }
    }
    return new SafariOptions();
  }
  









  public void setPort(int port)
  {
    this.port = port;
  }
  









  public void setUseCleanSession(boolean useCleanSession)
  {
    this.useCleanSession = useCleanSession;
  }
  






  public void setUseTechnologyPreview(boolean useTechnologyPreview)
  {
    this.useTechnologyPreview = useTechnologyPreview;
  }
  






  public int getPort()
  {
    return port;
  }
  



  public boolean getUseCleanSession()
  {
    return useCleanSession;
  }
  
  public boolean getUseTechnologyPreview() {
    return useTechnologyPreview;
  }
  






  public JsonObject toJson()
    throws IOException
  {
    JsonObject options = new JsonObject();
    options.addProperty("port", Integer.valueOf(port));
    options.addProperty("cleanSession", Boolean.valueOf(useCleanSession));
    options.addProperty("technologyPreview", Boolean.valueOf(useTechnologyPreview));
    return options;
  }
  







  private static SafariOptions fromJsonMap(Map<?, ?> options)
    throws IOException
  {
    SafariOptions safariOptions = new SafariOptions();
    
    Number port = (Number)options.get("port");
    if (port != null) {
      safariOptions.setPort(port.intValue());
    }
    
    Boolean useCleanSession = (Boolean)options.get("cleanSession");
    if (useCleanSession != null) {
      safariOptions.setUseCleanSession(useCleanSession.booleanValue());
    }
    
    Boolean useTechnologyPreview = (Boolean)options.get("technologyPreview");
    if (useTechnologyPreview != null) {
      safariOptions.setUseTechnologyPreview(useTechnologyPreview.booleanValue());
    }
    
    return safariOptions;
  }
  






  DesiredCapabilities toCapabilities()
  {
    DesiredCapabilities capabilities = DesiredCapabilities.safari();
    capabilities.setCapability("safari.options", this);
    return capabilities;
  }
  
  public boolean equals(Object other)
  {
    if (!(other instanceof SafariOptions)) {
      return false;
    }
    SafariOptions that = (SafariOptions)other;
    return (port == port) && (useCleanSession == useCleanSession) && (useTechnologyPreview == useTechnologyPreview);
  }
  


  public int hashCode()
  {
    return Objects.hashCode(new Object[] { Integer.valueOf(port), Boolean.valueOf(useCleanSession), Boolean.valueOf(useTechnologyPreview) });
  }
  
  private static class Option
  {
    private static final String CLEAN_SESSION = "cleanSession";
    private static final String TECHNOLOGY_PREVIEW = "technologyPreview";
    private static final String PORT = "port";
    
    private Option() {}
  }
}
