package org.apache.xpath.axes;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.VariableStack;
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.objects.XNodeSet;




























public class FilterExprIteratorSimple
  extends LocPathIterator
{
  static final long serialVersionUID = -6978977187025375579L;
  private Expression m_expr;
  private transient XNodeSet m_exprObj;
  private boolean m_mustHardReset = false;
  private boolean m_canDetachNodeset = true;
  




  public FilterExprIteratorSimple()
  {
    super(null);
  }
  




  public FilterExprIteratorSimple(Expression expr)
  {
    super(null);
    m_expr = expr;
  }
  







  public void setRoot(int context, Object environment)
  {
    super.setRoot(context, environment);
    m_exprObj = executeFilterExpr(context, m_execContext, getPrefixResolver(), getIsTopLevel(), m_stackFrame, m_expr);
  }
  









  public static XNodeSet executeFilterExpr(int context, XPathContext xctxt, PrefixResolver prefixResolver, boolean isTopLevel, int stackFrame, Expression expr)
    throws WrappedRuntimeException
  {
    PrefixResolver savedResolver = xctxt.getNamespaceContext();
    XNodeSet result = null;
    
    try
    {
      xctxt.pushCurrentNode(context);
      xctxt.setNamespaceContext(prefixResolver);
      





      if (isTopLevel)
      {

        VariableStack vars = xctxt.getVarStack();
        

        int savedStart = vars.getStackFrame();
        vars.setStackFrame(stackFrame);
        
        result = (XNodeSet)expr.execute(xctxt);
        result.setShouldCacheNodes(true);
        

        vars.setStackFrame(savedStart);
      }
      else {
        result = (XNodeSet)expr.execute(xctxt);
      }
      

    }
    catch (TransformerException se)
    {
      throw new WrappedRuntimeException(se);
    }
    finally
    {
      xctxt.popCurrentNode();
      xctxt.setNamespaceContext(savedResolver);
    }
    return result;
  }
  








  public int nextNode()
  {
    if (m_foundLast) {
      return -1;
    }
    
    int next;
    if (null != m_exprObj) {
      int next;
      m_lastFetched = (next = m_exprObj.nextNode());
    }
    else {
      m_lastFetched = (next = -1);
    }
    
    if (-1 != next)
    {
      m_pos += 1;
      return next;
    }
    

    m_foundLast = true;
    
    return -1;
  }
  






  public void detach()
  {
    if (m_allowDetach)
    {
      super.detach();
      m_exprObj.detach();
      m_exprObj = null;
    }
  }
  










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
      exp.exprSetParent(FilterExprIteratorSimple.this);
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
    FilterExprIteratorSimple fet = (FilterExprIteratorSimple)expr;
    if (!m_expr.deepEquals(m_expr)) {
      return false;
    }
    return true;
  }
  






  public int getAxis()
  {
    if (null != m_exprObj) {
      return m_exprObj.getAxis();
    }
    return 20;
  }
}
