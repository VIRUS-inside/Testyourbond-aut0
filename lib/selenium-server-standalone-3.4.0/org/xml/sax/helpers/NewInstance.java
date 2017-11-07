package org.xml.sax.helpers;

class NewInstance
{
  private static final boolean DO_FALLBACK = true;
  
  NewInstance() {}
  
  static Object newInstance(ClassLoader paramClassLoader, String paramString)
    throws ClassNotFoundException, IllegalAccessException, InstantiationException
  {
    Class localClass;
    if (paramClassLoader == null) {
      localClass = Class.forName(paramString);
    } else {
      try
      {
        localClass = paramClassLoader.loadClass(paramString);
      }
      catch (ClassNotFoundException localClassNotFoundException)
      {
        paramClassLoader = NewInstance.class.getClassLoader();
        if (paramClassLoader != null) {
          localClass = paramClassLoader.loadClass(paramString);
        } else {
          localClass = Class.forName(paramString);
        }
      }
    }
    Object localObject = localClass.newInstance();
    return localObject;
  }
  
  static ClassLoader getClassLoader()
  {
    ClassLoader localClassLoader = SecuritySupport.getContextClassLoader();
    if (localClassLoader == null) {
      localClassLoader = NewInstance.class.getClassLoader();
    }
    return localClassLoader;
  }
}
