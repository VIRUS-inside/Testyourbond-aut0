package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;








































public abstract class MultiValuedNodeHeapIterator
  extends DTMAxisIteratorBase
{
  private static final int InitSize = 8;
  public MultiValuedNodeHeapIterator() {}
  
  public abstract class HeapNode
    implements Cloneable
  {
    protected int _node;
    protected int _markedNode;
    protected boolean _isStartSet = false;
    



    public HeapNode() {}
    


    public abstract int step();
    


    public HeapNode cloneHeapNode()
    {
      HeapNode clone;
      

      try
      {
        clone = (HeapNode)super.clone();
      } catch (CloneNotSupportedException e) {
        BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", e.toString());
        
        return null;
      }
      
      _node = _node;
      _markedNode = _node;
      
      return clone;
    }
    


    public void setMark()
    {
      _markedNode = _node;
    }
    


    public void gotoMark()
    {
      _node = _markedNode;
    }
    






    public abstract boolean isLessThan(HeapNode paramHeapNode);
    






    public abstract HeapNode setStartNode(int paramInt);
    






    public abstract HeapNode reset();
  }
  





  private int _heapSize = 0;
  private int _size = 8;
  private HeapNode[] _heap = new HeapNode[8];
  private int _free = 0;
  


  private int _returnedLast;
  

  private int _cachedReturnedLast = -1;
  
  private int _cachedHeapSize;
  

  public DTMAxisIterator cloneIterator()
  {
    _isRestartable = false;
    HeapNode[] heapCopy = new HeapNode[_heap.length];
    try {
      MultiValuedNodeHeapIterator clone = (MultiValuedNodeHeapIterator)super.clone();
      

      for (int i = 0; i < _free; i++) {
        heapCopy[i] = _heap[i].cloneHeapNode();
      }
      clone.setRestartable(false);
      _heap = heapCopy;
      return clone.reset();
    }
    catch (CloneNotSupportedException e) {
      BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", e.toString());
    }
    return null;
  }
  
  protected void addHeapNode(HeapNode node)
  {
    if (_free == _size) {
      HeapNode[] newArray = new HeapNode[this._size *= 2];
      System.arraycopy(_heap, 0, newArray, 0, _free);
      _heap = newArray;
    }
    _heapSize += 1;
    _heap[(_free++)] = node;
  }
  
  public int next() {
    while (_heapSize > 0) {
      int smallest = _heap[0]._node;
      if (smallest == -1) {
        if (_heapSize > 1)
        {
          HeapNode temp = _heap[0];
          _heap[0] = _heap[(--_heapSize)];
          _heap[_heapSize] = temp;
        }
        else {
          return -1;
        }
      }
      else if (smallest == _returnedLast) {
        _heap[0].step();
      }
      else {
        _heap[0].step();
        heapify(0);
        return returnNode(this._returnedLast = smallest);
      }
      
      heapify(0);
    }
    return -1;
  }
  
  public DTMAxisIterator setStartNode(int node) {
    if (_isRestartable) {
      _startNode = node;
      for (int i = 0; i < _free; i++) {
        if (!_heap[i]._isStartSet) {
          _heap[i].setStartNode(node);
          _heap[i].step();
          _heap[i]._isStartSet = true;
        }
      }
      
      for (int i = (this._heapSize = _free) / 2; i >= 0; i--) {
        heapify(i);
      }
      _returnedLast = -1;
      return resetPosition();
    }
    return this;
  }
  
  protected void init() {
    for (int i = 0; i < _free; i++) {
      _heap[i] = null;
    }
    
    _heapSize = 0;
    _free = 0;
  }
  

  private void heapify(int i)
  {
    for (;;)
    {
      int r = i + 1 << 1;int l = r - 1;
      int smallest = (l < _heapSize) && (_heap[l].isLessThan(_heap[i])) ? l : i;
      
      if ((r < _heapSize) && (_heap[r].isLessThan(_heap[smallest]))) {
        smallest = r;
      }
      if (smallest == i) break;
      HeapNode temp = _heap[smallest];
      _heap[smallest] = _heap[i];
      _heap[i] = temp;
      i = smallest;
    }
  }
  


  public void setMark()
  {
    for (int i = 0; i < _free; i++) {
      _heap[i].setMark();
    }
    _cachedReturnedLast = _returnedLast;
    _cachedHeapSize = _heapSize;
  }
  
  public void gotoMark() {
    for (int i = 0; i < _free; i++) {
      _heap[i].gotoMark();
    }
    
    for (int i = (this._heapSize = _cachedHeapSize) / 2; i >= 0; i--) {
      heapify(i);
    }
    _returnedLast = _cachedReturnedLast;
  }
  
  public DTMAxisIterator reset() {
    for (int i = 0; i < _free; i++) {
      _heap[i].reset();
      _heap[i].step();
    }
    

    for (int i = (this._heapSize = _free) / 2; i >= 0; i--) {
      heapify(i);
    }
    
    _returnedLast = -1;
    return resetPosition();
  }
}
