package org.apache.xalan.templates;

import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.MsgMgr;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.utils.QName;
import org.apache.xpath.VariableStack;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;





































public class ElemCallTemplate
  extends ElemForEach
{
  static final long serialVersionUID = 5009634612916030591L;
  public QName m_templateName = null;
  



  public ElemCallTemplate() {}
  


  public void setName(QName name)
  {
    m_templateName = name;
  }
  







  public QName getName()
  {
    return m_templateName;
  }
  




  private ElemTemplate m_template = null;
  






  public int getXSLToken()
  {
    return 17;
  }
  





  public String getNodeName()
  {
    return "call-template";
  }
  





  public void compose(StylesheetRoot sroot)
    throws TransformerException
  {
    super.compose(sroot);
    


    int length = getParamElemCount();
    for (int i = 0; i < length; i++)
    {
      ElemWithParam ewp = getParamElem(i);
      ewp.compose(sroot);
    }
    
    if ((null != m_templateName) && (null == m_template)) {
      m_template = getStylesheetRoot().getTemplateComposed(m_templateName);
      

      if (null == m_template) {
        String themsg = XSLMessages.createMessage("ER_ELEMTEMPLATEELEM_ERR", new Object[] { m_templateName });
        



        throw new TransformerException(themsg, this);
      }
      

      length = getParamElemCount();
      for (int i = 0; i < length; i++)
      {
        ElemWithParam ewp = getParamElem(i);
        m_index = -1;
        

        int etePos = 0;
        for (ElemTemplateElement ete = m_template.getFirstChildElem(); 
            null != ete; ete = ete.getNextSiblingElem())
        {
          if (ete.getXSLToken() != 41)
            break;
          ElemParam ep = (ElemParam)ete;
          if (ep.getName().equals(ewp.getName()))
          {
            m_index = etePos;
          }
          


          etePos++;
        }
      }
    }
  }
  



  public void endCompose(StylesheetRoot sroot)
    throws TransformerException
  {
    int length = getParamElemCount();
    for (int i = 0; i < length; i++)
    {
      ElemWithParam ewp = getParamElem(i);
      ewp.endCompose(sroot);
    }
    
    super.endCompose(sroot);
  }
  










  public void execute(TransformerImpl transformer)
    throws TransformerException
  {
    if (transformer.getDebug()) {
      transformer.getTraceManager().fireTraceEvent(this);
    }
    if (null != m_template)
    {
      XPathContext xctxt = transformer.getXPathContext();
      VariableStack vars = xctxt.getVarStack();
      
      int thisframe = vars.getStackFrame();
      int nextFrame = vars.link(m_template.m_frameSize);
      


      if (m_template.m_inArgsSize > 0)
      {
        vars.clearLocalSlots(0, m_template.m_inArgsSize);
        
        if (null != m_paramElems)
        {
          int currentNode = xctxt.getCurrentNode();
          vars.setStackFrame(thisframe);
          int size = m_paramElems.length;
          
          for (int i = 0; i < size; i++)
          {
            ElemWithParam ewp = m_paramElems[i];
            if (m_index >= 0)
            {
              if (transformer.getDebug())
                transformer.getTraceManager().fireTraceEvent(ewp);
              XObject obj = ewp.getValue(transformer, currentNode);
              if (transformer.getDebug()) {
                transformer.getTraceManager().fireTraceEndEvent(ewp);
              }
              


              vars.setLocalVariable(m_index, obj, nextFrame);
            }
          }
          vars.setStackFrame(nextFrame);
        }
      }
      
      SourceLocator savedLocator = xctxt.getSAXLocator();
      
      try
      {
        xctxt.setSAXLocator(m_template);
        

        transformer.pushElemTemplateElement(m_template);
        m_template.execute(transformer);
      }
      finally
      {
        transformer.popElemTemplateElement();
        xctxt.setSAXLocator(savedLocator);
        








        vars.unlink(thisframe);
      }
    }
    else
    {
      transformer.getMsgMgr().error(this, "ER_TEMPLATE_NOT_FOUND", new Object[] { m_templateName });
    }
    

    if (transformer.getDebug()) {
      transformer.getTraceManager().fireTraceEndEvent(this);
    }
  }
  


  protected ElemWithParam[] m_paramElems = null;
  




  public int getParamElemCount()
  {
    return m_paramElems == null ? 0 : m_paramElems.length;
  }
  







  public ElemWithParam getParamElem(int i)
  {
    return m_paramElems[i];
  }
  





  public void setParamElem(ElemWithParam ParamElem)
  {
    if (null == m_paramElems)
    {
      m_paramElems = new ElemWithParam[1];
      m_paramElems[0] = ParamElem;

    }
    else
    {

      int length = m_paramElems.length;
      ElemWithParam[] ewp = new ElemWithParam[length + 1];
      System.arraycopy(m_paramElems, 0, ewp, 0, length);
      m_paramElems = ewp;
      ewp[length] = ParamElem;
    }
  }
  















  public ElemTemplateElement appendChild(ElemTemplateElement newChild)
  {
    int type = newChild.getXSLToken();
    
    if (2 == type)
    {
      setParamElem((ElemWithParam)newChild);
    }
    


    return super.appendChild(newChild);
  }
  















  public void callChildVisitors(XSLTVisitor visitor, boolean callAttrs)
  {
    super.callChildVisitors(visitor, callAttrs);
  }
}
