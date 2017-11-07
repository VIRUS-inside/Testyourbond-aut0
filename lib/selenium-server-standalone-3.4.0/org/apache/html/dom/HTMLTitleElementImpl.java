package org.apache.html.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.html.HTMLTitleElement;

public class HTMLTitleElementImpl
  extends HTMLElementImpl
  implements HTMLTitleElement
{
  private static final long serialVersionUID = 879646303512367875L;
  
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
  
  public HTMLTitleElementImpl(HTMLDocumentImpl paramHTMLDocumentImpl, String paramString)
  {
    super(paramHTMLDocumentImpl, paramString);
  }
}
