package org.apache.xerces.impl.xs;

import org.apache.xerces.impl.dv.ValidatedInfo;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.xs.XSAnnotation;
import org.apache.xerces.xs.XSAttributeGroupDefinition;
import org.apache.xerces.xs.XSAttributeUse;
import org.apache.xerces.xs.XSNamespaceItem;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSWildcard;

public class XSAttributeGroupDecl
  implements XSAttributeGroupDefinition
{
  public String fName = null;
  public String fTargetNamespace = null;
  int fAttrUseNum = 0;
  private static final int INITIAL_SIZE = 5;
  XSAttributeUseImpl[] fAttributeUses = new XSAttributeUseImpl[5];
  public XSWildcardDecl fAttributeWC = null;
  public String fIDAttrName = null;
  public XSObjectList fAnnotations;
  protected XSObjectListImpl fAttrUses = null;
  private XSNamespaceItem fNamespaceItem = null;
  
  public XSAttributeGroupDecl() {}
  
  public String addAttributeUse(XSAttributeUseImpl paramXSAttributeUseImpl)
  {
    if ((fUse != 2) && (fAttrDecl.fType.isIDType())) {
      if (fIDAttrName == null) {
        fIDAttrName = fAttrDecl.fName;
      } else {
        return fIDAttrName;
      }
    }
    if (fAttrUseNum == fAttributeUses.length) {
      fAttributeUses = resize(fAttributeUses, fAttrUseNum * 2);
    }
    fAttributeUses[(fAttrUseNum++)] = paramXSAttributeUseImpl;
    return null;
  }
  
  public void replaceAttributeUse(XSAttributeUse paramXSAttributeUse, XSAttributeUseImpl paramXSAttributeUseImpl)
  {
    for (int i = 0; i < fAttrUseNum; i++) {
      if (fAttributeUses[i] == paramXSAttributeUse) {
        fAttributeUses[i] = paramXSAttributeUseImpl;
      }
    }
  }
  
  public XSAttributeUse getAttributeUse(String paramString1, String paramString2)
  {
    for (int i = 0; i < fAttrUseNum; i++) {
      if ((fAttributeUses[i].fAttrDecl.fTargetNamespace == paramString1) && (fAttributeUses[i].fAttrDecl.fName == paramString2)) {
        return fAttributeUses[i];
      }
    }
    return null;
  }
  
  public XSAttributeUse getAttributeUseNoProhibited(String paramString1, String paramString2)
  {
    for (int i = 0; i < fAttrUseNum; i++) {
      if ((fAttributeUses[i].fAttrDecl.fTargetNamespace == paramString1) && (fAttributeUses[i].fAttrDecl.fName == paramString2) && (fAttributeUses[i].fUse != 2)) {
        return fAttributeUses[i];
      }
    }
    return null;
  }
  
  public void removeProhibitedAttrs()
  {
    if (fAttrUseNum == 0) {
      return;
    }
    int i = 0;
    XSAttributeUseImpl[] arrayOfXSAttributeUseImpl = new XSAttributeUseImpl[fAttrUseNum];
    for (int j = 0; j < fAttrUseNum; j++) {
      if (fAttributeUses[j].fUse != 2) {
        arrayOfXSAttributeUseImpl[(i++)] = fAttributeUses[j];
      }
    }
    fAttributeUses = arrayOfXSAttributeUseImpl;
    fAttrUseNum = i;
  }
  
  public Object[] validRestrictionOf(String paramString, XSAttributeGroupDecl paramXSAttributeGroupDecl)
  {
    Object[] arrayOfObject = null;
    XSAttributeUseImpl localXSAttributeUseImpl1 = null;
    XSAttributeDecl localXSAttributeDecl1 = null;
    XSAttributeUseImpl localXSAttributeUseImpl2 = null;
    XSAttributeDecl localXSAttributeDecl2 = null;
    for (int i = 0; i < fAttrUseNum; i++)
    {
      localXSAttributeUseImpl1 = fAttributeUses[i];
      localXSAttributeDecl1 = fAttrDecl;
      localXSAttributeUseImpl2 = (XSAttributeUseImpl)paramXSAttributeGroupDecl.getAttributeUse(fTargetNamespace, fName);
      if (localXSAttributeUseImpl2 != null)
      {
        if ((localXSAttributeUseImpl2.getRequired()) && (!localXSAttributeUseImpl1.getRequired()))
        {
          arrayOfObject = new Object[] { paramString, fName, fUse == 0 ? "optional" : "prohibited", "derivation-ok-restriction.2.1.1" };
          return arrayOfObject;
        }
        if (fUse != 2)
        {
          localXSAttributeDecl2 = fAttrDecl;
          if (!XSConstraints.checkSimpleDerivationOk(fType, fType, fType.getFinal()))
          {
            arrayOfObject = new Object[] { paramString, fName, fType.getName(), fType.getName(), "derivation-ok-restriction.2.1.2" };
            return arrayOfObject;
          }
          j = fConstraintType != 0 ? fConstraintType : localXSAttributeDecl2.getConstraintType();
          int k = fConstraintType != 0 ? fConstraintType : localXSAttributeDecl1.getConstraintType();
          if (j == 2)
          {
            if (k != 2)
            {
              arrayOfObject = new Object[] { paramString, fName, "derivation-ok-restriction.2.1.3.a" };
              return arrayOfObject;
            }
            ValidatedInfo localValidatedInfo1 = fDefault != null ? fDefault : fDefault;
            ValidatedInfo localValidatedInfo2 = fDefault != null ? fDefault : fDefault;
            if (!actualValue.equals(actualValue))
            {
              arrayOfObject = new Object[] { paramString, fName, localValidatedInfo2.stringValue(), localValidatedInfo1.stringValue(), "derivation-ok-restriction.2.1.3.b" };
              return arrayOfObject;
            }
          }
        }
      }
      else
      {
        if (fAttributeWC == null)
        {
          arrayOfObject = new Object[] { paramString, fName, "derivation-ok-restriction.2.2.a" };
          return arrayOfObject;
        }
        if (!fAttributeWC.allowNamespace(fTargetNamespace))
        {
          arrayOfObject = new Object[] { paramString, fName, fTargetNamespace == null ? "" : fTargetNamespace, "derivation-ok-restriction.2.2.b" };
          return arrayOfObject;
        }
      }
    }
    for (int j = 0; j < fAttrUseNum; j++)
    {
      localXSAttributeUseImpl2 = fAttributeUses[j];
      if (fUse == 1)
      {
        localXSAttributeDecl2 = fAttrDecl;
        if (getAttributeUse(fTargetNamespace, fName) == null)
        {
          arrayOfObject = new Object[] { paramString, fAttrDecl.fName, "derivation-ok-restriction.3" };
          return arrayOfObject;
        }
      }
    }
    if (fAttributeWC != null)
    {
      if (fAttributeWC == null)
      {
        arrayOfObject = new Object[] { paramString, "derivation-ok-restriction.4.1" };
        return arrayOfObject;
      }
      if (!fAttributeWC.isSubsetOf(fAttributeWC))
      {
        arrayOfObject = new Object[] { paramString, "derivation-ok-restriction.4.2" };
        return arrayOfObject;
      }
      if (fAttributeWC.weakerProcessContents(fAttributeWC))
      {
        arrayOfObject = new Object[] { paramString, fAttributeWC.getProcessContentsAsString(), fAttributeWC.getProcessContentsAsString(), "derivation-ok-restriction.4.3" };
        return arrayOfObject;
      }
    }
    return null;
  }
  
  static final XSAttributeUseImpl[] resize(XSAttributeUseImpl[] paramArrayOfXSAttributeUseImpl, int paramInt)
  {
    XSAttributeUseImpl[] arrayOfXSAttributeUseImpl = new XSAttributeUseImpl[paramInt];
    System.arraycopy(paramArrayOfXSAttributeUseImpl, 0, arrayOfXSAttributeUseImpl, 0, Math.min(paramArrayOfXSAttributeUseImpl.length, paramInt));
    return arrayOfXSAttributeUseImpl;
  }
  
  public void reset()
  {
    fName = null;
    fTargetNamespace = null;
    for (int i = 0; i < fAttrUseNum; i++) {
      fAttributeUses[i] = null;
    }
    fAttrUseNum = 0;
    fAttributeWC = null;
    fAnnotations = null;
    fIDAttrName = null;
  }
  
  public short getType()
  {
    return 5;
  }
  
  public String getName()
  {
    return fName;
  }
  
  public String getNamespace()
  {
    return fTargetNamespace;
  }
  
  public XSObjectList getAttributeUses()
  {
    if (fAttrUses == null) {
      fAttrUses = new XSObjectListImpl(fAttributeUses, fAttrUseNum);
    }
    return fAttrUses;
  }
  
  public XSWildcard getAttributeWildcard()
  {
    return fAttributeWC;
  }
  
  public XSAnnotation getAnnotation()
  {
    return fAnnotations != null ? (XSAnnotation)fAnnotations.item(0) : null;
  }
  
  public XSObjectList getAnnotations()
  {
    return fAnnotations != null ? fAnnotations : XSObjectListImpl.EMPTY_LIST;
  }
  
  public XSNamespaceItem getNamespaceItem()
  {
    return fNamespaceItem;
  }
  
  void setNamespaceItem(XSNamespaceItem paramXSNamespaceItem)
  {
    fNamespaceItem = paramXSNamespaceItem;
  }
}
