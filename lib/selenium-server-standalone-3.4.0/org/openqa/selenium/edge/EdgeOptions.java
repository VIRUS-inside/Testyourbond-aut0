package org.openqa.selenium.edge;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import java.io.IOException;
import org.openqa.selenium.remote.DesiredCapabilities;























































public class EdgeOptions
{
  public static final String CAPABILITY = "edgeOptions";
  private String pageLoadStrategy;
  
  public EdgeOptions() {}
  
  public void setPageLoadStrategy(String strategy)
  {
    pageLoadStrategy = ((String)Preconditions.checkNotNull(strategy));
  }
  




  public JsonObject toJson()
    throws IOException
  {
    JsonObject options = new JsonObject();
    if (pageLoadStrategy != null) {
      options.addProperty("pageLoadStrategy", pageLoadStrategy);
    }
    
    return options;
  }
  






  DesiredCapabilities toCapabilities()
  {
    DesiredCapabilities capabilities = DesiredCapabilities.edge();
    if (pageLoadStrategy != null) {
      capabilities.setCapability("pageLoadStrategy", pageLoadStrategy);
    }
    
    return capabilities;
  }
}
