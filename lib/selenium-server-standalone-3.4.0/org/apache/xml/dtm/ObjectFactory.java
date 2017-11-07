package org.apache.xml.dtm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;






















































final class ObjectFactory
{
  private static final String DEFAULT_PROPERTIES_FILENAME = "xalan.properties";
  private static final String SERVICES_PATH = "META-INF/services/";
  private static final boolean DEBUG = false;
  private static Properties fXalanProperties = null;
  





  private static long fLastModified = -1L;
  










  ObjectFactory() {}
  









  static Object createObject(String factoryId, String fallbackClassName)
    throws ObjectFactory.ConfigurationError
  {
    return createObject(factoryId, null, fallbackClassName);
  }
  
























  static Object createObject(String factoryId, String propertiesFilename, String fallbackClassName)
    throws ObjectFactory.ConfigurationError
  {
    Class factoryClass = lookUpFactoryClass(factoryId, propertiesFilename, fallbackClassName);
    


    if (factoryClass == null) {
      throw new ConfigurationError("Provider for " + factoryId + " cannot be found", null);
    }
    
    try
    {
      Object instance = factoryClass.newInstance();
      debugPrintln("created new instance of factory " + factoryId);
      return instance;
    } catch (Exception x) {
      throw new ConfigurationError("Provider for factory " + factoryId + " could not be instantiated: " + x, x);
    }
  }
  
























  static Class lookUpFactoryClass(String factoryId)
    throws ObjectFactory.ConfigurationError
  {
    return lookUpFactoryClass(factoryId, null, null);
  }
  
























  static Class lookUpFactoryClass(String factoryId, String propertiesFilename, String fallbackClassName)
    throws ObjectFactory.ConfigurationError
  {
    String factoryClassName = lookUpFactoryClassName(factoryId, propertiesFilename, fallbackClassName);
    

    ClassLoader cl = findClassLoader();
    
    if (factoryClassName == null) {
      factoryClassName = fallbackClassName;
    }
    
    try
    {
      Class providerClass = findProviderClass(factoryClassName, cl, true);
      

      debugPrintln("created new instance of " + providerClass + " using ClassLoader: " + cl);
      
      return providerClass;
    } catch (ClassNotFoundException x) {
      throw new ConfigurationError("Provider " + factoryClassName + " not found", x);
    }
    catch (Exception x) {
      throw new ConfigurationError("Provider " + factoryClassName + " could not be instantiated: " + x, x);
    }
  }
  


























  static String lookUpFactoryClassName(String factoryId, String propertiesFilename, String fallbackClassName)
  {
    try
    {
      String systemProp = SecuritySupport.getSystemProperty(factoryId);
      if (systemProp != null) {
        debugPrintln("found system property, value=" + systemProp);
        return systemProp;
      }
    }
    catch (SecurityException se) {}
    



    String factoryClassName = null;
    

    if (propertiesFilename == null) {
      File propertiesFile = null;
      boolean propertiesFileExists = false;
      try {
        String javah = SecuritySupport.getSystemProperty("java.home");
        propertiesFilename = javah + File.separator + "lib" + File.separator + "xalan.properties";
        
        propertiesFile = new File(propertiesFilename);
        propertiesFileExists = SecuritySupport.getFileExists(propertiesFile);
      }
      catch (SecurityException e) {
        fLastModified = -1L;
        fXalanProperties = null;
      }
      
      synchronized (ObjectFactory.class) {
        boolean loadProperties = false;
        FileInputStream fis = null;
        try
        {
          if (fLastModified >= 0L) {
            if ((propertiesFileExists) && (fLastModified < (ObjectFactory.fLastModified = SecuritySupport.getLastModified(propertiesFile))))
            {
              loadProperties = true;

            }
            else if (!propertiesFileExists) {
              fLastModified = -1L;
              fXalanProperties = null;
            }
            

          }
          else if (propertiesFileExists) {
            loadProperties = true;
            fLastModified = SecuritySupport.getLastModified(propertiesFile);
          }
          
          if (loadProperties)
          {

            fXalanProperties = new Properties();
            fis = SecuritySupport.getFileInputStream(propertiesFile);
            fXalanProperties.load(fis);
          }
          








          if (fis != null) {
            try {
              fis.close();
            }
            catch (IOException exc) {}
          }
        }
        catch (Exception x)
        {
          fXalanProperties = null;
          fLastModified = -1L;


        }
        finally
        {

          if (fis != null) {
            try {
              fis.close();
            }
            catch (IOException exc) {}
          }
        }
      }
      
      if (fXalanProperties != null) {
        factoryClassName = fXalanProperties.getProperty(factoryId);
      }
    } else {
      FileInputStream fis = null;
      try {
        fis = SecuritySupport.getFileInputStream(new File(propertiesFilename));
        Properties props = new Properties();
        props.load(fis);
        factoryClassName = props.getProperty(factoryId);
        






        if (fis != null) {
          try {
            fis.close();
          }
          catch (IOException exc) {}
        }
        


        if (factoryClassName == null) {
          break label489;
        }
      }
      catch (Exception x) {}finally
      {
        if (fis != null) {
          try {
            fis.close();
          }
          catch (IOException exc) {}
        }
      }
    }
    

    debugPrintln("found in " + propertiesFilename + ", value=" + factoryClassName);
    
    return factoryClassName;
    
    label489:
    
    return findJarServiceProviderName(factoryId);
  }
  








  private static void debugPrintln(String msg) {}
  







  static ClassLoader findClassLoader()
    throws ObjectFactory.ConfigurationError
  {
    ClassLoader context = SecuritySupport.getContextClassLoader();
    ClassLoader system = SecuritySupport.getSystemClassLoader();
    
    ClassLoader chain = system;
    for (;;) {
      if (context == chain)
      {







        ClassLoader current = ObjectFactory.class.getClassLoader();
        
        chain = system;
        for (;;) {
          if (current == chain)
          {

            return system;
          }
          if (chain == null) {
            break;
          }
          chain = SecuritySupport.getParentClassLoader(chain);
        }
        


        return current;
      }
      
      if (chain == null) {
        break;
      }
      



      chain = SecuritySupport.getParentClassLoader(chain);
    }
    


    return context;
  }
  




  static Object newInstance(String className, ClassLoader cl, boolean doFallback)
    throws ObjectFactory.ConfigurationError
  {
    try
    {
      Class providerClass = findProviderClass(className, cl, doFallback);
      Object instance = providerClass.newInstance();
      debugPrintln("created new instance of " + providerClass + " using ClassLoader: " + cl);
      
      return instance;
    } catch (ClassNotFoundException x) {
      throw new ConfigurationError("Provider " + className + " not found", x);
    }
    catch (Exception x) {
      throw new ConfigurationError("Provider " + className + " could not be instantiated: " + x, x);
    }
  }
  








  static Class findProviderClass(String className, ClassLoader cl, boolean doFallback)
    throws ClassNotFoundException, ObjectFactory.ConfigurationError
  {
    SecurityManager security = System.getSecurityManager();
    try {
      if (security != null) {
        int lastDot = className.lastIndexOf('.');
        String packageName = className;
        if (lastDot != -1) packageName = className.substring(0, lastDot);
        security.checkPackageAccess(packageName);
      }
    } catch (SecurityException e) {
      throw e;
    }
    
    Class providerClass;
    if (cl == null)
    {








      providerClass = Class.forName(className);
    } else {
      try {
        providerClass = cl.loadClass(className); } catch (ClassNotFoundException x) { Class providerClass;
        Class providerClass;
        if (doFallback)
        {
          ClassLoader current = ObjectFactory.class.getClassLoader();
          if (current == null) {
            providerClass = Class.forName(className); } else { Class providerClass;
            if (cl != current) {
              cl = current;
              providerClass = cl.loadClass(className);
            } else {
              throw x;
            }
          }
        } else { throw x;
        }
      }
    }
    Class providerClass;
    return providerClass;
  }
  





  private static String findJarServiceProviderName(String factoryId)
  {
    String serviceId = "META-INF/services/" + factoryId;
    InputStream is = null;
    

    ClassLoader cl = findClassLoader();
    
    is = SecuritySupport.getResourceAsStream(cl, serviceId);
    

    if (is == null) {
      ClassLoader current = ObjectFactory.class.getClassLoader();
      if (cl != current) {
        cl = current;
        is = SecuritySupport.getResourceAsStream(cl, serviceId);
      }
    }
    
    if (is == null)
    {
      return null;
    }
    
    debugPrintln("found jar resource=" + serviceId + " using ClassLoader: " + cl);
    








    BufferedReader rd;
    







    try
    {
      rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
    } catch (UnsupportedEncodingException e) {
      rd = new BufferedReader(new InputStreamReader(is));
    }
    
    String factoryClassName = null;
    
    try
    {
      factoryClassName = rd.readLine();
      




      try
      {
        rd.close();
      }
      catch (IOException exc) {}
      


      if (factoryClassName == null) {
        break label252;
      }
    }
    catch (IOException x)
    {
      return null;
    }
    finally
    {
      try {
        rd.close();
      }
      catch (IOException exc) {}
    }
    

    if (!"".equals(factoryClassName))
    {
      debugPrintln("found in resource, value=" + factoryClassName);
      





      return factoryClassName;
    }
    
    label252:
    return null;
  }
  






  static class ConfigurationError
    extends Error
  {
    static final long serialVersionUID = 5122054096615067992L;
    





    private Exception exception;
    






    ConfigurationError(String msg, Exception x)
    {
      super();
      exception = x;
    }
    




    Exception getException()
    {
      return exception;
    }
  }
}
