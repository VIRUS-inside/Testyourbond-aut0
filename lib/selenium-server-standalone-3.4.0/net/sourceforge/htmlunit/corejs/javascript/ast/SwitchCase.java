package net.sourceforge.htmlunit.corejs.javascript.ast;

import java.util.ArrayList;
import java.util.List;

























public class SwitchCase
  extends AstNode
{
  private AstNode expression;
  private List<AstNode> statements;
  
  public SwitchCase()
  {
    type = 115;
  }
  


  public SwitchCase(int pos)
  {
    super(pos);type = 115;
  }
  
  public SwitchCase(int pos, int len) {
    super(pos, len);type = 115;
  }
  


  public AstNode getExpression()
  {
    return expression;
  }
  





  public void setExpression(AstNode expression)
  {
    this.expression = expression;
    if (expression != null) {
      expression.setParent(this);
    }
  }
  



  public boolean isDefault()
  {
    return expression == null;
  }
  


  public List<AstNode> getStatements()
  {
    return statements;
  }
  



  public void setStatements(List<AstNode> statements)
  {
    if (this.statements != null) {
      this.statements.clear();
    }
    for (AstNode s : statements) {
      addStatement(s);
    }
  }
  









  public void addStatement(AstNode statement)
  {
    assertNotNull(statement);
    if (statements == null) {
      statements = new ArrayList();
    }
    int end = statement.getPosition() + statement.getLength();
    setLength(end - getPosition());
    statements.add(statement);
    statement.setParent(this);
  }
  
  public String toSource(int depth)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(makeIndent(depth));
    if (expression == null) {
      sb.append("default:\n");
    } else {
      sb.append("case ");
      sb.append(expression.toSource(0));
      sb.append(":\n");
    }
    if (statements != null) {
      for (AstNode s : statements) {
        sb.append(s.toSource(depth + 1));
      }
    }
    return sb.toString();
  }
  




  public void visit(NodeVisitor v)
  {
    if (v.visit(this)) {
      if (expression != null) {
        expression.visit(v);
      }
      if (statements != null) {
        for (AstNode s : statements) {
          s.visit(v);
        }
      }
    }
  }
}
