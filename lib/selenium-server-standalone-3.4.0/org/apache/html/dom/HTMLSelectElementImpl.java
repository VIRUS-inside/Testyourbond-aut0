package org.apache.html.dom;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLOptionElement;
import org.w3c.dom.html.HTMLSelectElement;

public class HTMLSelectElementImpl
  extends HTMLElementImpl
  implements HTMLSelectElement, HTMLFormControl
{
  private static final long serialVersionUID = -6998282711006968187L;
  private HTMLCollection _options;
  
  public String getType()
  {
    return getAttribute("type");
  }
  
  public String getValue()
  {
    return getAttribute("value");
  }
  
  public void setValue(String paramString)
  {
    setAttribute("value", paramString);
  }
  
  public int getSelectedIndex()
  {
    NodeList localNodeList = getElementsByTagName("OPTION");
    for (int i = 0; i < localNodeList.getLength(); i++) {
      if (((HTMLOptionElement)localNodeList.item(i)).getSelected()) {
        return i;
      }
    }
    return -1;
  }
  
  public void setSelectedIndex(int paramInt)
  {
    NodeList localNodeList = getElementsByTagName("OPTION");
    for (int i = 0; i < localNodeList.getLength(); i++) {
      ((HTMLOptionElementImpl)localNodeList.item(i)).setSelected(i == paramInt);
    }
  }
  
  public HTMLCollection getOptions()
  {
    if (_options == null) {
      _options = new HTMLCollectionImpl(this, (short)6);
    }
    return _options;
  }
  
  public int getLength()
  {
    return getOptions().getLength();
  }
  
  public boolean getDisabled()
  {
    return getBinary("disabled");
  }
  
  public void setDisabled(boolean paramBoolean)
  {
    setAttribute("disabled", paramBoolean);
  }
  
  public boolean getMultiple()
  {
    return getBinary("multiple");
  }
  
  public void setMultiple(boolean paramBoolean)
  {
    setAttribute("multiple", paramBoolean);
  }
  
  public String getName()
  {
    return getAttribute("name");
  }
  
  public void setName(String paramString)
  {
    setAttribute("name", paramString);
  }
  
  public int getSize()
  {
    return getInteger(getAttribute("size"));
  }
  
  public void setSize(int paramInt)
  {
    setAttribute("size", String.valueOf(paramInt));
  }
  
  public int getTabIndex()
  {
    return getInteger(getAttribute("tabindex"));
  }
  
  public void setTabIndex(int paramInt)
  {
    setAttribute("tabindex", String.valueOf(paramInt));
  }
  
  public void add(HTMLElement paramHTMLElement1, HTMLElement paramHTMLElement2)
  {
    insertBefore(paramHTMLElement1, paramHTMLElement2);
  }
  
  public void remove(int paramInt)
  {
    NodeList localNodeList = getElementsByTagName("OPTION");
    Node localNode = localNodeList.item(paramInt);
    if (localNode != null) {
      localNode.getParentNode().removeChild(localNode);
    }
  }
  
  public void blur() {}
  
  public void focus() {}
  
  public NodeList getChildNodes()
  {
    return getChildNodesUnoptimized();
  }
  
  public Node cloneNode(boolean paramBoolean)
  {
    HTMLSelectElementImpl localHTMLSelectElementImpl = (HTMLSelectElementImpl)super.cloneNode(paramBoolean);
    _options = null;
    return localHTMLSelectElementImpl;
  }
  
  public HTMLSelectElementImpl(HTMLDocumentImpl paramHTMLDocumentImpl, String paramString)
  {
    super(paramHTMLDocumentImpl, paramString);
  }
}
