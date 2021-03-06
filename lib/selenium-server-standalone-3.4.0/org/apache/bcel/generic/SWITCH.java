package org.apache.bcel.generic;
















public final class SWITCH
  implements CompoundInstruction
{
  private int[] match;
  














  private InstructionHandle[] targets;
  














  private Select instruction;
  














  private int match_length;
  















  public SWITCH(int[] match, InstructionHandle[] targets, InstructionHandle target, int max_gap)
  {
    this.match = ((int[])match.clone());
    this.targets = ((InstructionHandle[])targets.clone());
    
    if ((this.match_length = match.length) < 2) {
      instruction = new TABLESWITCH(match, targets, target);
    } else {
      sort(0, match_length - 1);
      
      if (matchIsOrdered(max_gap)) {
        fillup(max_gap, target);
        
        instruction = new TABLESWITCH(this.match, this.targets, target);
      }
      else {
        instruction = new LOOKUPSWITCH(this.match, this.targets, target);
      }
    }
  }
  
  public SWITCH(int[] match, InstructionHandle[] targets, InstructionHandle target) {
    this(match, targets, target, 1);
  }
  
  private final void fillup(int max_gap, InstructionHandle target) {
    int max_size = match_length + match_length * max_gap;
    int[] m_vec = new int[max_size];
    InstructionHandle[] t_vec = new InstructionHandle[max_size];
    int count = 1;
    
    m_vec[0] = match[0];
    t_vec[0] = targets[0];
    
    for (int i = 1; i < match_length; i++) {
      int prev = match[(i - 1)];
      int gap = match[i] - prev;
      
      for (int j = 1; j < gap; j++) {
        m_vec[count] = (prev + j);
        t_vec[count] = target;
        count++;
      }
      
      m_vec[count] = match[i];
      t_vec[count] = targets[i];
      count++;
    }
    
    match = new int[count];
    targets = new InstructionHandle[count];
    
    System.arraycopy(m_vec, 0, match, 0, count);
    System.arraycopy(t_vec, 0, targets, 0, count);
  }
  


  private final void sort(int l, int r)
  {
    int i = l;int j = r;
    int m = match[((l + r) / 2)];
    
    do
    {
      while (match[i] < m) i++;
      while (m < match[j]) { j--;
      }
      if (i <= j) {
        int h = match[i];match[i] = match[j];match[j] = h;
        InstructionHandle h2 = targets[i];targets[i] = targets[j];targets[j] = h2;
        i++;j--;
      }
    } while (i <= j);
    
    if (l < j) sort(l, j);
    if (i < r) { sort(i, r);
    }
  }
  

  private final boolean matchIsOrdered(int max_gap)
  {
    for (int i = 1; i < match_length; i++) {
      if (match[i] - match[(i - 1)] > max_gap)
        return false;
    }
    return true;
  }
  
  public final InstructionList getInstructionList() {
    return new InstructionList(instruction);
  }
  
  public final Instruction getInstruction() {
    return instruction;
  }
}
