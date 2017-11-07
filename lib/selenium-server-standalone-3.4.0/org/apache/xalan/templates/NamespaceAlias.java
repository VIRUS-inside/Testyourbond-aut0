package org.apache.xalan.templates;






public class NamespaceAlias
  extends ElemTemplateElement
{
  static final long serialVersionUID = 456173966637810718L;
  




  private String m_StylesheetPrefix;
  




  private String m_StylesheetNamespace;
  




  private String m_ResultPrefix;
  



  private String m_ResultNamespace;
  




  public NamespaceAlias(int docOrderNumber)
  {
    m_docOrderNumber = docOrderNumber;
  }
  











  public void setStylesheetPrefix(String v)
  {
    m_StylesheetPrefix = v;
  }
  





  public String getStylesheetPrefix()
  {
    return m_StylesheetPrefix;
  }
  











  public void setStylesheetNamespace(String v)
  {
    m_StylesheetNamespace = v;
  }
  





  public String getStylesheetNamespace()
  {
    return m_StylesheetNamespace;
  }
  











  public void setResultPrefix(String v)
  {
    m_ResultPrefix = v;
  }
  





  public String getResultPrefix()
  {
    return m_ResultPrefix;
  }
  











  public void setResultNamespace(String v)
  {
    m_ResultNamespace = v;
  }
  





  public String getResultNamespace()
  {
    return m_ResultNamespace;
  }
  





  public void recompose(StylesheetRoot root)
  {
    root.recomposeNamespaceAliases(this);
  }
}
