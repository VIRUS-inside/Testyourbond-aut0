package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.DTMFilter;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;








































public final class FilterIterator
  extends DTMAxisIteratorBase
{
  private DTMAxisIterator _source;
  private final DTMFilter _filter;
  private final boolean _isReverse;
  
  public FilterIterator(DTMAxisIterator source, DTMFilter filter)
  {
    _source = source;
    
    _filter = filter;
    _isReverse = source.isReverse();
  }
  
  public boolean isReverse() {
    return _isReverse;
  }
  
  public void setRestartable(boolean isRestartable)
  {
    _isRestartable = isRestartable;
    _source.setRestartable(isRestartable);
  }
  
  public DTMAxisIterator cloneIterator()
  {
    try {
      FilterIterator clone = (FilterIterator)super.clone();
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
    _source.reset();
    return resetPosition();
  }
  
  public int next() {
    int node;
    while ((node = _source.next()) != -1) {
      if (_filter.acceptNode(node, -1) == 1) {
        return returnNode(node);
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
