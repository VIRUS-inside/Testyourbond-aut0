package net.sourceforge.htmlunit.corejs.javascript.ast;







public class ConditionalExpression
  extends AstNode
{
  private AstNode testExpression;
  




  private AstNode trueExpression;
  




  private AstNode falseExpression;
  




  private int questionMarkPosition = -1;
  private int colonPosition = -1;
  
  public ConditionalExpression() {
    type = 102;
  }
  


  public ConditionalExpression(int pos)
  {
    super(pos);type = 102;
  }
  
  public ConditionalExpression(int pos, int len) {
    super(pos, len);type = 102;
  }
  


  public AstNode getTestExpression()
  {
    return testExpression;
  }
  







  public void setTestExpression(AstNode testExpression)
  {
    assertNotNull(testExpression);
    this.testExpression = testExpression;
    testExpression.setParent(this);
  }
  


  public AstNode getTrueExpression()
  {
    return trueExpression;
  }
  








  public void setTrueExpression(AstNode trueExpression)
  {
    assertNotNull(trueExpression);
    this.trueExpression = trueExpression;
    trueExpression.setParent(this);
  }
  


  public AstNode getFalseExpression()
  {
    return falseExpression;
  }
  








  public void setFalseExpression(AstNode falseExpression)
  {
    assertNotNull(falseExpression);
    this.falseExpression = falseExpression;
    falseExpression.setParent(this);
  }
  


  public int getQuestionMarkPosition()
  {
    return questionMarkPosition;
  }
  





  public void setQuestionMarkPosition(int questionMarkPosition)
  {
    this.questionMarkPosition = questionMarkPosition;
  }
  


  public int getColonPosition()
  {
    return colonPosition;
  }
  





  public void setColonPosition(int colonPosition)
  {
    this.colonPosition = colonPosition;
  }
  
  public boolean hasSideEffects()
  {
    if ((testExpression == null) || (trueExpression == null) || (falseExpression == null))
    {
      codeBug(); }
    return (trueExpression.hasSideEffects()) && 
      (falseExpression.hasSideEffects());
  }
  
  public String toSource(int depth)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(makeIndent(depth));
    sb.append(testExpression.toSource(depth));
    sb.append(" ? ");
    sb.append(trueExpression.toSource(0));
    sb.append(" : ");
    sb.append(falseExpression.toSource(0));
    return sb.toString();
  }
  




  public void visit(NodeVisitor v)
  {
    if (v.visit(this)) {
      testExpression.visit(v);
      trueExpression.visit(v);
      falseExpression.visit(v);
    }
  }
}
