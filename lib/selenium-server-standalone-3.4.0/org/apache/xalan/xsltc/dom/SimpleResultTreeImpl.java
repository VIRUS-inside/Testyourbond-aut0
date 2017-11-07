package org.apache.xalan.xsltc.dom;

import javax.xml.transform.SourceLocator;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.StripFilter;
import org.apache.xalan.xsltc.TransletException;
import org.apache.xalan.xsltc.runtime.Hashtable;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.DTMAxisTraverser;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;
import org.apache.xml.dtm.ref.DTMManagerDefault;
import org.apache.xml.serializer.EmptySerializer;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.XMLString;
import org.apache.xml.utils.XMLStringDefault;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;












































public class SimpleResultTreeImpl
  extends EmptySerializer
  implements DOM, DTM
{
  public final class SimpleIterator
    extends DTMAxisIteratorBase
  {
    static final int DIRECTION_UP = 0;
    static final int DIRECTION_DOWN = 1;
    static final int NO_TYPE = -1;
    int _direction = 1;
    
    int _type = -1;
    
    int _currentNode;
    

    public SimpleIterator() {}
    
    public SimpleIterator(int direction)
    {
      _direction = direction;
    }
    
    public SimpleIterator(int direction, int type)
    {
      _direction = direction;
      _type = type;
    }
    


    public int next()
    {
      if (_direction == 1) {
        while (_currentNode < 2) {
          if (_type != -1) {
            if (((_currentNode == 0) && (_type == 0)) || ((_currentNode == 1) && (_type == 3)))
            {
              return returnNode(getNodeHandle(_currentNode++));
            }
            _currentNode += 1;
          }
          else {
            return returnNode(getNodeHandle(_currentNode++));
          }
        }
        return -1;
      }
      

      while (_currentNode >= 0) {
        if (_type != -1) {
          if (((_currentNode == 0) && (_type == 0)) || ((_currentNode == 1) && (_type == 3)))
          {
            return returnNode(getNodeHandle(_currentNode--));
          }
          _currentNode -= 1;
        }
        else {
          return returnNode(getNodeHandle(_currentNode--));
        }
      }
      return -1;
    }
    

    public DTMAxisIterator setStartNode(int nodeHandle)
    {
      int nodeID = getNodeIdent(nodeHandle);
      _startNode = nodeID;
      

      if ((!_includeSelf) && (nodeID != -1)) {
        if (_direction == 1) {
          nodeID++;
        } else if (_direction == 0) {
          nodeID--;
        }
      }
      _currentNode = nodeID;
      return this;
    }
    
    public void setMark()
    {
      _markedNode = _currentNode;
    }
    
    public void gotoMark()
    {
      _currentNode = _markedNode;
    }
  }
  


  public final class SingletonIterator
    extends DTMAxisIteratorBase
  {
    static final int NO_TYPE = -1;
    
    int _type = -1;
    
    int _currentNode;
    

    public SingletonIterator() {}
    
    public SingletonIterator(int type)
    {
      _type = type;
    }
    
    public void setMark()
    {
      _markedNode = _currentNode;
    }
    
    public void gotoMark()
    {
      _currentNode = _markedNode;
    }
    
    public DTMAxisIterator setStartNode(int nodeHandle)
    {
      _currentNode = (this._startNode = getNodeIdent(nodeHandle));
      return this;
    }
    
    public int next()
    {
      if (_currentNode == -1) {
        return -1;
      }
      _currentNode = -1;
      
      if (_type != -1) {
        if (((_currentNode == 0) && (_type == 0)) || ((_currentNode == 1) && (_type == 3)))
        {
          return getNodeHandle(_currentNode);
        }
      } else {
        return getNodeHandle(_currentNode);
      }
      return -1;
    }
  }
  


  private static final DTMAxisIterator EMPTY_ITERATOR = new DTMAxisIteratorBase()
  {
    public DTMAxisIterator reset() { return this; }
    public DTMAxisIterator setStartNode(int node) { return this; }
    public int next() { return -1; }
    public void setMark() {}
    public void gotoMark() {}
    public int getLast() { return 0; }
    public int getPosition() { return 0; }
    public DTMAxisIterator cloneIterator() { return this; }
    


    public void setRestartable(boolean isRestartable) {}
  };
  

  public static final int RTF_ROOT = 0;
  

  public static final int RTF_TEXT = 1;
  
  public static final int NUMBER_OF_NODES = 2;
  
  private static int _documentURIIndex = 0;
  


  private static final String EMPTY_STR = "";
  


  private String _text;
  


  protected String[] _textArray;
  

  protected XSLTCDTMManager _dtmManager;
  

  protected int _size = 0;
  

  private int _documentID;
  

  private BitArray _dontEscape = null;
  

  private boolean _escaping = true;
  

  public SimpleResultTreeImpl(XSLTCDTMManager dtmManager, int documentID)
  {
    _dtmManager = dtmManager;
    _documentID = documentID;
    _textArray = new String[4];
  }
  
  public DTMManagerDefault getDTMManager()
  {
    return _dtmManager;
  }
  

  public int getDocument()
  {
    return _documentID;
  }
  

  public String getStringValue()
  {
    return _text;
  }
  
  public DTMAxisIterator getIterator()
  {
    return new SingletonIterator(getDocument());
  }
  
  public DTMAxisIterator getChildren(int node)
  {
    return new SimpleIterator().setStartNode(node);
  }
  
  public DTMAxisIterator getTypedChildren(int type)
  {
    return new SimpleIterator(1, type);
  }
  


  public DTMAxisIterator getAxisIterator(int axis)
  {
    switch (axis)
    {
    case 3: 
    case 4: 
      return new SimpleIterator(1);
    case 0: 
    case 10: 
      return new SimpleIterator(0);
    case 1: 
      return new SimpleIterator(0).includeSelf();
    case 5: 
      return new SimpleIterator(1).includeSelf();
    case 13: 
      return new SingletonIterator();
    }
    return EMPTY_ITERATOR;
  }
  

  public DTMAxisIterator getTypedAxisIterator(int axis, int type)
  {
    switch (axis)
    {
    case 3: 
    case 4: 
      return new SimpleIterator(1, type);
    case 0: 
    case 10: 
      return new SimpleIterator(0, type);
    case 1: 
      return new SimpleIterator(0, type).includeSelf();
    case 5: 
      return new SimpleIterator(1, type).includeSelf();
    case 13: 
      return new SingletonIterator(type);
    }
    return EMPTY_ITERATOR;
  }
  


  public DTMAxisIterator getNthDescendant(int node, int n, boolean includeself)
  {
    return null;
  }
  
  public DTMAxisIterator getNamespaceAxisIterator(int axis, int ns)
  {
    return null;
  }
  


  public DTMAxisIterator getNodeValueIterator(DTMAxisIterator iter, int returnType, String value, boolean op)
  {
    return null;
  }
  
  public DTMAxisIterator orderNodes(DTMAxisIterator source, int node)
  {
    return source;
  }
  
  public String getNodeName(int node)
  {
    if (getNodeIdent(node) == 1) {
      return "#text";
    }
    return "";
  }
  
  public String getNodeNameX(int node)
  {
    return "";
  }
  
  public String getNamespaceName(int node)
  {
    return "";
  }
  

  public int getExpandedTypeID(int nodeHandle)
  {
    int nodeID = getNodeIdent(nodeHandle);
    if (nodeID == 1)
      return 3;
    if (nodeID == 0) {
      return 0;
    }
    return -1;
  }
  
  public int getNamespaceType(int node)
  {
    return 0;
  }
  
  public int getParent(int nodeHandle)
  {
    int nodeID = getNodeIdent(nodeHandle);
    return nodeID == 1 ? getNodeHandle(0) : -1;
  }
  
  public int getAttributeNode(int gType, int element)
  {
    return -1;
  }
  
  public String getStringValueX(int nodeHandle)
  {
    int nodeID = getNodeIdent(nodeHandle);
    if ((nodeID == 0) || (nodeID == 1)) {
      return _text;
    }
    return "";
  }
  
  public void copy(int node, SerializationHandler handler)
    throws TransletException
  {
    characters(node, handler);
  }
  
  public void copy(DTMAxisIterator nodes, SerializationHandler handler)
    throws TransletException
  {
    int node;
    while ((node = nodes.next()) != -1)
    {
      copy(node, handler);
    }
  }
  
  public String shallowCopy(int node, SerializationHandler handler)
    throws TransletException
  {
    characters(node, handler);
    return null;
  }
  
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
  






  public void characters(int node, SerializationHandler handler)
    throws TransletException
  {
    int nodeID = getNodeIdent(node);
    if ((nodeID == 0) || (nodeID == 1)) {
      boolean escapeBit = false;
      boolean oldEscapeSetting = false;
      try
      {
        for (int i = 0; i < _size; i++)
        {
          if (_dontEscape != null) {
            escapeBit = _dontEscape.getBit(i);
            if (escapeBit) {
              oldEscapeSetting = handler.setEscaping(false);
            }
          }
          
          handler.characters(_textArray[i]);
          
          if (escapeBit) {
            handler.setEscaping(oldEscapeSetting);
          }
        }
      } catch (SAXException e) {
        throw new TransletException(e);
      }
    }
  }
  

  public Node makeNode(int index)
  {
    return null;
  }
  
  public Node makeNode(DTMAxisIterator iter)
  {
    return null;
  }
  
  public NodeList makeNodeList(int index)
  {
    return null;
  }
  
  public NodeList makeNodeList(DTMAxisIterator iter)
  {
    return null;
  }
  
  public String getLanguage(int node)
  {
    return null;
  }
  
  public int getSize()
  {
    return 2;
  }
  
  public String getDocumentURI(int node)
  {
    return "simple_rtf" + _documentURIIndex++;
  }
  


  public void setFilter(StripFilter filter) {}
  

  public void setupMapping(String[] names, String[] uris, int[] types, String[] namespaces) {}
  

  public boolean isElement(int node)
  {
    return false;
  }
  
  public boolean isAttribute(int node)
  {
    return false;
  }
  
  public String lookupNamespace(int node, String prefix)
    throws TransletException
  {
    return null;
  }
  



  public int getNodeIdent(int nodehandle)
  {
    return nodehandle != -1 ? nodehandle - _documentID : -1;
  }
  



  public int getNodeHandle(int nodeId)
  {
    return nodeId != -1 ? nodeId + _documentID : -1;
  }
  
  public DOM getResultTreeFrag(int initialSize, int rtfType)
  {
    return null;
  }
  
  public DOM getResultTreeFrag(int initialSize, int rtfType, boolean addToManager)
  {
    return null;
  }
  
  public SerializationHandler getOutputDomBuilder()
  {
    return this;
  }
  
  public int getNSType(int node)
  {
    return 0;
  }
  
  public String getUnparsedEntityURI(String name)
  {
    return null;
  }
  
  public Hashtable getElementsWithIDs()
  {
    return null;
  }
  





  public void startDocument()
    throws SAXException
  {}
  




  public void endDocument()
    throws SAXException
  {
    if (_size == 1) {
      _text = _textArray[0];
    } else {
      StringBuffer buffer = new StringBuffer();
      for (int i = 0; i < _size; i++) {
        buffer.append(_textArray[i]);
      }
      _text = buffer.toString();
    }
  }
  
  public void characters(String str)
    throws SAXException
  {
    if (_size >= _textArray.length) {
      String[] newTextArray = new String[_textArray.length * 2];
      System.arraycopy(_textArray, 0, newTextArray, 0, _textArray.length);
      _textArray = newTextArray;
    }
    


    if (!_escaping)
    {
      if (_dontEscape == null) {
        _dontEscape = new BitArray(8);
      }
      

      if (_size >= _dontEscape.size()) {
        _dontEscape.resize(_dontEscape.size() * 2);
      }
      _dontEscape.setBit(_size);
    }
    
    _textArray[(_size++)] = str;
  }
  
  public void characters(char[] ch, int offset, int length)
    throws SAXException
  {
    if (_size >= _textArray.length) {
      String[] newTextArray = new String[_textArray.length * 2];
      System.arraycopy(_textArray, 0, newTextArray, 0, _textArray.length);
      _textArray = newTextArray;
    }
    
    if (!_escaping) {
      if (_dontEscape == null) {
        _dontEscape = new BitArray(8);
      }
      
      if (_size >= _dontEscape.size()) {
        _dontEscape.resize(_dontEscape.size() * 2);
      }
      _dontEscape.setBit(_size);
    }
    
    _textArray[(_size++)] = new String(ch, offset, length);
  }
  
  public boolean setEscaping(boolean escape)
    throws SAXException
  {
    boolean temp = _escaping;
    _escaping = escape;
    return temp;
  }
  





  public void setFeature(String featureId, boolean state) {}
  





  public void setProperty(String property, Object value) {}
  





  public DTMAxisTraverser getAxisTraverser(int axis)
  {
    return null;
  }
  
  public boolean hasChildNodes(int nodeHandle)
  {
    return getNodeIdent(nodeHandle) == 0;
  }
  
  public int getFirstChild(int nodeHandle)
  {
    int nodeID = getNodeIdent(nodeHandle);
    if (nodeID == 0) {
      return getNodeHandle(1);
    }
    return -1;
  }
  
  public int getLastChild(int nodeHandle)
  {
    return getFirstChild(nodeHandle);
  }
  
  public int getAttributeNode(int elementHandle, String namespaceURI, String name)
  {
    return -1;
  }
  
  public int getFirstAttribute(int nodeHandle)
  {
    return -1;
  }
  
  public int getFirstNamespaceNode(int nodeHandle, boolean inScope)
  {
    return -1;
  }
  
  public int getNextSibling(int nodeHandle)
  {
    return -1;
  }
  
  public int getPreviousSibling(int nodeHandle)
  {
    return -1;
  }
  
  public int getNextAttribute(int nodeHandle)
  {
    return -1;
  }
  

  public int getNextNamespaceNode(int baseHandle, int namespaceHandle, boolean inScope)
  {
    return -1;
  }
  
  public int getOwnerDocument(int nodeHandle)
  {
    return getDocument();
  }
  
  public int getDocumentRoot(int nodeHandle)
  {
    return getDocument();
  }
  
  public XMLString getStringValue(int nodeHandle)
  {
    return new XMLStringDefault(getStringValueX(nodeHandle));
  }
  
  public int getStringValueChunkCount(int nodeHandle)
  {
    return 0;
  }
  

  public char[] getStringValueChunk(int nodeHandle, int chunkIndex, int[] startAndLen)
  {
    return null;
  }
  
  public int getExpandedTypeID(String namespace, String localName, int type)
  {
    return -1;
  }
  
  public String getLocalNameFromExpandedNameID(int ExpandedNameID)
  {
    return "";
  }
  
  public String getNamespaceFromExpandedNameID(int ExpandedNameID)
  {
    return "";
  }
  
  public String getLocalName(int nodeHandle)
  {
    return "";
  }
  
  public String getPrefix(int nodeHandle)
  {
    return null;
  }
  
  public String getNamespaceURI(int nodeHandle)
  {
    return "";
  }
  
  public String getNodeValue(int nodeHandle)
  {
    return getNodeIdent(nodeHandle) == 1 ? _text : null;
  }
  
  public short getNodeType(int nodeHandle)
  {
    int nodeID = getNodeIdent(nodeHandle);
    if (nodeID == 1)
      return 3;
    if (nodeID == 0) {
      return 0;
    }
    return -1;
  }
  

  public short getLevel(int nodeHandle)
  {
    int nodeID = getNodeIdent(nodeHandle);
    if (nodeID == 1)
      return 2;
    if (nodeID == 0) {
      return 1;
    }
    return -1;
  }
  
  public boolean isSupported(String feature, String version)
  {
    return false;
  }
  
  public String getDocumentBaseURI()
  {
    return "";
  }
  

  public void setDocumentBaseURI(String baseURI) {}
  

  public String getDocumentSystemIdentifier(int nodeHandle)
  {
    return null;
  }
  
  public String getDocumentEncoding(int nodeHandle)
  {
    return null;
  }
  
  public String getDocumentStandalone(int nodeHandle)
  {
    return null;
  }
  
  public String getDocumentVersion(int documentHandle)
  {
    return null;
  }
  
  public boolean getDocumentAllDeclarationsProcessed()
  {
    return false;
  }
  
  public String getDocumentTypeDeclarationSystemIdentifier()
  {
    return null;
  }
  
  public String getDocumentTypeDeclarationPublicIdentifier()
  {
    return null;
  }
  
  public int getElementById(String elementId)
  {
    return -1;
  }
  
  public boolean supportsPreStripping()
  {
    return false;
  }
  
  public boolean isNodeAfter(int firstNodeHandle, int secondNodeHandle)
  {
    return lessThan(firstNodeHandle, secondNodeHandle);
  }
  
  public boolean isCharacterElementContentWhitespace(int nodeHandle)
  {
    return false;
  }
  
  public boolean isDocumentAllDeclarationsProcessed(int documentHandle)
  {
    return false;
  }
  
  public boolean isAttributeSpecified(int attributeHandle)
  {
    return false;
  }
  


  public void dispatchCharactersEvents(int nodeHandle, ContentHandler ch, boolean normalize)
    throws SAXException
  {}
  


  public void dispatchToEvents(int nodeHandle, ContentHandler ch)
    throws SAXException
  {}
  

  public Node getNode(int nodeHandle)
  {
    return makeNode(nodeHandle);
  }
  
  public boolean needsTwoThreads()
  {
    return false;
  }
  
  public ContentHandler getContentHandler()
  {
    return null;
  }
  
  public LexicalHandler getLexicalHandler()
  {
    return null;
  }
  
  public EntityResolver getEntityResolver()
  {
    return null;
  }
  
  public DTDHandler getDTDHandler()
  {
    return null;
  }
  
  public ErrorHandler getErrorHandler()
  {
    return null;
  }
  
  public DeclHandler getDeclHandler()
  {
    return null;
  }
  


  public void appendChild(int newChild, boolean clone, boolean cloneDepth) {}
  

  public void appendTextChild(String str) {}
  

  public SourceLocator getSourceLocatorFor(int node)
  {
    return null;
  }
  
  public void documentRegistration() {}
  
  public void documentRelease() {}
  
  public void migrateTo(DTMManager manager) {}
}
