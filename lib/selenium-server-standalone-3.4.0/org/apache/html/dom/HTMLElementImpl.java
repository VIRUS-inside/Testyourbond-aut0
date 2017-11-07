package org.apache.html.dom;

import java.util.Locale;
import org.apache.xerces.dom.ElementImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLFormElement;

public class HTMLElementImpl
  extends ElementImpl
  implements HTMLElement
{
  private static final long serialVersionUID = 5283925246324423495L;
  
  public HTMLElementImpl(HTMLDocumentImpl paramHTMLDocumentImpl, String paramString)
  {
    super(paramHTMLDocumentImpl, paramString.toUpperCase(Locale.ENGLISH));
  }
  
  public String getId()
  {
    return getAttribute("id");
  }
  
  public void setId(String paramString)
  {
    setAttribute("id", paramString);
  }
  
  public String getTitle()
  {
    return getAttribute("title");
  }
  
  public void setTitle(String paramString)
  {
    setAttribute("title", paramString);
  }
  
  public String getLang()
  {
    return getAttribute("lang");
  }
  
  public void setLang(String paramString)
  {
    setAttribute("lang", paramString);
  }
  
  public String getDir()
  {
    return getAttribute("dir");
  }
  
  public void setDir(String paramString)
  {
    setAttribute("dir", paramString);
  }
  
  public String getClassName()
  {
    return getAttribute("class");
  }
  
  public void setClassName(String paramString)
  {
    setAttribute("class", paramString);
  }
  
  int getInteger(String paramString)
  {
    try
    {
      return Integer.parseInt(paramString);
    }
    catch (NumberFormatException localNumberFormatException) {}
    return 0;
  }
  
  boolean getBinary(String paramString)
  {
    return getAttributeNode(paramString) != null;
  }
  
  void setAttribute(String paramString, boolean paramBoolean)
  {
    if (paramBoolean) {
      setAttribute(paramString, paramString);
    } else {
      removeAttribute(paramString);
    }
  }
  
  public Attr getAttributeNode(String paramString)
  {
    return super.getAttributeNode(paramString.toLowerCase(Locale.ENGLISH));
  }
  
  public Attr getAttributeNodeNS(String paramString1, String paramString2)
  {
    if ((paramString1 != null) && (paramString1.length() > 0)) {
      return super.getAttributeNodeNS(paramString1, paramString2);
    }
    return super.getAttributeNode(paramString2.toLowerCase(Locale.ENGLISH));
  }
  
  public String getAttribute(String paramString)
  {
    return super.getAttribute(paramString.toLowerCase(Locale.ENGLISH));
  }
  
  public String getAttributeNS(String paramString1, String paramString2)
  {
    if ((paramString1 != null) && (paramString1.length() > 0)) {
      return super.getAttributeNS(paramString1, paramString2);
    }
    return super.getAttribute(paramString2.toLowerCase(Locale.ENGLISH));
  }
  
  public final NodeList getElementsByTagName(String paramString)
  {
    return super.getElementsByTagName(paramString.toUpperCase(Locale.ENGLISH));
  }
  
  public final NodeList getElementsByTagNameNS(String paramString1, String paramString2)
  {
    if ((paramString1 != null) && (paramString1.length() > 0)) {
      return super.getElementsByTagNameNS(paramString1, paramString2.toUpperCase(Locale.ENGLISH));
    }
    return super.getElementsByTagName(paramString2.toUpperCase(Locale.ENGLISH));
  }
  
  String capitalize(String paramString)
  {
    char[] arrayOfChar = paramString.toCharArray();
    if (arrayOfChar.length > 0)
    {
      arrayOfChar[0] = Character.toUpperCase(arrayOfChar[0]);
      for (int i = 1; i < arrayOfChar.length; i++) {
        arrayOfChar[i] = Character.toLowerCase(arrayOfChar[i]);
      }
      return String.valueOf(arrayOfChar);
    }
    return paramString;
  }
  
  String getCapitalized(String paramString)
  {
    String str = getAttribute(paramString);
    if (str != null)
    {
      char[] arrayOfChar = str.toCharArray();
      if (arrayOfChar.length > 0)
      {
        arrayOfChar[0] = Character.toUpperCase(arrayOfChar[0]);
        for (int i = 1; i < arrayOfChar.length; i++) {
          arrayOfChar[i] = Character.toLowerCase(arrayOfChar[i]);
        }
        return String.valueOf(arrayOfChar);
      }
    }
    return str;
  }
  
  public HTMLFormElement getForm()
  {
    for (Node localNode = getParentNode(); localNode != null; localNode = localNode.getParentNode()) {
      if ((localNode instanceof HTMLFormElement)) {
        return (HTMLFormElement)localNode;
      }
    }
    return null;
  }
}
