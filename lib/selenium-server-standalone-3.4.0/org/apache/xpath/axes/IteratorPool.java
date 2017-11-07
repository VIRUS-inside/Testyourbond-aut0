package org.apache.xpath.axes;

import java.io.Serializable;
import java.util.ArrayList;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.utils.WrappedRuntimeException;





































public final class IteratorPool
  implements Serializable
{
  static final long serialVersionUID = -460927331149566998L;
  private final DTMIterator m_orig;
  private final ArrayList m_freeStack;
  
  public IteratorPool(DTMIterator original)
  {
    m_orig = original;
    m_freeStack = new ArrayList();
  }
  






  public synchronized DTMIterator getInstanceOrThrow()
    throws CloneNotSupportedException
  {
    if (m_freeStack.isEmpty())
    {


      return (DTMIterator)m_orig.clone();
    }
    


    DTMIterator result = (DTMIterator)m_freeStack.remove(m_freeStack.size() - 1);
    return result;
  }
  







  public synchronized DTMIterator getInstance()
  {
    if (m_freeStack.isEmpty())
    {

      try
      {

        return (DTMIterator)m_orig.clone();
      }
      catch (Exception ex)
      {
        throw new WrappedRuntimeException(ex);
      }
    }
    


    DTMIterator result = (DTMIterator)m_freeStack.remove(m_freeStack.size() - 1);
    return result;
  }
  







  public synchronized void freeInstance(DTMIterator obj)
  {
    m_freeStack.add(obj);
  }
}
