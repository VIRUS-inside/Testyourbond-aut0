package net.sf.cglib.core;

import net.sf.cglib.asm..Type;



















public class Signature
{
  private String name;
  private String desc;
  
  public Signature(String name, String desc)
  {
    if (name.indexOf('(') >= 0) {
      throw new IllegalArgumentException("Name '" + name + "' is invalid");
    }
    this.name = name;
    this.desc = desc;
  }
  
  public Signature(String name, .Type returnType, .Type[] argumentTypes) {
    this(name, .Type.getMethodDescriptor(returnType, argumentTypes));
  }
  
  public String getName() {
    return name;
  }
  
  public String getDescriptor() {
    return desc;
  }
  
  public .Type getReturnType() {
    return .Type.getReturnType(desc);
  }
  
  public .Type[] getArgumentTypes() {
    return .Type.getArgumentTypes(desc);
  }
  
  public String toString() {
    return name + desc;
  }
  
  public boolean equals(Object o) {
    if (o == null)
      return false;
    if (!(o instanceof Signature))
      return false;
    Signature other = (Signature)o;
    return (name.equals(name)) && (desc.equals(desc));
  }
  
  public int hashCode() {
    return name.hashCode() ^ desc.hashCode();
  }
}
