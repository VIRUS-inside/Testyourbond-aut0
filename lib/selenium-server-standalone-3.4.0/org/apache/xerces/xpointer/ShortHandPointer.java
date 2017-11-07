package org.apache.xerces.xpointer;

import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xs.AttributePSVI;

final class ShortHandPointer
  implements XPointerPart
{
  private String fShortHandPointer;
  private boolean fIsFragmentResolved = false;
  private SymbolTable fSymbolTable;
  int fMatchingChildCount = 0;
  
  public ShortHandPointer() {}
  
  public ShortHandPointer(SymbolTable paramSymbolTable)
  {
    fSymbolTable = paramSymbolTable;
  }
  
  public void parseXPointer(String paramString)
    throws XNIException
  {
    fShortHandPointer = paramString;
    fIsFragmentResolved = false;
  }
  
  public boolean resolveXPointer(QName paramQName, XMLAttributes paramXMLAttributes, Augmentations paramAugmentations, int paramInt)
    throws XNIException
  {
    if (fMatchingChildCount == 0) {
      fIsFragmentResolved = false;
    }
    if (paramInt == 0)
    {
      if (fMatchingChildCount == 0) {
        fIsFragmentResolved = hasMatchingIdentifier(paramQName, paramXMLAttributes, paramAugmentations, paramInt);
      }
      if (fIsFragmentResolved) {
        fMatchingChildCount += 1;
      }
    }
    else if (paramInt == 2)
    {
      if (fMatchingChildCount == 0) {
        fIsFragmentResolved = hasMatchingIdentifier(paramQName, paramXMLAttributes, paramAugmentations, paramInt);
      }
    }
    else if (fIsFragmentResolved)
    {
      fMatchingChildCount -= 1;
    }
    return fIsFragmentResolved;
  }
  
  private boolean hasMatchingIdentifier(QName paramQName, XMLAttributes paramXMLAttributes, Augmentations paramAugmentations, int paramInt)
    throws XNIException
  {
    String str = null;
    if (paramXMLAttributes != null) {
      for (int i = 0; i < paramXMLAttributes.getLength(); i++)
      {
        str = getSchemaDeterminedID(paramXMLAttributes, i);
        if (str != null) {
          break;
        }
        str = getChildrenSchemaDeterminedID(paramXMLAttributes, i);
        if (str != null) {
          break;
        }
        str = getDTDDeterminedID(paramXMLAttributes, i);
        if (str != null) {
          break;
        }
      }
    }
    return (str != null) && (str.equals(fShortHandPointer));
  }
  
  public String getDTDDeterminedID(XMLAttributes paramXMLAttributes, int paramInt)
    throws XNIException
  {
    if (paramXMLAttributes.getType(paramInt).equals("ID")) {
      return paramXMLAttributes.getValue(paramInt);
    }
    return null;
  }
  
  public String getSchemaDeterminedID(XMLAttributes paramXMLAttributes, int paramInt)
    throws XNIException
  {
    Augmentations localAugmentations = paramXMLAttributes.getAugmentations(paramInt);
    AttributePSVI localAttributePSVI = (AttributePSVI)localAugmentations.getItem("ATTRIBUTE_PSVI");
    if (localAttributePSVI != null)
    {
      Object localObject = localAttributePSVI.getMemberTypeDefinition();
      if (localObject != null) {
        localObject = localAttributePSVI.getTypeDefinition();
      }
      if ((localObject != null) && (((XSSimpleType)localObject).isIDType())) {
        return localAttributePSVI.getSchemaNormalizedValue();
      }
    }
    return null;
  }
  
  public String getChildrenSchemaDeterminedID(XMLAttributes paramXMLAttributes, int paramInt)
    throws XNIException
  {
    return null;
  }
  
  public boolean isFragmentResolved()
  {
    return fIsFragmentResolved;
  }
  
  public boolean isChildFragmentResolved()
  {
    return (fIsFragmentResolved) && (fMatchingChildCount > 0);
  }
  
  public String getSchemeName()
  {
    return fShortHandPointer;
  }
  
  public String getSchemeData()
  {
    return null;
  }
  
  public void setSchemeName(String paramString)
  {
    fShortHandPointer = paramString;
  }
  
  public void setSchemeData(String paramString) {}
}
