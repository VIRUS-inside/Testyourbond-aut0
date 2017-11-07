package org.apache.xalan.lib;

import org.apache.xalan.extensions.ExpressionContext;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.ref.DTMNodeIterator;
import org.apache.xpath.NodeSet;
import org.apache.xpath.axes.RTFIterator;

















































public class ExsltCommon
{
  public ExsltCommon() {}
  
  public static String objectType(Object obj)
  {
    if ((obj instanceof String))
      return "string";
    if ((obj instanceof Boolean))
      return "boolean";
    if ((obj instanceof Number))
      return "number";
    if ((obj instanceof DTMNodeIterator))
    {
      DTMIterator dtmI = ((DTMNodeIterator)obj).getDTMIterator();
      if ((dtmI instanceof RTFIterator)) {
        return "RTF";
      }
      return "node-set";
    }
    
    return "unknown";
  }
  




















  public static NodeSet nodeSet(ExpressionContext myProcessor, Object rtf)
  {
    return Extensions.nodeset(myProcessor, rtf);
  }
}
