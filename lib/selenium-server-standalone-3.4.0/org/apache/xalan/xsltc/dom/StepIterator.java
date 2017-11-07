package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;














































public class StepIterator
  extends DTMAxisIteratorBase
{
  protected DTMAxisIterator _source;
  protected DTMAxisIterator _iterator;
  private int _pos = -1;
  
  public StepIterator(DTMAxisIterator source, DTMAxisIterator iterator) {
    _source = source;
    _iterator = iterator;
  }
  


  public void setRestartable(boolean isRestartable)
  {
    _isRestartable = isRestartable;
    _source.setRestartable(isRestartable);
    _iterator.setRestartable(true);
  }
  
  public DTMAxisIterator cloneIterator() {
    _isRestartable = false;
    try {
      StepIterator clone = (StepIterator)super.clone();
      _source = _source.cloneIterator();
      _iterator = _iterator.cloneIterator();
      _iterator.setRestartable(true);
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
      _source.setStartNode(this._startNode = node);
      


      _iterator.setStartNode(_includeSelf ? _startNode : _source.next());
      return resetPosition();
    }
    return this;
  }
  
  public DTMAxisIterator reset() {
    _source.reset();
    
    _iterator.setStartNode(_includeSelf ? _startNode : _source.next());
    return resetPosition();
  }
  
  public int next() {
    for (;;) {
      int node;
      if ((node = _iterator.next()) != -1) {
        return returnNode(node);
      }
      
      if ((node = _source.next()) == -1) {
        return -1;
      }
      

      _iterator.setStartNode(node);
    }
  }
  
  public void setMark()
  {
    _source.setMark();
    _iterator.setMark();
  }
  
  public void gotoMark()
  {
    _source.gotoMark();
    _iterator.gotoMark();
  }
}
