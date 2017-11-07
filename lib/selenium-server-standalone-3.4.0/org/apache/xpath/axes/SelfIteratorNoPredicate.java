package org.apache.xpath.axes;

import javax.xml.transform.TransformerException;
import org.apache.xpath.XPathContext;
import org.apache.xpath.compiler.Compiler;




































public class SelfIteratorNoPredicate
  extends LocPathIterator
{
  static final long serialVersionUID = -4226887905279814201L;
  
  SelfIteratorNoPredicate(Compiler compiler, int opPos, int analysis)
    throws TransformerException
  {
    super(compiler, opPos, analysis, false);
  }
  





  public SelfIteratorNoPredicate()
    throws TransformerException
  {
    super(null);
  }
  









  public int nextNode()
  {
    if (m_foundLast) {
      return -1;
    }
    
    int next;
    m_lastFetched = (next = -1 == m_lastFetched ? m_context : -1);
    



    if (-1 != next)
    {
      m_pos += 1;
      
      return next;
    }
    

    m_foundLast = true;
    
    return -1;
  }
  









  public int asNode(XPathContext xctxt)
    throws TransformerException
  {
    return xctxt.getCurrentNode();
  }
  








  public int getLastPos(XPathContext xctxt)
  {
    return 1;
  }
}
