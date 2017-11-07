package org.apache.xml.dtm.ref;

import java.util.BitSet;
import org.apache.xml.res.XMLMessages;










































































































public class CoroutineManager
{
  BitSet m_activeIDs = new BitSet();
  
















  static final int m_unreasonableId = 1024;
  
















  Object m_yield = null;
  


  static final int NOBODY = -1;
  


  static final int ANYBODY = -1;
  

  int m_nextCoroutine = -1;
  










  public CoroutineManager() {}
  










  public synchronized int co_joinCoroutineSet(int coroutineID)
  {
    if (coroutineID >= 0)
    {
      if ((coroutineID >= 1024) || (m_activeIDs.get(coroutineID))) {
        return -1;
      }
      
    }
    else
    {
      coroutineID = 0;
      while (coroutineID < 1024)
      {
        if (!m_activeIDs.get(coroutineID)) break;
        coroutineID++;
      }
      

      if (coroutineID >= 1024) {
        return -1;
      }
    }
    m_activeIDs.set(coroutineID);
    return coroutineID;
  }
  














  public synchronized Object co_entry_pause(int thisCoroutine)
    throws NoSuchMethodException
  {
    if (!m_activeIDs.get(thisCoroutine)) {
      throw new NoSuchMethodException();
    }
    while (m_nextCoroutine != thisCoroutine)
    {
      try
      {
        wait();
      }
      catch (InterruptedException e) {}
    }
    




    return m_yield;
  }
  













  public synchronized Object co_resume(Object arg_object, int thisCoroutine, int toCoroutine)
    throws NoSuchMethodException
  {
    if (!m_activeIDs.get(toCoroutine)) {
      throw new NoSuchMethodException(XMLMessages.createXMLMessage("ER_COROUTINE_NOT_AVAIL", new Object[] { Integer.toString(toCoroutine) }));
    }
    

    m_yield = arg_object;
    m_nextCoroutine = toCoroutine;
    
    notify();
    while ((m_nextCoroutine != thisCoroutine) || (m_nextCoroutine == -1) || (m_nextCoroutine == -1))
    {
      try
      {

        wait();
      }
      catch (InterruptedException e) {}
    }
    




    if (m_nextCoroutine == -1)
    {

      co_exit(thisCoroutine);
      

      throw new NoSuchMethodException(XMLMessages.createXMLMessage("ER_COROUTINE_CO_EXIT", null));
    }
    
    return m_yield;
  }
  













  public synchronized void co_exit(int thisCoroutine)
  {
    m_activeIDs.clear(thisCoroutine);
    m_nextCoroutine = -1;
    notify();
  }
  











  public synchronized void co_exit_to(Object arg_object, int thisCoroutine, int toCoroutine)
    throws NoSuchMethodException
  {
    if (!m_activeIDs.get(toCoroutine)) {
      throw new NoSuchMethodException(XMLMessages.createXMLMessage("ER_COROUTINE_NOT_AVAIL", new Object[] { Integer.toString(toCoroutine) }));
    }
    

    m_yield = arg_object;
    m_nextCoroutine = toCoroutine;
    
    m_activeIDs.clear(thisCoroutine);
    
    notify();
  }
}
