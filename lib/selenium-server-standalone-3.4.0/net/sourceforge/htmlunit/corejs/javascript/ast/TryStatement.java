package net.sourceforge.htmlunit.corejs.javascript.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
























public class TryStatement
  extends AstNode
{
  private static final List<CatchClause> NO_CATCHES = Collections.unmodifiableList(new ArrayList());
  
  private AstNode tryBlock;
  private List<CatchClause> catchClauses;
  private AstNode finallyBlock;
  private int finallyPosition = -1;
  
  public TryStatement() {
    type = 81;
  }
  


  public TryStatement(int pos)
  {
    super(pos);type = 81;
  }
  
  public TryStatement(int pos, int len) {
    super(pos, len);type = 81;
  }
  
  public AstNode getTryBlock() {
    return tryBlock;
  }
  





  public void setTryBlock(AstNode tryBlock)
  {
    assertNotNull(tryBlock);
    this.tryBlock = tryBlock;
    tryBlock.setParent(this);
  }
  



  public List<CatchClause> getCatchClauses()
  {
    return catchClauses != null ? catchClauses : NO_CATCHES;
  }
  




  public void setCatchClauses(List<CatchClause> catchClauses)
  {
    if (catchClauses == null) {
      this.catchClauses = null;
    } else {
      if (this.catchClauses != null)
        this.catchClauses.clear();
      for (CatchClause cc : catchClauses) {
        addCatchClause(cc);
      }
    }
  }
  






  public void addCatchClause(CatchClause clause)
  {
    assertNotNull(clause);
    if (catchClauses == null) {
      catchClauses = new ArrayList();
    }
    catchClauses.add(clause);
    clause.setParent(this);
  }
  


  public AstNode getFinallyBlock()
  {
    return finallyBlock;
  }
  



  public void setFinallyBlock(AstNode finallyBlock)
  {
    this.finallyBlock = finallyBlock;
    if (finallyBlock != null) {
      finallyBlock.setParent(this);
    }
  }
  

  public int getFinallyPosition()
  {
    return finallyPosition;
  }
  


  public void setFinallyPosition(int finallyPosition)
  {
    this.finallyPosition = finallyPosition;
  }
  
  public String toSource(int depth)
  {
    StringBuilder sb = new StringBuilder(250);
    sb.append(makeIndent(depth));
    sb.append("try ");
    sb.append(tryBlock.toSource(depth).trim());
    for (CatchClause cc : getCatchClauses()) {
      sb.append(cc.toSource(depth));
    }
    if (finallyBlock != null) {
      sb.append(" finally ");
      sb.append(finallyBlock.toSource(depth));
    }
    return sb.toString();
  }
  




  public void visit(NodeVisitor v)
  {
    if (v.visit(this)) {
      tryBlock.visit(v);
      for (CatchClause cc : getCatchClauses()) {
        cc.visit(v);
      }
      if (finallyBlock != null) {
        finallyBlock.visit(v);
      }
    }
  }
}
