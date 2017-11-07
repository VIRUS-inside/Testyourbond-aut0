package net.sourceforge.htmlunit.corejs.javascript.ast;









public class WhileLoop
  extends Loop
{
  private AstNode condition;
  







  public WhileLoop()
  {
    type = 117;
  }
  


  public WhileLoop(int pos)
  {
    super(pos);type = 117;
  }
  
  public WhileLoop(int pos, int len) {
    super(pos, len);type = 117;
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
  
  public String toSource(int depth)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(makeIndent(depth));
    sb.append("while (");
    sb.append(condition.toSource(0));
    sb.append(") ");
    if (body.getType() == 129) {
      sb.append(body.toSource(depth).trim());
      sb.append("\n");
    } else {
      sb.append("\n").append(body.toSource(depth + 1));
    }
    return sb.toString();
  }
  



  public void visit(NodeVisitor v)
  {
    if (v.visit(this)) {
      condition.visit(v);
      body.visit(v);
    }
  }
}
