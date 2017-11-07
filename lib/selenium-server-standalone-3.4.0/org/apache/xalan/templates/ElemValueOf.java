package org.apache.xalan.templates;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xpath.Expression;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.xml.sax.SAXException;






































public class ElemValueOf
  extends ElemTemplateElement
{
  static final long serialVersionUID = 3490728458007586786L;
  private XPath m_selectExpression = null;
  




  private boolean m_isDot = false;
  




  public ElemValueOf() {}
  



  public void setSelect(XPath v)
  {
    if (null != v)
    {
      String s = v.getPatternString();
      
      m_isDot = ((null != s) && (s.equals(".")));
    }
    
    m_selectExpression = v;
  }
  








  public XPath getSelect()
  {
    return m_selectExpression;
  }
  




  private boolean m_disableOutputEscaping = false;
  




















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
    return 30;
  }
  










  public void compose(StylesheetRoot sroot)
    throws TransformerException
  {
    super.compose(sroot);
    
    Vector vnames = sroot.getComposeState().getVariableNames();
    
    if (null != m_selectExpression) {
      m_selectExpression.fixupVariables(vnames, sroot.getComposeState().getGlobalsSize());
    }
  }
  





  public String getNodeName()
  {
    return "value-of";
  }
  















  public void execute(TransformerImpl transformer)
    throws TransformerException
  {
    XPathContext xctxt = transformer.getXPathContext();
    SerializationHandler rth = transformer.getResultTreeHandler();
    
    if (transformer.getDebug()) {
      transformer.getTraceManager().fireTraceEvent(this);
    }
    


























    try
    {
      xctxt.pushNamespaceContext(this);
      
      int current = xctxt.getCurrentNode();
      
      xctxt.pushCurrentNodeAndExpression(current, current);
      
      if (m_disableOutputEscaping) {
        rth.processingInstruction("javax.xml.transform.disable-output-escaping", "");
      }
      
      try
      {
        Expression expr = m_selectExpression.getExpression();
        
        if (transformer.getDebug())
        {
          XObject obj = expr.execute(xctxt);
          
          transformer.getTraceManager().fireSelectedEvent(current, this, "select", m_selectExpression, obj);
          
          obj.dispatchCharactersEvents(rth);
        }
        else
        {
          expr.executeCharsToContentHandler(xctxt, rth);
        }
      }
      finally
      {
        if (m_disableOutputEscaping) {
          rth.processingInstruction("javax.xml.transform.enable-output-escaping", "");
        }
        
        xctxt.popNamespaceContext();
        xctxt.popCurrentNodeAndExpression();
      }
      
    }
    catch (SAXException se)
    {
      throw new TransformerException(se);
    }
    catch (RuntimeException re) {
      TransformerException te = new TransformerException(re);
      te.setLocator(this);
      throw te;
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
    error("ER_CANNOT_ADD", new Object[] { newChild.getNodeName(), getNodeName() });
    



    return null;
  }
  




  protected void callChildVisitors(XSLTVisitor visitor, boolean callAttrs)
  {
    if (callAttrs)
      m_selectExpression.getExpression().callVisitors(m_selectExpression, visitor);
    super.callChildVisitors(visitor, callAttrs);
  }
}
