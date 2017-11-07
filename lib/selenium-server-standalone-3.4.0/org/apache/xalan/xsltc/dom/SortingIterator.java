package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;



























public final class SortingIterator
  extends DTMAxisIteratorBase
{
  private static final int INIT_DATA_SIZE = 16;
  private DTMAxisIterator _source;
  private NodeSortRecordFactory _factory;
  private NodeSortRecord[] _data;
  private int _free = 0;
  private int _current;
  
  public SortingIterator(DTMAxisIterator source, NodeSortRecordFactory factory)
  {
    _source = source;
    _factory = factory;
  }
  
  public int next() {
    return _current < _free ? _data[(_current++)].getNode() : -1;
  }
  
  public DTMAxisIterator setStartNode(int node) {
    try {
      _source.setStartNode(this._startNode = node);
      _data = new NodeSortRecord[16];
      _free = 0;
      

      while ((node = _source.next()) != -1) {
        addRecord(_factory.makeNodeSortRecord(node, _free));
      }
      
      quicksort(0, _free - 1);
      
      _current = 0;
      return this;
    }
    catch (Exception e) {}
    return this;
  }
  
  public int getPosition()
  {
    return _current == 0 ? 1 : _current;
  }
  
  public int getLast() {
    return _free;
  }
  
  public void setMark() {
    _source.setMark();
    _markedNode = _current;
  }
  
  public void gotoMark() {
    _source.gotoMark();
    _current = _markedNode;
  }
  



  public DTMAxisIterator cloneIterator()
  {
    try
    {
      SortingIterator clone = (SortingIterator)super.clone();
      _source = _source.cloneIterator();
      _factory = _factory;
      _data = _data;
      _free = _free;
      _current = _current;
      clone.setRestartable(false);
      return clone.reset();
    }
    catch (CloneNotSupportedException e) {
      BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", e.toString());
    }
    return null;
  }
  
  private void addRecord(NodeSortRecord record)
  {
    if (_free == _data.length) {
      NodeSortRecord[] newArray = new NodeSortRecord[_data.length * 2];
      System.arraycopy(_data, 0, newArray, 0, _free);
      _data = newArray;
    }
    _data[(_free++)] = record;
  }
  
  private void quicksort(int p, int r) {
    while (p < r) {
      int q = partition(p, r);
      quicksort(p, q);
      p = q + 1;
    }
  }
  
  private int partition(int p, int r) {
    NodeSortRecord x = _data[(p + r >>> 1)];
    int i = p - 1;
    int j = r + 1;
    for (;;) {
      if (x.compareTo(_data[(--j)]) >= 0) {
        while (x.compareTo(_data[(++i)]) > 0) {}
        if (i >= j) break;
        NodeSortRecord t = _data[i];
        _data[i] = _data[j];
        _data[j] = t;
      }
    }
    return j;
  }
}
