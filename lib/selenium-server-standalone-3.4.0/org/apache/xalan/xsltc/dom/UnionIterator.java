package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.DOM;
import org.apache.xml.dtm.DTMAxisIterator;
































public final class UnionIterator
  extends MultiValuedNodeHeapIterator
{
  private final DOM _dom;
  
  private final class LookAheadIterator
    extends MultiValuedNodeHeapIterator.HeapNode
  {
    public DTMAxisIterator iterator;
    
    public LookAheadIterator(DTMAxisIterator iterator)
    {
      super();
      this.iterator = iterator;
    }
    
    public int step() {
      _node = iterator.next();
      return _node;
    }
    
    public MultiValuedNodeHeapIterator.HeapNode cloneHeapNode() {
      LookAheadIterator clone = (LookAheadIterator)super.cloneHeapNode();
      iterator = iterator.cloneIterator();
      return clone;
    }
    
    public void setMark() {
      super.setMark();
      iterator.setMark();
    }
    
    public void gotoMark() {
      super.gotoMark();
      iterator.gotoMark();
    }
    
    public boolean isLessThan(MultiValuedNodeHeapIterator.HeapNode heapNode) {
      LookAheadIterator comparand = (LookAheadIterator)heapNode;
      return _dom.lessThan(_node, _node);
    }
    
    public MultiValuedNodeHeapIterator.HeapNode setStartNode(int node) {
      iterator.setStartNode(node);
      return this;
    }
    
    public MultiValuedNodeHeapIterator.HeapNode reset() {
      iterator.reset();
      return this;
    }
  }
  
  public UnionIterator(DOM dom) {
    _dom = dom;
  }
  
  public UnionIterator addIterator(DTMAxisIterator iterator) {
    addHeapNode(new LookAheadIterator(iterator));
    return this;
  }
}
