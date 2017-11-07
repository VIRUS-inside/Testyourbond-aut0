package net.sourceforge.htmlunit.corejs.javascript.ast;









public class ReturnStatement
  extends AstNode
{
  private AstNode returnValue;
  







  public ReturnStatement()
  {
    type = 4;
  }
  


  public ReturnStatement(int pos)
  {
    super(pos);type = 4;
  }
  
  public ReturnStatement(int pos, int len) {
    super(pos, len);type = 4;
  }
  
  public ReturnStatement(int pos, int len, AstNode returnValue) {
    super(pos, len);type = 4;
    setReturnValue(returnValue);
  }
  


  public AstNode getReturnValue()
  {
    return returnValue;
  }
  



  public void setReturnValue(AstNode returnValue)
  {
    this.returnValue = returnValue;
    if (returnValue != null) {
      returnValue.setParent(this);
    }
  }
  
  public String toSource(int depth) {
    StringBuilder sb = new StringBuilder();
    sb.append(makeIndent(depth));
    sb.append("return");
    if (returnValue != null) {
      sb.append(" ");
      sb.append(returnValue.toSource(0));
    }
    sb.append(";\n");
    return sb.toString();
  }
  



  public void visit(NodeVisitor v)
  {
    if ((v.visit(this)) && (returnValue != null)) {
      returnValue.visit(v);
    }
  }
}
