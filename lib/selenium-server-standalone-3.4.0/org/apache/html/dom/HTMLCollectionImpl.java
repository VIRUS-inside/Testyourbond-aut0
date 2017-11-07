package org.apache.html.dom;

import java.io.Serializable;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLAppletElement;
import org.w3c.dom.html.HTMLAreaElement;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLImageElement;
import org.w3c.dom.html.HTMLObjectElement;
import org.w3c.dom.html.HTMLOptionElement;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableRowElement;
import org.w3c.dom.html.HTMLTableSectionElement;

class HTMLCollectionImpl
  implements HTMLCollection, Serializable
{
  private static final long serialVersionUID = 9112122196669185082L;
  static final short ANCHOR = 1;
  static final short FORM = 2;
  static final short IMAGE = 3;
  static final short APPLET = 4;
  static final short LINK = 5;
  static final short OPTION = 6;
  static final short ROW = 7;
  static final short ELEMENT = 8;
  static final short AREA = -1;
  static final short TBODY = -2;
  static final short CELL = -3;
  private short _lookingFor;
  private Element _topLevel;
  
  HTMLCollectionImpl(HTMLElement paramHTMLElement, short paramShort)
  {
    if (paramHTMLElement == null) {
      throw new NullPointerException("HTM011 Argument 'topLevel' is null.");
    }
    _topLevel = paramHTMLElement;
    _lookingFor = paramShort;
  }
  
  public final int getLength()
  {
    return getLength(_topLevel);
  }
  
  public final Node item(int paramInt)
  {
    if (paramInt < 0) {
      throw new IllegalArgumentException("HTM012 Argument 'index' is negative.");
    }
    return item(_topLevel, new CollectionIndex(paramInt));
  }
  
  public final Node namedItem(String paramString)
  {
    if (paramString == null) {
      throw new NullPointerException("HTM013 Argument 'name' is null.");
    }
    return namedItem(_topLevel, paramString);
  }
  
  private int getLength(Element paramElement)
  {
    int i;
    synchronized (paramElement)
    {
      i = 0;
      for (Node localNode = paramElement.getFirstChild(); localNode != null; localNode = localNode.getNextSibling()) {
        if ((localNode instanceof Element)) {
          if (collectionMatch((Element)localNode, null)) {
            i++;
          } else if (recurse()) {
            i += getLength((Element)localNode);
          }
        }
      }
    }
    return i;
  }
  
  private Node item(Element paramElement, CollectionIndex paramCollectionIndex)
  {
    synchronized (paramElement)
    {
      for (Node localNode1 = paramElement.getFirstChild(); localNode1 != null; localNode1 = localNode1.getNextSibling()) {
        if ((localNode1 instanceof Element))
        {
          Object localObject1;
          if (collectionMatch((Element)localNode1, null))
          {
            if (paramCollectionIndex.isZero())
            {
              localObject1 = localNode1;
              return localObject1;
            }
            paramCollectionIndex.decrement();
          }
          else if (recurse())
          {
            Node localNode2 = item((Element)localNode1, paramCollectionIndex);
            if (localNode2 != null)
            {
              localObject1 = localNode2;
              return localObject1;
            }
          }
        }
      }
    }
    return null;
  }
  
  private Node namedItem(Element paramElement, String paramString)
  {
    synchronized (paramElement)
    {
      for (Node localNode1 = paramElement.getFirstChild(); localNode1 != null; localNode1 = localNode1.getNextSibling()) {
        if ((localNode1 instanceof Element))
        {
          if (collectionMatch((Element)localNode1, paramString))
          {
            localNode3 = localNode1;
            return localNode3;
          }
          if (recurse())
          {
            Node localNode2 = namedItem((Element)localNode1, paramString);
            if (localNode2 != null)
            {
              Node localNode4 = localNode2;
              return localNode4;
            }
          }
        }
      }
      Node localNode3 = localNode1;
      return localNode3;
    }
  }
  
  protected boolean recurse()
  {
    return _lookingFor > 0;
  }
  
  protected boolean collectionMatch(Element paramElement, String paramString)
  {
    boolean bool1;
    synchronized (paramElement)
    {
      bool1 = false;
      switch (_lookingFor)
      {
      case 1: 
        bool1 = ((paramElement instanceof HTMLAnchorElement)) && (paramElement.getAttribute("name").length() > 0);
        break;
      case 2: 
        bool1 = paramElement instanceof HTMLFormElement;
        break;
      case 3: 
        bool1 = paramElement instanceof HTMLImageElement;
        break;
      case 4: 
        bool1 = ((paramElement instanceof HTMLAppletElement)) || (((paramElement instanceof HTMLObjectElement)) && (("application/java".equals(paramElement.getAttribute("codetype"))) || (paramElement.getAttribute("classid").startsWith("java:"))));
        break;
      case 8: 
        bool1 = paramElement instanceof HTMLFormControl;
        break;
      case 5: 
        bool1 = (((paramElement instanceof HTMLAnchorElement)) || ((paramElement instanceof HTMLAreaElement))) && (paramElement.getAttribute("href").length() > 0);
        break;
      case -1: 
        bool1 = paramElement instanceof HTMLAreaElement;
        break;
      case 6: 
        bool1 = paramElement instanceof HTMLOptionElement;
        break;
      case 7: 
        bool1 = paramElement instanceof HTMLTableRowElement;
        break;
      case -2: 
        bool1 = ((paramElement instanceof HTMLTableSectionElement)) && (paramElement.getTagName().equals("TBODY"));
        break;
      case -3: 
        bool1 = paramElement instanceof HTMLTableCellElement;
      }
      if ((bool1) && (paramString != null))
      {
        if (((paramElement instanceof HTMLAnchorElement)) && (paramString.equals(paramElement.getAttribute("name"))))
        {
          boolean bool2 = true;
          return bool2;
        }
        bool1 = paramString.equals(paramElement.getAttribute("id"));
      }
    }
    return bool1;
  }
}
