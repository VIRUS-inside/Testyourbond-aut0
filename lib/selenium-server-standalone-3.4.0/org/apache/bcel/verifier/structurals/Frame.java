package org.apache.bcel.verifier.structurals;






















public class Frame
{
  protected static UninitializedObjectType _this;
  




















  private LocalVariables locals;
  



















  private OperandStack stack;
  




















  public Frame(int maxLocals, int maxStack)
  {
    locals = new LocalVariables(maxLocals);
    stack = new OperandStack(maxStack);
  }
  


  public Frame(LocalVariables locals, OperandStack stack)
  {
    this.locals = locals;
    this.stack = stack;
  }
  


  protected Object clone()
  {
    Frame f = new Frame(locals.getClone(), stack.getClone());
    return f;
  }
  


  public Frame getClone()
  {
    return (Frame)clone();
  }
  


  public LocalVariables getLocals()
  {
    return locals;
  }
  


  public OperandStack getStack()
  {
    return stack;
  }
  


  public boolean equals(Object o)
  {
    if (!(o instanceof Frame)) return false;
    Frame f = (Frame)o;
    return (stack.equals(stack)) && (locals.equals(locals));
  }
  


  public String toString()
  {
    String s = "Local Variables:\n";
    s = s + locals;
    s = s + "OperandStack:\n";
    s = s + stack;
    return s;
  }
}
