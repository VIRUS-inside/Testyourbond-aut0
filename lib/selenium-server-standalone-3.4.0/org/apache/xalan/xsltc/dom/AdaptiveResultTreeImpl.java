package org.apache.xalan.xsltc.dom;

import javax.xml.transform.SourceLocator;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.StripFilter;
import org.apache.xalan.xsltc.TransletException;
import org.apache.xalan.xsltc.runtime.AttributeList;
import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xalan.xsltc.runtime.Hashtable;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.DTMAxisTraverser;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.XMLString;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;


















































public class AdaptiveResultTreeImpl
  extends SimpleResultTreeImpl
{
  private static int _documentURIIndex = 0;
  


  private SAXImpl _dom;
  


  private DTMWSFilter _wsfilter;
  

  private int _initSize;
  

  private boolean _buildIdIndex;
  

  private final AttributeList _attributes = new AttributeList();
  


  private String _openElementName;
  



  public AdaptiveResultTreeImpl(XSLTCDTMManager dtmManager, int documentID, DTMWSFilter wsfilter, int initSize, boolean buildIdIndex)
  {
    super(dtmManager, documentID);
    
    _wsfilter = wsfilter;
    _initSize = initSize;
    _buildIdIndex = buildIdIndex;
  }
  

  public DOM getNestedDOM()
  {
    return _dom;
  }
  

  public int getDocument()
  {
    if (_dom != null) {
      return _dom.getDocument();
    }
    
    return super.getDocument();
  }
  


  public String getStringValue()
  {
    if (_dom != null) {
      return _dom.getStringValue();
    }
    
    return super.getStringValue();
  }
  

  public DTMAxisIterator getIterator()
  {
    if (_dom != null) {
      return _dom.getIterator();
    }
    
    return super.getIterator();
  }
  

  public DTMAxisIterator getChildren(int node)
  {
    if (_dom != null) {
      return _dom.getChildren(node);
    }
    
    return super.getChildren(node);
  }
  

  public DTMAxisIterator getTypedChildren(int type)
  {
    if (_dom != null) {
      return _dom.getTypedChildren(type);
    }
    
    return super.getTypedChildren(type);
  }
  

  public DTMAxisIterator getAxisIterator(int axis)
  {
    if (_dom != null) {
      return _dom.getAxisIterator(axis);
    }
    
    return super.getAxisIterator(axis);
  }
  

  public DTMAxisIterator getTypedAxisIterator(int axis, int type)
  {
    if (_dom != null) {
      return _dom.getTypedAxisIterator(axis, type);
    }
    
    return super.getTypedAxisIterator(axis, type);
  }
  

  public DTMAxisIterator getNthDescendant(int node, int n, boolean includeself)
  {
    if (_dom != null) {
      return _dom.getNthDescendant(node, n, includeself);
    }
    
    return super.getNthDescendant(node, n, includeself);
  }
  

  public DTMAxisIterator getNamespaceAxisIterator(int axis, int ns)
  {
    if (_dom != null) {
      return _dom.getNamespaceAxisIterator(axis, ns);
    }
    
    return super.getNamespaceAxisIterator(axis, ns);
  }
  


  public DTMAxisIterator getNodeValueIterator(DTMAxisIterator iter, int returnType, String value, boolean op)
  {
    if (_dom != null) {
      return _dom.getNodeValueIterator(iter, returnType, value, op);
    }
    
    return super.getNodeValueIterator(iter, returnType, value, op);
  }
  

  public DTMAxisIterator orderNodes(DTMAxisIterator source, int node)
  {
    if (_dom != null) {
      return _dom.orderNodes(source, node);
    }
    
    return super.orderNodes(source, node);
  }
  

  public String getNodeName(int node)
  {
    if (_dom != null) {
      return _dom.getNodeName(node);
    }
    
    return super.getNodeName(node);
  }
  

  public String getNodeNameX(int node)
  {
    if (_dom != null) {
      return _dom.getNodeNameX(node);
    }
    
    return super.getNodeNameX(node);
  }
  

  public String getNamespaceName(int node)
  {
    if (_dom != null) {
      return _dom.getNamespaceName(node);
    }
    
    return super.getNamespaceName(node);
  }
  


  public int getExpandedTypeID(int nodeHandle)
  {
    if (_dom != null) {
      return _dom.getExpandedTypeID(nodeHandle);
    }
    
    return super.getExpandedTypeID(nodeHandle);
  }
  

  public int getNamespaceType(int node)
  {
    if (_dom != null) {
      return _dom.getNamespaceType(node);
    }
    
    return super.getNamespaceType(node);
  }
  

  public int getParent(int nodeHandle)
  {
    if (_dom != null) {
      return _dom.getParent(nodeHandle);
    }
    
    return super.getParent(nodeHandle);
  }
  

  public int getAttributeNode(int gType, int element)
  {
    if (_dom != null) {
      return _dom.getAttributeNode(gType, element);
    }
    
    return super.getAttributeNode(gType, element);
  }
  

  public String getStringValueX(int nodeHandle)
  {
    if (_dom != null) {
      return _dom.getStringValueX(nodeHandle);
    }
    
    return super.getStringValueX(nodeHandle);
  }
  

  public void copy(int node, SerializationHandler handler)
    throws TransletException
  {
    if (_dom != null) {
      _dom.copy(node, handler);
    }
    else {
      super.copy(node, handler);
    }
  }
  
  public void copy(DTMAxisIterator nodes, SerializationHandler handler)
    throws TransletException
  {
    if (_dom != null) {
      _dom.copy(nodes, handler);
    }
    else {
      super.copy(nodes, handler);
    }
  }
  
  public String shallowCopy(int node, SerializationHandler handler)
    throws TransletException
  {
    if (_dom != null) {
      return _dom.shallowCopy(node, handler);
    }
    
    return super.shallowCopy(node, handler);
  }
  

  public boolean lessThan(int node1, int node2)
  {
    if (_dom != null) {
      return _dom.lessThan(node1, node2);
    }
    
    return super.lessThan(node1, node2);
  }
  







  public void characters(int node, SerializationHandler handler)
    throws TransletException
  {
    if (_dom != null) {
      _dom.characters(node, handler);
    }
    else {
      super.characters(node, handler);
    }
  }
  
  public Node makeNode(int index)
  {
    if (_dom != null) {
      return _dom.makeNode(index);
    }
    
    return super.makeNode(index);
  }
  

  public Node makeNode(DTMAxisIterator iter)
  {
    if (_dom != null) {
      return _dom.makeNode(iter);
    }
    
    return super.makeNode(iter);
  }
  

  public NodeList makeNodeList(int index)
  {
    if (_dom != null) {
      return _dom.makeNodeList(index);
    }
    
    return super.makeNodeList(index);
  }
  

  public NodeList makeNodeList(DTMAxisIterator iter)
  {
    if (_dom != null) {
      return _dom.makeNodeList(iter);
    }
    
    return super.makeNodeList(iter);
  }
  

  public String getLanguage(int node)
  {
    if (_dom != null) {
      return _dom.getLanguage(node);
    }
    
    return super.getLanguage(node);
  }
  

  public int getSize()
  {
    if (_dom != null) {
      return _dom.getSize();
    }
    
    return super.getSize();
  }
  

  public String getDocumentURI(int node)
  {
    if (_dom != null) {
      return _dom.getDocumentURI(node);
    }
    
    return "adaptive_rtf" + _documentURIIndex++;
  }
  

  public void setFilter(StripFilter filter)
  {
    if (_dom != null) {
      _dom.setFilter(filter);
    }
    else {
      super.setFilter(filter);
    }
  }
  
  public void setupMapping(String[] names, String[] uris, int[] types, String[] namespaces)
  {
    if (_dom != null) {
      _dom.setupMapping(names, uris, types, namespaces);
    }
    else {
      super.setupMapping(names, uris, types, namespaces);
    }
  }
  
  public boolean isElement(int node)
  {
    if (_dom != null) {
      return _dom.isElement(node);
    }
    
    return super.isElement(node);
  }
  

  public boolean isAttribute(int node)
  {
    if (_dom != null) {
      return _dom.isAttribute(node);
    }
    
    return super.isAttribute(node);
  }
  

  public String lookupNamespace(int node, String prefix)
    throws TransletException
  {
    if (_dom != null) {
      return _dom.lookupNamespace(node, prefix);
    }
    
    return super.lookupNamespace(node, prefix);
  }
  




  public final int getNodeIdent(int nodehandle)
  {
    if (_dom != null) {
      return _dom.getNodeIdent(nodehandle);
    }
    
    return super.getNodeIdent(nodehandle);
  }
  




  public final int getNodeHandle(int nodeId)
  {
    if (_dom != null) {
      return _dom.getNodeHandle(nodeId);
    }
    
    return super.getNodeHandle(nodeId);
  }
  

  public DOM getResultTreeFrag(int initialSize, int rtfType)
  {
    if (_dom != null) {
      return _dom.getResultTreeFrag(initialSize, rtfType);
    }
    
    return super.getResultTreeFrag(initialSize, rtfType);
  }
  

  public SerializationHandler getOutputDomBuilder()
  {
    return this;
  }
  
  public int getNSType(int node)
  {
    if (_dom != null) {
      return _dom.getNSType(node);
    }
    
    return super.getNSType(node);
  }
  

  public String getUnparsedEntityURI(String name)
  {
    if (_dom != null) {
      return _dom.getUnparsedEntityURI(name);
    }
    
    return super.getUnparsedEntityURI(name);
  }
  

  public Hashtable getElementsWithIDs()
  {
    if (_dom != null) {
      return _dom.getElementsWithIDs();
    }
    
    return super.getElementsWithIDs();
  }
  




  private void maybeEmitStartElement()
    throws SAXException
  {
    if (_openElementName != null)
    {
      int index;
      if ((index = _openElementName.indexOf(":")) < 0) {
        _dom.startElement(null, _openElementName, _openElementName, _attributes);
      } else {
        _dom.startElement(null, _openElementName.substring(index + 1), _openElementName, _attributes);
      }
      
      _openElementName = null;
    }
  }
  
  private void prepareNewDOM()
    throws SAXException
  {
    _dom = ((SAXImpl)_dtmManager.getDTM(null, true, _wsfilter, true, false, false, _initSize, _buildIdIndex));
    

    _dom.startDocument();
    
    for (int i = 0; i < _size; i++) {
      String str = _textArray[i];
      _dom.characters(str.toCharArray(), 0, str.length());
    }
    _size = 0;
  }
  
  public void startDocument()
    throws SAXException
  {}
  
  public void endDocument() throws SAXException
  {
    if (_dom != null) {
      _dom.endDocument();
    }
    else {
      super.endDocument();
    }
  }
  
  public void characters(String str) throws SAXException
  {
    if (_dom != null) {
      characters(str.toCharArray(), 0, str.length());
    }
    else {
      super.characters(str);
    }
  }
  
  public void characters(char[] ch, int offset, int length)
    throws SAXException
  {
    if (_dom != null) {
      maybeEmitStartElement();
      _dom.characters(ch, offset, length);
    }
    else {
      super.characters(ch, offset, length);
    }
  }
  
  public boolean setEscaping(boolean escape) throws SAXException
  {
    if (_dom != null) {
      return _dom.setEscaping(escape);
    }
    
    return super.setEscaping(escape);
  }
  
  public void startElement(String elementName)
    throws SAXException
  {
    if (_dom == null) {
      prepareNewDOM();
    }
    
    maybeEmitStartElement();
    _openElementName = elementName;
    _attributes.clear();
  }
  
  public void startElement(String uri, String localName, String qName)
    throws SAXException
  {
    startElement(qName);
  }
  
  public void startElement(String uri, String localName, String qName, Attributes attributes)
    throws SAXException
  {
    startElement(qName);
  }
  
  public void endElement(String elementName) throws SAXException
  {
    maybeEmitStartElement();
    _dom.endElement(null, null, elementName);
  }
  
  public void endElement(String uri, String localName, String qName)
    throws SAXException
  {
    endElement(qName);
  }
  
  public void addUniqueAttribute(String qName, String value, int flags)
    throws SAXException
  {
    addAttribute(qName, value);
  }
  
  public void addAttribute(String name, String value)
  {
    if (_openElementName != null) {
      _attributes.add(name, value);
    }
    else {
      BasisLibrary.runTimeError("STRAY_ATTRIBUTE_ERR", name);
    }
  }
  
  public void namespaceAfterStartElement(String prefix, String uri)
    throws SAXException
  {
    if (_dom == null) {
      prepareNewDOM();
    }
    
    _dom.startPrefixMapping(prefix, uri);
  }
  
  public void comment(String comment) throws SAXException
  {
    if (_dom == null) {
      prepareNewDOM();
    }
    
    maybeEmitStartElement();
    char[] chars = comment.toCharArray();
    _dom.comment(chars, 0, chars.length);
  }
  
  public void comment(char[] chars, int offset, int length)
    throws SAXException
  {
    if (_dom == null) {
      prepareNewDOM();
    }
    
    maybeEmitStartElement();
    _dom.comment(chars, offset, length);
  }
  
  public void processingInstruction(String target, String data)
    throws SAXException
  {
    if (_dom == null) {
      prepareNewDOM();
    }
    
    maybeEmitStartElement();
    _dom.processingInstruction(target, data);
  }
  


  public void setFeature(String featureId, boolean state)
  {
    if (_dom != null) {
      _dom.setFeature(featureId, state);
    }
  }
  
  public void setProperty(String property, Object value)
  {
    if (_dom != null) {
      _dom.setProperty(property, value);
    }
  }
  
  public DTMAxisTraverser getAxisTraverser(int axis)
  {
    if (_dom != null) {
      return _dom.getAxisTraverser(axis);
    }
    
    return super.getAxisTraverser(axis);
  }
  

  public boolean hasChildNodes(int nodeHandle)
  {
    if (_dom != null) {
      return _dom.hasChildNodes(nodeHandle);
    }
    
    return super.hasChildNodes(nodeHandle);
  }
  

  public int getFirstChild(int nodeHandle)
  {
    if (_dom != null) {
      return _dom.getFirstChild(nodeHandle);
    }
    
    return super.getFirstChild(nodeHandle);
  }
  

  public int getLastChild(int nodeHandle)
  {
    if (_dom != null) {
      return _dom.getLastChild(nodeHandle);
    }
    
    return super.getLastChild(nodeHandle);
  }
  

  public int getAttributeNode(int elementHandle, String namespaceURI, String name)
  {
    if (_dom != null) {
      return _dom.getAttributeNode(elementHandle, namespaceURI, name);
    }
    
    return super.getAttributeNode(elementHandle, namespaceURI, name);
  }
  

  public int getFirstAttribute(int nodeHandle)
  {
    if (_dom != null) {
      return _dom.getFirstAttribute(nodeHandle);
    }
    
    return super.getFirstAttribute(nodeHandle);
  }
  

  public int getFirstNamespaceNode(int nodeHandle, boolean inScope)
  {
    if (_dom != null) {
      return _dom.getFirstNamespaceNode(nodeHandle, inScope);
    }
    
    return super.getFirstNamespaceNode(nodeHandle, inScope);
  }
  

  public int getNextSibling(int nodeHandle)
  {
    if (_dom != null) {
      return _dom.getNextSibling(nodeHandle);
    }
    
    return super.getNextSibling(nodeHandle);
  }
  

  public int getPreviousSibling(int nodeHandle)
  {
    if (_dom != null) {
      return _dom.getPreviousSibling(nodeHandle);
    }
    
    return super.getPreviousSibling(nodeHandle);
  }
  

  public int getNextAttribute(int nodeHandle)
  {
    if (_dom != null) {
      return _dom.getNextAttribute(nodeHandle);
    }
    
    return super.getNextAttribute(nodeHandle);
  }
  


  public int getNextNamespaceNode(int baseHandle, int namespaceHandle, boolean inScope)
  {
    if (_dom != null) {
      return _dom.getNextNamespaceNode(baseHandle, namespaceHandle, inScope);
    }
    
    return super.getNextNamespaceNode(baseHandle, namespaceHandle, inScope);
  }
  

  public int getOwnerDocument(int nodeHandle)
  {
    if (_dom != null) {
      return _dom.getOwnerDocument(nodeHandle);
    }
    
    return super.getOwnerDocument(nodeHandle);
  }
  

  public int getDocumentRoot(int nodeHandle)
  {
    if (_dom != null) {
      return _dom.getDocumentRoot(nodeHandle);
    }
    
    return super.getDocumentRoot(nodeHandle);
  }
  

  public XMLString getStringValue(int nodeHandle)
  {
    if (_dom != null) {
      return _dom.getStringValue(nodeHandle);
    }
    
    return super.getStringValue(nodeHandle);
  }
  

  public int getStringValueChunkCount(int nodeHandle)
  {
    if (_dom != null) {
      return _dom.getStringValueChunkCount(nodeHandle);
    }
    
    return super.getStringValueChunkCount(nodeHandle);
  }
  


  public char[] getStringValueChunk(int nodeHandle, int chunkIndex, int[] startAndLen)
  {
    if (_dom != null) {
      return _dom.getStringValueChunk(nodeHandle, chunkIndex, startAndLen);
    }
    
    return super.getStringValueChunk(nodeHandle, chunkIndex, startAndLen);
  }
  

  public int getExpandedTypeID(String namespace, String localName, int type)
  {
    if (_dom != null) {
      return _dom.getExpandedTypeID(namespace, localName, type);
    }
    
    return super.getExpandedTypeID(namespace, localName, type);
  }
  

  public String getLocalNameFromExpandedNameID(int ExpandedNameID)
  {
    if (_dom != null) {
      return _dom.getLocalNameFromExpandedNameID(ExpandedNameID);
    }
    
    return super.getLocalNameFromExpandedNameID(ExpandedNameID);
  }
  

  public String getNamespaceFromExpandedNameID(int ExpandedNameID)
  {
    if (_dom != null) {
      return _dom.getNamespaceFromExpandedNameID(ExpandedNameID);
    }
    
    return super.getNamespaceFromExpandedNameID(ExpandedNameID);
  }
  

  public String getLocalName(int nodeHandle)
  {
    if (_dom != null) {
      return _dom.getLocalName(nodeHandle);
    }
    
    return super.getLocalName(nodeHandle);
  }
  

  public String getPrefix(int nodeHandle)
  {
    if (_dom != null) {
      return _dom.getPrefix(nodeHandle);
    }
    
    return super.getPrefix(nodeHandle);
  }
  

  public String getNamespaceURI(int nodeHandle)
  {
    if (_dom != null) {
      return _dom.getNamespaceURI(nodeHandle);
    }
    
    return super.getNamespaceURI(nodeHandle);
  }
  

  public String getNodeValue(int nodeHandle)
  {
    if (_dom != null) {
      return _dom.getNodeValue(nodeHandle);
    }
    
    return super.getNodeValue(nodeHandle);
  }
  

  public short getNodeType(int nodeHandle)
  {
    if (_dom != null) {
      return _dom.getNodeType(nodeHandle);
    }
    
    return super.getNodeType(nodeHandle);
  }
  

  public short getLevel(int nodeHandle)
  {
    if (_dom != null) {
      return _dom.getLevel(nodeHandle);
    }
    
    return super.getLevel(nodeHandle);
  }
  

  public boolean isSupported(String feature, String version)
  {
    if (_dom != null) {
      return _dom.isSupported(feature, version);
    }
    
    return super.isSupported(feature, version);
  }
  

  public String getDocumentBaseURI()
  {
    if (_dom != null) {
      return _dom.getDocumentBaseURI();
    }
    
    return super.getDocumentBaseURI();
  }
  

  public void setDocumentBaseURI(String baseURI)
  {
    if (_dom != null) {
      _dom.setDocumentBaseURI(baseURI);
    }
    else {
      super.setDocumentBaseURI(baseURI);
    }
  }
  
  public String getDocumentSystemIdentifier(int nodeHandle)
  {
    if (_dom != null) {
      return _dom.getDocumentSystemIdentifier(nodeHandle);
    }
    
    return super.getDocumentSystemIdentifier(nodeHandle);
  }
  

  public String getDocumentEncoding(int nodeHandle)
  {
    if (_dom != null) {
      return _dom.getDocumentEncoding(nodeHandle);
    }
    
    return super.getDocumentEncoding(nodeHandle);
  }
  

  public String getDocumentStandalone(int nodeHandle)
  {
    if (_dom != null) {
      return _dom.getDocumentStandalone(nodeHandle);
    }
    
    return super.getDocumentStandalone(nodeHandle);
  }
  

  public String getDocumentVersion(int documentHandle)
  {
    if (_dom != null) {
      return _dom.getDocumentVersion(documentHandle);
    }
    
    return super.getDocumentVersion(documentHandle);
  }
  

  public boolean getDocumentAllDeclarationsProcessed()
  {
    if (_dom != null) {
      return _dom.getDocumentAllDeclarationsProcessed();
    }
    
    return super.getDocumentAllDeclarationsProcessed();
  }
  

  public String getDocumentTypeDeclarationSystemIdentifier()
  {
    if (_dom != null) {
      return _dom.getDocumentTypeDeclarationSystemIdentifier();
    }
    
    return super.getDocumentTypeDeclarationSystemIdentifier();
  }
  

  public String getDocumentTypeDeclarationPublicIdentifier()
  {
    if (_dom != null) {
      return _dom.getDocumentTypeDeclarationPublicIdentifier();
    }
    
    return super.getDocumentTypeDeclarationPublicIdentifier();
  }
  

  public int getElementById(String elementId)
  {
    if (_dom != null) {
      return _dom.getElementById(elementId);
    }
    
    return super.getElementById(elementId);
  }
  

  public boolean supportsPreStripping()
  {
    if (_dom != null) {
      return _dom.supportsPreStripping();
    }
    
    return super.supportsPreStripping();
  }
  

  public boolean isNodeAfter(int firstNodeHandle, int secondNodeHandle)
  {
    if (_dom != null) {
      return _dom.isNodeAfter(firstNodeHandle, secondNodeHandle);
    }
    
    return super.isNodeAfter(firstNodeHandle, secondNodeHandle);
  }
  

  public boolean isCharacterElementContentWhitespace(int nodeHandle)
  {
    if (_dom != null) {
      return _dom.isCharacterElementContentWhitespace(nodeHandle);
    }
    
    return super.isCharacterElementContentWhitespace(nodeHandle);
  }
  

  public boolean isDocumentAllDeclarationsProcessed(int documentHandle)
  {
    if (_dom != null) {
      return _dom.isDocumentAllDeclarationsProcessed(documentHandle);
    }
    
    return super.isDocumentAllDeclarationsProcessed(documentHandle);
  }
  

  public boolean isAttributeSpecified(int attributeHandle)
  {
    if (_dom != null) {
      return _dom.isAttributeSpecified(attributeHandle);
    }
    
    return super.isAttributeSpecified(attributeHandle);
  }
  


  public void dispatchCharactersEvents(int nodeHandle, ContentHandler ch, boolean normalize)
    throws SAXException
  {
    if (_dom != null) {
      _dom.dispatchCharactersEvents(nodeHandle, ch, normalize);
    }
    else {
      super.dispatchCharactersEvents(nodeHandle, ch, normalize);
    }
  }
  
  public void dispatchToEvents(int nodeHandle, ContentHandler ch)
    throws SAXException
  {
    if (_dom != null) {
      _dom.dispatchToEvents(nodeHandle, ch);
    }
    else {
      super.dispatchToEvents(nodeHandle, ch);
    }
  }
  
  public Node getNode(int nodeHandle)
  {
    if (_dom != null) {
      return _dom.getNode(nodeHandle);
    }
    
    return super.getNode(nodeHandle);
  }
  

  public boolean needsTwoThreads()
  {
    if (_dom != null) {
      return _dom.needsTwoThreads();
    }
    
    return super.needsTwoThreads();
  }
  

  public ContentHandler getContentHandler()
  {
    if (_dom != null) {
      return _dom.getContentHandler();
    }
    
    return super.getContentHandler();
  }
  

  public LexicalHandler getLexicalHandler()
  {
    if (_dom != null) {
      return _dom.getLexicalHandler();
    }
    
    return super.getLexicalHandler();
  }
  

  public EntityResolver getEntityResolver()
  {
    if (_dom != null) {
      return _dom.getEntityResolver();
    }
    
    return super.getEntityResolver();
  }
  

  public DTDHandler getDTDHandler()
  {
    if (_dom != null) {
      return _dom.getDTDHandler();
    }
    
    return super.getDTDHandler();
  }
  

  public ErrorHandler getErrorHandler()
  {
    if (_dom != null) {
      return _dom.getErrorHandler();
    }
    
    return super.getErrorHandler();
  }
  

  public DeclHandler getDeclHandler()
  {
    if (_dom != null) {
      return _dom.getDeclHandler();
    }
    
    return super.getDeclHandler();
  }
  

  public void appendChild(int newChild, boolean clone, boolean cloneDepth)
  {
    if (_dom != null) {
      _dom.appendChild(newChild, clone, cloneDepth);
    }
    else {
      super.appendChild(newChild, clone, cloneDepth);
    }
  }
  
  public void appendTextChild(String str)
  {
    if (_dom != null) {
      _dom.appendTextChild(str);
    }
    else {
      super.appendTextChild(str);
    }
  }
  
  public SourceLocator getSourceLocatorFor(int node)
  {
    if (_dom != null) {
      return _dom.getSourceLocatorFor(node);
    }
    
    return super.getSourceLocatorFor(node);
  }
  

  public void documentRegistration()
  {
    if (_dom != null) {
      _dom.documentRegistration();
    }
    else {
      super.documentRegistration();
    }
  }
  
  public void documentRelease()
  {
    if (_dom != null) {
      _dom.documentRelease();
    }
    else {
      super.documentRelease();
    }
  }
}
