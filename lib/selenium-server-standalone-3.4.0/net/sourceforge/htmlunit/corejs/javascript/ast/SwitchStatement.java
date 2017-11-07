package net.sourceforge.htmlunit.corejs.javascript.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;




























public class SwitchStatement
  extends Jump
{
  private static final List<SwitchCase> NO_CASES = Collections.unmodifiableList(new ArrayList());
  
  private AstNode expression;
  private List<SwitchCase> cases;
  private int lp = -1;
  private int rp = -1;
  

  public SwitchStatement() { type = 114; } public SwitchStatement(int pos) { type = 114;
    






    position = pos;
  }
  
  public SwitchStatement(int pos, int len)
  {
    type = 114;
    










    position = pos;
    length = len;
  }
  


  public AstNode getExpression()
  {
    return expression;
  }
  






  public void setExpression(AstNode expression)
  {
    assertNotNull(expression);
    this.expression = expression;
    expression.setParent(this);
  }
  



  public List<SwitchCase> getCases()
  {
    return cases != null ? cases : NO_CASES;
  }
  






  public void setCases(List<SwitchCase> cases)
  {
    if (cases == null) {
      this.cases = null;
    } else {
      if (this.cases != null)
        this.cases.clear();
      for (SwitchCase sc : cases) {
        addCase(sc);
      }
    }
  }
  




  public void addCase(SwitchCase switchCase)
  {
    assertNotNull(switchCase);
    if (cases == null) {
      cases = new ArrayList();
    }
    cases.add(switchCase);
    switchCase.setParent(this);
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
    StringBuilder sb = new StringBuilder();
    sb.append(pad);
    sb.append("switch (");
    sb.append(expression.toSource(0));
    sb.append(") {\n");
    if (cases != null) {
      for (SwitchCase sc : cases) {
        sb.append(sc.toSource(depth + 1));
      }
    }
    sb.append(pad);
    sb.append("}\n");
    return sb.toString();
  }
  




  public void visit(NodeVisitor v)
  {
    if (v.visit(this)) {
      expression.visit(v);
      for (SwitchCase sc : getCases()) {
        sc.visit(v);
      }
    }
  }
}
