package org.apache.xml.dtm.ref;

import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.utils.WrappedRuntimeException;




























public abstract class DTMAxisIteratorBase
  implements DTMAxisIterator
{
  protected int _last = -1;
  



  protected int _position = 0;
  




  protected int _markedNode;
  



  protected int _startNode = -1;
  



  protected boolean _includeSelf = false;
  




  protected boolean _isRestartable = true;
  


  public DTMAxisIteratorBase() {}
  


  public int getStartNode()
  {
    return _startNode;
  }
  





  public DTMAxisIterator reset()
  {
    boolean temp = _isRestartable;
    
    _isRestartable = true;
    
    setStartNode(_startNode);
    
    _isRestartable = temp;
    
    return this;
  }
  










  public DTMAxisIterator includeSelf()
  {
    _includeSelf = true;
    
    return this;
  }
  












  public int getLast()
  {
    if (_last == -1)
    {







      int temp = _position;
      setMark();
      
      reset();
      do
      {
        _last += 1;
      }
      while (next() != -1);
      
      gotoMark();
      _position = temp;
    }
    
    return _last;
  }
  




  public int getPosition()
  {
    return _position == 0 ? 1 : _position;
  }
  



  public boolean isReverse()
  {
    return false;
  }
  








  public DTMAxisIterator cloneIterator()
  {
    try
    {
      DTMAxisIteratorBase clone = (DTMAxisIteratorBase)super.clone();
      
      _isRestartable = false;
      

      return clone;
    }
    catch (CloneNotSupportedException e)
    {
      throw new WrappedRuntimeException(e);
    }
  }
  


















  protected final int returnNode(int node)
  {
    _position += 1;
    
    return node;
  }
  









  protected final DTMAxisIterator resetPosition()
  {
    _position = 0;
    
    return this;
  }
  






  public boolean isDocOrdered()
  {
    return true;
  }
  






  public int getAxis()
  {
    return -1;
  }
  
  public void setRestartable(boolean isRestartable) {
    _isRestartable = isRestartable;
  }
  






  public int getNodeByPosition(int position)
  {
    if (position > 0) {
      int pos = isReverse() ? getLast() - position + 1 : position;
      
      int node;
      while ((node = next()) != -1) {
        if (pos == getPosition()) {
          return node;
        }
      }
    }
    return -1;
  }
}
