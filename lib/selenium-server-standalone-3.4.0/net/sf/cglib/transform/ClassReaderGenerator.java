package net.sf.cglib.transform;

import net.sf.cglib.asm..Attribute;
import net.sf.cglib.asm..ClassReader;
import net.sf.cglib.asm..ClassVisitor;
import net.sf.cglib.core.ClassGenerator;













public class ClassReaderGenerator
  implements ClassGenerator
{
  private final .ClassReader r;
  private final .Attribute[] attrs;
  private final int flags;
  
  public ClassReaderGenerator(.ClassReader r, int flags)
  {
    this(r, null, flags);
  }
  
  public ClassReaderGenerator(.ClassReader r, .Attribute[] attrs, int flags) {
    this.r = r;
    this.attrs = (attrs != null ? attrs : new .Attribute[0]);
    this.flags = flags;
  }
  
  public void generateClass(.ClassVisitor v) {
    r.accept(v, attrs, flags);
  }
}
