package net.sf.cglib.transform;








public class ClassFilterTransformer
  extends AbstractClassFilterTransformer
{
  private ClassFilter filter;
  







  public ClassFilterTransformer(ClassFilter filter, ClassTransformer pass)
  {
    super(pass);
    this.filter = filter;
  }
  
  protected boolean accept(int version, int access, String name, String signature, String superName, String[] interfaces) {
    return filter.accept(name.replace('/', '.'));
  }
}
