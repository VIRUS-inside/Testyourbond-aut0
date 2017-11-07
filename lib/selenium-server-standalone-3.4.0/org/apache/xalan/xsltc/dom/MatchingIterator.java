package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;















































public final class MatchingIterator
  extends DTMAxisIteratorBase
{
  private DTMAxisIterator _source;
  private final int _match;
  
  public MatchingIterator(int match, DTMAxisIterator source)
  {
    _source = source;
    _match = match;
  }
  
  public void setRestartable(boolean isRestartable)
  {
    _isRestartable = isRestartable;
    _source.setRestartable(isRestartable);
  }
  
  public DTMAxisIterator cloneIterator()
  {
    try {
      MatchingIterator clone = (MatchingIterator)super.clone();
      _source = _source.cloneIterator();
      _isRestartable = false;
      return clone.reset();
    }
    catch (CloneNotSupportedException e) {
      BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", e.toString());
    }
    return null;
  }
  
  public DTMAxisIterator setStartNode(int node)
  {
    if (_isRestartable)
    {
      _source.setStartNode(node);
      

      _position = 1;
      while (((node = _source.next()) != -1) && (node != _match)) {
        _position += 1;
      }
    }
    return this;
  }
  
  public DTMAxisIterator reset() {
    _source.reset();
    return resetPosition();
  }
  
  public int next() {
    return _source.next();
  }
  
  public int getLast() {
    if (_last == -1) {
      _last = _source.getLast();
    }
    return _last;
  }
  
  public int getPosition() {
    return _position;
  }
  
  public void setMark() {
    _source.setMark();
  }
  
  public void gotoMark() {
    _source.gotoMark();
  }
}
