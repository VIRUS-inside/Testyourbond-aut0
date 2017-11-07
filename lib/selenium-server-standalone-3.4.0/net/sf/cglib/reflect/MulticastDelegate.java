package net.sf.cglib.reflect;

import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.sf.cglib.asm..ClassVisitor;
import net.sf.cglib.asm..Type;
import net.sf.cglib.core.AbstractClassGenerator;
import net.sf.cglib.core.AbstractClassGenerator.Source;
import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.Constants;
import net.sf.cglib.core.EmitUtils;
import net.sf.cglib.core.Local;
import net.sf.cglib.core.MethodInfo;
import net.sf.cglib.core.ProcessArrayCallback;
import net.sf.cglib.core.ReflectUtils;
import net.sf.cglib.core.Signature;
import net.sf.cglib.core.TypeUtils;



public abstract class MulticastDelegate
  implements Cloneable
{
  protected Object[] targets = new Object[0];
  
  protected MulticastDelegate() {}
  
  public List getTargets()
  {
    return new ArrayList(Arrays.asList(targets));
  }
  
  public abstract MulticastDelegate add(Object paramObject);
  
  protected MulticastDelegate addHelper(Object target) {
    MulticastDelegate copy = newInstance();
    targets = new Object[targets.length + 1];
    System.arraycopy(targets, 0, targets, 0, targets.length);
    targets[targets.length] = target;
    return copy;
  }
  
  public MulticastDelegate remove(Object target) {
    for (int i = targets.length - 1; i >= 0; i--) {
      if (targets[i].equals(target)) {
        MulticastDelegate copy = newInstance();
        targets = new Object[targets.length - 1];
        System.arraycopy(targets, 0, targets, 0, i);
        System.arraycopy(targets, i + 1, targets, i, targets.length - i - 1);
        return copy;
      }
    }
    return this;
  }
  
  public abstract MulticastDelegate newInstance();
  
  public static MulticastDelegate create(Class iface) {
    Generator gen = new Generator();
    gen.setInterface(iface);
    return gen.create();
  }
  
  public static class Generator extends AbstractClassGenerator {
    private static final AbstractClassGenerator.Source SOURCE = new AbstractClassGenerator.Source(MulticastDelegate.class.getName());
    
    private static final .Type MULTICAST_DELEGATE = TypeUtils.parseType("net.sf.cglib.reflect.MulticastDelegate");
    private static final Signature NEW_INSTANCE = new Signature("newInstance", MULTICAST_DELEGATE, new .Type[0]);
    
    private static final Signature ADD_DELEGATE = new Signature("add", MULTICAST_DELEGATE, new .Type[] { Constants.TYPE_OBJECT });
    
    private static final Signature ADD_HELPER = new Signature("addHelper", MULTICAST_DELEGATE, new .Type[] { Constants.TYPE_OBJECT });
    
    private Class iface;
    
    public Generator()
    {
      super();
    }
    
    protected ClassLoader getDefaultClassLoader() {
      return iface.getClassLoader();
    }
    
    protected ProtectionDomain getProtectionDomain() {
      return ReflectUtils.getProtectionDomain(iface);
    }
    
    public void setInterface(Class iface) {
      this.iface = iface;
    }
    
    public MulticastDelegate create() {
      setNamePrefix(MulticastDelegate.class.getName());
      return (MulticastDelegate)super.create(iface.getName());
    }
    
    public void generateClass(.ClassVisitor cv) {
      MethodInfo method = ReflectUtils.getMethodInfo(ReflectUtils.findInterfaceMethod(iface));
      
      ClassEmitter ce = new ClassEmitter(cv);
      ce.begin_class(46, 1, 
      
        getClassName(), MULTICAST_DELEGATE, new .Type[] {
        
        .Type.getType(iface) }, "<generated>");
      
      EmitUtils.null_constructor(ce);
      

      emitProxy(ce, method);
      

      CodeEmitter e = ce.begin_method(1, NEW_INSTANCE, null);
      e.new_instance_this();
      e.dup();
      e.invoke_constructor_this();
      e.return_value();
      e.end_method();
      

      e = ce.begin_method(1, ADD_DELEGATE, null);
      e.load_this();
      e.load_arg(0);
      e.checkcast(.Type.getType(iface));
      e.invoke_virtual_this(ADD_HELPER);
      e.return_value();
      e.end_method();
      
      ce.end_class();
    }
    
    private void emitProxy(ClassEmitter ce, final MethodInfo method) {
      int modifiers = 1;
      if ((method.getModifiers() & 0x80) == 128) {
        modifiers |= 0x80;
      }
      final CodeEmitter e = EmitUtils.begin_method(ce, method, modifiers);
      .Type returnType = method.getSignature().getReturnType();
      final boolean returns = returnType != .Type.VOID_TYPE;
      Local result = null;
      if (returns) {
        result = e.make_local(returnType);
        e.zero_or_null(returnType);
        e.store_local(result);
      }
      e.load_this();
      e.super_getfield("targets", Constants.TYPE_OBJECT_ARRAY);
      final Local result2 = result;
      EmitUtils.process_array(e, Constants.TYPE_OBJECT_ARRAY, new ProcessArrayCallback() {
        public void processElement(.Type type) {
          e.checkcast(.Type.getType(iface));
          e.load_args();
          e.invoke(method);
          if (returns) {
            e.store_local(result2);
          }
        }
      });
      if (returns) {
        e.load_local(result);
      }
      e.return_value();
      e.end_method();
    }
    
    protected Object firstInstance(Class type)
    {
      return ((MulticastDelegate)ReflectUtils.newInstance(type)).newInstance();
    }
    
    protected Object nextInstance(Object instance) {
      return ((MulticastDelegate)instance).newInstance();
    }
  }
}
