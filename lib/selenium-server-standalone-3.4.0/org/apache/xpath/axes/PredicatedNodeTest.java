package org.apache.xpath.axes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.patterns.NodeTest;




















public abstract class PredicatedNodeTest
  extends NodeTest
  implements SubContextList
{
  static final long serialVersionUID = -6193530757296377351L;
  
  PredicatedNodeTest(LocPathIterator locPathIterator)
  {
    m_lpi = locPathIterator;
  }
  







  PredicatedNodeTest() {}
  







  private void readObject(ObjectInputStream stream)
    throws IOException, TransformerException
  {
    try
    {
      stream.defaultReadObject();
      m_predicateIndex = -1;
      resetProximityPositions();
    }
    catch (ClassNotFoundException cnfe)
    {
      throw new TransformerException(cnfe);
    }
  }
  








  public Object clone()
    throws CloneNotSupportedException
  {
    PredicatedNodeTest clone = (PredicatedNodeTest)super.clone();
    
    if ((null != m_proximityPositions) && (m_proximityPositions == m_proximityPositions))
    {

      m_proximityPositions = new int[m_proximityPositions.length];
      
      System.arraycopy(m_proximityPositions, 0, m_proximityPositions, 0, m_proximityPositions.length);
    }
    


    if (m_lpi == this) {
      m_lpi = ((LocPathIterator)clone);
    }
    return clone;
  }
  

  protected int m_predCount = -1;
  





  public int getPredicateCount()
  {
    if (-1 == m_predCount) {
      return null == m_predicates ? 0 : m_predicates.length;
    }
    return m_predCount;
  }
  










  public void setPredicateCount(int count)
  {
    if (count > 0)
    {
      Expression[] newPredicates = new Expression[count];
      for (int i = 0; i < count; i++)
      {
        newPredicates[i] = m_predicates[i];
      }
      m_predicates = newPredicates;
    }
    else {
      m_predicates = null;
    }
  }
  










  protected void initPredicateInfo(Compiler compiler, int opPos)
    throws TransformerException
  {
    int pos = compiler.getFirstPredicateOpPos(opPos);
    
    if (pos > 0)
    {
      m_predicates = compiler.getCompiledPredicates(pos);
      if (null != m_predicates)
      {
        for (int i = 0; i < m_predicates.length; i++)
        {
          m_predicates[i].exprSetParent(this);
        }
      }
    }
  }
  








  public Expression getPredicate(int index)
  {
    return m_predicates[index];
  }
  







  public int getProximityPosition()
  {
    return getProximityPosition(m_predicateIndex);
  }
  







  public int getProximityPosition(XPathContext xctxt)
  {
    return getProximityPosition();
  }
  








  public abstract int getLastPos(XPathContext paramXPathContext);
  








  protected int getProximityPosition(int predicateIndex)
  {
    return predicateIndex >= 0 ? m_proximityPositions[predicateIndex] : 0;
  }
  



  public void resetProximityPositions()
  {
    int nPredicates = getPredicateCount();
    if (nPredicates > 0)
    {
      if (null == m_proximityPositions) {
        m_proximityPositions = new int[nPredicates];
      }
      for (int i = 0; i < nPredicates; i++)
      {
        try
        {
          initProximityPosition(i);

        }
        catch (Exception e)
        {
          throw new WrappedRuntimeException(e);
        }
      }
    }
  }
  






  public void initProximityPosition(int i)
    throws TransformerException
  {
    m_proximityPositions[i] = 0;
  }
  








  protected void countProximityPosition(int i)
  {
    int[] pp = m_proximityPositions;
    if ((null != pp) && (i < pp.length)) {
      pp[i] += 1;
    }
  }
  




  public boolean isReverseAxes()
  {
    return false;
  }
  





  public int getPredicateIndex()
  {
    return m_predicateIndex;
  }
  











  boolean executePredicates(int context, XPathContext xctxt)
    throws TransformerException
  {
    int nPredicates = getPredicateCount();
    
    if (nPredicates == 0) {
      return true;
    }
    PrefixResolver savedResolver = xctxt.getNamespaceContext();
    
    try
    {
      m_predicateIndex = 0;
      xctxt.pushSubContextList(this);
      xctxt.pushNamespaceContext(m_lpi.getPrefixResolver());
      xctxt.pushCurrentNode(context);
      
      for (int i = 0; i < nPredicates; i++)
      {

        XObject pred = m_predicates[i].execute(xctxt);
        
        int proxPos;
        if (2 == pred.getType())
        {










          proxPos = getProximityPosition(m_predicateIndex);
          int predIndex = (int)pred.num();
          if (proxPos != predIndex)
          {






            return false;
          }
          















          if ((m_predicates[i].isStableNumber()) && (i == nPredicates - 1))
          {
            m_foundLast = true;
          }
        }
        else if (!pred.bool()) {
          return 0;
        }
        countProximityPosition(++m_predicateIndex);
      }
    }
    finally
    {
      xctxt.popCurrentNode();
      xctxt.popNamespaceContext();
      xctxt.popSubContextList();
      m_predicateIndex = -1;
    }
    
    return true;
  }
  










  public void fixupVariables(Vector vars, int globalsSize)
  {
    super.fixupVariables(vars, globalsSize);
    
    int nPredicates = getPredicateCount();
    
    for (int i = 0; i < nPredicates; i++)
    {
      m_predicates[i].fixupVariables(vars, globalsSize);
    }
  }
  








  protected String nodeToString(int n)
  {
    if (-1 != n)
    {
      DTM dtm = m_lpi.getXPathContext().getDTM(n);
      return dtm.getNodeName(n) + "{" + (n + 1) + "}";
    }
    

    return "null";
  }
  













  public short acceptNode(int n)
  {
    XPathContext xctxt = m_lpi.getXPathContext();
    
    try
    {
      xctxt.pushCurrentNode(n);
      
      XObject score = execute(xctxt, n);
      

      if (score != NodeTest.SCORE_NONE) {
        short s;
        if (getPredicateCount() > 0)
        {
          countProximityPosition(0);
          
          if (!executePredicates(n, xctxt)) {
            return 3;
          }
        }
        return 1;
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
  






  public LocPathIterator getLocPathIterator()
  {
    return m_lpi;
  }
  






  public void setLocPathIterator(LocPathIterator li)
  {
    m_lpi = li;
    if (this != li) {
      li.exprSetParent(this);
    }
  }
  





  public boolean canTraverseOutsideSubtree()
  {
    int n = getPredicateCount();
    for (int i = 0; i < n; i++)
    {
      if (getPredicate(i).canTraverseOutsideSubtree())
        return true;
    }
    return false;
  }
  







  public void callPredicateVisitors(XPathVisitor visitor)
  {
    if (null != m_predicates)
    {
      int n = m_predicates.length;
      for (int i = 0; i < n; i++)
      {
        ExpressionOwner predOwner = new PredOwner(i);
        if (visitor.visitPredicate(predOwner, m_predicates[i]))
        {
          m_predicates[i].callVisitors(predOwner, visitor);
        }
      }
    }
  }
  




  public boolean deepEquals(Expression expr)
  {
    if (!super.deepEquals(expr)) {
      return false;
    }
    PredicatedNodeTest pnt = (PredicatedNodeTest)expr;
    if (null != m_predicates)
    {

      int n = m_predicates.length;
      if ((null == m_predicates) || (m_predicates.length != n))
        return false;
      for (int i = 0; i < n; i++)
      {
        if (!m_predicates[i].deepEquals(m_predicates[i])) {
          return false;
        }
      }
    } else if (null != m_predicates) {
      return false;
    }
    return true;
  }
  

  protected transient boolean m_foundLast = false;
  



  protected LocPathIterator m_lpi;
  


  transient int m_predicateIndex = -1;
  


  private Expression[] m_predicates;
  

  protected transient int[] m_proximityPositions;
  

  static final boolean DEBUG_PREDICATECOUNTING = false;
  


  class PredOwner
    implements ExpressionOwner
  {
    int m_index;
    


    PredOwner(int index)
    {
      m_index = index;
    }
    



    public Expression getExpression()
    {
      return m_predicates[m_index];
    }
    




    public void setExpression(Expression exp)
    {
      exp.exprSetParent(PredicatedNodeTest.this);
      m_predicates[m_index] = exp;
    }
  }
}
