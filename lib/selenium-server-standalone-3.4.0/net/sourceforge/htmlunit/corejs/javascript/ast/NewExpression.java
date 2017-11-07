package net.sourceforge.htmlunit.corejs.javascript.ast;











public class NewExpression
  extends FunctionCall
{
  private ObjectLiteral initializer;
  










  public NewExpression()
  {
    type = 30;
  }
  


  public NewExpression(int pos)
  {
    super(pos);type = 30;
  }
  
  public NewExpression(int pos, int len) {
    super(pos, len);type = 30;
  }
  





  public ObjectLiteral getInitializer()
  {
    return initializer;
  }
  








  public void setInitializer(ObjectLiteral initializer)
  {
    this.initializer = initializer;
    if (initializer != null) {
      initializer.setParent(this);
    }
  }
  
  public String toSource(int depth) {
    StringBuilder sb = new StringBuilder();
    sb.append(makeIndent(depth));
    sb.append("new ");
    sb.append(target.toSource(0));
    sb.append("(");
    if (arguments != null) {
      printList(arguments, sb);
    }
    sb.append(")");
    if (initializer != null) {
      sb.append(" ");
      sb.append(initializer.toSource(0));
    }
    return sb.toString();
  }
  




  public void visit(NodeVisitor v)
  {
    if (v.visit(this)) {
      target.visit(v);
      for (AstNode arg : getArguments()) {
        arg.visit(v);
      }
      if (initializer != null) {
        initializer.visit(v);
      }
    }
  }
}
