package org.apache.bcel.generic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.ExceptionConstants;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.util.ByteSequence;



























































public class MULTIANEWARRAY
  extends CPInstruction
  implements LoadClass, AllocationInstruction, ExceptionThrower
{
  private short dimensions;
  
  MULTIANEWARRAY() {}
  
  public MULTIANEWARRAY(int index, short dimensions)
  {
    super((short)197, index);
    
    if (dimensions < 1) {
      throw new ClassGenException("Invalid dimensions value: " + dimensions);
    }
    this.dimensions = dimensions;
    length = 4;
  }
  


  public void dump(DataOutputStream out)
    throws IOException
  {
    out.writeByte(opcode);
    out.writeShort(index);
    out.writeByte(dimensions);
  }
  



  protected void initFromFile(ByteSequence bytes, boolean wide)
    throws IOException
  {
    super.initFromFile(bytes, wide);
    dimensions = ((short)bytes.readByte());
    length = 4;
  }
  

  public final short getDimensions()
  {
    return dimensions;
  }
  

  public String toString(boolean verbose)
  {
    return super.toString(verbose) + " " + index + " " + dimensions;
  }
  


  public String toString(ConstantPool cp)
  {
    return super.toString(cp) + " " + dimensions;
  }
  





  public int consumeStack(ConstantPoolGen cpg) { return dimensions; }
  
  public Class[] getExceptions() {
    Class[] cs = new Class[2 + ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION.length];
    
    System.arraycopy(ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION, 0, cs, 0, ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION.length);
    

    cs[(ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION.length + 1)] = ExceptionConstants.NEGATIVE_ARRAY_SIZE_EXCEPTION;
    cs[ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION.length] = ExceptionConstants.ILLEGAL_ACCESS_ERROR;
    
    return cs;
  }
  
  public ObjectType getLoadClassType(ConstantPoolGen cpg) {
    Type t = getType(cpg);
    
    if ((t instanceof ArrayType)) {
      t = ((ArrayType)t).getBasicType();
    }
    
    return (t instanceof ObjectType) ? (ObjectType)t : null;
  }
  







  public void accept(Visitor v)
  {
    v.visitLoadClass(this);
    v.visitAllocationInstruction(this);
    v.visitExceptionThrower(this);
    v.visitTypedInstruction(this);
    v.visitCPInstruction(this);
    v.visitMULTIANEWARRAY(this);
  }
}
