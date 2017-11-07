package net.sf.cglib.transform.impl;

import net.sf.cglib.core.ClassGenerator;
import net.sf.cglib.core.DefaultGeneratorStrategy;
import net.sf.cglib.core.TypeUtils;
import net.sf.cglib.transform.ClassTransformer;
import net.sf.cglib.transform.MethodFilter;
import net.sf.cglib.transform.MethodFilterTransformer;
import net.sf.cglib.transform.TransformingClassGenerator;





























public class UndeclaredThrowableStrategy
  extends DefaultGeneratorStrategy
{
  private Class wrapper;
  
  public UndeclaredThrowableStrategy(Class wrapper)
  {
    this.wrapper = wrapper;
  }
  
  private static final MethodFilter TRANSFORM_FILTER = new MethodFilter() {
    public boolean accept(int access, String name, String desc, String signature, String[] exceptions) {
      return (!TypeUtils.isPrivate(access)) && (name.indexOf('$') < 0);
    }
  };
  
  protected ClassGenerator transform(ClassGenerator cg) throws Exception {
    ClassTransformer tr = new UndeclaredThrowableTransformer(wrapper);
    tr = new MethodFilterTransformer(TRANSFORM_FILTER, tr);
    return new TransformingClassGenerator(cg, tr);
  }
}
