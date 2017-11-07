package org.apache.xerces.impl.xs.util;

import org.apache.xerces.util.SymbolHash;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSTypeDefinition;

public final class XSNamedMap4Types
  extends XSNamedMapImpl
{
  private final short fType;
  
  public XSNamedMap4Types(String paramString, SymbolHash paramSymbolHash, short paramShort)
  {
    super(paramString, paramSymbolHash);
    fType = paramShort;
  }
  
  public XSNamedMap4Types(String[] paramArrayOfString, SymbolHash[] paramArrayOfSymbolHash, int paramInt, short paramShort)
  {
    super(paramArrayOfString, paramArrayOfSymbolHash, paramInt);
    fType = paramShort;
  }
  
  public synchronized int getLength()
  {
    if (fLength == -1)
    {
      int i = 0;
      for (int j = 0; j < fNSNum; j++) {
        i += fMaps[j].getLength();
      }
      int k = 0;
      XSObject[] arrayOfXSObject = new XSObject[i];
      for (int m = 0; m < fNSNum; m++) {
        k += fMaps[m].getValues(arrayOfXSObject, k);
      }
      fLength = 0;
      fArray = new XSObject[i];
      for (int n = 0; n < i; n++)
      {
        XSTypeDefinition localXSTypeDefinition = (XSTypeDefinition)arrayOfXSObject[n];
        if (localXSTypeDefinition.getTypeCategory() == fType) {
          fArray[(fLength++)] = localXSTypeDefinition;
        }
      }
    }
    return fLength;
  }
  
  public XSObject itemByName(String paramString1, String paramString2)
  {
    for (int i = 0; i < fNSNum; i++) {
      if (XSNamedMapImpl.isEqual(paramString1, fNamespaces[i]))
      {
        XSTypeDefinition localXSTypeDefinition = (XSTypeDefinition)fMaps[i].get(paramString2);
        if ((localXSTypeDefinition != null) && (localXSTypeDefinition.getTypeCategory() == fType)) {
          return localXSTypeDefinition;
        }
        return null;
      }
    }
    return null;
  }
  
  public synchronized XSObject item(int paramInt)
  {
    if (fArray == null) {
      getLength();
    }
    if ((paramInt < 0) || (paramInt >= fLength)) {
      return null;
    }
    return fArray[paramInt];
  }
}
