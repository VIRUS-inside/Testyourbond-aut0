package com.gargoylesoftware.htmlunit.html;

import java.util.AbstractList;
import java.util.List;
import org.w3c.dom.Node;


















class StaticDomNodeList
  extends AbstractList<DomNode>
  implements DomNodeList<DomNode>
{
  private List<DomNode> elements_;
  
  StaticDomNodeList(List<DomNode> elements)
  {
    elements_ = elements;
  }
  



  public int getLength()
  {
    return elements_.size();
  }
  



  public int size()
  {
    return getLength();
  }
  



  public Node item(int index)
  {
    return get(index);
  }
  



  public DomNode get(int index)
  {
    return (DomNode)elements_.get(index);
  }
}
