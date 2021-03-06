package net.sf.cglib.beans;

import java.beans.PropertyDescriptor;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.sf.cglib.asm..ClassVisitor;
import net.sf.cglib.asm..Type;
import net.sf.cglib.core.AbstractClassGenerator;
import net.sf.cglib.core.AbstractClassGenerator.Source;
import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.Constants;
import net.sf.cglib.core.EmitUtils;
import net.sf.cglib.core.KeyFactory;
import net.sf.cglib.core.ReflectUtils;









public class BeanGenerator
  extends AbstractClassGenerator
{
  private static final AbstractClassGenerator.Source SOURCE = new AbstractClassGenerator.Source(BeanGenerator.class.getName());
  
  private static final BeanGeneratorKey KEY_FACTORY = (BeanGeneratorKey)KeyFactory.create(BeanGeneratorKey.class);
  


  private Class superclass;
  

  private Map props = new HashMap();
  private boolean classOnly;
  
  public BeanGenerator() {
    super(SOURCE);
  }
  





  public void setSuperclass(Class superclass)
  {
    if ((superclass != null) && (superclass.equals(Object.class))) {
      superclass = null;
    }
    this.superclass = superclass;
  }
  
  public void addProperty(String name, Class type) {
    if (props.containsKey(name)) {
      throw new IllegalArgumentException("Duplicate property name \"" + name + "\"");
    }
    props.put(name, .Type.getType(type));
  }
  
  protected ClassLoader getDefaultClassLoader() {
    if (superclass != null) {
      return superclass.getClassLoader();
    }
    return null;
  }
  
  protected ProtectionDomain getProtectionDomain()
  {
    return ReflectUtils.getProtectionDomain(superclass);
  }
  
  public Object create() {
    classOnly = false;
    return createHelper();
  }
  
  public Object createClass() {
    classOnly = true;
    return createHelper();
  }
  
  private Object createHelper() {
    if (superclass != null) {
      setNamePrefix(superclass.getName());
    }
    String superName = superclass != null ? superclass.getName() : "java.lang.Object";
    Object key = KEY_FACTORY.newInstance(superName, props);
    return super.create(key);
  }
  
  public void generateClass(.ClassVisitor v) throws Exception {
    int size = props.size();
    String[] names = (String[])props.keySet().toArray(new String[size]);
    .Type[] types = new .Type[size];
    for (int i = 0; i < size; i++) {
      types[i] = ((.Type)props.get(names[i]));
    }
    ClassEmitter ce = new ClassEmitter(v);
    ce.begin_class(46, 1, 
    
      getClassName(), superclass != null ? 
      .Type.getType(superclass) : Constants.TYPE_OBJECT, null, null);
    

    EmitUtils.null_constructor(ce);
    EmitUtils.add_properties(ce, names, types);
    ce.end_class();
  }
  
  protected Object firstInstance(Class type) {
    if (classOnly) {
      return type;
    }
    return ReflectUtils.newInstance(type);
  }
  
  protected Object nextInstance(Object instance)
  {
    Class protoclass = (instance instanceof Class) ? (Class)instance : instance.getClass();
    if (classOnly) {
      return protoclass;
    }
    return ReflectUtils.newInstance(protoclass);
  }
  
  public static void addProperties(BeanGenerator gen, Map props)
  {
    for (Iterator it = props.keySet().iterator(); it.hasNext();) {
      String name = (String)it.next();
      gen.addProperty(name, (Class)props.get(name));
    }
  }
  
  public static void addProperties(BeanGenerator gen, Class type) {
    addProperties(gen, ReflectUtils.getBeanProperties(type));
  }
  
  public static void addProperties(BeanGenerator gen, PropertyDescriptor[] descriptors) {
    for (int i = 0; i < descriptors.length; i++) {
      gen.addProperty(descriptors[i].getName(), descriptors[i].getPropertyType());
    }
  }
  
  static abstract interface BeanGeneratorKey
  {
    public abstract Object newInstance(String paramString, Map paramMap);
  }
}
