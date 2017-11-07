package org.apache.xerces.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class CharacterDataImpl
  extends ChildNode
{
  static final long serialVersionUID = 7931170150428474230L;
  protected String data;
  private static transient NodeList singletonNodeList = new NodeList()
  {
    public Node item(int paramAnonymousInt)
    {
      return null;
    }
    
    public int getLength()
    {
      return 0;
    }
  };
  
  public CharacterDataImpl() {}
  
  protected CharacterDataImpl(CoreDocumentImpl paramCoreDocumentImpl, String paramString)
  {
    super(paramCoreDocumentImpl);
    data = paramString;
  }
  
  public NodeList getChildNodes()
  {
    return singletonNodeList;
  }
  
  public String getNodeValue()
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    return data;
  }
  
  protected void setNodeValueInternal(String paramString)
  {
    setNodeValueInternal(paramString, false);
  }
  
  protected void setNodeValueInternal(String paramString, boolean paramBoolean)
  {
    CoreDocumentImpl localCoreDocumentImpl = ownerDocument();
    if ((errorChecking) && (isReadOnly()))
    {
      str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
      throw new DOMException((short)7, str);
    }
    if (needsSyncData()) {
      synchronizeData();
    }
    String str = data;
    localCoreDocumentImpl.modifyingCharacterData(this, paramBoolean);
    data = paramString;
    localCoreDocumentImpl.modifiedCharacterData(this, str, paramString, paramBoolean);
  }
  
  public void setNodeValue(String paramString)
  {
    setNodeValueInternal(paramString);
    ownerDocument().replacedText(this);
  }
  
  public String getData()
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    return data;
  }
  
  public int getLength()
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    return data.length();
  }
  
  public void appendData(String paramString)
  {
    if (isReadOnly())
    {
      String str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
      throw new DOMException((short)7, str);
    }
    if (paramString == null) {
      return;
    }
    if (needsSyncData()) {
      synchronizeData();
    }
    setNodeValue(data + paramString);
  }
  
  public void deleteData(int paramInt1, int paramInt2)
    throws DOMException
  {
    internalDeleteData(paramInt1, paramInt2, false);
  }
  
  void internalDeleteData(int paramInt1, int paramInt2, boolean paramBoolean)
    throws DOMException
  {
    CoreDocumentImpl localCoreDocumentImpl = ownerDocument();
    if (errorChecking)
    {
      String str1;
      if (isReadOnly())
      {
        str1 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
        throw new DOMException((short)7, str1);
      }
      if (paramInt2 < 0)
      {
        str1 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INDEX_SIZE_ERR", null);
        throw new DOMException((short)1, str1);
      }
    }
    if (needsSyncData()) {
      synchronizeData();
    }
    int i = Math.max(data.length() - paramInt2 - paramInt1, 0);
    try
    {
      String str2 = data.substring(0, paramInt1) + (i > 0 ? data.substring(paramInt1 + paramInt2, paramInt1 + paramInt2 + i) : "");
      setNodeValueInternal(str2, paramBoolean);
      localCoreDocumentImpl.deletedText(this, paramInt1, paramInt2);
    }
    catch (StringIndexOutOfBoundsException localStringIndexOutOfBoundsException)
    {
      String str3 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INDEX_SIZE_ERR", null);
      throw new DOMException((short)1, str3);
    }
  }
  
  public void insertData(int paramInt, String paramString)
    throws DOMException
  {
    internalInsertData(paramInt, paramString, false);
  }
  
  void internalInsertData(int paramInt, String paramString, boolean paramBoolean)
    throws DOMException
  {
    CoreDocumentImpl localCoreDocumentImpl = ownerDocument();
    String str1;
    if ((errorChecking) && (isReadOnly()))
    {
      str1 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
      throw new DOMException((short)7, str1);
    }
    if (needsSyncData()) {
      synchronizeData();
    }
    try
    {
      str1 = new StringBuffer(data).insert(paramInt, paramString).toString();
      setNodeValueInternal(str1, paramBoolean);
      localCoreDocumentImpl.insertedText(this, paramInt, paramString.length());
    }
    catch (StringIndexOutOfBoundsException localStringIndexOutOfBoundsException)
    {
      String str2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INDEX_SIZE_ERR", null);
      throw new DOMException((short)1, str2);
    }
  }
  
  public void replaceData(int paramInt1, int paramInt2, String paramString)
    throws DOMException
  {
    CoreDocumentImpl localCoreDocumentImpl = ownerDocument();
    if ((errorChecking) && (isReadOnly()))
    {
      str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
      throw new DOMException((short)7, str);
    }
    if (needsSyncData()) {
      synchronizeData();
    }
    localCoreDocumentImpl.replacingData(this);
    String str = data;
    internalDeleteData(paramInt1, paramInt2, true);
    internalInsertData(paramInt1, paramString, true);
    localCoreDocumentImpl.replacedCharacterData(this, str, data);
  }
  
  public void setData(String paramString)
    throws DOMException
  {
    setNodeValue(paramString);
  }
  
  public String substringData(int paramInt1, int paramInt2)
    throws DOMException
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    int i = data.length();
    if ((paramInt2 < 0) || (paramInt1 < 0) || (paramInt1 > i - 1))
    {
      String str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INDEX_SIZE_ERR", null);
      throw new DOMException((short)1, str);
    }
    int j = Math.min(paramInt1 + paramInt2, i);
    return data.substring(paramInt1, j);
  }
}
