package org.apache.xalan.templates;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.MsgMgr;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.XML11Char;
import org.apache.xpath.XPathContext;
import org.xml.sax.SAXException;





































public class ElemPI
  extends ElemTemplateElement
{
  static final long serialVersionUID = 5621976448020889825L;
  private AVT m_name_atv = null;
  


  public ElemPI() {}
  


  public void setName(AVT v)
  {
    m_name_atv = v;
  }
  






  public AVT getName()
  {
    return m_name_atv;
  }
  





  public void compose(StylesheetRoot sroot)
    throws TransformerException
  {
    super.compose(sroot);
    Vector vnames = sroot.getComposeState().getVariableNames();
    if (null != m_name_atv) {
      m_name_atv.fixupVariables(vnames, sroot.getComposeState().getGlobalsSize());
    }
  }
  







  public int getXSLToken()
  {
    return 58;
  }
  





  public String getNodeName()
  {
    return "processing-instruction";
  }
  












  public void execute(TransformerImpl transformer)
    throws TransformerException
  {
    if (transformer.getDebug()) {
      transformer.getTraceManager().fireTraceEvent(this);
    }
    XPathContext xctxt = transformer.getXPathContext();
    int sourceNode = xctxt.getCurrentNode();
    
    String piName = m_name_atv == null ? null : m_name_atv.evaluate(xctxt, sourceNode, this);
    

    if (piName == null) { return;
    }
    if (piName.equalsIgnoreCase("xml"))
    {
      transformer.getMsgMgr().warn(this, "WG_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML", new Object[] { "name", piName });
      

      return;
    }
    


    if ((!m_name_atv.isSimple()) && (!XML11Char.isXML11ValidNCName(piName)))
    {
      transformer.getMsgMgr().warn(this, "WG_PROCESSINGINSTRUCTION_NOTVALID_NCNAME", new Object[] { "name", piName });
      

      return;
    }
    








    String data = transformer.transformToString(this);
    
    try
    {
      transformer.getResultTreeHandler().processingInstruction(piName, data);
    }
    catch (SAXException se)
    {
      throw new TransformerException(se);
    }
    
    if (transformer.getDebug()) {
      transformer.getTraceManager().fireTraceEndEvent(this);
    }
  }
  









  public ElemTemplateElement appendChild(ElemTemplateElement newChild)
  {
    int type = newChild.getXSLToken();
    
    switch (type)
    {
    case 9: 
    case 17: 
    case 28: 
    case 30: 
    case 35: 
    case 36: 
    case 37: 
    case 42: 
    case 50: 
    case 72: 
    case 73: 
    case 74: 
    case 75: 
    case 78: 
      break;
    







    default: 
      error("ER_CANNOT_ADD", new Object[] { newChild.getNodeName(), getNodeName() });
    }
    
    



    return super.appendChild(newChild);
  }
}
