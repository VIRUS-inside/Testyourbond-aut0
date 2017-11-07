package net.sourceforge.htmlunit.corejs.javascript.ast;





















public class XmlDotQuery
  extends InfixExpression
{
  private int rp = -1;
  
  public XmlDotQuery() {
    type = 146;
  }
  


  public XmlDotQuery(int pos)
  {
    super(pos);type = 146;
  }
  
  public XmlDotQuery(int pos, int len) {
    super(pos, len);type = 146;
  }
  







  public int getRp()
  {
    return rp;
  }
  


  public void setRp(int rp)
  {
    this.rp = rp;
  }
  
  public String toSource(int depth)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(makeIndent(depth));
    sb.append(getLeft().toSource(0));
    sb.append(".(");
    sb.append(getRight().toSource(0));
    sb.append(")");
    return sb.toString();
  }
}
