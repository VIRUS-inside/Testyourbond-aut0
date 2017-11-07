package net.sourceforge.htmlunit.corejs.javascript.ast;






public class ForLoop
  extends Loop
{
  private AstNode initializer;
  




  private AstNode condition;
  



  private AstNode increment;
  




  public ForLoop()
  {
    type = 119;
  }
  


  public ForLoop(int pos)
  {
    super(pos);type = 119;
  }
  
  public ForLoop(int pos, int len) {
    super(pos, len);type = 119;
  }
  





  public AstNode getInitializer()
  {
    return initializer;
  }
  










  public void setInitializer(AstNode initializer)
  {
    assertNotNull(initializer);
    this.initializer = initializer;
    initializer.setParent(this);
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
  


  public AstNode getIncrement()
  {
    return increment;
  }
  








  public void setIncrement(AstNode increment)
  {
    assertNotNull(increment);
    this.increment = increment;
    increment.setParent(this);
  }
  
  public String toSource(int depth)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(makeIndent(depth));
    sb.append("for (");
    sb.append(initializer.toSource(0));
    sb.append("; ");
    sb.append(condition.toSource(0));
    sb.append("; ");
    sb.append(increment.toSource(0));
    sb.append(") ");
    if (body.getType() == 129) {
      sb.append(body.toSource(depth).trim()).append("\n");
    } else {
      sb.append("\n").append(body.toSource(depth + 1));
    }
    return sb.toString();
  }
  




  public void visit(NodeVisitor v)
  {
    if (v.visit(this)) {
      initializer.visit(v);
      condition.visit(v);
      increment.visit(v);
      body.visit(v);
    }
  }
}
