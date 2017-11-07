package org.apache.html.dom;

import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableRowElement;

public class HTMLTableCellElementImpl
  extends HTMLElementImpl
  implements HTMLTableCellElement
{
  private static final long serialVersionUID = -2406518157464313922L;
  
  public int getCellIndex()
  {
    Node localNode1 = getParentNode();
    int i = 0;
    if ((localNode1 instanceof HTMLTableRowElement)) {
      for (Node localNode2 = localNode1.getFirstChild(); localNode2 != null; localNode2 = localNode2.getNextSibling()) {
        if ((localNode2 instanceof HTMLTableCellElement))
        {
          if (localNode2 == this) {
            return i;
          }
          i++;
        }
      }
    }
    return -1;
  }
  
  public void setCellIndex(int paramInt)
  {
    Node localNode1 = getParentNode();
    if ((localNode1 instanceof HTMLTableRowElement)) {
      for (Node localNode2 = localNode1.getFirstChild(); localNode2 != null; localNode2 = localNode2.getNextSibling()) {
        if ((localNode2 instanceof HTMLTableCellElement))
        {
          if (paramInt == 0)
          {
            if (this != localNode2) {
              localNode1.insertBefore(this, localNode2);
            }
            return;
          }
          paramInt--;
        }
      }
    }
    localNode1.appendChild(this);
  }
  
  public String getAbbr()
  {
    return getAttribute("abbr");
  }
  
  public void setAbbr(String paramString)
  {
    setAttribute("abbr", paramString);
  }
  
  public String getAlign()
  {
    return capitalize(getAttribute("align"));
  }
  
  public void setAlign(String paramString)
  {
    setAttribute("align", paramString);
  }
  
  public String getAxis()
  {
    return getAttribute("axis");
  }
  
  public void setAxis(String paramString)
  {
    setAttribute("axis", paramString);
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
  
  public int getColSpan()
  {
    return getInteger(getAttribute("colspan"));
  }
  
  public void setColSpan(int paramInt)
  {
    setAttribute("colspan", String.valueOf(paramInt));
  }
  
  public String getHeaders()
  {
    return getAttribute("headers");
  }
  
  public void setHeaders(String paramString)
  {
    setAttribute("headers", paramString);
  }
  
  public String getHeight()
  {
    return getAttribute("height");
  }
  
  public void setHeight(String paramString)
  {
    setAttribute("height", paramString);
  }
  
  public boolean getNoWrap()
  {
    return getBinary("nowrap");
  }
  
  public void setNoWrap(boolean paramBoolean)
  {
    setAttribute("nowrap", paramBoolean);
  }
  
  public int getRowSpan()
  {
    return getInteger(getAttribute("rowspan"));
  }
  
  public void setRowSpan(int paramInt)
  {
    setAttribute("rowspan", String.valueOf(paramInt));
  }
  
  public String getScope()
  {
    return getAttribute("scope");
  }
  
  public void setScope(String paramString)
  {
    setAttribute("scope", paramString);
  }
  
  public String getVAlign()
  {
    return capitalize(getAttribute("valign"));
  }
  
  public void setVAlign(String paramString)
  {
    setAttribute("valign", paramString);
  }
  
  public String getWidth()
  {
    return getAttribute("width");
  }
  
  public void setWidth(String paramString)
  {
    setAttribute("width", paramString);
  }
  
  public HTMLTableCellElementImpl(HTMLDocumentImpl paramHTMLDocumentImpl, String paramString)
  {
    super(paramHTMLDocumentImpl, paramString);
  }
}
