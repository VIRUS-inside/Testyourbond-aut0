package org.apache.xml.serialize;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Locale;
import org.apache.xerces.util.EncodingMap;

/**
 * @deprecated
 */
public class Encodings
{
  static final int DEFAULT_LAST_PRINTABLE = 127;
  static final int LAST_PRINTABLE_UNICODE = 65535;
  static final String[] UNICODE_ENCODINGS = { "Unicode", "UnicodeBig", "UnicodeLittle", "GB2312", "UTF8", "UTF-16" };
  static final String DEFAULT_ENCODING = "UTF8";
  static Hashtable _encodings = new Hashtable();
  static final String JIS_DANGER_CHARS = "\\~¢£¥¬—―‖…‾‾∥∯〜＼～￠￡￢￣";
  
  public Encodings() {}
  
  static EncodingInfo getEncodingInfo(String paramString, boolean paramBoolean)
    throws UnsupportedEncodingException
  {
    EncodingInfo localEncodingInfo = null;
    if (paramString == null)
    {
      if ((localEncodingInfo = (EncodingInfo)_encodings.get("UTF8")) != null) {
        return localEncodingInfo;
      }
      localEncodingInfo = new EncodingInfo(EncodingMap.getJava2IANAMapping("UTF8"), "UTF8", 65535);
      _encodings.put("UTF8", localEncodingInfo);
      return localEncodingInfo;
    }
    paramString = paramString.toUpperCase(Locale.ENGLISH);
    String str = EncodingMap.getIANA2JavaMapping(paramString);
    if (str == null)
    {
      if (paramBoolean)
      {
        EncodingInfo.testJavaEncodingName(paramString);
        if ((localEncodingInfo = (EncodingInfo)_encodings.get(paramString)) != null) {
          return localEncodingInfo;
        }
        for (i = 0; i < UNICODE_ENCODINGS.length; i++) {
          if (UNICODE_ENCODINGS[i].equalsIgnoreCase(paramString))
          {
            localEncodingInfo = new EncodingInfo(EncodingMap.getJava2IANAMapping(paramString), paramString, 65535);
            break;
          }
        }
        if (i == UNICODE_ENCODINGS.length) {
          localEncodingInfo = new EncodingInfo(EncodingMap.getJava2IANAMapping(paramString), paramString, 127);
        }
        _encodings.put(paramString, localEncodingInfo);
        return localEncodingInfo;
      }
      throw new UnsupportedEncodingException(paramString);
    }
    if ((localEncodingInfo = (EncodingInfo)_encodings.get(str)) != null) {
      return localEncodingInfo;
    }
    for (int i = 0; i < UNICODE_ENCODINGS.length; i++) {
      if (UNICODE_ENCODINGS[i].equalsIgnoreCase(str))
      {
        localEncodingInfo = new EncodingInfo(paramString, str, 65535);
        break;
      }
    }
    if (i == UNICODE_ENCODINGS.length) {
      localEncodingInfo = new EncodingInfo(paramString, str, 127);
    }
    _encodings.put(str, localEncodingInfo);
    return localEncodingInfo;
  }
}
