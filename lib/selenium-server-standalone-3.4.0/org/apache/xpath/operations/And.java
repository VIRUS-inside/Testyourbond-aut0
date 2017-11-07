package org.apache.xpath.operations;

import javax.xml.transform.TransformerException;
import org.apache.xpath.Expression;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XBoolean;
import org.apache.xpath.objects.XObject;






























public class And
  extends Operation
{
  static final long serialVersionUID = 392330077126534022L;
  
  public And() {}
  
  public XObject execute(XPathContext xctxt)
    throws TransformerException
  {
    XObject expr1 = m_left.execute(xctxt);
    
    if (expr1.bool())
    {
      XObject expr2 = m_right.execute(xctxt);
      
      return expr2.bool() ? XBoolean.S_TRUE : XBoolean.S_FALSE;
    }
    
    return XBoolean.S_FALSE;
  }
  









  public boolean bool(XPathContext xctxt)
    throws TransformerException
  {
    return (m_left.bool(xctxt)) && (m_right.bool(xctxt));
  }
}
