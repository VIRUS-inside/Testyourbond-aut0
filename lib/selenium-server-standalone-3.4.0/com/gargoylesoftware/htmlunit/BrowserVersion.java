package com.gargoylesoftware.htmlunit;

import com.gargoylesoftware.htmlunit.javascript.configuration.BrowserFeature;
import com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
































































































public class BrowserVersion
  implements Serializable, Cloneable
{
  private static final String NETSCAPE = "Netscape";
  private static final String LANGUAGE_ENGLISH_US = "en-US";
  private static final String CPU_CLASS_X86 = "x86";
  private static final String PLATFORM_WIN32 = "Win32";
  public static final BrowserVersion FIREFOX_45 = new BrowserVersion(
    "Netscape", "5.0 (Windows)", 
    "Mozilla/5.0 (Windows NT 6.1; rv:45.0) Gecko/20100101 Firefox/45.0", 
    45, "FF45", null);
  




  public static final BrowserVersion FIREFOX_52 = new BrowserVersion(
    "Netscape", "5.0 (Windows)", 
    "Mozilla/5.0 (Windows NT 6.1; rv:52.0) Gecko/20100101 Firefox/52.0", 
    52, "FF52", null);
  

  public static final BrowserVersion INTERNET_EXPLORER = new BrowserVersion(
    "Netscape", "5.0 (Windows NT 6.1; Trident/7.0; rv:11.0) like Gecko", 
    "Mozilla/5.0 (Windows NT 6.1; Trident/7.0; rv:11.0) like Gecko", 11, "IE", null);
  

  public static final BrowserVersion CHROME = new BrowserVersion(
    "Netscape", "5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36", 
    
    "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36", 
    
    57, "Chrome", null);
  

  public static final BrowserVersion EDGE = new BrowserVersion(
    "Netscape", "5.0 (Windows NT 10.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586", 
    
    "Mozilla/5.0 (Windows NT 10.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586", 
    
    13, "Edge", null);
  



  public static final BrowserVersion BEST_SUPPORTED = CHROME;
  

  private static BrowserVersion DefaultBrowserVersion_ = BEST_SUPPORTED;
  

  static
  {
    FIREFOX_45.initDefaultFeatures();
    FIREFOX_45.setVendor("");
    FIREFOX_45buildId_ = "20161129180326";
    FIREFOX_45.setHeaderNamesOrdered(new String[] {
      "Host", "User-Agent", "Accept", "Accept-Language", "Accept-Encoding", "Referer", "Cookie", "Connection" });
    FIREFOX_45.setHtmlAcceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
    FIREFOX_45.setXmlHttpRequestAcceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
    FIREFOX_45.setImgAcceptHeader("image/png,image/*;q=0.8,*/*;q=0.5");
    FIREFOX_45.setCssAcceptHeader("text/css,*/*;q=0.1");
    

    FIREFOX_52.initDefaultFeatures();
    FIREFOX_52.setVendor("");
    FIREFOX_52buildId_ = "20170303022339";
    FIREFOX_52.setHeaderNamesOrdered(new String[] {
      "Host", "User-Agent", "Accept", "Accept-Language", "Accept-Encoding", "Referer", "Cookie", "Connection" });
    FIREFOX_52.setHtmlAcceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
    FIREFOX_52.setXmlHttpRequestAcceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
    FIREFOX_52.setImgAcceptHeader("image/png,image/*;q=0.8,*/*;q=0.5");
    FIREFOX_52.setCssAcceptHeader("text/css,*/*;q=0.1");
    

    INTERNET_EXPLORER.initDefaultFeatures();
    INTERNET_EXPLORER.setVendor("");
    INTERNET_EXPLORER.setHeaderNamesOrdered(new String[] {
      "Accept", "Referer", "Accept-Language", "User-Agent", "Accept-Encoding", "Host", "DNT", "Connection", 
      "Cookie" });
    INTERNET_EXPLORER.setHtmlAcceptHeader("text/html, application/xhtml+xml, */*");
    INTERNET_EXPLORER.setImgAcceptHeader("image/png, image/svg+xml, image/*;q=0.8, */*;q=0.5");
    INTERNET_EXPLORER.setCssAcceptHeader("text/css, */*");
    INTERNET_EXPLORER.setScriptAcceptHeader("application/javascript, */*;q=0.8");
    

    EDGE.initDefaultFeatures();
    EDGE.setVendor("");
    

    CHROME.initDefaultFeatures();
    CHROME.setApplicationCodeName("Mozilla");
    CHROME.setVendor("Google Inc.");
    CHROME.setPlatform("MacIntel");
    CHROME.setCpuClass(null);
    CHROME.setHeaderNamesOrdered(new String[] {
      "Host", "Connection", "Accept", "User-Agent", "Referer", "Accept-Encoding", "Accept-Language", "Cookie" });
    CHROME.setHtmlAcceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
    CHROME.setImgAcceptHeader("image/webp,image/*,*/*;q=0.8");
    CHROME.setCssAcceptHeader("text/css,*/*;q=0.1");
    CHROME.setScriptAcceptHeader("*/*");
    


    CHROME.registerUploadMimeType("html", "text/html");
    CHROME.registerUploadMimeType("htm", "text/html");
    CHROME.registerUploadMimeType("css", "text/css");
    CHROME.registerUploadMimeType("xml", "text/xml");
    CHROME.registerUploadMimeType("gif", "image/gif");
    CHROME.registerUploadMimeType("jpeg", "image/jpeg");
    CHROME.registerUploadMimeType("jpg", "image/jpeg");
    CHROME.registerUploadMimeType("webp", "image/webp");
    CHROME.registerUploadMimeType("mp4", "video/mp4");
    CHROME.registerUploadMimeType("m4v", "video/mp4");
    CHROME.registerUploadMimeType("m4a", "audio/x-m4a");
    CHROME.registerUploadMimeType("mp3", "audio/mp3");
    CHROME.registerUploadMimeType("ogv", "video/ogg");
    CHROME.registerUploadMimeType("ogm", "video/ogg");
    CHROME.registerUploadMimeType("ogg", "audio/ogg");
    CHROME.registerUploadMimeType("oga", "audio/ogg");
    CHROME.registerUploadMimeType("opus", "audio/ogg");
    CHROME.registerUploadMimeType("webm", "video/webm");
    CHROME.registerUploadMimeType("wav", "audio/wav");
    CHROME.registerUploadMimeType("flac", "audio/flac");
    CHROME.registerUploadMimeType("xhtml", "application/xhtml+xml");
    CHROME.registerUploadMimeType("xht", "application/xhtml+xml");
    CHROME.registerUploadMimeType("xhtm", "application/xhtml+xml");
    CHROME.registerUploadMimeType("txt", "text/plain");
    CHROME.registerUploadMimeType("text", "text/plain");
    
    FIREFOX_45.registerUploadMimeType("html", "text/html");
    FIREFOX_45.registerUploadMimeType("htm", "text/html");
    FIREFOX_45.registerUploadMimeType("css", "text/css");
    FIREFOX_45.registerUploadMimeType("xml", "text/xml");
    FIREFOX_45.registerUploadMimeType("gif", "image/gif");
    FIREFOX_45.registerUploadMimeType("jpeg", "image/jpeg");
    FIREFOX_45.registerUploadMimeType("jpg", "image/jpeg");
    FIREFOX_45.registerUploadMimeType("mp4", "video/mp4");
    FIREFOX_45.registerUploadMimeType("m4v", "video/mp4");
    FIREFOX_45.registerUploadMimeType("m4a", "audio/mp4");
    FIREFOX_45.registerUploadMimeType("mp3", "audio/mpeg");
    FIREFOX_45.registerUploadMimeType("ogv", "video/ogg");
    FIREFOX_45.registerUploadMimeType("ogm", "video/x-ogm");
    FIREFOX_45.registerUploadMimeType("ogg", "video/ogg");
    FIREFOX_45.registerUploadMimeType("oga", "audio/ogg");
    FIREFOX_45.registerUploadMimeType("opus", "audio/ogg");
    FIREFOX_45.registerUploadMimeType("webm", "video/webm");
    FIREFOX_45.registerUploadMimeType("wav", "audio/wav");
    FIREFOX_45.registerUploadMimeType("flac", "audio/x-flac");
    FIREFOX_45.registerUploadMimeType("xhtml", "application/xhtml+xml");
    FIREFOX_45.registerUploadMimeType("xht", "application/xhtml+xml");
    FIREFOX_45.registerUploadMimeType("txt", "text/plain");
    FIREFOX_45.registerUploadMimeType("text", "text/plain");
    
    INTERNET_EXPLORER.registerUploadMimeType("html", "text/html");
    INTERNET_EXPLORER.registerUploadMimeType("htm", "text/html");
    INTERNET_EXPLORER.registerUploadMimeType("css", "text/css");
    INTERNET_EXPLORER.registerUploadMimeType("xml", "text/xml");
    INTERNET_EXPLORER.registerUploadMimeType("gif", "image/gif");
    INTERNET_EXPLORER.registerUploadMimeType("jpeg", "image/jpeg");
    INTERNET_EXPLORER.registerUploadMimeType("jpg", "image/jpeg");
    INTERNET_EXPLORER.registerUploadMimeType("mp4", "video/mp4");
    INTERNET_EXPLORER.registerUploadMimeType("m4v", "video/mp4");
    INTERNET_EXPLORER.registerUploadMimeType("m4a", "audio/mp4");
    INTERNET_EXPLORER.registerUploadMimeType("mp3", "audio/mpeg");
    INTERNET_EXPLORER.registerUploadMimeType("ogm", "video/x-ogm");
    INTERNET_EXPLORER.registerUploadMimeType("ogg", "application/ogg");
    INTERNET_EXPLORER.registerUploadMimeType("wav", "audio/wav");
    INTERNET_EXPLORER.registerUploadMimeType("xhtml", "application/xhtml+xml");
    INTERNET_EXPLORER.registerUploadMimeType("xht", "application/xhtml+xml");
    INTERNET_EXPLORER.registerUploadMimeType("txt", "text/plain");
    

    PluginConfiguration flash = new PluginConfiguration("Shockwave Flash", 
      "Shockwave Flash 24.0 r0", "undefined", "internal-not-yet-present");
    flash.getMimeTypes().add(new PluginConfiguration.MimeType("application/x-shockwave-flash", 
      "Shockwave Flash", "swf"));
    CHROME.getPlugins().add(flash);
    
    flash = new PluginConfiguration("Shockwave Flash", 
      "Shockwave Flash 24.0 r0", "24.0.0.194", "NPSWF32_24_0_0_194.dll");
    flash.getMimeTypes().add(new PluginConfiguration.MimeType("application/x-shockwave-flash", 
      "Shockwave Flash", "swf"));
    FIREFOX_45.getPlugins().add(flash);
    
    flash = new PluginConfiguration("Shockwave Flash", 
      "Shockwave Flash 23.0 r0", "23.0.0.207", "Flash32_23_0_0_207.ocx");
    flash.getMimeTypes().add(new PluginConfiguration.MimeType("application/x-shockwave-flash", 
      "Shockwave Flash", "swf"));
    INTERNET_EXPLORER.getPlugins().add(flash);
    
    flash = new PluginConfiguration("Shockwave Flash", 
      "Shockwave Flash 18.0 r0", "18.0.0.232", "Flash.ocx");
    flash.getMimeTypes().add(new PluginConfiguration.MimeType("application/x-shockwave-flash", 
      "Shockwave Flash", "swf"));
    EDGE.getPlugins().add(flash);
  }
  
  private String applicationCodeName_ = "Mozilla";
  private String applicationMinorVersion_ = "0";
  private String applicationName_;
  private String applicationVersion_;
  private String buildId_;
  private String vendor_;
  private String browserLanguage_ = "en-US";
  private String cpuClass_ = "x86";
  private boolean onLine_ = true;
  private String platform_ = "Win32";
  private String systemLanguage_ = "en-US";
  private String userAgent_;
  private String userLanguage_ = "en-US";
  private int browserVersionNumeric_;
  private final Set<PluginConfiguration> plugins_ = new HashSet();
  private final Set<BrowserVersionFeatures> features_ = EnumSet.noneOf(BrowserVersionFeatures.class);
  private final String nickname_;
  private String htmlAcceptHeader_;
  private String imgAcceptHeader_;
  private String cssAcceptHeader_;
  private String scriptAcceptHeader_;
  private String xmlHttpRequestAcceptHeader_;
  private String[] headerNamesOrdered_;
  private Map<String, String> uploadMimeTypes_ = new HashMap();
  










  public BrowserVersion(String applicationName, String applicationVersion, String userAgent, int browserVersionNumeric)
  {
    this(applicationName, applicationVersion, userAgent, browserVersionNumeric, applicationName + browserVersionNumeric, null);
  }
  












  public BrowserVersion(String applicationName, String applicationVersion, String userAgent, int browserVersionNumeric, BrowserVersionFeatures[] features)
  {
    this(applicationName, applicationVersion, userAgent, browserVersionNumeric, applicationName + browserVersionNumeric, features);
  }
  













  private BrowserVersion(String applicationName, String applicationVersion, String userAgent, int browserVersionNumeric, String nickname, BrowserVersionFeatures[] features)
  {
    applicationName_ = applicationName;
    setApplicationVersion(applicationVersion);
    userAgent_ = userAgent;
    browserVersionNumeric_ = browserVersionNumeric;
    nickname_ = nickname;
    htmlAcceptHeader_ = "*/*";
    imgAcceptHeader_ = "*/*";
    cssAcceptHeader_ = "*/*";
    scriptAcceptHeader_ = "*/*";
    xmlHttpRequestAcceptHeader_ = "*/*";
    
    if (features != null)
      features_.addAll(Arrays.asList(features));
  }
  
  private void initDefaultFeatures() {
    String expectedBrowserName;
    String expectedBrowserName;
    if (isIE()) {
      expectedBrowserName = "IE";
    } else { String expectedBrowserName;
      if (isFirefox()) {
        expectedBrowserName = "FF";
      } else { String expectedBrowserName;
        if (isEdge()) {
          expectedBrowserName = "EDGE";
        }
        else
          expectedBrowserName = "CHROME";
      }
    }
    for (BrowserVersionFeatures features : BrowserVersionFeatures.values()) {
      try {
        Field field = BrowserVersionFeatures.class.getField(features.name());
        BrowserFeature browserFeature = (BrowserFeature)field.getAnnotation(BrowserFeature.class);
        if (browserFeature != null) {
          for (WebBrowser browser : browserFeature.value()) {
            if ((expectedBrowserName.equals(browser.value().name())) && 
              (browser.minVersion() <= getBrowserVersionNumeric()) && 
              (browser.maxVersion() >= getBrowserVersionNumeric())) {
              features_.add(features);
            }
          }
        }
      }
      catch (NoSuchFieldException e)
      {
        throw new IllegalStateException(e);
      }
    }
  }
  




  public static BrowserVersion getDefault()
  {
    return DefaultBrowserVersion_;
  }
  



  public static void setDefault(BrowserVersion newBrowserVersion)
  {
    WebAssert.notNull("newBrowserVersion", newBrowserVersion);
    DefaultBrowserVersion_ = newBrowserVersion;
  }
  




  public final boolean isIE()
  {
    return getNickname().startsWith("IE");
  }
  





  public final boolean isChrome()
  {
    return getNickname().startsWith("Chrome");
  }
  




  public final boolean isEdge()
  {
    return getNickname().startsWith("Edge");
  }
  




  public final boolean isFirefox()
  {
    return getNickname().startsWith("FF");
  }
  





  public String getApplicationCodeName()
  {
    return applicationCodeName_;
  }
  





  public String getApplicationMinorVersion()
  {
    return applicationMinorVersion_;
  }
  




  public String getApplicationName()
  {
    return applicationName_;
  }
  




  public String getApplicationVersion()
  {
    return applicationVersion_;
  }
  


  public String getVendor()
  {
    return vendor_;
  }
  





  public String getBrowserLanguage()
  {
    return browserLanguage_;
  }
  





  public String getCpuClass()
  {
    return cpuClass_;
  }
  





  public boolean isOnLine()
  {
    return onLine_;
  }
  





  public String getPlatform()
  {
    return platform_;
  }
  





  public String getSystemLanguage()
  {
    return systemLanguage_;
  }
  



  public String getUserAgent()
  {
    return userAgent_;
  }
  





  public String getUserLanguage()
  {
    return userLanguage_;
  }
  



  public String getHtmlAcceptHeader()
  {
    return htmlAcceptHeader_;
  }
  




  public String getScriptAcceptHeader()
  {
    return scriptAcceptHeader_;
  }
  




  public String getXmlHttpRequestAcceptHeader()
  {
    return xmlHttpRequestAcceptHeader_;
  }
  




  public String getImgAcceptHeader()
  {
    return imgAcceptHeader_;
  }
  




  public String getCssAcceptHeader()
  {
    return cssAcceptHeader_;
  }
  


  public void setApplicationCodeName(String applicationCodeName)
  {
    applicationCodeName_ = applicationCodeName;
  }
  


  public void setApplicationMinorVersion(String applicationMinorVersion)
  {
    applicationMinorVersion_ = applicationMinorVersion;
  }
  


  public void setApplicationName(String applicationName)
  {
    applicationName_ = applicationName;
  }
  


  public void setApplicationVersion(String applicationVersion)
  {
    applicationVersion_ = applicationVersion;
  }
  


  public void setVendor(String vendor)
  {
    vendor_ = vendor;
  }
  


  public void setBrowserLanguage(String browserLanguage)
  {
    browserLanguage_ = browserLanguage;
  }
  


  public void setCpuClass(String cpuClass)
  {
    cpuClass_ = cpuClass;
  }
  


  public void setOnLine(boolean onLine)
  {
    onLine_ = onLine;
  }
  


  public void setPlatform(String platform)
  {
    platform_ = platform;
  }
  


  public void setSystemLanguage(String systemLanguage)
  {
    systemLanguage_ = systemLanguage;
  }
  


  public void setUserAgent(String userAgent)
  {
    userAgent_ = userAgent;
  }
  


  public void setUserLanguage(String userLanguage)
  {
    userLanguage_ = userLanguage;
  }
  


  public void setBrowserVersion(int browserVersion)
  {
    browserVersionNumeric_ = browserVersion;
  }
  


  public void setHtmlAcceptHeader(String htmlAcceptHeader)
  {
    htmlAcceptHeader_ = htmlAcceptHeader;
  }
  


  public void setImgAcceptHeader(String imgAcceptHeader)
  {
    imgAcceptHeader_ = imgAcceptHeader;
  }
  


  public void setCssAcceptHeader(String cssAcceptHeader)
  {
    cssAcceptHeader_ = cssAcceptHeader;
  }
  


  public void setScriptAcceptHeader(String scriptAcceptHeader)
  {
    scriptAcceptHeader_ = scriptAcceptHeader;
  }
  



  public void setXmlHttpRequestAcceptHeader(String xmlHttpRequestAcceptHeader)
  {
    xmlHttpRequestAcceptHeader_ = xmlHttpRequestAcceptHeader;
  }
  


  public int getBrowserVersionNumeric()
  {
    return browserVersionNumeric_;
  }
  



  public boolean equals(Object o)
  {
    return EqualsBuilder.reflectionEquals(this, o, new String[0]);
  }
  



  public int hashCode()
  {
    return HashCodeBuilder.reflectionHashCode(this, new String[0]);
  }
  




  public Set<PluginConfiguration> getPlugins()
  {
    return plugins_;
  }
  




  public boolean hasFeature(BrowserVersionFeatures property)
  {
    return features_.contains(property);
  }
  




  public String getNickname()
  {
    return nickname_;
  }
  



  public String getBuildId()
  {
    return buildId_;
  }
  



  public String[] getHeaderNamesOrdered()
  {
    return headerNamesOrdered_;
  }
  



  public void setHeaderNamesOrdered(String[] headerNames)
  {
    headerNamesOrdered_ = headerNames;
  }
  




  public void registerUploadMimeType(String fileExtension, String mimeType)
  {
    if (fileExtension != null) {
      uploadMimeTypes_.put(fileExtension.toLowerCase(Locale.ROOT), mimeType);
    }
  }
  




  public String getUploadMimeType(File file)
  {
    if (file == null) {
      return "";
    }
    
    String fileExtension = FilenameUtils.getExtension(file.getName());
    
    String mimeType = (String)uploadMimeTypes_.get(fileExtension.toLowerCase(Locale.ROOT));
    if (mimeType != null) {
      return mimeType;
    }
    return "";
  }
  
  public String toString()
  {
    return nickname_;
  }
  





  public BrowserVersion clone()
  {
    BrowserVersion clone = new BrowserVersion(getApplicationName(), getApplicationVersion(), 
      getUserAgent(), getBrowserVersionNumeric(), getNickname(), null);
    
    clone.setApplicationCodeName(getApplicationCodeName());
    clone.setApplicationMinorVersion(getApplicationMinorVersion());
    clone.setVendor(getVendor());
    clone.setBrowserLanguage(getBrowserLanguage());
    clone.setCpuClass(getCpuClass());
    clone.setOnLine(isOnLine());
    clone.setPlatform(getPlatform());
    clone.setSystemLanguage(getSystemLanguage());
    clone.setUserLanguage(getUserLanguage());
    
    buildId_ = getBuildId();
    htmlAcceptHeader_ = getHtmlAcceptHeader();
    imgAcceptHeader_ = getImgAcceptHeader();
    cssAcceptHeader_ = getCssAcceptHeader();
    scriptAcceptHeader_ = getScriptAcceptHeader();
    xmlHttpRequestAcceptHeader_ = getXmlHttpRequestAcceptHeader();
    headerNamesOrdered_ = getHeaderNamesOrdered();
    
    for (PluginConfiguration pluginConf : getPlugins()) {
      clone.getPlugins().add(pluginConf.clone());
    }
    
    features_.addAll(features_);
    uploadMimeTypes_.putAll(uploadMimeTypes_);
    
    return clone;
  }
}
