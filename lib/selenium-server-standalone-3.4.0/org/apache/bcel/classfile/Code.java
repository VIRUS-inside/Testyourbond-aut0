package org.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;











































































public final class Code
  extends Attribute
{
  private int max_stack;
  private int max_locals;
  private int code_length;
  private byte[] code;
  private int exception_table_length;
  private CodeException[] exception_table;
  private int attributes_count;
  private Attribute[] attributes;
  
  public Code(Code c)
  {
    this(c.getNameIndex(), c.getLength(), c.getMaxStack(), c.getMaxLocals(), c.getCode(), c.getExceptionTable(), c.getAttributes(), c.getConstantPool());
  }
  









  Code(int name_index, int length, DataInputStream file, ConstantPool constant_pool)
    throws IOException
  {
    this(name_index, length, file.readUnsignedShort(), file.readUnsignedShort(), (byte[])null, (CodeException[])null, (Attribute[])null, constant_pool);
    



    code_length = file.readInt();
    code = new byte[code_length];
    file.readFully(code);
    



    exception_table_length = file.readUnsignedShort();
    exception_table = new CodeException[exception_table_length];
    
    for (int i = 0; i < exception_table_length; i++) {
      exception_table[i] = new CodeException(file);
    }
    


    attributes_count = file.readUnsignedShort();
    attributes = new Attribute[attributes_count];
    for (int i = 0; i < attributes_count; i++) {
      attributes[i] = Attribute.readAttribute(file, constant_pool);
    }
    



    this.length = length;
  }
  















  public Code(int name_index, int length, int max_stack, int max_locals, byte[] code, CodeException[] exception_table, Attribute[] attributes, ConstantPool constant_pool)
  {
    super((byte)2, name_index, length, constant_pool);
    
    this.max_stack = max_stack;
    this.max_locals = max_locals;
    
    setCode(code);
    setExceptionTable(exception_table);
    setAttributes(attributes);
  }
  






  public void accept(Visitor v)
  {
    v.visitCode(this);
  }
  





  public final void dump(DataOutputStream file)
    throws IOException
  {
    super.dump(file);
    
    file.writeShort(max_stack);
    file.writeShort(max_locals);
    file.writeInt(code_length);
    file.write(code, 0, code_length);
    
    file.writeShort(exception_table_length);
    for (int i = 0; i < exception_table_length; i++) {
      exception_table[i].dump(file);
    }
    file.writeShort(attributes_count);
    for (int i = 0; i < attributes_count; i++) {
      attributes[i].dump(file);
    }
  }
  

  public final Attribute[] getAttributes()
  {
    return attributes;
  }
  

  public LineNumberTable getLineNumberTable()
  {
    for (int i = 0; i < attributes_count; i++) {
      if ((attributes[i] instanceof LineNumberTable))
        return (LineNumberTable)attributes[i];
    }
    return null;
  }
  


  public LocalVariableTable getLocalVariableTable()
  {
    for (int i = 0; i < attributes_count; i++) {
      if ((attributes[i] instanceof LocalVariableTable))
        return (LocalVariableTable)attributes[i];
    }
    return null;
  }
  

  public final byte[] getCode()
  {
    return code;
  }
  

  public final CodeException[] getExceptionTable()
  {
    return exception_table;
  }
  
  public final int getMaxLocals()
  {
    return max_locals;
  }
  

  public final int getMaxStack()
  {
    return max_stack;
  }
  


  private final int getInternalLength()
  {
    return 8 + code_length + 2 + 8 * exception_table_length + 2;
  }
  







  private final int calculateLength()
  {
    int len = 0;
    
    for (int i = 0; i < attributes_count; i++) {
      len += attributes[i].length + 6;
    }
    return len + getInternalLength();
  }
  


  public final void setAttributes(Attribute[] attributes)
  {
    this.attributes = attributes;
    attributes_count = (attributes == null ? 0 : attributes.length);
    length = calculateLength();
  }
  


  public final void setCode(byte[] code)
  {
    this.code = code;
    code_length = (code == null ? 0 : code.length);
  }
  


  public final void setExceptionTable(CodeException[] exception_table)
  {
    this.exception_table = exception_table;
    exception_table_length = (exception_table == null ? 0 : exception_table.length);
  }
  



  public final void setMaxLocals(int max_locals)
  {
    this.max_locals = max_locals;
  }
  


  public final void setMaxStack(int max_stack)
  {
    this.max_stack = max_stack;
  }
  




  public final String toString(boolean verbose)
  {
    StringBuffer buf = new StringBuffer("Code(max_stack = " + max_stack + ", max_locals = " + max_locals + ", code_length = " + code_length + ")\n" + Utility.codeToString(code, constant_pool, 0, -1, verbose));
    



    if (exception_table_length > 0) {
      buf.append("\nException handler(s) = \nFrom\tTo\tHandler\tType\n");
      
      for (int i = 0; i < exception_table_length; i++) {
        buf.append(exception_table[i].toString(constant_pool, verbose) + "\n");
      }
    }
    if (attributes_count > 0) {
      buf.append("\nAttribute(s) = \n");
      
      for (int i = 0; i < attributes_count; i++) {
        buf.append(attributes[i].toString() + "\n");
      }
    }
    return buf.toString();
  }
  


  public final String toString()
  {
    return toString(true);
  }
  


  public Attribute copy(ConstantPool constant_pool)
  {
    Code c = (Code)clone();
    code = ((byte[])code.clone());
    constant_pool = constant_pool;
    
    exception_table = new CodeException[exception_table_length];
    for (int i = 0; i < exception_table_length; i++) {
      exception_table[i] = exception_table[i].copy();
    }
    attributes = new Attribute[attributes_count];
    for (int i = 0; i < attributes_count; i++) {
      attributes[i] = attributes[i].copy(constant_pool);
    }
    return c;
  }
}
