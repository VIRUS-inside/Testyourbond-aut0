package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.utils.QName;






































public class ElemAttributeSet
  extends ElemUse
{
  static final long serialVersionUID = -426740318278164496L;
  public QName m_qname = null;
  


  public ElemAttributeSet() {}
  


  public void setName(QName name)
  {
    m_qname = name;
  }
  






  public QName getName()
  {
    return m_qname;
  }
  






  public int getXSLToken()
  {
    return 40;
  }
  





  public String getNodeName()
  {
    return "attribute-set";
  }
  









  public void execute(TransformerImpl transformer)
    throws TransformerException
  {
    if (transformer.getDebug()) {
      transformer.getTraceManager().fireTraceEvent(this);
    }
    if (transformer.isRecursiveAttrSet(this))
    {
      throw new TransformerException(XSLMessages.createMessage("ER_XSLATTRSET_USED_ITSELF", new Object[] { m_qname.getLocalPart() }));
    }
    



    transformer.pushElemAttributeSet(this);
    super.execute(transformer);
    
    ElemAttribute attr = (ElemAttribute)getFirstChildElem();
    
    while (null != attr)
    {
      attr.execute(transformer);
      
      attr = (ElemAttribute)attr.getNextSiblingElem();
    }
    
    transformer.popElemAttributeSet();
    
    if (transformer.getDebug()) {
      transformer.getTraceManager().fireTraceEndEvent(this);
    }
  }
  















  public ElemTemplateElement appendChildElem(ElemTemplateElement newChild)
  {
    int type = newChild.getXSLToken();
    
    switch (type)
    {
    case 48: 
      break;
    default: 
      error("ER_CANNOT_ADD", new Object[] { newChild.getNodeName(), getNodeName() });
    }
    
    



    return super.appendChild(newChild);
  }
  





  public void recompose(StylesheetRoot root)
  {
    root.recomposeAttributeSets(this);
  }
}
