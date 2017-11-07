package org.apache.html.dom;

import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.util.Hashtable;
import java.util.Locale;
import org.apache.xerces.dom.DocumentImpl;
import org.apache.xerces.dom.ElementImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLBodyElement;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLFrameSetElement;
import org.w3c.dom.html.HTMLHeadElement;
import org.w3c.dom.html.HTMLHtmlElement;
import org.w3c.dom.html.HTMLTitleElement;

public class HTMLDocumentImpl
  extends DocumentImpl
  implements HTMLDocument
{
  private static final long serialVersionUID = 4285791750126227180L;
  private HTMLCollectionImpl _anchors;
  private HTMLCollectionImpl _forms;
  private HTMLCollectionImpl _images;
  private HTMLCollectionImpl _links;
  private HTMLCollectionImpl _applets;
  private StringWriter _writer;
  private static Hashtable _elementTypesHTML;
  private static final Class[] _elemClassSigHTML = { HTMLDocumentImpl.class, String.class };
  
  public HTMLDocumentImpl()
  {
    populateElementTypes();
  }
  
  public synchronized Element getDocumentElement()
  {
    for (Object localObject1 = getFirstChild(); localObject1 != null; localObject1 = ((Node)localObject1).getNextSibling()) {
      if ((localObject1 instanceof HTMLHtmlElement)) {
        return (HTMLElement)localObject1;
      }
    }
    localObject1 = new HTMLHtmlElementImpl(this, "HTML");
    Node localNode;
    for (Object localObject2 = getFirstChild(); localObject2 != null; localObject2 = localNode)
    {
      localNode = ((Node)localObject2).getNextSibling();
      ((Node)localObject1).appendChild((Node)localObject2);
    }
    appendChild((Node)localObject1);
    return (HTMLElement)localObject1;
  }
  
  public synchronized HTMLElement getHead()
  {
    Element localElement1 = getDocumentElement();
    Object localObject1;
    synchronized (localElement1)
    {
      for (localObject1 = localElement1.getFirstChild(); (localObject1 != null) && (!(localObject1 instanceof HTMLHeadElement)); localObject1 = ((Node)localObject1).getNextSibling()) {}
      if (localObject1 != null)
      {
        synchronized (localObject1)
        {
          Object localObject2 = localElement1.getFirstChild();
          do
          {
            Node localNode = ((Node)localObject2).getNextSibling();
            ((Node)localObject1).insertBefore((Node)localObject2, ((Node)localObject1).getFirstChild());
            localObject2 = localNode;
            if (localObject2 == null) {
              break;
            }
          } while (localObject2 != localObject1);
        }
        HTMLElement localHTMLElement = (HTMLElement)localObject1;
        return localHTMLElement;
      }
      localObject1 = new HTMLHeadElementImpl(this, "HEAD");
      localElement1.insertBefore((Node)localObject1, localElement1.getFirstChild());
    }
    return (HTMLElement)localObject1;
  }
  
  public synchronized String getTitle()
  {
    HTMLElement localHTMLElement = getHead();
    NodeList localNodeList = localHTMLElement.getElementsByTagName("TITLE");
    if (localNodeList.getLength() > 0)
    {
      Node localNode = localNodeList.item(0);
      return ((HTMLTitleElement)localNode).getText();
    }
    return "";
  }
  
  public synchronized void setTitle(String paramString)
  {
    HTMLElement localHTMLElement = getHead();
    NodeList localNodeList = localHTMLElement.getElementsByTagName("TITLE");
    Object localObject;
    if (localNodeList.getLength() > 0)
    {
      localObject = localNodeList.item(0);
      if (((Node)localObject).getParentNode() != localHTMLElement) {
        localHTMLElement.appendChild((Node)localObject);
      }
      ((HTMLTitleElement)localObject).setText(paramString);
    }
    else
    {
      localObject = new HTMLTitleElementImpl(this, "TITLE");
      ((HTMLTitleElement)localObject).setText(paramString);
      localHTMLElement.appendChild((Node)localObject);
    }
  }
  
  public synchronized HTMLElement getBody()
  {
    Element localElement1 = getDocumentElement();
    HTMLElement localHTMLElement1 = getHead();
    Object localObject1;
    synchronized (localElement1)
    {
      for (localObject1 = localHTMLElement1.getNextSibling(); (localObject1 != null) && (!(localObject1 instanceof HTMLBodyElement)) && (!(localObject1 instanceof HTMLFrameSetElement)); localObject1 = ((Node)localObject1).getNextSibling()) {}
      if (localObject1 != null)
      {
        synchronized (localObject1)
        {
          Object localObject2 = localHTMLElement1.getNextSibling();
          do
          {
            Node localNode = ((Node)localObject2).getNextSibling();
            ((Node)localObject1).insertBefore((Node)localObject2, ((Node)localObject1).getFirstChild());
            localObject2 = localNode;
            if (localObject2 == null) {
              break;
            }
          } while (localObject2 != localObject1);
        }
        HTMLElement localHTMLElement2 = (HTMLElement)localObject1;
        return localHTMLElement2;
      }
      localObject1 = new HTMLBodyElementImpl(this, "BODY");
      localElement1.appendChild((Node)localObject1);
    }
    return (HTMLElement)localObject1;
  }
  
  public synchronized void setBody(HTMLElement paramHTMLElement)
  {
    synchronized (paramHTMLElement)
    {
      Element localElement1 = getDocumentElement();
      HTMLElement localHTMLElement1 = getHead();
      synchronized (localElement1)
      {
        NodeList localNodeList = getElementsByTagName("BODY");
        if (localNodeList.getLength() > 0)
        {
          Node localNode1 = localNodeList.item(0);
          synchronized (localNode1)
          {
            for (Object localObject1 = localHTMLElement1; localObject1 != null; localObject1 = ((Node)localObject1).getNextSibling()) {
              if ((localObject1 instanceof Element))
              {
                if (localObject1 != localNode1) {
                  localElement1.insertBefore(paramHTMLElement, (Node)localObject1);
                } else {
                  localElement1.replaceChild(paramHTMLElement, localNode1);
                }
                return;
              }
            }
            localElement1.appendChild(paramHTMLElement);
          }
          return;
        }
        localElement1.appendChild(paramHTMLElement);
      }
    }
  }
  
  public synchronized Element getElementById(String paramString)
  {
    Element localElement = super.getElementById(paramString);
    if (localElement != null) {
      return localElement;
    }
    return getElementById(paramString, this);
  }
  
  public NodeList getElementsByName(String paramString)
  {
    return new NameNodeListImpl(this, paramString);
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
  
  public Element createElementNS(String paramString1, String paramString2, String paramString3)
    throws DOMException
  {
    return createElementNS(paramString1, paramString2);
  }
  
  public Element createElementNS(String paramString1, String paramString2)
  {
    if ((paramString1 == null) || (paramString1.length() == 0)) {
      return createElement(paramString2);
    }
    return super.createElementNS(paramString1, paramString2);
  }
  
  public Element createElement(String paramString)
    throws DOMException
  {
    paramString = paramString.toUpperCase(Locale.ENGLISH);
    Class localClass = (Class)_elementTypesHTML.get(paramString);
    if (localClass != null) {
      try
      {
        Constructor localConstructor = localClass.getConstructor(_elemClassSigHTML);
        return (Element)localConstructor.newInstance(new Object[] { this, paramString });
      }
      catch (Exception localException)
      {
        throw new IllegalStateException("HTM15 Tag '" + paramString + "' associated with an Element class that failed to construct.\n" + paramString);
      }
    }
    return new HTMLElementImpl(this, paramString);
  }
  
  public Attr createAttribute(String paramString)
    throws DOMException
  {
    return super.createAttribute(paramString.toLowerCase(Locale.ENGLISH));
  }
  
  public String getReferrer()
  {
    return null;
  }
  
  public String getDomain()
  {
    return null;
  }
  
  public String getURL()
  {
    return null;
  }
  
  public String getCookie()
  {
    return null;
  }
  
  public void setCookie(String paramString) {}
  
  public HTMLCollection getImages()
  {
    if (_images == null) {
      _images = new HTMLCollectionImpl(getBody(), (short)3);
    }
    return _images;
  }
  
  public HTMLCollection getApplets()
  {
    if (_applets == null) {
      _applets = new HTMLCollectionImpl(getBody(), (short)4);
    }
    return _applets;
  }
  
  public HTMLCollection getLinks()
  {
    if (_links == null) {
      _links = new HTMLCollectionImpl(getBody(), (short)5);
    }
    return _links;
  }
  
  public HTMLCollection getForms()
  {
    if (_forms == null) {
      _forms = new HTMLCollectionImpl(getBody(), (short)2);
    }
    return _forms;
  }
  
  public HTMLCollection getAnchors()
  {
    if (_anchors == null) {
      _anchors = new HTMLCollectionImpl(getBody(), (short)1);
    }
    return _anchors;
  }
  
  public void open()
  {
    if (_writer == null) {
      _writer = new StringWriter();
    }
  }
  
  public void close()
  {
    if (_writer != null) {
      _writer = null;
    }
  }
  
  public void write(String paramString)
  {
    if (_writer != null) {
      _writer.write(paramString);
    }
  }
  
  public void writeln(String paramString)
  {
    if (_writer != null) {
      _writer.write(paramString + "\n");
    }
  }
  
  public Node cloneNode(boolean paramBoolean)
  {
    HTMLDocumentImpl localHTMLDocumentImpl = new HTMLDocumentImpl();
    callUserDataHandlers(this, localHTMLDocumentImpl, (short)1);
    cloneNode(localHTMLDocumentImpl, paramBoolean);
    return localHTMLDocumentImpl;
  }
  
  protected boolean canRenameElements(String paramString1, String paramString2, ElementImpl paramElementImpl)
  {
    if (paramElementImpl.getNamespaceURI() != null) {
      return paramString1 != null;
    }
    Class localClass1 = (Class)_elementTypesHTML.get(paramString2.toUpperCase(Locale.ENGLISH));
    Class localClass2 = (Class)_elementTypesHTML.get(paramElementImpl.getTagName());
    return localClass1 == localClass2;
  }
  
  private Element getElementById(String paramString, Node paramNode)
  {
    for (Node localNode = paramNode.getFirstChild(); localNode != null; localNode = localNode.getNextSibling()) {
      if ((localNode instanceof Element))
      {
        if (paramString.equals(((Element)localNode).getAttribute("id"))) {
          return (Element)localNode;
        }
        Element localElement = getElementById(paramString, localNode);
        if (localElement != null) {
          return localElement;
        }
      }
    }
    return null;
  }
  
  private static synchronized void populateElementTypes()
  {
    if (_elementTypesHTML != null) {
      return;
    }
    _elementTypesHTML = new Hashtable(63);
    populateElementType("A", "HTMLAnchorElementImpl");
    populateElementType("APPLET", "HTMLAppletElementImpl");
    populateElementType("AREA", "HTMLAreaElementImpl");
    populateElementType("BASE", "HTMLBaseElementImpl");
    populateElementType("BASEFONT", "HTMLBaseFontElementImpl");
    populateElementType("BLOCKQUOTE", "HTMLQuoteElementImpl");
    populateElementType("BODY", "HTMLBodyElementImpl");
    populateElementType("BR", "HTMLBRElementImpl");
    populateElementType("BUTTON", "HTMLButtonElementImpl");
    populateElementType("DEL", "HTMLModElementImpl");
    populateElementType("DIR", "HTMLDirectoryElementImpl");
    populateElementType("DIV", "HTMLDivElementImpl");
    populateElementType("DL", "HTMLDListElementImpl");
    populateElementType("FIELDSET", "HTMLFieldSetElementImpl");
    populateElementType("FONT", "HTMLFontElementImpl");
    populateElementType("FORM", "HTMLFormElementImpl");
    populateElementType("FRAME", "HTMLFrameElementImpl");
    populateElementType("FRAMESET", "HTMLFrameSetElementImpl");
    populateElementType("HEAD", "HTMLHeadElementImpl");
    populateElementType("H1", "HTMLHeadingElementImpl");
    populateElementType("H2", "HTMLHeadingElementImpl");
    populateElementType("H3", "HTMLHeadingElementImpl");
    populateElementType("H4", "HTMLHeadingElementImpl");
    populateElementType("H5", "HTMLHeadingElementImpl");
    populateElementType("H6", "HTMLHeadingElementImpl");
    populateElementType("HR", "HTMLHRElementImpl");
    populateElementType("HTML", "HTMLHtmlElementImpl");
    populateElementType("IFRAME", "HTMLIFrameElementImpl");
    populateElementType("IMG", "HTMLImageElementImpl");
    populateElementType("INPUT", "HTMLInputElementImpl");
    populateElementType("INS", "HTMLModElementImpl");
    populateElementType("ISINDEX", "HTMLIsIndexElementImpl");
    populateElementType("LABEL", "HTMLLabelElementImpl");
    populateElementType("LEGEND", "HTMLLegendElementImpl");
    populateElementType("LI", "HTMLLIElementImpl");
    populateElementType("LINK", "HTMLLinkElementImpl");
    populateElementType("MAP", "HTMLMapElementImpl");
    populateElementType("MENU", "HTMLMenuElementImpl");
    populateElementType("META", "HTMLMetaElementImpl");
    populateElementType("OBJECT", "HTMLObjectElementImpl");
    populateElementType("OL", "HTMLOListElementImpl");
    populateElementType("OPTGROUP", "HTMLOptGroupElementImpl");
    populateElementType("OPTION", "HTMLOptionElementImpl");
    populateElementType("P", "HTMLParagraphElementImpl");
    populateElementType("PARAM", "HTMLParamElementImpl");
    populateElementType("PRE", "HTMLPreElementImpl");
    populateElementType("Q", "HTMLQuoteElementImpl");
    populateElementType("SCRIPT", "HTMLScriptElementImpl");
    populateElementType("SELECT", "HTMLSelectElementImpl");
    populateElementType("STYLE", "HTMLStyleElementImpl");
    populateElementType("TABLE", "HTMLTableElementImpl");
    populateElementType("CAPTION", "HTMLTableCaptionElementImpl");
    populateElementType("TD", "HTMLTableCellElementImpl");
    populateElementType("TH", "HTMLTableCellElementImpl");
    populateElementType("COL", "HTMLTableColElementImpl");
    populateElementType("COLGROUP", "HTMLTableColElementImpl");
    populateElementType("TR", "HTMLTableRowElementImpl");
    populateElementType("TBODY", "HTMLTableSectionElementImpl");
    populateElementType("THEAD", "HTMLTableSectionElementImpl");
    populateElementType("TFOOT", "HTMLTableSectionElementImpl");
    populateElementType("TEXTAREA", "HTMLTextAreaElementImpl");
    populateElementType("TITLE", "HTMLTitleElementImpl");
    populateElementType("UL", "HTMLUListElementImpl");
  }
  
  private static void populateElementType(String paramString1, String paramString2)
  {
    try
    {
      _elementTypesHTML.put(paramString1, ObjectFactory.findProviderClass("org.apache.html.dom." + paramString2, HTMLDocumentImpl.class.getClassLoader(), true));
    }
    catch (Exception localException)
    {
      throw new RuntimeException("HTM019 OpenXML Error: Could not find or execute class " + paramString2 + " implementing HTML element " + paramString1 + "\n" + paramString2 + "\t" + paramString1);
    }
  }
}
