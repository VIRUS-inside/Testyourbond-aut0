package org.eclipse.jetty.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Proxy;


























public class ClassLoadingObjectInputStream
  extends ObjectInputStream
{
  public ClassLoadingObjectInputStream(InputStream in)
    throws IOException
  {
    super(in);
  }
  


  public ClassLoadingObjectInputStream()
    throws IOException
  {}
  

  public Class<?> resolveClass(ObjectStreamClass cl)
    throws IOException, ClassNotFoundException
  {
    try
    {
      return Class.forName(cl.getName(), false, Thread.currentThread().getContextClassLoader());
    }
    catch (ClassNotFoundException e) {}
    
    return super.resolveClass(cl);
  }
  



  protected Class<?> resolveProxyClass(String[] interfaces)
    throws IOException, ClassNotFoundException
  {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    
    ClassLoader nonPublicLoader = null;
    boolean hasNonPublicInterface = false;
    

    Class<?>[] classObjs = new Class[interfaces.length];
    for (int i = 0; i < interfaces.length; i++)
    {
      Class<?> cl = Class.forName(interfaces[i], false, loader);
      if ((cl.getModifiers() & 0x1) == 0)
      {
        if (hasNonPublicInterface)
        {
          if (nonPublicLoader != cl.getClassLoader())
          {
            throw new IllegalAccessError("conflicting non-public interface class loaders");
          }
          
        }
        else
        {
          nonPublicLoader = cl.getClassLoader();
          hasNonPublicInterface = true;
        }
      }
      classObjs[i] = cl;
    }
    try
    {
      return Proxy.getProxyClass(hasNonPublicInterface ? nonPublicLoader : loader, classObjs);
    }
    catch (IllegalArgumentException e)
    {
      throw new ClassNotFoundException(null, e);
    }
  }
}
