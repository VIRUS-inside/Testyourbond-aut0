package org.apache.xpath.objects;

import javax.xml.transform.TransformerException;
import org.apache.xml.utils.WrappedRuntimeException;



























public class XBoolean
  extends XObject
{
  static final long serialVersionUID = -2964933058866100881L;
  public static final XBoolean S_TRUE = new XBooleanStatic(true);
  




  public static final XBoolean S_FALSE = new XBooleanStatic(false);
  





  private final boolean m_val;
  





  public XBoolean(boolean b)
  {
    m_val = b;
  }
  








  public XBoolean(Boolean b)
  {
    m_val = b.booleanValue();
    setObject(b);
  }
  






  public int getType()
  {
    return 1;
  }
  






  public String getTypeString()
  {
    return "#BOOLEAN";
  }
  





  public double num()
  {
    return m_val ? 1.0D : 0.0D;
  }
  





  public boolean bool()
  {
    return m_val;
  }
  





  public String str()
  {
    return m_val ? "true" : "false";
  }
  






  public Object object()
  {
    if (null == m_obj)
      setObject(m_val ? Boolean.TRUE : Boolean.FALSE);
    return m_obj;
  }
  













  public boolean equals(XObject obj2)
  {
    if (obj2.getType() == 4) {
      return obj2.equals(this);
    }
    try
    {
      return m_val == obj2.bool();
    }
    catch (TransformerException te)
    {
      throw new WrappedRuntimeException(te);
    }
  }
}
