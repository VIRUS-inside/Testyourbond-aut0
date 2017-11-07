package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;
import org.apache.xalan.transformer.MsgMgr;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.utils.QName;
import org.apache.xpath.VariableStack;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;































public class XUnresolvedVariable
  extends XObject
{
  static final long serialVersionUID = -256779804767950188L;
  private transient int m_context;
  private transient TransformerImpl m_transformer;
  private transient int m_varStackPos = -1;
  


  private transient int m_varStackContext;
  


  private boolean m_isGlobal;
  


  private transient boolean m_doneEval = true;
  



















  public XUnresolvedVariable(ElemVariable obj, int sourceNode, TransformerImpl transformer, int varStackPos, int varStackContext, boolean isGlobal)
  {
    super(obj);
    m_context = sourceNode;
    m_transformer = transformer;
    


    m_varStackPos = varStackPos;
    

    m_varStackContext = varStackContext;
    
    m_isGlobal = isGlobal;
  }
  








  public XObject execute(XPathContext xctxt)
    throws TransformerException
  {
    if (!m_doneEval)
    {
      m_transformer.getMsgMgr().error(xctxt.getSAXLocator(), "ER_REFERENCING_ITSELF", new Object[] { ((ElemVariable)object()).getName().getLocalName() });
    }
    

    VariableStack vars = xctxt.getVarStack();
    

    int currentFrame = vars.getStackFrame();
    


    ElemVariable velem = (ElemVariable)m_obj;
    try
    {
      m_doneEval = false;
      if (-1 != m_frameSize)
        vars.link(m_frameSize);
      XObject var = velem.getValue(m_transformer, m_context);
      m_doneEval = true;
      return var;


    }
    finally
    {

      if (-1 != m_frameSize) {
        vars.unlink(currentFrame);
      }
    }
  }
  







  public void setVarStackPos(int top)
  {
    m_varStackPos = top;
  }
  







  public void setVarStackContext(int bottom)
  {
    m_varStackContext = bottom;
  }
  





  public int getType()
  {
    return 600;
  }
  






  public String getTypeString()
  {
    return "XUnresolvedVariable (" + object().getClass().getName() + ")";
  }
}
