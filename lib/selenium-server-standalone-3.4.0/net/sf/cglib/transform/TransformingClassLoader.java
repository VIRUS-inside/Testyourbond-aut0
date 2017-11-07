package net.sf.cglib.transform;

import net.sf.cglib.asm..ClassReader;
import net.sf.cglib.core.ClassGenerator;














public class TransformingClassLoader
  extends AbstractClassLoader
{
  private ClassTransformerFactory t;
  
  public TransformingClassLoader(ClassLoader parent, ClassFilter filter, ClassTransformerFactory t)
  {
    super(parent, parent, filter);
    this.t = t;
  }
  
  protected ClassGenerator getGenerator(.ClassReader r) {
    ClassTransformer t2 = t.newInstance();
    return new TransformingClassGenerator(super.getGenerator(r), t2);
  }
}
