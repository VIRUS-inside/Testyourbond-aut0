package org.apache.xml.dtm.ref;

import org.apache.xml.dtm.DTM;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;










































public class DTMNamedNodeMap
  implements NamedNodeMap
{
  DTM dtm;
  int element;
  short m_count = -1;
  






  public DTMNamedNodeMap(DTM dtm, int element)
  {
    this.dtm = dtm;
    this.element = element;
  }
  






  public int getLength()
  {
    if (m_count == -1)
    {
      short count = 0;
      
      for (int n = dtm.getFirstAttribute(element); n != -1; 
          n = dtm.getNextAttribute(n))
      {
        count = (short)(count + 1);
      }
      
      m_count = count;
    }
    
    return m_count;
  }
  








  public Node getNamedItem(String name)
  {
    for (int n = dtm.getFirstAttribute(element); n != -1; 
        n = dtm.getNextAttribute(n))
    {
      if (dtm.getNodeName(n).equals(name)) {
        return dtm.getNode(n);
      }
    }
    return null;
  }
  









  public Node item(int i)
  {
    int count = 0;
    
    for (int n = dtm.getFirstAttribute(element); n != -1; 
        n = dtm.getNextAttribute(n))
    {
      if (count == i) {
        return dtm.getNode(n);
      }
      count++;
    }
    
    return null;
  }
  
























  public Node setNamedItem(Node newNode)
  {
    throw new DTMException((short)7);
  }
  















  public Node removeNamedItem(String name)
  {
    throw new DTMException((short)7);
  }
  











  public Node getNamedItemNS(String namespaceURI, String localName)
  {
    Node retNode = null;
    for (int n = dtm.getFirstAttribute(element); n != -1; 
        n = dtm.getNextAttribute(n))
    {
      if (localName.equals(dtm.getLocalName(n)))
      {
        String nsURI = dtm.getNamespaceURI(n);
        if (((namespaceURI == null) && (nsURI == null)) || ((namespaceURI != null) && (namespaceURI.equals(nsURI))))
        {

          retNode = dtm.getNode(n);
          break;
        }
      }
    }
    return retNode;
  }
  






















  public Node setNamedItemNS(Node arg)
    throws DOMException
  {
    throw new DTMException((short)7);
  }
  




















  public Node removeNamedItemNS(String namespaceURI, String localName)
    throws DOMException
  {
    throw new DTMException((short)7);
  }
  




  public static class DTMException
    extends DOMException
  {
    static final long serialVersionUID = -8290238117162437678L;
    




    public DTMException(short code, String message)
    {
      super(message);
    }
    






    public DTMException(short code)
    {
      super("");
    }
  }
}
