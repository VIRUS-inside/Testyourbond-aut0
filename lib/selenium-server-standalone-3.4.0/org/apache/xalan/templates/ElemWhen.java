package org.apache.xalan.templates;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xpath.Expression;
import org.apache.xpath.XPath;










































public class ElemWhen
  extends ElemTemplateElement
{
  static final long serialVersionUID = 5984065730262071360L;
  private XPath m_test;
  
  public void setTest(XPath v)
  {
    m_test = v;
  }
  







  public XPath getTest()
  {
    return m_test;
  }
  







  public int getXSLToken()
  {
    return 38;
  }
  






  public void compose(StylesheetRoot sroot)
    throws TransformerException
  {
    super.compose(sroot);
    Vector vnames = sroot.getComposeState().getVariableNames();
    if (null != m_test) {
      m_test.fixupVariables(vnames, sroot.getComposeState().getGlobalsSize());
    }
  }
  




  public String getNodeName()
  {
    return "when";
  }
  




  public ElemWhen() {}
  




  protected void callChildVisitors(XSLTVisitor visitor, boolean callAttrs)
  {
    if (callAttrs)
      m_test.getExpression().callVisitors(m_test, visitor);
    super.callChildVisitors(visitor, callAttrs);
  }
}
