package net.sourceforge.htmlunit.corejs.javascript.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;













public class FunctionCall
  extends AstNode
{
  protected static final List<AstNode> NO_ARGS = Collections.unmodifiableList(new ArrayList());
  
  protected AstNode target;
  protected List<AstNode> arguments;
  protected int lp = -1;
  protected int rp = -1;
  
  public FunctionCall() {
    type = 38;
  }
  


  public FunctionCall(int pos)
  {
    super(pos);type = 38;
  }
  
  public FunctionCall(int pos, int len) {
    super(pos, len);type = 38;
  }
  


  public AstNode getTarget()
  {
    return target;
  }
  








  public void setTarget(AstNode target)
  {
    assertNotNull(target);
    this.target = target;
    target.setParent(this);
  }
  





  public List<AstNode> getArguments()
  {
    return arguments != null ? arguments : NO_ARGS;
  }
  






  public void setArguments(List<AstNode> arguments)
  {
    if (arguments == null) {
      this.arguments = null;
    } else {
      if (this.arguments != null)
        this.arguments.clear();
      for (AstNode arg : arguments) {
        addArgument(arg);
      }
    }
  }
  







  public void addArgument(AstNode arg)
  {
    assertNotNull(arg);
    if (arguments == null) {
      arguments = new ArrayList();
    }
    arguments.add(arg);
    arg.setParent(this);
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
    StringBuilder sb = new StringBuilder();
    sb.append(makeIndent(depth));
    sb.append(target.toSource(0));
    sb.append("(");
    if (arguments != null) {
      printList(arguments, sb);
    }
    sb.append(")");
    return sb.toString();
  }
  



  public void visit(NodeVisitor v)
  {
    if (v.visit(this)) {
      target.visit(v);
      for (AstNode arg : getArguments()) {
        arg.visit(v);
      }
    }
  }
}
