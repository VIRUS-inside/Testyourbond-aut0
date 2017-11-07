package net.sf.cglib.proxy;

import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.sf.cglib.asm..ClassVisitor;
import net.sf.cglib.core.AbstractClassGenerator;
import net.sf.cglib.core.AbstractClassGenerator.Source;
import net.sf.cglib.core.ClassesKey;
import net.sf.cglib.core.KeyFactory;
import net.sf.cglib.core.ReflectUtils;



















public abstract class Mixin
{
  private static final MixinKey KEY_FACTORY = (MixinKey)KeyFactory.create(MixinKey.class, KeyFactory.CLASS_BY_NAME);
  private static final Map ROUTE_CACHE = Collections.synchronizedMap(new HashMap());
  

  public static final int STYLE_INTERFACES = 0;
  

  public static final int STYLE_BEANS = 1;
  
  public static final int STYLE_EVERYTHING = 2;
  

  public Mixin() {}
  

  public abstract Mixin newInstance(Object[] paramArrayOfObject);
  

  public static Mixin create(Object[] delegates)
  {
    Generator gen = new Generator();
    gen.setDelegates(delegates);
    return gen.create();
  }
  





  public static Mixin create(Class[] interfaces, Object[] delegates)
  {
    Generator gen = new Generator();
    gen.setClasses(interfaces);
    gen.setDelegates(delegates);
    return gen.create();
  }
  

  public static Mixin createBean(Object[] beans)
  {
    return createBean(null, beans);
  }
  





  public static Mixin createBean(ClassLoader loader, Object[] beans)
  {
    Generator gen = new Generator();
    gen.setStyle(1);
    gen.setDelegates(beans);
    gen.setClassLoader(loader);
    return gen.create(); }
  
  static abstract interface MixinKey { public abstract Object newInstance(int paramInt, String[] paramArrayOfString, int[] paramArrayOfInt); }
  
  public static class Generator extends AbstractClassGenerator { private static final AbstractClassGenerator.Source SOURCE = new AbstractClassGenerator.Source(Mixin.class.getName());
    
    private Class[] classes;
    private Object[] delegates;
    private int style = 0;
    private int[] route;
    
    public Generator()
    {
      super();
    }
    
    protected ClassLoader getDefaultClassLoader() {
      return classes[0].getClassLoader();
    }
    
    protected ProtectionDomain getProtectionDomain() {
      return ReflectUtils.getProtectionDomain(classes[0]);
    }
    
    public void setStyle(int style) {
      switch (style) {
      case 0: 
      case 1: 
      case 2: 
        this.style = style;
        break;
      default: 
        throw new IllegalArgumentException("Unknown mixin style: " + style);
      }
    }
    
    public void setClasses(Class[] classes) {
      this.classes = classes;
    }
    
    public void setDelegates(Object[] delegates) {
      this.delegates = delegates;
    }
    
    public Mixin create() {
      if ((classes == null) && (delegates == null)) {
        throw new IllegalStateException("Either classes or delegates must be set");
      }
      switch (style) {
      case 0: 
        if (classes == null) {
          Mixin.Route r = Mixin.route(delegates);
          classes = classes;
          route = route; }
        break;
      

      case 1: 
      case 2: 
        if (classes == null) {
          classes = ReflectUtils.getClasses(delegates);
        }
        else if (delegates != null) {
          Class[] temp = ReflectUtils.getClasses(delegates);
          if (classes.length != temp.length) {
            throw new IllegalStateException("Specified classes are incompatible with delegates");
          }
          for (int i = 0; i < classes.length; i++) {
            if (!classes[i].isAssignableFrom(temp[i])) {
              throw new IllegalStateException("Specified class " + classes[i] + " is incompatible with delegate class " + temp[i] + " (index " + i + ")");
            }
          }
        }
        break;
      }
      setNamePrefix(classes[ReflectUtils.findPackageProtected(classes)].getName());
      
      return (Mixin)super.create(Mixin.KEY_FACTORY.newInstance(style, ReflectUtils.getNames(classes), route));
    }
    
    public void generateClass(.ClassVisitor v) {
      switch (style) {
      case 0: 
        new MixinEmitter(v, getClassName(), classes, route);
        break;
      case 1: 
        new MixinBeanEmitter(v, getClassName(), classes);
        break;
      case 2: 
        new MixinEverythingEmitter(v, getClassName(), classes);
      }
    }
    
    protected Object firstInstance(Class type)
    {
      return ((Mixin)ReflectUtils.newInstance(type)).newInstance(delegates);
    }
    
    protected Object nextInstance(Object instance) {
      return ((Mixin)instance).newInstance(delegates);
    }
  }
  
  public static Class[] getClasses(Object[] delegates) {
    return (Class[])routeclasses.clone();
  }
  



  private static Route route(Object[] delegates)
  {
    Object key = ClassesKey.create(delegates);
    Route route = (Route)ROUTE_CACHE.get(key);
    if (route == null) {
      ROUTE_CACHE.put(key, route = new Route(delegates));
    }
    return route;
  }
  
  private static class Route
  {
    private Class[] classes;
    private int[] route;
    
    Route(Object[] delegates) {
      Map map = new HashMap();
      ArrayList collect = new ArrayList();
      Iterator it; for (int i = 0; i < delegates.length; i++) {
        Class delegate = delegates[i].getClass();
        collect.clear();
        ReflectUtils.addAllInterfaces(delegate, collect);
        for (it = collect.iterator(); it.hasNext();) {
          Class iface = (Class)it.next();
          if (!map.containsKey(iface)) {
            map.put(iface, new Integer(i));
          }
        }
      }
      classes = new Class[map.size()];
      route = new int[map.size()];
      int index = 0;
      for (Iterator it = map.keySet().iterator(); it.hasNext();) {
        Class key = (Class)it.next();
        classes[index] = key;
        route[index] = ((Integer)map.get(key)).intValue();
        index++;
      }
    }
  }
}
