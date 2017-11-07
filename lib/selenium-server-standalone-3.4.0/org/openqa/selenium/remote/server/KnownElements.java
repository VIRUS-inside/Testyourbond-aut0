package org.openqa.selenium.remote.server;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsElement;




















public class KnownElements
{
  private final BiMap<String, WebElement> elements = HashBiMap.create();
  
  public KnownElements() {}
  
  public String add(WebElement element) { if (elements.containsValue(element)) {
      return (String)elements.inverse().get(element);
    }
    String id = getNextId();
    elements.put(id, proxyElement(element, id));
    return id;
  }
  
  private int nextId;
  public WebElement get(String elementId) { return (WebElement)elements.get(elementId); }
  

  private String getNextId()
  {
    return String.valueOf(nextId++);
  }
  
  private WebElement proxyElement(final WebElement element, final String id) {
    InvocationHandler handler = new InvocationHandler() {
      public Object invoke(Object object, Method method, Object[] objects) throws Throwable {
        if ("getId".equals(method.getName()))
          return id;
        if ("getWrappedElement".equals(method.getName())) {
          return element;
        }
        try {
          return method.invoke(element, objects);
        } catch (InvocationTargetException e) {
          throw e.getTargetException();
        }
      }
    };
    
    Class<?>[] proxyThese;
    Class<?>[] proxyThese;
    if ((element instanceof Locatable)) {
      proxyThese = new Class[] { WebElement.class, ProxiedElement.class, Locatable.class };
    } else {
      proxyThese = new Class[] { WebElement.class, ProxiedElement.class };
    }
    
    return (WebElement)Proxy.newProxyInstance(element.getClass().getClassLoader(), proxyThese, handler);
  }
  
  public static abstract interface ProxiedElement
    extends WrapsElement
  {
    public abstract String getId();
  }
}
