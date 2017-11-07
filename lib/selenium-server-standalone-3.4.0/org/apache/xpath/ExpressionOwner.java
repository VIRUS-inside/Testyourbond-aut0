package org.apache.xpath;

public abstract interface ExpressionOwner
{
  public abstract Expression getExpression();
  
  public abstract void setExpression(Expression paramExpression);
}
