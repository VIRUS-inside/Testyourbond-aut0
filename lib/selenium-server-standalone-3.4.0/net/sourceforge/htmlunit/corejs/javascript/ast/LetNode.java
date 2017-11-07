package net.sourceforge.htmlunit.corejs.javascript.ast;









public class LetNode
  extends Scope
{
  private VariableDeclaration variables;
  







  private AstNode body;
  







  private int lp = -1;
  private int rp = -1;
  
  public LetNode() {
    type = 158;
  }
  


  public LetNode(int pos)
  {
    super(pos);type = 158;
  }
  
  public LetNode(int pos, int len) {
    super(pos, len);type = 158;
  }
  


  public VariableDeclaration getVariables()
  {
    return variables;
  }
  





  public void setVariables(VariableDeclaration variables)
  {
    assertNotNull(variables);
    this.variables = variables;
    variables.setParent(this);
  }
  








  public AstNode getBody()
  {
    return body;
  }
  






  public void setBody(AstNode body)
  {
    this.body = body;
    if (body != null) {
      body.setParent(this);
    }
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
    String pad = makeIndent(depth);
    StringBuilder sb = new StringBuilder();
    sb.append(pad);
    sb.append("let (");
    printList(variables.getVariables(), sb);
    sb.append(") ");
    if (body != null) {
      sb.append(body.toSource(depth));
    }
    return sb.toString();
  }
  




  public void visit(NodeVisitor v)
  {
    if (v.visit(this)) {
      variables.visit(v);
      if (body != null) {
        body.visit(v);
      }
    }
  }
}
