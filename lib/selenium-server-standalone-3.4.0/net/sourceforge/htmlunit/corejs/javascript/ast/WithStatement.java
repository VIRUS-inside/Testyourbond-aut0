package net.sourceforge.htmlunit.corejs.javascript.ast;







public class WithStatement
  extends AstNode
{
  private AstNode expression;
  




  private AstNode statement;
  




  private int lp = -1;
  private int rp = -1;
  
  public WithStatement() {
    type = 123;
  }
  


  public WithStatement(int pos)
  {
    super(pos);type = 123;
  }
  
  public WithStatement(int pos, int len) {
    super(pos, len);type = 123;
  }
  


  public AstNode getExpression()
  {
    return expression;
  }
  





  public void setExpression(AstNode expression)
  {
    assertNotNull(expression);
    this.expression = expression;
    expression.setParent(this);
  }
  


  public AstNode getStatement()
  {
    return statement;
  }
  





  public void setStatement(AstNode statement)
  {
    assertNotNull(statement);
    this.statement = statement;
    statement.setParent(this);
  }
  


  public int getLp()
  {
    return lp;
  }
  


  public void setLp(int lp)
  {
    this.lp = lp;
  }
  


  public int getRp()
  {
    return rp;
  }
  


  public void setRp(int rp)
  {
    this.rp = rp;
  }
  


  public void setParens(int lp, int rp)
  {
    this.lp = lp;
    this.rp = rp;
  }
  
  public String toSource(int depth)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(makeIndent(depth));
    sb.append("with (");
    sb.append(expression.toSource(0));
    sb.append(") ");
    if (statement.getType() == 129) {
      sb.append(statement.toSource(depth).trim());
      sb.append("\n");
    } else {
      sb.append("\n").append(statement.toSource(depth + 1));
    }
    return sb.toString();
  }
  



  public void visit(NodeVisitor v)
  {
    if (v.visit(this)) {
      expression.visit(v);
      statement.visit(v);
    }
  }
}
