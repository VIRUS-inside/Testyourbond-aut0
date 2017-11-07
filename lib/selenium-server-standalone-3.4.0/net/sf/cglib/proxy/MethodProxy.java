package net.sf.cglib.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.sf.cglib.core.AbstractClassGenerator;
import net.sf.cglib.core.CodeGenerationException;
import net.sf.cglib.core.GeneratorStrategy;
import net.sf.cglib.core.NamingPolicy;
import net.sf.cglib.core.Signature;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastClass.Generator;























public class MethodProxy
{
  private Signature sig1;
  private Signature sig2;
  private CreateInfo createInfo;
  private final Object initLock = new Object();
  

  private volatile FastClassInfo fastClassInfo;
  

  public static MethodProxy create(Class c1, Class c2, String desc, String name1, String name2)
  {
    MethodProxy proxy = new MethodProxy();
    sig1 = new Signature(name1, desc);
    sig2 = new Signature(name2, desc);
    createInfo = new CreateInfo(c1, c2);
    return proxy;
  }
  








  private void init()
  {
    if (fastClassInfo == null)
    {
      synchronized (initLock)
      {
        if (fastClassInfo == null)
        {
          CreateInfo ci = createInfo;
          
          FastClassInfo fci = new FastClassInfo(null);
          f1 = helper(ci, c1);
          f2 = helper(ci, c2);
          i1 = f1.getIndex(sig1);
          i2 = f2.getIndex(sig2);
          fastClassInfo = fci;
          createInfo = null;
        }
      }
    }
  }
  


  private static class CreateInfo
  {
    Class c1;
    

    Class c2;
    
    NamingPolicy namingPolicy;
    
    GeneratorStrategy strategy;
    
    boolean attemptLoad;
    

    public CreateInfo(Class c1, Class c2)
    {
      this.c1 = c1;
      this.c2 = c2;
      AbstractClassGenerator fromEnhancer = AbstractClassGenerator.getCurrent();
      if (fromEnhancer != null) {
        namingPolicy = fromEnhancer.getNamingPolicy();
        strategy = fromEnhancer.getStrategy();
        attemptLoad = fromEnhancer.getAttemptLoad();
      }
    }
  }
  
  private static FastClass helper(CreateInfo ci, Class type) {
    FastClass.Generator g = new FastClass.Generator();
    g.setType(type);
    g.setClassLoader(c2.getClassLoader());
    g.setNamingPolicy(namingPolicy);
    g.setStrategy(strategy);
    g.setAttemptLoad(attemptLoad);
    return g.create();
  }
  


  private MethodProxy() {}
  

  public Signature getSignature()
  {
    return sig1;
  }
  





  public String getSuperName()
  {
    return sig2.getName();
  }
  






  public int getSuperIndex()
  {
    init();
    return fastClassInfo.i2;
  }
  
  FastClass getFastClass()
  {
    init();
    return fastClassInfo.f1;
  }
  
  FastClass getSuperFastClass()
  {
    init();
    return fastClassInfo.f2;
  }
  






  public static MethodProxy find(Class type, Signature sig)
  {
    try
    {
      Method m = type.getDeclaredMethod("CGLIB$findMethodProxy", MethodInterceptorGenerator.FIND_PROXY_TYPES);
      
      return (MethodProxy)m.invoke(null, new Object[] { sig });
    } catch (NoSuchMethodException e) {
      throw new IllegalArgumentException("Class " + type + " does not use a MethodInterceptor");
    } catch (IllegalAccessException e) {
      throw new CodeGenerationException(e);
    } catch (InvocationTargetException e) {
      throw new CodeGenerationException(e);
    }
  }
  







  public Object invoke(Object obj, Object[] args)
    throws Throwable
  {
    try
    {
      init();
      FastClassInfo fci = fastClassInfo;
      return f1.invoke(i1, obj, args);
    } catch (InvocationTargetException e) {
      throw e.getTargetException();
    } catch (IllegalArgumentException e) {
      if (fastClassInfo.i1 < 0)
        throw new IllegalArgumentException("Protected method: " + sig1);
      throw e;
    }
  }
  







  public Object invokeSuper(Object obj, Object[] args)
    throws Throwable
  {
    try
    {
      init();
      FastClassInfo fci = fastClassInfo;
      return f2.invoke(i2, obj, args);
    } catch (InvocationTargetException e) {
      throw e.getTargetException();
    }
  }
  
  private static class FastClassInfo
  {
    FastClass f1;
    FastClass f2;
    int i1;
    int i2;
    
    private FastClassInfo() {}
  }
}
