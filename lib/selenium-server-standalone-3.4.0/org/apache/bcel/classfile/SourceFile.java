package org.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
































































public final class SourceFile
  extends Attribute
{
  private int sourcefile_index;
  
  public SourceFile(SourceFile c)
  {
    this(c.getNameIndex(), c.getLength(), c.getSourceFileIndex(), c.getConstantPool());
  }
  









  SourceFile(int name_index, int length, DataInputStream file, ConstantPool constant_pool)
    throws IOException
  {
    this(name_index, length, file.readUnsignedShort(), constant_pool);
  }
  







  public SourceFile(int name_index, int length, int sourcefile_index, ConstantPool constant_pool)
  {
    super((byte)0, name_index, length, constant_pool);
    this.sourcefile_index = sourcefile_index;
  }
  






  public void accept(Visitor v)
  {
    v.visitSourceFile(this);
  }
  





  public final void dump(DataOutputStream file)
    throws IOException
  {
    super.dump(file);
    file.writeShort(sourcefile_index);
  }
  

  public final int getSourceFileIndex()
  {
    return sourcefile_index;
  }
  

  public final void setSourceFileIndex(int sourcefile_index)
  {
    this.sourcefile_index = sourcefile_index;
  }
  


  public final String getSourceFileName()
  {
    ConstantUtf8 c = (ConstantUtf8)constant_pool.getConstant(sourcefile_index, (byte)1);
    
    return c.getBytes();
  }
  


  public final String toString()
  {
    return "SourceFile(" + getSourceFileName() + ")";
  }
  


  public Attribute copy(ConstantPool constant_pool)
  {
    return (SourceFile)clone();
  }
}
