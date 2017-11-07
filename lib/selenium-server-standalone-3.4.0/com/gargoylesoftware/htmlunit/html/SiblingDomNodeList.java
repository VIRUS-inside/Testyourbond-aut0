package com.gargoylesoftware.htmlunit.html;

import java.util.AbstractSequentialList;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import org.w3c.dom.Node;


















class SiblingDomNodeList
  extends AbstractSequentialList<DomNode>
  implements DomNodeList<DomNode>
{
  private DomNode parent_;
  
  SiblingDomNodeList(DomNode parent)
  {
    parent_ = parent;
  }
  



  public int getLength()
  {
    int length = 0;
    for (DomNode node = parent_.getFirstChild(); node != null; node = node.getNextSibling()) {
      length++;
    }
    return length;
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
    int i = 0;
    for (DomNode node = parent_.getFirstChild(); node != null; node = node.getNextSibling()) {
      if (i == index) {
        return node;
      }
      i++;
    }
    return null;
  }
  



  public ListIterator<DomNode> listIterator(int index)
  {
    return new SiblingListIterator(index);
  }
  



  public String toString()
  {
    return "SiblingDomNodeList[" + parent_ + "]";
  }
  
  private class SiblingListIterator implements ListIterator<DomNode> {
    private DomNode prev_;
    private DomNode next_;
    private int nextIndex_;
    
    SiblingListIterator(int index) {
      next_ = parent_.getFirstChild();
      nextIndex_ = 0;
      for (int i = 0; i < index; i++) {
        next();
      }
    }
    



    public boolean hasNext()
    {
      return next_ != null;
    }
    



    public DomNode next()
    {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      prev_ = next_;
      next_ = next_.getNextSibling();
      nextIndex_ += 1;
      return prev_;
    }
    



    public boolean hasPrevious()
    {
      return prev_ != null;
    }
    
    public DomNode previous()
    {
      if (!hasPrevious()) {
        throw new NoSuchElementException();
      }
      next_ = prev_;
      prev_ = prev_.getPreviousSibling();
      nextIndex_ -= 1;
      return next_;
    }
    
    public int nextIndex()
    {
      return nextIndex_;
    }
    
    public int previousIndex()
    {
      return nextIndex_ - 1;
    }
    
    public void add(DomNode e)
    {
      throw new UnsupportedOperationException();
    }
    
    public void remove()
    {
      throw new UnsupportedOperationException();
    }
    
    public void set(DomNode e)
    {
      throw new UnsupportedOperationException();
    }
  }
}
