package net.sf.cglib.transform;

import net.sf.cglib.asm..ClassVisitor;














public abstract class ClassTransformer
  extends .ClassVisitor
{
  public ClassTransformer()
  {
    super(327680);
  }
  
  public ClassTransformer(int opcode) { super(opcode); }
  
  public abstract void setTarget(.ClassVisitor paramClassVisitor);
}
