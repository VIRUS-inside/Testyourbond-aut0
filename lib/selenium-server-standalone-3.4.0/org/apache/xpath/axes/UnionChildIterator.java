package org.apache.xpath.axes;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.patterns.NodeTest;






























public class UnionChildIterator
  extends ChildTestIterator
{
  static final long serialVersionUID = 3500298482193003495L;
  private PredicatedNodeTest[] m_nodeTests = null;
  



  public UnionChildIterator()
  {
    super(null);
  }
  











  public void addNodeTest(PredicatedNodeTest test)
  {
    if (null == m_nodeTests)
    {
      m_nodeTests = new PredicatedNodeTest[1];
      m_nodeTests[0] = test;
    }
    else
    {
      PredicatedNodeTest[] tests = m_nodeTests;
      int len = m_nodeTests.length;
      
      m_nodeTests = new PredicatedNodeTest[len + 1];
      
      System.arraycopy(tests, 0, m_nodeTests, 0, len);
      
      m_nodeTests[len] = test;
    }
    test.exprSetParent(this);
  }
  










  public void fixupVariables(Vector vars, int globalsSize)
  {
    super.fixupVariables(vars, globalsSize);
    if (m_nodeTests != null) {
      for (int i = 0; i < m_nodeTests.length; i++) {
        m_nodeTests[i].fixupVariables(vars, globalsSize);
      }
    }
  }
  









  public short acceptNode(int n)
  {
    XPathContext xctxt = getXPathContext();
    try
    {
      xctxt.pushCurrentNode(n);
      for (int i = 0; i < m_nodeTests.length; i++)
      {
        PredicatedNodeTest pnt = m_nodeTests[i];
        XObject score = pnt.execute(xctxt, n);
        if (score != NodeTest.SCORE_NONE)
        {
          short s;
          if (pnt.getPredicateCount() > 0)
          {
            if (pnt.executePredicates(n, xctxt)) {
              return 1;
            }
          } else {
            return 1;
          }
          
        }
        
      }
    }
    catch (TransformerException se)
    {
      throw new RuntimeException(se.getMessage());
    }
    finally
    {
      xctxt.popCurrentNode();
    }
    return 3;
  }
}
