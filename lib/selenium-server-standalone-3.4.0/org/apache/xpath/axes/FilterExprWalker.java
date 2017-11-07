package org.apache.xpath.axes;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.operations.Variable;





























public class FilterExprWalker
  extends AxesWalker
{
  static final long serialVersionUID = 5457182471424488375L;
  private Expression m_expr;
  private transient XNodeSet m_exprObj;
  
  public FilterExprWalker(WalkingIterator locPathIterator)
  {
    super(locPathIterator, 20);
  }
  










  public void init(Compiler compiler, int opPos, int stepType)
    throws TransformerException
  {
    super.init(compiler, opPos, stepType);
    

    switch (stepType)
    {
    case 24: 
    case 25: 
      m_mustHardReset = true;
    case 22: 
    case 23: 
      m_expr = compiler.compile(opPos);
      m_expr.exprSetParent(this);
      
      if ((m_expr instanceof Variable))
      {

        m_canDetachNodeset = false;
      }
      break;
    default: 
      m_expr = compiler.compile(opPos + 2);
      m_expr.exprSetParent(this);
    }
    
  }
  


















  public void detach()
  {
    super.detach();
    if (m_canDetachNodeset)
    {
      m_exprObj.detach();
    }
    m_exprObj = null;
  }
  







  public void setRoot(int root)
  {
    super.setRoot(root);
    
    m_exprObj = FilterExprIteratorSimple.executeFilterExpr(root, m_lpi.getXPathContext(), m_lpi.getPrefixResolver(), m_lpi.getIsTopLevel(), m_lpi.m_stackFrame, m_expr);
  }
  










  public Object clone()
    throws CloneNotSupportedException
  {
    FilterExprWalker clone = (FilterExprWalker)super.clone();
    
    if (null != m_exprObj) {
      m_exprObj = ((XNodeSet)m_exprObj.clone());
    }
    return clone;
  }
  








  public short acceptNode(int n)
  {
    try
    {
      if (getPredicateCount() > 0)
      {
        countProximityPosition(0);
        
        if (!executePredicates(n, m_lpi.getXPathContext())) {
          return 3;
        }
      }
      return 1;
    }
    catch (TransformerException se)
    {
      throw new RuntimeException(se.getMessage());
    }
  }
  










  public int getNextNode()
  {
    if (null != m_exprObj)
    {
      int next = m_exprObj.nextNode();
      return next;
    }
    
    return -1;
  }
  








  public int getLastPos(XPathContext xctxt)
  {
    return m_exprObj.getLength();
  }
  







  private boolean m_mustHardReset = false;
  private boolean m_canDetachNodeset = true;
  










  public void fixupVariables(Vector vars, int globalsSize)
  {
    super.fixupVariables(vars, globalsSize);
    m_expr.fixupVariables(vars, globalsSize);
  }
  



  public Expression getInnerExpression()
  {
    return m_expr;
  }
  



  public void setInnerExpression(Expression expr)
  {
    expr.exprSetParent(this);
    m_expr = expr;
  }
  





  public int getAnalysisBits()
  {
    if ((null != m_expr) && ((m_expr instanceof PathComponent)))
    {
      return ((PathComponent)m_expr).getAnalysisBits();
    }
    return 67108864;
  }
  







  public boolean isDocOrdered()
  {
    return m_exprObj.isDocOrdered();
  }
  






  public int getAxis()
  {
    return m_exprObj.getAxis();
  }
  
  class filterExprOwner
    implements ExpressionOwner
  {
    filterExprOwner() {}
    
    public Expression getExpression()
    {
      return m_expr;
    }
    



    public void setExpression(Expression exp)
    {
      exp.exprSetParent(FilterExprWalker.this);
      m_expr = exp;
    }
  }
  







  public void callPredicateVisitors(XPathVisitor visitor)
  {
    m_expr.callVisitors(new filterExprOwner(), visitor);
    
    super.callPredicateVisitors(visitor);
  }
  




  public boolean deepEquals(Expression expr)
  {
    if (!super.deepEquals(expr)) {
      return false;
    }
    FilterExprWalker walker = (FilterExprWalker)expr;
    if (!m_expr.deepEquals(m_expr)) {
      return false;
    }
    return true;
  }
}
