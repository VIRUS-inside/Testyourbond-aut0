package org.apache.xerces.impl.xs.models;

import org.apache.xerces.impl.dtd.models.CMNode;
import org.apache.xerces.impl.dtd.models.CMStateSet;

public class XSCMBinOp
  extends CMNode
{
  private CMNode fLeftChild;
  private CMNode fRightChild;
  
  public XSCMBinOp(int paramInt, CMNode paramCMNode1, CMNode paramCMNode2)
  {
    super(paramInt);
    if ((type() != 101) && (type() != 102)) {
      throw new RuntimeException("ImplementationMessages.VAL_BST");
    }
    fLeftChild = paramCMNode1;
    fRightChild = paramCMNode2;
  }
  
  final CMNode getLeft()
  {
    return fLeftChild;
  }
  
  final CMNode getRight()
  {
    return fRightChild;
  }
  
  public boolean isNullable()
  {
    if (type() == 101) {
      return (fLeftChild.isNullable()) || (fRightChild.isNullable());
    }
    if (type() == 102) {
      return (fLeftChild.isNullable()) && (fRightChild.isNullable());
    }
    throw new RuntimeException("ImplementationMessages.VAL_BST");
  }
  
  protected void calcFirstPos(CMStateSet paramCMStateSet)
  {
    if (type() == 101)
    {
      paramCMStateSet.setTo(fLeftChild.firstPos());
      paramCMStateSet.union(fRightChild.firstPos());
    }
    else if (type() == 102)
    {
      paramCMStateSet.setTo(fLeftChild.firstPos());
      if (fLeftChild.isNullable()) {
        paramCMStateSet.union(fRightChild.firstPos());
      }
    }
    else
    {
      throw new RuntimeException("ImplementationMessages.VAL_BST");
    }
  }
  
  protected void calcLastPos(CMStateSet paramCMStateSet)
  {
    if (type() == 101)
    {
      paramCMStateSet.setTo(fLeftChild.lastPos());
      paramCMStateSet.union(fRightChild.lastPos());
    }
    else if (type() == 102)
    {
      paramCMStateSet.setTo(fRightChild.lastPos());
      if (fRightChild.isNullable()) {
        paramCMStateSet.union(fLeftChild.lastPos());
      }
    }
    else
    {
      throw new RuntimeException("ImplementationMessages.VAL_BST");
    }
  }
}
