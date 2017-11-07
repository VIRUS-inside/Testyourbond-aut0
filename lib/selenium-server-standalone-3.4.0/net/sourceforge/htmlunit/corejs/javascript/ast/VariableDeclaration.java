package net.sourceforge.htmlunit.corejs.javascript.ast;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.htmlunit.corejs.javascript.Node;
import net.sourceforge.htmlunit.corejs.javascript.Token;




















public class VariableDeclaration
  extends AstNode
{
  private List<VariableInitializer> variables = new ArrayList();
  private boolean isStatement;
  
  public VariableDeclaration() {
    type = 122;
  }
  


  public VariableDeclaration(int pos)
  {
    super(pos);type = 122;
  }
  
  public VariableDeclaration(int pos, int len) {
    super(pos, len);type = 122;
  }
  


  public List<VariableInitializer> getVariables()
  {
    return variables;
  }
  





  public void setVariables(List<VariableInitializer> variables)
  {
    assertNotNull(variables);
    this.variables.clear();
    for (VariableInitializer vi : variables) {
      addVariable(vi);
    }
  }
  






  public void addVariable(VariableInitializer v)
  {
    assertNotNull(v);
    variables.add(v);
    v.setParent(this);
  }
  






  public Node setType(int type)
  {
    if ((type != 122) && (type != 154) && (type != 153))
      throw new IllegalArgumentException("invalid decl type: " + type);
    return super.setType(type);
  }
  





  public boolean isVar()
  {
    return type == 122;
  }
  


  public boolean isConst()
  {
    return type == 154;
  }
  


  public boolean isLet()
  {
    return type == 153;
  }
  


  public boolean isStatement()
  {
    return isStatement;
  }
  


  public void setIsStatement(boolean isStatement)
  {
    this.isStatement = isStatement;
  }
  
  private String declTypeName() {
    return Token.typeToName(type).toLowerCase();
  }
  
  public String toSource(int depth)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(makeIndent(depth));
    sb.append(declTypeName());
    sb.append(" ");
    printList(variables, sb);
    if (isStatement()) {
      sb.append(";\n");
    }
    return sb.toString();
  }
  



  public void visit(NodeVisitor v)
  {
    if (v.visit(this)) {
      for (AstNode var : variables) {
        var.visit(v);
      }
    }
  }
}
