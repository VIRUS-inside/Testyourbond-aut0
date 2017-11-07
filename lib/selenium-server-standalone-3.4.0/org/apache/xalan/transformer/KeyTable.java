package org.apache.xalan.transformer;

import java.util.Hashtable;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.KeyDeclaration;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.QName;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.NodeSetDTM;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XObject;








































public class KeyTable
{
  private int m_docKey;
  private Vector m_keyDeclarations;
  private Hashtable m_refsTable = null;
  

  private XNodeSet m_keyNodes;
  


  public int getDocKey()
  {
    return m_docKey;
  }
  






  KeyIterator getKeyIterator()
  {
    return (KeyIterator)m_keyNodes.getContainedIter();
  }
  










  public KeyTable(int doc, PrefixResolver nscontext, QName name, Vector keyDeclarations, XPathContext xctxt)
    throws TransformerException
  {
    m_docKey = doc;
    m_keyDeclarations = keyDeclarations;
    KeyIterator ki = new KeyIterator(name, keyDeclarations);
    
    m_keyNodes = new XNodeSet(ki);
    m_keyNodes.allowDetachToRelease(false);
    m_keyNodes.setRoot(doc, xctxt);
  }
  








  public XNodeSet getNodeSetDTMByKey(QName name, XMLString ref)
  {
    XNodeSet refNodes = (XNodeSet)getRefsTable().get(ref);
    
    try
    {
      if (refNodes != null)
      {
        refNodes = (XNodeSet)refNodes.cloneWithReset();
      }
    }
    catch (CloneNotSupportedException e)
    {
      refNodes = null;
    }
    
    if (refNodes == null)
    {
      KeyIterator ki = (KeyIterator)m_keyNodes.getContainedIter();
      XPathContext xctxt = ki.getXPathContext();
      refNodes = new XNodeSet(xctxt.getDTMManager())
      {
        public void setRoot(int nodeHandle, Object environment) {}

      };
      refNodes.reset();
    }
    
    return refNodes;
  }
  





  public QName getKeyTableName()
  {
    return getKeyIterator().getName();
  }
  


  private Vector getKeyDeclarations()
  {
    int nDeclarations = m_keyDeclarations.size();
    Vector keyDecls = new Vector(nDeclarations);
    

    for (int i = 0; i < nDeclarations; i++)
    {
      KeyDeclaration kd = (KeyDeclaration)m_keyDeclarations.elementAt(i);
      


      if (kd.getName().equals(getKeyTableName())) {
        keyDecls.add(kd);
      }
    }
    
    return keyDecls;
  }
  




  private Hashtable getRefsTable()
  {
    if (m_refsTable == null)
    {
      m_refsTable = new Hashtable(89);
      
      KeyIterator ki = (KeyIterator)m_keyNodes.getContainedIter();
      XPathContext xctxt = ki.getXPathContext();
      
      Vector keyDecls = getKeyDeclarations();
      int nKeyDecls = keyDecls.size();
      

      m_keyNodes.reset();
      int currentNode; while (-1 != (currentNode = m_keyNodes.nextNode()))
      {
        try
        {
          for (int keyDeclIdx = 0; keyDeclIdx < nKeyDecls; keyDeclIdx++) {
            KeyDeclaration keyDeclaration = (KeyDeclaration)keyDecls.elementAt(keyDeclIdx);
            
            XObject xuse = keyDeclaration.getUse().execute(xctxt, currentNode, ki.getPrefixResolver());
            



            if (xuse.getType() != 4) {
              XMLString exprResult = xuse.xstr();
              addValueInRefsTable(xctxt, exprResult, currentNode);
            } else {
              DTMIterator i = ((XNodeSet)xuse).iterRaw();
              
              int currentNodeInUseClause;
              while (-1 != (currentNodeInUseClause = i.nextNode())) {
                DTM dtm = xctxt.getDTM(currentNodeInUseClause);
                XMLString exprResult = dtm.getStringValue(currentNodeInUseClause);
                
                addValueInRefsTable(xctxt, exprResult, currentNode);
              }
            }
          }
        } catch (TransformerException te) {
          throw new WrappedRuntimeException(te);
        }
      }
    }
    return m_refsTable;
  }
  







  private void addValueInRefsTable(XPathContext xctxt, XMLString ref, int node)
  {
    XNodeSet nodes = (XNodeSet)m_refsTable.get(ref);
    if (nodes == null)
    {
      nodes = new XNodeSet(node, xctxt.getDTMManager());
      nodes.nextNode();
      m_refsTable.put(ref, nodes);






    }
    else if (nodes.getCurrentNode() != node) {
      nodes.mutableNodeset().addNode(node);
      nodes.nextNode();
    }
  }
}
