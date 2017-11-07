package net.sourceforge.htmlunit.corejs.javascript.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;






























public class ArrayLiteral
  extends AstNode
  implements DestructuringForm
{
  private static final List<AstNode> NO_ELEMS = Collections.unmodifiableList(new ArrayList());
  private List<AstNode> elements;
  private int destructuringLength;
  private int skipCount;
  private boolean isDestructuring;
  
  public ArrayLiteral()
  {
    type = 65;
  }
  


  public ArrayLiteral(int pos)
  {
    super(pos);type = 65;
  }
  
  public ArrayLiteral(int pos, int len) {
    super(pos, len);type = 65;
  }
  






  public List<AstNode> getElements()
  {
    return elements != null ? elements : NO_ELEMS;
  }
  





  public void setElements(List<AstNode> elements)
  {
    if (elements == null) {
      this.elements = null;
    } else {
      if (this.elements != null)
        this.elements.clear();
      for (AstNode e : elements) {
        addElement(e);
      }
    }
  }
  







  public void addElement(AstNode element)
  {
    assertNotNull(element);
    if (elements == null)
      elements = new ArrayList();
    elements.add(element);
    element.setParent(this);
  }
  



  public int getSize()
  {
    return elements == null ? 0 : elements.size();
  }
  








  public AstNode getElement(int index)
  {
    if (elements == null)
      throw new IndexOutOfBoundsException("no elements");
    return (AstNode)elements.get(index);
  }
  


  public int getDestructuringLength()
  {
    return destructuringLength;
  }
  






  public void setDestructuringLength(int destructuringLength)
  {
    this.destructuringLength = destructuringLength;
  }
  




  public int getSkipCount()
  {
    return skipCount;
  }
  





  public void setSkipCount(int count)
  {
    skipCount = count;
  }
  




  public void setIsDestructuring(boolean destructuring)
  {
    isDestructuring = destructuring;
  }
  




  public boolean isDestructuring()
  {
    return isDestructuring;
  }
  
  public String toSource(int depth)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(makeIndent(depth));
    sb.append("[");
    if (elements != null) {
      printList(elements, sb);
    }
    sb.append("]");
    return sb.toString();
  }
  





  public void visit(NodeVisitor v)
  {
    if (v.visit(this)) {
      for (AstNode e : getElements()) {
        e.visit(v);
      }
    }
  }
}
