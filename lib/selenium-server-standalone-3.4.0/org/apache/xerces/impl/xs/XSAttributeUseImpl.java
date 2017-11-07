package org.apache.xerces.impl.xs;

import org.apache.xerces.impl.dv.ValidatedInfo;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.xs.ShortList;
import org.apache.xerces.xs.XSAttributeDeclaration;
import org.apache.xerces.xs.XSAttributeUse;
import org.apache.xerces.xs.XSNamespaceItem;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSValue;

public class XSAttributeUseImpl
  implements XSAttributeUse
{
  public XSAttributeDecl fAttrDecl = null;
  public short fUse = 0;
  public short fConstraintType = 0;
  public ValidatedInfo fDefault = null;
  public XSObjectList fAnnotations = null;
  
  public XSAttributeUseImpl() {}
  
  public void reset()
  {
    fDefault = null;
    fAttrDecl = null;
    fUse = 0;
    fConstraintType = 0;
    fAnnotations = null;
  }
  
  public short getType()
  {
    return 4;
  }
  
  public String getName()
  {
    return null;
  }
  
  public String getNamespace()
  {
    return null;
  }
  
  public boolean getRequired()
  {
    return fUse == 1;
  }
  
  public XSAttributeDeclaration getAttrDeclaration()
  {
    return fAttrDecl;
  }
  
  public short getConstraintType()
  {
    return fConstraintType;
  }
  
  public String getConstraintValue()
  {
    return getConstraintType() == 0 ? null : fDefault.stringValue();
  }
  
  public XSNamespaceItem getNamespaceItem()
  {
    return null;
  }
  
  public Object getActualVC()
  {
    return getConstraintType() == 0 ? null : fDefault.actualValue;
  }
  
  public short getActualVCType()
  {
    return getConstraintType() == 0 ? 45 : fDefault.actualValueType;
  }
  
  public ShortList getItemValueTypes()
  {
    return getConstraintType() == 0 ? null : fDefault.itemValueTypes;
  }
  
  public XSValue getValueConstraintValue()
  {
    return fDefault;
  }
  
  public XSObjectList getAnnotations()
  {
    return fAnnotations != null ? fAnnotations : XSObjectListImpl.EMPTY_LIST;
  }
}
