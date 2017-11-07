package net.sourceforge.htmlunit.corejs.javascript.ast;









public class DoLoop
  extends Loop
{
  private AstNode condition;
  







  private int whilePosition = -1;
  
  public DoLoop() {
    type = 118;
  }
  


  public DoLoop(int pos)
  {
    super(pos);type = 118;
  }
  
  public DoLoop(int pos, int len) {
    super(pos, len);type = 118;
  }
  


  public AstNode getCondition()
  {
    return condition;
  }
  





  public void setCondition(AstNode condition)
  {
    assertNotNull(condition);
    this.condition = condition;
    condition.setParent(this);
  }
  


  public int getWhilePosition()
  {
    return whilePosition;
  }
  


  public void setWhilePosition(int whilePosition)
  {
    this.whilePosition = whilePosition;
  }
  
  public String toSource(int depth)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(makeIndent(depth));
    sb.append("do ");
    sb.append(body.toSource(depth).trim());
    sb.append(" while (");
    sb.append(condition.toSource(0));
    sb.append(");\n");
    return sb.toString();
  }
  



  public void visit(NodeVisitor v)
  {
    if (v.visit(this)) {
      body.visit(v);
      condition.visit(v);
    }
  }
}
