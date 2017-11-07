package org.apache.xalan.lib.sql;

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
      public Object run() {
        ClassLoader cl = null;
        try {
          cl = Thread.currentThread().getContextClassLoader();
        } catch (SecurityException ex) {}
        return cl;
      }
    });
  }
  
  static ClassLoader getSystemClassLoader() {
    (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
    {
      public Object run() {
        ClassLoader cl = null;
        try {
          cl = ClassLoader.getSystemClassLoader();
        } catch (SecurityException ex) {}
        return cl;
      }
    });
  }
  
  static ClassLoader getParentClassLoader(ClassLoader cl) {
    (ClassLoader)AccessController.doPrivileged(new PrivilegedAction() {
      private final ClassLoader val$cl;
      
      public Object run() { ClassLoader parent = null;
        try {
          parent = val$cl.getParent();
        }
        catch (SecurityException ex) {}
        

        return parent == val$cl ? null : parent;
      }
    });
  }
  
  static String getSystemProperty(String propName) {
    (String)AccessController.doPrivileged(new PrivilegedAction() {
      private final String val$propName;
      
      public Object run() { return System.getProperty(val$propName); }
    });
  }
  
  static FileInputStream getFileInputStream(File file)
    throws FileNotFoundException
  {
    try
    {
      (FileInputStream)AccessController.doPrivileged(new PrivilegedExceptionAction() {
        private final File val$file;
        
        public Object run() throws FileNotFoundException { return new FileInputStream(val$file); }
      });
    }
    catch (PrivilegedActionException e) {
      throw ((FileNotFoundException)e.getException());
    }
  }
  

  static InputStream getResourceAsStream(ClassLoader cl, String name)
  {
    (InputStream)AccessController.doPrivileged(new PrivilegedAction() { private final ClassLoader val$cl;
      private final String val$name;
      
      public Object run() { InputStream ris;
        InputStream ris; if (val$cl == null) {
          ris = ClassLoader.getSystemResourceAsStream(val$name);
        } else {
          ris = val$cl.getResourceAsStream(val$name);
        }
        return ris;
      }
    });
  }
  
  static boolean getFileExists(File f) {
    ((Boolean)AccessController.doPrivileged(new PrivilegedAction() {
      private final File val$f;
      
      public Object run() { return val$f.exists() ? Boolean.TRUE : Boolean.FALSE; }
    })).booleanValue();
  }
  
  static long getLastModified(File f)
  {
    ((Long)AccessController.doPrivileged(new PrivilegedAction() {
      private final File val$f;
      
      public Object run() { return new Long(val$f.lastModified()); }
    })).longValue();
  }
  
  private SecuritySupport() {}
}
