package org.apache.xalan.xsltc.dom;

import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;























public class SingletonIterator
  extends DTMAxisIteratorBase
{
  private int _node;
  private final boolean _isConstant;
  
  public SingletonIterator()
  {
    this(Integer.MIN_VALUE, false);
  }
  
  public SingletonIterator(int node) {
    this(node, false);
  }
  
  public SingletonIterator(int node, boolean constant) {
    _node = (this._startNode = node);
    _isConstant = constant;
  }
  



  public DTMAxisIterator setStartNode(int node)
  {
    if (_isConstant) {
      _node = _startNode;
      return resetPosition();
    }
    if (_isRestartable) {
      if (_node <= 0)
        _node = (this._startNode = node);
      return resetPosition();
    }
    return this;
  }
  
  public DTMAxisIterator reset() {
    if (_isConstant) {
      _node = _startNode;
      return resetPosition();
    }
    
    boolean temp = _isRestartable;
    _isRestartable = true;
    setStartNode(_startNode);
    _isRestartable = temp;
    
    return this;
  }
  
  public int next() {
    int result = _node;
    _node = -1;
    return returnNode(result);
  }
  
  public void setMark() {
    _markedNode = _node;
  }
  
  public void gotoMark() {
    _node = _markedNode;
  }
}
