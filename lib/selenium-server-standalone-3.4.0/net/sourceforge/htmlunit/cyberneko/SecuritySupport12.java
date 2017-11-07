package net.sourceforge.htmlunit.cyberneko;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;





















class SecuritySupport12
  extends SecuritySupport
{
  SecuritySupport12() {}
  
  ClassLoader getContextClassLoader()
  {
    (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
    {
      public ClassLoader run() {
        ClassLoader cl = null;
        try {
          cl = Thread.currentThread().getContextClassLoader();
        } catch (SecurityException localSecurityException) {}
        return cl;
      }
    });
  }
  
  ClassLoader getSystemClassLoader()
  {
    (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
    {
      public ClassLoader run() {
        ClassLoader cl = null;
        try {
          cl = ClassLoader.getSystemClassLoader();
        } catch (SecurityException localSecurityException) {}
        return cl;
      }
    });
  }
  
  ClassLoader getParentClassLoader(final ClassLoader cl)
  {
    (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
    {
      public ClassLoader run() {
        ClassLoader parent = null;
        try {
          parent = cl.getParent();
        }
        catch (SecurityException localSecurityException) {}
        

        return parent == cl ? null : parent;
      }
    });
  }
  
  String getSystemProperty(final String propName)
  {
    (String)AccessController.doPrivileged(new PrivilegedAction()
    {
      public String run() {
        return System.getProperty(propName);
      }
    });
  }
  
  FileInputStream getFileInputStream(final File file)
    throws FileNotFoundException
  {
    try
    {
      (FileInputStream)AccessController.doPrivileged(new PrivilegedExceptionAction()
      {
        public FileInputStream run() throws FileNotFoundException {
          return new FileInputStream(file);
        }
      });
    } catch (PrivilegedActionException e) {
      throw ((FileNotFoundException)e.getException());
    }
  }
  


  InputStream getResourceAsStream(final ClassLoader cl, final String name)
  {
    (InputStream)AccessController.doPrivileged(new PrivilegedAction() {
      public InputStream run() {
        InputStream ris;
        InputStream ris;
        if (cl == null) {
          ris = ClassLoader.getSystemResourceAsStream(name);
        } else {
          ris = cl.getResourceAsStream(name);
        }
        return ris;
      }
    });
  }
  
  boolean getFileExists(final File f)
  {
    
    



      ((Boolean)AccessController.doPrivileged(new PrivilegedAction()
      {
        public Boolean run()
        {
          return Boolean.valueOf(f.exists());
        }
      })).booleanValue();
  }
  
  long getLastModified(final File f)
  {
    
    



      ((Long)AccessController.doPrivileged(new PrivilegedAction()
      {
        public Long run()
        {
          return Long.valueOf(f.lastModified());
        }
      })).longValue();
  }
}
