package org.apache.xerces.dom;

import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class TextImpl
  extends CharacterDataImpl
  implements CharacterData, Text
{
  static final long serialVersionUID = -5294980852957403469L;
  
  public TextImpl() {}
  
  public TextImpl(CoreDocumentImpl paramCoreDocumentImpl, String paramString)
  {
    super(paramCoreDocumentImpl, paramString);
  }
  
  public void setValues(CoreDocumentImpl paramCoreDocumentImpl, String paramString)
  {
    flags = 0;
    nextSibling = null;
    previousSibling = null;
    setOwnerDocument(paramCoreDocumentImpl);
    data = paramString;
  }
  
  public short getNodeType()
  {
    return 3;
  }
  
  public String getNodeName()
  {
    return "#text";
  }
  
  public void setIgnorableWhitespace(boolean paramBoolean)
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    isIgnorableWhitespace(paramBoolean);
  }
  
  public boolean isElementContentWhitespace()
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    return internalIsIgnorableWhitespace();
  }
  
  public String getWholeText()
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    StringBuffer localStringBuffer = new StringBuffer();
    if ((data != null) && (data.length() != 0)) {
      localStringBuffer.append(data);
    }
    getWholeTextBackward(getPreviousSibling(), localStringBuffer, getParentNode());
    String str = localStringBuffer.toString();
    localStringBuffer.setLength(0);
    getWholeTextForward(getNextSibling(), localStringBuffer, getParentNode());
    return str + localStringBuffer.toString();
  }
  
  protected void insertTextContent(StringBuffer paramStringBuffer)
    throws DOMException
  {
    String str = getNodeValue();
    if (str != null) {
      paramStringBuffer.insert(0, str);
    }
  }
  
  private boolean getWholeTextForward(Node paramNode1, StringBuffer paramStringBuffer, Node paramNode2)
  {
    int i = 0;
    if (paramNode2 != null) {
      i = paramNode2.getNodeType() == 5 ? 1 : 0;
    }
    while (paramNode1 != null)
    {
      int j = paramNode1.getNodeType();
      if (j == 5)
      {
        if (getWholeTextForward(paramNode1.getFirstChild(), paramStringBuffer, paramNode1)) {
          return true;
        }
      }
      else if ((j == 3) || (j == 4)) {
        ((NodeImpl)paramNode1).getTextContent(paramStringBuffer);
      } else {
        return true;
      }
      paramNode1 = paramNode1.getNextSibling();
    }
    if (i != 0)
    {
      getWholeTextForward(paramNode2.getNextSibling(), paramStringBuffer, paramNode2.getParentNode());
      return true;
    }
    return false;
  }
  
  private boolean getWholeTextBackward(Node paramNode1, StringBuffer paramStringBuffer, Node paramNode2)
  {
    int i = 0;
    if (paramNode2 != null) {
      i = paramNode2.getNodeType() == 5 ? 1 : 0;
    }
    while (paramNode1 != null)
    {
      int j = paramNode1.getNodeType();
      if (j == 5)
      {
        if (getWholeTextBackward(paramNode1.getLastChild(), paramStringBuffer, paramNode1)) {
          return true;
        }
      }
      else if ((j == 3) || (j == 4)) {
        ((TextImpl)paramNode1).insertTextContent(paramStringBuffer);
      } else {
        return true;
      }
      paramNode1 = paramNode1.getPreviousSibling();
    }
    if (i != 0)
    {
      getWholeTextBackward(paramNode2.getPreviousSibling(), paramStringBuffer, paramNode2.getParentNode());
      return true;
    }
    return false;
  }
  
  public Text replaceWholeText(String paramString)
    throws DOMException
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    Node localNode = getParentNode();
    if ((paramString == null) || (paramString.length() == 0))
    {
      if (localNode != null) {
        localNode.removeChild(this);
      }
      return null;
    }
    if (ownerDocumenterrorChecking)
    {
      if (!canModifyPrev(this)) {
        throw new DOMException((short)7, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null));
      }
      if (!canModifyNext(this)) {
        throw new DOMException((short)7, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null));
      }
    }
    Object localObject1 = null;
    if (isReadOnly())
    {
      localObject2 = ownerDocument().createTextNode(paramString);
      if (localNode != null)
      {
        localNode.insertBefore((Node)localObject2, this);
        localNode.removeChild(this);
        localObject1 = localObject2;
      }
      else
      {
        return localObject2;
      }
    }
    else
    {
      setData(paramString);
      localObject1 = this;
    }
    for (Object localObject2 = ((Text)localObject1).getPreviousSibling(); localObject2 != null; localObject2 = ((Node)localObject2).getPreviousSibling())
    {
      if ((((Node)localObject2).getNodeType() != 3) && (((Node)localObject2).getNodeType() != 4) && ((((Node)localObject2).getNodeType() != 5) || (!hasTextOnlyChildren((Node)localObject2)))) {
        break;
      }
      localNode.removeChild((Node)localObject2);
      localObject2 = localObject1;
    }
    for (Object localObject3 = ((Text)localObject1).getNextSibling(); localObject3 != null; localObject3 = ((Node)localObject3).getNextSibling())
    {
      if ((((Node)localObject3).getNodeType() != 3) && (((Node)localObject3).getNodeType() != 4) && ((((Node)localObject3).getNodeType() != 5) || (!hasTextOnlyChildren((Node)localObject3)))) {
        break;
      }
      localNode.removeChild((Node)localObject3);
      localObject3 = localObject1;
    }
    return localObject1;
  }
  
  private boolean canModifyPrev(Node paramNode)
  {
    int i = 0;
    for (Node localNode1 = paramNode.getPreviousSibling(); localNode1 != null; localNode1 = localNode1.getPreviousSibling())
    {
      int j = localNode1.getNodeType();
      if (j == 5)
      {
        Node localNode2 = localNode1.getLastChild();
        if (localNode2 == null) {
          return false;
        }
        while (localNode2 != null)
        {
          int k = localNode2.getNodeType();
          if ((k == 3) || (k == 4))
          {
            i = 1;
          }
          else if (k == 5)
          {
            if (!canModifyPrev(localNode2)) {
              return false;
            }
            i = 1;
          }
          else
          {
            return i == 0;
          }
          localNode2 = localNode2.getPreviousSibling();
        }
      }
      else if ((j != 3) && (j != 4))
      {
        return true;
      }
    }
    return true;
  }
  
  private boolean canModifyNext(Node paramNode)
  {
    int i = 0;
    for (Node localNode1 = paramNode.getNextSibling(); localNode1 != null; localNode1 = localNode1.getNextSibling())
    {
      int j = localNode1.getNodeType();
      if (j == 5)
      {
        Node localNode2 = localNode1.getFirstChild();
        if (localNode2 == null) {
          return false;
        }
        while (localNode2 != null)
        {
          int k = localNode2.getNodeType();
          if ((k == 3) || (k == 4))
          {
            i = 1;
          }
          else if (k == 5)
          {
            if (!canModifyNext(localNode2)) {
              return false;
            }
            i = 1;
          }
          else
          {
            return i == 0;
          }
          localNode2 = localNode2.getNextSibling();
        }
      }
      else if ((j != 3) && (j != 4))
      {
        return true;
      }
    }
    return true;
  }
  
  private boolean hasTextOnlyChildren(Node paramNode)
  {
    Node localNode = paramNode;
    if (localNode == null) {
      return false;
    }
    for (localNode = localNode.getFirstChild(); localNode != null; localNode = localNode.getNextSibling())
    {
      int i = localNode.getNodeType();
      if (i == 5) {
        return hasTextOnlyChildren(localNode);
      }
      if ((i != 3) && (i != 4) && (i != 5)) {
        return false;
      }
    }
    return true;
  }
  
  public boolean isIgnorableWhitespace()
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    return internalIsIgnorableWhitespace();
  }
  
  public Text splitText(int paramInt)
    throws DOMException
  {
    if (isReadOnly()) {
      throw new DOMException((short)7, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null));
    }
    if (needsSyncData()) {
      synchronizeData();
    }
    if ((paramInt < 0) || (paramInt > data.length())) {
      throw new DOMException((short)1, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INDEX_SIZE_ERR", null));
    }
    Text localText = getOwnerDocument().createTextNode(data.substring(paramInt));
    setNodeValue(data.substring(0, paramInt));
    Node localNode = getParentNode();
    if (localNode != null) {
      localNode.insertBefore(localText, nextSibling);
    }
    return localText;
  }
  
  public void replaceData(String paramString)
  {
    data = paramString;
  }
  
  public String removeData()
  {
    String str = data;
    data = "";
    return str;
  }
}
