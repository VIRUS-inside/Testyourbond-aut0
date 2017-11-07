package net.sourceforge.htmlunit.corejs.javascript.ast;








public class KeywordLiteral
  extends AstNode
{
  public KeywordLiteral() {}
  






  public KeywordLiteral(int pos)
  {
    super(pos);
  }
  
  public KeywordLiteral(int pos, int len) {
    super(pos, len);
  }
  





  public KeywordLiteral(int pos, int len, int nodeType)
  {
    super(pos, len);
    setType(nodeType);
  }
  






  public KeywordLiteral setType(int nodeType)
  {
    if ((nodeType != 43) && (nodeType != 42) && (nodeType != 45) && (nodeType != 44) && (nodeType != 160))
    {

      throw new IllegalArgumentException("Invalid node type: " + nodeType);
    }
    type = nodeType;
    return this;
  }
  



  public boolean isBooleanLiteral()
  {
    return (type == 45) || (type == 44);
  }
  
  public String toSource(int depth)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(makeIndent(depth));
    switch (getType()) {
    case 43: 
      sb.append("this");
      break;
    case 42: 
      sb.append("null");
      break;
    case 45: 
      sb.append("true");
      break;
    case 44: 
      sb.append("false");
      break;
    case 160: 
      sb.append("debugger;\n");
      break;
    
    default: 
      throw new IllegalStateException("Invalid keyword literal type: " + getType());
    }
    return sb.toString();
  }
  



  public void visit(NodeVisitor v)
  {
    v.visit(this);
  }
}
