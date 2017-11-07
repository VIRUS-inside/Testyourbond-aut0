package org.openqa.grid.web.utils;

import com.google.common.collect.Maps;
import java.io.InputStream;
import java.util.Map;
import org.openqa.grid.internal.Registry;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;






















public class BrowserNameUtils
{
  public BrowserNameUtils() {}
  
  public static Map<String, Object> parseGrid2Environment(String environment)
  {
    Map<String, Object> ret = Maps.newHashMap();
    
    String[] details = environment.split(" ");
    if (details.length == 1)
    {
      ret.put("browserName", details[0]);
    }
    else
    {
      ret.put("browserName", details[0]);
      if (details.length == 3) {
        ret.put("platform", Platform.extractFromSysProperty(details[2]));
      }
    }
    
    return ret;
  }
  
  public static String consoleIconName(DesiredCapabilities cap, Registry registry) {
    String browserString = cap.getBrowserName();
    if ((browserString == null) || ("".equals(browserString))) {
      return "missingBrowserName";
    }
    
    String ret = browserString;
    

    if ((browserString.contains("iexplore")) || (browserString.startsWith("*iehta"))) {
      ret = "internet explorer";
    } else if ((browserString.contains("firefox")) || (browserString.startsWith("*chrome"))) {
      if (((cap.getVersion() != null) && (cap.getVersion().toLowerCase().equals("beta"))) || 
        (cap.getBrowserName().toLowerCase().contains("beta"))) {
        ret = "firefoxbeta";
      } else if (((cap.getVersion() != null) && (cap.getVersion().toLowerCase().equals("aurora"))) || 
        (cap.getBrowserName().toLowerCase().contains("aurora"))) {
        ret = "aurora";
      } else if (((cap.getVersion() != null) && (cap.getVersion().toLowerCase().equals("nightly"))) || 
        (cap.getBrowserName().toLowerCase().contains("nightly"))) {
        ret = "nightly";
      } else {
        ret = "firefox";
      }
    }
    else if (browserString.startsWith("*safari")) {
      ret = "safari";
    } else if (browserString.startsWith("*googlechrome")) {
      ret = "chrome";
    } else if (browserString.startsWith("opera")) {
      ret = "opera";
    } else if (browserString.toLowerCase().contains("edge")) {
      ret = "MicrosoftEdge";
    }
    
    return ret.replace(" ", "_");
  }
  









  public static String getConsoleIconPath(DesiredCapabilities cap, Registry registry)
  {
    String name = consoleIconName(cap, registry);
    String path = "org/openqa/grid/images/";
    

    InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path + name + ".png");
    if (in == null) {
      return null;
    }
    return "/grid/resources/" + path + name + ".png";
  }
}
