package net.sourceforge.htmlunit.corejs.javascript.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.sourceforge.htmlunit.corejs.javascript.Node;














public class Scope
  extends Jump
{
  protected Map<String, Symbol> symbolTable;
  protected Scope parentScope;
  protected ScriptNode top;
  private List<Scope> childScopes;
  
  public Scope() { type = 129; } public Scope(int pos) { type = 129;
    





    position = pos;
  }
  
  public Scope(int pos, int len) {
    this(pos);
    length = len;
  }
  
  public Scope getParentScope() {
    return parentScope;
  }
  


  public void setParentScope(Scope parentScope)
  {
    this.parentScope = parentScope;
    top = (parentScope == null ? (ScriptNode)this : top);
  }
  


  public void clearParentScope()
  {
    parentScope = null;
  }
  




  public List<Scope> getChildScopes()
  {
    return childScopes;
  }
  






  public void addChildScope(Scope child)
  {
    if (childScopes == null) {
      childScopes = new ArrayList();
    }
    childScopes.add(child);
    child.setParentScope(this);
  }
  







  public void replaceWith(Scope newScope)
  {
    if (childScopes != null) {
      for (Scope kid : childScopes) {
        newScope.addChildScope(kid);
      }
      childScopes.clear();
      childScopes = null;
    }
    if ((symbolTable != null) && (!symbolTable.isEmpty())) {
      joinScopes(this, newScope);
    }
  }
  


  public ScriptNode getTop()
  {
    return top;
  }
  


  public void setTop(ScriptNode top)
  {
    this.top = top;
  }
  




  public static Scope splitScope(Scope scope)
  {
    Scope result = new Scope(scope.getType());
    symbolTable = symbolTable;
    symbolTable = null;
    parent = parent;
    result.setParentScope(scope.getParentScope());
    result.setParentScope(result);
    parent = result;
    top = top;
    return result;
  }
  


  public static void joinScopes(Scope source, Scope dest)
  {
    Map<String, Symbol> src = source.ensureSymbolTable();
    Map<String, Symbol> dst = dest.ensureSymbolTable();
    if (!Collections.disjoint(src.keySet(), dst.keySet())) {
      codeBug();
    }
    for (Map.Entry<String, Symbol> entry : src.entrySet()) {
      Symbol sym = (Symbol)entry.getValue();
      sym.setContainingTable(dest);
      dst.put(entry.getKey(), sym);
    }
  }
  







  public Scope getDefiningScope(String name)
  {
    for (Scope s = this; s != null; s = parentScope) {
      Map<String, Symbol> symbolTable = s.getSymbolTable();
      if ((symbolTable != null) && (symbolTable.containsKey(name))) {
        return s;
      }
    }
    return null;
  }
  






  public Symbol getSymbol(String name)
  {
    return symbolTable == null ? null : (Symbol)symbolTable.get(name);
  }
  


  public void putSymbol(Symbol symbol)
  {
    if (symbol.getName() == null)
      throw new IllegalArgumentException("null symbol name");
    ensureSymbolTable();
    symbolTable.put(symbol.getName(), symbol);
    symbol.setContainingTable(this);
    top.addSymbol(symbol);
  }
  




  public Map<String, Symbol> getSymbolTable()
  {
    return symbolTable;
  }
  


  public void setSymbolTable(Map<String, Symbol> table)
  {
    symbolTable = table;
  }
  
  private Map<String, Symbol> ensureSymbolTable() {
    if (symbolTable == null) {
      symbolTable = new LinkedHashMap(5);
    }
    return symbolTable;
  }
  








  public List<AstNode> getStatements()
  {
    List<AstNode> stmts = new ArrayList();
    Node n = getFirstChild();
    while (n != null) {
      stmts.add((AstNode)n);
      n = n.getNext();
    }
    return stmts;
  }
  
  public String toSource(int depth)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(makeIndent(depth));
    sb.append("{\n");
    for (Node kid : this) {
      sb.append(((AstNode)kid).toSource(depth + 1));
    }
    sb.append(makeIndent(depth));
    sb.append("}\n");
    return sb.toString();
  }
  
  public void visit(NodeVisitor v)
  {
    if (v.visit(this)) {
      for (Node kid : this) {
        ((AstNode)kid).visit(v);
      }
    }
  }
}
