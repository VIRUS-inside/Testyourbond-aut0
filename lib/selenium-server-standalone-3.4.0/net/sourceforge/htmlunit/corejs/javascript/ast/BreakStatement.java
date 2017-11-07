package net.sourceforge.htmlunit.corejs.javascript.ast;







public class BreakStatement
  extends Jump
{
  private Name breakLabel;
  





  private AstNode target;
  





  public BreakStatement() { type = 120; } public BreakStatement(int pos) { type = 120;
    






    position = pos;
  }
  
  public BreakStatement(int pos, int len)
  {
    type = 120;
    










    position = pos;
    length = len;
  }
  





  public Name getBreakLabel()
  {
    return breakLabel;
  }
  







  public void setBreakLabel(Name label)
  {
    breakLabel = label;
    if (label != null) {
      label.setParent(this);
    }
  }
  




  public AstNode getBreakTarget()
  {
    return target;
  }
  







  public void setBreakTarget(Jump target)
  {
    assertNotNull(target);
    this.target = target;
    setJumpStatement(target);
  }
  
  public String toSource(int depth)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(makeIndent(depth));
    sb.append("break");
    if (breakLabel != null) {
      sb.append(" ");
      sb.append(breakLabel.toSource(0));
    }
    sb.append(";\n");
    return sb.toString();
  }
  



  public void visit(NodeVisitor v)
  {
    if ((v.visit(this)) && (breakLabel != null)) {
      breakLabel.visit(v);
    }
  }
}
