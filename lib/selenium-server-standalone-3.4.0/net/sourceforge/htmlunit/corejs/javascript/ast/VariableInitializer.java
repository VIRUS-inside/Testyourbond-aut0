package net.sourceforge.htmlunit.corejs.javascript.ast;







public class VariableInitializer
  extends AstNode
{
  private AstNode target;
  




  private AstNode initializer;
  





  public VariableInitializer()
  {
    type = 122;
  }
  






  public void setNodeType(int nodeType)
  {
    if ((nodeType != 122) && (nodeType != 154) && (nodeType != 153))
    {
      throw new IllegalArgumentException("invalid node type"); }
    setType(nodeType);
  }
  


  public VariableInitializer(int pos)
  {
    super(pos);type = 122;
  }
  
  public VariableInitializer(int pos, int len) {
    super(pos, len);type = 122;
  }
  






  public boolean isDestructuring()
  {
    return !(target instanceof Name);
  }
  


  public AstNode getTarget()
  {
    return target;
  }
  








  public void setTarget(AstNode target)
  {
    if (target == null)
      throw new IllegalArgumentException("invalid target arg");
    this.target = target;
    target.setParent(this);
  }
  


  public AstNode getInitializer()
  {
    return initializer;
  }
  





  public void setInitializer(AstNode initializer)
  {
    this.initializer = initializer;
    if (initializer != null) {
      initializer.setParent(this);
    }
  }
  
  public String toSource(int depth) {
    StringBuilder sb = new StringBuilder();
    sb.append(makeIndent(depth));
    sb.append(target.toSource(0));
    if (initializer != null) {
      sb.append(" = ");
      sb.append(initializer.toSource(0));
    }
    return sb.toString();
  }
  




  public void visit(NodeVisitor v)
  {
    if (v.visit(this)) {
      target.visit(v);
      if (initializer != null) {
        initializer.visit(v);
      }
    }
  }
}
