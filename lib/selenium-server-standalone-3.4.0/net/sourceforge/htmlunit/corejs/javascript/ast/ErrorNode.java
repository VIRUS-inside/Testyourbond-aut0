package net.sourceforge.htmlunit.corejs.javascript.ast;







public class ErrorNode
  extends AstNode
{
  private String message;
  





  public ErrorNode()
  {
    type = -1;
  }
  


  public ErrorNode(int pos)
  {
    super(pos);type = -1;
  }
  
  public ErrorNode(int pos, int len) {
    super(pos, len);type = -1;
  }
  


  public String getMessage()
  {
    return message;
  }
  


  public void setMessage(String message)
  {
    this.message = message;
  }
  
  public String toSource(int depth)
  {
    return "";
  }
  




  public void visit(NodeVisitor v)
  {
    v.visit(this);
  }
}
