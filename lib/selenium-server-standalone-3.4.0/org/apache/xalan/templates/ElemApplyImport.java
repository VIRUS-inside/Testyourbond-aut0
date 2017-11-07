package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.MsgMgr;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xpath.XPathContext;
































public class ElemApplyImport
  extends ElemTemplateElement
{
  static final long serialVersionUID = 3764728663373024038L;
  
  public ElemApplyImport() {}
  
  public int getXSLToken()
  {
    return 72;
  }
  





  public String getNodeName()
  {
    return "apply-imports";
  }
  









  public void execute(TransformerImpl transformer)
    throws TransformerException
  {
    if (transformer.currentTemplateRuleIsNull())
    {
      transformer.getMsgMgr().error(this, "ER_NO_APPLY_IMPORT_IN_FOR_EACH");
    }
    

    if (transformer.getDebug()) {
      transformer.getTraceManager().fireTraceEvent(this);
    }
    int sourceNode = transformer.getXPathContext().getCurrentNode();
    if (-1 != sourceNode)
    {

      ElemTemplate matchTemplate = transformer.getMatchedTemplate();
      transformer.applyTemplateToNode(this, matchTemplate, sourceNode);
    }
    else
    {
      transformer.getMsgMgr().error(this, "ER_NULL_SOURCENODE_APPLYIMPORTS");
    }
    
    if (transformer.getDebug()) {
      transformer.getTraceManager().fireTraceEndEvent(this);
    }
  }
  








  public ElemTemplateElement appendChild(ElemTemplateElement newChild)
  {
    error("ER_CANNOT_ADD", new Object[] { newChild.getNodeName(), getNodeName() });
    



    return null;
  }
}
