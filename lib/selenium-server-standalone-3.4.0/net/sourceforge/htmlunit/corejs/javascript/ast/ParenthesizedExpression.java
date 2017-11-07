package net.sourceforge.htmlunit.corejs.javascript.ast;






public class ParenthesizedExpression
  extends AstNode
{
  private AstNode expression;
  





  public ParenthesizedExpression()
  {
    type = 87;
  }
  


  public ParenthesizedExpression(int pos)
  {
    super(pos);type = 87;
  }
  
  public ParenthesizedExpression(int pos, int len) {
    super(pos, len);type = 87;
  }
  
  public ParenthesizedExpression(AstNode expr) {
    this(expr != null ? expr.getPosition() : 0, expr != null ? expr
      .getLength() : 1, expr);
  }
  
  public ParenthesizedExpression(int pos, int len, AstNode expr) {
    super(pos, len);type = 87;
    setExpression(expr);
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
  
  public String toSource(int depth)
  {
    return makeIndent(depth) + "(" + expression.toSource(0) + ")";
  }
  



  public void visit(NodeVisitor v)
  {
    if (v.visit(this)) {
      expression.visit(v);
    }
  }
}
