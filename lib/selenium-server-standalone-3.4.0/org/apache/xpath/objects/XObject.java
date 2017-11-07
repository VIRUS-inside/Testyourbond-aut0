package org.apache.xpath.objects;

import java.io.Serializable;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.utils.FastStringBuffer;
import org.apache.xml.utils.XMLString;
import org.apache.xml.utils.XMLStringFactory;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.NodeSetDTM;
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathException;
import org.apache.xpath.XPathVisitor;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;





























public class XObject
  extends Expression
  implements Serializable, Cloneable
{
  static final long serialVersionUID = -821887098985662951L;
  protected Object m_obj;
  public static final int CLASS_NULL = -1;
  public static final int CLASS_UNKNOWN = 0;
  public static final int CLASS_BOOLEAN = 1;
  public static final int CLASS_NUMBER = 2;
  public static final int CLASS_STRING = 3;
  public static final int CLASS_NODESET = 4;
  public static final int CLASS_RTREEFRAG = 5;
  public static final int CLASS_UNRESOLVEDVARIABLE = 600;
  
  public XObject() {}
  
  public XObject(Object obj)
  {
    setObject(obj);
  }
  
  protected void setObject(Object obj) {
    m_obj = obj;
  }
  









  public XObject execute(XPathContext xctxt)
    throws TransformerException
  {
    return this;
  }
  








  public void allowDetachToRelease(boolean allowRelease) {}
  







  public void detach() {}
  







  public void destruct()
  {
    if (null != m_obj)
    {
      allowDetachToRelease(true);
      detach();
      
      setObject(null);
    }
  }
  








  public void reset() {}
  








  public void dispatchCharactersEvents(ContentHandler ch)
    throws SAXException
  {
    xstr().dispatchCharactersEvents(ch);
  }
  









  public static XObject create(Object val)
  {
    return XObjectFactory.create(val);
  }
  










  public static XObject create(Object val, XPathContext xctxt)
  {
    return XObjectFactory.create(val, xctxt);
  }
  





























  public int getType()
  {
    return 0;
  }
  






  public String getTypeString()
  {
    return "#UNKNOWN (" + object().getClass().getName() + ")";
  }
  







  public double num()
    throws TransformerException
  {
    error("ER_CANT_CONVERT_TO_NUMBER", new Object[] { getTypeString() });
    

    return 0.0D;
  }
  






  public double numWithSideEffects()
    throws TransformerException
  {
    return num();
  }
  







  public boolean bool()
    throws TransformerException
  {
    error("ER_CANT_CONVERT_TO_NUMBER", new Object[] { getTypeString() });
    

    return false;
  }
  





  public boolean boolWithSideEffects()
    throws TransformerException
  {
    return bool();
  }
  






  public XMLString xstr()
  {
    return XMLStringFactoryImpl.getFactory().newstr(str());
  }
  





  public String str()
  {
    return m_obj != null ? m_obj.toString() : "";
  }
  






  public String toString()
  {
    return str();
  }
  








  public int rtf(XPathContext support)
  {
    int result = rtf();
    
    if (-1 == result)
    {
      DTM frag = support.createDocumentFragment();
      

      frag.appendTextChild(str());
      
      result = frag.getDocument();
    }
    
    return result;
  }
  







  public DocumentFragment rtree(XPathContext support)
  {
    DocumentFragment docFrag = null;
    int result = rtf();
    
    if (-1 == result)
    {
      DTM frag = support.createDocumentFragment();
      

      frag.appendTextChild(str());
      
      docFrag = (DocumentFragment)frag.getNode(frag.getDocument());
    }
    else
    {
      DTM frag = support.getDTM(result);
      docFrag = (DocumentFragment)frag.getNode(frag.getDocument());
    }
    
    return docFrag;
  }
  






  public DocumentFragment rtree()
  {
    return null;
  }
  





  public int rtf()
  {
    return -1;
  }
  






  public Object object()
  {
    return m_obj;
  }
  







  public DTMIterator iter()
    throws TransformerException
  {
    error("ER_CANT_CONVERT_TO_NODELIST", new Object[] { getTypeString() });
    

    return null;
  }
  





  public XObject getFresh()
  {
    return this;
  }
  








  public NodeIterator nodeset()
    throws TransformerException
  {
    error("ER_CANT_CONVERT_TO_NODELIST", new Object[] { getTypeString() });
    

    return null;
  }
  







  public NodeList nodelist()
    throws TransformerException
  {
    error("ER_CANT_CONVERT_TO_NODELIST", new Object[] { getTypeString() });
    

    return null;
  }
  









  public NodeSetDTM mutableNodeset()
    throws TransformerException
  {
    error("ER_CANT_CONVERT_TO_MUTABLENODELIST", new Object[] { getTypeString() });
    

    return (NodeSetDTM)m_obj;
  }
  






  public Object castToType(int t, XPathContext support)
    throws TransformerException
  {
    Object result;
    





    switch (t)
    {
    case 3: 
      result = str();
      break;
    case 2: 
      result = new Double(num());
      break;
    case 4: 
      result = iter();
      break;
    case 1: 
      result = bool() ? Boolean.TRUE : Boolean.FALSE;
      break;
    case 0: 
      result = m_obj;
      break;
    




    default: 
      error("ER_CANT_CONVERT_TO_TYPE", new Object[] { getTypeString(), Integer.toString(t) });
      


      result = null;
    }
    
    return result;
  }
  















  public boolean lessThan(XObject obj2)
    throws TransformerException
  {
    if (obj2.getType() == 4) {
      return obj2.greaterThan(this);
    }
    return num() < obj2.num();
  }
  















  public boolean lessThanOrEqual(XObject obj2)
    throws TransformerException
  {
    if (obj2.getType() == 4) {
      return obj2.greaterThanOrEqual(this);
    }
    return num() <= obj2.num();
  }
  















  public boolean greaterThan(XObject obj2)
    throws TransformerException
  {
    if (obj2.getType() == 4) {
      return obj2.lessThan(this);
    }
    return num() > obj2.num();
  }
  















  public boolean greaterThanOrEqual(XObject obj2)
    throws TransformerException
  {
    if (obj2.getType() == 4) {
      return obj2.lessThanOrEqual(this);
    }
    return num() >= obj2.num();
  }
  













  public boolean equals(XObject obj2)
  {
    if (obj2.getType() == 4) {
      return obj2.equals(this);
    }
    if (null != m_obj)
    {
      return m_obj.equals(m_obj);
    }
    

    return m_obj == null;
  }
  














  public boolean notEquals(XObject obj2)
    throws TransformerException
  {
    if (obj2.getType() == 4) {
      return obj2.notEquals(this);
    }
    return !equals(obj2);
  }
  








  protected void error(String msg)
    throws TransformerException
  {
    error(msg, null);
  }
  










  protected void error(String msg, Object[] args)
    throws TransformerException
  {
    String fmsg = XSLMessages.createXPATHMessage(msg, args);
    






    throw new XPathException(fmsg, this);
  }
  








  public void fixupVariables(Vector vars, int globalsSize) {}
  








  public void appendToFsb(FastStringBuffer fsb)
  {
    fsb.append(str());
  }
  



  public void callVisitors(ExpressionOwner owner, XPathVisitor visitor)
  {
    assertion(false, "callVisitors should not be called for this object!!!");
  }
  


  public boolean deepEquals(Expression expr)
  {
    if (!isSameClass(expr)) {
      return false;
    }
    


    if (!equals((XObject)expr)) {
      return false;
    }
    return true;
  }
}
