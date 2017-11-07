package net.sourceforge.htmlunit.corejs.javascript.ast;





public class XmlString
  extends XmlFragment
{
  private String xml;
  



  public XmlString() {}
  



  public XmlString(int pos)
  {
    super(pos);
  }
  
  public XmlString(int pos, String s) {
    super(pos);
    setXml(s);
  }
  








  public void setXml(String s)
  {
    assertNotNull(s);
    xml = s;
    setLength(s.length());
  }
  



  public String getXml()
  {
    return xml;
  }
  
  public String toSource(int depth)
  {
    return makeIndent(depth) + xml;
  }
  



  public void visit(NodeVisitor v)
  {
    v.visit(this);
  }
}
