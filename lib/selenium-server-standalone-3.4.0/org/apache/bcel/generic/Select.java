package org.apache.bcel.generic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.util.ByteSequence;




























































public abstract class Select
  extends BranchInstruction
  implements VariableLengthInstruction, StackProducer
{
  protected int[] match;
  protected int[] indices;
  protected InstructionHandle[] targets;
  protected int fixed_length;
  protected int match_length;
  protected int padding = 0;
  






  Select() {}
  






  Select(short opcode, int[] match, InstructionHandle[] targets, InstructionHandle target)
  {
    super(opcode, target);
    
    this.targets = targets;
    for (int i = 0; i < targets.length; i++) {
      BranchInstruction.notifyTarget(null, targets[i], this);
    }
    this.match = match;
    
    if ((this.match_length = match.length) != targets.length) {
      throw new ClassGenException("Match and target array have not the same length");
    }
    indices = new int[match_length];
  }
  












  protected int updatePosition(int offset, int max_offset)
  {
    position += offset;
    
    short old_length = length;
    


    padding = ((4 - (position + 1) % 4) % 4);
    length = ((short)(fixed_length + padding));
    
    return length - old_length;
  }
  


  public void dump(DataOutputStream out)
    throws IOException
  {
    out.writeByte(opcode);
    
    for (int i = 0; i < padding; i++) {
      out.writeByte(0);
    }
    index = getTargetOffset();
    out.writeInt(index);
  }
  


  protected void initFromFile(ByteSequence bytes, boolean wide)
    throws IOException
  {
    padding = ((4 - bytes.getIndex() % 4) % 4);
    
    for (int i = 0; i < padding; i++) {
      byte b;
      if ((b = bytes.readByte()) != 0) {
        throw new ClassGenException("Padding byte != 0: " + b);
      }
    }
    
    index = bytes.readInt();
  }
  


  public String toString(boolean verbose)
  {
    StringBuffer buf = new StringBuffer(super.toString(verbose));
    
    if (verbose) {
      for (int i = 0; i < match_length; i++) {
        String s = "null";
        
        if (targets[i] != null) {
          s = targets[i].getInstruction().toString();
        }
        buf.append("(" + match[i] + ", " + s + " = {" + indices[i] + "})");
      }
      
    } else {
      buf.append(" ...");
    }
    return buf.toString();
  }
  


  public void setTarget(int i, InstructionHandle target)
  {
    BranchInstruction.notifyTarget(targets[i], target, this);
    targets[i] = target;
  }
  



  public void updateTarget(InstructionHandle old_ih, InstructionHandle new_ih)
  {
    boolean targeted = false;
    
    if (target == old_ih) {
      targeted = true;
      setTarget(new_ih);
    }
    
    for (int i = 0; i < targets.length; i++) {
      if (targets[i] == old_ih) {
        targeted = true;
        setTarget(i, new_ih);
      }
    }
    
    if (!targeted) {
      throw new ClassGenException("Not targeting " + old_ih);
    }
  }
  

  public boolean containsTarget(InstructionHandle ih)
  {
    if (target == ih) {
      return true;
    }
    for (int i = 0; i < targets.length; i++) {
      if (targets[i] == ih)
        return true;
    }
    return false;
  }
  


  void dispose()
  {
    super.dispose();
    
    for (int i = 0; i < targets.length; i++) {
      targets[i].removeTargeter(this);
    }
  }
  
  public int[] getMatchs()
  {
    return match;
  }
  
  public int[] getIndices()
  {
    return indices;
  }
  
  public InstructionHandle[] getTargets()
  {
    return targets;
  }
}
