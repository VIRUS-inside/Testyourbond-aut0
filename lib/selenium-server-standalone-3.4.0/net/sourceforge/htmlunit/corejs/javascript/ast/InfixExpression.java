package net.sourceforge.htmlunit.corejs.javascript.ast;

import net.sourceforge.htmlunit.corejs.javascript.Token;










public class InfixExpression
  extends AstNode
{
  protected AstNode left;
  protected AstNode right;
  protected int operatorPosition = -1;
  
  public InfixExpression() {}
  
  public InfixExpression(int pos)
  {
    super(pos);
  }
  
  public InfixExpression(int pos, int len) {
    super(pos, len);
  }
  
  public InfixExpression(int pos, int len, AstNode left, AstNode right) {
    super(pos, len);
    setLeft(left);
    setRight(right);
  }
  



  public InfixExpression(AstNode left, AstNode right)
  {
    setLeftAndRight(left, right);
  }
  






  public InfixExpression(int operator, AstNode left, AstNode right, int operatorPos)
  {
    setType(operator);
    setOperatorPosition(operatorPos - left.getPosition());
    setLeftAndRight(left, right);
  }
  
  public void setLeftAndRight(AstNode left, AstNode right) {
    assertNotNull(left);
    assertNotNull(right);
    
    int beg = left.getPosition();
    int end = right.getPosition() + right.getLength();
    setBounds(beg, end);
    

    setLeft(left);
    setRight(right);
  }
  


  public int getOperator()
  {
    return getType();
  }
  






  public void setOperator(int operator)
  {
    if (!Token.isValidToken(operator))
      throw new IllegalArgumentException("Invalid token: " + operator);
    setType(operator);
  }
  


  public AstNode getLeft()
  {
    return left;
  }
  








  public void setLeft(AstNode left)
  {
    assertNotNull(left);
    this.left = left;
    
    setLineno(left.getLineno());
    left.setParent(this);
  }
  






  public AstNode getRight()
  {
    return right;
  }
  






  public void setRight(AstNode right)
  {
    assertNotNull(right);
    this.right = right;
    right.setParent(this);
  }
  


  public int getOperatorPosition()
  {
    return operatorPosition;
  }
  





  public void setOperatorPosition(int operatorPosition)
  {
    this.operatorPosition = operatorPosition;
  }
  

  public boolean hasSideEffects()
  {
    switch (getType()) {
    case 89: 
      return (right != null) && (right.hasSideEffects());
    case 104: 
    case 105: 
      return ((left != null) && (left.hasSideEffects())) || ((right != null) && 
        (right.hasSideEffects()));
    }
    return super.hasSideEffects();
  }
  

  public String toSource(int depth)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(makeIndent(depth));
    sb.append(left.toSource());
    sb.append(" ");
    sb.append(operatorToString(getType()));
    sb.append(" ");
    sb.append(right.toSource());
    return sb.toString();
  }
  



  public void visit(NodeVisitor v)
  {
    if (v.visit(this)) {
      left.visit(v);
      right.visit(v);
    }
  }
}
