package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.PluginConfiguration;
import com.gargoylesoftware.htmlunit.PluginConfiguration.MimeType;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.geo.Geolocation;
import java.util.Iterator;
import java.util.Set;































@JsxClass
public class Navigator
  extends SimpleScriptable
{
  private PluginArray plugins_;
  private MimeTypeArray mimeTypes_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public Navigator() {}
  
  @JsxGetter
  public String getAppCodeName()
  {
    return getBrowserVersion().getApplicationCodeName();
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public String getAppMinorVersion()
  {
    return getBrowserVersion().getApplicationMinorVersion();
  }
  



  @JsxGetter
  public String getAppName()
  {
    return getBrowserVersion().getApplicationName();
  }
  



  @JsxGetter
  public String getAppVersion()
  {
    return getBrowserVersion().getApplicationVersion();
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public String getBrowserLanguage()
  {
    return getLanguage();
  }
  



  @JsxGetter
  public String getLanguage()
  {
    return getBrowserVersion().getBrowserLanguage();
  }
  



  @JsxGetter
  public boolean getCookieEnabled()
  {
    return getWindow().getWebWindow().getWebClient().getCookieManager().isCookiesEnabled();
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public String getCpuClass()
  {
    return getBrowserVersion().getCpuClass();
  }
  



  @JsxGetter
  public boolean getOnLine()
  {
    return getBrowserVersion().isOnLine();
  }
  



  @JsxGetter
  public String getPlatform()
  {
    return getBrowserVersion().getPlatform();
  }
  



  @JsxGetter
  public String getProduct()
  {
    return "Gecko";
  }
  




  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public String getProductSub()
  {
    return "20100215";
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public String getSystemLanguage()
  {
    return getBrowserVersion().getSystemLanguage();
  }
  



  @JsxGetter
  public String getUserAgent()
  {
    return getBrowserVersion().getUserAgent();
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public String getUserLanguage()
  {
    return getBrowserVersion().getUserLanguage();
  }
  



  @JsxGetter
  public Object getPlugins()
  {
    initPlugins();
    return plugins_;
  }
  
  private void initPlugins() {
    if (plugins_ != null) {
      return;
    }
    plugins_ = new PluginArray();
    plugins_.setParentScope(this);
    plugins_.setPrototype(getPrototype(PluginArray.class));
    
    mimeTypes_ = new MimeTypeArray();
    mimeTypes_.setParentScope(this);
    mimeTypes_.setPrototype(getPrototype(MimeTypeArray.class));
    Iterator localIterator2;
    for (Iterator localIterator1 = getBrowserVersion().getPlugins().iterator(); localIterator1.hasNext(); 
        





        localIterator2.hasNext())
    {
      PluginConfiguration pluginConfig = (PluginConfiguration)localIterator1.next();
      Plugin plugin = new Plugin(pluginConfig.getName(), pluginConfig.getDescription(), 
        pluginConfig.getVersion(), pluginConfig.getFilename());
      plugin.setParentScope(this);
      plugin.setPrototype(getPrototype(Plugin.class));
      plugins_.add(plugin);
      
      localIterator2 = pluginConfig.getMimeTypes().iterator(); continue;PluginConfiguration.MimeType mimeTypeConfig = (PluginConfiguration.MimeType)localIterator2.next();
      MimeType mimeType = new MimeType(mimeTypeConfig.getType(), mimeTypeConfig.getDescription(), 
        mimeTypeConfig.getSuffixes(), plugin);
      mimeType.setParentScope(this);
      mimeType.setPrototype(getPrototype(MimeType.class));
      mimeTypes_.add(mimeType);
      plugin.add(mimeType);
    }
  }
  




  @JsxGetter
  public Object getMimeTypes()
  {
    initPlugins();
    return mimeTypes_;
  }
  



  @JsxFunction
  public boolean javaEnabled()
  {
    return getWindow().getWebWindow().getWebClient().getOptions().isAppletEnabled();
  }
  



  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public boolean taintEnabled()
  {
    return false;
  }
  



  @JsxGetter
  public Geolocation getGeolocation()
  {
    Geolocation geolocation = new Geolocation();
    geolocation.setPrototype(getPrototype(geolocation.getClass()));
    geolocation.setParentScope(getParentScope());
    return geolocation;
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public String getBuildID()
  {
    return getBrowserVersion().getBuildId();
  }
  



  @JsxGetter
  public String getVendor()
  {
    return getBrowserVersion().getVendor();
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public String getVendorSub()
  {
    return "";
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public String getDoNotTrack()
  {
    if (getWindow().getWebWindow().getWebClient().getOptions().isDoNotTrackEnabled()) {
      return "yes";
    }
    return "unspecified";
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public String getOscpu()
  {
    return "Windows NT 6.1";
  }
}
