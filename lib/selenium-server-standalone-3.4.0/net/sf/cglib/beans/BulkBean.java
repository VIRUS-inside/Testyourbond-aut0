package net.sf.cglib.beans;

import java.security.ProtectionDomain;
import net.sf.cglib.asm..ClassVisitor;
import net.sf.cglib.core.AbstractClassGenerator;
import net.sf.cglib.core.AbstractClassGenerator.Source;
import net.sf.cglib.core.KeyFactory;
import net.sf.cglib.core.ReflectUtils;





















public abstract class BulkBean
{
  private static final BulkBeanKey KEY_FACTORY = (BulkBeanKey)KeyFactory.create(BulkBeanKey.class);
  
  protected Class target;
  
  protected String[] getters;
  protected String[] setters;
  protected Class[] types;
  
  protected BulkBean() {}
  
  public abstract void getPropertyValues(Object paramObject, Object[] paramArrayOfObject);
  
  public abstract void setPropertyValues(Object paramObject, Object[] paramArrayOfObject);
  
  public Object[] getPropertyValues(Object bean)
  {
    Object[] values = new Object[getters.length];
    getPropertyValues(bean, values);
    return values;
  }
  
  public Class[] getPropertyTypes() {
    return (Class[])types.clone();
  }
  
  public String[] getGetters() {
    return (String[])getters.clone();
  }
  
  public String[] getSetters() {
    return (String[])setters.clone();
  }
  
  public static BulkBean create(Class target, String[] getters, String[] setters, Class[] types) {
    Generator gen = new Generator();
    gen.setTarget(target);
    gen.setGetters(getters);
    gen.setSetters(setters);
    gen.setTypes(types);
    return gen.create();
  }
  
  public static class Generator extends AbstractClassGenerator {
    private static final AbstractClassGenerator.Source SOURCE = new AbstractClassGenerator.Source(BulkBean.class.getName());
    private Class target;
    private String[] getters;
    private String[] setters;
    private Class[] types;
    
    public Generator() {
      super();
    }
    
    public void setTarget(Class target) {
      this.target = target;
    }
    
    public void setGetters(String[] getters) {
      this.getters = getters;
    }
    
    public void setSetters(String[] setters) {
      this.setters = setters;
    }
    
    public void setTypes(Class[] types) {
      this.types = types;
    }
    
    protected ClassLoader getDefaultClassLoader() {
      return target.getClassLoader();
    }
    
    protected ProtectionDomain getProtectionDomain() {
      return ReflectUtils.getProtectionDomain(target);
    }
    
    public BulkBean create() {
      setNamePrefix(target.getName());
      String targetClassName = target.getName();
      String[] typeClassNames = ReflectUtils.getNames(types);
      Object key = BulkBean.KEY_FACTORY.newInstance(targetClassName, getters, setters, typeClassNames);
      return (BulkBean)super.create(key);
    }
    
    public void generateClass(.ClassVisitor v) throws Exception {
      new BulkBeanEmitter(v, getClassName(), target, getters, setters, types);
    }
    
    protected Object firstInstance(Class type) {
      BulkBean instance = (BulkBean)ReflectUtils.newInstance(type);
      target = target;
      
      int length = getters.length;
      getters = new String[length];
      System.arraycopy(getters, 0, getters, 0, length);
      
      setters = new String[length];
      System.arraycopy(setters, 0, setters, 0, length);
      
      types = new Class[types.length];
      System.arraycopy(types, 0, types, 0, types.length);
      
      return instance;
    }
    
    protected Object nextInstance(Object instance) {
      return instance;
    }
  }
  
  static abstract interface BulkBeanKey
  {
    public abstract Object newInstance(String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2, String[] paramArrayOfString3);
  }
}
