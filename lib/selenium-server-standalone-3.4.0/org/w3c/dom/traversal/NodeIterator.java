package org.w3c.dom.traversal;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public abstract interface NodeIterator
{
  public abstract Node getRoot();
  
  public abstract int getWhatToShow();
  
  public abstract NodeFilter getFilter();
  
  public abstract boolean getExpandEntityReferences();
  
  public abstract Node nextNode()
    throws DOMException;
  
  public abstract Node previousNode()
    throws DOMException;
  
  public abstract void detach();
}
