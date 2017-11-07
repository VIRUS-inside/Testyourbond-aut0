package org.apache.bcel.generic;

import java.io.PrintStream;
import java.util.AbstractCollection;
import java.util.ArrayList;
import org.apache.bcel.classfile.AccessFlags;
import org.apache.bcel.classfile.Attribute;


























































public abstract class FieldGenOrMethodGen
  extends AccessFlags
  implements NamedAndTyped, Cloneable
{
  protected String name;
  protected Type type;
  protected ConstantPoolGen cp;
  private ArrayList attribute_vec = new ArrayList();
  
  protected FieldGenOrMethodGen() {}
  
  public void setType(Type type) { this.type = type; }
  public Type getType() { return type; }
  


  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  
  public ConstantPoolGen getConstantPool() { return cp; }
  public void setConstantPool(ConstantPoolGen cp) { this.cp = cp; }
  






  public void addAttribute(Attribute a)
  {
    attribute_vec.add(a);
  }
  
  public void removeAttribute(Attribute a)
  {
    attribute_vec.remove(a);
  }
  
  public void removeAttributes()
  {
    attribute_vec.clear();
  }
  

  public Attribute[] getAttributes()
  {
    Attribute[] attributes = new Attribute[attribute_vec.size()];
    attribute_vec.toArray(attributes);
    return attributes;
  }
  
  public abstract String getSignature();
  
  public Object clone()
  {
    try
    {
      return super.clone();
    } catch (CloneNotSupportedException e) {
      System.err.println(e); }
    return null;
  }
}
