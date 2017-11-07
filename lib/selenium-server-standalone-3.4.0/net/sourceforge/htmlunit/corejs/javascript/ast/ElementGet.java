package net.sourceforge.htmlunit.corejs.javascript.ast;








public class ElementGet
  extends AstNode
{
  private AstNode target;
  






  private AstNode element;
  





  private int lb = -1;
  private int rb = -1;
  
  public ElementGet() {
    type = 36;
  }
  


  public ElementGet(int pos)
  {
    super(pos);type = 36;
  }
  
  public ElementGet(int pos, int len) {
    super(pos, len);type = 36;
  }
  
  public ElementGet(AstNode target, AstNode element)
  {
    type = 36;
    













    setTarget(target);
    setElement(element);
  }
  


  public AstNode getTarget()
  {
    return target;
  }
  








  public void setTarget(AstNode target)
  {
    assertNotNull(target);
    this.target = target;
    target.setParent(this);
  }
  


  public AstNode getElement()
  {
    return element;
  }
  





  public void setElement(AstNode element)
  {
    assertNotNull(element);
    this.element = element;
    element.setParent(this);
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
  
  public void setParens(int lb, int rb) {
    this.lb = lb;
    this.rb = rb;
  }
  
  public String toSource(int depth)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(makeIndent(depth));
    sb.append(target.toSource(0));
    sb.append("[");
    sb.append(element.toSource(0));
    sb.append("]");
    return sb.toString();
  }
  



  public void visit(NodeVisitor v)
  {
    if (v.visit(this)) {
      target.visit(v);
      element.visit(v);
    }
  }
}
