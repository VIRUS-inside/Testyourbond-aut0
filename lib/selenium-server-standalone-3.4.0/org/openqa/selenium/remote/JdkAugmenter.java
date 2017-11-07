package org.openqa.selenium.remote;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.reflect.AbstractInvocationHandler;
import java.io.PrintStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.openqa.selenium.Beta;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;



























@Beta
public class JdkAugmenter
  extends BaseAugmenter
{
  public JdkAugmenter() {}
  
  protected RemoteWebDriver extractRemoteWebDriver(WebDriver driver)
  {
    if ((driver instanceof RemoteWebDriver))
      return (RemoteWebDriver)driver;
    if (Proxy.isProxyClass(driver.getClass())) {
      InvocationHandler handler = Proxy.getInvocationHandler(driver);
      if ((handler instanceof JdkHandler)) {
        return driver;
      }
    }
    return null;
  }
  
  protected <X> X create(RemoteWebDriver driver, Map<String, AugmenterProvider> augmentors, X objectToAugment)
  {
    Map<String, ?> capabilities = driver.getCapabilities().asMap();
    Map<Method, InterfaceImplementation> augmentationHandlers = Maps.newHashMap();
    

    Set<Class<?>> proxiedInterfaces = Sets.newHashSet();
    Class<?> superClass = objectToAugment.getClass();
    
    while (null != superClass) {
      proxiedInterfaces.addAll(Arrays.asList(superClass.getInterfaces()));
      superClass = superClass.getSuperclass();
    }
    
    for (Map.Entry<String, ?> capabilityName : capabilities.entrySet()) {
      AugmenterProvider augmenter = (AugmenterProvider)augmentors.get(capabilityName.getKey());
      if (augmenter != null)
      {


        Object value = capabilityName.getValue();
        if ((!(value instanceof Boolean)) || (((Boolean)value).booleanValue()))
        {


          Class<?> interfaceProvided = augmenter.getDescribedInterface();
          Preconditions.checkState(interfaceProvided.isInterface(), "JdkAugmenter can only augment interfaces. %s is not an interface.", interfaceProvided);
          
          proxiedInterfaces.add(interfaceProvided);
          InterfaceImplementation augmentedImplementation = augmenter.getImplementation(value);
          for (Method method : interfaceProvided.getMethods()) {
            InterfaceImplementation oldHandler = (InterfaceImplementation)augmentationHandlers.put(method, augmentedImplementation);
            
            Preconditions.checkState(null == oldHandler, "Both %s and %s attempt to define %s.", oldHandler, augmentedImplementation
              .getClass(), method.getName());
          }
        }
      } }
    if (augmentationHandlers.isEmpty())
    {
      return objectToAugment;
    }
    
    Object proxyHandler = new JdkHandler(driver, objectToAugment, augmentationHandlers, null);
    
    X augmentedProxy = Proxy.newProxyInstance(
      getClass().getClassLoader(), 
      (Class[])proxiedInterfaces.toArray(new Class[proxiedInterfaces.size()]), (InvocationHandler)proxyHandler);
    
    return augmentedProxy;
  }
  
  private static class JdkHandler<X>
    extends AbstractInvocationHandler implements InvocationHandler
  {
    private final RemoteWebDriver driver;
    private final X realInstance;
    private final Map<Method, InterfaceImplementation> handlers;
    
    private JdkHandler(RemoteWebDriver driver, X realInstance, Map<Method, InterfaceImplementation> handlers)
    {
      this.driver = ((RemoteWebDriver)Preconditions.checkNotNull(driver));
      this.realInstance = Preconditions.checkNotNull(realInstance);
      this.handlers = ((Map)Preconditions.checkNotNull(handlers));
    }
    
    public Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable
    {
      InterfaceImplementation handler = (InterfaceImplementation)handlers.get(method);
      try {
        System.out.println("Method: " + method + "all handlers: " + handlers.keySet());
        if (null == handler) {
          return method.invoke(realInstance, args);
        }
        return handler.invoke(new RemoteExecuteMethod(driver), proxy, method, args);
      } catch (InvocationTargetException i) {
        throw i.getCause();
      }
    }
  }
}
