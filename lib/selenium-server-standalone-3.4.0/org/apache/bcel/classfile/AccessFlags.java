package org.apache.bcel.classfile;























public abstract class AccessFlags
{
  protected int access_flags;
  





















  public AccessFlags() {}
  





















  public AccessFlags(int a)
  {
    access_flags = a;
  }
  

  public final int getAccessFlags()
  {
    return access_flags;
  }
  

  public final void setAccessFlags(int access_flags)
  {
    this.access_flags = access_flags;
  }
  
  private final void setFlag(int flag, boolean set) {
    if ((access_flags & flag) != 0) {
      if (!set) {
        access_flags ^= flag;
      }
    } else if (set) {
      access_flags |= flag;
    }
  }
  
  public final void isPublic(boolean flag) { setFlag(1, flag); }
  
  public final boolean isPublic() { return (access_flags & 0x1) != 0; }
  

  public final void isPrivate(boolean flag) { setFlag(2, flag); }
  
  public final boolean isPrivate() { return (access_flags & 0x2) != 0; }
  

  public final void isProtected(boolean flag) { setFlag(4, flag); }
  
  public final boolean isProtected() { return (access_flags & 0x4) != 0; }
  

  public final void isStatic(boolean flag) { setFlag(8, flag); }
  
  public final boolean isStatic() { return (access_flags & 0x8) != 0; }
  

  public final void isFinal(boolean flag) { setFlag(16, flag); }
  
  public final boolean isFinal() { return (access_flags & 0x10) != 0; }
  

  public final void isSynchronized(boolean flag) { setFlag(32, flag); }
  
  public final boolean isSynchronized() { return (access_flags & 0x20) != 0; }
  

  public final void isVolatile(boolean flag) { setFlag(64, flag); }
  
  public final boolean isVolatile() { return (access_flags & 0x40) != 0; }
  

  public final void isTransient(boolean flag) { setFlag(128, flag); }
  
  public final boolean isTransient() { return (access_flags & 0x80) != 0; }
  

  public final void isNative(boolean flag) { setFlag(256, flag); }
  
  public final boolean isNative() { return (access_flags & 0x100) != 0; }
  

  public final void isInterface(boolean flag) { setFlag(512, flag); }
  
  public final boolean isInterface() { return (access_flags & 0x200) != 0; }
  

  public final void isAbstract(boolean flag) { setFlag(1024, flag); }
  
  public final boolean isAbstract() { return (access_flags & 0x400) != 0; }
  

  public final void isStrictfp(boolean flag) { setFlag(2048, flag); }
  
  public final boolean isStrictfp() { return (access_flags & 0x800) != 0; }
}
