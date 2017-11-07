package net.sf.cglib.proxy;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.sf.cglib.asm..ClassVisitor;
import net.sf.cglib.asm..Type;
import net.sf.cglib.core.AbstractClassGenerator;
import net.sf.cglib.core.AbstractClassGenerator.Source;
import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.ReflectUtils;
import net.sf.cglib.core.Signature;















public class InterfaceMaker
  extends AbstractClassGenerator
{
  private static final AbstractClassGenerator.Source SOURCE = new AbstractClassGenerator.Source(InterfaceMaker.class.getName());
  private Map signatures = new HashMap();
  




  public InterfaceMaker()
  {
    super(SOURCE);
  }
  




  public void add(Signature sig, .Type[] exceptions)
  {
    signatures.put(sig, exceptions);
  }
  




  public void add(Method method)
  {
    add(ReflectUtils.getSignature(method), 
      ReflectUtils.getExceptionTypes(method));
  }
  





  public void add(Class clazz)
  {
    Method[] methods = clazz.getMethods();
    for (int i = 0; i < methods.length; i++) {
      Method m = methods[i];
      if (!m.getDeclaringClass().getName().equals("java.lang.Object")) {
        add(m);
      }
    }
  }
  


  public Class create()
  {
    setUseCache(false);
    return (Class)super.create(this);
  }
  
  protected ClassLoader getDefaultClassLoader() {
    return null;
  }
  
  protected Object firstInstance(Class type) {
    return type;
  }
  
  protected Object nextInstance(Object instance) {
    throw new IllegalStateException("InterfaceMaker does not cache");
  }
  
  public void generateClass(.ClassVisitor v) throws Exception {
    ClassEmitter ce = new ClassEmitter(v);
    ce.begin_class(46, 513, 
    
      getClassName(), null, null, "<generated>");
    


    for (Iterator it = signatures.keySet().iterator(); it.hasNext();) {
      Signature sig = (Signature)it.next();
      .Type[] exceptions = (.Type[])signatures.get(sig);
      ce.begin_method(1025, sig, exceptions)
      
        .end_method();
    }
    ce.end_class();
  }
}
