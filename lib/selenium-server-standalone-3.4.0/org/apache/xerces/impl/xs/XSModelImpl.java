package org.apache.xerces.impl.xs;

import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Vector;
import org.apache.xerces.impl.xs.util.StringListImpl;
import org.apache.xerces.impl.xs.util.XSNamedMap4Types;
import org.apache.xerces.impl.xs.util.XSNamedMapImpl;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.util.SymbolHash;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSAttributeDeclaration;
import org.apache.xerces.xs.XSAttributeGroupDefinition;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSIDCDefinition;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSModelGroupDefinition;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSNamespaceItem;
import org.apache.xerces.xs.XSNamespaceItemList;
import org.apache.xerces.xs.XSNotationDeclaration;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSTypeDefinition;

public final class XSModelImpl
  extends AbstractList
  implements XSModel, XSNamespaceItemList
{
  private static final short MAX_COMP_IDX = 16;
  private static final boolean[] GLOBAL_COMP = { false, true, true, true, false, true, true, false, false, false, true, true, false, false, false, true, true };
  private final int fGrammarCount;
  private final String[] fNamespaces;
  private final SchemaGrammar[] fGrammarList;
  private final SymbolHash fGrammarMap;
  private final SymbolHash fSubGroupMap;
  private final XSNamedMap[] fGlobalComponents;
  private final XSNamedMap[][] fNSComponents;
  private final StringList fNamespacesList;
  private XSObjectList fAnnotations = null;
  private final boolean fHasIDC;
  
  public XSModelImpl(SchemaGrammar[] paramArrayOfSchemaGrammar)
  {
    this(paramArrayOfSchemaGrammar, (short)1);
  }
  
  public XSModelImpl(SchemaGrammar[] paramArrayOfSchemaGrammar, short paramShort)
  {
    int i = paramArrayOfSchemaGrammar.length;
    int j = Math.max(i + 1, 5);
    Object localObject1 = new String[j];
    Object localObject2 = new SchemaGrammar[j];
    int k = 0;
    SchemaGrammar localSchemaGrammar;
    Object localObject3;
    for (int m = 0; m < i; m++)
    {
      localSchemaGrammar = paramArrayOfSchemaGrammar[m];
      localObject3 = localSchemaGrammar.getTargetNamespace();
      localObject1[m] = localObject3;
      localObject2[m] = localSchemaGrammar;
      if (localObject3 == SchemaSymbols.URI_SCHEMAFORSCHEMA) {
        k = 1;
      }
    }
    if (k == 0)
    {
      localObject1[i] = SchemaSymbols.URI_SCHEMAFORSCHEMA;
      localObject2[(i++)] = SchemaGrammar.getS4SGrammar(paramShort);
    }
    for (int n = 0; n < i; n++)
    {
      localSchemaGrammar = localObject2[n];
      Vector localVector = localSchemaGrammar.getImportedGrammars();
      for (int i1 = localVector == null ? -1 : localVector.size() - 1; i1 >= 0; i1--)
      {
        localObject3 = (SchemaGrammar)localVector.elementAt(i1);
        for (int i2 = 0; i2 < i; i2++) {
          if (localObject3 == localObject2[i2]) {
            break;
          }
        }
        if (i2 == i)
        {
          if (i == localObject2.length)
          {
            String[] arrayOfString = new String[i * 2];
            System.arraycopy(localObject1, 0, arrayOfString, 0, i);
            localObject1 = arrayOfString;
            SchemaGrammar[] arrayOfSchemaGrammar = new SchemaGrammar[i * 2];
            System.arraycopy(localObject2, 0, arrayOfSchemaGrammar, 0, i);
            localObject2 = arrayOfSchemaGrammar;
          }
          localObject1[i] = ((SchemaGrammar)localObject3).getTargetNamespace();
          localObject2[i] = localObject3;
          i++;
        }
      }
    }
    fNamespaces = ((String[])localObject1);
    fGrammarList = ((SchemaGrammar[])localObject2);
    boolean bool = false;
    fGrammarMap = new SymbolHash(i * 2);
    for (n = 0; n < i; n++)
    {
      fGrammarMap.put(null2EmptyString(fNamespaces[n]), fGrammarList[n]);
      if (fGrammarList[n].hasIDConstraints()) {
        bool = true;
      }
    }
    fHasIDC = bool;
    fGrammarCount = i;
    fGlobalComponents = new XSNamedMap[17];
    fNSComponents = new XSNamedMap[i][17];
    fNamespacesList = new StringListImpl(fNamespaces, fGrammarCount);
    fSubGroupMap = buildSubGroups();
  }
  
  private SymbolHash buildSubGroups_Org()
  {
    SubstitutionGroupHandler localSubstitutionGroupHandler = new SubstitutionGroupHandler(null);
    for (int i = 0; i < fGrammarCount; i++) {
      localSubstitutionGroupHandler.addSubstitutionGroup(fGrammarList[i].getSubstitutionGroups());
    }
    XSNamedMap localXSNamedMap = getComponents((short)2);
    int j = localXSNamedMap.getLength();
    SymbolHash localSymbolHash = new SymbolHash(j * 2);
    for (int k = 0; k < j; k++)
    {
      XSElementDecl localXSElementDecl = (XSElementDecl)localXSNamedMap.item(k);
      XSElementDecl[] arrayOfXSElementDecl = localSubstitutionGroupHandler.getSubstitutionGroup(localXSElementDecl);
      localSymbolHash.put(localXSElementDecl, arrayOfXSElementDecl.length > 0 ? new XSObjectListImpl(arrayOfXSElementDecl, arrayOfXSElementDecl.length) : XSObjectListImpl.EMPTY_LIST);
    }
    return localSymbolHash;
  }
  
  private SymbolHash buildSubGroups()
  {
    SubstitutionGroupHandler localSubstitutionGroupHandler = new SubstitutionGroupHandler(null);
    for (int i = 0; i < fGrammarCount; i++) {
      localSubstitutionGroupHandler.addSubstitutionGroup(fGrammarList[i].getSubstitutionGroups());
    }
    XSObjectListImpl localXSObjectListImpl = getGlobalElements();
    int j = localXSObjectListImpl.getLength();
    SymbolHash localSymbolHash = new SymbolHash(j * 2);
    for (int k = 0; k < j; k++)
    {
      XSElementDecl localXSElementDecl = (XSElementDecl)localXSObjectListImpl.item(k);
      XSElementDecl[] arrayOfXSElementDecl = localSubstitutionGroupHandler.getSubstitutionGroup(localXSElementDecl);
      localSymbolHash.put(localXSElementDecl, arrayOfXSElementDecl.length > 0 ? new XSObjectListImpl(arrayOfXSElementDecl, arrayOfXSElementDecl.length) : XSObjectListImpl.EMPTY_LIST);
    }
    return localSymbolHash;
  }
  
  private XSObjectListImpl getGlobalElements()
  {
    SymbolHash[] arrayOfSymbolHash = new SymbolHash[fGrammarCount];
    int i = 0;
    for (int j = 0; j < fGrammarCount; j++)
    {
      arrayOfSymbolHash[j] = fGrammarList[j].fAllGlobalElemDecls;
      i += arrayOfSymbolHash[j].getLength();
    }
    if (i == 0) {
      return XSObjectListImpl.EMPTY_LIST;
    }
    XSObject[] arrayOfXSObject = new XSObject[i];
    int k = 0;
    for (int m = 0; m < fGrammarCount; m++)
    {
      arrayOfSymbolHash[m].getValues(arrayOfXSObject, k);
      k += arrayOfSymbolHash[m].getLength();
    }
    return new XSObjectListImpl(arrayOfXSObject, i);
  }
  
  public StringList getNamespaces()
  {
    return fNamespacesList;
  }
  
  public XSNamespaceItemList getNamespaceItems()
  {
    return this;
  }
  
  public synchronized XSNamedMap getComponents(short paramShort)
  {
    if ((paramShort <= 0) || (paramShort > 16) || (GLOBAL_COMP[paramShort] == 0)) {
      return XSNamedMapImpl.EMPTY_MAP;
    }
    SymbolHash[] arrayOfSymbolHash = new SymbolHash[fGrammarCount];
    if (fGlobalComponents[paramShort] == null)
    {
      for (int i = 0; i < fGrammarCount; i++) {
        switch (paramShort)
        {
        case 3: 
        case 15: 
        case 16: 
          arrayOfSymbolHash[i] = fGrammarList[i].fGlobalTypeDecls;
          break;
        case 1: 
          arrayOfSymbolHash[i] = fGrammarList[i].fGlobalAttrDecls;
          break;
        case 2: 
          arrayOfSymbolHash[i] = fGrammarList[i].fGlobalElemDecls;
          break;
        case 5: 
          arrayOfSymbolHash[i] = fGrammarList[i].fGlobalAttrGrpDecls;
          break;
        case 6: 
          arrayOfSymbolHash[i] = fGrammarList[i].fGlobalGroupDecls;
          break;
        case 11: 
          arrayOfSymbolHash[i] = fGrammarList[i].fGlobalNotationDecls;
          break;
        case 10: 
          arrayOfSymbolHash[i] = fGrammarList[i].fGlobalIDConstraintDecls;
        }
      }
      if ((paramShort == 15) || (paramShort == 16)) {
        fGlobalComponents[paramShort] = new XSNamedMap4Types(fNamespaces, arrayOfSymbolHash, fGrammarCount, paramShort);
      } else {
        fGlobalComponents[paramShort] = new XSNamedMapImpl(fNamespaces, arrayOfSymbolHash, fGrammarCount);
      }
    }
    return fGlobalComponents[paramShort];
  }
  
  public synchronized XSNamedMap getComponentsByNamespace(short paramShort, String paramString)
  {
    if ((paramShort <= 0) || (paramShort > 16) || (GLOBAL_COMP[paramShort] == 0)) {
      return XSNamedMapImpl.EMPTY_MAP;
    }
    int i = 0;
    if (paramString != null) {
      while (i < fGrammarCount)
      {
        if (paramString.equals(fNamespaces[i])) {
          break;
        }
        i++;
      }
    } else {
      while (i < fGrammarCount)
      {
        if (fNamespaces[i] == null) {
          break;
        }
        i++;
      }
    }
    if (i == fGrammarCount) {
      return XSNamedMapImpl.EMPTY_MAP;
    }
    if (fNSComponents[i][paramShort] == null)
    {
      SymbolHash localSymbolHash = null;
      switch (paramShort)
      {
      case 3: 
      case 15: 
      case 16: 
        localSymbolHash = fGrammarList[i].fGlobalTypeDecls;
        break;
      case 1: 
        localSymbolHash = fGrammarList[i].fGlobalAttrDecls;
        break;
      case 2: 
        localSymbolHash = fGrammarList[i].fGlobalElemDecls;
        break;
      case 5: 
        localSymbolHash = fGrammarList[i].fGlobalAttrGrpDecls;
        break;
      case 6: 
        localSymbolHash = fGrammarList[i].fGlobalGroupDecls;
        break;
      case 11: 
        localSymbolHash = fGrammarList[i].fGlobalNotationDecls;
        break;
      case 10: 
        localSymbolHash = fGrammarList[i].fGlobalIDConstraintDecls;
      }
      if ((paramShort == 15) || (paramShort == 16)) {
        fNSComponents[i][paramShort] = new XSNamedMap4Types(paramString, localSymbolHash, paramShort);
      } else {
        fNSComponents[i][paramShort] = new XSNamedMapImpl(paramString, localSymbolHash);
      }
    }
    return fNSComponents[i][paramShort];
  }
  
  public XSTypeDefinition getTypeDefinition(String paramString1, String paramString2)
  {
    SchemaGrammar localSchemaGrammar = (SchemaGrammar)fGrammarMap.get(null2EmptyString(paramString2));
    if (localSchemaGrammar == null) {
      return null;
    }
    return (XSTypeDefinition)fGlobalTypeDecls.get(paramString1);
  }
  
  public XSTypeDefinition getTypeDefinition(String paramString1, String paramString2, String paramString3)
  {
    SchemaGrammar localSchemaGrammar = (SchemaGrammar)fGrammarMap.get(null2EmptyString(paramString2));
    if (localSchemaGrammar == null) {
      return null;
    }
    return localSchemaGrammar.getGlobalTypeDecl(paramString1, paramString3);
  }
  
  public XSAttributeDeclaration getAttributeDeclaration(String paramString1, String paramString2)
  {
    SchemaGrammar localSchemaGrammar = (SchemaGrammar)fGrammarMap.get(null2EmptyString(paramString2));
    if (localSchemaGrammar == null) {
      return null;
    }
    return (XSAttributeDeclaration)fGlobalAttrDecls.get(paramString1);
  }
  
  public XSAttributeDeclaration getAttributeDeclaration(String paramString1, String paramString2, String paramString3)
  {
    SchemaGrammar localSchemaGrammar = (SchemaGrammar)fGrammarMap.get(null2EmptyString(paramString2));
    if (localSchemaGrammar == null) {
      return null;
    }
    return localSchemaGrammar.getGlobalAttributeDecl(paramString1, paramString3);
  }
  
  public XSElementDeclaration getElementDeclaration(String paramString1, String paramString2)
  {
    SchemaGrammar localSchemaGrammar = (SchemaGrammar)fGrammarMap.get(null2EmptyString(paramString2));
    if (localSchemaGrammar == null) {
      return null;
    }
    return (XSElementDeclaration)fGlobalElemDecls.get(paramString1);
  }
  
  public XSElementDeclaration getElementDeclaration(String paramString1, String paramString2, String paramString3)
  {
    SchemaGrammar localSchemaGrammar = (SchemaGrammar)fGrammarMap.get(null2EmptyString(paramString2));
    if (localSchemaGrammar == null) {
      return null;
    }
    return localSchemaGrammar.getGlobalElementDecl(paramString1, paramString3);
  }
  
  public XSAttributeGroupDefinition getAttributeGroup(String paramString1, String paramString2)
  {
    SchemaGrammar localSchemaGrammar = (SchemaGrammar)fGrammarMap.get(null2EmptyString(paramString2));
    if (localSchemaGrammar == null) {
      return null;
    }
    return (XSAttributeGroupDefinition)fGlobalAttrGrpDecls.get(paramString1);
  }
  
  public XSAttributeGroupDefinition getAttributeGroup(String paramString1, String paramString2, String paramString3)
  {
    SchemaGrammar localSchemaGrammar = (SchemaGrammar)fGrammarMap.get(null2EmptyString(paramString2));
    if (localSchemaGrammar == null) {
      return null;
    }
    return localSchemaGrammar.getGlobalAttributeGroupDecl(paramString1, paramString3);
  }
  
  public XSModelGroupDefinition getModelGroupDefinition(String paramString1, String paramString2)
  {
    SchemaGrammar localSchemaGrammar = (SchemaGrammar)fGrammarMap.get(null2EmptyString(paramString2));
    if (localSchemaGrammar == null) {
      return null;
    }
    return (XSModelGroupDefinition)fGlobalGroupDecls.get(paramString1);
  }
  
  public XSModelGroupDefinition getModelGroupDefinition(String paramString1, String paramString2, String paramString3)
  {
    SchemaGrammar localSchemaGrammar = (SchemaGrammar)fGrammarMap.get(null2EmptyString(paramString2));
    if (localSchemaGrammar == null) {
      return null;
    }
    return localSchemaGrammar.getGlobalGroupDecl(paramString1, paramString3);
  }
  
  public XSIDCDefinition getIDCDefinition(String paramString1, String paramString2)
  {
    SchemaGrammar localSchemaGrammar = (SchemaGrammar)fGrammarMap.get(null2EmptyString(paramString2));
    if (localSchemaGrammar == null) {
      return null;
    }
    return (XSIDCDefinition)fGlobalIDConstraintDecls.get(paramString1);
  }
  
  public XSIDCDefinition getIDCDefinition(String paramString1, String paramString2, String paramString3)
  {
    SchemaGrammar localSchemaGrammar = (SchemaGrammar)fGrammarMap.get(null2EmptyString(paramString2));
    if (localSchemaGrammar == null) {
      return null;
    }
    return localSchemaGrammar.getIDConstraintDecl(paramString1, paramString3);
  }
  
  public XSNotationDeclaration getNotationDeclaration(String paramString1, String paramString2)
  {
    SchemaGrammar localSchemaGrammar = (SchemaGrammar)fGrammarMap.get(null2EmptyString(paramString2));
    if (localSchemaGrammar == null) {
      return null;
    }
    return (XSNotationDeclaration)fGlobalNotationDecls.get(paramString1);
  }
  
  public XSNotationDeclaration getNotationDeclaration(String paramString1, String paramString2, String paramString3)
  {
    SchemaGrammar localSchemaGrammar = (SchemaGrammar)fGrammarMap.get(null2EmptyString(paramString2));
    if (localSchemaGrammar == null) {
      return null;
    }
    return localSchemaGrammar.getGlobalNotationDecl(paramString1, paramString3);
  }
  
  public synchronized XSObjectList getAnnotations()
  {
    if (fAnnotations != null) {
      return fAnnotations;
    }
    int i = 0;
    for (int j = 0; j < fGrammarCount; j++) {
      i += fGrammarList[j].fNumAnnotations;
    }
    if (i == 0)
    {
      fAnnotations = XSObjectListImpl.EMPTY_LIST;
      return fAnnotations;
    }
    XSAnnotationImpl[] arrayOfXSAnnotationImpl = new XSAnnotationImpl[i];
    int k = 0;
    for (int m = 0; m < fGrammarCount; m++)
    {
      SchemaGrammar localSchemaGrammar = fGrammarList[m];
      if (fNumAnnotations > 0)
      {
        System.arraycopy(fAnnotations, 0, arrayOfXSAnnotationImpl, k, fNumAnnotations);
        k += fNumAnnotations;
      }
    }
    fAnnotations = new XSObjectListImpl(arrayOfXSAnnotationImpl, arrayOfXSAnnotationImpl.length);
    return fAnnotations;
  }
  
  private static final String null2EmptyString(String paramString)
  {
    return paramString == null ? XMLSymbols.EMPTY_STRING : paramString;
  }
  
  public boolean hasIDConstraints()
  {
    return fHasIDC;
  }
  
  public XSObjectList getSubstitutionGroup(XSElementDeclaration paramXSElementDeclaration)
  {
    return (XSObjectList)fSubGroupMap.get(paramXSElementDeclaration);
  }
  
  public int getLength()
  {
    return fGrammarCount;
  }
  
  public XSNamespaceItem item(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= fGrammarCount)) {
      return null;
    }
    return fGrammarList[paramInt];
  }
  
  public Object get(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < fGrammarCount)) {
      return fGrammarList[paramInt];
    }
    throw new IndexOutOfBoundsException("Index: " + paramInt);
  }
  
  public int size()
  {
    return getLength();
  }
  
  public Iterator iterator()
  {
    return listIterator0(0);
  }
  
  public ListIterator listIterator()
  {
    return listIterator0(0);
  }
  
  public ListIterator listIterator(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < fGrammarCount)) {
      return listIterator0(paramInt);
    }
    throw new IndexOutOfBoundsException("Index: " + paramInt);
  }
  
  private ListIterator listIterator0(int paramInt)
  {
    return new XSNamespaceItemListIterator(paramInt);
  }
  
  public Object[] toArray()
  {
    Object[] arrayOfObject = new Object[fGrammarCount];
    toArray0(arrayOfObject);
    return arrayOfObject;
  }
  
  public Object[] toArray(Object[] paramArrayOfObject)
  {
    if (paramArrayOfObject.length < fGrammarCount)
    {
      Class localClass1 = paramArrayOfObject.getClass();
      Class localClass2 = localClass1.getComponentType();
      paramArrayOfObject = (Object[])Array.newInstance(localClass2, fGrammarCount);
    }
    toArray0(paramArrayOfObject);
    if (paramArrayOfObject.length > fGrammarCount) {
      paramArrayOfObject[fGrammarCount] = null;
    }
    return paramArrayOfObject;
  }
  
  private void toArray0(Object[] paramArrayOfObject)
  {
    if (fGrammarCount > 0) {
      System.arraycopy(fGrammarList, 0, paramArrayOfObject, 0, fGrammarCount);
    }
  }
  
  private final class XSNamespaceItemListIterator
    implements ListIterator
  {
    private int index;
    
    public XSNamespaceItemListIterator(int paramInt)
    {
      index = paramInt;
    }
    
    public boolean hasNext()
    {
      return index < fGrammarCount;
    }
    
    public Object next()
    {
      if (index < fGrammarCount) {
        return fGrammarList[(index++)];
      }
      throw new NoSuchElementException();
    }
    
    public boolean hasPrevious()
    {
      return index > 0;
    }
    
    public Object previous()
    {
      if (index > 0) {
        return fGrammarList[(--index)];
      }
      throw new NoSuchElementException();
    }
    
    public int nextIndex()
    {
      return index;
    }
    
    public int previousIndex()
    {
      return index - 1;
    }
    
    public void remove()
    {
      throw new UnsupportedOperationException();
    }
    
    public void set(Object paramObject)
    {
      throw new UnsupportedOperationException();
    }
    
    public void add(Object paramObject)
    {
      throw new UnsupportedOperationException();
    }
  }
}
