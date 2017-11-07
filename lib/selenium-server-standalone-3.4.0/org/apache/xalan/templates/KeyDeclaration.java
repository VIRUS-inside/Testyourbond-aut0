package org.apache.xalan.templates;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xml.utils.QName;
import org.apache.xpath.XPath;
































public class KeyDeclaration
  extends ElemTemplateElement
{
  static final long serialVersionUID = 7724030248631137918L;
  private QName m_name;
  
  public KeyDeclaration(Stylesheet parentNode, int docOrderNumber)
  {
    m_parentNode = parentNode;
    setUid(docOrderNumber);
  }
  














  public void setName(QName name)
  {
    m_name = name;
  }
  








  public QName getName()
  {
    return m_name;
  }
  





  public String getNodeName()
  {
    return "key";
  }
  





  private XPath m_matchPattern = null;
  



  private XPath m_use;
  




  public void setMatch(XPath v)
  {
    m_matchPattern = v;
  }
  









  public XPath getMatch()
  {
    return m_matchPattern;
  }
  














  public void setUse(XPath v)
  {
    m_use = v;
  }
  








  public XPath getUse()
  {
    return m_use;
  }
  






  public int getXSLToken()
  {
    return 31;
  }
  






  public void compose(StylesheetRoot sroot)
    throws TransformerException
  {
    super.compose(sroot);
    Vector vnames = sroot.getComposeState().getVariableNames();
    if (null != m_matchPattern)
      m_matchPattern.fixupVariables(vnames, sroot.getComposeState().getGlobalsSize());
    if (null != m_use) {
      m_use.fixupVariables(vnames, sroot.getComposeState().getGlobalsSize());
    }
  }
  




  public void recompose(StylesheetRoot root)
  {
    root.recomposeKeys(this);
  }
}
