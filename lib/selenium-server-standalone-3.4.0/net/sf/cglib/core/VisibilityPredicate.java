package net.sf.cglib.core;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import net.sf.cglib.asm..Type;












public class VisibilityPredicate
  implements Predicate
{
  private boolean protectedOk;
  private String pkg;
  private boolean samePackageOk;
  
  public VisibilityPredicate(Class source, boolean protectedOk)
  {
    this.protectedOk = protectedOk;
    

    samePackageOk = (source.getClassLoader() != null);
    pkg = TypeUtils.getPackageName(.Type.getType(source));
  }
  
  public boolean evaluate(Object arg) {
    Member member = (Member)arg;
    int mod = member.getModifiers();
    if (Modifier.isPrivate(mod))
      return false;
    if (Modifier.isPublic(mod))
      return true;
    if ((Modifier.isProtected(mod)) && (protectedOk))
    {
      return true;
    }
    


    return (samePackageOk) && (pkg.equals(TypeUtils.getPackageName(.Type.getType(member.getDeclaringClass()))));
  }
}
