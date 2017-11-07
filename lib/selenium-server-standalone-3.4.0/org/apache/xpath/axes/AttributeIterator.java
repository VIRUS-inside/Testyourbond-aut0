package org.apache.xpath.axes;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xpath.compiler.Compiler;


































public class AttributeIterator
  extends ChildTestIterator
{
  static final long serialVersionUID = -8417986700712229686L;
  
  AttributeIterator(Compiler compiler, int opPos, int analysis)
    throws TransformerException
  {
    super(compiler, opPos, analysis);
  }
  



  protected int getNextNode()
  {
    m_lastFetched = (-1 == m_lastFetched ? m_cdtm.getFirstAttribute(m_context) : m_cdtm.getNextAttribute(m_lastFetched));
    

    return m_lastFetched;
  }
  






  public int getAxis()
  {
    return 2;
  }
}
