package net.sf.cglib.proxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.sf.cglib.asm..ClassVisitor;
import net.sf.cglib.core.CollectionUtils;
import net.sf.cglib.core.ReflectUtils;
import net.sf.cglib.core.RejectModifierPredicate;

















class MixinEverythingEmitter
  extends MixinEmitter
{
  public MixinEverythingEmitter(.ClassVisitor v, String className, Class[] classes)
  {
    super(v, className, classes, null);
  }
  
  protected Class[] getInterfaces(Class[] classes) {
    List list = new ArrayList();
    for (int i = 0; i < classes.length; i++) {
      ReflectUtils.addAllInterfaces(classes[i], list);
    }
    return (Class[])list.toArray(new Class[list.size()]);
  }
  
  protected Method[] getMethods(Class type) {
    List methods = new ArrayList(Arrays.asList(type.getMethods()));
    CollectionUtils.filter(methods, new RejectModifierPredicate(24));
    return (Method[])methods.toArray(new Method[methods.size()]);
  }
}
