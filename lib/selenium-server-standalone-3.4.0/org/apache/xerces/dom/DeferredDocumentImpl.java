package org.apache.xerces.dom;

import java.util.ArrayList;
import java.util.Hashtable;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DeferredDocumentImpl
  extends DocumentImpl
  implements DeferredNode
{
  static final long serialVersionUID = 5186323580749626857L;
  private static final boolean DEBUG_PRINT_REF_COUNTS = false;
  private static final boolean DEBUG_PRINT_TABLES = false;
  private static final boolean DEBUG_IDS = false;
  protected static final int CHUNK_SHIFT = 11;
  protected static final int CHUNK_SIZE = 2048;
  protected static final int CHUNK_MASK = 2047;
  protected static final int INITIAL_CHUNK_COUNT = 32;
  protected transient int fNodeCount = 0;
  protected transient int[][] fNodeType;
  protected transient Object[][] fNodeName;
  protected transient Object[][] fNodeValue;
  protected transient int[][] fNodeParent;
  protected transient int[][] fNodeLastChild;
  protected transient int[][] fNodePrevSib;
  protected transient Object[][] fNodeURI;
  protected transient int[][] fNodeExtra;
  protected transient int fIdCount;
  protected transient String[] fIdName;
  protected transient int[] fIdElement;
  protected boolean fNamespacesEnabled = false;
  private final transient StringBuffer fBufferStr = new StringBuffer();
  private final transient ArrayList fStrChunks = new ArrayList();
  private static final int[] INIT_ARRAY = new int['ࠁ'];
  
  public DeferredDocumentImpl()
  {
    this(false);
  }
  
  public DeferredDocumentImpl(boolean paramBoolean)
  {
    this(paramBoolean, false);
  }
  
  public DeferredDocumentImpl(boolean paramBoolean1, boolean paramBoolean2)
  {
    super(paramBoolean2);
    needsSyncData(true);
    needsSyncChildren(true);
    fNamespacesEnabled = paramBoolean1;
  }
  
  public DOMImplementation getImplementation()
  {
    return DeferredDOMImplementationImpl.getDOMImplementation();
  }
  
  boolean getNamespacesEnabled()
  {
    return fNamespacesEnabled;
  }
  
  void setNamespacesEnabled(boolean paramBoolean)
  {
    fNamespacesEnabled = paramBoolean;
  }
  
  public int createDeferredDocument()
  {
    int i = createNode((short)9);
    return i;
  }
  
  public int createDeferredDocumentType(String paramString1, String paramString2, String paramString3)
  {
    int i = createNode((short)10);
    int j = i >> 11;
    int k = i & 0x7FF;
    setChunkValue(fNodeName, paramString1, j, k);
    setChunkValue(fNodeValue, paramString2, j, k);
    setChunkValue(fNodeURI, paramString3, j, k);
    return i;
  }
  
  public void setInternalSubset(int paramInt, String paramString)
  {
    int i = paramInt >> 11;
    int j = paramInt & 0x7FF;
    int k = createNode((short)10);
    int m = k >> 11;
    int n = k & 0x7FF;
    setChunkIndex(fNodeExtra, k, i, j);
    setChunkValue(fNodeValue, paramString, m, n);
  }
  
  public int createDeferredNotation(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    int i = createNode((short)12);
    int j = i >> 11;
    int k = i & 0x7FF;
    int m = createNode((short)12);
    int n = m >> 11;
    int i1 = m & 0x7FF;
    setChunkValue(fNodeName, paramString1, j, k);
    setChunkValue(fNodeValue, paramString2, j, k);
    setChunkValue(fNodeURI, paramString3, j, k);
    setChunkIndex(fNodeExtra, m, j, k);
    setChunkValue(fNodeName, paramString4, n, i1);
    return i;
  }
  
  public int createDeferredEntity(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
  {
    int i = createNode((short)6);
    int j = i >> 11;
    int k = i & 0x7FF;
    int m = createNode((short)6);
    int n = m >> 11;
    int i1 = m & 0x7FF;
    setChunkValue(fNodeName, paramString1, j, k);
    setChunkValue(fNodeValue, paramString2, j, k);
    setChunkValue(fNodeURI, paramString3, j, k);
    setChunkIndex(fNodeExtra, m, j, k);
    setChunkValue(fNodeName, paramString4, n, i1);
    setChunkValue(fNodeValue, null, n, i1);
    setChunkValue(fNodeURI, null, n, i1);
    int i2 = createNode((short)6);
    int i3 = i2 >> 11;
    int i4 = i2 & 0x7FF;
    setChunkIndex(fNodeExtra, i2, n, i1);
    setChunkValue(fNodeName, paramString5, i3, i4);
    return i;
  }
  
  public String getDeferredEntityBaseURI(int paramInt)
  {
    if (paramInt != -1)
    {
      int i = getNodeExtra(paramInt, false);
      i = getNodeExtra(i, false);
      return getNodeName(i, false);
    }
    return null;
  }
  
  public void setEntityInfo(int paramInt, String paramString1, String paramString2)
  {
    int i = getNodeExtra(paramInt, false);
    if (i != -1)
    {
      int j = i >> 11;
      int k = i & 0x7FF;
      setChunkValue(fNodeValue, paramString1, j, k);
      setChunkValue(fNodeURI, paramString2, j, k);
    }
  }
  
  public void setTypeInfo(int paramInt, Object paramObject)
  {
    int i = paramInt >> 11;
    int j = paramInt & 0x7FF;
    setChunkValue(fNodeValue, paramObject, i, j);
  }
  
  public void setInputEncoding(int paramInt, String paramString)
  {
    int i = getNodeExtra(paramInt, false);
    int j = getNodeExtra(i, false);
    int k = j >> 11;
    int m = j & 0x7FF;
    setChunkValue(fNodeValue, paramString, k, m);
  }
  
  public int createDeferredEntityReference(String paramString1, String paramString2)
  {
    int i = createNode((short)5);
    int j = i >> 11;
    int k = i & 0x7FF;
    setChunkValue(fNodeName, paramString1, j, k);
    setChunkValue(fNodeValue, paramString2, j, k);
    return i;
  }
  
  /**
   * @deprecated
   */
  public int createDeferredElement(String paramString1, String paramString2, Object paramObject)
  {
    int i = createNode((short)1);
    int j = i >> 11;
    int k = i & 0x7FF;
    setChunkValue(fNodeName, paramString2, j, k);
    setChunkValue(fNodeURI, paramString1, j, k);
    setChunkValue(fNodeValue, paramObject, j, k);
    return i;
  }
  
  /**
   * @deprecated
   */
  public int createDeferredElement(String paramString)
  {
    return createDeferredElement(null, paramString);
  }
  
  public int createDeferredElement(String paramString1, String paramString2)
  {
    int i = createNode((short)1);
    int j = i >> 11;
    int k = i & 0x7FF;
    setChunkValue(fNodeName, paramString2, j, k);
    setChunkValue(fNodeURI, paramString1, j, k);
    return i;
  }
  
  public int setDeferredAttribute(int paramInt, String paramString1, String paramString2, String paramString3, boolean paramBoolean1, boolean paramBoolean2, Object paramObject)
  {
    int i = createDeferredAttribute(paramString1, paramString2, paramString3, paramBoolean1);
    int j = i >> 11;
    int k = i & 0x7FF;
    setChunkIndex(fNodeParent, paramInt, j, k);
    int m = paramInt >> 11;
    int n = paramInt & 0x7FF;
    int i1 = getChunkIndex(fNodeExtra, m, n);
    if (i1 != 0) {
      setChunkIndex(fNodePrevSib, i1, j, k);
    }
    setChunkIndex(fNodeExtra, i, m, n);
    int i2 = getChunkIndex(fNodeExtra, j, k);
    if (paramBoolean2)
    {
      i2 |= 0x200;
      setChunkIndex(fNodeExtra, i2, j, k);
      String str = getChunkValue(fNodeValue, j, k);
      putIdentifier(str, paramInt);
    }
    if (paramObject != null)
    {
      int i3 = createNode((short)20);
      int i4 = i3 >> 11;
      int i5 = i3 & 0x7FF;
      setChunkIndex(fNodeLastChild, i3, j, k);
      setChunkValue(fNodeValue, paramObject, i4, i5);
    }
    return i;
  }
  
  /**
   * @deprecated
   */
  public int setDeferredAttribute(int paramInt, String paramString1, String paramString2, String paramString3, boolean paramBoolean)
  {
    int i = createDeferredAttribute(paramString1, paramString2, paramString3, paramBoolean);
    int j = i >> 11;
    int k = i & 0x7FF;
    setChunkIndex(fNodeParent, paramInt, j, k);
    int m = paramInt >> 11;
    int n = paramInt & 0x7FF;
    int i1 = getChunkIndex(fNodeExtra, m, n);
    if (i1 != 0) {
      setChunkIndex(fNodePrevSib, i1, j, k);
    }
    setChunkIndex(fNodeExtra, i, m, n);
    return i;
  }
  
  public int createDeferredAttribute(String paramString1, String paramString2, boolean paramBoolean)
  {
    return createDeferredAttribute(paramString1, null, paramString2, paramBoolean);
  }
  
  public int createDeferredAttribute(String paramString1, String paramString2, String paramString3, boolean paramBoolean)
  {
    int i = createNode((short)2);
    int j = i >> 11;
    int k = i & 0x7FF;
    setChunkValue(fNodeName, paramString1, j, k);
    setChunkValue(fNodeURI, paramString2, j, k);
    setChunkValue(fNodeValue, paramString3, j, k);
    int m = paramBoolean ? 32 : 0;
    setChunkIndex(fNodeExtra, m, j, k);
    return i;
  }
  
  public int createDeferredElementDefinition(String paramString)
  {
    int i = createNode((short)21);
    int j = i >> 11;
    int k = i & 0x7FF;
    setChunkValue(fNodeName, paramString, j, k);
    return i;
  }
  
  public int createDeferredTextNode(String paramString, boolean paramBoolean)
  {
    int i = createNode((short)3);
    int j = i >> 11;
    int k = i & 0x7FF;
    setChunkValue(fNodeValue, paramString, j, k);
    setChunkIndex(fNodeExtra, paramBoolean ? 1 : 0, j, k);
    return i;
  }
  
  public int createDeferredCDATASection(String paramString)
  {
    int i = createNode((short)4);
    int j = i >> 11;
    int k = i & 0x7FF;
    setChunkValue(fNodeValue, paramString, j, k);
    return i;
  }
  
  public int createDeferredProcessingInstruction(String paramString1, String paramString2)
  {
    int i = createNode((short)7);
    int j = i >> 11;
    int k = i & 0x7FF;
    setChunkValue(fNodeName, paramString1, j, k);
    setChunkValue(fNodeValue, paramString2, j, k);
    return i;
  }
  
  public int createDeferredComment(String paramString)
  {
    int i = createNode((short)8);
    int j = i >> 11;
    int k = i & 0x7FF;
    setChunkValue(fNodeValue, paramString, j, k);
    return i;
  }
  
  public int cloneNode(int paramInt, boolean paramBoolean)
  {
    int i = paramInt >> 11;
    int j = paramInt & 0x7FF;
    int k = fNodeType[i][j];
    int m = createNode((short)k);
    int n = m >> 11;
    int i1 = m & 0x7FF;
    setChunkValue(fNodeName, fNodeName[i][j], n, i1);
    setChunkValue(fNodeValue, fNodeValue[i][j], n, i1);
    setChunkValue(fNodeURI, fNodeURI[i][j], n, i1);
    int i2 = fNodeExtra[i][j];
    if (i2 != -1)
    {
      if ((k != 2) && (k != 3)) {
        i2 = cloneNode(i2, false);
      }
      setChunkIndex(fNodeExtra, i2, n, i1);
    }
    if (paramBoolean)
    {
      int i3 = -1;
      for (int i4 = getLastChild(paramInt, false); i4 != -1; i4 = getRealPrevSibling(i4, false))
      {
        int i5 = cloneNode(i4, paramBoolean);
        insertBefore(m, i5, i3);
        i3 = i5;
      }
    }
    return m;
  }
  
  public void appendChild(int paramInt1, int paramInt2)
  {
    int i = paramInt1 >> 11;
    int j = paramInt1 & 0x7FF;
    int k = paramInt2 >> 11;
    int m = paramInt2 & 0x7FF;
    setChunkIndex(fNodeParent, paramInt1, k, m);
    int n = getChunkIndex(fNodeLastChild, i, j);
    setChunkIndex(fNodePrevSib, n, k, m);
    setChunkIndex(fNodeLastChild, paramInt2, i, j);
  }
  
  public int setAttributeNode(int paramInt1, int paramInt2)
  {
    int i = paramInt1 >> 11;
    int j = paramInt1 & 0x7FF;
    int k = paramInt2 >> 11;
    int m = paramInt2 & 0x7FF;
    String str1 = getChunkValue(fNodeName, k, m);
    int n = getChunkIndex(fNodeExtra, i, j);
    int i1 = -1;
    int i2 = -1;
    int i3 = -1;
    while (n != -1)
    {
      i2 = n >> 11;
      i3 = n & 0x7FF;
      String str2 = getChunkValue(fNodeName, i2, i3);
      if (str2.equals(str1)) {
        break;
      }
      i1 = n;
      n = getChunkIndex(fNodePrevSib, i2, i3);
    }
    if (n != -1)
    {
      i4 = getChunkIndex(fNodePrevSib, i2, i3);
      if (i1 == -1)
      {
        setChunkIndex(fNodeExtra, i4, i, j);
      }
      else
      {
        i5 = i1 >> 11;
        i6 = i1 & 0x7FF;
        setChunkIndex(fNodePrevSib, i4, i5, i6);
      }
      clearChunkIndex(fNodeType, i2, i3);
      clearChunkValue(fNodeName, i2, i3);
      clearChunkValue(fNodeValue, i2, i3);
      clearChunkIndex(fNodeParent, i2, i3);
      clearChunkIndex(fNodePrevSib, i2, i3);
      int i5 = clearChunkIndex(fNodeLastChild, i2, i3);
      int i6 = i5 >> 11;
      int i7 = i5 & 0x7FF;
      clearChunkIndex(fNodeType, i6, i7);
      clearChunkValue(fNodeValue, i6, i7);
      clearChunkIndex(fNodeParent, i6, i7);
      clearChunkIndex(fNodeLastChild, i6, i7);
    }
    int i4 = getChunkIndex(fNodeExtra, i, j);
    setChunkIndex(fNodeExtra, paramInt2, i, j);
    setChunkIndex(fNodePrevSib, i4, k, m);
    return n;
  }
  
  public void setIdAttributeNode(int paramInt1, int paramInt2)
  {
    int i = paramInt2 >> 11;
    int j = paramInt2 & 0x7FF;
    int k = getChunkIndex(fNodeExtra, i, j);
    k |= 0x200;
    setChunkIndex(fNodeExtra, k, i, j);
    String str = getChunkValue(fNodeValue, i, j);
    putIdentifier(str, paramInt1);
  }
  
  public void setIdAttribute(int paramInt)
  {
    int i = paramInt >> 11;
    int j = paramInt & 0x7FF;
    int k = getChunkIndex(fNodeExtra, i, j);
    k |= 0x200;
    setChunkIndex(fNodeExtra, k, i, j);
  }
  
  public int insertBefore(int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt3 == -1)
    {
      appendChild(paramInt1, paramInt2);
      return paramInt2;
    }
    int i = paramInt2 >> 11;
    int j = paramInt2 & 0x7FF;
    int k = paramInt3 >> 11;
    int m = paramInt3 & 0x7FF;
    int n = getChunkIndex(fNodePrevSib, k, m);
    setChunkIndex(fNodePrevSib, paramInt2, k, m);
    setChunkIndex(fNodePrevSib, n, i, j);
    return paramInt2;
  }
  
  public void setAsLastChild(int paramInt1, int paramInt2)
  {
    int i = paramInt1 >> 11;
    int j = paramInt1 & 0x7FF;
    setChunkIndex(fNodeLastChild, paramInt2, i, j);
  }
  
  public int getParentNode(int paramInt)
  {
    return getParentNode(paramInt, false);
  }
  
  public int getParentNode(int paramInt, boolean paramBoolean)
  {
    if (paramInt == -1) {
      return -1;
    }
    int i = paramInt >> 11;
    int j = paramInt & 0x7FF;
    return paramBoolean ? clearChunkIndex(fNodeParent, i, j) : getChunkIndex(fNodeParent, i, j);
  }
  
  public int getLastChild(int paramInt)
  {
    return getLastChild(paramInt, true);
  }
  
  public int getLastChild(int paramInt, boolean paramBoolean)
  {
    if (paramInt == -1) {
      return -1;
    }
    int i = paramInt >> 11;
    int j = paramInt & 0x7FF;
    return paramBoolean ? clearChunkIndex(fNodeLastChild, i, j) : getChunkIndex(fNodeLastChild, i, j);
  }
  
  public int getPrevSibling(int paramInt)
  {
    return getPrevSibling(paramInt, true);
  }
  
  public int getPrevSibling(int paramInt, boolean paramBoolean)
  {
    if (paramInt == -1) {
      return -1;
    }
    int i = paramInt >> 11;
    int j = paramInt & 0x7FF;
    int k = getChunkIndex(fNodeType, i, j);
    if (k == 3) {
      do
      {
        paramInt = getChunkIndex(fNodePrevSib, i, j);
        if (paramInt == -1) {
          break;
        }
        i = paramInt >> 11;
        j = paramInt & 0x7FF;
        k = getChunkIndex(fNodeType, i, j);
      } while (k == 3);
    } else {
      paramInt = getChunkIndex(fNodePrevSib, i, j);
    }
    return paramInt;
  }
  
  public int getRealPrevSibling(int paramInt)
  {
    return getRealPrevSibling(paramInt, true);
  }
  
  public int getRealPrevSibling(int paramInt, boolean paramBoolean)
  {
    if (paramInt == -1) {
      return -1;
    }
    int i = paramInt >> 11;
    int j = paramInt & 0x7FF;
    return paramBoolean ? clearChunkIndex(fNodePrevSib, i, j) : getChunkIndex(fNodePrevSib, i, j);
  }
  
  public int lookupElementDefinition(String paramString)
  {
    if (fNodeCount > 1)
    {
      int i = -1;
      int j = 0;
      int k = 0;
      for (int m = getChunkIndex(fNodeLastChild, j, k); m != -1; m = getChunkIndex(fNodePrevSib, j, k))
      {
        j = m >> 11;
        k = m & 0x7FF;
        if (getChunkIndex(fNodeType, j, k) == 10)
        {
          i = m;
          break;
        }
      }
      if (i == -1) {
        return -1;
      }
      j = i >> 11;
      k = i & 0x7FF;
      for (int n = getChunkIndex(fNodeLastChild, j, k); n != -1; n = getChunkIndex(fNodePrevSib, j, k))
      {
        j = n >> 11;
        k = n & 0x7FF;
        if ((getChunkIndex(fNodeType, j, k) == 21) && (getChunkValue(fNodeName, j, k) == paramString)) {
          return n;
        }
      }
    }
    return -1;
  }
  
  public DeferredNode getNodeObject(int paramInt)
  {
    if (paramInt == -1) {
      return null;
    }
    int i = paramInt >> 11;
    int j = paramInt & 0x7FF;
    int k = getChunkIndex(fNodeType, i, j);
    if ((k != 3) && (k != 4)) {
      clearChunkIndex(fNodeType, i, j);
    }
    Object localObject = null;
    switch (k)
    {
    case 2: 
      if (fNamespacesEnabled) {
        localObject = new DeferredAttrNSImpl(this, paramInt);
      } else {
        localObject = new DeferredAttrImpl(this, paramInt);
      }
      break;
    case 4: 
      localObject = new DeferredCDATASectionImpl(this, paramInt);
      break;
    case 8: 
      localObject = new DeferredCommentImpl(this, paramInt);
      break;
    case 9: 
      localObject = this;
      break;
    case 10: 
      localObject = new DeferredDocumentTypeImpl(this, paramInt);
      docType = ((DocumentTypeImpl)localObject);
      break;
    case 1: 
      if (fNamespacesEnabled) {
        localObject = new DeferredElementNSImpl(this, paramInt);
      } else {
        localObject = new DeferredElementImpl(this, paramInt);
      }
      if (fIdElement != null)
      {
        int m = binarySearch(fIdElement, 0, fIdCount - 1, paramInt);
        while (m != -1)
        {
          String str = fIdName[m];
          if (str != null)
          {
            putIdentifier0(str, (Element)localObject);
            fIdName[m] = null;
          }
          if ((m + 1 < fIdCount) && (fIdElement[(m + 1)] == paramInt)) {
            m++;
          } else {
            m = -1;
          }
        }
      }
      break;
    case 6: 
      localObject = new DeferredEntityImpl(this, paramInt);
      break;
    case 5: 
      localObject = new DeferredEntityReferenceImpl(this, paramInt);
      break;
    case 12: 
      localObject = new DeferredNotationImpl(this, paramInt);
      break;
    case 7: 
      localObject = new DeferredProcessingInstructionImpl(this, paramInt);
      break;
    case 3: 
      localObject = new DeferredTextImpl(this, paramInt);
      break;
    case 21: 
      localObject = new DeferredElementDefinitionImpl(this, paramInt);
      break;
    case 11: 
    case 13: 
    case 14: 
    case 15: 
    case 16: 
    case 17: 
    case 18: 
    case 19: 
    case 20: 
    default: 
      throw new IllegalArgumentException("type: " + k);
    }
    if (localObject != null) {
      return localObject;
    }
    throw new IllegalArgumentException();
  }
  
  public String getNodeName(int paramInt)
  {
    return getNodeName(paramInt, true);
  }
  
  public String getNodeName(int paramInt, boolean paramBoolean)
  {
    if (paramInt == -1) {
      return null;
    }
    int i = paramInt >> 11;
    int j = paramInt & 0x7FF;
    return paramBoolean ? clearChunkValue(fNodeName, i, j) : getChunkValue(fNodeName, i, j);
  }
  
  public String getNodeValueString(int paramInt)
  {
    return getNodeValueString(paramInt, true);
  }
  
  public String getNodeValueString(int paramInt, boolean paramBoolean)
  {
    if (paramInt == -1) {
      return null;
    }
    int i = paramInt >> 11;
    int j = paramInt & 0x7FF;
    String str = paramBoolean ? clearChunkValue(fNodeValue, i, j) : getChunkValue(fNodeValue, i, j);
    if (str == null) {
      return null;
    }
    int k = getChunkIndex(fNodeType, i, j);
    int m;
    int n;
    if (k == 3)
    {
      m = getRealPrevSibling(paramInt);
      if ((m != -1) && (getNodeType(m, false) == 3))
      {
        fStrChunks.add(str);
        do
        {
          i = m >> 11;
          j = m & 0x7FF;
          str = getChunkValue(fNodeValue, i, j);
          fStrChunks.add(str);
          m = getChunkIndex(fNodePrevSib, i, j);
        } while ((m != -1) && (getNodeType(m, false) == 3));
        n = fStrChunks.size();
        for (int i1 = n - 1; i1 >= 0; i1--) {
          fBufferStr.append((String)fStrChunks.get(i1));
        }
        str = fBufferStr.toString();
        fStrChunks.clear();
        fBufferStr.setLength(0);
        return str;
      }
    }
    else if (k == 4)
    {
      m = getLastChild(paramInt, false);
      if (m != -1)
      {
        fBufferStr.append(str);
        while (m != -1)
        {
          i = m >> 11;
          j = m & 0x7FF;
          str = getChunkValue(fNodeValue, i, j);
          fStrChunks.add(str);
          m = getChunkIndex(fNodePrevSib, i, j);
        }
        for (n = fStrChunks.size() - 1; n >= 0; n--) {
          fBufferStr.append((String)fStrChunks.get(n));
        }
        str = fBufferStr.toString();
        fStrChunks.clear();
        fBufferStr.setLength(0);
        return str;
      }
    }
    return str;
  }
  
  public String getNodeValue(int paramInt)
  {
    return getNodeValue(paramInt, true);
  }
  
  public Object getTypeInfo(int paramInt)
  {
    if (paramInt == -1) {
      return null;
    }
    int i = paramInt >> 11;
    int j = paramInt & 0x7FF;
    Object localObject = fNodeValue[i] != null ? fNodeValue[i][j] : null;
    if (localObject != null)
    {
      fNodeValue[i][j] = null;
      RefCount localRefCount = (RefCount)fNodeValue[i]['ࠀ'];
      fCount -= 1;
      if (fCount == 0) {
        fNodeValue[i] = null;
      }
    }
    return localObject;
  }
  
  public String getNodeValue(int paramInt, boolean paramBoolean)
  {
    if (paramInt == -1) {
      return null;
    }
    int i = paramInt >> 11;
    int j = paramInt & 0x7FF;
    return paramBoolean ? clearChunkValue(fNodeValue, i, j) : getChunkValue(fNodeValue, i, j);
  }
  
  public int getNodeExtra(int paramInt)
  {
    return getNodeExtra(paramInt, true);
  }
  
  public int getNodeExtra(int paramInt, boolean paramBoolean)
  {
    if (paramInt == -1) {
      return -1;
    }
    int i = paramInt >> 11;
    int j = paramInt & 0x7FF;
    return paramBoolean ? clearChunkIndex(fNodeExtra, i, j) : getChunkIndex(fNodeExtra, i, j);
  }
  
  public short getNodeType(int paramInt)
  {
    return getNodeType(paramInt, true);
  }
  
  public short getNodeType(int paramInt, boolean paramBoolean)
  {
    if (paramInt == -1) {
      return -1;
    }
    int i = paramInt >> 11;
    int j = paramInt & 0x7FF;
    return paramBoolean ? (short)clearChunkIndex(fNodeType, i, j) : (short)getChunkIndex(fNodeType, i, j);
  }
  
  public String getAttribute(int paramInt, String paramString)
  {
    if ((paramInt == -1) || (paramString == null)) {
      return null;
    }
    int i = paramInt >> 11;
    int j = paramInt & 0x7FF;
    int m;
    int n;
    for (int k = getChunkIndex(fNodeExtra, i, j); k != -1; k = getChunkIndex(fNodePrevSib, m, n))
    {
      m = k >> 11;
      n = k & 0x7FF;
      if (getChunkValue(fNodeName, m, n) == paramString) {
        return getChunkValue(fNodeValue, m, n);
      }
    }
    return null;
  }
  
  public String getNodeURI(int paramInt)
  {
    return getNodeURI(paramInt, true);
  }
  
  public String getNodeURI(int paramInt, boolean paramBoolean)
  {
    if (paramInt == -1) {
      return null;
    }
    int i = paramInt >> 11;
    int j = paramInt & 0x7FF;
    return paramBoolean ? clearChunkValue(fNodeURI, i, j) : getChunkValue(fNodeURI, i, j);
  }
  
  public void putIdentifier(String paramString, int paramInt)
  {
    if (fIdName == null)
    {
      fIdName = new String[64];
      fIdElement = new int[64];
    }
    if (fIdCount == fIdName.length)
    {
      String[] arrayOfString = new String[fIdCount * 2];
      System.arraycopy(fIdName, 0, arrayOfString, 0, fIdCount);
      fIdName = arrayOfString;
      int[] arrayOfInt = new int[arrayOfString.length];
      System.arraycopy(fIdElement, 0, arrayOfInt, 0, fIdCount);
      fIdElement = arrayOfInt;
    }
    fIdName[fIdCount] = paramString;
    fIdElement[fIdCount] = paramInt;
    fIdCount += 1;
  }
  
  public void print() {}
  
  public int getNodeIndex()
  {
    return 0;
  }
  
  protected void synchronizeData()
  {
    needsSyncData(false);
    if (fIdElement != null)
    {
      IntVector localIntVector = new IntVector();
      for (int i = 0; i < fIdCount; i++)
      {
        int j = fIdElement[i];
        String str = fIdName[i];
        if (str != null)
        {
          localIntVector.removeAllElements();
          int k = j;
          do
          {
            localIntVector.addElement(k);
            int m = k >> 11;
            n = k & 0x7FF;
            k = getChunkIndex(fNodeParent, m, n);
          } while (k != -1);
          Object localObject1 = this;
          for (int n = localIntVector.size() - 2; n >= 0; n--)
          {
            k = localIntVector.elementAt(n);
            for (localObject2 = ((Node)localObject1).getLastChild(); localObject2 != null; localObject2 = ((Node)localObject2).getPreviousSibling()) {
              if ((localObject2 instanceof DeferredNode))
              {
                int i1 = ((DeferredNode)localObject2).getNodeIndex();
                if (i1 == k)
                {
                  localObject1 = localObject2;
                  break;
                }
              }
            }
          }
          Object localObject2 = (Element)localObject1;
          putIdentifier0(str, (Element)localObject2);
          fIdName[i] = null;
          while ((i + 1 < fIdCount) && (fIdElement[(i + 1)] == j))
          {
            str = fIdName[(++i)];
            if (str != null) {
              putIdentifier0(str, (Element)localObject2);
            }
          }
        }
      }
    }
  }
  
  protected void synchronizeChildren()
  {
    if (needsSyncData())
    {
      synchronizeData();
      if (!needsSyncChildren()) {
        return;
      }
    }
    boolean bool = mutationEvents;
    mutationEvents = false;
    needsSyncChildren(false);
    getNodeType(0);
    Object localObject1 = null;
    Object localObject2 = null;
    for (int i = getLastChild(0); i != -1; i = getPrevSibling(i))
    {
      ChildNode localChildNode = (ChildNode)getNodeObject(i);
      if (localObject2 == null) {
        localObject2 = localChildNode;
      } else {
        previousSibling = localChildNode;
      }
      ownerNode = this;
      localChildNode.isOwned(true);
      nextSibling = localObject1;
      localObject1 = localChildNode;
      int j = localChildNode.getNodeType();
      if (j == 1) {
        docElement = ((ElementImpl)localChildNode);
      } else if (j == 10) {
        docType = ((DocumentTypeImpl)localChildNode);
      }
    }
    if (localObject1 != null)
    {
      firstChild = localObject1;
      localObject1.isFirstChild(true);
      lastChild(localObject2);
    }
    mutationEvents = bool;
  }
  
  protected final void synchronizeChildren(AttrImpl paramAttrImpl, int paramInt)
  {
    boolean bool = getMutationEvents();
    setMutationEvents(false);
    paramAttrImpl.needsSyncChildren(false);
    int i = getLastChild(paramInt);
    int j = getPrevSibling(i);
    if (j == -1)
    {
      value = getNodeValueString(paramInt);
      paramAttrImpl.hasStringValue(true);
    }
    else
    {
      Object localObject1 = null;
      Object localObject2 = null;
      for (int k = i; k != -1; k = getPrevSibling(k))
      {
        ChildNode localChildNode = (ChildNode)getNodeObject(k);
        if (localObject2 == null) {
          localObject2 = localChildNode;
        } else {
          previousSibling = localChildNode;
        }
        ownerNode = paramAttrImpl;
        localChildNode.isOwned(true);
        nextSibling = localObject1;
        localObject1 = localChildNode;
      }
      if (localObject2 != null)
      {
        value = localObject1;
        localObject1.isFirstChild(true);
        paramAttrImpl.lastChild(localObject2);
      }
      paramAttrImpl.hasStringValue(false);
    }
    setMutationEvents(bool);
  }
  
  protected final void synchronizeChildren(ParentNode paramParentNode, int paramInt)
  {
    boolean bool = getMutationEvents();
    setMutationEvents(false);
    paramParentNode.needsSyncChildren(false);
    Object localObject1 = null;
    Object localObject2 = null;
    for (int i = getLastChild(paramInt); i != -1; i = getPrevSibling(i))
    {
      ChildNode localChildNode = (ChildNode)getNodeObject(i);
      if (localObject2 == null) {
        localObject2 = localChildNode;
      } else {
        previousSibling = localChildNode;
      }
      ownerNode = paramParentNode;
      localChildNode.isOwned(true);
      nextSibling = localObject1;
      localObject1 = localChildNode;
    }
    if (localObject2 != null)
    {
      firstChild = localObject1;
      localObject1.isFirstChild(true);
      paramParentNode.lastChild(localObject2);
    }
    setMutationEvents(bool);
  }
  
  protected void ensureCapacity(int paramInt)
  {
    if (fNodeType == null)
    {
      fNodeType = new int[32][];
      fNodeName = new Object[32][];
      fNodeValue = new Object[32][];
      fNodeParent = new int[32][];
      fNodeLastChild = new int[32][];
      fNodePrevSib = new int[32][];
      fNodeURI = new Object[32][];
      fNodeExtra = new int[32][];
    }
    else if (fNodeType.length <= paramInt)
    {
      int i = paramInt * 2;
      int[][] arrayOfInt = new int[i][];
      System.arraycopy(fNodeType, 0, arrayOfInt, 0, paramInt);
      fNodeType = arrayOfInt;
      Object[][] arrayOfObject; = new Object[i][];
      System.arraycopy(fNodeName, 0, arrayOfObject;, 0, paramInt);
      fNodeName = arrayOfObject;;
      arrayOfObject; = new Object[i][];
      System.arraycopy(fNodeValue, 0, arrayOfObject;, 0, paramInt);
      fNodeValue = arrayOfObject;;
      arrayOfInt = new int[i][];
      System.arraycopy(fNodeParent, 0, arrayOfInt, 0, paramInt);
      fNodeParent = arrayOfInt;
      arrayOfInt = new int[i][];
      System.arraycopy(fNodeLastChild, 0, arrayOfInt, 0, paramInt);
      fNodeLastChild = arrayOfInt;
      arrayOfInt = new int[i][];
      System.arraycopy(fNodePrevSib, 0, arrayOfInt, 0, paramInt);
      fNodePrevSib = arrayOfInt;
      arrayOfObject; = new Object[i][];
      System.arraycopy(fNodeURI, 0, arrayOfObject;, 0, paramInt);
      fNodeURI = arrayOfObject;;
      arrayOfInt = new int[i][];
      System.arraycopy(fNodeExtra, 0, arrayOfInt, 0, paramInt);
      fNodeExtra = arrayOfInt;
    }
    else if (fNodeType[paramInt] != null)
    {
      return;
    }
    createChunk(fNodeType, paramInt);
    createChunk(fNodeName, paramInt);
    createChunk(fNodeValue, paramInt);
    createChunk(fNodeParent, paramInt);
    createChunk(fNodeLastChild, paramInt);
    createChunk(fNodePrevSib, paramInt);
    createChunk(fNodeURI, paramInt);
    createChunk(fNodeExtra, paramInt);
  }
  
  protected int createNode(short paramShort)
  {
    int i = fNodeCount >> 11;
    int j = fNodeCount & 0x7FF;
    ensureCapacity(i);
    setChunkIndex(fNodeType, paramShort, i, j);
    return fNodeCount++;
  }
  
  protected static int binarySearch(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3)
  {
    while (paramInt1 <= paramInt2)
    {
      int i = paramInt1 + paramInt2 >>> 1;
      int j = paramArrayOfInt[i];
      if (j == paramInt3)
      {
        while ((i > 0) && (paramArrayOfInt[(i - 1)] == paramInt3)) {
          i--;
        }
        return i;
      }
      if (j > paramInt3) {
        paramInt2 = i - 1;
      } else {
        paramInt1 = i + 1;
      }
    }
    return -1;
  }
  
  private final void createChunk(int[][] paramArrayOfInt, int paramInt)
  {
    paramArrayOfInt[paramInt] = new int['ࠁ'];
    System.arraycopy(INIT_ARRAY, 0, paramArrayOfInt[paramInt], 0, 2048);
  }
  
  private final void createChunk(Object[][] paramArrayOfObject, int paramInt)
  {
    paramArrayOfObject[paramInt] = new Object['ࠁ'];
    paramArrayOfObject[paramInt]['ࠀ'] = new RefCount();
  }
  
  private final int setChunkIndex(int[][] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt1 == -1) {
      return clearChunkIndex(paramArrayOfInt, paramInt2, paramInt3);
    }
    int[] arrayOfInt = paramArrayOfInt[paramInt2];
    if (arrayOfInt == null)
    {
      createChunk(paramArrayOfInt, paramInt2);
      arrayOfInt = paramArrayOfInt[paramInt2];
    }
    int i = arrayOfInt[paramInt3];
    if (i == -1) {
      arrayOfInt['ࠀ'] += 1;
    }
    arrayOfInt[paramInt3] = paramInt1;
    return i;
  }
  
  private final String setChunkValue(Object[][] paramArrayOfObject, Object paramObject, int paramInt1, int paramInt2)
  {
    if (paramObject == null) {
      return clearChunkValue(paramArrayOfObject, paramInt1, paramInt2);
    }
    Object[] arrayOfObject = paramArrayOfObject[paramInt1];
    if (arrayOfObject == null)
    {
      createChunk(paramArrayOfObject, paramInt1);
      arrayOfObject = paramArrayOfObject[paramInt1];
    }
    String str = (String)arrayOfObject[paramInt2];
    if (str == null)
    {
      RefCount localRefCount = (RefCount)arrayOfObject['ࠀ'];
      fCount += 1;
    }
    arrayOfObject[paramInt2] = paramObject;
    return str;
  }
  
  private final int getChunkIndex(int[][] paramArrayOfInt, int paramInt1, int paramInt2)
  {
    return paramArrayOfInt[paramInt1] != null ? paramArrayOfInt[paramInt1][paramInt2] : -1;
  }
  
  private final String getChunkValue(Object[][] paramArrayOfObject, int paramInt1, int paramInt2)
  {
    return paramArrayOfObject[paramInt1] != null ? (String)paramArrayOfObject[paramInt1][paramInt2] : null;
  }
  
  private final String getNodeValue(int paramInt1, int paramInt2)
  {
    Object localObject = fNodeValue[paramInt1][paramInt2];
    if (localObject == null) {
      return null;
    }
    if ((localObject instanceof String)) {
      return (String)localObject;
    }
    return localObject.toString();
  }
  
  private final int clearChunkIndex(int[][] paramArrayOfInt, int paramInt1, int paramInt2)
  {
    int i = paramArrayOfInt[paramInt1] != null ? paramArrayOfInt[paramInt1][paramInt2] : -1;
    if (i != -1)
    {
      paramArrayOfInt[paramInt1]['ࠀ'] -= 1;
      paramArrayOfInt[paramInt1][paramInt2] = -1;
      if (paramArrayOfInt[paramInt1]['ࠀ'] == 0) {
        paramArrayOfInt[paramInt1] = null;
      }
    }
    return i;
  }
  
  private final String clearChunkValue(Object[][] paramArrayOfObject, int paramInt1, int paramInt2)
  {
    String str = paramArrayOfObject[paramInt1] != null ? (String)paramArrayOfObject[paramInt1][paramInt2] : null;
    if (str != null)
    {
      paramArrayOfObject[paramInt1][paramInt2] = null;
      RefCount localRefCount = (RefCount)paramArrayOfObject[paramInt1]['ࠀ'];
      fCount -= 1;
      if (fCount == 0) {
        paramArrayOfObject[paramInt1] = null;
      }
    }
    return str;
  }
  
  private final void putIdentifier0(String paramString, Element paramElement)
  {
    if (identifiers == null) {
      identifiers = new Hashtable();
    }
    identifiers.put(paramString, paramElement);
  }
  
  private static void print(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {}
  
  static
  {
    for (int i = 0; i < 2048; i++) {
      INIT_ARRAY[i] = -1;
    }
  }
  
  static final class IntVector
  {
    private int[] data;
    private int size;
    
    IntVector() {}
    
    public int size()
    {
      return size;
    }
    
    public int elementAt(int paramInt)
    {
      return data[paramInt];
    }
    
    public void addElement(int paramInt)
    {
      ensureCapacity(size + 1);
      data[(size++)] = paramInt;
    }
    
    public void removeAllElements()
    {
      size = 0;
    }
    
    private void ensureCapacity(int paramInt)
    {
      if (data == null)
      {
        data = new int[paramInt + 15];
      }
      else if (paramInt > data.length)
      {
        int[] arrayOfInt = new int[paramInt + 15];
        System.arraycopy(data, 0, arrayOfInt, 0, data.length);
        data = arrayOfInt;
      }
    }
  }
  
  static final class RefCount
  {
    int fCount;
    
    RefCount() {}
  }
}
