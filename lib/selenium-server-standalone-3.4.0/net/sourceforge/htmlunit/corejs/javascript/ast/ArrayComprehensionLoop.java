package net.sourceforge.htmlunit.corejs.javascript.ast;








public class ArrayComprehensionLoop
  extends ForInLoop
{
  public ArrayComprehensionLoop() {}
  







  public ArrayComprehensionLoop(int pos)
  {
    super(pos);
  }
  
  public ArrayComprehensionLoop(int pos, int len) {
    super(pos, len);
  }
  





  public AstNode getBody()
  {
    return null;
  }
  







  public void setBody(AstNode body)
  {
    throw new UnsupportedOperationException("this node type has no body");
  }
  
  public String toSource(int depth)
  {
    return 
      makeIndent(depth) + " for " + (isForEach() ? "each " : "") + "(" + iterator.toSource(0) + " in " + iteratedObject.toSource(0) + ")";
  }
  





  public void visit(NodeVisitor v)
  {
    if (v.visit(this)) {
      iterator.visit(v);
      iteratedObject.visit(v);
    }
  }
}
