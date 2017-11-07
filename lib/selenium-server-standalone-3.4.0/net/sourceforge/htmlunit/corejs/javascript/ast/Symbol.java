package net.sourceforge.htmlunit.corejs.javascript.ast;

import net.sourceforge.htmlunit.corejs.javascript.Node;
import net.sourceforge.htmlunit.corejs.javascript.Token;












public class Symbol
{
  private int declType;
  private int index = -1;
  

  private String name;
  

  private Node node;
  
  private Scope containingTable;
  

  public Symbol() {}
  

  public Symbol(int declType, String name)
  {
    setName(name);
    setDeclType(declType);
  }
  


  public int getDeclType()
  {
    return declType;
  }
  


  public void setDeclType(int declType)
  {
    if ((declType != 109) && (declType != 87) && (declType != 122) && (declType != 153) && (declType != 154))
    {

      throw new IllegalArgumentException("Invalid declType: " + declType); }
    this.declType = declType;
  }
  


  public String getName()
  {
    return name;
  }
  


  public void setName(String name)
  {
    this.name = name;
  }
  


  public Node getNode()
  {
    return node;
  }
  


  public int getIndex()
  {
    return index;
  }
  


  public void setIndex(int index)
  {
    this.index = index;
  }
  


  public void setNode(Node node)
  {
    this.node = node;
  }
  


  public Scope getContainingTable()
  {
    return containingTable;
  }
  


  public void setContainingTable(Scope containingTable)
  {
    this.containingTable = containingTable;
  }
  
  public String getDeclTypeName() {
    return Token.typeToName(declType);
  }
  
  public String toString()
  {
    StringBuilder result = new StringBuilder();
    result.append("Symbol (");
    result.append(getDeclTypeName());
    result.append(") name=");
    result.append(name);
    if (node != null) {
      result.append(" line=");
      result.append(node.getLineno());
    }
    return result.toString();
  }
}
