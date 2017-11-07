package com.gargoylesoftware.htmlunit.javascript.host.file;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import java.util.Date;
import java.util.Locale;
import org.apache.commons.lang3.time.FastDateFormat;






























@JsxClass
public class File
  extends Blob
{
  private static final String LAST_MODIFIED_DATE_FORMAT = "EEE MMM dd yyyy HH:mm:ss 'GMT'Z (zzzz)";
  private static final String LAST_MODIFIED_DATE_FORMAT_FF = "EEE MMM dd yyyy HH:mm:ss 'GMT'Z";
  private java.io.File file_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public File() {}
  
  File(String pathname)
  {
    file_ = new java.io.File(pathname);
  }
  



  @JsxGetter
  public String getName()
  {
    return file_.getName();
  }
  



  @JsxGetter
  public String getLastModifiedDate()
  {
    Date date = new Date(getLastModified());
    BrowserVersion browser = getBrowserVersion();
    Locale locale = new Locale(browser.getSystemLanguage());
    
    if (browser.hasFeature(BrowserVersionFeatures.JS_FILE_SHORT_DATE_FORMAT)) {
      FastDateFormat format = FastDateFormat.getInstance("EEE MMM dd yyyy HH:mm:ss 'GMT'Z", locale);
      return format.format(date);
    }
    
    FastDateFormat format = FastDateFormat.getInstance("EEE MMM dd yyyy HH:mm:ss 'GMT'Z (zzzz)", locale);
    return format.format(date);
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public long getLastModified()
  {
    return file_.lastModified();
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public String getWebkitRelativePath()
  {
    return "";
  }
  



  @JsxGetter
  public long getSize()
  {
    return file_.length();
  }
  



  @JsxGetter
  public String getType()
  {
    return getBrowserVersion().getUploadMimeType(file_);
  }
  




  @JsxFunction
  public void slice() {}
  




  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void msClose() {}
  



  public java.io.File getFile()
  {
    return file_;
  }
}
