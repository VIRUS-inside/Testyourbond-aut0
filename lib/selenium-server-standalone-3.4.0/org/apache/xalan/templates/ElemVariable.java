package org.apache.xalan.templates;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.utils.QName;
import org.apache.xpath.Expression;
import org.apache.xpath.VariableStack;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XRTreeFrag;
import org.apache.xpath.objects.XRTreeFragSelectWrapper;
import org.apache.xpath.objects.XString;














































public class ElemVariable
  extends ElemTemplateElement
{
  static final long serialVersionUID = 9111131075322790061L;
  protected int m_index;
  int m_frameSize = -1;
  
  private XPath m_selectPattern;
  
  protected QName m_qname;
  
  public ElemVariable() {}
  
  public void setIndex(int index)
  {
    m_index = index;
  }
  





  public int getIndex()
  {
    return m_index;
  }
  
















  public void setSelect(XPath v)
  {
    m_selectPattern = v;
  }
  










  public XPath getSelect()
  {
    return m_selectPattern;
  }
  
















  public void setName(QName v)
  {
    m_qname = v;
  }
  










  public QName getName()
  {
    return m_qname;
  }
  




  private boolean m_isTopLevel = false;
  







  public void setIsTopLevel(boolean v)
  {
    m_isTopLevel = v;
  }
  







  public boolean getIsTopLevel()
  {
    return m_isTopLevel;
  }
  







  public int getXSLToken()
  {
    return 73;
  }
  





  public String getNodeName()
  {
    return "variable";
  }
  







  public ElemVariable(ElemVariable param)
    throws TransformerException
  {
    m_selectPattern = m_selectPattern;
    m_qname = m_qname;
    m_isTopLevel = m_isTopLevel;
  }
  











  public void execute(TransformerImpl transformer)
    throws TransformerException
  {
    if (transformer.getDebug()) {
      transformer.getTraceManager().fireTraceEvent(this);
    }
    int sourceNode = transformer.getXPathContext().getCurrentNode();
    
    XObject var = getValue(transformer, sourceNode);
    

    transformer.getXPathContext().getVarStack().setLocalVariable(m_index, var);
    
    if (transformer.getDebug()) {
      transformer.getTraceManager().fireTraceEndEvent(this);
    }
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
          int df;
          





          try
          {
            int df;
            




            if ((m_parentNode instanceof Stylesheet)) {
              df = transformer.transformToGlobalRTF(this);
            } else {
              df = transformer.transformToRTF(this);
            }
          }
          finally {}
          

          var = new XRTreeFrag(df, xctxt, this);
        }
      }
    }
    finally {
      xctxt.popCurrentNode();
    }
    
    return var;
  }
  







  public void compose(StylesheetRoot sroot)
    throws TransformerException
  {
    if ((null == m_selectPattern) && (sroot.getOptimizer()))
    {

      XPath newSelect = rewriteChildToExpression(this);
      if (null != newSelect) {
        m_selectPattern = newSelect;
      }
    }
    StylesheetRoot.ComposeState cstate = sroot.getComposeState();
    


    Vector vnames = cstate.getVariableNames();
    if (null != m_selectPattern) {
      m_selectPattern.fixupVariables(vnames, cstate.getGlobalsSize());
    }
    

    if ((!(m_parentNode instanceof Stylesheet)) && (m_qname != null))
    {
      m_index = (cstate.addVariableName(m_qname) - cstate.getGlobalsSize());
    }
    else if ((m_parentNode instanceof Stylesheet))
    {



      cstate.resetStackFrameSize();
    }
    


    super.compose(sroot);
  }
  




  public void endCompose(StylesheetRoot sroot)
    throws TransformerException
  {
    super.endCompose(sroot);
    if ((m_parentNode instanceof Stylesheet))
    {
      StylesheetRoot.ComposeState cstate = sroot.getComposeState();
      m_frameSize = cstate.getFrameSize();
      cstate.resetStackFrameSize();
    }
  }
  























  static XPath rewriteChildToExpression(ElemTemplateElement varElem)
    throws TransformerException
  {
    ElemTemplateElement t = varElem.getFirstChildElem();
    


    if ((null != t) && (null == t.getNextSiblingElem()))
    {
      int etype = t.getXSLToken();
      
      if (30 == etype)
      {
        ElemValueOf valueof = (ElemValueOf)t;
        

        if ((!valueof.getDisableOutputEscaping()) && (valueof.getDOMBackPointer() == null))
        {

          m_firstChild = null;
          
          return new XPath(new XRTreeFragSelectWrapper(valueof.getSelect().getExpression()));
        }
      }
      else if (78 == etype)
      {
        ElemTextLiteral lit = (ElemTextLiteral)t;
        
        if ((!lit.getDisableOutputEscaping()) && (lit.getDOMBackPointer() == null))
        {

          String str = lit.getNodeValue();
          XString xstr = new XString(str);
          
          m_firstChild = null;
          
          return new XPath(new XRTreeFragSelectWrapper(xstr));
        }
      }
    }
    
    return null;
  }
  





  public void recompose(StylesheetRoot root)
  {
    root.recomposeVariables(this);
  }
  





  public void setParentElem(ElemTemplateElement p)
  {
    super.setParentElem(p);
    m_hasVariableDecl = true;
  }
  







  protected boolean accept(XSLTVisitor visitor)
  {
    return visitor.visitVariableOrParamDecl(this);
  }
  





  protected void callChildVisitors(XSLTVisitor visitor, boolean callAttrs)
  {
    if (null != m_selectPattern)
      m_selectPattern.getExpression().callVisitors(m_selectPattern, visitor);
    super.callChildVisitors(visitor, callAttrs);
  }
  




  public boolean isPsuedoVar()
  {
    String ns = m_qname.getNamespaceURI();
    if ((null != ns) && (ns.equals("http://xml.apache.org/xalan/psuedovar")))
    {
      if (m_qname.getLocalName().startsWith("#"))
        return true;
    }
    return false;
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
