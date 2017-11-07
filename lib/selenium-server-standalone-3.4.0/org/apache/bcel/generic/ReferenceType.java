package org.apache.bcel.generic;

import org.apache.bcel.Constants;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;

























































public class ReferenceType
  extends Type
{
  protected ReferenceType(byte t, String s)
  {
    super(t, s);
  }
  

  ReferenceType()
  {
    super((byte)14, "<null object>");
  }
  







  public boolean isCastableTo(Type t)
  {
    if (equals(Type.NULL)) {
      return true;
    }
    return isAssignmentCompatibleWith(t);
  }
  






  public boolean isAssignmentCompatibleWith(Type t)
  {
    if (!(t instanceof ReferenceType)) {
      return false;
    }
    ReferenceType T = (ReferenceType)t;
    
    if (equals(Type.NULL)) {
      return true;
    }
    

    if (((this instanceof ObjectType)) && (((ObjectType)this).referencesClass()))
    {


      if (((T instanceof ObjectType)) && (((ObjectType)T).referencesClass())) {
        if (equals(T)) {
          return true;
        }
        if (Repository.instanceOf(((ObjectType)this).getClassName(), ((ObjectType)T).getClassName()))
        {
          return true;
        }
      }
      

      if (((T instanceof ObjectType)) && (((ObjectType)T).referencesInterface()) && 
        (Repository.implementationOf(((ObjectType)this).getClassName(), ((ObjectType)T).getClassName())))
      {
        return true;
      }
    }
    


    if (((this instanceof ObjectType)) && (((ObjectType)this).referencesInterface()))
    {

      if (((T instanceof ObjectType)) && (((ObjectType)T).referencesClass()) && 
        (T.equals(Type.OBJECT))) { return true;
      }
      



      if (((T instanceof ObjectType)) && (((ObjectType)T).referencesInterface())) {
        if (equals(T)) return true;
        if (Repository.implementationOf(((ObjectType)this).getClassName(), ((ObjectType)T).getClassName())) {
          return true;
        }
      }
    }
    


    if ((this instanceof ArrayType))
    {

      if (((T instanceof ObjectType)) && (((ObjectType)T).referencesClass()) && 
        (T.equals(Type.OBJECT))) { return true;
      }
      



      if ((T instanceof ArrayType))
      {

        Type sc = ((ArrayType)this).getElementType();
        Type tc = ((ArrayType)this).getElementType();
        
        if (((sc instanceof BasicType)) && ((tc instanceof BasicType)) && (sc.equals(tc))) {
          return true;
        }
        

        if (((tc instanceof ReferenceType)) && ((sc instanceof ReferenceType)) && (((ReferenceType)sc).isAssignmentCompatibleWith((ReferenceType)tc))) {
          return true;
        }
      }
      





      if (((T instanceof ObjectType)) && (((ObjectType)T).referencesInterface())) {
        for (int ii = 0; ii < Constants.INTERFACES_IMPLEMENTED_BY_ARRAYS.length; ii++) {
          if (T.equals(new ObjectType(Constants.INTERFACES_IMPLEMENTED_BY_ARRAYS[ii]))) return true;
        }
      }
    }
    return false;
  }
  











  public ReferenceType firstCommonSuperclass(ReferenceType t)
  {
    if (equals(Type.NULL)) { return t;
    }
    if (t.equals(Type.NULL)) { return this;
    }
    if (equals(t)) { return this;
    }
    





    if (((this instanceof ArrayType)) || ((t instanceof ArrayType))) {
      return Type.OBJECT;
    }
    
    if ((((this instanceof ObjectType)) && (((ObjectType)this).referencesInterface())) || (((t instanceof ObjectType)) && (((ObjectType)t).referencesInterface())))
    {
      return Type.OBJECT;
    }
    




    ObjectType thiz = (ObjectType)this;
    ObjectType other = (ObjectType)t;
    JavaClass[] thiz_sups = Repository.getSuperClasses(thiz.getClassName());
    JavaClass[] other_sups = Repository.getSuperClasses(other.getClassName());
    
    if ((thiz_sups == null) || (other_sups == null)) {
      return null;
    }
    

    JavaClass[] this_sups = new JavaClass[thiz_sups.length + 1];
    JavaClass[] t_sups = new JavaClass[other_sups.length + 1];
    System.arraycopy(thiz_sups, 0, this_sups, 1, thiz_sups.length);
    System.arraycopy(other_sups, 0, t_sups, 1, other_sups.length);
    this_sups[0] = Repository.lookupClass(thiz.getClassName());
    t_sups[0] = Repository.lookupClass(other.getClassName());
    
    for (int i = 0; i < t_sups.length; i++) {
      for (int j = 0; j < this_sups.length; j++) {
        if (this_sups[j].equals(t_sups[i])) { return new ObjectType(this_sups[j].getClassName());
        }
      }
    }
    
    return null;
  }
}
