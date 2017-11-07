package org.apache.xalan.transformer;

import java.text.Collator;
import java.util.Locale;
import javax.xml.transform.TransformerException;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.XPath;



























































class NodeSortKey
{
  XPath m_selectPat;
  boolean m_treatAsNumbers;
  boolean m_descending;
  boolean m_caseOrderUpper;
  Collator m_col;
  Locale m_locale;
  PrefixResolver m_namespaceContext;
  TransformerImpl m_processor;
  
  NodeSortKey(TransformerImpl transformer, XPath selectPat, boolean treatAsNumbers, boolean descending, String langValue, boolean caseOrderUpper, PrefixResolver namespaceContext)
    throws TransformerException
  {
    m_processor = transformer;
    m_namespaceContext = namespaceContext;
    m_selectPat = selectPat;
    m_treatAsNumbers = treatAsNumbers;
    m_descending = descending;
    m_caseOrderUpper = caseOrderUpper;
    
    if ((null != langValue) && (!m_treatAsNumbers))
    {










      m_locale = new Locale(langValue.toLowerCase(), Locale.getDefault().getCountry());
      





      if (null == m_locale)
      {


        m_locale = Locale.getDefault();
      }
    }
    else
    {
      m_locale = Locale.getDefault();
    }
    
    m_col = Collator.getInstance(m_locale);
    
    if (null == m_col)
    {
      m_processor.getMsgMgr().warn(null, "WG_CANNOT_FIND_COLLATOR", new Object[] { langValue });
      

      m_col = Collator.getInstance();
    }
  }
}
