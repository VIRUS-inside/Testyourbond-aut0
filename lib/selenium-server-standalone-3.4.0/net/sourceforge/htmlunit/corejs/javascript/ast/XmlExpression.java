package net.sourceforge.htmlunit.corejs.javascript.ast;




public class XmlExpression
  extends XmlFragment
{
  private AstNode expression;
  


  private boolean isXmlAttribute;
  



  public XmlExpression() {}
  



  public XmlExpression(int pos)
  {
    super(pos);
  }
  
  public XmlExpression(int pos, int len) {
    super(pos, len);
  }
  
  public XmlExpression(int pos, AstNode expr) {
    super(pos);
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
  


  public boolean isXmlAttribute()
  {
    return isXmlAttribute;
  }
  


  public void setIsXmlAttribute(boolean isXmlAttribute)
  {
    this.isXmlAttribute = isXmlAttribute;
  }
  
  public String toSource(int depth)
  {
    return makeIndent(depth) + "{" + expression.toSource(depth) + "}";
  }
  



  public void visit(NodeVisitor v)
  {
    if (v.visit(this)) {
      expression.visit(v);
    }
  }
}
