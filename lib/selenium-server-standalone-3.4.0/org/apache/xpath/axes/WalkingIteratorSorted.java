package org.apache.xpath.axes;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.compiler.Compiler;





























public class WalkingIteratorSorted
  extends WalkingIterator
{
  static final long serialVersionUID = -4512512007542368213L;
  protected boolean m_inNaturalOrderStatic = false;
  






  public WalkingIteratorSorted(PrefixResolver nscontext)
  {
    super(nscontext);
  }
  
















  WalkingIteratorSorted(Compiler compiler, int opPos, int analysis, boolean shouldLoadWalkers)
    throws TransformerException
  {
    super(compiler, opPos, analysis, shouldLoadWalkers);
  }
  






  public boolean isDocOrdered()
  {
    return m_inNaturalOrderStatic;
  }
  








  boolean canBeWalkedInNaturalDocOrderStatic()
  {
    if (null != m_firstWalker)
    {
      AxesWalker walker = m_firstWalker;
      int prevAxis = -1;
      boolean prevIsSimpleDownAxis = true;
      
      for (int i = 0; null != walker; i++)
      {
        int axis = walker.getAxis();
        
        if (walker.isDocOrdered())
        {
          boolean isSimpleDownAxis = (axis == 3) || (axis == 13) || (axis == 19);
          



          if ((isSimpleDownAxis) || (axis == -1)) {
            walker = walker.getNextWalker();
          }
          else {
            boolean isLastWalker = null == walker.getNextWalker();
            if (isLastWalker)
            {
              if (((walker.isDocOrdered()) && ((axis == 4) || (axis == 5) || (axis == 17) || (axis == 18))) || (axis == 2))
              {

                return true; }
            }
            return false;
          }
        }
        else {
          return false;
        } }
      return true;
    }
    return false;
  }
  




























































  public void fixupVariables(Vector vars, int globalsSize)
  {
    super.fixupVariables(vars, globalsSize);
    
    int analysis = getAnalysisBits();
    if (WalkerFactory.isNaturalDocOrder(analysis))
    {
      m_inNaturalOrderStatic = true;
    }
    else
    {
      m_inNaturalOrderStatic = false;
    }
  }
}
