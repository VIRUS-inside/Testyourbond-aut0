package org.apache.xerces.impl.dv;

import org.apache.xerces.impl.xs.util.ShortListImpl;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.xs.ShortList;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSValue;

public class ValidatedInfo
  implements XSValue
{
  public String normalizedValue;
  public Object actualValue;
  public short actualValueType;
  public XSSimpleType actualType;
  public XSSimpleType memberType;
  public XSSimpleType[] memberTypes;
  public ShortList itemValueTypes;
  
  public ValidatedInfo() {}
  
  public void reset()
  {
    normalizedValue = null;
    actualValue = null;
    actualValueType = 45;
    actualType = null;
    memberType = null;
    memberTypes = null;
    itemValueTypes = null;
  }
  
  public String stringValue()
  {
    if (actualValue == null) {
      return normalizedValue;
    }
    return actualValue.toString();
  }
  
  public static boolean isComparable(ValidatedInfo paramValidatedInfo1, ValidatedInfo paramValidatedInfo2)
  {
    int i = convertToPrimitiveKind(actualValueType);
    int j = convertToPrimitiveKind(actualValueType);
    if (i != j) {
      return ((i == 1) && (j == 2)) || ((i == 2) && (j == 1));
    }
    if ((i == 44) || (i == 43))
    {
      ShortList localShortList1 = itemValueTypes;
      ShortList localShortList2 = itemValueTypes;
      int k = localShortList1 != null ? localShortList1.getLength() : 0;
      int m = localShortList2 != null ? localShortList2.getLength() : 0;
      if (k != m) {
        return false;
      }
      for (int n = 0; n < k; n++)
      {
        int i1 = convertToPrimitiveKind(localShortList1.item(n));
        int i2 = convertToPrimitiveKind(localShortList2.item(n));
        if ((i1 != i2) && ((i1 != 1) || (i2 != 2)) && ((i1 != 2) || (i2 != 1))) {
          return false;
        }
      }
    }
    return true;
  }
  
  private static short convertToPrimitiveKind(short paramShort)
  {
    if (paramShort <= 20) {
      return paramShort;
    }
    if (paramShort <= 29) {
      return 2;
    }
    if (paramShort <= 42) {
      return 4;
    }
    return paramShort;
  }
  
  public Object getActualValue()
  {
    return actualValue;
  }
  
  public short getActualValueType()
  {
    return actualValueType;
  }
  
  public ShortList getListValueTypes()
  {
    return itemValueTypes == null ? ShortListImpl.EMPTY_LIST : itemValueTypes;
  }
  
  public XSObjectList getMemberTypeDefinitions()
  {
    if (memberTypes == null) {
      return XSObjectListImpl.EMPTY_LIST;
    }
    return new XSObjectListImpl(memberTypes, memberTypes.length);
  }
  
  public String getNormalizedValue()
  {
    return normalizedValue;
  }
  
  public XSSimpleTypeDefinition getTypeDefinition()
  {
    return actualType;
  }
  
  public XSSimpleTypeDefinition getMemberTypeDefinition()
  {
    return memberType;
  }
  
  public void copyFrom(XSValue paramXSValue)
  {
    if (paramXSValue == null)
    {
      reset();
    }
    else
    {
      Object localObject;
      if ((paramXSValue instanceof ValidatedInfo))
      {
        localObject = (ValidatedInfo)paramXSValue;
        normalizedValue = normalizedValue;
        actualValue = actualValue;
        actualValueType = actualValueType;
        actualType = actualType;
        memberType = memberType;
        memberTypes = memberTypes;
        itemValueTypes = itemValueTypes;
      }
      else
      {
        normalizedValue = paramXSValue.getNormalizedValue();
        actualValue = paramXSValue.getActualValue();
        actualValueType = paramXSValue.getActualValueType();
        actualType = ((XSSimpleType)paramXSValue.getTypeDefinition());
        memberType = ((XSSimpleType)paramXSValue.getMemberTypeDefinition());
        localObject = memberType == null ? actualType : memberType;
        if ((localObject != null) && (((XSSimpleType)localObject).getBuiltInKind() == 43))
        {
          XSObjectList localXSObjectList = paramXSValue.getMemberTypeDefinitions();
          memberTypes = new XSSimpleType[localXSObjectList.getLength()];
          for (int i = 0; i < localXSObjectList.getLength(); i++) {
            memberTypes[i] = ((XSSimpleType)localXSObjectList.get(i));
          }
        }
        else
        {
          memberTypes = null;
        }
        itemValueTypes = paramXSValue.getListValueTypes();
      }
    }
  }
}
