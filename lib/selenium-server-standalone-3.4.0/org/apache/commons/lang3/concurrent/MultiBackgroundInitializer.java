package org.apache.commons.lang3.concurrent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ExecutorService;























































































public class MultiBackgroundInitializer
  extends BackgroundInitializer<MultiBackgroundInitializerResults>
{
  private final Map<String, BackgroundInitializer<?>> childInitializers = new HashMap();
  






  public MultiBackgroundInitializer() {}
  






  public MultiBackgroundInitializer(ExecutorService exec)
  {
    super(exec);
  }
  











  public void addInitializer(String name, BackgroundInitializer<?> init)
  {
    if (name == null) {
      throw new IllegalArgumentException("Name of child initializer must not be null!");
    }
    
    if (init == null) {
      throw new IllegalArgumentException("Child initializer must not be null!");
    }
    

    synchronized (this) {
      if (isStarted()) {
        throw new IllegalStateException("addInitializer() must not be called after start()!");
      }
      
      childInitializers.put(name, init);
    }
  }
  










  protected int getTaskCount()
  {
    int result = 1;
    
    for (BackgroundInitializer<?> bi : childInitializers.values()) {
      result += bi.getTaskCount();
    }
    
    return result;
  }
  





  protected MultiBackgroundInitializerResults initialize()
    throws Exception
  {
    Map<String, BackgroundInitializer<?>> inits;
    



    synchronized (this)
    {
      inits = new HashMap(childInitializers);
    }
    
    Map<String, BackgroundInitializer<?>> inits;
    
    ExecutorService exec = getActiveExecutor();
    for (BackgroundInitializer<?> bi : inits.values()) {
      if (bi.getExternalExecutor() == null)
      {
        bi.setExternalExecutor(exec);
      }
      bi.start();
    }
    

    Object results = new HashMap();
    Map<String, ConcurrentException> excepts = new HashMap();
    for (Map.Entry<String, BackgroundInitializer<?>> e : inits.entrySet()) {
      try {
        ((Map)results).put(e.getKey(), ((BackgroundInitializer)e.getValue()).get());
      } catch (ConcurrentException cex) {
        excepts.put(e.getKey(), cex);
      }
    }
    
    return new MultiBackgroundInitializerResults(inits, (Map)results, excepts, null);
  }
  







  public static class MultiBackgroundInitializerResults
  {
    private final Map<String, BackgroundInitializer<?>> initializers;
    





    private final Map<String, Object> resultObjects;
    





    private final Map<String, ConcurrentException> exceptions;
    






    private MultiBackgroundInitializerResults(Map<String, BackgroundInitializer<?>> inits, Map<String, Object> results, Map<String, ConcurrentException> excepts)
    {
      initializers = inits;
      resultObjects = results;
      exceptions = excepts;
    }
    







    public BackgroundInitializer<?> getInitializer(String name)
    {
      return checkName(name);
    }
    











    public Object getResultObject(String name)
    {
      checkName(name);
      return resultObjects.get(name);
    }
    







    public boolean isException(String name)
    {
      checkName(name);
      return exceptions.containsKey(name);
    }
    









    public ConcurrentException getException(String name)
    {
      checkName(name);
      return (ConcurrentException)exceptions.get(name);
    }
    






    public Set<String> initializerNames()
    {
      return Collections.unmodifiableSet(initializers.keySet());
    }
    





    public boolean isSuccessful()
    {
      return exceptions.isEmpty();
    }
    








    private BackgroundInitializer<?> checkName(String name)
    {
      BackgroundInitializer<?> init = (BackgroundInitializer)initializers.get(name);
      if (init == null) {
        throw new NoSuchElementException("No child initializer with name " + name);
      }
      

      return init;
    }
  }
}
