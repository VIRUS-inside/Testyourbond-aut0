package org.apache.xpath.operations;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.ElemVariable;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.QName;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionNode;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.VariableStack;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.axes.PathComponent;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XObject;



















public class Variable
  extends Expression
  implements PathComponent
{
  static final long serialVersionUID = -4334975375609297049L;
  private boolean m_fixUpWasCalled = false;
  



  protected QName m_qname;
  


  protected int m_index;
  



  public Variable() {}
  



  public void setIndex(int index)
  {
    m_index = index;
  }
  





  public int getIndex()
  {
    return m_index;
  }
  





  public void setIsGlobal(boolean isGlobal)
  {
    m_isGlobal = isGlobal;
  }
  





  public boolean getGlobal()
  {
    return m_isGlobal;
  }
  




  protected boolean m_isGlobal = false;
  




  static final String PSUEDOVARNAMESPACE = "http://xml.apache.org/xalan/psuedovar";
  




  public void fixupVariables(Vector vars, int globalsSize)
  {
    m_fixUpWasCalled = true;
    int sz = vars.size();
    
    for (int i = vars.size() - 1; i >= 0; i--)
    {
      QName qn = (QName)vars.elementAt(i);
      
      if (qn.equals(m_qname))
      {

        if (i < globalsSize)
        {
          m_isGlobal = true;
          m_index = i;
        }
        else
        {
          m_index = (i - globalsSize);
        }
        
        return;
      }
    }
    
    String msg = XSLMessages.createXPATHMessage("ER_COULD_NOT_FIND_VAR", new Object[] { m_qname.toString() });
    

    TransformerException te = new TransformerException(msg, this);
    
    throw new WrappedRuntimeException(te);
  }
  







  public void setQName(QName qname)
  {
    m_qname = qname;
  }
  





  public QName getQName()
  {
    return m_qname;
  }
  












  public XObject execute(XPathContext xctxt)
    throws TransformerException
  {
    return execute(xctxt, false);
  }
  












  public XObject execute(XPathContext xctxt, boolean destructiveOK)
    throws TransformerException
  {
    PrefixResolver xprefixResolver = xctxt.getNamespaceContext();
    
    XObject result;
    
    XObject result;
    if (m_fixUpWasCalled) {
      XObject result;
      if (m_isGlobal) {
        result = xctxt.getVarStack().getGlobalVariable(xctxt, m_index, destructiveOK);
      } else {
        result = xctxt.getVarStack().getLocalVariable(xctxt, m_index, destructiveOK);
      }
    } else {
      result = xctxt.getVarStack().getVariableOrParam(xctxt, m_qname);
    }
    
    if (null == result)
    {

      warn(xctxt, "WG_ILLEGAL_VARIABLE_REFERENCE", new Object[] { m_qname.getLocalPart() });
      




      result = new XNodeSet(xctxt.getDTMManager());
    }
    
    return result;
  }
  






























  public ElemVariable getElemVariable()
  {
    ElemVariable vvar = null;
    ExpressionNode owner = getExpressionOwner();
    
    if ((null != owner) && ((owner instanceof ElemTemplateElement)))
    {

      ElemTemplateElement prev = (ElemTemplateElement)owner;
      

      if (!(prev instanceof Stylesheet))
      {
        while ((prev != null) && (!(prev.getParentNode() instanceof Stylesheet)))
        {
          ElemTemplateElement savedprev = prev;
          
          while (null != (prev = prev.getPreviousSiblingElem()))
          {
            if ((prev instanceof ElemVariable))
            {
              vvar = (ElemVariable)prev;
              
              if (vvar.getName().equals(m_qname))
              {
                return vvar;
              }
              vvar = null;
            }
          }
          prev = savedprev.getParentElem();
        }
      }
      if (prev != null)
        vvar = prev.getStylesheetRoot().getVariableOrParamComposed(m_qname);
    }
    return vvar;
  }
  









  public boolean isStableNumber()
  {
    return true;
  }
  




  public int getAnalysisBits()
  {
    ElemVariable vvar = getElemVariable();
    if (null != vvar)
    {
      XPath xpath = vvar.getSelect();
      if (null != xpath)
      {
        Expression expr = xpath.getExpression();
        if ((null != expr) && ((expr instanceof PathComponent)))
        {
          return ((PathComponent)expr).getAnalysisBits();
        }
      }
    }
    return 67108864;
  }
  




  public void callVisitors(ExpressionOwner owner, XPathVisitor visitor)
  {
    visitor.visitVariableRef(owner, this);
  }
  


  public boolean deepEquals(Expression expr)
  {
    if (!isSameClass(expr)) {
      return false;
    }
    if (!m_qname.equals(m_qname)) {
      return false;
    }
    

    if (getElemVariable() != ((Variable)expr).getElemVariable()) {
      return false;
    }
    return true;
  }
  






  public boolean isPsuedoVarRef()
  {
    String ns = m_qname.getNamespaceURI();
    if ((null != ns) && (ns.equals("http://xml.apache.org/xalan/psuedovar")))
    {
      if (m_qname.getLocalName().startsWith("#"))
        return true;
    }
    return false;
  }
}
