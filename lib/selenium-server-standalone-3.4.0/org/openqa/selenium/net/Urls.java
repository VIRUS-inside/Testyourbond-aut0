package org.openqa.selenium.net;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Logger;
import org.openqa.selenium.WebDriverException;



















public class Urls
{
  private static Logger log = Logger.getLogger(Urls.class.getName());
  


  public Urls() {}
  

  public static String urlEncode(String value)
  {
    try
    {
      return URLEncoder.encode(value, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new WebDriverException(e);
    }
  }
}
