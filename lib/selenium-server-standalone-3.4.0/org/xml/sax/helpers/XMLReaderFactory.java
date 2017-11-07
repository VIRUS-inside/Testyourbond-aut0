package org.xml.sax.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public final class XMLReaderFactory
{
  private static final String property = "org.xml.sax.driver";
  private static final int DEFAULT_LINE_LENGTH = 80;
  
  private XMLReaderFactory() {}
  
  public static XMLReader createXMLReader()
    throws SAXException
  {
    String str1 = null;
    ClassLoader localClassLoader1 = NewInstance.getClassLoader();
    try
    {
      str1 = SecuritySupport.getSystemProperty("org.xml.sax.driver");
    }
    catch (Exception localException1) {}
    if ((str1 == null) || (str1.length() == 0))
    {
      String str2 = "META-INF/services/org.xml.sax.driver";
      InputStream localInputStream = null;
      str1 = null;
      ClassLoader localClassLoader2 = SecuritySupport.getContextClassLoader();
      if (localClassLoader2 != null)
      {
        localInputStream = SecuritySupport.getResourceAsStream(localClassLoader2, str2);
        if (localInputStream == null)
        {
          localClassLoader2 = XMLReaderFactory.class.getClassLoader();
          localInputStream = SecuritySupport.getResourceAsStream(localClassLoader2, str2);
        }
      }
      else
      {
        localClassLoader2 = XMLReaderFactory.class.getClassLoader();
        localInputStream = SecuritySupport.getResourceAsStream(localClassLoader2, str2);
      }
      if (localInputStream != null)
      {
        BufferedReader localBufferedReader;
        try
        {
          localBufferedReader = new BufferedReader(new InputStreamReader(localInputStream, "UTF-8"), 80);
        }
        catch (UnsupportedEncodingException localUnsupportedEncodingException)
        {
          localBufferedReader = new BufferedReader(new InputStreamReader(localInputStream), 80);
        }
        try
        {
          str1 = localBufferedReader.readLine();
        }
        catch (Exception localException3) {}finally
        {
          try
          {
            localBufferedReader.close();
          }
          catch (IOException localIOException) {}
        }
      }
    }
    if (str1 == null) {
      str1 = "org.apache.xerces.parsers.SAXParser";
    }
    if (str1 != null) {
      return loadClass(localClassLoader1, str1);
    }
    try
    {
      return new ParserAdapter(ParserFactory.makeParser());
    }
    catch (Exception localException2)
    {
      throw new SAXException("Can't create default XMLReader; is system property org.xml.sax.driver set?");
    }
  }
  
  public static XMLReader createXMLReader(String paramString)
    throws SAXException
  {
    return loadClass(NewInstance.getClassLoader(), paramString);
  }
  
  private static XMLReader loadClass(ClassLoader paramClassLoader, String paramString)
    throws SAXException
  {
    try
    {
      return (XMLReader)NewInstance.newInstance(paramClassLoader, paramString);
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      throw new SAXException("SAX2 driver class " + paramString + " not found", localClassNotFoundException);
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      throw new SAXException("SAX2 driver class " + paramString + " found but cannot be loaded", localIllegalAccessException);
    }
    catch (InstantiationException localInstantiationException)
    {
      throw new SAXException("SAX2 driver class " + paramString + " loaded but cannot be instantiated (no empty public constructor?)", localInstantiationException);
    }
    catch (ClassCastException localClassCastException)
    {
      throw new SAXException("SAX2 driver class " + paramString + " does not implement XMLReader", localClassCastException);
    }
  }
}
