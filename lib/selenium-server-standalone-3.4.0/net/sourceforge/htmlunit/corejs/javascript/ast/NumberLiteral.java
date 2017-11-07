package net.sourceforge.htmlunit.corejs.javascript.ast;





public class NumberLiteral
  extends AstNode
{
  private String value;
  


  private double number;
  



  public NumberLiteral()
  {
    type = 40;
  }
  


  public NumberLiteral(int pos)
  {
    super(pos);type = 40;
  }
  
  public NumberLiteral(int pos, int len) {
    super(pos, len);type = 40;
  }
  


  public NumberLiteral(int pos, String value)
  {
    super(pos);type = 40;
    setValue(value);
    setLength(value.length());
  }
  


  public NumberLiteral(int pos, String value, double number)
  {
    this(pos, value);
    setDouble(number);
  }
  
  public NumberLiteral(double number)
  {
    type = 40;
    






























    setDouble(number);
    setValue(Double.toString(number));
  }
  


  public String getValue()
  {
    return value;
  }
  





  public void setValue(String value)
  {
    assertNotNull(value);
    this.value = value;
  }
  


  public double getNumber()
  {
    return number;
  }
  


  public void setNumber(double value)
  {
    number = value;
  }
  
  public String toSource(int depth)
  {
    return makeIndent(depth) + (value == null ? "<null>" : value);
  }
  



  public void visit(NodeVisitor v)
  {
    v.visit(this);
  }
}
