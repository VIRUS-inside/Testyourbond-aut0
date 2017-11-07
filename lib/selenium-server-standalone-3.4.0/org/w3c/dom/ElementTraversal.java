package org.w3c.dom;

public abstract interface ElementTraversal
{
  public abstract Element getFirstElementChild();
  
  public abstract Element getLastElementChild();
  
  public abstract Element getPreviousElementSibling();
  
  public abstract Element getNextElementSibling();
  
  public abstract int getChildElementCount();
}
