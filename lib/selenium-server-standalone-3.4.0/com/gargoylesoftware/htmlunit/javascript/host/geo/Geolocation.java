package com.gargoylesoftware.htmlunit.javascript.host.geo;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.background.BackgroundJavaScriptFactory;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJob;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClasses;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



























@JsxClasses({@com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(isJSObject=false, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})})
public class Geolocation
  extends SimpleScriptable
{
  private static final Log LOG = LogFactory.getLog(Geolocation.class);
  

  private static String PROVIDER_URL_ = "https://maps.googleapis.com/maps/api/browserlocation/json";
  


  private Function successHandler_;
  


  private Function errorHandler_;
  



  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public Geolocation() {}
  



  @JsxFunction
  public void getCurrentPosition(Function successCallback, Object errorCallback, Object options)
  {
    successHandler_ = successCallback;
    if ((errorCallback instanceof Function)) {
      errorHandler_ = ((Function)errorCallback);
    }
    else {
      errorHandler_ = null;
    }
    WebWindow webWindow = getWindow().getWebWindow();
    if (webWindow.getWebClient().getOptions().isGeolocationEnabled()) {
      JavaScriptJob job = BackgroundJavaScriptFactory.theFactory()
        .createJavaScriptJob(0, null, new Runnable()
        {
          public void run() {
            Geolocation.this.doGetPosition();
          }
        });
      webWindow.getJobManager().addJob(job, getWindow().getWebWindow().getEnclosedPage());
    }
  }
  







  @JsxFunction
  public int watchPosition(Function successCallback, Object errorCallback, Object options)
  {
    return 0;
  }
  


  @JsxFunction
  public void clearWatch(int watchId) {}
  


  private void doGetPosition()
  {
    String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);
    String wifiStringString = null;
    if (os.contains("win")) {
      wifiStringString = getWifiStringWindows();
    }
    if (wifiStringString != null) {
      String url = PROVIDER_URL_;
      if (url.contains("?")) {
        url = url + '&';
      }
      else {
        url = url + '?';
      }
      url = url + "browser=firefox&sensor=true";
      url = url + wifiStringString;
      
      while (url.length() >= 1900) {
        url = url.substring(0, url.lastIndexOf("&wifi="));
      }
      
      if (LOG.isInfoEnabled()) {
        LOG.info("Invoking URL: " + url);
      }
      try {
        Object localObject1 = null;Object localObject4 = null; Object localObject3; label463: try { webClient = new WebClient(BrowserVersion.FIREFOX_45);
        }
        finally
        {
          WebClient webClient;
          
          Page page;
          
          String content;
          
          double latitude;
          
          double longitude;
          
          double accuracy;
          
          Coordinates coordinates;
          Position position;
          JavaScriptEngine jsEngine;
          localObject3 = localThrowable; break label463; if (localObject3 != localThrowable) localObject3.addSuppressed(localThrowable);
        }
      } catch (Exception e) { LOG.error("", e);
      }
    }
    else {
      LOG.error("Operating System not supported: " + os);
    }
  }
  
  private static String getJSONValue(String content, String key) {
    StringBuilder builder = new StringBuilder();
    int index = content.indexOf("\"" + key + "\"") + key.length() + 2;
    for (index = content.indexOf(':', index) + 1; index < content.length(); index++) {
      char ch = content.charAt(index);
      if ((ch == ',') || (ch == '}')) {
        break;
      }
      builder.append(ch);
    }
    return builder.toString().trim();
  }
  
  String getWifiStringWindows() {
    StringBuilder builder = new StringBuilder();
    try {
      List<String> lines = runCommand("netsh wlan show networks mode=bssid");
      for (Iterator<String> it = lines.iterator(); it.hasNext();) {
        String line = (String)it.next();
        if (line.startsWith("SSID ")) {
          String name = line.substring(line.lastIndexOf(' ') + 1);
          if (it.hasNext()) {
            it.next();
          }
          if (it.hasNext()) {
            it.next();
          }
          if (it.hasNext()) {
            it.next();
          }
          while (it.hasNext()) {
            line = (String)it.next();
            if (line.trim().startsWith("BSSID ")) {
              String mac = line.substring(line.lastIndexOf(' ') + 1);
              if (it.hasNext()) {
                line = ((String)it.next()).trim();
                if (line.startsWith("Signal")) {
                  String signal = line.substring(line.lastIndexOf(' ') + 1, line.length() - 1);
                  int signalStrength = Integer.parseInt(signal) / 2 - 100;
                  builder.append("&wifi=")
                    .append("mac:")
                    .append(mac.replace(':', '-'))
                    .append("%7C")
                    .append("ssid:")
                    .append(name)
                    .append("%7C")
                    .append("ss:")
                    .append(signalStrength);
                }
              }
            }
            if (line.trim().isEmpty()) {
              break;
            }
          }
        }
      }
    }
    catch (IOException localIOException) {}
    

    return builder.toString();
  }
  
  private static List<String> runCommand(String command) throws IOException {
    List<String> list = new ArrayList();
    Process p = Runtime.getRuntime().exec(command);
    Object localObject1 = null;Object localObject4 = null; Object localObject3; label114: try { reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
    } finally {
      BufferedReader reader;
      String line;
      String line;
      localObject3 = localThrowable; break label114; if (localObject3 != localThrowable) localObject3.addSuppressed(localThrowable); }
    return list;
  }
  
  static void setProviderUrl(String url) {
    PROVIDER_URL_ = url;
  }
}
