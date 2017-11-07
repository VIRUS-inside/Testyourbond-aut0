package org.apache.html.dom;

import org.w3c.dom.html.HTMLLabelElement;

public class HTMLLabelElementImpl
  extends HTMLElementImpl
  implements HTMLLabelElement, HTMLFormControl
{
  private static final long serialVersionUID = 5774388295313199380L;
  
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
  
  public String getHtmlFor()
  {
    return getAttribute("for");
  }
  
  public void setHtmlFor(String paramString)
  {
    setAttribute("for", paramString);
  }
  
  public HTMLLabelElementImpl(HTMLDocumentImpl paramHTMLDocumentImpl, String paramString)
  {
    super(paramHTMLDocumentImpl, paramString);
  }
}
