package net.sourceforge.htmlunit.corejs.javascript.ast;

import java.util.SortedSet;
import java.util.TreeSet;
import net.sourceforge.htmlunit.corejs.javascript.Node;



















public class AstRoot
  extends ScriptNode
{
  private SortedSet<Comment> comments;
  private boolean inStrictMode;
  
  public AstRoot()
  {
    type = 136;
  }
  


  public AstRoot(int pos)
  {
    super(pos);type = 136;
  }
  




  public SortedSet<Comment> getComments()
  {
    return comments;
  }
  






  public void setComments(SortedSet<Comment> comments)
  {
    if (comments == null) {
      this.comments = null;
    } else {
      if (this.comments != null)
        this.comments.clear();
      for (Comment c : comments) {
        addComment(c);
      }
    }
  }
  






  public void addComment(Comment comment)
  {
    assertNotNull(comment);
    if (comments == null) {
      comments = new TreeSet(new AstNode.PositionComparator());
    }
    comments.add(comment);
    comment.setParent(this);
  }
  
  public void setInStrictMode(boolean inStrictMode) {
    this.inStrictMode = inStrictMode;
  }
  
  public boolean isInStrictMode() {
    return inStrictMode;
  }
  








  public void visitComments(NodeVisitor visitor)
  {
    if (comments != null) {
      for (Comment c : comments) {
        visitor.visit(c);
      }
    }
  }
  







  public void visitAll(NodeVisitor visitor)
  {
    visit(visitor);
    visitComments(visitor);
  }
  
  public String toSource(int depth)
  {
    StringBuilder sb = new StringBuilder();
    for (Node node : this) {
      sb.append(((AstNode)node).toSource(depth));
    }
    return sb.toString();
  }
  



  public String debugPrint()
  {
    AstNode.DebugPrintVisitor dpv = new AstNode.DebugPrintVisitor(new StringBuilder(1000));
    visitAll(dpv);
    return dpv.toString();
  }
  






  public void checkParentLinks()
  {
    visit(new NodeVisitor() {
      public boolean visit(AstNode node) {
        int type = node.getType();
        if (type == 136)
          return true;
        if (node.getParent() == null)
        {
          throw new IllegalStateException("No parent for node: " + node + "\n" + node.toSource(0)); }
        return true;
      }
    });
  }
}
