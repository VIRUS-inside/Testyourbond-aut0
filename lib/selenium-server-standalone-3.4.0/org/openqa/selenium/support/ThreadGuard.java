package org.openqa.selenium.support;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;





























public class ThreadGuard
{
  public ThreadGuard() {}
  
  public static WebDriver protect(WebDriver actualWebDriver)
  {
    WebDriverInvocationHandler invocationHandler = new WebDriverInvocationHandler(actualWebDriver);
    return 
      (WebDriver)Proxy.newProxyInstance(actualWebDriver.getClass().getClassLoader(), 
      getInterfaces(actualWebDriver), invocationHandler);
  }
  
  private static Class<?>[] getInterfaces(Object target)
  {
    Class<?> base = target.getClass();
    Set<Class<?>> interfaces = new HashSet();
    if (base.isInterface()) {
      interfaces.add(base);
    }
    while ((base != null) && (!Object.class.equals(base))) {
      interfaces.addAll(Arrays.asList(base.getInterfaces()));
      base = base.getSuperclass();
    }
    return (Class[])interfaces.toArray(new Class[interfaces.size()]);
  }
  
  static class WebDriverInvocationHandler
    implements InvocationHandler
  {
    private final long threadId;
    private final Object underlying;
    private final String threadName;
    
    public WebDriverInvocationHandler(Object underlyingWebDriver)
    {
      Thread thread = Thread.currentThread();
      threadId = thread.getId();
      threadName = thread.getName();
      underlying = underlyingWebDriver;
    }
    
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      try {
        if (Thread.currentThread().getId() != threadId) {
          Thread currentThread = Thread.currentThread();
          
          throw new WebDriverException(String.format("Thread safety error; this instance of WebDriver was constructed on thread %s (id %d) and is being accessed by thread %s (id %d)This is not permitted and *will* cause undefined behaviour", new Object[] { threadName, 
          

            Long.valueOf(threadId), currentThread.getName(), Long.valueOf(currentThread.getId()) }));
        }
        return invokeUnderlying(method, args);
      } catch (InvocationTargetException e) {
        throw e.getTargetException();
      }
    }
    
    protected Object invokeUnderlying(Method method, Object[] args) throws IllegalAccessException, InvocationTargetException
    {
      return method.invoke(underlying, args);
    }
  }
}
