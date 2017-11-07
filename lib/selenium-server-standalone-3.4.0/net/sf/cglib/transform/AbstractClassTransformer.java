package net.sf.cglib.transform;

import net.sf.cglib.asm..ClassVisitor;














public abstract class AbstractClassTransformer
  extends ClassTransformer
{
  protected AbstractClassTransformer()
  {
    super(327680);
  }
  
  public void setTarget(.ClassVisitor target) {
    cv = target;
  }
}
