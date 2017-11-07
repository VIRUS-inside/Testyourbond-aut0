package org.apache.xml.dtm.ref;

import org.apache.xml.dtm.DTM;
import org.w3c.dom.Node;































































public class DTMChildIterNodeList
  extends DTMNodeListBase
{
  private int m_firstChild;
  private DTM m_parentDTM;
  
  private DTMChildIterNodeList() {}
  
  public DTMChildIterNodeList(DTM parentDTM, int parentHandle)
  {
    m_parentDTM = parentDTM;
    m_firstChild = parentDTM.getFirstChild(parentHandle);
  }
  












  public Node item(int index)
  {
    int handle = m_firstChild;
    for (;;) { index--; if ((index < 0) || (handle == -1)) break;
      handle = m_parentDTM.getNextSibling(handle);
    }
    if (handle == -1) {
      return null;
    }
    return m_parentDTM.getNode(handle);
  }
  



  public int getLength()
  {
    int count = 0;
    for (int handle = m_firstChild; 
        handle != -1; 
        handle = m_parentDTM.getNextSibling(handle)) {
      count++;
    }
    return count;
  }
}
