package org.apache.xpath.axes;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMAxisTraverser;
import org.apache.xpath.Expression;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.compiler.OpMap;




























public class OneStepIteratorForward
  extends ChildTestIterator
{
  static final long serialVersionUID = -1576936606178190566L;
  protected int m_axis = -1;
  









  OneStepIteratorForward(Compiler compiler, int opPos, int analysis)
    throws TransformerException
  {
    super(compiler, opPos, analysis);
    int firstStepPos = OpMap.getFirstChildPos(opPos);
    
    m_axis = WalkerFactory.getAxisFromStep(compiler, firstStepPos);
  }
  








  public OneStepIteratorForward(int axis)
  {
    super(null);
    
    m_axis = axis;
    int whatToShow = -1;
    initNodeTest(whatToShow);
  }
  










  public void setRoot(int context, Object environment)
  {
    super.setRoot(context, environment);
    m_traverser = m_cdtm.getAxisTraverser(m_axis);
  }
  












































  protected int getNextNode()
  {
    m_lastFetched = (-1 == m_lastFetched ? m_traverser.first(m_context) : m_traverser.next(m_context, m_lastFetched));
    

    return m_lastFetched;
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
