package javax.xml.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

final class FactoryFinder
{
  private static boolean debug = false;
  private static Properties cacheProps = new Properties();
  private static boolean firstTime = true;
  private static final int DEFAULT_LINE_LENGTH = 80;
  
  private FactoryFinder() {}
  
  private static void dPrint(String paramString)
  {
    if (debug) {
      System.err.println("JAXP: " + paramString);
    }
  }
  
  static Object newInstance(String paramString, ClassLoader paramClassLoader, boolean paramBoolean)
    throws FactoryFinder.ConfigurationError
  {
    try
    {
      Class localClass;
      if (paramClassLoader == null) {
        localClass = Class.forName(paramString);
      } else {
        try
        {
          localClass = paramClassLoader.loadClass(paramString);
        }
        catch (ClassNotFoundException localClassNotFoundException2)
        {
          if (paramBoolean)
          {
            paramClassLoader = FactoryFinder.class.getClassLoader();
            if (paramClassLoader != null) {
              localClass = paramClassLoader.loadClass(paramString);
            } else {
              localClass = Class.forName(paramString);
            }
          }
          else
          {
            throw localClassNotFoundException2;
          }
        }
      }
      Object localObject = localClass.newInstance();
      if (debug) {
        dPrint("created new instance of " + localClass + " using ClassLoader: " + paramClassLoader);
      }
      return localObject;
    }
    catch (ClassNotFoundException localClassNotFoundException1)
    {
      throw new ConfigurationError("Provider " + paramString + " not found", localClassNotFoundException1);
    }
    catch (Exception localException)
    {
      throw new ConfigurationError("Provider " + paramString + " could not be instantiated: " + localException, localException);
    }
  }
  
  static Object find(String paramString1, String paramString2)
    throws FactoryFinder.ConfigurationError
  {
    ClassLoader localClassLoader = SecuritySupport.getContextClassLoader();
    if (localClassLoader == null) {
      localClassLoader = FactoryFinder.class.getClassLoader();
    }
    if (debug) {
      dPrint("find factoryId =" + paramString1);
    }
    try
    {
      String str1 = SecuritySupport.getSystemProperty(paramString1);
      if ((str1 != null) && (str1.length() > 0))
      {
        if (debug) {
          dPrint("found system property, value=" + str1);
        }
        return newInstance(str1, localClassLoader, true);
      }
    }
    catch (SecurityException localSecurityException) {}
    try
    {
      String str2 = SecuritySupport.getSystemProperty("java.home");
      String str3 = str2 + File.separator + "lib" + File.separator + "jaxp.properties";
      String str4 = null;
      if (firstTime) {
        synchronized (cacheProps)
        {
          if (firstTime)
          {
            File localFile = new File(str3);
            firstTime = false;
            if (SecuritySupport.doesFileExist(localFile))
            {
              if (debug) {
                dPrint("Read properties file " + localFile);
              }
              cacheProps.load(SecuritySupport.getFileInputStream(localFile));
            }
          }
        }
      }
      str4 = cacheProps.getProperty(paramString1);
      if (str4 != null)
      {
        if (debug) {
          dPrint("found in $java.home/jaxp.properties, value=" + str4);
        }
        return newInstance(str4, localClassLoader, true);
      }
    }
    catch (Exception localException)
    {
      if (debug) {
        localException.printStackTrace();
      }
    }
    Object localObject1 = findJarServiceProvider(paramString1);
    if (localObject1 != null) {
      return localObject1;
    }
    if (paramString2 == null) {
      throw new ConfigurationError("Provider for " + paramString1 + " cannot be found", null);
    }
    if (debug) {
      dPrint("loaded from fallback value: " + paramString2);
    }
    return newInstance(paramString2, localClassLoader, true);
  }
  
  private static Object findJarServiceProvider(String paramString)
    throws FactoryFinder.ConfigurationError
  {
    String str1 = "META-INF/services/" + paramString;
    InputStream localInputStream = null;
    ClassLoader localClassLoader = SecuritySupport.getContextClassLoader();
    if (localClassLoader != null)
    {
      localInputStream = SecuritySupport.getResourceAsStream(localClassLoader, str1);
      if (localInputStream == null)
      {
        localClassLoader = FactoryFinder.class.getClassLoader();
        localInputStream = SecuritySupport.getResourceAsStream(localClassLoader, str1);
      }
    }
    else
    {
      localClassLoader = FactoryFinder.class.getClassLoader();
      localInputStream = SecuritySupport.getResourceAsStream(localClassLoader, str1);
    }
    if (localInputStream == null) {
      return null;
    }
    if (debug) {
      dPrint("found jar resource=" + str1 + " using ClassLoader: " + localClassLoader);
    }
    BufferedReader localBufferedReader;
    try
    {
      localBufferedReader = new BufferedReader(new InputStreamReader(localInputStream, "UTF-8"), 80);
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      localBufferedReader = new BufferedReader(new InputStreamReader(localInputStream), 80);
    }
    String str2 = null;
    try
    {
      str2 = localBufferedReader.readLine();
    }
    catch (IOException localIOException1)
    {
      Object localObject1 = null;
      return localObject1;
    }
    finally
    {
      try
      {
        localBufferedReader.close();
      }
      catch (IOException localIOException2) {}
    }
    if ((str2 != null) && (!"".equals(str2)))
    {
      if (debug) {
        dPrint("found in resource, value=" + str2);
      }
      return newInstance(str2, localClassLoader, false);
    }
    return null;
  }
  
  static
  {
    try
    {
      String str = SecuritySupport.getSystemProperty("jaxp.debug");
      debug = (str != null) && (!"false".equals(str));
    }
    catch (SecurityException localSecurityException)
    {
      debug = false;
    }
  }
  
  static class ConfigurationError
    extends Error
  {
    private Exception exception;
    
    ConfigurationError(String paramString, Exception paramException)
    {
      super();
      exception = paramException;
    }
    
    Exception getException()
    {
      return exception;
    }
  }
}
