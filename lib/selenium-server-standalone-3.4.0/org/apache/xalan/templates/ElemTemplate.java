package org.apache.xalan.templates;

import java.util.Vector;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.StackGuard;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.utils.QName;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
















































public class ElemTemplate
  extends ElemTemplateElement
{
  static final long serialVersionUID = -5283056789965384058L;
  private String m_publicId;
  private String m_systemId;
  private Stylesheet m_stylesheet;
  
  public ElemTemplate() {}
  
  public String getPublicId()
  {
    return m_publicId;
  }
  










  public String getSystemId()
  {
    return m_systemId;
  }
  






  public void setLocaterInfo(SourceLocator locator)
  {
    m_publicId = locator.getPublicId();
    m_systemId = locator.getSystemId();
    
    super.setLocaterInfo(locator);
  }
  














  public StylesheetComposed getStylesheetComposed()
  {
    return m_stylesheet.getStylesheetComposed();
  }
  





  public Stylesheet getStylesheet()
  {
    return m_stylesheet;
  }
  





  public void setStylesheet(Stylesheet sheet)
  {
    m_stylesheet = sheet;
  }
  





  public StylesheetRoot getStylesheetRoot()
  {
    return m_stylesheet.getStylesheetRoot();
  }
  





  private XPath m_matchPattern = null;
  











  public void setMatch(XPath v)
  {
    m_matchPattern = v;
  }
  











  public XPath getMatch()
  {
    return m_matchPattern;
  }
  




  private QName m_name = null;
  



  private QName m_mode;
  




  public void setName(QName v)
  {
    m_name = v;
  }
  









  public QName getName()
  {
    return m_name;
  }
  
















  public void setMode(QName v)
  {
    m_mode = v;
  }
  









  public QName getMode()
  {
    return m_mode;
  }
  





  private double m_priority = Double.NEGATIVE_INFINITY;
  

  public int m_frameSize;
  

  int m_inArgsSize;
  

  private int[] m_argsQNameIDs;
  

  public void setPriority(double v)
  {
    m_priority = v;
  }
  










  public double getPriority()
  {
    return m_priority;
  }
  






  public int getXSLToken()
  {
    return 19;
  }
  





  public String getNodeName()
  {
    return "template";
  }
  

























  public void compose(StylesheetRoot sroot)
    throws TransformerException
  {
    super.compose(sroot);
    StylesheetRoot.ComposeState cstate = sroot.getComposeState();
    Vector vnames = cstate.getVariableNames();
    if (null != m_matchPattern) {
      m_matchPattern.fixupVariables(vnames, sroot.getComposeState().getGlobalsSize());
    }
    cstate.resetStackFrameSize();
    m_inArgsSize = 0;
  }
  


  public void endCompose(StylesheetRoot sroot)
    throws TransformerException
  {
    StylesheetRoot.ComposeState cstate = sroot.getComposeState();
    super.endCompose(sroot);
    m_frameSize = cstate.getFrameSize();
    
    cstate.resetStackFrameSize();
  }
  










  public void execute(TransformerImpl transformer)
    throws TransformerException
  {
    XPathContext xctxt = transformer.getXPathContext();
    
    transformer.getStackGuard().checkForInfinateLoop();
    
    xctxt.pushRTFContext();
    
    if (transformer.getDebug()) {
      transformer.getTraceManager().fireTraceEvent(this);
    }
    


    transformer.executeChildTemplates(this, true);
    









    if (transformer.getDebug()) {
      transformer.getTraceManager().fireTraceEndEvent(this);
    }
    xctxt.popRTFContext();
  }
  





  public void recompose(StylesheetRoot root)
  {
    root.recomposeTemplates(this);
  }
}
