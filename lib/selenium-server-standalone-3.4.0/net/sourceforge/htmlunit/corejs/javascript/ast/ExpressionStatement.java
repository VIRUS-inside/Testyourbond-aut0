package net.sourceforge.htmlunit.corejs.javascript.ast;







public class ExpressionStatement
  extends AstNode
{
  private AstNode expr;
  





  public ExpressionStatement()
  {
    type = 133;
  }
  



  public void setHasResult()
  {
    type = 134;
  }
  














  public ExpressionStatement(AstNode expr, boolean hasResult)
  {
    this(expr);
    if (hasResult) {
      setHasResult();
    }
  }
  







  public ExpressionStatement(AstNode expr)
  {
    this(expr.getPosition(), expr.getLength(), expr);
  }
  
  public ExpressionStatement(int pos, int len) {
    super(pos, len);type = 133;
  }
  









  public ExpressionStatement(int pos, int len, AstNode expr)
  {
    super(pos, len);type = 133;
    setExpression(expr);
  }
  


  public AstNode getExpression()
  {
    return expr;
  }
  





  public void setExpression(AstNode expression)
  {
    assertNotNull(expression);
    expr = expression;
    expression.setParent(this);
    setLineno(expression.getLineno());
  }
  






  public boolean hasSideEffects()
  {
    return (type == 134) || (expr.hasSideEffects());
  }
  
  public String toSource(int depth)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(expr.toSource(depth));
    sb.append(";\n");
    return sb.toString();
  }
  



  public void visit(NodeVisitor v)
  {
    if (v.visit(this)) {
      expr.visit(v);
    }
  }
}
