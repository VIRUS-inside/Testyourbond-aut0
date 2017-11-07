package net.sf.cglib.beans;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;
import net.sf.cglib.asm..ClassVisitor;
import net.sf.cglib.asm..Type;
import net.sf.cglib.core.AbstractClassGenerator;
import net.sf.cglib.core.AbstractClassGenerator.Source;
import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.Constants;
import net.sf.cglib.core.Converter;
import net.sf.cglib.core.EmitUtils;
import net.sf.cglib.core.KeyFactory;
import net.sf.cglib.core.Local;
import net.sf.cglib.core.MethodInfo;
import net.sf.cglib.core.ReflectUtils;
import net.sf.cglib.core.Signature;
import net.sf.cglib.core.TypeUtils;







public abstract class BeanCopier
{
  private static final BeanCopierKey KEY_FACTORY = (BeanCopierKey)KeyFactory.create(BeanCopierKey.class);
  
  private static final .Type CONVERTER = TypeUtils.parseType("net.sf.cglib.core.Converter");
  
  private static final .Type BEAN_COPIER = TypeUtils.parseType("net.sf.cglib.beans.BeanCopier");
  private static final Signature COPY = new Signature("copy", .Type.VOID_TYPE, new .Type[] { Constants.TYPE_OBJECT, Constants.TYPE_OBJECT, CONVERTER });
  

  private static final Signature CONVERT = TypeUtils.parseSignature("Object convert(Object, Class, Object)");
  

  public BeanCopier() {}
  
  public static BeanCopier create(Class source, Class target, boolean useConverter)
  {
    Generator gen = new Generator();
    gen.setSource(source);
    gen.setTarget(target);
    gen.setUseConverter(useConverter);
    return gen.create();
  }
  
  public abstract void copy(Object paramObject1, Object paramObject2, Converter paramConverter);
  
  public static class Generator extends AbstractClassGenerator {
    private static final AbstractClassGenerator.Source SOURCE = new AbstractClassGenerator.Source(BeanCopier.class.getName());
    private Class source;
    private Class target;
    private boolean useConverter;
    
    public Generator() {
      super();
    }
    
    public void setSource(Class source) {
      if (!Modifier.isPublic(source.getModifiers())) {
        setNamePrefix(source.getName());
      }
      this.source = source;
    }
    
    public void setTarget(Class target) {
      if (!Modifier.isPublic(target.getModifiers())) {
        setNamePrefix(target.getName());
      }
      
      this.target = target;
    }
    
    public void setUseConverter(boolean useConverter) {
      this.useConverter = useConverter;
    }
    
    protected ClassLoader getDefaultClassLoader() {
      return source.getClassLoader();
    }
    
    protected ProtectionDomain getProtectionDomain() {
      return ReflectUtils.getProtectionDomain(source);
    }
    
    public BeanCopier create() {
      Object key = BeanCopier.KEY_FACTORY.newInstance(source.getName(), target.getName(), useConverter);
      return (BeanCopier)super.create(key);
    }
    
    public void generateClass(.ClassVisitor v) {
      .Type sourceType = .Type.getType(source);
      .Type targetType = .Type.getType(target);
      ClassEmitter ce = new ClassEmitter(v);
      ce.begin_class(46, 1, 
      
        getClassName(), 
        BeanCopier.BEAN_COPIER, null, "<generated>");
      


      EmitUtils.null_constructor(ce);
      CodeEmitter e = ce.begin_method(1, BeanCopier.COPY, null);
      PropertyDescriptor[] getters = ReflectUtils.getBeanGetters(source);
      PropertyDescriptor[] setters = ReflectUtils.getBeanSetters(target);
      
      Map names = new HashMap();
      for (int i = 0; i < getters.length; i++) {
        names.put(getters[i].getName(), getters[i]);
      }
      Local targetLocal = e.make_local();
      Local sourceLocal = e.make_local();
      if (useConverter) {
        e.load_arg(1);
        e.checkcast(targetType);
        e.store_local(targetLocal);
        e.load_arg(0);
        e.checkcast(sourceType);
        e.store_local(sourceLocal);
      } else {
        e.load_arg(1);
        e.checkcast(targetType);
        e.load_arg(0);
        e.checkcast(sourceType);
      }
      for (int i = 0; i < setters.length; i++) {
        PropertyDescriptor setter = setters[i];
        PropertyDescriptor getter = (PropertyDescriptor)names.get(setter.getName());
        if (getter != null) {
          MethodInfo read = ReflectUtils.getMethodInfo(getter.getReadMethod());
          MethodInfo write = ReflectUtils.getMethodInfo(setter.getWriteMethod());
          if (useConverter) {
            .Type setterType = write.getSignature().getArgumentTypes()[0];
            e.load_local(targetLocal);
            e.load_arg(2);
            e.load_local(sourceLocal);
            e.invoke(read);
            e.box(read.getSignature().getReturnType());
            EmitUtils.load_class(e, setterType);
            e.push(write.getSignature().getName());
            e.invoke_interface(BeanCopier.CONVERTER, BeanCopier.CONVERT);
            e.unbox_or_zero(setterType);
            e.invoke(write);
          } else if (compatible(getter, setter)) {
            e.dup2();
            e.invoke(read);
            e.invoke(write);
          }
        }
      }
      e.return_value();
      e.end_method();
      ce.end_class();
    }
    
    private static boolean compatible(PropertyDescriptor getter, PropertyDescriptor setter)
    {
      return setter.getPropertyType().isAssignableFrom(getter.getPropertyType());
    }
    
    protected Object firstInstance(Class type) {
      return ReflectUtils.newInstance(type);
    }
    
    protected Object nextInstance(Object instance) {
      return instance;
    }
  }
  
  static abstract interface BeanCopierKey
  {
    public abstract Object newInstance(String paramString1, String paramString2, boolean paramBoolean);
  }
}
