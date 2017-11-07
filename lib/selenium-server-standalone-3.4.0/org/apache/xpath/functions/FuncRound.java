package org.apache.xpath.functions;

import javax.xml.transform.TransformerException;
import org.apache.xpath.Expression;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;



























public class FuncRound
  extends FunctionOneArg
{
  static final long serialVersionUID = -7970583902573826611L;
  
  public FuncRound() {}
  
  public XObject execute(XPathContext xctxt)
    throws TransformerException
  {
    XObject obj = m_arg0.execute(xctxt);
    double val = obj.num();
    if ((val >= -0.5D) && (val < 0.0D)) return new XNumber(-0.0D);
    if (val == 0.0D) return new XNumber(val);
    return new XNumber(Math.floor(val + 0.5D));
  }
}
