package org.apache.bcel.generic;

































public final class BranchHandle
  extends InstructionHandle
{
  private BranchInstruction bi;
  































  private BranchHandle(BranchInstruction i)
  {
    super(i);
    bi = i;
  }
  


  private static BranchHandle bh_list = null;
  
  static final BranchHandle getBranchHandle(BranchInstruction i) {
    if (bh_list == null) {
      return new BranchHandle(i);
    }
    BranchHandle bh = bh_list;
    bh_list = (BranchHandle)next;
    
    bh.setInstruction(i);
    
    return bh;
  }
  


  protected void addHandle()
  {
    next = bh_list;
    bh_list = this;
  }
  


  public int getPosition()
  {
    return bi.position;
  }
  
  void setPosition(int pos) { i_position = (bi.position = pos); }
  
  protected int updatePosition(int offset, int max_offset)
  {
    int x = bi.updatePosition(offset, max_offset);
    i_position = bi.position;
    return x;
  }
  


  public void setTarget(InstructionHandle ih)
  {
    bi.setTarget(ih);
  }
  


  public void updateTarget(InstructionHandle old_ih, InstructionHandle new_ih)
  {
    bi.updateTarget(old_ih, new_ih);
  }
  


  public InstructionHandle getTarget()
  {
    return bi.getTarget();
  }
  


  public void setInstruction(Instruction i)
  {
    super.setInstruction(i);
    
    if (!(i instanceof BranchInstruction)) {
      throw new ClassGenException("Assigning " + i + " to branch handle which is not a branch instruction");
    }
    
    bi = ((BranchInstruction)i);
  }
}
