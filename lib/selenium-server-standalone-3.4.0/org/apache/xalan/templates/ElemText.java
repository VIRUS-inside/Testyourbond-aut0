package org.apache.xalan.templates;





















public class ElemText
  extends ElemTemplateElement
{
  static final long serialVersionUID = 1383140876182316711L;
  


















  private boolean m_disableOutputEscaping = false;
  









  public ElemText() {}
  









  public void setDisableOutputEscaping(boolean v)
  {
    m_disableOutputEscaping = v;
  }
  




















  public boolean getDisableOutputEscaping()
  {
    return m_disableOutputEscaping;
  }
  







  public int getXSLToken()
  {
    return 42;
  }
  





  public String getNodeName()
  {
    return "text";
  }
  










  public ElemTemplateElement appendChild(ElemTemplateElement newChild)
  {
    int type = newChild.getXSLToken();
    
    switch (type)
    {
    case 78: 
      break;
    default: 
      error("ER_CANNOT_ADD", new Object[] { newChild.getNodeName(), getNodeName() });
    }
    
    



    return super.appendChild(newChild);
  }
}
