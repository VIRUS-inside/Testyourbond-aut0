package org.apache.xalan.xsltc.dom;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.DOMEnhancedForDTM;
import org.apache.xalan.xsltc.StripFilter;
import org.apache.xalan.xsltc.TransletException;
import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xml.dtm.Axis;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.dtm.ref.DTMAxisIterNodeList;
import org.apache.xml.dtm.ref.DTMDefaultBaseIterators;
import org.apache.xml.dtm.ref.DTMDefaultBaseIterators.InternalAxisIteratorBase;
import org.apache.xml.dtm.ref.DTMDefaultBaseIterators.NamespaceIterator;
import org.apache.xml.dtm.ref.DTMDefaultBaseIterators.NthDescendantIterator;
import org.apache.xml.dtm.ref.DTMDefaultBaseIterators.RootIterator;
import org.apache.xml.dtm.ref.DTMDefaultBaseIterators.SingletonIterator;
import org.apache.xml.dtm.ref.DTMNodeProxy;
import org.apache.xml.dtm.ref.EmptyIterator;
import org.apache.xml.dtm.ref.ExpandedNameTable;
import org.apache.xml.dtm.ref.sax2dtm.SAX2DTM2;
import org.apache.xml.dtm.ref.sax2dtm.SAX2DTM2.AncestorIterator;
import org.apache.xml.dtm.ref.sax2dtm.SAX2DTM2.AttributeIterator;
import org.apache.xml.dtm.ref.sax2dtm.SAX2DTM2.ChildrenIterator;
import org.apache.xml.dtm.ref.sax2dtm.SAX2DTM2.DescendantIterator;
import org.apache.xml.dtm.ref.sax2dtm.SAX2DTM2.FollowingIterator;
import org.apache.xml.dtm.ref.sax2dtm.SAX2DTM2.FollowingSiblingIterator;
import org.apache.xml.dtm.ref.sax2dtm.SAX2DTM2.ParentIterator;
import org.apache.xml.dtm.ref.sax2dtm.SAX2DTM2.PrecedingIterator;
import org.apache.xml.dtm.ref.sax2dtm.SAX2DTM2.PrecedingSiblingIterator;
import org.apache.xml.dtm.ref.sax2dtm.SAX2DTM2.TypedAncestorIterator;
import org.apache.xml.dtm.ref.sax2dtm.SAX2DTM2.TypedAttributeIterator;
import org.apache.xml.dtm.ref.sax2dtm.SAX2DTM2.TypedChildrenIterator;
import org.apache.xml.dtm.ref.sax2dtm.SAX2DTM2.TypedDescendantIterator;
import org.apache.xml.dtm.ref.sax2dtm.SAX2DTM2.TypedFollowingIterator;
import org.apache.xml.dtm.ref.sax2dtm.SAX2DTM2.TypedFollowingSiblingIterator;
import org.apache.xml.dtm.ref.sax2dtm.SAX2DTM2.TypedPrecedingIterator;
import org.apache.xml.dtm.ref.sax2dtm.SAX2DTM2.TypedPrecedingSiblingIterator;
import org.apache.xml.dtm.ref.sax2dtm.SAX2DTM2.TypedRootIterator;
import org.apache.xml.dtm.ref.sax2dtm.SAX2DTM2.TypedSingletonIterator;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.serializer.ToXMLSAXHandler;
import org.apache.xml.utils.IntStack;
import org.apache.xml.utils.SystemIDResolver;
import org.apache.xml.utils.XMLStringFactory;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;





















public final class SAXImpl
  extends SAX2DTM2
  implements DOMEnhancedForDTM, DOMBuilder
{
  private int _uriCount = 0;
  private int _prefixCount = 0;
  

  private int[] _xmlSpaceStack;
  
  private int _idx = 1;
  private boolean _preserve = false;
  
  private static final String XML_STRING = "xml:";
  
  private static final String XML_PREFIX = "xml";
  private static final String XMLSPACE_STRING = "xml:space";
  private static final String PRESERVE_STRING = "preserve";
  private static final String XMLNS_PREFIX = "xmlns";
  private static final String XML_URI = "http://www.w3.org/XML/1998/namespace";
  private boolean _escaping = true;
  private boolean _disableEscaping = false;
  private int _textNodeToProcess = -1;
  



  private static final String EMPTYSTRING = "";
  



  private static final DTMAxisIterator EMPTYITERATOR = ;
  
  private int _namesSize = -1;
  

  private org.apache.xalan.xsltc.runtime.Hashtable _nsIndex = new org.apache.xalan.xsltc.runtime.Hashtable();
  

  private int _size = 0;
  

  private BitArray _dontEscape = null;
  

  private String _documentURI = null;
  private static int _documentURIIndex = 0;
  


  private Document _document;
  


  private org.apache.xalan.xsltc.runtime.Hashtable _node2Ids = null;
  

  private boolean _hasDOMSource = false;
  

  private XSLTCDTMManager _dtmManager;
  

  private Node[] _nodes;
  
  private NodeList[] _nodeLists;
  
  private static final String XML_LANG_ATTRIBUTE = "http://www.w3.org/XML/1998/namespace:@lang";
  

  public void setDocumentURI(String uri)
  {
    if (uri != null) {
      setDocumentBaseURI(SystemIDResolver.getAbsoluteURI(uri));
    }
  }
  


  public String getDocumentURI()
  {
    String baseURI = getDocumentBaseURI();
    return "rtf" + _documentURIIndex++;
  }
  
  public String getDocumentURI(int node) {
    return getDocumentURI();
  }
  





  public void setupMapping(String[] names, String[] urisArray, int[] typesArray, String[] namespaces) {}
  




  public String lookupNamespace(int node, String prefix)
    throws TransletException
  {
    SAX2DTM2.AncestorIterator ancestors = new SAX2DTM2.AncestorIterator(this);
    
    if (isElement(node)) {
      ancestors.includeSelf();
    }
    
    ancestors.setStartNode(node);
    int anode; while ((anode = ancestors.next()) != -1) {
      DTMDefaultBaseIterators.NamespaceIterator namespaces = new DTMDefaultBaseIterators.NamespaceIterator(this);
      
      namespaces.setStartNode(anode);
      int nsnode; while ((nsnode = namespaces.next()) != -1) {
        if (getLocalName(nsnode).equals(prefix)) {
          return getNodeValue(nsnode);
        }
      }
    }
    
    BasisLibrary.runTimeError("NAMESPACE_PREFIX_ERR", prefix);
    return null;
  }
  


  public boolean isElement(int node)
  {
    return getNodeType(node) == 1;
  }
  


  public boolean isAttribute(int node)
  {
    return getNodeType(node) == 2;
  }
  


  public int getSize()
  {
    return getNumberOfNodes();
  }
  




  public void setFilter(StripFilter filter) {}
  



  public boolean lessThan(int node1, int node2)
  {
    if (node1 == -1) {
      return false;
    }
    
    if (node2 == -1) {
      return true;
    }
    
    return node1 < node2;
  }
  


  public Node makeNode(int index)
  {
    if (_nodes == null) {
      _nodes = new Node[_namesSize];
    }
    
    int nodeID = makeNodeIdentity(index);
    if (nodeID < 0) {
      return null;
    }
    if (nodeID < _nodes.length) {
      return _nodes[nodeID] = (_nodes[nodeID] != null ? _nodes[nodeID] : ) = new DTMNodeProxy(this, index);
    }
    

    return new DTMNodeProxy(this, index);
  }
  




  public Node makeNode(DTMAxisIterator iter)
  {
    return makeNode(iter.next());
  }
  


  public NodeList makeNodeList(int index)
  {
    if (_nodeLists == null) {
      _nodeLists = new NodeList[_namesSize];
    }
    
    int nodeID = makeNodeIdentity(index);
    if (nodeID < 0) {
      return null;
    }
    if (nodeID < _nodeLists.length) {
      return _nodeLists[nodeID] = (_nodeLists[nodeID] != null ? _nodeLists[nodeID] : ) = new DTMAxisIterNodeList(this, new DTMDefaultBaseIterators.SingletonIterator(this, index));
    }
    


    return new DTMAxisIterNodeList(this, new DTMDefaultBaseIterators.SingletonIterator(this, index));
  }
  




  public NodeList makeNodeList(DTMAxisIterator iter)
  {
    return new DTMAxisIterNodeList(this, iter);
  }
  




  public class TypedNamespaceIterator
    extends DTMDefaultBaseIterators.NamespaceIterator
  {
    private String _nsPrefix;
    




    public TypedNamespaceIterator(int nodeType)
    {
      super();
      if (m_expandedNameTable != null) {
        _nsPrefix = m_expandedNameTable.getLocalName(nodeType);
      }
    }
    




    public int next()
    {
      if ((_nsPrefix == null) || (_nsPrefix.length() == 0)) {
        return -1;
      }
      int node = -1;
      for (node = super.next(); node != -1; node = super.next()) {
        if (_nsPrefix.compareTo(getLocalName(node)) == 0) {
          return returnNode(node);
        }
      }
      return -1;
    }
  }
  


  private final class NodeValueIterator
    extends DTMDefaultBaseIterators.InternalAxisIteratorBase
  {
    private DTMAxisIterator _source;
    
    private String _value;
    
    private boolean _op;
    
    private final boolean _isReverse;
    
    private int _returnType = 1;
    
    public NodeValueIterator(DTMAxisIterator source, int returnType, String value, boolean op)
    {
      super();
      _source = source;
      _returnType = returnType;
      _value = value;
      _op = op;
      _isReverse = source.isReverse();
    }
    
    public boolean isReverse()
    {
      return _isReverse;
    }
    
    public DTMAxisIterator cloneIterator()
    {
      try {
        NodeValueIterator clone = (NodeValueIterator)super.clone();
        _isRestartable = false;
        _source = _source.cloneIterator();
        _value = _value;
        _op = _op;
        return clone.reset();
      }
      catch (CloneNotSupportedException e) {
        BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", e.toString());
      }
      return null;
    }
    

    public void setRestartable(boolean isRestartable)
    {
      _isRestartable = isRestartable;
      _source.setRestartable(isRestartable);
    }
    
    public DTMAxisIterator reset()
    {
      _source.reset();
      return resetPosition();
    }
    
    public int next()
    {
      int node;
      while ((node = _source.next()) != -1) {
        String val = getStringValueX(node);
        if (_value.equals(val) == _op) {
          if (_returnType == 0) {
            return returnNode(node);
          }
          
          return returnNode(getParent(node));
        }
      }
      
      return -1;
    }
    
    public DTMAxisIterator setStartNode(int node)
    {
      if (_isRestartable) {
        _source.setStartNode(this._startNode = node);
        return resetPosition();
      }
      return this;
    }
    
    public void setMark()
    {
      _source.setMark();
    }
    
    public void gotoMark()
    {
      _source.gotoMark();
    }
  }
  

  public DTMAxisIterator getNodeValueIterator(DTMAxisIterator iterator, int type, String value, boolean op)
  {
    return new NodeValueIterator(iterator, type, value, op);
  }
  



  public DTMAxisIterator orderNodes(DTMAxisIterator source, int node)
  {
    return new DupFilterIterator(source);
  }
  





  public DTMAxisIterator getIterator()
  {
    return new DTMDefaultBaseIterators.SingletonIterator(this, getDocument(), true);
  }
  



  public int getNSType(int node)
  {
    String s = getNamespaceURI(node);
    if (s == null) {
      return 0;
    }
    int eType = getIdForNamespace(s);
    return ((Integer)_nsIndex.get(new Integer(eType))).intValue();
  }
  





  public int getNamespaceType(int node)
  {
    return super.getNamespaceType(node);
  }
  




  private int[] setupMapping(String[] names, String[] uris, int[] types, int nNames)
  {
    int[] result = new int[m_expandedNameTable.getSize()];
    for (int i = 0; i < nNames; i++)
    {
      int type = m_expandedNameTable.getExpandedTypeID(uris[i], names[i], types[i], false);
      result[type] = type;
    }
    return result;
  }
  


  public int getGeneralizedType(String name)
  {
    return getGeneralizedType(name, true);
  }
  


  public int getGeneralizedType(String name, boolean searchOnly)
  {
    String ns = null;
    int index = -1;
    


    if ((index = name.lastIndexOf(':')) > -1) {
      ns = name.substring(0, index);
    }
    


    int lNameStartIdx = index + 1;
    
    int code;
    
    if (name.charAt(lNameStartIdx) == '@') {
      int code = 2;
      lNameStartIdx++;
    }
    else {
      code = 1;
    }
    

    String lName = lNameStartIdx == 0 ? name : name.substring(lNameStartIdx);
    
    return m_expandedNameTable.getExpandedTypeID(ns, lName, code, searchOnly);
  }
  





  public short[] getMapping(String[] names, String[] uris, int[] types)
  {
    if (_namesSize < 0) {
      return getMapping2(names, uris, types);
    }
    

    int namesLength = names.length;
    int exLength = m_expandedNameTable.getSize();
    
    short[] result = new short[exLength];
    

    for (int i = 0; i < 14; i++) {
      result[i] = ((short)i);
    }
    
    for (i = 14; i < exLength; i++) {
      result[i] = m_expandedNameTable.getType(i);
    }
    

    for (i = 0; i < namesLength; i++) {
      int genType = m_expandedNameTable.getExpandedTypeID(uris[i], names[i], types[i], true);
      


      if ((genType >= 0) && (genType < exLength)) {
        result[genType] = ((short)(i + 14));
      }
    }
    
    return result;
  }
  




  public int[] getReverseMapping(String[] names, String[] uris, int[] types)
  {
    int[] result = new int[names.length + 14];
    

    for (int i = 0; i < 14; i++) {
      result[i] = i;
    }
    

    for (i = 0; i < names.length; i++) {
      int type = m_expandedNameTable.getExpandedTypeID(uris[i], names[i], types[i], true);
      result[(i + 14)] = type;
    }
    return result;
  }
  





  private short[] getMapping2(String[] names, String[] uris, int[] types)
  {
    int namesLength = names.length;
    int exLength = m_expandedNameTable.getSize();
    int[] generalizedTypes = null;
    if (namesLength > 0) {
      generalizedTypes = new int[namesLength];
    }
    
    int resultLength = exLength;
    
    for (int i = 0; i < namesLength; i++)
    {



      generalizedTypes[i] = m_expandedNameTable.getExpandedTypeID(uris[i], names[i], types[i], false);
      



      if ((_namesSize < 0) && (generalizedTypes[i] >= resultLength)) {
        resultLength = generalizedTypes[i] + 1;
      }
    }
    
    short[] result = new short[resultLength];
    

    for (i = 0; i < 14; i++) {
      result[i] = ((short)i);
    }
    
    for (i = 14; i < exLength; i++) {
      result[i] = m_expandedNameTable.getType(i);
    }
    

    for (i = 0; i < namesLength; i++) {
      int genType = generalizedTypes[i];
      if ((genType >= 0) && (genType < resultLength)) {
        result[genType] = ((short)(i + 14));
      }
    }
    
    return result;
  }
  



  public short[] getNamespaceMapping(String[] namespaces)
  {
    int nsLength = namespaces.length;
    int mappingLength = _uriCount;
    
    short[] result = new short[mappingLength];
    

    for (int i = 0; i < mappingLength; i++) {
      result[i] = -1;
    }
    
    for (i = 0; i < nsLength; i++) {
      int eType = getIdForNamespace(namespaces[i]);
      Integer type = (Integer)_nsIndex.get(new Integer(eType));
      if (type != null) {
        result[type.intValue()] = ((short)i);
      }
    }
    
    return result;
  }
  




  public short[] getReverseNamespaceMapping(String[] namespaces)
  {
    int length = namespaces.length;
    short[] result = new short[length];
    
    for (int i = 0; i < length; i++) {
      int eType = getIdForNamespace(namespaces[i]);
      Integer type = (Integer)_nsIndex.get(new Integer(eType));
      result[i] = (type == null ? -1 : type.shortValue());
    }
    
    return result;
  }
  






  public SAXImpl(XSLTCDTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing, boolean buildIdIndex)
  {
    this(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing, 512, buildIdIndex, false);
  }
  









  public SAXImpl(XSLTCDTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing, int blocksize, boolean buildIdIndex, boolean newNameTable)
  {
    super(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing, blocksize, false, buildIdIndex, newNameTable);
    

    _dtmManager = mgr;
    _size = blocksize;
    

    _xmlSpaceStack = new int[blocksize <= 64 ? 4 : 64];
    

    _xmlSpaceStack[0] = 0;
    


    if ((source instanceof DOMSource)) {
      _hasDOMSource = true;
      DOMSource domsrc = (DOMSource)source;
      Node node = domsrc.getNode();
      if ((node instanceof Document)) {
        _document = ((Document)node);
      }
      else {
        _document = node.getOwnerDocument();
      }
      _node2Ids = new org.apache.xalan.xsltc.runtime.Hashtable();
    }
  }
  






  public void migrateTo(DTMManager manager)
  {
    super.migrateTo(manager);
    if ((manager instanceof XSLTCDTMManager)) {
      _dtmManager = ((XSLTCDTMManager)manager);
    }
  }
  






  public int getElementById(String idString)
  {
    Node node = _document.getElementById(idString);
    if (node != null) {
      Integer id = (Integer)_node2Ids.get(node);
      return id != null ? id.intValue() : -1;
    }
    
    return -1;
  }
  




  public boolean hasDOMSource()
  {
    return _hasDOMSource;
  }
  








  private void xmlSpaceDefine(String val, int node)
  {
    boolean setting = val.equals("preserve");
    if (setting != _preserve) {
      _xmlSpaceStack[(_idx++)] = node;
      _preserve = setting;
    }
  }
  




  private void xmlSpaceRevert(int node)
  {
    if (node == _xmlSpaceStack[(_idx - 1)]) {
      _idx -= 1;
      _preserve = (!_preserve);
    }
  }
  






  protected boolean getShouldStripWhitespace()
  {
    return _preserve ? false : super.getShouldStripWhitespace();
  }
  


  private void handleTextEscaping()
  {
    if ((_disableEscaping) && (_textNodeToProcess != -1) && (_type(_textNodeToProcess) == 3))
    {
      if (_dontEscape == null) {
        _dontEscape = new BitArray(_size);
      }
      

      if (_textNodeToProcess >= _dontEscape.size()) {
        _dontEscape.resize(_dontEscape.size() * 2);
      }
      
      _dontEscape.setBit(_textNodeToProcess);
      _disableEscaping = false;
    }
    _textNodeToProcess = -1;
  }
  







  public void characters(char[] ch, int start, int length)
    throws SAXException
  {
    super.characters(ch, start, length);
    
    _disableEscaping = (!_escaping);
    _textNodeToProcess = getNumberOfNodes();
  }
  


  public void startDocument()
    throws SAXException
  {
    super.startDocument();
    
    _nsIndex.put(new Integer(0), new Integer(_uriCount++));
    definePrefixAndUri("xml", "http://www.w3.org/XML/1998/namespace");
  }
  


  public void endDocument()
    throws SAXException
  {
    super.endDocument();
    
    handleTextEscaping();
    _namesSize = m_expandedNameTable.getSize();
  }
  






  public void startElement(String uri, String localName, String qname, Attributes attributes, Node node)
    throws SAXException
  {
    startElement(uri, localName, qname, attributes);
    
    if (m_buildIdIndex) {
      _node2Ids.put(node, new Integer(m_parents.peek()));
    }
  }
  




  public void startElement(String uri, String localName, String qname, Attributes attributes)
    throws SAXException
  {
    super.startElement(uri, localName, qname, attributes);
    
    handleTextEscaping();
    
    if (m_wsfilter != null)
    {


      int index = attributes.getIndex("xml:space");
      if (index >= 0) {
        xmlSpaceDefine(attributes.getValue(index), m_parents.peek());
      }
    }
  }
  



  public void endElement(String namespaceURI, String localName, String qname)
    throws SAXException
  {
    super.endElement(namespaceURI, localName, qname);
    
    handleTextEscaping();
    

    if (m_wsfilter != null) {
      xmlSpaceRevert(m_previous);
    }
  }
  



  public void processingInstruction(String target, String data)
    throws SAXException
  {
    super.processingInstruction(target, data);
    handleTextEscaping();
  }
  




  public void ignorableWhitespace(char[] ch, int start, int length)
    throws SAXException
  {
    super.ignorableWhitespace(ch, start, length);
    _textNodeToProcess = getNumberOfNodes();
  }
  



  public void startPrefixMapping(String prefix, String uri)
    throws SAXException
  {
    super.startPrefixMapping(prefix, uri);
    handleTextEscaping();
    
    definePrefixAndUri(prefix, uri);
  }
  

  private void definePrefixAndUri(String prefix, String uri)
    throws SAXException
  {
    Integer eType = new Integer(getIdForNamespace(uri));
    if ((Integer)_nsIndex.get(eType) == null) {
      _nsIndex.put(eType, new Integer(_uriCount++));
    }
  }
  



  public void comment(char[] ch, int start, int length)
    throws SAXException
  {
    super.comment(ch, start, length);
    handleTextEscaping();
  }
  
  public boolean setEscaping(boolean value) {
    boolean temp = _escaping;
    _escaping = value;
    return temp;
  }
  







  public void print(int node, int level)
  {
    switch (getNodeType(node))
    {
    case 0: 
    case 9: 
      print(getFirstChild(node), level);
      break;
    case 3: 
    case 7: 
    case 8: 
      System.out.print(getStringValueX(node));
      break;
    case 1: case 2: case 4: case 5: case 6: default: 
      String name = getNodeName(node);
      System.out.print("<" + name);
      for (int a = getFirstAttribute(node); a != -1; a = getNextAttribute(a))
      {
        System.out.print("\n" + getNodeName(a) + "=\"" + getStringValueX(a) + "\"");
      }
      System.out.print('>');
      for (int child = getFirstChild(node); child != -1; 
          child = getNextSibling(child)) {
        print(child, level + 1);
      }
      System.out.println("</" + name + '>');
    }
    
  }
  




  public String getNodeName(int node)
  {
    int nodeh = node;
    short type = getNodeType(nodeh);
    switch (type)
    {
    case 0: 
    case 3: 
    case 8: 
    case 9: 
      return "";
    case 13: 
      return getLocalName(nodeh);
    }
    return super.getNodeName(nodeh);
  }
  




  public String getNamespaceName(int node)
  {
    if (node == -1) {
      return "";
    }
    
    String s;
    return (s = getNamespaceURI(node)) == null ? "" : s;
  }
  




  public int getAttributeNode(int type, int element)
  {
    for (int attr = getFirstAttribute(element); 
        attr != -1; 
        attr = getNextAttribute(attr))
    {
      if (getExpandedTypeID(attr) == type) return attr;
    }
    return -1;
  }
  



  public String getAttributeValue(int type, int element)
  {
    int attr = getAttributeNode(type, element);
    return attr != -1 ? getStringValueX(attr) : "";
  }
  



  public String getAttributeValue(String name, int element)
  {
    return getAttributeValue(getGeneralizedType(name), element);
  }
  



  public DTMAxisIterator getChildren(int node)
  {
    return new SAX2DTM2.ChildrenIterator(this).setStartNode(node);
  }
  




  public DTMAxisIterator getTypedChildren(int type)
  {
    return new SAX2DTM2.TypedChildrenIterator(this, type);
  }
  






  public DTMAxisIterator getAxisIterator(int axis)
  {
    switch (axis)
    {
    case 13: 
      return new DTMDefaultBaseIterators.SingletonIterator(this);
    case 3: 
      return new SAX2DTM2.ChildrenIterator(this);
    case 10: 
      return new SAX2DTM2.ParentIterator(this);
    case 0: 
      return new SAX2DTM2.AncestorIterator(this);
    case 1: 
      return new SAX2DTM2.AncestorIterator(this).includeSelf();
    case 2: 
      return new SAX2DTM2.AttributeIterator(this);
    case 4: 
      return new SAX2DTM2.DescendantIterator(this);
    case 5: 
      return new SAX2DTM2.DescendantIterator(this).includeSelf();
    case 6: 
      return new SAX2DTM2.FollowingIterator(this);
    case 11: 
      return new SAX2DTM2.PrecedingIterator(this);
    case 7: 
      return new SAX2DTM2.FollowingSiblingIterator(this);
    case 12: 
      return new SAX2DTM2.PrecedingSiblingIterator(this);
    case 9: 
      return new DTMDefaultBaseIterators.NamespaceIterator(this);
    case 19: 
      return new DTMDefaultBaseIterators.RootIterator(this);
    }
    BasisLibrary.runTimeError("AXIS_SUPPORT_ERR", Axis.getNames(axis));
    

    return null;
  }
  





  public DTMAxisIterator getTypedAxisIterator(int axis, int type)
  {
    if (axis == 3) {
      return new SAX2DTM2.TypedChildrenIterator(this, type);
    }
    
    if (type == -1) {
      return EMPTYITERATOR;
    }
    
    switch (axis)
    {
    case 13: 
      return new SAX2DTM2.TypedSingletonIterator(this, type);
    case 3: 
      return new SAX2DTM2.TypedChildrenIterator(this, type);
    case 10: 
      return new SAX2DTM2.ParentIterator(this).setNodeType(type);
    case 0: 
      return new SAX2DTM2.TypedAncestorIterator(this, type);
    case 1: 
      return new SAX2DTM2.TypedAncestorIterator(this, type).includeSelf();
    case 2: 
      return new SAX2DTM2.TypedAttributeIterator(this, type);
    case 4: 
      return new SAX2DTM2.TypedDescendantIterator(this, type);
    case 5: 
      return new SAX2DTM2.TypedDescendantIterator(this, type).includeSelf();
    case 6: 
      return new SAX2DTM2.TypedFollowingIterator(this, type);
    case 11: 
      return new SAX2DTM2.TypedPrecedingIterator(this, type);
    case 7: 
      return new SAX2DTM2.TypedFollowingSiblingIterator(this, type);
    case 12: 
      return new SAX2DTM2.TypedPrecedingSiblingIterator(this, type);
    case 9: 
      return new TypedNamespaceIterator(type);
    case 19: 
      return new SAX2DTM2.TypedRootIterator(this, type);
    }
    BasisLibrary.runTimeError("TYPED_AXIS_SUPPORT_ERR", Axis.getNames(axis));
    

    return null;
  }
  








  public DTMAxisIterator getNamespaceAxisIterator(int axis, int ns)
  {
    DTMAxisIterator iterator = null;
    
    if (ns == -1) {
      return EMPTYITERATOR;
    }
    
    switch (axis) {
    case 3: 
      return new NamespaceChildrenIterator(ns);
    case 2: 
      return new NamespaceAttributeIterator(ns);
    }
    return new NamespaceWildcardIterator(axis, ns);
  }
  







  public final class NamespaceWildcardIterator
    extends DTMDefaultBaseIterators.InternalAxisIteratorBase
  {
    protected int m_nsType;
    





    protected DTMAxisIterator m_baseIterator;
    






    public NamespaceWildcardIterator(int axis, int nsType)
    {
      super();
      m_nsType = nsType;
      


      switch (axis)
      {

      case 2: 
        m_baseIterator = getAxisIterator(axis);
      


      case 9: 
        m_baseIterator = getAxisIterator(axis);
      }
      
      

      m_baseIterator = getTypedAxisIterator(axis, 1);
    }
    










    public DTMAxisIterator setStartNode(int node)
    {
      if (_isRestartable) {
        _startNode = node;
        m_baseIterator.setStartNode(node);
        resetPosition();
      }
      return this;
    }
    



    public int next()
    {
      int node;
      

      while ((node = m_baseIterator.next()) != -1)
      {
        if (getNSType(node) == m_nsType) {
          return returnNode(node);
        }
      }
      
      return -1;
    }
    




    public DTMAxisIterator cloneIterator()
    {
      try
      {
        DTMAxisIterator nestedClone = m_baseIterator.cloneIterator();
        NamespaceWildcardIterator clone = (NamespaceWildcardIterator)super.clone();
        

        m_baseIterator = nestedClone;
        m_nsType = m_nsType;
        _isRestartable = false;
        
        return clone;
      } catch (CloneNotSupportedException e) {
        BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", e.toString());
      }
      return null;
    }
    





    public boolean isReverse()
    {
      return m_baseIterator.isReverse();
    }
    
    public void setMark() {
      m_baseIterator.setMark();
    }
    
    public void gotoMark() {
      m_baseIterator.gotoMark();
    }
  }
  






  public final class NamespaceChildrenIterator
    extends DTMDefaultBaseIterators.InternalAxisIteratorBase
  {
    private final int _nsType;
    






    public NamespaceChildrenIterator(int type)
    {
      super();
      _nsType = type;
    }
    








    public DTMAxisIterator setStartNode(int node)
    {
      if (node == 0) {
        node = getDocument();
      }
      
      if (_isRestartable) {
        _startNode = node;
        _currentNode = (node == -1 ? -1 : -2);
        
        return resetPosition();
      }
      
      return this;
    }
    




    public int next()
    {
      if (_currentNode != -1) {
        for (int node = -2 == _currentNode ? _firstch(makeNodeIdentity(_startNode)) : _nextsib(_currentNode); 
            

            node != -1; 
            node = _nextsib(node)) {
          int nodeHandle = makeNodeHandle(node);
          
          if (getNSType(nodeHandle) == _nsType) {
            _currentNode = node;
            
            return returnNode(nodeHandle);
          }
        }
      }
      
      return -1;
    }
  }
  





  public final class NamespaceAttributeIterator
    extends DTMDefaultBaseIterators.InternalAxisIteratorBase
  {
    private final int _nsType;
    





    public NamespaceAttributeIterator(int nsType)
    {
      super();
      
      _nsType = nsType;
    }
    








    public DTMAxisIterator setStartNode(int node)
    {
      if (node == 0) {
        node = getDocument();
      }
      
      if (_isRestartable) {
        int nsType = _nsType;
        
        _startNode = node;
        
        for (node = getFirstAttribute(node); 
            node != -1; 
            node = getNextAttribute(node)) {
          if (getNSType(node) == nsType) {
            break;
          }
        }
        
        _currentNode = node;
        return resetPosition();
      }
      
      return this;
    }
    




    public int next()
    {
      int node = _currentNode;
      int nsType = _nsType;
      

      if (node == -1) {
        return -1;
      }
      
      for (int nextNode = getNextAttribute(node); 
          nextNode != -1; 
          nextNode = getNextAttribute(nextNode)) {
        if (getNSType(nextNode) == nsType) {
          break;
        }
      }
      
      _currentNode = nextNode;
      
      return returnNode(node);
    }
  }
  




  public DTMAxisIterator getTypedDescendantIterator(int type)
  {
    return new SAX2DTM2.TypedDescendantIterator(this, type);
  }
  



  public DTMAxisIterator getNthDescendant(int type, int n, boolean includeself)
  {
    DTMAxisIterator source = new SAX2DTM2.TypedDescendantIterator(this, type);
    return new DTMDefaultBaseIterators.NthDescendantIterator(this, n);
  }
  



  public void characters(int node, SerializationHandler handler)
    throws TransletException
  {
    if (node != -1) {
      try {
        dispatchCharactersEvents(node, handler, false);
      } catch (SAXException e) {
        throw new TransletException(e);
      }
    }
  }
  


  public void copy(DTMAxisIterator nodes, SerializationHandler handler)
    throws TransletException
  {
    int node;
    
    while ((node = nodes.next()) != -1) {
      copy(node, handler);
    }
  }
  


  public void copy(SerializationHandler handler)
    throws TransletException
  {
    copy(getDocument(), handler);
  }
  







  public void copy(int node, SerializationHandler handler)
    throws TransletException
  {
    copy(node, handler, false);
  }
  

  private final void copy(int node, SerializationHandler handler, boolean isChild)
    throws TransletException
  {
    int nodeID = makeNodeIdentity(node);
    int eType = _exptype2(nodeID);
    int type = _exptype2Type(eType);
    try
    {
      switch (type)
      {
      case 0: 
      case 9: 
        for (int c = _firstch2(nodeID); c != -1; c = _nextsib2(c)) {
          copy(makeNodeHandle(c), handler, true);
        }
        break;
      case 7: 
        copyPI(node, handler);
        break;
      case 8: 
        handler.comment(getStringValueX(node));
        break;
      case 3: 
        boolean oldEscapeSetting = false;
        boolean escapeBit = false;
        
        if (_dontEscape != null) {
          escapeBit = _dontEscape.getBit(getNodeIdent(node));
          if (escapeBit) {
            oldEscapeSetting = handler.setEscaping(false);
          }
        }
        
        copyTextNode(nodeID, handler);
        
        if (escapeBit) {
          handler.setEscaping(oldEscapeSetting);
        }
        break;
      case 2: 
        copyAttribute(nodeID, eType, handler);
        break;
      case 13: 
        handler.namespaceAfterStartElement(getNodeNameX(node), getNodeValue(node));
        break;
      case 1: case 4: case 5: case 6: case 10: case 11: case 12: default: 
        if (type == 1)
        {

          String name = copyElement(nodeID, eType, handler);
          

          copyNS(nodeID, handler, !isChild);
          copyAttributes(nodeID, handler);
          
          for (int c = _firstch2(nodeID); c != -1; c = _nextsib2(c)) {
            copy(makeNodeHandle(c), handler, true);
          }
          

          handler.endElement(name);
        }
        else
        {
          String uri = getNamespaceName(node);
          if (uri.length() != 0) {
            String prefix = getPrefix(node);
            handler.namespaceAfterStartElement(prefix, uri);
          }
          handler.addAttribute(getNodeName(node), getNodeValue(node));
        }
        break;
      }
    }
    catch (Exception e) {
      throw new TransletException(e);
    }
  }
  



  private void copyPI(int node, SerializationHandler handler)
    throws TransletException
  {
    String target = getNodeName(node);
    String value = getStringValueX(node);
    try
    {
      handler.processingInstruction(target, value);
    } catch (Exception e) {
      throw new TransletException(e);
    }
  }
  



  public String shallowCopy(int node, SerializationHandler handler)
    throws TransletException
  {
    int nodeID = makeNodeIdentity(node);
    int exptype = _exptype2(nodeID);
    int type = _exptype2Type(exptype);
    try
    {
      switch (type)
      {
      case 1: 
        String name = copyElement(nodeID, exptype, handler);
        copyNS(nodeID, handler, true);
        return name;
      case 0: 
      case 9: 
        return "";
      case 3: 
        copyTextNode(nodeID, handler);
        return null;
      case 7: 
        copyPI(node, handler);
        return null;
      case 8: 
        handler.comment(getStringValueX(node));
        return null;
      case 13: 
        handler.namespaceAfterStartElement(getNodeNameX(node), getNodeValue(node));
        return null;
      case 2: 
        copyAttribute(nodeID, exptype, handler);
        return null;
      }
      String uri1 = getNamespaceName(node);
      if (uri1.length() != 0) {
        String prefix = getPrefix(node);
        handler.namespaceAfterStartElement(prefix, uri1);
      }
      handler.addAttribute(getNodeName(node), getNodeValue(node));
      return null;
    }
    catch (Exception e) {
      throw new TransletException(e);
    }
  }
  



  public String getLanguage(int node)
  {
    int parent = node;
    while (-1 != parent) {
      if (1 == getNodeType(parent)) {
        int langAttr = getAttributeNode(parent, "http://www.w3.org/XML/1998/namespace", "lang");
        
        if (-1 != langAttr) {
          return getNodeValue(langAttr);
        }
      }
      
      parent = getParent(parent);
    }
    return null;
  }
  





  public DOMBuilder getBuilder()
  {
    return this;
  }
  




  public SerializationHandler getOutputDomBuilder()
  {
    return new ToXMLSAXHandler(this, "UTF-8");
  }
  



  public DOM getResultTreeFrag(int initSize, int rtfType)
  {
    return getResultTreeFrag(initSize, rtfType, true);
  }
  








  public DOM getResultTreeFrag(int initSize, int rtfType, boolean addToManager)
  {
    if (rtfType == 0) {
      if (addToManager) {
        int dtmPos = _dtmManager.getFirstFreeDTMID();
        SimpleResultTreeImpl rtf = new SimpleResultTreeImpl(_dtmManager, dtmPos << 16);
        
        _dtmManager.addDTM(rtf, dtmPos, 0);
        return rtf;
      }
      
      return new SimpleResultTreeImpl(_dtmManager, 0);
    }
    
    if (rtfType == 1) {
      if (addToManager) {
        int dtmPos = _dtmManager.getFirstFreeDTMID();
        AdaptiveResultTreeImpl rtf = new AdaptiveResultTreeImpl(_dtmManager, dtmPos << 16, m_wsfilter, initSize, m_buildIdIndex);
        

        _dtmManager.addDTM(rtf, dtmPos, 0);
        return rtf;
      }
      

      return new AdaptiveResultTreeImpl(_dtmManager, 0, m_wsfilter, initSize, m_buildIdIndex);
    }
    


    return (DOM)_dtmManager.getDTM(null, true, m_wsfilter, true, false, false, initSize, m_buildIdIndex);
  }
  





  public org.apache.xalan.xsltc.runtime.Hashtable getElementsWithIDs()
  {
    if (m_idAttributes == null) {
      return null;
    }
    

    Iterator idEntries = m_idAttributes.entrySet().iterator();
    if (!idEntries.hasNext()) {
      return null;
    }
    
    org.apache.xalan.xsltc.runtime.Hashtable idAttrsTable = new org.apache.xalan.xsltc.runtime.Hashtable();
    
    while (idEntries.hasNext()) {
      Map.Entry entry = (Map.Entry)idEntries.next();
      idAttrsTable.put(entry.getKey(), entry.getValue());
    }
    
    return idAttrsTable;
  }
  







  public String getUnparsedEntityURI(String name)
  {
    if (_document != null) {
      String uri = "";
      DocumentType doctype = _document.getDoctype();
      if (doctype != null) {
        NamedNodeMap entities = doctype.getEntities();
        
        if (entities == null) {
          return uri;
        }
        
        Entity entity = (Entity)entities.getNamedItem(name);
        
        if (entity == null) {
          return uri;
        }
        
        String notationName = entity.getNotationName();
        if (notationName != null) {
          uri = entity.getSystemId();
          if (uri == null) {
            uri = entity.getPublicId();
          }
        }
      }
      return uri;
    }
    
    return super.getUnparsedEntityURI(name);
  }
}
