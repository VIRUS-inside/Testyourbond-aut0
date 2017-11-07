package com.gargoylesoftware.htmlunit.util;

import com.gargoylesoftware.htmlunit.WebAssert;
import java.awt.Color;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.client.utils.DateUtils;

























public final class StringUtils
{
  private static final Pattern HEX_COLOR = Pattern.compile("#([0-9a-fA-F]{3}|[0-9a-fA-F]{6})");
  
  private static final Pattern RGB_COLOR = Pattern.compile("rgb\\s*?\\(\\s*?(\\d{1,3})\\s*?,\\s*?(\\d{1,3})\\s*?,\\s*?(\\d{1,3})\\s*?\\)");
  private static final Pattern ILLEGAL_FILE_NAME_CHARS = Pattern.compile("\\\\|/|\\||:|\\?|\\*|\"|<|>|\\p{Cntrl}");
  







  private StringUtils() {}
  







  public static String escapeXmlChars(String s)
  {
    return 
      org.apache.commons.lang3.StringUtils.replaceEach(s, new String[] { "&", "<", ">" }, new String[] { "&amp;", "&lt;", "&gt;" });
  }
  





  public static String escapeXmlAttributeValue(String attValue)
  {
    int len = attValue.length();
    StringBuilder sb = null;
    for (int i = len - 1; i >= 0; i--) {
      char c = attValue.charAt(i);
      String replacement = null;
      if (c == '<') {
        replacement = "&lt;";
      }
      else if (c == '&') {
        replacement = "&amp;";
      }
      else if (c == '"') {
        replacement = "&quot;";
      }
      
      if (replacement != null) {
        if (sb == null) {
          sb = new StringBuilder(attValue);
        }
        sb.replace(i, i + 1, replacement);
      }
    }
    
    if (sb != null) {
      return sb.toString();
    }
    return attValue;
  }
  









  public static int indexOf(String s, char searchChar, int beginIndex, int endIndex)
  {
    for (int i = beginIndex; i < endIndex; i++) {
      if (s.charAt(i) == searchChar) {
        return i;
      }
    }
    return -1;
  }
  






  public static Date parseHttpDate(String s)
  {
    if (s == null) {
      return null;
    }
    return DateUtils.parseDate(s);
  }
  




  public static Color asColorHexadecimal(String token)
  {
    if (token == null) {
      return null;
    }
    Matcher tmpMatcher = HEX_COLOR.matcher(token);
    boolean tmpFound = tmpMatcher.matches();
    if (!tmpFound) {
      return null;
    }
    
    String tmpHex = tmpMatcher.group(1);
    if (tmpHex.length() == 6) {
      int tmpRed = Integer.parseInt(tmpHex.substring(0, 2), 16);
      int tmpGreen = Integer.parseInt(tmpHex.substring(2, 4), 16);
      int tmpBlue = Integer.parseInt(tmpHex.substring(4, 6), 16);
      Color tmpColor = new Color(tmpRed, tmpGreen, tmpBlue);
      return tmpColor;
    }
    
    int tmpRed = Integer.parseInt(tmpHex.substring(0, 1) + tmpHex.substring(0, 1), 16);
    int tmpGreen = Integer.parseInt(tmpHex.substring(1, 2) + tmpHex.substring(1, 2), 16);
    int tmpBlue = Integer.parseInt(tmpHex.substring(2, 3) + tmpHex.substring(2, 3), 16);
    Color tmpColor = new Color(tmpRed, tmpGreen, tmpBlue);
    return tmpColor;
  }
  




  public static boolean isColorRGB(String token)
  {
    if (token == null) {
      return false;
    }
    return RGB_COLOR.matcher(token.trim()).matches();
  }
  




  public static Color asColorRGB(String token)
  {
    if (token == null) {
      return null;
    }
    Matcher tmpMatcher = RGB_COLOR.matcher(token);
    boolean tmpFound = tmpMatcher.matches();
    if (!tmpFound) {
      return null;
    }
    
    int tmpRed = Integer.parseInt(tmpMatcher.group(1));
    int tmpGreen = Integer.parseInt(tmpMatcher.group(2));
    int tmpBlue = Integer.parseInt(tmpMatcher.group(3));
    Color tmpColor = new Color(tmpRed, tmpGreen, tmpBlue);
    return tmpColor;
  }
  




  public static Color findColorRGB(String token)
  {
    if (token == null) {
      return null;
    }
    Matcher tmpMatcher = RGB_COLOR.matcher(token);
    boolean tmpFound = tmpMatcher.find();
    if (!tmpFound) {
      return null;
    }
    
    int tmpRed = Integer.parseInt(tmpMatcher.group(1));
    int tmpGreen = Integer.parseInt(tmpMatcher.group(2));
    int tmpBlue = Integer.parseInt(tmpMatcher.group(3));
    Color tmpColor = new Color(tmpRed, tmpGreen, tmpBlue);
    return tmpColor;
  }
  





  public static String formatColor(Color aColor)
  {
    return "rgb(" + aColor.getRed() + ", " + aColor.getGreen() + ", " + aColor.getBlue() + ")";
  }
  





  public static String formatHttpDate(Date date)
  {
    WebAssert.notNull("date", date);
    return DateUtils.formatDate(date);
  }
  







  public static String sanitizeForAppendReplacement(String toSanitize)
  {
    String toReplace = org.apache.commons.lang3.StringUtils.replaceEach(toSanitize, 
      new String[] { "\\", "$" }, new String[] { "\\\\", "\\$" });
    return toReplace;
  }
  






  public static String sanitizeForFileName(String toSanitize)
  {
    return ILLEGAL_FILE_NAME_CHARS.matcher(toSanitize).replaceAll("_");
  }
}
