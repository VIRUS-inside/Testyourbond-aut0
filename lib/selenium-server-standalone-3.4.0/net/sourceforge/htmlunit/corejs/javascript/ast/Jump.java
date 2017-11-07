package net.sourceforge.htmlunit.corejs.javascript.ast;

import net.sourceforge.htmlunit.corejs.javascript.Node;












public class Jump
  extends AstNode
{
  public Node target;
  private Node target2;
  private Jump jumpNode;
  
  public Jump()
  {
    type = -1;
  }
  
  public Jump(int nodeType) {
    type = nodeType;
  }
  
  public Jump(int type, int lineno) {
    this(type);
    setLineno(lineno);
  }
  
  public Jump(int type, Node child) {
    this(type);
    addChildToBack(child);
  }
  
  public Jump(int type, Node child, int lineno) {
    this(type, child);
    setLineno(lineno);
  }
  
  public Jump getJumpStatement() {
    if ((type != 120) && (type != 121))
      codeBug();
    return jumpNode;
  }
  
  public void setJumpStatement(Jump jumpStatement) {
    if ((type != 120) && (type != 121))
      codeBug();
    if (jumpStatement == null)
      codeBug();
    if (jumpNode != null)
      codeBug();
    jumpNode = jumpStatement;
  }
  
  public Node getDefault() {
    if (type != 114)
      codeBug();
    return target2;
  }
  
  public void setDefault(Node defaultTarget) {
    if (type != 114)
      codeBug();
    if (defaultTarget.getType() != 131)
      codeBug();
    if (target2 != null)
      codeBug();
    target2 = defaultTarget;
  }
  
  public Node getFinally() {
    if (type != 81)
      codeBug();
    return target2;
  }
  
  public void setFinally(Node finallyTarget) {
    if (type != 81)
      codeBug();
    if (finallyTarget.getType() != 131)
      codeBug();
    if (target2 != null)
      codeBug();
    target2 = finallyTarget;
  }
  
  public Jump getLoop() {
    if (type != 130)
      codeBug();
    return jumpNode;
  }
  
  public void setLoop(Jump loop) {
    if (type != 130)
      codeBug();
    if (loop == null)
      codeBug();
    if (jumpNode != null)
      codeBug();
    jumpNode = loop;
  }
  
  public Node getContinue() {
    if (type != 132)
      codeBug();
    return target2;
  }
  
  public void setContinue(Node continueTarget) {
    if (type != 132)
      codeBug();
    if (continueTarget.getType() != 131)
      codeBug();
    if (target2 != null)
      codeBug();
    target2 = continueTarget;
  }
  






  public void visit(NodeVisitor visitor)
  {
    throw new UnsupportedOperationException(toString());
  }
  
  public String toSource(int depth)
  {
    throw new UnsupportedOperationException(toString());
  }
}
