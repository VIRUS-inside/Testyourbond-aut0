package org.eclipse.jetty.websocket.api.extensions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

















public abstract class ExtensionFactory
  implements Iterable<Class<? extends Extension>>
{
  private ServiceLoader<Extension> extensionLoader = ServiceLoader.load(Extension.class);
  private Map<String, Class<? extends Extension>> availableExtensions;
  
  public ExtensionFactory()
  {
    availableExtensions = new HashMap();
    for (Extension ext : extensionLoader)
    {
      if (ext != null)
      {
        availableExtensions.put(ext.getName(), ext.getClass());
      }
    }
  }
  
  public Map<String, Class<? extends Extension>> getAvailableExtensions()
  {
    return availableExtensions;
  }
  
  public Class<? extends Extension> getExtension(String name)
  {
    return (Class)availableExtensions.get(name);
  }
  
  public Set<String> getExtensionNames()
  {
    return availableExtensions.keySet();
  }
  
  public boolean isAvailable(String name)
  {
    return availableExtensions.containsKey(name);
  }
  

  public Iterator<Class<? extends Extension>> iterator()
  {
    return availableExtensions.values().iterator();
  }
  
  public abstract Extension newInstance(ExtensionConfig paramExtensionConfig);
  
  public void register(String name, Class<? extends Extension> extension)
  {
    availableExtensions.put(name, extension);
  }
  
  public void unregister(String name)
  {
    availableExtensions.remove(name);
  }
}
