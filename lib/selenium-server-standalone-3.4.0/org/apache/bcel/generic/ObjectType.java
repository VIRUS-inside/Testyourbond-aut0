package org.apache.bcel.generic;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.AccessFlags;
import org.apache.bcel.classfile.JavaClass;




























































public final class ObjectType
  extends ReferenceType
{
  private String class_name;
  
  public ObjectType(String class_name)
  {
    super((byte)14, "L" + class_name.replace('.', '/') + ";");
    this.class_name = class_name.replace('/', '.');
  }
  
  public String getClassName()
  {
    return class_name;
  }
  
  public int hashCode() {
    return class_name.hashCode();
  }
  
  public boolean equals(Object type)
  {
    return (type instanceof ObjectType) ? class_name.equals(class_name) : false;
  }
  




  public boolean referencesClass()
  {
    JavaClass jc = Repository.lookupClass(class_name);
    if (jc == null) {
      return false;
    }
    return jc.isClass();
  }
  



  public boolean referencesInterface()
  {
    JavaClass jc = Repository.lookupClass(class_name);
    if (jc == null) {
      return false;
    }
    return !jc.isClass();
  }
  
  public boolean subclassOf(ObjectType superclass) {
    if ((referencesInterface()) || (superclass.referencesInterface())) {
      return false;
    }
    return Repository.instanceOf(class_name, class_name);
  }
  


  public boolean accessibleTo(ObjectType accessor)
  {
    JavaClass jc = Repository.lookupClass(class_name);
    
    if (jc.isPublic()) {
      return true;
    }
    JavaClass acc = Repository.lookupClass(class_name);
    return acc.getPackageName().equals(jc.getPackageName());
  }
}
