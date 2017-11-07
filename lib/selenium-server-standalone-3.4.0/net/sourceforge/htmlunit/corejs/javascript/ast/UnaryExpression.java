package net.sourceforge.htmlunit.corejs.javascript.ast;

import net.sourceforge.htmlunit.corejs.javascript.Token;


















public class UnaryExpression
  extends AstNode
{
  private AstNode operand;
  private boolean isPostfix;
  
  public UnaryExpression() {}
  
  public UnaryExpression(int pos)
  {
    super(pos);
  }
  


  public UnaryExpression(int pos, int len)
  {
    super(pos, len);
  }
  



  public UnaryExpression(int operator, int operatorPosition, AstNode operand)
  {
    this(operator, operatorPosition, operand, false);
  }
  
















  public UnaryExpression(int operator, int operatorPosition, AstNode operand, boolean postFix)
  {
    assertNotNull(operand);
    int beg = postFix ? operand.getPosition() : operatorPosition;
    

    int end = postFix ? operatorPosition + 2 : operand.getPosition() + operand.getLength();
    setBounds(beg, end);
    setOperator(operator);
    setOperand(operand);
    isPostfix = postFix;
  }
  


  public int getOperator()
  {
    return type;
  }
  






  public void setOperator(int operator)
  {
    if (!Token.isValidToken(operator))
      throw new IllegalArgumentException("Invalid token: " + operator);
    setType(operator);
  }
  
  public AstNode getOperand() {
    return operand;
  }
  





  public void setOperand(AstNode operand)
  {
    assertNotNull(operand);
    this.operand = operand;
    operand.setParent(this);
  }
  


  public boolean isPostfix()
  {
    return isPostfix;
  }
  


  public boolean isPrefix()
  {
    return !isPostfix;
  }
  


  public void setIsPostfix(boolean isPostfix)
  {
    this.isPostfix = isPostfix;
  }
  
  public String toSource(int depth)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(makeIndent(depth));
    int type = getType();
    if (!isPostfix) {
      sb.append(operatorToString(type));
      if ((type == 32) || (type == 31) || (type == 126))
      {
        sb.append(" ");
      }
    }
    sb.append(operand.toSource());
    if (isPostfix) {
      sb.append(operatorToString(type));
    }
    return sb.toString();
  }
  



  public void visit(NodeVisitor v)
  {
    if (v.visit(this)) {
      operand.visit(v);
    }
  }
}
