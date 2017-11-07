package org.apache.xpath.axes;

import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xpath.XPathContext;




























public class ReverseAxesWalker
  extends AxesWalker
{
  static final long serialVersionUID = 2847007647832768941L;
  protected DTMAxisIterator m_iterator;
  
  ReverseAxesWalker(LocPathIterator locPathIterator, int axis)
  {
    super(locPathIterator, axis);
  }
  






  public void setRoot(int root)
  {
    super.setRoot(root);
    m_iterator = getDTM(root).getAxisIterator(m_axis);
    m_iterator.setStartNode(root);
  }
  





  public void detach()
  {
    m_iterator = null;
    super.detach();
  }
  





  protected int getNextNode()
  {
    if (m_foundLast) {
      return -1;
    }
    int next = m_iterator.next();
    
    if (m_isFresh) {
      m_isFresh = false;
    }
    if (-1 == next) {
      m_foundLast = true;
    }
    return next;
  }
  






  public boolean isReverseAxes()
  {
    return true;
  }
  
























  protected int getProximityPosition(int predicateIndex)
  {
    if (predicateIndex < 0) {
      return -1;
    }
    int count = m_proximityPositions[predicateIndex];
    
    if (count <= 0)
    {
      AxesWalker savedWalker = wi().getLastUsedWalker();
      
      try
      {
        ReverseAxesWalker clone = (ReverseAxesWalker)clone();
        
        clone.setRoot(getRoot());
        
        clone.setPredicateCount(predicateIndex);
        
        clone.setPrevWalker(null);
        clone.setNextWalker(null);
        wi().setLastUsedWalker(clone);
        

        count++;
        
        int next;
        while (-1 != (next = clone.nextNode()))
        {
          count++;
        }
        
        m_proximityPositions[predicateIndex] = count;



      }
      catch (CloneNotSupportedException cnse) {}finally
      {


        wi().setLastUsedWalker(savedWalker);
      }
    }
    
    return count;
  }
  





  protected void countProximityPosition(int i)
  {
    if (i < m_proximityPositions.length) {
      m_proximityPositions[i] -= 1;
    }
  }
  









  public int getLastPos(XPathContext xctxt)
  {
    int count = 0;
    AxesWalker savedWalker = wi().getLastUsedWalker();
    
    try
    {
      ReverseAxesWalker clone = (ReverseAxesWalker)clone();
      
      clone.setRoot(getRoot());
      
      clone.setPredicateCount(m_predicateIndex);
      
      clone.setPrevWalker(null);
      clone.setNextWalker(null);
      wi().setLastUsedWalker(clone);
      

      int next;
      

      while (-1 != (next = clone.nextNode()))
      {
        count++;

      }
      


    }
    catch (CloneNotSupportedException cnse) {}finally
    {

      wi().setLastUsedWalker(savedWalker);
    }
    
    return count;
  }
  







  public boolean isDocOrdered()
  {
    return false;
  }
}
