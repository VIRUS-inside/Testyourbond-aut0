package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.serializer.SerializationHandler;
import org.xml.sax.SAXException;
































public class ElemComment
  extends ElemTemplateElement
{
  static final long serialVersionUID = -8813199122875770142L;
  
  public ElemComment() {}
  
  public int getXSLToken()
  {
    return 59;
  }
  





  public String getNodeName()
  {
    return "comment";
  }
  









  public void execute(TransformerImpl transformer)
    throws TransformerException
  {
    if (transformer.getDebug()) {
      transformer.getTraceManager().fireTraceEvent(this);
    }
    






    try
    {
      String data = transformer.transformToString(this);
      
      transformer.getResultTreeHandler().comment(data);
    }
    catch (SAXException se)
    {
      throw new TransformerException(se);
    }
    finally
    {
      if (transformer.getDebug()) {
        transformer.getTraceManager().fireTraceEndEvent(this);
      }
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
