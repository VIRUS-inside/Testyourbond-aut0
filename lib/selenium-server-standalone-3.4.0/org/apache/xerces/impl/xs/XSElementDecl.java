package org.apache.xerces.impl.xs;

import org.apache.xerces.impl.dv.ValidatedInfo;
import org.apache.xerces.impl.xs.identity.IdentityConstraint;
import org.apache.xerces.impl.xs.util.XSNamedMapImpl;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xs.ShortList;
import org.apache.xerces.xs.XSAnnotation;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSNamespaceItem;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSTypeDefinition;
import org.apache.xerces.xs.XSValue;

public class XSElementDecl
  implements XSElementDeclaration
{
  public static final short SCOPE_ABSENT = 0;
  public static final short SCOPE_GLOBAL = 1;
  public static final short SCOPE_LOCAL = 2;
  public String fName = null;
  public String fTargetNamespace = null;
  public XSTypeDefinition fType = null;
  public QName fUnresolvedTypeName = null;
  short fMiscFlags = 0;
  public short fScope = 0;
  XSComplexTypeDecl fEnclosingCT = null;
  public short fBlock = 0;
  public short fFinal = 0;
  public XSObjectList fAnnotations = null;
  public ValidatedInfo fDefault = null;
  public XSElementDecl fSubGroup = null;
  static final int INITIAL_SIZE = 2;
  int fIDCPos = 0;
  IdentityConstraint[] fIDConstraints = new IdentityConstraint[2];
  private XSNamespaceItem fNamespaceItem = null;
  private static final short CONSTRAINT_MASK = 3;
  private static final short NILLABLE = 4;
  private static final short ABSTRACT = 8;
  private String fDescription = null;
  
  public XSElementDecl() {}
  
  public void setConstraintType(short paramShort)
  {
    fMiscFlags = ((short)(fMiscFlags ^ fMiscFlags & 0x3));
    fMiscFlags = ((short)(fMiscFlags | paramShort & 0x3));
  }
  
  public void setIsNillable()
  {
    fMiscFlags = ((short)(fMiscFlags | 0x4));
  }
  
  public void setIsAbstract()
  {
    fMiscFlags = ((short)(fMiscFlags | 0x8));
  }
  
  public void setIsGlobal()
  {
    fScope = 1;
  }
  
  public void setIsLocal(XSComplexTypeDecl paramXSComplexTypeDecl)
  {
    fScope = 2;
    fEnclosingCT = paramXSComplexTypeDecl;
  }
  
  public void addIDConstraint(IdentityConstraint paramIdentityConstraint)
  {
    if (fIDCPos == fIDConstraints.length) {
      fIDConstraints = resize(fIDConstraints, fIDCPos * 2);
    }
    fIDConstraints[(fIDCPos++)] = paramIdentityConstraint;
  }
  
  public IdentityConstraint[] getIDConstraints()
  {
    if (fIDCPos == 0) {
      return null;
    }
    if (fIDCPos < fIDConstraints.length) {
      fIDConstraints = resize(fIDConstraints, fIDCPos);
    }
    return fIDConstraints;
  }
  
  static final IdentityConstraint[] resize(IdentityConstraint[] paramArrayOfIdentityConstraint, int paramInt)
  {
    IdentityConstraint[] arrayOfIdentityConstraint = new IdentityConstraint[paramInt];
    System.arraycopy(paramArrayOfIdentityConstraint, 0, arrayOfIdentityConstraint, 0, Math.min(paramArrayOfIdentityConstraint.length, paramInt));
    return arrayOfIdentityConstraint;
  }
  
  public String toString()
  {
    if (fDescription == null) {
      if (fTargetNamespace != null)
      {
        StringBuffer localStringBuffer = new StringBuffer(fTargetNamespace.length() + (fName != null ? fName.length() : 4) + 3);
        localStringBuffer.append('"');
        localStringBuffer.append(fTargetNamespace);
        localStringBuffer.append('"');
        localStringBuffer.append(':');
        localStringBuffer.append(fName);
        fDescription = localStringBuffer.toString();
      }
      else
      {
        fDescription = fName;
      }
    }
    return fDescription;
  }
  
  public int hashCode()
  {
    int i = fName.hashCode();
    if (fTargetNamespace != null) {
      i = (i << 16) + fTargetNamespace.hashCode();
    }
    return i;
  }
  
  public boolean equals(Object paramObject)
  {
    return paramObject == this;
  }
  
  public void reset()
  {
    fScope = 0;
    fName = null;
    fTargetNamespace = null;
    fType = null;
    fUnresolvedTypeName = null;
    fMiscFlags = 0;
    fBlock = 0;
    fFinal = 0;
    fDefault = null;
    fAnnotations = null;
    fSubGroup = null;
    for (int i = 0; i < fIDCPos; i++) {
      fIDConstraints[i] = null;
    }
    fIDCPos = 0;
  }
  
  public short getType()
  {
    return 2;
  }
  
  public String getName()
  {
    return fName;
  }
  
  public String getNamespace()
  {
    return fTargetNamespace;
  }
  
  public XSTypeDefinition getTypeDefinition()
  {
    return fType;
  }
  
  public short getScope()
  {
    return fScope;
  }
  
  public XSComplexTypeDefinition getEnclosingCTDefinition()
  {
    return fEnclosingCT;
  }
  
  public short getConstraintType()
  {
    return (short)(fMiscFlags & 0x3);
  }
  
  public String getConstraintValue()
  {
    return getConstraintType() == 0 ? null : fDefault.stringValue();
  }
  
  public boolean getNillable()
  {
    return (fMiscFlags & 0x4) != 0;
  }
  
  public XSNamedMap getIdentityConstraints()
  {
    return new XSNamedMapImpl(fIDConstraints, fIDCPos);
  }
  
  public XSElementDeclaration getSubstitutionGroupAffiliation()
  {
    return fSubGroup;
  }
  
  public boolean isSubstitutionGroupExclusion(short paramShort)
  {
    return (fFinal & paramShort) != 0;
  }
  
  public short getSubstitutionGroupExclusions()
  {
    return fFinal;
  }
  
  public boolean isDisallowedSubstitution(short paramShort)
  {
    return (fBlock & paramShort) != 0;
  }
  
  public short getDisallowedSubstitutions()
  {
    return fBlock;
  }
  
  public boolean getAbstract()
  {
    return (fMiscFlags & 0x8) != 0;
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
}
