package org.apache.xml.dtm.ref;

import java.util.Vector;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMDOMException;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.NodeSet;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;














































public class DTMNodeProxy
  implements Node, Document, Text, Element, Attr, ProcessingInstruction, Comment, DocumentFragment
{
  public DTM dtm;
  int node;
  private static final String EMPTYSTRING = "";
  static final DOMImplementation implementation = new DTMNodeProxyImplementation();
  
  protected String fDocumentURI;
  protected String actualEncoding;
  private String xmlEncoding;
  private boolean xmlStandalone;
  private String xmlVersion;
  
  public DTMNodeProxy(DTM dtm, int node)
  {
    this.dtm = dtm;
    this.node = node;
  }
  





  public final DTM getDTM()
  {
    return dtm;
  }
  





  public final int getDTMNodeNumber()
  {
    return node;
  }
  








  public final boolean equals(Node node)
  {
    try
    {
      DTMNodeProxy dtmp = (DTMNodeProxy)node;
      


      return (node == this.node) && (dtm == dtm);
    }
    catch (ClassCastException cce) {}
    
    return false;
  }
  













  public final boolean equals(Object node)
  {
    try
    {
      return equals((Node)node);
    }
    catch (ClassCastException cce) {}
    
    return false;
  }
  









  public final boolean sameNodeAs(Node other)
  {
    if (!(other instanceof DTMNodeProxy)) {
      return false;
    }
    DTMNodeProxy that = (DTMNodeProxy)other;
    
    return (dtm == dtm) && (node == node);
  }
  





  public final String getNodeName()
  {
    return dtm.getNodeName(node);
  }
  













  public final String getTarget()
  {
    return dtm.getNodeName(node);
  }
  





  public final String getLocalName()
  {
    return dtm.getLocalName(node);
  }
  




  public final String getPrefix()
  {
    return dtm.getPrefix(node);
  }
  






  public final void setPrefix(String prefix)
    throws DOMException
  {
    throw new DTMDOMException((short)7);
  }
  





  public final String getNamespaceURI()
  {
    return dtm.getNamespaceURI(node);
  }
  
















  public final boolean supports(String feature, String version)
  {
    return implementation.hasFeature(feature, version);
  }
  











  public final boolean isSupported(String feature, String version)
  {
    return implementation.hasFeature(feature, version);
  }
  







  public final String getNodeValue()
    throws DOMException
  {
    return dtm.getNodeValue(node);
  }
  




  public final String getStringValue()
    throws DOMException
  {
    return dtm.getStringValue(node).toString();
  }
  






  public final void setNodeValue(String nodeValue)
    throws DOMException
  {
    throw new DTMDOMException((short)7);
  }
  





  public final short getNodeType()
  {
    return dtm.getNodeType(node);
  }
  






  public final Node getParentNode()
  {
    if (getNodeType() == 2) {
      return null;
    }
    int newnode = dtm.getParent(node);
    
    return newnode == -1 ? null : dtm.getNode(newnode);
  }
  






  public final Node getOwnerNode()
  {
    int newnode = dtm.getParent(node);
    
    return newnode == -1 ? null : dtm.getNode(newnode);
  }
  









  public final NodeList getChildNodes()
  {
    return new DTMChildIterNodeList(dtm, node);
  }
  








  public final Node getFirstChild()
  {
    int newnode = dtm.getFirstChild(node);
    
    return newnode == -1 ? null : dtm.getNode(newnode);
  }
  






  public final Node getLastChild()
  {
    int newnode = dtm.getLastChild(node);
    
    return newnode == -1 ? null : dtm.getNode(newnode);
  }
  






  public final Node getPreviousSibling()
  {
    int newnode = dtm.getPreviousSibling(node);
    
    return newnode == -1 ? null : dtm.getNode(newnode);
  }
  







  public final Node getNextSibling()
  {
    if (dtm.getNodeType(node) == 2) {
      return null;
    }
    int newnode = dtm.getNextSibling(node);
    
    return newnode == -1 ? null : dtm.getNode(newnode);
  }
  








  public final NamedNodeMap getAttributes()
  {
    return new DTMNamedNodeMap(dtm, node);
  }
  








  public boolean hasAttribute(String name)
  {
    return -1 != dtm.getAttributeNode(node, null, name);
  }
  









  public boolean hasAttributeNS(String namespaceURI, String localName)
  {
    return -1 != dtm.getAttributeNode(node, namespaceURI, localName);
  }
  






  public final Document getOwnerDocument()
  {
    return (Document)dtm.getNode(dtm.getOwnerDocument(node));
  }
  










  public final Node insertBefore(Node newChild, Node refChild)
    throws DOMException
  {
    throw new DTMDOMException((short)7);
  }
  










  public final Node replaceChild(Node newChild, Node oldChild)
    throws DOMException
  {
    throw new DTMDOMException((short)7);
  }
  








  public final Node removeChild(Node oldChild)
    throws DOMException
  {
    throw new DTMDOMException((short)7);
  }
  








  public final Node appendChild(Node newChild)
    throws DOMException
  {
    throw new DTMDOMException((short)7);
  }
  





  public final boolean hasChildNodes()
  {
    return -1 != dtm.getFirstChild(node);
  }
  







  public final Node cloneNode(boolean deep)
  {
    throw new DTMDOMException((short)9);
  }
  





  public final DocumentType getDoctype()
  {
    return null;
  }
  





  public final DOMImplementation getImplementation()
  {
    return implementation;
  }
  







  public final Element getDocumentElement()
  {
    int dochandle = dtm.getDocument();
    int elementhandle = -1;
    for (int kidhandle = dtm.getFirstChild(dochandle); 
        kidhandle != -1; 
        kidhandle = dtm.getNextSibling(kidhandle))
    {
      switch (dtm.getNodeType(kidhandle))
      {
      case 1: 
        if (elementhandle != -1)
        {
          elementhandle = -1;
          kidhandle = dtm.getLastChild(dochandle);
        }
        else {
          elementhandle = kidhandle; }
        break;
      case 7: case 8: 
      case 10: 
        break;
      case 2: case 3: 
      case 4: case 5: 
      case 6: 
      case 9: 
      default: 
        elementhandle = -1;
        kidhandle = dtm.getLastChild(dochandle);
      }
      
    }
    if (elementhandle == -1) {
      throw new DTMDOMException((short)9);
    }
    return (Element)dtm.getNode(elementhandle);
  }
  








  public final Element createElement(String tagName)
    throws DOMException
  {
    throw new DTMDOMException((short)9);
  }
  





  public final DocumentFragment createDocumentFragment()
  {
    throw new DTMDOMException((short)9);
  }
  







  public final Text createTextNode(String data)
  {
    throw new DTMDOMException((short)9);
  }
  







  public final Comment createComment(String data)
  {
    throw new DTMDOMException((short)9);
  }
  









  public final CDATASection createCDATASection(String data)
    throws DOMException
  {
    throw new DTMDOMException((short)9);
  }
  










  public final ProcessingInstruction createProcessingInstruction(String target, String data)
    throws DOMException
  {
    throw new DTMDOMException((short)9);
  }
  








  public final Attr createAttribute(String name)
    throws DOMException
  {
    throw new DTMDOMException((short)9);
  }
  









  public final EntityReference createEntityReference(String name)
    throws DOMException
  {
    throw new DTMDOMException((short)9);
  }
  







  public final NodeList getElementsByTagName(String tagname)
  {
    Vector listVector = new Vector();
    Node retNode = dtm.getNode(node);
    if (retNode != null)
    {
      boolean isTagNameWildCard = "*".equals(tagname);
      if (1 == retNode.getNodeType())
      {
        NodeList nodeList = retNode.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++)
        {
          traverseChildren(listVector, nodeList.item(i), tagname, isTagNameWildCard);
        }
      }
      else if (9 == retNode.getNodeType()) {
        traverseChildren(listVector, dtm.getNode(node), tagname, isTagNameWildCard);
      }
    }
    
    int size = listVector.size();
    NodeSet nodeSet = new NodeSet(size);
    for (int i = 0; i < size; i++)
    {
      nodeSet.addNode((Node)listVector.elementAt(i));
    }
    return nodeSet;
  }
  













  private final void traverseChildren(Vector listVector, Node tempNode, String tagname, boolean isTagNameWildCard)
  {
    if (tempNode == null)
    {
      return;
    }
    

    if ((tempNode.getNodeType() == 1) && ((isTagNameWildCard) || (tempNode.getNodeName().equals(tagname))))
    {

      listVector.add(tempNode);
    }
    if (tempNode.hasChildNodes())
    {
      NodeList nodeList = tempNode.getChildNodes();
      for (int i = 0; i < nodeList.getLength(); i++)
      {
        traverseChildren(listVector, nodeList.item(i), tagname, isTagNameWildCard);
      }
    }
  }
  












  public final Node importNode(Node importedNode, boolean deep)
    throws DOMException
  {
    throw new DTMDOMException((short)7);
  }
  










  public final Element createElementNS(String namespaceURI, String qualifiedName)
    throws DOMException
  {
    throw new DTMDOMException((short)9);
  }
  










  public final Attr createAttributeNS(String namespaceURI, String qualifiedName)
    throws DOMException
  {
    throw new DTMDOMException((short)9);
  }
  









  public final NodeList getElementsByTagNameNS(String namespaceURI, String localName)
  {
    Vector listVector = new Vector();
    Node retNode = dtm.getNode(node);
    if (retNode != null)
    {
      boolean isNamespaceURIWildCard = "*".equals(namespaceURI);
      boolean isLocalNameWildCard = "*".equals(localName);
      if (1 == retNode.getNodeType())
      {
        NodeList nodeList = retNode.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++)
        {
          traverseChildren(listVector, nodeList.item(i), namespaceURI, localName, isNamespaceURIWildCard, isLocalNameWildCard);
        }
      }
      else if (9 == retNode.getNodeType())
      {
        traverseChildren(listVector, dtm.getNode(node), namespaceURI, localName, isNamespaceURIWildCard, isLocalNameWildCard);
      }
    }
    int size = listVector.size();
    NodeSet nodeSet = new NodeSet(size);
    for (int i = 0; i < size; i++)
    {
      nodeSet.addNode((Node)listVector.elementAt(i));
    }
    return nodeSet;
  }
  


















  private final void traverseChildren(Vector listVector, Node tempNode, String namespaceURI, String localname, boolean isNamespaceURIWildCard, boolean isLocalNameWildCard)
  {
    if (tempNode == null)
    {
      return;
    }
    

    if ((tempNode.getNodeType() == 1) && ((isLocalNameWildCard) || (tempNode.getLocalName().equals(localname))))
    {


      String nsURI = tempNode.getNamespaceURI();
      if (((namespaceURI == null) && (nsURI == null)) || (isNamespaceURIWildCard) || ((namespaceURI != null) && (namespaceURI.equals(nsURI))))
      {


        listVector.add(tempNode);
      }
    }
    if (tempNode.hasChildNodes())
    {
      NodeList nl = tempNode.getChildNodes();
      for (int i = 0; i < nl.getLength(); i++)
      {
        traverseChildren(listVector, nl.item(i), namespaceURI, localname, isNamespaceURIWildCard, isLocalNameWildCard);
      }
    }
  }
  








  public final Element getElementById(String elementId)
  {
    return (Element)dtm.getNode(dtm.getElementById(elementId));
  }
  








  public final Text splitText(int offset)
    throws DOMException
  {
    throw new DTMDOMException((short)9);
  }
  






  public final String getData()
    throws DOMException
  {
    return dtm.getNodeValue(node);
  }
  






  public final void setData(String data)
    throws DOMException
  {
    throw new DTMDOMException((short)9);
  }
  






  public final int getLength()
  {
    return dtm.getNodeValue(node).length();
  }
  









  public final String substringData(int offset, int count)
    throws DOMException
  {
    return getData().substring(offset, offset + count);
  }
  






  public final void appendData(String arg)
    throws DOMException
  {
    throw new DTMDOMException((short)9);
  }
  







  public final void insertData(int offset, String arg)
    throws DOMException
  {
    throw new DTMDOMException((short)9);
  }
  







  public final void deleteData(int offset, int count)
    throws DOMException
  {
    throw new DTMDOMException((short)9);
  }
  









  public final void replaceData(int offset, int count, String arg)
    throws DOMException
  {
    throw new DTMDOMException((short)9);
  }
  





  public final String getTagName()
  {
    return dtm.getNodeName(node);
  }
  








  public final String getAttribute(String name)
  {
    DTMNamedNodeMap map = new DTMNamedNodeMap(dtm, this.node);
    Node node = map.getNamedItem(name);
    return null == node ? "" : node.getNodeValue();
  }
  








  public final void setAttribute(String name, String value)
    throws DOMException
  {
    throw new DTMDOMException((short)9);
  }
  






  public final void removeAttribute(String name)
    throws DOMException
  {
    throw new DTMDOMException((short)9);
  }
  








  public final Attr getAttributeNode(String name)
  {
    DTMNamedNodeMap map = new DTMNamedNodeMap(dtm, node);
    return (Attr)map.getNamedItem(name);
  }
  








  public final Attr setAttributeNode(Attr newAttr)
    throws DOMException
  {
    throw new DTMDOMException((short)9);
  }
  








  public final Attr removeAttributeNode(Attr oldAttr)
    throws DOMException
  {
    throw new DTMDOMException((short)9);
  }
  





  public boolean hasAttributes()
  {
    return -1 != dtm.getFirstAttribute(node);
  }
  

  public final void normalize()
  {
    throw new DTMDOMException((short)9);
  }
  








  public final String getAttributeNS(String namespaceURI, String localName)
  {
    Node retNode = null;
    int n = dtm.getAttributeNode(node, namespaceURI, localName);
    if (n != -1)
      retNode = dtm.getNode(n);
    return null == retNode ? "" : retNode.getNodeValue();
  }
  










  public final void setAttributeNS(String namespaceURI, String qualifiedName, String value)
    throws DOMException
  {
    throw new DTMDOMException((short)9);
  }
  








  public final void removeAttributeNS(String namespaceURI, String localName)
    throws DOMException
  {
    throw new DTMDOMException((short)9);
  }
  








  public final Attr getAttributeNodeNS(String namespaceURI, String localName)
  {
    Attr retAttr = null;
    int n = dtm.getAttributeNode(node, namespaceURI, localName);
    if (n != -1)
      retAttr = (Attr)dtm.getNode(n);
    return retAttr;
  }
  








  public final Attr setAttributeNodeNS(Attr newAttr)
    throws DOMException
  {
    throw new DTMDOMException((short)9);
  }
  





  public final String getName()
  {
    return dtm.getNodeName(node);
  }
  









  public final boolean getSpecified()
  {
    return true;
  }
  





  public final String getValue()
  {
    return dtm.getNodeValue(node);
  }
  





  public final void setValue(String value)
  {
    throw new DTMDOMException((short)9);
  }
  






  public final Element getOwnerElement()
  {
    if (getNodeType() != 2) {
      return null;
    }
    
    int newnode = dtm.getParent(node);
    return newnode == -1 ? null : (Element)dtm.getNode(newnode);
  }
  










  public Node adoptNode(Node source)
    throws DOMException
  {
    throw new DTMDOMException((short)9);
  }
  












  public String getInputEncoding()
  {
    throw new DTMDOMException((short)9);
  }
  
















  public boolean getStrictErrorChecking()
  {
    throw new DTMDOMException((short)9);
  }
  















  public void setStrictErrorChecking(boolean strictErrorChecking)
  {
    throw new DTMDOMException((short)9);
  }
  
  static class DTMNodeProxyImplementation implements DOMImplementation
  {
    DTMNodeProxyImplementation() {}
    
    public DocumentType createDocumentType(String qualifiedName, String publicId, String systemId)
    {
      throw new DTMDOMException((short)9);
    }
    
    public Document createDocument(String namespaceURI, String qualfiedName, DocumentType doctype)
    {
      throw new DTMDOMException((short)9);
    }
    








    public boolean hasFeature(String feature, String version)
    {
      if ((("CORE".equals(feature.toUpperCase())) || ("XML".equals(feature.toUpperCase()))) && (("1.0".equals(version)) || ("2.0".equals(version))))
      {


        return true; }
      return false;
    }
    























    public Object getFeature(String feature, String version)
    {
      return null;
    }
  }
  





  public Object setUserData(String key, Object data, UserDataHandler handler)
  {
    return getOwnerDocument().setUserData(key, data, handler);
  }
  








  public Object getUserData(String key)
  {
    return getOwnerDocument().getUserData(key);
  }
  





















  public Object getFeature(String feature, String version)
  {
    return isSupported(feature, version) ? this : null;
  }
  









































  public boolean isEqualNode(Node arg)
  {
    if (arg == this) {
      return true;
    }
    if (arg.getNodeType() != getNodeType()) {
      return false;
    }
    

    if (getNodeName() == null) {
      if (arg.getNodeName() != null) {
        return false;
      }
    }
    else if (!getNodeName().equals(arg.getNodeName())) {
      return false;
    }
    
    if (getLocalName() == null) {
      if (arg.getLocalName() != null) {
        return false;
      }
    }
    else if (!getLocalName().equals(arg.getLocalName())) {
      return false;
    }
    
    if (getNamespaceURI() == null) {
      if (arg.getNamespaceURI() != null) {
        return false;
      }
    }
    else if (!getNamespaceURI().equals(arg.getNamespaceURI())) {
      return false;
    }
    
    if (getPrefix() == null) {
      if (arg.getPrefix() != null) {
        return false;
      }
    }
    else if (!getPrefix().equals(arg.getPrefix())) {
      return false;
    }
    
    if (getNodeValue() == null) {
      if (arg.getNodeValue() != null) {
        return false;
      }
    }
    else if (!getNodeValue().equals(arg.getNodeValue())) {
      return false;
    }
    









    return true;
  }
  








  public String lookupNamespaceURI(String specifiedPrefix)
  {
    short type = getNodeType();
    switch (type)
    {
    case 1: 
      String namespace = getNamespaceURI();
      String prefix = getPrefix();
      if (namespace != null)
      {
        if ((specifiedPrefix == null) && (prefix == specifiedPrefix))
        {
          return namespace; }
        if ((prefix != null) && (prefix.equals(specifiedPrefix)))
        {
          return namespace;
        }
      }
      if (hasAttributes()) {
        NamedNodeMap map = getAttributes();
        int length = map.getLength();
        for (int i = 0; i < length; i++) {
          Node attr = map.item(i);
          String attrPrefix = attr.getPrefix();
          String value = attr.getNodeValue();
          namespace = attr.getNamespaceURI();
          if ((namespace != null) && (namespace.equals("http://www.w3.org/2000/xmlns/")))
          {
            if ((specifiedPrefix == null) && (attr.getNodeName().equals("xmlns")))
            {

              return value; }
            if ((attrPrefix != null) && (attrPrefix.equals("xmlns")) && (attr.getLocalName().equals(specifiedPrefix)))
            {


              return value;
            }
          }
        }
      }
      





      return null;
    






    case 6: 
    case 10: 
    case 11: 
    case 12: 
      return null;
    case 2: 
      if (getOwnerElement().getNodeType() == 1) {
        return getOwnerElement().lookupNamespaceURI(specifiedPrefix);
      }
      return null;
    }
    
    





    return null;
  }
  







































































  public boolean isDefaultNamespace(String namespaceURI)
  {
    return false;
  }
  









  public String lookupPrefix(String namespaceURI)
  {
    if (namespaceURI == null) {
      return null;
    }
    
    short type = getNodeType();
    
    switch (type)
    {











    case 6: 
    case 10: 
    case 11: 
    case 12: 
      return null;
    case 2: 
      if (getOwnerElement().getNodeType() == 1) {
        return getOwnerElement().lookupPrefix(namespaceURI);
      }
      
      return null;
    }
    
    





    return null;
  }
  
















  public boolean isSameNode(Node other)
  {
    return this == other;
  }
  












































  public void setTextContent(String textContent)
    throws DOMException
  {
    setNodeValue(textContent);
  }
  











































  public String getTextContent()
    throws DOMException
  {
    return dtm.getStringValue(node).toString();
  }
  






  public short compareDocumentPosition(Node other)
    throws DOMException
  {
    return 0;
  }
  























  public String getBaseURI()
  {
    return null;
  }
  





  public Node renameNode(Node n, String namespaceURI, String name)
    throws DOMException
  {
    return n;
  }
  





  public void normalizeDocument() {}
  





  public DOMConfiguration getDomConfig()
  {
    return null;
  }
  






  public void setDocumentURI(String documentURI)
  {
    fDocumentURI = documentURI;
  }
  







  public String getDocumentURI()
  {
    return fDocumentURI;
  }
  










  public String getActualEncoding()
  {
    return actualEncoding;
  }
  







  public void setActualEncoding(String value)
  {
    actualEncoding = value;
  }
  












































  public Text replaceWholeText(String content)
    throws DOMException
  {
    return null;
  }
  




















  public String getWholeText()
  {
    return null;
  }
  




  public boolean isElementContentWhitespace()
  {
    return false;
  }
  





  public void setIdAttribute(boolean id) {}
  





  public void setIdAttribute(String name, boolean makeId) {}
  





  public void setIdAttributeNode(Attr at, boolean makeId) {}
  




  public void setIdAttributeNS(String namespaceURI, String localName, boolean makeId) {}
  




  public TypeInfo getSchemaTypeInfo()
  {
    return null;
  }
  
  public boolean isId() {
    return false;
  }
  


  public String getXmlEncoding()
  {
    return xmlEncoding;
  }
  
  public void setXmlEncoding(String xmlEncoding) {
    this.xmlEncoding = xmlEncoding;
  }
  

  public boolean getXmlStandalone()
  {
    return xmlStandalone;
  }
  
  public void setXmlStandalone(boolean xmlStandalone) throws DOMException {
    this.xmlStandalone = xmlStandalone;
  }
  

  public String getXmlVersion()
  {
    return xmlVersion;
  }
  
  public void setXmlVersion(String xmlVersion) throws DOMException {
    this.xmlVersion = xmlVersion;
  }
}
