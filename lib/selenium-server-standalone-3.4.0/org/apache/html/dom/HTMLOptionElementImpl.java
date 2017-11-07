package org.apache.html.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLOptionElement;
import org.w3c.dom.html.HTMLSelectElement;

public class HTMLOptionElementImpl
  extends HTMLElementImpl
  implements HTMLOptionElement
{
  private static final long serialVersionUID = -4486774554137530907L;
  
  public boolean getDefaultSelected()
  {
    return getBinary("default-selected");
  }
  
  public void setDefaultSelected(boolean paramBoolean)
  {
    setAttribute("default-selected", paramBoolean);
  }
  
  public String getText()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    for (Node localNode = getFirstChild(); localNode != null; localNode = localNode.getNextSibling()) {
      if ((localNode instanceof Text)) {
        localStringBuffer.append(((Text)localNode).getData());
      }
    }
    return localStringBuffer.toString();
  }
  
  public void setText(String paramString)
  {
    Node localNode;
    for (Object localObject = getFirstChild(); localObject != null; localObject = localNode)
    {
      localNode = ((Node)localObject).getNextSibling();
      removeChild((Node)localObject);
    }
    insertBefore(getOwnerDocument().createTextNode(paramString), getFirstChild());
  }
  
  public int getIndex()
  {
    for (Node localNode = getParentNode(); (localNode != null) && (!(localNode instanceof HTMLSelectElement)); localNode = localNode.getParentNode()) {}
    if (localNode != null)
    {
      NodeList localNodeList = ((HTMLElement)localNode).getElementsByTagName("OPTION");
      for (int i = 0; i < localNodeList.getLength(); i++) {
        if (localNodeList.item(i) == this) {
          return i;
        }
      }
    }
    return -1;
  }
  
  public void setIndex(int paramInt)
  {
    for (Node localNode1 = getParentNode(); (localNode1 != null) && (!(localNode1 instanceof HTMLSelectElement)); localNode1 = localNode1.getParentNode()) {}
    if (localNode1 != null)
    {
      NodeList localNodeList = ((HTMLElement)localNode1).getElementsByTagName("OPTION");
      if (localNodeList.item(paramInt) != this)
      {
        getParentNode().removeChild(this);
        Node localNode2 = localNodeList.item(paramInt);
        localNode2.getParentNode().insertBefore(this, localNode2);
      }
    }
  }
  
  public boolean getDisabled()
  {
    return getBinary("disabled");
  }
  
  public void setDisabled(boolean paramBoolean)
  {
    setAttribute("disabled", paramBoolean);
  }
  
  public String getLabel()
  {
    return capitalize(getAttribute("label"));
  }
  
  public void setLabel(String paramString)
  {
    setAttribute("label", paramString);
  }
  
  public boolean getSelected()
  {
    return getBinary("selected");
  }
  
  public void setSelected(boolean paramBoolean)
  {
    setAttribute("selected", paramBoolean);
  }
  
  public String getValue()
  {
    return getAttribute("value");
  }
  
  public void setValue(String paramString)
  {
    setAttribute("value", paramString);
  }
  
  public HTMLOptionElementImpl(HTMLDocumentImpl paramHTMLDocumentImpl, String paramString)
  {
    super(paramHTMLDocumentImpl, paramString);
  }
}
