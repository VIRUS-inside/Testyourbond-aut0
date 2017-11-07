package org.apache.xerces.impl.dv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

final class SecuritySupport
{
  static ClassLoader getContextClassLoader()
  {
    (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
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
  
  static ClassLoader getSystemClassLoader()
  {
    (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
    {
      public Object run()
      {
        ClassLoader localClassLoader = null;
        try
        {
          localClassLoader = ClassLoader.getSystemClassLoader();
        }
        catch (SecurityException localSecurityException) {}
        return localClassLoader;
      }
    });
  }
  
  static ClassLoader getParentClassLoader(ClassLoader paramClassLoader)
  {
    (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
    {
      private final ClassLoader val$cl;
      
      public Object run()
      {
        ClassLoader localClassLoader = null;
        try
        {
          localClassLoader = val$cl.getParent();
        }
        catch (SecurityException localSecurityException) {}
        return localClassLoader == val$cl ? null : localClassLoader;
      }
    });
  }
  
  static String getSystemProperty(String paramString)
  {
    (String)AccessController.doPrivileged(new PrivilegedAction()
    {
      private final String val$propName;
      
      public Object run()
      {
        return System.getProperty(val$propName);
      }
    });
  }
  
  static FileInputStream getFileInputStream(File paramFile)
    throws FileNotFoundException
  {
    try
    {
      (FileInputStream)AccessController.doPrivileged(new PrivilegedExceptionAction()
      {
        private final File val$file;
        
        public Object run()
          throws FileNotFoundException
        {
          return new FileInputStream(val$file);
        }
      });
    }
    catch (PrivilegedActionException localPrivilegedActionException)
    {
      throw ((FileNotFoundException)localPrivilegedActionException.getException());
    }
  }
  
  static InputStream getResourceAsStream(ClassLoader paramClassLoader, String paramString)
  {
    (InputStream)AccessController.doPrivileged(new PrivilegedAction()
    {
      private final ClassLoader val$cl;
      private final String val$name;
      
      public Object run()
      {
        InputStream localInputStream;
        if (val$cl == null) {
          localInputStream = ClassLoader.getSystemResourceAsStream(val$name);
        } else {
          localInputStream = val$cl.getResourceAsStream(val$name);
        }
        return localInputStream;
      }
    });
  }
  
  static boolean getFileExists(File paramFile)
  {
    ((Boolean)AccessController.doPrivileged(new PrivilegedAction()
    {
      private final File val$f;
      
      public Object run()
      {
        return val$f.exists() ? Boolean.TRUE : Boolean.FALSE;
      }
    })).booleanValue();
  }
  
  static long getLastModified(File paramFile)
  {
    ((Long)AccessController.doPrivileged(new PrivilegedAction()
    {
      private final File val$f;
      
      public Object run()
      {
        return new Long(val$f.lastModified());
      }
    })).longValue();
  }
  
  private SecuritySupport() {}
}
