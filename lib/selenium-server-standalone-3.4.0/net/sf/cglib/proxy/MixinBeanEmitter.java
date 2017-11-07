package net.sf.cglib.proxy;

import java.lang.reflect.Method;
import net.sf.cglib.asm..ClassVisitor;
import net.sf.cglib.core.ReflectUtils;

















class MixinBeanEmitter
  extends MixinEmitter
{
  public MixinBeanEmitter(.ClassVisitor v, String className, Class[] classes)
  {
    super(v, className, classes, null);
  }
  
  protected Class[] getInterfaces(Class[] classes) {
    return null;
  }
  
  protected Method[] getMethods(Class type) {
    return ReflectUtils.getPropertyMethods(ReflectUtils.getBeanProperties(type), true, true);
  }
}
