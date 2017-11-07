package org.apache.xpath.functions;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xpath.XPathContext;
import org.apache.xpath.axes.SubContextList;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;


























public class FuncLast
  extends Function
{
  static final long serialVersionUID = 9205812403085432943L;
  private boolean m_isTopLevel;
  
  public FuncLast() {}
  
  public void postCompileStep(Compiler compiler)
  {
    m_isTopLevel = (compiler.getLocationPathDepth() == -1);
  }
  












  public int getCountOfContextNodeList(XPathContext xctxt)
    throws TransformerException
  {
    SubContextList iter = m_isTopLevel ? null : xctxt.getSubContextList();
    

    if (null != iter) {
      return iter.getLastPos(xctxt);
    }
    DTMIterator cnl = xctxt.getContextNodeList();
    int count;
    int count; if (null != cnl)
    {
      count = cnl.getLength();
    }
    else
    {
      count = 0; }
    return count;
  }
  







  public XObject execute(XPathContext xctxt)
    throws TransformerException
  {
    XNumber xnum = new XNumber(getCountOfContextNodeList(xctxt));
    
    return xnum;
  }
  
  public void fixupVariables(Vector vars, int globalsSize) {}
}
