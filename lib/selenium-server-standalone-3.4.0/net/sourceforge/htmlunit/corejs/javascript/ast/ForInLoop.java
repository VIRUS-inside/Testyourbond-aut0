package net.sourceforge.htmlunit.corejs.javascript.ast;








public class ForInLoop
  extends Loop
{
  protected AstNode iterator;
  





  protected AstNode iteratedObject;
  





  protected int inPosition = -1;
  protected int eachPosition = -1;
  protected boolean isForEach;
  
  public ForInLoop() {
    type = 119;
  }
  


  public ForInLoop(int pos)
  {
    super(pos);type = 119;
  }
  
  public ForInLoop(int pos, int len) {
    super(pos, len);type = 119;
  }
  


  public AstNode getIterator()
  {
    return iterator;
  }
  






  public void setIterator(AstNode iterator)
  {
    assertNotNull(iterator);
    this.iterator = iterator;
    iterator.setParent(this);
  }
  


  public AstNode getIteratedObject()
  {
    return iteratedObject;
  }
  





  public void setIteratedObject(AstNode object)
  {
    assertNotNull(object);
    iteratedObject = object;
    object.setParent(this);
  }
  


  public boolean isForEach()
  {
    return isForEach;
  }
  


  public void setIsForEach(boolean isForEach)
  {
    this.isForEach = isForEach;
  }
  


  public int getInPosition()
  {
    return inPosition;
  }
  






  public void setInPosition(int inPosition)
  {
    this.inPosition = inPosition;
  }
  


  public int getEachPosition()
  {
    return eachPosition;
  }
  





  public void setEachPosition(int eachPosition)
  {
    this.eachPosition = eachPosition;
  }
  
  public String toSource(int depth)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(makeIndent(depth));
    sb.append("for ");
    if (isForEach()) {
      sb.append("each ");
    }
    sb.append("(");
    sb.append(iterator.toSource(0));
    sb.append(" in ");
    sb.append(iteratedObject.toSource(0));
    sb.append(") ");
    if (body.getType() == 129) {
      sb.append(body.toSource(depth).trim()).append("\n");
    } else {
      sb.append("\n").append(body.toSource(depth + 1));
    }
    return sb.toString();
  }
  



  public void visit(NodeVisitor v)
  {
    if (v.visit(this)) {
      iterator.visit(v);
      iteratedObject.visit(v);
      body.visit(v);
    }
  }
}
