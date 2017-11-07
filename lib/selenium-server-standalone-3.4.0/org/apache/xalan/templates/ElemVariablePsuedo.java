package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xpath.VariableStack;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;



























public class ElemVariablePsuedo
  extends ElemVariable
{
  static final long serialVersionUID = 692295692732588486L;
  XUnresolvedVariableSimple m_lazyVar;
  
  public ElemVariablePsuedo() {}
  
  public void setSelect(XPath v)
  {
    super.setSelect(v);
    m_lazyVar = new XUnresolvedVariableSimple(this);
  }
  












  public void execute(TransformerImpl transformer)
    throws TransformerException
  {
    transformer.getXPathContext().getVarStack().setLocalVariable(m_index, m_lazyVar);
  }
}
