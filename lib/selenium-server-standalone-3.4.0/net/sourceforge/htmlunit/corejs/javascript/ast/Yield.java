package net.sourceforge.htmlunit.corejs.javascript.ast;









public class Yield
  extends AstNode
{
  private AstNode value;
  








  public Yield()
  {
    type = 72;
  }
  


  public Yield(int pos)
  {
    super(pos);type = 72;
  }
  
  public Yield(int pos, int len) {
    super(pos, len);type = 72;
  }
  
  public Yield(int pos, int len, AstNode value) {
    super(pos, len);type = 72;
    setValue(value);
  }
  


  public AstNode getValue()
  {
    return value;
  }
  





  public void setValue(AstNode expr)
  {
    value = expr;
    if (expr != null) {
      expr.setParent(this);
    }
  }
  
  public String toSource(int depth) {
    return "yield " + value.toSource(0);
  }
  



  public void visit(NodeVisitor v)
  {
    if ((v.visit(this)) && (value != null)) {
      value.visit(v);
    }
  }
}
