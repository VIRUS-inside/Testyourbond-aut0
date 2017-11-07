package org.apache.bcel.generic;

import java.io.PrintStream;
import org.apache.bcel.classfile.LineNumber;



































































public class LineNumberGen
  implements InstructionTargeter, Cloneable
{
  private InstructionHandle ih;
  private int src_line;
  
  public LineNumberGen(InstructionHandle ih, int src_line)
  {
    setInstruction(ih);
    setSourceLine(src_line);
  }
  


  public boolean containsTarget(InstructionHandle ih)
  {
    return this.ih == ih;
  }
  



  public void updateTarget(InstructionHandle old_ih, InstructionHandle new_ih)
  {
    if (old_ih != ih) {
      throw new ClassGenException("Not targeting " + old_ih + ", but " + ih + "}");
    }
    setInstruction(new_ih);
  }
  





  public LineNumber getLineNumber()
  {
    return new LineNumber(ih.getPosition(), src_line);
  }
  
  public void setInstruction(InstructionHandle ih) {
    BranchInstruction.notifyTarget(this.ih, ih, this);
    
    this.ih = ih;
  }
  
  public Object clone() {
    try {
      return super.clone();
    } catch (CloneNotSupportedException e) {
      System.err.println(e); }
    return null;
  }
  

  public InstructionHandle getInstruction() { return ih; }
  public void setSourceLine(int src_line) { this.src_line = src_line; }
  public int getSourceLine() { return src_line; }
}
