package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.serializer.SerializationHandler;
import org.xml.sax.SAXException;
































public class ElemTextLiteral
  extends ElemTemplateElement
{
  static final long serialVersionUID = -7872620006767660088L;
  private boolean m_preserveSpace;
  private char[] m_ch;
  private String m_str;
  
  public ElemTextLiteral() {}
  
  public void setPreserveSpace(boolean v)
  {
    m_preserveSpace = v;
  }
  






  public boolean getPreserveSpace()
  {
    return m_preserveSpace;
  }
  

















  public void setChars(char[] v)
  {
    m_ch = v;
  }
  





  public char[] getChars()
  {
    return m_ch;
  }
  






  public synchronized String getNodeValue()
  {
    if (null == m_str)
    {
      m_str = new String(m_ch);
    }
    
    return m_str;
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
    return 78;
  }
  





  public String getNodeName()
  {
    return "#Text";
  }
  








  public void execute(TransformerImpl transformer)
    throws TransformerException
  {
    try
    {
      SerializationHandler rth = transformer.getResultTreeHandler();
      if (transformer.getDebug())
      {
        rth.flushPending();
        transformer.getTraceManager().fireTraceEvent(this);
      }
      
      if (m_disableOutputEscaping)
      {
        rth.processingInstruction("javax.xml.transform.disable-output-escaping", "");
      }
      
      rth.characters(m_ch, 0, m_ch.length);
      
      if (m_disableOutputEscaping)
      {
        rth.processingInstruction("javax.xml.transform.enable-output-escaping", "");
      }
      return;
    }
    catch (SAXException se) {
      throw new TransformerException(se);
    }
    finally
    {
      if (transformer.getDebug())
      {
        try
        {
          transformer.getResultTreeHandler().flushPending();
          transformer.getTraceManager().fireTraceEndEvent(this);
        }
        catch (SAXException se)
        {
          throw new TransformerException(se);
        }
      }
    }
  }
}
