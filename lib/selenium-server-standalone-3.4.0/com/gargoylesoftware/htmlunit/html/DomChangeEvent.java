package com.gargoylesoftware.htmlunit.html;

import java.util.EventObject;

























public class DomChangeEvent
  extends EventObject
{
  private final DomNode changedNode_;
  
  public DomChangeEvent(DomNode parentNode, DomNode changedNode)
  {
    super(parentNode);
    changedNode_ = changedNode;
  }
  



  public DomNode getParentNode()
  {
    return (DomNode)getSource();
  }
  



  public DomNode getChangedNode()
  {
    return changedNode_;
  }
}
