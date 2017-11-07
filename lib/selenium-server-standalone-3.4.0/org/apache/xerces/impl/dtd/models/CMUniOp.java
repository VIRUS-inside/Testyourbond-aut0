package org.apache.xerces.impl.dtd.models;

public class CMUniOp
  extends CMNode
{
  private final CMNode fChild;
  
  public CMUniOp(int paramInt, CMNode paramCMNode)
  {
    super(paramInt);
    if ((type() != 1) && (type() != 2) && (type() != 3)) {
      throw new RuntimeException("ImplementationMessages.VAL_UST");
    }
    fChild = paramCMNode;
  }
  
  final CMNode getChild()
  {
    return fChild;
  }
  
  public boolean isNullable()
  {
    if (type() == 3) {
      return fChild.isNullable();
    }
    return true;
  }
  
  protected void calcFirstPos(CMStateSet paramCMStateSet)
  {
    paramCMStateSet.setTo(fChild.firstPos());
  }
  
  protected void calcLastPos(CMStateSet paramCMStateSet)
  {
    paramCMStateSet.setTo(fChild.lastPos());
  }
}
