package org.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
































































public class LocalVariableTable
  extends Attribute
{
  private int local_variable_table_length;
  private LocalVariable[] local_variable_table;
  
  public LocalVariableTable(LocalVariableTable c)
  {
    this(c.getNameIndex(), c.getLength(), c.getLocalVariableTable(), c.getConstantPool());
  }
  









  public LocalVariableTable(int name_index, int length, LocalVariable[] local_variable_table, ConstantPool constant_pool)
  {
    super((byte)5, name_index, length, constant_pool);
    setLocalVariableTable(local_variable_table);
  }
  








  LocalVariableTable(int name_index, int length, DataInputStream file, ConstantPool constant_pool)
    throws IOException
  {
    this(name_index, length, (LocalVariable[])null, constant_pool);
    
    local_variable_table_length = file.readUnsignedShort();
    local_variable_table = new LocalVariable[local_variable_table_length];
    
    for (int i = 0; i < local_variable_table_length; i++) {
      local_variable_table[i] = new LocalVariable(file, constant_pool);
    }
  }
  





  public void accept(Visitor v)
  {
    v.visitLocalVariableTable(this);
  }
  





  public final void dump(DataOutputStream file)
    throws IOException
  {
    super.dump(file);
    file.writeShort(local_variable_table_length);
    for (int i = 0; i < local_variable_table_length; i++) {
      local_variable_table[i].dump(file);
    }
  }
  

  public final LocalVariable[] getLocalVariableTable()
  {
    return local_variable_table;
  }
  

  public final LocalVariable getLocalVariable(int index)
  {
    for (int i = 0; i < local_variable_table_length; i++) {
      if (local_variable_table[i].getIndex() == index)
        return local_variable_table[i];
    }
    return null;
  }
  
  public final void setLocalVariableTable(LocalVariable[] local_variable_table)
  {
    this.local_variable_table = local_variable_table;
    local_variable_table_length = (local_variable_table == null ? 0 : local_variable_table.length);
  }
  



  public final String toString()
  {
    StringBuffer buf = new StringBuffer("");
    
    for (int i = 0; i < local_variable_table_length; i++) {
      buf.append(local_variable_table[i].toString());
      
      if (i < local_variable_table_length - 1) {
        buf.append('\n');
      }
    }
    return buf.toString();
  }
  


  public Attribute copy(ConstantPool constant_pool)
  {
    LocalVariableTable c = (LocalVariableTable)clone();
    
    local_variable_table = new LocalVariable[local_variable_table_length];
    for (int i = 0; i < local_variable_table_length; i++) {
      local_variable_table[i] = local_variable_table[i].copy();
    }
    constant_pool = constant_pool;
    return c;
  }
  
  public final int getTableLength() { return local_variable_table_length; }
}
