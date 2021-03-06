package org.apache.bcel.verifier.statics;

import java.util.Hashtable;
import org.apache.bcel.generic.Type;
import org.apache.bcel.verifier.exc.LocalVariableInfoInconsistentException;


































































public class LocalVariableInfo
{
  private Hashtable types = new Hashtable();
  
  private Hashtable names = new Hashtable();
  

  public LocalVariableInfo() {}
  
  private void setName(int offset, String name)
  {
    names.put(Integer.toString(offset), name);
  }
  


  private void setType(int offset, Type t)
  {
    types.put(Integer.toString(offset), t);
  }
  







  public Type getType(int offset)
  {
    return (Type)types.get(Integer.toString(offset));
  }
  






  public String getName(int offset)
  {
    return (String)names.get(Integer.toString(offset));
  }
  


  public void add(String name, int startpc, int length, Type t)
    throws LocalVariableInfoInconsistentException
  {
    for (int i = startpc; i <= startpc + length; i++) {
      add(i, name, t);
    }
  }
  



  private void add(int offset, String name, Type t)
    throws LocalVariableInfoInconsistentException
  {
    if ((getName(offset) != null) && 
      (!getName(offset).equals(name))) {
      throw new LocalVariableInfoInconsistentException("At bytecode offset '" + offset + "' a local variable has two different names: '" + getName(offset) + "' and '" + name + "'.");
    }
    
    if ((getType(offset) != null) && 
      (!getType(offset).equals(t))) {
      throw new LocalVariableInfoInconsistentException("At bytecode offset '" + offset + "' a local variable has two different types: '" + getType(offset) + "' and '" + t + "'.");
    }
    
    setName(offset, name);
    setType(offset, t);
  }
}
