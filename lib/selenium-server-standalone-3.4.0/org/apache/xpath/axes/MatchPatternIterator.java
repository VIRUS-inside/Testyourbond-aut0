package org.apache.xpath.axes;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMAxisTraverser;
import org.apache.xpath.VariableStack;
import org.apache.xpath.XPathContext;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.compiler.OpMap;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.patterns.NodeTest;
import org.apache.xpath.patterns.StepPattern;
































public class MatchPatternIterator
  extends LocPathIterator
{
  static final long serialVersionUID = -5201153767396296474L;
  protected StepPattern m_pattern;
  protected int m_superAxis = -1;
  






  protected DTMAxisTraverser m_traverser;
  






  private static final boolean DEBUG = false;
  







  MatchPatternIterator(Compiler compiler, int opPos, int analysis)
    throws TransformerException
  {
    super(compiler, opPos, analysis, false);
    
    int firstStepPos = OpMap.getFirstChildPos(opPos);
    
    m_pattern = WalkerFactory.loadSteps(this, compiler, firstStepPos, 0);
    
    boolean fromRoot = false;
    boolean walkBack = false;
    boolean walkDescendants = false;
    boolean walkAttributes = false;
    
    if (0 != (analysis & 0x28000000))
    {
      fromRoot = true;
    }
    if (0 != (analysis & 0x5D86000))
    {






      walkBack = true;
    }
    if (0 != (analysis & 0x70000))
    {


      walkDescendants = true;
    }
    if (0 != (analysis & 0x208000))
    {
      walkAttributes = true;
    }
    





    if ((fromRoot) || (walkBack))
    {
      if (walkAttributes)
      {
        m_superAxis = 16;
      }
      else
      {
        m_superAxis = 17;
      }
    }
    else if (walkDescendants)
    {
      if (walkAttributes)
      {
        m_superAxis = 14;
      }
      else
      {
        m_superAxis = 5;
      }
      
    }
    else {
      m_superAxis = 16;
    }
  }
  













  public void setRoot(int context, Object environment)
  {
    super.setRoot(context, environment);
    m_traverser = m_cdtm.getAxisTraverser(m_superAxis);
  }
  







  public void detach()
  {
    if (m_allowDetach)
    {
      m_traverser = null;
      

      super.detach();
    }
  }
  




  protected int getNextNode()
  {
    m_lastFetched = (-1 == m_lastFetched ? m_traverser.first(m_context) : m_traverser.next(m_context, m_lastFetched));
    

    return m_lastFetched;
  }
  







  public int nextNode()
  {
    if (m_foundLast) {
      return -1;
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
      
      do
      {
        next = getNextNode();
      }
      while ((-1 != next) && 
      
        (1 != acceptNode(next, m_execContext)) && 
        






        (next != -1));
      int i;
      if (-1 != next)
      {





        incrementCurrentPos();
        
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
  











  public short acceptNode(int n, XPathContext xctxt)
  {
    try
    {
      xctxt.pushCurrentNode(n);
      xctxt.pushIteratorRoot(m_context);
      









      XObject score = m_pattern.execute(xctxt);
      








      return score == NodeTest.SCORE_NONE ? 3 : 1;


    }
    catch (TransformerException se)
    {

      throw new RuntimeException(se.getMessage());
    }
    finally
    {
      xctxt.popCurrentNode();
      xctxt.popIteratorRoot();
    }
  }
}
