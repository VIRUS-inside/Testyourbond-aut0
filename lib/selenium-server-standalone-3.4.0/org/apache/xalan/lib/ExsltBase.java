package org.apache.xalan.lib;

import org.apache.xml.dtm.ref.DTMNodeProxy;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;






























public abstract class ExsltBase
{
  public ExsltBase() {}
  
  protected static String toString(Node n)
  {
    if ((n instanceof DTMNodeProxy)) {
      return ((DTMNodeProxy)n).getStringValue();
    }
    
    String value = n.getNodeValue();
    if (value == null)
    {
      NodeList nodelist = n.getChildNodes();
      StringBuffer buf = new StringBuffer();
      for (int i = 0; i < nodelist.getLength(); i++)
      {
        Node childNode = nodelist.item(i);
        buf.append(toString(childNode));
      }
      return buf.toString();
    }
    
    return value;
  }
  








  protected static double toNumber(Node n)
  {
    double d = 0.0D;
    String str = toString(n);
    try
    {
      d = Double.valueOf(str).doubleValue();
    }
    catch (NumberFormatException e)
    {
      d = NaN.0D;
    }
    return d;
  }
}
