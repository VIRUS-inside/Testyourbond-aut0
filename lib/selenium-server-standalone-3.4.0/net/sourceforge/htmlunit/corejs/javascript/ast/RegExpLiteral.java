package net.sourceforge.htmlunit.corejs.javascript.ast;





public class RegExpLiteral
  extends AstNode
{
  private String value;
  


  private String flags;
  



  public RegExpLiteral()
  {
    type = 48;
  }
  


  public RegExpLiteral(int pos)
  {
    super(pos);type = 48;
  }
  
  public RegExpLiteral(int pos, int len) {
    super(pos, len);type = 48;
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
  


  public String getFlags()
  {
    return flags;
  }
  


  public void setFlags(String flags)
  {
    this.flags = flags;
  }
  
  public String toSource(int depth)
  {
    return makeIndent(depth) + "/" + value + "/" + (flags == null ? "" : flags);
  }
  




  public void visit(NodeVisitor v)
  {
    v.visit(this);
  }
}
