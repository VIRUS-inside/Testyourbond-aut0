package org.w3c.dom.bootstrap;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.StringTokenizer;
import java.util.Vector;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DOMImplementationList;
import org.w3c.dom.DOMImplementationSource;

public final class DOMImplementationRegistry
{
  public static final String PROPERTY = "org.w3c.dom.DOMImplementationSourceList";
  private static final int DEFAULT_LINE_LENGTH = 80;
  private static final String DEFAULT_DOM_IMPLEMENTATION_SOURCE = "org.apache.xerces.dom.DOMXSImplementationSourceImpl";
  private Vector sources;
  
  private DOMImplementationRegistry(Vector paramVector)
  {
    sources = paramVector;
  }
  
  public static DOMImplementationRegistry newInstance()
    throws ClassNotFoundException, InstantiationException, IllegalAccessException, ClassCastException
  {
    Vector localVector = new Vector();
    ClassLoader localClassLoader = getClassLoader();
    String str1 = getSystemProperty("org.w3c.dom.DOMImplementationSourceList");
    if ((str1 == null) || (str1.length() == 0)) {
      str1 = getServiceValue(localClassLoader);
    }
    if (str1 == null) {
      str1 = "org.apache.xerces.dom.DOMXSImplementationSourceImpl";
    }
    if (str1 != null)
    {
      StringTokenizer localStringTokenizer = new StringTokenizer(str1);
      while (localStringTokenizer.hasMoreTokens())
      {
        String str2 = localStringTokenizer.nextToken();
        Class localClass = null;
        if (localClassLoader != null) {
          localClass = localClassLoader.loadClass(str2);
        } else {
          localClass = Class.forName(str2);
        }
        DOMImplementationSource localDOMImplementationSource = (DOMImplementationSource)localClass.newInstance();
        localVector.addElement(localDOMImplementationSource);
      }
    }
    return new DOMImplementationRegistry(localVector);
  }
  
  public DOMImplementation getDOMImplementation(String paramString)
  {
    int i = sources.size();
    Object localObject = null;
    for (int j = 0; j < i; j++)
    {
      DOMImplementationSource localDOMImplementationSource = (DOMImplementationSource)sources.elementAt(j);
      DOMImplementation localDOMImplementation = localDOMImplementationSource.getDOMImplementation(paramString);
      if (localDOMImplementation != null) {
        return localDOMImplementation;
      }
    }
    return null;
  }
  
  public DOMImplementationList getDOMImplementationList(String paramString)
  {
    Vector localVector = new Vector();
    int i = sources.size();
    for (int j = 0; j < i; j++)
    {
      DOMImplementationSource localDOMImplementationSource = (DOMImplementationSource)sources.elementAt(j);
      DOMImplementationList localDOMImplementationList = localDOMImplementationSource.getDOMImplementationList(paramString);
      for (int k = 0; k < localDOMImplementationList.getLength(); k++)
      {
        DOMImplementation localDOMImplementation = localDOMImplementationList.item(k);
        localVector.addElement(localDOMImplementation);
      }
    }
    new DOMImplementationList()
    {
      private final Vector val$implementations;
      
      public DOMImplementation item(int paramAnonymousInt)
      {
        if ((paramAnonymousInt >= 0) && (paramAnonymousInt < val$implementations.size())) {
          try
          {
            return (DOMImplementation)val$implementations.elementAt(paramAnonymousInt);
          }
          catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
          {
            return null;
          }
        }
        return null;
      }
      
      public int getLength()
      {
        return val$implementations.size();
      }
    };
  }
  
  public void addSource(DOMImplementationSource paramDOMImplementationSource)
  {
    if (paramDOMImplementationSource == null) {
      throw new NullPointerException();
    }
    if (!sources.contains(paramDOMImplementationSource)) {
      sources.addElement(paramDOMImplementationSource);
    }
  }
  
  private static ClassLoader getClassLoader()
  {
    try
    {
      ClassLoader localClassLoader = getContextClassLoader();
      if (localClassLoader != null) {
        return localClassLoader;
      }
    }
    catch (Exception localException)
    {
      return DOMImplementationRegistry.class.getClassLoader();
    }
    return DOMImplementationRegistry.class.getClassLoader();
  }
  
  private static String getServiceValue(ClassLoader paramClassLoader)
  {
    String str1 = "META-INF/services/org.w3c.dom.DOMImplementationSourceList";
    try
    {
      InputStream localInputStream = getResourceAsStream(paramClassLoader, str1);
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
        String str2 = null;
        try
        {
          str2 = localBufferedReader.readLine();
        }
        finally
        {
          localBufferedReader.close();
        }
        if ((str2 != null) && (str2.length() > 0)) {
          return str2;
        }
      }
    }
    catch (Exception localException)
    {
      return null;
    }
    return null;
  }
  
  private static boolean isJRE11()
  {
    try
    {
      Class localClass = Class.forName("java.security.AccessController");
      return false;
    }
    catch (Exception localException) {}
    return true;
  }
  
  private static ClassLoader getContextClassLoader()
  {
    isJRE11() ? null : (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
    {
      public Object run()
      {
        ClassLoader localClassLoader = null;
        try
        {
          localClassLoader = Thread.currentThread().getContextClassLoader();
        }
        catch (SecurityException localSecurityException) {}
        return localClassLoader;
      }
    });
  }
  
  private static String getSystemProperty(String paramString)
  {
    isJRE11() ? System.getProperty(paramString) : (String)AccessController.doPrivileged(new PrivilegedAction()
    {
      private final String val$name;
      
      public Object run()
      {
        return System.getProperty(val$name);
      }
    });
  }
  
  private static InputStream getResourceAsStream(ClassLoader paramClassLoader, String paramString)
  {
    if (isJRE11())
    {
      InputStream localInputStream;
      if (paramClassLoader == null) {
        localInputStream = ClassLoader.getSystemResourceAsStream(paramString);
      } else {
        localInputStream = paramClassLoader.getResourceAsStream(paramString);
      }
      return localInputStream;
    }
    (InputStream)AccessController.doPrivileged(new PrivilegedAction()
    {
      private final ClassLoader val$classLoader;
      private final String val$name;
      
      public Object run()
      {
        InputStream localInputStream;
        if (val$classLoader == null) {
          localInputStream = ClassLoader.getSystemResourceAsStream(val$name);
        } else {
          localInputStream = val$classLoader.getResourceAsStream(val$name);
        }
        return localInputStream;
      }
    });
  }
}
