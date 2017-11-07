package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;
























public final class NthIterator
  extends DTMAxisIteratorBase
{
  private DTMAxisIterator _source;
  private final int _position;
  private boolean _ready;
  
  public NthIterator(DTMAxisIterator source, int n)
  {
    _source = source;
    _position = n;
  }
  
  public void setRestartable(boolean isRestartable) {
    _isRestartable = isRestartable;
    _source.setRestartable(isRestartable);
  }
  
  public DTMAxisIterator cloneIterator() {
    try {
      NthIterator clone = (NthIterator)super.clone();
      _source = _source.cloneIterator();
      _isRestartable = false;
      return clone;
    }
    catch (CloneNotSupportedException e) {
      BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", e.toString());
    }
    return null;
  }
  
  public int next()
  {
    if (_ready) {
      _ready = false;
      return _source.getNodeByPosition(_position);
    }
    return -1;
  }
  















  public DTMAxisIterator setStartNode(int node)
  {
    if (_isRestartable) {
      _source.setStartNode(node);
      _ready = true;
    }
    return this;
  }
  
  public DTMAxisIterator reset() {
    _source.reset();
    _ready = true;
    return this;
  }
  
  public int getLast() {
    return 1;
  }
  
  public int getPosition() {
    return 1;
  }
  
  public void setMark() {
    _source.setMark();
  }
  
  public void gotoMark() {
    _source.gotoMark();
  }
}
