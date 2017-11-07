package org.apache.xalan.transformer;

import java.text.CollationKey;
import java.text.Collator;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XObject;









































public class NodeSorter
{
  XPathContext m_execContext;
  Vector m_keys;
  
  public NodeSorter(XPathContext p)
  {
    m_execContext = p;
  }
  










  public void sort(DTMIterator v, Vector keys, XPathContext support)
    throws TransformerException
  {
    m_keys = keys;
    

    int n = v.getLength();
    






    Vector nodes = new Vector();
    
    for (int i = 0; i < n; i++)
    {
      NodeCompareElem elem = new NodeCompareElem(v.item(i));
      
      nodes.addElement(elem);
    }
    
    Vector scratchVector = new Vector();
    
    mergesort(nodes, scratchVector, 0, n - 1, support);
    

    for (int i = 0; i < n; i++)
    {
      v.setItem(elementAtm_node, i);
    }
    v.setCurrentPos(0);
  }
  



















  int compare(NodeCompareElem n1, NodeCompareElem n2, int kIndex, XPathContext support)
    throws TransformerException
  {
    int result = 0;
    NodeSortKey k = (NodeSortKey)m_keys.elementAt(kIndex);
    
    if (m_treatAsNumbers) {
      double n2Num;
      double n1Num;
      double n2Num;
      if (kIndex == 0)
      {
        double n1Num = ((Double)m_key1Value).doubleValue();
        n2Num = ((Double)m_key1Value).doubleValue();
      } else { double n2Num;
        if (kIndex == 1)
        {
          double n1Num = ((Double)m_key2Value).doubleValue();
          n2Num = ((Double)m_key2Value).doubleValue();





        }
        else
        {




          XObject r1 = m_selectPat.execute(m_execContext, m_node, m_namespaceContext);
          
          XObject r2 = m_selectPat.execute(m_execContext, m_node, m_namespaceContext);
          
          n1Num = r1.num();
          



          n2Num = r2.num();
        }
      }
      
      if ((n1Num == n2Num) && (kIndex + 1 < m_keys.size()))
      {
        result = compare(n1, n2, kIndex + 1, support);
      }
      else {
        double diff;
        double diff;
        if (Double.isNaN(n1Num)) {
          double diff;
          if (Double.isNaN(n2Num)) {
            diff = 0.0D;
          } else
            diff = -1.0D;
        } else { double diff;
          if (Double.isNaN(n2Num)) {
            diff = 1.0D;
          } else {
            diff = n1Num - n2Num;
          }
        }
        result = diff > 0.0D ? 1 : m_descending ? -1 : diff < 0.0D ? -1 : m_descending ? 1 : 0;
      }
    }
    else
    {
      CollationKey n2String;
      
      CollationKey n1String;
      CollationKey n2String;
      if (kIndex == 0)
      {
        CollationKey n1String = (CollationKey)m_key1Value;
        n2String = (CollationKey)m_key1Value;
      } else { CollationKey n2String;
        if (kIndex == 1)
        {
          CollationKey n1String = (CollationKey)m_key2Value;
          n2String = (CollationKey)m_key2Value;





        }
        else
        {




          XObject r1 = m_selectPat.execute(m_execContext, m_node, m_namespaceContext);
          
          XObject r2 = m_selectPat.execute(m_execContext, m_node, m_namespaceContext);
          

          n1String = m_col.getCollationKey(r1.str());
          n2String = m_col.getCollationKey(r2.str());
        }
      }
      

      result = n1String.compareTo(n2String);
      

      if (m_caseOrderUpper)
      {
        String tempN1 = n1String.getSourceString().toLowerCase();
        String tempN2 = n2String.getSourceString().toLowerCase();
        
        if (tempN1.equals(tempN2))
        {


          result = result == 0 ? 0 : -result;
        }
      }
      

      if (m_descending)
      {
        result = -result;
      }
    }
    
    if (0 == result)
    {
      if (kIndex + 1 < m_keys.size())
      {
        result = compare(n1, n2, kIndex + 1, support);
      }
    }
    
    if (0 == result)
    {





      DTM dtm = support.getDTM(m_node);
      result = dtm.isNodeAfter(m_node, m_node) ? -1 : 1;
    }
    


    return result;
  }
  
















  void mergesort(Vector a, Vector b, int l, int r, XPathContext support)
    throws TransformerException
  {
    if (r - l > 0)
    {
      int m = (r + l) / 2;
      
      mergesort(a, b, l, m, support);
      mergesort(a, b, m + 1, r, support);
      


      for (int i = m; i >= l; i--)
      {



        if (i >= b.size()) {
          b.insertElementAt(a.elementAt(i), i);
        } else {
          b.setElementAt(a.elementAt(i), i);
        }
      }
      i = l;
      
      for (int j = m + 1; j <= r; j++)
      {


        if (r + m + 1 - j >= b.size()) {
          b.insertElementAt(a.elementAt(j), r + m + 1 - j);
        } else {
          b.setElementAt(a.elementAt(j), r + m + 1 - j);
        }
      }
      j = r;
      


      for (int k = l; k <= r; k++)
      {
        int compVal;
        int compVal;
        if (i == j) {
          compVal = -1;
        } else {
          compVal = compare((NodeCompareElem)b.elementAt(i), (NodeCompareElem)b.elementAt(j), 0, support);
        }
        
        if (compVal < 0)
        {


          a.setElementAt(b.elementAt(i), k);
          
          i++;
        }
        else if (compVal > 0)
        {


          a.setElementAt(b.elementAt(j), k);
          
          j--;
        }
      }
    }
  }
  




















































  class NodeCompareElem
  {
    int m_node;
    


















































    int maxkey = 2;
    




    Object m_key1Value;
    




    Object m_key2Value;
    





    NodeCompareElem(int node)
      throws TransformerException
    {
      m_node = node;
      NodeSortKey k1;
      if (!m_keys.isEmpty())
      {
        k1 = (NodeSortKey)m_keys.elementAt(0);
        XObject r = m_selectPat.execute(m_execContext, node, m_namespaceContext);
        



        if (m_treatAsNumbers)
        {
          double d = r.num();
          

          m_key1Value = new Double(d);
        }
        else
        {
          m_key1Value = m_col.getCollationKey(r.str());
        }
        
        if (r.getType() == 4)
        {

          DTMIterator ni = ((XNodeSet)r).iterRaw();
          int current = ni.getCurrentNode();
          if (-1 == current) {
            current = ni.nextNode();
          }
        }
        




        if (m_keys.size() > 1)
        {
          NodeSortKey k2 = (NodeSortKey)m_keys.elementAt(1);
          
          XObject r2 = m_selectPat.execute(m_execContext, node, m_namespaceContext);
          

          if (m_treatAsNumbers) {
            double d = r2.num();
            m_key2Value = new Double(d);
          } else {
            m_key2Value = m_col.getCollationKey(r2.str());
          }
        }
      }
    }
  }
}
