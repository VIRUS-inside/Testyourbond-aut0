package org.apache.xpath.domapi;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;
import org.w3c.dom.xpath.XPathNamespace;































































class XPathNamespaceImpl
  implements XPathNamespace
{
  private final Node m_attributeNode;
  private String textContent;
  
  XPathNamespaceImpl(Node node)
  {
    m_attributeNode = node;
  }
  


  public Element getOwnerElement()
  {
    return ((Attr)m_attributeNode).getOwnerElement();
  }
  


  public String getNodeName()
  {
    return "#namespace";
  }
  

  public String getNodeValue()
    throws DOMException
  {
    return m_attributeNode.getNodeValue();
  }
  


  public void setNodeValue(String arg0)
    throws DOMException
  {}
  


  public short getNodeType()
  {
    return 13;
  }
  


  public Node getParentNode()
  {
    return m_attributeNode.getParentNode();
  }
  


  public NodeList getChildNodes()
  {
    return m_attributeNode.getChildNodes();
  }
  


  public Node getFirstChild()
  {
    return m_attributeNode.getFirstChild();
  }
  


  public Node getLastChild()
  {
    return m_attributeNode.getLastChild();
  }
  


  public Node getPreviousSibling()
  {
    return m_attributeNode.getPreviousSibling();
  }
  


  public Node getNextSibling()
  {
    return m_attributeNode.getNextSibling();
  }
  


  public NamedNodeMap getAttributes()
  {
    return m_attributeNode.getAttributes();
  }
  


  public Document getOwnerDocument()
  {
    return m_attributeNode.getOwnerDocument();
  }
  

  public Node insertBefore(Node arg0, Node arg1)
    throws DOMException
  {
    return null;
  }
  

  public Node replaceChild(Node arg0, Node arg1)
    throws DOMException
  {
    return null;
  }
  

  public Node removeChild(Node arg0)
    throws DOMException
  {
    return null;
  }
  

  public Node appendChild(Node arg0)
    throws DOMException
  {
    return null;
  }
  


  public boolean hasChildNodes()
  {
    return false;
  }
  


  public Node cloneNode(boolean arg0)
  {
    throw new DOMException((short)9, null);
  }
  


  public void normalize()
  {
    m_attributeNode.normalize();
  }
  


  public boolean isSupported(String arg0, String arg1)
  {
    return m_attributeNode.isSupported(arg0, arg1);
  }
  





  public String getNamespaceURI()
  {
    return m_attributeNode.getNodeValue();
  }
  


  public String getPrefix()
  {
    return m_attributeNode.getPrefix();
  }
  



  public void setPrefix(String arg0)
    throws DOMException
  {}
  



  public String getLocalName()
  {
    return m_attributeNode.getPrefix();
  }
  


  public boolean hasAttributes()
  {
    return m_attributeNode.hasAttributes();
  }
  
  public String getBaseURI() {
    return null;
  }
  
  public short compareDocumentPosition(Node other) throws DOMException {
    return 0;
  }
  
  public String getTextContent()
    throws DOMException
  {
    return textContent;
  }
  
  public void setTextContent(String textContent) throws DOMException {
    this.textContent = textContent;
  }
  
  public boolean isSameNode(Node other) {
    return false;
  }
  
  public String lookupPrefix(String namespaceURI) {
    return "";
  }
  
  public boolean isDefaultNamespace(String namespaceURI) {
    return false;
  }
  
  public String lookupNamespaceURI(String prefix) {
    return null;
  }
  
  public boolean isEqualNode(Node arg) {
    return false;
  }
  
  public Object getFeature(String feature, String version) {
    return null;
  }
  

  public Object setUserData(String key, Object data, UserDataHandler handler)
  {
    return null;
  }
  
  public Object getUserData(String key) {
    return null;
  }
}
