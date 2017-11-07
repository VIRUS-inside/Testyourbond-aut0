package org.apache.bcel.generic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.util.ByteSequence;


























































public abstract class LocalVariableInstruction
  extends Instruction
  implements TypedInstruction, IndexedInstruction
{
  protected int n = -1;
  private short c_tag = -1;
  private short canon_tag = -1;
  
  private final boolean wide() { return n > 255; }
  





  LocalVariableInstruction(short canon_tag, short c_tag)
  {
    this.canon_tag = canon_tag;
    this.c_tag = c_tag;
  }
  





  LocalVariableInstruction() {}
  




  protected LocalVariableInstruction(short opcode, short c_tag, int n)
  {
    super(opcode, (short)2);
    
    this.c_tag = c_tag;
    canon_tag = opcode;
    
    setIndex(n);
  }
  


  public void dump(DataOutputStream out)
    throws IOException
  {
    if (wide()) {
      out.writeByte(196);
    }
    out.writeByte(opcode);
    
    if (length > 1) {
      if (wide()) {
        out.writeShort(n);
      } else {
        out.writeByte(n);
      }
    }
  }
  







  public String toString(boolean verbose)
  {
    if (((opcode >= 26) && (opcode <= 45)) || ((opcode >= 59) && (opcode <= 78)))
    {


      return super.toString(verbose);
    }
    return super.toString(verbose) + " " + n;
  }
  




  protected void initFromFile(ByteSequence bytes, boolean wide)
    throws IOException
  {
    if (wide) {
      n = bytes.readUnsignedShort();
      length = 4;
    } else if (((opcode >= 21) && (opcode <= 25)) || ((opcode >= 54) && (opcode <= 58)))
    {


      n = bytes.readUnsignedByte();
      length = 2;
    } else if (opcode <= 45) {
      n = ((opcode - 26) % 4);
      length = 1;
    } else {
      n = ((opcode - 59) % 4);
      length = 1;
    }
  }
  

  public final int getIndex()
  {
    return n;
  }
  

  public void setIndex(int n)
  {
    if ((n < 0) || (n > 65535)) {
      throw new ClassGenException("Illegal value: " + n);
    }
    this.n = n;
    
    if ((n >= 0) && (n <= 3)) {
      opcode = ((short)(c_tag + n));
      length = 1;
    } else {
      opcode = canon_tag;
      
      if (wide()) {
        length = 4;
      } else {
        length = 2;
      }
    }
  }
  
  public short getCanonicalTag()
  {
    return canon_tag;
  }
  







  public Type getType(ConstantPoolGen cp)
  {
    switch (canon_tag) {
    case 21: case 54: 
      return Type.INT;
    case 22: case 55: 
      return Type.LONG;
    case 24: case 57: 
      return Type.DOUBLE;
    case 23: case 56: 
      return Type.FLOAT;
    case 25: case 58: 
      return Type.OBJECT;
    }
    throw new ClassGenException("Oops: unknown case in switch" + canon_tag);
  }
}
