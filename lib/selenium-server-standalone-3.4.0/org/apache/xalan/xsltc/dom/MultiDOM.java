package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.StripFilter;
import org.apache.xalan.xsltc.TransletException;
import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xalan.xsltc.runtime.Hashtable;
import org.apache.xml.dtm.Axis;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;
import org.apache.xml.dtm.ref.DTMDefaultBase;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.SuballocatedIntVector;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;






























public final class MultiDOM
  implements DOM
{
  private static final int NO_TYPE = -2;
  private static final int INITIAL_SIZE = 4;
  private DOM[] _adapters;
  private DOMAdapter _main;
  private DTMManager _dtmManager;
  private int _free;
  private int _size;
  private Hashtable _documents = new Hashtable();
  
  private final class AxisIterator
    extends DTMAxisIteratorBase
  {
    private final int _axis;
    private final int _type;
    private DTMAxisIterator _source;
    private int _dtmId = -1;
    
    public AxisIterator(int axis, int type) {
      _axis = axis;
      _type = type;
    }
    
    public int next() {
      if (_source == null) {
        return -1;
      }
      return _source.next();
    }
    
    public void setRestartable(boolean flag)
    {
      if (_source != null) {
        _source.setRestartable(flag);
      }
    }
    
    public DTMAxisIterator setStartNode(int node) {
      if (node == -1) {
        return this;
      }
      
      int dom = node >>> 16;
      

      if ((_source == null) || (_dtmId != dom)) {
        if (_type == -2) {
          _source = _adapters[dom].getAxisIterator(_axis);
        } else if (_axis == 3) {
          _source = _adapters[dom].getTypedChildren(_type);
        } else {
          _source = _adapters[dom].getTypedAxisIterator(_axis, _type);
        }
      }
      
      _dtmId = dom;
      _source.setStartNode(node);
      return this;
    }
    
    public DTMAxisIterator reset() {
      if (_source != null) {
        _source.reset();
      }
      return this;
    }
    
    public int getLast() {
      if (_source != null) {
        return _source.getLast();
      }
      
      return -1;
    }
    
    public int getPosition()
    {
      if (_source != null) {
        return _source.getPosition();
      }
      
      return -1;
    }
    
    public boolean isReverse()
    {
      return Axis.isReverse(_axis);
    }
    
    public void setMark() {
      if (_source != null) {
        _source.setMark();
      }
    }
    
    public void gotoMark() {
      if (_source != null) {
        _source.gotoMark();
      }
    }
    
    public DTMAxisIterator cloneIterator() {
      AxisIterator clone = new AxisIterator(MultiDOM.this, _axis, _type);
      if (_source != null) {
        _source = _source.cloneIterator();
      }
      _dtmId = _dtmId;
      return clone;
    }
  }
  

  private final class NodeValueIterator
    extends DTMAxisIteratorBase
  {
    private DTMAxisIterator _source;
    
    private String _value;
    
    private boolean _op;
    
    private final boolean _isReverse;
    private int _returnType = 1;
    
    public NodeValueIterator(DTMAxisIterator source, int returnType, String value, boolean op)
    {
      _source = source;
      _returnType = returnType;
      _value = value;
      _op = op;
      _isReverse = source.isReverse();
    }
    
    public boolean isReverse() {
      return _isReverse;
    }
    
    public DTMAxisIterator cloneIterator() {
      try {
        NodeValueIterator clone = (NodeValueIterator)super.clone();
        _source = _source.cloneIterator();
        clone.setRestartable(false);
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
    
    public DTMAxisIterator reset() {
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
    
    public DTMAxisIterator setStartNode(int node) {
      if (_isRestartable) {
        _source.setStartNode(this._startNode = node);
        return resetPosition();
      }
      return this;
    }
    
    public void setMark() {
      _source.setMark();
    }
    
    public void gotoMark() {
      _source.gotoMark();
    }
  }
  
  public MultiDOM(DOM main) {
    _size = 4;
    _free = 1;
    _adapters = new DOM[4];
    DOMAdapter adapter = (DOMAdapter)main;
    _adapters[0] = adapter;
    _main = adapter;
    DOM dom = adapter.getDOMImpl();
    if ((dom instanceof DTMDefaultBase)) {
      _dtmManager = ((DTMDefaultBase)dom).getManager();
    }
    














    addDOMAdapter(adapter, false);
  }
  
  public int nextMask() {
    return _free;
  }
  

  public void setupMapping(String[] names, String[] uris, int[] types, String[] namespaces) {}
  
  public int addDOMAdapter(DOMAdapter adapter)
  {
    return addDOMAdapter(adapter, true);
  }
  
  private int addDOMAdapter(DOMAdapter adapter, boolean indexByURI)
  {
    DOM dom = adapter.getDOMImpl();
    
    int domNo = 1;
    int dtmSize = 1;
    SuballocatedIntVector dtmIds = null;
    if ((dom instanceof DTMDefaultBase)) {
      DTMDefaultBase dtmdb = (DTMDefaultBase)dom;
      dtmIds = dtmdb.getDTMIDs();
      dtmSize = dtmIds.size();
      domNo = dtmIds.elementAt(dtmSize - 1) >>> 16;
    }
    else if ((dom instanceof SimpleResultTreeImpl)) {
      SimpleResultTreeImpl simpleRTF = (SimpleResultTreeImpl)dom;
      domNo = simpleRTF.getDocument() >>> 16;
    }
    
    if (domNo >= _size) {
      int oldSize = _size;
      do {
        _size *= 2;
      } while (_size <= domNo);
      
      DOMAdapter[] newArray = new DOMAdapter[_size];
      System.arraycopy(_adapters, 0, newArray, 0, oldSize);
      _adapters = newArray;
    }
    
    _free = (domNo + 1);
    
    if (dtmSize == 1) {
      _adapters[domNo] = adapter;
    }
    else if (dtmIds != null) {
      int domPos = 0;
      for (int i = dtmSize - 1; i >= 0; i--) {
        domPos = dtmIds.elementAt(i) >>> 16;
        _adapters[domPos] = adapter;
      }
      domNo = domPos;
    }
    

    if (indexByURI) {
      String uri = adapter.getDocumentURI(0);
      _documents.put(uri, new Integer(domNo));
    }
    



    if ((dom instanceof AdaptiveResultTreeImpl)) {
      AdaptiveResultTreeImpl adaptiveRTF = (AdaptiveResultTreeImpl)dom;
      DOM nestedDom = adaptiveRTF.getNestedDOM();
      if (nestedDom != null) {
        DOMAdapter newAdapter = new DOMAdapter(nestedDom, adapter.getNamesArray(), adapter.getUrisArray(), adapter.getTypesArray(), adapter.getNamespaceArray());
        



        addDOMAdapter(newAdapter);
      }
    }
    
    return domNo;
  }
  
  public int getDocumentMask(String uri) {
    Integer domIdx = (Integer)_documents.get(uri);
    if (domIdx == null) {
      return -1;
    }
    return domIdx.intValue();
  }
  
  public DOM getDOMAdapter(String uri)
  {
    Integer domIdx = (Integer)_documents.get(uri);
    if (domIdx == null) {
      return null;
    }
    return _adapters[domIdx.intValue()];
  }
  

  public int getDocument()
  {
    return _main.getDocument();
  }
  
  public DTMManager getDTMManager() {
    return _dtmManager;
  }
  



  public DTMAxisIterator getIterator()
  {
    return _main.getIterator();
  }
  
  public String getStringValue() {
    return _main.getStringValue();
  }
  
  public DTMAxisIterator getChildren(int node) {
    return _adapters[getDTMId(node)].getChildren(node);
  }
  
  public DTMAxisIterator getTypedChildren(int type) {
    return new AxisIterator(3, type);
  }
  
  public DTMAxisIterator getAxisIterator(int axis) {
    return new AxisIterator(axis, -2);
  }
  
  public DTMAxisIterator getTypedAxisIterator(int axis, int type)
  {
    return new AxisIterator(axis, type);
  }
  

  public DTMAxisIterator getNthDescendant(int node, int n, boolean includeself)
  {
    return _adapters[getDTMId(node)].getNthDescendant(node, n, includeself);
  }
  


  public DTMAxisIterator getNodeValueIterator(DTMAxisIterator iterator, int type, String value, boolean op)
  {
    return new NodeValueIterator(iterator, type, value, op);
  }
  

  public DTMAxisIterator getNamespaceAxisIterator(int axis, int ns)
  {
    DTMAxisIterator iterator = _main.getNamespaceAxisIterator(axis, ns);
    return iterator;
  }
  
  public DTMAxisIterator orderNodes(DTMAxisIterator source, int node) {
    return _adapters[getDTMId(node)].orderNodes(source, node);
  }
  
  public int getExpandedTypeID(int node) {
    if (node != -1) {
      return _adapters[(node >>> 16)].getExpandedTypeID(node);
    }
    
    return -1;
  }
  
  public int getNamespaceType(int node)
  {
    return _adapters[getDTMId(node)].getNamespaceType(node);
  }
  
  public int getNSType(int node)
  {
    return _adapters[getDTMId(node)].getNSType(node);
  }
  
  public int getParent(int node) {
    if (node == -1) {
      return -1;
    }
    return _adapters[(node >>> 16)].getParent(node);
  }
  
  public int getAttributeNode(int type, int el) {
    if (el == -1) {
      return -1;
    }
    return _adapters[(el >>> 16)].getAttributeNode(type, el);
  }
  
  public String getNodeName(int node) {
    if (node == -1) {
      return "";
    }
    return _adapters[(node >>> 16)].getNodeName(node);
  }
  
  public String getNodeNameX(int node) {
    if (node == -1) {
      return "";
    }
    return _adapters[(node >>> 16)].getNodeNameX(node);
  }
  
  public String getNamespaceName(int node) {
    if (node == -1) {
      return "";
    }
    return _adapters[(node >>> 16)].getNamespaceName(node);
  }
  
  public String getStringValueX(int node) {
    if (node == -1) {
      return "";
    }
    return _adapters[(node >>> 16)].getStringValueX(node);
  }
  
  public void copy(int node, SerializationHandler handler)
    throws TransletException
  {
    if (node != -1) {
      _adapters[(node >>> 16)].copy(node, handler);
    }
  }
  
  public void copy(DTMAxisIterator nodes, SerializationHandler handler)
    throws TransletException
  {
    int node;
    while ((node = nodes.next()) != -1) {
      _adapters[(node >>> 16)].copy(node, handler);
    }
  }
  

  public String shallowCopy(int node, SerializationHandler handler)
    throws TransletException
  {
    if (node == -1) {
      return "";
    }
    return _adapters[(node >>> 16)].shallowCopy(node, handler);
  }
  
  public boolean lessThan(int node1, int node2) {
    if (node1 == -1) {
      return true;
    }
    if (node2 == -1) {
      return false;
    }
    int dom1 = getDTMId(node1);
    int dom2 = getDTMId(node2);
    return dom1 < dom2 ? true : dom1 == dom2 ? _adapters[dom1].lessThan(node1, node2) : false;
  }
  

  public void characters(int textNode, SerializationHandler handler)
    throws TransletException
  {
    if (textNode != -1) {
      _adapters[(textNode >>> 16)].characters(textNode, handler);
    }
  }
  
  public void setFilter(StripFilter filter) {
    for (int dom = 0; dom < _free; dom++) {
      if (_adapters[dom] != null) {
        _adapters[dom].setFilter(filter);
      }
    }
  }
  
  public Node makeNode(int index) {
    if (index == -1) {
      return null;
    }
    return _adapters[getDTMId(index)].makeNode(index);
  }
  
  public Node makeNode(DTMAxisIterator iter)
  {
    return _main.makeNode(iter);
  }
  
  public NodeList makeNodeList(int index) {
    if (index == -1) {
      return null;
    }
    return _adapters[getDTMId(index)].makeNodeList(index);
  }
  
  public NodeList makeNodeList(DTMAxisIterator iter)
  {
    return _main.makeNodeList(iter);
  }
  
  public String getLanguage(int node) {
    return _adapters[getDTMId(node)].getLanguage(node);
  }
  
  public int getSize() {
    int size = 0;
    for (int i = 0; i < _size; i++) {
      size += _adapters[i].getSize();
    }
    return size;
  }
  
  public String getDocumentURI(int node) {
    if (node == -1) {
      node = 0;
    }
    return _adapters[(node >>> 16)].getDocumentURI(0);
  }
  
  public boolean isElement(int node) {
    if (node == -1) {
      return false;
    }
    return _adapters[(node >>> 16)].isElement(node);
  }
  
  public boolean isAttribute(int node) {
    if (node == -1) {
      return false;
    }
    return _adapters[(node >>> 16)].isAttribute(node);
  }
  
  public int getDTMId(int nodeHandle)
  {
    if (nodeHandle == -1) {
      return 0;
    }
    int id = nodeHandle >>> 16;
    while ((id >= 2) && (_adapters[id] == _adapters[(id - 1)])) {
      id--;
    }
    return id;
  }
  
  public int getNodeIdent(int nodeHandle)
  {
    return _adapters[(nodeHandle >>> 16)].getNodeIdent(nodeHandle);
  }
  
  public int getNodeHandle(int nodeId)
  {
    return _main.getNodeHandle(nodeId);
  }
  
  public DOM getResultTreeFrag(int initSize, int rtfType)
  {
    return _main.getResultTreeFrag(initSize, rtfType);
  }
  
  public DOM getResultTreeFrag(int initSize, int rtfType, boolean addToManager)
  {
    return _main.getResultTreeFrag(initSize, rtfType, addToManager);
  }
  
  public DOM getMain()
  {
    return _main;
  }
  



  public SerializationHandler getOutputDomBuilder()
  {
    return _main.getOutputDomBuilder();
  }
  
  public String lookupNamespace(int node, String prefix)
    throws TransletException
  {
    return _main.lookupNamespace(node, prefix);
  }
  
  public String getUnparsedEntityURI(String entity)
  {
    return _main.getUnparsedEntityURI(entity);
  }
  
  public Hashtable getElementsWithIDs()
  {
    return _main.getElementsWithIDs();
  }
}
