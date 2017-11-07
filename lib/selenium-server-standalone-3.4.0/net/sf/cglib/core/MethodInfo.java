package net.sf.cglib.core;

import net.sf.cglib.asm..Type;














public abstract class MethodInfo
{
  protected MethodInfo() {}
  
  public abstract ClassInfo getClassInfo();
  
  public abstract int getModifiers();
  
  public abstract Signature getSignature();
  
  public abstract .Type[] getExceptionTypes();
  
  public boolean equals(Object o)
  {
    if (o == null)
      return false;
    if (!(o instanceof MethodInfo))
      return false;
    return getSignature().equals(((MethodInfo)o).getSignature());
  }
  
  public int hashCode() {
    return getSignature().hashCode();
  }
  
  public String toString()
  {
    return getSignature().toString();
  }
}
