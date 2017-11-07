package org.apache.bcel.generic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.util.ByteSequence;


































































public abstract class BranchInstruction
  extends Instruction
  implements InstructionTargeter
{
  protected int index;
  protected InstructionHandle target;
  protected int position;
  
  BranchInstruction() {}
  
  protected BranchInstruction(short opcode, InstructionHandle target)
  {
    super(opcode, (short)3);
    setTarget(target);
  }
  


  public void dump(DataOutputStream out)
    throws IOException
  {
    out.writeByte(opcode);
    
    index = getTargetOffset();
    
    if (Math.abs(index) >= 32767) {
      throw new ClassGenException("Branch target offset too large for short");
    }
    out.writeShort(index);
  }
  



  protected int getTargetOffset(InstructionHandle target)
  {
    if (target == null) {
      throw new ClassGenException("Target of " + super.toString(true) + " is invalid null handle");
    }
    
    int t = target.getPosition();
    
    if (t < 0) {
      throw new ClassGenException("Invalid branch target position offset for " + super.toString(true) + ":" + t + ":" + target);
    }
    
    return t - position;
  }
  

  protected int getTargetOffset()
  {
    return getTargetOffset(target);
  }
  








  protected int updatePosition(int offset, int max_offset)
  {
    position += offset;
    return 0;
  }
  










  public String toString(boolean verbose)
  {
    String s = super.toString(verbose);
    String t = "null";
    
    if (verbose) {
      if (target != null) {
        if (target.getInstruction() == this) {
          t = "<points to itself>";
        } else if (target.getInstruction() == null) {
          t = "<null instruction!!!?>";
        } else {
          t = target.getInstruction().toString(false);
        }
      }
    } else if (target != null) {
      index = getTargetOffset();
      t = "" + (index + position);
    }
    

    return s + " -> " + t;
  }
  







  protected void initFromFile(ByteSequence bytes, boolean wide)
    throws IOException
  {
    length = 3;
    index = bytes.readShort();
  }
  

  public final int getIndex()
  {
    return index;
  }
  
  public InstructionHandle getTarget()
  {
    return target;
  }
  


  public void setTarget(InstructionHandle target)
  {
    notifyTarget(this.target, target, this);
    this.target = target;
  }
  



  static final void notifyTarget(InstructionHandle old_ih, InstructionHandle new_ih, InstructionTargeter t)
  {
    if (old_ih != null)
      old_ih.removeTargeter(t);
    if (new_ih != null) {
      new_ih.addTargeter(t);
    }
  }
  


  public void updateTarget(InstructionHandle old_ih, InstructionHandle new_ih)
  {
    if (target == old_ih) {
      setTarget(new_ih);
    } else {
      throw new ClassGenException("Not targeting " + old_ih + ", but " + target);
    }
  }
  

  public boolean containsTarget(InstructionHandle ih)
  {
    return target == ih;
  }
  


  void dispose()
  {
    setTarget(null);
    index = -1;
    position = -1;
  }
}
