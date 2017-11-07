package net.sf.cglib.transform;

import net.sf.cglib.asm..ClassVisitor;














public class ClassTransformerTee
  extends ClassTransformer
{
  private .ClassVisitor branch;
  
  public ClassTransformerTee(.ClassVisitor branch)
  {
    super(327680);
    this.branch = branch;
  }
  
  public void setTarget(.ClassVisitor target) {
    cv = new ClassVisitorTee(branch, target);
  }
}
