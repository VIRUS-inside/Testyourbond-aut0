package net.sourceforge.htmlunit.corejs.javascript.ast;







public class Name
  extends AstNode
{
  private String identifier;
  




  private Scope scope;
  





  public Name()
  {
    type = 39;
  }
  


  public Name(int pos)
  {
    super(pos);type = 39;
  }
  
  public Name(int pos, int len) {
    super(pos, len);type = 39;
  }
  









  public Name(int pos, int len, String name)
  {
    super(pos, len);type = 39;
    setIdentifier(name);
  }
  
  public Name(int pos, String name) {
    super(pos);type = 39;
    setIdentifier(name);
    setLength(name.length());
  }
  


  public String getIdentifier()
  {
    return identifier;
  }
  





  public void setIdentifier(String identifier)
  {
    assertNotNull(identifier);
    this.identifier = identifier;
    setLength(identifier.length());
  }
  










  public void setScope(Scope s)
  {
    scope = s;
  }
  






  public Scope getScope()
  {
    return scope;
  }
  





  public Scope getDefiningScope()
  {
    Scope enclosing = getEnclosingScope();
    String name = getIdentifier();
    return enclosing == null ? null : enclosing.getDefiningScope(name);
  }
  













  public boolean isLocalName()
  {
    Scope scope = getDefiningScope();
    return (scope != null) && (scope.getParentScope() != null);
  }
  





  public int length()
  {
    return identifier == null ? 0 : identifier.length();
  }
  
  public String toSource(int depth)
  {
    return makeIndent(depth) + (identifier == null ? "<null>" : identifier);
  }
  



  public void visit(NodeVisitor v)
  {
    v.visit(this);
  }
}
