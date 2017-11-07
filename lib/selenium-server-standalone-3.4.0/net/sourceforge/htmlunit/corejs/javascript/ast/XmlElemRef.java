package net.sourceforge.htmlunit.corejs.javascript.ast;



















public class XmlElemRef
  extends XmlRef
{
  private AstNode indexExpr;
  

















  private int lb = -1;
  private int rb = -1;
  
  public XmlElemRef() {
    type = 77;
  }
  


  public XmlElemRef(int pos)
  {
    super(pos);type = 77;
  }
  
  public XmlElemRef(int pos, int len) {
    super(pos, len);type = 77;
  }
  



  public AstNode getExpression()
  {
    return indexExpr;
  }
  





  public void setExpression(AstNode expr)
  {
    assertNotNull(expr);
    indexExpr = expr;
    expr.setParent(this);
  }
  


  public int getLb()
  {
    return lb;
  }
  


  public void setLb(int lb)
  {
    this.lb = lb;
  }
  


  public int getRb()
  {
    return rb;
  }
  


  public void setRb(int rb)
  {
    this.rb = rb;
  }
  


  public void setBrackets(int lb, int rb)
  {
    this.lb = lb;
    this.rb = rb;
  }
  
  public String toSource(int depth)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(makeIndent(depth));
    if (isAttributeAccess()) {
      sb.append("@");
    }
    if (namespace != null) {
      sb.append(namespace.toSource(0));
      sb.append("::");
    }
    sb.append("[");
    sb.append(indexExpr.toSource(0));
    sb.append("]");
    return sb.toString();
  }
  




  public void visit(NodeVisitor v)
  {
    if (v.visit(this)) {
      if (namespace != null) {
        namespace.visit(v);
      }
      indexExpr.visit(v);
    }
  }
}
