package net.sourceforge.htmlunit.corejs.javascript.ast;

import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;











public class StringLiteral
  extends AstNode
{
  private String value;
  private char quoteChar;
  
  public StringLiteral()
  {
    type = 41;
  }
  


  public StringLiteral(int pos)
  {
    super(pos);type = 41;
  }
  





  public StringLiteral(int pos, int len)
  {
    super(pos, len);type = 41;
  }
  





  public String getValue()
  {
    return value;
  }
  


  public String getValue(boolean includeQuotes)
  {
    if (!includeQuotes)
      return value;
    return quoteChar + value + quoteChar;
  }
  







  public void setValue(String value)
  {
    assertNotNull(value);
    this.value = value;
  }
  


  public char getQuoteCharacter()
  {
    return quoteChar;
  }
  
  public void setQuoteCharacter(char c) {
    quoteChar = c;
  }
  
  public String toSource(int depth)
  {
    return 
    
      makeIndent(depth) + quoteChar + ScriptRuntime.escapeString(value, quoteChar) + quoteChar;
  }
  



  public void visit(NodeVisitor v)
  {
    v.visit(this);
  }
}
