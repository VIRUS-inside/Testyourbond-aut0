package org.apache.xalan.xsltc.dom;

import org.apache.xml.dtm.DTMAxisIterator;





















public class ArrayNodeListIterator
  implements DTMAxisIterator
{
  private int _pos = 0;
  
  private int _mark = 0;
  
  private int[] _nodes;
  
  private static final int[] EMPTY = new int[0];
  
  public ArrayNodeListIterator(int[] nodes) {
    _nodes = nodes;
  }
  
  public int next() {
    return _pos < _nodes.length ? _nodes[(_pos++)] : -1;
  }
  
  public DTMAxisIterator reset() {
    _pos = 0;
    return this;
  }
  
  public int getLast() {
    return _nodes.length;
  }
  
  public int getPosition() {
    return _pos;
  }
  
  public void setMark() {
    _mark = _pos;
  }
  
  public void gotoMark() {
    _pos = _mark;
  }
  
  public DTMAxisIterator setStartNode(int node) {
    if (node == -1) _nodes = EMPTY;
    return this;
  }
  
  public int getStartNode() {
    return -1;
  }
  
  public boolean isReverse() {
    return false;
  }
  
  public DTMAxisIterator cloneIterator() {
    return new ArrayNodeListIterator(_nodes);
  }
  
  public void setRestartable(boolean isRestartable) {}
  
  public int getNodeByPosition(int position)
  {
    return _nodes[(position - 1)];
  }
}
