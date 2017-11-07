package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.MsgMgr;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;


































public class ElemChoose
  extends ElemTemplateElement
{
  static final long serialVersionUID = -3070117361903102033L;
  
  public int getXSLToken()
  {
    return 37;
  }
  





  public String getNodeName()
  {
    return "choose";
  }
  






  public ElemChoose() {}
  






  public void execute(TransformerImpl transformer)
    throws TransformerException
  {
    if (transformer.getDebug()) {
      transformer.getTraceManager().fireTraceEvent(this);
    }
    boolean found = false;
    
    for (ElemTemplateElement childElem = getFirstChildElem(); 
        childElem != null; childElem = childElem.getNextSiblingElem())
    {
      int type = childElem.getXSLToken();
      
      if (38 == type)
      {
        found = true;
        
        ElemWhen when = (ElemWhen)childElem;
        

        XPathContext xctxt = transformer.getXPathContext();
        int sourceNode = xctxt.getCurrentNode();
        





        if (transformer.getDebug())
        {
          XObject test = when.getTest().execute(xctxt, sourceNode, when);
          
          if (transformer.getDebug()) {
            transformer.getTraceManager().fireSelectedEvent(sourceNode, when, "test", when.getTest(), test);
          }
          
          if (test.bool())
          {
            transformer.getTraceManager().fireTraceEvent(when);
            
            transformer.executeChildTemplates(when, true);
            
            transformer.getTraceManager().fireTraceEndEvent(when);
            
            return;
          }
          
        }
        else if (when.getTest().bool(xctxt, sourceNode, when))
        {
          transformer.executeChildTemplates(when, true);
          
          return;
        }
      }
      else if (39 == type)
      {
        found = true;
        
        if (transformer.getDebug()) {
          transformer.getTraceManager().fireTraceEvent(childElem);
        }
        
        transformer.executeChildTemplates(childElem, true);
        
        if (transformer.getDebug())
          transformer.getTraceManager().fireTraceEndEvent(childElem);
        return;
      }
    }
    
    if (!found) {
      transformer.getMsgMgr().error(this, "ER_CHOOSE_REQUIRES_WHEN");
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
    case 38: 
    case 39: 
      break;
    

    default: 
      error("ER_CANNOT_ADD", new Object[] { newChild.getNodeName(), getNodeName() });
    }
    
    



    return super.appendChild(newChild);
  }
  




  public boolean canAcceptVariables()
  {
    return false;
  }
}
