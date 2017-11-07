package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomCDataSection;
import com.gargoylesoftware.htmlunit.html.DomDocumentFragment;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;
import org.apache.commons.lang3.StringUtils;





































@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
public class XMLDOMNode
  extends MSXMLScriptable
{
  private XMLDOMNodeList childNodes_;
  
  public XMLDOMNode() {}
  
  @JsxGetter
  public Object getAttributes()
  {
    return null;
  }
  



  @JsxGetter
  public String getBaseName()
  {
    DomNode domNode = getDomNodeOrDie();
    String baseName = domNode.getLocalName();
    if (baseName == null) {
      return "";
    }
    return baseName;
  }
  



  @JsxGetter
  public XMLDOMNodeList getChildNodes()
  {
    if (childNodes_ == null) {
      final DomNode domNode = getDomNodeOrDie();
      boolean isXmlPage = domNode.getOwnerDocument() instanceof XmlPage;
      Boolean xmlSpaceDefault = isXMLSpaceDefault(domNode);
      final boolean skipEmptyTextNode = (isXmlPage) && (!Boolean.FALSE.equals(xmlSpaceDefault));
      
      childNodes_ = new XMLDOMNodeList(domNode, false, "XMLDOMNode.childNodes")
      {
        protected List<DomNode> computeElements() {
          List<DomNode> response = new ArrayList();
          for (DomNode child : domNode.getChildren())
          {
            if ((!skipEmptyTextNode) || (!(child instanceof DomText)) || ((child instanceof DomCDataSection)) || 
              (!StringUtils.isBlank(((DomText)child).getNodeValue())))
            {

              response.add(child);
            }
          }
          return response;
        }
      };
    }
    return childNodes_;
  }
  
  private static Boolean isXMLSpaceDefault(DomNode node)
  {
    for (; 
        



        (node instanceof DomElement); node = node.getParentNode()) {
      String value = ((DomElement)node).getAttribute("xml:space");
      if (!value.isEmpty()) {
        return Boolean.valueOf("default".equals(value));
      }
    }
    return null;
  }
  



  @JsxGetter
  public String getDataType()
  {
    return null;
  }
  



  @JsxGetter
  public String getDefinition()
  {
    return null;
  }
  



  @JsxGetter
  public XMLDOMNode getFirstChild()
  {
    DomNode domNode = getDomNodeOrDie();
    return getJavaScriptNode(domNode.getFirstChild());
  }
  



  @JsxGetter
  public XMLDOMNode getLastChild()
  {
    DomNode domNode = getDomNodeOrDie();
    return getJavaScriptNode(domNode.getLastChild());
  }
  



  @JsxGetter
  public String getNamespaceURI()
  {
    DomNode domNode = getDomNodeOrDie();
    String namespaceURI = domNode.getNamespaceURI();
    if (namespaceURI == null) {
      return "";
    }
    return namespaceURI;
  }
  



  @JsxGetter
  public XMLDOMNode getNextSibling()
  {
    DomNode domNode = getDomNodeOrDie();
    return getJavaScriptNode(domNode.getNextSibling());
  }
  




  @JsxGetter
  public String getNodeName()
  {
    DomNode domNode = getDomNodeOrDie();
    return domNode.getNodeName();
  }
  




  @JsxGetter
  public short getNodeType()
  {
    DomNode domNode = getDomNodeOrDie();
    return domNode.getNodeType();
  }
  



  @JsxGetter
  public String getNodeValue()
  {
    DomNode domNode = getDomNodeOrDie();
    return domNode.getNodeValue();
  }
  



  @JsxSetter
  public void setNodeValue(String value)
  {
    if ((value == null) || ("null".equals(value))) {
      throw Context.reportRuntimeError("Type mismatch.");
    }
    
    DomNode domNode = getDomNodeOrDie();
    domNode.setNodeValue(value);
  }
  



  @JsxGetter
  public Object getOwnerDocument()
  {
    DomNode domNode = getDomNodeOrDie();
    Object document = domNode.getOwnerDocument();
    if (document == null) {
      return null;
    }
    return ((SgmlPage)document).getScriptableObject();
  }
  



  @JsxGetter
  public Object getParentNode()
  {
    DomNode domNode = getDomNodeOrDie();
    return getJavaScriptNode(domNode.getParentNode());
  }
  



  @JsxGetter
  public String getPrefix()
  {
    DomNode domNode = getDomNodeOrDie();
    String prefix = domNode.getPrefix();
    if ((prefix == null) || (domNode.getHtmlPageOrNull() != null)) {
      return "";
    }
    return prefix;
  }
  



  @JsxGetter
  public XMLDOMNode getPreviousSibling()
  {
    DomNode domNode = getDomNodeOrDie();
    return getJavaScriptNode(domNode.getPreviousSibling());
  }
  



  @JsxGetter
  public Object getText()
  {
    DomNode domNode = getDomNodeOrDie();
    return domNode.getTextContent();
  }
  



  @JsxSetter
  public void setText(Object text)
  {
    DomNode domNode = getDomNodeOrDie();
    domNode.setTextContent(text == null ? null : Context.toString(text));
  }
  



  @JsxGetter
  public Object getXml()
  {
    DomNode domNode = getDomNodeOrDie();
    String xml;
    String xml; if ((this instanceof XMLDOMElement)) {
      boolean preserveWhiteSpace = 
        ((XMLDOMDocument)getOwnerDocument()).isPreserveWhiteSpaceDuringLoad();
      
      XMLSerializer serializer = new XMLSerializer(preserveWhiteSpace);
      xml = serializer.serializeToString(this);
    }
    else {
      xml = domNode.asXml();
    }
    
    if (xml.endsWith("\r\n")) {
      xml = xml.substring(0, xml.length() - 2);
    }
    return xml;
  }
  




  @JsxFunction
  public Object appendChild(Object newChild)
  {
    if ((newChild == null) || ("null".equals(newChild))) {
      throw Context.reportRuntimeError("Type mismatch.");
    }
    
    Object appendedChild = null;
    if ((newChild instanceof XMLDOMNode)) {
      XMLDOMNode childNode = (XMLDOMNode)newChild;
      

      DomNode childDomNode = childNode.getDomNodeOrDie();
      

      DomNode parentNode = getDomNodeOrDie();
      

      parentNode.appendChild(childDomNode);
      appendedChild = newChild;
      


      if ((!(parentNode instanceof SgmlPage)) && 
        (!(this instanceof XMLDOMDocumentFragment)) && (parentNode.getParentNode() == null)) {
        DomDocumentFragment fragment = parentNode.getPage().createDocumentFragment();
        fragment.appendChild(parentNode);
      }
    }
    return appendedChild;
  }
  






  @JsxFunction
  public Object cloneNode(boolean deep)
  {
    DomNode domNode = getDomNodeOrDie();
    DomNode clonedNode = domNode.cloneNode(deep);
    
    XMLDOMNode jsClonedNode = getJavaScriptNode(clonedNode);
    return jsClonedNode;
  }
  



  @JsxFunction
  public boolean hasChildNodes()
  {
    DomNode domNode = getDomNodeOrDie();
    return domNode.getChildren().iterator().hasNext();
  }
  








  @JsxFunction
  public static Object insertBefore(Context context, Scriptable thisObj, Object[] args, Function function)
  {
    return ((XMLDOMNode)thisObj).insertBeforeImpl(args);
  }
  










  protected Object insertBeforeImpl(Object[] args)
  {
    Object newChildObject = args[0];
    Object refChildObject;
    Object refChildObject; if (args.length > 1) {
      refChildObject = args[1];
    }
    else {
      refChildObject = Undefined.instance;
    }
    Object appendedChild = null;
    
    if ((newChildObject instanceof XMLDOMNode)) {
      XMLDOMNode newChild = (XMLDOMNode)newChildObject;
      DomNode newChildNode = newChild.getDomNodeOrDie();
      
      if ((newChildNode instanceof DomDocumentFragment)) {
        DomDocumentFragment fragment = (DomDocumentFragment)newChildNode;
        for (DomNode child : fragment.getChildren()) {
          insertBeforeImpl(new Object[] { child.getScriptableObject(), refChildObject });
        }
        return newChildObject;
      }
      DomNode refChildNode;
      DomNode refChildNode;
      if (refChildObject == Undefined.instance) {
        if (args.length > 1) {
          throw Context.reportRuntimeError("Invalid argument.");
        }
        refChildNode = null;
      } else { DomNode refChildNode;
        if (refChildObject != null) {
          refChildNode = ((XMLDOMNode)refChildObject).getDomNodeOrDie();
        }
        else {
          refChildNode = null;
        }
      }
      DomNode domNode = getDomNodeOrDie();
      
      if (refChildNode != null) {
        refChildNode.insertBefore(newChildNode);
        appendedChild = newChildObject;
      }
      else {
        domNode.appendChild(newChildNode);
        appendedChild = newChildObject;
      }
      

      if (domNode.getParentNode() == null) {
        DomDocumentFragment fragment = domNode.getPage().createDocumentFragment();
        fragment.appendChild(domNode);
      }
    }
    return appendedChild;
  }
  




  @JsxFunction
  public Object removeChild(Object childNode)
  {
    Object removedChild = null;
    
    if ((childNode instanceof XMLDOMNode))
    {
      DomNode childDomNode = ((XMLDOMNode)childNode).getDomNodeOrDie();
      

      childDomNode.remove();
      removedChild = childNode;
    }
    return removedChild;
  }
  






  @JsxFunction
  public Object replaceChild(Object newChild, Object oldChild)
  {
    Object removedChild = null;
    
    if ((newChild instanceof XMLDOMDocumentFragment)) {
      XMLDOMDocumentFragment fragment = (XMLDOMDocumentFragment)newChild;
      XMLDOMNode firstNode = null;
      XMLDOMNode refChildObject = ((XMLDOMNode)oldChild).getNextSibling();
      for (DomNode node : fragment.getDomNodeOrDie().getChildren()) {
        if (firstNode == null) {
          replaceChild(node.getScriptableObject(), oldChild);
          firstNode = (XMLDOMNode)node.getScriptableObject();
        }
        else {
          insertBeforeImpl(new Object[] { node.getScriptableObject(), refChildObject });
        }
      }
      if (firstNode == null) {
        removeChild(oldChild);
      }
      removedChild = oldChild;
    }
    else if (((newChild instanceof XMLDOMNode)) && ((oldChild instanceof XMLDOMNode))) {
      XMLDOMNode newChildNode = (XMLDOMNode)newChild;
      

      DomNode newChildDomNode = newChildNode.getDomNodeOrDie();
      DomNode oldChildDomNode = ((XMLDOMNode)oldChild).getDomNodeOrDie();
      

      oldChildDomNode.replace(newChildDomNode);
      removedChild = oldChild;
    }
    
    return removedChild;
  }
  





  @JsxFunction
  public XMLDOMSelection selectNodes(final String expression)
  {
    final DomNode domNode = getDomNodeOrDie();
    boolean attributeChangeSensitive = expression.contains("@");
    XMLDOMSelection collection = new XMLDOMSelection(domNode, attributeChangeSensitive, 
      "XMLDOMNode.selectNodes('" + expression + "')")
      {
        protected List<DomNode> computeElements()
        {
          return new ArrayList(domNode.getByXPath(expression));
        }
      };
      return collection;
    }
    





    @JsxFunction
    public Object selectSingleNode(String expression)
    {
      XMLDOMNodeList collection = selectNodes(expression);
      if (collection.getLength() > 0) {
        return collection.get(0, collection);
      }
      return null;
    }
    




    protected XMLDOMNode getJavaScriptNode(DomNode domNode)
    {
      if (domNode == null) {
        return null;
      }
      return (XMLDOMNode)getScriptableFor(domNode);
    }
  }
