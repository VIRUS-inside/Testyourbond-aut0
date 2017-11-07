package org.apache.xml.serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import org.apache.xml.serializer.utils.WrappedRuntimeException;
































public final class Encodings
{
  private static final String ENCODINGS_FILE = SerializerBase.PKG_PATH + "/Encodings.properties";
  



  static final String DEFAULT_MIME_ENCODING = "UTF-8";
  



  public Encodings() {}
  



  static Writer getWriter(OutputStream output, String encoding)
    throws UnsupportedEncodingException
  {
    for (int i = 0; i < _encodings.length; i++)
    {
      if (_encodingsname.equalsIgnoreCase(encoding))
      {
        try
        {
          String javaName = _encodingsjavaName;
          return new OutputStreamWriter(output, javaName);
        }
        catch (IllegalArgumentException iae) {}catch (UnsupportedEncodingException usee) {}
      }
    }
    









    try
    {
      return new OutputStreamWriter(output, encoding);
    }
    catch (IllegalArgumentException iae)
    {
      throw new UnsupportedEncodingException(encoding);
    }
  }
  















  static EncodingInfo getEncodingInfo(String encoding)
  {
    String normalizedEncoding = toUpperCaseFast(encoding);
    EncodingInfo ei = (EncodingInfo)_encodingTableKeyJava.get(normalizedEncoding);
    if (ei == null)
      ei = (EncodingInfo)_encodingTableKeyMime.get(normalizedEncoding);
    if (ei == null)
    {
      ei = new EncodingInfo(null, null, '\000');
    }
    
    return ei;
  }
  









  public static boolean isRecognizedEncoding(String encoding)
  {
    String normalizedEncoding = encoding.toUpperCase();
    EncodingInfo ei = (EncodingInfo)_encodingTableKeyJava.get(normalizedEncoding);
    if (ei == null)
      ei = (EncodingInfo)_encodingTableKeyMime.get(normalizedEncoding);
    if (ei != null)
      return true;
    return false;
  }
  










  private static String toUpperCaseFast(String s)
  {
    boolean different = false;
    int mx = s.length();
    char[] chars = new char[mx];
    for (int i = 0; i < mx; i++) {
      char ch = s.charAt(i);
      
      if (('a' <= ch) && (ch <= 'z'))
      {
        ch = (char)(ch + '￠');
        different = true;
      }
      chars[i] = ch;
    }
    
    String upper;
    
    String upper;
    if (different) {
      upper = String.valueOf(chars);
    } else {
      upper = s;
    }
    return upper;
  }
  























  static String getMimeEncoding(String encoding)
  {
    if (null == encoding)
    {


      try
      {


        encoding = System.getProperty("file.encoding", "UTF8");
        
        if (null != encoding)
        {








          String jencoding = (encoding.equalsIgnoreCase("Cp1252")) || (encoding.equalsIgnoreCase("ISO8859_1")) || (encoding.equalsIgnoreCase("8859_1")) || (encoding.equalsIgnoreCase("UTF8")) ? "UTF-8" : convertJava2MimeEncoding(encoding);
          






          encoding = null != jencoding ? jencoding : "UTF-8";

        }
        else
        {
          encoding = "UTF-8";
        }
      }
      catch (SecurityException se)
      {
        encoding = "UTF-8";
      }
      
    }
    else {
      encoding = convertJava2MimeEncoding(encoding);
    }
    
    return encoding;
  }
  









  private static String convertJava2MimeEncoding(String encoding)
  {
    EncodingInfo enc = (EncodingInfo)_encodingTableKeyJava.get(toUpperCaseFast(encoding));
    
    if (null != enc)
      return name;
    return encoding;
  }
  













  public static String convertMime2JavaEncoding(String encoding)
  {
    for (int i = 0; i < _encodings.length; i++)
    {
      if (_encodingsname.equalsIgnoreCase(encoding))
      {
        return _encodingsjavaName;
      }
    }
    
    return encoding;
  }
  









  private static EncodingInfo[] loadEncodingInfo()
  {
    try
    {
      InputStream is = SecuritySupport.getResourceAsStream(ObjectFactory.findClassLoader(), ENCODINGS_FILE);
      

      Properties props = new Properties();
      if (is != null) {
        props.load(is);
        is.close();
      }
      







      int totalEntries = props.size();
      
      List encodingInfo_list = new ArrayList();
      Enumeration keys = props.keys();
      for (int i = 0; i < totalEntries; i++)
      {
        String javaName = (String)keys.nextElement();
        String val = props.getProperty(javaName);
        int len = lengthOfMimeNames(val);
        
        char highChar;
        
        if (len == 0)
        {

          String mimeName = javaName;
          highChar = '\000';
        }
        else
        {
          char highChar;
          try {
            String highVal = val.substring(len).trim();
            highChar = (char)Integer.decode(highVal).intValue();
          }
          catch (NumberFormatException e) {
            highChar = '\000';
          }
          String mimeNames = val.substring(0, len);
          StringTokenizer st = new StringTokenizer(mimeNames, ",");
          
          for (boolean first = true; 
              st.hasMoreTokens(); 
              first = false)
          {
            String mimeName = st.nextToken();
            EncodingInfo ei = new EncodingInfo(mimeName, javaName, highChar);
            encodingInfo_list.add(ei);
            _encodingTableKeyMime.put(mimeName.toUpperCase(), ei);
            if (first) {
              _encodingTableKeyJava.put(javaName.toUpperCase(), ei);
            }
          }
        }
      }
      
      EncodingInfo[] ret_ei = new EncodingInfo[encodingInfo_list.size()];
      encodingInfo_list.toArray(ret_ei);
      return ret_ei;
    }
    catch (MalformedURLException mue)
    {
      throw new WrappedRuntimeException(mue);
    }
    catch (IOException ioe)
    {
      throw new WrappedRuntimeException(ioe);
    }
  }
  







  private static int lengthOfMimeNames(String val)
  {
    int len = val.indexOf(' ');
    

    if (len < 0) {
      len = val.length();
    }
    return len;
  }
  






  static boolean isHighUTF16Surrogate(char ch)
  {
    return (55296 <= ch) && (ch <= 56319);
  }
  





  static boolean isLowUTF16Surrogate(char ch)
  {
    return (56320 <= ch) && (ch <= 57343);
  }
  






  static int toCodePoint(char highSurrogate, char lowSurrogate)
  {
    int codePoint = (highSurrogate - 55296 << 10) + (lowSurrogate - 56320) + 65536;
    


    return codePoint;
  }
  







  static int toCodePoint(char ch)
  {
    int codePoint = ch;
    return codePoint;
  }
  


















  public static char getHighChar(String encoding)
  {
    String normalizedEncoding = toUpperCaseFast(encoding);
    EncodingInfo ei = (EncodingInfo)_encodingTableKeyJava.get(normalizedEncoding);
    if (ei == null)
      ei = (EncodingInfo)_encodingTableKeyMime.get(normalizedEncoding);
    char highCodePoint; char highCodePoint; if (ei != null) {
      highCodePoint = ei.getHighChar();
    } else
      highCodePoint = '\000';
    return highCodePoint;
  }
  
  private static final Hashtable _encodingTableKeyJava = new Hashtable();
  private static final Hashtable _encodingTableKeyMime = new Hashtable();
  private static final EncodingInfo[] _encodings = loadEncodingInfo();
}
