package org.apache.xerces.impl.xs.opti;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class ElementImpl
  extends DefaultElement
{
  SchemaDOM schemaDOM;
  Attr[] attrs;
  int row = -1;
  int col = -1;
  int parentRow = -1;
  int line;
  int column;
  int charOffset;
  String fAnnotation;
  String fSyntheticAnnotation;
  
  public ElementImpl(int paramInt1, int paramInt2, int paramInt3)
  {
    nodeType = 1;
    line = paramInt1;
    column = paramInt2;
    charOffset = paramInt3;
  }
  
  public ElementImpl(int paramInt1, int paramInt2)
  {
    this(paramInt1, paramInt2, -1);
  }
  
  public ElementImpl(String paramString1, String paramString2, String paramString3, String paramString4, int paramInt1, int paramInt2, int paramInt3)
  {
    super(paramString1, paramString2, paramString3, paramString4, (short)1);
    line = paramInt1;
    column = paramInt2;
    charOffset = paramInt3;
  }
  
  public ElementImpl(String paramString1, String paramString2, String paramString3, String paramString4, int paramInt1, int paramInt2)
  {
    this(paramString1, paramString2, paramString3, paramString4, paramInt1, paramInt2, -1);
  }
  
  public Document getOwnerDocument()
  {
    return schemaDOM;
  }
  
  public Node getParentNode()
  {
    return schemaDOM.relations[row][0];
  }
  
  public boolean hasChildNodes()
  {
    return parentRow != -1;
  }
  
  public Node getFirstChild()
  {
    if (parentRow == -1) {
      return null;
    }
    return schemaDOM.relations[parentRow][1];
  }
  
  public Node getLastChild()
  {
    if (parentRow == -1) {
      return null;
    }
    for (int i = 1; i < schemaDOM.relations[parentRow].length; i++) {
      if (schemaDOM.relations[parentRow][i] == null) {
        return schemaDOM.relations[parentRow][(i - 1)];
      }
    }
    if (i == 1) {
      i++;
    }
    return schemaDOM.relations[parentRow][(i - 1)];
  }
  
  public Node getPreviousSibling()
  {
    if (col == 1) {
      return null;
    }
    return schemaDOM.relations[row][(col - 1)];
  }
  
  public Node getNextSibling()
  {
    if (col == schemaDOM.relations[row].length - 1) {
      return null;
    }
    return schemaDOM.relations[row][(col + 1)];
  }
  
  public NamedNodeMap getAttributes()
  {
    return new NamedNodeMapImpl(attrs);
  }
  
  public boolean hasAttributes()
  {
    return attrs.length != 0;
  }
  
  public String getTagName()
  {
    return rawname;
  }
  
  public String getAttribute(String paramString)
  {
    for (int i = 0; i < attrs.length; i++) {
      if (attrs[i].getName().equals(paramString)) {
        return attrs[i].getValue();
      }
    }
    return "";
  }
  
  public Attr getAttributeNode(String paramString)
  {
    for (int i = 0; i < attrs.length; i++) {
      if (attrs[i].getName().equals(paramString)) {
        return attrs[i];
      }
    }
    return null;
  }
  
  public String getAttributeNS(String paramString1, String paramString2)
  {
    for (int i = 0; i < attrs.length; i++) {
      if ((attrs[i].getLocalName().equals(paramString2)) && (nsEquals(attrs[i].getNamespaceURI(), paramString1))) {
        return attrs[i].getValue();
      }
    }
    return "";
  }
  
  public Attr getAttributeNodeNS(String paramString1, String paramString2)
  {
    for (int i = 0; i < attrs.length; i++) {
      if ((attrs[i].getName().equals(paramString2)) && (nsEquals(attrs[i].getNamespaceURI(), paramString1))) {
        return attrs[i];
      }
    }
    return null;
  }
  
  public boolean hasAttribute(String paramString)
  {
    for (int i = 0; i < attrs.length; i++) {
      if (attrs[i].getName().equals(paramString)) {
        return true;
      }
    }
    return false;
  }
  
  public boolean hasAttributeNS(String paramString1, String paramString2)
  {
    for (int i = 0; i < attrs.length; i++) {
      if ((attrs[i].getName().equals(paramString2)) && (nsEquals(attrs[i].getNamespaceURI(), paramString1))) {
        return true;
      }
    }
    return false;
  }
  
  public void setAttribute(String paramString1, String paramString2)
  {
    for (int i = 0; i < attrs.length; i++) {
      if (attrs[i].getName().equals(paramString1))
      {
        attrs[i].setValue(paramString2);
        return;
      }
    }
  }
  
  public int getLineNumber()
  {
    return line;
  }
  
  public int getColumnNumber()
  {
    return column;
  }
  
  public int getCharacterOffset()
  {
    return charOffset;
  }
  
  public String getAnnotation()
  {
    return fAnnotation;
  }
  
  public String getSyntheticAnnotation()
  {
    return fSyntheticAnnotation;
  }
  
  private static boolean nsEquals(String paramString1, String paramString2)
  {
    if (paramString1 == null) {
      return paramString2 == null;
    }
    return paramString1.equals(paramString2);
  }
}
