package org.apache.xpath.axes;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.VariableStack;
import org.apache.xpath.XPathContext;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.compiler.OpMap;







































public abstract class BasicTestIterator
  extends LocPathIterator
{
  static final long serialVersionUID = 3505378079378096623L;
  
  protected BasicTestIterator() {}
  
  protected BasicTestIterator(PrefixResolver nscontext)
  {
    super(nscontext);
  }
  












  protected BasicTestIterator(Compiler compiler, int opPos, int analysis)
    throws TransformerException
  {
    super(compiler, opPos, analysis, false);
    
    int firstStepPos = OpMap.getFirstChildPos(opPos);
    int whatToShow = compiler.getWhatToShow(firstStepPos);
    
    if ((0 == (whatToShow & 0x1043)) || (whatToShow == -1))
    {




      initNodeTest(whatToShow);
    }
    else {
      initNodeTest(whatToShow, compiler.getStepNS(firstStepPos), compiler.getStepLocalName(firstStepPos));
    }
    
    initPredicateInfo(compiler, firstStepPos);
  }
  
















  protected BasicTestIterator(Compiler compiler, int opPos, int analysis, boolean shouldLoadWalkers)
    throws TransformerException
  {
    super(compiler, opPos, analysis, shouldLoadWalkers);
  }
  







  protected abstract int getNextNode();
  






  public int nextNode()
  {
    if (m_foundLast)
    {
      m_lastFetched = -1;
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
      do
      {
        next = getNextNode();
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
  








  public DTMIterator cloneWithReset()
    throws CloneNotSupportedException
  {
    ChildTestIterator clone = (ChildTestIterator)super.cloneWithReset();
    
    clone.resetProximityPositions();
    
    return clone;
  }
}
