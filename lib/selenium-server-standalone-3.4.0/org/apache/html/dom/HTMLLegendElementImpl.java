package org.apache.html.dom;

import org.w3c.dom.html.HTMLLegendElement;

public class HTMLLegendElementImpl
  extends HTMLElementImpl
  implements HTMLLegendElement
{
  private static final long serialVersionUID = -621849164029630762L;
  
  public String getAccessKey()
  {
    String str = getAttribute("accesskey");
    if ((str != null) && (str.length() > 1)) {
      str = str.substring(0, 1);
    }
    return str;
  }
  
  public void setAccessKey(String paramString)
  {
    if ((paramString != null) && (paramString.length() > 1)) {
      paramString = paramString.substring(0, 1);
    }
    setAttribute("accesskey", paramString);
  }
  
  public String getAlign()
  {
    return getAttribute("align");
  }
  
  public void setAlign(String paramString)
  {
    setAttribute("align", paramString);
  }
  
  public HTMLLegendElementImpl(HTMLDocumentImpl paramHTMLDocumentImpl, String paramString)
  {
    super(paramHTMLDocumentImpl, paramString);
  }
}
