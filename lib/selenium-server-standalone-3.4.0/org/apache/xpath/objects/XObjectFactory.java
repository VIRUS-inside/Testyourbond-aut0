package org.apache.xpath.objects;

import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xpath.XPathContext;
import org.apache.xpath.axes.OneStepIterator;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeIterator;




























public class XObjectFactory
{
  public XObjectFactory() {}
  
  public static XObject create(Object val)
  {
    XObject result;
    XObject result;
    if ((val instanceof XObject))
    {
      result = (XObject)val;
    } else { XObject result;
      if ((val instanceof String))
      {
        result = new XString((String)val);
      } else { XObject result;
        if ((val instanceof Boolean))
        {
          result = new XBoolean((Boolean)val);
        } else { XObject result;
          if ((val instanceof Double))
          {
            result = new XNumber((Double)val);
          }
          else
          {
            result = new XObject(val); }
        }
      } }
    return result;
  }
  




  public static XObject create(Object val, XPathContext xctxt)
  {
    XObject result;
    



    XObject result;
    


    if ((val instanceof XObject))
    {
      result = (XObject)val;
    } else { XObject result;
      if ((val instanceof String))
      {
        result = new XString((String)val);
      } else { XObject result;
        if ((val instanceof Boolean))
        {
          result = new XBoolean((Boolean)val);
        } else { XObject result;
          if ((val instanceof Number))
          {
            result = new XNumber((Number)val);
          }
          else if ((val instanceof DTM))
          {
            DTM dtm = (DTM)val;
            try
            {
              int dtmRoot = dtm.getDocument();
              DTMAxisIterator iter = dtm.getAxisIterator(13);
              iter.setStartNode(dtmRoot);
              DTMIterator iterator = new OneStepIterator(iter, 13);
              iterator.setRoot(dtmRoot, xctxt);
              result = new XNodeSet(iterator);
            }
            catch (Exception ex) {
              XObject result;
              throw new WrappedRuntimeException(ex);
            }
          }
          else if ((val instanceof DTMAxisIterator))
          {
            DTMAxisIterator iter = (DTMAxisIterator)val;
            try
            {
              DTMIterator iterator = new OneStepIterator(iter, 13);
              iterator.setRoot(iter.getStartNode(), xctxt);
              result = new XNodeSet(iterator);
            }
            catch (Exception ex) {
              XObject result;
              throw new WrappedRuntimeException(ex);
            }
          } else { XObject result;
            if ((val instanceof DTMIterator))
            {
              result = new XNodeSet((DTMIterator)val);
            }
            else {
              XObject result;
              if ((val instanceof Node))
              {
                result = new XNodeSetForDOM((Node)val, xctxt);
              }
              else {
                XObject result;
                if ((val instanceof NodeList))
                {
                  result = new XNodeSetForDOM((NodeList)val, xctxt);
                } else { XObject result;
                  if ((val instanceof NodeIterator))
                  {
                    result = new XNodeSetForDOM((NodeIterator)val, xctxt);
                  }
                  else
                  {
                    result = new XObject(val); }
                }
              } } } } } }
    return result;
  }
}
