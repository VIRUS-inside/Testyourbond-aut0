package com.gargoylesoftware.htmlunit.javascript.host.dom;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomDocumentFragment;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLHtmlElement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Interpreter;
import net.sourceforge.htmlunit.corejs.javascript.JavaScriptException;
import net.sourceforge.htmlunit.corejs.javascript.RhinoException;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;
import org.apache.commons.lang3.StringUtils;


















































































@JsxClass
public class Node
  extends EventTarget
{
  @JsxConstant
  public static final short ELEMENT_NODE = 1;
  @JsxConstant
  public static final short ATTRIBUTE_NODE = 2;
  @JsxConstant
  public static final short TEXT_NODE = 3;
  @JsxConstant
  public static final short CDATA_SECTION_NODE = 4;
  @JsxConstant
  public static final short ENTITY_REFERENCE_NODE = 5;
  @JsxConstant
  public static final short ENTITY_NODE = 6;
  @JsxConstant
  public static final short PROCESSING_INSTRUCTION_NODE = 7;
  @JsxConstant
  public static final short COMMENT_NODE = 8;
  @JsxConstant
  public static final short DOCUMENT_NODE = 9;
  @JsxConstant
  public static final short DOCUMENT_TYPE_NODE = 10;
  @JsxConstant
  public static final short DOCUMENT_FRAGMENT_NODE = 11;
  @JsxConstant
  public static final short NOTATION_NODE = 12;
  @JsxConstant
  public static final short DOCUMENT_POSITION_DISCONNECTED = 1;
  @JsxConstant
  public static final short DOCUMENT_POSITION_PRECEDING = 2;
  @JsxConstant
  public static final short DOCUMENT_POSITION_FOLLOWING = 4;
  @JsxConstant
  public static final short DOCUMENT_POSITION_CONTAINS = 8;
  @JsxConstant
  public static final short DOCUMENT_POSITION_CONTAINED_BY = 16;
  @JsxConstant
  public static final short DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC = 32;
  private NodeList childNodes_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public Node() {}
  
  @JsxGetter
  public short getNodeType()
  {
    return getDomNodeOrDie().getNodeType();
  }
  



  @JsxGetter
  public String getNodeName()
  {
    return getDomNodeOrDie().getNodeName();
  }
  



  @JsxGetter
  public String getNodeValue()
  {
    return getDomNodeOrDie().getNodeValue();
  }
  



  @JsxSetter
  public void setNodeValue(String newValue)
  {
    getDomNodeOrDie().setNodeValue(newValue);
  }
  




  @JsxFunction
  public Object appendChild(Object childObject)
  {
    Object appendedChild = null;
    if ((childObject instanceof Node)) {
      Node childNode = (Node)childObject;
      

      if (!isNodeInsertable(childNode)) {
        throw asJavaScriptException(
          new DOMException("Node cannot be inserted at the specified point in the hierarchy", 
          (short)3));
      }
      

      DomNode childDomNode = childNode.getDomNodeOrDie();
      

      DomNode parentNode = getDomNodeOrDie();
      

      parentNode.appendChild(childDomNode);
      appendedChild = childObject;
      
      initInlineFrameIfNeeded(childDomNode);
      for (DomNode domNode : childDomNode.getDescendants()) {
        initInlineFrameIfNeeded(domNode);
      }
    }
    return appendedChild;
  }
  






  private static void initInlineFrameIfNeeded(DomNode childDomNode)
  {
    if ((childDomNode instanceof HtmlInlineFrame)) {
      HtmlInlineFrame frame = (HtmlInlineFrame)childDomNode;
      if (DomElement.ATTRIBUTE_NOT_DEFINED == frame.getSrcAttribute()) {
        frame.loadInnerPage();
      }
    }
  }
  





  protected RhinoException asJavaScriptException(DOMException exception)
  {
    exception.setPrototype(getWindow().getPrototype(exception.getClass()));
    exception.setParentScope(getWindow());
    


    int lineNumber;
    

    if (Context.getCurrentContext().getOptimizationLevel() == -1) {
      int[] linep = new int[1];
      String sourceName = new Interpreter().getSourcePositionFromStack(Context.getCurrentContext(), linep);
      String fileName = sourceName.replaceFirst("script in (.*) from .*", "$1");
      lineNumber = linep[0];
    }
    else {
      throw new Error("HtmlUnit not ready to run in compiled mode"); }
    int lineNumber;
    String fileName;
    exception.setLocation(fileName, lineNumber);
    
    return new JavaScriptException(exception, fileName, lineNumber);
  }
  









  @JsxFunction
  public static Object insertBefore(Context context, Scriptable thisObj, Object[] args, Function function)
  {
    return ((Node)thisObj).insertBeforeImpl(args);
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
    Object insertedChild = null;
    
    if ((newChildObject instanceof Node)) {
      Node newChild = (Node)newChildObject;
      DomNode newChildNode = newChild.getDomNodeOrDie();
      

      if (!isNodeInsertable(newChild)) {
        throw asJavaScriptException(
          new DOMException("Node cannot be inserted at the specified point in the hierarchy", 
          (short)3));
      }
      if ((newChildNode instanceof DomDocumentFragment)) {
        DomDocumentFragment fragment = (DomDocumentFragment)newChildNode;
        for (DomNode child : fragment.getChildren()) {
          if (!isNodeInsertable((Node)child.getScriptableObject())) {
            throw asJavaScriptException(
              new DOMException("Node cannot be inserted at the specified point in the hierarchy", 
              (short)3));
          }
        }
      }
      
      DomNode refChildNode;
      
      if (refChildObject == Undefined.instance) { DomNode refChildNode;
        if ((args.length == 2) || (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_NODE_INSERT_BEFORE_REF_OPTIONAL))) {
          refChildNode = null;
        }
        else
          throw Context.reportRuntimeError("insertBefore: not enough arguments");
      } else {
        DomNode refChildNode;
        if (refChildObject != null) {
          refChildNode = ((Node)refChildObject).getDomNodeOrDie();
        }
        else {
          refChildNode = null;
        }
      }
      DomNode domNode = getDomNodeOrDie();
      try
      {
        domNode.insertBefore(newChildNode, refChildNode);
      }
      catch (org.w3c.dom.DOMException e) {
        throw asJavaScriptException(
          new DOMException(e.getMessage(), 
          (short)3));
      }
      insertedChild = newChild;
    }
    return insertedChild;
  }
  




  private static boolean isNodeInsertable(Node childObject)
  {
    if ((childObject instanceof HTMLHtmlElement)) {
      DomNode domNode = childObject.getDomNodeOrDie();
      return domNode.getPage().getDocumentElement() != domNode;
    }
    return true;
  }
  



  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public void remove()
  {
    getDomNodeOrDie().remove();
  }
  




  @JsxFunction
  public Object removeChild(Object childObject)
  {
    if (!(childObject instanceof Node)) {
      return null;
    }
    

    DomNode childNode = ((Node)childObject).getDomNodeOrDie();
    
    if (!getDomNodeOrDie().isAncestorOf(childNode)) {
      Context.throwAsScriptRuntimeEx(new Exception("NotFoundError: Failed to execute 'removeChild' on '" + 
        this + "': The node to be removed is not a child of this node."));
    }
    
    childNode.remove();
    return childObject;
  }
  





  @JsxFunction
  public Object replaceChild(Object newChildObject, Object oldChildObject)
  {
    Object removedChild = null;
    
    if ((newChildObject instanceof DocumentFragment)) {
      DocumentFragment fragment = (DocumentFragment)newChildObject;
      Node firstNode = null;
      Node refChildObject = ((Node)oldChildObject).getNextSibling();
      for (DomNode node : fragment.getDomNodeOrDie().getChildren()) {
        if (firstNode == null) {
          replaceChild(node.getScriptableObject(), oldChildObject);
          firstNode = (Node)node.getScriptableObject();
        }
        else {
          insertBeforeImpl(new Object[] { node.getScriptableObject(), refChildObject });
        }
      }
      if (firstNode == null) {
        removeChild(oldChildObject);
      }
      removedChild = oldChildObject;
    }
    else if (((newChildObject instanceof Node)) && ((oldChildObject instanceof Node))) {
      Node newChild = (Node)newChildObject;
      

      if (!isNodeInsertable(newChild)) {
        throw Context.reportRuntimeError("Node cannot be inserted at the specified point in the hierarchy");
      }
      

      DomNode newChildNode = newChild.getDomNodeOrDie();
      DomNode oldChildNode = ((Node)oldChildObject).getDomNodeOrDie();
      

      oldChildNode.replace(newChildNode);
      removedChild = oldChildObject;
    }
    
    return removedChild;
  }
  




  @JsxFunction
  public Object cloneNode(boolean deep)
  {
    DomNode domNode = getDomNodeOrDie();
    DomNode clonedNode = domNode.cloneNode(deep);
    
    Node jsClonedNode = getJavaScriptNode(clonedNode);
    return jsClonedNode;
  }
  










  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public boolean isSameNode(Object other)
  {
    return other == this;
  }
  



  @JsxFunction
  public boolean hasChildNodes()
  {
    return getDomNodeOrDie().getChildren().iterator().hasNext();
  }
  



  @JsxGetter
  public NodeList getChildNodes()
  {
    if (childNodes_ == null) {
      final DomNode node = getDomNodeOrDie();
      childNodes_ = new NodeList(node, false)
      {
        protected List<Object> computeElements() {
          List<Object> response = new ArrayList();
          for (DomNode child : node.getChildren()) {
            response.add(child);
          }
          
          return response;
        }
      };
    }
    return childNodes_;
  }
  



  public final Node getParent()
  {
    return getJavaScriptNode(getDomNodeOrDie().getParentNode());
  }
  




  @JsxGetter
  public Object getParentNode()
  {
    return getJavaScriptNode(getDomNodeOrDie().getParentNode());
  }
  





  @JsxGetter
  public Node getNextSibling()
  {
    return getJavaScriptNode(getDomNodeOrDie().getNextSibling());
  }
  





  @JsxGetter
  public Node getPreviousSibling()
  {
    return getJavaScriptNode(getDomNodeOrDie().getPreviousSibling());
  }
  





  @JsxGetter
  public Node getFirstChild()
  {
    return getJavaScriptNode(getDomNodeOrDie().getFirstChild());
  }
  





  @JsxGetter
  public Node getLastChild()
  {
    return getJavaScriptNode(getDomNodeOrDie().getLastChild());
  }
  




  protected Node getJavaScriptNode(DomNode domNode)
  {
    if (domNode == null) {
      return null;
    }
    return (Node)getScriptableFor(domNode);
  }
  





  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void detachEvent(String type, Function listener)
  {
    removeEventListener(StringUtils.substring(type, 2), listener, false);
  }
  



  @JsxGetter
  public Object getOwnerDocument()
  {
    Object document = getDomNodeOrDie().getOwnerDocument();
    if (document != null) {
      return ((SgmlPage)document).getScriptableObject();
    }
    return null;
  }
  



  @JsxGetter
  public Object getPrefix()
  {
    DomNode domNode = getDomNodeOrDie();
    return domNode.getPrefix();
  }
  



  @JsxGetter
  public Object getLocalName()
  {
    return getDomNodeOrDie().getLocalName();
  }
  



  @JsxGetter
  public Object getNamespaceURI()
  {
    return getDomNodeOrDie().getNamespaceURI();
  }
  






  @JsxFunction
  public short compareDocumentPosition(Object node)
  {
    if (!(node instanceof Node)) {
      throw Context.reportRuntimeError("Could not convert JavaScript argument arg 0");
    }
    return getDomNodeOrDie().compareDocumentPosition(((Node)node).getDomNodeOrDie());
  }
  


  @JsxFunction
  public void normalize()
  {
    getDomNodeOrDie().normalize();
  }
  



  @JsxGetter
  public String getTextContent()
  {
    return getDomNodeOrDie().getTextContent();
  }
  



  @JsxSetter
  public void setTextContent(Object value)
  {
    getDomNodeOrDie().setTextContent(value == null ? null : Context.toString(value));
  }
  




  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public Element getParentElement()
  {
    Node parent = getParent();
    if (!(parent instanceof Element)) {
      return null;
    }
    return (Element)parent;
  }
  




  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(value=com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF, maxVersion=21)})
  public Object getAttributes()
  {
    return null;
  }
  




  @JsxFunction
  public boolean contains(Object element)
  {
    if (!(element instanceof Node)) {
      if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_NODE_CONTAINS_RETURNS_FALSE_FOR_INVALID_ARG)) {
        return false;
      }
      throw Context.reportRuntimeError("Could not convert JavaScript argument arg 0");
    }
    
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_NODE_CONTAINS_RETURNS_FALSE_FOR_INVALID_ARG)) {
      if ((element instanceof CharacterData)) {
        return false;
      }
      if ((this instanceof CharacterData)) {
        throw Context.reportRuntimeError("Function 'contains' not available for text nodes.");
      }
    }
    
    for (Node parent = (Node)element; parent != null; parent = parent.getParentElement()) {
      if (this == parent) {
        return true;
      }
    }
    return false;
  }
}
