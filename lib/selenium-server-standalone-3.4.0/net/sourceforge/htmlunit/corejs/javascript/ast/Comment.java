package net.sourceforge.htmlunit.corejs.javascript.ast;

import net.sourceforge.htmlunit.corejs.javascript.Token.CommentType;
























































public class Comment
  extends AstNode
{
  private String value;
  private Token.CommentType commentType;
  
  public Comment(int pos, int len, Token.CommentType type, String value)
  {
    super(pos, len);this.type = 161;
    commentType = type;
    this.value = value;
  }
  


  public Token.CommentType getCommentType()
  {
    return commentType;
  }
  






  public void setCommentType(Token.CommentType type)
  {
    commentType = type;
  }
  


  public String getValue()
  {
    return value;
  }
  
  public String toSource(int depth)
  {
    StringBuilder sb = new StringBuilder(getLength() + 10);
    sb.append(makeIndent(depth));
    sb.append(value);
    return sb.toString();
  }
  




  public void visit(NodeVisitor v)
  {
    v.visit(this);
  }
}
