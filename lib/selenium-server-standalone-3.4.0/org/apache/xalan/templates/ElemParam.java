package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xpath.VariableStack;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;









































public class ElemParam
  extends ElemVariable
{
  static final long serialVersionUID = -1131781475589006431L;
  int m_qnameID;
  
  public ElemParam() {}
  
  public int getXSLToken()
  {
    return 41;
  }
  





  public String getNodeName()
  {
    return "param";
  }
  






  public ElemParam(ElemParam param)
    throws TransformerException
  {
    super(param);
  }
  





  public void compose(StylesheetRoot sroot)
    throws TransformerException
  {
    super.compose(sroot);
    m_qnameID = sroot.getComposeState().getQNameID(m_qname);
    int parentToken = m_parentNode.getXSLToken();
    if ((parentToken == 19) || (parentToken == 88))
    {
      m_parentNode).m_inArgsSize += 1;
    }
  }
  






  public void execute(TransformerImpl transformer)
    throws TransformerException
  {
    if (transformer.getDebug()) {
      transformer.getTraceManager().fireTraceEvent(this);
    }
    VariableStack vars = transformer.getXPathContext().getVarStack();
    
    if (!vars.isLocalSet(m_index))
    {

      int sourceNode = transformer.getXPathContext().getCurrentNode();
      XObject var = getValue(transformer, sourceNode);
      

      transformer.getXPathContext().getVarStack().setLocalVariable(m_index, var);
    }
    
    if (transformer.getDebug()) {
      transformer.getTraceManager().fireTraceEndEvent(this);
    }
  }
}
