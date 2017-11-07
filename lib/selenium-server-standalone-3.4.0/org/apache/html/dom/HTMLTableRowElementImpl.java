package org.apache.html.dom;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTableRowElement;
import org.w3c.dom.html.HTMLTableSectionElement;

public class HTMLTableRowElementImpl
  extends HTMLElementImpl
  implements HTMLTableRowElement
{
  private static final long serialVersionUID = 5409562635656244263L;
  HTMLCollection _cells;
  
  public int getRowIndex()
  {
    Node localNode = getParentNode();
    if ((localNode instanceof HTMLTableSectionElement)) {
      localNode = localNode.getParentNode();
    }
    if ((localNode instanceof HTMLTableElement)) {
      return getRowIndex(localNode);
    }
    return -1;
  }
  
  public void setRowIndex(int paramInt)
  {
    Node localNode = getParentNode();
    if ((localNode instanceof HTMLTableSectionElement)) {
      localNode = localNode.getParentNode();
    }
    if ((localNode instanceof HTMLTableElement)) {
      ((HTMLTableElementImpl)localNode).insertRowX(paramInt, this);
    }
  }
  
  public int getSectionRowIndex()
  {
    Node localNode = getParentNode();
    if ((localNode instanceof HTMLTableSectionElement)) {
      return getRowIndex(localNode);
    }
    return -1;
  }
  
  public void setSectionRowIndex(int paramInt)
  {
    Node localNode = getParentNode();
    if ((localNode instanceof HTMLTableSectionElement)) {
      ((HTMLTableSectionElementImpl)localNode).insertRowX(paramInt, this);
    }
  }
  
  int getRowIndex(Node paramNode)
  {
    NodeList localNodeList = ((HTMLElement)paramNode).getElementsByTagName("TR");
    for (int i = 0; i < localNodeList.getLength(); i++) {
      if (localNodeList.item(i) == this) {
        return i;
      }
    }
    return -1;
  }
  
  public HTMLCollection getCells()
  {
    if (_cells == null) {
      _cells = new HTMLCollectionImpl(this, (short)-3);
    }
    return _cells;
  }
  
  public void setCells(HTMLCollection paramHTMLCollection)
  {
    for (Node localNode = getFirstChild(); localNode != null; localNode = localNode.getNextSibling()) {
      removeChild(localNode);
    }
    int i = 0;
    for (localNode = paramHTMLCollection.item(i); localNode != null; localNode = paramHTMLCollection.item(i))
    {
      appendChild(localNode);
      i++;
    }
  }
  
  public HTMLElement insertCell(int paramInt)
  {
    HTMLTableCellElementImpl localHTMLTableCellElementImpl = new HTMLTableCellElementImpl((HTMLDocumentImpl)getOwnerDocument(), "TD");
    for (Node localNode = getFirstChild(); localNode != null; localNode = localNode.getNextSibling()) {
      if ((localNode instanceof HTMLTableCellElement))
      {
        if (paramInt == 0)
        {
          insertBefore(localHTMLTableCellElementImpl, localNode);
          return localHTMLTableCellElementImpl;
        }
        paramInt--;
      }
    }
    appendChild(localHTMLTableCellElementImpl);
    return localHTMLTableCellElementImpl;
  }
  
  public void deleteCell(int paramInt)
  {
    for (Node localNode = getFirstChild(); localNode != null; localNode = localNode.getNextSibling()) {
      if ((localNode instanceof HTMLTableCellElement))
      {
        if (paramInt == 0)
        {
          removeChild(localNode);
          return;
        }
        paramInt--;
      }
    }
  }
  
  public String getAlign()
  {
    return capitalize(getAttribute("align"));
  }
  
  public void setAlign(String paramString)
  {
    setAttribute("align", paramString);
  }
  
  public String getBgColor()
  {
    return getAttribute("bgcolor");
  }
  
  public void setBgColor(String paramString)
  {
    setAttribute("bgcolor", paramString);
  }
  
  public String getCh()
  {
    String str = getAttribute("char");
    if ((str != null) && (str.length() > 1)) {
      str = str.substring(0, 1);
    }
    return str;
  }
  
  public void setCh(String paramString)
  {
    if ((paramString != null) && (paramString.length() > 1)) {
      paramString = paramString.substring(0, 1);
    }
    setAttribute("char", paramString);
  }
  
  public String getChOff()
  {
    return getAttribute("charoff");
  }
  
  public void setChOff(String paramString)
  {
    setAttribute("charoff", paramString);
  }
  
  public String getVAlign()
  {
    return capitalize(getAttribute("valign"));
  }
  
  public void setVAlign(String paramString)
  {
    setAttribute("valign", paramString);
  }
  
  public Node cloneNode(boolean paramBoolean)
  {
    HTMLTableRowElementImpl localHTMLTableRowElementImpl = (HTMLTableRowElementImpl)super.cloneNode(paramBoolean);
    _cells = null;
    return localHTMLTableRowElementImpl;
  }
  
  public HTMLTableRowElementImpl(HTMLDocumentImpl paramHTMLDocumentImpl, String paramString)
  {
    super(paramHTMLDocumentImpl, paramString);
  }
}
