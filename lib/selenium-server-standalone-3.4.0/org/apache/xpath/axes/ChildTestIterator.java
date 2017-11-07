package org.apache.xpath.axes;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMAxisTraverser;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xpath.compiler.Compiler;








































public class ChildTestIterator
  extends BasicTestIterator
{
  static final long serialVersionUID = -7936835957960705722L;
  protected transient DTMAxisTraverser m_traverser;
  
  ChildTestIterator(Compiler compiler, int opPos, int analysis)
    throws TransformerException
  {
    super(compiler, opPos, analysis);
  }
  








  public ChildTestIterator(DTMAxisTraverser traverser)
  {
    super(null);
    
    m_traverser = traverser;
  }
  






  protected int getNextNode()
  {
    m_lastFetched = (-1 == m_lastFetched ? m_traverser.first(m_context) : m_traverser.next(m_context, m_lastFetched));
    










    return m_lastFetched;
  }
  









  public DTMIterator cloneWithReset()
    throws CloneNotSupportedException
  {
    ChildTestIterator clone = (ChildTestIterator)super.cloneWithReset();
    m_traverser = m_traverser;
    
    return clone;
  }
  








  public void setRoot(int context, Object environment)
  {
    super.setRoot(context, environment);
    m_traverser = m_cdtm.getAxisTraverser(3);
  }
  

























  public int getAxis()
  {
    return 3;
  }
  







  public void detach()
  {
    if (m_allowDetach)
    {
      m_traverser = null;
      

      super.detach();
    }
  }
}
