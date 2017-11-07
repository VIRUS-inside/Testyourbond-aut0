package org.apache.xerces.impl.xs;

import java.util.Hashtable;
import java.util.Vector;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;

public class SubstitutionGroupHandler
{
  private static final XSElementDecl[] EMPTY_GROUP = new XSElementDecl[0];
  private final XSElementDeclHelper fXSElementDeclHelper;
  Hashtable fSubGroupsB = new Hashtable();
  private static final OneSubGroup[] EMPTY_VECTOR = new OneSubGroup[0];
  Hashtable fSubGroups = new Hashtable();
  
  public SubstitutionGroupHandler(XSElementDeclHelper paramXSElementDeclHelper)
  {
    fXSElementDeclHelper = paramXSElementDeclHelper;
  }
  
  public XSElementDecl getMatchingElemDecl(QName paramQName, XSElementDecl paramXSElementDecl)
  {
    if ((localpart == fName) && (uri == fTargetNamespace)) {
      return paramXSElementDecl;
    }
    if (fScope != 1) {
      return null;
    }
    if ((fBlock & 0x4) != 0) {
      return null;
    }
    XSElementDecl localXSElementDecl = fXSElementDeclHelper.getGlobalElementDecl(paramQName);
    if (localXSElementDecl == null) {
      return null;
    }
    if (substitutionGroupOK(localXSElementDecl, paramXSElementDecl, fBlock)) {
      return localXSElementDecl;
    }
    return null;
  }
  
  protected boolean substitutionGroupOK(XSElementDecl paramXSElementDecl1, XSElementDecl paramXSElementDecl2, short paramShort)
  {
    if (paramXSElementDecl1 == paramXSElementDecl2) {
      return true;
    }
    if ((paramShort & 0x4) != 0) {
      return false;
    }
    for (XSElementDecl localXSElementDecl = fSubGroup; (localXSElementDecl != null) && (localXSElementDecl != paramXSElementDecl2); localXSElementDecl = fSubGroup) {}
    if (localXSElementDecl == null) {
      return false;
    }
    return typeDerivationOK(fType, fType, paramShort);
  }
  
  private boolean typeDerivationOK(XSTypeDefinition paramXSTypeDefinition1, XSTypeDefinition paramXSTypeDefinition2, short paramShort)
  {
    int i = 0;
    int j = paramShort;
    Object localObject = paramXSTypeDefinition1;
    while ((localObject != paramXSTypeDefinition2) && (localObject != SchemaGrammar.fAnyType))
    {
      if (((XSTypeDefinition)localObject).getTypeCategory() == 15) {
        i = (short)(i | fDerivedBy);
      } else {
        i = (short)(i | 0x2);
      }
      localObject = ((XSTypeDefinition)localObject).getBaseType();
      if (localObject == null) {
        localObject = SchemaGrammar.fAnyType;
      }
      if (((XSTypeDefinition)localObject).getTypeCategory() == 15) {
        j = (short)(j | fBlock);
      }
    }
    if (localObject != paramXSTypeDefinition2)
    {
      if (paramXSTypeDefinition2.getTypeCategory() == 16)
      {
        XSSimpleTypeDefinition localXSSimpleTypeDefinition = (XSSimpleTypeDefinition)paramXSTypeDefinition2;
        if (localXSSimpleTypeDefinition.getVariety() == 3)
        {
          XSObjectList localXSObjectList = localXSSimpleTypeDefinition.getMemberTypes();
          int k = localXSObjectList.getLength();
          for (int m = 0; m < k; m++) {
            if (typeDerivationOK(paramXSTypeDefinition1, (XSTypeDefinition)localXSObjectList.item(m), paramShort)) {
              return true;
            }
          }
        }
      }
      return false;
    }
    return (i & j) == 0;
  }
  
  public boolean inSubstitutionGroup(XSElementDecl paramXSElementDecl1, XSElementDecl paramXSElementDecl2)
  {
    return substitutionGroupOK(paramXSElementDecl1, paramXSElementDecl2, fBlock);
  }
  
  public void reset()
  {
    fSubGroupsB.clear();
    fSubGroups.clear();
  }
  
  public void addSubstitutionGroup(XSElementDecl[] paramArrayOfXSElementDecl)
  {
    for (int i = paramArrayOfXSElementDecl.length - 1; i >= 0; i--)
    {
      XSElementDecl localXSElementDecl2 = paramArrayOfXSElementDecl[i];
      XSElementDecl localXSElementDecl1 = fSubGroup;
      Vector localVector = (Vector)fSubGroupsB.get(localXSElementDecl1);
      if (localVector == null)
      {
        localVector = new Vector();
        fSubGroupsB.put(localXSElementDecl1, localVector);
      }
      localVector.addElement(localXSElementDecl2);
    }
  }
  
  public XSElementDecl[] getSubstitutionGroup(XSElementDecl paramXSElementDecl)
  {
    Object localObject1 = fSubGroups.get(paramXSElementDecl);
    if (localObject1 != null) {
      return (XSElementDecl[])localObject1;
    }
    if ((fBlock & 0x4) != 0)
    {
      fSubGroups.put(paramXSElementDecl, EMPTY_GROUP);
      return EMPTY_GROUP;
    }
    OneSubGroup[] arrayOfOneSubGroup = getSubGroupB(paramXSElementDecl, new OneSubGroup());
    int i = arrayOfOneSubGroup.length;
    int j = 0;
    Object localObject2 = new XSElementDecl[i];
    for (int k = 0; k < i; k++) {
      if ((fBlock & dMethod) == 0) {
        localObject2[(j++)] = sub;
      }
    }
    if (j < i)
    {
      XSElementDecl[] arrayOfXSElementDecl = new XSElementDecl[j];
      System.arraycopy(localObject2, 0, arrayOfXSElementDecl, 0, j);
      localObject2 = arrayOfXSElementDecl;
    }
    fSubGroups.put(paramXSElementDecl, localObject2);
    return localObject2;
  }
  
  private OneSubGroup[] getSubGroupB(XSElementDecl paramXSElementDecl, OneSubGroup paramOneSubGroup)
  {
    Object localObject1 = fSubGroupsB.get(paramXSElementDecl);
    if (localObject1 == null)
    {
      fSubGroupsB.put(paramXSElementDecl, EMPTY_VECTOR);
      return EMPTY_VECTOR;
    }
    if ((localObject1 instanceof OneSubGroup[])) {
      return (OneSubGroup[])localObject1;
    }
    Vector localVector1 = (Vector)localObject1;
    Vector localVector2 = new Vector();
    for (int k = localVector1.size() - 1; k >= 0; k--)
    {
      localObject2 = (XSElementDecl)localVector1.elementAt(k);
      if (getDBMethods(fType, fType, paramOneSubGroup))
      {
        int i = dMethod;
        int j = bMethod;
        localVector2.addElement(new OneSubGroup((XSElementDecl)localObject2, dMethod, bMethod));
        OneSubGroup[] arrayOfOneSubGroup = getSubGroupB((XSElementDecl)localObject2, paramOneSubGroup);
        for (int m = arrayOfOneSubGroup.length - 1; m >= 0; m--)
        {
          short s1 = (short)(i | dMethod);
          short s2 = (short)(j | bMethod);
          if ((s1 & s2) == 0) {
            localVector2.addElement(new OneSubGroup(sub, s1, s2));
          }
        }
      }
    }
    Object localObject2 = new OneSubGroup[localVector2.size()];
    for (int n = localVector2.size() - 1; n >= 0; n--) {
      localObject2[n] = ((OneSubGroup)localVector2.elementAt(n));
    }
    fSubGroupsB.put(paramXSElementDecl, localObject2);
    return localObject2;
  }
  
  private boolean getDBMethods(XSTypeDefinition paramXSTypeDefinition1, XSTypeDefinition paramXSTypeDefinition2, OneSubGroup paramOneSubGroup)
  {
    short s1 = 0;
    short s2 = 0;
    while ((paramXSTypeDefinition1 != paramXSTypeDefinition2) && (paramXSTypeDefinition1 != SchemaGrammar.fAnyType))
    {
      if (paramXSTypeDefinition1.getTypeCategory() == 15) {
        s1 = (short)(s1 | fDerivedBy);
      } else {
        s1 = (short)(s1 | 0x2);
      }
      paramXSTypeDefinition1 = paramXSTypeDefinition1.getBaseType();
      if (paramXSTypeDefinition1 == null) {
        paramXSTypeDefinition1 = SchemaGrammar.fAnyType;
      }
      if (paramXSTypeDefinition1.getTypeCategory() == 15) {
        s2 = (short)(s2 | fBlock);
      }
    }
    if ((paramXSTypeDefinition1 != paramXSTypeDefinition2) || ((s1 & s2) != 0)) {
      return false;
    }
    dMethod = s1;
    bMethod = s2;
    return true;
  }
  
  private static final class OneSubGroup
  {
    XSElementDecl sub;
    short dMethod;
    short bMethod;
    
    OneSubGroup() {}
    
    OneSubGroup(XSElementDecl paramXSElementDecl, short paramShort1, short paramShort2)
    {
      sub = paramXSElementDecl;
      dMethod = paramShort1;
      bMethod = paramShort2;
    }
  }
}
