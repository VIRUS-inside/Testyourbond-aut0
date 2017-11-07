package org.apache.xml.dtm.ref;

import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMDOMException;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.utils.WrappedRuntimeException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;















































public class DTMNodeIterator
  implements NodeIterator
{
  private DTMIterator dtm_iter;
  private boolean valid = true;
  






  public DTMNodeIterator(DTMIterator dtmIterator)
  {
    try
    {
      dtm_iter = ((DTMIterator)dtmIterator.clone());
    }
    catch (CloneNotSupportedException cnse)
    {
      throw new WrappedRuntimeException(cnse);
    }
  }
  



  public DTMIterator getDTMIterator()
  {
    return dtm_iter;
  }
  











  public void detach()
  {
    valid = false;
  }
  





  public boolean getExpandEntityReferences()
  {
    return false;
  }
  












  public NodeFilter getFilter()
  {
    throw new DTMDOMException((short)9);
  }
  




  public Node getRoot()
  {
    int handle = dtm_iter.getRoot();
    return dtm_iter.getDTM(handle).getNode(handle);
  }
  




  public int getWhatToShow()
  {
    return dtm_iter.getWhatToShow();
  }
  





  public Node nextNode()
    throws DOMException
  {
    if (!valid) {
      throw new DTMDOMException((short)11);
    }
    int handle = dtm_iter.nextNode();
    if (handle == -1)
      return null;
    return dtm_iter.getDTM(handle).getNode(handle);
  }
  







  public Node previousNode()
  {
    if (!valid) {
      throw new DTMDOMException((short)11);
    }
    int handle = dtm_iter.previousNode();
    if (handle == -1)
      return null;
    return dtm_iter.getDTM(handle).getNode(handle);
  }
}
