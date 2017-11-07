package org.apache.html.dom;

import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLMapElement;

public class HTMLMapElementImpl
  extends HTMLElementImpl
  implements HTMLMapElement
{
  private static final long serialVersionUID = 7520887584251976392L;
  private HTMLCollection _areas;
  
  public HTMLCollection getAreas()
  {
    if (_areas == null) {
      _areas = new HTMLCollectionImpl(this, (short)-1);
    }
    return _areas;
  }
  
  public String getName()
  {
    return getAttribute("name");
  }
  
  public void setName(String paramString)
  {
    setAttribute("name", paramString);
  }
  
  public Node cloneNode(boolean paramBoolean)
  {
    HTMLMapElementImpl localHTMLMapElementImpl = (HTMLMapElementImpl)super.cloneNode(paramBoolean);
    _areas = null;
    return localHTMLMapElementImpl;
  }
  
  public HTMLMapElementImpl(HTMLDocumentImpl paramHTMLDocumentImpl, String paramString)
  {
    super(paramHTMLDocumentImpl, paramString);
  }
}
