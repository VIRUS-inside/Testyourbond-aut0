package net.sourceforge.htmlunit.corejs.javascript.ast;

import java.util.ArrayList;
import java.util.List;









public class GeneratorExpression
  extends Scope
{
  private AstNode result;
  private List<GeneratorExpressionLoop> loops = new ArrayList();
  private AstNode filter;
  private int ifPosition = -1;
  private int lp = -1;
  private int rp = -1;
  
  public GeneratorExpression() {
    type = 162;
  }
  


  public GeneratorExpression(int pos)
  {
    super(pos);type = 162;
  }
  
  public GeneratorExpression(int pos, int len) {
    super(pos, len);type = 162;
  }
  


  public AstNode getResult()
  {
    return result;
  }
  





  public void setResult(AstNode result)
  {
    assertNotNull(result);
    this.result = result;
    result.setParent(this);
  }
  


  public List<GeneratorExpressionLoop> getLoops()
  {
    return loops;
  }
  





  public void setLoops(List<GeneratorExpressionLoop> loops)
  {
    assertNotNull(loops);
    this.loops.clear();
    for (GeneratorExpressionLoop acl : loops) {
      addLoop(acl);
    }
  }
  





  public void addLoop(GeneratorExpressionLoop acl)
  {
    assertNotNull(acl);
    loops.add(acl);
    acl.setParent(this);
  }
  


  public AstNode getFilter()
  {
    return filter;
  }
  



  public void setFilter(AstNode filter)
  {
    this.filter = filter;
    if (filter != null) {
      filter.setParent(this);
    }
  }
  

  public int getIfPosition()
  {
    return ifPosition;
  }
  


  public void setIfPosition(int ifPosition)
  {
    this.ifPosition = ifPosition;
  }
  


  public int getFilterLp()
  {
    return lp;
  }
  


  public void setFilterLp(int lp)
  {
    this.lp = lp;
  }
  


  public int getFilterRp()
  {
    return rp;
  }
  


  public void setFilterRp(int rp)
  {
    this.rp = rp;
  }
  
  public String toSource(int depth)
  {
    StringBuilder sb = new StringBuilder(250);
    sb.append("(");
    sb.append(result.toSource(0));
    for (GeneratorExpressionLoop loop : loops) {
      sb.append(loop.toSource(0));
    }
    if (filter != null) {
      sb.append(" if (");
      sb.append(filter.toSource(0));
      sb.append(")");
    }
    sb.append(")");
    return sb.toString();
  }
  




  public void visit(NodeVisitor v)
  {
    if (!v.visit(this)) {
      return;
    }
    result.visit(v);
    for (GeneratorExpressionLoop loop : loops) {
      loop.visit(v);
    }
    if (filter != null) {
      filter.visit(v);
    }
  }
}
