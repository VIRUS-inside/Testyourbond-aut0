package org.apache.html.dom;

import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLTableCaptionElement;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTableRowElement;
import org.w3c.dom.html.HTMLTableSectionElement;

public class HTMLTableElementImpl
  extends HTMLElementImpl
  implements HTMLTableElement
{
  private static final long serialVersionUID = -1824053099870917532L;
  private HTMLCollectionImpl _rows;
  private HTMLCollectionImpl _bodies;
  
  public synchronized HTMLTableCaptionElement getCaption()
  {
    for (Node localNode = getFirstChild(); localNode != null; localNode = localNode.getNextSibling()) {
      if (((localNode instanceof HTMLTableCaptionElement)) && (localNode.getNodeName().equals("CAPTION"))) {
        return (HTMLTableCaptionElement)localNode;
      }
    }
    return null;
  }
  
  public synchronized void setCaption(HTMLTableCaptionElement paramHTMLTableCaptionElement)
  {
    if ((paramHTMLTableCaptionElement != null) && (!paramHTMLTableCaptionElement.getTagName().equals("CAPTION"))) {
      throw new IllegalArgumentException("HTM016 Argument 'caption' is not an element of type <CAPTION>.");
    }
    deleteCaption();
    if (paramHTMLTableCaptionElement != null) {
      appendChild(paramHTMLTableCaptionElement);
    }
  }
  
  public synchronized HTMLElement createCaption()
  {
    Object localObject = getCaption();
    if (localObject != null) {
      return localObject;
    }
    localObject = new HTMLTableCaptionElementImpl((HTMLDocumentImpl)getOwnerDocument(), "CAPTION");
    appendChild((Node)localObject);
    return localObject;
  }
  
  public synchronized void deleteCaption()
  {
    HTMLTableCaptionElement localHTMLTableCaptionElement = getCaption();
    if (localHTMLTableCaptionElement != null) {
      removeChild(localHTMLTableCaptionElement);
    }
  }
  
  public synchronized HTMLTableSectionElement getTHead()
  {
    for (Node localNode = getFirstChild(); localNode != null; localNode = localNode.getNextSibling()) {
      if (((localNode instanceof HTMLTableSectionElement)) && (localNode.getNodeName().equals("THEAD"))) {
        return (HTMLTableSectionElement)localNode;
      }
    }
    return null;
  }
  
  public synchronized void setTHead(HTMLTableSectionElement paramHTMLTableSectionElement)
  {
    if ((paramHTMLTableSectionElement != null) && (!paramHTMLTableSectionElement.getTagName().equals("THEAD"))) {
      throw new IllegalArgumentException("HTM017 Argument 'tHead' is not an element of type <THEAD>.");
    }
    deleteTHead();
    if (paramHTMLTableSectionElement != null) {
      appendChild(paramHTMLTableSectionElement);
    }
  }
  
  public synchronized HTMLElement createTHead()
  {
    Object localObject = getTHead();
    if (localObject != null) {
      return localObject;
    }
    localObject = new HTMLTableSectionElementImpl((HTMLDocumentImpl)getOwnerDocument(), "THEAD");
    appendChild((Node)localObject);
    return localObject;
  }
  
  public synchronized void deleteTHead()
  {
    HTMLTableSectionElement localHTMLTableSectionElement = getTHead();
    if (localHTMLTableSectionElement != null) {
      removeChild(localHTMLTableSectionElement);
    }
  }
  
  public synchronized HTMLTableSectionElement getTFoot()
  {
    for (Node localNode = getFirstChild(); localNode != null; localNode = localNode.getNextSibling()) {
      if (((localNode instanceof HTMLTableSectionElement)) && (localNode.getNodeName().equals("TFOOT"))) {
        return (HTMLTableSectionElement)localNode;
      }
    }
    return null;
  }
  
  public synchronized void setTFoot(HTMLTableSectionElement paramHTMLTableSectionElement)
  {
    if ((paramHTMLTableSectionElement != null) && (!paramHTMLTableSectionElement.getTagName().equals("TFOOT"))) {
      throw new IllegalArgumentException("HTM018 Argument 'tFoot' is not an element of type <TFOOT>.");
    }
    deleteTFoot();
    if (paramHTMLTableSectionElement != null) {
      appendChild(paramHTMLTableSectionElement);
    }
  }
  
  public synchronized HTMLElement createTFoot()
  {
    Object localObject = getTFoot();
    if (localObject != null) {
      return localObject;
    }
    localObject = new HTMLTableSectionElementImpl((HTMLDocumentImpl)getOwnerDocument(), "TFOOT");
    appendChild((Node)localObject);
    return localObject;
  }
  
  public synchronized void deleteTFoot()
  {
    HTMLTableSectionElement localHTMLTableSectionElement = getTFoot();
    if (localHTMLTableSectionElement != null) {
      removeChild(localHTMLTableSectionElement);
    }
  }
  
  public HTMLCollection getRows()
  {
    if (_rows == null) {
      _rows = new HTMLCollectionImpl(this, (short)7);
    }
    return _rows;
  }
  
  public HTMLCollection getTBodies()
  {
    if (_bodies == null) {
      _bodies = new HTMLCollectionImpl(this, (short)-2);
    }
    return _bodies;
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
  
  public String getBorder()
  {
    return getAttribute("border");
  }
  
  public void setBorder(String paramString)
  {
    setAttribute("border", paramString);
  }
  
  public String getCellPadding()
  {
    return getAttribute("cellpadding");
  }
  
  public void setCellPadding(String paramString)
  {
    setAttribute("cellpadding", paramString);
  }
  
  public String getCellSpacing()
  {
    return getAttribute("cellspacing");
  }
  
  public void setCellSpacing(String paramString)
  {
    setAttribute("cellspacing", paramString);
  }
  
  public String getFrame()
  {
    return capitalize(getAttribute("frame"));
  }
  
  public void setFrame(String paramString)
  {
    setAttribute("frame", paramString);
  }
  
  public String getRules()
  {
    return capitalize(getAttribute("rules"));
  }
  
  public void setRules(String paramString)
  {
    setAttribute("rules", paramString);
  }
  
  public String getSummary()
  {
    return getAttribute("summary");
  }
  
  public void setSummary(String paramString)
  {
    setAttribute("summary", paramString);
  }
  
  public String getWidth()
  {
    return getAttribute("width");
  }
  
  public void setWidth(String paramString)
  {
    setAttribute("width", paramString);
  }
  
  public HTMLElement insertRow(int paramInt)
  {
    HTMLTableRowElementImpl localHTMLTableRowElementImpl = new HTMLTableRowElementImpl((HTMLDocumentImpl)getOwnerDocument(), "TR");
    insertRowX(paramInt, localHTMLTableRowElementImpl);
    return localHTMLTableRowElementImpl;
  }
  
  void insertRowX(int paramInt, HTMLTableRowElementImpl paramHTMLTableRowElementImpl)
  {
    Object localObject = null;
    for (Node localNode = getFirstChild(); localNode != null; localNode = localNode.getNextSibling()) {
      if ((localNode instanceof HTMLTableRowElement))
      {
        if (paramInt == 0) {
          insertBefore(paramHTMLTableRowElementImpl, localNode);
        }
      }
      else if ((localNode instanceof HTMLTableSectionElementImpl))
      {
        localObject = localNode;
        paramInt = ((HTMLTableSectionElementImpl)localNode).insertRowX(paramInt, paramHTMLTableRowElementImpl);
        if (paramInt < 0) {
          return;
        }
      }
    }
    if (localObject != null) {
      localObject.appendChild(paramHTMLTableRowElementImpl);
    } else {
      appendChild(paramHTMLTableRowElementImpl);
    }
  }
  
  public synchronized void deleteRow(int paramInt)
  {
    for (Node localNode = getFirstChild(); localNode != null; localNode = localNode.getNextSibling()) {
      if ((localNode instanceof HTMLTableRowElement))
      {
        if (paramInt == 0)
        {
          removeChild(localNode);
          return;
        }
        paramInt--;
      }
      else if ((localNode instanceof HTMLTableSectionElementImpl))
      {
        paramInt = ((HTMLTableSectionElementImpl)localNode).deleteRowX(paramInt);
        if (paramInt < 0) {
          return;
        }
      }
    }
  }
  
  public Node cloneNode(boolean paramBoolean)
  {
    HTMLTableElementImpl localHTMLTableElementImpl = (HTMLTableElementImpl)super.cloneNode(paramBoolean);
    _rows = null;
    _bodies = null;
    return localHTMLTableElementImpl;
  }
  
  public HTMLTableElementImpl(HTMLDocumentImpl paramHTMLDocumentImpl, String paramString)
  {
    super(paramHTMLDocumentImpl, paramString);
  }
}
