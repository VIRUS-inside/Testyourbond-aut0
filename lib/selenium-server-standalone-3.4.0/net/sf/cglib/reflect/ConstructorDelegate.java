package net.sf.cglib.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import net.sf.cglib.asm..ClassVisitor;
import net.sf.cglib.asm..Type;
import net.sf.cglib.core.AbstractClassGenerator;
import net.sf.cglib.core.AbstractClassGenerator.Source;
import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.EmitUtils;
import net.sf.cglib.core.KeyFactory;
import net.sf.cglib.core.ReflectUtils;
import net.sf.cglib.core.TypeUtils;












public abstract class ConstructorDelegate
{
  private static final ConstructorKey KEY_FACTORY = (ConstructorKey)KeyFactory.create(ConstructorKey.class, KeyFactory.CLASS_BY_NAME);
  


  protected ConstructorDelegate() {}
  


  public static ConstructorDelegate create(Class targetClass, Class iface)
  {
    Generator gen = new Generator();
    gen.setTargetClass(targetClass);
    gen.setInterface(iface);
    return gen.create();
  }
  
  public static class Generator extends AbstractClassGenerator {
    private static final AbstractClassGenerator.Source SOURCE = new AbstractClassGenerator.Source(ConstructorDelegate.class.getName());
    
    private static final .Type CONSTRUCTOR_DELEGATE = TypeUtils.parseType("net.sf.cglib.reflect.ConstructorDelegate");
    private Class iface;
    private Class targetClass;
    
    public Generator()
    {
      super();
    }
    
    public void setInterface(Class iface) {
      this.iface = iface;
    }
    
    public void setTargetClass(Class targetClass) {
      this.targetClass = targetClass;
    }
    
    public ConstructorDelegate create() {
      setNamePrefix(targetClass.getName());
      Object key = ConstructorDelegate.KEY_FACTORY.newInstance(iface.getName(), targetClass.getName());
      return (ConstructorDelegate)super.create(key);
    }
    
    protected ClassLoader getDefaultClassLoader() {
      return targetClass.getClassLoader();
    }
    
    protected ProtectionDomain getProtectionDomain() {
      return ReflectUtils.getProtectionDomain(targetClass);
    }
    
    public void generateClass(.ClassVisitor v) {
      setNamePrefix(targetClass.getName());
      
      Method newInstance = ReflectUtils.findNewInstance(iface);
      if (!newInstance.getReturnType().isAssignableFrom(targetClass)) {
        throw new IllegalArgumentException("incompatible return type");
      }
      try
      {
        constructor = targetClass.getDeclaredConstructor(newInstance.getParameterTypes());
      } catch (NoSuchMethodException e) { Constructor constructor;
        throw new IllegalArgumentException("interface does not match any known constructor");
      }
      Constructor constructor;
      ClassEmitter ce = new ClassEmitter(v);
      ce.begin_class(46, 1, 
      
        getClassName(), CONSTRUCTOR_DELEGATE, new .Type[] {
        
        .Type.getType(iface) }, "<generated>");
      
      .Type declaring = .Type.getType(constructor.getDeclaringClass());
      EmitUtils.null_constructor(ce);
      CodeEmitter e = ce.begin_method(1, 
        ReflectUtils.getSignature(newInstance), 
        ReflectUtils.getExceptionTypes(newInstance));
      e.new_instance(declaring);
      e.dup();
      e.load_args();
      e.invoke_constructor(declaring, ReflectUtils.getSignature(constructor));
      e.return_value();
      e.end_method();
      ce.end_class();
    }
    
    protected Object firstInstance(Class type) {
      return ReflectUtils.newInstance(type);
    }
    
    protected Object nextInstance(Object instance) {
      return instance;
    }
  }
  
  static abstract interface ConstructorKey
  {
    public abstract Object newInstance(String paramString1, String paramString2);
  }
}
