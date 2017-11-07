package org.apache.xalan.transformer;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.KeyDeclaration;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.utils.QName;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.axes.ChildTestIterator;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XObject;
























public class KeyRefIterator
  extends ChildTestIterator
{
  static final long serialVersionUID = 3837456451659435102L;
  DTMIterator m_keysNodes;
  protected XMLString m_ref;
  protected QName m_name;
  protected Vector m_keyDeclarations;
  
  public KeyRefIterator(QName name, XMLString ref, Vector keyDecls, DTMIterator ki)
  {
    super(null);
    m_name = name;
    m_ref = ref;
    m_keyDeclarations = keyDecls;
    m_keysNodes = ki;
    setWhatToShow(-1);
  }
  



  protected int getNextNode()
  {
    int next;
    


    while (-1 != (next = m_keysNodes.nextNode()))
    {
      if (1 == filterNode(next))
        break;
    }
    m_lastFetched = next;
    
    return next;
  }
  












  public short filterNode(int testNode)
  {
    boolean foundKey = false;
    Vector keys = m_keyDeclarations;
    
    QName name = m_name;
    KeyIterator ki = (KeyIterator)((XNodeSet)m_keysNodes).getContainedIter();
    XPathContext xctxt = ki.getXPathContext();
    
    if (null == xctxt) {
      assertion(false, "xctxt can not be null here!");
    }
    try
    {
      XMLString lookupKey = m_ref;
      

      int nDeclarations = keys.size();
      

      for (int i = 0; i < nDeclarations; i++)
      {
        KeyDeclaration kd = (KeyDeclaration)keys.elementAt(i);
        


        if (kd.getName().equals(name))
        {

          foundKey = true;
          



          XObject xuse = kd.getUse().execute(xctxt, testNode, ki.getPrefixResolver());
          
          if (xuse.getType() != 4)
          {
            XMLString exprResult = xuse.xstr();
            
            if (lookupKey.equals(exprResult)) {
              return 1;
            }
          }
          else {
            DTMIterator nl = ((XNodeSet)xuse).iterRaw();
            
            int useNode;
            while (-1 != (useNode = nl.nextNode()))
            {
              DTM dtm = getDTM(useNode);
              XMLString exprResult = dtm.getStringValue(useNode);
              if ((null != exprResult) && (lookupKey.equals(exprResult))) {
                return 1;
              }
            }
          }
        }
      }
    }
    catch (TransformerException te) {
      throw new WrappedRuntimeException(te);
    }
    
    if (!foundKey) {
      throw new RuntimeException(XSLMessages.createMessage("ER_NO_XSLKEY_DECLARATION", new Object[] { name.getLocalName() }));
    }
    

    return 2;
  }
}
