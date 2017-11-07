package org.apache.xpath.operations;

import javax.xml.transform.TransformerException;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;





























public class String
  extends UnaryOperation
{
  static final long serialVersionUID = 2973374377453022888L;
  
  public String() {}
  
  public XObject operate(XObject right)
    throws TransformerException
  {
    return (XString)right.xstr();
  }
}
