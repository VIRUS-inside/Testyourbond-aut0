package org.apache.bcel.generic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.util.ByteSequence;



































































public class TABLESWITCH
  extends Select
{
  TABLESWITCH() {}
  
  public TABLESWITCH(int[] match, InstructionHandle[] targets, InstructionHandle target)
  {
    super((short)170, match, targets, target);
    
    length = ((short)(13 + match_length * 4));
    
    fixed_length = length;
  }
  


  public void dump(DataOutputStream out)
    throws IOException
  {
    super.dump(out);
    
    int low = match_length > 0 ? match[0] : 0;
    out.writeInt(low);
    
    int high = match_length > 0 ? match[(match_length - 1)] : 0;
    out.writeInt(high);
    
    for (int i = 0; i < match_length; i++) {
      out.writeInt(indices[i] = getTargetOffset(targets[i]));
    }
  }
  

  protected void initFromFile(ByteSequence bytes, boolean wide)
    throws IOException
  {
    super.initFromFile(bytes, wide);
    
    int low = bytes.readInt();
    int high = bytes.readInt();
    
    match_length = (high - low + 1);
    fixed_length = ((short)(13 + match_length * 4));
    length = ((short)(fixed_length + padding));
    
    match = new int[match_length];
    indices = new int[match_length];
    targets = new InstructionHandle[match_length];
    
    for (int i = low; i <= high; i++) {
      match[(i - low)] = i;
    }
    for (int i = 0; i < match_length; i++) {
      indices[i] = bytes.readInt();
    }
  }
  








  public void accept(Visitor v)
  {
    v.visitVariableLengthInstruction(this);
    v.visitStackProducer(this);
    v.visitBranchInstruction(this);
    v.visitSelect(this);
    v.visitTABLESWITCH(this);
  }
}
