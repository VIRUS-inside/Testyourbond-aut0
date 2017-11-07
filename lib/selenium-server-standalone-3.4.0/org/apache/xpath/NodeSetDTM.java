package org.apache.xpath;

import org.apache.xalan.res.XSLMessages;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMFilter;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.utils.NodeVector;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeIterator;



















































public class NodeSetDTM
  extends NodeVector
  implements DTMIterator, Cloneable
{
  static final long serialVersionUID = 7686480133331317070L;
  DTMManager m_manager;
  
  public NodeSetDTM(DTMManager dtmManager)
  {
    m_manager = dtmManager;
  }
  






  public NodeSetDTM(int blocksize, int dummy, DTMManager dtmManager)
  {
    super(blocksize);
    m_manager = dtmManager;
  }
  























  public NodeSetDTM(NodeSetDTM nodelist)
  {
    m_manager = nodelist.getDTMManager();
    m_root = nodelist.getRoot();
    
    addNodes(nodelist);
  }
  









  public NodeSetDTM(DTMIterator ni)
  {
    m_manager = ni.getDTMManager();
    m_root = ni.getRoot();
    addNodes(ni);
  }
  










  public NodeSetDTM(NodeIterator iterator, XPathContext xctxt)
  {
    m_manager = xctxt.getDTMManager();
    Node node;
    while (null != (node = iterator.nextNode()))
    {
      int handle = xctxt.getDTMHandleFromNode(node);
      addNodeInDocOrder(handle, xctxt);
    }
  }
  








  public NodeSetDTM(NodeList nodeList, XPathContext xctxt)
  {
    m_manager = xctxt.getDTMManager();
    
    int n = nodeList.getLength();
    for (int i = 0; i < n; i++)
    {
      Node node = nodeList.item(i);
      int handle = xctxt.getDTMHandleFromNode(node);
      
      addNode(handle);
    }
  }
  








  public NodeSetDTM(int node, DTMManager dtmManager)
  {
    m_manager = dtmManager;
    
    addNode(node);
  }
  











  public void setEnvironment(Object environment) {}
  










  public int getRoot()
  {
    if (-1 == m_root)
    {
      if (size() > 0) {
        return item(0);
      }
      return -1;
    }
    
    return m_root;
  }
  











  public void setRoot(int context, Object environment) {}
  











  public Object clone()
    throws CloneNotSupportedException
  {
    NodeSetDTM clone = (NodeSetDTM)super.clone();
    
    return clone;
  }
  










  public DTMIterator cloneWithReset()
    throws CloneNotSupportedException
  {
    NodeSetDTM clone = (NodeSetDTM)clone();
    
    clone.reset();
    
    return clone;
  }
  



  public void reset()
  {
    m_next = 0;
  }
  












  public int getWhatToShow()
  {
    return -17;
  }
  













  public DTMFilter getFilter()
  {
    return null;
  }
  
















  public boolean getExpandEntityReferences()
  {
    return true;
  }
  










  public DTM getDTM(int nodeHandle)
  {
    return m_manager.getDTM(nodeHandle);
  }
  











  public DTMManager getDTMManager()
  {
    return m_manager;
  }
  











  public int nextNode()
  {
    if (m_next < size())
    {
      int next = elementAt(m_next);
      
      m_next += 1;
      
      return next;
    }
    
    return -1;
  }
  












  public int previousNode()
  {
    if (!m_cacheNodes) {
      throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_CANNOT_ITERATE", null));
    }
    
    if (m_next - 1 > 0)
    {
      m_next -= 1;
      
      return elementAt(m_next);
    }
    
    return -1;
  }
  










  public void detach() {}
  










  public void allowDetachToRelease(boolean allowRelease) {}
  









  public boolean isFresh()
  {
    return m_next == 0;
  }
  













  public void runTo(int index)
  {
    if (!m_cacheNodes) {
      throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_CANNOT_INDEX", null));
    }
    
    if ((index >= 0) && (m_next < m_firstFree)) {
      m_next = index;
    } else {
      m_next = (m_firstFree - 1);
    }
  }
  












  public int item(int index)
  {
    runTo(index);
    
    return elementAt(index);
  }
  









  public int getLength()
  {
    runTo(-1);
    
    return size();
  }
  









  public void addNode(int n)
  {
    if (!m_mutable) {
      throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
    }
    addElement(n);
  }
  










  public void insertNode(int n, int pos)
  {
    if (!m_mutable) {
      throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
    }
    insertElementAt(n, pos);
  }
  








  public void removeNode(int n)
  {
    if (!m_mutable) {
      throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
    }
    removeElement(n);
  }
  





































































  public void addNodes(DTMIterator iterator)
  {
    if (!m_mutable) {
      throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
    }
    if (null != iterator)
    {
      int obj;
      
      while (-1 != (obj = iterator.nextNode()))
      {
        addElement(obj);
      }
    }
  }
  









































  public void addNodesInDocOrder(DTMIterator iterator, XPathContext support)
  {
    if (!m_mutable) {
      throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
    }
    
    int node;
    while (-1 != (node = iterator.nextNode()))
    {
      addNodeInDocOrder(node, support);
    }
  }
  












































































  public int addNodeInDocOrder(int node, boolean test, XPathContext support)
  {
    if (!m_mutable) {
      throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
    }
    int insertIndex = -1;
    
    if (test)
    {




      int size = size();
      
      for (int i = size - 1; i >= 0; i--)
      {
        int child = elementAt(i);
        
        if (child == node)
        {
          i = -2;

        }
        else
        {
          DTM dtm = support.getDTM(node);
          if (!dtm.isNodeAfter(node, child)) {
            break;
          }
        }
      }
      
      if (i != -2)
      {
        insertIndex = i + 1;
        
        insertElementAt(node, insertIndex);
      }
    }
    else
    {
      insertIndex = size();
      
      boolean foundit = false;
      
      for (int i = 0; i < insertIndex; i++)
      {
        if (i == node)
        {
          foundit = true;
          
          break;
        }
      }
      
      if (!foundit) {
        addElement(node);
      }
    }
    
    return insertIndex;
  }
  











  public int addNodeInDocOrder(int node, XPathContext support)
  {
    if (!m_mutable) {
      throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
    }
    return addNodeInDocOrder(node, true, support);
  }
  





  public int size()
  {
    return super.size();
  }
  








  public void addElement(int value)
  {
    if (!m_mutable) {
      throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
    }
    super.addElement(value);
  }
  












  public void insertElementAt(int value, int at)
  {
    if (!m_mutable) {
      throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
    }
    super.insertElementAt(value, at);
  }
  








  public void appendNodes(NodeVector nodes)
  {
    if (!m_mutable) {
      throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
    }
    super.appendNodes(nodes);
  }
  









  public void removeAllElements()
  {
    if (!m_mutable) {
      throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
    }
    super.removeAllElements();
  }
  














  public boolean removeElement(int s)
  {
    if (!m_mutable) {
      throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
    }
    return super.removeElement(s);
  }
  











  public void removeElementAt(int i)
  {
    if (!m_mutable) {
      throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
    }
    super.removeElementAt(i);
  }
  













  public void setElementAt(int node, int index)
  {
    if (!m_mutable) {
      throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
    }
    super.setElementAt(node, index);
  }
  









  public void setItem(int node, int index)
  {
    if (!m_mutable) {
      throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
    }
    super.setElementAt(node, index);
  }
  








  public int elementAt(int i)
  {
    runTo(i);
    
    return super.elementAt(i);
  }
  








  public boolean contains(int s)
  {
    runTo(-1);
    
    return super.contains(s);
  }
  












  public int indexOf(int elem, int index)
  {
    runTo(-1);
    
    return super.indexOf(elem, index);
  }
  











  public int indexOf(int elem)
  {
    runTo(-1);
    
    return super.indexOf(elem);
  }
  


  protected transient int m_next = 0;
  








  public int getCurrentPos()
  {
    return m_next;
  }
  







  public void setCurrentPos(int i)
  {
    if (!m_cacheNodes) {
      throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_CANNOT_INDEX", null));
    }
    
    m_next = i;
  }
  








  public int getCurrentNode()
  {
    if (!m_cacheNodes) {
      throw new RuntimeException("This NodeSetDTM can not do indexing or counting functions!");
    }
    
    int saved = m_next;
    


    int current = m_next > 0 ? m_next - 1 : m_next;
    int n = current < m_firstFree ? elementAt(current) : -1;
    m_next = saved;
    return n;
  }
  

  protected transient boolean m_mutable = true;
  


  protected transient boolean m_cacheNodes = true;
  

  protected int m_root = -1;
  






  public boolean getShouldCacheNodes()
  {
    return m_cacheNodes;
  }
  












  public void setShouldCacheNodes(boolean b)
  {
    if (!isFresh()) {
      throw new RuntimeException(XSLMessages.createXPATHMessage("ER_CANNOT_CALL_SETSHOULDCACHENODE", null));
    }
    
    m_cacheNodes = b;
    m_mutable = true;
  }
  






  public boolean isMutable()
  {
    return m_mutable;
  }
  
  private transient int m_last = 0;
  
  public int getLast()
  {
    return m_last;
  }
  
  public void setLast(int last)
  {
    m_last = last;
  }
  






  public boolean isDocOrdered()
  {
    return true;
  }
  






  public int getAxis()
  {
    return -1;
  }
}
