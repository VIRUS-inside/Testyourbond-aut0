package org.apache.bcel.verifier.structurals;

import org.apache.bcel.generic.ReferenceType;
import org.apache.bcel.generic.Type;
import org.apache.bcel.verifier.exc.AssertionViolatedException;
import org.apache.bcel.verifier.exc.StructuralCodeConstraintException;































































public class LocalVariables
{
  private Type[] locals;
  
  public LocalVariables(int maxLocals)
  {
    locals = new Type[maxLocals];
    for (int i = 0; i < maxLocals; i++) {
      locals[i] = Type.UNKNOWN;
    }
  }
  




  protected Object clone()
  {
    LocalVariables lvs = new LocalVariables(locals.length);
    for (int i = 0; i < locals.length; i++) {
      locals[i] = locals[i];
    }
    return lvs;
  }
  


  public Type get(int i)
  {
    return locals[i];
  }
  



  public LocalVariables getClone()
  {
    return (LocalVariables)clone();
  }
  



  public int maxLocals()
  {
    return locals.length;
  }
  


  public void set(int i, Type type)
  {
    if ((type == Type.BYTE) || (type == Type.SHORT) || (type == Type.BOOLEAN) || (type == Type.CHAR)) {
      throw new AssertionViolatedException("LocalVariables do not know about '" + type + "'. Use Type.INT instead.");
    }
    locals[i] = type;
  }
  


  public boolean equals(Object o)
  {
    if (!(o instanceof LocalVariables)) return false;
    LocalVariables lv = (LocalVariables)o;
    if (locals.length != locals.length) return false;
    for (int i = 0; i < locals.length; i++) {
      if (!locals[i].equals(locals[i]))
      {
        return false;
      }
    }
    return true;
  }
  




  public void merge(LocalVariables lv)
  {
    if (locals.length != locals.length) {
      throw new AssertionViolatedException("Merging LocalVariables of different size?!? From different methods or what?!?");
    }
    
    for (int i = 0; i < locals.length; i++) {
      merge(lv, i);
    }
  }
  







  private void merge(LocalVariables lv, int i)
  {
    if ((!(locals[i] instanceof UninitializedObjectType)) && ((locals[i] instanceof UninitializedObjectType))) {
      throw new StructuralCodeConstraintException("Backwards branch with an uninitialized object in the local variables detected.");
    }
    
    if ((!locals[i].equals(locals[i])) && ((locals[i] instanceof UninitializedObjectType)) && ((locals[i] instanceof UninitializedObjectType))) {
      throw new StructuralCodeConstraintException("Backwards branch with an uninitialized object in the local variables detected.");
    }
    
    if (((locals[i] instanceof UninitializedObjectType)) && 
      (!(locals[i] instanceof UninitializedObjectType))) {
      locals[i] = ((UninitializedObjectType)locals[i]).getInitialized();
    }
    
    if (((locals[i] instanceof ReferenceType)) && ((locals[i] instanceof ReferenceType))) {
      if (!locals[i].equals(locals[i])) {
        Type sup = ((ReferenceType)locals[i]).firstCommonSuperclass((ReferenceType)locals[i]);
        
        if (sup != null) {
          locals[i] = sup;
        }
        else
        {
          throw new AssertionViolatedException("Could not load all the super classes of '" + locals[i] + "' and '" + locals[i] + "'.");
        }
        
      }
    }
    else if (!locals[i].equals(locals[i]))
    {





      locals[i] = Type.UNKNOWN;
    }
  }
  



  public String toString()
  {
    String s = new String();
    for (int i = 0; i < locals.length; i++) {
      s = s + Integer.toString(i) + ": " + locals[i] + "\n";
    }
    return s;
  }
  



  public void initializeObject(UninitializedObjectType u)
  {
    for (int i = 0; i < locals.length; i++) {
      if (locals[i] == u) {
        locals[i] = u.getInitialized();
      }
    }
  }
}
