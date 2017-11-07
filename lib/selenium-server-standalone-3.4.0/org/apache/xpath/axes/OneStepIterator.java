package org.apache.xpath.axes;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xpath.Expression;
import org.apache.xpath.XPathContext;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.compiler.OpMap;



























public class OneStepIterator
  extends ChildTestIterator
{
  static final long serialVersionUID = 4623710779664998283L;
  protected int m_axis = -1;
  





  protected DTMAxisIterator m_iterator;
  





  OneStepIterator(Compiler compiler, int opPos, int analysis)
    throws TransformerException
  {
    super(compiler, opPos, analysis);
    int firstStepPos = OpMap.getFirstChildPos(opPos);
    
    m_axis = WalkerFactory.getAxisFromStep(compiler, firstStepPos);
  }
  










  public OneStepIterator(DTMAxisIterator iterator, int axis)
    throws TransformerException
  {
    super(null);
    
    m_iterator = iterator;
    m_axis = axis;
    int whatToShow = -1;
    initNodeTest(whatToShow);
  }
  







  public void setRoot(int context, Object environment)
  {
    super.setRoot(context, environment);
    if (m_axis > -1)
      m_iterator = m_cdtm.getAxisIterator(m_axis);
    m_iterator.setStartNode(m_context);
  }
  







  public void detach()
  {
    if (m_allowDetach)
    {
      if (m_axis > -1) {
        m_iterator = null;
      }
      
      super.detach();
    }
  }
  



  protected int getNextNode()
  {
    return this.m_lastFetched = m_iterator.next();
  }
  








  public Object clone()
    throws CloneNotSupportedException
  {
    OneStepIterator clone = (OneStepIterator)super.clone();
    
    if (m_iterator != null)
    {
      m_iterator = m_iterator.cloneIterator();
    }
    return clone;
  }
  








  public DTMIterator cloneWithReset()
    throws CloneNotSupportedException
  {
    OneStepIterator clone = (OneStepIterator)super.cloneWithReset();
    m_iterator = m_iterator;
    
    return clone;
  }
  







  public boolean isReverseAxes()
  {
    return m_iterator.isReverse();
  }
  











  protected int getProximityPosition(int predicateIndex)
  {
    if (!isReverseAxes()) {
      return super.getProximityPosition(predicateIndex);
    }
    


    if (predicateIndex < 0) {
      return -1;
    }
    if (m_proximityPositions[predicateIndex] <= 0)
    {
      XPathContext xctxt = getXPathContext();
      try
      {
        OneStepIterator clone = (OneStepIterator)clone();
        
        int root = getRoot();
        xctxt.pushCurrentNode(root);
        clone.setRoot(root, xctxt);
        

        m_predCount = predicateIndex;
        

        int count = 1;
        
        int next;
        while (-1 != (next = clone.nextNode()))
        {
          count++;
        }
        
        m_proximityPositions[predicateIndex] += count;



      }
      catch (CloneNotSupportedException cnse) {}finally
      {


        xctxt.popCurrentNode();
      }
    }
    
    return m_proximityPositions[predicateIndex];
  }
  






  public int getLength()
  {
    if (!isReverseAxes()) {
      return super.getLength();
    }
    
    boolean isPredicateTest = this == m_execContext.getSubContextList();
    

    int predCount = getPredicateCount();
    



    if ((-1 != m_length) && (isPredicateTest) && (m_predicateIndex < 1)) {
      return m_length;
    }
    int count = 0;
    
    XPathContext xctxt = getXPathContext();
    try
    {
      OneStepIterator clone = (OneStepIterator)cloneWithReset();
      
      int root = getRoot();
      xctxt.pushCurrentNode(root);
      clone.setRoot(root, xctxt);
      
      m_predCount = m_predicateIndex;
      
      int next;
      
      while (-1 != (next = clone.nextNode()))
      {
        count++;

      }
      

    }
    catch (CloneNotSupportedException cnse) {}finally
    {

      xctxt.popCurrentNode();
    }
    if ((isPredicateTest) && (m_predicateIndex < 1)) {
      m_length = count;
    }
    return count;
  }
  





  protected void countProximityPosition(int i)
  {
    if (!isReverseAxes()) {
      super.countProximityPosition(i);
    } else if (i < m_proximityPositions.length) {
      m_proximityPositions[i] -= 1;
    }
  }
  



  public void reset()
  {
    super.reset();
    if (null != m_iterator) {
      m_iterator.reset();
    }
  }
  





  public int getAxis()
  {
    return m_axis;
  }
  



  public boolean deepEquals(Expression expr)
  {
    if (!super.deepEquals(expr)) {
      return false;
    }
    if (m_axis != m_axis) {
      return false;
    }
    return true;
  }
}
