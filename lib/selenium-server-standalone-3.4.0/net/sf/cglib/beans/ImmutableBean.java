package net.sf.cglib.beans;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import net.sf.cglib.asm..ClassVisitor;
import net.sf.cglib.asm..Type;
import net.sf.cglib.core.AbstractClassGenerator;
import net.sf.cglib.core.AbstractClassGenerator.Source;
import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.EmitUtils;
import net.sf.cglib.core.MethodInfo;
import net.sf.cglib.core.ReflectUtils;
import net.sf.cglib.core.Signature;
import net.sf.cglib.core.TypeUtils;











public class ImmutableBean
{
  private static final .Type ILLEGAL_STATE_EXCEPTION = TypeUtils.parseType("IllegalStateException");
  
  private static final Signature CSTRUCT_OBJECT = TypeUtils.parseConstructor("Object");
  private static final Class[] OBJECT_CLASSES = { Object.class };
  private static final String FIELD_NAME = "CGLIB$RWBean";
  
  private ImmutableBean() {}
  
  public static Object create(Object bean)
  {
    Generator gen = new Generator();
    gen.setBean(bean);
    return gen.create();
  }
  
  public static class Generator extends AbstractClassGenerator {
    private static final AbstractClassGenerator.Source SOURCE = new AbstractClassGenerator.Source(ImmutableBean.class.getName());
    private Object bean;
    private Class target;
    
    public Generator() {
      super();
    }
    
    public void setBean(Object bean) {
      this.bean = bean;
      target = bean.getClass();
    }
    
    protected ClassLoader getDefaultClassLoader() {
      return target.getClassLoader();
    }
    
    protected ProtectionDomain getProtectionDomain() {
      return ReflectUtils.getProtectionDomain(target);
    }
    
    public Object create() {
      String name = target.getName();
      setNamePrefix(name);
      return super.create(name);
    }
    
    public void generateClass(.ClassVisitor v) {
      .Type targetType = .Type.getType(target);
      ClassEmitter ce = new ClassEmitter(v);
      ce.begin_class(46, 1, 
      
        getClassName(), targetType, null, "<generated>");
      



      ce.declare_field(18, "CGLIB$RWBean", targetType, null);
      
      CodeEmitter e = ce.begin_method(1, ImmutableBean.CSTRUCT_OBJECT, null);
      e.load_this();
      e.super_invoke_constructor();
      e.load_this();
      e.load_arg(0);
      e.checkcast(targetType);
      e.putfield("CGLIB$RWBean");
      e.return_value();
      e.end_method();
      
      PropertyDescriptor[] descriptors = ReflectUtils.getBeanProperties(target);
      Method[] getters = ReflectUtils.getPropertyMethods(descriptors, true, false);
      Method[] setters = ReflectUtils.getPropertyMethods(descriptors, false, true);
      
      for (int i = 0; i < getters.length; i++) {
        MethodInfo getter = ReflectUtils.getMethodInfo(getters[i]);
        e = EmitUtils.begin_method(ce, getter, 1);
        e.load_this();
        e.getfield("CGLIB$RWBean");
        e.invoke(getter);
        e.return_value();
        e.end_method();
      }
      
      for (int i = 0; i < setters.length; i++) {
        MethodInfo setter = ReflectUtils.getMethodInfo(setters[i]);
        e = EmitUtils.begin_method(ce, setter, 1);
        e.throw_exception(ImmutableBean.ILLEGAL_STATE_EXCEPTION, "Bean is immutable");
        e.end_method();
      }
      
      ce.end_class();
    }
    
    protected Object firstInstance(Class type) {
      return ReflectUtils.newInstance(type, ImmutableBean.OBJECT_CLASSES, new Object[] { bean });
    }
    
    protected Object nextInstance(Object instance)
    {
      return firstInstance(instance.getClass());
    }
  }
}
