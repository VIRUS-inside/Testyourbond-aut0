package org.apache.xpath.objects;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.ref.DTMNodeIterator;
import org.apache.xml.dtm.ref.DTMNodeList;
import org.apache.xml.utils.FastStringBuffer;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.NodeSetDTM;
import org.apache.xpath.axes.NodeSequence;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;




























public class XNodeSet
  extends NodeSequence
{
  static final long serialVersionUID = 1916026368035639667L;
  
  protected XNodeSet() {}
  
  public XNodeSet(DTMIterator val)
  {
    if ((val instanceof XNodeSet))
    {
      XNodeSet nodeSet = (XNodeSet)val;
      setIter(m_iter);
      m_dtmMgr = m_dtmMgr;
      m_last = m_last;
      

      if (!nodeSet.hasCache()) {
        nodeSet.setShouldCacheNodes(true);
      }
      
      setObject(nodeSet.getIteratorCache());
    }
    else {
      setIter(val);
    }
  }
  





  public XNodeSet(XNodeSet val)
  {
    setIter(m_iter);
    m_dtmMgr = m_dtmMgr;
    m_last = m_last;
    if (!val.hasCache())
      val.setShouldCacheNodes(true);
    setObject(m_obj);
  }
  





  public XNodeSet(DTMManager dtmMgr)
  {
    this(-1, dtmMgr);
  }
  






  public XNodeSet(int n, DTMManager dtmMgr)
  {
    super(new NodeSetDTM(dtmMgr));
    m_dtmMgr = dtmMgr;
    
    if (-1 != n)
    {
      ((NodeSetDTM)m_obj).addNode(n);
      m_last = 1;
    }
    else {
      m_last = 0;
    }
  }
  




  public int getType()
  {
    return 4;
  }
  






  public String getTypeString()
  {
    return "#NODESET";
  }
  







  public double getNumberFromNode(int n)
  {
    XMLString xstr = m_dtmMgr.getDTM(n).getStringValue(n);
    return xstr.toDouble();
  }
  







  public double num()
  {
    int node = item(0);
    return node != -1 ? getNumberFromNode(node) : NaN.0D;
  }
  







  public double numWithSideEffects()
  {
    int node = nextNode();
    
    return node != -1 ? getNumberFromNode(node) : NaN.0D;
  }
  






  public boolean bool()
  {
    return item(0) != -1;
  }
  






  public boolean boolWithSideEffects()
  {
    return nextNode() != -1;
  }
  










  public XMLString getStringFromNode(int n)
  {
    if (-1 != n)
    {
      return m_dtmMgr.getDTM(n).getStringValue(n);
    }
    

    return XString.EMPTYSTRING;
  }
  












  public void dispatchCharactersEvents(ContentHandler ch)
    throws SAXException
  {
    int node = item(0);
    
    if (node != -1)
    {
      m_dtmMgr.getDTM(node).dispatchCharactersEvents(node, ch, false);
    }
  }
  






  public XMLString xstr()
  {
    int node = item(0);
    return node != -1 ? getStringFromNode(node) : XString.EMPTYSTRING;
  }
  





  public void appendToFsb(FastStringBuffer fsb)
  {
    XString xstring = (XString)xstr();
    xstring.appendToFsb(fsb);
  }
  







  public String str()
  {
    int node = item(0);
    return node != -1 ? getStringFromNode(node).toString() : "";
  }
  






  public Object object()
  {
    if (null == m_obj) {
      return this;
    }
    return m_obj;
  }
  


































  public NodeIterator nodeset()
    throws TransformerException
  {
    return new DTMNodeIterator(iter());
  }
  






  public NodeList nodelist()
    throws TransformerException
  {
    DTMNodeList nodelist = new DTMNodeList(this);
    



    XNodeSet clone = (XNodeSet)nodelist.getDTMIterator();
    SetVector(clone.getVector());
    return nodelist;
  }
  















  public DTMIterator iterRaw()
  {
    return this;
  }
  




  public void release(DTMIterator iter) {}
  



  public DTMIterator iter()
  {
    try
    {
      if (hasCache()) {
        return cloneWithReset();
      }
      return this;
    }
    catch (CloneNotSupportedException cnse)
    {
      throw new RuntimeException(cnse.getMessage());
    }
  }
  





  public XObject getFresh()
  {
    try
    {
      if (hasCache()) {
        return (XObject)cloneWithReset();
      }
      return this;
    }
    catch (CloneNotSupportedException cnse)
    {
      throw new RuntimeException(cnse.getMessage());
    }
  }
  


  public NodeSetDTM mutableNodeset()
  {
    NodeSetDTM mnl;
    

    NodeSetDTM mnl;
    
    if ((m_obj instanceof NodeSetDTM))
    {
      mnl = (NodeSetDTM)m_obj;
    }
    else
    {
      mnl = new NodeSetDTM(iter());
      setObject(mnl);
      setCurrentPos(0);
    }
    
    return mnl;
  }
  

  static final LessThanComparator S_LT = new LessThanComparator();
  

  static final LessThanOrEqualComparator S_LTE = new LessThanOrEqualComparator();
  

  static final GreaterThanComparator S_GT = new GreaterThanComparator();
  

  static final GreaterThanOrEqualComparator S_GTE = new GreaterThanOrEqualComparator();
  


  static final EqualComparator S_EQ = new EqualComparator();
  

  static final NotEqualComparator S_NEQ = new NotEqualComparator();
  











  public boolean compare(XObject obj2, Comparator comparator)
    throws TransformerException
  {
    boolean result = false;
    int type = obj2.getType();
    
    if (4 == type)
    {













      DTMIterator list1 = iterRaw();
      DTMIterator list2 = ((XNodeSet)obj2).iterRaw();
      
      Vector node2Strings = null;
      int node1;
      while (-1 != (node1 = list1.nextNode()))
      {
        XMLString s1 = getStringFromNode(node1);
        
        if (null == node2Strings)
        {
          int node2;
          
          while (-1 != (node2 = list2.nextNode()))
          {
            XMLString s2 = getStringFromNode(node2);
            
            if (comparator.compareStrings(s1, s2))
            {
              result = true;
              
              break;
            }
            
            if (null == node2Strings) {
              node2Strings = new Vector();
            }
            node2Strings.addElement(s2);
          }
        }
        else
        {
          int n = node2Strings.size();
          
          for (int i = 0; i < n; i++)
          {
            if (comparator.compareStrings(s1, (XMLString)node2Strings.elementAt(i)))
            {
              result = true;
              
              break;
            }
          }
        }
      }
      list1.reset();
      list2.reset();
    }
    else if (1 == type)
    {







      double num1 = bool() ? 1.0D : 0.0D;
      double num2 = obj2.num();
      
      result = comparator.compareNumbers(num1, num2);
    }
    else if (2 == type)
    {








      DTMIterator list1 = iterRaw();
      double num2 = obj2.num();
      
      int node;
      while (-1 != (node = list1.nextNode()))
      {
        double num1 = getNumberFromNode(node);
        
        if (comparator.compareNumbers(num1, num2))
        {
          result = true;
          
          break;
        }
      }
      list1.reset();
    }
    else if (5 == type)
    {
      XMLString s2 = obj2.xstr();
      DTMIterator list1 = iterRaw();
      
      int node;
      while (-1 != (node = list1.nextNode()))
      {
        XMLString s1 = getStringFromNode(node);
        
        if (comparator.compareStrings(s1, s2))
        {
          result = true;
          
          break;
        }
      }
      list1.reset();
    }
    else if (3 == type)
    {







      XMLString s2 = obj2.xstr();
      DTMIterator list1 = iterRaw();
      
      int node;
      while (-1 != (node = list1.nextNode()))
      {
        XMLString s1 = getStringFromNode(node);
        if (comparator.compareStrings(s1, s2))
        {
          result = true;
          
          break;
        }
      }
      list1.reset();
    }
    else
    {
      result = comparator.compareNumbers(num(), obj2.num());
    }
    
    return result;
  }
  








  public boolean lessThan(XObject obj2)
    throws TransformerException
  {
    return compare(obj2, S_LT);
  }
  








  public boolean lessThanOrEqual(XObject obj2)
    throws TransformerException
  {
    return compare(obj2, S_LTE);
  }
  








  public boolean greaterThan(XObject obj2)
    throws TransformerException
  {
    return compare(obj2, S_GT);
  }
  









  public boolean greaterThanOrEqual(XObject obj2)
    throws TransformerException
  {
    return compare(obj2, S_GTE);
  }
  









  public boolean equals(XObject obj2)
  {
    try
    {
      return compare(obj2, S_EQ);
    }
    catch (TransformerException te)
    {
      throw new WrappedRuntimeException(te);
    }
  }
  








  public boolean notEquals(XObject obj2)
    throws TransformerException
  {
    return compare(obj2, S_NEQ);
  }
}
