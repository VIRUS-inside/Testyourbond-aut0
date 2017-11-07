package org.apache.xpath.axes;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMAxisTraverser;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xpath.Expression;
import org.apache.xpath.VariableStack;
import org.apache.xpath.XPathContext;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.compiler.OpMap;


































public class DescendantIterator
  extends LocPathIterator
{
  static final long serialVersionUID = -1190338607743976938L;
  protected transient DTMAxisTraverser m_traverser;
  protected int m_axis;
  protected int m_extendedTypeID;
  
  DescendantIterator(Compiler compiler, int opPos, int analysis)
    throws TransformerException
  {
    super(compiler, opPos, analysis, false);
    
    int firstStepPos = OpMap.getFirstChildPos(opPos);
    int stepType = compiler.getOp(firstStepPos);
    
    boolean orSelf = 42 == stepType;
    boolean fromRoot = false;
    if (48 == stepType)
    {
      orSelf = true;

    }
    else if (50 == stepType)
    {
      fromRoot = true;
      
      int nextStepPos = compiler.getNextStepPos(firstStepPos);
      if (compiler.getOp(nextStepPos) == 42) {
        orSelf = true;
      }
    }
    

    int nextStepPos = firstStepPos;
    for (;;)
    {
      nextStepPos = compiler.getNextStepPos(nextStepPos);
      if (nextStepPos <= 0)
        break;
      int stepOp = compiler.getOp(nextStepPos);
      if (-1 == stepOp) break;
      firstStepPos = nextStepPos;
    }
    







    if ((analysis & 0x10000) != 0) {
      orSelf = false;
    }
    if (fromRoot)
    {
      if (orSelf) {
        m_axis = 18;
      } else {
        m_axis = 17;
      }
    } else if (orSelf) {
      m_axis = 5;
    } else {
      m_axis = 4;
    }
    int whatToShow = compiler.getWhatToShow(firstStepPos);
    
    if ((0 == (whatToShow & 0x43)) || (whatToShow == -1))
    {


      initNodeTest(whatToShow);
    }
    else {
      initNodeTest(whatToShow, compiler.getStepNS(firstStepPos), compiler.getStepLocalName(firstStepPos));
    }
    
    initPredicateInfo(compiler, firstStepPos);
  }
  




  public DescendantIterator()
  {
    super(null);
    m_axis = 18;
    int whatToShow = -1;
    initNodeTest(whatToShow);
  }
  









  public DTMIterator cloneWithReset()
    throws CloneNotSupportedException
  {
    DescendantIterator clone = (DescendantIterator)super.cloneWithReset();
    m_traverser = m_traverser;
    
    clone.resetProximityPositions();
    
    return clone;
  }
  












  public int nextNode()
  {
    if (m_foundLast) {
      return -1;
    }
    if (-1 == m_lastFetched)
    {
      resetProximityPositions();
    }
    
    VariableStack vars;
    
    int savedStart;
    
    if (-1 != m_stackFrame)
    {
      VariableStack vars = m_execContext.getVarStack();
      

      int savedStart = vars.getStackFrame();
      
      vars.setStackFrame(m_stackFrame);

    }
    else
    {
      vars = null;
      savedStart = 0;
    }
    try
    {
      int next;
      do {
        int next;
        if (0 == m_extendedTypeID)
        {
          next = this.m_lastFetched = -1 == m_lastFetched ? m_traverser.first(m_context) : m_traverser.next(m_context, m_lastFetched);

        }
        else
        {

          next = this.m_lastFetched = -1 == m_lastFetched ? m_traverser.first(m_context, m_extendedTypeID) : m_traverser.next(m_context, m_lastFetched, m_extendedTypeID);

        }
        

      }
      while ((-1 != next) && 
      
        (1 != acceptNode(next)) && 
        






        (next != -1));
      int i;
      if (-1 != next)
      {
        m_pos += 1;
        return next;
      }
      

      m_foundLast = true;
      
      return -1;

    }
    finally
    {
      if (-1 != m_stackFrame)
      {

        vars.setStackFrame(savedStart);
      }
    }
  }
  







  public void setRoot(int context, Object environment)
  {
    super.setRoot(context, environment);
    m_traverser = m_cdtm.getAxisTraverser(m_axis);
    
    String localName = getLocalName();
    String namespace = getNamespace();
    int what = m_whatToShow;
    

    if ((-1 == what) || ("*".equals(localName)) || ("*".equals(namespace)))
    {


      m_extendedTypeID = 0;
    }
    else
    {
      int type = getNodeTypeTest(what);
      m_extendedTypeID = m_cdtm.getExpandedTypeID(namespace, localName, type);
    }
  }
  









  public int asNode(XPathContext xctxt)
    throws TransformerException
  {
    if (getPredicateCount() > 0) {
      return super.asNode(xctxt);
    }
    int current = xctxt.getCurrentNode();
    
    DTM dtm = xctxt.getDTM(current);
    DTMAxisTraverser traverser = dtm.getAxisTraverser(m_axis);
    
    String localName = getLocalName();
    String namespace = getNamespace();
    int what = m_whatToShow;
    




    if ((-1 == what) || (localName == "*") || (namespace == "*"))
    {


      return traverser.first(current);
    }
    

    int type = getNodeTypeTest(what);
    int extendedType = dtm.getExpandedTypeID(namespace, localName, type);
    return traverser.first(current, extendedType);
  }
  








  public void detach()
  {
    if (m_allowDetach) {
      m_traverser = null;
      m_extendedTypeID = 0;
      

      super.detach();
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
