package org.apache.xerces.util;

import java.lang.reflect.Method;
import java.util.Hashtable;
import org.apache.xerces.dom.AttrImpl;
import org.apache.xerces.dom.DocumentImpl;
import org.apache.xerces.impl.xs.opti.ElementImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ls.LSException;

public class DOMUtil
{
  protected DOMUtil() {}
  
  public static void copyInto(Node paramNode1, Node paramNode2)
    throws DOMException
  {
    Document localDocument = paramNode2.getOwnerDocument();
    boolean bool = localDocument instanceof DocumentImpl;
    Node localNode1 = paramNode1;
    Object localObject1 = paramNode1;
    Node localNode2 = paramNode1;
    while (localNode2 != null)
    {
      Object localObject2 = null;
      int i = localNode2.getNodeType();
      switch (i)
      {
      case 4: 
        localObject2 = localDocument.createCDATASection(localNode2.getNodeValue());
        break;
      case 8: 
        localObject2 = localDocument.createComment(localNode2.getNodeValue());
        break;
      case 1: 
        Element localElement = localDocument.createElement(localNode2.getNodeName());
        localObject2 = localElement;
        NamedNodeMap localNamedNodeMap = localNode2.getAttributes();
        int j = localNamedNodeMap.getLength();
        for (int k = 0; k < j; k++)
        {
          Attr localAttr = (Attr)localNamedNodeMap.item(k);
          String str1 = localAttr.getNodeName();
          String str2 = localAttr.getNodeValue();
          localElement.setAttribute(str1, str2);
          if ((bool) && (!localAttr.getSpecified())) {
            ((AttrImpl)localElement.getAttributeNode(str1)).setSpecified(false);
          }
        }
        break;
      case 5: 
        localObject2 = localDocument.createEntityReference(localNode2.getNodeName());
        break;
      case 7: 
        localObject2 = localDocument.createProcessingInstruction(localNode2.getNodeName(), localNode2.getNodeValue());
        break;
      case 3: 
        localObject2 = localDocument.createTextNode(localNode2.getNodeValue());
        break;
      case 2: 
      case 6: 
      default: 
        throw new IllegalArgumentException("can't copy node type, " + i + " (" + localNode2.getNodeName() + ')');
      }
      paramNode2.appendChild((Node)localObject2);
      if (localNode2.hasChildNodes())
      {
        localObject1 = localNode2;
        localNode2 = localNode2.getFirstChild();
        paramNode2 = (Node)localObject2;
      }
      else
      {
        localNode2 = localNode2.getNextSibling();
        while ((localNode2 == null) && (localObject1 != localNode1))
        {
          localNode2 = ((Node)localObject1).getNextSibling();
          localObject1 = ((Node)localObject1).getParentNode();
          paramNode2 = paramNode2.getParentNode();
        }
      }
    }
  }
  
  public static Element getFirstChildElement(Node paramNode)
  {
    for (Node localNode = paramNode.getFirstChild(); localNode != null; localNode = localNode.getNextSibling()) {
      if (localNode.getNodeType() == 1) {
        return (Element)localNode;
      }
    }
    return null;
  }
  
  public static Element getFirstVisibleChildElement(Node paramNode)
  {
    for (Node localNode = paramNode.getFirstChild(); localNode != null; localNode = localNode.getNextSibling()) {
      if ((localNode.getNodeType() == 1) && (!isHidden(localNode))) {
        return (Element)localNode;
      }
    }
    return null;
  }
  
  public static Element getFirstVisibleChildElement(Node paramNode, Hashtable paramHashtable)
  {
    for (Node localNode = paramNode.getFirstChild(); localNode != null; localNode = localNode.getNextSibling()) {
      if ((localNode.getNodeType() == 1) && (!isHidden(localNode, paramHashtable))) {
        return (Element)localNode;
      }
    }
    return null;
  }
  
  public static Element getLastChildElement(Node paramNode)
  {
    for (Node localNode = paramNode.getLastChild(); localNode != null; localNode = localNode.getPreviousSibling()) {
      if (localNode.getNodeType() == 1) {
        return (Element)localNode;
      }
    }
    return null;
  }
  
  public static Element getLastVisibleChildElement(Node paramNode)
  {
    for (Node localNode = paramNode.getLastChild(); localNode != null; localNode = localNode.getPreviousSibling()) {
      if ((localNode.getNodeType() == 1) && (!isHidden(localNode))) {
        return (Element)localNode;
      }
    }
    return null;
  }
  
  public static Element getLastVisibleChildElement(Node paramNode, Hashtable paramHashtable)
  {
    for (Node localNode = paramNode.getLastChild(); localNode != null; localNode = localNode.getPreviousSibling()) {
      if ((localNode.getNodeType() == 1) && (!isHidden(localNode, paramHashtable))) {
        return (Element)localNode;
      }
    }
    return null;
  }
  
  public static Element getNextSiblingElement(Node paramNode)
  {
    for (Node localNode = paramNode.getNextSibling(); localNode != null; localNode = localNode.getNextSibling()) {
      if (localNode.getNodeType() == 1) {
        return (Element)localNode;
      }
    }
    return null;
  }
  
  public static Element getNextVisibleSiblingElement(Node paramNode)
  {
    for (Node localNode = paramNode.getNextSibling(); localNode != null; localNode = localNode.getNextSibling()) {
      if ((localNode.getNodeType() == 1) && (!isHidden(localNode))) {
        return (Element)localNode;
      }
    }
    return null;
  }
  
  public static Element getNextVisibleSiblingElement(Node paramNode, Hashtable paramHashtable)
  {
    for (Node localNode = paramNode.getNextSibling(); localNode != null; localNode = localNode.getNextSibling()) {
      if ((localNode.getNodeType() == 1) && (!isHidden(localNode, paramHashtable))) {
        return (Element)localNode;
      }
    }
    return null;
  }
  
  public static void setHidden(Node paramNode)
  {
    if ((paramNode instanceof org.apache.xerces.impl.xs.opti.NodeImpl)) {
      ((org.apache.xerces.impl.xs.opti.NodeImpl)paramNode).setReadOnly(true, false);
    } else if ((paramNode instanceof org.apache.xerces.dom.NodeImpl)) {
      ((org.apache.xerces.dom.NodeImpl)paramNode).setReadOnly(true, false);
    }
  }
  
  public static void setHidden(Node paramNode, Hashtable paramHashtable)
  {
    if ((paramNode instanceof org.apache.xerces.impl.xs.opti.NodeImpl)) {
      ((org.apache.xerces.impl.xs.opti.NodeImpl)paramNode).setReadOnly(true, false);
    } else {
      paramHashtable.put(paramNode, "");
    }
  }
  
  public static void setVisible(Node paramNode)
  {
    if ((paramNode instanceof org.apache.xerces.impl.xs.opti.NodeImpl)) {
      ((org.apache.xerces.impl.xs.opti.NodeImpl)paramNode).setReadOnly(false, false);
    } else if ((paramNode instanceof org.apache.xerces.dom.NodeImpl)) {
      ((org.apache.xerces.dom.NodeImpl)paramNode).setReadOnly(false, false);
    }
  }
  
  public static void setVisible(Node paramNode, Hashtable paramHashtable)
  {
    if ((paramNode instanceof org.apache.xerces.impl.xs.opti.NodeImpl)) {
      ((org.apache.xerces.impl.xs.opti.NodeImpl)paramNode).setReadOnly(false, false);
    } else {
      paramHashtable.remove(paramNode);
    }
  }
  
  public static boolean isHidden(Node paramNode)
  {
    if ((paramNode instanceof org.apache.xerces.impl.xs.opti.NodeImpl)) {
      return ((org.apache.xerces.impl.xs.opti.NodeImpl)paramNode).getReadOnly();
    }
    if ((paramNode instanceof org.apache.xerces.dom.NodeImpl)) {
      return ((org.apache.xerces.dom.NodeImpl)paramNode).getReadOnly();
    }
    return false;
  }
  
  public static boolean isHidden(Node paramNode, Hashtable paramHashtable)
  {
    if ((paramNode instanceof org.apache.xerces.impl.xs.opti.NodeImpl)) {
      return ((org.apache.xerces.impl.xs.opti.NodeImpl)paramNode).getReadOnly();
    }
    return paramHashtable.containsKey(paramNode);
  }
  
  public static Element getFirstChildElement(Node paramNode, String paramString)
  {
    for (Node localNode = paramNode.getFirstChild(); localNode != null; localNode = localNode.getNextSibling()) {
      if ((localNode.getNodeType() == 1) && (localNode.getNodeName().equals(paramString))) {
        return (Element)localNode;
      }
    }
    return null;
  }
  
  public static Element getLastChildElement(Node paramNode, String paramString)
  {
    for (Node localNode = paramNode.getLastChild(); localNode != null; localNode = localNode.getPreviousSibling()) {
      if ((localNode.getNodeType() == 1) && (localNode.getNodeName().equals(paramString))) {
        return (Element)localNode;
      }
    }
    return null;
  }
  
  public static Element getNextSiblingElement(Node paramNode, String paramString)
  {
    for (Node localNode = paramNode.getNextSibling(); localNode != null; localNode = localNode.getNextSibling()) {
      if ((localNode.getNodeType() == 1) && (localNode.getNodeName().equals(paramString))) {
        return (Element)localNode;
      }
    }
    return null;
  }
  
  public static Element getFirstChildElementNS(Node paramNode, String paramString1, String paramString2)
  {
    for (Node localNode = paramNode.getFirstChild(); localNode != null; localNode = localNode.getNextSibling()) {
      if (localNode.getNodeType() == 1)
      {
        String str = localNode.getNamespaceURI();
        if ((str != null) && (str.equals(paramString1)) && (localNode.getLocalName().equals(paramString2))) {
          return (Element)localNode;
        }
      }
    }
    return null;
  }
  
  public static Element getLastChildElementNS(Node paramNode, String paramString1, String paramString2)
  {
    for (Node localNode = paramNode.getLastChild(); localNode != null; localNode = localNode.getPreviousSibling()) {
      if (localNode.getNodeType() == 1)
      {
        String str = localNode.getNamespaceURI();
        if ((str != null) && (str.equals(paramString1)) && (localNode.getLocalName().equals(paramString2))) {
          return (Element)localNode;
        }
      }
    }
    return null;
  }
  
  public static Element getNextSiblingElementNS(Node paramNode, String paramString1, String paramString2)
  {
    for (Node localNode = paramNode.getNextSibling(); localNode != null; localNode = localNode.getNextSibling()) {
      if (localNode.getNodeType() == 1)
      {
        String str = localNode.getNamespaceURI();
        if ((str != null) && (str.equals(paramString1)) && (localNode.getLocalName().equals(paramString2))) {
          return (Element)localNode;
        }
      }
    }
    return null;
  }
  
  public static Element getFirstChildElement(Node paramNode, String[] paramArrayOfString)
  {
    for (Node localNode = paramNode.getFirstChild(); localNode != null; localNode = localNode.getNextSibling()) {
      if (localNode.getNodeType() == 1) {
        for (int i = 0; i < paramArrayOfString.length; i++) {
          if (localNode.getNodeName().equals(paramArrayOfString[i])) {
            return (Element)localNode;
          }
        }
      }
    }
    return null;
  }
  
  public static Element getLastChildElement(Node paramNode, String[] paramArrayOfString)
  {
    for (Node localNode = paramNode.getLastChild(); localNode != null; localNode = localNode.getPreviousSibling()) {
      if (localNode.getNodeType() == 1) {
        for (int i = 0; i < paramArrayOfString.length; i++) {
          if (localNode.getNodeName().equals(paramArrayOfString[i])) {
            return (Element)localNode;
          }
        }
      }
    }
    return null;
  }
  
  public static Element getNextSiblingElement(Node paramNode, String[] paramArrayOfString)
  {
    for (Node localNode = paramNode.getNextSibling(); localNode != null; localNode = localNode.getNextSibling()) {
      if (localNode.getNodeType() == 1) {
        for (int i = 0; i < paramArrayOfString.length; i++) {
          if (localNode.getNodeName().equals(paramArrayOfString[i])) {
            return (Element)localNode;
          }
        }
      }
    }
    return null;
  }
  
  public static Element getFirstChildElementNS(Node paramNode, String[][] paramArrayOfString)
  {
    for (Node localNode = paramNode.getFirstChild(); localNode != null; localNode = localNode.getNextSibling()) {
      if (localNode.getNodeType() == 1) {
        for (int i = 0; i < paramArrayOfString.length; i++)
        {
          String str = localNode.getNamespaceURI();
          if ((str != null) && (str.equals(paramArrayOfString[i][0])) && (localNode.getLocalName().equals(paramArrayOfString[i][1]))) {
            return (Element)localNode;
          }
        }
      }
    }
    return null;
  }
  
  public static Element getLastChildElementNS(Node paramNode, String[][] paramArrayOfString)
  {
    for (Node localNode = paramNode.getLastChild(); localNode != null; localNode = localNode.getPreviousSibling()) {
      if (localNode.getNodeType() == 1) {
        for (int i = 0; i < paramArrayOfString.length; i++)
        {
          String str = localNode.getNamespaceURI();
          if ((str != null) && (str.equals(paramArrayOfString[i][0])) && (localNode.getLocalName().equals(paramArrayOfString[i][1]))) {
            return (Element)localNode;
          }
        }
      }
    }
    return null;
  }
  
  public static Element getNextSiblingElementNS(Node paramNode, String[][] paramArrayOfString)
  {
    for (Node localNode = paramNode.getNextSibling(); localNode != null; localNode = localNode.getNextSibling()) {
      if (localNode.getNodeType() == 1) {
        for (int i = 0; i < paramArrayOfString.length; i++)
        {
          String str = localNode.getNamespaceURI();
          if ((str != null) && (str.equals(paramArrayOfString[i][0])) && (localNode.getLocalName().equals(paramArrayOfString[i][1]))) {
            return (Element)localNode;
          }
        }
      }
    }
    return null;
  }
  
  public static Element getFirstChildElement(Node paramNode, String paramString1, String paramString2, String paramString3)
  {
    for (Node localNode = paramNode.getFirstChild(); localNode != null; localNode = localNode.getNextSibling()) {
      if (localNode.getNodeType() == 1)
      {
        Element localElement = (Element)localNode;
        if ((localElement.getNodeName().equals(paramString1)) && (localElement.getAttribute(paramString2).equals(paramString3))) {
          return localElement;
        }
      }
    }
    return null;
  }
  
  public static Element getLastChildElement(Node paramNode, String paramString1, String paramString2, String paramString3)
  {
    for (Node localNode = paramNode.getLastChild(); localNode != null; localNode = localNode.getPreviousSibling()) {
      if (localNode.getNodeType() == 1)
      {
        Element localElement = (Element)localNode;
        if ((localElement.getNodeName().equals(paramString1)) && (localElement.getAttribute(paramString2).equals(paramString3))) {
          return localElement;
        }
      }
    }
    return null;
  }
  
  public static Element getNextSiblingElement(Node paramNode, String paramString1, String paramString2, String paramString3)
  {
    for (Node localNode = paramNode.getNextSibling(); localNode != null; localNode = localNode.getNextSibling()) {
      if (localNode.getNodeType() == 1)
      {
        Element localElement = (Element)localNode;
        if ((localElement.getNodeName().equals(paramString1)) && (localElement.getAttribute(paramString2).equals(paramString3))) {
          return localElement;
        }
      }
    }
    return null;
  }
  
  public static String getChildText(Node paramNode)
  {
    if (paramNode == null) {
      return null;
    }
    StringBuffer localStringBuffer = new StringBuffer();
    for (Node localNode = paramNode.getFirstChild(); localNode != null; localNode = localNode.getNextSibling())
    {
      int i = localNode.getNodeType();
      if (i == 3) {
        localStringBuffer.append(localNode.getNodeValue());
      } else if (i == 4) {
        localStringBuffer.append(getChildText(localNode));
      }
    }
    return localStringBuffer.toString();
  }
  
  public static String getName(Node paramNode)
  {
    return paramNode.getNodeName();
  }
  
  public static String getLocalName(Node paramNode)
  {
    String str = paramNode.getLocalName();
    return str != null ? str : paramNode.getNodeName();
  }
  
  public static Element getParent(Element paramElement)
  {
    Node localNode = paramElement.getParentNode();
    if ((localNode instanceof Element)) {
      return (Element)localNode;
    }
    return null;
  }
  
  public static Document getDocument(Node paramNode)
  {
    return paramNode.getOwnerDocument();
  }
  
  public static Element getRoot(Document paramDocument)
  {
    return paramDocument.getDocumentElement();
  }
  
  public static Attr getAttr(Element paramElement, String paramString)
  {
    return paramElement.getAttributeNode(paramString);
  }
  
  public static Attr getAttrNS(Element paramElement, String paramString1, String paramString2)
  {
    return paramElement.getAttributeNodeNS(paramString1, paramString2);
  }
  
  public static Attr[] getAttrs(Element paramElement)
  {
    NamedNodeMap localNamedNodeMap = paramElement.getAttributes();
    Attr[] arrayOfAttr = new Attr[localNamedNodeMap.getLength()];
    for (int i = 0; i < localNamedNodeMap.getLength(); i++) {
      arrayOfAttr[i] = ((Attr)localNamedNodeMap.item(i));
    }
    return arrayOfAttr;
  }
  
  public static String getValue(Attr paramAttr)
  {
    return paramAttr.getValue();
  }
  
  public static String getAttrValue(Element paramElement, String paramString)
  {
    return paramElement.getAttribute(paramString);
  }
  
  public static String getAttrValueNS(Element paramElement, String paramString1, String paramString2)
  {
    return paramElement.getAttributeNS(paramString1, paramString2);
  }
  
  public static String getPrefix(Node paramNode)
  {
    return paramNode.getPrefix();
  }
  
  public static String getNamespaceURI(Node paramNode)
  {
    return paramNode.getNamespaceURI();
  }
  
  public static String getAnnotation(Node paramNode)
  {
    if ((paramNode instanceof ElementImpl)) {
      return ((ElementImpl)paramNode).getAnnotation();
    }
    return null;
  }
  
  public static String getSyntheticAnnotation(Node paramNode)
  {
    if ((paramNode instanceof ElementImpl)) {
      return ((ElementImpl)paramNode).getSyntheticAnnotation();
    }
    return null;
  }
  
  public static DOMException createDOMException(short paramShort, Throwable paramThrowable)
  {
    DOMException localDOMException = new DOMException(paramShort, paramThrowable != null ? paramThrowable.getMessage() : null);
    if ((paramThrowable != null) && (ThrowableMethods.fgThrowableMethodsAvailable)) {
      try
      {
        ThrowableMethods.fgThrowableInitCauseMethod.invoke(localDOMException, new Object[] { paramThrowable });
      }
      catch (Exception localException) {}
    }
    return localDOMException;
  }
  
  public static LSException createLSException(short paramShort, Throwable paramThrowable)
  {
    LSException localLSException = new LSException(paramShort, paramThrowable != null ? paramThrowable.getMessage() : null);
    if ((paramThrowable != null) && (ThrowableMethods.fgThrowableMethodsAvailable)) {
      try
      {
        ThrowableMethods.fgThrowableInitCauseMethod.invoke(localLSException, new Object[] { paramThrowable });
      }
      catch (Exception localException) {}
    }
    return localLSException;
  }
  
  static class ThrowableMethods
  {
    private static Method fgThrowableInitCauseMethod = null;
    private static boolean fgThrowableMethodsAvailable = false;
    
    private ThrowableMethods() {}
    
    static
    {
      try
      {
        fgThrowableInitCauseMethod = class$java$lang$Throwable.getMethod("initCause", new Class[] { Throwable.class });
        fgThrowableMethodsAvailable = true;
      }
      catch (Exception localException)
      {
        fgThrowableInitCauseMethod = null;
        fgThrowableMethodsAvailable = false;
      }
    }
  }
}
