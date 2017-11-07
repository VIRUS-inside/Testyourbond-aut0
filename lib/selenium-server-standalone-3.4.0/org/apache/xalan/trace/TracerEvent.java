package org.apache.xalan.trace;

import java.util.EventListener;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.utils.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


















































public class TracerEvent
  implements EventListener
{
  public final ElemTemplateElement m_styleNode;
  public final TransformerImpl m_processor;
  public final Node m_sourceNode;
  public final QName m_mode;
  
  public TracerEvent(TransformerImpl processor, Node sourceNode, QName mode, ElemTemplateElement styleNode)
  {
    m_processor = processor;
    m_sourceNode = sourceNode;
    m_mode = mode;
    m_styleNode = styleNode;
  }
  











  public static String printNode(Node n)
  {
    String r = n.hashCode() + " ";
    
    if ((n instanceof Element))
    {
      r = r + "<" + n.getNodeName();
      
      Node c = n.getFirstChild();
      
      while (null != c)
      {
        if ((c instanceof Attr))
        {
          r = r + printNode(c) + " ";
        }
        
        c = c.getNextSibling();
      }
      
      r = r + ">";


    }
    else if ((n instanceof Attr))
    {
      r = r + n.getNodeName() + "=" + n.getNodeValue();
    }
    else
    {
      r = r + n.getNodeName();
    }
    

    return r;
  }
  












  public static String printNodeList(NodeList l)
  {
    String r = l.hashCode() + "[";
    int len = l.getLength() - 1;
    int i = 0;
    
    while (i < len)
    {
      Node n = l.item(i);
      
      if (null != n)
      {
        r = r + printNode(n) + ", ";
      }
      
      i++;
    }
    
    if (i == len)
    {
      Node n = l.item(len);
      
      if (null != n)
      {
        r = r + printNode(n);
      }
    }
    
    return r + "]";
  }
}
