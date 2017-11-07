package org.apache.xml.serialize;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Method;
import org.apache.xerces.util.EncodingMap;

/**
 * @deprecated
 */
public class EncodingInfo
{
  private Object[] fArgsForMethod = null;
  String ianaName;
  String javaName;
  int lastPrintable;
  Object fCharsetEncoder = null;
  Object fCharToByteConverter = null;
  boolean fHaveTriedCToB = false;
  boolean fHaveTriedCharsetEncoder = false;
  
  public EncodingInfo(String paramString1, String paramString2, int paramInt)
  {
    ianaName = paramString1;
    javaName = EncodingMap.getIANA2JavaMapping(paramString1);
    lastPrintable = paramInt;
  }
  
  public String getIANAName()
  {
    return ianaName;
  }
  
  public Writer getWriter(OutputStream paramOutputStream)
    throws UnsupportedEncodingException
  {
    if (javaName != null) {
      return new OutputStreamWriter(paramOutputStream, javaName);
    }
    javaName = EncodingMap.getIANA2JavaMapping(ianaName);
    if (javaName == null) {
      return new OutputStreamWriter(paramOutputStream, "UTF8");
    }
    return new OutputStreamWriter(paramOutputStream, javaName);
  }
  
  public boolean isPrintable(char paramChar)
  {
    if (paramChar <= lastPrintable) {
      return true;
    }
    return isPrintable0(paramChar);
  }
  
  private boolean isPrintable0(char paramChar)
  {
    if ((fCharsetEncoder == null) && (CharsetMethods.fgNIOCharsetAvailable) && (!fHaveTriedCharsetEncoder))
    {
      if (fArgsForMethod == null) {
        fArgsForMethod = new Object[1];
      }
      try
      {
        fArgsForMethod[0] = javaName;
        Object localObject = CharsetMethods.fgCharsetForNameMethod.invoke(null, fArgsForMethod);
        if (((Boolean)CharsetMethods.fgCharsetCanEncodeMethod.invoke(localObject, (Object[])null)).booleanValue()) {
          fCharsetEncoder = CharsetMethods.fgCharsetNewEncoderMethod.invoke(localObject, (Object[])null);
        } else {
          fHaveTriedCharsetEncoder = true;
        }
      }
      catch (Exception localException1)
      {
        fHaveTriedCharsetEncoder = true;
      }
    }
    if (fCharsetEncoder != null) {
      try
      {
        fArgsForMethod[0] = new Character(paramChar);
        return ((Boolean)CharsetMethods.fgCharsetEncoderCanEncodeMethod.invoke(fCharsetEncoder, fArgsForMethod)).booleanValue();
      }
      catch (Exception localException2)
      {
        fCharsetEncoder = null;
        fHaveTriedCharsetEncoder = false;
      }
    }
    if (fCharToByteConverter == null)
    {
      if ((fHaveTriedCToB) || (!CharToByteConverterMethods.fgConvertersAvailable)) {
        return false;
      }
      if (fArgsForMethod == null) {
        fArgsForMethod = new Object[1];
      }
      try
      {
        fArgsForMethod[0] = javaName;
        fCharToByteConverter = CharToByteConverterMethods.fgGetConverterMethod.invoke(null, fArgsForMethod);
      }
      catch (Exception localException3)
      {
        fHaveTriedCToB = true;
        return false;
      }
    }
    try
    {
      fArgsForMethod[0] = new Character(paramChar);
      return ((Boolean)CharToByteConverterMethods.fgCanConvertMethod.invoke(fCharToByteConverter, fArgsForMethod)).booleanValue();
    }
    catch (Exception localException4)
    {
      fCharToByteConverter = null;
      fHaveTriedCToB = false;
    }
    return false;
  }
  
  public static void testJavaEncodingName(String paramString)
    throws UnsupportedEncodingException
  {
    byte[] arrayOfByte = { 118, 97, 108, 105, 100 };
    String str = new String(arrayOfByte, paramString);
  }
  
  static class CharToByteConverterMethods
  {
    private static Method fgGetConverterMethod = null;
    private static Method fgCanConvertMethod = null;
    private static boolean fgConvertersAvailable = false;
    
    private CharToByteConverterMethods() {}
    
    static
    {
      try
      {
        Class localClass = Class.forName("sun.io.CharToByteConverter");
        fgGetConverterMethod = localClass.getMethod("getConverter", new Class[] { String.class });
        fgCanConvertMethod = localClass.getMethod("canConvert", new Class[] { Character.TYPE });
        fgConvertersAvailable = true;
      }
      catch (Exception localException)
      {
        fgGetConverterMethod = null;
        fgCanConvertMethod = null;
        fgConvertersAvailable = false;
      }
    }
  }
  
  static class CharsetMethods
  {
    private static Method fgCharsetForNameMethod = null;
    private static Method fgCharsetCanEncodeMethod = null;
    private static Method fgCharsetNewEncoderMethod = null;
    private static Method fgCharsetEncoderCanEncodeMethod = null;
    private static boolean fgNIOCharsetAvailable = false;
    
    private CharsetMethods() {}
    
    static
    {
      try
      {
        Class localClass1 = Class.forName("java.nio.charset.Charset");
        Class localClass2 = Class.forName("java.nio.charset.CharsetEncoder");
        fgCharsetForNameMethod = localClass1.getMethod("forName", new Class[] { String.class });
        fgCharsetCanEncodeMethod = localClass1.getMethod("canEncode", new Class[0]);
        fgCharsetNewEncoderMethod = localClass1.getMethod("newEncoder", new Class[0]);
        fgCharsetEncoderCanEncodeMethod = localClass2.getMethod("canEncode", new Class[] { Character.TYPE });
        fgNIOCharsetAvailable = true;
      }
      catch (Exception localException)
      {
        fgCharsetForNameMethod = null;
        fgCharsetCanEncodeMethod = null;
        fgCharsetEncoderCanEncodeMethod = null;
        fgCharsetNewEncoderMethod = null;
        fgNIOCharsetAvailable = false;
      }
    }
  }
}
