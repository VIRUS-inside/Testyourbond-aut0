package org.apache.xalan.templates;

import java.io.Serializable;



























public class XMLNSDecl
  implements Serializable
{
  static final long serialVersionUID = 6710237366877605097L;
  private String m_prefix;
  private String m_uri;
  private boolean m_isExcluded;
  
  public XMLNSDecl(String prefix, String uri, boolean isExcluded)
  {
    m_prefix = prefix;
    m_uri = uri;
    m_isExcluded = isExcluded;
  }
  









  public String getPrefix()
  {
    return m_prefix;
  }
  








  public String getURI()
  {
    return m_uri;
  }
  










  public boolean getIsExcluded()
  {
    return m_isExcluded;
  }
}
