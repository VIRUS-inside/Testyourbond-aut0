package net.sf.cglib.core;

import java.lang.ref.WeakReference;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import net.sf.cglib.asm..ClassReader;
import net.sf.cglib.core.internal.Function;
import net.sf.cglib.core.internal.LoadingCache;























public abstract class AbstractClassGenerator<T>
  implements ClassGenerator
{
  private static final ThreadLocal CURRENT = new ThreadLocal();
  
  private static volatile Map<ClassLoader, ClassLoaderData> CACHE = new WeakHashMap();
  
  private GeneratorStrategy strategy = DefaultGeneratorStrategy.INSTANCE;
  private NamingPolicy namingPolicy = DefaultNamingPolicy.INSTANCE;
  private Source source;
  private ClassLoader classLoader;
  private String namePrefix;
  private Object key;
  private boolean useCache = true;
  private String className;
  private boolean attemptLoad;
  
  protected static class ClassLoaderData {
    private final Set<String> reservedClassNames = new HashSet();
    





    private final LoadingCache<AbstractClassGenerator, Object, Object> generatedClasses;
    





    private final WeakReference<ClassLoader> classLoader;
    




    private final Predicate uniqueNamePredicate = new Predicate() {
      public boolean evaluate(Object name) {
        return reservedClassNames.contains(name);
      }
    };
    
    private static final Function<AbstractClassGenerator, Object> GET_KEY = new Function() {
      public Object apply(AbstractClassGenerator gen) {
        return key;
      }
    };
    
    public ClassLoaderData(ClassLoader classLoader) {
      if (classLoader == null) {
        throw new IllegalArgumentException("classLoader == null is not yet supported");
      }
      this.classLoader = new WeakReference(classLoader);
      Function<AbstractClassGenerator, Object> load = new Function()
      {
        public Object apply(AbstractClassGenerator gen) {
          Class klass = gen.generate(AbstractClassGenerator.ClassLoaderData.this);
          return gen.wrapCachedClass(klass);
        }
      };
      generatedClasses = new LoadingCache(GET_KEY, load);
    }
    
    public ClassLoader getClassLoader() {
      return (ClassLoader)classLoader.get();
    }
    
    public void reserveName(String name) {
      reservedClassNames.add(name);
    }
    
    public Predicate getUniqueNamePredicate() {
      return uniqueNamePredicate;
    }
    
    public Object get(AbstractClassGenerator gen, boolean useCache) {
      if (!useCache) {
        return gen.generate(this);
      }
      Object cachedValue = generatedClasses.get(gen);
      return gen.unwrapCachedValue(cachedValue);
    }
  }
  
  protected T wrapCachedClass(Class klass)
  {
    return new WeakReference(klass);
  }
  
  protected Object unwrapCachedValue(T cached) {
    return ((WeakReference)cached).get();
  }
  
  protected static class Source {
    String name;
    
    public Source(String name) { this.name = name; }
  }
  
  protected AbstractClassGenerator(Source source)
  {
    this.source = source;
  }
  
  protected void setNamePrefix(String namePrefix) {
    this.namePrefix = namePrefix;
  }
  
  protected final String getClassName() {
    return className;
  }
  
  private void setClassName(String className) {
    this.className = className;
  }
  
  private String generateClassName(Predicate nameTestPredicate) {
    return namingPolicy.getClassName(namePrefix, source.name, key, nameTestPredicate);
  }
  








  public void setClassLoader(ClassLoader classLoader)
  {
    this.classLoader = classLoader;
  }
  




  public void setNamingPolicy(NamingPolicy namingPolicy)
  {
    if (namingPolicy == null)
      namingPolicy = DefaultNamingPolicy.INSTANCE;
    this.namingPolicy = namingPolicy;
  }
  


  public NamingPolicy getNamingPolicy()
  {
    return namingPolicy;
  }
  



  public void setUseCache(boolean useCache)
  {
    this.useCache = useCache;
  }
  


  public boolean getUseCache()
  {
    return useCache;
  }
  




  public void setAttemptLoad(boolean attemptLoad)
  {
    this.attemptLoad = attemptLoad;
  }
  
  public boolean getAttemptLoad() {
    return attemptLoad;
  }
  



  public void setStrategy(GeneratorStrategy strategy)
  {
    if (strategy == null)
      strategy = DefaultGeneratorStrategy.INSTANCE;
    this.strategy = strategy;
  }
  


  public GeneratorStrategy getStrategy()
  {
    return strategy;
  }
  



  public static AbstractClassGenerator getCurrent()
  {
    return (AbstractClassGenerator)CURRENT.get();
  }
  
  public ClassLoader getClassLoader() {
    ClassLoader t = classLoader;
    if (t == null) {
      t = getDefaultClassLoader();
    }
    if (t == null) {
      t = getClass().getClassLoader();
    }
    if (t == null) {
      t = Thread.currentThread().getContextClassLoader();
    }
    if (t == null) {
      throw new IllegalStateException("Cannot determine classloader");
    }
    return t;
  }
  




  protected abstract ClassLoader getDefaultClassLoader();
  




  protected ProtectionDomain getProtectionDomain()
  {
    return null;
  }
  
  protected Object create(Object key) {
    try {
      ClassLoader loader = getClassLoader();
      Map<ClassLoader, ClassLoaderData> cache = CACHE;
      ClassLoaderData data = (ClassLoaderData)cache.get(loader);
      if (data == null) {
        synchronized (AbstractClassGenerator.class) {
          cache = CACHE;
          data = (ClassLoaderData)cache.get(loader);
          if (data == null) {
            Map<ClassLoader, ClassLoaderData> newCache = new WeakHashMap(cache);
            data = new ClassLoaderData(loader);
            newCache.put(loader, data);
            CACHE = newCache;
          }
        }
      }
      this.key = key;
      Object obj = data.get(this, getUseCache());
      if ((obj instanceof Class)) {
        return firstInstance((Class)obj);
      }
      return nextInstance(obj);
    } catch (RuntimeException e) {
      throw e;
    } catch (Error e) {
      throw e;
    } catch (Exception e) {
      throw new CodeGenerationException(e);
    }
  }
  
  protected Class generate(ClassLoaderData data)
  {
    Object save = CURRENT.get();
    CURRENT.set(this);
    try {
      ClassLoader classLoader = data.getClassLoader();
      if (classLoader == null)
      {
        throw new IllegalStateException("ClassLoader is null while trying to define class " + getClassName() + ". It seems that the loader has been expired from a weak reference somehow. " + "Please file an issue at cglib's issue tracker.");
      }
      
      synchronized (classLoader) {
        String name = generateClassName(data.getUniqueNamePredicate());
        data.reserveName(name);
        setClassName(name);
      }
      if (attemptLoad) {
        try {
          Class gen = classLoader.loadClass(getClassName());
          return gen;
        }
        catch (ClassNotFoundException localClassNotFoundException) {}
      }
      
      byte[] b = strategy.generate(this);
      String className = ClassNameReader.getClassName(new .ClassReader(b));
      ProtectionDomain protectionDomain = getProtectionDomain();
      Class gen; synchronized (classLoader) { Class gen;
        if (protectionDomain == null) {
          gen = ReflectUtils.defineClass(className, b, classLoader);
        } else
          gen = ReflectUtils.defineClass(className, b, classLoader, protectionDomain);
      }
      Class gen;
      return gen;
    } catch (RuntimeException e) {
      throw e;
    } catch (Error e) {
      throw e;
    } catch (Exception e) {
      throw new CodeGenerationException(e);
    } finally {
      CURRENT.set(save);
    }
  }
  
  protected abstract Object firstInstance(Class paramClass)
    throws Exception;
  
  protected abstract Object nextInstance(Object paramObject)
    throws Exception;
}
