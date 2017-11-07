package org.apache.xalan.templates;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.utils.QName;
import org.apache.xpath.Expression;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XRTreeFrag;
import org.apache.xpath.objects.XString;










































public class ElemWithParam
  extends ElemTemplateElement
{
  static final long serialVersionUID = -1070355175864326257L;
  int m_index;
  private XPath m_selectPattern = null;
  



  public ElemWithParam() {}
  


  public void setSelect(XPath v)
  {
    m_selectPattern = v;
  }
  







  public XPath getSelect()
  {
    return m_selectPattern;
  }
  







  private QName m_qname = null;
  



  int m_qnameID;
  



  public void setName(QName v)
  {
    m_qname = v;
  }
  






  public QName getName()
  {
    return m_qname;
  }
  







  public int getXSLToken()
  {
    return 2;
  }
  






  public String getNodeName()
  {
    return "with-param";
  }
  






  public void compose(StylesheetRoot sroot)
    throws TransformerException
  {
    if ((null == m_selectPattern) && (sroot.getOptimizer()))
    {

      XPath newSelect = ElemVariable.rewriteChildToExpression(this);
      if (null != newSelect)
        m_selectPattern = newSelect;
    }
    m_qnameID = sroot.getComposeState().getQNameID(m_qname);
    super.compose(sroot);
    
    Vector vnames = sroot.getComposeState().getVariableNames();
    if (null != m_selectPattern) {
      m_selectPattern.fixupVariables(vnames, sroot.getComposeState().getGlobalsSize());
    }
  }
  






  public void setParentElem(ElemTemplateElement p)
  {
    super.setParentElem(p);
    m_hasVariableDecl = true;
  }
  












  public XObject getValue(TransformerImpl transformer, int sourceNode)
    throws TransformerException
  {
    XPathContext xctxt = transformer.getXPathContext();
    
    xctxt.pushCurrentNode(sourceNode);
    XObject var;
    try
    {
      if (null != m_selectPattern)
      {
        XObject var = m_selectPattern.execute(xctxt, sourceNode, this);
        
        var.allowDetachToRelease(false);
        
        if (transformer.getDebug())
          transformer.getTraceManager().fireSelectedEvent(sourceNode, this, "select", m_selectPattern, var);
      } else {
        XObject var;
        if (null == getFirstChildElem())
        {
          var = XString.EMPTYSTRING;

        }
        else
        {

          int df = transformer.transformToRTF(this);
          
          var = new XRTreeFrag(df, xctxt, this);
        }
      }
    }
    finally {
      xctxt.popCurrentNode();
    }
    
    return var;
  }
  




  protected void callChildVisitors(XSLTVisitor visitor, boolean callAttrs)
  {
    if ((callAttrs) && (null != m_selectPattern))
      m_selectPattern.getExpression().callVisitors(m_selectPattern, visitor);
    super.callChildVisitors(visitor, callAttrs);
  }
  










  public ElemTemplateElement appendChild(ElemTemplateElement elem)
  {
    if (m_selectPattern != null)
    {
      error("ER_CANT_HAVE_CONTENT_AND_SELECT", new Object[] { "xsl:" + getNodeName() });
      
      return null;
    }
    return super.appendChild(elem);
  }
}
