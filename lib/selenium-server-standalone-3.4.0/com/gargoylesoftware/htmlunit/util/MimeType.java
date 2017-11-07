package com.gargoylesoftware.htmlunit.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


















public final class MimeType
{
  private static final Map<String, String> type2extension = ;
  
  private static Map<String, String> buildMap() {
    Map<String, String> map = new HashMap();
    map.put("application/pdf", "pdf");
    map.put("application/x-javascript", "js");
    map.put("image/gif", "gif");
    map.put("image/jpg", "jpeg");
    map.put("image/jpeg", "jpeg");
    map.put("image/png", "png");
    map.put("image/svg+xml", "svg");
    map.put("text/css", "css");
    map.put("text/html", "html");
    map.put("text/plain", "txt");
    map.put("image/x-icon", "ico");
    return map;
  }
  





  private MimeType() {}
  




  public static String getFileExtension(String contentType)
  {
    String value = (String)type2extension.get(contentType.toLowerCase(Locale.ROOT));
    if (value == null) {
      return "unknown";
    }
    
    return value;
  }
}
