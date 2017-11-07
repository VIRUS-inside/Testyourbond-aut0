package net.sourceforge.htmlunit.corejs.javascript.ast;







public class IfStatement
  extends AstNode
{
  private AstNode condition;
  





  private AstNode thenPart;
  




  private int elsePosition = -1;
  private AstNode elsePart;
  private int lp = -1;
  private int rp = -1;
  
  public IfStatement() {
    type = 112;
  }
  


  public IfStatement(int pos)
  {
    super(pos);type = 112;
  }
  
  public IfStatement(int pos, int len) {
    super(pos, len);type = 112;
  }
  


  public AstNode getCondition()
  {
    return condition;
  }
  





  public void setCondition(AstNode condition)
  {
    assertNotNull(condition);
    this.condition = condition;
    condition.setParent(this);
  }
  


  public AstNode getThenPart()
  {
    return thenPart;
  }
  





  public void setThenPart(AstNode thenPart)
  {
    assertNotNull(thenPart);
    this.thenPart = thenPart;
    thenPart.setParent(this);
  }
  


  public AstNode getElsePart()
  {
    return elsePart;
  }
  






  public void setElsePart(AstNode elsePart)
  {
    this.elsePart = elsePart;
    if (elsePart != null) {
      elsePart.setParent(this);
    }
  }
  

  public int getElsePosition()
  {
    return elsePosition;
  }
  


  public void setElsePosition(int elsePosition)
  {
    this.elsePosition = elsePosition;
  }
  


  public int getLp()
  {
    return lp;
  }
  


  public void setLp(int lp)
  {
    this.lp = lp;
  }
  


  public int getRp()
  {
    return rp;
  }
  


  public void setRp(int rp)
  {
    this.rp = rp;
  }
  


  public void setParens(int lp, int rp)
  {
    this.lp = lp;
    this.rp = rp;
  }
  
  public String toSource(int depth)
  {
    String pad = makeIndent(depth);
    StringBuilder sb = new StringBuilder(32);
    sb.append(pad);
    sb.append("if (");
    sb.append(condition.toSource(0));
    sb.append(") ");
    if (thenPart.getType() != 129) {
      sb.append("\n").append(makeIndent(depth + 1));
    }
    sb.append(thenPart.toSource(depth).trim());
    if (elsePart != null) {
      if (thenPart.getType() != 129) {
        sb.append("\n").append(pad).append("else ");
      } else {
        sb.append(" else ");
      }
      if ((elsePart.getType() != 129) && 
        (elsePart.getType() != 112)) {
        sb.append("\n").append(makeIndent(depth + 1));
      }
      sb.append(elsePart.toSource(depth).trim());
    }
    sb.append("\n");
    return sb.toString();
  }
  




  public void visit(NodeVisitor v)
  {
    if (v.visit(this)) {
      condition.visit(v);
      thenPart.visit(v);
      if (elsePart != null) {
        elsePart.visit(v);
      }
    }
  }
}
