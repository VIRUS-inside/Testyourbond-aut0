package org.apache.xpath.functions;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;





























public class FuncNamespace
  extends FunctionDef1Arg
{
  static final long serialVersionUID = -4695674566722321237L;
  
  public FuncNamespace() {}
  
  public XObject execute(XPathContext xctxt)
    throws TransformerException
  {
    int context = getArg0AsNode(xctxt);
    
    String s;
    if (context != -1)
    {
      DTM dtm = xctxt.getDTM(context);
      int t = dtm.getNodeType(context);
      String s; if (t == 1)
      {
        s = dtm.getNamespaceURI(context);
      }
      else if (t == 2)
      {




        String s = dtm.getNodeName(context);
        if ((s.startsWith("xmlns:")) || (s.equals("xmlns"))) {
          return XString.EMPTYSTRING;
        }
        s = dtm.getNamespaceURI(context);
      }
      else {
        return XString.EMPTYSTRING;
      }
    } else {
      return XString.EMPTYSTRING; }
    String s;
    return null == s ? XString.EMPTYSTRING : new XString(s);
  }
}
