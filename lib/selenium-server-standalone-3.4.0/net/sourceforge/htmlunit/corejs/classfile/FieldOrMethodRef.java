package net.sourceforge.htmlunit.corejs.classfile;































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































final class FieldOrMethodRef
{
  private String className;
  





























































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































  private String name;
  




























































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































  private String type;
  





























































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































  FieldOrMethodRef(String className, String name, String type)
  {
    this.className = className;
    this.name = name;
    this.type = type;
  }
  
  public String getClassName() {
    return className;
  }
  
  public String getName() {
    return name;
  }
  
  public String getType() {
    return type;
  }
  
  public boolean equals(Object obj)
  {
    if (!(obj instanceof FieldOrMethodRef)) {
      return false;
    }
    FieldOrMethodRef x = (FieldOrMethodRef)obj;
    return (className.equals(className)) && (name.equals(name)) && 
      (type.equals(type));
  }
  
  public int hashCode()
  {
    if (hashCode == -1) {
      int h1 = className.hashCode();
      int h2 = name.hashCode();
      int h3 = type.hashCode();
      hashCode = (h1 ^ h2 ^ h3);
    }
    return hashCode;
  }
  



  private int hashCode = -1;
}
