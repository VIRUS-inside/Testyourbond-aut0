package net.sourceforge.htmlunit.corejs.javascript.ast;

import java.util.ArrayList;
import java.util.List;

















public class LabeledStatement
  extends AstNode
{
  private List<Label> labels = new ArrayList();
  private AstNode statement;
  
  public LabeledStatement() {
    type = 133;
  }
  


  public LabeledStatement(int pos)
  {
    super(pos);type = 133;
  }
  
  public LabeledStatement(int pos, int len) {
    super(pos, len);type = 133;
  }
  


  public List<Label> getLabels()
  {
    return labels;
  }
  






  public void setLabels(List<Label> labels)
  {
    assertNotNull(labels);
    if (this.labels != null)
      this.labels.clear();
    for (Label l : labels) {
      addLabel(l);
    }
  }
  





  public void addLabel(Label label)
  {
    assertNotNull(label);
    labels.add(label);
    label.setParent(this);
  }
  


  public AstNode getStatement()
  {
    return statement;
  }
  




  public Label getLabelByName(String name)
  {
    for (Label label : labels) {
      if (name.equals(label.getName())) {
        return label;
      }
    }
    return null;
  }
  





  public void setStatement(AstNode statement)
  {
    assertNotNull(statement);
    this.statement = statement;
    statement.setParent(this);
  }
  
  public Label getFirstLabel() {
    return (Label)labels.get(0);
  }
  

  public boolean hasSideEffects()
  {
    return true;
  }
  
  public String toSource(int depth)
  {
    StringBuilder sb = new StringBuilder();
    for (Label label : labels) {
      sb.append(label.toSource(depth));
    }
    sb.append(statement.toSource(depth + 1));
    return sb.toString();
  }
  




  public void visit(NodeVisitor v)
  {
    if (v.visit(this)) {
      for (AstNode label : labels) {
        label.visit(v);
      }
      statement.visit(v);
    }
  }
}
