package net.sourceforge.htmlunit.corejs.javascript.ast;







public class Label
  extends Jump
{
  private String name;
  






  public Label()
  {
    type = 130;
  }
  


  public Label(int pos)
  {
    this(pos, -1);
  }
  
  public Label(int pos, int len)
  {
    type = 130;
    










    position = pos;
    length = len;
  }
  
  public Label(int pos, int len, String name) {
    this(pos, len);
    setName(name);
  }
  


  public String getName()
  {
    return name;
  }
  





  public void setName(String name)
  {
    name = name == null ? null : name.trim();
    if ((name == null) || ("".equals(name)))
      throw new IllegalArgumentException("invalid label name");
    this.name = name;
  }
  
  public String toSource(int depth)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(makeIndent(depth));
    sb.append(name);
    sb.append(":\n");
    return sb.toString();
  }
  



  public void visit(NodeVisitor v)
  {
    v.visit(this);
  }
}
