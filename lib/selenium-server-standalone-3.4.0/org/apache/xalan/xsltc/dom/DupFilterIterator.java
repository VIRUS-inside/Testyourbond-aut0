package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xalan.xsltc.util.IntegerArray;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;



































public final class DupFilterIterator
  extends DTMAxisIteratorBase
{
  private DTMAxisIterator _source;
  private IntegerArray _nodes = new IntegerArray();
  



  private int _current = 0;
  



  private int _nodesSize = 0;
  



  private int _lastNext = -1;
  



  private int _markedLastNext = -1;
  
  public DupFilterIterator(DTMAxisIterator source) {
    _source = source;
    




    if ((source instanceof KeyIndex)) {
      setStartNode(0);
    }
  }
  




  public DTMAxisIterator setStartNode(int node)
  {
    if (_isRestartable)
    {

      boolean sourceIsKeyIndex = _source instanceof KeyIndex;
      
      if ((sourceIsKeyIndex) && (_startNode == 0))
      {
        return this;
      }
      
      if (node != _startNode) {
        _source.setStartNode(this._startNode = node);
        
        _nodes.clear();
        while ((node = _source.next()) != -1) {
          _nodes.add(node);
        }
        


        if (!sourceIsKeyIndex) {
          _nodes.sort();
        }
        
        _nodesSize = _nodes.cardinality();
        _current = 0;
        _lastNext = -1;
        resetPosition();
      }
    }
    return this;
  }
  
  public int next() {
    while (_current < _nodesSize) {
      int next = _nodes.at(_current++);
      if (next != _lastNext) {
        return returnNode(this._lastNext = next);
      }
    }
    return -1;
  }
  
  public DTMAxisIterator cloneIterator() {
    try {
      DupFilterIterator clone = (DupFilterIterator)super.clone();
      
      _nodes = ((IntegerArray)_nodes.clone());
      _source = _source.cloneIterator();
      _isRestartable = false;
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
  
  public void setMark() {
    _markedNode = _current;
    _markedLastNext = _lastNext;
  }
  
  public void gotoMark() {
    _current = _markedNode;
    _lastNext = _markedLastNext;
  }
  
  public DTMAxisIterator reset() {
    _current = 0;
    _lastNext = -1;
    return resetPosition();
  }
}
