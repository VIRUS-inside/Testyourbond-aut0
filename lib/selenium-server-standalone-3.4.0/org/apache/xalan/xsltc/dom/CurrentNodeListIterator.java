package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.runtime.AbstractTranslet;
import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xalan.xsltc.util.IntegerArray;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;

















































public final class CurrentNodeListIterator
  extends DTMAxisIteratorBase
{
  private boolean _docOrder;
  private DTMAxisIterator _source;
  private final CurrentNodeListFilter _filter;
  private IntegerArray _nodes = new IntegerArray();
  



  private int _currentIndex;
  



  private final int _currentNode;
  



  private AbstractTranslet _translet;
  



  public CurrentNodeListIterator(DTMAxisIterator source, CurrentNodeListFilter filter, int currentNode, AbstractTranslet translet)
  {
    this(source, !source.isReverse(), filter, currentNode, translet);
  }
  



  public CurrentNodeListIterator(DTMAxisIterator source, boolean docOrder, CurrentNodeListFilter filter, int currentNode, AbstractTranslet translet)
  {
    _source = source;
    _filter = filter;
    _translet = translet;
    _docOrder = docOrder;
    _currentNode = currentNode;
  }
  
  public DTMAxisIterator forceNaturalOrder() {
    _docOrder = true;
    return this;
  }
  
  public void setRestartable(boolean isRestartable) {
    _isRestartable = isRestartable;
    _source.setRestartable(isRestartable);
  }
  
  public boolean isReverse() {
    return !_docOrder;
  }
  
  public DTMAxisIterator cloneIterator() {
    try {
      CurrentNodeListIterator clone = (CurrentNodeListIterator)super.clone();
      
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
  
  public DTMAxisIterator reset()
  {
    _currentIndex = 0;
    return resetPosition();
  }
  
  public int next() {
    int last = _nodes.cardinality();
    int currentNode = _currentNode;
    AbstractTranslet translet = _translet;
    
    for (int index = _currentIndex; index < last;) {
      int position = _docOrder ? index + 1 : last - index;
      int node = _nodes.at(index++);
      
      if (_filter.test(node, position, last, currentNode, translet, this))
      {
        _currentIndex = index;
        return returnNode(node);
      }
    }
    return -1;
  }
  
  public DTMAxisIterator setStartNode(int node) {
    if (_isRestartable) {
      _source.setStartNode(this._startNode = node);
      
      _nodes.clear();
      while ((node = _source.next()) != -1) {
        _nodes.add(node);
      }
      _currentIndex = 0;
      resetPosition();
    }
    return this;
  }
  
  public int getLast() {
    if (_last == -1) {
      _last = computePositionOfLast();
    }
    return _last;
  }
  
  public void setMark() {
    _markedNode = _currentIndex;
  }
  
  public void gotoMark() {
    _currentIndex = _markedNode;
  }
  
  private int computePositionOfLast() {
    int last = _nodes.cardinality();
    int currNode = _currentNode;
    AbstractTranslet translet = _translet;
    
    int lastPosition = _position;
    for (int index = _currentIndex; index < last;) {
      int position = _docOrder ? index + 1 : last - index;
      int nodeIndex = _nodes.at(index++);
      
      if (_filter.test(nodeIndex, position, last, currNode, translet, this))
      {
        lastPosition++;
      }
    }
    return lastPosition;
  }
}
