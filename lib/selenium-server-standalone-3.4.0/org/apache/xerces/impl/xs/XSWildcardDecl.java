package org.apache.xerces.impl.xs;

import org.apache.xerces.impl.xs.util.StringListImpl;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSAnnotation;
import org.apache.xerces.xs.XSNamespaceItem;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSWildcard;

public class XSWildcardDecl
  implements XSWildcard
{
  public static final String ABSENT = null;
  public short fType = 1;
  public short fProcessContents = 1;
  public String[] fNamespaceList;
  public XSObjectList fAnnotations = null;
  private String fDescription = null;
  
  public XSWildcardDecl() {}
  
  public boolean allowNamespace(String paramString)
  {
    if (fType == 1) {
      return true;
    }
    int i;
    int j;
    if (fType == 2)
    {
      i = 0;
      j = fNamespaceList.length;
      for (int k = 0; (k < j) && (i == 0); k++) {
        if (paramString == fNamespaceList[k]) {
          i = 1;
        }
      }
      if (i == 0) {
        return true;
      }
    }
    if (fType == 3)
    {
      i = fNamespaceList.length;
      for (j = 0; j < i; j++) {
        if (paramString == fNamespaceList[j]) {
          return true;
        }
      }
    }
    return false;
  }
  
  public boolean isSubsetOf(XSWildcardDecl paramXSWildcardDecl)
  {
    if (paramXSWildcardDecl == null) {
      return false;
    }
    if (fType == 1) {
      return true;
    }
    if ((fType == 2) && (fType == 2) && (fNamespaceList[0] == fNamespaceList[0])) {
      return true;
    }
    if (fType == 3)
    {
      if ((fType == 3) && (subset2sets(fNamespaceList, fNamespaceList))) {
        return true;
      }
      if ((fType == 2) && (!elementInSet(fNamespaceList[0], fNamespaceList)) && (!elementInSet(ABSENT, fNamespaceList))) {
        return true;
      }
    }
    return false;
  }
  
  public boolean weakerProcessContents(XSWildcardDecl paramXSWildcardDecl)
  {
    return ((fProcessContents == 3) && (fProcessContents == 1)) || ((fProcessContents == 2) && (fProcessContents != 2));
  }
  
  public XSWildcardDecl performUnionWith(XSWildcardDecl paramXSWildcardDecl, short paramShort)
  {
    if (paramXSWildcardDecl == null) {
      return null;
    }
    XSWildcardDecl localXSWildcardDecl = new XSWildcardDecl();
    fProcessContents = paramShort;
    if (areSame(paramXSWildcardDecl))
    {
      fType = fType;
      fNamespaceList = fNamespaceList;
    }
    else if ((fType == 1) || (fType == 1))
    {
      fType = 1;
    }
    else if ((fType == 3) && (fType == 3))
    {
      fType = 3;
      fNamespaceList = union2sets(fNamespaceList, fNamespaceList);
    }
    else if ((fType == 2) && (fType == 2))
    {
      fType = 2;
      fNamespaceList = new String[2];
      fNamespaceList[0] = ABSENT;
      fNamespaceList[1] = ABSENT;
    }
    else if (((fType == 2) && (fType == 3)) || ((fType == 3) && (fType == 2)))
    {
      String[] arrayOfString1 = null;
      String[] arrayOfString2 = null;
      if (fType == 2)
      {
        arrayOfString1 = fNamespaceList;
        arrayOfString2 = fNamespaceList;
      }
      else
      {
        arrayOfString1 = fNamespaceList;
        arrayOfString2 = fNamespaceList;
      }
      boolean bool1 = elementInSet(ABSENT, arrayOfString2);
      if (arrayOfString1[0] != ABSENT)
      {
        boolean bool2 = elementInSet(arrayOfString1[0], arrayOfString2);
        if ((bool2) && (bool1))
        {
          fType = 1;
        }
        else if ((bool2) && (!bool1))
        {
          fType = 2;
          fNamespaceList = new String[2];
          fNamespaceList[0] = ABSENT;
          fNamespaceList[1] = ABSENT;
        }
        else
        {
          if ((!bool2) && (bool1)) {
            return null;
          }
          fType = 2;
          fNamespaceList = arrayOfString1;
        }
      }
      else if (bool1)
      {
        fType = 1;
      }
      else
      {
        fType = 2;
        fNamespaceList = arrayOfString1;
      }
    }
    return localXSWildcardDecl;
  }
  
  public XSWildcardDecl performIntersectionWith(XSWildcardDecl paramXSWildcardDecl, short paramShort)
  {
    if (paramXSWildcardDecl == null) {
      return null;
    }
    XSWildcardDecl localXSWildcardDecl = new XSWildcardDecl();
    fProcessContents = paramShort;
    if (areSame(paramXSWildcardDecl))
    {
      fType = fType;
      fNamespaceList = fNamespaceList;
    }
    else
    {
      Object localObject;
      if ((fType == 1) || (fType == 1))
      {
        localObject = this;
        if (fType == 1) {
          localObject = paramXSWildcardDecl;
        }
        fType = fType;
        fNamespaceList = fNamespaceList;
      }
      else if (((fType == 2) && (fType == 3)) || ((fType == 3) && (fType == 2)))
      {
        localObject = null;
        String[] arrayOfString1 = null;
        if (fType == 2)
        {
          arrayOfString1 = fNamespaceList;
          localObject = fNamespaceList;
        }
        else
        {
          arrayOfString1 = fNamespaceList;
          localObject = fNamespaceList;
        }
        int i = localObject.length;
        String[] arrayOfString2 = new String[i];
        int j = 0;
        for (int k = 0; k < i; k++) {
          if ((localObject[k] != arrayOfString1[0]) && (localObject[k] != ABSENT)) {
            arrayOfString2[(j++)] = localObject[k];
          }
        }
        fType = 3;
        fNamespaceList = new String[j];
        System.arraycopy(arrayOfString2, 0, fNamespaceList, 0, j);
      }
      else if ((fType == 3) && (fType == 3))
      {
        fType = 3;
        fNamespaceList = intersect2sets(fNamespaceList, fNamespaceList);
      }
      else if ((fType == 2) && (fType == 2))
      {
        if ((fNamespaceList[0] != ABSENT) && (fNamespaceList[0] != ABSENT)) {
          return null;
        }
        localObject = this;
        if (fNamespaceList[0] == ABSENT) {
          localObject = paramXSWildcardDecl;
        }
        fType = fType;
        fNamespaceList = fNamespaceList;
      }
    }
    return localXSWildcardDecl;
  }
  
  private boolean areSame(XSWildcardDecl paramXSWildcardDecl)
  {
    if (fType == fType)
    {
      if (fType == 1) {
        return true;
      }
      if (fType == 2) {
        return fNamespaceList[0] == fNamespaceList[0];
      }
      if (fNamespaceList.length == fNamespaceList.length)
      {
        for (int i = 0; i < fNamespaceList.length; i++) {
          if (!elementInSet(fNamespaceList[i], fNamespaceList)) {
            return false;
          }
        }
        return true;
      }
    }
    return false;
  }
  
  String[] intersect2sets(String[] paramArrayOfString1, String[] paramArrayOfString2)
  {
    String[] arrayOfString1 = new String[Math.min(paramArrayOfString1.length, paramArrayOfString2.length)];
    int i = 0;
    for (int j = 0; j < paramArrayOfString1.length; j++) {
      if (elementInSet(paramArrayOfString1[j], paramArrayOfString2)) {
        arrayOfString1[(i++)] = paramArrayOfString1[j];
      }
    }
    String[] arrayOfString2 = new String[i];
    System.arraycopy(arrayOfString1, 0, arrayOfString2, 0, i);
    return arrayOfString2;
  }
  
  String[] union2sets(String[] paramArrayOfString1, String[] paramArrayOfString2)
  {
    String[] arrayOfString1 = new String[paramArrayOfString1.length];
    int i = 0;
    for (int j = 0; j < paramArrayOfString1.length; j++) {
      if (!elementInSet(paramArrayOfString1[j], paramArrayOfString2)) {
        arrayOfString1[(i++)] = paramArrayOfString1[j];
      }
    }
    String[] arrayOfString2 = new String[i + paramArrayOfString2.length];
    System.arraycopy(arrayOfString1, 0, arrayOfString2, 0, i);
    System.arraycopy(paramArrayOfString2, 0, arrayOfString2, i, paramArrayOfString2.length);
    return arrayOfString2;
  }
  
  boolean subset2sets(String[] paramArrayOfString1, String[] paramArrayOfString2)
  {
    for (int i = 0; i < paramArrayOfString1.length; i++) {
      if (!elementInSet(paramArrayOfString1[i], paramArrayOfString2)) {
        return false;
      }
    }
    return true;
  }
  
  boolean elementInSet(String paramString, String[] paramArrayOfString)
  {
    boolean bool = false;
    for (int i = 0; (i < paramArrayOfString.length) && (!bool); i++) {
      if (paramString == paramArrayOfString[i]) {
        bool = true;
      }
    }
    return bool;
  }
  
  public String toString()
  {
    if (fDescription == null)
    {
      StringBuffer localStringBuffer = new StringBuffer();
      localStringBuffer.append("WC[");
      switch (fType)
      {
      case 1: 
        localStringBuffer.append("##any");
        break;
      case 2: 
        localStringBuffer.append("##other");
        localStringBuffer.append(":\"");
        if (fNamespaceList[0] != null) {
          localStringBuffer.append(fNamespaceList[0]);
        }
        localStringBuffer.append("\"");
        break;
      case 3: 
        if (fNamespaceList.length != 0)
        {
          localStringBuffer.append("\"");
          if (fNamespaceList[0] != null) {
            localStringBuffer.append(fNamespaceList[0]);
          }
          localStringBuffer.append("\"");
          for (int i = 1; i < fNamespaceList.length; i++)
          {
            localStringBuffer.append(",\"");
            if (fNamespaceList[i] != null) {
              localStringBuffer.append(fNamespaceList[i]);
            }
            localStringBuffer.append("\"");
          }
        }
        break;
      }
      localStringBuffer.append(']');
      fDescription = localStringBuffer.toString();
    }
    return fDescription;
  }
  
  public short getType()
  {
    return 9;
  }
  
  public String getName()
  {
    return null;
  }
  
  public String getNamespace()
  {
    return null;
  }
  
  public short getConstraintType()
  {
    return fType;
  }
  
  public StringList getNsConstraintList()
  {
    return new StringListImpl(fNamespaceList, fNamespaceList == null ? 0 : fNamespaceList.length);
  }
  
  public short getProcessContents()
  {
    return fProcessContents;
  }
  
  public String getProcessContentsAsString()
  {
    switch (fProcessContents)
    {
    case 2: 
      return "skip";
    case 3: 
      return "lax";
    case 1: 
      return "strict";
    }
    return "invalid value";
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
    return null;
  }
}
