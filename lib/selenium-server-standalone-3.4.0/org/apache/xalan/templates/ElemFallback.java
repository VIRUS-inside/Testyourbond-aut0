package org.apache.xalan.templates;

import java.io.PrintStream;
import javax.xml.transform.TransformerException;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.TransformerImpl;
































public class ElemFallback
  extends ElemTemplateElement
{
  static final long serialVersionUID = 1782962139867340703L;
  
  public ElemFallback() {}
  
  public int getXSLToken()
  {
    return 57;
  }
  





  public String getNodeName()
  {
    return "fallback";
  }
  













  public void execute(TransformerImpl transformer)
    throws TransformerException
  {}
  













  public void executeFallback(TransformerImpl transformer)
    throws TransformerException
  {
    int parentElemType = m_parentNode.getXSLToken();
    if ((79 == parentElemType) || (-1 == parentElemType))
    {


      if (transformer.getDebug()) {
        transformer.getTraceManager().fireTraceEvent(this);
      }
      transformer.executeChildTemplates(this, true);
      
      if (transformer.getDebug()) {
        transformer.getTraceManager().fireTraceEndEvent(this);
      }
      
    }
    else
    {
      System.out.println("Error!  parent of xsl:fallback must be an extension or unknown element!");
    }
  }
}
